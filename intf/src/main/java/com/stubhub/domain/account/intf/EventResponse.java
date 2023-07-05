package com.stubhub.domain.account.intf;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;

import com.stubhub.domain.account.common.Response;

@XmlAccessorType(XmlAccessType.FIELD)
@JsonRootName(value = "event")
@XmlRootElement(name = "event")
@XmlType(name = "", propOrder = {
		"eventId", 
		"eventDescription", 
		"eventStatus", 
		"eventDateUTC",
		"eventDateLocal",
		"hideEventDate",
		"venueDescription" 
		})		
public class EventResponse extends Response {
	@XmlElement(name = "eventId", required = false)
	@JsonProperty("eventId")
	private String eventId;
	@XmlElement(name = "eventDescription", required = false)
	@JsonProperty("eventDescription")
	private String eventDescription;
	@XmlElement(name = "eventStatus", required = false)
	@JsonProperty("eventStatus")
	private String eventStatus;
	@XmlElement(name = "eventDateUTC", required = false)
	@JsonProperty("eventDateUTC")
	private String eventDateUTC;
	@XmlElement(name = "eventDateLocal", required = false)
	@JsonProperty("eventDateLocal")
	private String eventDateLocal;
	@XmlElement(name = "venueDescription", required = false)
	@JsonProperty("venueDescription")
	private String venueDescription;
	@XmlElement(name = "hideEventDate", required = false)
	@JsonProperty("hideEventDate")
	private boolean hideEventDate;
	
	public String getEventId() {
		return eventId;
	}
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	public String getEventDescription() {
		return eventDescription;
	}
	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}
	public String getEventStatus() {
		return eventStatus;
	}
	public void setEventStatus(String eventStatus) {
		this.eventStatus = eventStatus;
	}
	public String getEventDateUTC() {
		return eventDateUTC;
	}
	public void setEventDateUTC(String eventDateUTC) {
		this.eventDateUTC = eventDateUTC;
	}
	public String getVenueDescription() {
		return venueDescription;
	}
	public void setVenueDescription(String venueDescription) {
		this.venueDescription = venueDescription;
	}
	public String getEventDateLocal() {
		return eventDateLocal;
	}
	public void setEventDateLocal(String eventDateLocal) {
		this.eventDateLocal = eventDateLocal;
	}
	public boolean getHideEventDate() {
		return hideEventDate;
	}
	public void setHideEventDate(boolean hideEventDate) {
		this.hideEventDate = hideEventDate;
	}

}
