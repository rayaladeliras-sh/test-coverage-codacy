package com.stubhub.domain.account.datamodel.dao.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
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

import com.stubhub.domain.account.datamodel.entity.AdjustmentReason;
import com.stubhub.domain.account.datamodel.entity.InvoiceDO;
import com.stubhub.domain.account.datamodel.entity.OrderAdjustment;
import org.testng.collections.Lists;

public class InvoiceDAOImplTest {

    private InvoiceDAOImpl dao;
    private HibernateTemplate hibernateTemplate;
    private SessionFactory sessionFactory;
    private Session session;
    private SQLQuery query;

    @BeforeMethod
    public void setUp() {
        dao = new InvoiceDAOImpl();
        hibernateTemplate = Mockito.mock(HibernateTemplate.class);
        sessionFactory = Mockito.mock(SessionFactory.class);
        session = Mockito.mock(Session.class);
        query = Mockito.mock(SQLQuery.class);
        when(hibernateTemplate.getSessionFactory()).thenReturn(sessionFactory);
        when(sessionFactory.getCurrentSession()).thenReturn(session);
        ReflectionTestUtils.setField(dao, "hibernateTemplate", hibernateTemplate);
    }

    @Test
    public void testGetByReferenceNumber() {
        String refNumber = "It's refNumber";
        InvoiceDO invoice = new InvoiceDO();
        invoice.setRefNumber(refNumber);
        when(
                session.createSQLQuery(any(String.class)))
                .thenReturn(query);
        when(query.setResultTransformer(any(ResultTransformer.class))).thenReturn(query);

        when(query.uniqueResult()).thenReturn(invoice);
        InvoiceDO result = dao.getByReferenceNumber(refNumber);
        assertNotNull(result);
        Assert.assertEquals(result.getOrderId(), invoice.getOrderId());

        when(query.uniqueResult()).thenReturn(null);
        result = dao.getByReferenceNumber(refNumber);
        Assert.assertNull(result);
    }

    @Test
    public void testGetByReferenceNumberAndTid() {
        String refNumber = "It's refNumber";
        Long tid = 1L;
        final InvoiceDO invoice = new InvoiceDO();
        invoice.setRefNumber(refNumber);
        invoice.setOrderId(tid);
        when(session.createSQLQuery(any(String.class))).thenReturn(query);
        when(query.setResultTransformer(any(ResultTransformer.class))).thenReturn(query);

        List<InvoiceDO> invoiceDOList  = new ArrayList<InvoiceDO>(){{
            add(invoice);
        }};

        when(query.list()).thenReturn(invoiceDOList);
        InvoiceDO result = dao.getByReferenceNumberAndTid(refNumber, tid);
        assertNotNull(result);
        Assert.assertEquals(result.getOrderId(), invoice.getOrderId());
        Assert.assertEquals(result.getRefNumber(), invoice.getRefNumber());

        when(query.list()).thenReturn(Lists.newArrayList());
        InvoiceDO result1 = dao.getByReferenceNumberAndTid(refNumber, tid);
        assertNull(result1);

        when(query.list()).thenReturn(null);
        InvoiceDO result2 = dao.getByReferenceNumberAndTid(refNumber, tid);
        assertNull(result2);
    }

    @Test
    public void testGetOrderAdjustmentByOrderId() {
        Long orderId = 1234L;
        List<Object[]> adjustments = new ArrayList<Object[]>();
        Object[] adjustment = new Object[5];
        adjustment[0] = new BigDecimal(orderId.longValue());
        adjustment[1] = "currency";
        adjustment[2] = "refNumber";
        adjustment[3] = new BigDecimal("15");
        adjustment[4] = new BigDecimal(4L);
        adjustments.add(adjustment);
        when(
                session.createSQLQuery("select TID, CURRENCY_CODE, REFERENCE_NUMBER, AMOUNT, REASON_CODE from MYACT_ORDER_ADJUSTMENT_VW where TID = (:TID)"))
                .thenReturn(query);

        when(query.list()).thenReturn(adjustments);
        List<OrderAdjustment> result = dao.getOrderAdjustmentByOrderId(orderId);
        assertNotNull(result);
        assertEquals(result.size(), adjustments.size());
        assertEquals(result.get(0).getOrderId(), orderId);

        when(query.list()).thenReturn(null);
        result = dao.getOrderAdjustmentByOrderId(orderId);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetAllAdjustmentReasons() {
        List<AdjustmentReason> reasons = new ArrayList<AdjustmentReason>();
        AdjustmentReason reason = new AdjustmentReason();
        reason.setReasonCode(22L);
        reason.setReasonDescription("reasonDescription");
        reasons.add(reason);
        when(session.getNamedQuery("AdjustmentReason.getAllAdustmentReasons")).thenReturn(query);

        when(query.list()).thenReturn(reasons);
        Map<Long, AdjustmentReason> result = dao.getAllAdjustmentReasons();
        assertNotNull(result);
        assertEquals(result.size(), reasons.size());
        assertEquals(result.get(reason.getReasonCode()).getReasonCode(), reason.getReasonCode());
        assertEquals(result.get(reason.getReasonCode()).getReasonDescription(), reason.getReasonDescription());

        when(query.list()).thenReturn(null);
        result = dao.getAllAdjustmentReasons();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
