package com.stubhub.domain.account.datamodel.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.mockito.Mockito;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.stubhub.domain.account.datamodel.dao.StubnetUsersDAO;
import com.stubhub.domain.account.datamodel.entity.StubnetUsers;

public class StubnetUsersDAOTest {
	
	private StubnetUsersDAO stubnetUsersDAO;
	private HibernateTemplate hibernateTemplate;
	private SessionFactory sessionFactory;
	private Session session;
	private Query query;
	private Long l1 = 200L;

	@BeforeTest
	public void setUp() {
		stubnetUsersDAO = new StubnetUsersDAOImpl();
		hibernateTemplate = Mockito.mock(HibernateTemplate.class);
		sessionFactory = Mockito.mock(SessionFactory.class);
		session = Mockito.mock(Session.class);
		query = Mockito.mock(Query.class);
		Mockito.when(hibernateTemplate.getSessionFactory()).thenReturn(sessionFactory);
		Mockito.when(sessionFactory.getCurrentSession()).thenReturn(session);
		ReflectionTestUtils.setField(stubnetUsersDAO, "hibernateTemplate", hibernateTemplate);
	}

	@Test
	public void testGetStubnetUserByLoginName() {
		List<StubnetUsers> userList = new ArrayList<StubnetUsers>();
		StubnetUsers user = new StubnetUsers();
		user.setActive(true);
		user.setId(l1);
		user.setNameLogin("bijain");
		userList.add(user);
		
		String namedQuery = "stubnetUsers.getUserByLoginName";
		Mockito.when(session.getNamedQuery(namedQuery)).thenReturn(query);
		Mockito.when(query.list()).thenReturn(userList);
		Assert.assertEquals(stubnetUsersDAO.getStubnetUserByLoginName("bijain").size(), 1);
	}
	
}
