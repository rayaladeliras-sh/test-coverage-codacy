package com.stubhub.domain.account.biz.impl;

import java.lang.reflect.Field;
import java.util.*;

import junit.framework.Assert;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.stubhub.domain.account.common.PaymentsSearchCriteria;
import com.stubhub.domain.account.datamodel.dao.SellerPaymentStatusHistDAO;
import com.stubhub.domain.account.datamodel.dao.SellerPaymentsDAO;
import com.stubhub.domain.account.datamodel.entity.SellerPayment;
import com.stubhub.domain.account.datamodel.entity.SellerPaymentStatusEnum;
import com.stubhub.domain.account.datamodel.entity.SellerPaymentStatusHist;
import com.stubhub.domain.account.datamodel.entity.SellerPayments;
import com.stubhub.domain.account.intf.UpdatePaymentStatusRequest;
import com.stubhub.newplatform.property.MasterStubHubProperties;

import static org.mockito.Mockito.*;

public class SellerPaymentBOTest {
	
	private SellerPaymentBOImpl sellerPaymentBO;
	private SellerPaymentsDAO sellerPaymentsDAO;
	private SellerPaymentStatusHistDAO sellerPaymentStatusHistDAO;
	private PaymentsSolrUtil paymentsSolrUtil;
	
	@BeforeMethod
	public void setUp() throws Exception {
		sellerPaymentBO = new SellerPaymentBOImpl();
		sellerPaymentsDAO = Mockito.mock(SellerPaymentsDAO.class);
		paymentsSolrUtil = Mockito.mock(PaymentsSolrUtil.class);
		sellerPaymentStatusHistDAO = Mockito.mock(SellerPaymentStatusHistDAO.class);
		ReflectionTestUtils.setField(sellerPaymentBO, "sellerPaymentsDAO", sellerPaymentsDAO);
		ReflectionTestUtils.setField(sellerPaymentBO, "paymentsSolrUtil", paymentsSolrUtil);
		ReflectionTestUtils.setField(sellerPaymentBO, "sellerPaymentStatusHistDAO", sellerPaymentStatusHistDAO);
		 Map<String, String> props = new HashMap<String, String>();
	        Field field = MasterStubHubProperties.class.getDeclaredField("props");
	        field.setAccessible(true);
	        field.set(null, props);
	        MasterStubHubProperties.setProperty("myaccount.payments.pending.status.ids", "1 2 3 5 7 8 9 15 16 17 20 21 23");
	        MasterStubHubProperties.setProperty("myaccount.payments.paid.status.ids", "10 12 14 22 24");
	        MasterStubHubProperties.setProperty("myaccount.payments.available.status.ids", "7");
	}
		
	@Test
	public void testGetSellerPayments(){
		List<SellerPayments> payments = new ArrayList<SellerPayments>();
		
		SellerPayments payment = new SellerPayments();
		payment.setId(12345l);
		payment.setAmount(100.12);
		payment.setBobId(1l);
		payment.setDateAdded(Calendar.getInstance());
		payment.setEventDateLocal(Calendar.getInstance().toString());
		payment.setEventId(8972342l);
		payment.setOrderId(23424324l);
		payment.setSellerPaymentStatusId(10l);
		payments.add(payment);
		
		Mockito.when(sellerPaymentsDAO.getSellerPayments(Mockito.anyLong(), Mockito.anyString())).thenReturn(payments);
		List<SellerPayments> spayments = sellerPaymentBO.getSellerPayments(12345);
		for(SellerPayments spayment: spayments){
			Assert.assertTrue(spayment.getId().equals(new Long(12345)));
			Assert.assertTrue(spayment.getOrderId().equals(new Long(23424324)));
			Assert.assertTrue(spayment.getAmount() == 100.12);
			Assert.assertTrue(spayment.getBobId().equals(new Long(1)));
			Assert.assertTrue(spayment.getEventId().equals(new Long(8972342)));
		}
	}
	
	@Test
	public void testGetSellerPaymentById(){
		SellerPayment payment = new SellerPayment();
		payment.setId(12345L);
		Mockito.when(sellerPaymentsDAO.getSellerPaymentById(payment.getId())).thenReturn(payment);
		
		SellerPayment out = sellerPaymentBO.getSellerPaymentById(payment.getId());
		Assert.assertEquals(out, payment);
	}

	@Test
	public void testFindSellerPaymentsByAction(){
		Long sellerId = 11111L;
		SellerPayment holdForDDPayment = new SellerPayment();
		holdForDDPayment.setId(12345L);
		List<SellerPayment> holdForDDList = new ArrayList<SellerPayment>();
		holdForDDList.add(holdForDDPayment);
		SellerPayment holdForManualPayment = new SellerPayment();
		holdForManualPayment.setId(54321L);
		List<SellerPayment> holdForManualList = new ArrayList<SellerPayment>();
		holdForManualList.add(holdForManualPayment);
		Mockito.when(sellerPaymentsDAO.findSellerPaymentsByStatus(Matchers.anyLong(), Matchers.eq(SellerPaymentStatusEnum.HOLD_PAYMENT_DUE_TO_DD))).thenReturn(holdForDDList);
		Mockito.when(sellerPaymentsDAO.findSellerPaymentsByStatus(Matchers.anyLong(), Matchers.eq(SellerPaymentStatusEnum.HOLD_PAYMENT_FOR_MANUAL_DISBURSEMENT))).thenReturn(holdForManualList);
		
		List<SellerPayment> outHoldForManualList = sellerPaymentBO.findSellerPaymentsByAction(sellerId, UpdatePaymentStatusRequest.Action.HOLD_PAYMENT_FOR_DUE_DILIGENCE);
		Assert.assertEquals(outHoldForManualList, holdForManualList);
		List<SellerPayment> outHoldForDDList = sellerPaymentBO.findSellerPaymentsByAction(sellerId, UpdatePaymentStatusRequest.Action.RELEASE_PAYMENT_FOR_DUE_DILIGENCE);
		Assert.assertEquals(outHoldForDDList, holdForDDList);
	}


	@Test
	public void testFindSellerPaymentsByActionDate(){
		Long sellerId = 11111L;
		SellerPayment holdForDDPayment = new SellerPayment();
		holdForDDPayment.setId(12345L);
		List<SellerPayment> holdForDDList = new ArrayList<SellerPayment>();
		holdForDDList.add(holdForDDPayment);
		SellerPayment holdForManualPayment = new SellerPayment();
		holdForManualPayment.setId(54321L);
		List<SellerPayment> holdForManualList = new ArrayList<SellerPayment>();
		holdForManualList.add(holdForManualPayment);
		Mockito.when(sellerPaymentsDAO.findSellerPaymentsByStatus(Matchers.anyLong(), Matchers.eq(SellerPaymentStatusEnum.HOLD_PAYMENT_DUE_TO_DD), any(Calendar.class))).thenReturn(holdForDDList);
		Mockito.when(sellerPaymentsDAO.findSellerPaymentsByStatus(Matchers.anyLong(), Matchers.eq(SellerPaymentStatusEnum.HOLD_PAYMENT_FOR_MANUAL_DISBURSEMENT), any(Calendar.class))).thenReturn(holdForManualList);

		List<SellerPayment> outHoldForManualList = sellerPaymentBO.findSellerPaymentsByAction(sellerId, UpdatePaymentStatusRequest.Action.HOLD_PAYMENT_FOR_DUE_DILIGENCE, Calendar.getInstance());
		Assert.assertEquals(outHoldForManualList, holdForManualList);
		List<SellerPayment> outHoldForDDList = sellerPaymentBO.findSellerPaymentsByAction(sellerId, UpdatePaymentStatusRequest.Action.RELEASE_PAYMENT_FOR_DUE_DILIGENCE, Calendar.getInstance());
		Assert.assertEquals(outHoldForDDList, holdForDDList);
	}

	
	@Test
	public void testUpdateSellerPaymentStatus(){
		SellerPayment payment = new SellerPayment();
		payment.setId(12345L);
		SellerPaymentStatusEnum status = SellerPaymentStatusEnum.READY_TO_PAY;
		when(sellerPaymentsDAO.updateSellerPaymentStatus(any(SellerPayment.class))).thenReturn(1);
		sellerPaymentBO.updateSellerPaymentStatus(payment, status);
		Mockito.verify(sellerPaymentsDAO).updateSellerPaymentStatus(payment);
		Mockito.verify(sellerPaymentStatusHistDAO).updateEndDate(payment.getId());
		Mockito.verify(sellerPaymentStatusHistDAO).saveSellerPaymentStatusHist(Matchers.any(SellerPaymentStatusHist.class));
	}

	@Test
	public void testUpdateSellerPaymentStatusNoUpdate(){
		SellerPayment payment = new SellerPayment();
		payment.setId(12345L);
		SellerPaymentStatusEnum status = SellerPaymentStatusEnum.READY_TO_PAY;
		when(sellerPaymentsDAO.updateSellerPaymentStatus(any(SellerPayment.class))).thenReturn(0);
		sellerPaymentBO.updateSellerPaymentStatus(payment, status);
		Mockito.verify(sellerPaymentsDAO).updateSellerPaymentStatus(payment);
		verify(sellerPaymentStatusHistDAO, times(0)).updateEndDate(anyLong());
		verify(sellerPaymentStatusHistDAO, times(0)).saveSellerPaymentStatusHist(any(SellerPaymentStatusHist.class));
	}

	@Test
	public void testSaveSellerPayment() {
        SellerPayment payment = new SellerPayment();
        payment.setId(12345L);
        sellerPaymentBO.saveSellerPayment(payment);
        Mockito.verify(sellerPaymentsDAO).saveSellerPayment(payment);
    }

}
