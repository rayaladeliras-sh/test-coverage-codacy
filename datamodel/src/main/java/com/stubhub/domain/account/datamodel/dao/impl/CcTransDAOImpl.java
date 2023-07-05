package com.stubhub.domain.account.datamodel.dao.impl;

import org.hibernate.SQLQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.stubhub.domain.account.datamodel.dao.CcTransDAO;
import com.stubhub.newplatform.persistence.adapter.UTCCalendarToTimestampAdapter;

@Component("ccTransDAO")
public class CcTransDAOImpl implements CcTransDAO {

	private final static Logger log = LoggerFactory.getLogger(CcTransDAOImpl.class);

	@Autowired
	@Qualifier("accountHibernateTemplate")
	private HibernateTemplate hibernateTemplate;
	
	@Override
    @Transactional
	public int updateByTid(Long oldTid, Long newTid, String operatorId) {
		int result = 0;
		log.info("api_domain=account api_resource=orders api_method=updateByTid message=updating ccTrans for orderId=" + oldTid + " to new orderId=" + newTid);
		SQLQuery query = (SQLQuery) hibernateTemplate.getSessionFactory().getCurrentSession().getNamedQuery("ccTrans.updateByTid");
		query.setLong("oldTid", oldTid);
		query.setLong("newTid", newTid);
		query.setString("lastUpdatedBy", operatorId);
		query.setCalendar("lastUpdatedDate", UTCCalendarToTimestampAdapter.getNewUTCCalendarInstanceStatic());
		result = query.executeUpdate();
		log.debug("updated ccTrans for orderId="+oldTid);
		return result;
	}
}
