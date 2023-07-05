package com.stubhub.domain.account.intf;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;

import com.stubhub.domain.account.common.Response;
import com.stubhub.newplatform.common.entity.Money;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "transaction")
@JsonRootName("transaction")
@XmlType(name = "", propOrder = {
		"orderId", 
		"listingId", 
		"pricePerTicket", 
		"totalCost", 
		"quantityPurchased",
		"section", 
		"row", 
		"seats", 
		"buyerId",
		"sellerId",
		"sellerConfirmed",
		"cancelled", 
		"csFlagged", 
		"buyerContactId", 
		"saleDateUTC",
		"sellerPayoutAmount",
		"sellerPaymentTypeId"
})		
public class TransactionResponse extends Response{
	@XmlElement(name = "orderId", required = false)
	@JsonProperty("orderId")
	private String orderId;		
	@XmlElement(name = "listingId", required = false)
	@JsonProperty("listingId")
	private String listingId;		
	@XmlElement(name = "pricePerTicket", required = false)
	@JsonProperty("pricePerTicket")
	private Money pricePerTicket;
	@XmlElement(name = "totalCost", required = false)
	@JsonProperty("totalCost")
	private Money totalCost;
	@XmlElement(name = "quantityPurchased", required = false)
	@JsonProperty("quantityPurchased")
	private String quantityPurchased;
	@XmlElement(name = "section", required = false)
	@JsonProperty("section")
	private String section;
	@XmlElement(name = "row", required = false)
	@JsonProperty("row")
	private String row;
	@XmlElement(name = "seats", required = false)
	@JsonProperty("seats")
	private String seats;
	@XmlElement(name = "buyerId", required = false)
	@JsonProperty("buyerId")
	private String buyerId;
	@XmlElement(name = "cancelled", required = false)
	@JsonProperty("cancelled")
	private Boolean cancelled;
	@XmlElement(name = "csFlagged", required = false)
	@JsonProperty("csFlagged")
	private Boolean csFlagged;
	@XmlElement(name = "buyerContactId", required = false)
	@JsonProperty("buyerContactId")
	private String buyerContactId;
	@XmlElement(name = "saleDateUTC", required = false)
	@JsonProperty("saleDateUTC")
	private String saleDateUTC;
	@XmlElement(name = "sellerConfirmed", required = false)
	@JsonProperty("sellerConfirmed")
	private Boolean sellerConfirmed;
	@XmlElement(name = "sellerId", required = false)
	@JsonProperty("sellerId")
	private String sellerId;
	@XmlElement(name = "sellerPayoutAmount", required = false)
	@JsonProperty("sellerPayoutAmount")
	private Money sellerPayoutAmount;	
	@XmlElement(name = "sellerPaymentTypeId", required = false)
	@JsonProperty("sellerPaymentTypeId")
	private String sellerPaymentTypeId;
	
	public Money getSellerPayoutAmount() {
		return sellerPayoutAmount;
	}
	public void setSellerPayoutAmount(Money sellerPayoutAmount) {
		this.sellerPayoutAmount = sellerPayoutAmount;
	}
	public String getSellerPaymentTypeId() {
		return sellerPaymentTypeId;
	}
	public void setSellerPaymentTypeId(String sellerPaymentTypeId) {
		this.sellerPaymentTypeId = sellerPaymentTypeId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getListingId() {
		return listingId;
	}
	public void setListingId(String listingId) {
		this.listingId = listingId;
	}
	public Money getPricePerTicket() {
		return pricePerTicket;
	}
	public void setPricePerTicket(Money pricePerTicket) {
		this.pricePerTicket = pricePerTicket;
	}
	public Money getTotalCost() {
		return totalCost;
	}
	public void setTotalCost(Money totalCost) {
		this.totalCost = totalCost;
	}
	public String getQuantityPurchased() {
		return quantityPurchased;
	}
	public void setQuantityPurchased(String quantityPurchased) {
		this.quantityPurchased = quantityPurchased;
	}
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}
	public String getRow() {
		return row;
	}
	public void setRow(String row) {
		this.row = row;
	}
	public String getSeats() {
		return seats;
	}
	public void setSeats(String seats) {
		this.seats = seats;
	}
	public String getBuyerId() {
		return buyerId;
	}
	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}
	public Boolean getCancelled() {
		return cancelled;
	}
	public void setCancelled(Boolean cancelled) {
		this.cancelled = cancelled;
	}
	public Boolean getCsFlagged() {
		return csFlagged;
	}
	public void setCsFlagged(Boolean csFlagged) {
		this.csFlagged = csFlagged;
	}
	public String getBuyerContactId() {
		return buyerContactId;
	}
	public void setBuyerContactId(String buyerContactId) {
		this.buyerContactId = buyerContactId;
	}
	public String getSaleDateUTC() {
		return saleDateUTC;
	}
	public void setSaleDateUTC(String saleDateUTC) {
		this.saleDateUTC = saleDateUTC;
	}
	public Boolean getSellerConfirmed() {
		return sellerConfirmed;
	}
	public void setSellerConfirmed(Boolean sellerConfirmed) {
		this.sellerConfirmed = sellerConfirmed;
	}
	public String getSellerId() {
		return sellerId;
	}
	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

}