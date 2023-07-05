package com.stubhub.domain.account.common.enums;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "splitOption")
@XmlEnum(String.class)
public enum SplitOption {
	@XmlEnumValue("NONE")
	NONE,
	@XmlEnumValue("MULTIPLES")
	MULTIPLES,
	@XmlEnumValue("NOSINGLES")
	NOSINGLES;
	
	public static SplitOption fromString(String type){
		if(type.equals(NONE.name()))
			return NONE;
		else if(type.equals(MULTIPLES.name()))
			return MULTIPLES;
		else if(type.equals(NOSINGLES.name()))
			return NOSINGLES;
		else if(type.equals("0"))
			return NONE;
		else if(type.equals("1"))
			return MULTIPLES;
		else if(type.equals("2"))
			return NOSINGLES;
		return NONE;
	}

}

