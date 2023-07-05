package com.stubhub.domain.account.datamodel.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.stubhub.domain.account.datamodel.dao.UserDAO;
import com.stubhub.domain.account.datamodel.entity.UserDO;

/**
 * Created at 11/5/15 2:14 PM
 *
 * @author : Caron Zhao
 * @version : 1.0.0
 */
@Repository("userDAO")
public class UserDAOImpl implements UserDAO {
    private final static Logger LOGGER = LoggerFactory.getLogger(UserDAOImpl.class);
    public static final String GET_SELLER_PAYMENT_TYPE_ID_BY_USERID = "User.getSellerPaymentType";

    @Autowired
    @Qualifier("accountHibernateTemplate")
    private HibernateTemplate hibernateTemplate;

    @Override
    @Transactional(readOnly = true)
    public UserDO findUserByGuid(String userGuid) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("find user for guid " + userGuid);
        }

        Query query = hibernateTemplate.getSessionFactory().getCurrentSession()
                .createQuery(" from UserDO where userGuid = :userGuid");
        query.setString("userGuid", userGuid);
        Object result = query.uniqueResult();
        if (result != null) {
            return (UserDO) result;
        }

        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDO findUserById(String id) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("find user for id " + id);
        }

        Query query = hibernateTemplate.getSessionFactory().getCurrentSession()
                .createQuery(" from UserDO where ID = :ID");
        query.setString("ID", id);
        Object result = query.uniqueResult();
        if (result != null) {
            return (UserDO) result;
        }

        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Long getUserSellerPaymentType(Long userId){
    	LOGGER.info("Retrieving seller payment type for user =" + userId);

    	Query query =  this.hibernateTemplate.getSessionFactory().getCurrentSession()
                .getNamedQuery(GET_SELLER_PAYMENT_TYPE_ID_BY_USERID);
    	query.setLong("arg1", userId);
        @SuppressWarnings("unchecked")
        List<Number> result = query.list();
		return (result!=null ? result.get(0).longValue() : null);
    }
}