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

import com.stubhub.domain.account.datamodel.dao.TransactionCountDAO;
import com.stubhub.domain.account.datamodel.entity.BuysCount;
import com.stubhub.domain.account.datamodel.entity.ListingsCount;
import com.stubhub.domain.account.datamodel.entity.SalesCount;
import com.stubhub.domain.account.datamodel.entity.UpsTracking;

public class TransactionCountDAOImplTest {
	
	private TransactionCountDAO transactionCountDAO;
	private HibernateTemplate hibernateTemplate;
	private SessionFactory sessionFactory;
	private Session session;
	private SQLQuery query;
	private String uid = "123";
	private Long count = 1L;

	@BeforeTest
	public void setUp() {
		transactionCountDAO = new TransactionCountDAOImpl();
		hibernateTemplate = Mockito.mock(HibernateTemplate.class);
		sessionFactory = Mockito.mock(SessionFactory.class);
		session = Mockito.mock(Session.class);
		query = Mockito.mock(SQLQuery.class);
		Mockito.when(hibernateTemplate.getSessionFactory()).thenReturn(sessionFactory);
		Mockito.when(sessionFactory.getCurrentSession()).thenReturn(session);
		ReflectionTestUtils.setField(transactionCountDAO, "hibernateTemplate", hibernateTemplate);
	}

	@Test
	public void testFindListingsCountByUserId() {
		String namedQuery = "ListingsCount.getCountByUserId";
		Mockito.when(session.getNamedQuery(namedQuery)).thenReturn(query);
		Mockito.when(query.list()).thenReturn(getListings());
		Assert.assertEquals(transactionCountDAO.findListingsCountByUserId(uid).size(), 1);
		Mockito.when(query.list()).thenReturn(null);
		Assert.assertNull(transactionCountDAO.findListingsCountByUserId(uid));
	}
	
	@Test
	public void testFindSalesCountByUserId() {
		String namedQuery = "SalesCount.getCountByUserId";
		Mockito.when(session.getNamedQuery(namedQuery)).thenReturn(query);
		Mockito.when(query.list()).thenReturn(getListings());
		Assert.assertEquals(transactionCountDAO.findSalesCountByUserId(uid).size(), 1);
		Mockito.when(query.list()).thenReturn(null);
		Assert.assertNull(transactionCountDAO.findSalesCountByUserId(uid));
	}
	
	@Test
	public void testFindBuysCountByUserId() {
		String namedQuery = "BuysCount.getCountByUserId";
		Mockito.when(session.getNamedQuery(namedQuery)).thenReturn(query);
		Mockito.when(query.list()).thenReturn(getListings());
		Assert.assertEquals(transactionCountDAO.findBuysCountByUserId(uid).size(), 1);
		Mockito.when(query.list()).thenReturn(null);
		Assert.assertNull(transactionCountDAO.findBuysCountByUserId(uid));
	}
	
	public List<ListingsCount> getListings(){
		ListingsCount listingsCount = new ListingsCount();
		listingsCount.setActiveCount(count);
		listingsCount.setInactiveCount(count);
		listingsCount.setIncompleteCount(count);
		listingsCount.setPendingLmsApprovalCount(count);
		listingsCount.setPendingLockCount(count);
		listingsCount.setUserId(Long.getLong(uid));
		List<ListingsCount> list = new ArrayList<ListingsCount>();
		list.add(listingsCount);
		return list;
	}
	
	public List<SalesCount> getSales(){
		SalesCount salesCount = new SalesCount();
		salesCount.setCancelledCount(count);
		salesCount.setCompleted180DaysCount(count);
		salesCount.setCompleted30DaysCount(count);
		salesCount.setUnconfirmedCount(count);
		salesCount.setUserId(Long.getLong(uid));
		List<SalesCount> list = new ArrayList<SalesCount>();
		list.add(salesCount);
		return list;
	}
	
	public List<BuysCount> getOrders(){
		BuysCount buysCount = new BuysCount();
		buysCount.setCancelled(count);
		buysCount.setCompleted180Days(count);
		buysCount.setCompleted30Days(count);
		buysCount.setUserId(Long.getLong(uid));
		List<BuysCount> list = new ArrayList<BuysCount>();
		list.add(buysCount);
		return list;
	}
}
