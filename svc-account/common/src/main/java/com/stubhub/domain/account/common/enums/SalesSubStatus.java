package com.stubhub.domain.account.common.enums;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "subStatus")
@XmlEnum(String.class)
public enum SalesSubStatus {
	@XmlEnumValue("CHANGE_REQUESTED_DELIVERY_DATE")
	CHANGE_REQUESTED_DELIVERY_DATE, 
	@XmlEnumValue("CHANGE_REQUESTED_DELIVERY_METHOD")
	CHANGE_REQUESTED_DELIVERY_METHOD, 
	@XmlEnumValue("REPLACEMENT_TICKETS_OFFERED")
	REPLACEMENT_TICKETS_OFFERED, 
	@XmlEnumValue("DROPPED_SALE")
	DROPPED_SALE, 
	@XmlEnumValue("SUBS_OFFERED")
	SUBS_OFFERED, 
	@XmlEnumValue("SUBS_REJECTED")
	SUBS_REJECTED, 
	@XmlEnumValue("GENERATE_SHIPPING_LABEL")
	GENERATE_SHIPPING_LABEL, 
	@XmlEnumValue("REPRINT_SHIPPING_LABEL")
	REPRINT_SHIPPING_LABEL,
	@XmlEnumValue("PENDING_UPLOAD_PDF")
	PENDING_UPLOAD_PDF,
	@XmlEnumValue("SUBS_BY_SYSTEM")
	SUBS_BY_SYSTEM;
	
}
