package com.stubhub.domain.account.datamodel.dao;

import java.util.Calendar;
import java.util.List;

import com.stubhub.common.exception.RecordNotFoundForIdException;
import com.stubhub.domain.account.datamodel.entity.SalesTrans;

public interface SalesTransDAO {
	
	public SalesTrans getByTId(Long saleId) throws RecordNotFoundForIdException;
	public List<SalesTrans> getByBuyerId(Long buyerId,Integer startRow, Integer rowNumber);
	public List<SalesTrans> getByEventDate(Calendar eventStartDate, Calendar eventEndDate, Integer startRow, Integer rowNumber);
	public List<SalesTrans> getByBuyerIDAndEventDate(Long buyerId,Calendar eventStartDate, Calendar eventEndDate, Integer startRow, Integer rowNumber);


}
