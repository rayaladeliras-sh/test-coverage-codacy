package com.stubhub.domain.account.datamodel.dao.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.stubhub.domain.account.datamodel.entity.Tin;
public class TinDAOImpTest {

	private TinDAOImpl tinDAOImpl;
	
	@Mock private HibernateTemplate hibernateTemplate;
	@Mock private SessionFactory sessionFactory;
	@Mock private Session currentSession;
	@Mock private SQLQuery sqlQuery;
		
	@BeforeMethod
	public void setUp(){
		tinDAOImpl = new TinDAOImpl();
		hibernateTemplate = mock(HibernateTemplate.class);
		sessionFactory = mock(SessionFactory.class);
		currentSession = mock(Session.class);
		sqlQuery = mock(SQLQuery.class);

		ReflectionTestUtils.setField(tinDAOImpl, "hibernateTemplate", hibernateTemplate);

		when(hibernateTemplate.getSessionFactory()).thenReturn(sessionFactory);
	    when(sessionFactory.getCurrentSession()).thenReturn(currentSession);
	    when(currentSession.getNamedQuery(Mockito.anyString())).thenReturn(sqlQuery);
	    when(currentSession.save(Mockito.any(Tin.class))).thenReturn("tinGuid");
	}

	@Test
	public void testGetTinByGuid(){
		Tin tin = new Tin();
		when(sqlQuery.uniqueResult()).thenReturn(tin);
		Tin newTin = tinDAOImpl.getTinByGuid("guid");
		Assert.assertNotNull(newTin);
	}

	@Test
	public void testGetTinByGuidNull(){
		Tin newTin = tinDAOImpl.getTinByGuid(null);
		Assert.assertNull(newTin);
	}

	@Test
	public void testGetTinByGuidTinNull(){
		when(sqlQuery.uniqueResult()).thenReturn(null);
		Tin newTin = tinDAOImpl.getTinByGuid("guid");
		Assert.assertNull(newTin);
	}

	@Test
	public void testAddTinNullParam(){
		String tinGuid = tinDAOImpl.addTin(null);
		Assert.assertNull(tinGuid);
	}

	@Test
	public void testAddTin(){
		Tin tin = new Tin();
		String tinGuid = tinDAOImpl.addTin(tin);
		Assert.assertEquals(tinGuid, "tinGuid");
	}

	@Test
	public void testUpdateTinNull(){
		Tin tin = new Tin();
		tin.setTin("tin");
		tin.setLastUpdatedBy("last updated");
		tin.setTinGuid("tinGuid");
		when(sqlQuery.executeUpdate()).thenReturn(1);
		int count = tinDAOImpl.updateTin(tin);
		Assert.assertEquals(1, count);
	}

	@Test
	public void testUpdateTinNullParam(){
		when(sqlQuery.executeUpdate()).thenReturn(1);
		int count = tinDAOImpl.updateTin(null);
		Assert.assertEquals(0, count);
	}

}
