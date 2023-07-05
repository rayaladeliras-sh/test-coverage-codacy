package com.stubhub.domain.account.common.enums;

public enum ListingType {
	TICKET(1L, "TICKET"),
	PARKING_PASS(2L, "PARKING_PASS"),
	TICKETS_WITH_PARKING_PASS_INCLUDED(3L, "TICKETS_WITH_PARKING_PASS_INCLUDED");

	private Long id;
	private String name;
	
	ListingType(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public static ListingType getEnum(String name) {
        ListingType list[] = ListingType.values();
		for (int i = 0; i < list.length; i++) {
			ListingType obj = list[i];
			if (obj.getName().equalsIgnoreCase(name)) {
				return obj;
			}
		}
		return null;
	}
}
