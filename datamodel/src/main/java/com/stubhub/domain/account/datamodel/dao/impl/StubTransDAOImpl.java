package com.stubhub.domain.account.datamodel.dao.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.stubhub.common.exception.RecordNotFoundForIdException;
import com.stubhub.domain.account.datamodel.dao.StubTransDAO;
import com.stubhub.domain.account.datamodel.entity.StubTrans;

@Component("stubTransDAO")
public class StubTransDAOImpl implements StubTransDAO {

	private final static Logger log = LoggerFactory.getLogger(StubTransDAOImpl.class);

	@Autowired
	@Qualifier("accountHibernateTemplate")
	private HibernateTemplate hibernateTemplate;

	@Override
    @Transactional
	public StubTrans persist(StubTrans stubTrans){
		this.hibernateTemplate.getSessionFactory().getCurrentSession().saveOrUpdate(stubTrans);
		log.debug("stubtrans persisted. orderId=" + stubTrans.getOrderId());
		return stubTrans;
	}

	@Override
    @Transactional
	public List<StubTrans> getOrderProcSubStatusCode(Long tid){
		List<StubTrans> results = null;
		log.info("api_domain=account api_resource=orders api_method=getOrderProcSubStatusCode status=success message=Querying DB for the orderId=" + tid);
		SQLQuery sqlQuery = (SQLQuery)this.hibernateTemplate.getSessionFactory().getCurrentSession().getNamedQuery("Stubtrans.getById");
		sqlQuery.setLong("orderId", tid);
		results = sqlQuery.list();
		return results;
	}

	@Override
    @Transactional
	public int updateBuyerContactId(Long tid, Long contactId, String addedBy, Calendar dateAdded){
		int results = 0;
		log.info("api_domain=account api_resource=orders api_method=updateBuyerContactId status=success message=Querying DB for the orderId=" + tid);
		SQLQuery sqlQuery = (SQLQuery)this.hibernateTemplate.getSessionFactory().getCurrentSession().getNamedQuery("Stubtrans.updateBuyerContactId");
		sqlQuery.setLong("orderId", tid);
		sqlQuery.setLong("contactId", contactId);
		sqlQuery.setString("addedBy", addedBy);
		sqlQuery.setCalendar("dateAdded", dateAdded);
		results = sqlQuery.executeUpdate();
		return results;
	}

    @Override
    @Transactional
    public StubTrans getById(Long tid) throws RecordNotFoundForIdException {
    	StubTrans results = null;
		results=(StubTrans) this.hibernateTemplate.getSessionFactory().getCurrentSession().get(StubTrans.class, tid);
		if (results == null) {
            throw new RecordNotFoundForIdException("orderId", tid);
        }
		return results;
    }

	@Override
    @Transactional
	public int getBuyerFlipCount(Long userId){
		log.info("api_domain=account api_resource=summary api_method=getBuyerFlipCount status=success message=buyerflip count for the userId=" + userId);
		SQLQuery sqlQuery = (SQLQuery)hibernateTemplate.getSessionFactory().getCurrentSession().getNamedQuery("Stubtrans.getBuyerFlipCount");
		sqlQuery.setLong("userId", userId.longValue());
		if(sqlQuery.uniqueResult() != null)
		{
			return ((BigDecimal)sqlQuery.uniqueResult()).intValue();
		}
		return 0;
	}

	@Override
    @Transactional
	public void updateOrderProcStatus(final Long orderId, final Long newStatus){
		log.info("api_domain=account api_resource=orders api_method=updateOrderProcStatus status=success message=Update stub_trans table for the orderId=" + orderId + ", newStatus:" + newStatus);
		SQLQuery sqlQuery = (SQLQuery)hibernateTemplate.getSessionFactory().getCurrentSession().getNamedQuery("StubTrans.updateSTOrderProcStatusById");
		sqlQuery.setLong("newStatus", newStatus.longValue());
		sqlQuery.setLong("orderId", orderId.longValue());
		sqlQuery.executeUpdate();
	}


	@Override
    @Transactional
	public int getSelStubTicketCount(Long sellerId){
		log.info("api_domain=account api_resource=summary api_method=getSelStubTicketCount status=success message=Seller Stub Ticket Count count for the sellerId=" + sellerId);
		SQLQuery sqlQuery = (SQLQuery)hibernateTemplate.getSessionFactory().getCurrentSession().getNamedQuery("Stubtrans.getSelStubTicketCount");
		sqlQuery.setLong("sellerId", sellerId.longValue());
		if(sqlQuery.uniqueResult() != null)
		{
			return ((BigDecimal)sqlQuery.uniqueResult()).intValue();
		}
		return 0;
	}

	@Override
    @Transactional
	public int getSelTransTikCount(Long sellerId){
		log.info("api_domain=account api_resource=summary api_method=getSelTransTikCount status=success message=Seller Sel Trans CC Ticket Count count for the sellerId=" + sellerId);
		SQLQuery sqlQuery = (SQLQuery)hibernateTemplate.getSessionFactory().getCurrentSession().getNamedQuery("Stubtrans.getSelTransTikCount");
		sqlQuery.setLong("sellerId", sellerId.longValue());
		if(sqlQuery.uniqueResult() != null)
		{
			return ((BigDecimal)sqlQuery.uniqueResult()).intValue();
		}
		return 0;
	}

	@Override
    @Transactional
	public int getSelPayTicketCount(Long sellerId){
		log.info("api_domain=account api_resource=summary api_method=getSelPayTicketCount status=success message=Seller Sel Pay Ticket Count count for the sellerId=" + sellerId);
		SQLQuery sqlQuery = (SQLQuery)hibernateTemplate.getSessionFactory().getCurrentSession().getNamedQuery("Stubtrans.getSelPayTicketCount");
		sqlQuery.setLong("sellerId", sellerId.longValue());
		if(sqlQuery.uniqueResult() != null)
		{
			return ((BigDecimal)sqlQuery.uniqueResult()).intValue();
		}
		return 0;
	}

	@Override
	@Transactional
	public Map getUserSummaryOrderStats(Long buyerId){
		Map<String, String> result = null;
		log.info("api_domain=account api_resource=summary api_method=getUserSummaryOrderStats status=success message=Buyer Get User Summary Order count for the buyerId=" + buyerId);
		SQLQuery sqlQuery = (SQLQuery)hibernateTemplate.getSessionFactory().getCurrentSession().getNamedQuery("Stubtrans.getUserSummaryOrderStats");
		sqlQuery.setLong("buyerId", buyerId.longValue());
		if(sqlQuery.uniqueResult() != null){
			result = new HashMap<String, String>(3);
			Object[] resultSet = (Object[])sqlQuery.uniqueResult();

			int cancelled_count = ((BigDecimal)resultSet[0]).intValue();
			result.put("cancelled_order_count",String.valueOf(cancelled_count));

			int completed_count = ((BigDecimal)resultSet[1]).intValue();
			result.put("completed_order_count",String.valueOf(completed_count));

			int unconfirmed_count = ((BigDecimal)resultSet[2]).intValue();
			result.put("unconfirmed_order_count",String.valueOf(unconfirmed_count));
		}
		return result;
	}

	@Override
	@Transactional
	public Map getUserSummarySaleStats(Long sellerId){
		Map<String, String> result = null;
		log.info("api_domain=account api_resource=summary api_method=getUserSummarySaleStats status=success message=Buyer Get User Summary Sale count for the sellerId=" + sellerId);
		SQLQuery sqlQuery = (SQLQuery)hibernateTemplate.getSessionFactory().getCurrentSession().getNamedQuery("Stubtrans.getUserSummarySaleStats");
		sqlQuery.setLong("sellerId", sellerId.longValue());
		if(sqlQuery.uniqueResult() != null){
			result = new HashMap<String, String>(3);
			Object[] resultSet = (Object[])sqlQuery.uniqueResult();

			int cancelled_count = ((BigDecimal)resultSet[0]).intValue();
			result.put("cancelled_sale_count",String.valueOf(cancelled_count));

			int completed_count = ((BigDecimal)resultSet[1]).intValue();
			result.put("completed_sale_count",String.valueOf(completed_count));

			int unconfirmed_count = ((BigDecimal)resultSet[2]).intValue();
			result.put("unconfirmed_sale_count",String.valueOf(unconfirmed_count));
		}
		return result;
	}
}
