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
@JsonRootName(value = "orders")
@XmlRootElement(name = "orders")
@XmlType(name = "", propOrder = {
						"ordersFound",	
						"order" 
						})
public class OrdersResponse extends Response {

	@XmlElement(name = "ordersFound", required = true)
	@JsonProperty("ordersFound")
	private int ordersFound;
	@XmlElement(name = "order", required = true)
	@JsonProperty("order")
	private List<CSOrderDetailsResponse> order;
	
	public int getOrdersFound() {
		return ordersFound;
	}
	public void setOrdersFound(int ordersFound) {
		this.ordersFound = ordersFound;
	}
	public List<CSOrderDetailsResponse> getOrder() {
		return order;
	}
	public void setOrder(List<CSOrderDetailsResponse> order) {
		this.order = order;
	}	
}
