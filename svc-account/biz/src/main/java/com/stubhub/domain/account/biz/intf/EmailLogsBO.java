package com.stubhub.domain.account.biz.intf;

import java.util.List;

import com.stubhub.domain.account.datamodel.entity.EmailLog;
import com.stubhub.domain.account.intf.SearchEmailCriteria;

public interface EmailLogsBO {
	
	public EmailLog getEmailById(Long emailId);
	
	public List<EmailLog> getEmailLogs(Long userId, SearchEmailCriteria searchCriteria);

}
