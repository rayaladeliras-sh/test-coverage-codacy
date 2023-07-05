package com.stubhub.domain.account.datamodel.dao.impl;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Component("commonDAO")
public class CommonDAO {
    private final static Logger log = LoggerFactory.getLogger(CommonDAO.class);

    @Autowired
    @Qualifier("accountHibernateTemplate")
    private HibernateTemplate hibernateTemplate;

    @Transactional
    public Integer getCoefficientVersion () {
        Query query = hibernateTemplate.getSessionFactory().getCurrentSession()
                .createSQLQuery("select COEFFICIENT_VERSION from coefficient_version where VERSION_STATUS ='ONLINE'");
        List<BigDecimal> result= query.list();
        return result.get(0).intValue();
    }

}
