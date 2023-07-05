package com.stubhub.domain.account.biz.impl;

import java.util.Comparator;

import com.stubhub.domain.account.datamodel.entity.SellerCcTrans;

public class SellerCcTransAmountComparator implements Comparator<SellerCcTrans> {

	@Override	
	public int compare(SellerCcTrans o1, SellerCcTrans o2) {
		if(o1.getAmount() != null && o2.getAmount() != null){
			if(o1.getAmount() < o2.getAmount()){
				return 1;
			} else if(o1.getAmount() > o2.getAmount()) {
				return -1;
			}
		}				
		return 0;
	}
}
