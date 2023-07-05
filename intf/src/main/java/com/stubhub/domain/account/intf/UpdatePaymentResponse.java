package com.stubhub.domain.account.intf;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonRootName;

import com.stubhub.domain.account.common.Response;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "payment")
@JsonRootName(value = "payment")
@XmlType(name = "", propOrder = {"id","payeeEmailId"})
@JsonIgnoreProperties(ignoreUnknown = true)

public class UpdatePaymentResponse extends Response {
	
	@XmlElement(name="id")
	private Long id;
	@XmlElement(name = "payeeEmailId")
	private String payeeEmailId;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPayeeEmailId() {
		return payeeEmailId;
	}
	public void setPayeeEmailId(String payeeEmailId) {
		this.payeeEmailId = payeeEmailId;
	}

}
