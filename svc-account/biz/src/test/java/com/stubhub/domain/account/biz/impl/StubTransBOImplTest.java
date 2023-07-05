package com.stubhub.domain.account.biz.impl;

import static org.mockito.Matchers.argThat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.mockito.Matchers.any;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.stubhub.common.exception.InvalidArgumentException;
import com.stubhub.common.exception.RecordNotFoundForIdException;
import com.stubhub.domain.account.biz.intf.StubTransBO;
import com.stubhub.domain.account.common.util.OrderConstants;
import com.stubhub.domain.account.datamodel.dao.ListingSeatTraitDAO;
import com.stubhub.domain.account.datamodel.dao.OrderProcStatusAdapterDAO;
import com.stubhub.domain.account.datamodel.dao.StubTransAdjHistDAO;
import com.stubhub.domain.account.datamodel.dao.StubTransDAO;
import com.stubhub.domain.account.datamodel.dao.StubTransDetailDAO;
import com.stubhub.domain.account.datamodel.dao.StubTransFmDmDAO;
import com.stubhub.domain.account.datamodel.dao.StubTransSeatTraitDAO;
import com.stubhub.domain.account.datamodel.dao.StubTransTmpDAO;
import com.stubhub.domain.account.datamodel.dao.TransactionCancellationDAO;
import com.stubhub.domain.account.datamodel.dao.impl.ListingSeatTraitDAOImpl;
import com.stubhub.domain.account.datamodel.dao.impl.OrderProcStatusAdapterDAOImpl;
import com.stubhub.domain.account.datamodel.dao.impl.StubTransAdjHistDAOImpl;
import com.stubhub.domain.account.datamodel.dao.impl.StubTransDAOImpl;
import com.stubhub.domain.account.datamodel.dao.impl.StubTransDetailDAOImpl;
import com.stubhub.domain.account.datamodel.dao.impl.StubTransFmDmDAOImpl;
import com.stubhub.domain.account.datamodel.dao.impl.StubTransSeatTraitDAOImpl;
import com.stubhub.domain.account.datamodel.dao.impl.StubTransTmpDAOImpl;
import com.stubhub.domain.account.datamodel.dao.impl.TransactionCancellationDAOImpl;
import com.stubhub.domain.account.datamodel.entity.ListingSeatTrait;
import com.stubhub.domain.account.datamodel.entity.StubTrans;
import com.stubhub.domain.account.datamodel.entity.StubTransDetail;
import com.stubhub.domain.account.datamodel.entity.StubTransFmDm;
import com.stubhub.domain.account.datamodel.entity.StubTransTmp;
import com.stubhub.domain.account.datamodel.entity.TransactionCancellation;
import com.stubhub.domain.account.intf.CSOrderDetailsResponse;
import com.stubhub.domain.account.intf.DeliveryResponse;
import com.stubhub.domain.account.intf.OrdersResponse;
import com.stubhub.domain.account.intf.SubstitutionRequest;
import com.stubhub.domain.account.intf.TransactionResponse;
import com.stubhub.domain.inventory.v2.DTO.ListingResponse;
import com.stubhub.domain.inventory.v2.listings.intf.DeliveryMethod;
import com.stubhub.newplatform.common.entity.Money;
import com.stubhub.newplatform.persistence.adapter.UTCCalendarToTimestampAdapter;

public class StubTransBOImplTest {

	private StubTransBO stubTransBO;
	private StubTransDAO stubTransDAO;
	private StubTransFmDmDAO stubTransFmDmDAO;
	private StubTransTmpDAO stubTransTmpDAO;	
	private OrderProcStatusAdapterDAO orderProcStatusAdapterDAO;
	private ListingSeatTraitDAO listingSeatTraitDAO;
	private StubTransSeatTraitDAO stubTransSeatTraitDAO;
	private StubTransDetailDAO stubTransDetailDAO;
	private TransactionCancellationDAO transactionCancellationDAO;
	private StubTransAdjHistDAO stubTransAdjHistDAO;
	private Long orderId = 1l;
	private StubTrans stubTrans;
	private Long contactId = 200L;
	private String csrRep = "BJ";
	private Calendar dateAdded = Calendar.getInstance();


	@BeforeTest
	public void setUp() {
		stubTransBO = new StubTransBOImpl();
		stubTransDAO = Mockito.mock(StubTransDAOImpl.class);
		stubTransFmDmDAO = Mockito.mock(StubTransFmDmDAOImpl.class);
		stubTransTmpDAO = Mockito.mock(StubTransTmpDAOImpl.class);
		orderProcStatusAdapterDAO = Mockito.mock(OrderProcStatusAdapterDAOImpl.class);
		listingSeatTraitDAO = Mockito.mock(ListingSeatTraitDAOImpl.class);
		stubTransSeatTraitDAO = Mockito.mock(StubTransSeatTraitDAOImpl.class);
		stubTransDetailDAO = Mockito.mock(StubTransDetailDAOImpl.class);
		transactionCancellationDAO = Mockito.mock(TransactionCancellationDAOImpl.class);
		stubTransAdjHistDAO = Mockito.mock(StubTransAdjHistDAOImpl.class);
		ReflectionTestUtils.setField(stubTransBO, "stubTransDAO", stubTransDAO);
		ReflectionTestUtils.setField(stubTransBO, "orderProcStatusAdapterDAO", orderProcStatusAdapterDAO);
		ReflectionTestUtils.setField(stubTransBO, "stubTransFmDmDAO", stubTransFmDmDAO);
		ReflectionTestUtils.setField(stubTransBO, "stubTransTmpDAO", stubTransTmpDAO);
		ReflectionTestUtils.setField(stubTransBO, "listingSeatTraitDAO", listingSeatTraitDAO);
		ReflectionTestUtils.setField(stubTransBO, "stubTransSeatTraitDAO", stubTransSeatTraitDAO);
		ReflectionTestUtils.setField(stubTransBO, "stubTransDetailDAO", stubTransDetailDAO);
		ReflectionTestUtils.setField(stubTransBO, "transactionCancellationDAO", transactionCancellationDAO);
		ReflectionTestUtils.setField(stubTransBO, "stubTransAdjHistDAO", stubTransAdjHistDAO);
		stubTrans = new StubTrans();
		stubTrans.setOrderId(orderId);
	}

	@Test
	public void testGetOrderProcSubStatus(){
		List<StubTrans> list = new ArrayList<StubTrans>();
		StubTrans stubTrans = new StubTrans();
		stubTrans.setOrderId(orderId);
		stubTrans.setOrderProcSubStatusCode(1L);
		list.add(stubTrans);
		Mockito.when(stubTransDAO.getOrderProcSubStatusCode(orderId)).thenReturn(list);
		Assert.assertNotNull(stubTransBO.getOrderProcSubStatus(orderId));
	}

	@Test
	public void testUpdateBuyerContactId(){
		List<StubTrans> list = new ArrayList<StubTrans>();
		StubTrans stubTrans = new StubTrans();
		stubTrans.setOrderId(orderId);
		stubTrans.setOrderProcSubStatusCode(1L);
		list.add(stubTrans);
		Mockito.when(stubTransDAO.updateBuyerContactId(orderId, contactId, csrRep, dateAdded)).thenReturn(1);
		Assert.assertNotNull(stubTransBO.updateBuyerContactId(orderId, contactId, csrRep, dateAdded));
	}
	@Test
	public void testGetBuyerFlipCount(){
		Mockito.when(stubTransDAO.getBuyerFlipCount(123L)).thenReturn(2);
		Assert.assertNotNull(stubTransBO.getBuyerFlipCount(123L));
	}
	@Test
	public void testGetById() throws RecordNotFoundForIdException{
		Mockito.when(stubTransDAO.getById(123L)).thenReturn(mockStubTrans());
		Assert.assertNotNull(stubTransBO.getStubTransById(123L));
	}

	@Test
	public void testGetDrpOrderCount(){
		// When All the counts come back as 0, Should not throw any ArithmeticException. The calculation logic should not happen.
		Mockito.when(stubTransDAO.getSelStubTicketCount(123L)).thenReturn(0);
		Mockito.when(stubTransDAO.getSelTransTikCount(123L)).thenReturn(0);
		Mockito.when(stubTransDAO.getSelPayTicketCount(123L)).thenReturn(0);
		Assert.assertNotNull(stubTransBO.getDrpOrderCount(123L));

		// The value should be rounded to example 0.10 if its 0.10100
		// Seller_trans_cc TID is "0" and Seller_Payment TID count is "1000"
		BigDecimal drpRate = new BigDecimal("0.10");
		Mockito.when(stubTransDAO.getSelStubTicketCount(123L)).thenReturn(98746);
		Mockito.when(stubTransDAO.getSelTransTikCount(123L)).thenReturn(0);
		Mockito.when(stubTransDAO.getSelPayTicketCount(123L)).thenReturn(10000);
		Assert.assertEquals(stubTransBO.getDrpOrderCount(123L),drpRate);


		// The value should be rounded to example 0.10 if its 0.10100
		// Seller_trans_cc TID count is "1000" and Seller_Payment TID count is "0"
		BigDecimal drpRate1 = new BigDecimal("0.10");
		Mockito.when(stubTransDAO.getSelStubTicketCount(123L)).thenReturn(98746);
		Mockito.when(stubTransDAO.getSelTransTikCount(123L)).thenReturn(10000);
		Mockito.when(stubTransDAO.getSelPayTicketCount(123L)).thenReturn(0);
		Assert.assertEquals(stubTransBO.getDrpOrderCount(123L),drpRate1);


		// Seller_trans_cc TID count is "0" and Seller_Payment TID count is "0" and aslo Stub_Trans ID count is "0" which 
		//unlikely to happen.
		BigDecimal drpRate2 = new BigDecimal("0");
		Mockito.when(stubTransDAO.getSelStubTicketCount(123L)).thenReturn(0);
		Mockito.when(stubTransDAO.getSelTransTikCount(123L)).thenReturn(0);
		Mockito.when(stubTransDAO.getSelPayTicketCount(123L)).thenReturn(0);
		Assert.assertEquals(stubTransBO.getDrpOrderCount(123L),drpRate2);

	}
	@Test
	public void testUpdateOrder() throws RecordNotFoundForIdException{
		StubTrans stubTrans = new StubTrans();
		stubTrans.setOrderId(orderId);
		stubTrans.setOrderProcSubStatusCode(1L);

		OrdersResponse orders = new OrdersResponse();
		List<CSOrderDetailsResponse> list = new ArrayList<CSOrderDetailsResponse>();
		CSOrderDetailsResponse order = new CSOrderDetailsResponse();
		DeliveryResponse delivery = new DeliveryResponse();
		delivery.setOrderProcSubStatusCode("7");
		TransactionResponse transaction = new TransactionResponse();
		transaction.setOrderId(orderId.toString());
		transaction.setCancelled(true);
		order.setDelivery(delivery);
		order.setTransaction(transaction);
		list.add(order);
		orders.setOrder(list);
		Mockito.when(orderProcStatusAdapterDAO.updateOrderStatusByTransId(orderId, "bijain", Long.parseLong(orders.getOrder().get(0).getDelivery().getOrderProcSubStatusCode()))).thenReturn(2L);
		Mockito.when(stubTransDAO.getById(orderId)).thenReturn(stubTrans);
		Assert.assertNotNull(stubTransBO.updateOrder("bijain", orders));
	}

	@Test
	public void testValidateOrderforSubstitution() throws RecordNotFoundForIdException, InvalidArgumentException{
		SubstitutionOrderBOTest sbo = new SubstitutionOrderBOTest();
		Mockito.when(stubTransDAO.getById(orderId)).thenReturn(sbo.mockStubTrans());
		Assert.assertNotNull(stubTransBO.validateOrderforSubstitution(orderId));
		Assert.assertEquals(stubTransDAO.getById(orderId).getOrderId(), orderId);
	}

	@Test
	public void testCreateStubTrans(){
		//SubstitutionOrderBOTest sbo = new SubstitutionOrderBOTest();
		Mockito.when(this.stubTransDAO.persist(any(StubTrans.class))).thenReturn(stubTrans);
		Mockito.when(this.stubTransTmpDAO.persist(any(StubTransTmp.class))).thenReturn(Long.MIN_VALUE);
		//Mockito.when(stubTransBO.createStubTrans(sbo.mockSubsRequest(), sbo.mockStubTrans(),sbo.mockListingResponse(),"",null)).thenReturn(stubTrans);
		//Mockito.when(stubTransBO.createStubTransTmpFromStubTrans(stubTrans)).thenReturn(Long.MIN_VALUE);
		//Assert.assertNotNull(stubTransBO.createStubTrans(sbo.mockSubsRequest(), sbo.mockStubTrans(),sbo.mockListingResponse(),"",null));
		StubTrans st = new StubTrans();
		st.setOrderId(123L);

		SubstitutionRequest request = new SubstitutionRequest();
		request.setDeliveryMethodId("1");
		ListingResponse newListing = new ListingResponse();
		List<DeliveryMethod> deliveryMethods = new ArrayList<DeliveryMethod>();

		DeliveryMethod dm = new DeliveryMethod();
		dm.setId(1L);
		dm.setEstimatedDeliveryTime("2015-09-34T12:00:00.000Z");
		deliveryMethods.add(dm);

		DeliveryMethod dm2 = new DeliveryMethod();
		dm2.setId(2L);
		dm2.setEstimatedDeliveryTime("2015-09-34T12:00:00.000Z");
		deliveryMethods.add(dm2);

		newListing.setDeliveryMethods(deliveryMethods );

		Money shipCost = new Money();
		shipCost.setCurrency("USD");
		shipCost.setAmount(new BigDecimal(10.10d));
		stubTrans.setShipCost(shipCost);

		Money discountCost = new Money();
		discountCost.setCurrency("USD");

		stubTrans.setDiscountCost(discountCost);
		//Mockito.when(stubTransBO.createStubTrans(request, st,newListing,null,null)).thenReturn(stubTrans);
		Assert.assertNotNull(stubTransBO.createStubTrans(request,st,newListing,null,null, null));
		st.setBuyerId(11111L);
		Assert.assertNotNull(stubTransBO.createStubTrans(request,st,newListing,null,null, null));

		request.setListingId("100000");
		Assert.assertNotNull(stubTransBO.createStubTrans(request,st,newListing,null,null, null));
		request.setQuantity("100");
		Assert.assertNotNull(stubTransBO.createStubTrans(request,st,newListing,null,null, null));
		st.setCcId(100l);
		Assert.assertNotNull(stubTransBO.createStubTrans(request,st,newListing,null,null, null));
		st.setBidderCobrand("test");
		newListing.setSection("abc");
		Assert.assertNotNull(stubTransBO.createStubTrans(request,st,newListing,null,null, null));
		st.setAffiliateId(1000L);
		st.setBuyerFee("100");
		st.setContactId(111L);
		st.setGroupComments("abc");
		st.setBobId(1111L);
		request.setTicketCost(new Money(new BigDecimal(100),"$"));
		request.setSellerFeeVal(new Money(new BigDecimal(100),"$"));
		request.setSellerPayoutAmount(new Money(new BigDecimal(100),"$"));
		request.setDiscountCost(new Money(new BigDecimal(100),"$"));
		request.setShipCost(new Money(new BigDecimal(100),"$"));
		request.setVatSellFee(new Money(new BigDecimal(100),"$"));
		request.setVatBuyFee(new Money(new BigDecimal(100),"$"));
		request.setTotalCost(new Money(new BigDecimal(100),"$"));
		request.setAddOnFee(new Money(new BigDecimal(100),"$"));
		newListing.setSellerId(100L);
		st.setBuyerAuthenticatedSessionGuid("12344");
		Assert.assertNotNull(stubTransBO.createStubTrans(request,st,newListing,null,null, null));

	}

	//	@Test
	//	public void testCreateStubTransTemp(){
	//		Mockito.when(this.stubTransTmpDAO.persist(any(StubTransTmp.class))).thenReturn(Long.MIN_VALUE);
	//		StubTrans st = new StubTrans();
	//		st.setOrderId(123L);
	//		Money ticketCost = new Money(new BigDecimal(10),"$");
	//		st.setTicketCost(ticketCost);
	//		
	//		SubstitutionRequest request = new SubstitutionRequest();
	//		request.setDeliveryMethodId("1");
	//		ListingResponse newListing = new ListingResponse();
	//		List<DeliveryMethod> deliveryMethods = new ArrayList<DeliveryMethod>();
	//		
	//		DeliveryMethod dm = new DeliveryMethod();
	//		dm.setId(1L);
	//		dm.setEstimatedDeliveryTime("2015-09-34T12:00:00.000Z");
	//		deliveryMethods.add(dm);
	//		
	//		DeliveryMethod dm2 = new DeliveryMethod();
	//		dm2.setId(2L);
	//		dm2.setEstimatedDeliveryTime("2015-09-34T12:00:00.000Z");
	//		deliveryMethods.add(dm2);
	//		
	//		newListing.setDeliveryMethods(deliveryMethods );
	//		
	//		Money shipCost = new Money();
	//		shipCost.setCurrency("USD");
	//		shipCost.setAmount(new BigDecimal(10.10d));
	//		stubTrans.setShipCost(shipCost);
	//		
	//		Money discountCost = new Money();
	//		discountCost.setCurrency("USD");
	//		
	//		stubTrans.setDiscountCost(discountCost);
	//		//Mockito.when(stubTransBO.createStubTrans(request, st,newListing,null,null)).thenReturn(stubTrans);
	//		Assert.assertNotNull(stubTransBO.createStubTransTmpFromStubTrans(st));
	//		st.setTicketCost(null);
	//		Assert.assertNotNull(stubTransBO.createStubTransTmpFromStubTrans(st));
	//		Assert.assertNull(stubTransBO.createStubTransTmpFromStubTrans(null));
	//		
	//		st.setTicketCost(new Money());
	//		Assert.assertNotNull(stubTransBO.createStubTransTmpFromStubTrans(st));
	//		
	//	}


	@Test
	public void testCreateStubTransWithException(){
		//SubstitutionOrderBOTest sbo = new SubstitutionOrderBOTest();
		//Mockito.when(stubTransBO.createStubTrans(sbo.mockSubsRequest(), sbo.mockStubTrans(),sbo.mockListingResponse(),"",null)).thenReturn(stubTrans);
		//Assert.assertNotNull(stubTransBO.createStubTrans(sbo.mockSubsRequest(), sbo.mockStubTrans(),sbo.mockListingResponse(),"",null));
		StubTrans st = new StubTrans();
		st.setOrderId(123L);
		SubstitutionRequest request = new SubstitutionRequest();
		request.setDeliveryMethodId("1");
		ListingResponse newListing = new ListingResponse();
		List<DeliveryMethod> deliveryMethods = new ArrayList<DeliveryMethod>();
		DeliveryMethod dm = new DeliveryMethod();
		dm.setId(1L);
		deliveryMethods.add(dm);
		newListing.setDeliveryMethods(deliveryMethods );
		//Mockito.when(stubTransBO.createStubTrans(request, st,newListing,null,null)).thenReturn(stubTrans);
		Assert.assertNotNull(stubTransBO.createStubTrans(request,st,newListing,null,null, null));
	}
	@Test
	public void testCreateStubTransFmDm(){
		SubstitutionOrderBOTest sbo = new SubstitutionOrderBOTest();
		Mockito.when(stubTransFmDmDAO.persist((StubTransFmDm)argThat(getMatcher()))).thenReturn(orderId);
		Assert.assertNotNull(stubTransBO.createStubTransFmDm(sbo.mockSubsRequest(), orderId));
		Assert.assertEquals(stubTransFmDmDAO.persist(mockStubTransFmDm()), orderId);
	}

	@Test
	public void testCreateStubTransTmp(){
		SubstitutionOrderBOTest sbo = new SubstitutionOrderBOTest();
		Mockito.when(stubTransTmpDAO.persist((StubTransTmp)argThat(getMatcher()))).thenReturn(orderId);
		Assert.assertNotNull(stubTransBO.createStubTransTmp(sbo.mockSubsRequest(), sbo.mockStubTrans(), "13,14", "1", "USD", "1"));
		Assert.assertEquals(stubTransTmpDAO.persist(mockStubTransTmp()), orderId);
	}


	@Test
	public void testCreateStubTransSeatTrait(){
		SubstitutionOrderBOTest sbo = new SubstitutionOrderBOTest();
		List<TicketSeatUtil> lst = new ArrayList<TicketSeatUtil>();
		lst.add(mockTicketSeatUtil());
		Mockito.when(stubTransDetailDAO.persist((StubTransDetail)argThat(getMatcher()))).thenReturn(orderId);
		Assert.assertNotNull(stubTransBO.createStubTransDetail(sbo.mockSubsRequest(),orderId,sbo.mockListingResponse(),lst));
		Assert.assertEquals(stubTransDetailDAO.persist(mockStubTransDetail()), orderId);
	}

	@Test
	public void testCancelOrder(){
		SubstitutionOrderBOTest sbo = new SubstitutionOrderBOTest();
		StubTrans st = sbo.mockStubTrans();
		Mockito.when(stubTransDAO.persist(st)).thenReturn(stubTrans);
		Mockito.when(orderProcStatusAdapterDAO.updateOrderStatusByTransId(orderId, "bijain", OrderConstants.CANCELLED_AND_SUBBED)).thenReturn(1l);		
		Mockito.when(transactionCancellationDAO.persist(mockTransactionCancellation())).thenReturn(orderId);
		Assert.assertEquals(stubTransBO.cancelOrder(st,"bijain"), true);
	}


	@Test
	public void testGetFulfillmentMethodByTid(){
		List<StubTransFmDm> list = new ArrayList<StubTransFmDm>();
		StubTransFmDm  stf = new StubTransFmDm();
		stf.setFulfillmentMethodId(10L);
		stf.setTid(111L);
		list.add(stf);
		Mockito.when(stubTransFmDmDAO.getFmDmByTids(Mockito.anyList())).thenReturn(list);
		Assert.assertEquals(stubTransBO.getFulfillmentMethodIdByTid(111L),new Long(10L));

	}

	public TransactionCancellation mockTransactionCancellation(){
		TransactionCancellation tc = new TransactionCancellation();
		tc.setId(1l);
		tc.setCancelledBy("bijain");
		tc.setDateAdded(UTCCalendarToTimestampAdapter.getNewUTCCalendarInstanceStatic());
		tc.setExtraInfo("order subbed");
		tc.setLastUpdatedBy("bijain");
		tc.setLastUpdatedDate(UTCCalendarToTimestampAdapter.getNewUTCCalendarInstanceStatic());
		tc.setReasonId(1l);
		tc.setTid(1l);
		return tc;
	}


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
		stubTransDetail.setCreatedDate(UTCCalendarToTimestampAdapter.getNewUTCCalendarInstanceStatic());
		stubTransDetail.setGeneralAdmissionIndicator(1l);
		stubTransDetail.setLastUpdatedBy("bijain");
		stubTransDetail.setLastUpdatedDate(UTCCalendarToTimestampAdapter.getNewUTCCalendarInstanceStatic());
		stubTransDetail.setRowNumber("1");
		stubTransDetail.setSeatNumber("1");
		stubTransDetail.setSectionName("1");
		stubTransDetail.setTicketListTypeId(1l);
		stubTransDetail.setTicketSeatId(1l);
		stubTransDetail.setTid(1l);
		return stubTransDetail;
	}

	public TicketSeatUtil mockTicketSeatUtil(){
		Long val = 1l;
		TicketSeatUtil ticketSeatUtil = new TicketSeatUtil();
		ticketSeatUtil.setRow("1");
		ticketSeatUtil.setSeatId(1l);
		ticketSeatUtil.setSeatNumber("1");
		ticketSeatUtil.setSection("1");
		ticketSeatUtil.setTicketListTypeId(val);
		ticketSeatUtil.setVenueConfigSectionId(val);
		Assert.assertEquals(ticketSeatUtil.getRow(),"1");
		Assert.assertEquals(ticketSeatUtil.getSeatNumber(),"1");
		Assert.assertEquals(ticketSeatUtil.getSection(),"1");
		Assert.assertEquals(ticketSeatUtil.getSeatId(),val);
		Assert.assertEquals(ticketSeatUtil.getTicketListTypeId(),val);
		Assert.assertEquals(ticketSeatUtil.getVenueConfigSectionId(),val);
		return ticketSeatUtil;
	}

	@Test
	public void testCreateStubTransDetail(){
		ListingSeatTrait listingSeatTrait = mockListingSeatTrait();
		List<ListingSeatTrait> listingSeatTraitList = new ArrayList<ListingSeatTrait>();
		listingSeatTraitList.add(listingSeatTrait);
		Mockito.when(listingSeatTraitDAO.getByListingId(1l)).thenReturn(listingSeatTraitList);
		Assert.assertNotNull(stubTransBO.createStubTransSeatTrait(1l,1l,"bijain"));
	}

	public ListingSeatTrait mockListingSeatTrait(){
		ListingSeatTrait listingSeatTrait = new ListingSeatTrait();
		listingSeatTrait.setId(1l);
		listingSeatTrait.setTicketId(1l);
		listingSeatTrait.setActive(true);
		listingSeatTrait.setCreatedBy("bijain");
		listingSeatTrait.setCreatedDate(UTCCalendarToTimestampAdapter.getNewUTCCalendarInstanceStatic());
		listingSeatTrait.setEditableInd(true);
		listingSeatTrait.setExtSystemSpecifiedInd(true);
		listingSeatTrait.setId(1l);
		listingSeatTrait.setLastUpdatedBy("bijain");
		listingSeatTrait.setLastUpdatedDate(UTCCalendarToTimestampAdapter.getNewUTCCalendarInstanceStatic());
		listingSeatTrait.setRecordVersionNumber(1l);
		listingSeatTrait.setSellerSpecifiedInd(true);
		listingSeatTrait.setSupplementSeatTraitId(1l);
		listingSeatTrait.setTicketId(1l);
		return listingSeatTrait;
	}

	public StubTransFmDm mockStubTransFmDm(){
		StubTransFmDm stubTransFmDm = new StubTransFmDm();
		stubTransFmDm.setId(1l);
		stubTransFmDm.setActive(true);
		stubTransFmDm.setCreatedBy("bijain");
		stubTransFmDm.setCreatedDate(UTCCalendarToTimestampAdapter.getNewUTCCalendarInstanceStatic());
		stubTransFmDm.setDeliveryMethodId(1l);
		stubTransFmDm.setFulfillmentMethodId(1l);
		stubTransFmDm.setLastUpdatedBy("bijain");
		stubTransFmDm.setLastUpdatedDate(UTCCalendarToTimestampAdapter.getNewUTCCalendarInstanceStatic());
		stubTransFmDm.setLmsLocationId(1l);
		stubTransFmDm.setLogisticsTypeId(1l);
		stubTransFmDm.setTid(1l);
		return stubTransFmDm;
	}


	public StubTransTmp mockStubTransTmp(){
		StubTransTmp transTemp = new StubTransTmp();
		GregorianCalendar calender = UTCCalendarToTimestampAdapter.getNewUTCCalendarInstanceStatic();
		//transTemp.setBuyerFee(request.getBuyerFeeVal().);
		//transTemp.setBuyerFeeVal(request.getBuyerFeeVal().getAmount());
		transTemp.setBidderCobrand("1");
		transTemp.setBuyerId(1L);
		//transTemp.setCcId(request.get.getUserCcId());
		transTemp.setContactId(1L);
		transTemp.setDateAdded(calender);
		transTemp.setId(1L);
		return transTemp;
	}


	public StubTrans mockStubTrans(){
		StubTrans transTemp = new StubTrans();
		GregorianCalendar calender = UTCCalendarToTimestampAdapter.getNewUTCCalendarInstanceStatic();
		//transTemp.setBuyerFee(request.getBuyerFeeVal().);
		//transTemp.setBuyerFeeVal(request.getBuyerFeeVal().getAmount());
		transTemp.setBidderCobrand("1");
		transTemp.setBuyerId(1L);
		//transTemp.setCcId(request.get.getUserCcId());
		transTemp.setContactId(1L);
		transTemp.setDateAdded(calender);
		transTemp.setOrderId(1L);
		return transTemp;
	}

	private BaseMatcher getMatcher() {
		BaseMatcher matcher = new BaseMatcher() {
			@Override
			public boolean matches(Object item) {
				return true;
			}
			@Override
			public void describeTo(Description description) {
			}
		};
		return matcher;
	}
}