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

import com.stubhub.domain.account.datamodel.dao.UpsTrackingDAO;
import com.stubhub.domain.account.datamodel.entity.UpsTracking;

@Component("upsTrackingDAO")
public class UpsTrackingDAOImpl implements UpsTrackingDAO {

	private final static Logger log = LoggerFactory.getLogger(UpsTrackingDAOImpl.class);

	@Autowired
	@Qualifier("accountHibernateTemplate")
	private HibernateTemplate hibernateTemplate;

	@Override
    @Transactional
	public List<UpsTracking> checkUPSOrder(Long tid) {
		List<UpsTracking> results = null;
		try{			
			SQLQuery sqlQuery = (SQLQuery)this.hibernateTemplate.getSessionFactory().getCurrentSession().getNamedQuery("UpsTracking.checkUpsOrder");
			sqlQuery.setLong("orderId", tid);
			results = sqlQuery.list();
		}catch(Exception e){
			log.error("api_domain=account api_resource=orders api_method=checkUPSOrder status=error message=Error getting data from DB for the orderId=" + tid , e);
		}		
		return results;
	}
}