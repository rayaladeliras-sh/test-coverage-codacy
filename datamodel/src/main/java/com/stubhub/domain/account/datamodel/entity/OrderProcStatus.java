package com.stubhub.domain.account.datamodel.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

public class OrderProcStatus {

    protected Long statusCode;
    protected String statusDescription;
    protected Long subStatusCode;
    protected String subStatusDescription;
    protected String statusEffectiveDate;
	public Long getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(Long statusCode) {
		this.statusCode = statusCode;
	}
	public String getStatusDescription() {
		return statusDescription;
	}
	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
	}
	public Long getSubStatusCode() {
		return subStatusCode;
	}
	public void setSubStatusCode(Long subStatusCode) {
		this.subStatusCode = subStatusCode;
	}
	public String getSubStatusDescription() {
		return subStatusDescription;
	}
	public void setSubStatusDescription(String subStatusDescription) {
		this.subStatusDescription = subStatusDescription;
	}
	public String getStatusEffectiveDate() {
		return statusEffectiveDate;
	}
	public void setStatusEffectiveDate(String statusEffectiveDate) {
		this.statusEffectiveDate = statusEffectiveDate;
	}

}

