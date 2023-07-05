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
@XmlRootElement(name = "sub")
@JsonRootName(value = "sub")
@XmlType(name = "", propOrder = {
		"newOrderId"		
		})
public class SubstitutionResponse extends Response{
    @XmlElement(name = "newOrderId")
    @JsonProperty("newOrderId")
    protected Long newOrderId;

	public Long getNewOrderId() {
		return newOrderId;
	}

	public void setNewOrderId(Long newOrderId) {
		this.newOrderId = newOrderId;
	}
}
