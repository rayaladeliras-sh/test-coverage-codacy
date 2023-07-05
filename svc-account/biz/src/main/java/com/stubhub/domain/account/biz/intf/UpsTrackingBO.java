package com.stubhub.domain.account.biz.intf;

import java.util.List;

import com.stubhub.domain.account.datamodel.entity.UpsTracking;

public interface UpsTrackingBO {

	public List<UpsTracking> checkUPSOrder(Long tid);
}
