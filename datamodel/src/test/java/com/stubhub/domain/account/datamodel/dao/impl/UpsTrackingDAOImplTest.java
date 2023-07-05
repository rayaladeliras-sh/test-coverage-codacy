package com.stubhub.domain.account.datamodel.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.mockito.Mockito;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.stubhub.domain.account.datamodel.dao.UpsTrackingDAO;
import com.stubhub.domain.account.datamodel.entity.UpsTracking;

public class UpsTrackingDAOImplTest {
	
	private UpsTrackingDAO upsTrackingDAO;
	private HibernateTemplate hibernateTemplate;
	private SessionFactory sessionFactory;
	private Session session;
	private SQLQuery query;
	private Long l1 = 200L;
	private Long l2 = 404L;

	@BeforeTest
	public void setUp() {
		upsTrackingDAO = new UpsTrackingDAOImpl();
		hibernateTemplate = Mockito.mock(HibernateTemplate.class);
		sessionFactory = Mockito.mock(SessionFactory.class);
		session = Mockito.mock(Session.class);
		query = Mockito.mock(SQLQuery.class);
		Mockito.when(hibernateTemplate.getSessionFactory()).thenReturn(sessionFactory);
		Mockito.when(sessionFactory.getCurrentSession()).thenReturn(session);
		ReflectionTestUtils.setField(upsTrackingDAO, "hibernateTemplate", hibernateTemplate);
	}

	@Test
	public void testCheckUPSOrder() {
		List<UpsTracking> list = new ArrayList<UpsTracking>();
		UpsTracking upsTracking = new UpsTracking();
		upsTracking.setOrderId(l1);
		list.add(upsTracking);
		
		String namedQuery = "UpsTracking.checkUpsOrder";
		Mockito.when(session.getNamedQuery(namedQuery)).thenReturn(query);
		Mockito.when(query.list()).thenReturn(list);
		Assert.assertEquals(upsTrackingDAO.checkUPSOrder(l1).size(), 1);
		Mockito.when(query.list()).thenReturn(null);
		Assert.assertNull(upsTrackingDAO.checkUPSOrder(l2));
	}
}
