package com.stubhub.domain.account.intf;

import com.stubhub.newplatform.common.entity.Money;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AllPayment {

  private String allPaymentsId;
  private Long id;
  private Long transactionId;
  private Long sellerId;
  private String type;
  private BigDecimal amount;
  private String currency;
  private String bankName;
  private String lastFourDigits;
  private String paymentMethod;
  private String referenceNumber;
  private Boolean cmApplied;
  private List<CMAppliedHistory> creditMemosApplied;
  private String chargedDate;
  private String createdDate;
  private String lastUpdateDate;
  private Long eventId;
  private String eventName;
  private String eventDate;

  private String transactionStatus;
  private String status;
  private String payeeName;
  private Long payeeId;
  private String payeeEmailId;

  public String getAllPaymentsId() {
    return allPaymentsId;
  }

  public void setAllPaymentsId(String allPaymentsId) {
    this.allPaymentsId = allPaymentsId;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(Long transactionId) {
    this.transactionId = transactionId;
  }

  public Long getSellerId() {
    return sellerId;
  }

  public void setSellerId(Long sellerId) {
    this.sellerId = sellerId;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public String getBankName() {
    return bankName;
  }

  public void setBankName(String bankName) {
    this.bankName = bankName;
  }

  public String getLastFourDigits() {
    return lastFourDigits;
  }

  public void setLastFourDigits(String lastFourDigits) {
    this.lastFourDigits = lastFourDigits;
  }

  public String getPaymentMethod() {
    return paymentMethod;
  }

  public void setPaymentMethod(String paymentMethod) {
    this.paymentMethod = paymentMethod;
  }

  public String getReferenceNumber() {
    return referenceNumber;
  }

  public void setReferenceNumber(String referenceNumber) {
    this.referenceNumber = referenceNumber;
  }

  public Boolean getCmApplied() {
    return cmApplied;
  }

  public void setCmApplied(Boolean cmApplied) {
    this.cmApplied = cmApplied;
  }

  public List<CMAppliedHistory> getCreditMemosApplied() {
    return creditMemosApplied;
  }

  public void setCreditMemosApplied(List<CMAppliedHistory> creditMemosApplied) {
    this.creditMemosApplied = creditMemosApplied;
  }

  public String getChargedDate() {
    return chargedDate;
  }

  public void setChargedDate(String chargedDate) {
    this.chargedDate = chargedDate;
  }

  public String getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(String createdDate) {
    this.createdDate = createdDate;
  }

  public String getLastUpdateDate() {
    return lastUpdateDate;
  }

  public void setLastUpdateDate(String lastUpdateDate) {
    this.lastUpdateDate = lastUpdateDate;
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

  public String getTransactionStatus() {
    return transactionStatus;
  }

  public void setTransactionStatus(String transactionStatus) {
    this.transactionStatus = transactionStatus;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getPayeeName() {
    return payeeName;
  }

  public void setPayeeName(String payeeName) {
    this.payeeName = payeeName;
  }

  public Long getPayeeId() {
    return payeeId;
  }

  public void setPayeeId(Long payeeId) {
    this.payeeId = payeeId;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public String getPayeeEmailId() {
    return payeeEmailId;
  }

  public void setPayeeEmailId(String payeeEmailId) {
    this.payeeEmailId = payeeEmailId;
  }

  public String getEventDate() {
    return eventDate;
  }

  public void setEventDate(String eventDate) {
    this.eventDate = eventDate;
  }

  @Override
  public String toString() {
    return "AllPayment{" +
            "allPaymentsId=" + allPaymentsId +
            ", id=" + id +
            ", transactionId=" + transactionId +
            ", sellerId=" + sellerId +
            ", type='" + type + '\'' +
            ", amount=" + amount +
            ", bankName='" + bankName + '\'' +
            ", lastFourDigits='" + lastFourDigits + '\'' +
            ", paymentMethod='" + paymentMethod + '\'' +
            ", referenceNumber='" + referenceNumber + '\'' +
            ", cmApplied=" + cmApplied +
            ", creditMemosApplied=" + creditMemosApplied +
            ", chargedDate='" + chargedDate + '\'' +
            ", createdDate='" + createdDate + '\'' +
            ", lastUpdateDate='" + lastUpdateDate + '\'' +
            ", eventId='" + eventId + '\'' +
            ", eventName='" + eventName + '\'' +
            ", transactionStatus='" + transactionStatus + '\'' +
            ", status='" + status + '\'' +
            ", payeeName='" + payeeName + '\'' +
            ", payeeId='" + payeeId + '\'' +
            '}';
  }
}
