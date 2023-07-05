package com.stubhub.domain.account.intf;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.stubhub.domain.account.common.Response;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "UpdatePaymentStatusResponse")
@XmlType(name = "", propOrder = {"payments"})
@JsonIgnoreProperties(ignoreUnknown = true)

public class UpdatePaymentStatusResponse extends Response {
	
	@XmlElement(name="payment")
	private List<SellerPayment> payments;

	public List<SellerPayment> getPayments() {
		return payments;
	}

	public void setPayments(List<SellerPayment> payments) {
		this.payments = payments;
	}
}
