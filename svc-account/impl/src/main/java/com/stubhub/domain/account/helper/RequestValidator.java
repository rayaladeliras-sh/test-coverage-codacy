package com.stubhub.domain.account.helper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.stubhub.domain.account.common.Error;
import com.stubhub.domain.account.common.enums.ErrorCode;
import com.stubhub.domain.account.common.enums.ErrorType;
import com.stubhub.domain.account.common.util.ValidationUtil;
import com.stubhub.domain.account.intf.CSOrderDetailsRequest;
import com.stubhub.domain.account.intf.OrdersResponse;
import com.stubhub.domain.account.intf.SearchEmailCriteria;
import com.stubhub.domain.account.intf.SubstitutionRequest;
import com.stubhub.domain.account.intf.TransactionSummaryRequest;
import com.stubhub.domain.infrastructure.soa.core.context.SHServiceContext;

@Component("requestValidator")
public class RequestValidator {
	private final static Log log = LogFactory.getLog(RequestValidator.class);
	private String api_domain = "account";

	public List<Error> validateCSOrderDetailsRequestFields(CSOrderDetailsRequest csOrderDetailsRequest) {
		String api_resource = "csorderdetails";
		String orderId = csOrderDetailsRequest.getOrderId();
		String proxiedId = csOrderDetailsRequest.getProxiedId();
		String eventStartDate = csOrderDetailsRequest.getEventStartDate();
		String eventEndDate = csOrderDetailsRequest.getEventEndDate();
		String start = csOrderDetailsRequest.getStart();
		String row = csOrderDetailsRequest.getRow();
		List<Error> errorList = new LinkedList<Error>();

		if(StringUtils.trimToNull(orderId) == null && (StringUtils.trimToNull(proxiedId) == null)){
			log.error("api_domain=" + api_domain + " api_resource=" + api_resource + " api_method=validateCSOrderDetailsRequestFields" +
					" status=error error_message=No input provided. Either orderId or proxiedId must be supplied." +
					" orderId=" + orderId + ", proxiedId=" + proxiedId);
			errorList.add(new Error(
					ErrorType.INPUTERROR, 
					ErrorCode.INVALID_INPUT, 
					"No input provided. Either orderId or proxiedId must be supplied", 
					"orderId=" + orderId + ", proxiedId=" + proxiedId));
			return errorList;
		}	
		if(orderId != null && proxiedId != null){
			log.error("api_domain=" + api_domain + " api_resource=" + api_resource + " api_method=validateCSOrderDetailsRequestFields" +
					" status=error error_message=Multiple params supplied; either orderId or proxiedId is required." +
					" orderId=" + orderId + ", proxiedId=" + proxiedId);
			errorList.add(new Error(
					ErrorType.INPUTERROR, 
					ErrorCode.INVALID_INPUT, 
					"Multiple params supplied; Either orderId or proxiedId is required", 
					"orderId=" + orderId + " ,proxiedId=" + proxiedId));
			return errorList;
		}
		if(orderId != null && !isValidLong("orderId", orderId)){
			log.error("api_domain=" + api_domain + " api_resource=" + api_resource + " api_method=validateCSOrderDetailsRequestFields" +
					" status=error error_message=Invalid input provided." +
					" orderId=" + orderId);
			errorList.add(new Error(
					ErrorType.INPUTERROR, 
					ErrorCode.INVALID_INPUT, 
					"Invalid input provided", 
					"orderId=" + orderId));
			return errorList;
		}
		if(proxiedId != null && !isValidLong("proxiedId", proxiedId)){
			log.error("api_domain=" + api_domain + " api_resource=" + api_resource + " api_method=validateCSOrderDetailsRequestFields" +
					" status=error error_message=Invalid input provided." +
					" proxiedId=" + proxiedId);
			errorList.add(new Error(
					ErrorType.INPUTERROR, 
					ErrorCode.INVALID_INPUT, 
					"Invalid input provided", 
					"proxiedId=" + proxiedId));
			return errorList;
		}
		if((start != null && !isValidLong("start", start)) || (start != null && Integer.parseInt(start) < 0)){
			log.error("api_domain=" + api_domain + " api_resource=" + api_resource + " api_method=validateCSOrderDetailsRequestFields" +
					" status=error error_message=Invalid input provided." +
					" start=" + start + ", row=" + row);
			errorList.add(new Error(
					ErrorType.INPUTERROR, 
					ErrorCode.INVALID_INPUT, 
					"Invalid input provided", 
					"start=" + start));
			return errorList;
		}
		if((row != null && !isValidLong("row", row)) || (row != null && Integer.parseInt(row) <= 0)){
			log.error("api_domain=" + api_domain + " api_resource=" + api_resource + " api_method=validateCSOrderDetailsRequestFields" +
					" status=error error_message=Invalid input provided." +
					" row=" + row);
			errorList.add(new Error(
					ErrorType.INPUTERROR, 
					ErrorCode.INVALID_INPUT, 
					"Invalid input provided", 
					"row=" + row));
			return errorList;
		}
		if((row != null && start == null) || (start != null && row == null)){
			log.error("api_domain=" + api_domain + " api_resource=" + api_resource + " api_method=validateCSOrderDetailsRequestFields" +
					" status=error error_message=Must provide both start and row or none." +
					"start=" + start + ", row=" + row);
			errorList.add(new Error(
					ErrorType.INPUTERROR, 
					ErrorCode.INVALID_INPUT, 
					"Must provide both start and row or none", 
					"start=" + start + ", row=" + row));
			return errorList;
		}
		if(eventStartDate != null && !isValidDate("eventStartDate", eventStartDate)){
			log.error("api_domain=" + api_domain + " api_resource=" + api_resource + " api_method=validateCSOrderDetailsRequestFields" +
					" status=error error_message=Invalid input provided." +
					" eventStartDate=" + eventStartDate);
			errorList.add(new Error(
					ErrorType.INPUTERROR, 
					ErrorCode.INVALID_INPUT, 
					"Invalid input provided", 
					"eventStartDate=" + eventStartDate));
			return errorList;
		}
		if(eventEndDate != null && !isValidDate("eventEndDate", eventEndDate)){
			log.error("api_domain=" + api_domain + " api_resource=" + api_resource + " api_method=validateCSOrderDetailsRequestFields" +
					" status=error error_message=Invalid input provided." +
					" eventEndDate=" + eventEndDate);
			errorList.add(new Error(
					ErrorType.INPUTERROR, 
					ErrorCode.INVALID_INPUT, 
					"Invalid input provided", 
					"eventEndDate=" + eventEndDate));
			return errorList;
		}
		if(eventStartDate != null && eventEndDate != null && (compareDates(eventStartDate, eventEndDate) == 1)){
			log.error("api_domain=" + api_domain + " api_resource=" + api_resource + " api_method=validateCSOrderDetailsRequestFields" +
					" status=error error_message=Invalid input provided." +
					" eventStartDate=" + eventStartDate + " is after " + "eventEndDate=" + eventEndDate);
			errorList.add(new Error(
					ErrorType.INPUTERROR, 
					ErrorCode.INVALID_INPUT, 
					"Invalid input provided", 
					"eventStartDate=" + eventStartDate + " is after " + "eventEndDate=" + eventEndDate));
			return errorList;
		}
		return null;
	}

	public boolean isValidLong(String param, String value) {
		try {
			Long.parseLong(value.trim());
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	public boolean isValidBoolean(String value){
		if (value != null){
			if ("true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value)){
				return true;
			} else {
				return false;
			}
		} else 
			return false;
	}

	public boolean isValidDate(String param, String value) {
		String datePattern = "\\d{4}-\\d{1,2}-\\d{1,2}";
		return value.matches(datePattern);
	}

	public int compareDates(String eventStartDate, String eventEndDate) {
		try {
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date startDate = format.parse(eventStartDate);
			Date endDate = format.parse(eventEndDate);
			if(startDate.compareTo(endDate)>0) return 1;
			else if(startDate.compareTo(endDate)<0) return -1;
			else if(startDate.compareTo(endDate)==0) return 0;
		} catch (ParseException e) {}
		return 1;
	}

	public List<Error> validateTransactionSummaryRequestFields(TransactionSummaryRequest summaryRequest) {
		List<Error> errorList = new LinkedList<Error>();
		String invalidArgument = (summaryRequest.getProxiedId() == null || !ValidationUtil.isValidLong(summaryRequest.getProxiedId())) ? "proxiedId"
				: ((summaryRequest.getBuyerFlip() != null && !ValidationUtil.isValidBoolean(summaryRequest.getBuyerFlip())) ? "buyerFlip"
						: null);
		if (invalidArgument != null){
			log.error("api_domain=account" + " api_resource=summary" + " api_method=getTransactionSummary" +
					" status=error error_message=missing or invalid required argument: " + invalidArgument);
			errorList.add(new Error(
					ErrorType.INPUTERROR, 
					ErrorCode.INVALID_INPUT, 
					"missing or invalid required argument", 
					invalidArgument));
			return errorList;
		}
		return null;
	}
	
	public List<Error> validateEmailHistoryRequestField(String proxiedId, SearchEmailCriteria searchCriteria) {
		String api_resource = "emails";
		List<Error> errorList = new LinkedList<Error>();
		String orderId = StringUtils.trimToNull(searchCriteria.getOrderId());
		String start = StringUtils.trimToNull(searchCriteria.getStart());
		String rows = StringUtils.trimToNull(searchCriteria.getRows());
		String fromDate = StringUtils.trimToNull(searchCriteria.getFromDate());
		String toDate = StringUtils.trimToNull(searchCriteria.getToDate());
		String buyerOrderId = StringUtils.trimToNull(searchCriteria.getBuyerOrderId());
		
		if(StringUtils.trimToNull(proxiedId) == null){
			log.error("api_domain=" + api_domain + " api_resource=" + api_resource + " api_method=validateEmailHistoryRequestField" +
					" status=error error_message=proxiedId must be supplied in the header." +
					" proxiedId=" + proxiedId);
			errorList.add(new Error(
					ErrorType.INPUTERROR, 
					ErrorCode.INVALID_INPUT, 
					"proxiedId must be supplied  in the header", 
					" proxiedId=" + proxiedId));
			return errorList;
		}
		if(orderId != null && !isValidLong("orderId", orderId)){
			log.error("api_domain=" + api_domain + " api_resource=" + api_resource + " api_method=validateEmailHistoryRequestField" +
					" status=error error_message=Invalid input provided." +
					" orderId=" + orderId);
			errorList.add(new Error(
					ErrorType.INPUTERROR, 
					ErrorCode.INVALID_INPUT, 
					"Invalid input provided", 
					"orderId=" + orderId));
			return errorList;
		}

		if(buyerOrderId != null && !isValidLong("buyerOrderId", buyerOrderId)){
				log.error("api_domain=" + api_domain + " api_resource=" + api_resource + " api_method=validateEmailHistoryRequestField" +
						" status=error error_message=Invalid input provided." +
						" buyerOrderId=" + buyerOrderId);
				errorList.add(new Error(
						ErrorType.INPUTERROR, 
						ErrorCode.INVALID_INPUT, 
						"Invalid input provided", 
						"buyerOrderId=" + buyerOrderId));
				return errorList;
		}		
		if(start != null && (!isValidLong("start", start) || Integer.parseInt(start) < 1)){
			log.error("api_domain=" + api_domain + " api_resource=" + api_resource + " api_method=validateEmailHistoryRequestField" +
					" status=error error_message=Invalid input provided." +
					" start=" + start);
			errorList.add(new Error(
					ErrorType.INPUTERROR, 
					ErrorCode.INVALID_INPUT, 
					"Invalid input provided", 
					"start=" + start));
			return errorList;
		}
		if(rows != null && (!isValidLong("rows", rows) ||  Integer.parseInt(rows) < 1 || Integer.parseInt(rows) > 60)){
			log.error("api_domain=" + api_domain + " api_resource=" + api_resource + " api_method=validateEmailHistoryRequestField" +
					" status=error error_message=Invalid input provided." +
					" rows=" + rows);
			errorList.add(new Error(
					ErrorType.INPUTERROR, 
					ErrorCode.INVALID_INPUT, 
					"Invalid input provided", 
					"rows=" + rows));
			return errorList;
		}
		if((fromDate != null && toDate == null) 
				|| ((fromDate == null && toDate != null)) 
				|| (fromDate != null && !isValidDate("fromDate", fromDate))
				|| (toDate != null && !isValidDate("toDate", toDate))
				|| (((fromDate != null && toDate != null)) && (compareDates(fromDate, toDate) == 1))) {
			log.error("api_domain=" + api_domain + " api_resource=" + api_resource + " api_method=validateEmailHistoryRequestField" +
					" status=error error_message=Invalid/null date provided." +
					" fromDate=" + fromDate);
			errorList.add(new Error(
					ErrorType.INPUTERROR, 
					ErrorCode.INVALID_INPUT, 
					"Invalid/null date provided", 
					"fromDate=" + fromDate + " ,toDate=" + toDate));
			return errorList;
		}
		return null;
		
	}
	
	public List<Error> validateUpdateCSOrderDetails(SHServiceContext serviceContext, OrdersResponse request) {
		List<Error> errorList = new LinkedList<Error>();
		String invalidArgument = serviceContext.getOperatorId() == null ? "operatorId"
				: (!ValidationUtil.isValidLong(request.getOrder().get(0).getTransaction().getOrderId()) ? "orderId"
				: ((request.getOrder().get(0).getDelivery().getOrderProcSubStatusCode() != null) && (!ValidationUtil.isValidLong(request.getOrder().get(0).getDelivery().getOrderProcSubStatusCode())) ? "orderProcSubStatusCode"
				: ((request.getOrder().get(0).getTransaction().getCancelled() != null) && (!ValidationUtil.isValidBoolean(request.getOrder().get(0).getTransaction().getCancelled().toString())) ? "cancelled"
				: null)));
		
		if (invalidArgument != null){
			log.error("api_domain=" + api_domain + " api_resource=csorderdetails" + " api_method=updateCSOrderDetails" +
					" status=error error_message=missing or invalid required argument: " + invalidArgument);
			errorList.add(new Error(ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT, "missing or invalid required argument", invalidArgument));
			return errorList;
		}
		return null;
	}
	
	public List<Error> validateCreateSubOrderRequest (SubstitutionRequest request, String orderId, SHServiceContext serviceContext) {
		List<Error> errorList = new LinkedList<Error>();
		String invalidArgument = 
				   !ValidationUtil.isValidLong(orderId) ? "orderId"				   
				: (!ValidationUtil.isValidLong(request.getListingId()) ? "listingId"
				: ((!ValidationUtil.isValidLong(request.getQuantity()) || Integer.parseInt(request.getQuantity()) <= 0) ? "quantity"
				: (!ValidationUtil.isValidMoney(request.getTicketCostDifference()) ? "ticketCostDifference"
				: (!ValidationUtil.isValidMoney(request.getSellerPayoutDifference()) ? "sellerPayoutDifference"
				: (!ValidationUtil.isValidLong(request.getSubsReasonId()) ? "subsReasonId"
				: (!ValidationUtil.isValidLong(request.getDeliveryMethodId()) ? "deliveryMethodId"
				: (!ValidationUtil.isValidLong(request.getFulfillmentMethodId()) ? "fulfillmentMethodId"
				: (request.getLmsLocationId() != null && !ValidationUtil.isValidLong(request.getLmsLocationId()) ? "lmsLocationId"
				: (request.getInHandDate() != null && !isValidDate(null, request.getInHandDate()) ? "inHandDate"
				: (!ValidationUtil.isValidMoney(request.getTicketCost()) ? "ticketCost"
				: (!ValidationUtil.isValidMoney(request.getShipCost()) ? "shipCost"
				: (!ValidationUtil.isValidMoney(request.getTotalCost()) ? "totalCost"
				: (!ValidationUtil.isValidMoney(request.getDiscountCost()) ? "discountCost"
				: (!ValidationUtil.isValidMoney(request.getSellerFeeVal()) ? "sellerFeeVal"
				: (!ValidationUtil.isValidMoney(request.getBuyerFeeVal()) ? "buyerFeeVal"
				: (request.getPremiumFees() != null && !ValidationUtil.isValidMoney(request.getPremiumFees()) ? "premiumFees"
				: (!ValidationUtil.isValidMoney(request.getSellerPayoutAmount()) ? "sellerPayoutAmount"
				: (!ValidationUtil.isValidMoney(request.getSellerPayoutAtConfirm()) ? "sellerPayoutAtConfirm"
				: (request.getAddOnFee() != null && !ValidationUtil.isValidMoney(request.getAddOnFee()) ? "addOnFee"
				: (request.getVatBuyFee() != null && !ValidationUtil.isValidMoney(request.getVatBuyFee()) ? "vatBuyFee"
				: (request.getVatLogFee() != null && !ValidationUtil.isValidMoney(request.getVatLogFee()) ? "vatLogFee"
				: (request.getVatSellFee() != null && !ValidationUtil.isValidMoney(request.getVatSellFee()) ? "vatSellFee"
				: (request.getAdditionalSellFeePerTicket() != null && !ValidationUtil.isValidMoney(request.getAdditionalSellFeePerTicket()) ? "additionalSellFeePerTicket": null)))))))))))))))))))))));
		
		if (invalidArgument != null){
			log.error("api_domain=" + api_domain + " api_resource=orders" + " api_method=validateCreateSubOrderRequest" +
					" status=error error_message=missing or invalid required argument: " + invalidArgument);
			errorList.add(new Error(ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT, "missing or invalid required argument", invalidArgument));
			return errorList;
		}
		return null;
	}
}
