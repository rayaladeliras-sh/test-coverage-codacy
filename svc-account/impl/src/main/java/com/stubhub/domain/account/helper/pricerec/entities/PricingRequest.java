package com.stubhub.domain.account.helper.pricerec.entities;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.collections.CollectionUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonRootName;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "pricingRequest")
@JsonRootName(value = "pricingRequest")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PricingRequest {
	private List<Long> listingIds;
	private List<Event> events;

	public List<Event> getEvents() { return this.events; }



	public void setEvents(List<Event> events) { this.events = events; }



	public List<Long> getListingIds() { return this.listingIds; }



	public void setListingIds(List<Long> listingIds) { this.listingIds = listingIds; }


	public static class Event
	{
		private Integer eventId;
		private Integer sectionId;
		private String row;
		private String seats;
		private String deliveryTypeIds;

		public Integer getEventId() { return this.eventId; }



		public void setEventId(Integer eventId) { this.eventId = eventId; }



		public Integer getSectionId() { return this.sectionId; }



		public void setSectionId(Integer sectionId) { this.sectionId = sectionId; }



		public String getRow() { return this.row; }



		public void setRow(String row) { this.row = row; }



		public String getSeats() { return this.seats; }



		public void setSeats(String seats) { this.seats = seats; }



		public String getDeliveryTypeIds() { return this.deliveryTypeIds; }



		public void setDeliveryTypeIds(String deliveryTypeIds) { this.deliveryTypeIds = deliveryTypeIds; }
	}
}
