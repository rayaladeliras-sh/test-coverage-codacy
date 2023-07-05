package com.stubhub.integration.partnerorder.entity;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.StringUtils;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "id", "price","statusId", "statusDesc", "faceValue",
		"ticketSeatId", "section", "row", "seatNumber" })
@XmlRootElement(name = "Seat")
public class PartnerOrderTicket{
	
	@XmlElement(name = "Id", required = true)
	private String id;
	
	@XmlElement(name = "Price", required = true)
	private BigDecimal price;
	
	@XmlElement(name = "StatusId", required = true)
	private Long statusId;

	@XmlElement(name = "StatusDesc", required = true)
	private String statusDesc;

	@XmlElement(name = "FaceValue", required = true)
	private String faceValue;

	@XmlElement(name = "TicketSeatId", required = true)
	private Long ticketSeatId;

	@XmlElement(name = "Section", required = true)
	private String section;

	@XmlElement(name = "Row")
	private String row;

	@XmlElement(name = "SeatNumber")
	private String seatNumber;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public BigDecimal getPrice() {
		return price;
	}
	
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
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
	
	/**
	 * If ticketSeat equals ignore case 'General Admission', set seat with 'GA'.
	 * 
	 * @param ticketSeat
	 * @param seatNumber
	 */
	public void setSeatNumberGA(String ticketSeat, String seatNumber) {
		if ("General Admission".equalsIgnoreCase(ticketSeat) && StringUtils.isBlank(seatNumber)) {
			this.seatNumber = "GA";
		} else {
			this.seatNumber = seatNumber;
		}
	}
	
	@Override
	public String toString() {
		return "Ticket [id="+id+", price=" + price + ", statusId=" + statusId + ", statusDesc=" + statusDesc
				+ ", faceValue=" + faceValue + ", ticketSeatId=" + ticketSeatId
				+ ", section=" + section + ", row=" + row + ", seatNumber="
				+ seatNumber + "]";
	}

}




	


	
