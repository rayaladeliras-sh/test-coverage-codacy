package com.stubhub.domain.account.biz.impl;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.Assert;
import org.testng.AssertJUnit;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.collections.map.MultiValueMap;
import org.apache.commons.io.IOUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.stubhub.common.exception.InvalidArgumentException;
import com.stubhub.common.exception.RecordNotFoundForIdException;
import com.stubhub.common.exception.UserNotAuthorizedException;
import com.stubhub.domain.account.biz.intf.InventoryBO;
import com.stubhub.domain.account.biz.intf.ListingBO;
import com.stubhub.domain.account.datamodel.dao.ListingDAO;
import com.stubhub.domain.account.datamodel.dao.ListingSeatsDAO;
import com.stubhub.domain.account.datamodel.dao.impl.ListingDAOImpl;
import com.stubhub.domain.account.datamodel.dao.impl.ListingSeatsDAOImpl;
import com.stubhub.domain.account.datamodel.entity.Listing;
import com.stubhub.domain.inventory.common.entity.ListingStatus;
import com.stubhub.domain.account.datamodel.entity.StubTrans;
import com.stubhub.domain.account.datamodel.entity.TicketSeat;
import com.stubhub.domain.account.datamodel.entity.UserContact;
import com.stubhub.domain.account.intf.SubstitutionRequest;
import com.stubhub.domain.inventory.common.entity.SaleMethod;
import com.stubhub.domain.inventory.v2.DTO.ListingResponse;
import com.stubhub.domain.inventory.v2.DTO.ListingsControllerRequest;
import com.stubhub.domain.inventory.v2.DTO.ListingsControllerResponse;
import com.stubhub.domain.inventory.v2.DTO.Product;
import com.stubhub.domain.inventory.v2.enums.ProductType;
import com.stubhub.newplatform.persistence.adapter.UTCCalendarToTimestampAdapter;
import com.stubhub.platform.utilities.webservice.svclocator.SvcLocator;

import static org.mockito.Matchers.any;

public class ListingBOTest {

	private ListingBO listingBO;
	private InventoryBO inventoryBO;
	private SvcLocator svcLocator;
	private WebClient webClient;
	private Long l1 = 200L;
	private ListingDAO listingDAO;
	private ListingSeatsDAO listingSeatsDAO;
	private MultivaluedMap<String, String> headers;
	private Response responseMock;
	
	@BeforeMethod
	@BeforeTest
	public void setUp() {
		responseMock = Mockito.mock(Response.class);
		listingDAO = Mockito.mock(ListingDAOImpl.class);
		listingSeatsDAO = Mockito.mock(ListingSeatsDAOImpl.class);
		inventoryBO = Mockito.mock(InventoryBOImpl.class);

		listingBO = new ListingBOImpl(){
			protected String getProperty(String propertyName, String defaultValue) {
				if ("inventory.listings.api.url".equals(propertyName)) {
					return "http://api-int.${default_domain}/inventoryv2/listings/v2/{listingId}";
				}
				return "";	
			}			
		};
		svcLocator = Mockito.mock(SvcLocator.class);
		webClient  = Mockito.mock(WebClient.class);
		ReflectionTestUtils.setField(listingBO, "svcLocator", svcLocator);
		ReflectionTestUtils.setField(listingBO, "listingDAO", listingDAO);
		ReflectionTestUtils.setField(listingBO, "listingSeatsDAO", listingSeatsDAO);
		ReflectionTestUtils.setField(listingBO, "inventoryBO", inventoryBO);
	}

	@Test
	public void testAllocateSeats(){
		ListingResponse listingResponse = getMockListing(false);
		List<TicketSeatUtil> result= new ArrayList<TicketSeatUtil>();
		TicketSeatUtil ticketSeatUtil = mockTicketSeatUtil();
		result.add(ticketSeatUtil);
		SubstitutionRequest request = new SubstitutionRequest();
		request.setQuantity("7");
		String[] seatList = new String[2];
		AssertJUnit.assertNotNull(listingBO.allocateSeats(listingResponse, request, seatList));
		listingResponse.setSection("General Admission");
		AssertJUnit.assertNotNull(listingBO.allocateSeats(listingResponse, request, seatList));
		for (int i=0;i<listingResponse.getProducts().size();i++)
			listingResponse.getProducts().get(i).setSeat(null);
		AssertJUnit.assertNotNull(listingBO.allocateSeats(listingResponse, request, seatList));
	}
	
	@Test
	public void testAllocateSeatsWithParkingPassesOnlySuccess(){
		ListingResponse listingResponse = getMockListingParkingPassesOnly();
		List<TicketSeatUtil> result= new ArrayList<TicketSeatUtil>();
		TicketSeatUtil ticketSeatUtil = mockTicketSeatUtil();
		result.add(ticketSeatUtil);
		SubstitutionRequest request = new SubstitutionRequest();
		request.setQuantity("8");
		String[] seatList = new String[2];
		List<TicketSeatUtil> seatUtilList = listingBO.allocateSeats(listingResponse, request, seatList);
		Assert.assertEquals(seatUtilList.size(), listingResponse.getProducts().size());
	}
	
	@Test
	public void testAllocateSeatsWithParkingPassesOnlyFailure(){
		ListingResponse listingResponse = getMockListingParkingPassesOnly();
		List<TicketSeatUtil> result= new ArrayList<TicketSeatUtil>();
		TicketSeatUtil ticketSeatUtil = mockTicketSeatUtil();
		result.add(ticketSeatUtil);
		int requestedQuantity = 5;
		SubstitutionRequest request = new SubstitutionRequest();
		request.setQuantity(Integer.toString(requestedQuantity));
		String[] seatList = new String[2];
		List<TicketSeatUtil> seatUtilList = listingBO.allocateSeats(listingResponse, request, seatList);
		Assert.assertNotEquals(seatUtilList.size(), listingResponse.getProducts().size());
		Assert.assertEquals(seatUtilList.size(), requestedQuantity);
	}
	
	@Test
	public void testAllocateSeatsWithMixedOrderSuccess(){
		ListingResponse listingResponse = getMockListing(true);
		List<TicketSeatUtil> result= new ArrayList<TicketSeatUtil>();
		TicketSeatUtil ticketSeatUtil = mockTicketSeatUtil();
		result.add(ticketSeatUtil);
		SubstitutionRequest request = new SubstitutionRequest();
		String[] seatList = new String[2];
		request.setQuantity("7");
		List<TicketSeatUtil> seatUtilList = listingBO.allocateSeats(listingResponse, request, seatList);
		Assert.assertEquals(seatUtilList.size(), listingResponse.getProducts().size());
		Assert.assertNotNull(listingBO.allocateSeats(listingResponse, request, seatList));
		listingResponse.setSection("General Admission");
		Assert.assertNotNull(listingBO.allocateSeats(listingResponse, request, seatList));
		for (int i=0;i<listingResponse.getProducts().size();i++)
			listingResponse.getProducts().get(i).setSeat(null);
		Assert.assertNotNull(listingBO.allocateSeats(listingResponse, request, seatList));
	}
	
	@Test
	public void testAllocateSeatsWithMixedOrderFailure(){
		ListingResponse listingResponse = getMockListing(false);
		List<TicketSeatUtil> result= new ArrayList<TicketSeatUtil>();
		TicketSeatUtil ticketSeatUtil = mockTicketSeatUtil();
		result.add(ticketSeatUtil);
		SubstitutionRequest request = new SubstitutionRequest();
		int requestedQuantity = 5;
		request.setQuantity(Integer.toString(requestedQuantity));
		String[] seatList = new String[2];
		List<TicketSeatUtil> seatUtilList = listingBO.allocateSeats(listingResponse, request, seatList);
		AssertJUnit.assertNotNull(seatUtilList);
		Assert.assertNotEquals(seatUtilList.size(), listingResponse.getProducts().size());
		Assert.assertEquals(seatUtilList.size(), requestedQuantity);
		listingResponse.setSection("General Admission");
		seatUtilList = listingBO.allocateSeats(listingResponse, request, seatList);
		AssertJUnit.assertNotNull(seatUtilList);
		Assert.assertNotEquals(seatUtilList.size(), listingResponse.getProducts().size());
		Assert.assertEquals(seatUtilList.size(), requestedQuantity);
		for (int i=0;i<listingResponse.getProducts().size();i++)
			listingResponse.getProducts().get(i).setSeat(null);
		seatUtilList = listingBO.allocateSeats(listingResponse, request, seatList);
		AssertJUnit.assertNotNull(seatUtilList);
		Assert.assertNotEquals(seatUtilList.size(), listingResponse.getProducts().size());
		Assert.assertEquals(seatUtilList.size(), requestedQuantity);
	}

	@Test
	public void testGetListing() throws RecordNotFoundForIdException, UserNotAuthorizedException, Exception{
		ListingResponse listingResponse = getMockListing(false);
		Response response = Response.status(Status.OK).entity(listingResponse).build();
		Mockito.when(svcLocator.locate(Mockito.anyString(), Mockito.anyList())).thenReturn(webClient);
		Mockito.when(webClient.get()).thenReturn(response);
		AssertJUnit.assertNotNull(listingBO.getListing(l1));
	}
	
	private InputStream toInputStream(Object obj) throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper omMapper = new ObjectMapper();
		String str = omMapper.writeValueAsString(obj);
		//System.out.println(str);
		return IOUtils.toInputStream(str, "UTF-8");
	}
	
	@Test
	public void testCheckResponseObjectException() {
		ObjectMapper mapper = Mockito.mock(ObjectMapper.class);
		try {
			ListingBOImpl service = (ListingBOImpl)listingBO;
			service.checkResponseObject(responseMock, mapper, null);
			Assert.fail();
		} catch (Exception ex) {
			// OK;
		}
	}

	@Test
	public void testcallListingControllerPurchase() throws RecordNotFoundForIdException, UserNotAuthorizedException, Exception{
		Response response = Response.status(Status.OK).build();
		StubTrans newStubTrans = new StubTrans();
		newStubTrans.setOrderId(1000L);
		Mockito.when(svcLocator.locate(Mockito.anyString(), Mockito.anyList())).thenReturn(webClient);
		Mockito.when(webClient.post(any(ListingsControllerRequest.class))).thenReturn(responseMock);
		UserContact buyerUsrCont = new UserContact();
		buyerUsrCont.setFirstName("firstName");
		buyerUsrCont.setLastName("lastName");
		SubstitutionRequest request = new SubstitutionRequest();
		request.setQuantity("1");
		List<TicketSeatUtil> selectedSeat = new ArrayList<TicketSeatUtil>();
		TicketSeatUtil util = new TicketSeatUtil();
		util.setSeatId(1L);
		selectedSeat.add(util);
		
		Mockito.when(responseMock.getStatus()).thenReturn(200);
		try {
			listingBO.callListingControllerPurchase(newStubTrans, buyerUsrCont,
					getMockListing(false), request, 100L, selectedSeat);
			Assert.fail();
		} catch (Exception ex) {
			// OK;
		}
		
		Mockito.when(responseMock.getEntity()).thenReturn(null);
		
		try {
			listingBO.callListingControllerPurchase(newStubTrans, buyerUsrCont,
					getMockListing(false), request, 100L, selectedSeat);
			Assert.fail();
		} catch (Exception ex) {
			// OK;
		}
		
		ListingsControllerResponse lcr = new ListingsControllerResponse();
		Mockito.when(responseMock.getEntity()).thenReturn(toInputStream(lcr));
		
		try {
			listingBO.callListingControllerPurchase(newStubTrans, buyerUsrCont,
					getMockListing(false), request, 100L, selectedSeat);
			Assert.fail();
		} catch (Exception ex) {
			// OK;
		}
		
		lcr.setStatus("NOTSUCCESS");
		Mockito.when(responseMock.getEntity()).thenReturn(toInputStream(lcr));
		try {
			listingBO.callListingControllerPurchase(newStubTrans, buyerUsrCont,
					getMockListing(false), request, 100L, selectedSeat);
			Assert.fail();
		} catch (Exception ex) {
			// OK;
		}
		
		lcr.setStatus("SUCCESS");
		Mockito.when(responseMock.getEntity()).thenReturn(toInputStream(lcr));
		try {
			listingBO.callListingControllerPurchase(newStubTrans, buyerUsrCont,
					getMockListing(false), request, 100L, selectedSeat);
		} catch (Exception ex) {
			Assert.fail("Exception caused ");
		}

		
		Mockito.when(responseMock.getStatus()).thenReturn(400);
		try {
			listingBO.callListingControllerPurchase(newStubTrans, buyerUsrCont,
					getMockListing(false), request, 100L, selectedSeat);
			Assert.fail();
		} catch (Exception ex) {
			// OK;
		}
		
		// hidden listing
		Mockito.when(responseMock.getStatus()).thenReturn(200);

		listingBO.callListingControllerPurchase(newStubTrans, buyerUsrCont,
				getMockListing(false, true), request, 100L, selectedSeat);

	}

	@Test
	public void testcallListingControllerPurchaseWithFulfilmentTest() throws RecordNotFoundForIdException, UserNotAuthorizedException, Exception{
		
		ListingsControllerResponse lcr = new ListingsControllerResponse();	
		lcr.setStatus("SUCCESS");
		Mockito.when(responseMock.getEntity()).thenReturn(toInputStream(lcr));
		Mockito.when(responseMock.getStatus()).thenReturn(200);
		
		StubTrans newStubTrans = new StubTrans();
		newStubTrans.setOrderId(1000L);
		Mockito.when(svcLocator.locate(Mockito.anyString(), Mockito.anyList())).thenReturn(webClient);
		Mockito.when(webClient.post(any(ListingsControllerRequest.class))).thenReturn(responseMock);
		Mockito.when(inventoryBO.getFulFillmentType(Mockito.anyLong())).thenReturn("UPS");
		UserContact buyerUsrCont = new UserContact();
		buyerUsrCont.setFirstName("firstName");
		buyerUsrCont.setLastName("lastName");
		SubstitutionRequest request = new SubstitutionRequest();
		request.setFulfillmentMethodId("1234");
		request.setQuantity("1");
		List<TicketSeatUtil> selectedSeat = new ArrayList<TicketSeatUtil>();
		TicketSeatUtil util = new TicketSeatUtil();
		util.setSeatId(1L);
		selectedSeat.add(util);
		
		try {
			listingBO.callListingControllerPurchase(newStubTrans, buyerUsrCont,
					getMockListing(false), request, 100L, selectedSeat);
		} catch (Exception ex) {
			Assert.fail("Exception caused ");
		}

		Mockito.when(responseMock.getEntity()).thenReturn(toInputStream(lcr));
		Mockito.when(inventoryBO.getFulFillmentType(Mockito.anyLong()))
				.thenReturn("");
		try {

			listingBO.callListingControllerPurchase(newStubTrans, buyerUsrCont,
					getMockListing(false), request, 100L, selectedSeat);

		} catch (Exception ex) {
			Assert.fail("Exception caused ");
		}

		Mockito.when(responseMock.getEntity()).thenReturn(toInputStream(lcr));
		Mockito.when(inventoryBO.getFulFillmentType(Mockito.anyLong()))
				.thenReturn(null);
		try {

			listingBO.callListingControllerPurchase(newStubTrans, buyerUsrCont,
					getMockListing(false), request, 100L, selectedSeat);

		} catch (Exception ex) {
			Assert.fail("Exception caused ");
		}

	}


	@Test
	public void testValidateListingforSubstitutionHappyPath() throws RecordNotFoundForIdException, UserNotAuthorizedException, Exception{
		SubstitutionRequest request = new SubstitutionRequest();
		request.setListingId("1140386353");
		request.setQuantity("1");
		ListingResponse listingResponse = getMockListing(false);
		Response response = Response.status(Status.OK).entity(listingResponse).build();
		Mockito.when(svcLocator.locate(Mockito.anyString(), Mockito.anyList())).thenReturn(webClient);
		Mockito.when(webClient.get()).thenReturn(response);
		Assert.assertNotNull(listingBO.validateListingforSubstitution(request));
	}

	@Test(expectedExceptions=InvalidArgumentException.class)
	public void testValidateListingforSubstitutionErrorInsufficientQuantity() throws RecordNotFoundForIdException, UserNotAuthorizedException, Exception{
		SubstitutionRequest request = new SubstitutionRequest();
		request.setListingId("1140386353");
		request.setQuantity("10");
		ListingResponse listingResponse = getMockListing(false);
		Response response = Response.status(Status.OK).entity(listingResponse).build();
		Mockito.when(svcLocator.locate(Mockito.anyString(), Mockito.anyList())).thenReturn(webClient);
		Mockito.when(webClient.get()).thenReturn(response);
		Assert.assertNotNull(listingBO.validateListingforSubstitution(request));
	}

	@Test(expectedExceptions=Exception.class)
	public void testcallListingControllerPurchaseWithException() throws RecordNotFoundForIdException, UserNotAuthorizedException, Exception{
		Response response = Response.status(Status.BAD_REQUEST).build();
		StubTrans newStubTrans = new StubTrans();
		newStubTrans.setOrderId(1000L);
		Mockito.when(svcLocator.locate(Mockito.anyString(), Mockito.anyList())).thenReturn(webClient);
		Mockito.when(webClient.post(any(ListingsControllerRequest.class))).thenReturn(response);
		UserContact buyerUsrCont = new UserContact();
		buyerUsrCont.setFirstName("firstName");
		buyerUsrCont.setLastName("lastName");
		SubstitutionRequest request = new SubstitutionRequest();
		request.setQuantity("1");
		listingBO.callListingControllerPurchase(newStubTrans, buyerUsrCont , getMockListing(false), request , 100L,null);
	}


	@Test
	public void testcallListingControllerRelease() throws RecordNotFoundForIdException, UserNotAuthorizedException, Exception{
		Response response = Response.status(Status.OK).build();
		StubTrans newStubTrans = new StubTrans();
		newStubTrans.setOrderId(1000L);
		Mockito.when(svcLocator.locate(Mockito.anyString(), Mockito.anyList())).thenReturn(webClient);
		Mockito.when(webClient.post(any(ListingsControllerRequest.class))).thenReturn(response);
		try{
			List<TicketSeatUtil> allocatedSeats = new ArrayList<TicketSeatUtil>();
			TicketSeatUtil seatUtils = new TicketSeatUtil();
			seatUtils.setSeatId(100001L);
			seatUtils.setSeatNumber("abc");
			allocatedSeats.add(seatUtils);
			listingBO.callListingControllerRelease(newStubTrans, getMockListing(false), 100L,allocatedSeats);
		}catch(Exception ex){
			Assert.fail("Exception caused ");	
		}

	}

	@Test(expectedExceptions=Exception.class)
	public void testcallListingControllerReleaseeWithException() throws RecordNotFoundForIdException, UserNotAuthorizedException, Exception{
		Response response = Response.status(Status.BAD_REQUEST).build();
		StubTrans newStubTrans = new StubTrans();
		newStubTrans.setOrderId(1000L);
		Mockito.when(svcLocator.locate(Mockito.anyString(), Mockito.anyList())).thenReturn(webClient);
		Mockito.when(webClient.post(any(ListingsControllerRequest.class))).thenReturn(response);
		List<TicketSeatUtil> allocatedSeats = new ArrayList<TicketSeatUtil>();
		TicketSeatUtil seatUtils = new TicketSeatUtil();
		seatUtils.setSeatId(100001L);
		seatUtils.setSeatNumber("abc");
		allocatedSeats.add(seatUtils);
		listingBO.callListingControllerRelease(newStubTrans, getMockListing(false), 100L,allocatedSeats);
	}

	public TicketSeat mockTicketSeat(){
		TicketSeat tc = new TicketSeat();
		tc.setTicketSeatId(1l);
		tc.setSeatStatusId(2l);
		tc.setOrderPlacedInd(true);
		tc.setLastUpdatedBy("bijain");
		tc.setLastUpdatedDate(UTCCalendarToTimestampAdapter.getNewUTCCalendarInstanceStatic());
		tc.setCreatedBy("bijain");
		tc.setCreatedDate(UTCCalendarToTimestampAdapter.getNewUTCCalendarInstanceStatic());
		tc.setExternalSeatId("1");
		tc.setGeneralAdmissionInd(true);
		tc.setRowNumber("1");
		tc.setSeatDesc("1");
		tc.setSeatStatusId(1l);
		tc.setSectionName("1");
		tc.setTicketId(1l);
		tc.setTixListTypeId(1l);
		tc.setSeatNumber("1");
		return tc;
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
		AssertJUnit.assertEquals(ticketSeatUtil.getRow(),"1");
		AssertJUnit.assertEquals(ticketSeatUtil.getSeatNumber(),"1");
		AssertJUnit.assertEquals(ticketSeatUtil.getSection(),"1");
		AssertJUnit.assertEquals(ticketSeatUtil.getSeatId(),val);
		AssertJUnit.assertEquals(ticketSeatUtil.getTicketListTypeId(),val);
		AssertJUnit.assertEquals(ticketSeatUtil.getVenueConfigSectionId(),val);
		return ticketSeatUtil;
	}

	public List<TicketSeatUtil> mockTicketSeatUtilList()
	{
		Long val = 1l;
		List<TicketSeatUtil> list =  new ArrayList<TicketSeatUtil>();

		TicketSeatUtil ticketSeatUtil1 = new TicketSeatUtil();
		ticketSeatUtil1.setRow("1");
		ticketSeatUtil1.setSeatId(1l);
		ticketSeatUtil1.setSeatNumber("4");
		ticketSeatUtil1.setSection("1");
		ticketSeatUtil1.setTicketListTypeId(val);
		ticketSeatUtil1.setVenueConfigSectionId(val);
		TicketSeatUtil ticketSeatUtil2 = new TicketSeatUtil();
		ticketSeatUtil2.setRow("1");
		ticketSeatUtil2.setSeatId(1l);
		ticketSeatUtil2.setSeatNumber("6");
		ticketSeatUtil2.setSection("1");
		ticketSeatUtil2.setTicketListTypeId(val);
		ticketSeatUtil2.setVenueConfigSectionId(val);
		TicketSeatUtil ticketSeatUtil3 = new TicketSeatUtil();
		ticketSeatUtil3.setRow("1");
		ticketSeatUtil3.setSeatId(1l);
		ticketSeatUtil3.setSeatNumber("8");
		ticketSeatUtil3.setSection("1");
		ticketSeatUtil3.setTicketListTypeId(val);
		ticketSeatUtil3.setVenueConfigSectionId(val);
		list.add(ticketSeatUtil1);
		list.add(ticketSeatUtil2);
		list.add(ticketSeatUtil3);
		return list;


	}

	public List<TicketSeatUtil> mockTicketSeatUtilAllList()
	{
		Long val = 1l;
		List<TicketSeatUtil> list =  new ArrayList<TicketSeatUtil>();

		TicketSeatUtil ticketSeatUtil1 = new TicketSeatUtil();
		ticketSeatUtil1.setRow("1");
		ticketSeatUtil1.setSeatId(1l);
		ticketSeatUtil1.setSeatNumber("3");
		ticketSeatUtil1.setSection("1");
		ticketSeatUtil1.setTicketListTypeId(val);
		ticketSeatUtil1.setVenueConfigSectionId(val);
		TicketSeatUtil ticketSeatUtil2 = new TicketSeatUtil();
		ticketSeatUtil2.setRow("1");
		ticketSeatUtil2.setSeatId(1l);
		ticketSeatUtil2.setSeatNumber("4");
		ticketSeatUtil2.setSection("1");
		ticketSeatUtil2.setTicketListTypeId(val);
		ticketSeatUtil2.setVenueConfigSectionId(val);
		TicketSeatUtil ticketSeatUtil3 = new TicketSeatUtil();
		ticketSeatUtil3.setRow("1");
		ticketSeatUtil3.setSeatId(1l);
		ticketSeatUtil3.setSeatNumber("5");
		ticketSeatUtil3.setSection("1");
		ticketSeatUtil3.setTicketListTypeId(val);
		ticketSeatUtil3.setVenueConfigSectionId(val);
		TicketSeatUtil ticketSeatUtil4 = new TicketSeatUtil();
		ticketSeatUtil4.setRow("1");
		ticketSeatUtil4.setSeatId(1l);
		ticketSeatUtil4.setSeatNumber("6");
		ticketSeatUtil4.setSection("1");
		ticketSeatUtil4.setTicketListTypeId(val);
		ticketSeatUtil4.setVenueConfigSectionId(val);
		TicketSeatUtil ticketSeatUtil5 = new TicketSeatUtil();
		ticketSeatUtil5.setRow("1");
		ticketSeatUtil5.setSeatId(1l);
		ticketSeatUtil5.setSeatNumber("7");
		ticketSeatUtil5.setSection("1");
		ticketSeatUtil5.setTicketListTypeId(val);
		ticketSeatUtil5.setVenueConfigSectionId(val);
		TicketSeatUtil ticketSeatUtil6 = new TicketSeatUtil();
		ticketSeatUtil6.setRow("1");
		ticketSeatUtil6.setSeatId(1l);
		ticketSeatUtil6.setSeatNumber("8");
		ticketSeatUtil6.setSection("1");
		ticketSeatUtil6.setTicketListTypeId(val);
		ticketSeatUtil6.setVenueConfigSectionId(val);

		TicketSeatUtil ticketSeatUtil7 = new TicketSeatUtil();
		ticketSeatUtil7.setRow("1");
		ticketSeatUtil7.setSeatId(1l);
		ticketSeatUtil7.setSeatNumber("9");
		ticketSeatUtil7.setSection("1");
		ticketSeatUtil7.setTicketListTypeId(val);
		ticketSeatUtil7.setVenueConfigSectionId(val);

		list.add(ticketSeatUtil1);
		list.add(ticketSeatUtil2);
		list.add(ticketSeatUtil3);
		list.add(ticketSeatUtil4);
		list.add(ticketSeatUtil5);
		list.add(ticketSeatUtil6);
		list.add(ticketSeatUtil7);
		return list;


	}
	
	public ListingResponse getMockListing(boolean isParkingIncluded){
		return getMockListing(isParkingIncluded, false);
	}

	public ListingResponse getMockListing(boolean isParkingIncluded, boolean isHidden){
		ListingResponse resp = new ListingResponse();
		resp.setId("1140386353");
		resp.setQuantityRemain(7);
		resp.setSplitVector("1,2,3,4,5,7");
		resp.setSection("142");
		resp.setVenueConfigSectionId(142499L);
		resp.setSaleMethod(SaleMethod.FIXED);
		resp.setSellerId(10133681L);
		resp.setQuantity(7);
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
		
		if(isParkingIncluded){
			prod = new Product();
			prod.setProductType(ProductType.PARKING_PASS);
			prodList.add(prod);
		}
		if (isHidden) {
			resp.setStatus(ListingStatus.HIDDEN);
		} else {
			resp.setStatus(ListingStatus.ACTIVE);
		}
		resp.setProducts(prodList);
		return resp;
	}
	
	public ListingResponse getMockListingParkingPassesOnly(){
		ListingResponse resp = new ListingResponse();
		resp.setId("1140386353");
		resp.setQuantityRemain(7);
		resp.setSplitVector("1,2,3,4,5,7");
		resp.setSection("142");
		resp.setVenueConfigSectionId(142499L);
		resp.setSaleMethod(SaleMethod.FIXED);
		resp.setSellerId(10133681L);
		resp.setQuantity(7);
		List<Product> prodList = new ArrayList<Product>();
		Product prod = new Product();
		prod.setProductType(ProductType.PARKING_PASS);
		prodList.add(prod);
		prod = new Product();
		prod.setProductType(ProductType.PARKING_PASS);
		prodList.add(prod);
		prod = new Product();
		prod.setProductType(ProductType.PARKING_PASS);
		prodList.add(prod);
		prod = new Product();
		prod.setProductType(ProductType.PARKING_PASS);
		prodList.add(prod);
		prod = new Product();
		prod.setProductType(ProductType.PARKING_PASS);
		prodList.add(prod);
		prod = new Product();
		prod.setProductType(ProductType.PARKING_PASS);
		prodList.add(prod);
		prod = new Product();
		prod.setProductType(ProductType.PARKING_PASS);
		prodList.add(prod);

		prod = new Product();
		prod.setProductType(ProductType.PARKING_PASS);
		prodList.add(prod);
		resp.setProducts(prodList);
		return resp;
	}

	@Test
	void test_getticket_seat_info(){
		List<TicketSeat> ticketSeats= this.listingBO.getTicketSeatInfo(123L);
		AssertJUnit.assertNotNull(ticketSeats);
	}


}