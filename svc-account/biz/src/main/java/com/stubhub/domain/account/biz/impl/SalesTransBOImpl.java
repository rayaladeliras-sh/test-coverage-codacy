package com.stubhub.domain.account.biz.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.stubhub.common.exception.RecordNotFoundForIdException;
import com.stubhub.domain.account.biz.intf.SalesTransBO;
import com.stubhub.domain.account.datamodel.dao.SalesTransDAO;
import com.stubhub.domain.account.datamodel.entity.SalesTrans;

@Component("saleTransBO")
public class SalesTransBOImpl implements SalesTransBO {
	
	private final static Logger log = LoggerFactory.getLogger(SalesTransBOImpl.class);

	@Autowired
	@Qualifier("salesTransDAO")
	private SalesTransDAO salesTransDAO;

	@Override
	public SalesTrans getSaleTransById(Long saleId) throws RecordNotFoundForIdException {
		log.info("api_domain=account api_resource=sales api_method=getSaleTransById message=getting sales from saleId=" + saleId);
		return salesTransDAO.getByTId(saleId);
	}

	@Override
	public List<SalesTrans> getSaleTransByBuyerId(Long buyerId, Integer startRow, Integer rowNumber) {
		log.info("api_domain=account api_resource=sales api_method=getSaleTransByBuyerId message=getting sales from buyerId=" + buyerId);
		List<SalesTrans> saleTransLst = new ArrayList<SalesTrans>();
		saleTransLst = salesTransDAO.getByBuyerId(buyerId, startRow, rowNumber);
		return saleTransLst;
	}

	@Override
	public List<SalesTrans> getSaleTransByEventDate(Calendar eventStartDate, Calendar eventEndDate, Integer startRow,
			Integer rowNumber) {
		List<SalesTrans> saleTransLst = new ArrayList<SalesTrans>();
		saleTransLst = salesTransDAO.getByEventDate(eventStartDate, eventEndDate, startRow, rowNumber);
		return saleTransLst;
	}

	@Override
	public List<SalesTrans> getSaleTransByBuyerIDAndEventDate(Long buyerId, Calendar eventStartDate, Calendar eventEndDate,
			Integer startRow, Integer rowNumber) {
		List<SalesTrans> saleTransLst = new ArrayList<SalesTrans>();
		saleTransLst = salesTransDAO.getByBuyerIDAndEventDate(buyerId, eventStartDate, eventEndDate, startRow, rowNumber);
		return saleTransLst;
	}

	

}
