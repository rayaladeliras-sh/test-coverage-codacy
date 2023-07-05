package com.stubhub.domain.account.common.enums;

public enum OrderStatus {
	HOLD(10000),PURCHASED(1000),CONFIRMED(2000),UNSHIPPED(3000),SHIPPED(4000),DELIVERED(5000)
	,FULFILLED(6000),SUBS_OFFERED(7000),CANCELLED(8000),DELIVERY_EXCEPTION(9000);
	
	private int id;

	private OrderStatus(int id) {
		this.id = id;
	}
	
	public static OrderStatus getById(int id) {
		for(OrderStatus os : OrderStatus.values()){
			if(os.id == id)
				return os;
		}
		return null;
	}
}
