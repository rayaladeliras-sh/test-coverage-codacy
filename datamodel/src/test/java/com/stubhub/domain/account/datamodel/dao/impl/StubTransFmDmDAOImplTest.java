package com.stubhub.domain.account.datamodel.dao.impl;


import com.stubhub.domain.account.datamodel.dao.StubTransFmDmDAO;
import com.stubhub.domain.account.datamodel.entity.StubTransFmDm;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.mockito.Mockito;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class StubTransFmDmDAOImplTest {


    private StubTransFmDmDAO stubTransFmDmDAO;
    private HibernateTemplate hibernateTemplate;
    private SessionFactory sessionFactory;
    private Session session;
    private SQLQuery query;


    @BeforeMethod
    public void setUp() {
        stubTransFmDmDAO = new StubTransFmDmDAOImpl();
        hibernateTemplate = Mockito.mock(HibernateTemplate.class);
        sessionFactory = Mockito.mock(SessionFactory.class);
        session = Mockito.mock(Session.class);
        query = Mockito.mock(SQLQuery.class);
        Mockito.when(hibernateTemplate.getSessionFactory()).thenReturn(sessionFactory);
        Mockito.when(sessionFactory.getCurrentSession()).thenReturn(session);
        ReflectionTestUtils.setField(stubTransFmDmDAO, "hibernateTemplate", hibernateTemplate);
    }

    @Test
    public void testPersist() {
        StubTransFmDm fmdm = new StubTransFmDm();
        fmdm.setLmsLocationId(1111L);

        Mockito.doNothing().when(session).saveOrUpdate(fmdm);
        stubTransFmDmDAO.persist(fmdm);

    }

    @Test
    public void testGetFmDmByTids() {

        List<Long> tids = new ArrayList<Long>();
        tids.add(111L);
        tids.add(222L);
        String namedQuery = "StubTransFmDm.getFmDmByTids";
        List<StubTransFmDm> result = new ArrayList<StubTransFmDm>();
        StubTransFmDm fmdm = new StubTransFmDm();
        fmdm.setLmsLocationId(1111L);
        StubTransFmDm fmdm1 = new StubTransFmDm();
        fmdm1.setLmsLocationId(2222L);
        result.add(fmdm);
        result.add(fmdm1);
        Mockito.when(session.getNamedQuery(namedQuery)).thenReturn(query);
        Mockito.when(query.list()).thenReturn(result);
        Assert.assertTrue(stubTransFmDmDAO.getFmDmByTids(tids).size() == 2);
    }
}
