package com.stubhub.domain.account.intf;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;

import com.stubhub.domain.account.common.Response;

@XmlAccessorType(XmlAccessType.FIELD)
@JsonRootName(value = "emailTransaction")
@XmlRootElement(name = "emailTransaction")
@XmlType(name = "", propOrder = {
		"email" 
})
public class EmailLogsResponse extends Response {
	@XmlElement(name = "email", required = true)
	@JsonProperty("email")
	private List<EmailResponse> email;

	public List<EmailResponse> getEmail() {
		return email;
	}
	public void setEmail(List<EmailResponse> email) {
		this.email = email;
	}
}
