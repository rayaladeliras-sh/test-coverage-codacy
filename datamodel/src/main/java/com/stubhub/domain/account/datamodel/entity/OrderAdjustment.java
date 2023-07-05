package com.stubhub.domain.account.datamodel.entity;

import java.math.BigDecimal;

public class OrderAdjustment {

    private Long orderId;

    private String currencyCode;

    private String referenceNumber;

    private BigDecimal amount;

    private Long reasonCode;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(Long reasonCode) {
        this.reasonCode = reasonCode;
    }
}