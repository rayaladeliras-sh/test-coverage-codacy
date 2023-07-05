package com.stubhub.domain.account.helper.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonRootName;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "listingScoreRequest")
@JsonRootName(value = "listingScoreRequest")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ListingScoreRequest {
	private List<Listing> listings;
	private Integer coefficientVersion;
	
	public List<Listing> getListings() {
    	if(CollectionUtils.isEmpty(listings)) {
    		return new ArrayList<Listing>();
    	}
    	return new ArrayList<Listing>(listings);
	}

	public void setListings(List<Listing> listings) {
		if (CollectionUtils.isEmpty(listings)) {
			this.listings = new ArrayList<Listing>();
		} else {
			this.listings = new ArrayList<Listing>(listings);
		}
	}

	public Integer getCoefficientVersion() {
		return coefficientVersion;
	}

	public void setCoefficientVersion(Integer coefficientVersion) {
		this.coefficientVersion = coefficientVersion;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("ListingScoreRequest={").append("listings=").append(listings);
		sb.append("coefficientVersion=").append(coefficientVersion);
		sb.append("}");
		return sb.toString();
	}
	
	public static class Listing {
		private Long listingId;
		private Long eventId;
		private Long sectionId;
		private String row;
		private String seats;
		private List<Integer> deliveryTypeIds;
		private Long venueConfigId;
		private Double price;
		
		public Long getEventId() {
			return eventId;
		}
		public void setEventId(Long eventId) {
			this.eventId = eventId;
		}
		public Long getSectionId() {
			return sectionId;
		}
		public void setSectionId(Long sectionId) {
			this.sectionId = sectionId;
		}
		public String getRow() {
			return row;
		}
		public void setRow(String row) {
			this.row = row;
		}
		public String getSeats() {
			return seats;
		}
		public void setSeats(String seats) {
			this.seats = seats;
		}
		public List<Integer> getDeliveryTypeIds() {
	    	if(CollectionUtils.isEmpty(deliveryTypeIds)) {
	    		return new ArrayList<Integer>();
	    	}
	    	return new ArrayList<Integer>(deliveryTypeIds);
		}
		public void setDeliveryTypeIds(List<Integer> deliveryTypeIds) {
			if (CollectionUtils.isEmpty(deliveryTypeIds)) {
				this.deliveryTypeIds = new ArrayList<Integer>();
			} else {
				this.deliveryTypeIds = new ArrayList<Integer>(deliveryTypeIds);
			}
		}
		public Long getVenueConfigId() {
			return venueConfigId;
		}
		public void setVenueConfigId(Long venueConfigId) {
			this.venueConfigId = venueConfigId;
		}
		public Double getPrice() {
			return price;
		}
		public void setPrice(Double price) {
			this.price = price;
		}
		public Long getListingId() {
			return listingId;
		}
		public void setListingId(Long listingId) {
			this.listingId = listingId;
		}
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("Listing={").append("venueConfigId=").append(venueConfigId);
			sb.append(",listingId=").append(listingId);
			sb.append(",price=").append(price);
			sb.append(",seats=").append(seats);
			sb.append(",row=").append(row);
			sb.append(",sectionId=").append(sectionId);
			sb.append(",eventId=").append(eventId);
			sb.append(",deliveryTypeIds=").append(StringUtils.join(deliveryTypeIds, ','));
			sb.append("}");
			return sb.toString();
		}
	}
}
