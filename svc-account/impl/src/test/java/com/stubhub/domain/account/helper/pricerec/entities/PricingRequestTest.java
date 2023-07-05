package com.stubhub.domain.account.helper.pricerec.entities;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

import junit.framework.Assert;

public class PricingRequestTest {

	@Test
	public void testPriceGuidanceRequest() {

		PricingRequest pricingRequest = new PricingRequest();
		pricingRequest.setListingIds(null);
		List<Long> noListings = pricingRequest.getListingIds();
		
		List<Long> listings = new ArrayList<Long>();
		listings.add(12345L);
		pricingRequest.setListingIds(listings); 
		
		listings = pricingRequest.getListingIds();
		Assert.assertEquals(1, listings.size());
	}

}