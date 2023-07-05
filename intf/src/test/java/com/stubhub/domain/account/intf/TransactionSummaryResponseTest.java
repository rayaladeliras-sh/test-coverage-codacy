package com.stubhub.domain.account.intf;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.stubhub.newplatform.common.entity.Money;

public class TransactionSummaryResponseTest {
	private TransactionSummaryResponse summaryResponse;

	@BeforeTest
	public void setUp() {
		summaryResponse = new TransactionSummaryResponse();
	}

	@Test
	public void testOrdersResponse(){
		TransactionSummaryResponse summaryResponse = new TransactionSummaryResponse();
		BuysCountResponse buyResponse = new BuysCountResponse();
		SalesCountResponse salesResponse = new SalesCountResponse();
		ListingCountResponse listingsResponse = new ListingCountResponse();
		buyResponse.setCancelledOrders("0");
		buyResponse.setCompletedOrders("5");
		buyResponse.setEarliestEventDateLocal("2013-11-05T08:15:30.000-05:00");
		buyResponse.setEarliestEventDateUTC("2013-11-05T13:15:30.000Z");
		buyResponse.setPurchaseCount("7");
		buyResponse.setPurchaseTotal(new Money("2014.77", "USD"));
		buyResponse.setOpenOrderPurchaseTotal(new Money("99", "USD"));
		buyResponse.setAvgOrderSize(new Money("99", "USD"));
		buyResponse.setUnconfirmedOrders("2");

		salesResponse.setCancelledSales("1");
		salesResponse.setCompletedSales("2");
		salesResponse.setSalesTotalPayment(new Money("899.23", "USD"));
		salesResponse.setUnconfirmedSales("1");

		listingsResponse.setActiveListings("4");
		listingsResponse.setDeletedListings("1");
		listingsResponse.setInactiveListings("1");
		listingsResponse.setIncompleteListings("2");
		listingsResponse.setPendingLMSActivation("1");
		listingsResponse.setPendingLock("1");

		summaryResponse.setIsTopBuyer("true");
		summaryResponse.setUserId("1234567");
		summaryResponse.setBuyerFlip("2");
		summaryResponse.setListings(listingsResponse);
		summaryResponse.setOrders(buyResponse);
		summaryResponse.setSales(salesResponse);

		Assert.assertEquals(summaryResponse.getIsTopBuyer(), "true");
		Assert.assertEquals(summaryResponse.getUserId(), "1234567");
		Assert.assertEquals(summaryResponse.getBuyerFlip(), "2");
		Assert.assertEquals(summaryResponse.getListings(), listingsResponse);
		Assert.assertEquals(summaryResponse.getOrders(), buyResponse);
		Assert.assertEquals(summaryResponse.getSales(), salesResponse);

		Assert.assertEquals(summaryResponse.getListings().getActiveListings(), listingsResponse.getActiveListings());
		Assert.assertEquals(summaryResponse.getListings().getDeletedListings(), listingsResponse.getDeletedListings());
		Assert.assertEquals(summaryResponse.getListings().getInactiveListings(), listingsResponse.getInactiveListings());
		Assert.assertEquals(summaryResponse.getListings().getIncompleteListings(), listingsResponse.getIncompleteListings());
		Assert.assertEquals(summaryResponse.getListings().getPendingLMSActivation(), listingsResponse.getPendingLMSActivation());
		Assert.assertEquals(summaryResponse.getListings().getPendingLock(), listingsResponse.getPendingLock());

		Assert.assertEquals(summaryResponse.getOrders().getCancelledOrders(), buyResponse.getCancelledOrders());
		Assert.assertEquals(summaryResponse.getOrders().getCompletedOrders(), buyResponse.getCompletedOrders());
		Assert.assertEquals(summaryResponse.getOrders().getEarliestEventDateLocal(), buyResponse.getEarliestEventDateLocal());
		Assert.assertEquals(summaryResponse.getOrders().getEarliestEventDateUTC(), buyResponse.getEarliestEventDateUTC());
		Assert.assertEquals(summaryResponse.getOrders().getPurchaseCount(), buyResponse.getPurchaseCount());
		Assert.assertEquals(summaryResponse.getOrders().getPurchaseTotal(), buyResponse.getPurchaseTotal());
		Assert.assertEquals(summaryResponse.getOrders().getOpenOrderPurchaseTotal(), buyResponse.getOpenOrderPurchaseTotal());
		Assert.assertEquals(summaryResponse.getOrders().getAvgOrderSize(), buyResponse.getAvgOrderSize());
		Assert.assertEquals(summaryResponse.getOrders().getUnconfirmedOrders(), buyResponse.getUnconfirmedOrders());

		Assert.assertEquals(summaryResponse.getSales().getCancelledSales(), salesResponse.getCancelledSales());
		Assert.assertEquals(summaryResponse.getSales().getCompletedSales(), salesResponse.getCompletedSales());
		Assert.assertEquals(summaryResponse.getSales().getUnconfirmedSales(), salesResponse.getUnconfirmedSales());
		Assert.assertEquals(summaryResponse.getSales().getSalesTotalPayment(), salesResponse.getSalesTotalPayment());

	}
}
