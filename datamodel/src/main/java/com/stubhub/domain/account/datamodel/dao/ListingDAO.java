package com.stubhub.domain.account.datamodel.dao;

import com.stubhub.domain.account.datamodel.entity.Listing;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ListingDAO {

	public int updateListing(Listing listing);
	public int updateListingPaymentType(Long listingId, Long sellerPaymentTypeId);
	public Map<String, String> getListingsLastUpdates(Set<String> listingIds);
	public Map<String, String> getUserSummaryTicketStats(Long sellerId);
}
