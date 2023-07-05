package com.stubhub.domain.account.intf;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PricingRecommendation {

	private Long listingId;

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

	public Long getListingId() {
		return listingId;
	}

	public void setListingId(Long listingId) {
		this.listingId = listingId;
	}

	public Long getUpperboundPrice() {
		return upperboundPrice;
	}

	public void setUpperboundPrice(Long upperboundPrice) {
		this.upperboundPrice = upperboundPrice;
	}

	public Long getLowerboundPrice() {
		return lowerboundPrice;
	}

	public void setLowerboundPrice(Long lowerboundPrice) {
		this.lowerboundPrice = lowerboundPrice;
	}

	public Long getSuggestedPrice() {
		return suggestedPrice;
	}

	public void setSuggestedPrice(Long suggestedPrice) {
		this.suggestedPrice = suggestedPrice;
	}

	public Double getSectionMinPrice() {
		return sectionMinPrice;
	}

	public void setSectionMinPrice(Double sectionMinPrice) {
		this.sectionMinPrice = sectionMinPrice;
	}

	public Double getSectionMaxPrice() {
		return sectionMaxPrice;
	}

	public void setSectionMaxPrice(Double sectionMaxPrice) {
		this.sectionMaxPrice = sectionMaxPrice;
	}

	public Double getZoneMinPrice() {
		return zoneMinPrice;
	}

	public void setZoneMinPrice(Double zoneMinPrice) {
		this.zoneMinPrice = zoneMinPrice;
	}

	public Double getZoneMaxPrice() {
		return zoneMaxPrice;
	}

	public void setZoneMaxPrice(Double zoneMaxPrice) {
		this.zoneMaxPrice = zoneMaxPrice;
	}

	public Double getEventMinPrice() {
		return eventMinPrice;
	}

	public void setEventMinPrice(Double eventMinPrice) {
		this.eventMinPrice = eventMinPrice;
	}

	public Double getEventMaxPrice() {
		return eventMaxPrice;
	}

	public void setEventMaxPrice(Double eventMaxPrice) {
		this.eventMaxPrice = eventMaxPrice;
	}

	public Boolean getIsOverPriced() {
		return isOverPriced;
	}

	public void setIsOverPriced(Boolean isOverPriced) {
		this.isOverPriced = isOverPriced;
	}

	public Boolean getIsUnderPriced() {
		return isUnderPriced;
	}

	public void setIsUnderPriced(Boolean isUnderPriced) {
		this.isUnderPriced = isUnderPriced;
	}
	
}
