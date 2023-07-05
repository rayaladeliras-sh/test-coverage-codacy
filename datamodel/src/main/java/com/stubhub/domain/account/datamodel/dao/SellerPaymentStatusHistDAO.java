package com.stubhub.domain.account.datamodel.dao;

import com.stubhub.domain.account.datamodel.entity.SellerPaymentStatusHist;

public interface SellerPaymentStatusHistDAO {
	
	public int updateEndDate(Long sellerPaymentId);
	
	public void saveSellerPaymentStatusHist(SellerPaymentStatusHist sellerPaymentStatusHist);
}
