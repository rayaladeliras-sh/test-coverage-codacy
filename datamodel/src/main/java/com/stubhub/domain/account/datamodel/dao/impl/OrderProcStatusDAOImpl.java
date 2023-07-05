package com.stubhub.domain.account.datamodel.dao.impl;

import java.util.List;

import org.hibernate.SQLQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.stubhub.domain.account.datamodel.dao.OrderProcStatusDAO;
import com.stubhub.domain.account.datamodel.entity.OrderProcStatusDO;

@Component("orderProcStatusDAO")
public class OrderProcStatusDAOImpl implements OrderProcStatusDAO {

	private static final Logger log = LoggerFactory.getLogger(OrderProcStatusDAOImpl.class);
	private static final String FIND_ORDER_PROC_BY_TRANS_ID_QUERY = "OrderProcStatus.getOrderProcStatusByTransId";
	
	@Autowired
	@Qualifier("accountHibernateTemplate")
	private HibernateTemplate hibernateTemplate;

	@Override
    @Transactional
	public List<Object[]> findOrderStatusByTransId(Long tid) {
		List<Object[]> results = null;
		SQLQuery sqlQuery = (SQLQuery)this.hibernateTemplate.getSessionFactory().getCurrentSession().getNamedQuery(FIND_ORDER_PROC_BY_TRANS_ID_QUERY);
		sqlQuery.setLong("arg1", tid);
		results = sqlQuery.list();
		log.debug("retrieved order proc status history for orderId="+tid);
		return results;
	}
	
	@Override
    @Transactional
	public Long persist(OrderProcStatusDO orderProcStatusDO) {
		hibernateTemplate.getSessionFactory().getCurrentSession().saveOrUpdate(orderProcStatusDO);
		log.debug("created order proc status id=" + orderProcStatusDO.getOrderProcStatusId() + " for orderId=" + orderProcStatusDO.getTid());
		return orderProcStatusDO.getOrderProcStatusId();
	}
}
