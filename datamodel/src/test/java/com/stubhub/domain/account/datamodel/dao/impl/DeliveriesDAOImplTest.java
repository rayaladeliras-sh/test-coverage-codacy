package com.stubhub.domain.account.datamodel.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.mockito.Mockito;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.stubhub.domain.account.datamodel.dao.DeliveriesDAO;
import com.stubhub.domain.account.datamodel.entity.Deliveries;
import com.stubhub.domain.account.datamodel.entity.StubnetUsers;
import com.stubhub.newplatform.persistence.adapter.UTCCalendarToTimestampAdapter;

public class DeliveriesDAOImplTest {
	
	private DeliveriesDAO deliveriesDAO;
	private HibernateTemplate hibernateTemplate;
	private SessionFactory sessionFactory;
	private Session session;
	private SQLQuery query;
	private Long l1 = 200L;

	@BeforeTest
	public void setUp() {
		deliveriesDAO = new DeliveriesDAOImpl();
		hibernateTemplate = Mockito.mock(HibernateTemplate.class);
		sessionFactory = Mockito.mock(SessionFactory.class);
		session = Mockito.mock(Session.class);
		query = Mockito.mock(SQLQuery.class);
		Mockito.when(hibernateTemplate.getSessionFactory()).thenReturn(sessionFactory);
		Mockito.when(sessionFactory.getCurrentSession()).thenReturn(session);
		ReflectionTestUtils.setField(deliveriesDAO, "hibernateTemplate", hibernateTemplate);
	}

	@Test
	public void testGetByTid() {
		Long orderId=1l;
		java.util.GregorianCalendar cal = UTCCalendarToTimestampAdapter.getNewUTCCalendarInstanceStatic();
		List<Deliveries> list = new ArrayList<Deliveries>();
		Deliveries delivery = new Deliveries();
		delivery.setExpectedArrivalDate(cal);
		delivery.setId(orderId);
		delivery.setTid(orderId);
		list.add(delivery);
		
		String namedQuery = "Deliveries.getByTid";
		Mockito.when(session.getNamedQuery(namedQuery)).thenReturn(query);
		Mockito.when(query.list()).thenReturn(list);
		Assert.assertEquals(deliveriesDAO.getByTid(l1).size(), 1);
		Assert.assertEquals(delivery.getExpectedArrivalDate(),cal );
		Assert.assertEquals(delivery.getId(), orderId);
		Assert.assertEquals(delivery.getTid(), orderId);
	}
	
}
