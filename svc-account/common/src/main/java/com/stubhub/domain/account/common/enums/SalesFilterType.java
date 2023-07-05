package com.stubhub.domain.account.common.enums;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


@XmlType(name = "salesfilterType")
@XmlEnum(String.class)
public enum SalesFilterType {
	@XmlEnumValue("EVENT")
	EVENT, 
	@XmlEnumValue("EVENTDATE")
	EVENTDATE,
	@XmlEnumValue("QUANTITY")
	QUANTITY,
	@XmlEnumValue("PRICE")
	PRICE,	
	@XmlEnumValue("SALEDATE")
	SALEDATE,
	@XmlEnumValue("DATELASTMODIFIED")
	DATELASTMODIFIED,
	@XmlEnumValue("Q")
	Q,
	@XmlEnumValue("DELIVERYOPTION")
	DELIVERYOPTION,
	@XmlEnumValue("SALEID")
	SALEID,
	@XmlEnumValue("LISTINGIDS")
	LISTINGIDS,
	@XmlEnumValue("EXTERNALLISTINGIDS")
	EXTERNALLISTINGIDS,
	@XmlEnumValue("GENREID")
	GENREID,
	@XmlEnumValue("VENUEID")
	VENUEID,
	@XmlEnumValue("CATEGORY")
	CATEGORY,
	@XmlEnumValue("ACTION")
	ACTION,
	@XmlEnumValue("STATUS")
	STATUS,
	@XmlEnumValue("INHANDDATE")
	INHANDDATE,
	@XmlEnumValue("EVENTDATEUTC")
	EVENTDATEUTC;
	

	
	public static SalesFilterType fromString(String inType){
		String type = inType == null ? "" : inType.toUpperCase();
		if(type.equals(EVENT.name()))
			return EVENT;
		if(type.equals(EVENTDATE.name()))
			return EVENTDATE;
		if(type.equals(QUANTITY.name()))
			return QUANTITY;		
		if(type.equals(PRICE.name()))
			return PRICE;
		if(type.equals(SALEDATE.name()))
			return SALEDATE;
		if(type.equals(DATELASTMODIFIED.name()))
			return DATELASTMODIFIED;
		if(type.equals(DELIVERYOPTION.name()))
			return DELIVERYOPTION;
		if(type.equals(SALEID.name()))
			return SALEID;
		if(type.equals(Q.name()))
			return Q;
		if(type.equals(DELIVERYOPTION.name()))
			return DELIVERYOPTION;
		if(type.equals(LISTINGIDS.name()))
			return LISTINGIDS;
		if(type.equals(EXTERNALLISTINGIDS.name()))
			return EXTERNALLISTINGIDS;
		if(type.equals(VENUEID.name()))
			return VENUEID;
		if(type.equals(GENREID.name()))
			return GENREID;
		if(type.equals(CATEGORY.name()))
			return CATEGORY;
		if(type.equals(ACTION.name()))
			return ACTION;
		if(type.equals(STATUS.name()))
			return STATUS;
		if(type.equals(INHANDDATE.name()))
			return INHANDDATE;
		if(type.equals(EVENTDATEUTC.name()))
			return EVENTDATEUTC;
		else
			return Q;
	}
}
