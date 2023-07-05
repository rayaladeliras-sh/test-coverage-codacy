package com.stubhub.domain.account.intf;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "csSaleDetailsRequest")
@JsonRootName(value = "csSaleDetailsRequest")
@XmlType(name = "", propOrder = { 
		"saleId", 
		"proxiedId", 
		"eventStartDate",
		"eventEndDate",
		"start",
		"row" 
})
public class CSSaleDetailsRequest {
	@XmlElement(name = "saleId", required = false)
	@JsonProperty("saleId")
	private String saleId;
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

	public String getSaleId() {
		return saleId;
	}
	public void setSaleId(String saleId) {
		this.saleId = saleId;
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
