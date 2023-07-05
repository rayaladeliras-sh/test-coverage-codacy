package com.stubhub.domain.account.datamodel.dao;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public interface TransactionSummaryDAO {
	
	public List<HashMap<String,String>>  getSummaryDetails(long proxiedId, String currencyCode, boolean fullSummaryDetails);
	

}
