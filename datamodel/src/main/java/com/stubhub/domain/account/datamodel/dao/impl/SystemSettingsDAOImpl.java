package com.stubhub.domain.account.datamodel.dao.impl;

import com.stubhub.domain.account.datamodel.dao.SystemSettingsDAO;
import com.stubhub.domain.account.datamodel.entity.SystemSettings;
import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author yuntzhao
 */
@Component("systemSettingsDAO")
public class SystemSettingsDAOImpl implements SystemSettingsDAO {

    @Autowired
    @Qualifier("accountHibernateTemplate")
    private HibernateTemplate hibernateTemplate;

    @Override
    @Transactional
    public SystemSettings findByName(String name) {
        SQLQuery sqlQuery = (SQLQuery)this.hibernateTemplate.getSessionFactory()
                .getCurrentSession()
                .createSQLQuery("SELECT * FROM SYSTEM_SETTINGS WHERE NAME=:name")
                .addEntity(SystemSettings.class);
        sqlQuery.setParameter("name", name);
        List results = sqlQuery.list();
        if (results != null && results.size() > 0) {
            return (SystemSettings)results.get(0);
        }
        return null;
    }
}
