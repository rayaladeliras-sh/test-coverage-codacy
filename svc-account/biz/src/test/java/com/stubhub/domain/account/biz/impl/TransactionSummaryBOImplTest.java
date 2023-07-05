package com.stubhub.domain.account.biz.impl;

import static junit.framework.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.stubhub.domain.account.biz.intf.TransactionSummaryBO;
import com.stubhub.domain.account.datamodel.dao.TransactionSummaryDAO;
import com.stubhub.domain.account.datamodel.dao.impl.TransactionSummaryDAOImpl;
import com.stubhub.domain.catalog.read.v3.intf.events.dto.response.Event;

public class TransactionSummaryBOImplTest {
	
	private TransactionSummaryBO transactionSummaryBO;
	
	private TransactionSummaryDAO transactionSummaryDAO;
	
	private EventUtil eventUtil;
	
	private Long userId = 123L;
	
	private Event event;
	
	
	@BeforeTest
	public void setUp() {
		transactionSummaryBO = new TransactionSummaryBOImpl();
		transactionSummaryDAO = Mockito.mock(TransactionSummaryDAOImpl.class);
		eventUtil = Mockito.mock(EventUtil.class);
		ReflectionTestUtils.setField(transactionSummaryBO, "transactionSummaryDAO", transactionSummaryDAO);
		ReflectionTestUtils.setField(transactionSummaryBO, "eventUtil", eventUtil);
		event = new Event();
		event.setId(12345L);
		event.setEventDateLocal("2014-09-09T01:01:00Z");
		event.setEventDateUTC("2016-09-09T01:01:00Z");
		Mockito.when(eventUtil.getEventV3(event.getId().toString(), null)).thenReturn(event);
	}
	
	@Test
	public void testGetTransactionSummaryDetails(){
			Assert.assertNotNull(userId);
			Mockito.when(transactionSummaryDAO.getSummaryDetails(userId, "USD",true)).thenReturn(getSummaryDetails());
			Assert.assertNotNull(transactionSummaryBO.getUserTransactionSummary(userId, "USD",true));
	}

	public List<HashMap<String,String>> getSummaryDetails()
	{
		List<HashMap<String,String>> summaryDetailsList = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> orderStats = new HashMap<String, String>();
		HashMap<String, String> listingStats = new HashMap<String, String>();
		orderStats.put("EARLIEST_EVENT_DATE_UTC", "2016-09-09T01:01:00Z");
		orderStats.put("EARLIEST_EVENT_DATE_LOCAL", "2014-09-09T01:01:00Z");
		orderStats.put("PURCHASE_TOTAL", "12000.00");
		orderStats.put("OPEN_ORDER_PURCHASE_TOTAL", "3000.00");
		orderStats.put("PURCHASE_COUNT", "1000");
		orderStats.put("AVERAGE_ORDER_SIZE", "1200.00");
		orderStats.put("COMPLETED_SALES_COUNT", "24");
		orderStats.put("CANCELLED_SALES_COUNT", "11");
		orderStats.put("UNCONFIRMED_SALES_COUNT", "5");
		orderStats.put("CANCELLED_BUYS_COUNT", "8");
		orderStats.put("COMPLETED_BUYS_COUNT", "6");
		orderStats.put("UNCONFIRMED_BUYS_COUNT", "13");
		orderStats.put("IS_TOP_BUYER", "TRUE");
		orderStats.put("BUYER_FLIP_COUNT", "2");
		orderStats.put("DROP_ORDER_RATE", "0.09");
		orderStats.put("EVENT_ID", event.getId().toString());
		listingStats.put("ACTIVE_USER_LISTING_COUNT", "13");
		listingStats.put("INACTIVE_USER_LISTING_COUNT", "15");
		listingStats.put("PENDING_LOCK_USER_LISTING_COUNT", "7");
		listingStats.put("DELETED_USER_LISTING_COUNT", "12");
		listingStats.put("INCOMPLETE_USER_LISTING_COUNT", "6");
		listingStats.put("PENDING_LMS_APPROVAL_USER_LISTING_COUNT", "4");
		summaryDetailsList.add(orderStats);
		summaryDetailsList.add(listingStats);	
		return summaryDetailsList;
	}

}
