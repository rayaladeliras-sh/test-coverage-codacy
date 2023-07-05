package com.stubhub.domain.account.datamodel.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.mockito.Mockito;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.stubhub.domain.account.datamodel.dao.CcTransDAO;
import com.stubhub.domain.account.datamodel.dao.ListingDAO;
import com.stubhub.domain.account.datamodel.dao.ListingSeatTraitDAO;
import com.stubhub.domain.account.datamodel.dao.ListingSeatsDAO;
import com.stubhub.domain.account.datamodel.dao.StubTransAdjHistDAO;
import com.stubhub.domain.account.datamodel.dao.StubTransDetailDAO;
import com.stubhub.domain.account.datamodel.dao.StubTransFmDmDAO;
import com.stubhub.domain.account.datamodel.dao.StubTransSeatTraitDAO;
import com.stubhub.domain.account.datamodel.dao.StubTransTmpDAO;
import com.stubhub.domain.account.datamodel.dao.TransactionCancellationDAO;
import com.stubhub.domain.account.datamodel.entity.CcTrans;
import com.stubhub.domain.account.datamodel.entity.Listing;
import com.stubhub.domain.account.datamodel.entity.ListingSeatTrait;
import com.stubhub.domain.account.datamodel.entity.StubTransAdjHist;
import com.stubhub.domain.account.datamodel.entity.StubTransDetail;
import com.stubhub.domain.account.datamodel.entity.StubTransFmDm;
import com.stubhub.domain.account.datamodel.entity.StubTransSeatTrait;
import com.stubhub.domain.account.datamodel.entity.StubTransTmp;
import com.stubhub.domain.account.datamodel.entity.TicketSeat;
import com.stubhub.domain.account.datamodel.entity.TransactionCancellation;
import com.stubhub.newplatform.common.entity.Money;
import com.stubhub.newplatform.persistence.adapter.UTCCalendarToTimestampAdapter;

public class SubstitutionOrderDAOTest {
	private HibernateTemplate hibernateTemplate = Mockito.mock(HibernateTemplate.class);
	private SessionFactory sessionFactory = Mockito.mock(SessionFactory.class);
	private Session session = Mockito.mock(Session.class);
	private Transaction tx = Mockito.mock(Transaction.class);
	private SQLQuery query = Mockito.mock(SQLQuery.class);
	GregorianCalendar calender = UTCCalendarToTimestampAdapter.getNewUTCCalendarInstanceStatic();

	@Test
	public void testStubTransDetailDAO() {
		StubTransDetail stubTransDetail = mockStubTransDetail();
		StubTransDetailDAO stubTransDetailDAO = new StubTransDetailDAOImpl();
		Mockito.when(hibernateTemplate.getSessionFactory()).thenReturn(sessionFactory);
		Mockito.when(sessionFactory.getCurrentSession()).thenReturn(session);
		ReflectionTestUtils.setField(stubTransDetailDAO, "hibernateTemplate", hibernateTemplate);
		Assert.assertNotNull(stubTransDetailDAO.persist(stubTransDetail));
	}
	
	@Test
	public void testStubTransFmDmDAO() {
		StubTransFmDm stubTransFmDm = mockStubTransFmDm();
		StubTransFmDmDAO stubTransFmDmDAO = new StubTransFmDmDAOImpl();
		Mockito.when(hibernateTemplate.getSessionFactory()).thenReturn(sessionFactory);
		Mockito.when(sessionFactory.getCurrentSession()).thenReturn(session);
		ReflectionTestUtils.setField(stubTransFmDmDAO, "hibernateTemplate", hibernateTemplate);
		Assert.assertNotNull(stubTransFmDmDAO.persist(stubTransFmDm));
	}
	
	@Test
	public void testStubTransTmpDAO() {
		StubTransTmp stubTransTmp = mockStubTransTmp();
		StubTransTmpDAO stubTransTmpDAO = new StubTransTmpDAOImpl();
		Mockito.when(hibernateTemplate.getSessionFactory()).thenReturn(sessionFactory);
		Mockito.when(sessionFactory.getCurrentSession()).thenReturn(session);
		ReflectionTestUtils.setField(stubTransTmpDAO, "hibernateTemplate", hibernateTemplate);
		Assert.assertNotNull(stubTransTmpDAO.persist(stubTransTmp));
	}
	
	@Test
	public void testListingSeatTraitDAO() {
		List<ListingSeatTrait> lst = new ArrayList<ListingSeatTrait>();
		ListingSeatTrait listingSeatTrait = mockListingSeatTrait();
		lst.add(listingSeatTrait);
		ListingSeatTraitDAO listingSeatTraitDAO = new ListingSeatTraitDAOImpl();
		Mockito.when(hibernateTemplate.getSessionFactory()).thenReturn(sessionFactory);
		Mockito.when(sessionFactory.getCurrentSession()).thenReturn(session);
		ReflectionTestUtils.setField(listingSeatTraitDAO, "hibernateTemplate", hibernateTemplate);
		String namedQuery = "ListingSeatTrait.getByListingId";
		Mockito.when(session.getNamedQuery(namedQuery)).thenReturn(query);
		Mockito.when(query.list()).thenReturn(lst);
		Assert.assertEquals(listingSeatTraitDAO.getByListingId(1l).size(), 1);
	}
	
	@Test
	public void testStubTransSeatTraitDAO() {
		List<StubTransSeatTrait> lst = new ArrayList<StubTransSeatTrait>();
		StubTransSeatTrait stubTransSeatTrait = mockStubTransSeatTrait();
		lst.add(stubTransSeatTrait);
		StubTransSeatTraitDAO stubTransSeatTraitDAO = new StubTransSeatTraitDAOImpl();
		Mockito.when(hibernateTemplate.getSessionFactory()).thenReturn(sessionFactory);
		Mockito.when(sessionFactory.openSession()).thenReturn(session);
		Mockito.when(session.beginTransaction()).thenReturn(tx);
		ReflectionTestUtils.setField(stubTransSeatTraitDAO, "hibernateTemplate", hibernateTemplate);
		Assert.assertNotNull(stubTransSeatTraitDAO.persist(lst));
	}
	
	@Test
	public void testListingDAO() {
		Listing listing = new Listing();
		listing.setId(123L);
		listing.setQuantityRemain(1);
		
		ListingDAO listingDAO = new ListingDAOImpl();
		query = Mockito.mock(SQLQuery.class);
		Mockito.when(hibernateTemplate.getSessionFactory()).thenReturn(sessionFactory);
		Mockito.when(sessionFactory.getCurrentSession()).thenReturn(session);
		ReflectionTestUtils.setField(listingDAO, "hibernateTemplate", hibernateTemplate);
		String namedQuery = "Listing.updateQuantityRemain";
		Mockito.when(session.getNamedQuery(namedQuery)).thenReturn(query);
		Mockito.when(query.executeUpdate()).thenReturn(1);
		Assert.assertEquals(listingDAO.updateListing(listing), 1);
	}
	
	@Test
	public void testListingSeatsDAO() {
		List<TicketSeat> lst = new ArrayList<TicketSeat>();
		TicketSeat tc = mockTicketSeat();
		lst.add(tc);		
		ListingSeatsDAO listingSeatsDAO = new ListingSeatsDAOImpl();
		query = Mockito.mock(SQLQuery.class);
		Mockito.when(hibernateTemplate.getSessionFactory()).thenReturn(sessionFactory);
		Mockito.when(sessionFactory.openSession()).thenReturn(session);
		Mockito.when(session.beginTransaction()).thenReturn(tx);
		ReflectionTestUtils.setField(listingSeatsDAO, "hibernateTemplate", hibernateTemplate);
		String namedQuery = "TicketSeat.updateSeatStatus";
		Mockito.when(session.getNamedQuery(namedQuery)).thenReturn(query);
		Mockito.when(query.executeUpdate()).thenReturn(1);
		Assert.assertEquals(listingSeatsDAO.updateTicketSeatStatus(lst), 1);
	}
	
	@Test
	public void testCcTransDAO() {
		CcTransDAO ccTransDAO = new CcTransDAOImpl();
		query = Mockito.mock(SQLQuery.class);
		Mockito.when(hibernateTemplate.getSessionFactory()).thenReturn(sessionFactory);
		Mockito.when(sessionFactory.getCurrentSession()).thenReturn(session);
		ReflectionTestUtils.setField(ccTransDAO, "hibernateTemplate", hibernateTemplate);
		String namedQuery = "ccTrans.updateByTid";
		Mockito.when(session.getNamedQuery(namedQuery)).thenReturn(query);
		Mockito.when(query.executeUpdate()).thenReturn(1);
		Assert.assertEquals(ccTransDAO.updateByTid(1l,2l,"bijain"), 1);
	}
	
	@Test
	public void testTransactionCancellationDAO() {
		TransactionCancellation transactionCancellation = mockTransactionCancellation();
		TransactionCancellationDAO transactionCancellationDAO = new TransactionCancellationDAOImpl();
		Mockito.when(hibernateTemplate.getSessionFactory()).thenReturn(sessionFactory);
		Mockito.when(sessionFactory.getCurrentSession()).thenReturn(session);
		ReflectionTestUtils.setField(transactionCancellationDAO, "hibernateTemplate", hibernateTemplate);
		Assert.assertNotNull(transactionCancellationDAO.persist(transactionCancellation));
	}
	
	@Test
	public void testStubTransAdjHistDAO() {
		StubTransAdjHist stubTransAdjHist = mockStubTransAdjHist();
		StubTransAdjHistDAO stubTransAdjHistDAO = new StubTransAdjHistDAOImpl();
		Mockito.when(hibernateTemplate.getSessionFactory()).thenReturn(sessionFactory);
		Mockito.when(sessionFactory.getCurrentSession()).thenReturn(session);
		ReflectionTestUtils.setField(stubTransAdjHistDAO, "hibernateTemplate", hibernateTemplate);
		Assert.assertNotNull(stubTransAdjHistDAO.persist(stubTransAdjHist));
	}
	
	@Test
	public StubTransDetail mockStubTransDetail(){
		StubTransDetail stubTransDetail = new StubTransDetail();
		stubTransDetail.setStubTransDtlId(1l);
		stubTransDetail.setBarcodeCancelled(false);
		stubTransDetail.setConfirmedGeneralAdmissionIndicator(1l);
		stubTransDetail.setConfirmedRowNumber("1");
		stubTransDetail.setConfirmedSeatNumber("1");
		stubTransDetail.setConfirmedSectionName("1");
		stubTransDetail.setConfirmedTicketListTypeId(1l);
		stubTransDetail.setCreatedBy("bijain");
		stubTransDetail.setCreatedDate(calender);
		stubTransDetail.setGeneralAdmissionIndicator(1l);
		stubTransDetail.setLastUpdatedBy("bijain");
		stubTransDetail.setLastUpdatedDate(calender);
		stubTransDetail.setRowNumber("1");
		stubTransDetail.setSeatNumber("1");
		stubTransDetail.setSectionName("1");
		stubTransDetail.setTicketListTypeId(1l);
		stubTransDetail.setTicketSeatId(1l);
		stubTransDetail.setTid(1l);
		Assert.assertEquals(stubTransDetail.getConfirmedRowNumber(),"1");
		Assert.assertEquals(stubTransDetail.getConfirmedSeatNumber(),"1");
		Assert.assertEquals(stubTransDetail.getConfirmedSectionName(),"1");
		Assert.assertEquals(stubTransDetail.getCreatedBy(),"bijain");
		Assert.assertEquals(stubTransDetail.getLastUpdatedBy(),"bijain");
		Assert.assertEquals(stubTransDetail.getRowNumber(),"1");
		Assert.assertEquals(stubTransDetail.getSeatNumber(),"1");
		Assert.assertEquals(stubTransDetail.getSectionName(),"1");
		Assert.assertFalse(stubTransDetail.getBarcodeCancelled());
		Assert.assertEquals(stubTransDetail.getConfirmedGeneralAdmissionIndicator(),Long.valueOf(1l));
		Assert.assertEquals(stubTransDetail.getConfirmedTicketListTypeId(),Long.valueOf(1l));
		Assert.assertEquals(stubTransDetail.getCreatedDate(),calender);
		Assert.assertEquals(stubTransDetail.getGeneralAdmissionIndicator(),Long.valueOf(1l));
		Assert.assertEquals(stubTransDetail.getLastUpdatedDate(),calender);
		Assert.assertEquals(stubTransDetail.getStubTransDtlId(),Long.valueOf(1l));
		Assert.assertEquals(stubTransDetail.getTicketListTypeId(),Long.valueOf(1l));
		Assert.assertEquals(stubTransDetail.getTicketSeatId(),Long.valueOf(1l));
		Assert.assertEquals(stubTransDetail.getTid(),Long.valueOf(1l));
		return stubTransDetail;
	}
	
	@Test
	public StubTransFmDm mockStubTransFmDm(){
		StubTransFmDm stubTransFmDm = new StubTransFmDm();
		stubTransFmDm.setId(1l);
		stubTransFmDm.setActive(true);
		stubTransFmDm.setCreatedBy("bijain");
		stubTransFmDm.setCreatedDate(calender);
		stubTransFmDm.setDeliveryMethodId(1l);
		stubTransFmDm.setFulfillmentMethodId(1l);
		stubTransFmDm.setLastUpdatedBy("bijain");
		stubTransFmDm.setLastUpdatedDate(calender);
		stubTransFmDm.setLmsLocationId(1l);
		stubTransFmDm.setLogisticsTypeId(1l);
		stubTransFmDm.setTid(1l);
		Assert.assertEquals(stubTransFmDm.getCreatedBy(),"bijain");
		Assert.assertEquals(stubTransFmDm.getLastUpdatedBy(),"bijain");
		Assert.assertTrue(stubTransFmDm.getActive());
		Assert.assertEquals(stubTransFmDm.getCreatedDate(),calender);
		Assert.assertEquals(stubTransFmDm.getDeliveryMethodId(),Long.valueOf(1l));
		Assert.assertEquals(stubTransFmDm.getFulfillmentMethodId(),Long.valueOf(1l));
		Assert.assertEquals(stubTransFmDm.getId(),Long.valueOf(1l));
		Assert.assertEquals(stubTransFmDm.getLastUpdatedDate(),calender);
		Assert.assertEquals(stubTransFmDm.getLmsLocationId(),Long.valueOf(1l));
		Assert.assertEquals(stubTransFmDm.getLogisticsTypeId(),Long.valueOf(1l));
		Assert.assertEquals(stubTransFmDm.getTid(),Long.valueOf(1l));
		return stubTransFmDm;
	}
	
	
	@Test
	public StubTransTmp mockStubTransTmp(){
		StubTransTmp stubTransTmp = new StubTransTmp();
		stubTransTmp.setId(1l);
		stubTransTmp.setBidderCobrand("1");
		stubTransTmp.setBuyerFee("1");
		stubTransTmp.setBuyerFeeVal(1L);
		stubTransTmp.setBuyerId(1L);
		stubTransTmp.setCcId(1L);
		stubTransTmp.setContactId(1L);
		stubTransTmp.setCurrency("USD");
		stubTransTmp.setDateAdded(calender);
		stubTransTmp.setDateLastModified(calender);
		stubTransTmp.setDiscountCost(1L);
		stubTransTmp.setDiscountList("1");
		stubTransTmp.setGroupComments("1");
		stubTransTmp.setLogisticsMethod(1L);
		stubTransTmp.setLogisticsMethodDescription("1");
		stubTransTmp.setQuantity(1L);
		stubTransTmp.setSaleMethod(1L);
		stubTransTmp.setSeats("1");
		stubTransTmp.setSellerFee("1");
		stubTransTmp.setSellerFeeVal(1L);
		stubTransTmp.setShipCost(1L);
		stubTransTmp.setTicketCost(1L);
		stubTransTmp.setTicketId(1L);
		
		Assert.assertEquals(stubTransTmp.getId(),Long.valueOf(1l));
		Assert.assertEquals(stubTransTmp.getBidderCobrand(),"1");
		Assert.assertEquals(stubTransTmp.getBuyerFee(),"1");
		Assert.assertEquals(stubTransTmp.getBuyerFeeVal(),Long.valueOf(1l));
		Assert.assertEquals(stubTransTmp.getCcId(),Long.valueOf(1l));
		Assert.assertEquals(stubTransTmp.getContactId(),Long.valueOf(1l));
		Assert.assertEquals(stubTransTmp.getDateAdded(),calender);
		Assert.assertEquals(stubTransTmp.getDateLastModified(),calender);
		Assert.assertEquals(stubTransTmp.getDiscountCost(),Long.valueOf(1l));
		Assert.assertEquals(stubTransTmp.getLogisticsMethod(),Long.valueOf(1l));
		Assert.assertEquals(stubTransTmp.getQuantity(),Long.valueOf(1l));
		Assert.assertEquals(stubTransTmp.getDiscountList(),"1");
		Assert.assertEquals(stubTransTmp.getLogisticsMethodDescription(),"1");
		Assert.assertEquals(stubTransTmp.getSeats(),"1");
		Assert.assertEquals(stubTransTmp.getSellerFee(),"1");
		Assert.assertEquals(stubTransTmp.getShipCost(),Long.valueOf(1l));
		Assert.assertEquals(stubTransTmp.getTicketCost(),Long.valueOf(1l));
		Assert.assertEquals(stubTransTmp.getTicketId(),Long.valueOf(1l));
		Assert.assertEquals(stubTransTmp.getSaleMethod(),Long.valueOf(1l));
		Assert.assertEquals(stubTransTmp.getBuyerId(),Long.valueOf(1l));
		Assert.assertEquals(stubTransTmp.getSaleMethod(),Long.valueOf(1l));
		return stubTransTmp;
	}
	
	@Test
	public ListingSeatTrait mockListingSeatTrait(){
		ListingSeatTrait listingSeatTrait = new ListingSeatTrait();
		listingSeatTrait.setId(1l);
		listingSeatTrait.setTicketId(1l);
		listingSeatTrait.setActive(true);
		listingSeatTrait.setCreatedBy("bijain");
		listingSeatTrait.setCreatedDate(calender);
		listingSeatTrait.setEditableInd(true);
		listingSeatTrait.setExtSystemSpecifiedInd(true);
		listingSeatTrait.setId(1l);
		listingSeatTrait.setLastUpdatedBy("bijain");
		listingSeatTrait.setLastUpdatedDate(calender);
		listingSeatTrait.setRecordVersionNumber(1l);
		listingSeatTrait.setSellerSpecifiedInd(true);
		listingSeatTrait.setSupplementSeatTraitId(1l);
		listingSeatTrait.setTicketId(1l);
		Assert.assertEquals(listingSeatTrait.getCreatedBy(),"bijain");
		Assert.assertEquals(listingSeatTrait.getLastUpdatedBy(),"bijain");
		Assert.assertTrue(listingSeatTrait.getActive());
		Assert.assertEquals(listingSeatTrait.getCreatedDate(),calender);
		Assert.assertTrue(listingSeatTrait.getEditableInd());
		Assert.assertTrue(listingSeatTrait.getExtSystemSpecifiedInd());
		Assert.assertEquals(listingSeatTrait.getId(),Long.valueOf(1l));
		Assert.assertEquals(listingSeatTrait.getLastUpdatedDate(),calender);
		Assert.assertEquals(listingSeatTrait.getRecordVersionNumber(),Long.valueOf(1l));
		Assert.assertTrue(listingSeatTrait.getSellerSpecifiedInd());
		Assert.assertEquals(listingSeatTrait.getSupplementSeatTraitId(),Long.valueOf(1l));
		Assert.assertEquals(listingSeatTrait.getTicketId(),Long.valueOf(1l));
		return listingSeatTrait;
	}
	
	@Test
	public StubTransSeatTrait mockStubTransSeatTrait(){
		StubTransSeatTrait stubTransSeatTrait = new StubTransSeatTrait();
		stubTransSeatTrait.setId(1l);
		stubTransSeatTrait.setCreatedBy("bijain");
		stubTransSeatTrait.setCreatedDate(calender);
		stubTransSeatTrait.setExtSystemSpecifiedInd(true);
		stubTransSeatTrait.setId(1l);
		stubTransSeatTrait.setLastUpdatedBy("bijain");
		stubTransSeatTrait.setLastUpdatedDate(calender);
		stubTransSeatTrait.setOrderId(1l);
		stubTransSeatTrait.setSellerSpecifiedInd(true);
		stubTransSeatTrait.setSupplementSeatTraitId(1l);
		Assert.assertEquals(stubTransSeatTrait.getCreatedBy(),"bijain");
		Assert.assertEquals(stubTransSeatTrait.getLastUpdatedBy(),"bijain");
		Assert.assertEquals(stubTransSeatTrait.getCreatedDate(),calender);
		Assert.assertTrue(stubTransSeatTrait.getExtSystemSpecifiedInd());
		Assert.assertEquals(stubTransSeatTrait.getId(),Long.valueOf(1l));
		Assert.assertEquals(stubTransSeatTrait.getLastUpdatedDate(),calender);
		Assert.assertEquals(stubTransSeatTrait.getOrderId(),Long.valueOf(1l));
		Assert.assertTrue(stubTransSeatTrait.getSellerSpecifiedInd());
		Assert.assertEquals(stubTransSeatTrait.getSupplementSeatTraitId(),Long.valueOf(1l));
		return stubTransSeatTrait;
	}
	
	
	@Test
	public TicketSeat mockTicketSeat(){
		TicketSeat tc = new TicketSeat();
		tc.setTicketSeatId(1l);
		tc.setSeatStatusId(2l);
		tc.setOrderPlacedInd(true);
		tc.setLastUpdatedBy("bijain");
		tc.setLastUpdatedDate(calender);
		tc.setCreatedBy("bijain");
		tc.setCreatedDate(calender);
		tc.setExternalSeatId("1");
		tc.setGeneralAdmissionInd(true);
		tc.setRowNumber("1");
		tc.setSeatDesc("1");
		tc.setSeatStatusId(1l);
		tc.setSectionName("1");
		tc.setTicketId(1l);
		tc.setTixListTypeId(1l);
		tc.setSeatNumber("1");
		Assert.assertEquals(tc.getCreatedBy(),"bijain");
		Assert.assertEquals(tc.getExternalSeatId(),"1");
		Assert.assertEquals(tc.getLastUpdatedBy(),"bijain");
		Assert.assertEquals(tc.getRowNumber(),"1");
		Assert.assertEquals(tc.getSeatDesc(),"1");
		Assert.assertEquals(tc.getSeatNumber(),"1");
		Assert.assertEquals(tc.getSectionName(),"1");
		Assert.assertEquals(tc.getCreatedDate(),calender);
		Assert.assertTrue(tc.getGeneralAdmissionInd());
		Assert.assertEquals(tc.getLastUpdatedDate(),calender);
		Assert.assertTrue(tc.getOrderPlacedInd());
		Assert.assertEquals(tc.getSeatStatusId(),Long.valueOf(1l));
		Assert.assertEquals(tc.getTicketId(),Long.valueOf(1l));
		Assert.assertEquals(tc.getTicketSeatId(),Long.valueOf(1l));
		Assert.assertEquals(tc.getTixListTypeId(),Long.valueOf(1l));
		return tc;
	}
	
	@Test
	public TransactionCancellation mockTransactionCancellation(){
		TransactionCancellation tc = new TransactionCancellation();
		tc.setId(1l);
		tc.setCancelledBy("bijain");
		tc.setDateAdded(calender);
		tc.setExtraInfo("order subbed");
		tc.setLastUpdatedBy("bijain");
		tc.setLastUpdatedDate(calender);
		tc.setReasonId(1l);
		tc.setTid(1l);
		Assert.assertEquals(tc.getCancelledBy(),"bijain");
		Assert.assertEquals(tc.getExtraInfo(),"order subbed");
		Assert.assertEquals(tc.getLastUpdatedBy(),"bijain");
		Assert.assertEquals(tc.getDateAdded(),calender);
		Assert.assertEquals(tc.getId(),Long.valueOf(1l));
		Assert.assertEquals(tc.getLastUpdatedDate(),calender);
		Assert.assertEquals(tc.getReasonId(),Long.valueOf(1l));
		Assert.assertEquals(tc.getTid(),Long.valueOf(1l));		
		return tc;
	}
	
	@Test
	public void mockCcTrans(){
		CcTrans cc = new CcTrans();
		cc.setId(1l);
		cc.setLastUpdatedBy("bijain");
		cc.setLastUpdatedDate(calender);
		cc.setTid(1l);
		Assert.assertEquals(cc.getLastUpdatedBy(),"bijain");
		Assert.assertEquals(cc.getLastUpdatedDate(),calender);
		Assert.assertEquals(cc.getId(),Long.valueOf(1l));
		Assert.assertEquals(cc.getTid(),Long.valueOf(1l));		
	}
	
	@Test
	public StubTransAdjHist mockStubTransAdjHist(){
		Money money = new Money(new BigDecimal(1), "USD");
		Long val = 1L;
		StubTransAdjHist stubTransAdjHist = new StubTransAdjHist(); 
		stubTransAdjHist.setTid(1l); 
		stubTransAdjHist.setActiveCsFlag(val);
		stubTransAdjHist.setAffiliateId(val);
		stubTransAdjHist.setAttentionFlag(true);
		stubTransAdjHist.setBidderCobrand("1");
		stubTransAdjHist.setBuyerFee("1");
		stubTransAdjHist.setBuyerFeeValAfterAdj(money);
		stubTransAdjHist.setBuyerFeeValBeforeAdj(money);
		stubTransAdjHist.setBuyerId(val);
		stubTransAdjHist.setBuyVAT(money);
		stubTransAdjHist.setCancelled(true);
		stubTransAdjHist.setCcId(val);
		stubTransAdjHist.setContactId(val);
		stubTransAdjHist.setCreatedBy("bijain");
		stubTransAdjHist.setCreatedDate(calender);
		stubTransAdjHist.setCurrency("USD");
		stubTransAdjHist.setDateAdded(calender);
		stubTransAdjHist.setDiscountCostAfterAdj(money);
		stubTransAdjHist.setDiscountCostBeforeAdj(money);
		stubTransAdjHist.setEventId(val);
		stubTransAdjHist.setGroupComments("hi");
		stubTransAdjHist.setHoldPaymentTypeId(val);
		stubTransAdjHist.setLastUpdatedBy("bijain");
		stubTransAdjHist.setLastUpdatedDate(calender);
		stubTransAdjHist.setLatestOrdProcStatus(val);
		stubTransAdjHist.setLogisticsMethodId(val);
		stubTransAdjHist.setLogisticVAT(money);
		stubTransAdjHist.setOrderCreatedBy("bijain");
		stubTransAdjHist.setOrderDateLastModified(calender);
		stubTransAdjHist.setOrderLastUpdatedBy("bijain");
		stubTransAdjHist.setPaymentTermsId(val);
		stubTransAdjHist.setPid(val);
		stubTransAdjHist.setPremiumFeesAfterAdj(money);
		stubTransAdjHist.setPremiumFeesBeforeAdj(money);
		stubTransAdjHist.setQuantity(val);
		stubTransAdjHist.setReasonCode(val);
		stubTransAdjHist.setRowDesc("1");
		stubTransAdjHist.setSaleMethod(val);
		stubTransAdjHist.setSeats("1");
		stubTransAdjHist.setSection("1");
		stubTransAdjHist.setSellerCobrand("1");
		stubTransAdjHist.setSellerComments("hi");
		stubTransAdjHist.setSellerConfirmed(true);
		stubTransAdjHist.setSellerFee("1");
		stubTransAdjHist.setSellerId(val);
		stubTransAdjHist.setSellerPaymentTypeId(val);
		stubTransAdjHist.setSellerPayoutAmountAtConfrm(money);
		stubTransAdjHist.setSellerPayoutAmtAfterAdj(money);
		stubTransAdjHist.setSellerPayoutAmtBeforeAdj(money);
		stubTransAdjHist.setSellFeeValAfterAdj(money);
		stubTransAdjHist.setSellVAT(money);
		stubTransAdjHist.setShipCostBeforeAdj(money);
		stubTransAdjHist.setShipCostAfterAdj(money);
		stubTransAdjHist.setStubTransAdjHistId(val);
		stubTransAdjHist.setTicketCostAfterAdj(money);
		stubTransAdjHist.setTicketCostBeforeAdj(money);
		stubTransAdjHist.setTicketId(val);
		stubTransAdjHist.setTid(val);
		stubTransAdjHist.setTotalCostAfterAdj(money);
		stubTransAdjHist.setTotalCostBeforeAdj(money);
		stubTransAdjHist.setSellerFeeValBeforeAdj(money);
		Assert.assertEquals(stubTransAdjHist.isCancelled(),true); 
		Assert.assertEquals(stubTransAdjHist.isAttentionFlag(),true); 
		Assert.assertEquals(stubTransAdjHist.isSellerConfirmed(),true);
		Assert.assertEquals(stubTransAdjHist.getBidderCobrand(),"1"); 
		Assert.assertEquals(stubTransAdjHist.getBuyerFee(),"1");
		Assert.assertEquals(stubTransAdjHist.getCreatedBy(),"bijain");
		Assert.assertEquals(stubTransAdjHist.getCurrency(),"USD");
		Assert.assertEquals(stubTransAdjHist.getGroupComments(),"hi");
		Assert.assertEquals(stubTransAdjHist.getLastUpdatedBy(),"bijain");
		Assert.assertEquals(stubTransAdjHist.getOrderCreatedBy(),"bijain");
		Assert.assertEquals(stubTransAdjHist.getOrderLastUpdatedBy(),"bijain");
		Assert.assertEquals(stubTransAdjHist.getRowDesc(),"1");
		Assert.assertEquals(stubTransAdjHist.getSeats(),"1");
		Assert.assertEquals(stubTransAdjHist.getSection(),"1");
		Assert.assertEquals(stubTransAdjHist.getSellerCobrand(),"1");
		Assert.assertEquals(stubTransAdjHist.getSellerComments(),"hi");
		Assert.assertEquals(stubTransAdjHist.getSellerFee(),"1");
		Assert.assertEquals(stubTransAdjHist.getActiveCsFlag(),val);
		Assert.assertEquals(stubTransAdjHist.getAffiliateId(),val);
		Assert.assertEquals(stubTransAdjHist.getBuyerFeeValAfterAdj(),money);
		Assert.assertEquals(stubTransAdjHist.getBuyerFeeValBeforeAdj(),money);
		Assert.assertEquals(stubTransAdjHist.getBuyerId(),val);
		Assert.assertEquals(stubTransAdjHist.getBuyVAT(),money);
		Assert.assertEquals(stubTransAdjHist.getCcId(),val);
		Assert.assertEquals(stubTransAdjHist.getContactId(),val);
		Assert.assertEquals(stubTransAdjHist.getCreatedDate(),calender);
		Assert.assertEquals(stubTransAdjHist.getDateAdded(),calender);
		Assert.assertEquals(stubTransAdjHist.getDiscountCostAfterAdj(),money);
		Assert.assertEquals(stubTransAdjHist.getDiscountCostBeforeAdj(),money);
		Assert.assertEquals(stubTransAdjHist.getEventId(),val);
		Assert.assertEquals(stubTransAdjHist.getHoldPaymentTypeId(),val);
		Assert.assertEquals(stubTransAdjHist.getLastUpdatedDate(),calender);
		Assert.assertEquals(stubTransAdjHist.getLatestOrdProcStatus(),val);
		Assert.assertEquals(stubTransAdjHist.getLogisticsMethodId(),val);
		Assert.assertEquals(stubTransAdjHist.getLogisticVAT(),money);
		Assert.assertEquals(stubTransAdjHist.getOrderDateLastModified(),calender);
		Assert.assertEquals(stubTransAdjHist.getPaymentTermsId(),val);
		Assert.assertEquals(stubTransAdjHist.getPid(),val);
		Assert.assertEquals(stubTransAdjHist.getPremiumFeesAfterAdj(),money);
		Assert.assertEquals(stubTransAdjHist.getPremiumFeesBeforeAdj(),money);
		Assert.assertEquals(stubTransAdjHist.getPremiumFeesBeforeAdj(),money);
		Assert.assertEquals(stubTransAdjHist.getQuantity(),val);
		Assert.assertEquals(stubTransAdjHist.getReasonCode(),val);
		Assert.assertEquals(stubTransAdjHist.getSaleMethod(),val);
		Assert.assertEquals(stubTransAdjHist.getSellerFeeValBeforeAdj(),money);
		Assert.assertEquals(stubTransAdjHist.getSellerId(),val);
		Assert.assertEquals(stubTransAdjHist.getSellerPaymentTypeId(),val);
		Assert.assertEquals(stubTransAdjHist.getSellerPayoutAmountAtConfrm(),money);
		Assert.assertEquals(stubTransAdjHist.getSellerPayoutAmtAfterAdj(),money);
		Assert.assertEquals(stubTransAdjHist.getSellerPayoutAmtBeforeAdj(),money);
		Assert.assertEquals(stubTransAdjHist.getSellFeeValAfterAdj(),money);
		Assert.assertEquals(stubTransAdjHist.getSellVAT(),money);
		Assert.assertEquals(stubTransAdjHist.getShipCostAfterAdj(),money);
		Assert.assertEquals(stubTransAdjHist.getShipCostBeforeAdj(),money);
		Assert.assertEquals(stubTransAdjHist.getStubTransAdjHistId(),val);
		Assert.assertEquals(stubTransAdjHist.getTicketCostAfterAdj(),money);
		Assert.assertEquals(stubTransAdjHist.getTicketCostBeforeAdj(),money);
		Assert.assertEquals(stubTransAdjHist.getTicketId(),val);
		Assert.assertEquals(stubTransAdjHist.getTid(),val);
		Assert.assertEquals(stubTransAdjHist.getTotalCostAfterAdj(),money);
		Assert.assertEquals(stubTransAdjHist.getTotalCostBeforeAdj(),money);
		return stubTransAdjHist;
	}
	

}
