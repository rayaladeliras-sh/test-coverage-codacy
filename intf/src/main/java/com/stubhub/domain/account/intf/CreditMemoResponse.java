package com.stubhub.domain.account.intf;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.stubhub.domain.account.common.Response;
import com.stubhub.domain.account.datamodel.entity.SellerPaymentStatusEnum;
import com.stubhub.newplatform.common.entity.Money;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "creditMemo")
@XmlType(name = "", propOrder = {
		"id",
		"orderId",
		"sellerId",
		"referenceNumber",
		"creditAmount",		
		"createdDate",
		"appliedDate",
		"status",
		"eventId",
		"eventDate",
		"orderStatus",
		"reason",
		"reasonCode",
		"bookOfBusinessId",
		"eventName"
		})

public final class CreditMemoResponse extends Response {
	
	@XmlElement(name = "id", required = false)
	private String id;
	
	@XmlElement(name = "orderId", required = false)
	private String orderId;	
	@XmlElement(name = "sellerId", required = false)
	private String sellerId;	
	
	@XmlElement(name = "referenceNumber", required = false)
	private String referenceNumber;	
	
	@XmlElement(name = "creditAmount", required = false)
	private Money creditAmount;
	
	// chargedDate format is "YYYY-MM-DDTHH24:MI:SS"
	@XmlElement(name = "createdDate", required = false)
	private String createdDate;
	
	// appliedDate format is "YYYY-MM-DDTHH24:MI:SS"
	@XmlElement(name = "appliedDate", required = false)
	private String appliedDate;	
	
	@XmlElement(name = "status", required = false)
	private SellerPaymentStatusEnum status;
	
	@XmlElement(name = "eventId", required = false)
	private String eventId;	
	
	// eventDate format is "YYYY-MM-DDTHH24:MI:SS"
	@XmlElement(name = "eventDate", required = false)
	private String eventDate;
	
	@XmlElement(name = "orderStatus", required = false)
	private String orderStatus;	
	
	@XmlElement(name = "reason", required = false)
	private String reason;

	@XmlElement(name = "reasonCode", required = false)
	private String reasonCode;
		
	@XmlElement(name = "bookOfBusinessId", required = false)
	private String bookOfBusinessId;

	@XmlElement(name = "eventName", required = false)
	private String eventName;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getReferenceNumber() {
		return referenceNumber;
	}
	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}
	public Money getCreditAmount() {
		return creditAmount;
	}
	public void setCreditAmount(Money creditAmount) {
		this.creditAmount = creditAmount;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getAppliedDate() {
		return appliedDate;
	}
	public void setAppliedDate(String appliedDate) {
		this.appliedDate = appliedDate;
	}
	public SellerPaymentStatusEnum getStatus() {
		return status;
	}
	public void setStatus(SellerPaymentStatusEnum status) {
		this.status = status;
	}
	public String getEventId() {
		return eventId;
	}
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	public String getEventDate() {
		return eventDate;
	}
	public void setEventDate(String eventDate) {
		this.eventDate = eventDate;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getReasonCode() {return reasonCode;}
	public void setReasonCode(String reasonCode) {this.reasonCode = reasonCode; }
	public String getBookOfBusinessId() {
		return bookOfBusinessId;
	}
	public void setBookOfBusinessId(String bookOfBusinessId) {
		this.bookOfBusinessId = bookOfBusinessId;
	}
    public String getEventName() {
        return eventName;
    }
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
	public String getSellerId() {
		return sellerId;
	}
	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}
}