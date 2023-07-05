package com.stubhub.domain.account.datamodel.dao.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.stubhub.domain.account.datamodel.dao.EmailLogsDAO;
import com.stubhub.domain.account.datamodel.entity.EmailLog;

@SuppressWarnings("unchecked")
@Component("emailLogsDAO")
public class EmailLogsDAOImpl implements EmailLogsDAO {

	private static final Logger log = LoggerFactory.getLogger(EmailLogsDAOImpl.class);

	@Autowired
	@Qualifier("accountHibernateTemplate")
	private HibernateTemplate hibernateTemplate;
	
	private Session getSession() {
		return this.hibernateTemplate.getSessionFactory().getCurrentSession();
	}

	@Override
    @Transactional
	public EmailLog getEmailById(Long emailId) {
		try{
			SQLQuery sqlQuery = (SQLQuery) getSession().getNamedQuery("EmailLogs.getEmailById");
			sqlQuery.setLong("arg1", emailId);
			List<EmailLog> results = sqlQuery.list();
			if (results != null && results.size() > 0)
				return results.get(0);
		}catch(Exception e){
			log.error("api_domain=account api_resource=emails api_method=findEmailById " +
					"status=error message=Error getting email from DB for emailId=" + emailId , e);
		}
		return null;
	}
	
	@Override
	@Transactional
	public List<EmailLog> getUserMessagesByUserId(Long userId, Calendar fromDate, Calendar toDate, String start,
			String rows) {
		try {
			SQLQuery sqlQuery = (SQLQuery) getSession().getNamedQuery("UserMessages.getUserMessagesByUserId");
			
			sqlQuery.setLong("usrId", userId);
			sqlQuery.setCalendar("fromDate", fromDate);
			sqlQuery.setCalendar("toDate", toDate);
			sqlQuery.setString("start", start);
			sqlQuery.setString("rows", rows);
			
			log.info("Query: {}", sqlQuery.toString());
			
			return sqlQuery.list();
			
		} catch (Exception e) {
			log.error("api_domain=account api_resource=emails api_method=getUserMessagesByUserId status=error message=Error getting email logs from DB for userId=" + userId, e);
			return null;
		}
	}
	
	@Override
	@Transactional
	public List<EmailLog> getEmailLogsByUserIdAndCriteria(Long userId, Long orderId, Long buyerOrderId, String subject,
			Calendar fromDate, Calendar toDate, String start, String rows) {

		try {

			StringBuilder query = new StringBuilder(
					"SELECT emailId, userId, tId, buyerOrderId, addressFrom, addressTo, addressCc, addressBcc, dateAdded, dateSent, subject, format, body FROM (Select id AS emailId, user_id AS userId, tid AS tId, buyer_order_id AS buyerOrderId, addr_from AS addressFrom, addr_to as addressTo, addr_cc AS addressCc, addr_bcc AS addressBcc, date_added AS dateAdded, date_sent AS dateSent, subject AS subject, format AS format, body AS body from email_logs where user_id = :userId");

			if (fromDate != null && toDate != null)
				query.append(" AND date_added BETWEEN :fromDate AND :toDate");

			if ((orderId != null) && (buyerOrderId != null)) {
				query.append(" AND (tid = :orderId OR buyer_order_id = :buyerOrderId)");
			} else if (buyerOrderId != null) {
				query.append(" AND buyer_order_id = :buyerOrderId");
			} else if (orderId != null) {
				query.append(" AND tid =:orderId");
			}

			if (subject != null)
				query.append(" AND LOWER(subject) LIKE LOWER(").append(subject)
						.append(")");

			query.append(" ORDER BY date_added DESC ) WHERE ROWNUM >= :start AND ROWNUM <= :rows");

			SQLQuery sqlQuery = (SQLQuery) getSession().createSQLQuery(query.toString()).addEntity(EmailLog.class);

			sqlQuery.setLong("userId", userId);
			sqlQuery.setCalendar("fromDate", fromDate);
			sqlQuery.setCalendar("toDate", toDate);

			if ((orderId != null) && (buyerOrderId != null)) {
				sqlQuery.setLong("orderId", orderId);
				sqlQuery.setLong("buyerOrderId", buyerOrderId);
			} else if (buyerOrderId != null) {
				sqlQuery.setLong("buyerOrderId", buyerOrderId);
			} else if (orderId != null) {
				sqlQuery.setLong("orderId", orderId);
			}

/*			sqlQuery.setLong("orderId", orderId);
			sqlQuery.setLong("buyerOrderId", buyerOrderId);*/
			
			sqlQuery.setString("start", start);
			sqlQuery.setString("rows", rows); 
			
			return sqlQuery.list();
	
		} catch (Exception e) {
			log.error("api_domain=account api_resource=emails api_method=getEmailLogsByUserIdAndCrteria status=error message=Error getting email logs from DB for userId=" + userId, e);
			return null;
		}
	}	
}
