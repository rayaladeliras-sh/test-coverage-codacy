package com.stubhub.domain.account.intf;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ListingsResponseTest {
	
	@Test
	 public void testGetSet() {
		
		
		ListingsResponse listingsResponse =new ListingsResponse();
		List<com.stubhub.domain.account.common.Error> errors = new ArrayList<com.stubhub.domain.account.common.Error>();
		List<ListingResponse> listings = new ArrayList<ListingResponse>();

              try{
		
				listingsResponse.setErrors(errors);
				listingsResponse.setListings(listings);
			
				Assert.assertEquals(listingsResponse.getErrors(),errors);
				Assert.assertEquals(listingsResponse.getListings(),listings);
		
			  	
			  	
           	} catch(Exception e){} 
	}
}
