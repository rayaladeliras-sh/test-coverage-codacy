package com.stubhub.domain.account.datamodel.dao.impl;

import com.stubhub.domain.account.datamodel.dao.UserDAO;
import com.stubhub.domain.account.datamodel.entity.UserDO;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import org.testng.Assert;

/**
 * Created at 11/7/15 11:32 AM
 *
 * @author : Caron Zhao
 * @version : 1.0.0
 */
public class UserDAOImplTest {
    private UserDAO userDAO;

    @Mock
    private HibernateTemplate hibernateTemplate;

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;
    
    @Mock
    SQLQuery sql;

    @BeforeTest
    public void setUp() {
    	MockitoAnnotations.initMocks(this);
        userDAO = new UserDAOImpl();
        hibernateTemplate = mock(HibernateTemplate.class);
        sessionFactory = mock(SessionFactory.class);
        session = mock(Session.class);
        when(hibernateTemplate.getSessionFactory()).thenReturn(sessionFactory);
        when(sessionFactory.getCurrentSession()).thenReturn(session);
        ReflectionTestUtils.setField(userDAO, "hibernateTemplate", hibernateTemplate);
    }

    @Test
    public void testFindUserByGuid() {
        String hql = " from UserDO where userGuid = :userGuid";
        Query query = mock(Query.class);
        when(session.createQuery(hql)).thenReturn(query);
        UserDO expected = new UserDO();
        expected.setUserId(Long.valueOf(1));
        when(query.uniqueResult()).thenReturn(expected);
        UserDO result = userDAO.findUserByGuid("guid");
        assertNotNull(result);
        assertEquals(expected, result);
    }

    @Test
    public void testFindUserByGuid_NotFound() {
        String hql = " from UserDO where userGuid = :userGuid";
        Query query = mock(Query.class);
        when(session.createQuery(hql)).thenReturn(query);
        when(query.uniqueResult()).thenReturn(null);
        UserDO result = userDAO.findUserByGuid("guid");
        assertNull(result);
    }
    
    
    @Test
    public void testGetUserSellerPaymentType(){
    	
    	Number number = Mockito.mock(Number.class);
   		List<Number> result = new ArrayList<Number>();
   		result.add(number);
    	when(session.getNamedQuery(Mockito.anyString())).thenReturn(sql);
    	when(session.createSQLQuery(Mockito.anyString())).thenReturn(sql);
        when(sql.list()).thenReturn(result);
    	Long sellerPaymentTypeId = userDAO.getUserSellerPaymentType(1L);
    	Assert.assertNotNull(sellerPaymentTypeId);
    	when(session.createSQLQuery(Mockito.anyString())).thenReturn(sql);
        when(sql.list()).thenReturn(null);
    	sellerPaymentTypeId = userDAO.getUserSellerPaymentType(1L);
    	Assert.assertNull(null);
  	
    }
    
}