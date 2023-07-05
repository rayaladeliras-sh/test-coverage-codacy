package com.stubhub.domain.account.common.enums;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ticketTrait")
@XmlType(name = "", propOrder = {
	"id",
	"name",
	"type"
})

public class TicketTrait implements Serializable {
	private static final long serialVersionUID = -6988514715457713204L;

	@XmlElement(name = "id", required = true)
    private String id;
	
	@XmlElement(name = "name", required = true)
    private String name;
	
	@XmlElement(name = "type", required = true)
    private String type;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

}
