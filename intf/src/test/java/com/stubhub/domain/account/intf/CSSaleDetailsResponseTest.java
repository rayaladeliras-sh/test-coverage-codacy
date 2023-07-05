package com.stubhub.domain.account.intf;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.stubhub.newplatform.common.entity.Money;

public class CSSaleDetailsResponseTest {
	private CSSalesResponse csSalesResponse;
	private CSSaleDetailsResponse csSaleDetailsResponse;

	@BeforeTest
	public void setUp() {
		csSaleDetailsResponse = new CSSaleDetailsResponse();
		csSalesResponse = new CSSalesResponse();
	}

	@Test
	public void testSaleDetailsResponse(){
		
		List<CSSaleDetailsResponse> list = new ArrayList<CSSaleDetailsResponse>();
		
		BigDecimal amount = new BigDecimal("1");
		String currency = "USD";
		com.stubhub.newplatform.common.entity.Money money = new com.stubhub.newplatform.common.entity.Money();
        money.setAmount(amount);
        money.setCurrency(currency);
		
		CSSaleTransactionResponse transactionResponse = new CSSaleTransactionResponse();		
		transactionResponse.setSaleId("77777777");
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
		
		transactionResponse.setBuyVAT(money);
		transactionResponse.setSellVAT(money);
		transactionResponse.setShippingFeeCost(money);
		transactionResponse.setDiscountCost(money);
		
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
		csSaleDetailsResponse.setTransaction(transactionResponse);
		csSaleDetailsResponse.setSellerPayments(sellerPayments);
		csSaleDetailsResponse.setEvent(eventResponse);
		csSaleDetailsResponse.setSubs(subResponse);
		csSaleDetailsResponse.setDelivery(deliveryResponse);
		list.add(csSaleDetailsResponse);
		
		csSalesResponse.setSale(list);
		csSalesResponse.setSalesFound(list.size());

		Assert.assertEquals(csSalesResponse.getSale(), list);
		Assert.assertEquals(csSalesResponse.getSale().get(0).getDelivery(), deliveryResponse);
		Assert.assertEquals(csSalesResponse.getSale().get(0).getEvent(), eventResponse);
		Assert.assertEquals(csSalesResponse.getSale().get(0).getSellerPayments().getPayments().get(0), sellerPayment);
		Assert.assertEquals(csSalesResponse.getSale().get(0).getSubs(), subResponse);
		Assert.assertEquals(csSalesResponse.getSale().get(0).getTransaction(), transactionResponse);

		Assert.assertEquals(csSalesResponse.getSale().get(0).getTransaction().getBuyerContactId(), transactionResponse.getBuyerContactId());
		Assert.assertEquals(csSalesResponse.getSale().get(0).getTransaction().getBuyerId(), transactionResponse.getBuyerId());
		Assert.assertEquals(csSalesResponse.getSale().get(0).getTransaction().getListingId(), transactionResponse.getListingId());
		Assert.assertEquals(csSalesResponse.getSale().get(0).getTransaction().getSaleId(), transactionResponse.getSaleId());
		Assert.assertEquals(csSalesResponse.getSale().get(0).getTransaction().getQuantityPurchased(), transactionResponse.getQuantityPurchased());
		Assert.assertEquals(csSalesResponse.getSale().get(0).getTransaction().getRow(), transactionResponse.getRow());
		Assert.assertEquals(csSalesResponse.getSale().get(0).getTransaction().getSaleDateUTC(), transactionResponse.getSaleDateUTC());
		Assert.assertEquals(csSalesResponse.getSale().get(0).getTransaction().getSeats(), transactionResponse.getSeats());
		Assert.assertEquals(csSalesResponse.getSale().get(0).getTransaction().getSection(), transactionResponse.getSection());
		Assert.assertEquals(csSalesResponse.getSale().get(0).getTransaction().getSellerId(), transactionResponse.getSellerId());
		Assert.assertEquals(csSalesResponse.getSale().get(0).getTransaction().getCancelled(), transactionResponse.getCancelled());
		Assert.assertEquals(csSalesResponse.getSale().get(0).getTransaction().getCsFlagged(), transactionResponse.getCsFlagged());
		Assert.assertEquals(csSalesResponse.getSale().get(0).getTransaction().getPricePerTicket(), transactionResponse.getPricePerTicket());
		Assert.assertEquals(csSalesResponse.getSale().get(0).getTransaction().getSellerConfirmed(), transactionResponse.getSellerConfirmed());
		Assert.assertEquals(csSalesResponse.getSale().get(0).getTransaction().getTotalCost(), transactionResponse.getTotalCost());
		Assert.assertEquals(csSalesResponse.getSale().get(0).getTransaction().getSellerPayoutAmount(), transactionResponse.getSellerPayoutAmount());
		Assert.assertEquals(csSalesResponse.getSale().get(0).getTransaction().getSellerPaymentTypeId(), transactionResponse.getSellerPaymentTypeId());

		Assert.assertEquals(csSalesResponse.getSale().get(0).getTransaction().getBuyVAT(), transactionResponse.getBuyVAT());
		Assert.assertEquals(csSalesResponse.getSale().get(0).getTransaction().getSellVAT(), transactionResponse.getSellVAT());
		Assert.assertEquals(csSalesResponse.getSale().get(0).getTransaction().getShippingFeeCost(), transactionResponse.getShippingFeeCost());
		Assert.assertEquals(csSalesResponse.getSale().get(0).getTransaction().getDiscountCost(), transactionResponse.getDiscountCost());
		
		Assert.assertEquals(csSalesResponse.getSale().get(0).getSubs().getSubbedOrderId(), subResponse.getSubbedOrderId());
		Assert.assertEquals(csSalesResponse.getSale().get(0).getSubs().getSubbedFlag(), subResponse.getSubbedFlag());

		Assert.assertEquals(csSalesResponse.getSale().get(0).getSellerPayments().getPayments().get(0).getPaymentAmount(), sellerPayment.getPaymentAmount());
		Assert.assertEquals(csSalesResponse.getSale().get(0).getSellerPayments().getPayments().get(0).getStatus(), sellerPayment.getStatus());
		Assert.assertEquals(csSalesResponse.getSale().get(0).getSellerPayments().getPayments().get(0).getPaymentDate(), sellerPayment.getPaymentDate());
		Assert.assertEquals(csSalesResponse.getSale().get(0).getSellerPayments().getPayments().get(0).getBookOfBusinessID(), sellerPayment.getBookOfBusinessID());
		Assert.assertEquals(csSalesResponse.getSale().get(0).getSellerPayments().getPayments().get(0).getEventName(), sellerPayment.getEventName());
//		Assert.assertEquals(csSalesResponse.getSale().get(0).getSellerPayments().getPayments().get(0).getExpectedPaymentDate(), sellerPayment.getExpectedPaymentDate());
		Assert.assertEquals(csSalesResponse.getSale().get(0).getSellerPayments().getPayments().get(0).getID(), sellerPayment.getID());
		Assert.assertEquals(csSalesResponse.getSale().get(0).getSellerPayments().getPayments().get(0).getOrderID(), sellerPayment.getOrderID());
		Assert.assertEquals(csSalesResponse.getSale().get(0).getSellerPayments().getPayments().get(0).getPayeeName(), sellerPayment.getPayeeName());
		Assert.assertEquals(csSalesResponse.getSale().get(0).getSellerPayments().getPayments().get(0).getPaymentAmount(), sellerPayment.getPaymentAmount());
		Assert.assertEquals(csSalesResponse.getSale().get(0).getSellerPayments().getPayments().get(0).getPaymentInitiatedDate(), sellerPayment.getPaymentInitiatedDate());
		Assert.assertEquals(csSalesResponse.getSale().get(0).getSellerPayments().getPayments().get(0).getPaymentMode(), sellerPayment.getPaymentMode());
		Assert.assertEquals(csSalesResponse.getSale().get(0).getSellerPayments().getPayments().get(0).getPaypalEmail(), sellerPayment.getPaypalEmail());
		Assert.assertEquals(csSalesResponse.getSale().get(0).getSellerPayments().getPayments().get(0).getReferenceNumber(), sellerPayment.getReferenceNumber());		

		Assert.assertEquals(csSalesResponse.getSale().get(0).getEvent().getEventDateUTC(), eventResponse.getEventDateUTC());
		Assert.assertEquals(csSalesResponse.getSale().get(0).getEvent().getEventDescription(), eventResponse.getEventDescription());
		Assert.assertEquals(csSalesResponse.getSale().get(0).getEvent().getEventId(), eventResponse.getEventId());
		Assert.assertEquals(csSalesResponse.getSale().get(0).getEvent().getEventStatus(), eventResponse.getEventStatus());
		Assert.assertEquals(csSalesResponse.getSale().get(0).getEvent().getVenueDescription(), eventResponse.getVenueDescription());
		Assert.assertEquals(csSalesResponse.getSale().get(0).getEvent().getEventDateLocal(), eventResponse.getEventDateLocal());

		Assert.assertEquals(csSalesResponse.getSale().get(0).getDelivery().getDeliveryMethodDescription(), deliveryResponse.getDeliveryMethodDescription());
		Assert.assertEquals(csSalesResponse.getSale().get(0).getDelivery().getDeliveryMethodId(), deliveryResponse.getDeliveryMethodId());
		Assert.assertEquals(csSalesResponse.getSale().get(0).getDelivery().getDeliveryTypeId(), deliveryResponse.getDeliveryTypeId());
		Assert.assertEquals(csSalesResponse.getSale().get(0).getDelivery().getDeliveryTypeDescription(), deliveryResponse.getDeliveryTypeDescription());
		Assert.assertEquals(csSalesResponse.getSale().get(0).getDelivery().getExpectedArrivalDateUTC(), deliveryResponse.getExpectedArrivalDateUTC());
		Assert.assertEquals(csSalesResponse.getSale().get(0).getDelivery().getInHandDateUTC(), deliveryResponse.getInHandDateUTC());		
		Assert.assertEquals(csSalesResponse.getSale().get(0).getDelivery().getShipDateUTC(), deliveryResponse.getShipDateUTC());
		Assert.assertEquals(csSalesResponse.getSale().get(0).getDelivery().getOrderProcSubStatusCode(), deliveryResponse.getOrderProcSubStatusCode());
		Assert.assertEquals(csSalesResponse.getSale().get(0).getDelivery().getOrderProcSubStatusDesc(), deliveryResponse.getOrderProcSubStatusDesc());
		Assert.assertEquals(csSalesResponse.getSale().get(0).getDelivery().getTrackingNumber(), deliveryResponse.getTrackingNumber());		
		Assert.assertEquals(csSalesResponse.getSale().get(0).getDelivery().getNotInHand(), deliveryResponse.getNotInHand());
		
		Assert.assertEquals(csSalesResponse.getSalesFound(),1);

	}
}
