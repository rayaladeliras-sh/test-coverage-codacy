package com.stubhub.domain.account.datamodel.entity;

public class Ticket {

	private Long statusId;

	private String statusDesc;

	private String faceValue;

	private Long ticketSeatId;

	private String section;

	private String row;

	private String seatNumber;

	public Long getStatusId() {
		return statusId;
	}

	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}

	public String getStatusDesc() {
		return statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}

	public String getFaceValue() {
		return faceValue;
	}

	public void setFaceValue(String faceValue) {
		this.faceValue = faceValue;
	}

	public Long getTicketSeatId() {
		return ticketSeatId;
	}

	public void setTicketSeatId(Long ticketSeatId) {
		this.ticketSeatId = ticketSeatId;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getRow() {
		return row;
	}

	public void setRow(String row) {
		this.row = row;
	}

	public String getSeatNumber() {
		return seatNumber;
	}

	public void setSeatNumber(String seatNumber) {
		this.seatNumber = seatNumber;
	}

}
