package com.stubhub.domain.account.datamodel.dao.impl;

import com.stubhub.domain.account.datamodel.dao.UserContactDAO;
import com.stubhub.domain.account.datamodel.entity.UserContact;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component("userContactDAO")
public class UserContactDAOImpl implements UserContactDAO {
    private final static Logger LOGGER = LoggerFactory.getLogger(UserContactDAOImpl.class);

    @Autowired
    @Qualifier("accountHibernateTemplate")
    private HibernateTemplate hibernateTemplate;

    @Override
    @Transactional(readOnly = true)
    public UserContact getUserContactById(Long userContactId) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("get user contact by userContactId={}", userContactId);
        }

        Query query = this.hibernateTemplate.getSessionFactory()
                .getCurrentSession().getNamedQuery("UserContact.getUserContactById");
        query.setLong("userContactId", userContactId);
        List<UserContact> result = query.list();
        if (result != null && result.size() > 0) {
            return result.get(0);
        }
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public UserContact getDefaultUserContactByOwnerId(Long ownerId) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("get default user contact by ownerId={}", ownerId);
        }

        Query query = this.hibernateTemplate.getSessionFactory()
                .getCurrentSession().getNamedQuery("UserContact.getDefaultUserContactByOwnerId");
        query.setLong("ownerId", ownerId);
        List<UserContact> result = query.list();
        if (result != null && result.size() > 0) {
            return result.get(0);
        }

        return null;
    }

}