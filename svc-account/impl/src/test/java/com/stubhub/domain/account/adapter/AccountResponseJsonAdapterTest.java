package com.stubhub.domain.account.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.List;

import com.stubhub.domain.account.biz.impl.MCIRequestUtil;
import com.stubhub.domain.account.common.enums.ListingStatus;
import com.stubhub.domain.account.common.enums.UserType;
import com.stubhub.domain.account.intf.*;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.stubhub.domain.account.common.enums.SalesSubStatus;

public class AccountResponseJsonAdapterTest {
	ObjectMapper om = new ObjectMapper();

	@Test
	public void testConvertToSalesSubStatus() {
		ObjectNode doc = om.createObjectNode();

		doc.put("orderProcSubStatusCode", 2L);

		doc.put("confirmFlowTrackId", "4");

		Assert.assertEquals(AccountResponseJsonAdapter.convertToSalesSubStatus(doc),
				SalesSubStatus.REPLACEMENT_TICKETS_OFFERED);
		doc.put("confirmFlowTrackId", "5");
		Assert.assertEquals(AccountResponseJsonAdapter.convertToSalesSubStatus(doc), SalesSubStatus.DROPPED_SALE);
		doc.put("confirmFlowTrackId", "6");
		Assert.assertEquals(AccountResponseJsonAdapter.convertToSalesSubStatus(doc),SalesSubStatus.SUBS_BY_SYSTEM);

		doc.put("orderProcSubStatusCode", 3L);

		doc.put("subbedInd", "0");
		Assert.assertEquals(AccountResponseJsonAdapter.convertToSalesSubStatus(doc), SalesSubStatus.SUBS_REJECTED);
		doc.put("subbedInd", "1");
		Assert.assertEquals(AccountResponseJsonAdapter.convertToSalesSubStatus(doc), SalesSubStatus.SUBS_OFFERED);
		doc.put("subbedInd", "2");
		Assert.assertNull(AccountResponseJsonAdapter.convertToSalesSubStatus(doc));

		doc.put("orderProcSubStatusCode", 48L);
		Assert.assertEquals(AccountResponseJsonAdapter.convertToSalesSubStatus(doc),
				SalesSubStatus.CHANGE_REQUESTED_DELIVERY_DATE);

		doc.put("orderProcSubStatusCode", 49L);
		Assert.assertEquals(AccountResponseJsonAdapter.convertToSalesSubStatus(doc),
				SalesSubStatus.CHANGE_REQUESTED_DELIVERY_METHOD);

		doc.put("orderProcSubStatusCode", 6L);
		Assert.assertEquals(AccountResponseJsonAdapter.convertToSalesSubStatus(doc),
				SalesSubStatus.GENERATE_SHIPPING_LABEL);
		doc.put("orderProcSubStatusCode", 7L);
		Assert.assertEquals(AccountResponseJsonAdapter.convertToSalesSubStatus(doc),
				SalesSubStatus.GENERATE_SHIPPING_LABEL);

		doc.put("orderProcSubStatusCode", 8L);
		Assert.assertEquals(AccountResponseJsonAdapter.convertToSalesSubStatus(doc),
				SalesSubStatus.REPRINT_SHIPPING_LABEL);
		doc.put("orderProcSubStatusCode", 9L);
		Assert.assertEquals(AccountResponseJsonAdapter.convertToSalesSubStatus(doc),
				SalesSubStatus.REPRINT_SHIPPING_LABEL);
		doc.put("orderProcSubStatusCode", 10L);
		Assert.assertEquals(AccountResponseJsonAdapter.convertToSalesSubStatus(doc),
				SalesSubStatus.REPRINT_SHIPPING_LABEL);

		doc.put("orderProcSubStatusCode", 32L);
		Assert.assertEquals(AccountResponseJsonAdapter.convertToSalesSubStatus(doc), SalesSubStatus.PENDING_UPLOAD_PDF);

		doc.put("orderProcSubStatusCode", 100L);
		Assert.assertNull(AccountResponseJsonAdapter.convertToSalesSubStatus(doc));
	}

	@Test
	public void testConvertToListingStatus() {
		ListingResponse listingResponse = new ListingResponse();
		AccountResponseJsonAdapter.convertToListingStatus(listingResponse, "ACTIVE");
		Assert.assertEquals(listingResponse.getStatus(), ListingStatus.ACTIVE);
		AccountResponseJsonAdapter.convertToListingStatus(listingResponse, "INACTIVE");
		Assert.assertEquals(listingResponse.getStatus(), ListingStatus.INACTIVE);
		AccountResponseJsonAdapter.convertToListingStatus(listingResponse, "INCOMPLETE");
		Assert.assertEquals(listingResponse.getStatus(), ListingStatus.INCOMPLETE);
		AccountResponseJsonAdapter.convertToListingStatus(listingResponse, "DELETED");
		Assert.assertEquals(listingResponse.getStatus(), ListingStatus.DELETED);
		AccountResponseJsonAdapter.convertToListingStatus(listingResponse, "PENDING LOCK");
		Assert.assertEquals(listingResponse.getStatus(), ListingStatus.PENDING);
		AccountResponseJsonAdapter.convertToListingStatus(listingResponse, "PENDING PDF REVIEW");
		Assert.assertEquals(listingResponse.getStatus(), ListingStatus.PENDING);

	}

	@Test
	public void testConvertJsonNodeToSalesResponse() throws Exception {
		SalesResponse salesResponse = AccountResponseJsonAdapter.convertJsonNodeToSalesResponse(null);
		Assert.assertNotNull(salesResponse);
		salesResponse = AccountResponseJsonAdapter.convertJsonNodeToSalesResponse(om.createArrayNode());
		Assert.assertNotNull(salesResponse);
		Assert.assertEquals(salesResponse.getNumFound(), 0L);
		salesResponse = AccountResponseJsonAdapter.convertJsonNodeToSalesResponse(om.createObjectNode());
		Assert.assertNotNull(salesResponse);
		Assert.assertEquals(salesResponse.getNumFound(), 0L);
		ObjectNode createObjectNode = om.createObjectNode();
		ObjectNode respNode = om.createObjectNode();
		createObjectNode.put("response", respNode);
		salesResponse = AccountResponseJsonAdapter.convertJsonNodeToSalesResponse(createObjectNode);
		Assert.assertNotNull(salesResponse);
		Assert.assertEquals(salesResponse.getNumFound(), 0L);
		respNode.put("docs", om.createArrayNode());
		salesResponse = AccountResponseJsonAdapter.convertJsonNodeToSalesResponse(createObjectNode);
		Assert.assertNotNull(salesResponse);
		Assert.assertEquals(salesResponse.getNumFound(), 0L);
		InputStream in = AccountResponseJsonAdapterTest.class.getClassLoader().getResourceAsStream("saleResponse.json");
		salesResponse = AccountResponseJsonAdapter.convertJsonNodeToSalesResponse(om.readTree(in));
		Assert.assertNotNull(salesResponse);
		Assert.assertEquals(salesResponse.getNumFound(), 6237L);
		List<SaleResponse> sales = salesResponse.getSales();
		Assert.assertNotNull(sales);
		Assert.assertEquals(sales.size(), 10);
		SaleResponse firstSale = sales.get(0);
		Assert.assertNotNull(firstSale);
		Assert.assertTrue(firstSale.getInHand());
		Assert.assertEquals(firstSale.getExternalListingId(), "CID1407376222318:131682457");
		Assert.assertEquals(firstSale.getVenueDescription(), "Expo Idaho");
		Assert.assertEquals(firstSale.getSaleDate(), "2014-08-06T19:52:10");
		Assert.assertEquals(firstSale.getDateLastModified(), "2014-08-06T19:52:10");
		Assert.assertEquals(firstSale.getEarliestInhandDate(), "2014-08-05T02:49:17");
		Assert.assertEquals(firstSale.getInhandDate(), "2014-08-06T19:50:22");
		Assert.assertEquals(firstSale.getInhandDatePST(), "2014-08-06T18:50:22");
		Assert.assertEquals(firstSale.getEventTimeZone(), "Mountain Standard Time");
		Assert.assertEquals(firstSale.getSaleId(), "166225627");
		Assert.assertEquals(firstSale.getSellerContactId(), new Long(100000000));

		Assert.assertNotNull(salesResponse.getEventSummary());
		Assert.assertEquals(salesResponse.getEventSummary().size(), 2);
		Summary firstEventSummary = salesResponse.getEventSummary().get(0);
		Assert.assertNotNull(firstEventSummary);
		Assert.assertEquals(firstEventSummary.getId(), Long.valueOf(9093927));
		Assert.assertEquals(firstEventSummary.getCount(), 1465);
		Assert.assertEquals(firstEventSummary.getDescription(), "For Test Wicked Seattle Tickets");
		Summary secondEventSummary = salesResponse.getEventSummary().get(1);
		Assert.assertNotNull(secondEventSummary);
		Assert.assertEquals(secondEventSummary.getId(), Long.valueOf(9101780));
		Assert.assertEquals(secondEventSummary.getCount(), 4772);
		Assert.assertEquals(secondEventSummary.getDescription(), "For testing PDF Seattle Tickets");
		Assert.assertNotNull(salesResponse.getGenreSummary());
		Assert.assertEquals(salesResponse.getGenreSummary().size(), 1);
		Summary firstGenreSummary = salesResponse.getGenreSummary().get(0);
		Assert.assertNotNull(firstGenreSummary);
		Assert.assertEquals(firstGenreSummary.getId(), Long.valueOf(111217));
		Assert.assertEquals(firstGenreSummary.getCount(), 6237);
		Assert.assertEquals(firstGenreSummary.getDescription(), "Wicked Seattle");

		Assert.assertNotNull(salesResponse.getVenueSummary());
		Assert.assertEquals(salesResponse.getVenueSummary().size(), 1);
		Summary firstVenueSummary = salesResponse.getVenueSummary().get(0);
		Assert.assertNotNull(firstVenueSummary);
		Assert.assertEquals(firstVenueSummary.getId(), Long.valueOf(438424));
		Assert.assertEquals(firstVenueSummary.getCount(), 6237);
		Assert.assertEquals(firstVenueSummary.getDescription(), "Expo Idaho");

		in = AccountResponseJsonAdapterTest.class.getClassLoader()
				.getResourceAsStream("saleResponseWithoutSummary.json");
		salesResponse = AccountResponseJsonAdapter.convertJsonNodeToSalesResponse(om.readTree(in));
		Assert.assertNotNull(salesResponse);
		Assert.assertNull(salesResponse.getEventSummary());
		Assert.assertNull(salesResponse.getGenreSummary());
		Assert.assertNull(salesResponse.getVenueSummary());
		in = AccountResponseJsonAdapterTest.class.getClassLoader()
				.getResourceAsStream("saleResponseWithoutSummary2.json");
		salesResponse = AccountResponseJsonAdapter.convertJsonNodeToSalesResponse(om.readTree(in));
		Assert.assertNotNull(salesResponse);
		Assert.assertNull(salesResponse.getEventSummary());
		Assert.assertNull(salesResponse.getGenreSummary());
		Assert.assertNull(salesResponse.getVenueSummary());

	}

	@Test
	public void testConvertJsonNodeToDeliverySpecResponse() throws IOException {
		DeliverySpecificationResponse response = AccountResponseJsonAdapter.convertJsonNodeToDeliverySpecResponse(null);
		Assert.assertNotNull(response);
		InputStream in = AccountResponseJsonAdapterTest.class.getClassLoader()
				.getResourceAsStream("deliverySpecResponse.json");
		response = AccountResponseJsonAdapter.convertJsonNodeToDeliverySpecResponse(om.readTree(in));
		Assert.assertNotNull(response);
		Assert.assertEquals(response.getDeliverySpecifications().size(), 1);
		List<DeliverySpecification> deliverySpecifications = response.getDeliverySpecifications();
		DeliverySpecification ds = deliverySpecifications.get(0);
		Assert.assertEquals(ds.getEventDateUTC(), "2016-05-20T02:30:00+0000");
		Assert.assertEquals(ds.getEventDateLocal(), "2016-05-19T19:30:00Z");
	}

	@Test
	public void testConvertJsonNodeToListingsResponse() throws Exception {
		ListingsResponse listingsResponse = AccountResponseJsonAdapter.convertJsonNodeToListingsResponse(null);
		Assert.assertNotNull(listingsResponse);
		listingsResponse = AccountResponseJsonAdapter.convertJsonNodeToListingsResponse(om.createArrayNode());
		Assert.assertNotNull(listingsResponse);
		Assert.assertEquals(listingsResponse.getNumFound(), 0L);
		listingsResponse = AccountResponseJsonAdapter.convertJsonNodeToListingsResponse(om.createObjectNode());
		Assert.assertNotNull(listingsResponse);
		Assert.assertEquals(listingsResponse.getNumFound(), 0L);
		ObjectNode createObjectNode = om.createObjectNode();
		ObjectNode respNode = om.createObjectNode();
		createObjectNode.put("response", respNode);
		listingsResponse = AccountResponseJsonAdapter.convertJsonNodeToListingsResponse(createObjectNode);
		Assert.assertNotNull(listingsResponse);
		Assert.assertEquals(listingsResponse.getNumFound(), 0L);
		respNode.put("docs", om.createArrayNode());
		listingsResponse = AccountResponseJsonAdapter.convertJsonNodeToListingsResponse(createObjectNode);
		Assert.assertNotNull(listingsResponse);
		Assert.assertEquals(listingsResponse.getNumFound(), 0L);
		InputStream in = AccountResponseJsonAdapterTest.class.getClassLoader()
				.getResourceAsStream("listingResponse.json");
		listingsResponse = AccountResponseJsonAdapter.convertJsonNodeToListingsResponse(om.readTree(in));
		Assert.assertNotNull(listingsResponse);
		Assert.assertEquals(listingsResponse.getNumFound(), 7576L);
		List<ListingResponse> listings = listingsResponse.getListings();
		Assert.assertNotNull(listings);
		Assert.assertEquals(listings.size(), 11);
		ListingResponse firstListing = listings.get(0);
		Assert.assertNotNull(firstListing);
		Assert.assertFalse(firstListing.getAbleToRequestLMS());
		Assert.assertEquals(firstListing.getExternalListingId(), "204978477");
		Assert.assertEquals(firstListing.getVenueDescription(),
				"Veterans United Home Loans Amphitheater at Virginia Beach");
		Assert.assertEquals(firstListing.getSaleEndDate(), "2016-09-07T15:00:00-0400");
		Assert.assertEquals(firstListing.getEarliestInhandDate(), "2016-01-22T16:19:58");
		Assert.assertEquals(firstListing.getInhandDate(), "2016-08-26T20:00:00-0400");
		Assert.assertEquals(firstListing.getEventDate(), "2016-09-09T19:00:00-0400");
		Assert.assertEquals(firstListing.getEventTimeZone(), "EDT");
		Assert.assertEquals(firstListing.getId(), "1180684644");

		Assert.assertNotNull(listingsResponse.getEventSummary());
		Assert.assertEquals(listingsResponse.getEventSummary().size(), 2);
		Summary firstEventSummary = listingsResponse.getEventSummary().get(0);
		Assert.assertNotNull(firstEventSummary);
		Assert.assertEquals(firstEventSummary.getId(), Long.valueOf(9093927));
		Assert.assertEquals(firstEventSummary.getCount(), 1465);
		Assert.assertEquals(firstEventSummary.getDescription(), "For Test Wicked Seattle Tickets");
		Summary secondEventSummary = listingsResponse.getEventSummary().get(1);
		Assert.assertNotNull(secondEventSummary);
		Assert.assertEquals(secondEventSummary.getId(), Long.valueOf(9101780));
		Assert.assertEquals(secondEventSummary.getCount(), 4772);
		Assert.assertEquals(secondEventSummary.getDescription(), "For testing PDF Seattle Tickets");
		Assert.assertNotNull(listingsResponse.getGenreSummary());
		Assert.assertEquals(listingsResponse.getGenreSummary().size(), 1);
		Summary firstGenreSummary = listingsResponse.getGenreSummary().get(0);
		Assert.assertNotNull(firstGenreSummary);
		Assert.assertEquals(firstGenreSummary.getId(), Long.valueOf(111217));
		Assert.assertEquals(firstGenreSummary.getCount(), 6237);
		Assert.assertEquals(firstGenreSummary.getDescription(), "Wicked Seattle");

		Assert.assertNotNull(listingsResponse.getVenueSummary());
		Assert.assertEquals(listingsResponse.getVenueSummary().size(), 1);
		Summary firstVenueSummary = listingsResponse.getVenueSummary().get(0);
		Assert.assertNotNull(firstVenueSummary);
		Assert.assertEquals(firstVenueSummary.getId(), Long.valueOf(438424));
		Assert.assertEquals(firstVenueSummary.getCount(), 6237);
		Assert.assertEquals(firstVenueSummary.getDescription(), "Expo Idaho");

		ListingResponse secondListing = listings.get(1);
		Assert.assertTrue(secondListing.getAbleToRequestLMS());
		in = AccountResponseJsonAdapterTest.class.getClassLoader()
				.getResourceAsStream("listingResponseWithoutSummary.json");
		listingsResponse = AccountResponseJsonAdapter.convertJsonNodeToListingsResponse(om.readTree(in));
		Assert.assertNotNull(listingsResponse);
		Assert.assertNull(listingsResponse.getEventSummary());
		Assert.assertNull(listingsResponse.getGenreSummary());
		Assert.assertNull(listingsResponse.getVenueSummary());

	}

	@Test
	public void testConvertJsonNodeToCSOrderResponse() throws Exception {
		// test null
		OrdersResponse orderResponse = AccountResponseJsonAdapter.convertJsonNodeToOrdersResponse(null, "4",
				UserType.BUYER);
		Assert.assertNotNull(orderResponse);
		// test root not object
		orderResponse = AccountResponseJsonAdapter.convertJsonNodeToOrdersResponse(om.createArrayNode(), "4",
				UserType.BUYER);
		Assert.assertNotNull(orderResponse);
		Assert.assertEquals(orderResponse.getOrdersFound(), 0);
		// test empty
		orderResponse = AccountResponseJsonAdapter.convertJsonNodeToOrdersResponse(om.createObjectNode(), "4",
				UserType.BUYER);
		Assert.assertNotNull(orderResponse);
		Assert.assertEquals(orderResponse.getOrdersFound(), 0);
		// test no docs
		ObjectNode createObjectNode = om.createObjectNode();
		ObjectNode respNode = om.createObjectNode();
		createObjectNode.put("response", respNode);
		orderResponse = AccountResponseJsonAdapter.convertJsonNodeToOrdersResponse(createObjectNode, "4",
				UserType.BUYER);
		Assert.assertNotNull(orderResponse);
		Assert.assertEquals(orderResponse.getOrdersFound(), 0);
		// test empty docs
		ArrayNode docsNode = om.createArrayNode();
		respNode.put("docs", docsNode);
		orderResponse = AccountResponseJsonAdapter.convertJsonNodeToOrdersResponse(createObjectNode, "4",
				UserType.BUYER);
		Assert.assertNotNull(orderResponse);
		Assert.assertEquals(orderResponse.getOrdersFound(), 0);
		// test empty doc
		ObjectNode docNode = om.createObjectNode();
		docsNode.add(docNode);
		respNode.put("numFound", 1);
		orderResponse = AccountResponseJsonAdapter.convertJsonNodeToOrdersResponse(createObjectNode, "4",
				UserType.BUYER);
		Assert.assertNotNull(orderResponse);
		Assert.assertFalse(orderResponse.getOrder().isEmpty());
		// test piggyback
		docNode.put("rowDesc", "1,2");
		docNode.put("seats", "1-1,1-2,2-1,2-2");
		orderResponse = AccountResponseJsonAdapter.convertJsonNodeToOrdersResponse(createObjectNode, "4",
				UserType.BUYER);
		Assert.assertEquals(orderResponse.getOrder().get(0).getTransaction().getSeats(), "1,2,1,2");
		// test input buyer type
		InputStream in = AccountResponseJsonAdapterTest.class.getClassLoader().getResourceAsStream("saleResponse.json");
		JsonNode jsonNode = om.readTree(in);

		orderResponse = AccountResponseJsonAdapter.convertJsonNodeToOrdersResponse(jsonNode, "10", UserType.BUYER);
		Assert.assertNotNull(orderResponse);
		Assert.assertEquals(orderResponse.getOrdersFound(), 6237);
		List<CSOrderDetailsResponse> sales = orderResponse.getOrder();
		Assert.assertNotNull(sales);
		Assert.assertEquals(sales.size(), 10);
		CSOrderDetailsResponse firstSale = sales.get(0);
		Assert.assertNotNull(firstSale);
		Assert.assertFalse(firstSale.getDelivery().getNotInHand());
		Assert.assertEquals(firstSale.getEvent().getVenueDescription(), "Expo Idaho");
		Assert.assertEquals(firstSale.getTransaction().getSaleDateUTC(), "2014-08-07T01:52:10Z");
		Assert.assertEquals(firstSale.getDelivery().getInHandDateUTC(), "2014-08-07T01:50:22Z");
		Assert.assertEquals(firstSale.getTransaction().getOrderId(), "166225627");
		Assert.assertNotNull(firstSale.getSubs());

		// test seller type
		orderResponse = AccountResponseJsonAdapter.convertJsonNodeToOrdersResponse(jsonNode, "10", UserType.SELLER);
		Assert.assertNotNull(orderResponse);
		Assert.assertEquals(orderResponse.getOrdersFound(), 6237);
		sales = orderResponse.getOrder();
		Assert.assertNotNull(sales);
		Assert.assertEquals(sales.size(), 10);
		firstSale = sales.get(0);
		Assert.assertNotNull(firstSale);
		Assert.assertFalse(firstSale.getDelivery().getNotInHand());
		Assert.assertEquals(firstSale.getEvent().getVenueDescription(), "Expo Idaho");
		Assert.assertEquals(firstSale.getTransaction().getSaleDateUTC(), "2014-08-07T01:52:10Z");
		Assert.assertNull(firstSale.getDelivery().getInHandDateUTC());
		Assert.assertEquals(firstSale.getTransaction().getOrderId(), "166225627");
		Assert.assertNotNull(firstSale.getSellerPayments());

	}

	@Test
	public void testConvertJsonNodeToSalesHistoryResponse() throws Exception {
		// test null
		SalesHistoryResponse response = AccountResponseJsonAdapter.convertJsonNodeToSalesHistoryResponse(null,
				"listprice");
		Assert.assertNotNull(response);
		// test empty
		response = AccountResponseJsonAdapter.convertJsonNodeToSalesHistoryResponse(om.createObjectNode(), "listprice");
		Assert.assertNotNull(response);

		// test no docs
		ObjectNode createObjectNode = om.createObjectNode();
		ObjectNode respNode = om.createObjectNode();
		createObjectNode.put("response", respNode);
		response = AccountResponseJsonAdapter.convertJsonNodeToSalesHistoryResponse(createObjectNode, "listprice");
		Assert.assertNotNull(response);

		// test empty docs
		ArrayNode docsNode = om.createArrayNode();
		respNode.put("docs", docsNode);
		response = AccountResponseJsonAdapter.convertJsonNodeToSalesHistoryResponse(createObjectNode, "listprice");
		Assert.assertNotNull(response);

		// test input
		InputStream in = AccountResponseJsonAdapterTest.class.getClassLoader()
				.getResourceAsStream("salesHistoryResponse.json");
		JsonNode jsonNode = om.readTree(in);

		// price per ticket
		response = AccountResponseJsonAdapter.convertJsonNodeToSalesHistoryResponse(jsonNode, "listprice");
		Assert.assertNotNull(response);
		Assert.assertNotNull(response.getAveragePricePerTicket());
		Assert.assertNotNull(response.getMaxPricePerTicket());
		Assert.assertNotNull(response.getMedianPricePerTicket());
		Assert.assertNotNull(response.getMinPricePerTicket());
		// cost per ticket
		response = AccountResponseJsonAdapter.convertJsonNodeToSalesHistoryResponse(jsonNode, "allinprice");
		Assert.assertNotNull(response);
		Assert.assertNotNull(response.getAveragePricePerTicket());
		Assert.assertNotNull(response.getMaxPricePerTicket());
		Assert.assertNotNull(response.getMedianPricePerTicket());
		Assert.assertNotNull(response.getMinPricePerTicket());

		// test input
		in = AccountResponseJsonAdapterTest.class.getClassLoader()
				.getResourceAsStream("salesHistoryResponseEmpty.json");
		jsonNode = om.readTree(in);
		// price per ticket
		response = AccountResponseJsonAdapter.convertJsonNodeToSalesHistoryResponse(jsonNode, "listprice");
		Assert.assertNotNull(response);
		Assert.assertNull(response.getAveragePricePerTicket());
		Assert.assertNull(response.getMaxPricePerTicket());
		Assert.assertNull(response.getMedianPricePerTicket());
		Assert.assertNull(response.getMinPricePerTicket());
		// cost per ticket
		response = AccountResponseJsonAdapter.convertJsonNodeToSalesHistoryResponse(jsonNode, "allinprice");
		Assert.assertNotNull(response);
		Assert.assertNull(response.getAveragePricePerTicket());
		Assert.assertNull(response.getMaxPricePerTicket());
		Assert.assertNull(response.getMedianPricePerTicket());
		Assert.assertNull(response.getMinPricePerTicket());

	}

	@Test
	public void testConvertJsonNodeToMyOrderListEntities() throws Exception {
		MyOrderListResponse ordersResponse = AccountResponseJsonAdapter.convertJsonNodeToMyOrderListEntities(null);
		Assert.assertNotNull(ordersResponse);
		ordersResponse = AccountResponseJsonAdapter.convertJsonNodeToMyOrderListEntities(om.createArrayNode());
		Assert.assertNotNull(ordersResponse);
		Assert.assertEquals(ordersResponse.getNumFound(), 0L);
		ordersResponse = AccountResponseJsonAdapter.convertJsonNodeToMyOrderListEntities(om.createObjectNode());
		Assert.assertNotNull(ordersResponse);
		Assert.assertEquals(ordersResponse.getNumFound(), 0L);
		ObjectNode createObjectNode = om.createObjectNode();
		ObjectNode respNode = om.createObjectNode();
		createObjectNode.put("response", respNode);
		ordersResponse = AccountResponseJsonAdapter.convertJsonNodeToMyOrderListEntities(createObjectNode);
		Assert.assertNotNull(ordersResponse);
		Assert.assertEquals(ordersResponse.getNumFound(), 0L);
		respNode.put("docs", om.createArrayNode());
		ordersResponse = AccountResponseJsonAdapter.convertJsonNodeToMyOrderListEntities(createObjectNode);
		Assert.assertNotNull(ordersResponse);
		Assert.assertEquals(ordersResponse.getNumFound(), 0L);
		InputStream in = AccountResponseJsonAdapterTest.class.getClassLoader()
				.getResourceAsStream("orderResponse.json");
		ordersResponse = AccountResponseJsonAdapter.convertJsonNodeToMyOrderListEntities(om.readTree(in));
		Assert.assertNotNull(ordersResponse);
		Assert.assertEquals(ordersResponse.getNumFound(), 905L);
		List<MyOrderResponse> orders = ordersResponse.getMyOrderList();
		Assert.assertNotNull(orders);
		Assert.assertEquals(orders.size(), 4);
		MyOrderResponse firstOrder = orders.get(0);
		Assert.assertNotNull(firstOrder);
		Assert.assertEquals(firstOrder.getVenueDescription(), "PrivateBank Theatre");
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		sf.setTimeZone(MCIRequestUtil.UTC_TIME_ZONE);
		Assert.assertFalse(firstOrder.getHideEventDate());
		Assert.assertFalse(firstOrder.getHideEventTime());
		Assert.assertEquals(sf.format(firstOrder.getInhandDate().getTime()), "2017-01-12T01:30:00");
		Assert.assertEquals(sf.format(firstOrder.getInhandDate().getTime()), "2017-01-12T01:30:00");
		Assert.assertEquals(sf.format(firstOrder.getEventDate().getTime()), "2017-01-19T01:30:00");
		Assert.assertEquals(sf.format(firstOrder.getEventDateLocal().getTime()), "2017-01-18T19:30:00");
		Assert.assertEquals(sf.format(firstOrder.getExpectedDeliveryDate().getTime()), "2017-01-16T14:00:00");
		Assert.assertEquals(firstOrder.getEventDateTimeZone(), "CST");

		Assert.assertFalse(orders.get(1).getHideEventDate());
		Assert.assertFalse(orders.get(1).getHideEventTime());

		Assert.assertFalse(orders.get(2).getHideEventDate());
		Assert.assertFalse(orders.get(2).getHideEventTime());

		Assert.assertTrue(orders.get(3).getHideEventDate());
		Assert.assertTrue(orders.get(3).getHideEventTime());
	}

	@Test
	public void testConvertJsonNodeToEventSalesData() throws Exception {
		SalesResponse salesResponse = AccountResponseJsonAdapter.convertJsonNodeToEventSalesData(null, "",
				AccountResponseAdapter.PRICE_TYPE_LISTPRICE, "dummySellerId");
		Assert.assertNotNull(salesResponse);
		salesResponse = AccountResponseJsonAdapter.convertJsonNodeToEventSalesData(om.createArrayNode(), "",
				AccountResponseAdapter.PRICE_TYPE_LISTPRICE, "dummySellerId");
		Assert.assertNotNull(salesResponse);
		Assert.assertEquals(salesResponse.getNumFound(), 0L);
		salesResponse = AccountResponseJsonAdapter.convertJsonNodeToEventSalesData(om.createObjectNode(), "",
				AccountResponseAdapter.PRICE_TYPE_LISTPRICE, "dummySellerId");
		Assert.assertNotNull(salesResponse);
		Assert.assertEquals(salesResponse.getNumFound(), 0L);
		ObjectNode createObjectNode = om.createObjectNode();
		ObjectNode respNode = om.createObjectNode();
		createObjectNode.put("response", respNode);
		salesResponse = AccountResponseJsonAdapter.convertJsonNodeToEventSalesData(createObjectNode, "",
				AccountResponseAdapter.PRICE_TYPE_LISTPRICE, "dummySellerId");
		Assert.assertNotNull(salesResponse);
		Assert.assertEquals(salesResponse.getNumFound(), 0L);
		respNode.put("docs", om.createArrayNode());
		salesResponse = AccountResponseJsonAdapter.convertJsonNodeToEventSalesData(createObjectNode, "",
				AccountResponseAdapter.PRICE_TYPE_LISTPRICE, "dummySellerId");
		Assert.assertNotNull(salesResponse);
		Assert.assertEquals(salesResponse.getNumFound(), 0L);
		InputStream in = AccountResponseJsonAdapterTest.class.getClassLoader().getResourceAsStream("saleResponseForEventSale.json");
		salesResponse = AccountResponseJsonAdapter.convertJsonNodeToEventSalesData(om.readTree(in), "",
				AccountResponseAdapter.PRICE_TYPE_LISTPRICE, "dummySellerId");
		Assert.assertNotNull(salesResponse);
		Assert.assertEquals(salesResponse.getNumFound(), 6237L);
		List<SaleResponse> sales = salesResponse.getSales();
		Assert.assertNotNull(sales);
		Assert.assertEquals(sales.size(), 10);
		SaleResponse firstSale = sales.get(0);
		Assert.assertNotNull(firstSale);
		Assert.assertEquals(firstSale.getSectionId().longValue(), 12345L);
		in = AccountResponseJsonAdapterTest.class.getClassLoader().getResourceAsStream("saleResponseForEventSale.json");
		salesResponse = AccountResponseJsonAdapter.convertJsonNodeToEventSalesData(om.readTree(in), "DELIVERYOPTION:PDF",
				AccountResponseAdapter.PRICE_TYPE_ALLINPRICE, "dummySellerId");
		Assert.assertNotNull(salesResponse);
		Assert.assertEquals(salesResponse.getNumFound(), 6237L);
		in = AccountResponseJsonAdapterTest.class.getClassLoader().getResourceAsStream("saleResponseForEventSale.json");
		salesResponse = AccountResponseJsonAdapter.convertJsonNodeToEventSalesData(om.readTree(in), "DELIVERYOPTION:BARCODE",
				AccountResponseAdapter.PRICE_TYPE_ALLINPRICE, "dummySellerId");
		Assert.assertNotNull(salesResponse);
		Assert.assertEquals(salesResponse.getNumFound(), 6237L);
	}

}
