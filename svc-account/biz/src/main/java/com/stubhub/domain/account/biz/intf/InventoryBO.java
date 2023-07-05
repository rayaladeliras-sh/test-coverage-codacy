package com.stubhub.domain.account.biz.intf;

import com.stubhub.common.exception.RecordNotFoundForIdException;
import com.stubhub.domain.account.datamodel.entity.InventoryData;

public interface InventoryBO {
	
	public InventoryData getInventoryDataById(Long orderId) throws RecordNotFoundForIdException;
	public String getFulFillmentType(Long methodId) throws RecordNotFoundForIdException;

}
