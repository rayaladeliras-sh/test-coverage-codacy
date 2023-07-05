package com.stubhub.domain.account.datamodel.entity;

import javax.persistence.Column;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class ListingsCountTest {

	private ListingsCount listingsCount;
	
	@BeforeTest
	public void setUp() {
		listingsCount = new ListingsCount();
	}

	@Test
	public void testStubTrans(){
		Long userId = 123L;
		Long activeCount = 1L;
		Long inactiveCount = 2L;
		Long pendingLockCount = 3L;
		Long incompleteCount = 4L;
		Long pendingLmsApprovalCount = 5L;
		
		listingsCount.setUserId(userId);
		listingsCount.setActiveCount(activeCount);
		listingsCount.setInactiveCount(inactiveCount);
		listingsCount.setIncompleteCount(incompleteCount);
		listingsCount.setPendingLmsApprovalCount(pendingLmsApprovalCount);
		listingsCount.setPendingLockCount(pendingLockCount);
    
		Assert.assertEquals(listingsCount.getActiveCount(), activeCount);
		Assert.assertEquals(listingsCount.getInactiveCount(), inactiveCount);
		Assert.assertEquals(listingsCount.getIncompleteCount(), incompleteCount);
		Assert.assertEquals(listingsCount.getPendingLmsApprovalCount(), pendingLmsApprovalCount);
		Assert.assertEquals(listingsCount.getPendingLockCount(), pendingLockCount);
		Assert.assertEquals(listingsCount.getUserId(), userId);	

	}
}
