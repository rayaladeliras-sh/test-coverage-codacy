package com.stubhub.domain.account.datamodel.dao;

import java.util.List;

import com.stubhub.domain.account.datamodel.entity.VenueConfigZoneSection;

public interface VenueConfigZoneSectionDAO {
	
	public List<VenueConfigZoneSection> getZoneSectionByVenueConfigId(Long venueConfigId);

}
