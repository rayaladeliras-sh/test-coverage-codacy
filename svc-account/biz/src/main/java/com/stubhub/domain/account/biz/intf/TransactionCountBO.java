package com.stubhub.domain.account.biz.intf;

import java.util.List;

import com.stubhub.domain.account.datamodel.entity.BuysCount;
import com.stubhub.domain.account.datamodel.entity.ListingsCount;
import com.stubhub.domain.account.datamodel.entity.SalesCount;

public interface TransactionCountBO {

	public List<ListingsCount> getListingsCount(String uid);
	public List<SalesCount> getSalesCount(String uid);
	public List<BuysCount> getBuysCount(String uid);
	
}
