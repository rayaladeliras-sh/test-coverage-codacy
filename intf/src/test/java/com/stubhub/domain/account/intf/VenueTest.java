package com.stubhub.domain.account.intf;

import junit.framework.Assert;

import org.testng.annotations.Test;

public class VenueTest {

	@Test
	 public void testGetSet() {
		
		Venue venue=new Venue();
		String city="CITY";
		String country="USA";
		String description="TEST";
		Long venueConfigId=3L;
		
		venue.setCity(city);
		Assert.assertEquals(venue.getCity(), city);
	    venue.setCountry(country);
	    Assert.assertEquals(venue.getCountry(),country);
	    venue.setDescription(description);
	    Assert.assertEquals(venue.getDescription(),description);
	    venue.setVenueConfigId(venueConfigId);
	    Assert.assertEquals(venue.getVenueConfigId(),Long.valueOf(venueConfigId));
	    
	    Assert.assertNotNull(venue.hashCode());
		Assert.assertNotNull(new Venue().hashCode());
		Assert.assertTrue(venue.equals(venue));
		Assert.assertFalse(venue.equals(new Venue()));
		Assert.assertFalse(new Venue().equals(venue));
		Assert.assertFalse(new Venue().equals(null));
		Assert.assertEquals(venue, venue);
		Assert.assertNotSame(venue,new Integer(0));	
		
		Object other = new Venue();
		Object different = new String();
		
		Assert.assertNotNull(venue.hashCode());
		Assert.assertFalse(venue.equals(different));
		Assert.assertFalse(venue.equals(other));
		Assert.assertFalse(venue.equals(null));
		
		
		
}
}