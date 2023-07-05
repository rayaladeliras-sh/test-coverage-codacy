package com.stubhub.domain.account.intf;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;


@XmlAccessorType(XmlAccessType.FIELD)
@JsonRootName(value = "orders")
@XmlRootElement(name = "orders")
@XmlType(name = "", propOrder = {
		"numFound","myOrderList"
		})
public class MyOrderListResponse {
	private long numFound;
	
	@JsonProperty(value="order")
	private List<MyOrderResponse> myOrderList = new ArrayList<MyOrderResponse>();	
	
	public List<MyOrderResponse> getMyOrderList() {
		return myOrderList;
	}

	public void setMyOrderList(List<MyOrderResponse> myOrderList) {
		this.myOrderList = myOrderList;
	}	

	public long getNumFound() {
		return numFound;
	}

	public void setNumFound(long numFound) {
		this.numFound = numFound;
	}
}
