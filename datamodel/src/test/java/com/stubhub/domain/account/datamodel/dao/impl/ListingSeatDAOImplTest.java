package com.stubhub.domain.account.datamodel.dao.impl;

import com.stubhub.domain.account.datamodel.dao.ListingSeatsDAO;
import com.stubhub.domain.account.datamodel.entity.TicketSeat;
import com.stubhub.domain.account.datamodel.entity.UserContact;
import junit.framework.Assert;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.mockito.Mockito;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by jicui on 9/6/15.
 */
@Test
public class ListingSeatDAOImplTest {

    private ListingSeatsDAOImpl listingSeatsDAO;

    private HibernateTemplate hibernateTemplate;
    private SessionFactory sessionFactory;
    private Session currentSession;
    private Query query;

    @BeforeMethod
    public void setUp() {
        listingSeatsDAO = new ListingSeatsDAOImpl();
        hibernateTemplate = mock(HibernateTemplate.class);
        sessionFactory = mock(SessionFactory.class);
        currentSession = mock(Session.class);
        query = mock(SQLQuery.class);

        ReflectionTestUtils.setField(listingSeatsDAO, "hibernateTemplate", hibernateTemplate);
        when(hibernateTemplate.getSessionFactory()).thenReturn(sessionFactory);
        when(sessionFactory.getCurrentSession()).thenReturn(currentSession);

    }


    @Test
    void test(){
        when(currentSession.getNamedQuery(Mockito.anyString())).thenReturn(query);
        List<TicketSeat> ts= listingSeatsDAO.getTicketSeats(123L);
        Assert.assertNull(ts);
    }

    @Test
    void test1(){
        when(currentSession.getNamedQuery(Mockito.anyString())).thenReturn(query);
        List<TicketSeat> result=new ArrayList<TicketSeat>();
        result.add(new TicketSeat());
        when(query.list()).thenReturn(result);
        List<TicketSeat> ts=listingSeatsDAO.getTicketSeats(123L);
        Assert.assertNotNull(ts);
    }

}
