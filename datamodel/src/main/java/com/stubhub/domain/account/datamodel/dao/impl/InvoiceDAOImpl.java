package com.stubhub.domain.account.datamodel.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.stubhub.domain.account.datamodel.dao.InvoiceDAO;
import com.stubhub.domain.account.datamodel.entity.AdjustmentReason;
import com.stubhub.domain.account.datamodel.entity.InvoiceDO;
import com.stubhub.domain.account.datamodel.entity.OrderAdjustment;
import org.springframework.util.CollectionUtils;

@Component("invoiceDAOImpl")
public class InvoiceDAOImpl implements InvoiceDAO {
    private static final String QUERY_GET_BY_REFERENCE_NUMBER = "select * from MYACT_INVOICE_VW_01 invoice where invoice.REFERENCE_NUMBER = (:refNumber)";
    private static final String QUERY_GET_BY_REFERENCE_NUMBER_AND_TID = "select * from MYACT_INVOICE_VW_01 invoice where invoice.REFERENCE_NUMBER = (:refNumber) and invoice.TID = (:TID)";
    private static final String QUERY_GET_ORDER_ADJUSTMENT = "select TID, CURRENCY_CODE, REFERENCE_NUMBER, AMOUNT, REASON_CODE from MYACT_ORDER_ADJUSTMENT_VW where TID = (:TID)";

    @Resource(name = "accountHibernateTemplate")
    private HibernateTemplate hibernateTemplate;

    private final static Logger LOGGER = LoggerFactory.getLogger(InvoiceDAOImpl.class);

    @Transactional(readOnly = true)
    @Override
    public InvoiceDO getByReferenceNumber(String refNumber) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("refNumber=" + refNumber);
        }

        Query query = hibernateTemplate.getSessionFactory().getCurrentSession()
                .createSQLQuery(QUERY_GET_BY_REFERENCE_NUMBER).setResultTransformer(new InvoiceResultTransformer());
        query.setParameter("refNumber", refNumber);
        Object result = query.uniqueResult();
        if (result != null) {
            return (InvoiceDO) result;
        }

        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public InvoiceDO getByReferenceNumberAndTid(String refNumber, Long tid) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("refNumber={}, tid={}", refNumber, tid);
        }

        Query query = hibernateTemplate.getSessionFactory().getCurrentSession()
                                       .createSQLQuery(QUERY_GET_BY_REFERENCE_NUMBER_AND_TID).setResultTransformer(new InvoiceResultTransformer());
        query.setParameter("refNumber", refNumber);
        query.setParameter("TID", tid);

        List<InvoiceDO> invoiceDOList = query.list();
        if (!CollectionUtils.isEmpty(invoiceDOList)) {
            return invoiceDOList.get(0);
        }

        return null;
    }

    @Transactional
    @Override
    public List<OrderAdjustment> getOrderAdjustmentByOrderId(Long orderID) {
        Query query = hibernateTemplate.getSessionFactory().getCurrentSession()
                .createSQLQuery(QUERY_GET_ORDER_ADJUSTMENT);
        query.setParameter("TID", orderID);
        List<Object[]> result = query.list();
        if (result == null) {
            return Collections.emptyList();
        }
        List<OrderAdjustment> adjustments = new ArrayList<OrderAdjustment>(result.size());
        for (Object[] obj : result) {
            OrderAdjustment adj = new OrderAdjustment();
            adj.setOrderId(((BigDecimal) obj[0]).longValue());
            adj.setCurrencyCode((String) obj[1]);
            adj.setReferenceNumber((String) obj[2]);
            adj.setAmount((BigDecimal) obj[3]);
            adj.setReasonCode(((BigDecimal) obj[4]).longValue());
            adjustments.add(adj);
        }
        return adjustments;
    }

    @Transactional
    @Override
    public Map<Long, AdjustmentReason> getAllAdjustmentReasons() {
        Query query = hibernateTemplate.getSessionFactory().getCurrentSession()
                .getNamedQuery("AdjustmentReason.getAllAdustmentReasons");
        List<AdjustmentReason> list = query.list();
        if (list == null) {
            return Collections.emptyMap();
        }
        Map<Long, AdjustmentReason> map = new HashMap<Long, AdjustmentReason>();
        for (AdjustmentReason reason : list) {
            map.put(reason.getReasonCode(), reason);
        }
        return map;
    }
}