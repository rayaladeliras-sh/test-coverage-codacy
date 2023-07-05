package com.stubhub.domain.account.intf;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DeliveryMethod", propOrder = {
        "deliveryMethodId",
        "deliveryMethodDisplayName",
        "deliveryMethodShortInstruction",
        "deliveryMethodShortAppInstruction",
        "deliveryMethodLongInstruction",
        "deliveryMethodLongAppInstruction",
		"deliverPrimaryName"
})
public class DeliveryMethod {

	private Long deliveryMethodId;
    
	//Localized messages for delivery method id
	private String deliveryMethodDisplayName;
	private String deliveryMethodShortInstruction;
	private String deliveryMethodShortAppInstruction;
	private String deliveryMethodLongInstruction;
	private String deliveryMethodLongAppInstruction;
	private String deliverPrimaryName;
	
	public Long getDeliveryMethodId() {
		return deliveryMethodId;
	}
	public void setDeliveryMethodId(Long deliveryMethodId) {
		this.deliveryMethodId = deliveryMethodId;
	}
	public String getDeliveryMethodDisplayName() {
		return deliveryMethodDisplayName;
	}
	public void setDeliveryMethodDisplayName(String deliveryMethodDisplayName) {
		this.deliveryMethodDisplayName = deliveryMethodDisplayName;
	}
	public String getDeliveryMethodShortInstruction() {
		return deliveryMethodShortInstruction;
	}
	public void setDeliveryMethodShortInstruction(
			String deliveryMethodShortInstruction) {
		this.deliveryMethodShortInstruction = deliveryMethodShortInstruction;
	}
	public String getDeliveryMethodShortAppInstruction() {
		return deliveryMethodShortAppInstruction;
	}
	public void setDeliveryMethodShortAppInstruction(
			String deliveryMethodShortAppInstruction) {
		this.deliveryMethodShortAppInstruction = deliveryMethodShortAppInstruction;
	}
	public String getDeliveryMethodLongInstruction() {
		return deliveryMethodLongInstruction;
	}
	public void setDeliveryMethodLongInstruction(
			String deliveryMethodLongInstruction) {
		this.deliveryMethodLongInstruction = deliveryMethodLongInstruction;
	}
	public String getDeliveryMethodLongAppInstruction() {
		return deliveryMethodLongAppInstruction;
	}
	public void setDeliveryMethodLongAppInstruction(
			String deliveryMethodLongAppInstruction) {
		this.deliveryMethodLongAppInstruction = deliveryMethodLongAppInstruction;
	}

	public String getDeliverPrimaryName() {
		return deliverPrimaryName;
	}

	public void setDeliverPrimaryName(String deliverPrimaryName) {
		this.deliverPrimaryName = deliverPrimaryName;
	}
}
