package com.stubhub.domain.account.intf;

public class SearchEmailCriteria {

	private String fromDate;
	private String toDate;
	private String start;
	private String rows;
	private String subject;
	private String orderId;
	private String buyerOrderId;
	private String isExtended;
	
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
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getRows() {
		return rows;
	}
	public void setRows(String rows) {
		this.rows = rows;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
	public String getIsExtended() {
		return isExtended;
	}
	public void setIsExtended(String isExtended) {
		this.isExtended = isExtended;
	}
	
	public String getBuyerOrderId() {
		return buyerOrderId;
	}
	public void setBuyerOrderId(String buyerOrderId) {
		this.buyerOrderId = buyerOrderId;
	}
	

}
