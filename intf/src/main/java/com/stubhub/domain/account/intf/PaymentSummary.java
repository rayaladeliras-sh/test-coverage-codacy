package com.stubhub.domain.account.intf;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.stubhub.domain.account.common.Response;
import com.stubhub.newplatform.common.entity.Money;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "paymentSummary")
@XmlType(name = "", propOrder = {
		"currency",
		"totalAmount",
		"pendingAmount",
		"processingAmount",
		"availableAmount"
		})
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentSummary extends Response {

	@XmlElement(name = "currency", required = true)
	private String currency;
	
	@XmlElement(name = "totalAmount", required = true)
	private Money totalAmount;

	@XmlElement(name = "pendingAmount", required = true)
	private Money pendingAmount;

	@XmlElement(name = "processingAmount", required = true)
	private Money processingAmount;

	@XmlElement(name = "availableAmount", required = true)
	private Money availableAmount;



	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Money getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Money totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	public Money getPendingAmount() {
		return pendingAmount;
	}

	public void setPendingAmount(Money pendingAmount) {
		this.pendingAmount = pendingAmount;
	}


	public Money getProcessingAmount() {
		return processingAmount;
	}

	public void setProcessingAmount(Money processingAmount) {
        this.processingAmount = processingAmount;
	}

	public Money getAvailableAmount() {
		return availableAmount;
	}

	public void setAvailableAmount(Money availableAmount) {
		this.availableAmount = availableAmount;
	}


}
