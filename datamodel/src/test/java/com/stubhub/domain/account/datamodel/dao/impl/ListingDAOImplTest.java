package com.stubhub.domain.account.datamodel.dao.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;

import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class ListingDAOImplTest {
	
	@InjectMocks
    private ListingDAOImpl dao;

	@Mock
	private HibernateTemplate hibernateTemplate;

	@Mock
	SessionFactory sessionFactory;

	@Mock
	Session session;

	@Mock
	SQLQuery query;

	@BeforeTest
	public void beforeTest() {
		MockitoAnnotations.initMocks(this);
	}

	@BeforeMethod
	public void beforeMethod() {
		//dao = new ListingDAOImpl();
	//    hibernateTemplate = mock(HibernateTemplate.class);
	  //  sessionFactory = mock(SessionFactory.class);
	    //session = mock(Session.class);
		when(hibernateTemplate.getSessionFactory()).thenReturn(sessionFactory);
		when(sessionFactory.getCurrentSession()).thenReturn(session);
	}
	
	@Test
	public void testUpdateSellerPaymentTypeId(){
		Long listingId = 1234L;
		Long sellerPaymentTypeId = 2L;
		when(session.createSQLQuery(Mockito.anyString())).thenReturn(query);
		when(query.executeUpdate()).thenReturn(1);
		System.out.println("dao value : " +dao);
		int result = dao.updateListingPaymentType(listingId, sellerPaymentTypeId);
		Assert.assertEquals(result, 1);
	}


}
