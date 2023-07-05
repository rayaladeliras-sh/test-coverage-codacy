package com.stubhub.domain.account.intf;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import org.codehaus.jackson.annotate.JsonProperty;

@XmlAccessorType(XmlAccessType.FIELD)
public class BuyerEntity {
	@XmlElement(name = "saleId", required = true)
	@JsonProperty("saleId")
	private Integer id;
	@XmlElement(name = "firstName", required = true)
	@JsonProperty("firstName")
	private String firstName;
	@XmlElement(name = "lastName", required = true)
	@JsonProperty("lastName")
	private String lastName;
	@XmlElement(name = "email", required = true)
	@JsonProperty("email")
	private String email;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}


}
