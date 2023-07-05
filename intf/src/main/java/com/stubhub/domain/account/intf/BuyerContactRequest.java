package com.stubhub.domain.account.intf;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.map.annotate.JsonRootName;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "buyerContactId")
@JsonRootName(value = "buyerContactId")
@XmlType(name = "", propOrder = { "contactId","csrRepresentative" })
public class BuyerContactRequest {

	@XmlElement(name = "contactId", required = true)
	private String contactId;
	
	@XmlElement(name = "csrRepresentative", required = false)
	private String csrRepresentative;


	public String getContactId() {
		return contactId;
	}

	public void setContactId(String contactId) {
		this.contactId = contactId;
	}

	public String getCsrRepresentative() {
		return csrRepresentative;
	}

	public void setCsrRepresentative(String csrRepresentative) {
		this.csrRepresentative = csrRepresentative;
	}
	
}
