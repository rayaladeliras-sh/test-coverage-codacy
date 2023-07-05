package com.stubhub.domain.account.datamodel.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import oracle.jdbc.OracleTypes;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.stubhub.domain.account.datamodel.dao.TransactionSummaryDAO;

@Component("transactionSummaryDAO")
public class TransactionSummaryDAOImpl implements TransactionSummaryDAO {

	private static final String FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	private static final String FORMAT_YYYY_MM_DD_T_HH_MM_SS_Z = "yyyy-MM-dd'T'HH:mm:ss'Z'";

	private static final Logger log = LoggerFactory.getLogger(TransactionSummaryDAOImpl.class);
	
	
	@Autowired
	@Qualifier("accountROHibernateTemplate")
	private HibernateTemplate hibernateTemplate;

	/**
	 * Added this deprecated callable statement approach temporarily will fix
	 * through hibernate when upgrade to new version. The below invokes DB store
	 * procedure to get the summary details for the given userid.
	 * 
	 * @param i_user_id
	 * 
	 * @return List<Map>
	 * 
	 * @see com.stubhub.domain.account.datamodel.dao.TransactionSummaryDAO.
	 *      getSummaryDetails(long)
	 */
	@Transactional
	public List<HashMap<String, String>> getSummaryDetails(long userId, String currencyCode, boolean fullSummaryDetails) {

		List<HashMap<String, String>> results = new ArrayList<HashMap<String, String>>();
		try {
				log.info("Getting getSummaryDetails", userId);
				results = retrieveSummaryDetails(userId, currencyCode,fullSummaryDetails);
			}catch (Exception ex) {
			log.debug("Exception Occured in getSummary Details : ", ex);
		}
		return results;
	}

	@SuppressWarnings("deprecation")
	@Transactional
	public List<HashMap<String, String>> retrieveSummaryDetails(long userId, String currencyCode, boolean fullSummaryDetails) {
		ResultSet summaryResultSet = null;
		CallableStatement callableStatement = null;
		Connection conn = null;
		List<HashMap<String, String>> summaryDetailsList = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> orderStats = new HashMap<String, String>();
		HashMap<String, String> listingStats = new HashMap<String, String>();
		String currency = "USD";
		if(!StringUtils.isEmpty(currencyCode)){
			currency = currencyCode;
		}
		//As per DB Dev passing Boolean is compatible with DB store procedure
		String fullSummaryFlag = "FALSE";
		if(fullSummaryDetails){
			fullSummaryFlag = "TRUE";
		}

		try {
			conn = this.hibernateTemplate.getSessionFactory().getCurrentSession().connection();
			String sql = "{call get_user_txn_summary_v2 (?, ?, ?, ?)}";
			callableStatement = conn.prepareCall(sql);
			callableStatement.setString(2, currency);
			callableStatement.setLong(3, userId);
			callableStatement.setString(4, fullSummaryFlag);
			callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
			callableStatement.executeUpdate();
			log.debug("Transaction Summary Procedure Executed for {}", userId);

			summaryResultSet = (ResultSet) callableStatement.getObject(1);
			log.info("after result set rs");
			if (summaryResultSet != null) {
				while (summaryResultSet.next()) {
					log.info("Entering resultset of buildSummaryDetails");
					orderStats.put("CURRENCY", currency);
					
				    // CSAPIS-1813 >>>>
					
					if(summaryResultSet.getString("BEYOND_AUDIENCE_YN_FLAG") != null)
						orderStats.put("BEYOND_AUDIENCE_YN_FLAG", summaryResultSet.getString("BEYOND_AUDIENCE_YN_FLAG"));
					
					if(summaryResultSet.getString("BEYOND_AUDIENCE_EFF_DATE") != null)
						orderStats.put("BEYOND_AUDIENCE_EFF_DATE", summaryResultSet.getString("BEYOND_AUDIENCE_EFF_DATE"));
					
					if(summaryResultSet.getString("BEYOND_AUDIENCE_EXP_DATE") != null)
						orderStats.put("BEYOND_AUDIENCE_EXP_DATE", summaryResultSet.getString("BEYOND_AUDIENCE_EXP_DATE"));
					
					if(summaryResultSet.getString("SEM_SEGMENT_ID") != null)
						orderStats.put("SEM_SEGMENT_ID", summaryResultSet.getString("SEM_SEGMENT_ID"));
					
					if(summaryResultSet.getString("SEM_SEGMENT_NAME") != null)
						orderStats.put("SEM_SEGMENT_NAME", summaryResultSet.getString("SEM_SEGMENT_NAME"));
					
					// CSAPIS-1813 <<<<<

					if(fullSummaryDetails){
						if(summaryResultSet.getString("EVENT_ID") != null)
							orderStats.put("EVENT_ID", summaryResultSet.getString("EVENT_ID"));
						
						if(summaryResultSet.getString("PURCHASE_TOTAL") != null)
							orderStats.put("PURCHASE_TOTAL", summaryResultSet.getString("PURCHASE_TOTAL"));
						
						if(summaryResultSet.getString("OPEN_ORDER_PURCHASE_TOTAL") != null)
							orderStats.put("OPEN_ORDER_PURCHASE_TOTAL", summaryResultSet.getString("OPEN_ORDER_PURCHASE_TOTAL"));
						
						if(summaryResultSet.getString("PURCHASE_COUNT") != null)
							orderStats.put("PURCHASE_COUNT", summaryResultSet.getString("PURCHASE_COUNT"));
						
						if(summaryResultSet.getString("AVERAGE_ORDER_SIZE") != null)
							orderStats.put("AVERAGE_ORDER_SIZE", summaryResultSet.getString("AVERAGE_ORDER_SIZE"));
						
						if(summaryResultSet.getString("COMPLETED_SALES_COUNT") != null)
							orderStats.put("COMPLETED_SALES_COUNT", summaryResultSet.getString("COMPLETED_SALES_COUNT"));
						
						if(summaryResultSet.getString("CANCELLED_SALES_COUNT") != null)
							orderStats.put("CANCELLED_SALES_COUNT", summaryResultSet.getString("CANCELLED_SALES_COUNT"));
						
						if(summaryResultSet.getString("UNCONFIRMED_SALES_COUNT") != null)
							orderStats.put("UNCONFIRMED_SALES_COUNT", summaryResultSet.getString("UNCONFIRMED_SALES_COUNT"));
						
						if(summaryResultSet.getString("EVENT_ID") != null)
						    orderStats.put("CANCELLED_BUYS_COUNT", summaryResultSet.getString("CANCELLED_BUYS_COUNT"));
						
						if(summaryResultSet.getString("CANCELLED_BUYS_COUNT") != null)
							orderStats.put("COMPLETED_BUYS_COUNT", summaryResultSet.getString("COMPLETED_BUYS_COUNT"));
						
						if(summaryResultSet.getString("UNCONFIRMED_BUYS_COUNT") != null)
							orderStats.put("UNCONFIRMED_BUYS_COUNT", summaryResultSet.getString("UNCONFIRMED_BUYS_COUNT"));
						
						if(summaryResultSet.getString("TOP_BUYER") != null)
							orderStats.put("IS_TOP_BUYER", summaryResultSet.getString("TOP_BUYER"));
						
						if(summaryResultSet.getString("ACTIVE_COUNT") != null)
							listingStats.put("ACTIVE_USER_LISTING_COUNT", summaryResultSet.getString("ACTIVE_COUNT"));
						
						if(summaryResultSet.getString("INACTIVE_COUNT") != null)
							listingStats.put("INACTIVE_USER_LISTING_COUNT", summaryResultSet.getString("INACTIVE_COUNT"));
						
						if(summaryResultSet.getString("PENDING_LOCK_COUNT") != null)
							listingStats.put("PENDING_LOCK_USER_LISTING_COUNT",summaryResultSet.getString("PENDING_LOCK_COUNT"));
						
						if(summaryResultSet.getString("DELETED_COUNT") != null)
							listingStats.put("DELETED_USER_LISTING_COUNT", summaryResultSet.getString("DELETED_COUNT"));
						
						if(summaryResultSet.getString("INCOMPLETE_COUNT") != null)
							listingStats.put("INCOMPLETE_USER_LISTING_COUNT", summaryResultSet.getString("INCOMPLETE_COUNT"));
						
						if(summaryResultSet.getString("PENDING_LMS_COUNT") != null)
							listingStats.put("PENDING_LMS_APPROVAL_USER_LISTING_COUNT",summaryResultSet.getString("PENDING_LMS_COUNT"));
						
						if(summaryResultSet.getString("BUYER_FLIP_COUNT") != null)
							orderStats.put("BUYER_FLIP_COUNT", summaryResultSet.getString("BUYER_FLIP_COUNT").trim());
						
						if(summaryResultSet.getString("DROP_ORDER_RATE") != null)
							orderStats.put("DROP_ORDER_RATE", summaryResultSet.getString("DROP_ORDER_RATE"));
						
				}else{
					if(summaryResultSet.getString("PURCHASE_TOTAL") != null)
						orderStats.put("PURCHASE_TOTAL", summaryResultSet.getString("PURCHASE_TOTAL"));
					
					if(summaryResultSet.getString("OPEN_ORDER_PURCHASE_TOTAL") != null)
						orderStats.put("OPEN_ORDER_PURCHASE_TOTAL", summaryResultSet.getString("OPEN_ORDER_PURCHASE_TOTAL"));
					
					if(summaryResultSet.getString("PURCHASE_COUNT") != null)
						orderStats.put("PURCHASE_COUNT", summaryResultSet.getString("PURCHASE_COUNT"));
					
					if(summaryResultSet.getString("AVERAGE_ORDER_SIZE") != null)
						orderStats.put("AVERAGE_ORDER_SIZE", summaryResultSet.getString("AVERAGE_ORDER_SIZE"));	
					
					if(summaryResultSet.getString("TOP_BUYER") != null)
						orderStats.put("IS_TOP_BUYER", summaryResultSet.getString("TOP_BUYER"));
				}
						
					summaryDetailsList.add(orderStats);
					summaryDetailsList.add(listingStats);
			}
			}
		} 
		catch (SQLException sqlEx) {
			log.error("SQLException in retrieveSummary : ", sqlEx);
		}
		catch (Exception ex) {
			log.error("Exception Occured in retrieveSummary Details : ", ex);
		}finally {
			close(summaryResultSet);
			close(callableStatement);
			close(conn);
			} 
		return summaryDetailsList;
	}

	void close(ResultSet obj) {
		if (obj != null) {
			try {
				obj.close();
			} catch (SQLException ex) {
				log.debug("An error occured during obj clossing", ex);
			}
		}
	}

	void close(Connection obj) {
		if (obj != null) {
			try {
				obj.close();
			} catch (SQLException ex) {
				log.debug("An error occured during obj clossing", ex);
			}
		}
	}

	void close(CallableStatement obj) {
		if (obj != null) {
			try {
				obj.close();
			} catch (SQLException ex) {
				log.debug("An error occured during obj clossing", ex);
			}
		}
	}

}