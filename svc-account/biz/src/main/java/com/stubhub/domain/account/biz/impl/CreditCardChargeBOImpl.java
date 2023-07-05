package com.stubhub.domain.account.biz.impl;

import java.math.BigDecimal;
import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.stubhub.domain.account.biz.intf.CreditCardChargeBO;
import com.stubhub.domain.account.datamodel.dao.SellerCcTransDAO;
import com.stubhub.domain.account.datamodel.entity.SellerCcTrans;

@Component("creditCardChargeBO")
public class CreditCardChargeBOImpl implements CreditCardChargeBO {
	
	private final static Logger log = LoggerFactory.getLogger(CreditCardChargeBOImpl.class);
	
	@Autowired
	private SellerCcTransDAO sellerCcTransDAO;
	
	@Override
	@Transactional
	public List<SellerCcTrans> getCreditCardCharges(Set<Long> sellerCcIds, String sort, Calendar from, Calendar to, Integer startRow, Integer rowNumber, List<String> transTypeList, String currencyCode) {
		List<SellerCcTrans> sellerCcTransList = null;
		if(sellerCcIds != null && !sellerCcIds.isEmpty()) {
			sellerCcTransList = sellerCcTransDAO.getSellerCcTransBySellerCcId(sellerCcIds, from, to, startRow, rowNumber, transTypeList, currencyCode);
		}
		
		if(StringUtils.trimToNull(sort) != null && sellerCcTransList != null && !sellerCcTransList.isEmpty()) {
			if(sort.contains("CHARGEDDATE")) {
				Collections.sort(sellerCcTransList, new SellerCcTransChargedDateComparator());
				if(sort.contains("ASC")){				
					Collections.reverse(sellerCcTransList);
				}
			} else if(sort.contains("AMOUNT")) {
				Collections.sort(sellerCcTransList, new SellerCcTransAmountComparator());
				if(sort.contains("ASC")){				
					Collections.reverse(sellerCcTransList);
				}
			} else if(sort.contains("ORDERID")) {
				Collections.sort(sellerCcTransList, new SellerCcTransOrderIdComparator());
				if(sort.contains("ASC")){				
					Collections.reverse(sellerCcTransList);
				}
			}
		}
		
		return sellerCcTransList;
	}

    @Override
    @Transactional
    public long getCreditCardChargesCount(Set<Long> sellerCcIds, Calendar from, Calendar to, List<String> transTypeList, String currencyCode) {
        return sellerCcTransDAO.countSellerCcTransBySellerCcId(sellerCcIds, from, to, transTypeList, currencyCode);
    }

	@Override
	public Map<Long, BigDecimal> getChargeToSellerAmountByTid(List<Long> tids) {
		return sellerCcTransDAO.getChargeToSellerAmountByTid(tids);
	}
}
