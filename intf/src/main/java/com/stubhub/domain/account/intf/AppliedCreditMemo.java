package com.stubhub.domain.account.intf;

import java.math.BigDecimal;
import java.util.Calendar;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AppliedCreditMemo", propOrder = { "orderId", "amount", "currency", "appliedDate", "eventId", "eventName", "evetnDate",
        "referenceNumber" })
public class AppliedCreditMemo {

    @XmlElement(name = "OrderId")
    private Long orderId;

    @XmlElement(name = "Amount")
    private BigDecimal amount;

    @XmlElement(name = "Currency")
    private String currency;

    @XmlElement(name = "AppliedDate")
    private Calendar appliedDate;

    @XmlElement(name = "EventId", required = false)
    private Long eventId;

    @XmlElement(name = "EventName", required = false)
    private String eventName;

    @XmlElement(name = "EvetnDate", required = false)
    private Calendar evetnDate;

    @XmlElement(name = "ReferenceNumber", required = false)
    private String referenceNumber;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

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

    public Calendar getAppliedDate() {
        return appliedDate;
    }

    public void setAppliedDate(Calendar appliedDate) {
        this.appliedDate = appliedDate;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Calendar getEvetnDate() {
        return evetnDate;
    }

    public void setEvetnDate(Calendar evetnDate) {
        this.evetnDate = evetnDate;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }
}
