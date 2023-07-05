/**
 * 
 */
package com.stubhub.domain.account.datamodel.entity;

import java.math.BigDecimal;
import java.util.Calendar;

public class InvoiceDO {

    private Long orderId;
    
    private Long eventId;

    private String refNumber;

    private Calendar orderDate;

    private String eventName;

    private String venue;

    private Long sellerId;

    private Calendar eventDate;

    private Integer quantity;

    private String section;

    private String rowDesc;

    private String seats;

    private String ticketFeatures;

    private String ticketDisclosures;

    private String comments;

    private BigDecimal ticketCost;

    private BigDecimal pricePerTicket;

    private BigDecimal sellerFeeCost;

    private BigDecimal shippingFeeCost;

    private BigDecimal sellerPayoutAmount;

    private Long sellerPaymentTypeId;

    private String deliveryMethod;

    private String payeeName;

    private String currencyCode;

    private BigDecimal vatBuyFee;

    private BigDecimal vatBuyPercentage;

    private BigDecimal vatSellFee;

    private BigDecimal vatSellPercentage;

    private BigDecimal vatLogFee;

    private BigDecimal vatLogPercentage;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getRefNumber() {
        return refNumber;
    }

    public void setRefNumber(String refNumber) {
        this.refNumber = refNumber;
    }

    public Calendar getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Calendar orderDate) {
        this.orderDate = orderDate;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public Calendar getEventDate() {
        return eventDate;
    }

    public void setEventDate(Calendar eventDate) {
        this.eventDate = eventDate;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getRowDesc() {
        return rowDesc;
    }

    public void setRowDesc(String rowDesc) {
        this.rowDesc = rowDesc;
    }

    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }

    public String getTicketFeatures() {
        return ticketFeatures;
    }

    public void setTicketFeatures(String ticketFeatures) {
        this.ticketFeatures = ticketFeatures;
    }

    public String getTicketDisclosures() {
        return ticketDisclosures;
    }

    public void setTicketDisclosures(String ticketDisclosures) {
        this.ticketDisclosures = ticketDisclosures;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public BigDecimal getTicketCost() {
        return ticketCost;
    }

    public void setTicketCost(BigDecimal ticketCost) {
        this.ticketCost = ticketCost;
    }

    public BigDecimal getPricePerTicket() {
        return pricePerTicket;
    }

    public void setPricePerTicket(BigDecimal pricePerTicket) {
        this.pricePerTicket = pricePerTicket;
    }

    public BigDecimal getSellerFeeCost() {
        return sellerFeeCost;
    }

    public void setSellerFeeCost(BigDecimal sellerFeeCost) {
        this.sellerFeeCost = sellerFeeCost;
    }

    public BigDecimal getShippingFeeCost() {
        return shippingFeeCost;
    }

    public void setShippingFeeCost(BigDecimal shippingFeeCost) {
        this.shippingFeeCost = shippingFeeCost;
    }

    public BigDecimal getSellerPayoutAmount() {
        return sellerPayoutAmount;
    }

    public void setSellerPayoutAmount(BigDecimal sellerPayoutAmount) {
        this.sellerPayoutAmount = sellerPayoutAmount;
    }

    public Long getSellerPaymentTypeId() {
        return sellerPaymentTypeId;
    }

    public void setSellerPaymentTypeId(Long sellerPaymentTypeId) {
        this.sellerPaymentTypeId = sellerPaymentTypeId;
    }

    public String getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(String deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public BigDecimal getVatBuyFee() {
        return vatBuyFee;
    }

    public void setVatBuyFee(BigDecimal vatBuyFee) {
        this.vatBuyFee = vatBuyFee;
    }

    public BigDecimal getVatBuyPercentage() {
        return vatBuyPercentage;
    }

    public void setVatBuyPercentage(BigDecimal vatBuyPercentage) {
        this.vatBuyPercentage = vatBuyPercentage;
    }

    public BigDecimal getVatSellFee() {
        return vatSellFee;
    }

    public void setVatSellFee(BigDecimal vatSellFee) {
        this.vatSellFee = vatSellFee;
    }

    public BigDecimal getVatSellPercentage() {
        return vatSellPercentage;
    }

    public void setVatSellPercentage(BigDecimal vatSellPercentage) {
        this.vatSellPercentage = vatSellPercentage;
    }

    public BigDecimal getVatLogFee() {
        return vatLogFee;
    }

    public void setVatLogFee(BigDecimal vatLogFee) {
        this.vatLogFee = vatLogFee;
    }

    public BigDecimal getVatLogPercentage() {
        return vatLogPercentage;
    }

    public void setVatLogPercentage(BigDecimal vatLogPercentage) {
        this.vatLogPercentage = vatLogPercentage;
    }

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}
}
