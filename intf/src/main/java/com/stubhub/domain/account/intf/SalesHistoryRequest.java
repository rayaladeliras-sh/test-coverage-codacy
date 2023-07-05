package com.stubhub.domain.account.intf;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class SalesHistoryRequest {
	
	@XmlElement(name = "eventId", required = true)
	private String eventId;
	
	@XmlElement(name = "sectionIds", required = false)
	private String sectionIds;
	
	@XmlElement(name = "zoneIds", required = false)
	private String zoneIds;
	
	@XmlElement(name = "deliveryOptions", required = false)
	private String deliveryOptions;
	
	@XmlElement(name = "fromDate", required = false)
	private String fromDate;
	
	@XmlElement(name = "toDate", required = false)
	private String toDate;
	
	@XmlElement(name = "rows", required = false)
	private String rows;
	
	@XmlElement(name = "quantity", required = false)
	private String quantity;

	@XmlElement(name = "priceType", required = false)
	private String priceType;

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getSectionIds() {
		return sectionIds;
	}

	public void setSectionIds(String sectionIds) {
		this.sectionIds = sectionIds;
	}

	public String getZoneIds() {
		return zoneIds;
	}

	public void setZoneIds(String zoneIds) {
		this.zoneIds = zoneIds;
	}

	public String getDeliveryOptions() {
		return deliveryOptions;
	}

	public void setDeliveryOptions(String deliveryOptions) {
		this.deliveryOptions = deliveryOptions;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getRows() {
		return rows;
	}

	public void setRows(String rows) {
		this.rows = rows;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getPriceType() {
		return priceType;
	}

	public void setPriceType(String priceType) {
		this.priceType = priceType;
	}	
}
