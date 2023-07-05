package com.stubhub.domain.account.intf;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class CSSaleDetailsRequestTest {
	private CSSaleDetailsRequest csSaleDetailsRequest;

	@BeforeTest
	public void setUp() {
		csSaleDetailsRequest = new CSSaleDetailsRequest();
	}

	@Test
	public void testSaleDetailsRequest(){
		String saleId = "11";
		String proxiedId = "111";
		String start = "0";
		String row = "5";
		String eventStartDate = "2014-09-09";
		String eventEndDate = "2014-09-09";
		csSaleDetailsRequest.setSaleId(saleId);
		csSaleDetailsRequest.setProxiedId(proxiedId);
		csSaleDetailsRequest.setStart(start);
		csSaleDetailsRequest.setRow(row);
		csSaleDetailsRequest.setEventStartDate(eventStartDate);
		csSaleDetailsRequest.setEventEndDate(eventEndDate);


		Assert.assertEquals(csSaleDetailsRequest.getSaleId(), saleId);
		Assert.assertEquals(csSaleDetailsRequest.getProxiedId(), proxiedId);
		Assert.assertEquals(csSaleDetailsRequest.getStart(), start);
		Assert.assertEquals(csSaleDetailsRequest.getRow(), row);	
		Assert.assertEquals(csSaleDetailsRequest.getEventStartDate(), eventStartDate);
		Assert.assertEquals(csSaleDetailsRequest.getEventEndDate(), eventEndDate);
	}
}
