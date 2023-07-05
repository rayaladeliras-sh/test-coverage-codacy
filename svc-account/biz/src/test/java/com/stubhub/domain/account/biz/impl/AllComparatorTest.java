package com.stubhub.domain.account.biz.impl;

import java.util.Calendar;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.stubhub.domain.account.datamodel.entity.SellerCcTrans;
import com.stubhub.domain.account.intf.CreditMemoResponse;
import com.stubhub.newplatform.common.entity.Money;

public class AllComparatorTest {

	private SellerCcTransAmountComparator sellerCcTransAmountComparator;
	private SellerCcTransChargedDateComparator sellerCcTransChargedDateComparator;
	private SellerCcTransOrderIdComparator sellerCcTransOrderIdComparator;
	private CreditMemoComparator creditMemocomparator;
	
	@BeforeTest
	public void setUp(){
		sellerCcTransAmountComparator = new SellerCcTransAmountComparator();
		sellerCcTransChargedDateComparator = new SellerCcTransChargedDateComparator();
		sellerCcTransOrderIdComparator = new SellerCcTransOrderIdComparator();
		creditMemocomparator = new CreditMemoComparator();
	}
	
	@Test
	public void testSellerCcTransChargedDateComparator(){
		SellerCcTrans o1 = new SellerCcTrans();
		SellerCcTrans o2 = new SellerCcTrans();
				
		int result = sellerCcTransChargedDateComparator.compare(o1, o2);
		Assert.assertEquals(result, 0);
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 10);
		
		o1.setLastUpdatedDate(Calendar.getInstance());
		o2.setLastUpdatedDate(cal);
		result = sellerCcTransChargedDateComparator.compare(o1, o2);
		Assert.assertEquals(result, 1);
		
		o1.setLastUpdatedDate(cal);
		o2.setLastUpdatedDate(Calendar.getInstance());
		result = sellerCcTransChargedDateComparator.compare(o1, o2);
		Assert.assertEquals(result, -1);		
	}
	
	@Test
	public void testSellerCcTransAmountComparator(){
		SellerCcTrans o1 = new SellerCcTrans();
		SellerCcTrans o2 = new SellerCcTrans();

		int result = sellerCcTransAmountComparator.compare(o1, o2);
		Assert.assertEquals(result, 0);
		
		o1.setAmount(new Double(20));
		o2.setAmount(new Double(10));
		result = sellerCcTransAmountComparator.compare(o1, o2);
		Assert.assertEquals(result, -1);
		
		o1.setAmount(new Double(2));
		o2.setAmount(new Double(10));
		result = sellerCcTransAmountComparator.compare(o1, o2);
		Assert.assertEquals(result, 1);		
	}
	
	@Test
	public void testSellerCcTransOrderIdComparator(){
		SellerCcTrans o1 = new SellerCcTrans();
		SellerCcTrans o2 = new SellerCcTrans();

		int result = sellerCcTransOrderIdComparator.compare(o1, o2);
		Assert.assertEquals(result, 0);
		
		o1.setTid(10L);
		o2.setTid(20L);
		result = sellerCcTransOrderIdComparator.compare(o1, o2);
		Assert.assertEquals(result, 1);
		
		o1.setTid(20L);
		o2.setTid(10L);
		result = sellerCcTransOrderIdComparator.compare(o1, o2);
		Assert.assertEquals(result, -1);
	}
	
	@Test
	public void testCreditMemoComparator() {
		CreditMemoResponse o1 = new CreditMemoResponse();
		CreditMemoResponse o2 = new CreditMemoResponse();
		o1.setOrderId("123");
		o2.setOrderId("124");
		o1.setAppliedDate("2013-02-25T23:23:49+0000");
		o2.setAppliedDate("2013-02-26T23:23:49+0000");
		o1.setCreatedDate("2013-02-25T23:23:49+0000");
		o2.setCreatedDate("2013-02-26T23:23:49+0000");
		o1.setCreditAmount(new Money("1"));
		o2.setCreditAmount(new Money("2"));
		
		creditMemocomparator.setSort("ORDERID DESC");
		int result = creditMemocomparator.compare(o1, o2);
		Assert.assertEquals(result, 1);
		
		creditMemocomparator.setSort("AMOUNT DESC");
		result = creditMemocomparator.compare(o1, o2);
		Assert.assertEquals(result, 1);
		
		creditMemocomparator.setSort("APPLIEDDATE DESC");
		result = creditMemocomparator.compare(o1, o2);
		Assert.assertEquals(result, 1);
		
		creditMemocomparator.setSort("CREATEDDATE DESC");
		result = creditMemocomparator.compare(o1, o2);
		Assert.assertEquals(result, 1);
		
		creditMemocomparator.setSort("ORDERID ASC");
		result = creditMemocomparator.compare(o1, o2);
		Assert.assertEquals(result, -1);
		
		creditMemocomparator.setSort("AMOUNT ASC");
		result = creditMemocomparator.compare(o1, o2);
		Assert.assertEquals(result, -1);
		
		creditMemocomparator.setSort("APPLIEDDATE ASC");
		result = creditMemocomparator.compare(o1, o2);
		Assert.assertEquals(result, -1);
		
		creditMemocomparator.setSort("CREATEDDATE ASC");
		result = creditMemocomparator.compare(o1, o2);
		Assert.assertEquals(result, -1);
	}
	
}
