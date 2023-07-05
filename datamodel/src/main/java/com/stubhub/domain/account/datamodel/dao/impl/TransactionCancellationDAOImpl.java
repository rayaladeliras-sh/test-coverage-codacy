package com.stubhub.domain.account.datamodel.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.stubhub.domain.account.datamodel.dao.TransactionCancellationDAO;
import com.stubhub.domain.account.datamodel.entity.TransactionCancellation;

@Component("transactionCancellationDAO")
public class TransactionCancellationDAOImpl implements TransactionCancellationDAO {

	private final static Logger log = LoggerFactory.getLogger(TransactionCancellationDAOImpl.class);

	@Autowired
	@Qualifier("accountHibernateTemplate")
	private HibernateTemplate hibernateTemplate;

	@Override
    @Transactional
	public Long persist(TransactionCancellation transactionCancellation) {
		this.hibernateTemplate.getSessionFactory().getCurrentSession().saveOrUpdate(transactionCancellation);
		log.debug("created transaction cancellation for orderId="+ transactionCancellation.getTid());
		return transactionCancellation.getId();
	}
}
