package com.stubhub.domain.account.intf;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;

import com.stubhub.domain.account.common.Response;

@XmlAccessorType(XmlAccessType.FIELD)
@JsonRootName(value = "delivery")
@XmlRootElement(name = "delivery")
@XmlType(name = "", propOrder = {
		"deliveryMethodId", 
		"deliveryMethodDescription", 
		"deliveryTypeId", 
		"deliveryTypeDescription", 
		"expectedArrivalDateUTC", 
		"inHandDateUTC", 
		"shipDateUTC", 
		"notInHand", 
		"trackingNumber", 
		"orderProcSubStatusDesc", 
		"orderProcSubStatusCode"
})
public class DeliveryResponse extends Response {
	@XmlElement(name = "deliveryMethodId", required = true)
	@JsonProperty("deliveryMethodId")
	private String deliveryMethodId;
	@XmlElement(name = "deliveryMethodDescription", required = true)
	@JsonProperty("deliveryMethodDescription")
	private String deliveryMethodDescription;
	@XmlElement(name = "shipDateUTC", required = true)
	@JsonProperty("shipDateUTC")
	private String shipDateUTC;
	@XmlElement(name = "deliveryTypeId", required = true)
	@JsonProperty("deliveryTypeId")
	private String deliveryTypeId;
	@XmlElement(name = "expectedArrivalDateUTC", required = true)
	@JsonProperty("expectedArrivalDateUTC")
	private String expectedArrivalDateUTC;
	@XmlElement(name = "inHandDateUTC", required = true)
	@JsonProperty("inHandDateUTC")
	private String inHandDateUTC;
	@XmlElement(name = "notInHand", required = true)
	@JsonProperty("notInHand")
	private Boolean notInHand;
	@XmlElement(name = "trackingNumber", required = true)
	@JsonProperty("trackingNumber")
	private String trackingNumber;
	@XmlElement(name = "deliveryTypeDescription", required = true)
	@JsonProperty("deliveryTypeDescription")
	private String deliveryTypeDescription;
	@XmlElement(name = "orderProcSubStatusDesc", required = true)
	@JsonProperty("orderProcSubStatusDesc")
	private String orderProcSubStatusDesc;
	@XmlElement(name = "orderProcSubStatusCode", required = true)
	@JsonProperty("orderProcSubStatusCode")
	private String orderProcSubStatusCode;
	
	public String getDeliveryMethodId() {
		return deliveryMethodId;
	}
	public void setDeliveryMethodId(String deliveryMethodId) {
		this.deliveryMethodId = deliveryMethodId;
	}
	public String getDeliveryMethodDescription() {
		return deliveryMethodDescription;
	}
	public void setDeliveryMethodDescription(String deliveryMethodDescription) {
		this.deliveryMethodDescription = deliveryMethodDescription;
	}
	public String getShipDateUTC() {
		return shipDateUTC;
	}
	public void setShipDateUTC(String shipDateUTC) {
		this.shipDateUTC = shipDateUTC;
	}
	public String getDeliveryTypeId() {
		return deliveryTypeId;
	}
	public void setDeliveryTypeId(String deliveryTypeId) {
		this.deliveryTypeId = deliveryTypeId;
	}
	public String getExpectedArrivalDateUTC() {
		return expectedArrivalDateUTC;
	}
	public void setExpectedArrivalDateUTC(String expectedArrivalDateUTC) {
		this.expectedArrivalDateUTC = expectedArrivalDateUTC;
	}
	public String getInHandDateUTC() {
		return inHandDateUTC;
	}
	public void setInHandDateUTC(String inHandDateUTC) {
		this.inHandDateUTC = inHandDateUTC;
	}
	public Boolean getNotInHand() {
		return notInHand;
	}
	public void setNotInHand(Boolean notInHand) {
		this.notInHand = notInHand;
	}
	public String getTrackingNumber() {
		return trackingNumber;
	}
	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}
	public String getDeliveryTypeDescription() {
		return deliveryTypeDescription;
	}
	public void setDeliveryTypeDescription(String deliveryTypeDescription) {
		this.deliveryTypeDescription = deliveryTypeDescription;
	}
	public String getOrderProcSubStatusDesc() {
		return orderProcSubStatusDesc;
	}
	public void setOrderProcSubStatusDesc(String orderProcSubStatusDesc) {
		this.orderProcSubStatusDesc = orderProcSubStatusDesc;
	}
	public String getOrderProcSubStatusCode() {
		return orderProcSubStatusCode;
	}
	public void setOrderProcSubStatusCode(String orderProcSubStatusCode) {
		this.orderProcSubStatusCode = orderProcSubStatusCode;
	}
}
