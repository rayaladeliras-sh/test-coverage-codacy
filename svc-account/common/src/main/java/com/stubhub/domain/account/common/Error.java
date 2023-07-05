package com.stubhub.domain.account.common;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "type", "code", "message", "parameter" })
public class Error{

	@XmlElement(name = "errorType", required = true)
	private com.stubhub.domain.account.common.enums.ErrorType type;
	@XmlElement(name = "errorCode", required = true)
	private com.stubhub.domain.account.common.enums.ErrorCode code;
	@XmlElement(name = "errorMessage")
	private String message;
	@XmlElement(name = "errorParam")
	private String parameter;
	
	public Error(){
		super();
	}
	
	public Error(com.stubhub.domain.account.common.enums.ErrorType type, com.stubhub.domain.account.common.enums.ErrorCode code, String message,
			String parameter) {
		super();
		this.type = type;
		this.code = code;
		this.message = message;
		this.parameter = parameter;
	}
	
	
	public com.stubhub.domain.account.common.enums.ErrorType getType() {
		return type;
	}
	public void setType(com.stubhub.domain.account.common.enums.ErrorType type) {
		this.type = type;
	}
	public com.stubhub.domain.account.common.enums.ErrorCode getCode() {
		return code;
	}
	public void setCode(com.stubhub.domain.account.common.enums.ErrorCode code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getParameter() {
		return parameter;
	}
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

}
