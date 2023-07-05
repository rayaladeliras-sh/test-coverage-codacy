package com.stubhub.domain.account.intf;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class CSOrdersRequestTest {
	private CSOrderDetailsRequest csOrderDetailsRequest;

	@BeforeTest
	public void setUp() {
		csOrderDetailsRequest = new CSOrderDetailsRequest();
	}

	@Test
	public void testBuyerContactRequest(){
		String orderId = "11";
		String proxiedId = "111";
		String start = "0";
		String row = "5";
		String eventStartDate = "2014-09-09";
		String eventEndDate = "2014-09-09";
		csOrderDetailsRequest.setOrderId(orderId);
		csOrderDetailsRequest.setProxiedId(proxiedId);
		csOrderDetailsRequest.setStart(start);
		csOrderDetailsRequest.setRow(row);
		csOrderDetailsRequest.setEventStartDate(eventStartDate);
		csOrderDetailsRequest.setEventEndDate(eventEndDate);


		Assert.assertEquals(csOrderDetailsRequest.getOrderId(), orderId);
		Assert.assertEquals(csOrderDetailsRequest.getProxiedId(), proxiedId);
		Assert.assertEquals(csOrderDetailsRequest.getStart(), start);
		Assert.assertEquals(csOrderDetailsRequest.getRow(), row);	
		Assert.assertEquals(csOrderDetailsRequest.getEventStartDate(), eventStartDate);
		Assert.assertEquals(csOrderDetailsRequest.getEventEndDate(), eventEndDate);
	}
}
