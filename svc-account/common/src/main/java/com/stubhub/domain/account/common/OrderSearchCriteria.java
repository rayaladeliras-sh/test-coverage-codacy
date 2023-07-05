package com.stubhub.domain.account.common;

import java.util.Calendar;
import java.util.List;

import com.stubhub.domain.account.common.enums.DeliveryOption;

public class OrderSearchCriteria {
	
	private Long eventId;
	private String[] sectionIds;
	private String[] zoneIds;
	private List<DeliveryOption> deliveryOptions;
	private Calendar soldFromDate;
	private Calendar soldToDate;
	private String[] rows;
	private Long quantity;
	
	public Long getEventId() {
		return eventId;
	}
	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}
	public String[] getSectionIds() {
		return sectionIds;
	}
	public void setSectionIds(String[] sectionIds) {
		this.sectionIds = sectionIds;
	}
	public String[] getZoneIds() {
		return zoneIds;
	}
	public void setZoneIds(String[] zoneIds) {
		this.zoneIds = zoneIds;
	}
	public List<DeliveryOption> getDeliveryOptions() {
		return deliveryOptions;
	}
	public void setDeliveryOptions(List<DeliveryOption> deliveryOptions) {
		this.deliveryOptions = deliveryOptions;
	}
	public Calendar getSoldFromDate() {
		return soldFromDate;
	}
	public void setSoldFromDate(Calendar soldFromDate) {
		this.soldFromDate = soldFromDate;
	}
	public Calendar getSoldToDate() {
		return soldToDate;
	}
	public void setSoldToDate(Calendar soldToDate) {
		this.soldToDate = soldToDate;
	}
	public String[] getRows() {
		return rows;
	}
	public void setRows(String[] rows) {
		this.rows = rows;
	}
	public Long getQuantity() {
		return quantity;
	}
	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}	
}
