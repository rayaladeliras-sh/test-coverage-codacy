package com.stubhub.domain.account.impl;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.codehaus.jackson.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.stubhub.domain.account.adapter.AccountRequestAdapter;
import com.stubhub.domain.account.adapter.AccountResponseAdapter;
import com.stubhub.domain.account.adapter.AccountResponseJsonAdapter;
import com.stubhub.domain.account.biz.intf.AccountSolrCloudBiz;
import com.stubhub.domain.account.common.Error;
import com.stubhub.domain.account.common.OrderSearchCriteria;
import com.stubhub.domain.account.common.SalesSearchCriteria;
import com.stubhub.domain.account.common.enums.ErrorCode;
import com.stubhub.domain.account.common.enums.ErrorType;
import com.stubhub.domain.account.common.exception.AccountException;
import com.stubhub.domain.account.intf.SalesHistoryRequest;
import com.stubhub.domain.account.intf.SalesHistoryResponse;
import com.stubhub.domain.account.intf.SalesHistoryService;
import com.stubhub.newplatform.property.MasterStubHubProperties;
import com.stubhub.platform.utilities.webservice.security.ExtendedSecurityContext;

@Component("salesHistoryService")
public class SalesHistoryServiceImpl implements SalesHistoryService {
	
	private final static Log log = LogFactory.getLog(SalesHistoryServiceImpl.class);
	private static String api_domain = "account";
	private static String api_resource = "salesHistory";

    @Autowired
	@Qualifier("accountSolrCloudBiz")
    private AccountSolrCloudBiz accountSolrCloudBiz;

	@Override
	public SalesHistoryResponse getSalesHistory(SalesHistoryRequest request, ExtendedSecurityContext securityContext) {
		SalesHistoryResponse response = new SalesHistoryResponse();
		if(securityContext == null || StringUtils.isEmpty(securityContext.getUserId())) {
			response.setErrors(new ArrayList<Error>());
			response.getErrors().add(new Error(ErrorType.AUTHENTICATIONERROR, ErrorCode.INVALID_SELLER, "Invalid Seller", null));
			return response;
		}
		boolean useSolrCloud = MasterStubHubProperties.getPropertyAsBoolean("account.v1.saleshistory.useSolrCloud",
				false);
		try {
			if(useSolrCloud){
				SalesSearchCriteria criteria = AccountRequestAdapter.convertRequestToSaleSearchCriteria(request);
				JsonNode queryResponse = accountSolrCloudBiz.getCSOrderDetails(criteria);
				response = AccountResponseJsonAdapter.convertJsonNodeToSalesHistoryResponse(queryResponse, request.getPriceType());
			}
		} catch(AccountException e) {					
			log.error("api_domain=" + api_domain + " api_resource=" + api_resource + " api_method=getSalesHistory status=success_with_error "
					+ "error_message=\"AccountException occured while getting salesSummary\""+" eventId=" + request.getEventId(), e);			
			response.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());			
			response.getErrors().add(e.getListingError());
			return response;
		} catch(Exception e) {		
			log.error("api_domain=" + api_domain + " api_resource=" + api_resource + " api_method=getSalesHistory status=error error_message=\"Exception occured while getting saleSummary\"" 
					+ " eventId="+request.getEventId(), e);			
			response.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
			response.getErrors().add(new com.stubhub.domain.account.common.Error(ErrorType.SYSTEMERROR, ErrorCode.SYSTEM_ERROR, "", ""));
			return response;
		}
		return response;
	}
	
}
