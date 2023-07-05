package com.stubhub.domain.account.biz.impl;

import java.util.ArrayList;
import java.util.List;

import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.stubhub.domain.account.biz.intf.TransactionCountBO;
import com.stubhub.domain.account.datamodel.dao.TransactionCountDAO;
import com.stubhub.domain.account.datamodel.dao.impl.TransactionCountDAOImpl;
import com.stubhub.domain.account.datamodel.entity.BuysCount;
import com.stubhub.domain.account.datamodel.entity.ListingsCount;
import com.stubhub.domain.account.datamodel.entity.SalesCount;

public class TransactionCountBOImplTest {

	private TransactionCountBO transactionCountBO;
	private TransactionCountDAO transactionCountDAO;
	private Long count = 1L;
	private Long userId = 123L;

	@BeforeTest
	public void setUp() {
		transactionCountBO = new TransactionCountBOImpl();
		transactionCountDAO = Mockito.mock(TransactionCountDAOImpl.class);
		ReflectionTestUtils.setField(transactionCountBO, "transactionCountDAO", transactionCountDAO);
	}

	@Test
	public void testGetListingsCount(){
		Mockito.when(transactionCountDAO.findListingsCountByUserId(userId.toString())).thenReturn(getListings());
		Assert.assertNotNull(transactionCountBO.getListingsCount(userId.toString()));
	}
	
	@Test
	public void testGetSalesCount(){
		Mockito.when(transactionCountDAO.findSalesCountByUserId(userId.toString())).thenReturn(getSales());
		Assert.assertNotNull(transactionCountBO.getSalesCount(userId.toString()));
	}
	
	@Test
	public void testGetBuysCount(){
		Mockito.when(transactionCountDAO.findBuysCountByUserId(userId.toString())).thenReturn(getOrders());
		Assert.assertNotNull(transactionCountBO.getBuysCount(userId.toString()));
	}

	public List<ListingsCount> getListings(){
		ListingsCount listingsCount = new ListingsCount();
		listingsCount.setActiveCount(count);
		listingsCount.setInactiveCount(count);
		listingsCount.setIncompleteCount(count);
		listingsCount.setPendingLmsApprovalCount(count);
		listingsCount.setPendingLockCount(count);
		listingsCount.setUserId(userId);
		List<ListingsCount> list = new ArrayList<ListingsCount>();
		list.add(listingsCount);
		return list;
	}
	
	public List<SalesCount> getSales(){
		SalesCount salesCount = new SalesCount();
		salesCount.setCancelledCount(count);
		salesCount.setCompleted180DaysCount(count);
		salesCount.setCompleted30DaysCount(count);
		salesCount.setUnconfirmedCount(count);
		salesCount.setUserId(userId);
		List<SalesCount> list = new ArrayList<SalesCount>();
		list.add(salesCount);
		return list;
	}
	
	public List<BuysCount> getOrders(){
		BuysCount buysCount = new BuysCount();
		buysCount.setCancelled(count);
		buysCount.setCompleted180Days(count);
		buysCount.setCompleted30Days(count);
		buysCount.setUserId(userId);
		List<BuysCount> list = new ArrayList<BuysCount>();
		list.add(buysCount);
		return list;
	}
	
}
