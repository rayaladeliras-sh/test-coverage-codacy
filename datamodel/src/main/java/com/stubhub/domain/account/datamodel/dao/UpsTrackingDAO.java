package com.stubhub.domain.account.datamodel.dao;

import java.util.List;

import com.stubhub.domain.account.datamodel.entity.UpsTracking;

public interface UpsTrackingDAO {
	
	public List<UpsTracking> checkUPSOrder(Long tid);
}
