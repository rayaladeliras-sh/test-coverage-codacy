package com.stubhub.domain.account.datamodel.entity;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class BuysCountTest {

	private BuysCount buysCount;
	
	@BeforeTest
	public void setUp() {
		buysCount = new BuysCount();
	}

	@Test
	public void testStubTrans(){
		Long userId = 123L;
		Long cancelled = 1L;
		Long completed30Days = 2L;
		Long completed180Days = 3L;
		
		buysCount.setUserId(userId);
		buysCount.setCancelled(cancelled);
		buysCount.setCompleted30Days(completed30Days);
		buysCount.setCompleted180Days(completed180Days);
    
		Assert.assertEquals(buysCount.getUserId(), userId);
		Assert.assertEquals(buysCount.getCancelled(), cancelled);
		Assert.assertEquals(buysCount.getCompleted30Days(), completed30Days);
		Assert.assertEquals(buysCount.getCompleted180Days(), completed180Days);
	}
}
