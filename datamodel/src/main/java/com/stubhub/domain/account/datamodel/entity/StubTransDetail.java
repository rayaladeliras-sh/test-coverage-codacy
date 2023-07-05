package com.stubhub.domain.account.datamodel.entity;

import java.util.Calendar;

import javax.persistence.*;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "STUB_TRANS_DETAIL")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
@NamedQueries(value = {
        @NamedQuery(name="StubTransDetail.getSeatInfo",query="from StubTransDetail sd WHERE sd.tid = :tid")
})
public class StubTransDetail{

	@Id
	@Column(name = "STUB_TRANS_DTL_ID")
	@SequenceGenerator(name="STUB_TRANS_DETAIL_ID_SEQ", sequenceName="STUB_TRANS_DETAIL_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="STUB_TRANS_DETAIL_ID_SEQ")
	private Long stubTransDtlId;
	@Column(name = "TID")
	private Long tid;
	@Column(name = "TICKET_SEAT_ID")
	private Long ticketSeatId;
	@Column(name = "SECTION_NAME")
	private String sectionName;
	@Column(name = "ROW_NUMBER")
	private String rowNumber;
	@Column(name = "SEAT_NUMBER")
	private String seatNumber;
	@Column(name = "TIX_LIST_TYPE_ID")
	private Long ticketListTypeId;
	@Column(name = "GENERAL_ADMISSION_IND")
	private Long generalAdmissionIndicator;
	@Column(name = "CONFIRM_SECTION_NAME")
	private String confirmedSectionName;
	@Column(name = "CONFIRM_ROW_NUMBER")
	private String confirmedRowNumber;
	@Column(name = "CONFIRM_SEAT_NUMBER")
	private String confirmedSeatNumber;
	@Column(name = "CONFIRM_TIX_LIST_TYPE_ID")
	private Long confirmedTicketListTypeId;
	@Column(name = "CONFIRM_GENERAL_ADMISSION_IND")
	private Long confirmedGeneralAdmissionIndicator;
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
	@Column(name = "TIX_CONT_CD_CANCELLED_IND")
	private Boolean barcodeCancelled;
	
	public Long getStubTransDtlId() {
		return stubTransDtlId;
	}
	public void setStubTransDtlId(Long stubTransDtlId) {
		this.stubTransDtlId = stubTransDtlId;
	}
	public Long getTicketSeatId() {
		return ticketSeatId;
	}
	public void setTicketSeatId(Long ticketSeatId) {
		this.ticketSeatId = ticketSeatId;
	}
	public Long getTid() {
		return tid;
	}
	public void setTid(Long tid) {
		this.tid = tid;
	}
	public String getSectionName() {
		return sectionName;
	}
	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}
	public String getRowNumber() {
		return rowNumber;
	}
	public void setRowNumber(String rowNumber) {
		this.rowNumber = rowNumber;
	}
	public String getSeatNumber() {
		return seatNumber;
	}
	public void setSeatNumber(String seatNumber) {
		this.seatNumber = seatNumber;
	}
	public Long getTicketListTypeId() {
		return ticketListTypeId;
	}
	public void setTicketListTypeId(Long ticketListTypeId) {
		this.ticketListTypeId = ticketListTypeId;
	}
	public Long getGeneralAdmissionIndicator() {
		return generalAdmissionIndicator;
	}
	public void setGeneralAdmissionIndicator(Long generalAdmissionIndicator) {
		this.generalAdmissionIndicator = generalAdmissionIndicator;
	}
	public String getConfirmedSectionName() {
		return confirmedSectionName;
	}
	public void setConfirmedSectionName(String confirmedSectionName) {
		this.confirmedSectionName = confirmedSectionName;
	}
	public String getConfirmedRowNumber() {
		return confirmedRowNumber;
	}
	public void setConfirmedRowNumber(String confirmedRowNumber) {
		this.confirmedRowNumber = confirmedRowNumber;
	}
	public String getConfirmedSeatNumber() {
		return confirmedSeatNumber;
	}
	public void setConfirmedSeatNumber(String confirmedSeatNumber) {
		this.confirmedSeatNumber = confirmedSeatNumber;
	}
	public Long getConfirmedTicketListTypeId() {
		return confirmedTicketListTypeId;
	}
	public void setConfirmedTicketListTypeId(Long confirmedTicketListTypeId) {
		this.confirmedTicketListTypeId = confirmedTicketListTypeId;
	}
	public Long getConfirmedGeneralAdmissionIndicator() {
		return confirmedGeneralAdmissionIndicator;
	}
	public void setConfirmedGeneralAdmissionIndicator(
			Long confirmedGeneralAdmissionIndicator) {
		this.confirmedGeneralAdmissionIndicator = confirmedGeneralAdmissionIndicator;
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
	public Boolean getBarcodeCancelled() {
		return barcodeCancelled;
	}
	public void setBarcodeCancelled(Boolean barcodeCancelled) {
		this.barcodeCancelled = barcodeCancelled;
	}	
}
