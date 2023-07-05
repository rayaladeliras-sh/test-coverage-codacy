package com.stubhub.domain.account.datamodel.dao;

import com.stubhub.common.exception.RecordNotFoundForIdException;
import com.stubhub.domain.account.datamodel.entity.InventoryData;
import com.stubhub.domain.account.datamodel.entity.TTAPIintegration;

public interface InventoryDAO {
	
	public InventoryData getInventoryDataById(Long orderId ) throws RecordNotFoundForIdException;

	public String getFulFillmentType(Long methodId) throws RecordNotFoundForIdException;

	TTAPIintegration getTTProperties() throws RecordNotFoundForIdException;

}
