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
@XmlType(name = "", propOrder = { "orderId", "listingId", "externalListingId", "buyerFirstName", 
		"buyerLastName", "buyerGUID", "payoutAmount","marcomPartnerId", "marcomOptin", "tickets", "orderStatusId","userContact", "sellerGUID" })
@XmlRootElement(name = "PlaceOrderRequest")
public class PurchaseInventoryRequest{
	
	@XmlElement(name = "OrderId", required = true)
	private Long orderId;
	
	@XmlElement(name = "ListingId", required = true)
	private Long listingId;
	
	@XmlElement(name = "ExternalListingId", required = true)
	private String externalListingId;
	
	@XmlElement(name = "BuyerFirstName", required = true)
	private String buyerFirstName;

	@XmlElement(name = "BuyerLastName", required = true)
	private String buyerLastName;
	
	@XmlElement(name = "BuyerGUID", required = true)
	private String buyerGUID;
	
	@XmlElement(name = "PayoutAmount", required = true)
	private String payoutAmount;
	@XmlElement(name = "MarcomPartnerId", required = false)
	private Long marcomPartnerId;

	@XmlElement(name = "MarcomOptin", required = false)
	private Boolean marcomOptin;

	@XmlElementWrapper(name = "Tickets", required = true)
	@XmlElement(name = "Ticket", required = true)
	private List<PartnerOrderTicket> tickets;
	
	@XmlElement(name = "SellerGUID", required = false)
	private String sellerGUID;
	
	@XmlTransient 
	private long sellerId;
	
	@XmlElement(name = "OrderStatusId", required = false)
	private long orderStatusId;
	
	@XmlElement(name = "FulfillmentContact", required = true)
	private UserContact userContact;
	
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	
	public Long getOrderId() {
		return orderId;
	}
	
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

	public String getBuyerGUID() {
		return buyerGUID;
	}

	public void setBuyerGUID(String buyerGUID) {
		this.buyerGUID = buyerGUID;
	}

	public String getPayoutAmount() {
		return payoutAmount;
	}

	public void setPayoutAmount(String payoutAmount) {
		this.payoutAmount = payoutAmount;
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
	
	public long getSellerId() {
		return sellerId;
	}

	public void setSellerId(long sellerId) {
		this.sellerId = sellerId;
	}

	public UserContact getUserContact() {
		return userContact;
	}

	public void setUserContact(UserContact userContact) {
		this.userContact = userContact;
	}

	public long getOrderStatusId() {
		return orderStatusId;
	}

	public void setOrderStatusId(long orderStatusId) {
		this.orderStatusId = orderStatusId;
	}
	

	public Long getMarcomPartnerId() {
		return marcomPartnerId;
	}

	public void setMarcomPartnerId(Long marcomPartnerId) {
		this.marcomPartnerId = marcomPartnerId;
	}

	public Boolean getMarcomOptin() {
		return marcomOptin;
	}

	public void setMarcomOptin(Boolean marcomOptin) {
		this.marcomOptin = marcomOptin;
	}

	@Override
	public String toString() {
		return "PlaceOrderRequest [orderId=" + getOrderId()  + ", listingId="+ listingId 
				+ ", externalListingId="+ externalListingId + ", buyerFirstName="+ buyerFirstName + ", buyerLastName="+ buyerLastName 
				+ ", buyerGUID=" + buyerGUID + ", payoutAmount=" + payoutAmount + ", tickets="+ tickets + ", userContact=" + userContact + "]";
	}

	public String getSellerGUID() {
		return sellerGUID;
	}

	public void setSellerGUID(String sellerGUID) {
		this.sellerGUID = sellerGUID;
	}

}

