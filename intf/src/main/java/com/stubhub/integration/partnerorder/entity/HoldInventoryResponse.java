package com.stubhub.integration.partnerorder.entity;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "id", "endTime", "tickets"})

@XmlRootElement(name = "ReserveResponse")
public class HoldInventoryResponse {
	@XmlElement(name = "Id", required = false)
	private String id;

	@XmlElement(name = "EndTime", required = false)
	private String endTime;

	@XmlElementWrapper(name = "Tickets", required = true)
	@XmlElement(name = "Ticket", required = true)
	private List<PartnerOrderResponseTicket> tickets;
	
	@XmlTransient
	private int responseStatusCode;

	@XmlTransient
	private Boolean deleteTicketFlag;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public List<PartnerOrderResponseTicket> getTickets() {
		return tickets;
	}

	public void setTickets(List<PartnerOrderResponseTicket> tickets) {
		this.tickets = tickets;
	}

	public int getResponseStatusCode() {
		return responseStatusCode;
	}

	public void setResponseStatusCode(int responseStatusCode) {
		this.responseStatusCode = responseStatusCode;
	}
	
	public Boolean getDeleteTicketFlag() {
		return deleteTicketFlag;
	}

	public void setDeleteTicketFlag(Boolean deleteTicketFlag) {
		this.deleteTicketFlag = deleteTicketFlag;
	}
}
