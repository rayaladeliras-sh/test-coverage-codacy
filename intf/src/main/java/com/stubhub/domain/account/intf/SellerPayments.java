package com.stubhub.domain.account.intf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.stubhub.domain.account.common.Response;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "payments")
@JsonIgnoreProperties(ignoreUnknown = true)

public class SellerPayments extends Response {
	
	@XmlElement(name="payment")
	private List<SellerPayment> payments;

	@XmlElement(name="numFound")
	private int numFound=0;
	
	@XmlElement(name="paymentsSummary")
	private List<PaymentSummary> paymentsSummary; 
    
    @XmlElement(name = "currencySummary")
    private List<CurrencySummary> currencySummary;
	
    public List<CurrencySummary> getCurrencySummary() {
        if (currencySummary == null) {
            return Collections.emptyList();
        }
        List<CurrencySummary> result = new ArrayList<CurrencySummary>();
        result.addAll(currencySummary);
        return result;
    }
    
    public void setCurrencySummary(List<CurrencySummary> currencySummary) {
        if (currencySummary == null || currencySummary.isEmpty()) { return; }
        if (this.currencySummary == null) {
            this.currencySummary = new ArrayList<CurrencySummary>();
        }
        this.currencySummary.clear();
        this.currencySummary.addAll(currencySummary);
    }
    
    public List<SellerPayment> getPayments() {
        return payments;
	}

	public void setPayments(List<SellerPayment> payments) {
		this.payments = payments;
	}

	public int getNumFound() {
		return numFound;
	}

	public void setNumFound(int numFound) {
		this.numFound = numFound;
	}

	public List<PaymentSummary> getPaymentsSummary() {
		return paymentsSummary;
	}

	public void setPaymentsSummary(List<PaymentSummary> paymentsSummary) {
		this.paymentsSummary = paymentsSummary;
	}

	
	

}
