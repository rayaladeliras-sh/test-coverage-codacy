package com.stubhub.domain.account.datamodel.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.stubhub.domain.account.datamodel.dao.StubTransAdjHistDAO;
import com.stubhub.domain.account.datamodel.entity.StubTransAdjHist;

@Component("stubTransAdjHistDAO")
public class StubTransAdjHistDAOImpl implements StubTransAdjHistDAO {

	private final static Logger log = LoggerFactory.getLogger(StubTransAdjHistDAOImpl.class);

	@Autowired
	@Qualifier("accountHibernateTemplate")
	private HibernateTemplate hibernateTemplate;

	@Override
    @Transactional
	public Long persist(StubTransAdjHist stubTransAdjHist) {
		this.hibernateTemplate.getSessionFactory().getCurrentSession().saveOrUpdate(stubTransAdjHist);
		log.debug("created stubtrans adjustment history for orderId="+ stubTransAdjHist.getTid());
		return stubTransAdjHist.getStubTransAdjHistId();
	}
}
