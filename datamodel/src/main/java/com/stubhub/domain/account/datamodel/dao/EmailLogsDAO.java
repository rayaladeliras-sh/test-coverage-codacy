package com.stubhub.domain.account.datamodel.dao;

import java.util.Calendar;
import java.util.List;

import com.stubhub.domain.account.datamodel.entity.EmailLog;


public interface EmailLogsDAO {

	public EmailLog getEmailById(Long emailId);
	
	public List<EmailLog> getEmailLogsByUserIdAndCriteria(Long userId,Long orderId,Long buyerOrderId,String subject,Calendar fromDate, Calendar toDate, String start, String rows);

	public List<EmailLog> getUserMessagesByUserId(Long userId,Calendar fromDate, Calendar toDate, String start, String rows);

}
