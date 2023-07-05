package com.stubhub.domain.account.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Currency;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.stubhub.domain.account.biz.impl.SellerPaymentSolrCloudBOImpl;
import com.stubhub.domain.account.biz.intf.*;
import com.stubhub.domain.infrastructure.common.core.monitor.SHMonitor;
import com.stubhub.domain.infrastructure.common.core.monitor.SHMonitorFactory;
import com.stubhub.domain.infrastructure.config.client.core.SHConfig;
import com.stubhub.domain.infrastructure.web.client.SHRestTemplate;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.jaxrs.ext.search.SearchContext;
import org.apache.solr.client.solrj.SolrServerException;
import org.codehaus.jackson.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.stubhub.common.exception.UserNotAuthorizedException;
import com.stubhub.domain.account.adapter.PaymentResponseJsonAdpater;
import com.stubhub.domain.account.common.PaymentsSearchCriteria;
import com.stubhub.domain.account.common.enums.ErrorCode;
import com.stubhub.domain.account.common.enums.ErrorType;
import com.stubhub.domain.account.common.enums.PaymentUserDefinedStatus;
import com.stubhub.domain.account.common.exception.DisbursementOptionException;
import com.stubhub.domain.account.common.exception.InvalidPaymentException;
import com.stubhub.domain.account.common.exception.PaymentStatusException;
import com.stubhub.domain.account.common.util.SecurityContextUtil;
import com.stubhub.domain.account.datamodel.entity.DisbursementOptionEnum;
import com.stubhub.domain.account.datamodel.entity.SellerPaymentStatusEnum;
import com.stubhub.domain.account.impl.export.ExportFile;
import com.stubhub.domain.account.impl.export.FileCreator;
import com.stubhub.domain.account.impl.export.FileCreatorFactory;
import com.stubhub.domain.account.intf.PaymentsService;
import com.stubhub.domain.account.intf.SellerPayment;
import com.stubhub.domain.account.intf.SellerPayments;
import com.stubhub.domain.account.intf.UpdatePaymentRequest;
import com.stubhub.domain.account.intf.UpdatePaymentResponse;
import com.stubhub.domain.account.intf.UpdatePaymentStatusRequest;
import com.stubhub.domain.account.intf.UpdatePaymentStatusResponse;
import com.stubhub.domain.infrastructure.common.exception.derived.SHUnauthorizedException;
import com.stubhub.newplatform.common.entity.Money;
import com.stubhub.newplatform.property.MasterStubHubProperties;
import com.stubhub.platform.utilities.webservice.security.ExtendedSecurityContext;
import org.springframework.util.CollectionUtils;

@Component("paymentsService")
public class PaymentsServiceImpl implements PaymentsService {

	private final static Log log = LogFactory.getLog(PaymentsServiceImpl.class);
	private static final Set<String> validFilters = initValidFilters();

	@Value("${paymentsService.allowedAppName}")
	private String allowedAppName;

	@Autowired
	private SHConfig shConfig;
	
	@Autowired
	private StubTransBO stubTransBO;

	@Autowired
	private CreditCardChargeBO creditCardChargeBO;

	@Autowired
	@Qualifier("sellerPaymentBO")
	private SellerPaymentBO sellerPaymentBO;

	@Autowired
	private CreditMemoBO creditMemoBO;

	@Autowired
	@Qualifier("sellerPaymentSolrCloudBO")
	private SellerPaymentSolrCloudBO sellerPaymentSolrCloudBO;

	@Autowired
	private SecurityContextUtil securityContextUtil;

	private static final int maxNumberOfRows = 200;

	private SHRestTemplate restTemplate;
	@PostConstruct
	public void init() {
		restTemplate = new SHRestTemplate();
		restTemplate.initialize();
	}

	@Override
	public SellerPayments getPayments(String sellerGUID, String sort,
			String filters, String start, String rows,
            String includePaymentSummary, String includeCurrencySummary, String includeCreditMemo, String includeChargeAmtCMAmt, String includeRecordType, String includeCMAppliedHistories,
            SearchContext searchContext,
			ExtendedSecurityContext context) {
		SHMonitor monitor =  SHMonitorFactory.getMonitor();
		monitor.start();
		SellerPayments payments = new SellerPayments();
		try {
			// if(searchContext !=null ){
			// SearchCondition<SellerPayment> conditions =
			// searchContext.getCondition(SellerPayment.class);
			// }
			
            securityContextUtil.validateUserGuid(context, sellerGUID);
            //filters=dateLastModified:[2017-12-16T06:43:55ZTO2019-11-16T06:43:55Z]
            if (StringUtils.isNotEmpty(filters) && !filters.contains(SellerPaymentSolrCloudBOImpl.DATE_LAST_MODIFIED)){
				validateFiltersFormat(filters);
			}

			String currency = validateParamsAndReturnCurrency(filters, includePaymentSummary);

            String sellerId = context.getUserId();
			PaymentsSearchCriteria criteria = new PaymentsSearchCriteria();
			criteria.setSellerId(sellerId);
			criteria.setSort(sort);
			criteria.setQ(filters);
			criteria.setCurrency(currency);

			if (start != null) {
				criteria.setStart(Integer.parseInt(start));
			}
			if (rows != null) {
				int parsedRows = Integer.parseInt(rows);
				if (parsedRows > maxNumberOfRows) {
					parsedRows = maxNumberOfRows;
				}
				criteria.setRows(parsedRows);
			} else {
				criteria.setRows(maxNumberOfRows);
			}

			criteria.setIncludePaymentSummary(Boolean.valueOf(includePaymentSummary));
            criteria.setIncludeCurrencySummary(Boolean.valueOf(includeCurrencySummary));
			criteria.setIncludeCreditMemo(Boolean.valueOf(includeCreditMemo));
			boolean useSolrCloud = MasterStubHubProperties.getPropertyAsBoolean("account.v1.payment.useSolrCloud",
					false);
			if(useSolrCloud){
				JsonNode response = sellerPaymentSolrCloudBO.getSellerPayments(criteria);
				payments = PaymentResponseJsonAdpater.convertJsonNodeToResponse(response,currency, BooleanUtils.toBoolean(includeRecordType), BooleanUtils.toBoolean(includeCMAppliedHistories));
				if (Boolean.valueOf(includeChargeAmtCMAmt)){
					payments = setChargeToSellerAmtCreditMemoAmt(payments);
				}

			}
			log.info("api_domain=account api_resource=payments api_method=getPayments status=success"
					+ " sellerGuid=" + sellerGUID);

			
        }
        catch (SolrServerException e) {
			log.error(
					"api_domain=account api_resource=payments api_method=getPayments status=error error_message=unexpected exception occured while getting payments"
							+ " sellerGuid=" + sellerGUID, e);
			SellerPayments sp = new SellerPayments();
			sp.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
			sp.getErrors().add(
					new com.stubhub.domain.account.common.Error(
							ErrorType.SYSTEMERROR, ErrorCode.SYSTEM_ERROR,
							"solr error", ""));
			return sp;
		} catch (IllegalArgumentException e) {
			log.error(
					"api_domain=account api_resource=payments api_method=getPayments status=error error_message=unexpected exception occured while getting payments.",
					e);
			SellerPayments sp = new SellerPayments();
			sp.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
			sp.getErrors().add(
					new com.stubhub.domain.account.common.Error(
							ErrorType.SYSTEMERROR, ErrorCode.SYSTEM_ERROR,
							e.getMessage(), ""));
			return sp;
        }
        catch (UserNotAuthorizedException e) {
            log.error(
                    "api_domain=account api_resource=payments api_method=getPayments status=error error_message=authorization error"
                            + " sellerGuid=" + sellerGUID,
                    e);
            SellerPayments sp = new SellerPayments();
            sp.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
            sp.getErrors()
                    .add(new com.stubhub.domain.account.common.Error(ErrorType.AUTHENTICATIONERROR,
                            ErrorCode.INVALID_SELLER, "Invalid Seller", "sellerGUID"));
            return sp;
        }finally {
			monitor.stop();
			log.info("api_domain=account api_resource=payments api_method=getPayments execute time" + monitor.getTime());
		}
		return payments;
	}
	
	private SellerPayments setChargeToSellerAmtCreditMemoAmt(SellerPayments payments){
		SHMonitor monitor =  SHMonitorFactory.getMonitor();
		monitor.start();
		List<SellerPayment> sellerPayments = payments.getPayments();
		List<SellerPayment> returnSellerPayments =  new ArrayList<SellerPayment>();
		if (CollectionUtils.isEmpty(sellerPayments)){
			return payments;
		}
		List<Long> pidList = Lists.newArrayList();
		List<Long> tidList = Lists.newArrayList();

		for (SellerPayment sellerPayment : sellerPayments){
			pidList.add(Long.parseLong(sellerPayment.getID()));
			tidList.add(Long.parseLong(sellerPayment.getOrderID()));
		}

		Map<Long,BigDecimal> tidChargeSellerAmtMap = creditCardChargeBO.getChargeToSellerAmountByTid(tidList);
		Map<Long,BigDecimal> pidAppliedAmtMap = creditMemoBO.getAppliedAmountByPID(pidList);
		for (SellerPayment sellerPayment : sellerPayments){
			Long pid = Long.parseLong(sellerPayment.getID());
			Long tid = Long.parseLong(sellerPayment.getOrderID());
			if (tidChargeSellerAmtMap.containsKey(tid)){
				sellerPayment.setChargeToSellerAmt(new Money(tidChargeSellerAmtMap.get(tid), sellerPayment.getCurrencyCode()));
			}
			if (pidAppliedAmtMap.containsKey(pid)){
				sellerPayment.setCreditMemoAmt(new Money(pidAppliedAmtMap.get(pid), sellerPayment.getCurrencyCode()));
			}
			returnSellerPayments.add(sellerPayment);
		}
		payments.setPayments(returnSellerPayments);
		monitor.stop();
		log.info("api_domain=account api_resource=payments api_method=setChargeToSellerAmtCreditMemoAmt execute time "+monitor.getTime());
		return payments;
	}

	private  void validateFiltersFormat(String filters){
		if(StringUtils.isNotBlank(filters)){
		 String pattern = "^([^,:]+:[^,:]+,)*[^,:]+:[^,:]+$";
	      Pattern r = Pattern.compile(pattern);
	      Matcher m = r.matcher(filters);
	      if(!m.find()){
	    	  throw new IllegalArgumentException(
						"The valid format of fitlers should be like 'a:b,c:d'");
	      }
		}
	}
	
	private static final List<String> validStatusInFilter = new ArrayList<String>();
	static {
		for (PaymentUserDefinedStatus status : PaymentUserDefinedStatus.values()) {
			validStatusInFilter.add(status.getName().toLowerCase());
		}
	}

	private String validateParamsAndReturnCurrency(String filters, String includePaymentSummary) {
		String currency = null;
		
		if (StringUtils.isNotBlank(filters)) {
			String filtersArray[] = filters.split(",");
			for (String filter : filtersArray) {
				String params[] = filter.split(":");
				if (!validFilters.contains(params[0].trim())) {
					throw new IllegalArgumentException(
							"Invalid filter parameter input. Filter name is:" + params[0]);
				}
				if ("currency".equalsIgnoreCase(params[0].trim())) {
					currency = params[1].trim();
					try{
						Currency.getInstance(currency);
						}catch(IllegalArgumentException e){
							throw  new IllegalArgumentException(
									"The currency in the filter is not a supported ISO 4217 code.");
						}
				}else if("status".equalsIgnoreCase(params[0].trim())){
					String statusArray = params[1].trim();
					String statuses[] = statusArray.split(" ");
					for(String status:statuses){
						if(!(validStatusInFilter.contains(status.toLowerCase()))){
							throw  new IllegalArgumentException(
									"The status " + status + " is invalid.");
						}
					}
				}
			}
			
		}
		if (includePaymentSummary != null) {
			if (!(Boolean.TRUE.toString().equalsIgnoreCase(
					includePaymentSummary.trim()) || Boolean.FALSE.toString()
					.equalsIgnoreCase(includePaymentSummary.trim()))) {
				    throw new IllegalArgumentException(
						"Invalid includePaymentSummary input. It must be 'true' or 'false'.");
			}
            else if (Boolean.TRUE.toString().equalsIgnoreCase(includePaymentSummary.trim())
                    && currency == null) { throw new IllegalArgumentException(
                            "Need to put 'currency' to the filter if specify includePaymentSummary to true"); }
        }

		return currency;
	}

	private static Set<String> initValidFilters() {
		Set<String> filters = new HashSet<String>();
		filters.add("orderID");
		filters.add("currency");
		filters.add("paymentDate");
		filters.add("paymentInitiatedDate");
		filters.add("status");
		filters.add("paymentAmount");
		filters.add("paymentType");
		filters.add("keyword");
		filters.add("dateLastModified");
		filters.add("holdPaymentType");
		return Collections.unmodifiableSet(filters);
	}

	private static String formatCalendar(Date date, TimeZone tz) {
		String dateFormat = "yyyy-MM-dd'T'HH:mm:ssZ";
		SimpleDateFormat sf = new SimpleDateFormat(dateFormat);
		sf.setTimeZone(tz);
		return sf.format(date);
	}

	@Override
	@Transactional
	public UpdatePaymentStatusResponse updatePaymentStatus(UpdatePaymentStatusRequest request, ExtendedSecurityContext context) {
		if (!StringUtils.equalsIgnoreCase(allowedAppName, context.getApplicationName())) {
			throw new SHUnauthorizedException();
		}

		if (request.getUserId() == null) {
			// empty user id
			InvalidPaymentException ex = new InvalidPaymentException();
			ex.getData().put("userId", "");
			throw ex;
		}

		if (request.getAction() == null) {
			// empty action
			InvalidPaymentException ex = new InvalidPaymentException();
			ex.getData().put("action", "");
			throw ex;
		}
		Calendar latestPaymentDate = request.getLatestPaymentDate();
		log.info(String.format("_message=\"updatePaymentStatus\" userId=%s action=%s latestPaymentDate=%s", request.getUserId(), request.getAction(), latestPaymentDate == null ? null:formatCalendar((latestPaymentDate.getTime()),
				TimeZone.getTimeZone("UTC"))));

		// find payments to update
		List<com.stubhub.domain.account.datamodel.entity.SellerPayment> payments =
				new ArrayList<com.stubhub.domain.account.datamodel.entity.SellerPayment>();
		if (latestPaymentDate == null){
			payments = sellerPaymentBO.findSellerPaymentsByAction(request.getUserId(), request.getAction());
		}else{
			payments = sellerPaymentBO.findSellerPaymentsByAction(request.getUserId(), request.getAction(), latestPaymentDate);
		}

        if (log.isInfoEnabled()) {
            log.info("Found payments for update with the total number:" + payments.size());
        }

		// update all payments and add result to response
		UpdatePaymentStatusResponse response = new UpdatePaymentStatusResponse();
		response.setPayments(new ArrayList<SellerPayment>());
		for (com.stubhub.domain.account.datamodel.entity.SellerPayment payment : payments) {
            SellerPaymentStatusEnum status = calSellerPaymentStatus(request.getAction(), payment);
			sellerPaymentBO.updateSellerPaymentStatus(payment, status);

			SellerPayment webEntity = new SellerPayment();
			webEntity.setID(String.valueOf(payment.getId()));
			webEntity.setStatus(payment.getStatus());
			response.getPayments().add(webEntity);
		}
		return response;
	}

    private SellerPaymentStatusEnum calSellerPaymentStatus(UpdatePaymentStatusRequest.Action action,
                                                           com.stubhub.domain.account.datamodel.entity.SellerPayment payment) {
        switch (action) {
            case RELEASE_PAYMENT_FOR_DUE_DILIGENCE:
                return calSellerPaymentStatus4Release(payment);
            case HOLD_PAYMENT_FOR_DUE_DILIGENCE:
            default:
                return calSellerPaymentStatus4Hold();
        }
    }

    private SellerPaymentStatusEnum calSellerPaymentStatus4Release(com.stubhub.domain.account.datamodel.entity.SellerPayment payment) {
        if (payment.getDisbursementOptionId() != null) {
            if (payment.getDisbursementOptionId().intValue() == 1) {
                return SellerPaymentStatusEnum.READY_TO_PAY;
            } else if(payment.getDisbursementOptionId().intValue() == 2) {
                return SellerPaymentStatusEnum.HOLD_PAYMENT_FOR_MANUAL_DISBURSEMENT;
            }
        }

        // default for null value
        return SellerPaymentStatusEnum.READY_TO_PAY;
    }

    private SellerPaymentStatusEnum calSellerPaymentStatus4Hold() {
        return SellerPaymentStatusEnum.HOLD_PAYMENT_DUE_TO_DD;
    }

	@Override
	@Transactional
	//TODO update
	public UpdatePaymentResponse updatePayment(Long paymentId,
			UpdatePaymentRequest request, ExtendedSecurityContext context) {
		com.stubhub.domain.account.datamodel.entity.SellerPayment payment = sellerPaymentBO
				.getSellerPaymentById(paymentId);

		// validate payment exist
		if (payment == null) {
			InvalidPaymentException ex = new InvalidPaymentException();
			ex.getData().put("paymentId", String.valueOf(paymentId));
			throw ex;
		}

		// validate payment belong to seller
		if (!String.valueOf(payment.getSellerId()).equals(context.getUserId())) {
			throw new SHUnauthorizedException();
		}

		// validate payment status
		SellerPaymentStatusEnum status = SellerPaymentStatusEnum
				.getSellerPaymentStatusEnum(payment.getSellerPaymentStatusId());
		if (status != SellerPaymentStatusEnum.FUND_CAPTURE
				&& status != SellerPaymentStatusEnum.HOLD_PAYMENT_DUE_TO_DD
				&& status != SellerPaymentStatusEnum.HOLD_PAYMENT_FOR_MANUAL_DISBURSEMENT) {
			PaymentStatusException ex = new PaymentStatusException();
			ex.getData().put("status", status.getName());
			throw ex;
		}

		// validate disbursement option
		if (DisbursementOptionEnum.getDisbursementOptionById(payment
				.getDisbursementOptionId()) != DisbursementOptionEnum.MANUAL) {
			DisbursementOptionException ex = new DisbursementOptionException();
			throw ex;
		}

		// update payeeEmailId
		payment.setPayeeEmailId(request.getPayeeEmailId());
		
		// sync Payee Name with Payee EmailId
		payment.setPayeeName(payment.getPayeeEmailId());

		// if status is hold for manual, change to ready to pay, then do update
		if (SellerPaymentStatusEnum.HOLD_PAYMENT_FOR_MANUAL_DISBURSEMENT == SellerPaymentStatusEnum
				.getSellerPaymentStatusEnum(payment.getSellerPaymentStatusId())) {
			// update SellerPayment and history
			sellerPaymentBO.updateSellerPaymentStatus(payment,
					SellerPaymentStatusEnum.READY_TO_PAY);
		} else {
			// update SellerPayment
			sellerPaymentBO.saveSellerPayment(payment);
		}

		// build response and return
		UpdatePaymentResponse response = new UpdatePaymentResponse();
		response.setId(payment.getId());
		response.setPayeeEmailId(payment.getPayeeEmailId());
		return response;
	}

	private static final String[] header = new String[] {"Payment Date", "Order Number", "Total Ticket Price",
			"Commission Paid", "Adjustment Amount", "Payout", "Payee", "Payment Type",
			"Event Name", "Event Date", "Venue", "StubHub Listing ID", "External Listing ID",
			"Quantity", "Section", "Row", "Seat", "Payment Status", "Reference Number"};

	@Override
	public Response exportPayments(String sellerGUID, ExportFileType fileType, Integer fromDateUnits, Integer toDateUnits, DateUnit dateUnit, String dateFormat, String currencyCode, ExtendedSecurityContext context) {

		try {
			securityContextUtil.validateUserGuid(context, sellerGUID);

			String sellerId = context.getUserId();

			PaymentsSearchCriteria criteria = new PaymentsSearchCriteria();
			criteria.setSellerId(sellerId);
			criteria.setSort("orderID asc");

			String fromPart = "NOW-" + toDateUnits + dateUnit;
			String toPart = "NOW-" + fromDateUnits + dateUnit;

			String q = "paymentDate:[" + fromPart + " TO " + toPart + "]";
			if (org.apache.commons.lang3.StringUtils.isNotEmpty(currencyCode)) {
				q += ",currency:" + currencyCode;
			}
			criteria.setQ(q);

			criteria.setIncludePaymentSummary(false);
			criteria.setIncludeCreditMemo(false);

			criteria.setRows(Integer.MAX_VALUE);
			List<String[]> paymentEntries = new ArrayList<String[]>();
			ExportFile exportFile = new ExportFile();

			paymentEntries.add(header);

			SimpleDateFormat simpleDateFormat;
			if (dateFormat == null) {
				simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			} else {
				simpleDateFormat = new SimpleDateFormat(dateFormat);
			}

			boolean useSolrCloud = MasterStubHubProperties.getPropertyAsBoolean("account.v1.payment.useSolrCloud",
					false);
			if (useSolrCloud) {
				JsonNode response = sellerPaymentSolrCloudBO.getSellerPayments(criteria);
				PaymentResponseJsonAdpater.convertJsonNodeToSellerPaymentList(paymentEntries, response, header.length,
						simpleDateFormat);
			}
			
			exportFile.setData(paymentEntries);
			FileCreator fileCreator = FileCreatorFactory.getFileCreator(fileType, exportFile);

			byte[] bodyBytes = fileCreator.createExport();

			String fileName = "PaymentExport." + fileType.name();

			return Response.status(Response.Status.OK).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM).header("Content-Disposition", "attachment; filename="
					+ fileName).entity(bodyBytes).build();

		} catch (UserNotAuthorizedException e) {
			log.error(
					"api_domain=account api_resource=payments api_method=exportPayments status=error error_message=authorization error"
							+ " sellerGuid=" + sellerGUID, e);
			SellerPayments sp = new SellerPayments();
			sp.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
			sp.getErrors().add(
					new com.stubhub.domain.account.common.Error(
							ErrorType.AUTHENTICATIONERROR,
							ErrorCode.INVALID_SELLER, "Invalid Seller",
							"sellerGUID"));
			return Response.status(Response.Status.UNAUTHORIZED).entity(sp).build();
		} catch (SolrServerException e) {
			log.error(
					"api_domain=account api_resource=payments api_method=exportPayments status=error error_message=unexpected exception while exportPayments."
							+ " sellerGuid=" + sellerGUID, e);
			SellerPayments sp = new SellerPayments();
			sp.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
			sp.getErrors().add(
					new com.stubhub.domain.account.common.Error(
							ErrorType.SYSTEMERROR, ErrorCode.SYSTEM_ERROR,
							"solr error", ""));
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(sp).build();
		} catch (Exception e) {
			log.error(
					"api_domain=account api_resource=payments api_method=exportPayments status=error error_message=unexpected exception while exportPayments.",
					e);
			SellerPayments sp = new SellerPayments();
			sp.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
			sp.getErrors().add(
					new com.stubhub.domain.account.common.Error(
							ErrorType.SYSTEMERROR, ErrorCode.SYSTEM_ERROR,
							e.getMessage(), ""));
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(sp).build();
		}

	}

	@Override
	public Response evaluatePIRule(Long sellerId, String rule, ExtendedSecurityContext context) {
		if (!StringUtils.equalsIgnoreCase(allowedAppName, context.getApplicationName())) {
			throw new SHUnauthorizedException();
		}

		String appToken = shConfig.getProperty("newapi.accessToken");
		String url = shConfig.getProperty("paymentService.api.calculateThreshold.url", "https://api-wg.stubprod.com/payments/payables/v1/pi/");
		String thresholdUrl = url + sellerId;

        Map<String, String> queryParams = Maps.newHashMap();
        queryParams.put("rule", rule);

		log.info("message\"call pi rule api\", url=" + thresholdUrl + ", rule=" + rule);

		String response = restTemplate
				.clone()
				.setUri(thresholdUrl).setQueryParams(queryParams)
				.setHeader("Content-Type", MediaType.APPLICATION_JSON)
				.setHeader("Accept", MediaType.APPLICATION_JSON)
				.setHeader("Authorization", "Bearer " + appToken)
				.sync()
				.get(String.class);
		return Response.ok(response).build();
	}

}
