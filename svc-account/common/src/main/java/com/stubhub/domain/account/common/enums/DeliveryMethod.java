package com.stubhub.domain.account.common.enums;

public enum DeliveryMethod {
	_1(1, "Electronic - Download", 1, "Electronic"),
	_2(2, "Electronic - Instant Download", 2, "Electronic Instant Download"),
	_3(3, "FedEx Priority Overnight - AK and HI", 3, "FedEx"),
	_4(4, "FedEx Standard Overnight", 3, "FedEx"),
	_5(5, "FedEx Priority Overnight - Continental US", 3, "FedEx"),
	_6(6, "FedEx Intra Canada Overnight", 3, "FedEx"),
	_7(7, "FedEx Two Day", 3, "FedEx"),
	_8(8, "FedEx Saturday", 3, "FedEx"),
	_9(9, "FedEx International Economy - Intra Canada", 3, "FedEx"),
	_10(10, "WillCall", 4, "Pickup"),
	_11(11, "Kiosk", 4, "Pickup"),
	_12(12, "Pickup", 4, "Pickup"),
	_13(13, "Email", 1, "Electronic"),
	_14(14, "Courier", 11, "Pickup"),
	_15(15, "FedEx International Priority Puerto Rico", 3, "FedEx"),
	_16(16, "FedEx International Priority Canada", 3, "FedEx"),
	_17(17, "Pickup - Event Day", 4, "Pickup"),
	_18(18, "Off-site Pickup", 4, "Pickup"),
	_19(19, "Hospitality", 4, "Pickup"),
	_22(22, "UPS Worldwide Saver", 5, "UPS"),
	_23(23, "UPS Next Business Day Saver - Intra-USA", 5, "UPS"),
	_24(24, "UPS 2nd Day Air - Intra-USA", 5, "UPS"),
	_25(25, "UPS Next Business Day Air", 5, "UPS"),
	_27(27, "UPS Express Saver - Intra-CA", 5, "UPS"),
	_36(36, "UPS Standard - Intra-UK", 5, "UPS"),
	_37(37, "Royal Mail - MetaPack", 6, "Royal Mail"),
	_38(38, "Deutsche Post Intra-DE", 7, "Deutsche Post"),
	_39(39, "Deutsche Post EU wide", 7, "Deutsche Post"),
	FlashSeat_Instant(40, "FlashSeat - Instant Download", 8, "MobileId"),
	
	// TODO : In future retire this enum and use fufillment api to get Delivery details
	Mobile_Ticket_Instant(41, "Mobile ticket - Instant", 12, "Mobile Ticket"),
	Mobile_Ticket(42, "Mobile ticket", 9, "Mobile Ticket"),
	Mobile_Transfer(43, "Mobile Transfer", 10, "Mobile Transfer"),
	Flash_Transfer(44, "Flash Transfer", 10, "Flash Transfer"),
	LOCAL_DELIVERY(49, "Local Delivery", 16,"LocalDelivery"),
	FlashSeat(45,"FlashSeat",13,"FlashSeat"),
	Electronic_and_Mobile_Ticket(47,"Electronic and Mobile Ticket",14,"Electronic and Mobile Ticket"),
	Electronic_and_Mobile_Ticket_Instant(48,"Electronic and Mobile Ticket Instant",15,"Electronic and Mobile Ticket Instant"),
	Mobile_Transfer_Seat_Geek(60, "Mobile Transfer Seat Geek", 10, "Mobile Transfer"),
	TM_Mobile_Secure_Entry(62,"TM Mobile Secure Entry",9,"Mobile Ticket"),
	TM_Mobile_Secure_Entry_Instant(63,"TM Mobile Secure Entry Instant",12,"Mobile Ticket Instant")
	;


	private int deliveryMethodId;
	private String deliveryMethodDesc;
	private int deliveryTypeId;
	private String deliveryTypeDesc;
	
	private DeliveryMethod(int deliveryMethodId, String deliveryMethodDesc, int deliveryTypeId, String deliveryTypeDesc) {
		this.deliveryMethodId = deliveryMethodId;
		this.deliveryMethodDesc = deliveryMethodDesc;
		this.deliveryTypeId = deliveryTypeId;
		this.deliveryTypeDesc = deliveryTypeDesc;
	}	

	public int getDeliveryMethodId() {
		return deliveryMethodId;
	}
	public String getDeliveryMethodDesc() {
		return deliveryMethodDesc;
	}
	public int getDeliveryTypeId() {
		return deliveryTypeId;
	}
	public String getDeliveryTypeDesc() {
		return deliveryTypeDesc;
	}
	
	public static DeliveryMethod getDeliveryMethodById(int id) {
		for (DeliveryMethod deliveryMethod : DeliveryMethod.values()) {
			if (deliveryMethod.getDeliveryTypeId() == id) {
				return deliveryMethod;
			}
		}
		return null;
	}

}

