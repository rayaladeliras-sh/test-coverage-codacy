package com.stubhub.domain.account.datamodel.entity;

import org.testng.annotations.Test;

import junit.framework.Assert;

public class UserContactTest {

    public UserContact buildUserContact() {
        UserContact userCont = new UserContact();
        userCont.setUserContactId(1L);
        userCont.setOwnerId(2L);
        userCont.setCountry("USA");
        userCont.setCity("LA");
        userCont.setZip("12345");
        userCont.setFirstName("First");
        userCont.setLastName("Last");
        userCont.setStreet("street");
        userCont.setPhoneNumber("12345");
        userCont.setState("CA");
        userCont.setDefaultContact(1L);
        userCont.setUserContactGuid("abcd");
        userCont.setEmail("test@stubhub.com");
        userCont.setPhoneCountryCd("1");
        return userCont;
    }

    @Test
    public void testUserContact() {
        Assert.assertEquals(1L, buildUserContact().getUserContactId().longValue());
        Assert.assertEquals(2L, buildUserContact().getOwnerId().longValue());
        Assert.assertEquals("street", buildUserContact().getStreet());
        Assert.assertEquals("LA", buildUserContact().getCity());
        Assert.assertEquals("USA", buildUserContact().getCountry());
        Assert.assertEquals("12345", buildUserContact().getZip());
        Assert.assertEquals("12345", buildUserContact().getPhoneNumber());
        Assert.assertEquals("CA", buildUserContact().getState());
        Assert.assertEquals("First", buildUserContact().getFirstName());
        Assert.assertEquals("Last", buildUserContact().getLastName());
        Assert.assertEquals(1L, buildUserContact().getDefaultContact().longValue());
        Assert.assertEquals("abcd", buildUserContact().getUserContactGuid());
        Assert.assertEquals("test@stubhub.com", buildUserContact().getEmail());
    }
}