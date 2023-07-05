package com.stubhub.domain.account.intf;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Invoice", propOrder = {"sellerPaymentType", "referenceNumber", "payeeName", "subtotal",
        "adjustments", "total", "reasonDesc", "isAdjustment", "appliedCreditMemos"})
public class Invoice {
    @XmlElement(name = "SellerPaymentType", required = false)
    private String sellerPaymentType;

    @XmlElement(name = "ReferenceNumber", required = false)
    private String referenceNumber;

    @XmlElement(name = "PayeeName", required = false)
    private String payeeName;

    @XmlElement(name = "Subtotal", required = false)
    private Money subtotal;

    @XmlElement(name = "Adjustment", required = false)
    private Money adjustments;

    @XmlElement(name = "Total", required = false)
    private Money total;

    @XmlElement(name = "ReasonDesc", required = false)
    private String reasonDesc;

    @XmlElement(name = "IsAdjustment", required = false)
    private Boolean isAdjustment = Boolean.FALSE;

    @XmlElement(name = "AppliedCreditMemos", required = false)
    private List<AppliedCreditMemo> appliedCreditMemos;

    public String getSellerPaymentType() {
        return sellerPaymentType;
    }

    public void setSellerPaymentType(String sellerPaymentType) {
        this.sellerPaymentType = sellerPaymentType;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public Money getAdjustments() {
        return adjustments;
    }

    public void setAdjustments(Money adjustments) {
        this.adjustments = adjustments;
    }

    public Money getTotal() {
        return total;
    }

    public void setTotal(Money total) {
        this.total = total;
    }

    public Money getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Money subtotal) {
        this.subtotal = subtotal;
    }

    public String getReasonDesc() {
        return reasonDesc;
    }

    public void setReasonDesc(String reasonDesc) {
        this.reasonDesc = reasonDesc;
    }

    public Boolean getIsAdjustment() {
        return isAdjustment;
    }

    public void setIsAdjustment(Boolean isAdjustment) {
        this.isAdjustment = isAdjustment;
    }

    public List<AppliedCreditMemo> getAppliedCreditMemos() {
        return appliedCreditMemos;
    }

    public void setAppliedCreditMemos(List<AppliedCreditMemo> creditMemos) {
        this.appliedCreditMemos = creditMemos;
    }
}