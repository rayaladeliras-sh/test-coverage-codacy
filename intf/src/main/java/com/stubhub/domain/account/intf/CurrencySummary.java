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
@XmlRootElement(name = "currencySummary")
@XmlType(name = "", propOrder = {
		"currency",
		"totalAmount",
        "count"
		})
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrencySummary extends Response {

	@XmlElement(name = "currency", required = true)
	private String currency;
	
    @XmlElement(name = "totalAmount", required = false)
	private Money totalAmount;

    @XmlElement(name = "count", required = false)
    private Integer count;

    public Integer getCount() {
        return count;
    }
    
    public void setCount(Integer count) {
        this.count = count;
    }
    
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

}
