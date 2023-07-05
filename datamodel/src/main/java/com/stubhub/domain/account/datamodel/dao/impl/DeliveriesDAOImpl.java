package com.stubhub.domain.account.datamodel.dao.impl;

import java.util.List;

import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.stubhub.domain.account.datamodel.dao.DeliveriesDAO;
import com.stubhub.domain.account.datamodel.entity.Deliveries;

@Component("deliveriesDAO")
public class DeliveriesDAOImpl implements DeliveriesDAO {

	@Autowired
	@Qualifier("accountHibernateTemplate")
	private HibernateTemplate hibernateTemplate;

	@Transactional
	public List<Deliveries> getByTid(Long tid){
		List<Deliveries> results = null;
		SQLQuery sqlQuery = (SQLQuery)this.hibernateTemplate.getSessionFactory().getCurrentSession().getNamedQuery("Deliveries.getByTid");
		sqlQuery.setLong("orderId", tid);
		results = sqlQuery.list();
		return results;
	}
}
