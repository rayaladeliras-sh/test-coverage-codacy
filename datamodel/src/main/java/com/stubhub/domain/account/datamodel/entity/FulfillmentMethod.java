package com.stubhub.domain.account.datamodel.entity;
public enum FulfillmentMethod {
	BarcodePreDeliverySTH("Barcode - PreDelivery (STH)"),
	BarcodePreDeliveryNonSTH("Barcode - PreDelivery (Non-STH)"),
	Barcode("Barcode"),
	PDFPreDelivery("PDF - PreDelivery"),
	PDF("PDF"),
	FedEx("FedEx"),
	LMS("LMS"),
	LMSPreDelivery("LMS - PreDelivery"),
	OtherPreDelivery("Other - PreDelivery"),
	UPS("UPS"),
	Courier("Courier");

	private String name;
	
	FulfillmentMethod(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public static FulfillmentMethod getFulfillmentMethodEnumByName(String name) {
		FulfillmentMethod list[] = FulfillmentMethod.values();
		for (int i=0; i<list.length; i++) {
			FulfillmentMethod obj = list[i];
			if (obj.getName().equalsIgnoreCase(name)) {
				return obj;
			}
		}
		
		return null;
	}

}
