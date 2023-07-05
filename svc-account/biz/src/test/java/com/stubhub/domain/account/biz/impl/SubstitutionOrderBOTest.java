package com.stubhub.domain.account.biz.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.stubhub.domain.account.biz.intf.InventoryBO;
import com.stubhub.domain.account.biz.intf.ListingBO;
import com.stubhub.domain.account.biz.intf.StubTransBO;
import com.stubhub.domain.account.biz.intf.SubstitutionOrderBO;
import com.stubhub.domain.account.biz.intf.UsedDiscountBO;
import com.stubhub.domain.account.biz.intf.UserContactBiz;
import com.stubhub.domain.account.datamodel.dao.CcTransDAO;
import com.stubhub.domain.account.datamodel.dao.ListingDAO;
import com.stubhub.domain.account.datamodel.dao.OrderProcStatusAdapterDAO;
import com.stubhub.domain.account.datamodel.dao.StubTransDetailDAO;
import com.stubhub.domain.account.datamodel.dao.UserDAO;
import com.stubhub.domain.account.datamodel.dao.impl.CcTransDAOImpl;
import com.stubhub.domain.account.datamodel.dao.impl.ListingDAOImpl;
import com.stubhub.domain.account.datamodel.dao.impl.OrderProcStatusAdapterDAOImpl;
import com.stubhub.domain.account.datamodel.dao.impl.StubTransDetailDAOImpl;
import com.stubhub.domain.account.datamodel.dao.impl.UserDAOImpl;
import com.stubhub.domain.account.datamodel.entity.InventoryData;
import com.stubhub.domain.account.datamodel.entity.StubTrans;
import com.stubhub.domain.account.datamodel.entity.StubTransDetail;
import com.stubhub.domain.account.datamodel.entity.UserContact;
import com.stubhub.domain.account.intf.SubstitutionRequest;
import com.stubhub.domain.account.intf.SubstitutionResponse;
import com.stubhub.domain.inventory.common.entity.SaleMethod;
import com.stubhub.domain.inventory.v2.DTO.ListingResponse;
import com.stubhub.domain.inventory.v2.DTO.Product;
import com.stubhub.domain.inventory.v2.enums.ProductType;
import com.stubhub.integration.partnerorder.entity.PartnerOrderTicket;
import com.stubhub.integration.partnerorder.entity.PurchaseInventoryRequest;
import com.stubhub.newplatform.common.entity.Money;

public class SubstitutionOrderBOTest {
	
	@Mock
	private SubstitutionOrderBO substitutionOrderBO;
	
	@Mock
	private StubTransBO stubTransBO;
	
	@Mock
	private ListingBO listingBO;
	
	@Mock
	private OrderProcStatusAdapterDAO orderProcStatusAdapterDAO;
	
	@Mock
	private CcTransDAO ccTransDAO;
	
	@Mock
	private StubTransDetailDAO stubTransDetailDAO;
	
	@Mock
	private UsedDiscountBO usedDiscountBO;
	
	@Mock
	private InventoryBO inventoryBO;
	
	@Mock
	private QueryResponse newListingAdditionals;
	
	
	private Long val = 1l;
	
	private static String operatorId = "sf";
	@Mock
	private UserContactBiz userContactBiz;
	
	private UserDAO userDAO;
	
	private ListingDAO listingDAO;
	

	@BeforeTest
	public void setUp() {
		substitutionOrderBO = new SubstitutionOrderBOImpl();
		stubTransBO = Mockito.mock(StubTransBOImpl.class);
		listingBO = Mockito.mock(ListingBOImpl.class);
		ccTransDAO = Mockito.mock(CcTransDAOImpl.class);
		usedDiscountBO = Mockito.mock(UsedDiscountBOImpl.class);
		inventoryBO = Mockito.mock(InventoryBOImpl.class);
		stubTransDetailDAO = Mockito.mock(StubTransDetailDAOImpl.class);
		userContactBiz =  Mockito.mock(UserContactBizImpl.class);
		newListingAdditionals = Mockito.mock(QueryResponse.class);
		orderProcStatusAdapterDAO =Mockito.mock(OrderProcStatusAdapterDAOImpl.class);;
		userDAO = Mockito.mock(UserDAOImpl.class);
		listingDAO = Mockito.mock(ListingDAOImpl.class);
	
		ReflectionTestUtils.setField(substitutionOrderBO, "stubTransBO", stubTransBO);
		ReflectionTestUtils.setField(substitutionOrderBO, "listingBO", listingBO);
		ReflectionTestUtils.setField(substitutionOrderBO, "ccTransDAO", ccTransDAO);
		ReflectionTestUtils.setField(substitutionOrderBO, "usedDiscountBO", usedDiscountBO);
		ReflectionTestUtils.setField(substitutionOrderBO, "stubTransDetailDAO", stubTransDetailDAO);
		ReflectionTestUtils.setField(substitutionOrderBO, "inventoryBO", inventoryBO);
		ReflectionTestUtils.setField(substitutionOrderBO, "userContactBiz", userContactBiz);
		ReflectionTestUtils.setField(substitutionOrderBO, "orderProcStatusAdapterDAO", orderProcStatusAdapterDAO);
		ReflectionTestUtils.setField(substitutionOrderBO, "userDAO", userDAO);
		ReflectionTestUtils.setField(substitutionOrderBO, "listingDAO", listingDAO);
		
	}
	
	private StubTrans getStubTrans(Long id) {
		StubTrans st = new StubTrans();
		st.setOrderId(id);
		st.setSellerId(id);
		st.setSellerPaymentTypeId(id);
		return st;
	}
	
	private StubTrans getNewStubTrans() {
		StubTrans st = new StubTrans();
		st.setOrderId(val);
		st.setSellerId(val);
		st.setSellerPaymentTypeId(val);
		return st;
	}
	
	private ListingResponse getListingResponse() {
		ListingResponse ls = new ListingResponse();
		ls.setSellerId(val);
		ls.setId(val.toString());
		return ls;
	}
	
	private List<TicketSeatUtil> getTicketsList() {
		List<TicketSeatUtil> l = new ArrayList<TicketSeatUtil>();
		return l;
	}
	
	@Test
	public void testCreateOrderHappyPath() throws Exception {
		SubstitutionRequest request = new SubstitutionRequest() ;
		request.setListingId(val.toString());
		
		StubTrans st = getStubTrans(val);
		StubTrans nst = getNewStubTrans();
		ListingResponse ls = getListingResponse();
		List<TicketSeatUtil> tl = getTicketsList();
		UserContact buyerUsrCont = new UserContact();
		buyerUsrCont.setEmail("someemail@email.com");
		when(stubTransBO.validateOrderforSubstitution(val)).thenReturn(st);
		when(listingBO.validateListingforSubstitution(request)).thenReturn(ls);
		when(userContactBiz.getUserContactById(any(Long.class))).thenReturn(buyerUsrCont);
		when(listingBO.allocateSeats(any(ListingResponse.class), any(SubstitutionRequest.class), any(String[].class))).thenReturn(tl);
		when(userDAO.getUserSellerPaymentType(1L)).thenReturn(2L);
		when(listingDAO.updateListingPaymentType(Long.valueOf(ls.getId()), 1L)).thenReturn(1);
		when(stubTransBO.createStubTrans(any(SubstitutionRequest.class), any(StubTrans.class), any(ListingResponse.class), any(String.class), any(QueryResponse.class), any(Long.class))).thenReturn(nst);
		when(orderProcStatusAdapterDAO.updateOrderStatusByTransId(any(Long.class), any(String.class), any(Long.class))).thenReturn(43L);
		Long res = substitutionOrderBO.createOrder(request, val.toString(), operatorId, null);
		
	    Assert.assertNotNull(res);
	    Assert.assertTrue(res.equals(val));
	}
	
	
	@Test
	public void testCreateOrderHappyPathWithDefaultContactEmail() throws Exception {
		SubstitutionRequest request = new SubstitutionRequest() ;
		request.setListingId(val.toString());
		
		StubTrans st = getStubTrans(val);
		StubTrans nst = getNewStubTrans();
		ListingResponse ls = getListingResponse();
		List<TicketSeatUtil> tl = getTicketsList();
		UserContact buyerUsrCont = new UserContact();

		UserContact buyerUsrDefaultCont = new UserContact();
		buyerUsrDefaultCont.setEmail("someemail@email.com");
		
		when(stubTransBO.validateOrderforSubstitution(val)).thenReturn(st);
		when(listingBO.validateListingforSubstitution(request)).thenReturn(ls);
		when(userContactBiz.getUserContactById(any(Long.class))).thenReturn(buyerUsrCont);
		when(userContactBiz.getDefaultUserContactByOwernId(any(Long.class))).thenReturn(buyerUsrDefaultCont);
		
		when(listingBO.allocateSeats(any(ListingResponse.class), any(SubstitutionRequest.class), any(String[].class))).thenReturn(tl);
		when(userDAO.getUserSellerPaymentType(1L)).thenReturn(2L);
		when(listingDAO.updateListingPaymentType(Long.valueOf(ls.getId()), 1L)).thenReturn(1);
		when(stubTransBO.createStubTrans(any(SubstitutionRequest.class), any(StubTrans.class), any(ListingResponse.class), any(String.class), any(QueryResponse.class), any(Long.class))).thenReturn(nst);
		Long res = substitutionOrderBO.createOrder(request, val.toString(), operatorId, null);
		
	    Assert.assertNotNull(res);
	    Assert.assertTrue(res.equals(val));
	    
	    
	    
	    buyerUsrCont.setEmail(" ");
		Long res1 = substitutionOrderBO.createOrder(request, val.toString(), operatorId, null);
	    Assert.assertNotNull(res1);
	    Assert.assertTrue(res.equals(val));
	    
	    
	    
	}
	
	@Test
	public void testCreateOrderHappyPath2() throws Exception {
		SubstitutionRequest request = new SubstitutionRequest() ;
		request.setListingId(val.toString());
		
		StubTrans st = getStubTrans(2l);
		//st.setSellerPaymentTypeId(null);
		StubTrans nst = getNewStubTrans();
		
		ListingResponse ls = getListingResponse();
		List<TicketSeatUtil> tl = getTicketsList();
		
		when(stubTransBO.validateOrderforSubstitution(val)).thenReturn(st);
		when(listingBO.validateListingforSubstitution(request)).thenReturn(ls);
		when(listingBO.allocateSeats(any(ListingResponse.class), any(SubstitutionRequest.class), any(String[].class))).thenReturn(tl);
		when(userDAO.getUserSellerPaymentType(1L)).thenReturn(2L);
		when(listingDAO.updateListingPaymentType(Long.valueOf(ls.getId()), 1L)).thenReturn(1);
		when(stubTransBO.createStubTrans(any(SubstitutionRequest.class), any(StubTrans.class), any(ListingResponse.class), any(String.class), any(QueryResponse.class), any(Long.class))).thenReturn(nst);
		when(orderProcStatusAdapterDAO.updateOrderStatusByTransId(any(Long.class), any(String.class), any(Long.class))).thenReturn(43L);
		Long res = substitutionOrderBO.createOrder(request, val.toString(), operatorId, null);
		
	    Assert.assertNotNull(res);
	    Assert.assertTrue(res.equals(val)); 
	}
	
	@Test(expectedExceptions = Exception.class)
	public void testCreateOrderWithException() throws Exception {
		SubstitutionRequest request = new SubstitutionRequest() ;
		request.setListingId(val.toString());
		
		StubTrans st = getStubTrans(val);
		StubTrans nst = getNewStubTrans();
		ListingResponse ls = getListingResponse();
		List<TicketSeatUtil> tl = getTicketsList();
		
		when(stubTransBO.validateOrderforSubstitution(val)).thenReturn(st);
		when(listingBO.validateListingforSubstitution(request)).thenReturn(ls);
		when(listingBO.allocateSeats(any(ListingResponse.class), any(SubstitutionRequest.class), any(String[].class))).thenReturn(tl);
		when(userDAO.getUserSellerPaymentType(1L)).thenReturn(2L);
		when(listingDAO.updateListingPaymentType(Long.valueOf(ls.getId()), 1L)).thenReturn(1);
		when(stubTransBO.createStubTrans(any(SubstitutionRequest.class), any(StubTrans.class), any(ListingResponse.class), any(String.class), any(QueryResponse.class), any(Long.class))).thenReturn(nst);
		
		when(stubTransBO.cancelOrder(any(StubTrans.class), any(String.class))).thenThrow(new RuntimeException("Yosemite"));
		Long res = substitutionOrderBO.createOrder(request, val.toString(), operatorId, null);
		
	    Assert.assertNotNull(res);
	    Assert.assertTrue(res.equals(val));
	}

	
	public TicketSeatUtil mockTicketSeatUtil(){
		TicketSeatUtil ticketSeatUtil = new TicketSeatUtil();
		ticketSeatUtil.setRow("1");
		ticketSeatUtil.setSeatId(val);
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
	
	
	public InventoryData mockInventoryData()
	{
		
		InventoryData inv =  new InventoryData();
		
		inv.setListingId(1L);
		inv.setTicTecListingId("1234");
		inv.setSellerId(10133681L);
		inv.setBrokerId(val);
		inv.setOrderId(val);
	//	inv.setFulfillmentType("UPS");
		
		return inv;
	}
	
	@Test
	public void testPurchaseInventoryRequest()
	{
		PurchaseInventoryRequest purchReq = new PurchaseInventoryRequest();
		purchReq.setOrderId(1l);
		purchReq.setListingId(1l);
		purchReq.setExternalListingId("123");
		purchReq.setBuyerFirstName("BuyerFirstName");
		purchReq.setBuyerLastName("BuyerLastName");
		purchReq.setBuyerGUID("1sw23deer");
		purchReq.setPayoutAmount("12");
		purchReq.setOrderStatusId(1l);
		PartnerOrderTicket ticket = new PartnerOrderTicket();
		ticket.setId("1");
		ticket.setFaceValue("100");
		ticket.setPrice(BigDecimal.valueOf(100));
		ticket.setRow("1");
		ticket.setSeatNumber("1");
		ticket.setSection("center");
		ticket.setStatusDesc("HOLD");
		ticket.setStatusId(2L);
		ticket.setTicketSeatId(2L);
		List<PartnerOrderTicket> tics =  new ArrayList<PartnerOrderTicket>();
		tics.add(ticket);
		purchReq.setTickets(tics);
		
		TicTecUtil ticTecUtil = new TicTecUtil();
		
		PurchaseInventoryRequest purchReqTicTecUtil =	ticTecUtil.createPurchaseInventoryRequest (getMockUserContact(),mockListingResponse(),
				mockInventoryData(), mockStubTransDetail(), mockStubTrans());
		
		
		Assert.assertEquals(purchReqTicTecUtil.getBuyerFirstName(),purchReq.getBuyerFirstName());
		Assert.assertEquals(purchReqTicTecUtil.getBuyerLastName(),purchReq.getBuyerLastName());
		
		
	}
	
	@Test
	public SubstitutionRequest mockSubsRequest(){		
		com.stubhub.newplatform.common.entity.Money money = new com.stubhub.newplatform.common.entity.Money(new BigDecimal(1), "USD");
		SubstitutionRequest sub = new SubstitutionRequest();
		sub.setAdditionalSellFeePerTicket(money);
		sub.setAddOnFee(money);
		sub.setBuyerFeeVal(money);
		sub.setDeliveryMethodId("1");
		sub.setDiscountCost(money);
		sub.setFulfillmentMethodId("1");
		sub.setInHandDate("2015-05-05");
		sub.setListingId("1");
		sub.setLmsLocationId("1");
		sub.setOperatorId(operatorId);
		sub.setPremiumFees(money);
		sub.setQuantity("1");
		sub.setSellerFeeVal(money);
		sub.setSellerPayoutAmount(money);
		sub.setSellerPayoutAtConfirm(money);
		sub.setSellerPayoutDifference(money);
		sub.setShipCost(money);
		sub.setSubsReasonId("1");
		sub.setTicketCost(money);
		sub.setTicketCostDifference(money);
		sub.setTotalCost(money);
		sub.setVatBuyFee(money);
		sub.setVatLogFee(money);
		sub.setVatSellFee(money);
		
		Assert.assertEquals(sub.getDeliveryMethodId(),"1");
		Assert.assertEquals(sub.getFulfillmentMethodId(),"1");
		Assert.assertEquals(sub.getInHandDate(),"2015-05-05");
		Assert.assertEquals(sub.getListingId(),"1");
		Assert.assertEquals(sub.getLmsLocationId(),"1");
		Assert.assertEquals(sub.getOperatorId(),operatorId);
		Assert.assertEquals(sub.getQuantity(),"1");
		Assert.assertEquals(sub.getSubsReasonId(),"1");
		Assert.assertEquals(sub.getAdditionalSellFeePerTicket(),money);
		Assert.assertEquals(sub.getAddOnFee(),money);
		Assert.assertEquals(sub.getBuyerFeeVal(),money);
		Assert.assertEquals(sub.getDiscountCost(),money);
		Assert.assertEquals(sub.getPremiumFees(),money); 
		Assert.assertEquals(sub.getSellerFeeVal(),money);
		Assert.assertEquals(sub.getSellerPayoutAmount(),money);
		Assert.assertEquals(sub.getSellerPayoutAtConfirm(),money);
		Assert.assertEquals(sub.getSellerPayoutDifference(),money);
		Assert.assertEquals(sub.getShipCost(),money);
		Assert.assertEquals(sub.getTicketCost(),money);
		Assert.assertEquals(sub.getTicketCostDifference(),money);
		Assert.assertEquals(sub.getTotalCost(),money);
		Assert.assertEquals(sub.getVatBuyFee(),money);
		Assert.assertEquals(sub.getVatLogFee(),money);
		Assert.assertEquals(sub.getVatSellFee(),money);
		
		return sub;
	}
	
	@Test
	public void mockSubsResponse(){
		SubstitutionResponse  response = new SubstitutionResponse();
		response.setNewOrderId(1l);
		Assert.assertEquals(response.getNewOrderId(), Long.valueOf(1l));
	}
	
	public ListingResponse mockListingResponse(){
		ListingResponse resp = new ListingResponse();
		Money money = new Money(new BigDecimal(1), "USD");
		resp.setId("1");
		resp.setQuantityRemain(7);
		resp.setSplitVector("1,2,3,4,5,7");
		resp.setSection("142");
		resp.setVenueConfigSectionId(142499L);
		resp.setSaleMethod(SaleMethod.FIXED);
		resp.setSellerId(10133681L);
		resp.setQuantity(7);
		resp.setPricePerProduct(money);
		List<Product> prodList = new ArrayList<Product>();
		Product prod = new Product();
		prod.setSeatId(2482604301L);
		prod.setRow("6");
		prod.setSeat("3");
		prod.setProductType(ProductType.TICKET);
		prodList.add(prod);
		
		prod = new Product();
		prod.setSeatId(2482604302L);
		prod.setRow("6");
		prod.setSeat("4");
		prod.setProductType(ProductType.TICKET);
		prodList.add(prod);
		
		prod = new Product();
		prod.setSeatId(2482604304L);
		prod.setRow("6");
		prod.setSeat("5");
		prod.setProductType(ProductType.TICKET);
		prodList.add(prod);
		
		prod = new Product();
		prod.setSeatId(2482604305L);
		prod.setRow("6");
		prod.setSeat("6");
		prod.setProductType(ProductType.TICKET);
		prodList.add(prod);
		
		prod = new Product();
		prod.setSeatId(2482604307L);
		prod.setRow("6");
		prod.setSeat("7");
		prod.setProductType(ProductType.TICKET);
		prodList.add(prod);
		
		prod = new Product();
		prod.setSeatId(2482604308L);
		prod.setRow("6");
		prod.setSeat("8");
		prod.setProductType(ProductType.TICKET);
		prodList.add(prod);
		
		prod = new Product();
		prod.setSeatId(2482604310L);
		prod.setRow("6");
		prod.setSeat("9");
		prod.setProductType(ProductType.TICKET);
		prodList.add(prod);
		
		resp.setProducts(prodList);
		return resp;
	}
	
	
	public List<StubTransDetail> mockStubTransDetail()
	{
		List<StubTransDetail> list = new  ArrayList<StubTransDetail>();
		StubTransDetail stdet1 = new StubTransDetail();
		StubTransDetail stdet2 = new StubTransDetail();
		stdet1.setTicketSeatId(2482604301L);
		stdet2.setTicketSeatId(2482604305L);
		list.add(stdet1);
		list.add(stdet2);
		
		
		return list;
	}
	public StubTrans mockStubTrans(){
		StubTrans st = new StubTrans();
		Long val = 1l;
		String operatorId = "bijain";
		Calendar today = Calendar.getInstance();
		Money money = new Money(new BigDecimal(1), "USD");
		st.setAccertifyUserName("bijain");
		st.setActiveCsFlag(val);
		st.setAddOnFee(money);
		st.setAffiliateId(val);
		st.setAttentionFlag(true);
		st.setBidderCobrand("1");
		st.setBobId(val);
		st.setBuyDomainId(val);
		st.setBuyerAuthenticatedSessionGuid("1");
		st.setBuyerFee("1");
		st.setBuyerFeeVal(money);
		st.setBuyerId(val);
		st.setBuyerIpAddress("1");
		st.setBuyerTealeafSessionGuid("1");
		st.setBuyerThreatMetrixRefId("1");
		st.setBuyVAT(money);
		st.setCancelled(false);
		st.setCcId(val);
		st.setConfirmFlowTrackId(val);
		st.setConfirmOptionId(val);
		st.setConfirmSource(val);
		st.setContactId(val);
		st.setCreatedBy(operatorId);
		st.setCreatedTS(today);
		st.setCvvCheckStatusId(val);
		st.setDateAdded(today);
		st.setDateLastModified(today);
		st.setDeliveryOptionId(val);
		st.setDiscountCost(money);
		st.setEventId(val);
		st.setEventStatusIdAtConfirm(val);
		st.setExpectedDeliveryDate(today);
		st.setFraudCheckStatusId(val);
		st.setFraudOrderReviewHoldTime(today);
		st.setFraudResolutionId(val);
		st.setGroupComments(operatorId);
		st.setHoldPaymentTypeId(val);
		st.setInHandDate(today);
		st.setLastUpdatedBy(operatorId);
		st.setLastUpdatedTS(today);
		st.setLatestOrderProcStatusId(val);
		st.setListingVersion(val);
		st.setLogisticsMethod(val);
		st.setLogisticVAT(money);
		st.setMktngCommOptin(true);
		st.setMktngPartnerId(val);
		st.setOrderId(val);
		st.setOrderProcStatusCode(val);
		st.setOrderProcStatusEffDate(today);
		st.setOrderProcSubStatusCode(val);
		st.setOrderSourceId(val);
		st.setPaymentBufferInHours(11.11);
		st.setPaymentTermId(val);
		st.setPremiumFees(money);
		st.setQuantity(val);
		st.setRowDesc("1");
		st.setSaleMethod(val);
		st.setSeats("1");
		st.setSection("1");
		st.setSellDomainId(val);
		st.setSellerCCId(val);
		st.setSellerCobrand(operatorId);
		st.setSellerComments(operatorId);
		st.setSellerConfirmed(true);
		st.setSellerFee("1");
		st.setSellerFeeVal(money);
		st.setSellerId(val);
		st.setSellerPaymentTypeId(val);
		st.setSellerPayoutAmount(money);
		st.setSellerPayoutAmountAtConfrm(money);
		st.setSellIncrementalFee(money);
		st.setSellVAT(money);
		st.setShipCost(money);
		st.setStubnetUserId(val);
		st.setTicketCost(money);
		st.setTicketId(val);
		st.setTicketMediumId(val);
		st.setTotalCost(money);
		return st;
	}
	
	
	public StubTrans mockStubTransNew(){
		StubTrans st = new StubTrans();
		Long val = 1l;
		String operatorId = "bijain";
		Calendar today = Calendar.getInstance();
		Money money = new Money(new BigDecimal(1), "USD");
		st.setAccertifyUserName("bijain");
		st.setActiveCsFlag(val);
		st.setAddOnFee(money);
		st.setAffiliateId(val);
		st.setAttentionFlag(true);
		st.setBidderCobrand("1");
		st.setBobId(val);
		st.setBuyDomainId(val);
		st.setBuyerAuthenticatedSessionGuid("1");
		st.setBuyerFee("1");
		st.setBuyerFeeVal(money);
		st.setBuyerId(val);
		st.setBuyerIpAddress("1");
		st.setBuyerTealeafSessionGuid("1");
		st.setBuyerThreatMetrixRefId("1");
		st.setBuyVAT(money);
		st.setCancelled(false);
		st.setCcId(val);
		st.setConfirmFlowTrackId(val);
		st.setConfirmOptionId(val);
		st.setConfirmSource(val);
		st.setContactId(val);
		st.setCreatedBy(operatorId);
		st.setCreatedTS(today);
		st.setCvvCheckStatusId(val);
		st.setDateAdded(today);
		st.setDateLastModified(today);
		st.setDeliveryOptionId(val);
		st.setDiscountCost(money);
		st.setEventId(val);
		st.setEventStatusIdAtConfirm(val);
		st.setExpectedDeliveryDate(today);
		st.setFraudCheckStatusId(val);
		st.setFraudOrderReviewHoldTime(today);
		st.setFraudResolutionId(val);
		st.setGroupComments(operatorId);
		st.setHoldPaymentTypeId(val);
		st.setInHandDate(today);
		st.setLastUpdatedBy(operatorId);
		st.setLastUpdatedTS(today);
		st.setLatestOrderProcStatusId(val);
		st.setListingVersion(val);
		st.setLogisticsMethod(val);
		st.setLogisticVAT(money);
		st.setMktngCommOptin(true);
		st.setMktngPartnerId(val);
		st.setOrderId(val);
		st.setOrderProcStatusCode(val);
		st.setOrderProcStatusEffDate(today);
		st.setOrderProcSubStatusCode(val);
		st.setOrderSourceId(val);
		st.setPaymentBufferInHours(11.11);
		st.setPaymentTermId(val);
		st.setPremiumFees(money);
		st.setQuantity(val);
		st.setRowDesc("1");
		st.setSaleMethod(val);
		st.setSeats("1");
		st.setSection("1");
		st.setSellDomainId(val);
		st.setSellerCCId(val);
		st.setSellerCobrand(operatorId);
		st.setSellerComments(operatorId);
		st.setSellerConfirmed(true);
		st.setSellerFee("1");
		st.setSellerFeeVal(money);
		st.setSellerId(val);
		st.setSellerPaymentTypeId(val);
		st.setSellerPayoutAmount(money);
		st.setSellerPayoutAmountAtConfrm(money);
		st.setSellIncrementalFee(money);
		st.setSellVAT(money);
		st.setShipCost(money);
		st.setStubnetUserId(val);
		st.setTicketCost(money);
		st.setTicketId(val);
		st.setTicketMediumId(val);
		st.setTotalCost(money);
		return st;
	}
	
	public UserContact getMockUserContact()
	{
		UserContact userBuyer = new UserContact();
		userBuyer.setFirstName("BuyerFirstName");
		userBuyer.setLastName("BuyerLastName");
		return userBuyer;
	}
	

}
