package com.stubhub.domain.account.intf;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

import com.stubhub.integration.partnerorder.entity.Response;

import junit.framework.Assert;

public class ResponseTest {

	@Test
	public void testErrors()
	{
		Response resp = buildErrors();
        for (Error err : resp.getErrors())
		{
            Assert.assertEquals("Stubhub internal Error", err.getMessage());
		}
		Assert.assertEquals("Response [errors=" +  resp.getErrors() + "]",resp.toString());
	}
	
	public Response buildErrors()
	{
		Response resp = new Response();
        List<Error> errors = new ArrayList<Error>();
		Error err = new Error("Stubhub internal Error");
        errors.add(err);
		resp.setErrors(errors);
		return resp;
	
		
		
	}
	
	
}
