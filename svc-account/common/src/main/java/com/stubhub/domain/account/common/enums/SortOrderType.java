package com.stubhub.domain.account.common.enums;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


@XmlType(name = "sortOrderType")
@XmlEnum(String.class)
public enum SortOrderType {
	@XmlEnumValue("Asc")
	ASCENDING,
	@XmlEnumValue("Desc")
	DESCENDING;
	
	public static SortOrderType fromString(String type){
		if("Desc".equalsIgnoreCase(type))
				return DESCENDING;
		else
			return ASCENDING;
	}
	
}
