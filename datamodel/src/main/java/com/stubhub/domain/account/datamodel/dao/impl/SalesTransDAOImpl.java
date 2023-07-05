package com.stubhub.domain.account.datamodel.dao.impl;

import java.util.Calendar;
import java.util.List;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.stubhub.common.exception.RecordNotFoundForIdException;
import com.stubhub.domain.account.datamodel.dao.SalesTransDAO;
import com.stubhub.domain.account.datamodel.entity.SalesTrans;

@Component("salesTransDAO")
public class SalesTransDAOImpl implements SalesTransDAO {
	
	private final static Logger log = LoggerFactory.getLogger(SalesTransDAOImpl.class);
	
	@Autowired
	@Qualifier("accountHibernateTemplate")
	private HibernateTemplate hibernateTemplate;
	
	@Transactional(readOnly = true)
	@Override
	public SalesTrans getByTId(Long saleId) throws RecordNotFoundForIdException {
		if (saleId == null) {
            return null;
        }
		Query query = hibernateTemplate.getSessionFactory().getCurrentSession().getNamedQuery("SalesTrans.getByTid");
        query.setLong("arg1", saleId);
        List<SalesTrans> result = query.list();
        if (result == null || result.isEmpty()) {
            return null;
        }
        return result.get(0);
	}

	@Transactional(readOnly = true)
	@Override
	public List<SalesTrans> getByBuyerId(Long buyerId,Integer startRow, Integer rowNumber) {
		log.debug("Get sale trans by buyerId=" + buyerId);
		Query query = this.hibernateTemplate.getSessionFactory().getCurrentSession().getNamedQuery("SalesTrans.getTidByBuyerID");
		if (buyerId == null) {
            return null;
        }
		query.setLong("arg1", buyerId);
		if (startRow != null && startRow >= 0) {
            query.setFirstResult(startRow);
        }
        if (rowNumber != null && rowNumber >= 0) {
            query.setMaxResults(rowNumber);
        }
        return query.list();
	}

	@Transactional(readOnly = true)
	@Override
	public List<SalesTrans> getByEventDate(Calendar eventStartDate, Calendar eventEndDate, Integer startRow,
			Integer rowNumber) {
		log.debug("Get stub trans by eventStartDate=" + eventStartDate + ", eventEndDate=" + eventEndDate);
		Query query = this.hibernateTemplate.getSessionFactory().getCurrentSession().getNamedQuery("SalesTrans.getTidByEventDate");
		if(eventStartDate != null || eventEndDate != null) {
			query.setCalendar("eventStartDate", eventStartDate);
			query.setCalendar("eventEndDate", eventEndDate);
		}
		if (startRow != null && startRow >= 0) {
            query.setFirstResult(startRow);
        }
        if (rowNumber != null && rowNumber >= 0) {
            query.setMaxResults(rowNumber);
        }

		return query.list();
	}

	@Transactional(readOnly = true)
	@Override
	public List<SalesTrans> getByBuyerIDAndEventDate(Long buyerId, Calendar eventStartDate, Calendar eventEndDate,
			Integer startRow, Integer rowNumber) {
		log.debug("Get stub trans by eventStartDate=" + eventStartDate + ", eventEndDate=" + eventEndDate);
		Query query = this.hibernateTemplate.getSessionFactory().getCurrentSession().getNamedQuery("SalesTrans.getTidByBuyerIDAndEventDate");
		if(eventStartDate != null || eventEndDate != null) {
			query.setCalendar("eventStartDate", eventStartDate);
			query.setCalendar("eventEndDate", eventEndDate);
		}
		query.setLong("arg1", buyerId);
		if (startRow != null && startRow >= 0) {
            query.setFirstResult(startRow);
        }
        if (rowNumber != null && rowNumber >= 0) {
            query.setMaxResults(rowNumber);
        }

		return query.list();
	}
}
