package com.stubhub.domain.account.common.enums;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


@XmlType(name = "sortColumn")
@XmlEnum(String.class)
public enum SaleSortType {
	@XmlEnumValue("EVENT")
	EVENT,
	@XmlEnumValue("SALEDATE")
	SALEDATE,
	@XmlEnumValue("EVENTDATE")
	EVENTDATE,
	@XmlEnumValue("QUANTITY")
	QUANTITY,
	@XmlEnumValue("PRICE")
	PRICE,
	@XmlEnumValue("STATUS")
	STATUS,
	@XmlEnumValue("DELIVERYOPTION")
	DELIVERYOPTION,
	@XmlEnumValue("SALECATEGORY")
	SALECATEGORY,
	@XmlEnumValue("PAYOUT")
	PAYOUT,
	@XmlEnumValue("INHANDDATE")
    INHANDDATE, @XmlEnumValue("SECTION")
    SECTION, @XmlEnumValue("ROW")
    ROW;
	
	public static SaleSortType fromString(String inType){

		String type = inType == null ? "" : inType.toUpperCase();

		if(type.equals(EVENT.name()))
			return EVENT;
		if(type.equals(EVENTDATE.name()))
			return EVENTDATE;
		if(type.equals(QUANTITY.name()))
			return QUANTITY;
		if(type.equals(PRICE.name()))
			return PRICE;
		if(type.equals(STATUS.name()))
			return STATUS;	
		if(type.equals(DELIVERYOPTION.name()))
			return DELIVERYOPTION;
		if(type.equals(SALECATEGORY.name()))
			return SALECATEGORY;
		if(type.equals(PAYOUT.name()))
			return PAYOUT;
		if(type.equals(INHANDDATE.name()))
			return INHANDDATE;		
        if (type.equals(SECTION.name()))
            return SECTION;
        if (type.equals(ROW.name()))
            return ROW;
		else
			return SALEDATE;
	}
}
