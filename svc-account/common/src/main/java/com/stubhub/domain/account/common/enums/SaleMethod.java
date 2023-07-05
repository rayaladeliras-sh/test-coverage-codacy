package com.stubhub.domain.account.common.enums;

public enum SaleMethod {
	
	FixedPrice(1L), DECLINING(3L);
	
	Long value;
	
	SaleMethod(Long value){
		this.value = value;
	}
	public Long getValue() {
		return value;
	}

	public static SaleMethod getSaleMethod(Long value){
        
        for (SaleMethod obj : SaleMethod.values()) {
			if (obj.getValue().equals(value)) {
				return obj;
			}
		}
		return null;
	}
	
}

	

