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
import com.stubhub.domain.account.datamodel.entity.OrderProcStatus;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "orderStatuses")
@JsonRootName("orderStatuses")
@XmlType(name = "", propOrder = {"orderId", "orderProc"})
public class OrderStatusResponse extends Response {
	
	@XmlElement(name = "orderId", required = false)
	@JsonProperty("orderId")
	private String orderId;
	
	@XmlElement(name = "orderStatus", required = false)
	@JsonProperty("orderStatus")
	private List<OrderProcStatus> orderProc;

	public List<OrderProcStatus> getOrderProc() {
		return orderProc;
	}
	public void setOrderProc(List<OrderProcStatus> orderProc) {
		this.orderProc = orderProc;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
}

