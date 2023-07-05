package com.stubhub.domain.account.intf;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class BuyerContactRequestTest {
	private BuyerContactRequest buyerContactRequest;
	
	@BeforeTest
	public void setUp() {
		buyerContactRequest = new BuyerContactRequest();
	}
	
	@Test
	public void testBuyerContactRequest(){
		String id = "200";
	    String csrRepresentative = "BJ";
		buyerContactRequest.setContactId(id);
		buyerContactRequest.setCsrRepresentative("BJ");

		Assert.assertEquals(buyerContactRequest.getContactId(), id);
		Assert.assertEquals(buyerContactRequest.getCsrRepresentative(), csrRepresentative);
	}

}
