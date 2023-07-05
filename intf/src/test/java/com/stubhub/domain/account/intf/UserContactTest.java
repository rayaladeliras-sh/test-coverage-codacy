package com.stubhub.domain.account.intf;

import junit.framework.Assert;

import org.testng.annotations.Test;

import com.stubhub.integration.partnerorder.entity.UserContact;

public class UserContactTest {
	
	@Test
	public void testUserContatc()
	{
		 Assert.assertEquals("Street 1",buildUserContact().getStreet());
		 Assert.assertEquals("City 1",buildUserContact().getCity());
		 Assert.assertEquals("USA",buildUserContact().getCountry());
		 Assert.assertEquals("1234",buildUserContact().getZip());
		 Assert.assertEquals("123456789",buildUserContact().getPhone());
		 Assert.assertEquals("CA",buildUserContact().getState());
	}
	
	public UserContact buildUserContact()
	{
		UserContact usrCont =  new UserContact();
		
		usrCont.setStreet("Street 1");
		usrCont.setCity("City 1");
		usrCont.setCountry("USA");
		usrCont.setZip("1234");
		usrCont.setPhone("123456789");
		usrCont.setState("CA");
		return usrCont;
		
		
	}
	

}
