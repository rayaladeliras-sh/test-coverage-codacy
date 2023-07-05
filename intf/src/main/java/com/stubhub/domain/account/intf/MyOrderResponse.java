package com.stubhub.domain.account.intf;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.map.annotate.JsonRootName;

import com.stubhub.domain.account.common.enums.DeliveryOption;
import com.stubhub.domain.account.common.enums.TicketTrait;
import com.stubhub.newplatform.common.entity.Money;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "order")
@JsonRootName("order")
@XmlType(name = "")
public class MyOrderResponse implements TranslatableEventData{
	private Long orderId;
	
	private Long quantity;
	
	private Money pricePerTicket;
	
	private Money costPerTicket;
	
	private Money totalCost;
	
	private Calendar inhandDate;

	private Long eventId;
	
	private List<Seat> seats = new ArrayList<Seat>();

	private DeliveryOption deliveryOption;
	
	private Calendar orderDate;
	
	private String status;

	private boolean allowViewTicket;

	private Set<TicketTrait> ticketTraits;
	
	private Calendar eventDate;
	
	private Calendar eventDateLocal;

	private Boolean hideEventDate;

	private Boolean hideEventTime;

	private String eventDateTimeZone;
	
	private String eventDescription;
	
	private Long venueId;
	
	private String venueDescription;
	
	private String country;
	
	private String state;
	
	private String city;
	
	private String trackingNumber;
	
	private Calendar expectedDeliveryDate;

	private String lmsLocation;
	
	private Long genreId;
	
    private Integer stubhubMobileTicket = 0;

	private Long venueConfigId;
	
	public Long getGenreId() {
		return genreId;
	}

	public void setGenreId(Long genreId) {
		this.genreId = genreId;
	}

	public static class Seat {
		public String primarySection;
		public String row; 
        public String seatNumber;
        public Boolean GA = false;

		public Seat() {
		}

		public Seat(String seatNumber, String primarySection, String row,
				Boolean GA) {
        	this(seatNumber,primarySection,row);
			this.GA = GA;
		}

		public Seat(String seatNumber, String primarySection, String row) {
			super();
			this.seatNumber = seatNumber;
			this.primarySection = primarySection;
			this.row = row;
		}



		@Override
        public String toString() {
            return "Seat{" +                    
                    ", seatNumber='" + seatNumber + '\'' +
                    ", primarySection='" + primarySection + '\'' +
                    ", row='" + row + "\'}";
        }
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

	public String getLmsLocation() {
		return lmsLocation;
	}

	public void setLmsLocation(String lmsLocation) {
		this.lmsLocation = lmsLocation;
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

	public boolean isAllowViewTicket() {
		return allowViewTicket;
	}

	public void setAllowViewTicket(boolean allowViewTicket) {
		this.allowViewTicket = allowViewTicket;
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

	public Boolean getHideEventDate() {
		return hideEventDate;
	}

	public void setHideEventDate(Boolean hideEventDate) {
		this.hideEventDate = hideEventDate;
	}

	public Boolean getHideEventTime() {
		return hideEventTime;
	}

	public void setHideEventTime(Boolean hideEventTime) {
		this.hideEventTime = hideEventTime;
	}

	public String getEventDateTimeZone() {
		return eventDateTimeZone;
	}

	public void setEventDateTimeZone(String eventDateTimeZone) {
		this.eventDateTimeZone = eventDateTimeZone;
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

	public Integer getStubhubMobileTicket() {
		return stubhubMobileTicket;
	}

	public void setStubhubMobileTicket(Integer stubhubMobileTicket) {
		this.stubhubMobileTicket = stubhubMobileTicket;
	}

	@Override
	public Long getVenueConfigId() {
		return venueConfigId;
	}

	@Override
	public void setVenueConfigId(Long venueConfigId) {
		this.venueConfigId = venueConfigId;
	}
}
