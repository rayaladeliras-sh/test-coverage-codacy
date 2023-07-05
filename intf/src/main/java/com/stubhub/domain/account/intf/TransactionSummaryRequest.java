package com.stubhub.domain.account.intf;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "summaryRequest")
@JsonRootName(value = "summaryRequest")
@JsonIgnoreProperties(ignoreUnknown = true)
@XmlType(name = "", propOrder = { 
		"proxiedId",
		"buyerFlip",
		"currencyCode",
		"fullSummaryDetails"
})
public class TransactionSummaryRequest {
	@XmlElement(name = "proxiedId", required = true)
	@JsonProperty("proxiedId")
	private String proxiedId;
	@XmlElement(name = "buyerFlip", required = false)
	@JsonProperty("buyerFlip")
	private String buyerFlip;
	@XmlElement(name = "currencyCode", required = false)
	@JsonProperty("currencyCode")
	private String currencyCode;
	
	@XmlElement(name = "fullSummaryDetails", required = false)
	@JsonProperty("fullSummaryDetails")
	private boolean fullSummaryDetails;

	public String getProxiedId() {
		return proxiedId;
	}
	public void setProxiedId(String proxiedId) {
		this.proxiedId = proxiedId;
	}
	public void setBuyerFlip(String buyerFlip) {
		this.buyerFlip = buyerFlip;
	}
	public String getBuyerFlip() {
		return buyerFlip;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public boolean getFullSummaryDetails() {
		return fullSummaryDetails;
	}
	public void setFullSummaryDetails(boolean fullSummaryDetails) {
		this.fullSummaryDetails = fullSummaryDetails;
	}
}
