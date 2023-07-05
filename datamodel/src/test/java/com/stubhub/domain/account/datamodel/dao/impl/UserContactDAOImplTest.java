package com.stubhub.domain.account.datamodel.dao.impl;

import com.stubhub.domain.account.datamodel.entity.UserContact;
import junit.framework.Assert;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.mockito.Mockito;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by jicui on 9/6/15.
 */
@Test
public class UserContactDAOImplTest {
    private UserContactDAOImpl userContactDAO;
    private HibernateTemplate hibernateTemplate;
    private SessionFactory sessionFactory;
    private Session currentSession;
    private Query query;

    @BeforeMethod
    public void setUp() {
        userContactDAO = new UserContactDAOImpl();
        hibernateTemplate = mock(HibernateTemplate.class);
        sessionFactory = mock(SessionFactory.class);
        currentSession = mock(Session.class);
        query = mock(SQLQuery.class);

        ReflectionTestUtils.setField(userContactDAO, "hibernateTemplate", hibernateTemplate);
        when(hibernateTemplate.getSessionFactory()).thenReturn(sessionFactory);
        when(sessionFactory.getCurrentSession()).thenReturn(currentSession);

    }

    @Test
    public void testGetUserContactById_NoRecord() {
        when(currentSession.getNamedQuery(Mockito.anyString())).thenReturn(query);
        UserContact userContact = userContactDAO.getUserContactById(123L);
        Assert.assertNull(userContact);
    }

    @Test
    public void testGetUserContactById() {
        when(currentSession.getNamedQuery(Mockito.anyString())).thenReturn(query);
        List<UserContact> result = new ArrayList<UserContact>();
        result.add(new UserContact());
        when(query.list()).thenReturn(result);
        UserContact userContact = userContactDAO.getUserContactById(123L);
        Assert.assertNotNull(userContact);
    }

    @Test
    public void testGetDefaultUserContactByOwnerId_NoRecord() {
        when(currentSession.getNamedQuery(Mockito.anyString())).thenReturn(query);
        UserContact userContact = userContactDAO.getDefaultUserContactByOwnerId(123L);
        Assert.assertNull(userContact);
    }

    @Test
    public void testGetDefaultUserContactByOwnerId() {
        when(currentSession.getNamedQuery(Mockito.anyString())).thenReturn(query);
        List<UserContact> result = new ArrayList<UserContact>();
        result.add(new UserContact());
        when(query.list()).thenReturn(result);
        UserContact userContact = userContactDAO.getDefaultUserContactByOwnerId(123L);
        Assert.assertNotNull(userContact);
    }
}