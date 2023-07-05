package com.stubhub.domain.account.intf;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.stubhub.domain.account.common.Response;
import com.stubhub.newplatform.common.entity.Money;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "salesSummary")
@XmlType(name = "", propOrder = {
		"minPricePerTicket",
		"maxPricePerTicket",
		"medianPricePerTicket",
		"averagePricePerTicket"
		})
public class SalesHistoryResponse extends Response {
	
	@XmlElement(name = "minPricePerTicket", required = false)
	private Money minPricePerTicket;
	
	@XmlElement(name = "maxPricePerTicket", required = false)
	private Money maxPricePerTicket;
	
	@XmlElement(name = "medianPricePerTicket", required = false)
	private Money medianPricePerTicket;
	
	@XmlElement(name = "averagePricePerTicket", required = false)
	private Money averagePricePerTicket;

	public Money getMinPricePerTicket() {
		return minPricePerTicket;
	}

	public void setMinPricePerTicket(Money minPricePerTicket) {
		this.minPricePerTicket = minPricePerTicket;
	}

	public Money getMaxPricePerTicket() {
		return maxPricePerTicket;
	}

	public void setMaxPricePerTicket(Money maxPricePerTicket) {
		this.maxPricePerTicket = maxPricePerTicket;
	}

	public Money getMedianPricePerTicket() {
		return medianPricePerTicket;
	}

	public void setMedianPricePerTicket(Money medianPricePerTicket) {
		this.medianPricePerTicket = medianPricePerTicket;
	}

	public Money getAveragePricePerTicket() {
		return averagePricePerTicket;
	}

	public void setAveragePricePerTicket(Money averagePricePerTicket) {
		this.averagePricePerTicket = averagePricePerTicket;
	}
	
}


