package com.stubhub.domain.account.intf;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;

import com.stubhub.domain.account.common.Response;

@XmlAccessorType(XmlAccessType.FIELD)
@JsonRootName(value = "sales")
@XmlRootElement(name = "sales")
@XmlType(name = "", propOrder = {
		"numFound", "sales", "venueSummary", "genreSummary", "eventSummary"
		})
public class SalesResponse extends Response {
	private long numFound;

	@XmlElement(name = "sale", required = true)
	@JsonProperty(value="sale")
	private List<SaleResponse> sales;

	private List<Summary> venueSummary;
	private List<Summary> genreSummary;
	private List<Summary> eventSummary;
	
	public long getNumFound() {
		return numFound;
	}
	
	public void setNumFound(long numFound) {
		this.numFound = numFound;
	}

	public List<SaleResponse> getSales() {
		return sales;
	}

	public void setSales(List<SaleResponse> sales) {
		this.sales = sales;
	}

	public List<Summary> getVenueSummary() {
		return venueSummary;
	}

	public void setVenueSummary(List<Summary> venueSummary) {
		this.venueSummary = venueSummary;
	}

	public List<Summary> getGenreSummary() {
		return genreSummary;
	}

	public void setGenreSummary(List<Summary> genreSummary) {
		this.genreSummary = genreSummary;
	}

	public List<Summary> getEventSummary() {
		return eventSummary;
	}

	public void setEventSummary(List<Summary> eventSummary) {
		this.eventSummary = eventSummary;
	}
}
