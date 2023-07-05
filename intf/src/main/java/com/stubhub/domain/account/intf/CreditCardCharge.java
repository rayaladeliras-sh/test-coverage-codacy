package com.stubhub.domain.account.intf;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.stubhub.domain.account.common.Response;
import com.stubhub.newplatform.common.entity.Money;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "creditCardCharge")
@XmlType(name = "", propOrder = {
		"id",
		"orderID",
		"chargedAmount",		
		"creditCardNumber",
		"chargedDate",
		"status",
		"transactionType",
		"reasonCode",
		"reasonDescription",
		"reasonGroupCode",
		"reasonGroupDescription",
		"eventName"
		})

public final class CreditCardCharge extends Response {
	
	@XmlElement(name = "id", required = true)
	private String id;
	
	@XmlElement(name = "orderID", required = true)
	private Long orderID;
	
	@XmlElement(name = "chargedAmount", required = true)
	private Money chargedAmount;
	
	//Last four digits of Credit Card Number
	@XmlElement(name = "creditCardNumber", required = true)
	private String creditCardNumber;
	
	@XmlElement(name = "chargedDate", required = true)
	private String chargedDate;
	
	@XmlElement(name = "status", required = true)
	private String status;
	
	@XmlElement(name = "transactionType", required = true)
	private String transactionType;
	
	@XmlElement(name = "reasonCode", required = true)
	private String reasonCode;
	
	@XmlElement(name = "reasonDescription", required = true)
	private String reasonDescription;
	
	@XmlElement(name = "reasonGroupCode", required = true)
	private String reasonGroupCode;
	
	@XmlElement(name = "reasonGroupDescription", required = true)
	private String reasonGroupDescription;

	@XmlElement(name = "eventName")
	private String eventName;
	
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public Long getOrderID() {
		return orderID;
	}
	
	public void setOrderID(Long orderID) {
		this.orderID = orderID;
	}
	
	public Money getChargedAmount() {
		return chargedAmount;
	}
	
	public void setChargedAmount(Money chargedAmount) {
		this.chargedAmount = chargedAmount;
	}
	
	public String getCreditCardNumber() {
		return creditCardNumber;
	}
	
	public void setCreditCardNumber(String creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
	}
	
	public String getChargedDate() {
		return chargedDate;
	}
	
	public void setChargedDate(String chargedDate) {
		this.chargedDate = chargedDate;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getReasonDescription() {
		return reasonDescription;
	}

	public void setReasonDescription(String reasonDescription) {
		this.reasonDescription = reasonDescription;
	}

	public String getReasonCode() {
		return reasonCode;
	}

	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}

	public String getReasonGroupCode() {
		return reasonGroupCode;
	}

	public void setReasonGroupCode(String reasonGroupCode) {
		this.reasonGroupCode = reasonGroupCode;
	}

	public String getReasonGroupDescription() {
		return reasonGroupDescription;
	}

	public void setReasonGroupDescription(String reasonGroupDescription) {
		this.reasonGroupDescription = reasonGroupDescription;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	
}
