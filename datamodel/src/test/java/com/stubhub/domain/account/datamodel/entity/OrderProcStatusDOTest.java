package com.stubhub.domain.account.datamodel.entity;

import java.util.Calendar;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class OrderProcStatusDOTest {

	private OrderProcStatusDO orderProcStatusDO;
	
	@BeforeTest
	public void setUp() {
		orderProcStatusDO = new OrderProcStatusDO();
	}

	@Test
	public void testOrderProcStatusDO(){
		Long orderProcStatusId = 1L;
		Long tid = 2L;
		Long orderProcSubStatusCode = 3L;
		Calendar orderProcStatusEffDate = Calendar.getInstance();
		Calendar orderProcStatusEndDate = Calendar.getInstance();
		Calendar createdDate = Calendar.getInstance();
		Calendar lastUpdatedDate = Calendar.getInstance();
		String createdBy = "bijain";
		String lastUpdatedBy = "bijain";
	    orderProcStatusDO.setOrderProcStatusId(orderProcStatusId);
	    orderProcStatusDO.setTid(tid);
	    orderProcStatusDO.setOrderProcSubStatusCode(orderProcSubStatusCode);
	    orderProcStatusDO.setOrderProcStatusEffDate(orderProcStatusEffDate);
	    orderProcStatusDO.setOrderProcStatusEndDate(orderProcStatusEndDate);
	    orderProcStatusDO.setCreatedDate(createdDate);
	    orderProcStatusDO.setLastUpdatedBy(lastUpdatedBy);
	    orderProcStatusDO.setLastUpdatedDate(lastUpdatedDate);
	    orderProcStatusDO.setCreatedBy(createdBy);
		Assert.assertEquals(orderProcStatusDO.getOrderProcStatusId(), orderProcStatusId);
		Assert.assertEquals(orderProcStatusDO.getTid(), tid);
		Assert.assertEquals(orderProcStatusDO.getOrderProcSubStatusCode(), orderProcSubStatusCode);
		Assert.assertEquals(orderProcStatusDO.getOrderProcStatusEffDate(), orderProcStatusEffDate);
		Assert.assertEquals(orderProcStatusDO.getOrderProcStatusEndDate(), orderProcStatusEndDate);
		Assert.assertEquals(orderProcStatusDO.getCreatedDate(), createdDate);
		Assert.assertEquals(orderProcStatusDO.getLastUpdatedBy(), lastUpdatedBy);
		Assert.assertEquals(orderProcStatusDO.getLastUpdatedDate(), lastUpdatedDate);
		Assert.assertEquals(orderProcStatusDO.getCreatedBy(), createdBy);
	}
}
