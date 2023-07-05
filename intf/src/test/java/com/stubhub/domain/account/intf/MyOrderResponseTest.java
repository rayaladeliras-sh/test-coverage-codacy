package com.stubhub.domain.account.intf;

import org.testng.annotations.Test;

public class MyOrderResponseTest {
	@Test
	public void test(){
		MyOrderResponse mor = new MyOrderResponse();
		mor.setCity(mor.getCity());
		mor.setCostPerTicket(mor.getCostPerTicket());
		mor.setCountry(mor.getCountry());
		mor.setDeliveryOption(mor.getDeliveryOption());
		mor.setEventDate(mor.getEventDate());
		mor.setEventDateLocal(mor.getEventDateLocal());
		mor.setHideEventDate(mor.getHideEventDate());
		mor.setHideEventTime(mor.getHideEventTime());
		mor.setEventDateTimeZone(mor.getEventDateTimeZone());
		mor.setEventDescription(mor.getEventDescription());
		mor.setEventId(mor.getEventId());
		mor.setExpectedDeliveryDate(mor.getExpectedDeliveryDate());
		mor.setInhandDate(mor.getInhandDate());
		mor.setLmsLocation(mor.getLmsLocation());
		mor.setOrderDate(mor.getOrderDate());
		mor.setOrderId(mor.getOrderId());
		mor.setPricePerTicket(mor.getPricePerTicket());
		mor.setQuantity(mor.getQuantity());
		mor.setSeats(mor.getSeats());
		mor.setState(mor.getState());
		mor.setStatus(mor.getStatus());
		mor.setTicketTraits(mor.getTicketTraits());
		mor.setTotalCost(mor.getTotalCost());
		mor.setTrackingNumber(mor.getTrackingNumber());
		mor.setVenueDescription(mor.getVenueDescription());
		mor.setVenueId(mor.getVenueId());
	}
}
