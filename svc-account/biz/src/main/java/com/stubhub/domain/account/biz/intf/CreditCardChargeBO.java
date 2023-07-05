package com.stubhub.domain.account.biz.intf;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.stubhub.domain.account.datamodel.entity.SellerCcTrans;

public interface CreditCardChargeBO {

	public List<SellerCcTrans> getCreditCardCharges(Set<Long> sellerCcIds, String sort, Calendar fromDate, Calendar toDate, Integer startRow, Integer rowNumber, List<String> transTypeList, String currencyCode);

	public long getCreditCardChargesCount(Set<Long> sellerCcIds, Calendar from, Calendar to, List<String> transTypeList, String currencyCode);

	/*
	 * @param tids,
	 * 			A list of SELLER_CC_TRANS.TID
	 *
	 * @return HashMap(TID, AMOUNT)
	 * 			TID : SELLER_CC_TRANS.TID
	 * 			AMOUNT:the amount charged to seller, sum(amount) with trans_type D - sum(amount) with trans_type C
	 */
	Map<Long, BigDecimal> getChargeToSellerAmountByTid(List<Long> tids);
}
