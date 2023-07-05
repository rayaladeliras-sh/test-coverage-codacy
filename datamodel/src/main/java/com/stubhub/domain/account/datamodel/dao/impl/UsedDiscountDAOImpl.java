package com.stubhub.domain.account.datamodel.dao.impl;

import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.stubhub.domain.account.datamodel.dao.UsedDiscountDAO;
import com.stubhub.domain.account.datamodel.entity.UsedDiscount;

@Component("usedDiscountDAO")
public class UsedDiscountDAOImpl implements UsedDiscountDAO {

	private static final Logger log = LoggerFactory.getLogger(UsedDiscountDAOImpl.class);
	private String getDetailByTidQuery = "SELECT ud.amount_used AS used_discount, ud.currency_code AS currency_code, " +
										"dt.id AS discount_id, dt.type AS discount_type, d.description AS discount_desc " + 
										"FROM used_discounts ud, discounts d, discount_types dt WHERE ud.tid =:arg1 " + 
										"AND ud.discount_id = d.id AND d.discount_type_id = dt.id ";

	@Autowired
	@Qualifier("accountHibernateTemplate")
	private HibernateTemplate hibernateTemplate;

	@Override
    @Transactional
	public List<List> findDetailByTid(Long tid) {
		List<List> results = null;
		SQLQuery sqlQuery = this.hibernateTemplate.getSessionFactory().getCurrentSession().createSQLQuery(getDetailByTidQuery);
		sqlQuery.setLong("arg1", tid);
		sqlQuery.setResultTransformer(Transformers.TO_LIST);
		results = sqlQuery.list();
		return results;
	}
	
	@Override
    @Transactional
	public List<UsedDiscount> findByTid(Long tid) {
		List<UsedDiscount> results = null;
		SQLQuery sqlQuery = (SQLQuery)this.hibernateTemplate.getSessionFactory().getCurrentSession().getNamedQuery("UsedDiscount.getByTid");
		sqlQuery.setLong("arg1", tid);
		results = sqlQuery.list();
		return results;
	}
	
	@Override
    public List<UsedDiscount> persist(List<UsedDiscount> usedDiscount){
		Session session = this.hibernateTemplate.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		for (UsedDiscount transientObj : usedDiscount)
			session.saveOrUpdate(transientObj);
		tx.commit();
		session.close();
		log.debug("updated usedDiscounts for orderId=" + usedDiscount.get(1).getTid());
		return usedDiscount;
	}
}
