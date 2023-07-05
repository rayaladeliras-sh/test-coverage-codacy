package com.stubhub.domain.account.biz.impl;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.stubhub.domain.account.biz.intf.UsedDiscountBO;
import com.stubhub.domain.account.datamodel.dao.UsedDiscountDAO;
import com.stubhub.domain.account.datamodel.entity.UsedDiscount;
import com.stubhub.newplatform.persistence.adapter.UTCCalendarToTimestampAdapter;

@Component("usedDiscountBO")
public class UsedDiscountBOImpl implements UsedDiscountBO{

	private static final Logger log = LoggerFactory.getLogger(UsedDiscountBOImpl.class);

	@Autowired
	@Qualifier("usedDiscountDAO")
	private UsedDiscountDAO usedDiscountDAO;

	@Override
    public List<List> getDiscounts(String tid) {
		log.info("api_domain=account api_resource=orders api_method=getDiscounts status=success message=Get discounts for the orderId=" + tid);
		return usedDiscountDAO.findDetailByTid(Long.valueOf(tid));
	}
	
	@Override
    public List<UsedDiscount> copyDiscountsforSubsOrder(Long oldOrderId, Long newOrderId, String operatorId){
		log.info("api_domain=account api_resource=orders api_method=copyDiscountsforSubsOrder message=Copying discounts from old orderId=" + oldOrderId + " to new orderId=" + newOrderId);
		List<UsedDiscount> usedDiscountList = usedDiscountDAO.findByTid(oldOrderId);				
		List<UsedDiscount> discountLst = null;
		if (usedDiscountList != null && usedDiscountList.size() > 0){
			discountLst = new ArrayList<UsedDiscount>();
			GregorianCalendar calender = UTCCalendarToTimestampAdapter.getNewUTCCalendarInstanceStatic();
			for (UsedDiscount usedDiscount : usedDiscountList) {			
				// copy used discount from existing order and associate to new order
				UsedDiscount newUsedDiscount = new UsedDiscount();
				newUsedDiscount.setDiscountId(usedDiscount.getDiscountId());
				newUsedDiscount.setTid(newOrderId);
				newUsedDiscount.setAmountUsed(usedDiscount.getAmountUsed());
				newUsedDiscount.setDateAdded(calender);
				newUsedDiscount.setCreatedBy(operatorId);
				newUsedDiscount.setLastUpdateBy(operatorId);
				newUsedDiscount.setLastUpdatedDate(calender);
				newUsedDiscount.setCurrencyCode(usedDiscount.getCurrencyCode());					
				discountLst.add(newUsedDiscount);			

				// inactive used discount which is associated to the old order 
				usedDiscount.setActive(0);
				usedDiscount.setLastUpdateBy(operatorId);
				usedDiscount.setLastUpdatedDate(calender);
				discountLst.add(usedDiscount);
			}
			usedDiscountDAO.persist(discountLst);
		}
		return discountLst;
	}
}


