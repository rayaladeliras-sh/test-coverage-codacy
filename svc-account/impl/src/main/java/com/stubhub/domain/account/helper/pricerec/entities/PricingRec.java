package com.stubhub.domain.account.helper.pricerec.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@XmlAccessorType(XmlAccessType.FIELD)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PricingRec implements SerializedRec {
	private Long listingId;
	private Integer eventId;
	private Integer sectionId;
	private String row;
	private String seats;
	private Double currentPrice;
	private Long upperboundPrice;
	private Long lowerboundPrice;
	private Long suggestedPrice;
	private Double sectionMinPrice;
	private Double sectionMaxPrice;
	private Double zoneMinPrice;
	private Double zoneMaxPrice;
	private Double eventMinPrice;
	private Double eventMaxPrice;
	private Boolean isOverPriced;
	private Boolean isUnderPriced;

	public Long getListingId() { return this.listingId; }



	public void setListingId(Long listingId) { this.listingId = listingId; }



	public Integer getEventId() { return this.eventId; }



	public void setEventId(Integer eventId) { this.eventId = eventId; }



	public Integer getSectionId() { return this.sectionId; }



	public void setSectionId(Integer sectionId) { this.sectionId = sectionId; }



	public String getRow() { return this.row; }



	public void setRow(String row) { this.row = row; }



	public String getSeats() { return this.seats; }



	public Double getCurrentPrice() { return this.currentPrice; }



	public void setCurrentPrice(Double currentPrice) { this.currentPrice = currentPrice; }



	public void setSeats(String seats) { this.seats = seats; }



	public Long getUpperboundPrice() { return this.upperboundPrice; }



	public void setUpperboundPrice(Long upperboundPrice) { this.upperboundPrice = upperboundPrice; }



	public Long getLowerboundPrice() { return this.lowerboundPrice; }



	public void setLowerboundPrice(Long lowerboundPrice) { this.lowerboundPrice = lowerboundPrice; }



	public Long getSuggestedPrice() { return this.suggestedPrice; }



	public void setSuggestedPrice(Long suggestedPrice) { this.suggestedPrice = suggestedPrice; }



	public Double getSectionMinPrice() { return this.sectionMinPrice; }



	public void setSectionMinPrice(Double sectionMinPrice) { this.sectionMinPrice = sectionMinPrice; }



	public Double getSectionMaxPrice() { return this.sectionMaxPrice; }



	public void setSectionMaxPrice(Double sectionMaxPrice) { this.sectionMaxPrice = sectionMaxPrice; }



	public Double getZoneMinPrice() { return this.zoneMinPrice; }



	public void setZoneMinPrice(Double zoneMinPrice) { this.zoneMinPrice = zoneMinPrice; }



	public Double getZoneMaxPrice() { return this.zoneMaxPrice; }



	public void setZoneMaxPrice(Double zoneMaxPrice) { this.zoneMaxPrice = zoneMaxPrice; }



	public Double getEventMinPrice() { return this.eventMinPrice; }



	public void setEventMinPrice(Double eventMinPrice) { this.eventMinPrice = eventMinPrice; }



	public Double getEventMaxPrice() { return this.eventMaxPrice; }



	public void setEventMaxPrice(Double eventMaxPrice) { this.eventMaxPrice = eventMaxPrice; }



	public Boolean getIsOverPriced() { return this.isOverPriced; }



	public void setIsOverPriced(Boolean isOverPriced) { this.isOverPriced = isOverPriced; }



	public Boolean getIsUnderPriced() { return this.isUnderPriced; }



	public void setIsUnderPriced(Boolean isUnderPriced) { this.isUnderPriced = isUnderPriced; }
}
