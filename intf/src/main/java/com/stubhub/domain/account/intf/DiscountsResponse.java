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
@XmlRootElement(name = "discounts")
@JsonRootName("discounts")
@XmlType(name = "", propOrder = {"discounts"})
public class DiscountsResponse extends Response {
	
	@XmlElement(name = "discount", required = false)
	@JsonProperty("discount")
	private List<DiscountResponse> discounts;

	public List<DiscountResponse> getDiscounts() {
		return discounts;
	}

	public void setDiscounts(List<DiscountResponse> discounts) {
		this.discounts = discounts;
	}

}



