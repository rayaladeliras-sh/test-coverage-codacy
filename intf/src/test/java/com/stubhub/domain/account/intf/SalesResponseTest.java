package com.stubhub.domain.account.intf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

public class SalesResponseTest {
	
	@Test
	 public void testGetSet() {
		
		
		SalesResponse salesResponse =new SalesResponse();
		List<com.stubhub.domain.account.common.Error> errors = new ArrayList<com.stubhub.domain.account.common.Error>();
		List<SaleResponse> sales = new ArrayList<SaleResponse>();

             try{
            	 
            	salesResponse.setNumFound(5);
				Assert.assertEquals(salesResponse.getNumFound(), 5);						

				salesResponse.setErrors(errors);	
				
			
				Assert.assertEquals(salesResponse.getErrors(),errors);		
				
          	} catch(Exception e){} 
	}

}
