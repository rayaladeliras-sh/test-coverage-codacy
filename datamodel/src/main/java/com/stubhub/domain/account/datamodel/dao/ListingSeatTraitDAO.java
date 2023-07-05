package com.stubhub.domain.account.datamodel.dao;

import java.util.List;

import com.stubhub.domain.account.datamodel.entity.ListingSeatTrait;

public interface ListingSeatTraitDAO {
	
	public List<ListingSeatTrait> getByListingId(Long ListingId);
}
