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
@JsonRootName(value = "subs")
@XmlRootElement(name = "subs")
@XmlType(name = "", propOrder = {
		"subbedFlag", 
		"subbedOrderId" 
})
public class SubResponse extends Response {
	@XmlElement(name = "subbedFlag", required = false)
	@JsonProperty("subbedFlag")
	private Boolean subbedFlag;
	@XmlElement(name = "subbedOrderId", required = false)
	@JsonProperty("subbedOrderId")
	private String subbedOrderId;

	public Boolean getSubbedFlag() {
		return subbedFlag;
	}
	public void setSubbedFlag(Boolean subbedFlag) {
		this.subbedFlag = subbedFlag;
	}
	public String getSubbedOrderId() {
		return subbedOrderId;
	}
	public void setSubbedOrderId(String subbedOrderId) {
		this.subbedOrderId = subbedOrderId;
	}

}
