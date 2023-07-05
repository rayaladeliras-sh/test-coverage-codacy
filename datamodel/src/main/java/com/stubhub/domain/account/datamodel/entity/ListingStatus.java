package com.stubhub.domain.account.datamodel.entity;

public enum ListingStatus {

	ACTIVE("ACTIVE"), INACTIVE("INACTIVE"), DELETED("DELETED"), INCOMPLETE("INCOMPLETE"), PENDING_LOCK("PENDING LOCK"), PENDING_PDF_REVIEW("PENDING PDF REVIEW");
	
	private String description;
	
	ListingStatus(String desc) {
		this.description = desc;
	}
    
	public String getDescription() {
		return this.description;
	}
	
	public final static ListingStatus getListingStatus(String desc) {
		ListingStatus[] statusArray = ListingStatus.values();
		for(ListingStatus listingStatus : statusArray){
			if(desc!=null && listingStatus.getDescription().equalsIgnoreCase(desc)) {
				return listingStatus;
			}
		}
		return null;
	}
	
	public String toString() {
		return this.description;
	}
	
}
