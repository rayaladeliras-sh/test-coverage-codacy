package com.stubhub.domain.account.biz.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.stubhub.domain.account.biz.intf.TransactionCountBO;
import com.stubhub.domain.account.datamodel.dao.TransactionCountDAO;
import com.stubhub.domain.account.datamodel.entity.BuysCount;
import com.stubhub.domain.account.datamodel.entity.ListingsCount;
import com.stubhub.domain.account.datamodel.entity.SalesCount;

@Component("transactionCountBO")
public class TransactionCountBOImpl implements TransactionCountBO{

	private static final Logger log = LoggerFactory.getLogger(TransactionCountBOImpl.class);

	@Autowired
	@Qualifier("transactionCountDAO")
	private TransactionCountDAO transactionCountDAO;

	@Override
    public List<ListingsCount> getListingsCount(String uid) {
		log.info("api_domain=account api_resource=listings api_method=getListingsCount status=success message=Get listing summary for userId=" + uid);
		return transactionCountDAO.findListingsCountByUserId(uid);
	}
	
	@Override
    public List<SalesCount> getSalesCount(String uid) {
		log.info("api_domain=account api_resource=sales api_method=getSalesCount status=success message=Get sales summary for userId=" + uid);
		return transactionCountDAO.findSalesCountByUserId(uid);
	}
	
	@Override
    public List<BuysCount> getBuysCount(String uid) {
		log.info("api_domain=account api_resource=orders api_method=getBuysCount status=success message=Get orders summary for userId=" + uid);
		return transactionCountDAO.findBuysCountByUserId(uid);
	}
	
}
