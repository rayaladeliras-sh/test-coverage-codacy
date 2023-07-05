package com.stubhub.domain.account.intf;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.map.annotate.JsonRootName;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "payment")
@JsonRootName(value = "payment")
@XmlType(name = "", propOrder = { "payeeEmailId" })
public class UpdatePaymentRequest {

	@XmlElement(name = "payeeEmailId", required = true)
	private String payeeEmailId;

	public String getPayeeEmailId() {
		return payeeEmailId;
	}

	public void setPayeeEmailId(String payeeEmailId) {
		this.payeeEmailId = payeeEmailId;
	}

}
