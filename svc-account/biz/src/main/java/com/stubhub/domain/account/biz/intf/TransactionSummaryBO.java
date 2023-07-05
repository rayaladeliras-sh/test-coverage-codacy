package com.stubhub.domain.account.biz.intf;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public interface TransactionSummaryBO {
	
	public List<HashMap<String,String>> getUserTransactionSummary(Long userId, String currencyCode, boolean fullSummaryDetails); 

}
