package com.stubhub.domain.account.datamodel.dao.impl;

import java.math.BigDecimal;
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
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.stubhub.common.exception.RecordNotFoundForIdException;
import com.stubhub.domain.account.datamodel.dao.StubTransDAO;
import com.stubhub.domain.account.datamodel.entity.StubTrans;
import com.stubhub.domain.account.datamodel.entity.UpsTracking;

public class StubTransDAOImplTest {
	
	private StubTransDAO stubTransDAO;
	private HibernateTemplate hibernateTemplate;
	private SessionFactory sessionFactory;
	private Session session;
	private SQLQuery query;
	private Long order1 = 200L;
	private Long contactId = 200L;
	private String csrRep = "BJ";
	private Calendar dateAdded = Calendar.getInstance();
	private Long order2 = 404L;
	private Long newStatus = 38920801850L;

	@BeforeMethod
	public void setUp() {
		stubTransDAO = new StubTransDAOImpl();
		hibernateTemplate = Mockito.mock(HibernateTemplate.class);
		sessionFactory = Mockito.mock(SessionFactory.class);
		session = Mockito.mock(Session.class);
		query = Mockito.mock(SQLQuery.class);
		Mockito.when(hibernateTemplate.getSessionFactory()).thenReturn(sessionFactory);
		Mockito.when(sessionFactory.getCurrentSession()).thenReturn(session);
		ReflectionTestUtils.setField(stubTransDAO, "hibernateTemplate", hibernateTemplate);
	}

	@Test
	public void testGetOrderProcSubStatusCode() {
		List<UpsTracking> list = new ArrayList<UpsTracking>();
		UpsTracking upsTracking = new UpsTracking();
		upsTracking.setOrderId(order1);
		list.add(upsTracking);
		
		String namedQuery = "Stubtrans.getById";
		Mockito.when(session.getNamedQuery(namedQuery)).thenReturn(query);
		Mockito.when(query.list()).thenReturn(list);
		Assert.assertEquals(stubTransDAO.getOrderProcSubStatusCode(order1).size(), 1);
	}
	
	@Test
	public void testUpdateBuyerContactId() {
		List<UpsTracking> list = new ArrayList<UpsTracking>();
		UpsTracking upsTracking = new UpsTracking();
		upsTracking.setOrderId(order1);
		list.add(upsTracking);
		
		String namedQuery = "Stubtrans.updateBuyerContactId";
		Mockito.when(session.getNamedQuery(namedQuery)).thenReturn(query);
		Mockito.when(query.executeUpdate()).thenReturn(1);
		Assert.assertEquals(stubTransDAO.updateBuyerContactId(order1, contactId, csrRep, dateAdded), 1);
	}

    @Test
    public void testGetById() throws RecordNotFoundForIdException {
        Long orderId = 1234L;
        StubTrans st = new StubTrans();
        st.setOrderId(orderId);
        Mockito.when(session.get(StubTrans.class, orderId)).thenReturn(st);
        StubTrans order = stubTransDAO.getById(orderId);
        Assert.assertNotNull(order);
    }
    
    @Test
    public void testGetBuyerFlipCount() {
        Long userId = 1234L;
        Mockito.when(session.getNamedQuery("Stubtrans.getBuyerFlipCount")).thenReturn(query);
        Mockito.when(query.uniqueResult()).thenReturn(new BigDecimal(2));
        int count = stubTransDAO.getBuyerFlipCount(userId);
        Assert.assertNotNull(count);
        Mockito.when(query.uniqueResult()).thenReturn(null);
        count = stubTransDAO.getBuyerFlipCount(userId);
        Assert.assertNotNull(count);
    }
    
    
    @Test
    public void testGetSelStubTicketCount() {
        Long userId = 1234L;
        Mockito.when(session.getNamedQuery("Stubtrans.getSelStubTicketCount")).thenReturn(query);
        Mockito.when(query.uniqueResult()).thenReturn(new BigDecimal(2));
        int count = stubTransDAO.getSelStubTicketCount(userId);
        Assert.assertNotNull(count);
        Mockito.when(query.uniqueResult()).thenReturn(null);
        count = stubTransDAO.getSelStubTicketCount(userId);
        Assert.assertNotNull(count);
    }
    
    @Test
    public void testGetSelTransTikCount() {
        Long userId = 1234L;
        Mockito.when(session.getNamedQuery("Stubtrans.getSelTransTikCount")).thenReturn(query);
        Mockito.when(query.uniqueResult()).thenReturn(new BigDecimal(2));
        int count = stubTransDAO.getSelTransTikCount(userId);
        Assert.assertNotNull(count);
        Mockito.when(query.uniqueResult()).thenReturn(null);
        count = stubTransDAO.getSelTransTikCount(userId);
        Assert.assertNotNull(count);
    }
    
    @Test
    public void testGetSelPayTicketCount() {
        Long userId = 1234L;
        Mockito.when(session.getNamedQuery("Stubtrans.getSelPayTicketCount")).thenReturn(query);
        Mockito.when(query.uniqueResult()).thenReturn(new BigDecimal(2));
        int count = stubTransDAO.getSelPayTicketCount(userId);
        Assert.assertNotNull(count);
        Mockito.when(query.uniqueResult()).thenReturn(null);
        count = stubTransDAO.getSelPayTicketCount(userId);
        Assert.assertNotNull(count);
    }
    
	@Test
	public void testPersist() {
		StubTrans st = new StubTrans();
		st.setOrderId(123L);
		Assert.assertNotNull(stubTransDAO.persist(st));
	}
	
	@Test
	public void testUpdateOrderProcStatus() {

		String namedQuery = "StubTrans.updateSTOrderProcStatusById";
		Mockito.when(session.getNamedQuery(namedQuery)).thenReturn(query);
		Mockito.when(query.executeUpdate()).thenReturn(1);
		stubTransDAO.updateOrderProcStatus(order1, newStatus);
	}
}
