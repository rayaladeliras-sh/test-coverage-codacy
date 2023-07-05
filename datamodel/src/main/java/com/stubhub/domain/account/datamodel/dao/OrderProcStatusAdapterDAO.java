package com.stubhub.domain.account.datamodel.dao;

import java.util.List;

import com.stubhub.domain.account.datamodel.entity.OrderProcStatus;
import com.stubhub.domain.account.datamodel.entity.StubTrans;

public interface OrderProcStatusAdapterDAO {

	public List<OrderProcStatus> findOrderStatusByTransId(Long tid);
	public Long updateOrderStatusByTransId(Long tid, String operatorId, Long newOrderProcSubStatusCode);
}

