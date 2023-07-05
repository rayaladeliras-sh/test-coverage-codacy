package com.stubhub.domain.account.dto;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import com.stubhub.domain.account.annotation.ResponseField;
import com.stubhub.domain.account.common.enums.DeliveryOption;
import com.stubhub.domain.account.common.enums.OrderStatus;
import com.stubhub.domain.account.common.enums.TicketTrait;
import com.stubhub.domain.account.intf.MyOrderResponse;
import com.stubhub.domain.account.intf.MyOrderResponse.Seat;
import com.stubhub.domain.account.mapper.LocalTimeZoneMapper;
import com.stubhub.domain.account.mapper.DeliveryOptionMapper;
import com.stubhub.domain.account.mapper.OrderStatusMapper;
import com.stubhub.domain.account.mapper.SeatsMapper;
import com.stubhub.domain.account.mapper.ShortTimeZoneMapper;
import com.stubhub.domain.account.mapper.TicketTraitMapper;
import com.stubhub.newplatform.common.entity.Money;

public class MyOrderResponseDTO {

	@ResponseField(fieldName="TID")
	private Long orderId;
	
	@ResponseField(fieldName="QUANTITY")
	private Long quantity;
	
	@ResponseField(fieldName={"PRICE_PER_TICKET","CURRENCY_CODE"})
	private Money pricePerTicket;
	
	@ResponseField(fieldName={"COST_PER_TICKET","CURRENCY_CODE"})
	private Money costPerTicket;
	
	@ResponseField(fieldName={"TOTAL_COST","CURRENCY_CODE"})
	private Money totalCost;
	
	@ResponseField(fieldName="INHAND_DATE")
	private Calendar inhandDate;
	
	@ResponseField(fieldName="EVENT_ID")
	private Long eventId;
	
	@ResponseField(fieldName={"SECTION","ROW_DESC","SEATS"}, mapperClass=SeatsMapper.class)
	private List<Seat> seats = new ArrayList<Seat>();

	@ResponseField(fieldName={"FULFILLMENT_METHOD_ID","DELIVERY_OPTION_ID","TICKET_MEDIUM_ID","LMS_APPROVAL_STATUS_ID"}, mapperClass=DeliveryOptionMapper.class)
	private DeliveryOption deliveryOption;
	
	@ResponseField(fieldName="TRANSACTION_DATE")
	private Calendar orderDate;
	
	@ResponseField(fieldName="ORDER_PROC_STATUS_ID",mapperClass=OrderStatusMapper.class)
	private String status;

	@ResponseField(fieldName="ORDER_PROC_SUB_STATUS_CODE")
	private Long subStatusCode;

	@ResponseField(fieldName="SEAT_TRAITS", mapperClass=TicketTraitMapper.class)
	private Set<TicketTrait> ticketTraits;
	
	@ResponseField(fieldName="EVENT_DATE")
	private Calendar eventDate;
	
	@ResponseField(fieldName="EVENT_DATE_LOCAL")
	private Calendar eventDateLocal;

	@ResponseField(fieldName="HIDE_EVENT_DATE")
	private String hideEventDate;

	@ResponseField(fieldName="HIDE_EVENT_TIME")
	private String hideEventTime;

	@ResponseField(fieldName={"JDK_TIMEZONE","EVENT_DATE"}, mapperClass=ShortTimeZoneMapper.class)
	private String eventDateTimeZone;
	
	@ResponseField(fieldName="EVENT_DESCRIPTION")
	private String eventDescription;
	
	@ResponseField(fieldName="VENUE_ID")
	private Long venueId;
	
	@ResponseField(fieldName="VENUE_DESCRIPTION")
	private String venueDescription;
	
	@ResponseField(fieldName="COUNTRY")
	private String country;
	
	@ResponseField(fieldName="STATE")
	private String state;
	
	@ResponseField(fieldName="CITY")
	private String city;
	
	@ResponseField(fieldName="GENRE_ID")
	private Long genreId;
	
	@ResponseField(fieldName="FEDEX_TRACKING_NUMBER")
	private String trackingNumber;
	
	@ResponseField(fieldName={"EXPECTED_DELIVERY_DATE","JDK_TIMEZONE"}, mapperClass=LocalTimeZoneMapper.class)
	private Calendar expectedDeliveryDate;	
	
	public MyOrderResponse wrap2MyOrderResponse(){
		MyOrderResponse response = new MyOrderResponse();
		response.setCity(city);
		response.setCostPerTicket(costPerTicket);
		response.setCountry(country);
		response.setDeliveryOption(deliveryOption);
		response.setEventDate(eventDate);
		response.setEventDateLocal(eventDateLocal);
		response.setHideEventDate("1".equals(hideEventDate));
		response.setHideEventTime("1".equals(hideEventTime));
		response.setEventDateTimeZone(eventDateTimeZone);
		response.setEventDescription(eventDescription);
		response.setEventId(eventId);
		response.setExpectedDeliveryDate(expectedDeliveryDate);
		response.setInhandDate(inhandDate);		
		response.setOrderDate(orderDate);
		response.setOrderId(orderId);
		response.setPricePerTicket(pricePerTicket);
		response.setQuantity(quantity);
		response.setSeats(seats);
		response.setState(state);
		response.setStatus(status);
		response.setTicketTraits(ticketTraits);
		response.setTotalCost(totalCost);
		response.setTrackingNumber(trackingNumber);
		response.setVenueDescription(venueDescription);
		response.setVenueId(venueId);
		response.setGenreId(genreId);

		boolean allowViewTicket = false;

		if (DeliveryOption.BARCODE == deliveryOption || DeliveryOption.PDF == deliveryOption || DeliveryOption.MOBILE_TICKET == deliveryOption) {
			if (OrderStatus.DELIVERED.name().equals(status) || OrderStatus.FULFILLED.name().equals(status)) {
				allowViewTicket = true;

				// Not allow view ticket in case of
				//	OrderProcSubStatusCodeEnum.AtLMTOfficeWontBeUsed(12L, "at lmt office (wont be used)", OrderProcStatusCodeEnum.Delivered),
				if (subStatusCode == 12L) {
					allowViewTicket = false;
				}
			}
		}

		response.setAllowViewTicket(allowViewTicket);

		return response;
	}
	
	public Long getGenreId() {
		return genreId;
	}

	public void setGenreId(Long genreId) {
		this.genreId = genreId;
	}

	public List<Seat> getSeats() {
		return seats;
	}

	public void setSeats(List<Seat> seats) {
		this.seats = seats;
	}

	
	public Calendar getExpectedDeliveryDate() {
		return expectedDeliveryDate;
	}

	public void setExpectedDeliveryDate(Calendar expectedDeliveryDate) {
		this.expectedDeliveryDate = expectedDeliveryDate;
	}

	public String getTrackingNumber() {
		return trackingNumber;
	}

	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public Money getPricePerTicket() {
		return pricePerTicket;
	}

	public void setPricePerTicket(Money pricePerTicket) {
		this.pricePerTicket = pricePerTicket;
	}

	public Money getCostPerTicket() {
		return costPerTicket;
	}

	public void setCostPerTicket(Money costPerTicket) {
		this.costPerTicket = costPerTicket;
	}

	public Money getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(Money totalCost) {
		this.totalCost = totalCost;
	}

	public Calendar getInhandDate() {
		return inhandDate;
	}

	public void setInhandDate(Calendar inhandDate) {
		this.inhandDate = inhandDate;
	}

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public DeliveryOption getDeliveryOption() {
		return deliveryOption;
	}

	public void setDeliveryOption(DeliveryOption deliveryOption) {
		this.deliveryOption = deliveryOption;
	}

	public Calendar getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Calendar orderDate) {
		this.orderDate = orderDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Set<TicketTrait> getTicketTraits() {
		return ticketTraits;
	}

	public void setTicketTraits(Set<TicketTrait> ticketTraits) {
		this.ticketTraits = ticketTraits;
	}

	public Calendar getEventDate() {
		return eventDate;
	}

	public void setEventDate(Calendar eventDate) {
		this.eventDate = eventDate;
	}

	public Calendar getEventDateLocal() {
		return eventDateLocal;
	}

	public void setEventDateLocal(Calendar eventDateLocal) {
		this.eventDateLocal = eventDateLocal;
	}

	public String getEventDateTimeZone() {
		return eventDateTimeZone;
	}

	public void setEventDateTimeZone(String eventDateTimeZone) {
		this.eventDateTimeZone = eventDateTimeZone;
	}

	public String getHideEventDate() {
		return hideEventDate;
	}

	public void setHideEventDate(String hideEventDate) {
		this.hideEventDate = hideEventDate;
	}

	public String getHideEventTime() {
		return hideEventTime;
	}

	public void setHideEventTime(String hideEventTime) {
		this.hideEventTime = hideEventTime;
	}

	public Long getSubStatusCode() {
		return subStatusCode;
	}

	public void setSubStatusCode(Long subStatusCode) {
		this.subStatusCode = subStatusCode;
	}

	public String getEventDescription() {
		return eventDescription;
	}

	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}

	public Long getVenueId() {
		return venueId;
	}

	public void setVenueId(Long venueId) {
		this.venueId = venueId;
	}

	public String getVenueDescription() {
		return venueDescription;
	}

	public void setVenueDescription(String venueDescription) {
		this.venueDescription = venueDescription;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

}
