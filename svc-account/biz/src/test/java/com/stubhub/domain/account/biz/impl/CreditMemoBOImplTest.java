package com.stubhub.domain.account.biz.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.text.ParseException;

import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.stubhub.domain.account.biz.intf.CreditMemoBO;
import com.stubhub.domain.account.common.exception.AccountException;
import com.stubhub.domain.account.datamodel.dao.SellerPaymentsDAO;
import com.stubhub.domain.account.datamodel.dao.impl.SellerPaymentsDAOImpl;
import com.stubhub.domain.account.datamodel.entity.SellerPaymentStatusHist;
import com.stubhub.domain.account.datamodel.entity.SellerPayments;
import com.stubhub.domain.account.intf.CreditMemosResponse;

public class CreditMemoBOImplTest {
	private CreditMemoBO creditMemoBO;
	private SellerPaymentsDAO sellerPaymentsDAO;	
	private EventUtil eventUtil;
	
	@BeforeTest
	public void setUp(){
		creditMemoBO = new CreditMemoBOImpl();
		sellerPaymentsDAO = Mockito.mock(SellerPaymentsDAOImpl.class);
		ReflectionTestUtils.setField(creditMemoBO, "sellerPaymentsDAO", sellerPaymentsDAO);
		eventUtil = Mockito.mock(EventUtil.class);
        ReflectionTestUtils.setField(creditMemoBO, "eventUtil", eventUtil);
	}
	
	
	@Test
	public void testGetSellerCreditMemos(){
		Long sellerId = 12345L;
		List<SellerPayments> sellerPaymentsList = getSellerPayments();
		SellerPaymentStatusHist sellerPaymentStatusHist = getSellerPaymentStatusHist();
		Mockito.when(sellerPaymentsDAO.getSellerPaymentsBySellerId(Mockito.anyLong(), Mockito.anyString(), Mockito.anyString(), (Calendar)Mockito.anyObject(),  (Calendar)Mockito.anyObject(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString())).thenReturn(sellerPaymentsList);
		Map<Long, String> eventNames = new HashMap<Long, String>();
		eventNames.put(12345L, "event name");
		Mockito.when(eventUtil.getEventNames(Mockito.anySet())).thenReturn(eventNames);
		String sort = "APPLIEDDATE,DESC";
		String createdFromDate = "2012-10-10";
		String createdToDate = "2013-08-30";
		CreditMemosResponse response = creditMemoBO.getSellerCreditMemos(sellerId, sort, createdFromDate, createdToDate, null, null, null);
		Assert.assertNotNull(response);
		Assert.assertEquals(response.getCreditMemos().size(), 1);
		Assert.assertEquals(response.getCreditMemos().get(0).getEventName(), "event name");
	}
	
	
	@Test
	public void testGetSellerCreditMemosIndy(){
		
		List<SellerPayments> sellerPaymentsList = getSellerPayments();
	
		Mockito.when(sellerPaymentsDAO.getSellerPaymentsIndy((Calendar)Mockito.anyObject(),  (Calendar)Mockito.anyObject())).thenReturn(sellerPaymentsList);
	
		
		String createdFromDate = "2012-10-10";
		String createdToDate = "2013-08-30";
		CreditMemosResponse response = creditMemoBO.getSellersCreditMemos(createdFromDate, createdToDate);
		Assert.assertNotNull(response);
		Assert.assertEquals(response.getCreditMemos().size(), 1);
	
	}
	

	@Test
	public void testGetSellerCreditMemosIndyDefault(){
		try{
		
		List<SellerPayments> sellerPaymentsList = getSellerPayments();
	
		Mockito.when(sellerPaymentsDAO.getSellerPaymentsIndy((Calendar)Mockito.anyObject(),  (Calendar)Mockito.anyObject())).thenReturn(sellerPaymentsList);
	
		
		String createdFromDate = "2013-08-30";
		String createdToDate = "2012-10-10";
		CreditMemosResponse response = creditMemoBO.getSellersCreditMemos(createdFromDate, createdToDate);
		}
		catch(AccountException ae){
			Assert.assertTrue(true);
		}
	
	}
	
	@Test
	public void testGetSellerCreditMemosIndy_ParserException() throws ParseException{
		
		
		List<SellerPayments> sellerPaymentsList = getSellerPayments();
	
		Mockito.when(sellerPaymentsDAO.getSellerPaymentsIndy((Calendar)Mockito.anyObject(),  (Calendar)Mockito.anyObject())).thenReturn(sellerPaymentsList);
	
		
		String createdFromDate = "ansms";
		String createdToDate = "ansnsn";
		CreditMemosResponse response = creditMemoBO.getSellersCreditMemos(createdFromDate, createdToDate);
		Assert.assertNotNull(response);
	
	}
	
//	@Test
//	public void testGetSellerCreditMemos_appliedDateNull(){
//		Long sellerId = 12345L;
//		List<SellerPayments> sellerPaymentsList = getSellerPayments();
//		sellerPaymentsList.get(0).setAppliedDate(null);
//		Mockito.when(sellerPaymentsDAO.getSellerPaymentsBySellerId(Mockito.anyLong(), Mockito.anyString(), Mockito.anyString(), (Calendar)Mockito.anyObject(),  (Calendar)Mockito.anyObject())).thenReturn(sellerPaymentsList);
//		String sort = "APPLIEDDATE,DESC";
//		String createdFromDate = "2012-10-10";
//		String createdToDate = "2013-08-30";
//		CreditMemosResponse response = creditMemoBO.getSellerCreditMemos(sellerId, sort, createdFromDate, createdToDate);
//		Assert.assertNotNull(response);
//	}
	@Test
	public void testGetSellerCreditMemos_AccountException(){
		Long sellerId = 12345L;
		List<SellerPayments> sellerPaymentsList = getSellerPayments();
		sellerPaymentsList.get(0).setEventDateLocal("02/16/2014 (PST)");//Invalid date.Throws parseException
		SellerPaymentStatusHist sellerPaymentStatusHist = getSellerPaymentStatusHist();
		Mockito.when(sellerPaymentsDAO.getSellerPaymentsBySellerId(Mockito.anyLong(), Mockito.anyString(), Mockito.anyString(), (Calendar)Mockito.anyObject(),  (Calendar)Mockito.anyObject(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString())).thenReturn(sellerPaymentsList);
		
		try{
			String sort = "";
			String createdFromDate = "2013-10-10";
			String createdToDate = "2012-10-10";
			creditMemoBO.getSellerCreditMemos(sellerId, sort, createdFromDate, createdToDate, null, null, "USD");
		}catch(AccountException ae){
			Assert.assertTrue(true);
		}		
	}
	
	@Test
	public void testGetSellerCreditMemos_NonLocalEventDate(){
		Long sellerId = 12345L;
		List<SellerPayments> sellerPaymentsList = getSellerPayments();
		sellerPaymentsList.get(0).setEventDateLocal("02/16/2014 07:30:00 (IST)");
		SellerPaymentStatusHist sellerPaymentStatusHist = getSellerPaymentStatusHist();
		Mockito.when(sellerPaymentsDAO.getSellerPaymentsBySellerId(Mockito.anyLong(), Mockito.anyString(), Mockito.anyString(), (Calendar)Mockito.anyObject(),  (Calendar)Mockito.anyObject(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString())).thenReturn(sellerPaymentsList);
		String sort = "";
		String createdFromDate = "abc";
		String createdToDate = "123";
		CreditMemosResponse response = creditMemoBO.getSellerCreditMemos(sellerId, sort, createdFromDate, createdToDate, null, null, "GBP");
		Assert.assertNotNull(response);
	}
	
	@Test
	public void testGetSellerCreditMemos_NullInput(){
		Long sellerId = null;
		try{
			String sort = "";
			String createdFromDate = "";
			String createdToDate = "";
			creditMemoBO.getSellerCreditMemos(sellerId, sort, createdFromDate, createdToDate, null, null, null);
		}catch(AccountException e){
			Assert.assertNotNull(e);
			Assert.assertTrue(true);
		}		
	}
	
	@Test
	public void testGetSellerCreditMemos_NoPaymentRecords(){
		Long sellerId = 12345L;		
		Mockito.when(sellerPaymentsDAO.getSellerPaymentsBySellerId(Mockito.anyLong(), Mockito.anyString(), Mockito.anyString(), (Calendar)Mockito.anyObject(),  (Calendar)Mockito.anyObject(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString())).thenReturn(null);
		try{
			String sort = "";
			String createdFromDate = "";
			String createdToDate = "";
			creditMemoBO.getSellerCreditMemos(sellerId, sort, createdFromDate, createdToDate, null, null, "EUR");
		}catch(AccountException e){
			Assert.assertNotNull(e);
			Assert.assertTrue(true);
		}
	}
	
	private List<SellerPayments> getSellerPayments(){
		List<SellerPayments> sellerPaymentsList = new ArrayList<SellerPayments>();
		SellerPayments sellerPayments = new SellerPayments();
		sellerPayments.setAmount(1.0);
		sellerPayments.setCurrencyCode("USD");
		sellerPayments.setDateAdded(Calendar.getInstance());
		sellerPayments.setAppliedDate(Calendar.getInstance());
		sellerPayments.setEventDateLocal("02/16/2014 07:30:00 (PST)");
		sellerPayments.setEventDate(Calendar.getInstance());
		sellerPayments.setEventId(12345L);
		sellerPayments.setId(12345L);
		sellerPayments.setOrderId(12345L);
		sellerPayments.setOrderStatus("orderStatus");
		sellerPayments.setReasonDescription("22-reason");
		sellerPayments.setReferenceNumber("12345");
		sellerPayments.setSellerPaymentStatusId(12L);
		sellerPayments.setBobId(1L);
		sellerPaymentsList.add(sellerPayments);
		return sellerPaymentsList;
	}
	
	private SellerPaymentStatusHist getSellerPaymentStatusHist(){
		SellerPaymentStatusHist sellerPaymentStatusHist = new SellerPaymentStatusHist();
		sellerPaymentStatusHist.setSellerPaymentId(124L);
		sellerPaymentStatusHist.setSellerPaymentStatusEffDate(Calendar.getInstance());
		sellerPaymentStatusHist.setSellerPaymentStatusHistId(12345L);
		return sellerPaymentStatusHist;
	}
}
