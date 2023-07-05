package com.stubhub.domain.account.common.enums;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


@XmlType(name = "listingfilterType")
@XmlEnum(String.class)
public enum ListingFilterType {
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
	@XmlEnumValue("DATELASTMODIFIED")
	DATELASTMODIFIED,
	@XmlEnumValue("SALEENDDATE")
	SALEENDDATE,
	@XmlEnumValue("Q")
	Q,
	@XmlEnumValue("GEOGRAPYID")
	GEOGRAPYID,
	@XmlEnumValue("DELIVERYOPTION")
	DELIVERYOPTION,
	@XmlEnumValue("TICKETID")
	TICKETID,
	@XmlEnumValue("VENUEID")
	VENUEID,
	@XmlEnumValue("GENREID")
	GENREID,
	@XmlEnumValue("STATUS")
	STATUS,
	@XmlEnumValue("EXTERNALLISTINGID")
	EXTERNALLISTINGID;
	

	
	public static ListingFilterType fromString(String inType){
		String type = inType == null ? "" : inType.toUpperCase();
		if(type.equals(EVENT.name()))
			return EVENT;
		if(type.equals(EVENTDATE.name()))
			return EVENTDATE;
		if(type.equals(QUANTITY.name()))
			return QUANTITY;
		if(type.equals(QUANTITY_REMAIN.name()))
			return QUANTITY_REMAIN;
		if(type.equals(PRICE.name()))
			return PRICE;
		if(type.equals(SECTION.name()))
			return SECTION;
		if(type.equals(INHANDDATE.name()))
			return INHANDDATE;
		if(type.equals(DATELASTMODIFIED.name()))
			return DATELASTMODIFIED;
		if(type.equals(STATUS.name()))
			return STATUS;
		if(type.equals(Q.name()))
			return Q;
		if(type.equals(DELIVERYOPTION.name()))
			return DELIVERYOPTION;
		if(type.equals(SALEENDDATE.name()))
			return SALEENDDATE;
		if(type.equals(GEOGRAPYID.name()))
			return GEOGRAPYID;
		if(type.equals(TICKETID.name()))
			return TICKETID;
		if(type.equals(VENUEID.name()))
			return VENUEID;
		if(type.equals(GENREID.name()))
			return GENREID;
		if(type.equals(EXTERNALLISTINGID.name()))
			return EXTERNALLISTINGID;
		else
			return Q;
	}
}
