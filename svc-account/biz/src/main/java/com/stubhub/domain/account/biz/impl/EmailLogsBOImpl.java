package com.stubhub.domain.account.biz.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.stubhub.domain.account.biz.intf.EmailLogsBO;
import com.stubhub.domain.account.datamodel.dao.EmailLogsDAO;
import com.stubhub.domain.account.datamodel.entity.EmailLog;
import com.stubhub.domain.account.intf.SearchEmailCriteria;

@Component("emailLogsBO")
public class EmailLogsBOImpl implements EmailLogsBO{

	@Autowired
	@Qualifier("emailLogsDAO")
	private EmailLogsDAO emailLogsDAO;

	public EmailLog getEmailById(Long emailId) {
		return emailLogsDAO.getEmailById(emailId);		
	}
	
	public List<EmailLog> getEmailLogs(Long userId, SearchEmailCriteria searchCriteria){ 
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		Calendar fromDate, toDate;
		
		try {
			if(searchCriteria.getFromDate() != null && dateFormat.parse(searchCriteria.getFromDate()) != null) {
				fromDate = Calendar.getInstance();
				fromDate.setTime(dateFormat.parse(searchCriteria.getFromDate()));
			} else {
				fromDate = Calendar.getInstance();
				fromDate.add(Calendar.DAY_OF_YEAR, -30);
			}
			if(searchCriteria.getToDate() != null && dateFormat.parse(searchCriteria.getToDate()) != null) {
				toDate = Calendar.getInstance();
				toDate.setTime(dateFormat.parse(searchCriteria.getToDate()));
				toDate.set(Calendar.HOUR_OF_DAY, 23);
				toDate.set(Calendar.MINUTE, 59);
				toDate.set(Calendar.SECOND, 59);
				toDate.set(Calendar.MILLISECOND, 0);
			} else{
				toDate = Calendar.getInstance();
				toDate.set(Calendar.HOUR_OF_DAY, 23);
				toDate.set(Calendar.MINUTE, 59);
				toDate.set(Calendar.SECOND, 59);
				toDate.set(Calendar.MILLISECOND, 0);
			}
		} catch (ParseException e) {
			fromDate = null;
			toDate = null;
		}

		String start = searchCriteria.getStart() != null ? searchCriteria.getStart() : "1";
		String rows = searchCriteria.getRows() != null ? searchCriteria.getRows() : "60";
		
		Boolean isExtended = Boolean.valueOf(searchCriteria.getIsExtended());
		Long orderId = (searchCriteria.getOrderId() != null) ? Long.valueOf(searchCriteria.getOrderId()) : null;
		String subject = (searchCriteria.getSubject() != null) ? "%" + searchCriteria.getSubject() + "%": null;
		Long buyerOrderId = (searchCriteria.getBuyerOrderId() != null) ? Long.valueOf(searchCriteria.getBuyerOrderId()) : null;
		
		List<EmailLog> emailLog = emailLogsDAO.getEmailLogsByUserIdAndCriteria(userId,orderId,buyerOrderId,subject, fromDate, toDate, start, rows);
		
		if (isExtended && StringUtils.isBlank(searchCriteria.getOrderId())
				&& StringUtils.isBlank(searchCriteria.getBuyerOrderId())) {
			
			List<EmailLog> userMessages = new ArrayList<EmailLog>();
			
			userMessages = emailLogsDAO.getUserMessagesByUserId(userId, fromDate, toDate, start, rows);
			if (userMessages != null && userMessages.size() > 0) 
			{
				if (emailLog != null && emailLog.size() > 0)
					emailLog.addAll(userMessages);
				else
					emailLog = userMessages;
			}
		}
		
		return emailLog;
		   
	}
}
