package com.stubhub.domain.account.datamodel.dao.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.mockito.Mockito;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.stubhub.domain.account.datamodel.dao.SellerCcTransDAO;

public class SellerCcTransDAOTest {
	
private SellerCcTransDAO sellerCcTransDAO;
	
	private HibernateTemplate hibernateTemplate;
	private SessionFactory sessionFactory;
	private Session currentSession;
	private Query query;
	
	@BeforeMethod
	public void setUp() {
		sellerCcTransDAO = new SellerCcTransDAOImpl();
		hibernateTemplate = mock(HibernateTemplate.class);
		sessionFactory = mock(SessionFactory.class);
		currentSession = mock(Session.class);
		query = mock(SQLQuery.class);
		
		ReflectionTestUtils.setField(sellerCcTransDAO, "hibernateTemplate", hibernateTemplate);
		when(hibernateTemplate.getSessionFactory()).thenReturn(sessionFactory);
	    when(sessionFactory.getCurrentSession()).thenReturn(currentSession);
	}
	
	@Test
	public void testGetSellerCcTransBySellerCcId() {
		when(currentSession.getNamedQuery(Mockito.anyString())).thenReturn(query);
		Set<Long> ccIds = new HashSet<Long>();
		ccIds.add(1234L);
		sellerCcTransDAO.getSellerCcTransBySellerCcId(ccIds, null, null, null, null, null, null);
	}
	
	@Test
	public void testGetSellerCcTransBySellerCcIdWithParams() {
		when(currentSession.createQuery(Mockito.anyString())).thenReturn(query);
		Set<Long> ccIds = new HashSet<Long>();
		ccIds.add(1234L);
		sellerCcTransDAO.getSellerCcTransBySellerCcId(ccIds, Calendar.getInstance(), Calendar.getInstance(), null, null, null, null);
	}

    @Test
    public void testCountSellerCcTransBySellerCcId() {
        when(currentSession.getNamedQuery(Mockito.anyString())).thenReturn(query);
		when(currentSession.createQuery(Mockito.anyString())).thenReturn(query);
        Set<Long> ccIds = new HashSet<Long>();
        ccIds.add(1234L);
        List<Long> result = new ArrayList<Long>();
        result.add(5L);
        when(query.iterate()).thenReturn(result.iterator());
        Assert.assertEquals(5L, sellerCcTransDAO.countSellerCcTransBySellerCcId(ccIds, null, null, null, "GBP"));
    }
}


