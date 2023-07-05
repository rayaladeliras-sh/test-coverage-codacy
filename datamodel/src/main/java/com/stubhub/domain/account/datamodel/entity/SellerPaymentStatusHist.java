package com.stubhub.domain.account.datamodel.entity;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "SELLER_PAYMENT_STATUS_HIST")
@NamedQueries(value = {
		@NamedQuery(name= "SellerPaymentStatusHist.getSellerPaymentStatusHist", query="FROM SellerPaymentStatusHist WHERE pid=:sellerPaymentId and seller_payment_status_id = 22")
})
@NamedNativeQueries(value ={
		@NamedNativeQuery(name = "SellerPaymentStatusHist.updateSellerPmtStatusEndDate", query = "update seller_payment_status_hist set seller_payment_status_end_date = :end_date, last_updated_by = :last_updated_by, last_updated_date = sysdate where pid = :pid and seller_payment_status_end_date is null ", resultClass=SellerPaymentStatusHist.class)
		})
public class SellerPaymentStatusHist implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4918552274138052499L;

	@Id
	@Column(name = "SELLER_PAYMENT_STATUS_HIST_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SELLER_PMT_STATUS_HIST_ID_SEQ")
	@SequenceGenerator(name = "SELLER_PMT_STATUS_HIST_ID_SEQ", sequenceName = "SELLER_PMT_STATUS_HIST_ID_SEQ", allocationSize = 1)
	private Long sellerPaymentStatusHistId;
	
	@Column(name = "PID")
	private Long sellerPaymentId;
	
	@Column(name = "SELLER_PAYMENT_STATUS_EFF_DATE")
	@Type(type="com.stubhub.newplatform.persistence.adapter.UTCCalendarToTimestampAdapter")
	private Calendar sellerPaymentStatusEffDate;

	@Column(name = "GP_MESSAGE_ID")
	private String gpMessageId;

	@Column(name = "SELLER_PAYMENT_STATUS_ID")
	private Long sellerPaymentStatusId;
	
	@Column(name = "ACKNOWLEDGEMENT_IND")
	private Long acknowledgementInd;

	@Column(name = "SELLER_PAYMENT_STATUS_END_DATE")
	@Type(type="com.stubhub.newplatform.persistence.adapter.UTCCalendarToTimestampAdapter")
	private Calendar sellerPaymentStatusEndDate;

	@Column(name = "CREATED_DATE")
	@Type(type="com.stubhub.newplatform.persistence.adapter.UTCCalendarToTimestampAdapter")
	private Calendar createdDate;
	
	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "LAST_UPDATED_DATE")
	@Type(type="com.stubhub.newplatform.persistence.adapter.UTCCalendarToTimestampAdapter")
	private Calendar lastUpdated;

	@Column(name = "LAST_UPDATED_BY")
	private String lastUpdatedBy;

	@Column(name = "SELLER_PAYMENT_DETAIL_STATUS")
	private String sellerPaymentDetailStatus;
	
	public Long getSellerPaymentStatusHistId() {
		return sellerPaymentStatusHistId;
	}

	public void setSellerPaymentStatusHistId(Long sellerPaymentStatusHistId) {
		this.sellerPaymentStatusHistId = sellerPaymentStatusHistId;
	}

	public Long getSellerPaymentId() {
		return sellerPaymentId;
	}

	public void setSellerPaymentId(Long sellerPaymentId) {
		this.sellerPaymentId = sellerPaymentId;
	}

	public Calendar getSellerPaymentStatusEffDate() {
		return sellerPaymentStatusEffDate;
	}

	public void setSellerPaymentStatusEffDate(Calendar sellerPaymentStatusEffDate) {
		this.sellerPaymentStatusEffDate = sellerPaymentStatusEffDate;
	}

	public String getGpMessageId() {
		return gpMessageId;
	}

	public void setGpMessageId(String gpMessageId) {
		this.gpMessageId = gpMessageId;
	}

	public Calendar getSellerPaymentStatusEndDate() {
		return sellerPaymentStatusEndDate;
	}

	public void setSellerPaymentStatusEndDate(Calendar sellerPaymentStatusEndDate) {
		this.sellerPaymentStatusEndDate = sellerPaymentStatusEndDate;
	}

	public Calendar getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Calendar createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Calendar getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Calendar lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public String getSellerPaymentDetailStatus() {
		return sellerPaymentDetailStatus;
	}

	public void setSellerPaymentDetailStatus(String sellerPaymentDetailStatus) {
		this.sellerPaymentDetailStatus = sellerPaymentDetailStatus;
	}

	public Long getSellerPaymentStatusId() {
		return sellerPaymentStatusId;
	}

	public void setSellerPaymentStatusId(Long sellerPaymentStatusId) {
		this.sellerPaymentStatusId = sellerPaymentStatusId;
	}

	public Long getAcknowledgementInd() {
		return acknowledgementInd;
	}

	public void setAcknowledgementInd(Long acknowledgementInd) {
		this.acknowledgementInd = acknowledgementInd;
	}
}
