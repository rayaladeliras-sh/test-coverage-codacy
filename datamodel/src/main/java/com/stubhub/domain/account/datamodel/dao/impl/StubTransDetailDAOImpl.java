package com.stubhub.domain.account.datamodel.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.stubhub.domain.account.datamodel.dao.StubTransDetailDAO;
import com.stubhub.domain.account.datamodel.entity.StubTransDetail;

@Component("stubTransDetailDAO")
public class StubTransDetailDAOImpl implements StubTransDetailDAO {

	private final static Logger log = LoggerFactory.getLogger(StubTransDetailDAOImpl.class);

	@Autowired
	@Qualifier("accountHibernateTemplate")
	private HibernateTemplate hibernateTemplate;

	@Override
    @Transactional
	public Long persist(StubTransDetail stubTransDetail){
		this.hibernateTemplate.getSessionFactory().getCurrentSession().saveOrUpdate(stubTransDetail);
		log.debug("created stubTransDetailId=" + stubTransDetail.getStubTransDtlId());
		return stubTransDetail.getStubTransDtlId();
	}

    @Override
    @Transactional
    public List<StubTransDetail> getSeatDetails(Long tid) {
        log.debug("get ticket seat by tid="+tid);
        Query query = this.hibernateTemplate.getSessionFactory()
                .getCurrentSession().getNamedQuery("StubTransDetail.getSeatInfo");
        query.setLong("tid", tid);
        List<StubTransDetail> result = query.list();
        if(result != null && result.size() > 0) {
            return result;
        }
        return null;
    }
}
