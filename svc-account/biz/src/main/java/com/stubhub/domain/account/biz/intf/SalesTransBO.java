package com.stubhub.domain.account.biz.intf;

import java.util.List;

import com.stubhub.common.exception.RecordNotFoundForIdException;
import com.stubhub.domain.account.datamodel.entity.SalesTrans;
import java.util.Calendar;



public interface SalesTransBO {
	
	public SalesTrans getSaleTransById(Long saleId) throws RecordNotFoundForIdException;
	public List<SalesTrans> getSaleTransByBuyerId(Long buyerId,Integer startRow, Integer rowNumber);
	public List<SalesTrans> getSaleTransByEventDate(Calendar eventStartDate, Calendar eventEndDate, Integer startRow, Integer rowNumber);
	public List<SalesTrans> getSaleTransByBuyerIDAndEventDate(Long buyerId,Calendar eventStartDate, Calendar eventEndDate, Integer startRow, Integer rowNumber);

}
