package com.stubhub.domain.account.biz.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.stubhub.domain.account.biz.intf.OrderProcStatusBO;
import com.stubhub.domain.account.datamodel.dao.OrderProcStatusAdapterDAO;
import com.stubhub.domain.account.datamodel.entity.OrderProcStatus;

@Component("orderProcStatusBO")
public class OrderProcStatusBOImpl implements OrderProcStatusBO{

	private static final Logger log = LoggerFactory.getLogger(OrderProcStatusBOImpl.class);

	@Autowired
	@Qualifier("orderProcStatusAdapterDAO")
	private OrderProcStatusAdapterDAO orderProcStatusAdapterDAO;

	@Override
    public List<OrderProcStatus> getOrderStatus(String tid) {
		log.info("getting order statuses for orderId=" + tid);
		return orderProcStatusAdapterDAO.findOrderStatusByTransId(Long.valueOf(tid));
	}
}

