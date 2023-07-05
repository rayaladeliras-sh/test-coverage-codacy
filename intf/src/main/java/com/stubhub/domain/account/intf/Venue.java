package com.stubhub.domain.account.intf;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "venue")
@XmlType(name = "", propOrder = {
    "description","venueConfigId", "country", "city"
})
public class Venue {	
	@XmlElement(name="description", required = false)
	protected String description;
	
	@XmlElement(name="venueConfigId", required = false)
	private Long venueConfigId;
	
	@XmlElement(name="city", required = false)
	private String city;
	
	@XmlElement(name = "country", required = false)
	private String country;
	
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getVenueConfigId() {
		return venueConfigId;
	}

	public void setVenueConfigId(Long venueConfigId) {
		this.venueConfigId = venueConfigId;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
}
