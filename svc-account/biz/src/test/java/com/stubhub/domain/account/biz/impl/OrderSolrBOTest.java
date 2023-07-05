package com.stubhub.domain.account.biz.impl;

import com.stubhub.domain.account.datamodel.dao.DeliveriesDAO;
import com.stubhub.domain.account.datamodel.entity.Deliveries;
import com.stubhub.newplatform.persistence.adapter.UTCCalendarToTimestampAdapter;
import org.apache.solr.common.SolrDocument;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderSolrBOTest {
	private OrderSolrBOImpl orderSolrBOImpl;
	private DeliveriesDAO deliveriesDAO;

	@BeforeMethod
	public void setUp(){
		orderSolrBOImpl = new OrderSolrBOImpl(){
			protected String getProperty (String propertyName, String defaultValue) {
				if ("cs.ivr.rejected.at.gate.event.start.time.hours.before".equalsIgnoreCase(propertyName))  {
					return "8";
				}
				if ("cs.ivr.top.buyer.criteria.purchase.total".equalsIgnoreCase(propertyName))  {
					return "10000";
				}
				if ("cs.ivr.top.buyer.criteria.average.purchase.total".equalsIgnoreCase(propertyName))  {
					return "2000";
				}
				if ("cs.ivr.top.buyer.criteria.open.orders.purchase.total".equalsIgnoreCase(propertyName))  {
					return "2000";
				}
				if ("cs.ivr.top.buyer.criteria.default.row".equalsIgnoreCase(propertyName))  {
					return "1000";
				}
				if ("cs.ivr.top.buyer.criteria.purchase.timeframe.in.days".equalsIgnoreCase(propertyName))  {
					return "365";
				}
				return "";
			}
		};
		deliveriesDAO = Mockito.mock(DeliveriesDAO.class);
		ReflectionTestUtils.setField(orderSolrBOImpl, "deliveriesDAO", deliveriesDAO);
	}
	
	@Test
	public void testGetEddByTid(){
		Long orderId = 1l;
		List<Deliveries> list = new ArrayList<Deliveries>();
		Deliveries delivery = new Deliveries();
		delivery.setExpectedArrivalDate(UTCCalendarToTimestampAdapter.getNewUTCCalendarInstanceStatic());
		delivery.setId(orderId);
		delivery.setTid(orderId);
		list.add(delivery);
		Mockito.when(deliveriesDAO.getByTid(orderId)).thenReturn(list);
		Assert.assertNotNull(orderSolrBOImpl.getEddByTid(orderId));
	}

	@Test
	public void testGetEddByTidNullDelivery(){
		Long orderId = 1l;
		List<Deliveries> list = new ArrayList<Deliveries>();
		Mockito.when(deliveriesDAO.getByTid(orderId)).thenReturn(list);
		Assert.assertNull(orderSolrBOImpl.getEddByTid(orderId));
	}

	@Test
	public void testGetEddByTidNullEDD(){
		Long orderId = 1l;
		List<Deliveries> list = new ArrayList<Deliveries>();
		Deliveries delivery = new Deliveries();
		delivery.setId(orderId);
		delivery.setTid(orderId);
		list.add(delivery);
		Mockito.when(deliveriesDAO.getByTid(orderId)).thenReturn(list);
		Assert.assertNull(orderSolrBOImpl.getEddByTid(orderId));}


	private SolrDocument mockOrderSolrDocument(long id, String status, long eventId, float price, String deliveryOptionId, String fulfillmentMethod, String ticketMedium) {
		SolrDocument doc = new SolrDocument();
		doc.setField("SELLER_ID", "1");
		doc.setField("TICKET_ID", id + "");
		doc.setField("TID", id + "");
		doc.setField("TICKET_STATUS", status);
		doc.setField("EXTERNAL_LISTING_ID", "10000");
		doc.setField("SECTION", "Lower");
		doc.setField("ROW_DESC", "1,2");
		doc.setField("SEATS", "1,2,3");
		doc.setField("QUANTITY", "2");
		doc.setField("SOLD_QUANTITY", 2);
		doc.setField("CURRENCY_CODE", "USD");
		doc.setField("VAT_SELL_FEE", price + "");
		doc.setField("SELLER_FEE_VAL", price + "");
		doc.setField("TICKET_COST", price + "");
		doc.setField("TOTAL_COST", price + "");
		doc.setField("CURR_PRICE", price + "");
		doc.setField("PRICE_PER_TICKET", price);
		doc.setField("STATS_COST_PER_TICKET", price);
		doc.setField("DELIVERY_OPTION_ID", deliveryOptionId);
		doc.setField("TICKET_MEDIUM_ID",ticketMedium);
		doc.setField("LMS_APPROVAL_STATUS_ID", "0");
		doc.setField("SELLER_PAYOUT_AMOUNT", 80.00f);		
		doc.setField("EXPECTED_INHAND_DATE", new Date());
		doc.setField("INHAND_DATE", "2012-11-03T12:00:00Z");
		doc.setField("TRANSACTION_DATE", "2012-09-03T12:00:00Z");
		doc.setField("SOLD_DATE", new Date());
		doc.setField("SALE_END_DATE", "2013-11-03T12:00:00Z");
		doc.setField("EVENT_DATE", new Date());
		doc.setField("SELLER_PAYMENT_TYPE_ID", "1");
		List<String> traits = new ArrayList<String>();
		traits.add("959,966,102,203,601,101|Actual 4th row,50 yd line,Parking Pass,Alcohol-free seating,Student Ticket,Aisle|3,3,1,2,2,1|Seller Comments,Seller Comments,Ticket Feature,Listing Disclosure,Listing Disclosure,Ticket Feature");
		doc.setField("SEAT_TRAITS", traits);
		doc.setField("EVENT_DESCRIPTION", "U2");
		doc.setField("EVENT_ID", eventId + "");
		doc.setField("EVENT_DATE_LOCAL", "2014-04-03T12:00:00Z");
		doc.setField("VENUE_DESCRIPTION", "O2 Arena");
		doc.setField("VENUE_CONFIG_SECTIONS_ID", 271756L);
		doc.setField("FULFILLMENT_METHOD_ID", fulfillmentMethod);
		doc.setField("ORDER_PROC_STATUS_ID", 4000L);
		doc.setField("ORDER_PROC_SUB_STATUS_CODE", 50L);
		doc.setField("SALE_CATEGORY","3");
		doc.setField("SELLER_NOTES","TEST");
		doc.setField("CITY","San Francisco");
		doc.setField("COUNTRY","United States");
		return doc;
	}

}
