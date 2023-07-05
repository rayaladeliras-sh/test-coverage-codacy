package com.stubhub.domain.account.datamodel.dao.impl;

import org.hibernate.SQLQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.stubhub.domain.account.datamodel.dao.TaxPayerDetailsDAO;
import com.stubhub.domain.account.datamodel.entity.TaxPayerDetails;

import java.io.Serializable;

/**
 * @author vpothuru
 * date 11/02/15
 *
 */
@Repository("taxPayerDetailsDAO")
public class TaxPayerDetailsDAOImpl implements TaxPayerDetailsDAO {
	private final static Logger log = LoggerFactory.getLogger(TaxPayerDetailsDAOImpl.class);

	@Autowired
	@Qualifier("accountHibernateTemplate")
	private HibernateTemplate hibernateTemplate;

	@Override
    @Transactional(readOnly = true)
	public TaxPayerDetails getTaxPayerDetailsByUserId(Long userId) {
		if (userId == null) return null;

		SQLQuery getTaxPayerQuery = (SQLQuery) this.hibernateTemplate.getSessionFactory().getCurrentSession().getNamedQuery("TaxPayer.getTaxPayerByUserId");

		log.debug("getTaxPayerDetails for user guid " + getTaxPayerQuery.getQueryString());

		getTaxPayerQuery.setLong("userId", userId);
		Object result = getTaxPayerQuery.uniqueResult();
		if (result != null) {
			return (TaxPayerDetails) result;
		}
		log.info("no taxpayerdetails found for " + userId);
		return null;
	}

	@Override
    @Transactional(readOnly = true)
	public TaxPayerDetails getTaxPayerDetailsByUserGuid(String userGuid) {
		String sqlQuery = "SELECT * FROM TAXPAYER WHERE user_id in(SELECT ID FROM USERS WHERE user_cookie_guid=:userGuid)";

		if (log.isDebugEnabled()) {
			log.debug("getTaxPayerDetails for user guid " + userGuid);
		}

		SQLQuery query = hibernateTemplate.getSessionFactory().getCurrentSession()
				.createSQLQuery(sqlQuery).addEntity(TaxPayerDetails.class);
		query.setString("userGuid",userGuid);
		Object result = query.uniqueResult();
		if (result != null) {
			return (TaxPayerDetails) result;
		}

		if (log.isInfoEnabled()) {
			log.info("No record found for the user Guid" + userGuid);
		}
		return null;
	}

	@Override
	@Transactional
	public int updateTaxPayerDetails(TaxPayerDetails taxPayerDetails, boolean updateAddress) {

		int rowsUpdated = 0;
		if(taxPayerDetails == null) return rowsUpdated;
		
		SQLQuery taxPayerUpdateQuery = getTaxPayerUpdateSQLQuery(taxPayerDetails, updateAddress);
		rowsUpdated = taxPayerUpdateQuery.executeUpdate();
		log.debug("updateTin updated row(s)=" + rowsUpdated + " for listingId=" + taxPayerDetails.getUserId());

		return rowsUpdated;
	}

	@Override
	@Transactional
	public long addTaxPayerDetails(TaxPayerDetails taxPayerDetails) {
		long taxPayerDetailsId = 0;
		if (taxPayerDetails != null) {
			taxPayerDetailsId =  (long) this.hibernateTemplate.getSessionFactory().getCurrentSession().save(taxPayerDetails);
		}
		return taxPayerDetailsId;
	}

	private SQLQuery getTaxPayerUpdateSQLQuery(TaxPayerDetails taxPayerDetails, boolean updateAddress) {
		
		SQLQuery taxPayerUpdateQuery = null;
		
		if (updateAddress) {
			taxPayerUpdateQuery = (SQLQuery) this.hibernateTemplate.getSessionFactory().getCurrentSession().getNamedQuery("TaxPayer.updateTaxPayerWithAddress");

			taxPayerUpdateQuery.setInteger("taxIdStatus", taxPayerDetails.getTaxIdStatus());
			taxPayerUpdateQuery.setString("tinType", taxPayerDetails.getTinType());
			taxPayerUpdateQuery.setString("tinGuid", taxPayerDetails.getTinGuid());
			taxPayerUpdateQuery.setString("lastUpdateBy", taxPayerDetails.getUserId().toString());

			taxPayerUpdateQuery.setString("nameFirst", taxPayerDetails.getNameFirst());
			taxPayerUpdateQuery.setString("nameLast", taxPayerDetails.getNameLast());
			taxPayerUpdateQuery.setString("company", taxPayerDetails.getCompany());
			taxPayerUpdateQuery.setString("addr1", taxPayerDetails.getAddr1());
			taxPayerUpdateQuery.setString("addr2", taxPayerDetails.getAddr2());
			taxPayerUpdateQuery.setString("addrCity", taxPayerDetails.getAddrCity());
			taxPayerUpdateQuery.setString("addrState", taxPayerDetails.getAddrState());
			taxPayerUpdateQuery.setString("addrZip", taxPayerDetails.getAddrZip());
			taxPayerUpdateQuery.setString("phone1", taxPayerDetails.getPhone1());
			taxPayerUpdateQuery.setString("addrCountry", taxPayerDetails.getAddrCountry());
			taxPayerUpdateQuery.setString("taxCountryCode", taxPayerDetails.getTaxCountry());
			taxPayerUpdateQuery.setString("countryCallingCode", taxPayerDetails.getAddrCountry());
			
			taxPayerUpdateQuery.setLong("userId", taxPayerDetails.getUserId()); //where clause

		} else {
			taxPayerUpdateQuery = (SQLQuery) this.hibernateTemplate.getSessionFactory().getCurrentSession().getNamedQuery("TaxPayer.updateTaxPayerWithOutAddress");

			taxPayerUpdateQuery.setInteger("taxIdStatus", taxPayerDetails.getTaxIdStatus());
			taxPayerUpdateQuery.setString("tinType", taxPayerDetails.getTinType());
			taxPayerUpdateQuery.setString("tinGuid", taxPayerDetails.getTinGuid());
			taxPayerUpdateQuery.setString("lastUpdateBy", taxPayerDetails.getUserId().toString());
			
			taxPayerUpdateQuery.setLong("userId", taxPayerDetails.getUserId()); // where clause
		}
		
		return taxPayerUpdateQuery;
		
	}
}
