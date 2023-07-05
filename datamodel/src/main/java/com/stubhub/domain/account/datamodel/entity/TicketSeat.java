package com.stubhub.domain.account.datamodel.entity;

import java.util.Calendar;

import javax.persistence.*;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "TICKET_SEAT")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert=true)
@NamedNativeQueries(value ={
		@NamedNativeQuery(name = "TicketSeat.updateSeatStatus", query = "" +
				"UPDATE TICKET_SEAT t " +
				"SET t.SEAT_STATUS_ID =:seatStatusId, " +
				"t.ORDER_PLACED_IND =:orderPlacedInd, " +
				"t.LAST_UPDATED_DATE =:lastUpdateDate, " +
				"t.LAST_UPDATED_BY =:lastUpdatedBy " +
				"WHERE t.TICKET_SEAT_ID =:ticketSeatId ", resultClass=TicketSeat.class)       
})
@NamedQueries(value = {
        @NamedQuery(name="TicketSeat.getSeatInfo",query="from TicketSeat ts WHERE ts.ticketId = :ticketId")
})
public class TicketSeat{

	@Id
	@Column(name = "TICKET_SEAT_ID")
	@SequenceGenerator(name="TICKET_SEAT_ID_SEQ", sequenceName="TICKET_SEAT_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TICKET_SEAT_ID_SEQ")
	private Long ticketSeatId;
	@Column(name = "TICKET_ID")
	private Long ticketId;
	@Column(name = "TIX_LIST_TYPE_ID")
	private Long tixListTypeId;
	@Column(name = "SECTION_NAME")
	private String sectionName;
	@Column(name = "ROW_NUMBER")
	private String rowNumber;
	@Column(name = "SEAT_NUMBER")
	private String seatNumber;
	@Column(name = "GENERAL_ADMISSION_IND")
	private Boolean generalAdmissionInd;
	@Column(name = "SEAT_DESC")
	private String seatDesc;
	@Column(name = "SEAT_STATUS_ID")
	private Long seatStatusId;
	@Column(name = "EXTERNAL_SEAT_ID")
	private String externalSeatId;
	@Type(type="com.stubhub.newplatform.persistence.adapter.UTCCalendarToTimestampAdapter")
	@Column(name = "CREATED_DATE")
	private Calendar createdDate;
	@Column(name = "CREATED_BY")
	private String createdBy;
	@Type(type="com.stubhub.newplatform.persistence.adapter.UTCCalendarToTimestampAdapter")
	@Column(name = "LAST_UPDATED_DATE")
	private Calendar lastUpdatedDate;
	@Column(name = "LAST_UPDATED_BY")
	private String lastUpdatedBy;
	@Column(name = "ORDER_PLACED_IND")
	private Boolean orderPlacedInd;
	
	public Long getTicketSeatId() {
		return ticketSeatId;
	}
	public void setTicketSeatId(Long ticketSeatId) {
		this.ticketSeatId = ticketSeatId;
	}
	public Long getTicketId() {
		return ticketId;
	}
	public void setTicketId(Long ticketId) {
		this.ticketId = ticketId;
	}
	public Long getTixListTypeId() {
		return tixListTypeId;
	}
	public void setTixListTypeId(Long tixListTypeId) {
		this.tixListTypeId = tixListTypeId;
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
	public Boolean getGeneralAdmissionInd() {
		return generalAdmissionInd;
	}
	public void setGeneralAdmissionInd(Boolean generalAdmissionInd) {
		this.generalAdmissionInd = generalAdmissionInd;
	}
	public String getSeatDesc() {
		return seatDesc;
	}
	public void setSeatDesc(String seatDesc) {
		this.seatDesc = seatDesc;
	}
	public Long getSeatStatusId() {
		return seatStatusId;
	}
	public void setSeatStatusId(Long seatStatusId) {
		this.seatStatusId = seatStatusId;
	}
	public String getExternalSeatId() {
		return externalSeatId;
	}
	public void setExternalSeatId(String externalSeatId) {
		this.externalSeatId = externalSeatId;
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
	public Boolean getOrderPlacedInd() {
		return orderPlacedInd;
	}
	public void setOrderPlacedInd(Boolean orderPlacedInd) {
		this.orderPlacedInd = orderPlacedInd;
	}
}
