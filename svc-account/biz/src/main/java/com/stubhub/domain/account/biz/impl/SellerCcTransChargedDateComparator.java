package com.stubhub.domain.account.biz.impl;

import java.util.Comparator;

import com.stubhub.domain.account.datamodel.entity.SellerCcTrans;

public class SellerCcTransChargedDateComparator implements Comparator<SellerCcTrans> {

	@Override	
	public int compare(SellerCcTrans o1, SellerCcTrans o2) {
		if(o1.getLastUpdatedDate() != null && o2.getLastUpdatedDate() != null){
			if(o1.getLastUpdatedDate().before(o2.getLastUpdatedDate())) {
				return 1;
			} else if(o1.getLastUpdatedDate().after(o2.getLastUpdatedDate())) {
				return -1;
			}
		}				
		return 0;
	}
}
