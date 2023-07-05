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

import com.stubhub.domain.account.datamodel.dao.VenueConfigZoneSectionDAO;
import com.stubhub.domain.account.datamodel.entity.VenueConfigZoneSection;

@Component("venueConfigZoneSectionDAO")
public class VenueConfigZoneSectionDAOImpl implements VenueConfigZoneSectionDAO {
	
	private final static Logger log = LoggerFactory.getLogger(VenueConfigZoneSectionDAOImpl.class);
	
	@Autowired
	@Qualifier("accountHibernateTemplate")
	private HibernateTemplate hibernateTemplate;

	@Override
	@Transactional
	public List<VenueConfigZoneSection> getZoneSectionByVenueConfigId(Long venueConfigId){
		log.debug("message=\"get Zone Section by VenueConfigId\""+"VenueConfigId=" + venueConfigId);
		SQLQuery sqlQuery = (SQLQuery)this.hibernateTemplate
								.getSessionFactory()
								.getCurrentSession()
								.getNamedQuery("VenueConfigZoneSection.getZoneSectionByVenueConfigId");
		sqlQuery.setLong("venue_config_id", venueConfigId);
		List<VenueConfigZoneSection> zones =sqlQuery.list();		
    	return zones;      	
	}

}
