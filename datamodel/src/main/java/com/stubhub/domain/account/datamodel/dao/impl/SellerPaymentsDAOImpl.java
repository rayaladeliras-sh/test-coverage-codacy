package com.stubhub.domain.account.datamodel.dao.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.stubhub.domain.account.datamodel.dao.SellerPaymentsDAO;
import com.stubhub.domain.account.datamodel.entity.SellerPayment;
import com.stubhub.domain.account.datamodel.entity.SellerPaymentStatusEnum;
import com.stubhub.domain.account.datamodel.entity.SellerPayments;


@Component("sellerPaymentsDAO")
public class SellerPaymentsDAOImpl implements SellerPaymentsDAO {

	private final static Logger log = LoggerFactory.getLogger(SellerPaymentsDAOImpl.class);

	private final static int days = 90;
	private final static int month = 30;

	@Autowired
	@Qualifier("accountHibernateTemplate")
	private HibernateTemplate hibernateTemplate;

	@Transactional
	@Override
	public List<SellerPayments> getSellerPayments(Long sellerId,
			String recordType) {
		List<SellerPayments> results = null;
		log.debug("Get seller payments for sellerId=" + sellerId
				+ ", recordType=" + recordType);
		Query query = this.hibernateTemplate.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery("SellerPayments.getSellerPayments");
		query.setLong("sellerId", sellerId);
		query.setString("recordType", recordType);
		query.setInteger("days", days);
		results = query.list();
		return results;
	}

	@Transactional
	@Override
	public List<SellerPayments> getSellerPaymentsBySellerId(Long sellerId, String recordType, String sort, Calendar createdFromDate, Calendar createdToDate, Integer startRow, Integer rowNumber, String currencyCode) {
		log.debug("Get seller payments for sellerId=" + sellerId + ", recordType=" + recordType + ", currencyCode=" + currencyCode);
		Query query = this.hibernateTemplate.getSessionFactory().getCurrentSession().getNamedQuery("SellerPayments.getSellerPaymentsDefault");
		query.setInteger("days", days);
		if(createdFromDate != null || createdToDate != null || StringUtils.isNotEmpty(currencyCode)) {
			if (StringUtils.isNotEmpty(currencyCode)) {
				query = this.hibernateTemplate.getSessionFactory().getCurrentSession().getNamedQuery("SellerPayments.getSellerPaymentsWithFiltersCurrency");
				query.setString("currencyCode", currencyCode);
			} else {
				query = this.hibernateTemplate.getSessionFactory().getCurrentSession().getNamedQuery("SellerPayments.getSellerPaymentsWithFilters");
			}
			if (createdFromDate == null) {
				createdFromDate = Calendar.getInstance();
				createdFromDate.add(Calendar.DAY_OF_YEAR, -90);
			}
			if (createdToDate == null) {
				createdToDate = Calendar.getInstance();
			}
			query.setCalendar("createdFromDate", createdFromDate);
			query.setCalendar("createdToDate", createdToDate);
		}
		query.setLong("sellerId", sellerId);
		query.setString("recordType", recordType);

        if (startRow != null && startRow >= 0) {
            query.setFirstResult(startRow);
        }
        if (rowNumber != null && rowNumber >= 0) {
            query.setMaxResults(rowNumber);
        }

		return query.list();
	}
	


	@Override
	@Transactional(readOnly = true)
	public SellerPayment getSellerPaymentById(Long sellerPaymentId) {
		return (SellerPayment)hibernateTemplate.getSessionFactory().getCurrentSession().get(SellerPayment.class, sellerPaymentId);
	}

	@Transactional(readOnly = true)
	@Override
	@Deprecated
	public List<SellerPayment> findSellerPaymentsByStatus(Long sellerId, SellerPaymentStatusEnum status) {
		log.info("findSellerPaymentsByStatus, sellerId=%s, status=%s,%s", sellerId, status.getId(), status.getName());
		Query query = hibernateTemplate.getSessionFactory().getCurrentSession()
				.createQuery(" from SellerPayment where sellerId = :sellerId and sellerPaymentStatusId = :sellerPaymentStatusId");
		query.setLong("sellerId", sellerId);
		query.setLong("sellerPaymentStatusId", status.getId());
		List<SellerPayment> result = query.list();
		if (result != null && result.size() > 0) {
			if (log.isInfoEnabled()) {
				log.info("SellerPaymentsDAO::findSellerPaymentsByStatus results number:" + result.size());
			}
			return result;
		} else {
			return new ArrayList<SellerPayment>();
		}
	}
	@Transactional(readOnly = true)
	@Override
	public List<SellerPayment> findSellerPaymentsByStatus(Long sellerId, SellerPaymentStatusEnum status, Calendar latestPaymentDate) {
		log.info(String.format("findSellerPaymentsByStatus with date, sellerId=%s, status=%s,%s", sellerId, status.getId(), status.getName()));
		Query query = hibernateTemplate.getSessionFactory().getCurrentSession()
				.createQuery(" from SellerPayment where sellerId = :sellerId and sellerPaymentStatusId = :sellerPaymentStatusId and lastUpdatedDate <= :latestPaymentDate");
		query.setLong("sellerId", sellerId);
		query.setLong("sellerPaymentStatusId", status.getId());
		query.setCalendar("latestPaymentDate", latestPaymentDate);
		List<SellerPayment> result = query.list();
		if (result != null && result.size() > 0) {
			log.info(String.format("findSellerPaymentsByStatus results for seller=%s ,size=%s", sellerId, result.size()));
			return result;
		} else {
			log.info(String.format("findSellerPaymentsByStatus no results for seller=%s", sellerId));
			return new ArrayList<SellerPayment>();
		}
	}
	@Transactional(readOnly = true)
    @Override
    public SellerPayment getSellerPaymentByRefNumber(String refNumber) {
        if (refNumber == null) {
            return null;
        }

        Query query = hibernateTemplate.getSessionFactory().getCurrentSession().getNamedQuery("SellerPayment.getSellerPaymentByRefNumber");
        query.setParameter("refNumber", refNumber);
        List<SellerPayment> result = query.list();
        if (result == null || result.isEmpty()) {
            return null;
        }
        return result.get(0);
    }

	@Transactional(readOnly = true)
	@Override
	public List<SellerPayment> getSellerPaymentsByIds(List<Long> ids) {
	    if (ids == null || ids.isEmpty()) {
	        return Collections.emptyList();
	    }

	    Query query = hibernateTemplate.getSessionFactory().getCurrentSession().getNamedQuery("SellerPayment.getSellerPaymentsByIds");
	    query.setParameterList("ids", ids);
	    List<SellerPayment> result = query.list();
	    if (result == null) {
	        return Collections.emptyList();
	    }
	    return result;
	}

	@Override
	public void saveSellerPayment(SellerPayment sellerPayment) {
		hibernateTemplate.getSessionFactory().getCurrentSession().saveOrUpdate(sellerPayment);
	}

	@Override
	public int updateSellerPaymentStatus(SellerPayment sellerPayment) {
		Query query = hibernateTemplate.getSessionFactory().getCurrentSession().getNamedQuery("SellerPayments.updatePaymentStatus");
		query.setLong("statusId", sellerPayment.getSellerPaymentStatusId());
		query.setString("status", sellerPayment.getStatus());
		query.setCalendar("lastUpdated", sellerPayment.getLastUpdatedDate());
		query.setString("lastUpdateBy", sellerPayment.getLastUpdatedBy());
		query.setLong("id", sellerPayment.getId());
		query.setLong("sellerId", sellerPayment.getSellerId());
		return query.executeUpdate();
	}

	@Transactional
    @Override
    public long countSellerPaymentsBySellerId(Long sellerId, String recordType, Calendar createdFromDate,
            Calendar createdToDate, String currencyCode) {
        log.debug("Count seller payments for sellerId=" + sellerId + ", recordType=" + recordType + ", currencyCode=" + currencyCode);
        Query query = null;
        if(createdFromDate != null || createdToDate != null || StringUtils.isNotEmpty(currencyCode)) {
        	if (StringUtils.isNotEmpty(currencyCode)) {
        		query = this.hibernateTemplate.getSessionFactory().getCurrentSession().getNamedQuery("SellerPayments.countSellerPaymentsWithFiltersCurrency");
        		query.setString("currencyCode", currencyCode);
			} else {
				query = this.hibernateTemplate.getSessionFactory().getCurrentSession().getNamedQuery("SellerPayments.countSellerPaymentsWithFilters");
			}
            if (createdFromDate == null) {
                createdFromDate = Calendar.getInstance();
                createdFromDate.add(Calendar.DAY_OF_YEAR, -90);
            }
            if (createdToDate == null) {
                createdToDate = Calendar.getInstance();
            }
            query.setCalendar("createdFromDate", createdFromDate);
            query.setCalendar("createdToDate", createdToDate);
        } else {
            query = hibernateTemplate.getSessionFactory().getCurrentSession().getNamedQuery("SellerPayments.countSellerPaymentsDefault");
            query.setInteger("days", days);
        }
        query.setLong("sellerId", sellerId);
        query.setString("recordType", recordType);

        return ((BigDecimal) query.list().get(0)).longValue();
    }

    
    @Transactional
	@Override
	public List<SellerPayments> getSellerPaymentsIndy(Calendar fromDate,
			Calendar toDate) {
		
			Query query = this.hibernateTemplate.getSessionFactory().getCurrentSession().getNamedQuery("SellerPayments.getIndyPaymentsDefault");
			query.setInteger("days", month);
			if(fromDate != null || toDate != null) {
				query = this.hibernateTemplate.getSessionFactory().getCurrentSession().getNamedQuery("SellerPayments.getIndyPaymentsWithFilters");
				 log.info("fromDate seller payments for sellerId=" + fromDate);
				 log.info("toDate seller payments for sellerId=" + toDate);
				if (fromDate == null) {
					fromDate = Calendar.getInstance();
					fromDate.add(Calendar.DAY_OF_YEAR, -30);
				}
				if (toDate == null) {
					toDate = Calendar.getInstance();
				}
				
				query.setCalendar("createdFromDate", fromDate);
				query.setCalendar("createdToDate", toDate);
			}
	

			 List<SellerPayments> result = query.list();
			return result;
	
	}
}
