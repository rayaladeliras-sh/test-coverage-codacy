package com.stubhub.domain.account.intf;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class TransactionSummaryRequestTest {
	private TransactionSummaryRequest transactionSummaryRequest;

	@BeforeTest
	public void setUp() {
		transactionSummaryRequest = new TransactionSummaryRequest();
	}

	@Test
	public void testBuyerContactRequest(){
		String proxiedId = "111";
		String buyerFlip = "true";
		transactionSummaryRequest.setProxiedId(proxiedId);
		transactionSummaryRequest.setBuyerFlip(buyerFlip);
		Assert.assertEquals(transactionSummaryRequest.getProxiedId(), proxiedId);
		Assert.assertEquals(transactionSummaryRequest.getBuyerFlip(), buyerFlip);
	}
}
