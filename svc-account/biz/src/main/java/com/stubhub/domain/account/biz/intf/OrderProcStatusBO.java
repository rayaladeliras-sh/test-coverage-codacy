package com.stubhub.domain.account.biz.intf;

import java.util.List;

import com.stubhub.domain.account.datamodel.entity.OrderProcStatus;

public interface OrderProcStatusBO {

	public List<OrderProcStatus> getOrderStatus(String tid);
}
