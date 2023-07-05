package com.stubhub.domain.account.intf;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;

import com.stubhub.newplatform.common.entity.Money;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "sub")
@JsonRootName(value = "sub")
public class SubstitutionRequest {
    @XmlElement(name = "listingId", required = false)
    @JsonProperty("listingId")
    protected String listingId;
    @XmlElement(name = "quantity", required = false)
    @JsonProperty("quantity")
    protected String quantity;
    @XmlElement(name = "ticketCostDifference", required = false)
    @JsonProperty("ticketCostDifference")
    protected Money ticketCostDifference;
    @XmlElement(name = "sellerPayoutDifference", required = false)
    @JsonProperty("sellerPayoutDifference")
    protected Money sellerPayoutDifference;
    @XmlElement(name = "subsReasonId", required = false)
    @JsonProperty("subsReasonId")
    protected String subsReasonId;
    @XmlElement(name = "deliveryMethodId", required = false)
    @JsonProperty("deliveryMethodId")
    protected String deliveryMethodId;
    @XmlElement(name = "fulfillmentMethodId", required = false)
    @JsonProperty("fulfillmentMethodId")
    protected String fulfillmentMethodId;    
    @XmlElement(name = "lmsLocationId", required = false)
    @JsonProperty("lmsLocationId")
    protected String lmsLocationId;
    @XmlElement(name = "inHandDate", required = false)
    @JsonProperty("inHandDate")
    protected String inHandDate;    
    @XmlElement(name = "ticketCost", required = false)
    @JsonProperty("ticketCost")
    protected Money ticketCost;
    @XmlElement(name = "shipCost", required = false)
    @JsonProperty("shipCost")
    protected Money shipCost;
    @XmlElement(name = "totalCost", required = false)
    @JsonProperty("totalCost")
    protected Money totalCost;
    @XmlElement(name = "discountCost", required = false)
    @JsonProperty("discountCost")
    protected Money discountCost;
    @XmlElement(name = "sellerFeeVal", required = false)
    @JsonProperty("sellerFeeVal")
    protected Money sellerFeeVal;
    @XmlElement(name = "buyerFeeVal", required = false)
    @JsonProperty("buyerFeeVal")
    protected Money buyerFeeVal;
    @XmlElement(name = "premiumFees", required = false)
    @JsonProperty("premiumFees")
    protected Money premiumFees;
    @XmlElement(name = "sellerPayoutAmount", required = false)
    @JsonProperty("sellerPayoutAmount")
    protected Money sellerPayoutAmount;
    @XmlElement(name = "sellerPayoutAtConfirm", required = false)
    @JsonProperty("sellerPayoutAtConfirm")
    protected Money sellerPayoutAtConfirm;
    @XmlElement(name = "addOnFee", required = false)
    @JsonProperty("addOnFee")
    protected Money addOnFee;
    @XmlElement(name = "vatBuyFee", required = false)
    @JsonProperty("vatBuyFee")
    protected Money vatBuyFee;
    @XmlElement(name = "vatLogFee", required = false)
    @JsonProperty("vatLogFee")
    protected Money vatLogFee;
    @XmlElement(name = "vatSellFee", required = false)
    @JsonProperty("vatSellFee")
    protected Money vatSellFee;
    @XmlElement(name = "additionalSellFeePerTicket", required = false)
    @JsonProperty("additionalSellFeePerTicket")
    protected Money additionalSellFeePerTicket;
    private String operatorId;    
    
	public String getListingId() {
		return listingId;
	}
	public void setListingId(String listingId) {
		this.listingId = listingId;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public Money getTicketCostDifference() {
		return ticketCostDifference;
	}
	public void setTicketCostDifference(Money ticketCostDifference) {
		this.ticketCostDifference = ticketCostDifference;
	}
	public Money getSellerPayoutDifference() {
		return sellerPayoutDifference;
	}
	public void setSellerPayoutDifference(Money sellerPayoutDifference) {
		this.sellerPayoutDifference = sellerPayoutDifference;
	}
	public String getSubsReasonId() {
		return subsReasonId;
	}
	public void setSubsReasonId(String subsReasonId) {
		this.subsReasonId = subsReasonId;
	}
	public String getDeliveryMethodId() {
		return deliveryMethodId;
	}
	public void setDeliveryMethodId(String deliveryMethodId) {
		this.deliveryMethodId = deliveryMethodId;
	}
	public String getFulfillmentMethodId() {
		return fulfillmentMethodId;
	}
	public void setFulfillmentMethodId(String fulfillmentMethodId) {
		this.fulfillmentMethodId = fulfillmentMethodId;
	}
	public String getLmsLocationId() {
		return lmsLocationId;
	}
	public void setLmsLocationId(String lmsLocationId) {
		this.lmsLocationId = lmsLocationId;
	}
	public String getInHandDate() {
		return inHandDate;
	}
	public void setInHandDate(String inHandDate) {
		this.inHandDate = inHandDate;
	}
	public Money getTicketCost() {
		return ticketCost;
	}
	public void setTicketCost(Money ticketCost) {
		this.ticketCost = ticketCost;
	}
	public Money getShipCost() {
		return shipCost;
	}
	public void setShipCost(Money shipCost) {
		this.shipCost = shipCost;
	}
	public Money getTotalCost() {
		return totalCost;
	}
	public void setTotalCost(Money totalCost) {
		this.totalCost = totalCost;
	}
	public Money getDiscountCost() {
		return discountCost;
	}
	public void setDiscountCost(Money discountCost) {
		this.discountCost = discountCost;
	}
	public Money getSellerFeeVal() {
		return sellerFeeVal;
	}
	public void setSellerFeeVal(Money sellerFeeVal) {
		this.sellerFeeVal = sellerFeeVal;
	}
	public Money getBuyerFeeVal() {
		return buyerFeeVal;
	}
	public void setBuyerFeeVal(Money buyerFeeVal) {
		this.buyerFeeVal = buyerFeeVal;
	}
	public Money getPremiumFees() {
		return premiumFees;
	}
	public void setPremiumFees(Money premiumFees) {
		this.premiumFees = premiumFees;
	}
	public Money getSellerPayoutAmount() {
		return sellerPayoutAmount;
	}
	public void setSellerPayoutAmount(Money sellerPayoutAmount) {
		this.sellerPayoutAmount = sellerPayoutAmount;
	}
	public Money getSellerPayoutAtConfirm() {
		return sellerPayoutAtConfirm;
	}
	public void setSellerPayoutAtConfirm(Money sellerPayoutAtConfirm) {
		this.sellerPayoutAtConfirm = sellerPayoutAtConfirm;
	}
	public Money getAddOnFee() {
		return addOnFee;
	}
	public void setAddOnFee(Money addOnFee) {
		this.addOnFee = addOnFee;
	}
	public Money getVatBuyFee() {
		return vatBuyFee;
	}
	public void setVatBuyFee(Money vatBuyFee) {
		this.vatBuyFee = vatBuyFee;
	}
	public Money getVatLogFee() {
		return vatLogFee;
	}
	public void setVatLogFee(Money vatLogFee) {
		this.vatLogFee = vatLogFee;
	}
	public Money getVatSellFee() {
		return vatSellFee;
	}
	public void setVatSellFee(Money vatSellFee) {
		this.vatSellFee = vatSellFee;
	}
	public Money getAdditionalSellFeePerTicket() {
		return additionalSellFeePerTicket;
	}
	public void setAdditionalSellFeePerTicket(Money additionalSellFeePerTicket) {
		this.additionalSellFeePerTicket = additionalSellFeePerTicket;
	}
	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}
	public String getOperatorId() {
		return operatorId;
	}
	
	
}
