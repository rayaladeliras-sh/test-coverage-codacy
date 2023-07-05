package com.stubhub.integration.partnerorder.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "zip", "street","city", "state", "country", "phone" })
@XmlRootElement(name = "FulfillmentContact")
public class UserContact {
	@XmlElement(name = "Street")
	public String street;
	
	@XmlElement(name = "City")
	public String city;
	
	@XmlElement(name = "State")
	public String state;
	
	@XmlElement(name = "Zip")
	public String zip;
	
	@XmlElement(name = "Phone")
	public String phone;
	
	@XmlElement(name = "Country")
	public String country;

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

}
