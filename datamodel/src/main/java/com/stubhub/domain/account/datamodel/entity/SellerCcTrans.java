package com.stubhub.domain.account.datamodel.entity;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "SELLER_CC_TRANS")
@NamedQueries(value = {
	@NamedQuery(name="SellerCcTrans.findBySellerCcId", query="from SellerCcTrans where sellerCcId in (:sellerCcIds) and transacionType in (:transTypeList) and lastUpdatedDate > SYSDATE-:days ORDER BY lastUpdatedDate DESC"),
	@NamedQuery(name="SellerCcTrans.findBySellerCcId.count", query="select count(SELLER_CC_TRANS_ID) from SellerCcTrans where sellerCcId in (:sellerCcIds) and transacionType in (:transTypeList) and lastUpdatedDate > SYSDATE-:days ORDER BY lastUpdatedDate DESC")
})
public class SellerCcTrans {
	
	@Id
	@Column(name = "SELLER_CC_TRANS_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SELLER_CC_TRANS_ID_SEQ")
	@SequenceGenerator(name = "SELLER_CC_TRANS_ID_SEQ", sequenceName = "SELLER_CC_TRANS_ID_SEQ", allocationSize = 1)
	private Long id;
	
	@Column(name = "TID")
	private Long tid;
	
	@Column(name = "SELLER_CC_ID")
	private Long sellerCcId;
	
	@Column(name = "AMOUNT")
	private Double amount;
	
	@Column(name = "TRANS_TYPE")
	private String transacionType;
	
	@Column(name = "REFERENCE_CODE")
	private String referenceCode;
	
	@Column(name = "STATUS")
	private String status;
	
	@Column(name = "RESULT_CODE")
	private Long resultCode;
	
	@Column(name = "RESPONSE_MESSAGE")
	private String responseMessage;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "REASON_CODE")
	private CcTransReason reason;
	
	@Column(name = "DATE_ADDED")
	@Type(type="com.stubhub.newplatform.persistence.adapter.UTCCalendarToTimestampAdapter")
	private Calendar dateAdded;
	
	@Column(name = "LAST_UPDATED_DATE")
	@Type(type="com.stubhub.newplatform.persistence.adapter.UTCCalendarToTimestampAdapter")
	private Calendar lastUpdatedDate;
	
	@Column(name = "CURRENCY_CODE")
	private String currencyCode;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTid() {
		return tid;
	}

	public void setTid(Long tid) {
		this.tid = tid;
	}

	public Long getSellerCcId() {
		return sellerCcId;
	}

	public void setSellerCcId(Long sellerCcId) {
		this.sellerCcId = sellerCcId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getTransacionType() {
		return transacionType;
	}

//	public String getReasonDescription() {
//		return reasonDescription;
//	}
//
//	public void setReasonDescription(String reasonDescription) {
//		this.reasonDescription = reasonDescription;
//	}

	public void setTransacionType(String transacionType) {
		this.transacionType = transacionType;
	}

	public String getReferenceCode() {
		return referenceCode;
	}

	public void setReferenceCode(String referenceCode) {
		this.referenceCode = referenceCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getResultCode() {
		return resultCode;
	}

	public void setResultCode(Long resultCode) {
		this.resultCode = resultCode;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}


	public CcTransReason getReason() {
		return reason;
	}

	public void setReason(CcTransReason reason) {
		this.reason = reason;
	}

	public Calendar getDateAdded() {
		return dateAdded;
	}

	public void setDateAdded(Calendar dateAdded) {
		this.dateAdded = dateAdded;
	}

	public Calendar getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(Calendar lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	
}
