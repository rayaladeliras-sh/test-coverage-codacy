package com.stubhub.domain.account.biz.impl;

import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.stubhub.common.exception.RecordNotFoundForIdException;
import com.stubhub.domain.account.biz.intf.InventoryBO;
import com.stubhub.domain.account.datamodel.dao.InventoryDAO;
import com.stubhub.domain.account.datamodel.dao.impl.InventoryDAOImpl;
import com.stubhub.domain.account.datamodel.entity.InventoryData;

public class InventoryBOTest {

	
	private InventoryDAO inventoryDAO;
	private InventoryBO inventoryBO;
	private Long val = 1l;

	@BeforeTest
	public void setUp() {
	
		inventoryDAO = Mockito.mock(InventoryDAOImpl.class);
		inventoryBO = new InventoryBOImpl();
		ReflectionTestUtils.setField(inventoryBO, "inventoryDAO", inventoryDAO);
	}
	@Test
	public void testGetInventoryDataById() throws RecordNotFoundForIdException{

		
		InventoryData invData =  mockInventoryData();
		Mockito.when(inventoryDAO.getInventoryDataById(Matchers.anyLong())).thenReturn(invData);
		Assert.assertNotNull(inventoryBO.getInventoryDataById(Matchers.anyLong()));
	}
	
	@Test
	public void testGetFulFillmentType() throws RecordNotFoundForIdException{
       String FulfillmentType = "UPS";
		
		Mockito.when(inventoryDAO.getFulFillmentType(Matchers.anyLong())).thenReturn(FulfillmentType);
		Assert.assertNotNull(inventoryBO.getFulFillmentType(Matchers.anyLong()));
	}
	
	public InventoryData mockInventoryData()
	{
		
		InventoryData inv =  new InventoryData();
		
		inv.setListingId(1L);
		inv.setTicTecListingId("1234");
		inv.setSellerId(10133681L);
		inv.setBrokerId(val);
		inv.setOrderId(val);
	//	inv.setFulfillmentType("UPS");
		
		return inv;
	}
	
	
}