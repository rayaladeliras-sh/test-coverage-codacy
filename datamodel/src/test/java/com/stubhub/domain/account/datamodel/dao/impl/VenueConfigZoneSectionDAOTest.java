package com.stubhub.domain.account.datamodel.dao.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

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

import com.stubhub.domain.account.datamodel.dao.VenueConfigZoneSectionDAO;
import com.stubhub.domain.account.datamodel.entity.VenueConfigZoneSection;

public class VenueConfigZoneSectionDAOTest {
	
	private VenueConfigZoneSectionDAO venueConfigZoneSectionDAO;
	
	private HibernateTemplate hibernateTemplate;
	private SessionFactory sessionFactory;
	private Session currentSession;
	private Query query;
	
	@BeforeMethod
	public void setUp() {
		venueConfigZoneSectionDAO = new VenueConfigZoneSectionDAOImpl();
		hibernateTemplate = mock(HibernateTemplate.class);
		sessionFactory = mock(SessionFactory.class);
		currentSession = mock(Session.class);
		query = mock(SQLQuery.class);
		
		ReflectionTestUtils.setField(venueConfigZoneSectionDAO, "hibernateTemplate", hibernateTemplate);
		when(hibernateTemplate.getSessionFactory()).thenReturn(sessionFactory);
	    when(sessionFactory.getCurrentSession()).thenReturn(currentSession);
	}
	
	@Test
	public void testGetZoneSectionByVenueConfigId(){
		Mockito.when(currentSession.getNamedQuery(Mockito.anyString())).thenReturn(query);
		List<VenueConfigZoneSection> listVenueConfigZoneSection = venueConfigZoneSectionDAO.getZoneSectionByVenueConfigId(1234L);
		Assert.assertNotNull(listVenueConfigZoneSection);
	}
	

}
