package com.stubhub.domain.account.biz.impl;

import java.util.Comparator;

import com.stubhub.domain.account.datamodel.entity.SellerCcTrans;

public class SellerCcTransOrderIdComparator implements Comparator<SellerCcTrans> {

	@Override	
	public int compare(SellerCcTrans o1, SellerCcTrans o2) {
		if(o1.getTid() != null && o2.getTid() != null){
			if(o1.getTid() < o2.getTid()){
				return 1;
			} else if(o1.getTid() > o2.getTid()) {
				return -1;
			}
		}				
		return 0;
	}
}
