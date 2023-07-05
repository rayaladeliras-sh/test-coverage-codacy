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
@JsonRootName(value = "listings")
@XmlRootElement(name = "listings")
@XmlType(name = "", propOrder = {
		"numFound","listings", "venueSummary", "genreSummary", "eventSummary"
		})
public class ListingsResponse extends Response {
	
	private long numFound;
	
	@XmlElement(name = "listing", required = true)
	@JsonProperty(value="listing")
	private List<ListingResponse> listings;

	private List<Summary> venueSummary;
	private List<Summary> genreSummary;
	private List<Summary> eventSummary;

	public long getNumFound() {
		return numFound;
	}
	
	public void setNumFound(long numFound) {
		this.numFound = numFound;
	}
	
	public List<ListingResponse> getListings() {
		return listings;
	}
	public void setListings(List<ListingResponse> listings) {
		this.listings = listings;
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
