package com.stubhub.domain.account.datamodel.entity;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "SELLER_PAYMENTS")
@NamedQueries(value = {
        @NamedQuery(name = "SellerPayment.getSellerPaymentsByIds", query = "FROM SellerPayment WHERE id IN (:ids)"),
        @NamedQuery(name = "SellerPayment.getSellerPaymentByRefNumber", query = "FROM SellerPayment WHERE referenceNumber = :refNumber") })
public class SellerPayment implements java.io.Serializable {
	private static final long serialVersionUID = -6150812633316075373L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SELLER_PAYMENTS_SEQ")
	@SequenceGenerator(name = "SELLER_PAYMENTS_SEQ", sequenceName = "SELLER_PAYMENTS_SEQ", allocationSize = 1)
	private Long id;

	@Column(name = "TID")
	private Long orderId;

	@Column(name = "SELLER_PAYMENT_STATUS_ID")
	private Long sellerPaymentStatusId;

	@Column(name = "AMOUNT")
	private Double amount;

	@Column(name = "CURRENCY_CODE")
	private String currencyCode;

	@Column(name = "BOOK_OF_BUSINESS_ID")
	private Long bobId;

	@Column(name = "EVENT_DATE_LOCAL")
	private String eventDateLocal;

	@Column(name = "EVENT_DATE")
	@Type(type = "com.stubhub.newplatform.persistence.adapter.UTCCalendarToTimestampAdapter")
	private Calendar eventDate;

	@Column(name = "EVENT_ID")
	private Long eventId;

	@Column(name = "ORDER_STATUS")
	private String orderStatus;

	@Column(name = "PAYEE_EMAIL_ID")
	private String payeeEmailId;

	@Column(name = "PAYEE_NAME")
	private String payeeName;

	@Column(name = "REASON_DESCRIPTION")
	private String reasonDescription;

	@Column(name = "REFERENCE_NUMBER")
	private String referenceNumber;

	@Column(name = "DATE_ADDED")
	@Type(type = "com.stubhub.newplatform.persistence.adapter.UTCCalendarToTimestampAdapter")
	private Calendar dateAdded;

	@Column(name = "SELLER_ID")
	private Long sellerId;

	@Column(name = "STATUS")
	private String status;

	@Column(name = "DISBURSEMENT_OPTION_ID")
	private Long disbursementOptionId;

	@Column(name = "ACCT_LAST_FOUR_DIGITS")
	private String acctLastFourDigits;

	@Column(name = "BANK_NAME")
	private String bankName;

	@Column(name = "SELLER_PAYMENT_TYPE_ID")
	private Long sellerPaymentTypeId;

	@Column(name = "LAST_UPDATED_BY")
	private String lastUpdatedBy;

	@Column(name = "LAST_UPDATED")
	@Type(type = "com.stubhub.newplatform.persistence.adapter.UTCCalendarToTimestampAdapter")
	private Calendar lastUpdatedDate;

	@Column(name = "PAYMENT_SENT_TO_GATEWAY_DT")
	@Type(type = "com.stubhub.newplatform.persistence.adapter.UTCCalendarToTimestampAdapter")
	private Calendar paymentSent2GatewayDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public Calendar getDateAdded() {
		return dateAdded;
	}

	public void setDateAdded(Calendar dateAdded) {
		this.dateAdded = dateAdded;
	}

	public Calendar getEventDate() {
		return eventDate;
	}

	public void setEventDate(Calendar eventDate) {
		this.eventDate = eventDate;
	}

	public Long getSellerPaymentStatusId() {
		return sellerPaymentStatusId;
	}

	public void setSellerPaymentStatusId(Long sellerPaymentStatusId) {
		this.sellerPaymentStatusId = sellerPaymentStatusId;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public String getReasonDescription() {
		return reasonDescription;
	}

	public void setReasonDescription(String reasonDescription) {
		this.reasonDescription = reasonDescription;
	}

	public Long getBobId() {
		return bobId;
	}

	public void setBobId(Long bobId) {
		this.bobId = bobId;
	}

	public String getEventDateLocal() {
		return eventDateLocal;
	}

	public void setEventDateLocal(String eventDateLocal) {
		this.eventDateLocal = eventDateLocal;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public Long getSellerId() {
		return sellerId;
	}

	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Calendar getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(Calendar lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public String getPayeeEmailId() {
		return payeeEmailId;
	}

	public void setPayeeEmailId(String payeeEmailId) {
		this.payeeEmailId = payeeEmailId;
	}

	public String getPayeeName() {
		return payeeName;
	}

	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}

	public Long getDisbursementOptionId() {
		return disbursementOptionId;
	}

	public void setDisbursementOptionId(Long disbursementOptionId) {
		this.disbursementOptionId = disbursementOptionId;
	}

	public Calendar getPaymentSent2GatewayDate() {
		return paymentSent2GatewayDate;
	}

	public void setPaymentSent2GatewayDate(Calendar paymentSent2GatewayDate) {
		this.paymentSent2GatewayDate = paymentSent2GatewayDate;
	}

	public Long getSellerPaymentTypeId() {
		return sellerPaymentTypeId;
	}

	public void setSellerPaymentTypeId(Long sellerPaymentTypeId) {
		this.sellerPaymentTypeId = sellerPaymentTypeId;
	}

	public String getAcctLastFourDigits() {
		return acctLastFourDigits;
	}

	public void setAcctLastFourDigits(String acctLastFourDigits) {
		this.acctLastFourDigits = acctLastFourDigits;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
}