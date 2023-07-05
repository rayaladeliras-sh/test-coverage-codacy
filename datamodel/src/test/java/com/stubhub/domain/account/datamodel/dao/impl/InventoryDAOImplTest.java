package com.stubhub.domain.account.datamodel.dao.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.hibernate.transform.ResultTransformer;
import org.mockito.Mockito;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.stubhub.common.exception.RecordNotFoundForIdException;
import com.stubhub.domain.account.datamodel.entity.AdjustmentReason;
import com.stubhub.domain.account.datamodel.entity.InventoryData;
import com.stubhub.domain.account.datamodel.entity.InvoiceDO;
import com.stubhub.domain.account.datamodel.entity.OrderAdjustment;
import com.stubhub.domain.account.datamodel.entity.TTAPIintegration;

public class InventoryDAOImplTest {

    private InvoiceDAOImpl dao;
    private HibernateTemplate hibernateTemplate;
    private SessionFactory sessionFactory;
    private Session session;
    private SQLQuery query;
    private InventoryDAOImpl daoInv;
	private Long val = 1l;

    @BeforeMethod
    public void setUp() {
        dao = new InvoiceDAOImpl();
        daoInv =  new InventoryDAOImpl();
        hibernateTemplate = Mockito.mock(HibernateTemplate.class);
        sessionFactory = Mockito.mock(SessionFactory.class);
        session = Mockito.mock(Session.class);
        query = Mockito.mock(SQLQuery.class);
        when(hibernateTemplate.getSessionFactory()).thenReturn(sessionFactory);
        when(sessionFactory.getCurrentSession()).thenReturn(session);
        ReflectionTestUtils.setField(daoInv, "hibernateTemplate", hibernateTemplate);
        
    }
    
    @Test
    public void testGetInventoryDataById() throws RecordNotFoundForIdException {

        Long orderId = 1L;
        InventoryData inv = mockInventoryData();
        inv.setOrderId(orderId);
        when(   session.getNamedQuery("InventoryData.getSellerInfo"))
            .thenReturn(query);
        when(query.setResultTransformer(any(ResultTransformer.class))).thenReturn(query);

        when(query.uniqueResult()).thenReturn(inv);
        InventoryData result = daoInv.getInventoryDataById(orderId);
        assertNotNull(result);
        Assert.assertEquals(result.getOrderId(), orderId);
        InventoryData resultNull = null;
        when(query.uniqueResult()).thenReturn(null);
        try{
        	resultNull = daoInv.getInventoryDataById(orderId);
        }
        catch(RecordNotFoundForIdException e)
        {
        	 Assert.assertEquals("StubHub Business Error;  Id Does Not Exist for Entity class/Id:   orderId/1", e.getMessage());
        }
        Assert.assertNull(resultNull);
        
    }
    
    
    @Test
    public void testGetFulFillmentType() throws RecordNotFoundForIdException {

        Long methodId = 1L;
       String fulFillmentType = "UPS" ;
        when(   session.createSQLQuery(any(String.class)))
            .thenReturn(query);
        when(query.setResultTransformer(any(ResultTransformer.class))).thenReturn(query);

        when(query.uniqueResult()).thenReturn(fulFillmentType);
        String result = daoInv.getFulFillmentType(methodId);
        assertNotNull(result);
        Assert.assertEquals(result, fulFillmentType);
        String resultNull = null;
        when(query.uniqueResult()).thenReturn(null);
        try{
        	resultNull = daoInv.getFulFillmentType(methodId);
        }
        catch(RecordNotFoundForIdException e)
        {
        	 Assert.assertEquals("StubHub Business Error;  Id Does Not Exist for Entity class/Id:   methodId/1", e.getMessage());
        }
        Assert.assertNull(resultNull);
    }
    
    
    @Test
    public void testGetTTProperties() throws RecordNotFoundForIdException {

       
       String username = "user" ;
     
        when(   session.createSQLQuery(any(String.class)))
            .thenReturn(query);
        when(query.setResultTransformer(any(ResultTransformer.class))).thenReturn(query);

        when(query.uniqueResult()).thenReturn(username);
        TTAPIintegration result = daoInv.getTTProperties();
        assertNotNull(result);
        Assert.assertEquals(result.getUsername(), username);
        TTAPIintegration resultNull = null;
        when(query.uniqueResult()).thenReturn(null);
        try{
        	resultNull = daoInv.getTTProperties();
        }
        catch(RecordNotFoundForIdException e)
        {
        	
        }
        Assert.assertEquals(resultNull.getUsername(),null);
    }
    public InventoryData mockInventoryData()
	{
		
		InventoryData inv =  new InventoryData();
		
		inv.setListingId(1L);
		inv.setTicTecListingId("1234");
		inv.setSellerId(10133681L);
		inv.setBrokerId(val);
		inv.setOrderId(val);
	//	inv.setFulfillmentType("UPS");
		
		return inv;
	}

  
}
