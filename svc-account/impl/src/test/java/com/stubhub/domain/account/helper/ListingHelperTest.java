package com.stubhub.domain.account.helper;

import static junit.framework.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import junit.framework.Assert;

import org.apache.cxf.jaxrs.client.WebClient;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.stubhub.domain.fulfillment.clients.fulfillmentlabel.util.MessageSourceBundle;
import com.stubhub.domain.inventory.DTO.Fee;
import com.stubhub.domain.account.intf.DeliveryMethod;
import com.stubhub.domain.account.intf.FulfillmentMethod;
import com.stubhub.domain.account.intf.ListingResponse;
import com.stubhub.domain.inventory.common.entity.DeliveryOption;
import com.stubhub.domain.inventory.common.entity.ListingStatus;
import com.stubhub.domain.inventory.common.entity.SaleMethod;
import com.stubhub.domain.inventory.common.entity.SplitOption;
import com.stubhub.domain.inventory.common.entity.TicketTrait;
import com.stubhub.domain.inventory.enums.FeeType;
import com.stubhub.newplatform.common.entity.Money;
import com.stubhub.platform.utilities.webservice.svclocator.SvcLocator;

public class ListingHelperTest {

	private SvcLocator svcLocator;
	private WebClient webClient;
	private ListingHelper helper;
	private MessageSourceBundle messageSourceBundle;

	@BeforeMethod
	public void setUp() {
		helper = new ListingHelper() {
			protected String getProperty(String propertyName,
					String defaultValue) {
				if ("inventoryListing.api.url".equals(propertyName)) {
					return "https://api.srwd34.com/inventorynew/listings/v1/{listingId}/seller";
				}
				return "";
			}
		};
		svcLocator = Mockito.mock(SvcLocator.class);
		webClient = Mockito.mock(WebClient.class);
		messageSourceBundle = Mockito.mock(MessageSourceBundle.class);
		ReflectionTestUtils.setField(helper, "svcLocator", svcLocator);
		ReflectionTestUtils.setField(helper, "messageSourceBundle",
				messageSourceBundle);
	}

	@Test
	public void testAddLocalizedMessagesForFmDm() {

		ListingResponse resp = new ListingResponse();
		resp.setId("100");

		List<DeliveryMethod> dmLst = new ArrayList<DeliveryMethod>();
		DeliveryMethod dm = new DeliveryMethod();
		dm.setDeliveryMethodId(2l);
		dmLst.add(dm);
		resp.setDeliveryMethod(dmLst);

		List<FulfillmentMethod> fmLst = new ArrayList<FulfillmentMethod>();
		FulfillmentMethod fm = new FulfillmentMethod();
		fm.setFulfillmentMethodId(1l);
		fmLst.add(fm);
		resp.setFulfillmentMethod(fmLst);

		// pojo coverage
		fm.getFulfillmentMethodDisplayName();
		fm.getFulfillmentMethodLongAppInstruction();
		fm.getFulfillmentMethodLongInstruction();
		fm.getFulfillmentMethodShortAppInstruction();
		fm.getFulfillmentMethodShortInstruction();
		dm.getDeliveryMethodDisplayName();
		dm.getDeliveryMethodLongAppInstruction();
		dm.getDeliveryMethodLongInstruction();
		dm.getDeliveryMethodShortAppInstruction();
		dm.getDeliveryMethodShortInstruction();
		
		helper.addLocalizedMessagesForFmDm(resp, Locale.US);
	}



	@Test
	public void testAddLocalizedMessagesForFmDmNullFMDMLst() {

		ListingResponse resp = new ListingResponse();
		resp.setId("100");

		resp.setDeliveryMethod(null);
		resp.setFulfillmentMethod(null);

		helper.addLocalizedMessagesForFmDm(resp, Locale.US);
	}
	
	@Test
	public void testAddLocalizedMessagesForFmDmListDiffSizes() {

		ListingResponse resp = new ListingResponse();
		resp.setId("100");
		List<DeliveryMethod> dmLst = new ArrayList<DeliveryMethod>();
		DeliveryMethod dm = new DeliveryMethod();
		dm.setDeliveryMethodId(2l);
		dmLst.add(dm);
		resp.setDeliveryMethod(dmLst);

		List<FulfillmentMethod> fmLst = new ArrayList<FulfillmentMethod>();
		FulfillmentMethod fm = new FulfillmentMethod();
		fm.setFulfillmentMethodId(1l);
		FulfillmentMethod fm1 = new FulfillmentMethod();
		fm.setFulfillmentMethodId(2l);
		fmLst.add(fm);
		fmLst.add(fm1);
		resp.setFulfillmentMethod(fmLst);
		helper.addLocalizedMessagesForFmDm(resp, Locale.US);

		Assert.assertEquals(resp.getDeliveryMethod().get(0).getDeliveryMethodId().longValue(), 2l);
	}



	@Test
	public void testAddLocalizedMessagesForFmDmEmptyLst() {

		ListingResponse resp = new ListingResponse();
		resp.setId("100");

		List<DeliveryMethod> dmLst = new ArrayList<DeliveryMethod>();
		resp.setDeliveryMethod(dmLst);

		List<FulfillmentMethod> fmLst = new ArrayList<FulfillmentMethod>();
		resp.setFulfillmentMethod(fmLst);

		helper.addLocalizedMessagesForFmDm(resp, Locale.US);
		Assert.assertEquals(resp.getDeliveryMethod().isEmpty(), true);

	}
	
	@Test
	public void testAddLocalizedMessagesForFmDmWithNull() {

		ListingResponse resp = new ListingResponse();
		resp.setId("100");

		List<DeliveryMethod> dmLst = new ArrayList<DeliveryMethod>();
		DeliveryMethod dm = new DeliveryMethod();
		dmLst.add(dm);
		resp.setDeliveryMethod(dmLst);

		List<FulfillmentMethod> fmLst = new ArrayList<FulfillmentMethod>();
		FulfillmentMethod fm = new FulfillmentMethod();
		fm.setFulfillmentMethodId(18L);
		fmLst.add(fm);
		resp.setFulfillmentMethod(fmLst);

		helper.addLocalizedMessagesForFmDm(resp, Locale.US);
	}

	@Test
	public void getMyListingByListingId_Test() throws Exception {
		ListingHelper helper = new ListingHelper();
		ListingResponse listResponse = helper
				.convertInventoryListingToAccountListing(populateinventoryListingResponse());
		assertNotNull(listResponse);

	}

	@Test
	public void testGetListingById() throws Exception {
		Mockito.when(svcLocator.locate(Mockito.anyString(), Mockito.anyList()))
				.thenReturn(webClient);
		Mockito.when(webClient.get()).thenReturn(getResponse());

		ListingResponse response = helper.getListingById(
				"C779915579FE5E14E04400212861B256", "2378908", "true");
		Assert.assertNotNull(response);
	}

	@Test
	public void testGetListingById_ReponseNotFound() throws Exception {
		Response response = Response.status(Status.NOT_FOUND).build();

		Mockito.when(svcLocator.locate(Mockito.anyString(), Mockito.anyList()))
				.thenReturn(webClient);
		Mockito.when(webClient.get()).thenReturn(response);

		ListingResponse listingResponse = helper.getListingById(
				"C779915579FE5E14E04400212861B256", "2378908", "true");
		Assert.assertNotNull(listingResponse);
	}

	@Test
	public void testGetListingById_ReponseUnauthorized() throws Exception {
		Response response = Response.status(Status.UNAUTHORIZED).build();

		Mockito.when(svcLocator.locate(Mockito.anyString(), Mockito.anyList()))
				.thenReturn(webClient);
		Mockito.when(webClient.get()).thenReturn(response);

		ListingResponse listingResponse = helper.getListingById(
				"C779915579FE5E14E04400212861B256", "2378908", "true");
		Assert.assertNotNull(listingResponse);
	}

	@Test
	public void testGetListingById_ReponseBadRequest() throws Exception {
		Response response = Response.status(Status.BAD_REQUEST).build();

		Mockito.when(svcLocator.locate(Mockito.anyString(), Mockito.anyList()))
				.thenReturn(webClient);
		Mockito.when(webClient.get()).thenReturn(response);

		ListingResponse listingResponse = helper.getListingById(
				"C779915579FE5E14E04400212861B256", "2378908", "true");
		Assert.assertNotNull(listingResponse);
	}

	private com.stubhub.domain.inventory.DTO.ListingResponse populateinventoryListingResponse() {
		com.stubhub.domain.inventory.DTO.ListingResponse inventoryListingResponse = new com.stubhub.domain.inventory.DTO.ListingResponse();
		List<Fee> feeList = new ArrayList<Fee>();
		Fee fee = new Fee();
		fee.setType(FeeType.SELL);
		fee.setDescription("SERVICEFEE");

		Money price = new Money();
		fee.setAmount(price);
		feeList.add(fee);
		inventoryListingResponse.setFees(feeList);
		inventoryListingResponse.setCcId(12345L);
		inventoryListingResponse.setContactId(12345L);
		inventoryListingResponse.setDeliveryOption(DeliveryOption.BARCODE);
		inventoryListingResponse.setDisplayPricePerTicket(price);
		inventoryListingResponse.setEndPricePerTicket(price);
		inventoryListingResponse.setEventDate("2013-04-29T16:00:00.000Z");
		inventoryListingResponse.setEventDescription("event Description");
		inventoryListingResponse.setEventId("22222");
		inventoryListingResponse.setExternalListingId("123456");
		inventoryListingResponse.setFaceValue(price);
		inventoryListingResponse.setId("12345");
		inventoryListingResponse.setInhandDate("2013-04-29T16:00:00.000Z");
		inventoryListingResponse.setInternalNotes("comments");
		inventoryListingResponse.setPaymentType("2");
		inventoryListingResponse.setPayoutPerTicket(price);
		inventoryListingResponse.setPreDelivered(true);
		inventoryListingResponse.setPricePerTicket(price);
		inventoryListingResponse.setPurchasePrice(price);
		inventoryListingResponse.setQuantity(1);
		inventoryListingResponse.setQuantityRemain(1);
		inventoryListingResponse.setSaleEndDate("2013-04-29T16:00:00.000Z");
		inventoryListingResponse.setSaleMethod(SaleMethod.FIXED);
		inventoryListingResponse.setSeats("1,2");
		inventoryListingResponse.setSection("Lower Box 112");
		inventoryListingResponse.setSplitOption(SplitOption.NOSINGLES);
		inventoryListingResponse.setSplitQuantity(1);
		inventoryListingResponse.setStartPricePerTicket(price);
		inventoryListingResponse.setStatus(ListingStatus.ACTIVE);
		Set<TicketTrait> tttlist = new HashSet<TicketTrait>();
		TicketTrait tt = new TicketTrait();
		tt.setId("123");
		tt.setType("Comment");
		tt.setName("name");
		tttlist.add(tt);

		inventoryListingResponse.setTicketTraits(tttlist);
		inventoryListingResponse.setVenueConfigSectionId(12345L);
		inventoryListingResponse.setVenueDescription("ATT Park");

		return inventoryListingResponse;
	}

	private Response getResponse() {
		Response response = new Response() {
			@Override
			public int getStatus() {
				return 200;
			}

			@Override
			public MultivaluedMap<String, Object> getMetadata() {
				return null;
			}

			@Override
			public Object getEntity() {
				return populateinventoryListingResponse();
			}
		};
		return response;
	}

}
