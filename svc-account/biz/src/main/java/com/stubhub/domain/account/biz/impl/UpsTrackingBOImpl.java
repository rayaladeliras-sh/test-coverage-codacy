package com.stubhub.domain.account.biz.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.stubhub.domain.account.biz.intf.UpsTrackingBO;
import com.stubhub.domain.account.datamodel.dao.UpsTrackingDAO;
import com.stubhub.domain.account.datamodel.entity.UpsTracking;

@Component("upsTrackingBO")
public class UpsTrackingBOImpl implements UpsTrackingBO{

	private static final Logger log = LoggerFactory.getLogger(UpsTrackingBOImpl.class);

	@Autowired
	@Qualifier("upsTrackingDAO")
	private UpsTrackingDAO upsTrackingDAO;

	@Override
    public List<UpsTracking> checkUPSOrder(Long tid) {
		log.info("api_domain=account api_resource=orders api_method=checkUPSOrder status=success message=Checking UPS status for the orderId=" + tid);
		return upsTrackingDAO.checkUPSOrder(tid);
	}

}


