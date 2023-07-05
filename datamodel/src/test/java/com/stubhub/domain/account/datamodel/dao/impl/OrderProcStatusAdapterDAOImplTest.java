package com.stubhub.domain.account.datamodel.dao.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.stubhub.domain.account.datamodel.dao.OrderProcStatusDAO;
import com.stubhub.domain.account.datamodel.entity.OrderProcStatusDO;

public class OrderProcStatusAdapterDAOImplTest {

	private OrderProcStatusAdapterDAOImpl orderProcStatusAdapterDAO;
	private OrderProcStatusDAO orderProcStatusDAO;
	private Long l1 = 200L;
	private Long l2 = 404L;
	
	@BeforeTest
	public void setUp() {
		orderProcStatusAdapterDAO = new OrderProcStatusAdapterDAOImpl();
		orderProcStatusDAO = Mockito.mock(OrderProcStatusDAOImpl.class);
		ReflectionTestUtils.setField(orderProcStatusAdapterDAO, "orderProcStatusDAO", orderProcStatusDAO);
	}
	
	@Test
	public void testFindOrderStatusByTransId(){
		List<Object[]> list = new ArrayList<Object[]>();
		Object[] obj = new Object[4];
		OrderProcStatusDO orderProcStatusDO = new OrderProcStatusDO();

		orderProcStatusDO.setOrderProcSubStatusCode(5L);
		orderProcStatusDO.setOrderProcStatusEffDate(Calendar.getInstance());
		obj[0]= orderProcStatusDO;
		obj[1]= "123";
		obj[2]= "Delivered";
		obj[3]= "Delivered";
		list.add(obj);
		
		Mockito.when(orderProcStatusDAO.findOrderStatusByTransId(l1)).thenReturn(list);
		Assert.assertEquals(orderProcStatusAdapterDAO.findOrderStatusByTransId(l1).size(), 1);
		
		Mockito.when(orderProcStatusAdapterDAO.findOrderStatusByTransId(l2)).thenReturn(null);
		Assert.assertNull(orderProcStatusAdapterDAO.findOrderStatusByTransId(l2));		
	}
	
	@Test
	public void testUpdateOrderStatusByTransId(){
		List<Object[]> list = new ArrayList<Object[]>();
		Object[] obj = new Object[4];
		OrderProcStatusDO orderProcStatusDO = new OrderProcStatusDO();
		Long orderId = 1L;
		orderProcStatusDO.setOrderProcSubStatusCode(5L);
		orderProcStatusDO.setOrderProcStatusEffDate(Calendar.getInstance());
		orderProcStatusDO.setOrderProcStatusId(l1);
		obj[0]= orderProcStatusDO;
		obj[1]= "123";
		obj[2]= "Delivered";
		obj[3]= "Delivered";
		list.add(obj);
		
		OrderProcStatusDO orderProcStatusDOUpdated = new OrderProcStatusDO();
		orderProcStatusDOUpdated.setOrderProcSubStatusCode(7L);
		orderProcStatusDOUpdated.setOrderProcStatusEffDate(Calendar.getInstance());
		orderProcStatusDOUpdated.setOrderProcStatusId(l1);
		Mockito.when(orderProcStatusDAO.findOrderStatusByTransId(orderId)).thenReturn(list);
		Mockito.when(orderProcStatusDAO.persist(orderProcStatusDOUpdated)).thenReturn(l1);
		Assert.assertNull(orderProcStatusAdapterDAO.updateOrderStatusByTransId(orderId, "bijian", 5L));
		Mockito.when(orderProcStatusDAO.findOrderStatusByTransId(orderId)).thenReturn(null);
		Assert.assertNull(orderProcStatusAdapterDAO.updateOrderStatusByTransId(orderId, "bijian", 5L));
		List<Object[]> objLst = new ArrayList<Object[]>();
		Mockito.when(orderProcStatusDAO.findOrderStatusByTransId(orderId)).thenReturn(objLst);
		Assert.assertNull(orderProcStatusAdapterDAO.updateOrderStatusByTransId(orderId, "bijian", 5L));

	}
}
