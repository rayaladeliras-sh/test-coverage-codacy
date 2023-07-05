package com.stubhub.domain.account.biz.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.stubhub.common.exception.RecordNotFoundForIdException;
import com.stubhub.domain.account.biz.intf.InventoryBO;
import com.stubhub.domain.account.datamodel.dao.InventoryDAO;
import com.stubhub.domain.account.datamodel.entity.InventoryData;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("inventoryBO")
public class InventoryBOImpl implements InventoryBO {
	
	 @Autowired
	 @Qualifier("inventoryDAO")
	 private InventoryDAO inventoryDAO;

	@Override
	public InventoryData getInventoryDataById(Long orderId) throws RecordNotFoundForIdException {
		// TODO Auto-generated method stub
		return inventoryDAO.getInventoryDataById(orderId);
	}

	@Override
	public String getFulFillmentType(Long methodId)
			throws RecordNotFoundForIdException {
		// TODO Auto-generated method stub
		return inventoryDAO.getFulFillmentType(methodId);
	}

}
