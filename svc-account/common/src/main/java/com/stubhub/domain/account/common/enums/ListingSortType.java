package com.stubhub.domain.account.common.enums;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


@XmlType(name = "listingSortType")
@XmlEnum(String.class)
public enum ListingSortType {
	@XmlEnumValue("EVENT")
	EVENT, 
	@XmlEnumValue("EVENTDATE")
	EVENTDATE,
	@XmlEnumValue("QUANTITY")
	QUANTITY,
	@XmlEnumValue("QUANTITY_REMAIN")
	QUANTITY_REMAIN,
	@XmlEnumValue("PRICE")
	PRICE,
	@XmlEnumValue("SECTION")
	SECTION,
	@XmlEnumValue("INHANDDATE")
	INHANDDATE,
	@XmlEnumValue("STATUS")
	STATUS,
	@XmlEnumValue("DELIVERYOPTION")
	DELIVERYOPTION,
	@XmlEnumValue("SALEENDDATE")
	SALEENDDATE;

	
	public static ListingSortType fromString(String type){
		if(type.equalsIgnoreCase(EVENT.name()))
			return EVENT;
		if(type.equalsIgnoreCase(EVENTDATE.name()))
			return EVENTDATE;
		if(type.equalsIgnoreCase(QUANTITY.name()))
			return QUANTITY;
		if(type.equalsIgnoreCase(QUANTITY_REMAIN.name()))
			return QUANTITY_REMAIN;
		if(type.equalsIgnoreCase(PRICE.name()))
			return PRICE;
		if(type.equalsIgnoreCase(SECTION.name()))
			return SECTION;
		if(type.equalsIgnoreCase(INHANDDATE.name()))
			return INHANDDATE;
		if(type.equalsIgnoreCase(STATUS.name()))
			return STATUS;		
		if(type.equalsIgnoreCase(DELIVERYOPTION.name()))
			return DELIVERYOPTION;
		if(type.equalsIgnoreCase(SALEENDDATE.name()))
			return SALEENDDATE;
		else
			return EVENTDATE;
	}
}
