package com.stubhub.domain.account.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.stubhub.domain.account.biz.intf.AccountSolrCloudBiz;
import com.stubhub.newplatform.property.MasterStubHubProperties;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.stubhub.domain.account.adapter.AccountResponseAdapter;
import com.stubhub.domain.account.biz.impl.EventUtil;
import com.stubhub.domain.account.biz.intf.CreditCardChargeBO;
import com.stubhub.domain.account.biz.intf.OrderSolrBO;
import com.stubhub.domain.account.common.Error;
import com.stubhub.domain.account.common.SalesSearchCriteria;
import com.stubhub.domain.account.common.enums.ErrorCode;
import com.stubhub.domain.account.common.enums.ErrorType;
import com.stubhub.domain.account.common.enums.UserType;
import com.stubhub.domain.account.datamodel.entity.SellerCcTrans;
import com.stubhub.domain.account.exception.UnauthorizedException;
import com.stubhub.domain.account.helper.PaymentHelper;
import com.stubhub.domain.account.intf.CSOrderDetailsResponse;
import com.stubhub.domain.account.intf.CreditCardCharge;
import com.stubhub.domain.account.intf.CreditCardChargesResponse;
import com.stubhub.domain.account.intf.CreditCardChargesService;
import com.stubhub.domain.account.intf.OrdersResponse;
import com.stubhub.domain.user.payments.intf.CreditCardDetails;
import com.stubhub.platform.utilities.webservice.security.ExtendedSecurityContext;

@Component("creditCardChargesService")
public class CreditCardChargesServiceImpl implements CreditCardChargesService {
	
	private final static Log log = LogFactory.getLog(CreditCardChargesServiceImpl.class);
	
	@Autowired
	private CreditCardChargeBO creditCardChargeBO;
	
	@Autowired
	private PaymentHelper paymentHelper;

	@Autowired
	private OrderSolrBO orderSolrBO;

	@Autowired
	private EventUtil eventUtil;

	@Autowired
	private AccountSolrCloudBiz accountSolrCloudBiz;

	@Override
	public CreditCardChargesResponse getCreditCardCharges(ExtendedSecurityContext securityContext, String sellerGUID, String sort, String fromDate, String toDate, Integer start, Integer rows, String transactionType, String currencyCode) {
		CreditCardChargesResponse response = new CreditCardChargesResponse();
		if(StringUtils.trimToNull(sellerGUID) == null || securityContext == null || !sellerGUID.equals(securityContext.getUserGuid())) {
			response.setErrors(new ArrayList<Error>());
			response.getErrors().add(new Error(ErrorType.AUTHENTICATIONERROR, ErrorCode.INVALID_SELLER, "Invalid Seller", "sellerGUID"));
		    return response;
		}
		Long sellerId = Long.parseLong(securityContext.getUserId());
        Calendar from = null;
        Calendar to = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if(StringUtils.trimToNull(fromDate) != null && dateFormat.parse(fromDate) != null) {
                from = Calendar.getInstance();
                from.setTime(dateFormat.parse(fromDate));
            }
            if(StringUtils.trimToNull(toDate) != null && dateFormat.parse(toDate) != null) {
                to = Calendar.getInstance();
                to.setTime(dateFormat.parse(toDate));
            }
            if(from != null && to != null && from.after(to)) {
                log.warn("Invalid dates in the input - date filters will be ignored");
                from = null;
                to = null;
            }
        } catch (ParseException e) {
            log.warn("Invalid dates in the input - date filters will be ignored");
            from = null;
            to = null;
        }
        List<String> transTypeList = new ArrayList<String>();
        if(!StringUtils.isEmpty(transactionType)){
        	for(String type:transactionType.split(",")){
        		transTypeList.add(type);
        	}
        }
        if(transTypeList.isEmpty()){
        	// default
        	transTypeList.add("D");
        }
		try {
			Map<Long, CreditCardDetails> sellerCcMap = paymentHelper.getMappedValidSellerCcId(sellerGUID);
			long count = creditCardChargeBO.getCreditCardChargesCount(sellerCcMap.keySet(), from, to, transTypeList, currencyCode);
			List<SellerCcTrans> sellerCcTransList = creditCardChargeBO.getCreditCardCharges(sellerCcMap.keySet(), sort, from, to, start, rows, transTypeList, currencyCode);
			response = AccountResponseAdapter.convert(sellerCcTransList, sellerCcMap);
			populateEventNames(response.getCreditCardCharges(), sellerId);
			response.setTotalCount(count);
			log.info("api_domain=account api_resource=creditCardCharges api_method=getCreditCardCharges status=success");
			return response;
		} catch (Exception e) {
			log.error(
					"api_domain=account api_resource=creditCardCharges api_method=getCreditCardCharges status=error error_message=unexpected exception occured"+" sellerGuid="
							+ sellerGUID, e);
			response.setErrors(new ArrayList<Error>());
			response.getErrors().add(new Error(ErrorType.SYSTEMERROR, ErrorCode.SYSTEM_ERROR, "Sytem error occured", null));
			return response;
		}
	}
	
	private void populateEventNames(List<CreditCardCharge> creditCardCharges, Long sellerId) throws SolrServerException, ParseException  {
		if(creditCardCharges != null && !creditCardCharges.isEmpty()){
			Map<Long, Long> orderEventMap = new HashMap<Long, Long>();

			boolean useSolrCloud = MasterStubHubProperties.getPropertyAsBoolean("account.v1.creditCardCharges.useSolrCloud",
					false);
			if (useSolrCloud) {
				orderEventMap = buildOrderEventMapFromSolrCloud(creditCardCharges);
			}
			Map<Long, String> eventIdNameMap = eventUtil.getEventNames(new HashSet<Long>(orderEventMap.values()));

			for(CreditCardCharge creditCardCharge:creditCardCharges){
				Long eventId = orderEventMap.get(creditCardCharge.getOrderID());
				creditCardCharge.setEventName(eventIdNameMap.get(eventId));
			}

		}
	}



	private Map<Long, Long> buildOrderEventMapFromSolrCloud(List<CreditCardCharge> creditCardCharges){

		List<String> idList = new ArrayList<String>();
		for (CreditCardCharge creditCardCharge : creditCardCharges) {
			idList.add(String.valueOf(creditCardCharge.getOrderID()));
		}

		JsonNode jsonNode = accountSolrCloudBiz.getByIdList("sale", "id", idList);

		Map<Long, Long> orderEventMap = new HashMap<Long, Long>();
		if(!notNull(jsonNode)){
			return orderEventMap;
		}

		JsonNode responseNode = jsonNode.get("response");
		if(!notNull(responseNode)){
			return orderEventMap;
		}
		ArrayNode docsNode = (ArrayNode) responseNode.get("docs");
		if(notNull(docsNode)){
			int size = docsNode.size();
			if(size <= 0 ){
				return  orderEventMap;
			}
			for(int i = 0; i < size; i++){
				ObjectNode  docNode = (ObjectNode) docsNode.get(i);
				JsonNode obj = docNode.get("id");
				Long tid = null;
				Long eventId = null;
				if(notNull(obj)){
					 tid = obj.asLong();
				}
				obj = docNode.get("eventId");
				if(notNull(obj)){
					eventId = obj.asLong();
				}

				if(tid != null && eventId != null){
					orderEventMap.put(tid,eventId);
				}
			}
		}
		return orderEventMap;

	}

	private boolean notNull(JsonNode jsonNode){
		return jsonNode != null && !jsonNode.isNull();
	}

}
