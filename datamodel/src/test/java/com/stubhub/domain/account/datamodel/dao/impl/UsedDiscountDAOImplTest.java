package com.stubhub.domain.account.datamodel.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.mockito.Mockito;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.stubhub.domain.account.datamodel.dao.UsedDiscountDAO;
import com.stubhub.domain.account.datamodel.entity.UsedDiscount;

public class UsedDiscountDAOImplTest {
	
	private UsedDiscountDAO usedDiscountDAO;
	private HibernateTemplate hibernateTemplate;
	private SessionFactory sessionFactory;
	private Session session;
	private Transaction tx;
	private SQLQuery query;
	private Long l1 = 200L;
	private Long l2 = 404L;

	@BeforeTest
	public void setUp() {
		usedDiscountDAO = new UsedDiscountDAOImpl();
		hibernateTemplate = Mockito.mock(HibernateTemplate.class);
		sessionFactory = Mockito.mock(SessionFactory.class);
		session = Mockito.mock(Session.class);
		query = Mockito.mock(SQLQuery.class);
		Mockito.when(hibernateTemplate.getSessionFactory()).thenReturn(sessionFactory);
		Mockito.when(sessionFactory.getCurrentSession()).thenReturn(session);
		ReflectionTestUtils.setField(usedDiscountDAO, "hibernateTemplate", hibernateTemplate);
	}
	
	@Test
	public void testPersist() {	
		List<UsedDiscount> lst = new ArrayList<UsedDiscount>();
		UsedDiscount usedDiscount = new UsedDiscount();
		usedDiscount.setDiscountId(l1);
		usedDiscount.setAmountUsed(new BigDecimal(25.00));
		usedDiscount.setCurrencyCode("USD");
		lst.add(usedDiscount);
		usedDiscount = new UsedDiscount();
		usedDiscount.setDiscountId(l1);
		usedDiscount.setAmountUsed(new BigDecimal(25.00));
		usedDiscount.setCurrencyCode("USD");
		lst.add(usedDiscount);
		UsedDiscountDAO usedDiscountDAO = new UsedDiscountDAOImpl();
		hibernateTemplate = Mockito.mock(HibernateTemplate.class);
		sessionFactory = Mockito.mock(SessionFactory.class);
		session = Mockito.mock(Session.class);
		tx = Mockito.mock(Transaction.class);
		Mockito.when(hibernateTemplate.getSessionFactory()).thenReturn(sessionFactory);
		Mockito.when(sessionFactory.openSession()).thenReturn(session);
		Mockito.when(session.beginTransaction()).thenReturn(tx);
		ReflectionTestUtils.setField(usedDiscountDAO, "hibernateTemplate", hibernateTemplate);
		Assert.assertNotNull(usedDiscountDAO.persist(lst));
	}
	
	@Test
	public void testFindByTid() {	
		List<UsedDiscount> lst = new ArrayList<UsedDiscount>();
		UsedDiscount usedDiscount = new UsedDiscount();
		usedDiscount.setDiscountId(l1);
		usedDiscount.setAmountUsed(new BigDecimal(25.00));
		usedDiscount.setCurrencyCode("USD");
		lst.add(usedDiscount);
		usedDiscount = new UsedDiscount();
		usedDiscount.setDiscountId(l1);
		usedDiscount.setAmountUsed(new BigDecimal(25.00));
		usedDiscount.setCurrencyCode("USD");
		lst.add(usedDiscount);
		String namedQuery = "UsedDiscount.getByTid";
		Mockito.when(session.getNamedQuery(namedQuery)).thenReturn(query);
		Mockito.when(query.list()).thenReturn(lst);
		Assert.assertNotNull(usedDiscountDAO.findByTid(l1));
	}

	@Test
	public void testGetUsedDiscountsByTransId() {
		List<UsedDiscount> list = new ArrayList<UsedDiscount>();
		UsedDiscount usedDiscount = new UsedDiscount();
		usedDiscount.setDiscountId(l1);
		usedDiscount.setAmountUsed(new BigDecimal(25.00));
		usedDiscount.setCurrencyCode("USD");
		list.add(usedDiscount);
		
		String namedQuery = "SELECT ud.amount_used AS used_discount, ud.currency_code AS currency_code, dt.id AS discount_id, dt.type AS discount_type, d.description AS discount_desc FROM used_discounts ud, discounts d, discount_types dt WHERE ud.tid =:arg1 AND ud.discount_id = d.id AND d.discount_type_id = dt.id ";
		Mockito.when(session.createSQLQuery(namedQuery)).thenReturn(query);
		Mockito.when(query.list()).thenReturn(list);
		Assert.assertEquals(usedDiscountDAO.findDetailByTid(l1).size(), 1);
	}
}
