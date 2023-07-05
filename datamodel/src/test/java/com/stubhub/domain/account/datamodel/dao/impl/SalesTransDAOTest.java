package com.stubhub.domain.account.datamodel.dao.impl;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.mockito.Mockito;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.stubhub.common.exception.RecordNotFoundForIdException;
import com.stubhub.domain.account.datamodel.dao.SalesTransDAO;

import com.stubhub.domain.account.datamodel.entity.SalesTrans;


public class SalesTransDAOTest {
	
	private SalesTransDAO salesTransDAO;
	private HibernateTemplate hibernateTemplate;
	private SessionFactory sessionFactory;
	private Session session;
	private SQLQuery query;
	private Integer startRow;
	private Integer rowNumber;
	private Calendar eventStartDate;
	private Calendar eventEndDate;
	
	
	@BeforeMethod
	public void setUp() {
		salesTransDAO = new SalesTransDAOImpl();
		hibernateTemplate = Mockito.mock(HibernateTemplate.class);
		sessionFactory = Mockito.mock(SessionFactory.class);
		session = Mockito.mock(Session.class);
		query = Mockito.mock(SQLQuery.class);
		Mockito.when(hibernateTemplate.getSessionFactory()).thenReturn(sessionFactory);
		Mockito.when(sessionFactory.getCurrentSession()).thenReturn(session);
		ReflectionTestUtils.setField(salesTransDAO, "hibernateTemplate", hibernateTemplate);
		startRow = 1;
		rowNumber = 5;
		eventStartDate = Mockito.mock(Calendar.class);
		eventEndDate = Mockito.mock(Calendar.class);
	}
	
	
	@Test
    public void testGetSalesTransByBuyerId() {
		String buyerId = "1234";
		Long saleId=11111l;
		List<SalesTrans> list = new ArrayList<SalesTrans>();
		SalesTrans salesTrans = new SalesTrans();
		salesTrans.setBuyerId(buyerId);
        salesTrans.setSaleId(saleId);
        list.add(salesTrans);
        Mockito.when(session.getNamedQuery("SalesTrans.getTidByBuyerID")).thenReturn(query);
		Mockito.when(query.list()).thenReturn(list);
		List<SalesTrans> result = salesTransDAO.getByBuyerId(Long.parseLong(buyerId), startRow, rowNumber);
		assertNotNull(result);
		assertEquals(result.size(), 1);
        assertEquals(result.get(0).getSaleId(), saleId);

    }
	
	@Test
    public void testGetSalesTransByBuyerIdWithNULL() {
		String buyerId = "1234";
		Long saleId=11111l;
		List<SalesTrans> list = new ArrayList<SalesTrans>();
		SalesTrans salesTrans = new SalesTrans();
		salesTrans.setBuyerId(buyerId);
        salesTrans.setSaleId(saleId);
        list.add(salesTrans);
        Mockito.when(session.getNamedQuery("SalesTrans.getTidByBuyerID")).thenReturn(query);
		Mockito.when(query.list()).thenReturn(list);
		List<SalesTrans> result = salesTransDAO.getByBuyerId(null, startRow, rowNumber);
		assertEquals(result, null);

    }
	
	
	@Test
    public void testGetSalesTransByTId() throws RecordNotFoundForIdException {
		String buyerId = "1234";
		Long saleId=11111l;
		List<SalesTrans> list = new ArrayList<SalesTrans>();
		SalesTrans salesTrans = new SalesTrans();
		salesTrans.setBuyerId(buyerId);
        salesTrans.setSaleId(saleId);
        list.add(salesTrans);
        Mockito.when(session.getNamedQuery("SalesTrans.getByTid")).thenReturn(query);
        Mockito.when(query.list()).thenReturn(list);
		SalesTrans result = salesTransDAO.getByTId(saleId);
		assertNotNull(result);
		assertEquals(result.getSaleId(), saleId);
		assertEquals(result.getBuyerId(), buyerId);

    }
	
	@Test
    public void testGetSalesTransByTIdNULL1() throws RecordNotFoundForIdException {
		String buyerId = "1234";
		Long saleId=11111l;
		List<SalesTrans> list = new ArrayList<SalesTrans>();
		SalesTrans salesTrans = new SalesTrans();
		salesTrans.setBuyerId(buyerId);
        salesTrans.setSaleId(saleId);
        list.add(salesTrans);
        Mockito.when(session.getNamedQuery("SalesTrans.getByTid")).thenReturn(query);
        Mockito.when(query.list()).thenReturn(list);
		SalesTrans result = salesTransDAO.getByTId(null);
		assertEquals(result, null);

    }
	
	@Test
    public void testGetSalesTransByTIdNULL2() throws RecordNotFoundForIdException {
		Long saleId=11111l;
		List<SalesTrans> list = new ArrayList<SalesTrans>();
		Mockito.when(session.getNamedQuery("SalesTrans.getByTid")).thenReturn(query);
        Mockito.when(query.list()).thenReturn(list);
		SalesTrans result = salesTransDAO.getByTId(saleId);
		assertEquals(result, null);

    }
	
	
	@Test
    public void testGetSalesTransByEventDate() {
		String buyerId = "1234";
		Long saleId=11111l;
		List<SalesTrans> list = new ArrayList<SalesTrans>();
		SalesTrans salesTrans = new SalesTrans();
		salesTrans.setBuyerId(buyerId);
        salesTrans.setSaleId(saleId);
        list.add(salesTrans);
        Mockito.when(session.getNamedQuery("SalesTrans.getTidByEventDate")).thenReturn(query);
        Mockito.when(query.list()).thenReturn(list);
        List<SalesTrans> result = salesTransDAO.getByEventDate(eventStartDate, eventEndDate, startRow, rowNumber);
		assertNotNull(result);
		assertEquals(result.size(), 1);
        assertEquals(result.get(0).getSaleId(), saleId);

    }
	
	
	@Test
    public void testGetSalesTransByEventDateAndBuyerId() {
		String buyerId = "1234";
		Long saleId=11111l;
		List<SalesTrans> list = new ArrayList<SalesTrans>();
		SalesTrans salesTrans = new SalesTrans();
		salesTrans.setBuyerId(buyerId);
        salesTrans.setSaleId(saleId);
        list.add(salesTrans);
        Mockito.when(session.getNamedQuery("SalesTrans.getTidByBuyerIDAndEventDate")).thenReturn(query);
        Mockito.when(query.list()).thenReturn(list);
        List<SalesTrans> result = salesTransDAO.getByBuyerIDAndEventDate(Long.parseLong(buyerId), eventStartDate, eventEndDate, startRow, rowNumber);
		assertNotNull(result);
		assertEquals(result.size(), 1);
        assertEquals(result.get(0).getSaleId(), saleId);

    }
	
	
	

}
