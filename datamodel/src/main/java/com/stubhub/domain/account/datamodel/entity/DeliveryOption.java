package com.stubhub.domain.account.datamodel.entity;
public enum DeliveryOption {
	NONE(0), MANUAL(2), PREDELIVERY(1);
	int value;
	DeliveryOption(int value){
		this.value = value;
	}
	
	public static DeliveryOption getDeliveryOption(int value){
		if(value == MANUAL.getValue()){
			return MANUAL;
		}else if(value == PREDELIVERY.getValue()){
			return PREDELIVERY;
		}
		return NONE;
	}

	public int getValue() {
		return value;
	}
	
}
