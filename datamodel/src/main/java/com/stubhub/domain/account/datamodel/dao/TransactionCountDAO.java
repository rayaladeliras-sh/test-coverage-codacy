package com.stubhub.domain.account.datamodel.dao;

import java.util.List;

import com.stubhub.domain.account.datamodel.entity.BuysCount;
import com.stubhub.domain.account.datamodel.entity.ListingsCount;
import com.stubhub.domain.account.datamodel.entity.SalesCount;

public interface TransactionCountDAO {

	public List<ListingsCount> findListingsCountByUserId(String uid);
	public List<SalesCount> findSalesCountByUserId(String uid);
	public List<BuysCount> findBuysCountByUserId(String uid);
}
