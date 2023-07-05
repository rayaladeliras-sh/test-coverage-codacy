package com.stubhub.domain.account.datamodel.dao.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.util.CollectionUtils;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.stubhub.domain.account.datamodel.dao.AppliedCreditMemoDAO;
import com.stubhub.domain.account.datamodel.entity.AppliedCreditMemoDO;

@Component("appliedCreditMemoDAOImpl")
public class AppliedCreditMemoDAOImpl implements AppliedCreditMemoDAO {

    @Resource(name = "accountHibernateTemplate")
    private HibernateTemplate template;

    private final static Logger log = LoggerFactory.getLogger(AppliedCreditMemoDAOImpl.class);

    @Transactional(readOnly = true)
    @Override
    public List<AppliedCreditMemoDO> findByAppliedPaymentId(Long pid) {
        log.debug("findByAppliedPaymentId pid=" + pid);
        Query query = template.getSessionFactory().getCurrentSession()
                .createQuery(" FROM AppliedCreditMemoDO WHERE appliedPid = :appliedPid");
        query.setLong("appliedPid", pid);
        List<AppliedCreditMemoDO> results = query.list();
        if (results == null) {
            return Collections.emptyList();
        }
        return results;
    }
    @Transactional(readOnly = true)
    @Override
    public Map<Long, BigDecimal> findAppliedAmountByAppliedPaymentId(List<Long> pids) {
        Map<Long, BigDecimal> resultMap = Maps.newHashMap();
        if (CollectionUtils.isEmpty(pids)){
            return Maps.newHashMap();
        }
        List<List<Long>> pidsList = Lists.partition(pids,1000);
        for (List<Long> pidList : pidsList){
            Query query = template.getSessionFactory().getCurrentSession().createQuery("select appliedPid, sum(appliedAmount) FROM AppliedCreditMemoDO WHERE appliedPid in(:appliedPids) and cancelled = 0 group by appliedPid");
            query.setParameterList("appliedPids", pidList);
            List<Object[]> result = query.list();
            if (CollectionUtils.isEmpty(result)){
                continue;
            }
            for (Object[] res :result){
                resultMap.put(Long.parseLong(res[0].toString()), new BigDecimal(res[1].toString()));
            }
        }
        return resultMap;
    }
}