package com.stubhub.domain.account.datamodel.entity;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class UpsTrackingTest {

	private UpsTracking upsTracking;
	
	@BeforeTest
	public void setUp() {
		upsTracking = new UpsTracking();
	}

	@Test
	public void testUpsTracking(){
	    Long id = 2000L;
	    upsTracking.setOrderId(id);
    
		Assert.assertEquals(upsTracking.getOrderId(), id);
	}
}
