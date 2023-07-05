package com.stubhub.domain.account.intf;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;

import com.stubhub.domain.account.common.Response;

@XmlAccessorType(XmlAccessType.FIELD)
@JsonRootName(value = "sale")
@XmlRootElement(name = "sale")
@XmlType(name = "", propOrder = {
		"transaction", 
		"subs", 
		"event", 
		"sellerPayments", 
		"delivery"
})		
public class CSSaleDetailsResponse extends Response {
	@XmlElement(name = "transaction", required = true)
	@JsonProperty("transaction")
	private CSSaleTransactionResponse transaction;	
	@XmlElement(name = "subs", required = true)
	@JsonProperty("subs")
	private SubResponse subs;
	@XmlElement(name = "event", required = true)
	@JsonProperty("event")
	private EventResponse event;
	@XmlElement(name = "sellerPayments", required = true)
	@JsonProperty("sellerPayments")
	private SellerPayments sellerPayments;
	@XmlElement(name = "delivery", required = true)
	@JsonProperty("delivery")
	private DeliveryResponse delivery;

	public CSSaleTransactionResponse getTransaction() {
		return transaction;
	}
	public void setTransaction(CSSaleTransactionResponse transaction) {
		this.transaction = transaction;
	}
	public SubResponse getSubs() {
		return subs;
	}
	public void setSubs(SubResponse subs) {
		this.subs = subs;
	}
	public EventResponse getEvent() {
		return event;
	}
	public void setEvent(EventResponse event) {
		this.event = event;
	}
	public DeliveryResponse getDelivery() {
		return delivery;
	}
	public void setDelivery(DeliveryResponse delivery) {
		this.delivery = delivery;
	}
	public SellerPayments getSellerPayments() {
		return sellerPayments;
	}
	public void setSellerPayments(SellerPayments sellerPayments) {
		this.sellerPayments = sellerPayments;
	}
	
}
