package com.stubhub.integration.partnerorder.entity;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"orderId", "fulfillmentType",
		"buyerFirstName", "buyerLastName", "tickets", "orderTotal",
		"sellerTotalPayout", "listingId", "externalListingId"})

@XmlRootElement(name = "ReserveRequest")
public class HoldInventoryRequest{
	
	@XmlElement(name = "OrderId", required = true)
	private Long orderId;

	@XmlElement(name = "FulfillmentType", required = true)
	private String fulfillmentType;

	@XmlElement(name = "BuyerFirstName", required = true)
	private String buyerFirstName;

	@XmlElement(name = "BuyerLastName", required = true)
	private String buyerLastName;

	@XmlElementWrapper(name = "Tickets", required = true)
	@XmlElement(name = "Ticket", required = true)
	private List<PartnerOrderTicket> tickets;

	@XmlElement(name = "OrderTotal", required = true)
	private String orderTotal;

	@XmlElement(name = "SellerTotalPayout", required = true)
	private String sellerTotalPayout;
	
	@XmlElement(name = "ListingId", required = true)
	private Long listingId;
	
	@XmlElement(name = "ExternalListingId", required = true)
	private String externalListingId;
	
	@XmlTransient 
	private Long sellerId;
	
	public Long getListingId() {
		return listingId;
	}

	public void setListingId(Long listingId) {
		this.listingId = listingId;
	}

	public String getExternalListingId() {
		return externalListingId;
	}

	public void setExternalListingId(String externalListingId) {
		this.externalListingId = externalListingId;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getFulfillmentType() {
		return fulfillmentType;
	}

	public void setFulfillmentType(String fulfillmentType) {
		this.fulfillmentType = fulfillmentType;
	}

	public String getBuyerFirstName() {
		return buyerFirstName;
	}

	public void setBuyerFirstName(String buyerFirstName) {
		this.buyerFirstName = buyerFirstName;
	}

	public String getBuyerLastName() {
		return buyerLastName;
	}

	public void setBuyerLastName(String buyerLastName) {
		this.buyerLastName = buyerLastName;
	}

	public List<PartnerOrderTicket> getTickets() {
		return tickets;
	}

	public void setTickets(List<PartnerOrderTicket> tickets) {
		this.tickets = tickets;
	}
	
	public void addTicket(PartnerOrderTicket ticket) {
		if (this.tickets == null) {
			this.tickets = new ArrayList<PartnerOrderTicket>();
		}
		if (ticket != null) {
			this.tickets.add(ticket);
		}
	}
	
	public String getOrderSeats() {
		StringBuilder seats = new StringBuilder();
		for(PartnerOrderTicket ticket : this.tickets) {
			seats.append(ticket.getSeatNumber()).append(",");
		}
		if(seats.length() > 0) {
			seats.setLength(seats.length()-1);
		}
		return seats.toString();
	}

	public String getOrderTotal() {
		return orderTotal;
	}

	public void setOrderTotal(String orderTotal) {
		this.orderTotal = orderTotal;
	}

	public String getSellerTotalPayout() {
		return sellerTotalPayout;
	}

	public void setSellerTotalPayout(String sellerTotalPayout) {
		this.sellerTotalPayout = sellerTotalPayout;
	}

	public Long getSellerId() {
		return sellerId;
	}

	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}

	@Override
	public String toString() {
		return "HoldInventoryRequest [orderId=" + orderId
				+ ", fulfillmentType=" + fulfillmentType + ", buyerFirstName="
				+ buyerFirstName + ", buyerLastName=" + buyerLastName
				+ ", tickets=" + tickets + ", orderTotal=" + orderTotal
				+ ", sellerTotalPayout=" + sellerTotalPayout + ", listingId="
				+ listingId + ", externalListingId=" + externalListingId
				+ ", sellerId=" + sellerId + "]";
	}

}

