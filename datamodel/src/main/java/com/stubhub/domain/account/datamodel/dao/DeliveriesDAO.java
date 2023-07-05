package com.stubhub.domain.account.datamodel.dao;

import java.util.List;

import com.stubhub.domain.account.datamodel.entity.Deliveries;

public interface DeliveriesDAO {
	
	public List<Deliveries> getByTid(Long tid);
}
