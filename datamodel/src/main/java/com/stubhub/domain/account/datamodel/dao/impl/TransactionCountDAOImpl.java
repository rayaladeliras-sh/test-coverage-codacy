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

import com.stubhub.domain.account.datamodel.dao.TransactionCountDAO;
import com.stubhub.domain.account.datamodel.entity.BuysCount;
import com.stubhub.domain.account.datamodel.entity.ListingsCount;
import com.stubhub.domain.account.datamodel.entity.SalesCount;

@Component("transactionCountDAO")
public class TransactionCountDAOImpl implements TransactionCountDAO {

	private static final Logger log = LoggerFactory.getLogger(TransactionCountDAOImpl.class);

	@Autowired
	@Qualifier("accountHibernateTemplate")
	private HibernateTemplate hibernateTemplate;
	
	@Override
    @Transactional
	public List<ListingsCount> findListingsCountByUserId(String uid){
		List<ListingsCount> results = null;
		try{
			log.info("api_domain=account api_resource=listings api_method=findListingsCountByUserId status=success message=Querying DB for the userId=" + uid);
			SQLQuery sqlQuery = (SQLQuery)this.hibernateTemplate.getSessionFactory().getCurrentSession().getNamedQuery("ListingsCount.getCountByUserId");
			sqlQuery.setString("arg1", uid);
			results = sqlQuery.list();
		}catch(Exception e){
			log.error("api_domain=account api_resource=listings api_method=findListingsCountByUserId status=error message=Error getting data from DB for the userId=" + uid , e);
		}
		return results;
	}
	
	@Override
    @Transactional
	public List<SalesCount> findSalesCountByUserId(String uid){
		List<SalesCount> results = null;
		try{
			log.info("api_domain=account api_resource=sales api_method=findSalesCountByUserId status=success message=Querying DB for the userId=" + uid);
			SQLQuery sqlQuery = (SQLQuery)this.hibernateTemplate.getSessionFactory().getCurrentSession().getNamedQuery("SalesCount.getCountByUserId");
			sqlQuery.setString("arg1", uid);
			results = sqlQuery.list();
		}catch(Exception e){
			log.error("api_domain=account api_resource=sales api_method=findSalesCountByUserId status=error message=Error getting data from DB for the userId=" + uid , e);
		}
		return results;
	}
	
	@Override
    @Transactional
	public List<BuysCount> findBuysCountByUserId(String uid){
		List<BuysCount> results = null;
		try{
			log.info("api_domain=account api_resource=orders api_method=findBuysCountByUserId status=success message=Querying DB for the userId=" + uid);
			SQLQuery sqlQuery = (SQLQuery)this.hibernateTemplate.getSessionFactory().getCurrentSession().getNamedQuery("BuysCount.getCountByUserId");
			sqlQuery.setString("arg1", uid);
			results = sqlQuery.list();
		}catch(Exception e){
			log.error("api_domain=account api_resource=orders api_method=findBuysCountByUserId status=error message=Error getting data from DB for the userId=" + uid , e);
		}
		return results;
	}

}
