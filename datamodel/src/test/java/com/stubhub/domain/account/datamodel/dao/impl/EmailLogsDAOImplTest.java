package com.stubhub.domain.account.datamodel.dao.impl;

import java.util.ArrayList;
import java.util.Calendar;
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

import com.stubhub.domain.account.datamodel.dao.EmailLogsDAO;
import com.stubhub.domain.account.datamodel.entity.EmailLog;

public class EmailLogsDAOImplTest {
	
	private EmailLogsDAO emailLogsDAO;
	private EmailLog emailLog;
	private HibernateTemplate hibernateTemplate;
	private SessionFactory sessionFactory;
	private Session session;
	private SQLQuery query;
	private Calendar fromDate, toDate;
	

	@BeforeTest
	public void setUp() {
		emailLogsDAO = new EmailLogsDAOImpl();
		hibernateTemplate = Mockito.mock(HibernateTemplate.class);
		sessionFactory = Mockito.mock(SessionFactory.class);
		session = Mockito.mock(Session.class);
		query = Mockito.mock(SQLQuery.class);
		Mockito.when(hibernateTemplate.getSessionFactory()).thenReturn(sessionFactory);
		Mockito.when(sessionFactory.getCurrentSession()).thenReturn(session);
		ReflectionTestUtils.setField(emailLogsDAO, "hibernateTemplate", hibernateTemplate);
		fromDate = Calendar.getInstance();
		toDate = Calendar.getInstance();
		fromDate.add(Calendar.DAY_OF_YEAR, -30);
	}
	
    @Test
	public void testGetEmailById() {
		List<EmailLog> list = new ArrayList<EmailLog>();
		EmailLog email = new EmailLog();
		email.setEmailId(1L);
		list.add(email);		
		String namedQuery = "EmailLogs.getEmailById";
		Mockito.when(session.getNamedQuery(namedQuery)).thenReturn(query);
		Mockito.when(query.list()).thenReturn(list);
		Assert.assertEquals(emailLogsDAO.getEmailById(1L).getEmailId(), email.getEmailId());
	}

	@Test
	public void getUserMessagesByUserId() {
		List<EmailLog> list = new ArrayList<EmailLog>();
		EmailLog email = new EmailLog();
		email.setEmailId(1L);
		list.add(email);
		String namedQuery = "UserMessages.getUserMessagesByUserId";
		Mockito.when(session.getNamedQuery(namedQuery)).thenReturn(query);
		Mockito.when(query.list()).thenReturn(list);
		Assert.assertNotNull(emailLogsDAO.getUserMessagesByUserId(1L, fromDate, toDate, "1", "2"));
	}
	
	@Test
	public void getEmailLogsByUserIdAndCrteria() {
		List<EmailLog> list = new ArrayList<EmailLog>();
		EmailLog email = new EmailLog();
		email.setEmailId(1L);
		list.add(email);
		Mockito.when(session.createSQLQuery(Mockito.anyString())).thenReturn(query);
		Mockito.when(query.addEntity(Mockito.any(Class.class))).thenReturn(query);
		Mockito.when(query.list()).thenReturn(list);
		Assert.assertNotNull(emailLogsDAO.getEmailLogsByUserIdAndCriteria(1L,1L,2L,"sample",fromDate, toDate, "1", "2"));
	}
	
	@Test
	public void getUserMessagesByUserIdException() {
		String namedQuery = "UserMessages.getUserMessagesByUserId";
		Mockito.when(session.getNamedQuery(namedQuery)).thenReturn(query);
		Mockito.when(query.list()).thenReturn(null);
		Assert.assertNull(emailLogsDAO.getUserMessagesByUserId(1L, fromDate, toDate, "1", "2"));
	}
	
	@Test
	public void getEmailLogsByUserIdAndCrteriaException() {
		Mockito.when(session.createSQLQuery(Mockito.anyString())).thenReturn(query);
		Mockito.when(query.addEntity(Mockito.any(Class.class))).thenReturn(query);
		Mockito.when(query.list()).thenReturn(null);
		Assert.assertNull(emailLogsDAO.getEmailLogsByUserIdAndCriteria(1L,1L,2L,"sample",fromDate, toDate, "1", "2"));
	}
	
	
}
