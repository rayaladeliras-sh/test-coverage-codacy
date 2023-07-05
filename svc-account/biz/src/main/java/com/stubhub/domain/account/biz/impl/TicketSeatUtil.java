package com.stubhub.domain.account.biz.impl;

public class TicketSeatUtil{
	private String section;
    private Long venueConfigSectionId;
    private String row;
    private String seatNumber;
    private Long seatId;
    private Long ticketListTypeId;
    
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}
	public Long getVenueConfigSectionId() {
		return venueConfigSectionId;
	}
	public void setVenueConfigSectionId(Long venueConfigSectionId) {
		this.venueConfigSectionId = venueConfigSectionId;
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
	public Long getSeatId() {
		return seatId;
	}
	public void setSeatId(Long seatId) {
		this.seatId = seatId;
	}
	public Long getTicketListTypeId() {
		return ticketListTypeId;
	}
	public void setTicketListTypeId(Long ticketListTypeId) {
		this.ticketListTypeId = ticketListTypeId;
	}

}
