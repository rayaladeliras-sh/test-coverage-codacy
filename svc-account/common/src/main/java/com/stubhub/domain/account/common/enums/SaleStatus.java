package com.stubhub.domain.account.common.enums;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "status")
@XmlEnum(String.class)
public enum SaleStatus {
	@XmlEnumValue("PENDING")
	PENDING, 
	@XmlEnumValue("CONFIRMED")
	CONFIRMED,
	@XmlEnumValue("SHIPPED")
	SHIPPED,
	@XmlEnumValue("DELIVERED")
	DELIVERED, 
	@XmlEnumValue("SUBSOFFERED")
	SUBSOFFERED, 
	@XmlEnumValue("CANCELLED")
	CANCELLED, 
	@XmlEnumValue("DELIVERYEXCEPTION")
	DELIVERYEXCEPTION, 
	@XmlEnumValue("ONHOLD")
	ONHOLD,
	@XmlEnumValue("PENDINGREVIEW")
	PENDINGREVIEW;
	
	public static SaleStatus fromString(String type){
		if(PENDING.name().equals(type)){
			return PENDING;
		}else if(CONFIRMED.name().equals(type)){
			return CONFIRMED;
		}else if(SHIPPED.name().equals(type)){
			return SHIPPED;
		}else if(DELIVERED.name().equals(type)){
			return DELIVERED;
		}else if(SUBSOFFERED.name().equals(type)){
			return SUBSOFFERED;
		}else if(CANCELLED.name().equals(type)){
			return CANCELLED;
		}else if(DELIVERYEXCEPTION.name().equals(type)){
			return DELIVERYEXCEPTION;
		}else if(ONHOLD.name().equals(type)){
			return ONHOLD;
		}else if(PENDINGREVIEW.name().equals(type)){
			return PENDINGREVIEW;
		}
		return null;
	}

	public static SaleStatus fromNumber(Long status, Long subStatus){
		if(status == 2000l){
			return CONFIRMED;
		}else if(status == 3000l || (status == 1000l && subStatus != 50l)){
			return PENDING;
		}else if(status == 4000l){
			return SHIPPED;
		}else if(status == 5000l || status == 6000l){
			return DELIVERED;
		}else if(status == 7000l){
			return SUBSOFFERED;
		}else if(status == 8000l){
			return CANCELLED;
		}else if(status == 9000l && subStatus != 44l){
			return DELIVERYEXCEPTION;
		}else if(status == 10000l){
			return ONHOLD;
		}else if((status == 1000l && subStatus == 50l) || (status == 9000l && subStatus == 44l)){
			return PENDINGREVIEW;
		}
		return null;
	}
	
}
