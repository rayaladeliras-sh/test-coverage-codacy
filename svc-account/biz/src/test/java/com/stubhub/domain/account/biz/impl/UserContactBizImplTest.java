package com.stubhub.domain.account.biz.impl;

import com.stubhub.domain.account.biz.intf.UserContactBiz;
import com.stubhub.domain.account.datamodel.dao.UserContactDAO;
import com.stubhub.domain.account.datamodel.entity.UserContact;
import junit.framework.Assert;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Created by jicui on 9/6/15.
 */
@Test
public class UserContactBizImplTest {
    UserContactBiz userContactBiz;
    UserContactDAO userContactDAO;

    @BeforeMethod
    void init() {
        userContactBiz = new UserContactBizImpl();
        userContactDAO = Mockito.mock(UserContactDAO.class);
        ReflectionTestUtils.setField(userContactBiz, "userContactDAO", userContactDAO);
    }

    @Test
    public void testGetUserContactById() {
        UserContact userContact = new UserContact();
        userContact.setCity("Shanghai");
        userContact.setCountry("China");
        userContact.setState("SH");
        userContact.setUserContactId(123L);
        userContact.setZip("200000");
        Mockito.when(userContactDAO.getUserContactById(Mockito.anyLong())).thenReturn(userContact);
        UserContact uc1 = userContactBiz.getUserContactById(123L);
        Assert.assertEquals(uc1.getCity(), "Shanghai");
        Assert.assertEquals(uc1.getCountry(), "China");
        Assert.assertEquals(uc1.getState(), "SH");
        Assert.assertEquals(uc1.getZip(), "200000");
        Assert.assertEquals(uc1.getUserContactId(), Long.valueOf(123L));
    }

    @Test
    public void testGetDefaultUserContactByOwernId() {
        UserContact userContact = new UserContact();
        userContact.setUserContactId(1L);
        Mockito.when(userContactDAO.getDefaultUserContactByOwnerId(Mockito.anyLong())).thenReturn(userContact);
        UserContact uc1 = userContactBiz.getDefaultUserContactByOwernId(123L);
        Assert.assertEquals(uc1.getUserContactId(), Long.valueOf(1));
    }
}