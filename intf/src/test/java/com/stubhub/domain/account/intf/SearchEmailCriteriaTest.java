package com.stubhub.domain.account.intf;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class SearchEmailCriteriaTest {
	private SearchEmailCriteria sc;
	
	@BeforeTest
	public void setUp() {
		sc = new SearchEmailCriteria();
	}
	
	@Test
	public void testEmailLogsResponse(){
		String fromDate = "2015-02-02";
		String orderId = "1234";
		String rows = "2";
		String start = "1";
		String subject = "welcome";
		String toDate = "2015-02-04";
		sc.setFromDate(fromDate);
		sc.setOrderId(orderId);
		sc.setRows(rows);
		sc.setStart(start);
		sc.setSubject(subject);
		sc.setToDate(toDate);
		
		Assert.assertEquals(sc.getFromDate(), fromDate);
		Assert.assertEquals(sc.getOrderId(), orderId);
		Assert.assertEquals(sc.getRows(), rows);
		Assert.assertEquals(sc.getStart(), start);
		Assert.assertEquals(sc.getSubject(), subject);
		Assert.assertEquals(sc.getToDate(), toDate);
	}
}