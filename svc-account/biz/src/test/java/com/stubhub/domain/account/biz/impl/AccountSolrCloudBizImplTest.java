package com.stubhub.domain.account.biz.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import com.stubhub.domain.account.context.MCIHeaderThreadLocal;
import com.stubhub.domain.account.context.MCIQueryParamThreadLocal;
import com.stubhub.newplatform.http.util.HttpClient4Util;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.EntityEnclosingRequestWrapper;
import org.apache.http.protocol.HttpContext;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.stubhub.domain.account.common.ListingSearchCriteria;
import com.stubhub.domain.account.common.MyOrderSearchCriteria;
import com.stubhub.domain.account.common.PaginationInput;
import com.stubhub.domain.account.common.SalesSearchCriteria;
import com.stubhub.domain.account.common.SalesSearchCriteria.Action;
import com.stubhub.domain.account.common.SalesSearchCriteria.Category;
import com.stubhub.domain.account.common.SortingDirective;
import com.stubhub.domain.account.common.enums.DeliveryOption;
import com.stubhub.domain.account.common.enums.ListingStatus;
import com.stubhub.domain.account.common.enums.SaleStatus;
import com.stubhub.domain.account.common.enums.SortColumnType;
import com.stubhub.domain.account.common.exception.AccountException;
import com.stubhub.domain.account.datamodel.dao.VenueConfigZoneSectionDAO;
import com.stubhub.domain.account.datamodel.entity.VenueConfigZoneSection;
import com.stubhub.domain.catalog.read.v3.intf.events.dto.response.Event;
import com.stubhub.newplatform.common.util.DateUtil;
import com.stubhub.newplatform.http.util.HttpClient4Util.SimpleHttpResponse;
import com.stubhub.newplatform.http.util.HttpClient4UtilHelper;
import com.stubhub.newplatform.property.MasterStubhubPropertiesWrapper;

@SuppressWarnings("unchecked")
public class AccountSolrCloudBizImplTest {

	@InjectMocks
	private AccountSolrCloudBizImpl accountBiz;
	@Mock
	private HttpClient4UtilHelper httpClient4UtilHelper;
	@Mock
	private MasterStubhubPropertiesWrapper masterStubhubProperties;
	ObjectMapper om = new ObjectMapper();

	@Mock
	private HttpRequest httpRequest;
	@Mock
	private HttpContext httpContext;

	@Mock
	EventUtil eventUtil;
	@Mock
	VenueConfigZoneSectionDAO venueConfigZoneSectionDAO;

	@BeforeMethod
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		accountBiz.afterPropertiesSet();
		Mockito.when(masterStubhubProperties.getProperty(MCIRequestUtil.SEARCH_MCI_API_URL_PROP_NAME,
				MCIRequestUtil.DEFAULT_SEARCH_MCI_API_V1)).thenReturn("http://api-int.slcq048.com/search/mci/v1");
		Mockito.when(masterStubhubProperties.getProperty(MCIRequestUtil.SEARCH_MCI_API_TIME_OUT_PROP_NAME,
				MCIRequestUtil.DEFAULT_SEARCH_MCI_API_TIME_OUT))
				.thenReturn(MCIRequestUtil.DEFAULT_SEARCH_MCI_API_TIME_OUT).thenReturn("abc");

	}


	@Test
	public void testMciRequestInterceptor() throws IOException, HttpException, URISyntaxException {
		DefaultHttpClient client= (DefaultHttpClient) HttpClient4Util.getHttpClient();
		when(masterStubhubProperties.getProperty("mci.headers")).thenReturn("Prefer");
		Enumeration<String> enumeration = Collections.enumeration(Arrays.asList("test"));
		MCIHeaderThreadLocal.set("Prefer",enumeration);
		MCIQueryParamThreadLocal.set("shStore","123");
		int count=client.getRequestInterceptorCount();
		HttpRequestInterceptor interceptor=client.getRequestInterceptor(count-1);
		EntityEnclosingRequestWrapper request=Mockito.mock(EntityEnclosingRequestWrapper.class);
		when(request.getURI()).thenReturn(new URI("http://www.test.com"));
		interceptor.process(request,httpContext);

		MCIQueryParamThreadLocal.set("shStore|","123");
		when(request.getURI()).thenReturn(new URI("http://www.test.com?a=1"));
		interceptor.process(request,httpContext);
	}

	@Test
	public void testGetDeliverySpecsCloud() throws Exception {
		SimpleHttpResponse value = Mockito.mock(SimpleHttpResponse.class);
		Mockito.when(value.getContent()).thenReturn("{}");
		Mockito.when(httpClient4UtilHelper.postToUrl(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyList(), Mockito.anyInt(),
				Mockito.anyBoolean())).thenReturn(value);
		String orders = "123";
		JsonNode node = accountBiz.getByIdList("sale", "id", Arrays.asList(orders.split(",")));
		Assert.assertNotNull(node);
	}

	@Test(expectedExceptions = { RuntimeException.class })
	public void testGetDeliverySpecsCloudWithIOException() throws Exception {
		SimpleHttpResponse value = Mockito.mock(SimpleHttpResponse.class);
		Mockito.when(value.getContent()).thenReturn("{}");
		Mockito.when(httpClient4UtilHelper.postToUrl(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyList(), Mockito.anyInt(),
				Mockito.anyBoolean())).thenThrow(IOException.class);
		String orders = "123";
		JsonNode node = accountBiz.getByIdList("sale", "id", Arrays.asList(orders.split(",")));
		Assert.assertNotNull(node);
	}

	@Test(expectedExceptions = { AccountException.class })
	public void testGetDeliverySpecsCloudWithAccountException() throws Exception {
		SimpleHttpResponse value = Mockito.mock(SimpleHttpResponse.class);
		Mockito.when(value.getContent()).thenReturn("{}");
		Mockito.when(httpClient4UtilHelper.postToUrl(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyList(), Mockito.anyInt(),
				Mockito.anyBoolean())).thenThrow(AccountException.class);
		String orders = "123";
		JsonNode node = accountBiz.getByIdList("sale", "id", Arrays.asList(orders.split(",")));

		Assert.assertNotNull(node);
	}

	@Test
	public void testGetSellerSales() throws Exception {
		SimpleHttpResponse value = Mockito.mock(SimpleHttpResponse.class);
		Mockito.when(value.getContent()).thenReturn("{}");
		Mockito.when(httpClient4UtilHelper.postToUrl(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyList(), Mockito.anyInt(),
				Mockito.anyBoolean())).thenReturn(value);
		SalesSearchCriteria request = new SalesSearchCriteria();
		request.setSellerId(1234L);
		JsonNode node = accountBiz.getSellerSales(request);
		Assert.assertNotNull(node);

		node = accountBiz.getSellerSales(request);
		Assert.assertNotNull(node);
	}

	@Test(expectedExceptions = { RuntimeException.class })
	public void testGetSellerSalesWithIOException() throws Exception {
		SimpleHttpResponse value = Mockito.mock(SimpleHttpResponse.class);
		Mockito.when(value.getContent()).thenReturn("{}");
		Mockito.when(httpClient4UtilHelper.postToUrl(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyList(), Mockito.anyInt(),
				Mockito.anyBoolean())).thenThrow(new IOException());
		SalesSearchCriteria request = new SalesSearchCriteria();
		request.setSellerId(1234L);
		accountBiz.getSellerSales(request);
	}

	@Test(expectedExceptions = { AccountException.class })
	public void testGetSellerSalesWithAccountException() throws Exception {
		SimpleHttpResponse value = Mockito.mock(SimpleHttpResponse.class);
		Mockito.when(value.getContent()).thenReturn("{}");
		Mockito.when(httpClient4UtilHelper.postToUrl(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyList(), Mockito.anyInt(),
				Mockito.anyBoolean())).thenThrow(new RuntimeException());
		SalesSearchCriteria request = new SalesSearchCriteria();
		request.setSellerId(1234L);
		accountBiz.getSellerSales(request);
	}

	@Test
	public void testGetCSOrders() throws Exception {
		SimpleHttpResponse value = Mockito.mock(SimpleHttpResponse.class);
		Mockito.when(value.getContent()).thenReturn("{}");
		Mockito.when(httpClient4UtilHelper.postToUrl(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyList(), Mockito.anyInt(),
				Mockito.anyBoolean())).thenReturn(value);
		SalesSearchCriteria request = new SalesSearchCriteria();
		request.setSellerId(12345L);
		request.setBuyerId(54321L);
		request.setSubbedId("(12345 OR 54321)");
		JsonNode node = accountBiz.getCSOrderDetails(request);
		Assert.assertNotNull(node);
	}

	@Test(expectedExceptions = { RuntimeException.class })
	public void testGetCSOrdersWithIOException() throws Exception {
		SimpleHttpResponse value = Mockito.mock(SimpleHttpResponse.class);
		Mockito.when(value.getContent()).thenReturn("{}");
		Mockito.when(httpClient4UtilHelper.postToUrl(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyList(), Mockito.anyInt(),
				Mockito.anyBoolean())).thenThrow(new IOException());
		SalesSearchCriteria request = new SalesSearchCriteria();
		request.setSellerId(12345L);
		request.setBuyerId(54321L);
		request.setSubbedId("(12345 OR 54321)");
		accountBiz.getCSOrderDetails(request);
	}

	@Test(expectedExceptions = { AccountException.class })
	public void testGetCSOrdersWithAccountException() throws Exception {
		SimpleHttpResponse value = Mockito.mock(SimpleHttpResponse.class);
		Mockito.when(value.getContent()).thenReturn("{}");
		Mockito.when(httpClient4UtilHelper.postToUrl(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyList(), Mockito.anyInt(),
				Mockito.anyBoolean())).thenThrow(new RuntimeException());
		SalesSearchCriteria request = new SalesSearchCriteria();
		request.setSellerId(12345L);
		request.setBuyerId(54321L);
		request.setSubbedId("(12345 OR 54321)");
		accountBiz.getCSOrderDetails(request);
	}

	private Event getEvent() {
		Event response = new Event();
		com.stubhub.domain.catalog.read.v3.intf.events.dto.response.Venue venue = new com.stubhub.domain.catalog.read.v3.intf.events.dto.response.Venue();
		venue.setConfigurationId("1234");
		response.setVenue(venue);
		return response;
	}

	@Test
	public void testBuildRequestForZoneIds() throws Exception {
		List<VenueConfigZoneSection> venueConfigZoneList = new ArrayList<VenueConfigZoneSection>();
		VenueConfigZoneSection venueConfigZone = new VenueConfigZoneSection();
		venueConfigZone.setZoneId(11L);
		venueConfigZone.setSectionId(11L);
		venueConfigZoneList.add(venueConfigZone);
		Event event = getEvent();
		event.setId(12345L);

		when(eventUtil.getEventV3(Mockito.eq(String.valueOf(event.getId())), Mockito.anyString())).thenReturn(event);
		when(venueConfigZoneSectionDAO
				.getZoneSectionByVenueConfigId(Long.valueOf(event.getVenue().getConfigurationId())))
						.thenReturn(venueConfigZoneList);

		SalesSearchCriteria request = new SalesSearchCriteria();
		request.setEventId(event.getId());
		request.setZoneIds(new String[] { venueConfigZone.getZoneId().toString() });
		String buildRequest = accountBiz.buildRequest(request);
		Assert.assertTrue(buildRequest.contains("\"venueConfigSectionsId\""));
	}

	@Test
	public void testBuildRequest() throws Exception {
		SalesSearchCriteria request = new SalesSearchCriteria();
		PaginationInput paginationInput = new PaginationInput();
		request.setCategory(Category.OPEN);
		List<SortingDirective> sortingDirectives = new ArrayList<SortingDirective>();
		SortingDirective sd1 = new SortingDirective();
		sd1.setSortColumnType(SortColumnType.EVENT_DESCRIPTION);
		sd1.setSortDirection(0);
		sortingDirectives.add(sd1);
		SortingDirective sd2 = new SortingDirective();
		sd2.setSortColumnType(SortColumnType.INHAND_DATE);
		sd2.setSortDirection(1);
		sortingDirectives.add(sd2);
		request.setSortingDirectives(sortingDirectives);
		request.setIncludeEventSummary(true);
		request.setIncludeVenueSummary(true);
		request.setIncludeGenreSummary(true);
		request.setPaginationInput(paginationInput);
		request.setQ("test");
		request.setListingIds("1234,,3456,~~,");
		request.setExternalListingIds("1234,,3456,~~,");
		String buildRequest = accountBiz.buildRequest(request);
		JsonNode node = om.readTree(buildRequest);
		Assert.assertNotNull(node.get("filter"));
		Assert.assertNotNull(node.get("query"));
		ArrayNode sortNode = (ArrayNode) node.get("sort");
		Assert.assertNotNull(sortNode);
		Assert.assertEquals(sortNode.size(), 3);
		Assert.assertNotNull(node.get("aggregations"));
		request = new SalesSearchCriteria();
		request.setCategory(Category.COMPLETE);
		request.setStatus("all");
		buildRequest = accountBiz.buildRequest(request);
		node = om.readTree(buildRequest);
		Assert.assertNotNull(node.get("filter"));

		request = new SalesSearchCriteria();
		request.setCategory(null);
		request.setStatus("all");
		request.setSellerCategoryCode("1");
		buildRequest = accountBiz.buildRequest(request);
		node = om.readTree(buildRequest);
		Assert.assertNotNull(node.get("filter"));

		request = new SalesSearchCriteria();
		request.setCategory(null);
		request.setIncludePending(true);
		request.setSellerCategoryCode("1,2,3");
		buildRequest = accountBiz.buildRequest(request);
		node = om.readTree(buildRequest);
		Assert.assertNotNull(node.get("filter"));

		request = new SalesSearchCriteria();
		request.setEventId(1234L);
		request.setGenreId("(197)");
		request.setVenueId("(28)");
		request.setCategory(null);
		request.setSellerCategoryCode("all");
		request.setIncludePending(false);
		buildRequest = accountBiz.buildRequest(request);
		node = om.readTree(buildRequest);
		Assert.assertNotNull(node.get("filter"));

		request = new SalesSearchCriteria();
		request.setEventId(1234L);
		request.setGenreId("197");
		request.setVenueId("28");
		request.setCategory(null);
		request.setSellerCategoryCode("all");
		request.setIncludePending(false);
		buildRequest = accountBiz.buildRequest(request);
		node = om.readTree(buildRequest);
		Assert.assertNotNull(node.get("filter"));

		request = new SalesSearchCriteria();
		request.setEventId(1234L);
		request.setGenreId("()");
		request.setVenueId("()");
		request.setCategory(null);
		request.setSellerCategoryCode("all");
		request.setIncludePending(false);
		buildRequest = accountBiz.buildRequest(request);
		node = om.readTree(buildRequest);
		Assert.assertNotNull(node.get("filter"));

		request = new SalesSearchCriteria();
		Event event = getEvent();
		event.setId(12345L);
		List<VenueConfigZoneSection> venueConfigZoneList = new ArrayList<VenueConfigZoneSection>();
		VenueConfigZoneSection venueConfigZone = new VenueConfigZoneSection();
		venueConfigZone.setZoneId(11L);
		venueConfigZone.setSectionId(12L);
		venueConfigZoneList.add(venueConfigZone);
		when(venueConfigZoneSectionDAO
				.getZoneSectionByVenueConfigId(Long.valueOf(event.getVenue().getConfigurationId())))
				.thenReturn(venueConfigZoneList);
		request.setEventId(12345L);
		when(eventUtil.getEventV3("12345", null)).thenReturn(event);
		request.setSectionIds(new String[] { "12345", "54321" });
		request.setZoneIds(new String[] {"11"});
		buildRequest = accountBiz.buildRequest(request);
		node = om.readTree(buildRequest);
		Assert.assertNotNull(node.get("filter"));
		Assert.assertTrue(buildRequest.contains("\"venueConfigSectionsId\""));

		request = new SalesSearchCriteria();
		request.setRowDescs(new String[] { "12345", "54321" });
		buildRequest = accountBiz.buildRequest(request);
		node = om.readTree(buildRequest);
		Assert.assertNotNull(node.get("filter"));
		Assert.assertTrue(buildRequest.contains("\"rowDesc\""));

		request = new SalesSearchCriteria();
		request.setExcludeCancel(true);
		buildRequest = accountBiz.buildRequest(request);
		node = om.readTree(buildRequest);
		Assert.assertNotNull(node.get("filter"));
		Assert.assertTrue(buildRequest.contains("\"orderProcStatusId\""));
		Assert.assertTrue(buildRequest.contains("\"orderProcSubStatusCode\""));

		request = new SalesSearchCriteria();
		request.setExcludeZeroCost(true);
		buildRequest = accountBiz.buildRequest(request);
		node = om.readTree(buildRequest);
		Assert.assertNotNull(node.get("filter"));
		Assert.assertTrue(buildRequest.contains("\"statsCostPerTicket\""));

		request = new SalesSearchCriteria();
		request.setIncludeCostPerTicketSummary(true);
		buildRequest = accountBiz.buildRequest(request);
		node = om.readTree(buildRequest);
		Assert.assertTrue(buildRequest.contains("\"aggregations\""));
		Assert.assertTrue(buildRequest.contains("\"statsCostPerTicket\""));

		request = new SalesSearchCriteria();
		request.setIncludePricePerTicketSummary(true);
		buildRequest = accountBiz.buildRequest(request);
		node = om.readTree(buildRequest);
		Assert.assertTrue(buildRequest.contains("\"aggregations\""));
		Assert.assertTrue(buildRequest.contains("\"statsPricePerTicket\""));

		request = new SalesSearchCriteria();
		request.setAction(Action.COURIER);
		buildRequest = accountBiz.buildRequest(request);
		node = om.readTree(buildRequest);
		Assert.assertTrue(buildRequest.contains("\"fulfillmentMethodId\""));
		Assert.assertTrue(buildRequest.contains("\"15\""));

		request = new SalesSearchCriteria();
		request.setAction(Action.PROOF);
		buildRequest = accountBiz.buildRequest(request);
		node = om.readTree(buildRequest);
		Assert.assertTrue(buildRequest.contains("\"orderProcSubStatusCode\""));
		Assert.assertTrue(buildRequest.contains("\"55\""));

		request = new SalesSearchCriteria();
		request.setAction(Action.VERIFY);
		buildRequest = accountBiz.buildRequest(request);
		node = om.readTree(buildRequest);
		Assert.assertTrue(buildRequest.contains("\"orderProcSubStatusCode\""));
		Assert.assertTrue(buildRequest.contains("\"52\""));

		Assert.assertEquals(SalesSearchCriteria.Action.fromString(Action.PROOF.name()), Action.PROOF);
		Assert.assertEquals(SalesSearchCriteria.Action.fromString(Action.VERIFY.name()), Action.VERIFY);
		Assert.assertEquals(SalesSearchCriteria.Action.fromString(Action.COURIER.name()), Action.COURIER);
		Assert.assertEquals(SalesSearchCriteria.Action.fromString(null), null);
	}

	@Test
	public void testBuildRequestForSaleStatus() throws Exception {
		SalesSearchCriteria request = new SalesSearchCriteria();
		request.setStatus(SaleStatus.CONFIRMED.name());
		request.setFromSaleId(100L);
		request.setPriceMin(10.0D);
		request.setMinSoldQuantity(1);
		request.setInHandDateMin(DateUtil.getNowCalUTC());
		request.setSaleDateMin(DateUtil.getNowCalUTC());
		request.setEventDateMin(DateUtil.getNowCalUTC());
		request.setEventDateUTCMin(DateUtil.getNowCalUTC());
		request.setDateLastModifiedMax(DateUtil.getNowCalUTC());
		request.setDateLastModifiedMin(DateUtil.getNowCalUTC());
		String buildRequest = accountBiz.buildRequest(request);
		JsonNode node = om.readTree(buildRequest);
		Assert.assertNotNull(node.get("filter"));

		request = new SalesSearchCriteria();
		request.setStatus(SaleStatus.CANCELLED.name());
		request.setFromSaleId(100L);
		request.setToSaleId(1000L);
		request.setPriceMin(10.0D);
		request.setPriceMax(100.0D);
		request.setMinSoldQuantity(1);
		request.setMaxSoldQuantity(10);
		request.setInHandDateMin(DateUtil.getNowCalUTC());
		request.setInHandDateMax(DateUtil.getNowCalUTC());
		request.setSaleDateMin(DateUtil.getNowCalUTC());
		request.setSaleDateMax(DateUtil.getNowCalUTC());
		request.setEventDateMin(DateUtil.getNowCalUTC());
		request.setEventDateMax(DateUtil.getNowCalUTC());
		request.setEventDateUTCMin(DateUtil.getNowCalUTC());
		request.setEventDateUTCMax(DateUtil.getNowCalUTC());
		buildRequest = accountBiz.buildRequest(request);
		node = om.readTree(buildRequest);
		Assert.assertNotNull(node.get("filter"));

		request = new SalesSearchCriteria();
		request.setStatus(SaleStatus.SHIPPED.name());
		request.setToSaleId(1000L);
		request.setPriceMax(100.0D);
		request.setMaxSoldQuantity(10);
		request.setInHandDateMax(DateUtil.getNowCalUTC());
		request.setSaleDateMax(DateUtil.getNowCalUTC());
		request.setEventDateMax(DateUtil.getNowCalUTC());
		request.setEventDateUTCMax(DateUtil.getNowCalUTC());
		buildRequest = accountBiz.buildRequest(request);
		node = om.readTree(buildRequest);
		Assert.assertNotNull(node.get("filter"));

		request = new SalesSearchCriteria();
		request.setStatus(SaleStatus.DELIVERED.name());
		request.setSaleId(1000L);
		request.setPrice(100.0D);
		request.setSoldQuantity(10);
		buildRequest = accountBiz.buildRequest(request);
		node = om.readTree(buildRequest);
		Assert.assertNotNull(node.get("filter"));

		request = new SalesSearchCriteria();
		request.setStatus(SaleStatus.SUBSOFFERED.name());
		buildRequest = accountBiz.buildRequest(request);
		node = om.readTree(buildRequest);
		Assert.assertNotNull(node.get("filter"));

		request = new SalesSearchCriteria();
		request.setStatus(SaleStatus.ONHOLD.name() + "," + SaleStatus.PENDING.name());
		buildRequest = accountBiz.buildRequest(request);
		node = om.readTree(buildRequest);
		Assert.assertNotNull(node.get("filter"));

		request = new SalesSearchCriteria();
		request.setStatus(SaleStatus.PENDINGREVIEW.name() + "," + SaleStatus.PENDING.name());
		buildRequest = accountBiz.buildRequest(request);
		node = om.readTree(buildRequest);
		Assert.assertNotNull(node.get("filter"));

		request = new SalesSearchCriteria();
		request.setStatus(SaleStatus.PENDINGREVIEW.name() + "," + SaleStatus.DELIVERYEXCEPTION.name());
		buildRequest = accountBiz.buildRequest(request);
		node = om.readTree(buildRequest);
		Assert.assertNotNull(node.get("filter"));

	}

	@Test
	public void testBuildRequestForDeliveryOption() throws Exception {
		SalesSearchCriteria request = new SalesSearchCriteria();
		request.setDeliveryOption("all");
		request.setAction(Action.GENERATE);
		String buildRequest = accountBiz.buildRequest(request);
		JsonNode node = om.readTree(buildRequest);
		Assert.assertNotNull(node.get("filter"));

		request = new SalesSearchCriteria();
		request.setDeliveryOption(DeliveryOption.BARCODE.name());
		request.setAction(Action.REPRINT);
		buildRequest = accountBiz.buildRequest(request);
		node = om.readTree(buildRequest);
		Assert.assertNotNull(node.get("filter"));

		request = new SalesSearchCriteria();
		request.setDeliveryOption(DeliveryOption.PDF.name());
		request.setAction(Action.UPLOAD);
		buildRequest = accountBiz.buildRequest(request);
		node = om.readTree(buildRequest);
		Assert.assertNotNull(node.get("filter"));

		request = new SalesSearchCriteria();
		request.setDeliveryOption(DeliveryOption.FEDEX.name());
		request.setAction(Action.ENTER);
		buildRequest = accountBiz.buildRequest(request);
		node = om.readTree(buildRequest);
		Assert.assertNotNull(node.get("filter"));

		request = new SalesSearchCriteria();
		request.setDeliveryOption(DeliveryOption.LMS.name());
		buildRequest = accountBiz.buildRequest(request);
		node = om.readTree(buildRequest);
		Assert.assertNotNull(node.get("filter"));

		request = new SalesSearchCriteria();
		request.setDeliveryOption(DeliveryOption.UPS.name());
		buildRequest = accountBiz.buildRequest(request);
		node = om.readTree(buildRequest);
		Assert.assertNotNull(node.get("filter"));

		request = new SalesSearchCriteria();
		request.setDeliveryOption(DeliveryOption.ROYALMAIL.name());
		buildRequest = accountBiz.buildRequest(request);
		node = om.readTree(buildRequest);
		Assert.assertNotNull(node.get("filter"));

		request = new SalesSearchCriteria();
		request.setDeliveryOption(DeliveryOption.DEUTSCHEPOST.name() + "," + DeliveryOption.OTHER);
		buildRequest = accountBiz.buildRequest(request);
		node = om.readTree(buildRequest);
		Assert.assertNotNull(node.get("filter"));

		request = new SalesSearchCriteria();
		request.setDeliveryOption(DeliveryOption.COURIER.name());
		buildRequest = accountBiz.buildRequest(request);
		node = om.readTree(buildRequest);
		Assert.assertNotNull(node.get("filter"));
		buildRequest = accountBiz.buildRequest(request, null);
		node = om.readTree(buildRequest);
		Assert.assertNotNull(node.get("filter"));

		request = new SalesSearchCriteria();
		request.setDeliveryOption(DeliveryOption.MOBILE_TICKET.name() + "," + DeliveryOption.EXTERNAL_TRANSFER.name());
		buildRequest = accountBiz.buildRequest(request);
		node = om.readTree(buildRequest);
		Assert.assertNotNull(node.get("filter"));
		buildRequest = accountBiz.buildRequest(request, null);
		node = om.readTree(buildRequest);
		Assert.assertNotNull(node.get("filter"));

	}

	@Test
	public void testGetSellerListings() throws Exception {
		SimpleHttpResponse value = Mockito.mock(SimpleHttpResponse.class);
		Mockito.when(value.getContent()).thenReturn("{}");
		Mockito.when(httpClient4UtilHelper.postToUrl(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyList(), Mockito.anyInt(),
				Mockito.anyBoolean())).thenReturn(value);
		ListingSearchCriteria request = new ListingSearchCriteria();
		request.setSellerId(1234L);
		JsonNode node = accountBiz.getSellerListings(request);
		Assert.assertNotNull(node);

	}

	@Test(expectedExceptions = { RuntimeException.class })
	public void testGetSellerListingsWithIOException() throws Exception {
		SimpleHttpResponse value = Mockito.mock(SimpleHttpResponse.class);
		Mockito.when(value.getContent()).thenReturn("{}");
		Mockito.when(httpClient4UtilHelper.postToUrl(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyList(), Mockito.anyInt(),
				Mockito.anyBoolean())).thenThrow(new IOException());
		ListingSearchCriteria request = new ListingSearchCriteria();
		request.setSellerId(1234L);
		accountBiz.getSellerListings(request);
	}

	@Test(expectedExceptions = { AccountException.class })
	public void testGetSellerListingsWithAccountException() throws Exception {
		SimpleHttpResponse value = Mockito.mock(SimpleHttpResponse.class);
		Mockito.when(value.getContent()).thenReturn("{}");
		Mockito.when(httpClient4UtilHelper.postToUrl(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyList(), Mockito.anyInt(),
				Mockito.anyBoolean())).thenThrow(new RuntimeException());
		ListingSearchCriteria request = new ListingSearchCriteria();
		request.setSellerId(1234L);
		accountBiz.getSellerListings(request);
	}

	@Test
	public void testBuildSellerListingsRequest() throws Exception {
		ListingSearchCriteria request = new ListingSearchCriteria();
		PaginationInput paginationInput = new PaginationInput();
		List<SortingDirective> sortingDirectives = new ArrayList<SortingDirective>();
		SortingDirective sd1 = new SortingDirective();
		sd1.setSortColumnType(SortColumnType.EVENT_DESCRIPTION);
		sd1.setSortDirection(0);
		sortingDirectives.add(sd1);
		SortingDirective sd2 = new SortingDirective();
		sd2.setSortColumnType(SortColumnType.INHAND_DATE);
		sd2.setSortDirection(1);
		sortingDirectives.add(sd2);
		request.setSortingDirectives(sortingDirectives);
		request.setIncludeEventSummary(true);
		request.setIncludeVenueSummary(true);
		request.setIncludeGenreSummary(true);
		request.setPaginationInput(paginationInput);
		request.setQ("test");
		request.setListingId(1234L);
		String buildRequest = accountBiz.buildRequest(request);
		JsonNode node = om.readTree(buildRequest);
		Assert.assertNotNull(node.get("filter"));
		Assert.assertNotNull(node.get("query"));
		ArrayNode sortNode = (ArrayNode) node.get("sort");
		Assert.assertNotNull(sortNode);
		Assert.assertEquals(sortNode.size(), 3);
		Assert.assertNotNull(node.get("aggregations"));
		request = new ListingSearchCriteria();
		request.setStatus(ListingStatus.ACTIVE);
		buildRequest = accountBiz.buildRequest(request);
		node = om.readTree(buildRequest);
		Assert.assertNotNull(node.get("filter"));

		request = new ListingSearchCriteria();
		request.setEventId("123");
		buildRequest = accountBiz.buildRequest(request);
		node = om.readTree(buildRequest);
		Assert.assertNotNull(node.get("filter"));

		request = new ListingSearchCriteria();
		Calendar eventMax = Calendar.getInstance();
		eventMax.add(Calendar.DAY_OF_MONTH, 30);
		Calendar eventMin = Calendar.getInstance();
		request.setEventDateMax(eventMax);
		request.setEventDateMin(eventMin);
		buildRequest = accountBiz.buildRequest(request);
		node = om.readTree(buildRequest);
		Assert.assertNotNull(node.get("filter"));


		request = new ListingSearchCriteria();
		Calendar lastModifiedMax = Calendar.getInstance();
		eventMax.add(Calendar.DAY_OF_MONTH, 30);
		Calendar lastModifiedMin = Calendar.getInstance();
		request.setDateLastModifiedMax(lastModifiedMax);
		request.setDateLastModifiedMin(lastModifiedMin);
		buildRequest = accountBiz.buildRequest(request);
		node = om.readTree(buildRequest);
		Assert.assertNotNull(node.get("filter"));

		request = new ListingSearchCriteria();
		request.setGenreId("(1234)");
		buildRequest = accountBiz.buildRequest(request);
		node = om.readTree(buildRequest);
		Assert.assertNotNull(node.get("filter"));

		request = new ListingSearchCriteria();
		request.setGenreId("1234");
		request.setVenueId("(123456)");
		buildRequest = accountBiz.buildRequest(request);
		node = om.readTree(buildRequest);
		Assert.assertNotNull(node.get("filter"));

		request = new ListingSearchCriteria();
		request.setPrice(10.00);
		buildRequest = accountBiz.buildRequest(request);
		node = om.readTree(buildRequest);
		Assert.assertNotNull(node.get("filter"));

		request = new ListingSearchCriteria();
		request.setPriceMax(100.00);
		request.setPriceMax(20.00);
		buildRequest = accountBiz.buildRequest(request);
		node = om.readTree(buildRequest);
		Assert.assertNotNull(node.get("filter"));

		request = new ListingSearchCriteria();
		request.setPriceMax(20.00);
		buildRequest = accountBiz.buildRequest(request);
		node = om.readTree(buildRequest);
		Assert.assertNotNull(node.get("filter"));
		
		request = new ListingSearchCriteria();
		request.setGenreId(" ");
		request.setVenueId(" ");
		request.setPriceMin(50.00);
		buildRequest = accountBiz.buildRequest(request);
		node = om.readTree(buildRequest);
		Assert.assertNotNull(node.get("filter"));

		request = new ListingSearchCriteria();
		request.setGenreId(" ");
		request.setVenueId(" ");
		request.setPriceMax(100.00);
		buildRequest = accountBiz.buildRequest(request);
		node = om.readTree(buildRequest);
		Assert.assertNotNull(node.get("filter"));
		
		request = new ListingSearchCriteria();
		request.setGenreId(" ");
		request.setVenueId(" ");
		request.setPriceMin(50.00);
		request.setPriceMax(100.00);
		buildRequest = accountBiz.buildRequest(request);
		node = om.readTree(buildRequest);
		Assert.assertNotNull(node.get("filter"));

		request = new ListingSearchCriteria();
		request.setVenueId("123");
		request.setRows(1);
		buildRequest = accountBiz.buildRequest(request);
		node = om.readTree(buildRequest);
		Assert.assertNotNull(node.get("filter"));

		request = new ListingSearchCriteria();
		request.setExternalListingId("abc123");
		request.setRows(1);
		buildRequest = accountBiz.buildRequest(request);
		node = om.readTree(buildRequest);
		Assert.assertNotNull(node.get("filter"));

		request = new ListingSearchCriteria();
		request.setDeliveryOption(DeliveryOption.COURIER);
		buildRequest = accountBiz.buildRequest(request);
		node = om.readTree(buildRequest);
		Assert.assertNotNull(node.get("filter"));

		request = new ListingSearchCriteria();
		request.setDeliveryOption(DeliveryOption.LOCALDELIVERY);
		buildRequest = accountBiz.buildRequest(request);
		node = om.readTree(buildRequest);
		Assert.assertNotNull(node.get("filter"));

		request = new ListingSearchCriteria();
		request.setStatus(ListingStatus.SOLD);
		buildRequest = accountBiz.buildRequest(request);
		node = om.readTree(buildRequest);
		Assert.assertNotNull(node.get("filter"));
	}

	@Test
	public void testBuildSellerListingsRequestForListingStatus() throws Exception {
		ListingSearchCriteria request = new ListingSearchCriteria();

		request.setStatus(ListingStatus.ACTIVE);
		String buildRequest = accountBiz.buildRequest(request);
		JsonNode node = om.readTree(buildRequest);
		Assert.assertNotNull(node.get("filter"));

		request.setStatus(ListingStatus.DELETED);
		buildRequest = accountBiz.buildRequest(request);
		node = om.readTree(buildRequest);
		Assert.assertNotNull(node.get("filter"));

		request.setStatus(ListingStatus.EXPIRED);
		buildRequest = accountBiz.buildRequest(request);
		node = om.readTree(buildRequest);
		Assert.assertNotNull(node.get("filter"));

		request.setStatus(ListingStatus.INACTIVE);
		buildRequest = accountBiz.buildRequest(request);
		node = om.readTree(buildRequest);
		Assert.assertNotNull(node.get("filter"));

		request.setStatus(ListingStatus.INCOMPLETE);
		buildRequest = accountBiz.buildRequest(request);
		node = om.readTree(buildRequest);
		Assert.assertNotNull(node.get("filter"));

		request.setStatus(ListingStatus.PENDING);
		buildRequest = accountBiz.buildRequest(request);
		node = om.readTree(buildRequest);
		Assert.assertNotNull(node.get("filter"));

	}

	@Test
	public void testBuildSellerListingsRequestForDeliveryOption() throws Exception {
		ListingSearchCriteria request = new ListingSearchCriteria();

		request.setDeliveryOption(DeliveryOption.BARCODE);
		String buildRequest = accountBiz.buildRequest(request);
		JsonNode node = om.readTree(buildRequest);
		Assert.assertNotNull(node.get("filter"));

		request.setDeliveryOption(DeliveryOption.DEUTSCHEPOST);
		buildRequest = accountBiz.buildRequest(request);
		node = om.readTree(buildRequest);
		Assert.assertNotNull(node.get("filter"));

		request.setDeliveryOption(DeliveryOption.FEDEX);
		buildRequest = accountBiz.buildRequest(request);
		node = om.readTree(buildRequest);
		Assert.assertNotNull(node.get("filter"));

		request.setDeliveryOption(DeliveryOption.LMS);
		buildRequest = accountBiz.buildRequest(request);
		node = om.readTree(buildRequest);
		Assert.assertNotNull(node.get("filter"));

		request.setDeliveryOption(DeliveryOption.PDF);
		buildRequest = accountBiz.buildRequest(request);
		node = om.readTree(buildRequest);
		Assert.assertNotNull(node.get("filter"));

		request.setDeliveryOption(DeliveryOption.BARCODE);
		buildRequest = accountBiz.buildRequest(request);
		node = om.readTree(buildRequest);
		Assert.assertNotNull(node.get("filter"));

		request.setDeliveryOption(DeliveryOption.ROYALMAIL);
		buildRequest = accountBiz.buildRequest(request);
		node = om.readTree(buildRequest);
		Assert.assertNotNull(node.get("filter"));

		request.setDeliveryOption(DeliveryOption.UPS);
		buildRequest = accountBiz.buildRequest(request);
		node = om.readTree(buildRequest);
		Assert.assertNotNull(node.get("filter"));
		
		request.setDeliveryOption(DeliveryOption.FLASHSEAT);
		buildRequest = accountBiz.buildRequest(request);
		node = om.readTree(buildRequest);
		Assert.assertNotNull(node.get("filter"));
	}

	@Test
	public void testGetBuyerOrders() throws Exception {
		SimpleHttpResponse value = Mockito.mock(SimpleHttpResponse.class);
		Mockito.when(value.getContent()).thenReturn("{}");
		Mockito.when(httpClient4UtilHelper.postToUrl(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyList(), Mockito.anyInt(),
				Mockito.anyBoolean())).thenReturn(value);
		MyOrderSearchCriteria criteria = new MyOrderSearchCriteria();
		criteria.setBuyerId("1234");
		JsonNode node = accountBiz.getBuyerOrders(criteria);
		Assert.assertNotNull(node);

		criteria.setStatus("ongoing");
		criteria.setBuyerId("");
		criteria.setOrderDate("2012-03-03 TO 2015-03-05");
		criteria.setOrderBy(new String[] { "EVENTDESC asc", "", "test" });
		node = accountBiz.getBuyerOrders(criteria);
		Assert.assertNotNull(node);

		criteria.setStatus("past");
		criteria.setOrderDate("[2012-03-03 TO 2015-03-05]");
		node = accountBiz.getBuyerOrders(criteria);
		Assert.assertNotNull(node);

		criteria.setStatus("past");
		criteria.setOrderDate("2012-03-03");
		node = accountBiz.getBuyerOrders(criteria);
		Assert.assertNotNull(node);
	}

	@Test(expectedExceptions = { AccountException.class })
	public void testGetBuyerOrdersWithIOException() throws Exception {
		Mockito.when(httpClient4UtilHelper.postToUrl(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyList(), Mockito.anyInt(),
				Mockito.anyBoolean())).thenThrow(new IOException());
		MyOrderSearchCriteria criteria = new MyOrderSearchCriteria();
		criteria.setBuyerId("1234");
		accountBiz.getBuyerOrders(criteria);
	}

	@Test(expectedExceptions = { AccountException.class })
	public void testGetBuyerOrdersWithAccountException() throws Exception {
		Mockito.when(httpClient4UtilHelper.postToUrl(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyList(), Mockito.anyInt(),
				Mockito.anyBoolean())).thenThrow(new RuntimeException());
		MyOrderSearchCriteria criteria = new MyOrderSearchCriteria();
		criteria.setBuyerId("1234");
		accountBiz.getBuyerOrders(criteria);
	}

	@Test
	public void testGetListings() throws Exception {
		SimpleHttpResponse value = Mockito.mock(SimpleHttpResponse.class);
		Mockito.when(value.getContent()).thenReturn("{}");
		Mockito.when(httpClient4UtilHelper.postToUrl(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyList(), Mockito.anyInt(),
				Mockito.anyBoolean())).thenReturn(value);
		JsonNode node = accountBiz.getSellerListings("123,null,124", "null", "", null);
		Assert.assertNotNull(node);
		node = accountBiz.getSellerListings("null", "234", "ACTIVE,PENDING", null);
		Assert.assertNotNull(node);
		node = accountBiz.getSellerListings("null", "234", "WRONG", null);
		Assert.assertNotNull(node);
	}

	@Test(expectedExceptions = { AccountException.class })
	public void testGetListingsWithIOException() throws Exception {
		Mockito.when(httpClient4UtilHelper.postToUrl(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(),
				Mockito.anyBoolean())).thenThrow(new IOException());
		accountBiz.getSellerListings("", "", "", null);
	}

	@Test(expectedExceptions = { AccountException.class })
	public void testGetListingWithAccountException() throws Exception {
		Mockito.when(httpClient4UtilHelper.postToUrl(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyList(), Mockito.anyInt(),
				Mockito.anyBoolean())).thenThrow(new RuntimeException());
		accountBiz.getSellerListings("", "", "", new PaginationInput());
	}

	@Test
	public void testGetSellerEventSales() throws Exception {
		SimpleHttpResponse value = Mockito.mock(SimpleHttpResponse.class);
		Mockito.when(value.getContent()).thenReturn("{}");
		Mockito.when(httpClient4UtilHelper.postToUrl(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyList(), Mockito.anyInt(),
				Mockito.anyBoolean())).thenReturn(value);
		SalesSearchCriteria criteria = new SalesSearchCriteria();
		criteria.setEventId(1234L);
		SortingDirective sortingDirective = new SortingDirective();
		sortingDirective.setSortDirection(0);
		sortingDirective.setSortColumnType(SortColumnType.SOLD_DATE);
		List<SortingDirective> sortingDirectives = Arrays.asList(sortingDirective);
		criteria.setSortingDirectives(sortingDirectives);
		JsonNode node = accountBiz.getSellerEventSales(criteria);
		Assert.assertNotNull(node);
		criteria.setSectionIds(new String[] { "1234", "0", "abc" });
		node = accountBiz.getSellerEventSales(criteria);
		Assert.assertNotNull(node);
	}

	@Test(expectedExceptions = { AccountException.class })
	public void testGetSellerEventSalesWithIOException() throws Exception {
		Mockito.when(httpClient4UtilHelper.postToUrl(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(),
				Mockito.anyBoolean())).thenThrow(new IOException());
		SalesSearchCriteria criteria = new SalesSearchCriteria();
		criteria.setEventId(1234L);
		accountBiz.getSellerEventSales(criteria);
	}

	@Test(expectedExceptions = { AccountException.class })
	public void testGetSellerEventSalesWithAccountException() throws Exception {
		Mockito.when(httpClient4UtilHelper.postToUrl(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyList(), Mockito.anyInt(),
				Mockito.anyBoolean())).thenThrow(new RuntimeException());
		SalesSearchCriteria criteria = new SalesSearchCriteria();
		criteria.setEventId(1234L);
		accountBiz.getSellerEventSales(criteria);
	}

}
