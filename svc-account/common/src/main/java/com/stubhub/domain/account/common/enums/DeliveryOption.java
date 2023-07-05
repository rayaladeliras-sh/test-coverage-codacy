package com.stubhub.domain.account.common.enums;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;
import java.util.Locale;

@XmlType(name = "deliveryOption")
@XmlEnum(String.class)
public enum DeliveryOption {
	@XmlEnumValue("FEDEX")
	FEDEX,

	@XmlEnumValue("UPS")
	UPS,

	@XmlEnumValue("BARCODE")
	BARCODE,
	
	@XmlEnumValue("PDF")
	PDF,

	@XmlEnumValue("ROYALMAIL")
	ROYALMAIL,

	@XmlEnumValue("DEUTSCHEPOST")
	DEUTSCHEPOST,
	
	@XmlEnumValue("LMS")
	LMS,

	@XmlEnumValue("WILLCALL")
	WILLCALL,
	
	@XmlEnumValue("OTHER")
	OTHER,
    
	@XmlEnumValue("FLASHSEAT")
	FLASHSEAT,

	@XmlEnumValue("COURIER")
	COURIER,

	@XmlEnumValue("MOBILE_TICKET")
	MOBILE_TICKET,

	@XmlEnumValue("LOCALDELIVERY")
	LOCALDELIVERY,

	@XmlEnumValue("EXTERNAL_TRANSFER")
	EXTERNAL_TRANSFER;

	public static DeliveryOption fromString(String option) {
		try {
			return option != null ? DeliveryOption.valueOf(option.toUpperCase(Locale.ROOT)) : null;
		} catch (IllegalArgumentException ex) {
			return null;
		}
	}
}
