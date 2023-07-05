package com.stubhub.domain.account.datamodel.dao.impl;

import com.stubhub.domain.account.datamodel.dao.BrokerLicenseDAO;
import com.stubhub.domain.account.datamodel.entity.BrokerLicense;
import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by mengli on 11/14/18.
 */
@Component("brokerLicenseDAOImpl")
public class BrokerLicenseDAOImpl implements BrokerLicenseDAO {

    private final static Logger log = LoggerFactory.getLogger(BrokerLicenseDAOImpl.class);

    public static final String GET_BROKER_LICENSES_BY_SELLER_GUID_QUERY = "BrokerLicense.getBrokerLicensesBySellerGuid";
    public static final String GET_BROKER_LICENSE_QUERY = "BrokerLicense.getBrokerLicense";

    @Autowired
    @Qualifier("accountHibernateTemplate")
    private HibernateTemplate hibernateTemplate;


    /**
     * Save new broker license number
     *
     * @param brokerLicense
     * @return user broker license id
     */
    @Override
    @Transactional
    public long saveBrokerLicense(BrokerLicense brokerLicense) {
        long userBrokerLicenseId = 0L;
        if (brokerLicense == null) {
            log.error("message={}", "broker license is null");
            return userBrokerLicenseId;
        }

        if(brokerLicense.getBrokerLicenseNumber().trim().length() > 20) {
            log.error("message={}", "broker license number maximum length is 20");
            return userBrokerLicenseId;
        }

        userBrokerLicenseId = (Long) hibernateTemplate.getSessionFactory().getCurrentSession().save(brokerLicense);
        log.debug("message=\"saved a broker license\" sellerGuid={} userBrokerLicenseId={}",
                brokerLicense.getSellerGuid(),
                brokerLicense.getUserBrokerLicenseId());

        return userBrokerLicenseId;
    }


    /**
     * Get broker licenses by seller guid
     *
     * @param sellerGuid
     * @return list of broker license entity
     */
    @Override
    @Transactional(readOnly = true)
    public List<BrokerLicense> getBrokerLicensesBySellerGuid(String sellerGuid) {
        List<BrokerLicense> brokerLicenses;

        if (StringUtils.isEmpty(sellerGuid)) {
            log.error("message=\"sellerGuid is null or empty\" sellerGuid = {}", sellerGuid);
        }

        SQLQuery getBrokerLicensesQuery = (SQLQuery) this.hibernateTemplate.getSessionFactory().getCurrentSession().getNamedQuery(GET_BROKER_LICENSES_BY_SELLER_GUID_QUERY);
        getBrokerLicensesQuery.setString("brokerLicenseSellerGuid", sellerGuid);
        log.debug("message=\"Get broker licenses by seller Guid\" sql={}", getBrokerLicensesQuery.getQueryString());

        brokerLicenses = getBrokerLicensesQuery.list();

        if (brokerLicenses == null || brokerLicenses.isEmpty()) {
            log.error("message=\"No broker license found for seller\" sellerGuid={}", sellerGuid);
        }
        return brokerLicenses;
    }


    /**
     *
     * @param userBrokerLicenseId
     * @return broker license entity
     */
    @Override
    @Transactional(readOnly = true)
    public BrokerLicense getBrokerLicense(long userBrokerLicenseId) {
        SQLQuery getBrokerLicenseQuery = (SQLQuery) this.hibernateTemplate.getSessionFactory().getCurrentSession().getNamedQuery(GET_BROKER_LICENSE_QUERY);
        getBrokerLicenseQuery.setLong("userBrokerLicenseId", userBrokerLicenseId);
        log.debug("message=\"Get broker license by seller Guid\" sql={}", getBrokerLicenseQuery.getQueryString());

        Object object = getBrokerLicenseQuery.uniqueResult();
        if(object != null) {
            return (BrokerLicense) object;
        }
        log.info("no broker license found for " + userBrokerLicenseId);
        return null;
    }


    @Override
    @Transactional
    public int updateBrokerLicenseBySellerGuid(String sellerGuid, String brokerLicenseNumber, String stateCode, String countryCode, Integer active, long userBrokerLicenseId) {
        int result = 0;

        if (StringUtils.isEmpty(sellerGuid)) {
            log.error("message={}", "seller guid is null or empty");
            return result;
        }

        if(brokerLicenseNumber.trim().length() > 20) {
            log.error("message={}", "broker license number maximum length is 20");
            return result;
        }

        Session session = this.hibernateTemplate.getSessionFactory().getCurrentSession();
        BrokerLicense brokerLicense = getBrokerLicense(userBrokerLicenseId);
        if(brokerLicense == null) {
            log.error("message={}", "broker license is null for id " + userBrokerLicenseId);
            return 0;
        }

        brokerLicense.setActive(active);
        brokerLicense.setStateCode(stateCode);
        brokerLicense.setCountryCode(countryCode);
        brokerLicense.setBrokerLicenseNumber(brokerLicenseNumber);
        session.update(brokerLicense);
        log.debug("message=\"updated a broker license\" sellerGuid={}",
                sellerGuid);
        return 1;
    }
}
