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
						"salesFound",	
						"sale" 
						})
public class CSSalesResponse extends Response {

	@XmlElement(name = "salesFound", required = true)
	@JsonProperty("salesFound")
	private int salesFound;
	@XmlElement(name = "sale", required = true)
	@JsonProperty("sale")
	private List<CSSaleDetailsResponse> sale;
	
	public int getSalesFound() {
		return salesFound;
	}
	public void setSalesFound(int salesFound) {
		this.salesFound = salesFound;
	}
	public List<CSSaleDetailsResponse> getSale() {
		return sale;
	}
	public void setSale(List<CSSaleDetailsResponse> sale) {
		this.sale = sale;
	}	
}
