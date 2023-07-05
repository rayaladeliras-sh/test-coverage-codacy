package com.stubhub.integration.partnerorder.entity;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "id", "transactionId", "tickets"})
@XmlRootElement(name = "PlaceOrderResponse")
public class PurchaseInventoryResponse extends Response{
	
	@XmlElement(name = "Id", required = true)
	private Long id;
	@XmlElement(name = "TransactionId", required = true)
	private String transactionId;
	@XmlElementWrapper(name = "Tickets", required = true)
	@XmlElement(name = "Ticket", required = true)
	private List<PartnerOrderResponseTicket> tickets;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public List<PartnerOrderResponseTicket> getTickets() {
		return tickets;
	}
	public void setTickets(List<PartnerOrderResponseTicket> tickets) {
		this.tickets = tickets;
	}
	
	@Override
	public String toString() {
		return "PurchaseInventoryResponse [Id=" + id + ", TransactionId=" + transactionId 
				+", Tickets=" + tickets +"]";
	}
}
