package com.stubhub.domain.account.common.enums;

public enum LocalizedMessageTypes {

	NAME("name"),
	DELIVERY_METHOD_DISPLAY_NAME("deliveryMethodDisplayName"),
	SHORT_INSTRUCTION("shortInstruction"),
	SHORT_APP_INSTRUCTION("shortAppInstruction"),
	LONG_INSTRUCTION("longInstruction"),
	LONG_APP_INSTRUCTION("longAppInstruction");
	
	private String identifier;
	LocalizedMessageTypes(String identifier) {
		this.identifier = identifier;
	}
	
	public String toString() {
		return identifier;
	}
}
