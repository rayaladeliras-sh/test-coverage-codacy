package com.stubhub.integration.partnerorder.entity;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

@XmlAccessorType(XmlAccessType.FIELD)
public class Response {
	@XmlElementWrapper(name = "errors", required = false)
	@XmlElement(name = "error", required = false)
    private List<Error> errors;

    public List<Error> getErrors() {
		return errors;
	}

    public void setErrors(List<Error> errors) {
		this.errors = errors;
	}

	@Override
	public String toString() {
		return "Response [errors=" + errors + "]";
	}
}
