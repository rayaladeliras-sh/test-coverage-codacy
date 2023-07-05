package com.stubhub.domain.account.common.enums;

public enum LMSOption {
	NONE(0), PENDINGAPPROVAL(1), APPROVED(2), REJECTED(3), REMOVED(4);
	int value;
	LMSOption(int value){
		this.value = value;
	}
	public int getValue() {
		return value;
	}
	
	public static LMSOption getLMSOption(int value){
		
        LMSOption list[] = LMSOption.values();
		for (int i = 0; i < list.length; i++) {
			LMSOption obj = list[i];
			if (obj.getValue() == value) {
				return obj;
			}
		}
		return null;
	}	
}
