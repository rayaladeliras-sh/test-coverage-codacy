package com.stubhub.domain.account.datamodel.entity;

import junit.framework.Assert;

import org.testng.annotations.Test;

public class FulfillmentTypeTest {
	
	
	@Test
	public void testFulfillmentType()
	{
		FulfillmentTypeEnum.getFulfillmentTypeEnumByName("Barcode");
		 Assert.assertEquals(FulfillmentTypeEnum.Barcode,FulfillmentTypeEnum.getFulfillmentTypeEnumByName("Barcode"));
		 Assert.assertEquals(FulfillmentTypeEnum.PDF,FulfillmentTypeEnum.getFulfillmentTypeEnumByName("PDF"));
		 Assert.assertEquals(FulfillmentTypeEnum.FedEx,FulfillmentTypeEnum.getFulfillmentTypeEnumByName("FedEx"));
		 Assert.assertEquals(FulfillmentTypeEnum.LMS,FulfillmentTypeEnum.getFulfillmentTypeEnumByName("LMS"));
		 Assert.assertEquals(FulfillmentTypeEnum.UPS,FulfillmentTypeEnum.getFulfillmentTypeEnumByName("UPS"));
		 Assert.assertEquals(FulfillmentTypeEnum.Shipping,FulfillmentTypeEnum.getFulfillmentTypeEnumByName("Shipping"));
		 Assert.assertEquals(FulfillmentTypeEnum.Other,FulfillmentTypeEnum.getFulfillmentTypeEnumByName("Other"));
		 
		
		 
		 
	}

}
