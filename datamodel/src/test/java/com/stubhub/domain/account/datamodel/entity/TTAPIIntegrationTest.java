package com.stubhub.domain.account.datamodel.entity;

import junit.framework.Assert;

import org.testng.annotations.Test;

public class TTAPIIntegrationTest {
	
	@Test
	public void testTTAPIIntegration() {
		TTAPIintegration apiInt = new TTAPIintegration();
		apiInt.setUsername("user1");
		apiInt.setPass("pass1");
		 Assert.assertEquals("user1",apiInt.getUsername());
		 Assert.assertEquals("pass1",apiInt.getPass());
	}

}
