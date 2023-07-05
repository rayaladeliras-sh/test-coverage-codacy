package com.stubhub.domain.account.intf;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "csOrderDetailsRequest")
@JsonRootName(value = "csOrderDetailsRequest")
@XmlType(name = "", propOrder = { 
		"orderId", 
		"proxiedId", 
		"eventStartDate",
		"eventEndDate",
		"start",
		"row" 
})
public class CSOrderDetailsRequest {
	@XmlElement(name = "orderId", required = false)
	@JsonProperty("orderId")
	private String orderId;
	@XmlElement(name = "proxiedId", required = false)
	@JsonProperty("proxiedId")
	private String proxiedId;
	@XmlElement(name = "eventStartDate", required = false)
	@JsonProperty("eventStartDate")
	private String eventStartDate;
	@XmlElement(name = "eventEndDate", required = false)
	@JsonProperty("eventEndDate")
	private String eventEndDate;
	@XmlElement(name = "start", required = false)
	@JsonProperty("start")
	private String start;
	@XmlElement(name = "row", required = false)
	@JsonProperty("row")
	private String row;

	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getProxiedId() {
		return proxiedId;
	}
	public void setProxiedId(String proxiedId) {
		this.proxiedId = proxiedId;
	}
	public String getRow() {
		return row;
	}
	public void setRow(String row) {
		this.row = row;
	}
	public String getEventStartDate() {
		return eventStartDate;
	}
	public void setEventStartDate(String eventStartDate) {
		this.eventStartDate = eventStartDate;
	}
	public String getEventEndDate() {
		return eventEndDate;
	}
	public void setEventEndDate(String eventEndDate) {
		this.eventEndDate = eventEndDate;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	
}
