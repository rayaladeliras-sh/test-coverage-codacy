package com.stubhub.domain.account.datamodel.entity;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

/*
 * This needs to be removed once we have these items available through inventory API
 */

@Entity
@Table(name = "LISTING_SEAT_TRAIT")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
@NamedNativeQueries(value ={
		@NamedNativeQuery(name = "ListingSeatTrait.getByListingId", query = "" +
				"SELECT * FROM LISTING_SEAT_TRAIT WHERE ACTIVE=1 AND TICKET_ID=:ticketId ", 
				resultClass=ListingSeatTrait.class)})
public class ListingSeatTrait{
	@Id
	@Column(name = "LISTING_SEAT_TRAIT_ID")
	@SequenceGenerator(name="LISTING_SEAT_TRAIT_SEQ", sequenceName="LISTING_SEAT_TRAIT_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LISTING_SEAT_TRAIT_SEQ")
	private Long id;
	@Column(name = "TICKET_ID")
	private Long ticketId;
	@Column(name = "SUPPLEMENT_SEAT_TRAIT_ID")
	private Long supplementSeatTraitId;
	@Column(name = "ACTIVE")
	private Boolean active;
	@Column(name = "EDITABLE_IND")
	private Boolean editableInd; 
	@Column(name = "SELLER_SPECIFIED_IND")
	private Boolean sellerSpecifiedInd;
	@Column(name = "EXT_SYSTEM_SPECIFIED_IND")
	private Boolean extSystemSpecifiedInd; 
	@Column(name = "RECORD_VERSION_NUMBER")
	private  Long recordVersionNumber;
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
	public Long getTicketId() {
		return ticketId;
	}
	public void setTicketId(Long ticketId) {
		this.ticketId = ticketId;
	}
	public Long getSupplementSeatTraitId() {
		return supplementSeatTraitId;
	}
	public void setSupplementSeatTraitId(Long supplementSeatTraitId) {
		this.supplementSeatTraitId = supplementSeatTraitId;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	public Boolean getEditableInd() {
		return editableInd;
	}
	public void setEditableInd(Boolean editableInd) {
		this.editableInd = editableInd;
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
	public Long getRecordVersionNumber() {
		return recordVersionNumber;
	}
	public void setRecordVersionNumber(Long recordVersionNumber) {
		this.recordVersionNumber = recordVersionNumber;
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
