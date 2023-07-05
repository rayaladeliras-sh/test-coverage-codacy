package com.stubhub.domain.account.common;

import java.util.Calendar;

public class ListingFulfillmentWindow {

	/**
	 * fulfillmentMethodId
	 */
	private Long fulfillmentMethodId;

	/**
	 * deliveryMethodId
	 */
	private Long deliveryMethodId;

	/**
	 * baseCost
	 */
	private Double baseCost;

	/**
	 * startDate
	 */
	private Calendar startDate;

	/**
	 * endDate
	 */
	private Calendar endDate;

	public Long getFulfillmentMethodId() {
		return fulfillmentMethodId;
	}

	public void setFulfillmentMethodId(Long fulfillmentMethodId) {
		this.fulfillmentMethodId = fulfillmentMethodId;
	}

	public Long getDeliveryMethodId() {
		return deliveryMethodId;
	}

	public void setDeliveryMethodId(Long deliveryMethodId) {
		this.deliveryMethodId = deliveryMethodId;
	}

	public Double getBaseCost() {
		return baseCost;
	}

	public void setBaseCost(Double baseCost) {
		this.baseCost = baseCost;
	}

	public Calendar getStartDate() {
		return startDate;
	}

	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}

	public Calendar getEndDate() {
		return endDate;
	}

	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;
	}

}