package com.stubhub.domain.account.datamodel.dao.impl;

import org.hibernate.SQLQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.stubhub.domain.account.datamodel.dao.TinDAO;
import com.stubhub.domain.account.datamodel.entity.Tin;

/**
 * @author vpothuru
 * date 11/02/15
 *
 */
@Repository("tinDAO")
public class TinDAOImpl implements TinDAO {

	private final static Logger log = LoggerFactory.getLogger(TinDAOImpl.class);

	@Autowired
	@Qualifier("accountHibernateTemplate")
	private HibernateTemplate hibernateTemplate;

	@Override
	@Transactional(readOnly = true)
	public Tin getTinByGuid(String tinGuid) {
		if (tinGuid == null) return null;

		SQLQuery getTinByGuidQuery = (SQLQuery) this.hibernateTemplate.getSessionFactory().getCurrentSession().getNamedQuery("Tin.getTinByGuid");
		getTinByGuidQuery.setString("tinGuid", tinGuid); // where clause
		log.debug("TIN sql " + getTinByGuidQuery.getQueryString());
		Object result = getTinByGuidQuery.uniqueResult();
		if (result != null) {
			return (Tin) result;
		}

		log.info("no taxpayerdetails found for " + tinGuid);
		return null;
	}

	@Override
	@Transactional
	public String addTin(Tin tin) {
		String tinGuid = null;
		if (tin != null) {
			tinGuid = (String) this.hibernateTemplate.getSessionFactory().getCurrentSession().save(tin);
		}
		return tinGuid;
	}

	@Override
	@Transactional
	public int updateTin(Tin tin) {
		
		int rowsUpdated = 0;
		if(tin == null) return rowsUpdated;
		
		SQLQuery tinUpdateQuery = (SQLQuery) this.hibernateTemplate.getSessionFactory().getCurrentSession().getNamedQuery("Tin.updateTin");
		tinUpdateQuery.setString("tin", tin.getTin());
		tinUpdateQuery.setString("lastUpdatedBy", tin.getLastUpdatedBy());
		tinUpdateQuery.setString("tinGuid", tin.getTinGuid()); //for where clause
		rowsUpdated = tinUpdateQuery.executeUpdate();
		log.info("updateTin updated row(s)=" + rowsUpdated + " for listingId=" + tin.getTinGuid());
		return rowsUpdated;
	} 	
	
}
