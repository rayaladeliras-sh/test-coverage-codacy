package com.stubhub.domain.account.biz.impl;

import java.util.ArrayList;
import java.util.List;

import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.stubhub.domain.account.biz.intf.OrderProcStatusBO;
import com.stubhub.domain.account.datamodel.dao.OrderProcStatusAdapterDAO;
import com.stubhub.domain.account.datamodel.dao.impl.OrderProcStatusAdapterDAOImpl;
import com.stubhub.domain.account.datamodel.entity.OrderProcStatus;

public class OrderProcStatusBOImplTest {

	private OrderProcStatusBO orderProcStatusBO;
	private OrderProcStatusAdapterDAO orderProcStatusAdapterDAO;
	private Long l1 = 200L;

	@BeforeTest
	public void setUp() {
		orderProcStatusBO = new OrderProcStatusBOImpl();
		orderProcStatusAdapterDAO = Mockito.mock(OrderProcStatusAdapterDAOImpl.class);
		ReflectionTestUtils.setField(orderProcStatusBO, "orderProcStatusAdapterDAO", orderProcStatusAdapterDAO);
	}

	@Test
	public void testGetOrderStatus(){
		List<OrderProcStatus> list = new ArrayList<OrderProcStatus>();
		OrderProcStatus orderStatus = new OrderProcStatus();
		orderStatus.setStatusCode(2000L);
		orderStatus.setStatusDescription("Confirmed");
		orderStatus.setSubStatusCode(5L);
		orderStatus.setSubStatusDescription("confirmed: CSR");
		orderStatus.setStatusEffectiveDate("2012-03-05T20:03:12+0000");
		list.add(orderStatus);

		Mockito.when(orderProcStatusAdapterDAO.findOrderStatusByTransId(l1)).thenReturn(list);
		Assert.assertNotNull(orderProcStatusBO.getOrderStatus(l1.toString()));
	}
}
