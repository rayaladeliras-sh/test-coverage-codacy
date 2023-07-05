package com.stubhub.domain.account.datamodel.entity;

import javax.persistence.Column;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class SalesCountTest {

	private SalesCount salesCount;
	
	@BeforeTest
	public void setUp() {
		salesCount = new SalesCount();
	}

	@Test
	public void testStubTrans(){
		Long userId = 123L;
		Long unconfirmedCount = 1L;
		Long cancelledCount = 2L;
		Long completed30DaysCount = 3L;
		Long completed180DaysCount = 4L;
		
		salesCount.setCancelledCount(cancelledCount);
		salesCount.setCompleted180DaysCount(completed180DaysCount);
		salesCount.setCompleted30DaysCount(completed30DaysCount);
		salesCount.setUnconfirmedCount(unconfirmedCount);
		salesCount.setUserId(userId);
    
		Assert.assertEquals(salesCount.getCancelledCount(), cancelledCount);
		Assert.assertEquals(salesCount.getCompleted180DaysCount(), completed180DaysCount);
		Assert.assertEquals(salesCount.getCompleted30DaysCount(), completed30DaysCount);
		Assert.assertEquals(salesCount.getUnconfirmedCount(), unconfirmedCount);
		Assert.assertEquals(salesCount.getUserId(), userId);

	}
}
