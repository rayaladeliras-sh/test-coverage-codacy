package com.stubhub.domain.account.intf;

import java.util.ArrayList;
import java.util.List;

import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.stubhub.domain.account.datamodel.entity.OrderProcStatus;

public class OrderStatusResponseTest {
	private OrderStatusResponse orderStatusResponse;
	
	@BeforeTest
	public void setUp() {
		orderStatusResponse = new OrderStatusResponse();
	}

	@Test
	public void testOrderProcStatus(){
		List<OrderProcStatus> orderProc = new ArrayList<OrderProcStatus>();
		OrderProcStatus orderProcStatus = Mockito.mock(OrderProcStatus.class);
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
	    orderProc.add(orderProcStatus);
	    orderStatusResponse.setOrderProc(orderProc);
	    orderStatusResponse.setOrderId("12345");
		Assert.assertEquals(orderStatusResponse.getOrderProc(), orderProc);
		Assert.assertEquals(orderStatusResponse.getOrderId(), "12345");
	}
}
