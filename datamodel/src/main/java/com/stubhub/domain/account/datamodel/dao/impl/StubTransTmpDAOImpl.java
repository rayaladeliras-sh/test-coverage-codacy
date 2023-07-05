package com.stubhub.domain.account.datamodel.dao.impl;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.stubhub.domain.account.datamodel.dao.StubTransTmpDAO;
import com.stubhub.domain.account.datamodel.entity.StubTransTmp;


@Component("stubTransTmpDAO")
public class StubTransTmpDAOImpl implements StubTransTmpDAO {

	private final static Logger log = LoggerFactory.getLogger(StubTransTmpDAOImpl.class);
	//private static final String GET_NEXT_SEQ_NUMBER = "StubTransTemp.getNextSequenceNumber";

	@Autowired
	@Qualifier("accountHibernateTemplate")
	private HibernateTemplate hibernateTemplate;
	
	@Override
    @Transactional
	public Long persist(StubTransTmp stubTransTmp){
		this.hibernateTemplate.getSessionFactory().getCurrentSession().saveOrUpdate(stubTransTmp);
		log.debug("created stubTransTmpId=" + stubTransTmp.getId());
		return stubTransTmp.getId();
	}
}
