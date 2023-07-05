package com.stubhub.domain.account.biz.intf;

import com.stubhub.domain.account.common.PaymentsSearchCriteria;
import com.stubhub.domain.account.datamodel.entity.SellerPayment;
import com.stubhub.domain.account.datamodel.entity.SellerPaymentStatusEnum;
import com.stubhub.domain.account.datamodel.entity.SellerPayments;
import com.stubhub.domain.account.intf.UpdatePaymentStatusRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;

import java.util.Calendar;
import java.util.List;

public interface SellerPaymentBO {
	List<SellerPayments> getSellerPayments(long userId);

	SellerPayment getSellerPaymentById(Long sellerPaymentId);

	List<SellerPayment> findSellerPaymentsByAction(Long sellerId, UpdatePaymentStatusRequest.Action action, Calendar latestPaymentDate);
	@Deprecated
	List<SellerPayment> findSellerPaymentsByAction(Long sellerId, UpdatePaymentStatusRequest.Action action);

	void updateSellerPaymentStatus(SellerPayment sellerPayment, SellerPaymentStatusEnum status);
	
	void saveSellerPayment(SellerPayment sellerPayment);
}