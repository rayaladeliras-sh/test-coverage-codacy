package com.stubhub.domain.account.datamodel.entity;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TinTest {

	private String tinGuid = "tin guid";
    private String taxId = "tin";
    private String createdBy = "created by";
    private String lastUpdatedBy = "last updated by";
    
    private Tin tin = new Tin();

    @Test
	 public void testGetSet() {
    	
    	tin.setTinGuid(tinGuid);
    	tin.setTin(taxId);
    	tin.setCreatedBy(createdBy);
    	tin.setLastUpdatedBy(lastUpdatedBy);
   	
    	Assert.assertEquals(tin.getTinGuid(), tinGuid);
    	Assert.assertEquals(tin.getTin(), taxId);
    	Assert.assertEquals(tin.getLastUpdatedBy(),lastUpdatedBy);
    	Assert.assertEquals(tin.getCreatedBy(),createdBy);
		
	}
}
