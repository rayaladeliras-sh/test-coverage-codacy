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

import com.stubhub.domain.account.datamodel.dao.OrderProcStatusDAO;
import com.stubhub.domain.account.datamodel.entity.OrderProcStatusDO;

public class OrderProcStatusDAOImplTest {
	
	private OrderProcStatusDAO orderProcStatusDAO;
	private HibernateTemplate hibernateTemplate;
	private SessionFactory sessionFactory;
	private Session session;
	private SQLQuery query;
	private Long l1 = 200L;

	@BeforeTest
	public void setUp() {
		orderProcStatusDAO = new OrderProcStatusDAOImpl();
		hibernateTemplate = Mockito.mock(HibernateTemplate.class);
		sessionFactory = Mockito.mock(SessionFactory.class);
		session = Mockito.mock(Session.class);
		query = Mockito.mock(SQLQuery.class);
		Mockito.when(hibernateTemplate.getSessionFactory()).thenReturn(sessionFactory);
		Mockito.when(sessionFactory.getCurrentSession()).thenReturn(session);
		ReflectionTestUtils.setField(orderProcStatusDAO, "hibernateTemplate", hibernateTemplate);
	}

	@Test
	public void testFindOrderStatusByTransId() {
		List<OrderProcStatusDO> list = new ArrayList<OrderProcStatusDO>();
		OrderProcStatusDO orderProcStatusDO = new OrderProcStatusDO();
		Long orderProcStatusId = 1L;
		Long tid = 2L;
		Long orderProcSubStatusCode = 3L;
		Calendar orderProcStatusEffDate = Calendar.getInstance();
		Calendar orderProcStatusEndDate = Calendar.getInstance();
		Calendar createdDate = Calendar.getInstance();
		Calendar lastUpdatedDate = Calendar.getInstance();
		String createdBy = "bijain";
		String lastUpdatedBy = "bijain";
	    orderProcStatusDO.setOrderProcStatusId(orderProcStatusId);
	    orderProcStatusDO.setTid(tid);
	    orderProcStatusDO.setOrderProcSubStatusCode(orderProcSubStatusCode);
	    orderProcStatusDO.setOrderProcStatusEffDate(orderProcStatusEffDate);
	    orderProcStatusDO.setOrderProcStatusEndDate(orderProcStatusEndDate);
	    orderProcStatusDO.setCreatedDate(createdDate);
	    orderProcStatusDO.setLastUpdatedBy(lastUpdatedBy);
	    orderProcStatusDO.setLastUpdatedDate(lastUpdatedDate);
	    orderProcStatusDO.setCreatedBy(createdBy);
	    orderProcStatusDO.setOrderProcStatusEffDate(Calendar.getInstance());
		list.add(orderProcStatusDO);
		
		String namedQuery = "OrderProcStatus.getOrderProcStatusByTransId";
		Mockito.when(session.getNamedQuery(namedQuery)).thenReturn(query);
		Mockito.when(query.list()).thenReturn(list);
		Assert.assertEquals(orderProcStatusDAO.findOrderStatusByTransId(l1).size(), 1);
//		Mockito.when(query.list()).thenThrow(new NullPointerException());
//		Assert.assertNull(orderProcStatusDAO.findOrderStatusByTransId(l1));
	}

	@Test
	public void testPersist() {
		OrderProcStatusDO orderProcStatusDO = new OrderProcStatusDO();
		orderProcStatusDO.setTid(1l);
		Assert.assertNull(orderProcStatusDAO.persist(orderProcStatusDO));
//		Mockito.when(sessionFactory.getCurrentSession()).thenReturn(null);
//		Assert.assertNull(orderProcStatusDAO.persist(orderProcStatusDO));
	}
}
