package com.stubhub.domain.account.datamodel.entity;

public enum TicketMedium {
	PAPER(1), PDF(2), BARCODE(3),
	FLASHSEAT(4),
	SEASONCARD(5),
	EVENTCARD(6),
	EXTMOBILE(7),
	EXTFLASH(8),
	MOBILE(9),
	WRISTBAND(10),
	RFID(11),
	GUESTLIST(12);

	int value;
	
	TicketMedium(int value){
		this.value = value;
	}
	public int getValue() {
		return value;
	}


	public static TicketMedium getTicketMedium(int value){
		TicketMedium list[] = TicketMedium.values();
		for (TicketMedium obj:list) {
			if (obj.getValue() == value) {
				return obj;
			}
		}
		return null;
	}
}
