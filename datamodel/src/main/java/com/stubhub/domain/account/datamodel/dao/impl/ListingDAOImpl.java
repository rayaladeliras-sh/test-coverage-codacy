package com.stubhub.domain.account.datamodel.dao.impl;

import org.hibernate.SQLQuery;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.stubhub.domain.account.datamodel.dao.ListingDAO;
import com.stubhub.domain.account.datamodel.entity.Listing;
import com.stubhub.newplatform.persistence.adapter.UTCCalendarToTimestampAdapter;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component("listingDAO")
public class ListingDAOImpl implements ListingDAO {

	private final static Logger log = LoggerFactory.getLogger(ListingDAOImpl.class);

	@Autowired
	@Qualifier("accountHibernateTemplate")
	private HibernateTemplate hibernateTemplate;

	@Override
    @Transactional
	public int updateListing(Listing listing){
		int rowsUpdated = 0;
		SQLQuery query = (SQLQuery) this.hibernateTemplate.getSessionFactory().getCurrentSession().getNamedQuery("Listing.updateQuantityRemain");
		query.setLong("listingId", listing.getId());
		query.setInteger("quantityRemain", listing.getQuantityRemain());
		query.setString("lastUpdatedBy", listing.getLastUpdatedBy());
		query.setCalendar("lastUpdatedTS", UTCCalendarToTimestampAdapter.getNewUTCCalendarInstanceStatic());
		query.setString("seats", listing.getSeats());
		rowsUpdated = query.executeUpdate();
		log.debug("updated row=" + rowsUpdated + " for listingId=" + listing.getId());
		return rowsUpdated;
	}

	@Override
    @Transactional
    public int updateListingPaymentType(Long listingId, Long sellerPaymentTypeId){
		log.info("api_domain=account api_resource=orders api_method=updateListingPaymentType status=success message=Querying DB for the listingId=" + listingId);
		Query query = hibernateTemplate.getSessionFactory().getCurrentSession().createSQLQuery("UPDATE tickets set seller_payment_type_id=:sellerPaymentTypeId" + " WHERE id=:listingId");
		query.setParameter("sellerPaymentTypeId", sellerPaymentTypeId);
		query.setParameter("listingId", listingId);
		int result = query.executeUpdate();
		log.info("api_domain=account api_resource=orders api_method=updateSellerPaymentTypeId status=success message=Successfully updated sellerPaymentType Id for listingId=" + listingId);
		return result;
    }

	@Override
	@Transactional(readOnly = true)
	public Map<String, String> getListingsLastUpdates(Set<String> listingIds) {
			Map<String, String> listingsUpdatedBys = new HashMap<String, String>();
			if (listingIds == null || listingIds.isEmpty()) {
				return listingsUpdatedBys;
			}
			Query query = this.hibernateTemplate.getSessionFactory().getCurrentSession().createSQLQuery("select t.id, t.last_updated_by from tickets t where t.id in (:ids) ");
			query.setParameterList("ids", listingIds);

			final List<Object[]> list = query.list();

			if (list != null && !list.isEmpty()) {
				for (Object[] entry : list) {
					if (entry.length == 2 && entry[0] != null && entry[1] != null) {
						listingsUpdatedBys.put(String.valueOf(entry[0]), String.valueOf(entry[1]));
					}
				}
			}
			return listingsUpdatedBys;
		}

	@Override
	@Transactional
	public Map getUserSummaryTicketStats(Long sellerId){
		Map<String, String> result = null;
		log.info("api_domain=account api_resource=summary api_method=getUserSummaryTicketStats status=success message=Seller Get User Summary Ticket count for the sellerId=" + sellerId);
		SQLQuery sqlQuery = (SQLQuery)hibernateTemplate.getSessionFactory().getCurrentSession().getNamedQuery("Listing.getUserSummaryTicketStats");
		sqlQuery.setLong("sellerId", sellerId.longValue());
		if(sqlQuery.uniqueResult() != null){
			result = new HashMap<String, String>(4);
			Object[] resultSet = (Object[])sqlQuery.uniqueResult();

			int active_ticket_count = ((BigDecimal)resultSet[0]).intValue();
			result.put("active_ticket_count",String.valueOf(active_ticket_count));

			int inactive_ticket_count = ((BigDecimal)resultSet[1]).intValue();
			result.put("inactive_ticket_count",String.valueOf(inactive_ticket_count));

			int deleted_ticket_count = ((BigDecimal)resultSet[2]).intValue();
			result.put("deleted_ticket_count",String.valueOf(deleted_ticket_count));

			int pending_lms_count = ((BigDecimal)resultSet[3]).intValue();
			result.put("pending_lms_count",String.valueOf(pending_lms_count));
		}
		return result;
	}
}
