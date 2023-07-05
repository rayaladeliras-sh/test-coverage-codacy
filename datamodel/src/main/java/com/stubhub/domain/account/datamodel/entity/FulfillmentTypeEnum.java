package com.stubhub.domain.account.datamodel.entity;

public enum FulfillmentTypeEnum {
	// this order of declaration is of importance. Do not change it unless needed.
	Barcode("Barcode"),
	PDF("PDF"),
	FedEx("FedEx"),
	LMS("LMS"),
	UPS("UPS"),
	Shipping("Shipping"),
	Other("Other");

	private String name;
	
	FulfillmentTypeEnum(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public static FulfillmentTypeEnum getFulfillmentTypeEnumByName(String name) {
		FulfillmentTypeEnum list[] = FulfillmentTypeEnum.values();
		for (int i=0; i<list.length; i++) {
			FulfillmentTypeEnum obj = list[i];
			if (obj.getName().equalsIgnoreCase(name)) {
				return obj;
			}
		}
		
		return null;
	}
	

}
