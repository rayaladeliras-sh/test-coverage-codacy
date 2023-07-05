package com.stubhub.domain.account.intf;

import java.util.Arrays;
import java.util.Collections;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class SellerPaymentTest {

	private SellerPayment sellerPayment;

	@BeforeTest
	public void setUp() {
		sellerPayment = new SellerPayment();
	}

	@Test
	public void testSellerPayment() {
		sellerPayment.setBankName("BOA");
		sellerPayment.setBookOfBusinessID("1");
		sellerPayment.setCmApplied(false);
		sellerPayment.setDisbursementOptionID("123");
		sellerPayment.setErrors(Arrays.asList(new com.stubhub.domain.account.common.Error()));
		sellerPayment.setEventName("test");
		sellerPayment.setID("130336474");
		sellerPayment.setLastFourDigits("8790");
		sellerPayment.setOrderID("130336474");
		sellerPayment.setPayeeEmailID("33056366@testmail.com");
		sellerPayment.setPayeeName("33056366");
		sellerPayment.setPaymentAmount(new com.stubhub.newplatform.common.entity.Money("100"));
		sellerPayment.setPaymentDate("2015-08-21");
		sellerPayment.setPaymentInitiatedDate("2015-08-21");
		sellerPayment.setPaymentMode("ACH");
		sellerPayment.setPaymentTermId("2");
		sellerPayment.setPaymentTypeId("5");
		sellerPayment.setPaypalEmail("33056366@testmail.com");
		sellerPayment.setReferenceNumber("0317000002");
		sellerPayment.setSellerPaymentStatus("26");
		sellerPayment.setSellerPayoutAmount(new com.stubhub.newplatform.common.entity.Money("100"));
		sellerPayment.setStatus("test");

		// test
		Assert.assertEquals(sellerPayment.getBankName(), "BOA");
		Assert.assertEquals(sellerPayment.getBookOfBusinessID(), "1");
		Assert.assertEquals(sellerPayment.isCmApplied(), false);
		Assert.assertEquals(sellerPayment.getDisbursementOptionID(), ("123"));
		Assert.assertNotNull(sellerPayment.getErrors());
		Assert.assertEquals(sellerPayment.getEventName(), "test");
		Assert.assertEquals(sellerPayment.getID(), "130336474");
		Assert.assertEquals(sellerPayment.getLastFourDigits(), "8790");
		Assert.assertEquals(sellerPayment.getOrderID(), "130336474");
		Assert.assertEquals(sellerPayment.getPayeeEmailID(), "33056366@testmail.com");
		Assert.assertEquals(sellerPayment.getPayeeName(), "33056366");
		Assert.assertEquals(sellerPayment.getPaymentAmount(), new com.stubhub.newplatform.common.entity.Money("100"));
		Assert.assertEquals(sellerPayment.getPaymentDate(), "2015-08-21");
		Assert.assertEquals(sellerPayment.getPaymentInitiatedDate(), "2015-08-21");
		Assert.assertEquals(sellerPayment.getPaymentMode(), "ACH");
		Assert.assertEquals(sellerPayment.getPaymentTermId(), "2");
		Assert.assertEquals(sellerPayment.getPaymentTypeId(), "5");
		Assert.assertEquals(sellerPayment.getPaypalEmail(), "33056366@testmail.com");
		Assert.assertEquals(sellerPayment.getReferenceNumber(), "0317000002");
		Assert.assertEquals(sellerPayment.getSellerPaymentStatus(), "26");
		Assert.assertEquals(sellerPayment.getSellerPayoutAmount(), new com.stubhub.newplatform.common.entity.Money(
				"100"));
		Assert.assertEquals(sellerPayment.getStatus(), "test");
        
        SellerPayments sp = new SellerPayments();
        
        sp.setCurrencySummary(null);
        sp.getCurrencySummary();
        sp.setCurrencySummary(Collections.<CurrencySummary>emptyList());
        sp.getCurrencySummary();
        sp.setCurrencySummary(Arrays.asList(new CurrencySummary()));
        sp.getCurrencySummary();
	}
}
