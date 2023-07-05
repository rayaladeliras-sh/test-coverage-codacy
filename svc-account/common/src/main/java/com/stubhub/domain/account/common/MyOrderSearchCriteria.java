package com.stubhub.domain.account.common;



public class MyOrderSearchCriteria {	
	private int start=0;
	
	private int row = PaginationInput.DEFAULT_ENTRIES_PERPAGE;
	
	private String[] orderBy;
	
	private String orderDate;
	
	private String locale;
	
	private String buyerId;
	
	private String eventId;
	
	private String status;	
	
	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public String getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}

	public String[] getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String[] orderBy) {
		this.orderBy = orderBy;
	}
}
