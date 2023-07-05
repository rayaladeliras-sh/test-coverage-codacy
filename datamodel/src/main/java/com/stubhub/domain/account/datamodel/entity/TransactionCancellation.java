package com.stubhub.domain.account.datamodel.entity;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "TRANSACTION_CANCELLATION")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert=true)
public class TransactionCancellation implements java.io.Serializable {

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name="TRANS_CANCEL_SEQ", sequenceName="TRANS_CANCEL_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TRANS_CANCEL_SEQ")
	private Long id;
	@Column(name = "REASON_ID")
	private Long reasonId;
	@Column(name = "TID")
	private Long tid;
	@Column(name = "EXTRA_INFO")
	private String extraInfo;
	@Column(name = "CANCELLED_BY")
	private String cancelledBy;
	@Type(type="com.stubhub.newplatform.persistence.adapter.UTCCalendarToTimestampAdapter")
	@Column(name = "DATE_ADDED")
	private Calendar dateAdded;
	@Column(name = "LAST_UPDATED_DATE")
	@Type(type="com.stubhub.newplatform.persistence.adapter.UTCCalendarToTimestampAdapter")
	private Calendar lastUpdatedDate;
	@Column(name = "LAST_UPDATED_BY")
	private String lastUpdatedBy;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getReasonId() {
		return reasonId;
	}
	public void setReasonId(Long reasonId) {
		this.reasonId = reasonId;
	}
	public Long getTid() {
		return tid;
	}
	public void setTid(Long tid) {
		this.tid = tid;
	}
	public String getExtraInfo() {
		return extraInfo;
	}
	public void setExtraInfo(String extraInfo) {
		this.extraInfo = extraInfo;
	}
	public String getCancelledBy() {
		return cancelledBy;
	}
	public void setCancelledBy(String cancelledBy) {
		this.cancelledBy = cancelledBy;
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
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
}
