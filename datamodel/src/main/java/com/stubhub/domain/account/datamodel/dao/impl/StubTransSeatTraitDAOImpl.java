package com.stubhub.domain.account.datamodel.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

import com.stubhub.domain.account.datamodel.dao.StubTransSeatTraitDAO;
import com.stubhub.domain.account.datamodel.entity.StubTransSeatTrait;

@Component("stubTransSeatTraitDAO")
public class StubTransSeatTraitDAOImpl implements StubTransSeatTraitDAO {

	private final static Logger log = LoggerFactory.getLogger(StubTransSeatTraitDAOImpl.class);

	@Autowired
	@Qualifier("accountHibernateTemplate")
	private HibernateTemplate hibernateTemplate;

	@Override
    public List<StubTransSeatTrait> persist(List<StubTransSeatTrait> stubTransSeatTraitList){
		Session session = this.hibernateTemplate.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		for (StubTransSeatTrait transientObj : stubTransSeatTraitList){
			session.save(transientObj);
			log.debug("created stubTransSeatTraitId=" + transientObj.getId());
		}
		tx.commit();
		session.close();
		return stubTransSeatTraitList;
	}
}
