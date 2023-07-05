package com.stubhub.domain.account.common.enums;

public enum TicketMedium {
    PAPER(1), PDF(2), BARCODE(3), FLASHSEAT(4),
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


    public static TicketMedium getTicketMedium(int value) {
        for (TicketMedium obj : TicketMedium.values()) {
			if (obj.getValue() == value) {
				return obj;
			}
		}
		return null;
	}
}
