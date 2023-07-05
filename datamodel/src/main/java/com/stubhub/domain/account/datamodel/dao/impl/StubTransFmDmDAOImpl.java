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

import com.stubhub.domain.account.datamodel.dao.StubTransFmDmDAO;
import com.stubhub.domain.account.datamodel.entity.StubTransFmDm;

@Component("stubTransFmDmDAO")
public class StubTransFmDmDAOImpl implements StubTransFmDmDAO {

	private final static Logger log = LoggerFactory.getLogger(StubTransFmDmDAOImpl.class);

	@Autowired
	@Qualifier("accountHibernateTemplate")
	private HibernateTemplate hibernateTemplate;
	
	@Override
    @Transactional
	public Long persist(StubTransFmDm stubTransFmDm){
		this.hibernateTemplate.getSessionFactory().getCurrentSession().saveOrUpdate(stubTransFmDm);
		log.debug("created stubTransFmDmId=" + stubTransFmDm.getId());
		return stubTransFmDm.getId();
	}

	@Override
	@Transactional(readOnly = true)
	public List<StubTransFmDm> getFmDmByTids(List<Long> tids) {

		List<StubTransFmDm> result = null;
		SQLQuery sqlQuery = (SQLQuery)this.hibernateTemplate.getSessionFactory().getCurrentSession().getNamedQuery("StubTransFmDm.getFmDmByTids");
		sqlQuery.setParameterList("tids",tids);
		result = sqlQuery.list();
		return result;
	}

}
