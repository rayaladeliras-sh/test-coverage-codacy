package com.stubhub.domain.account.datamodel.dao.impl;

import java.math.BigDecimal;
import java.util.*;

import com.stubhub.domain.account.datamodel.entity.SellerPaymentStatusEnum;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

import com.stubhub.domain.account.datamodel.entity.SellerPayment;
import com.stubhub.domain.account.datamodel.entity.SellerPayments;

public class SellerPaymentsDAOImplTest {

	private SellerPaymentsDAOImpl sellerPaymentsDAO;
	private HibernateTemplate hibernateTemplate;
	private SessionFactory sessionFactory;
	private Session session;
	private Query query;
	
	@BeforeMethod
	public void setUp() {
		sellerPaymentsDAO = new SellerPaymentsDAOImpl();
		hibernateTemplate = Mockito.mock(HibernateTemplate.class);
		sessionFactory = Mockito.mock(SessionFactory.class);
		session = Mockito.mock(Session.class);
		query = Mockito.mock(Query.class);
		Mockito.when(hibernateTemplate.getSessionFactory()).thenReturn(sessionFactory);
		Mockito.when(sessionFactory.getCurrentSession()).thenReturn(session);
		ReflectionTestUtils.setField(sellerPaymentsDAO, "hibernateTemplate", hibernateTemplate);
		
	}
	
	@Test
	public void testGetSellerCreditMemos(){
		List<SellerPayments> list1 = new ArrayList<SellerPayments>();
		SellerPayments sellerPayments = new SellerPayments();
		sellerPayments.setAmount(1.00);
		sellerPayments.setDateAdded(Calendar.getInstance());
		sellerPayments.setEventDateLocal("02/16/2014 07:30:00 (PST)");
		sellerPayments.setEventDate(Calendar.getInstance());
		sellerPayments.setBobId(1L);
		sellerPayments.setEventId(12345L);
		sellerPayments.setId(12345L);
		sellerPayments.setOrderId(12345L);
		sellerPayments.setOrderStatus("orderStatus");
		sellerPayments.setReasonDescription("reason");
		sellerPayments.setReferenceNumber("12345");
		sellerPayments.setSellerPaymentStatusId(12L);
		sellerPayments.setCurrencyCode("USD");
		list1.add(sellerPayments);
		
		Long sellerId = 12345L;
		String recordType = "memo";		
		String namedQuery = "SellerPayments.getSellerPayments";
		Mockito.when(session.getNamedQuery(namedQuery)).thenReturn(query);
		Mockito.when(query.list()).thenReturn(list1);
		List<SellerPayments> list2 = sellerPaymentsDAO.getSellerPayments(sellerId, recordType);
		Assert.assertNotNull(list2);
		Assert.assertEquals(list2.size(), 1);
		Assert.assertNotNull(list2.get(0).getAmount());
		Assert.assertNotNull(list2.get(0).getDateAdded());
		Assert.assertNotNull(list2.get(0).getEventDateLocal());
		Assert.assertNotNull(list2.get(0).getEventId());
		Assert.assertNotNull(list2.get(0).getId());
		Assert.assertNotNull(list2.get(0).getOrderId());
		Assert.assertNotNull(list2.get(0).getOrderStatus());
		Assert.assertNotNull(list2.get(0).getReasonDescription());
		Assert.assertNotNull(list2.get(0).getReferenceNumber());
		Assert.assertNotNull(list2.get(0).getSellerPaymentStatusId());
		Assert.assertNotNull(list2.get(0).getEventDate());
		Assert.assertNotNull(list2.get(0).getBobId());
		Assert.assertNotNull(list2.get(0).getCurrencyCode());
	}
	
	@Test
	public void testGetSellerCreditMemosIndy(){
		List<SellerPayments> list1 = new ArrayList<SellerPayments>();
		SellerPayments sellerPayments = new SellerPayments();
		sellerPayments.setAmount(1.00);
		sellerPayments.setDateAdded(Calendar.getInstance());
		sellerPayments.setEventDateLocal("02/16/2014 07:30:00 (PST)");
		sellerPayments.setEventDate(Calendar.getInstance());
		sellerPayments.setBobId(1L);
		sellerPayments.setEventId(12345L);
		sellerPayments.setId(12345L);
		sellerPayments.setSellerId(12345L);
		sellerPayments.setOrderId(12345L);
		sellerPayments.setOrderStatus("orderStatus");
		sellerPayments.setReasonDescription("reason");
		sellerPayments.setReferenceNumber("12345");
		sellerPayments.setSellerPaymentStatusId(12L);
		sellerPayments.setCurrencyCode("USD");
		list1.add(sellerPayments);
		
	
	
		String namedQueryDefault = "SellerPayments.getIndyPaymentsDefault";
		Mockito.when(session.getNamedQuery(namedQueryDefault)).thenReturn(query);
		Mockito.when(query.list()).thenReturn(list1);
		List<SellerPayments> list2 = sellerPaymentsDAO.getSellerPaymentsIndy(null, null);
		Assert.assertNotNull(list2);
		Assert.assertEquals(list2.size(), 1);
		Assert.assertNotNull(list2.get(0).getAmount());
		Assert.assertNotNull(list2.get(0).getDateAdded());
		Assert.assertNotNull(list2.get(0).getEventDateLocal());
		Assert.assertNotNull(list2.get(0).getEventId());
		Assert.assertNotNull(list2.get(0).getId());
		Assert.assertNotNull(list2.get(0).getOrderId());
		Assert.assertNotNull(list2.get(0).getOrderStatus());
		Assert.assertNotNull(list2.get(0).getReasonDescription());
		Assert.assertNotNull(list2.get(0).getReferenceNumber());
		Assert.assertNotNull(list2.get(0).getSellerPaymentStatusId());
		Assert.assertNotNull(list2.get(0).getEventDate());
		Assert.assertNotNull(list2.get(0).getBobId());
		Assert.assertNotNull(list2.get(0).getCurrencyCode());
		Assert.assertNotNull(list2.get(0).getSellerId());
		
		
			Calendar createdFromDate = Calendar.getInstance();
			Calendar createdToDate = Calendar.getInstance();	
		
	
			String namedQuery = "SellerPayments.getIndyPaymentsWithFilters";
			Mockito.when(session.getNamedQuery(namedQuery)).thenReturn(query);
			Mockito.when(query.list()).thenReturn(list1);
			List<SellerPayments> list3 = sellerPaymentsDAO.getSellerPaymentsIndy(createdFromDate, createdToDate);
			Assert.assertNotNull(list3);
			Assert.assertEquals(list3.size(), 1);
			Assert.assertNotNull(list3.get(0).getAmount());
			Assert.assertNotNull(list3.get(0).getDateAdded());
			Assert.assertNotNull(list3.get(0).getEventDateLocal());
			Assert.assertNotNull(list3.get(0).getEventId());
			Assert.assertNotNull(list3.get(0).getId());
			Assert.assertNotNull(list3.get(0).getOrderId());
			Assert.assertNotNull(list3.get(0).getOrderStatus());
			Assert.assertNotNull(list3.get(0).getReasonDescription());
			Assert.assertNotNull(list3.get(0).getReferenceNumber());
			Assert.assertNotNull(list3.get(0).getSellerPaymentStatusId());
			Assert.assertNotNull(list3.get(0).getEventDate());
			Assert.assertNotNull(list3.get(0).getBobId());
			Assert.assertNotNull(list3.get(0).getCurrencyCode());
			
			
			
			Calendar createdToDate2 = Calendar.getInstance();	
		
	
			String namedQuery2 = "SellerPayments.getIndyPaymentsWithFilters";
			Mockito.when(session.getNamedQuery(namedQuery2)).thenReturn(query);
			Mockito.when(query.list()).thenReturn(list1);
			List<SellerPayments> list4 = sellerPaymentsDAO.getSellerPaymentsIndy(null, createdToDate2);
			Assert.assertNotNull(list4);
			Assert.assertEquals(list4.size(), 1);
			Assert.assertNotNull(list4.get(0).getAmount());
			Assert.assertNotNull(list4.get(0).getDateAdded());
			Assert.assertNotNull(list4.get(0).getEventDateLocal());
			Assert.assertNotNull(list4.get(0).getEventId());
			Assert.assertNotNull(list4.get(0).getId());
			Assert.assertNotNull(list4.get(0).getOrderId());
			Assert.assertNotNull(list4.get(0).getOrderStatus());
			Assert.assertNotNull(list4.get(0).getReasonDescription());
			Assert.assertNotNull(list4.get(0).getReferenceNumber());
			Assert.assertNotNull(list4.get(0).getSellerPaymentStatusId());
			Assert.assertNotNull(list4.get(0).getEventDate());
			Assert.assertNotNull(list4.get(0).getBobId());
			Assert.assertNotNull(list4.get(0).getCurrencyCode());
			
			
			
			Calendar createdFromDate2 = Calendar.getInstance();	
			
			
			String namedQuery3 = "SellerPayments.getIndyPaymentsWithFilters";
			Mockito.when(session.getNamedQuery(namedQuery3)).thenReturn(query);
			Mockito.when(query.list()).thenReturn(list1);
			List<SellerPayments> list5 = sellerPaymentsDAO.getSellerPaymentsIndy(createdFromDate2, null);
			Assert.assertNotNull(list5);
			Assert.assertEquals(list5.size(), 1);
			Assert.assertNotNull(list5.get(0).getAmount());
			Assert.assertNotNull(list5.get(0).getDateAdded());
			Assert.assertNotNull(list5.get(0).getEventDateLocal());
			Assert.assertNotNull(list5.get(0).getEventId());
			Assert.assertNotNull(list5.get(0).getId());
			Assert.assertNotNull(list5.get(0).getOrderId());
			Assert.assertNotNull(list5.get(0).getOrderStatus());
			Assert.assertNotNull(list5.get(0).getReasonDescription());
			Assert.assertNotNull(list5.get(0).getReferenceNumber());
			Assert.assertNotNull(list5.get(0).getSellerPaymentStatusId());
			Assert.assertNotNull(list5.get(0).getEventDate());
			Assert.assertNotNull(list5.get(0).getBobId());
			Assert.assertNotNull(list5.get(0).getCurrencyCode());
			
	}
	
	@Test
	public void testGetSellerPaymentsBySellerId(){
		List<SellerPayments> list1 = new ArrayList<SellerPayments>();
		SellerPayments sellerPayments = new SellerPayments();
		sellerPayments.setAmount(1.00);
		sellerPayments.setDateAdded(Calendar.getInstance());
		sellerPayments.setEventDateLocal("02/16/2014 07:30:00 (PST)");
		sellerPayments.setEventId(12345L);
		sellerPayments.setId(12345L);
		sellerPayments.setOrderId(12345L);
		sellerPayments.setOrderStatus("orderStatus");
		sellerPayments.setReasonDescription("reason");
		sellerPayments.setReferenceNumber("12345");
		sellerPayments.setSellerPaymentStatusId(12L);
		list1.add(sellerPayments);
		
		Long sellerId = 12345L;
		String recordType = "memo";				
		Mockito.when(session.createQuery(Mockito.anyString())).thenReturn(query);
		Mockito.when(session.getNamedQuery(Mockito.anyString())).thenReturn(query);
		Mockito.when(query.list()).thenReturn(list1);
		Mockito.when(query.getQueryString()).thenReturn("Query String");
		
		String sort = "ORDERID,ASC";
		Calendar createdFromDate = Calendar.getInstance();
		Calendar createdToDate = Calendar.getInstance();	
		List<SellerPayments> list2 = sellerPaymentsDAO.getSellerPaymentsBySellerId(sellerId, recordType, sort, createdFromDate, createdToDate, null, null, "USD");
		Assert.assertNotNull(list2);
	}
	
	@Test
	public void testGetSellerPaymentsBySellerId_NullInputs(){
		List<SellerPayments> list1 = new ArrayList<SellerPayments>();
		SellerPayments sellerPayments = new SellerPayments();
		sellerPayments.setAmount(1.00);
		sellerPayments.setDateAdded(Calendar.getInstance());
		sellerPayments.setEventDateLocal("02/16/2014 07:30:00 (PST)");
		sellerPayments.setEventId(12345L);
		sellerPayments.setId(12345L);
		sellerPayments.setOrderId(12345L);
		sellerPayments.setOrderStatus("orderStatus");
		sellerPayments.setReasonDescription("reason");
		sellerPayments.setReferenceNumber("12345");
		sellerPayments.setSellerPaymentStatusId(12L);
		list1.add(sellerPayments);
		
		Long sellerId = 12345L;
		String recordType = "memo";		
		Mockito.when(session.createQuery(Mockito.anyString())).thenReturn(query);
		Mockito.when(session.getNamedQuery(Mockito.anyString())).thenReturn(query);			
		Mockito.when(query.list()).thenReturn(list1);
		Mockito.when(query.getQueryString()).thenReturn("Query String");
		
		String sort = null;
		Calendar createdFromDate = null;
		Calendar createdToDate = null;	
		List<SellerPayments> list2 = sellerPaymentsDAO.getSellerPaymentsBySellerId(sellerId, recordType, sort, createdFromDate, createdToDate, null, null, null);
		Assert.assertNotNull(list2);
		
		sort = null;
		createdFromDate = null;
		createdToDate = Calendar.getInstance();	
		list2 = sellerPaymentsDAO.getSellerPaymentsBySellerId(sellerId, recordType, sort, createdFromDate, createdToDate, null, null, "USD");
		Assert.assertNotNull(list2);
		
		sort = "CREATEDDATE";
		createdFromDate = Calendar.getInstance();
		createdToDate = null;	
		list2 = sellerPaymentsDAO.getSellerPaymentsBySellerId(sellerId, recordType, sort, createdFromDate, createdToDate, null, null, null);
		Assert.assertNotNull(list2);
		
		sort = "AMOUNT,DESC";
		createdFromDate = null;
		createdToDate = null;	
		list2 = sellerPaymentsDAO.getSellerPaymentsBySellerId(sellerId, recordType, sort, createdFromDate, createdToDate, null, null, null);
		Assert.assertNotNull(list2);
	}
	
	@Test
	public void testGetSellerPaymentById(){
		SellerPayment payment = new SellerPayment();
		payment.setId(12345L);
		Mockito.when(session.get(SellerPayment.class, payment.getId())).thenReturn(payment);
		
		SellerPayment out = sellerPaymentsDAO.getSellerPaymentById(payment.getId());
		Assert.assertEquals(out, payment);
	}
	
	@Test
	public void testSaveSellerPayment(){
		SellerPayment payment = new SellerPayment();
		sellerPaymentsDAO.saveSellerPayment(payment);
		Mockito.verify(session).saveOrUpdate(payment);
	}

	@Test
	public void testGetSellerPaymentByRefNumber() {

	    Assert.assertNull(sellerPaymentsDAO.getSellerPaymentByRefNumber(null));
	    SellerPayment payment = new SellerPayment();
	    payment.setId(12345L);
	    payment.setReferenceNumber("refNumber");
	    Mockito.when(session.getNamedQuery("SellerPayment.getSellerPaymentByRefNumber")).thenReturn(query);
	    Mockito.when(query.list()).thenReturn(Arrays.asList(payment));

	    SellerPayment result = sellerPaymentsDAO.getSellerPaymentByRefNumber("refNumber");
	    Assert.assertNotNull(result);
	    Assert.assertEquals(result.getReferenceNumber(), "refNumber");

	    Mockito.when(query.list()).thenReturn(null);
	    result = sellerPaymentsDAO.getSellerPaymentByRefNumber("refNumber");
        Assert.assertNull(result);
	}

	@Test
	public void testGetSellerPaymentsByIds() {
	    List<SellerPayment> result = sellerPaymentsDAO.getSellerPaymentsByIds(null);
	    Assert.assertNotNull(result);
	    Assert.assertTrue(result.isEmpty());

	    SellerPayment payment = new SellerPayment();
	    payment.setId(12345L);
	    Mockito.when(session.getNamedQuery("SellerPayment.getSellerPaymentsByIds")).thenReturn(query);
	    Mockito.when(query.list()).thenReturn(Arrays.asList(payment));

	    result = sellerPaymentsDAO.getSellerPaymentsByIds(Arrays.asList(payment.getId()));
	    Assert.assertNotNull(result);
	    Assert.assertEquals(result.size(), 1);
	    Assert.assertEquals(result.get(0).getId(), payment.getId());

	    Mockito.when(query.list()).thenReturn(null);
	    result = sellerPaymentsDAO.getSellerPaymentsByIds(Arrays.asList(payment.getId()));
	    Assert.assertNotNull(result);
	    Assert.assertTrue(result.isEmpty());
	}

	@Test
	public void testCountSellerPaymentsBySellerId01() {
	    Calendar from = Calendar.getInstance();
	    from.setTimeInMillis(100000);
	    Calendar to = Calendar.getInstance();
        to.setTimeInMillis(100000);

        Mockito.when(session.getNamedQuery("SellerPayments.countSellerPaymentsDefault")).thenReturn(query);
        Mockito.when(session.getNamedQuery("SellerPayments.countSellerPaymentsWithFilters")).thenReturn(query);
		Mockito.when(session.getNamedQuery("SellerPayments.countSellerPaymentsWithFiltersCurrency")).thenReturn(query);
        Mockito.when(query.list()).thenReturn(Arrays.asList(new BigDecimal(1L)));
        long count = sellerPaymentsDAO.countSellerPaymentsBySellerId(1L, "recordType", from, to, "EUR");

        Assert.assertEquals(count, 1);
        Mockito.verify(query).setCalendar("createdFromDate", from);
        Mockito.verify(query).setCalendar("createdToDate", to);
        Mockito.verify(query).setLong("sellerId", 1L);
        Mockito.verify(query).setString("recordType", "recordType");
        Mockito.verify(session, Mockito.never()).getNamedQuery("SellerPayments.countSellerPaymentsDefault");
        Mockito.verify(session).getNamedQuery("SellerPayments.countSellerPaymentsWithFiltersCurrency");
	}
	
	

	@Test
	public void testCountSellerPaymentsBySellerId02() {
	    Calendar from = Calendar.getInstance();
	    from.setTimeInMillis(100000);
	    Calendar to = Calendar.getInstance();
	    to.setTimeInMillis(100000);

	    Mockito.when(session.getNamedQuery("SellerPayments.countSellerPaymentsDefault")).thenReturn(query);
	    Mockito.when(session.getNamedQuery("SellerPayments.countSellerPaymentsWithFilters")).thenReturn(query);
	    Mockito.when(query.list()).thenReturn(Arrays.asList(new BigDecimal(1L)));

	    long count = sellerPaymentsDAO.countSellerPaymentsBySellerId(1L, "recordType", null, null, null);

	    Assert.assertEquals(count, 1);
	    Mockito.verify(query).setLong("sellerId", 1L);
	    Mockito.verify(query).setString("recordType", "recordType");
	    Mockito.verify(query).setInteger("days", 90);
	    Mockito.verify(session).getNamedQuery("SellerPayments.countSellerPaymentsDefault");
	    Mockito.verify(session, Mockito.never()).getNamedQuery("SellerPayments.countSellerPaymentsWithFilters");
	}

	@Test
	public void testCountSellerPaymentsBySellerId03() {
	    Calendar from = Calendar.getInstance();
	    from.setTimeInMillis(100000);
	    Calendar to = Calendar.getInstance();
	    to.setTimeInMillis(100000);

	    Mockito.when(session.getNamedQuery("SellerPayments.countSellerPaymentsDefault")).thenReturn(query);
	    Mockito.when(session.getNamedQuery("SellerPayments.countSellerPaymentsWithFilters")).thenReturn(query);
	    Mockito.when(query.list()).thenReturn(Arrays.asList(new BigDecimal(1L)));

	    long count = sellerPaymentsDAO.countSellerPaymentsBySellerId(1L, "recordType", from, null, null);

        Assert.assertEquals(count, 1);
        Mockito.verify(query).setCalendar("createdFromDate", from);
        Mockito.verify(query).setCalendar(Mockito.matches("createdToDate"), Mockito.any(Calendar.class));
        Mockito.verify(query).setLong("sellerId", 1L);
        Mockito.verify(query).setString("recordType", "recordType");
        Mockito.verify(session, Mockito.never()).getNamedQuery("SellerPayments.countSellerPaymentsDefault");
        Mockito.verify(session).getNamedQuery("SellerPayments.countSellerPaymentsWithFilters");
	}

	@Test
	public void testCountSellerPaymentsBySellerId04() {
	    Calendar from = Calendar.getInstance();
	    from.setTimeInMillis(100000);
	    Calendar to = Calendar.getInstance();
	    to.setTimeInMillis(100000);

	    Mockito.when(session.getNamedQuery("SellerPayments.countSellerPaymentsDefault")).thenReturn(query);
	    Mockito.when(session.getNamedQuery("SellerPayments.countSellerPaymentsWithFilters")).thenReturn(query);
		Mockito.when(session.getNamedQuery("SellerPayments.countSellerPaymentsWithFiltersCurrency")).thenReturn(query);
	    Mockito.when(query.list()).thenReturn(Arrays.asList(new BigDecimal(1L)));

	    long count = sellerPaymentsDAO.countSellerPaymentsBySellerId(1L, "recordType", null, to, "USD");

        Assert.assertEquals(count, 1);
        Mockito.verify(query).setCalendar(Mockito.matches("createdFromDate"), Mockito.any(Calendar.class));
        Mockito.verify(query).setCalendar("createdToDate", to);
        Mockito.verify(query).setLong("sellerId", 1L);
        Mockito.verify(query).setString("recordType", "recordType");
        Mockito.verify(session, Mockito.never()).getNamedQuery("SellerPayments.countSellerPaymentsDefault");
        Mockito.verify(session).getNamedQuery("SellerPayments.countSellerPaymentsWithFiltersCurrency");
	}

	@Test
	public void testFindSellerPaymentsByStatusNotFound(){
		when(hibernateTemplate.getSessionFactory()).thenReturn(sessionFactory);
		when(sessionFactory.getCurrentSession()).thenReturn(session);
		when(session.createQuery(anyString())).thenReturn(query);
		when(query.setLong(anyString(), anyLong())).thenReturn(query);
		when(query.setDate(anyString(), any(Date.class))).thenReturn(query);
		when(query.list()).thenReturn(null);
		Assert.assertTrue(sellerPaymentsDAO.findSellerPaymentsByStatus(1l, SellerPaymentStatusEnum.HOLD_PAYMENT_DUE_TO_DD, Calendar.getInstance()).size() == 0);
	}

	@Test
	public void testFindSellerPaymentsByStatus(){
		when(hibernateTemplate.getSessionFactory()).thenReturn(sessionFactory);
		when(sessionFactory.getCurrentSession()).thenReturn(session);
		when(session.createQuery(anyString())).thenReturn(query);
		when(query.setLong(anyString(), anyLong())).thenReturn(query);
		when(query.setDate(anyString(), any(Date.class))).thenReturn(query);
		List<SellerPayment> result = new ArrayList<SellerPayment>();
		SellerPayment sellerPayment = new SellerPayment();
		result.add(sellerPayment);
		when(query.list()).thenReturn(result);
		Assert.assertTrue(sellerPaymentsDAO.findSellerPaymentsByStatus(1l, SellerPaymentStatusEnum.HOLD_PAYMENT_DUE_TO_DD, Calendar.getInstance()).size() == 1);
	}


	@Test
	public void testFindSellerPaymentsByStatusNoDateNotFound(){
		when(hibernateTemplate.getSessionFactory()).thenReturn(sessionFactory);
		when(sessionFactory.getCurrentSession()).thenReturn(session);
		when(session.createQuery(anyString())).thenReturn(query);
		when(query.setLong(anyString(), anyLong())).thenReturn(query);
		when(query.setDate(anyString(), any(Date.class))).thenReturn(query);
		when(query.list()).thenReturn(null);
		Assert.assertTrue(sellerPaymentsDAO.findSellerPaymentsByStatus(1l, SellerPaymentStatusEnum.HOLD_PAYMENT_DUE_TO_DD).size() == 0);
	}

	@Test
	public void testFindSellerPaymentsByStatusNoDate(){
		when(hibernateTemplate.getSessionFactory()).thenReturn(sessionFactory);
		when(sessionFactory.getCurrentSession()).thenReturn(session);
		when(session.createQuery(anyString())).thenReturn(query);
		when(query.setLong(anyString(), anyLong())).thenReturn(query);
		when(query.setDate(anyString(), any(Date.class))).thenReturn(query);
		List<SellerPayment> result = new ArrayList<SellerPayment>();
		SellerPayment sellerPayment = new SellerPayment();
		result.add(sellerPayment);
		when(query.list()).thenReturn(result);
		Assert.assertTrue(sellerPaymentsDAO.findSellerPaymentsByStatus(1l, SellerPaymentStatusEnum.HOLD_PAYMENT_DUE_TO_DD).size() == 1);
	}

	@Test
	public void testUpdateSellerPaymentStatus(){
		when(hibernateTemplate.getSessionFactory()).thenReturn(sessionFactory);
		when(sessionFactory.getCurrentSession()).thenReturn(session);
		when(session.getNamedQuery(eq("SellerPayments.updatePaymentStatus"))).thenReturn(query);
		when(query.setLong(anyString(), anyLong())).thenReturn(query);
		when(query.setCalendar(anyString(), any(Calendar.class))).thenReturn(query);
		when(query.setString(anyString(), anyString())).thenReturn(query);
		when(query.executeUpdate()).thenReturn(1);
		SellerPayment sellerPayment = new SellerPayment();
		sellerPayment.setId(1l);
		sellerPayment.setSellerId(1111l);
		sellerPayment.setSellerPaymentStatusId(1l);
		sellerPayment.setStatus("ReadyToPay");
		sellerPayment.setLastUpdatedBy("test");
		sellerPayment.setLastUpdatedDate(Calendar.getInstance());
		sellerPaymentsDAO.updateSellerPaymentStatus(sellerPayment);
	}
}
