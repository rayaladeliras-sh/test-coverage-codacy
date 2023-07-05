package com.stubhub.domain.account.datamodel.dao.impl;

import java.math.BigDecimal;
import java.util.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

import com.stubhub.domain.account.datamodel.dao.SellerCcTransDAO;
import com.stubhub.domain.account.datamodel.entity.SellerCcTrans;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Component("sellerCcTransDAO")
public class SellerCcTransDAOImpl implements SellerCcTransDAO {
	
	private final static Logger log = LoggerFactory.getLogger(SellerCcTransDAOImpl.class);
	
	private final static int days = 90;
	
	@Autowired
	@Qualifier("accountHibernateTemplate")
	private HibernateTemplate hibernateTemplate;

    @Override
    public List<SellerCcTrans> getSellerCcTransBySellerCcId(Set<Long> sellerCcIds, Calendar fromDate, Calendar toDate,
            Integer startRow, Integer rowNumber, List<String> transTypeList, String currencyCode) {
        log.debug("Getting SellerCcTrans record for sellerCcIds=" + sellerCcIds);
        Query query = buildQuery(sellerCcIds, fromDate, toDate, false, transTypeList, currencyCode);

        if (startRow != null && startRow >= 0) {
            query.setFirstResult(startRow);
        }
        if (rowNumber != null && rowNumber >= 0) {
            query.setMaxResults(rowNumber);
        }

        return query.list();
    }

    private Query buildQuery(Set<Long> sellerCcIds, Calendar fromDate, Calendar toDate, boolean isCount, List<String> transTypeList, String currencyCode) {
        Query query = null;
        // all trans type for default
        if(transTypeList == null || transTypeList.isEmpty()){
        	transTypeList = new ArrayList<String>();
        	transTypeList.add("A");
        	transTypeList.add("C");
        	transTypeList.add("D");
        	transTypeList.add("V");
        }
        // empty cc list defending
        if(sellerCcIds == null || sellerCcIds.isEmpty()){
        	sellerCcIds = new HashSet<Long>();
        	sellerCcIds.add(-1L);
        }
        if (fromDate != null || toDate != null || StringUtils.isNotEmpty(currencyCode)) {
            String prefix = isCount ? "select count(SELLER_CC_TRANS_ID) " : "";
            String queryString = prefix + "from SellerCcTrans where sellerCcId in (:sellerCcIds) and transacionType in (:transTypeList) ";
            if (StringUtils.isNotEmpty(currencyCode)) {
                queryString += "and currencyCode = :currencyCode ";
            }
            queryString += "and lastUpdatedDate between :fromDate and :toDate ORDER BY lastUpdatedDate DESC";

            query = this.hibernateTemplate.getSessionFactory().getCurrentSession().createQuery(queryString);
            query.setParameterList("sellerCcIds", sellerCcIds);
            query.setParameterList("transTypeList", transTypeList);
            if (fromDate == null) {
                fromDate = Calendar.getInstance();
                fromDate.add(Calendar.DAY_OF_YEAR, -90);
            }
            if (toDate == null) {
                toDate = Calendar.getInstance();
            }

            query.setCalendar("fromDate", fromDate);
            query.setCalendar("toDate", toDate);
            if (StringUtils.isNotEmpty(currencyCode)) {
                query.setString("currencyCode", currencyCode);
            }
        } else {
            if (isCount) {
                query = this.hibernateTemplate.getSessionFactory().getCurrentSession()
                        .getNamedQuery("SellerCcTrans.findBySellerCcId.count");
            } else {
                query = this.hibernateTemplate.getSessionFactory().getCurrentSession()
                        .getNamedQuery("SellerCcTrans.findBySellerCcId");
            }
            query.setParameterList("sellerCcIds", sellerCcIds);
            query.setParameterList("transTypeList", transTypeList);
            query.setInteger("days", days);
        }
        return query;
    }

    @Override
    public long countSellerCcTransBySellerCcId(Set<Long> sellerCcIds, Calendar from, Calendar to, List<String> transTypeList, String currencyCode) {
        log.debug("Getting SellerCcTrans records count for sellerCcIds=" + sellerCcIds);
        Query query = buildQuery(sellerCcIds, from, to, true, transTypeList, currencyCode);
        return (Long) query.iterate().next();
    }

    @Override
    @Transactional
    public Map<Long, BigDecimal> getChargeToSellerAmountByTid(List<Long> tids) {
        Map<Long, BigDecimal> tidChargeAmountMap = Maps.newHashMap();
        if (CollectionUtils.isEmpty(tids)){
            return tidChargeAmountMap;
        }
        List<List<Long>> tidsList = Lists.partition(tids, 1000);
        for (List<Long> tidList : tidsList){
            Query query = this.hibernateTemplate.getSessionFactory().getCurrentSession().createSQLQuery("select tid, sum(case when trans_type='D' then amount else 0-amount end) as amount from seller_cc_trans where TRANS_TYPE in ('C','D') and RESULT_CODE = 0 and tid in (:tids) group by tid ");
            query.setParameterList("tids", tidList);
            List<Object[]> tidChargeToSellerAmounts = query.list();
            if (CollectionUtils.isEmpty(tidChargeToSellerAmounts)){
                continue;
            }
            for (Object[] result : tidChargeToSellerAmounts){
                tidChargeAmountMap.put(Long.parseLong(result[0].toString()), new BigDecimal(result[1].toString()));
            }
        }
        return tidChargeAmountMap;
    }
}
