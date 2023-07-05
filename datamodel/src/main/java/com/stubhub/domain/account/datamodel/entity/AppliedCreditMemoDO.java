package com.stubhub.domain.account.datamodel.entity;

import java.math.BigDecimal;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "CM_PAYMENT_XREF")
@NamedQuery(name = "AppliedCreditMemoDO.findByAppliedPaymentId", query = "FROM AppliedCreditMemoDO WHERE appliedPid = :appliedPid")
public class AppliedCreditMemoDO {

    @Id
    @Column(name = "CM_PAYMENT_XREF_ID")
    private Long id;

    @Column(name = "GP_MSG_SEQ")
    private String messageSeq;

    @Column(name = "CM_PID")
    private Long cmPid;

    @Column(name = "CM_TID")
    private Long cmTid;

    @Column(name = "APPLIED_PID")
    private Long appliedPid;

    @Column(name = "APPLIED_TID")
    private Long appliedTid;

    @Column(name = "APPLIED_DATE")
    @Type(type = "com.stubhub.newplatform.persistence.adapter.UTCCalendarToTimestampAdapter")
    private Calendar appliedDate;

    @Column(name = "APPLIED_AMOUNT")
    private BigDecimal appliedAmount;

    @Column(name = "CANCELLED")
    private Boolean cancelled;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessageSeq() {
        return messageSeq;
    }

    public void setMessageSeq(String messageSeq) {
        this.messageSeq = messageSeq;
    }

    public Long getCmPid() {
        return cmPid;
    }

    public void setCmPid(Long cmPid) {
        this.cmPid = cmPid;
    }

    public Long getCmTid() {
        return cmTid;
    }

    public void setCmTid(Long cmTid) {
        this.cmTid = cmTid;
    }

    public Long getAppliedPid() {
        return appliedPid;
    }

    public void setAppliedPid(Long appliedPid) {
        this.appliedPid = appliedPid;
    }

    public Long getAppliedTid() {
        return appliedTid;
    }

    public void setAppliedTid(Long appliedTid) {
        this.appliedTid = appliedTid;
    }

    public Calendar getAppliedDate() {
        return appliedDate;
    }

    public void setAppliedDate(Calendar appliedDate) {
        this.appliedDate = appliedDate;
    }

    public BigDecimal getAppliedAmount() {
        return appliedAmount;
    }

    public void setAppliedAmount(BigDecimal appliedAmount) {
        this.appliedAmount = appliedAmount;
    }

    public Boolean getCancelled() {
        return cancelled;
    }

    public void setCancelled(Boolean cancelled) {
        this.cancelled = cancelled;
    }
}