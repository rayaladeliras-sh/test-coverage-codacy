package com.stubhub.domain.account.biz.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.stubhub.domain.account.datamodel.dao.AppliedCreditMemoDAO;
import com.stubhub.domain.account.datamodel.entity.AppliedCreditMemoDO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.stubhub.domain.account.biz.intf.CreditMemoBO;
import com.stubhub.domain.account.common.enums.ErrorCode;
import com.stubhub.domain.account.common.enums.ErrorType;
import com.stubhub.domain.account.common.exception.AccountException;
import com.stubhub.domain.account.datamodel.dao.SellerPaymentsDAO;
import com.stubhub.domain.account.datamodel.entity.SellerPaymentStatusEnum;
import com.stubhub.domain.account.datamodel.entity.SellerPayments;
import com.stubhub.domain.account.intf.CreditMemoResponse;
import com.stubhub.domain.account.intf.CreditMemosResponse;
import com.stubhub.newplatform.common.entity.Money;
import com.stubhub.newplatform.common.util.DateUtil;
import org.springframework.util.CollectionUtils;

@Component("creditMemoBO")
public class CreditMemoBOImpl implements CreditMemoBO {

	@Autowired
	private SellerPaymentsDAO sellerPaymentsDAO;

	@Autowired
	private AppliedCreditMemoDAO appliedCreditMemoDAO;

	@Autowired
	private EventUtil eventUtil;

	private static String creditMemoRecordType = "Credit Memo";
	private final static Logger log = LoggerFactory.getLogger(CreditMemoBOImpl.class);
	@Override
	public CreditMemosResponse getSellerCreditMemos(Long sellerId, String sort, String createdFromDate, String createdToDate, Integer start, Integer rows, String currencyCode) {
		CreditMemosResponse creditMemosResponse = new CreditMemosResponse();
		if (sellerId == null) {
			log.error("Invalid sellerId=" + sellerId);
			com.stubhub.domain.account.common.Error error = new com.stubhub.domain.account.common.Error(ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT, "SellerId is null", "sellerId");
			throw new AccountException(error);
		}
		
		
		//Set default values for from and to dates
		Calendar createdFrom =  null;
		Calendar createdTo = null;
				
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if(StringUtils.trimToNull(createdFromDate) != null && dateFormat.parse(createdFromDate) != null) {
				createdFrom = Calendar.getInstance();
				createdFrom.setTime(dateFormat.parse(createdFromDate));
			}
			if(StringUtils.trimToNull(createdToDate) != null && dateFormat.parse(createdToDate) != null) {
				createdTo = Calendar.getInstance();
				createdTo.setTime(dateFormat.parse(createdToDate));
			}
			if(createdFrom != null && createdTo != null && createdFrom.after(createdTo)) {
				log.warn("From date is greater than to date - date filters will be ignored");
				createdFrom = null;
				createdTo = null;
			}
		} catch (ParseException e) {
			log.warn("Invalid dates in the input - date filters will be ignored");
			createdFrom = null;
			createdTo = null;
		}

		// Get Seller payment records for given sellerId and recordType = "Credit Memo"
		List<SellerPayments> sellerPaymentList = sellerPaymentsDAO.getSellerPaymentsBySellerId(sellerId, creditMemoRecordType, sort, createdFrom, createdTo, start, rows, currencyCode);
		List<CreditMemoResponse> creditMemoResponseList = new ArrayList<CreditMemoResponse>();
		// Populate CreditMemoResponse
		if(sellerPaymentList != null) {
			for (SellerPayments sellerPayments : sellerPaymentList) {
				// Populate CreditMemoResponse
				CreditMemoResponse creditMemoResponse = populateCreditMemoResponse(sellerPayments);
				creditMemoResponseList.add(creditMemoResponse);
			}
			populateEventName(creditMemoResponseList);
		}

		//Sort CreditMemo
		if(StringUtils.trimToNull(sort) != null && !sort.equalsIgnoreCase("CREATEDDATE DESC") && creditMemoResponseList != null && !creditMemoResponseList.isEmpty()) {
			CreditMemoComparator comparator = new CreditMemoComparator();
			comparator.setSort(sort);
			Collections.sort(creditMemoResponseList, comparator);				
		}
		
		creditMemosResponse.setCreditMemos(creditMemoResponseList);	
		creditMemosResponse.setTotalCount(sellerPaymentsDAO.countSellerPaymentsBySellerId(sellerId, creditMemoRecordType, createdFrom, createdTo, currencyCode));

		return creditMemosResponse;
	}

    private void populateEventName(List<CreditMemoResponse> creditMemoResponseList) {
        Set<Long> ids = new HashSet<Long>();
        for (CreditMemoResponse response : creditMemoResponseList) {
            if (response.getEventId() != null) {
                ids.add(Long.parseLong(response.getEventId()));
            }
        }
        Map<Long, String> names = eventUtil.getEventNames(ids);
        for (CreditMemoResponse response : creditMemoResponseList) {
            if (response.getEventId() != null) {
                response.setEventName(names.get(Long.parseLong(response.getEventId())));
            }
        }
    }

	/**
	 * populateCreditMemoResponse
	 * @param sellerPayments
	 * @return CreditMemoResponse
	 */
	private CreditMemoResponse populateCreditMemoResponse(SellerPayments sellerPayments){
		CreditMemoResponse creditMemoResponse = new CreditMemoResponse();
		creditMemoResponse.setCreditAmount(new Money(sellerPayments.getAmount().toString(), sellerPayments.getCurrencyCode()));
		creditMemoResponse.setBookOfBusinessId(sellerPayments.getBobId().toString());
		//Parse DateAdded as createdDate in UTC
		creditMemoResponse.setCreatedDate(formatCalendarToUTCString(sellerPayments.getDateAdded()));
		if(sellerPayments.getAppliedDate()!=null){
			creditMemoResponse.setAppliedDate(formatCalendarToUTCString(sellerPayments.getAppliedDate()));
		}
		//Check if eventDate is a local to US. If yes, parse eventDateLocal as eventDate in response
		TimeZone localTimeZone = getTimeZone(sellerPayments.getEventDateLocal());
		if(localTimeZone != null){
			creditMemoResponse.setEventDate(formatDateString(sellerPayments.getEventDateLocal(), localTimeZone));
		}else if(sellerPayments.getEventDate() != null){
			//EventDate is not local. Set UTC date as eventDate
			creditMemoResponse.setEventDate(formatCalendarToUTCString(sellerPayments.getEventDate()));
		}
		
		if(sellerPayments.getEventId() != null) {
			creditMemoResponse.setEventId(sellerPayments.getEventId().toString());
		}
		
		creditMemoResponse.setId(sellerPayments.getId().toString());
		creditMemoResponse.setOrderId(sellerPayments.getOrderId().toString());
		creditMemoResponse.setOrderStatus(sellerPayments.getOrderStatus());
		creditMemoResponse.setReason(sellerPayments.getReasonDescription());
		creditMemoResponse.setReferenceNumber(sellerPayments.getReferenceNumber());
		if(sellerPayments.getSellerPaymentStatusId() != null) {
			creditMemoResponse.setStatus(SellerPaymentStatusEnum.getSellerPaymentStatusEnum(sellerPayments.getSellerPaymentStatusId()));
		}
		return creditMemoResponse;
	}
	
	
	
	/**
	 * populateCreditMemoResponse
	 * @param sellerPayments
	 * @return CreditMemoResponse
	 */
	private CreditMemoResponse populateCreditMemoResponseIndy(SellerPayments sellerPayments){
		CreditMemoResponse creditMemoResponse = new CreditMemoResponse();
		creditMemoResponse.setCreditAmount(new Money(sellerPayments.getAmount().toString(), sellerPayments.getCurrencyCode()));
		creditMemoResponse.setBookOfBusinessId(sellerPayments.getBobId().toString());
		if(sellerPayments.getSellerId()!=null)
		{
		creditMemoResponse.setSellerId(sellerPayments.getSellerId().toString());
		}
		//Parse DateAdded as createdDate in UTC
		creditMemoResponse.setCreatedDate(formatCalendarToUTCString(sellerPayments.getDateAdded()));
		if(sellerPayments.getAppliedDate()!=null){
			creditMemoResponse.setAppliedDate(formatCalendarToUTCString(sellerPayments.getAppliedDate()));
		}
		//Check if eventDate is a local to US. If yes, parse eventDateLocal as eventDate in response
		TimeZone localTimeZone = getTimeZone(sellerPayments.getEventDateLocal());
		if(localTimeZone != null){
			creditMemoResponse.setEventDate(formatDateString(sellerPayments.getEventDateLocal(), localTimeZone));
		}else if(sellerPayments.getEventDate() != null){
			//EventDate is not local. Set UTC date as eventDate
			creditMemoResponse.setEventDate(formatCalendarToUTCString(sellerPayments.getEventDate()));
		}
		
		if(sellerPayments.getEventId() != null) {
			creditMemoResponse.setEventId(sellerPayments.getEventId().toString());
		}
		
		creditMemoResponse.setId(sellerPayments.getId().toString());
		creditMemoResponse.setOrderId(sellerPayments.getOrderId().toString());
		creditMemoResponse.setOrderStatus(sellerPayments.getOrderStatus());
		String desc = sellerPayments.getReasonDescription();
		creditMemoResponse.setReasonCode(desc.substring(0, desc.indexOf("-")));
		creditMemoResponse.setReason(desc.substring(desc.indexOf("-")+1));
		creditMemoResponse.setReferenceNumber(sellerPayments.getReferenceNumber());
		if(sellerPayments.getSellerPaymentStatusId() != null) {
			creditMemoResponse.setStatus(SellerPaymentStatusEnum.getSellerPaymentStatusEnum(sellerPayments.getSellerPaymentStatusId()));
		}
		return creditMemoResponse;
	}
	/**
	 * formatCalendar to string in "yyyy-MM-dd'T'HH:mm:ssZ" format and UTC as timeZone
	 * @param cal
	 * @return String
	 */
	private String formatCalendarToUTCString(Calendar cal) {	
	    if (cal == null) {
	        return null;
	    }
		TimeZone tz = TimeZone.getTimeZone("UTC");
		String dateFormat = "yyyy-MM-dd'T'HH:mm:ssZ";					
		Calendar newcal = DateUtil.convertCalendarToNewTimeZone(cal, tz);	
		SimpleDateFormat sf = new SimpleDateFormat(dateFormat);
		sf.setTimeZone(tz);
		return sf.format(newcal.getTime());
	}
	
	/**
	 * Format dateString "MM/dd/yyyy HH:mm:ss (z)" to new format "yyyy-MM-dd'T'HH:mm:ssZ"
	 * 
	 * @param dateString
	 * @param TimeZone
	 * @return String
	 * @throws ParseException
	 */
	private String formatDateString(String dateString, TimeZone tz) {
	    if (dateString == null) {
	        return null;
	    }
		String format = "MM/dd/yyyy HH:mm:ss (z)";
		String newFormat = "yyyy-MM-dd'T'HH:mm:ssZ";	
				
		SimpleDateFormat df = new SimpleDateFormat(format);
		df.setTimeZone(tz);
		try {
			Date date = df.parse(dateString);
			SimpleDateFormat newDf = new SimpleDateFormat(newFormat);
			newDf.setTimeZone(tz);
			return newDf.format(date);
		} catch (Exception e) {
			log.error("Unable to parse the date string - dateString=" + dateString);
		}
		return null;
	}
	
	/**
	 * getTimeZone parses the giveDateString in "MM/dd/yyyy HH:mm:ss (z)" format
	 * to check if the timezone is local to US.
	 * If yes returns TimeZone
	 * @param dateString
	 * @return TimeZone
	 */
	private TimeZone getTimeZone(String dateString){
		if(dateString != null){
			String tz = dateString.substring(dateString.indexOf("(") + 1, dateString.indexOf("(") + 4);		
			//Map of US local time zones.
			Map<String, String> tzMap = new HashMap<String, String>();
			tzMap.put("PST", "PST8PDT");
			tzMap.put("PDT", "PST8PDT");
			tzMap.put("MST", "MST7MDT");
			tzMap.put("MDT", "MST7MDT");
			tzMap.put("CST", "CST6CDT");
			tzMap.put("CDT", "CST6CDT");
			tzMap.put("EST", "EST5EDT");
			tzMap.put("EDT", "EST5EDT");
			
			Set<String> tzSet = new HashSet<String>();
			tzSet = tzMap.keySet();
			if(tzSet.contains(tz)) {
				return TimeZone.getTimeZone(tzMap.get(tz));
			}
		}
		return null;
	}

	@Override
	public CreditMemosResponse getSellersCreditMemos(
			String createdFromDate, String createdToDate) {
		CreditMemosResponse creditMemosResponse = new CreditMemosResponse();
		

		//Set default values for from and to dates
		Calendar createdFrom =  null;
		Calendar createdTo = null;
				
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if(StringUtils.trimToNull(createdFromDate) != null && dateFormat.parse(createdFromDate) != null) {
				createdFrom = Calendar.getInstance();
				createdFrom.setTime(dateFormat.parse(createdFromDate));
			}
			if(StringUtils.trimToNull(createdToDate) != null && dateFormat.parse(createdToDate) != null) {
				createdTo = Calendar.getInstance();
				createdTo.setTime(dateFormat.parse(createdToDate));
			}
			if(createdFrom != null && createdTo != null && createdFrom.after(createdTo)) {
				log.warn("From date is greater than to date - date filters will be ignored");
				createdFrom = null;
				createdTo = null;
			}
		} catch (ParseException e) {
			log.warn("Invalid dates in the input - date filters will be ignored");
			createdFrom = null;
			createdTo = null;
		}

		// TODO Auto-generated method stub
		List<SellerPayments> sellerPaymentList = sellerPaymentsDAO.getSellerPaymentsIndy(createdFrom, createdTo);
		
		// Get Multiple Seller payment records 
	
		List<CreditMemoResponse> creditMemoResponseList = new ArrayList<CreditMemoResponse>();
		// Populate CreditMemoResponse
		if(sellerPaymentList != null) {
			for (SellerPayments sellerPayments : sellerPaymentList) {
				// Populate CreditMemoResponse
				CreditMemoResponse creditMemoResponse = populateCreditMemoResponseIndy(sellerPayments);
				creditMemoResponseList.add(creditMemoResponse);
			}
			populateEventName(creditMemoResponseList);
		}

		creditMemosResponse.setCreditMemos(creditMemoResponseList);	
		return creditMemosResponse;
	}

	@Override
	public Map<Long, BigDecimal> getAppliedAmountByPID(List<Long> pids) {
		return appliedCreditMemoDAO.findAppliedAmountByAppliedPaymentId(pids);
	}


}
