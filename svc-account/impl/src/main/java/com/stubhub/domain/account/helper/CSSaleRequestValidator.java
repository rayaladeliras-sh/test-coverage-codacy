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

import com.stubhub.domain.account.intf.CSSaleDetailsRequest;
import com.stubhub.domain.account.intf.OrdersResponse;
import com.stubhub.domain.account.intf.SearchEmailCriteria;
import com.stubhub.domain.account.intf.SubstitutionRequest;
import com.stubhub.domain.account.intf.TransactionSummaryRequest;
import com.stubhub.domain.infrastructure.soa.core.context.SHServiceContext;

@Component("csSaleRequestValidator")
public class CSSaleRequestValidator {
	private final static Log log = LogFactory.getLog(RequestValidator.class);
	private String api_domain = "account";

	public List<Error> validateCSSaleDetailsRequestFields(CSSaleDetailsRequest csSaleDetailsRequest) {
		String api_resource = "cssaledetails";
		String saleId = csSaleDetailsRequest.getSaleId();
		String proxiedId = csSaleDetailsRequest.getProxiedId();
		String eventStartDate = csSaleDetailsRequest.getEventStartDate();
		String eventEndDate = csSaleDetailsRequest.getEventEndDate();
		String start = csSaleDetailsRequest.getStart();
		String row = csSaleDetailsRequest.getRow();
		List<Error> errorList = new LinkedList<Error>();

		if(StringUtils.trimToNull(saleId) == null && (StringUtils.trimToNull(proxiedId) == null)){
			log.error("api_domain=" + api_domain + " api_resource=" + api_resource + " api_method=validatecsSaleDetailsRequestFields" +
					" status=error error_message=No input provided. Either saleId or proxiedId must be supplied." +
					" saleId=" + saleId + ", proxiedId=" + proxiedId);
			errorList.add(new Error(
					ErrorType.INPUTERROR, 
					ErrorCode.INVALID_INPUT, 
					"No input provided. Either saleId or proxiedId must be supplied", 
					"saleId=" + saleId + ", proxiedId=" + proxiedId));
			return errorList;
		}	
		if(saleId != null && proxiedId != null){
			log.error("api_domain=" + api_domain + " api_resource=" + api_resource + " api_method=validatecsSaleDetailsRequestFields" +
					" status=error error_message=Multiple params supplied; either saleId or proxiedId is required." +
					" saleId=" + saleId + ", proxiedId=" + proxiedId);
			errorList.add(new Error(
					ErrorType.INPUTERROR, 
					ErrorCode.INVALID_INPUT, 
					"Multiple params supplied; Either saleId or proxiedId is required", 
					"saleId=" + saleId + " ,proxiedId=" + proxiedId));
			return errorList;
		}
		if(saleId != null && !isValidLong("saleId", saleId)){
			log.error("api_domain=" + api_domain + " api_resource=" + api_resource + " api_method=validatecsSaleDetailsRequestFields" +
					" status=error error_message=Invalid input provided." +
					" saleId=" + saleId);
			errorList.add(new Error(
					ErrorType.INPUTERROR, 
					ErrorCode.INVALID_INPUT, 
					"Invalid input provided", 
					"saleId=" + saleId));
			return errorList;
		}
		if(proxiedId != null && !isValidLong("proxiedId", proxiedId)){
			log.error("api_domain=" + api_domain + " api_resource=" + api_resource + " api_method=validatecsSaleDetailsRequestFields" +
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
			log.error("api_domain=" + api_domain + " api_resource=" + api_resource + " api_method=validatecsSaleDetailsRequestFields" +
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
			log.error("api_domain=" + api_domain + " api_resource=" + api_resource + " api_method=validatecsSaleDetailsRequestFields" +
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
			log.error("api_domain=" + api_domain + " api_resource=" + api_resource + " api_method=validatecsSaleDetailsRequestFields" +
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
			log.error("api_domain=" + api_domain + " api_resource=" + api_resource + " api_method=validatecsSaleDetailsRequestFields" +
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
			log.error("api_domain=" + api_domain + " api_resource=" + api_resource + " api_method=validatecsSaleDetailsRequestFields" +
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
			log.error("api_domain=" + api_domain + " api_resource=" + api_resource + " api_method=validatecsSaleDetailsRequestFields" +
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

	private boolean isValidLong(String param, String value) {
		try {
			Long.parseLong(value.trim());
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	

	private boolean isValidDate(String param, String value) {
		String datePattern = "\\d{4}-\\d{1,2}-\\d{1,2}";
		return value.matches(datePattern);
	}

	private int compareDates(String eventStartDate, String eventEndDate) {
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

	
	
	
	
	
	
	
}
