package com.stubhub.domain.account.datamodel.dao;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.stubhub.domain.account.datamodel.entity.SellerCcTrans;

public interface SellerCcTransDAO {

	public List<SellerCcTrans> getSellerCcTransBySellerCcId(Set<Long> sellerCcIds, Calendar fromDate, Calendar toDate, Integer startRow, Integer rowNumber, List<String> transTypeList, String currencyCode);

	public long countSellerCcTransBySellerCcId(Set<Long> sellerCcIds, Calendar fromDate, Calendar toDate, List<String> transTypeList, String currencyCode);

	Map<Long, BigDecimal> getChargeToSellerAmountByTid(List<Long> tids);
}
