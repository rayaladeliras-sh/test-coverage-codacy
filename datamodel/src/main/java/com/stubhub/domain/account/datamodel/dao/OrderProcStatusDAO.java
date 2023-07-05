package com.stubhub.domain.account.datamodel.dao;

import java.util.List;

import com.stubhub.domain.account.datamodel.entity.OrderProcStatusDO;

public interface OrderProcStatusDAO {

	public List<Object[]> findOrderStatusByTransId(Long tid);
	public Long persist(OrderProcStatusDO transientInstance);
}

