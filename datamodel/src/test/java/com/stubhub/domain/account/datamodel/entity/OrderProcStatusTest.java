package com.stubhub.domain.account.datamodel.entity;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class OrderProcStatusTest {

	private OrderProcStatus orderProcStatus;
	
	@BeforeTest
	public void setUp() {
		orderProcStatus = new OrderProcStatus();
	}

	@Test
	public void testOrderProcStatus(){
	    Long orderProcStatusCode = 2000L;
	    String orderProcStatusDesc = "Confirmed";
	    Long orderProcSubStatusCode = 5L;
	    String orderProcSubStatusDesc = "confirmed: CSR";
	    String orderProcStatusEffDate = "2012-03-05T20:03:12+0000";
	    orderProcStatus.setStatusCode(orderProcStatusCode);
	    orderProcStatus.setStatusDescription(orderProcStatusDesc);
	    orderProcStatus.setSubStatusCode(orderProcSubStatusCode);
	    orderProcStatus.setSubStatusDescription(orderProcSubStatusDesc);
	    orderProcStatus.setStatusEffectiveDate(orderProcStatusEffDate);
		Assert.assertEquals(orderProcStatus.getStatusCode(), orderProcStatusCode);
		Assert.assertEquals(orderProcStatus.getStatusDescription(), orderProcStatusDesc);
		Assert.assertEquals(orderProcStatus.getSubStatusCode(), orderProcSubStatusCode);
		Assert.assertEquals(orderProcStatus.getSubStatusDescription(), orderProcSubStatusDesc);
		Assert.assertEquals(orderProcStatus.getStatusEffectiveDate(), orderProcStatusEffDate);
	}
}
