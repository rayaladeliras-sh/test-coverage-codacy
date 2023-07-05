package com.stubhub.domain.account.intf;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;

import com.stubhub.domain.account.common.Response;

@XmlAccessorType(XmlAccessType.FIELD)
@JsonRootName(value = "order")
@XmlRootElement(name = "order")
@XmlType(name = "", propOrder = {
		"transaction", 
		"subs", 
		"event", 
		"sellerPayments", 
		"delivery"
})		
public class CSOrderDetailsResponse extends Response {
	@XmlElement(name = "transaction", required = true)
	@JsonProperty("transaction")
	private TransactionResponse transaction;	
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

	public TransactionResponse getTransaction() {
		return transaction;
	}
	public void setTransaction(TransactionResponse transaction) {
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
