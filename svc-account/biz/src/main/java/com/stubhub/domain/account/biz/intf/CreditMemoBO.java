package com.stubhub.domain.account.biz.intf;


import com.stubhub.domain.account.intf.CreditMemosResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface CreditMemoBO {
	public CreditMemosResponse getSellerCreditMemos(Long sellerId, String sort, String createdFromDate, String createdToDate, Integer start, Integer rows, String currencyCode);
	public CreditMemosResponse getSellersCreditMemos(String createdFromDate, String createdToDate);
	/*
	 * @param pids,
	 * 			A list of CM_PAYMENT_XREF.APPLIED_PID
	 *
	 * @return HashMap(
	 * 			APPLIED_PID,
	 * 			sum(APPLIED_AMOUNT) with cancelled=0 group by APPLIED_PID
	 * 			)
	 */
	Map<Long,BigDecimal> getAppliedAmountByPID(List<Long> pids);
}
