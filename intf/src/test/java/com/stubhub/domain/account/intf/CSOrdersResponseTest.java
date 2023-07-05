package com.stubhub.domain.account.intf;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.stubhub.newplatform.common.entity.Money;

public class CSOrdersResponseTest {
	private OrdersResponse ordersResponse;

	@BeforeTest
	public void setUp() {
		ordersResponse = new OrdersResponse();
	}

	@Test
	public void testOrdersResponse(){
		List<CSOrderDetailsResponse> list = new ArrayList<CSOrderDetailsResponse>();
		CSOrderDetailsResponse orderDetailResponse = new CSOrderDetailsResponse();

		TransactionResponse transactionResponse = new TransactionResponse();		
		transactionResponse.setOrderId("77777777");
		transactionResponse.setListingId("678954321");
		transactionResponse.setPricePerTicket(new Money("50.00", "USD"));
		transactionResponse.setQuantityPurchased("2");
		transactionResponse.setSection("StubHub Ticket Gift Certificates");
		transactionResponse.setRow("Gift Certificate");
		transactionResponse.setSeats("General Admission");
		transactionResponse.setSellerId("99999999");
		transactionResponse.setSellerConfirmed(true);
		transactionResponse.setCancelled(false);
		transactionResponse.setCsFlagged(true);
		transactionResponse.setSaleDateUTC("2013-11-05T13:15:30.000Z");
		transactionResponse.setTotalCost(new Money("100.00", "USD"));
		transactionResponse.setBuyerId("99999999");
		transactionResponse.setBuyerContactId("9876543");
		transactionResponse.setSellerPayoutAmount(new Money("50.00", "USD"));
		transactionResponse.setSellerPaymentTypeId("2");
		
		EventResponse eventResponse = new EventResponse();		
		eventResponse.setEventId("24275");
		eventResponse.setEventDescription("Nutcracker - War Memorial Theater Tickets");
		eventResponse.setEventStatus("Active");
		eventResponse.setEventDateUTC("2013-11-05T13:15:30.000Z");
		eventResponse.setVenueDescription("Bluebird Theater");
		eventResponse.setEventDateLocal("2013-11-05T13:15:30");

		SubResponse subResponse = new SubResponse();
		subResponse.setSubbedFlag(true);
		subResponse.setSubbedOrderId("55556666");

		SellerPayment sellerPayment = new SellerPayment();
		List<SellerPayment> paymentList = new ArrayList<SellerPayment>();
		SellerPayments sellerPayments = new SellerPayments();
		sellerPayment.setPaymentAmount(new Money("1001.25", "USD"));
		sellerPayment.setStatus("GP Payment Completed");
		sellerPayment.setPaymentDate("2013-11-05T08:15:30.000-05:00");
		sellerPayment.setBookOfBusinessID("2");
		sellerPayment.setEventName("Nutcracker - War Memorial Theater Tickets");
//		sellerPayment.setExpectedPaymentDate("2013-11-05T08:15:30.000-05:00");
		sellerPayment.setID("1234");
		sellerPayment.setOrderID("1234");
		sellerPayment.setPayeeName("xyz");
		sellerPayment.setPaymentAmount(new Money("1001.25", "USD"));
		sellerPayment.setPaymentInitiatedDate("2013-11-05T08:15:30.000-05:00");
		sellerPayment.setPaymentMode("CC");
		sellerPayment.setPaypalEmail("xyz@paypal.com");
		sellerPayment.setReferenceNumber("1234");		
		paymentList.add(sellerPayment);
		sellerPayments.setPayments(paymentList);
		
		DeliveryResponse deliveryResponse = new DeliveryResponse();
		deliveryResponse.setDeliveryMethodId("24");
		deliveryResponse.setDeliveryMethodDescription("UPS 2nd Day Air - Intra-USA");
		deliveryResponse.setShipDateUTC("2013-11-05T13:15:30.000Z");
		deliveryResponse.setDeliveryTypeId("5");
		deliveryResponse.setDeliveryTypeDescription("UPS");
		deliveryResponse.setExpectedArrivalDateUTC("2013-11-05T13:15:30.000Z");
		deliveryResponse.setNotInHand(false);
		deliveryResponse.setTrackingNumber("1Z18V5310152361991");
		deliveryResponse.setOrderProcSubStatusCode("6000");
		deliveryResponse.setOrderProcSubStatusDesc("Delivery Fulfilled");
		deliveryResponse.setInHandDateUTC("2013-11-05T13:15:30.000Z");
		orderDetailResponse.setTransaction(transactionResponse);
		orderDetailResponse.setSellerPayments(sellerPayments);
		orderDetailResponse.setEvent(eventResponse);
		orderDetailResponse.setSubs(subResponse);
		orderDetailResponse.setDelivery(deliveryResponse);
		list.add(orderDetailResponse);
		ordersResponse.setOrder(list);	

		Assert.assertEquals(ordersResponse.getOrder(), list);
		Assert.assertEquals(ordersResponse.getOrder().get(0).getDelivery(), deliveryResponse);
		Assert.assertEquals(ordersResponse.getOrder().get(0).getEvent(), eventResponse);
		Assert.assertEquals(ordersResponse.getOrder().get(0).getSellerPayments().getPayments().get(0), sellerPayment);
		Assert.assertEquals(ordersResponse.getOrder().get(0).getSubs(), subResponse);
		Assert.assertEquals(ordersResponse.getOrder().get(0).getTransaction(), transactionResponse);

		Assert.assertEquals(ordersResponse.getOrder().get(0).getTransaction().getBuyerContactId(), transactionResponse.getBuyerContactId());
		Assert.assertEquals(ordersResponse.getOrder().get(0).getTransaction().getBuyerId(), transactionResponse.getBuyerId());
		Assert.assertEquals(ordersResponse.getOrder().get(0).getTransaction().getListingId(), transactionResponse.getListingId());
		Assert.assertEquals(ordersResponse.getOrder().get(0).getTransaction().getOrderId(), transactionResponse.getOrderId());
		Assert.assertEquals(ordersResponse.getOrder().get(0).getTransaction().getQuantityPurchased(), transactionResponse.getQuantityPurchased());
		Assert.assertEquals(ordersResponse.getOrder().get(0).getTransaction().getRow(), transactionResponse.getRow());
		Assert.assertEquals(ordersResponse.getOrder().get(0).getTransaction().getSaleDateUTC(), transactionResponse.getSaleDateUTC());
		Assert.assertEquals(ordersResponse.getOrder().get(0).getTransaction().getSeats(), transactionResponse.getSeats());
		Assert.assertEquals(ordersResponse.getOrder().get(0).getTransaction().getSection(), transactionResponse.getSection());
		Assert.assertEquals(ordersResponse.getOrder().get(0).getTransaction().getSellerId(), transactionResponse.getSellerId());
		Assert.assertEquals(ordersResponse.getOrder().get(0).getTransaction().getCancelled(), transactionResponse.getCancelled());
		Assert.assertEquals(ordersResponse.getOrder().get(0).getTransaction().getCsFlagged(), transactionResponse.getCsFlagged());
		Assert.assertEquals(ordersResponse.getOrder().get(0).getTransaction().getPricePerTicket(), transactionResponse.getPricePerTicket());
		Assert.assertEquals(ordersResponse.getOrder().get(0).getTransaction().getSellerConfirmed(), transactionResponse.getSellerConfirmed());
		Assert.assertEquals(ordersResponse.getOrder().get(0).getTransaction().getTotalCost(), transactionResponse.getTotalCost());
		Assert.assertEquals(ordersResponse.getOrder().get(0).getTransaction().getSellerPayoutAmount(), transactionResponse.getSellerPayoutAmount());
		Assert.assertEquals(ordersResponse.getOrder().get(0).getTransaction().getSellerPaymentTypeId(), transactionResponse.getSellerPaymentTypeId());

		Assert.assertEquals(ordersResponse.getOrder().get(0).getSubs().getSubbedOrderId(), subResponse.getSubbedOrderId());
		Assert.assertEquals(ordersResponse.getOrder().get(0).getSubs().getSubbedFlag(), subResponse.getSubbedFlag());

		Assert.assertEquals(ordersResponse.getOrder().get(0).getSellerPayments().getPayments().get(0).getPaymentAmount(), sellerPayment.getPaymentAmount());
		Assert.assertEquals(ordersResponse.getOrder().get(0).getSellerPayments().getPayments().get(0).getStatus(), sellerPayment.getStatus());
		Assert.assertEquals(ordersResponse.getOrder().get(0).getSellerPayments().getPayments().get(0).getPaymentDate(), sellerPayment.getPaymentDate());
		Assert.assertEquals(ordersResponse.getOrder().get(0).getSellerPayments().getPayments().get(0).getBookOfBusinessID(), sellerPayment.getBookOfBusinessID());
		Assert.assertEquals(ordersResponse.getOrder().get(0).getSellerPayments().getPayments().get(0).getEventName(), sellerPayment.getEventName());
//		Assert.assertEquals(ordersResponse.getOrder().get(0).getSellerPayments().getPayments().get(0).getExpectedPaymentDate(), sellerPayment.getExpectedPaymentDate());
		Assert.assertEquals(ordersResponse.getOrder().get(0).getSellerPayments().getPayments().get(0).getID(), sellerPayment.getID());
		Assert.assertEquals(ordersResponse.getOrder().get(0).getSellerPayments().getPayments().get(0).getOrderID(), sellerPayment.getOrderID());
		Assert.assertEquals(ordersResponse.getOrder().get(0).getSellerPayments().getPayments().get(0).getPayeeName(), sellerPayment.getPayeeName());
		Assert.assertEquals(ordersResponse.getOrder().get(0).getSellerPayments().getPayments().get(0).getPaymentAmount(), sellerPayment.getPaymentAmount());
		Assert.assertEquals(ordersResponse.getOrder().get(0).getSellerPayments().getPayments().get(0).getPaymentInitiatedDate(), sellerPayment.getPaymentInitiatedDate());
		Assert.assertEquals(ordersResponse.getOrder().get(0).getSellerPayments().getPayments().get(0).getPaymentMode(), sellerPayment.getPaymentMode());
		Assert.assertEquals(ordersResponse.getOrder().get(0).getSellerPayments().getPayments().get(0).getPaypalEmail(), sellerPayment.getPaypalEmail());
		Assert.assertEquals(ordersResponse.getOrder().get(0).getSellerPayments().getPayments().get(0).getReferenceNumber(), sellerPayment.getReferenceNumber());		

		Assert.assertEquals(ordersResponse.getOrder().get(0).getEvent().getEventDateUTC(), eventResponse.getEventDateUTC());
		Assert.assertEquals(ordersResponse.getOrder().get(0).getEvent().getEventDescription(), eventResponse.getEventDescription());
		Assert.assertEquals(ordersResponse.getOrder().get(0).getEvent().getEventId(), eventResponse.getEventId());
		Assert.assertEquals(ordersResponse.getOrder().get(0).getEvent().getEventStatus(), eventResponse.getEventStatus());
		Assert.assertEquals(ordersResponse.getOrder().get(0).getEvent().getVenueDescription(), eventResponse.getVenueDescription());
		Assert.assertEquals(ordersResponse.getOrder().get(0).getEvent().getEventDateLocal(), eventResponse.getEventDateLocal());

		Assert.assertEquals(ordersResponse.getOrder().get(0).getDelivery().getDeliveryMethodDescription(), deliveryResponse.getDeliveryMethodDescription());
		Assert.assertEquals(ordersResponse.getOrder().get(0).getDelivery().getDeliveryMethodId(), deliveryResponse.getDeliveryMethodId());
		Assert.assertEquals(ordersResponse.getOrder().get(0).getDelivery().getDeliveryTypeId(), deliveryResponse.getDeliveryTypeId());
		Assert.assertEquals(ordersResponse.getOrder().get(0).getDelivery().getDeliveryTypeDescription(), deliveryResponse.getDeliveryTypeDescription());
		Assert.assertEquals(ordersResponse.getOrder().get(0).getDelivery().getExpectedArrivalDateUTC(), deliveryResponse.getExpectedArrivalDateUTC());
		Assert.assertEquals(ordersResponse.getOrder().get(0).getDelivery().getInHandDateUTC(), deliveryResponse.getInHandDateUTC());		
		Assert.assertEquals(ordersResponse.getOrder().get(0).getDelivery().getShipDateUTC(), deliveryResponse.getShipDateUTC());
		Assert.assertEquals(ordersResponse.getOrder().get(0).getDelivery().getOrderProcSubStatusCode(), deliveryResponse.getOrderProcSubStatusCode());
		Assert.assertEquals(ordersResponse.getOrder().get(0).getDelivery().getOrderProcSubStatusDesc(), deliveryResponse.getOrderProcSubStatusDesc());
		Assert.assertEquals(ordersResponse.getOrder().get(0).getDelivery().getTrackingNumber(), deliveryResponse.getTrackingNumber());		
		Assert.assertEquals(ordersResponse.getOrder().get(0).getDelivery().getNotInHand(), deliveryResponse.getNotInHand());

	}
}
