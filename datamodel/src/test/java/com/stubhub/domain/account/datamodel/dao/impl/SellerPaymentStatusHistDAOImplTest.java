package com.stubhub.domain.account.datamodel.dao.impl;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.stubhub.domain.account.datamodel.entity.SellerPaymentStatusHist;

public class SellerPaymentStatusHistDAOImplTest {

	private SellerPaymentStatusHistDAOImpl sellerPaymentStatusHistDAO;
	private HibernateTemplate hibernateTemplate;
	private SessionFactory sessionFactory;
	private Session session;
	private Query query;
	
	@BeforeTest
	public void setUp() {
		sellerPaymentStatusHistDAO = new SellerPaymentStatusHistDAOImpl();
		hibernateTemplate = Mockito.mock(HibernateTemplate.class);
		sessionFactory = Mockito.mock(SessionFactory.class);
		session = Mockito.mock(Session.class);
		query = Mockito.mock(Query.class);
		Mockito.when(hibernateTemplate.getSessionFactory()).thenReturn(sessionFactory);
		Mockito.when(sessionFactory.getCurrentSession()).thenReturn(session);
		Mockito.when(session.getNamedQuery(Matchers.anyString())).thenReturn(query);
		ReflectionTestUtils.setField(sellerPaymentStatusHistDAO, "hibernateTemplate", hibernateTemplate);
		
	}
	
	@Test
	public void testUpdateEndDate(){
		Long paymentId = 1234L;
		sellerPaymentStatusHistDAO.updateEndDate(paymentId);
		Mockito.verify(query).executeUpdate();
	}
	
	@Test
	public void testSaveSellerPaymentStatusHist(){
		SellerPaymentStatusHist entity = new SellerPaymentStatusHist();
		sellerPaymentStatusHistDAO.saveSellerPaymentStatusHist(entity);
		Mockito.verify(session).saveOrUpdate(entity);
	}
	
}
