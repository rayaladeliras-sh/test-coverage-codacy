package com.stubhub.domain.account.biz.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.stubhub.domain.account.biz.intf.EmailLogsBO;
import com.stubhub.domain.account.datamodel.dao.EmailLogsDAO;
import com.stubhub.domain.account.datamodel.dao.impl.EmailLogsDAOImpl;
import com.stubhub.domain.account.datamodel.entity.EmailLog;
import com.stubhub.domain.account.intf.SearchEmailCriteria;

public class EmailLogsBOImplTest {

	private EmailLogsBO emailLogsBO;
	private EmailLogsDAO emailLogsDAO;

	@BeforeTest
	public void setUp() {
		emailLogsBO = new EmailLogsBOImpl();
		emailLogsDAO = Mockito.mock(EmailLogsDAOImpl.class);
		ReflectionTestUtils.setField(emailLogsBO, "emailLogsDAO", emailLogsDAO);
	}

	@Test
	public void testGetEmailById(){
		EmailLog emailLog = new EmailLog();
		emailLog.setEmailId(1L);
		Mockito.when(emailLogsDAO.getEmailById(1L)).thenReturn(emailLog);		
		Assert.assertNotNull(emailLogsBO.getEmailById(1L));
	}
	
	@Test
	public void testGetEmailLogs(){
		SearchEmailCriteria sc = new SearchEmailCriteria();
		sc.setFromDate("2015-02-02");
		sc.setOrderId("1234");
		sc.setBuyerOrderId("5678");
		sc.setIsExtended("true");
		sc.setRows("2");
		sc.setStart("1");
		sc.setSubject("welcome");
		sc.setToDate("2015-02-04");
		Long user = 1L; 
		EmailLog emailLog = new EmailLog();
		emailLog.setAddressBcc("bijain@stubhub.com");
		emailLog.setAddressCc("bijain1@stubhub.com");
		emailLog.setAddressFrom("bijain2@stubhub.com");
		emailLog.setAddressTo("bijain3@stubhub.com");
		emailLog.setDateAdded(Calendar.getInstance());
		emailLog.setDateSent(Calendar.getInstance());
		emailLog.setEmailId(Long.valueOf("123456"));
		emailLog.setFormat("text");
		emailLog.setSubject("deliver the tickets");
		emailLog.settId("1234");
		emailLog.setbuyerOrderId("5678");
		emailLog.setUserId("12345");
		List<EmailLog> emailLogs = new ArrayList<EmailLog>();
		emailLogs.add(emailLog);
		
		Calendar fromDate = Calendar.getInstance();
		Calendar toDate = Calendar.getInstance();
		fromDate.add(Calendar.DAY_OF_YEAR, -30);
		
		Long orderId = null;
		Long buyerOrderId = null;
		
		if (sc.getOrderId() != null) orderId = Long.valueOf(sc.getOrderId());
		if (sc.getBuyerOrderId() != null) buyerOrderId = Long.valueOf(sc.getBuyerOrderId());
		
		
		Mockito.when(emailLogsDAO.getEmailLogsByUserIdAndCriteria(user,orderId,buyerOrderId,sc.getSubject(), fromDate, toDate, sc.getStart(),  sc.getRows())).thenReturn(emailLogs);
		Mockito.when(emailLogsDAO.getUserMessagesByUserId(user, fromDate, toDate, sc.getStart(),sc.getRows())).thenReturn(emailLogs);
		Assert.assertNotNull(emailLogsBO.getEmailLogs(user, sc));		
		Assert.assertEquals(emailLogs.size(), 1);
	}
}
