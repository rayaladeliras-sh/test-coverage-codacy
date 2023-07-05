package com.stubhub.domain.account.intf;

import java.sql.Clob;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;

import com.stubhub.domain.account.common.Response;

@XmlAccessorType(XmlAccessType.FIELD)
@JsonRootName(value = "email")
@XmlRootElement(name = "email")
@XmlType(name = "", propOrder = {
		"emailId", 
		"userId", 
		"tId", 
		"buyerOrderId", 
		"addressFrom", 
		"addressTo",
		"addressCc",
		"addressBcc",
		"dateAdded",
		"dateSent",
		"subject",
		"format",
		"body"
})
public class EmailResponse extends Response {

	@XmlElement(name = "emailId", required = false)
	@JsonProperty("emailId")
	private Long emailId;
	@XmlElement(name = "userId", required = false)
	@JsonProperty("userId")
	private String userId;
	@XmlElement(name = "tId", required = false)
	@JsonProperty("tId")
	private String tId;
	@XmlElement(name = "buyerOrderId", required = false)
	@JsonProperty("buyerOrderId")
	private String buyerOrderId;
	@XmlElement(name = "addressFrom", required = false)
	@JsonProperty("addressFrom")
	private String addressFrom;
	@XmlElement(name = "addressTo", required = false)
	@JsonProperty("addressTo")
	private String addressTo;
	@XmlElement(name = "addressCc", required = false)
	@JsonProperty("addressCc")
	private String addressCc;
	@XmlElement(name = "addressBcc", required = false)
	@JsonProperty("addressBcc")
	private String addressBcc;
	@XmlElement(name = "dateAdded", required = false)
	@JsonProperty("dateAdded")
	private String dateAdded;
	@XmlElement(name = "dateSent", required = false)
	@JsonProperty("dateSent")
	private String dateSent;
	@XmlElement(name = "subject", required = false)
	@JsonProperty("subject")
	private String subject;
	@XmlElement(name = "format", required = false)
	@JsonProperty("format")
	private String format;
	@XmlElement(name = "body", required = false)
	@JsonProperty("body")
	private String body;
	
	public Long getEmailId() {
		return emailId;
	}
	public void setEmailId(Long emailId) {
		this.emailId = emailId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String gettId() {
		return tId;
	}
	public void settId(String tId) {
		this.tId = tId;
	}
	
	public String getbuyerOrderId() {
		return buyerOrderId;
	}
	public void setbuyerOrderId(String buyerOrderId) {
		this.buyerOrderId = buyerOrderId;
	}
	
	public String getAddressFrom() {
		return addressFrom;
	}
	public void setAddressFrom(String addressFrom) {
		this.addressFrom = addressFrom;
	}
	public String getAddressTo() {
		return addressTo;
	}
	public void setAddressTo(String addressTo) {
		this.addressTo = addressTo;
	}
	public String getAddressCc() {
		return addressCc;
	}
	public void setAddressCc(String addressCc) {
		this.addressCc = addressCc;
	}
	public String getAddressBcc() {
		return addressBcc;
	}
	public void setAddressBcc(String addressBcc) {
		this.addressBcc = addressBcc;
	}
	public String getDateAdded() {
		return dateAdded;
	}
	public void setDateAdded(String dateAdded) {
		this.dateAdded = dateAdded;
	}
	public String getDateSent() {
		return dateSent;
	}
	public void setDateSent(String dateSent) {
		this.dateSent = dateSent;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
}
