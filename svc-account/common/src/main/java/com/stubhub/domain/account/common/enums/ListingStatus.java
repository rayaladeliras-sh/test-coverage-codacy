package com.stubhub.domain.account.common.enums;

import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;


@XmlType(name = "listingStatus")
@XmlEnum(String.class)
public enum ListingStatus {
	@XmlEnumValue("ACTIVE")
	ACTIVE("ACTIVE"),
	
	@XmlEnumValue("INCOMPLETE")
	INCOMPLETE("INCOMPLETE"),
	
	@XmlEnumValue("EXPIRED")
	EXPIRED("EXPIRED"),
	
	@XmlEnumValue("DELETED")
	DELETED("DELETED"),
	
	@XmlEnumValue("PENDING")
	PENDING("PENDING"),
	
	@XmlEnumValue("INACTIVE")
	INACTIVE("INACTIVE"),

	@XmlEnumValue("SOLD")
	SOLD("SOLD");
	
	private String value;
	
	ListingStatus(String value) {
		this.value=value;
	}
	
	public String getValue() {
		return value;
	}
	
    public static ListingStatus fromValue(String v) {
		if (v != null) {
			for (ListingStatus ls : ListingStatus.values()) {
				if (ls.getValue().equalsIgnoreCase(v)) {
					return ls;
				}
			}
		}
        return null;
    }	
}
