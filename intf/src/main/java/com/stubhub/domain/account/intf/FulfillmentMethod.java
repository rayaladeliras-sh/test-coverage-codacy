package com.stubhub.domain.account.intf;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FulfillmentMethod", propOrder = {
        "fulfillmentMethodId",
        "fulfillmentMethodDisplayName",
        "fulfillmentMethodShortInstruction",
        "fulfillmentMethodShortAppInstruction",
        "fulfillmentMethodLongInstruction",
        "fulfillmentMethodLongAppInstruction"
})
public class FulfillmentMethod {

	private Long fulfillmentMethodId;
	
	//Localized messages for fulfillment method id
	private String fulfillmentMethodDisplayName;
	private String fulfillmentMethodShortInstruction;
	private String fulfillmentMethodShortAppInstruction;
	private String fulfillmentMethodLongInstruction;
	private String fulfillmentMethodLongAppInstruction;
	
	public Long getFulfillmentMethodId() {
		return fulfillmentMethodId;
	}
	public void setFulfillmentMethodId(Long fulfillmentMethodId) {
		this.fulfillmentMethodId = fulfillmentMethodId;
	}
	public String getFulfillmentMethodDisplayName() {
		return fulfillmentMethodDisplayName;
	}
	public void setFulfillmentMethodDisplayName(String fulfillmentMethodDisplayName) {
		this.fulfillmentMethodDisplayName = fulfillmentMethodDisplayName;
	}
	public String getFulfillmentMethodShortInstruction() {
		return fulfillmentMethodShortInstruction;
	}
	public void setFulfillmentMethodShortInstruction(
			String fulfillmentMethodShortInstruction) {
		this.fulfillmentMethodShortInstruction = fulfillmentMethodShortInstruction;
	}
	public String getFulfillmentMethodShortAppInstruction() {
		return fulfillmentMethodShortAppInstruction;
	}
	public void setFulfillmentMethodShortAppInstruction(
			String fulfillmentMethodShortAppInstruction) {
		this.fulfillmentMethodShortAppInstruction = fulfillmentMethodShortAppInstruction;
	}
	public String getFulfillmentMethodLongInstruction() {
		return fulfillmentMethodLongInstruction;
	}
	public void setFulfillmentMethodLongInstruction(
			String fulfillmentMethodLongInstruction) {
		this.fulfillmentMethodLongInstruction = fulfillmentMethodLongInstruction;
	}
	public String getFulfillmentMethodLongAppInstruction() {
		return fulfillmentMethodLongAppInstruction;
	}
	public void setFulfillmentMethodLongAppInstruction(
			String fulfillmentMethodLongAppInstruction) {
		this.fulfillmentMethodLongAppInstruction = fulfillmentMethodLongAppInstruction;
	}
}
