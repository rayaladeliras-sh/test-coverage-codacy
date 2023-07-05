package com.stubhub.domain.account.biz.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.stubhub.domain.account.biz.intf.CreditCardChargeBO;
import com.stubhub.domain.account.datamodel.dao.SellerCcTransDAO;
import com.stubhub.domain.account.datamodel.entity.SellerCcTrans;

public class CreditCardChargeBOTest {
	
	private CreditCardChargeBO creditCardChargeBO;
	private SellerCcTransDAO sellerCcTransDAO;
	
	@BeforeMethod
	public void setUp(){
		creditCardChargeBO = new CreditCardChargeBOImpl();
		sellerCcTransDAO = mock(SellerCcTransDAO.class);
		ReflectionTestUtils.setField(creditCardChargeBO, "sellerCcTransDAO", sellerCcTransDAO);
	}
	
	@Test
	public void testGetCreditCardCharges() {
		List<SellerCcTrans> sellerCcTransList = new ArrayList<SellerCcTrans>();
		SellerCcTrans sellerCcTrans = new SellerCcTrans();
		sellerCcTrans.setId(123L);
		sellerCcTrans.setTid(12345L);
		sellerCcTransList.add(sellerCcTrans);
		when(sellerCcTransDAO.getSellerCcTransBySellerCcId(Mockito.anySet(), Mockito.any(Calendar.class), Mockito.any(Calendar.class), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyList(), Mockito.anyString())).thenReturn(sellerCcTransList);
		Set<Long> sellerCcIds = new HashSet<Long>();
		sellerCcIds.add(123L);
		List<SellerCcTrans> ccCharges = creditCardChargeBO.getCreditCardCharges(sellerCcIds, null, null, null, null, null, null, "USD");
		Assert.assertNotNull(ccCharges);
	}
	
	@Test
	public void testGetCreditCardChargesWithDateRange() {
		List<SellerCcTrans> sellerCcTransList = new ArrayList<SellerCcTrans>();
		SellerCcTrans sellerCcTrans = new SellerCcTrans();
		sellerCcTrans.setId(123L);
		sellerCcTrans.setTid(12345L);
		sellerCcTransList.add(sellerCcTrans);
		when(sellerCcTransDAO.getSellerCcTransBySellerCcId(Mockito.anySet(), Mockito.any(Calendar.class), Mockito.any(Calendar.class), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyList(), Mockito.anyString())).thenReturn(sellerCcTransList);
		Set<Long> sellerCcIds = new HashSet<Long>();
		sellerCcIds.add(123L);
		Calendar from = Calendar.getInstance();
		from.set(Calendar.YEAR, 2013);
		from.set(Calendar.MONTH, Calendar.AUGUST);
		from.set(Calendar.DAY_OF_MONTH, 1);
		Calendar to = Calendar.getInstance();
		to.set(Calendar.YEAR, 2013);
		to.set(Calendar.MONTH, Calendar.SEPTEMBER);
		to.set(Calendar.DAY_OF_MONTH, 1);
		List<SellerCcTrans> ccCharges = creditCardChargeBO.getCreditCardCharges(sellerCcIds, null, from, to, null, null, null, null);
		Assert.assertNotNull(ccCharges);
	}
	
	@Test
	public void testGetCreditCardChargesSortByChargedDate() {
		List<SellerCcTrans> sellerCcTransList = new ArrayList<SellerCcTrans>();
		SellerCcTrans sellerCcTrans = new SellerCcTrans();
		sellerCcTrans.setId(123L);
		sellerCcTrans.setTid(12345L);
		sellerCcTransList.add(sellerCcTrans);
		when(sellerCcTransDAO.getSellerCcTransBySellerCcId(Mockito.anySet(), Mockito.any(Calendar.class), Mockito.any(Calendar.class), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyList(), Mockito.anyString())).thenReturn(sellerCcTransList);
		Set<Long> sellerCcIds = new HashSet<Long>();
		sellerCcIds.add(123L);
		Calendar from = Calendar.getInstance();
		from.set(Calendar.YEAR, 2013);
		from.set(Calendar.MONTH, Calendar.AUGUST);
		from.set(Calendar.DAY_OF_MONTH, 1);
		Calendar to = Calendar.getInstance();
		to.set(Calendar.YEAR, 2013);
		to.set(Calendar.MONTH, Calendar.SEPTEMBER);
		to.set(Calendar.DAY_OF_MONTH, 1);
		List<SellerCcTrans> ccCharges = creditCardChargeBO.getCreditCardCharges(sellerCcIds, "CHARGEDDATE ASC", from, to, null, null, null, null);
		Assert.assertNotNull(ccCharges);
	}
	
	@Test
	public void testGetCreditCardChargesSortByAmount() {
		List<SellerCcTrans> sellerCcTransList = new ArrayList<SellerCcTrans>();
		SellerCcTrans sellerCcTrans = new SellerCcTrans();
		sellerCcTrans.setId(123L);
		sellerCcTrans.setTid(12345L);
		sellerCcTransList.add(sellerCcTrans);
		when(sellerCcTransDAO.getSellerCcTransBySellerCcId(Mockito.anySet(), Mockito.any(Calendar.class), Mockito.any(Calendar.class), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyList(), Mockito.anyString())).thenReturn(sellerCcTransList);
		Set<Long> sellerCcIds = new HashSet<Long>();
		sellerCcIds.add(123L);
		Calendar from = Calendar.getInstance();
		from.set(Calendar.YEAR, 2013);
		from.set(Calendar.MONTH, Calendar.AUGUST);
		from.set(Calendar.DAY_OF_MONTH, 1);
		Calendar to = Calendar.getInstance();
		to.set(Calendar.YEAR, 2013);
		to.set(Calendar.MONTH, Calendar.SEPTEMBER);
		to.set(Calendar.DAY_OF_MONTH, 1);
		List<SellerCcTrans> ccCharges = creditCardChargeBO.getCreditCardCharges(sellerCcIds, "AMOUNT ASC", from, to, null, null, null, "USD");
		Assert.assertNotNull(ccCharges);
	}
	
	@Test
	public void testGetCreditCardChargesSortByOrderId() {
		List<SellerCcTrans> sellerCcTransList = new ArrayList<SellerCcTrans>();
		SellerCcTrans sellerCcTrans = new SellerCcTrans();
		sellerCcTrans.setId(123L);
		sellerCcTrans.setTid(12345L);
		sellerCcTransList.add(sellerCcTrans);
		when(sellerCcTransDAO.getSellerCcTransBySellerCcId(Mockito.anySet(), Mockito.any(Calendar.class), Mockito.any(Calendar.class), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyList(), Mockito.anyString())).thenReturn(sellerCcTransList);
		Set<Long> sellerCcIds = new HashSet<Long>();
		sellerCcIds.add(123L);
		Calendar from = Calendar.getInstance();
		from.set(Calendar.YEAR, 2013);
		from.set(Calendar.MONTH, Calendar.AUGUST);
		from.set(Calendar.DAY_OF_MONTH, 1);
		Calendar to = Calendar.getInstance();
		to.set(Calendar.YEAR, 2013);
		to.set(Calendar.MONTH, Calendar.SEPTEMBER);
		to.set(Calendar.DAY_OF_MONTH, 1);
		List<SellerCcTrans> ccCharges = creditCardChargeBO.getCreditCardCharges(sellerCcIds, "ORDERID ASC", from, to, null, null, null, null);
		Assert.assertNotNull(ccCharges);
	}
	
    @Test
    public void testGetCreditCardChargesCount() {
        List<SellerCcTrans> sellerCcTransList = new ArrayList<SellerCcTrans>();
        SellerCcTrans sellerCcTrans = new SellerCcTrans();
        sellerCcTrans.setId(123L);
        sellerCcTrans.setTid(12345L);
        sellerCcTransList.add(sellerCcTrans);
        when(sellerCcTransDAO.countSellerCcTransBySellerCcId(Mockito.anySet(), Mockito.any(Calendar.class), Mockito.any(Calendar.class), Mockito.anyList(), Mockito.anyString())).thenReturn(5L);
        Set<Long> sellerCcIds = new HashSet<Long>();
        sellerCcIds.add(123L);
        Assert.assertEquals(5L, creditCardChargeBO.getCreditCardChargesCount(sellerCcIds, null, null, null, null));
    }
}
