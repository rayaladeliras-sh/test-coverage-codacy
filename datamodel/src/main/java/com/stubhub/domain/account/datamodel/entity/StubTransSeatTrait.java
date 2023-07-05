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
@Table(name = "STUB_TRANS_SEAT_TRAIT")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class StubTransSeatTrait{
	@Id
	@Column(name = "STUB_TRANS_SEAT_TRAIT_ID")
	@SequenceGenerator(name="STUB_TRANS_SEAT_TRAIT_SEQ", sequenceName="STUB_TRANS_SEAT_TRAIT_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="STUB_TRANS_SEAT_TRAIT_SEQ")
	private Long id;
	@Column(name = "TID")
	private Long orderId;
	@Column(name = "SUPPLEMENT_SEAT_TRAIT_ID")
	private Long supplementSeatTraitId;
	@Column(name = "SELLER_SPECIFIED_IND")
	private Boolean sellerSpecifiedInd;
	@Column(name = "EXT_SYSTEM_SPECIFIED_IND")
	private Boolean extSystemSpecifiedInd;
	@Column(name = "CREATED_DATE")
	@Type(type="com.stubhub.newplatform.persistence.adapter.UTCCalendarToTimestampAdapter")
	private Calendar createdDate;
	@Column(name = "CREATED_BY")
	private String createdBy;
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
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public Long getSupplementSeatTraitId() {
		return supplementSeatTraitId;
	}
	public void setSupplementSeatTraitId(Long supplementSeatTraitId) {
		this.supplementSeatTraitId = supplementSeatTraitId;
	}
	public Boolean getSellerSpecifiedInd() {
		return sellerSpecifiedInd;
	}
	public void setSellerSpecifiedInd(Boolean sellerSpecifiedInd) {
		this.sellerSpecifiedInd = sellerSpecifiedInd;
	}
	public Boolean getExtSystemSpecifiedInd() {
		return extSystemSpecifiedInd;
	}
	public void setExtSystemSpecifiedInd(Boolean extSystemSpecifiedInd) {
		this.extSystemSpecifiedInd = extSystemSpecifiedInd;
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
