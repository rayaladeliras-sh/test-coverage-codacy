package com.stubhub.domain.account.datamodel.dao.impl;

import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.mockito.Mockito;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.stubhub.domain.account.datamodel.entity.AppliedCreditMemoDO;

public class AppliedCreditMemoDAOImplTest {

    private AppliedCreditMemoDAOImpl dao;
    private HibernateTemplate hibernateTemplate;
    private SessionFactory sessionFactory;
    private Session session;
    private SQLQuery query;

    @BeforeMethod
    public void setUp() {
        dao = new AppliedCreditMemoDAOImpl();
        hibernateTemplate = Mockito.mock(HibernateTemplate.class);
        sessionFactory = Mockito.mock(SessionFactory.class);
        session = Mockito.mock(Session.class);
        query = Mockito.mock(SQLQuery.class);
        when(hibernateTemplate.getSessionFactory()).thenReturn(sessionFactory);
        when(sessionFactory.getCurrentSession()).thenReturn(session);
        ReflectionTestUtils.setField(dao, "template", hibernateTemplate);
    }
    
    @Test
    public void testFindByAppliedPaymentId() {
        Long pid = 12345L;
        List<AppliedCreditMemoDO> cms = new ArrayList<AppliedCreditMemoDO>();
        AppliedCreditMemoDO cm = new AppliedCreditMemoDO();
        cm.setId(1L);
        cms.add(cm);
        when(session.createQuery(" FROM AppliedCreditMemoDO WHERE appliedPid = :appliedPid")).thenReturn(query);
        when(query.list()).thenReturn(cms);
        
        List<AppliedCreditMemoDO> result = dao.findByAppliedPaymentId(pid);
        assertNotNull(result);
        assertEquals(result.size(), 1);
        
        when(query.list()).thenReturn(null);
        result = dao.findByAppliedPaymentId(pid);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
