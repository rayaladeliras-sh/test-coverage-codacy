package com.stubhub.domain.account.datamodel.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.stubhub.domain.account.datamodel.entity.SellerPayment;
import com.stubhub.domain.account.datamodel.entity.SellerPaymentStatusEnum;
import com.stubhub.domain.account.datamodel.entity.SellerPayments;


public interface SellerPaymentsDAO {
	List<SellerPayments> getSellerPayments(Long sellerId, String recordType);

	List<SellerPayments> getSellerPaymentsBySellerId(Long sellerId, String recordType, String sort, Calendar createdFromDate, Calendar createdToDate, Integer startRow, Integer rowNumber, String currencyCode);

	long countSellerPaymentsBySellerId(Long sellerId, String recordType, Calendar createdFromDate, Calendar createdToDate, String currencyCode);

	SellerPayment getSellerPaymentById(Long sellerPaymentId);

	@Deprecated
	List<SellerPayment> findSellerPaymentsByStatus(Long sellerId, SellerPaymentStatusEnum status);

	List<SellerPayment> findSellerPaymentsByStatus(Long sellerId, SellerPaymentStatusEnum status, Calendar latestPaymentDate);

	SellerPayment getSellerPaymentByRefNumber(String refNumber);

	List<SellerPayment> getSellerPaymentsByIds(List<Long> ids);

	void saveSellerPayment(SellerPayment sellerPayment);

	int updateSellerPaymentStatus(SellerPayment sellerPayment);
	
	List<SellerPayments> getSellerPaymentsIndy(Calendar fromDate, Calendar toDate);
}