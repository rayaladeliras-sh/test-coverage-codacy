package com.stubhub.domain.account.intf;

public interface TranslatableEventData {
	Object getEventId();
	
	String getEventDescription();
	Long getVenueConfigId() ;
	void setEventDescription(String eventDescription);
	void setStubhubMobileTicket(Integer stubhubMobileTicket);
	void setVenueConfigId(Long venueConfigId);
}
