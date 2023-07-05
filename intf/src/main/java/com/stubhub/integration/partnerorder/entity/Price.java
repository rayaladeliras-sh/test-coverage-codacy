package com.stubhub.integration.partnerorder.entity;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "amount", "currency" })
@XmlRootElement(name = "Price")
public class Price {
	
	@XmlElement(name = "Amount", required = true)
	private BigDecimal amount;
	@XmlElement(name = "Currency", required = true)
	private String currency;
	
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	@Override
	public String toString() {
		return "Price [Amount=" + amount + ", Currency=" + currency + "]";
	}
}
