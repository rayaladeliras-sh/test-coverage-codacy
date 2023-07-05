package com.stubhub.domain.account.biz.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.stubhub.domain.account.datamodel.dao.AppliedCreditMemoDAO;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.stubhub.domain.account.biz.intf.SellerPaymentBO;
import com.stubhub.domain.account.common.PaymentsSearchCriteria;
import com.stubhub.domain.account.common.enums.SellerPaymentType;
import com.stubhub.domain.account.datamodel.dao.SellerPaymentStatusHistDAO;
import com.stubhub.domain.account.datamodel.dao.SellerPaymentsDAO;
import com.stubhub.domain.account.datamodel.entity.SellerPayment;
import com.stubhub.domain.account.datamodel.entity.SellerPaymentStatusEnum;
import com.stubhub.domain.account.datamodel.entity.SellerPaymentStatusHist;
import com.stubhub.domain.account.datamodel.entity.SellerPayments;
import com.stubhub.domain.account.intf.UpdatePaymentStatusRequest;

@Component("sellerPaymentBO")
public class SellerPaymentBOImpl implements SellerPaymentBO {

    private final static Logger log = LoggerFactory
			.getLogger(SellerPaymentBOImpl.class);
	@Autowired
	private SellerPaymentsDAO sellerPaymentsDAO;

	@Autowired
	private SellerPaymentStatusHistDAO sellerPaymentStatusHistDAO;

	@Autowired
	private PaymentsSolrUtil paymentsSolrUtil;

	@Override
	public List<SellerPayments> getSellerPayments(long userId) {
		return sellerPaymentsDAO.getSellerPayments(userId, "Payment Voucher");
	}

	@Override
	public SellerPayment getSellerPaymentById(Long sellerPaymentId) {
		return sellerPaymentsDAO.getSellerPaymentById(sellerPaymentId);
	}

	@Override
	@Deprecated
	public List<SellerPayment> findSellerPaymentsByAction(Long sellerId, UpdatePaymentStatusRequest.Action action) {
		log.info(String.format("findSellerPaymentsByAction, sellerId=%s",sellerId));
		if (action == UpdatePaymentStatusRequest.Action.RELEASE_PAYMENT_FOR_DUE_DILIGENCE) {
            if (log.isInfoEnabled()) {
                log.info("Found payments for releasing");
            }
			return sellerPaymentsDAO.findSellerPaymentsByStatus(sellerId, SellerPaymentStatusEnum.HOLD_PAYMENT_DUE_TO_DD);
		} else {
            if (log.isInfoEnabled()) {
                log.info("Found payments for holding");
            }
			return sellerPaymentsDAO.findSellerPaymentsByStatus(sellerId, SellerPaymentStatusEnum.HOLD_PAYMENT_FOR_MANUAL_DISBURSEMENT);
		}
	}

	@Override
	public List<SellerPayment> findSellerPaymentsByAction(Long sellerId, UpdatePaymentStatusRequest.Action action, Calendar latestPaymentDate) {
		log.info(String.format("findSellerPaymentsByAction with date, sellerId=%s",sellerId));
		if (action == UpdatePaymentStatusRequest.Action.RELEASE_PAYMENT_FOR_DUE_DILIGENCE) {
			if (log.isInfoEnabled()) {
				log.info("Found payments for releasing");
			}
			return sellerPaymentsDAO.findSellerPaymentsByStatus(sellerId, SellerPaymentStatusEnum.HOLD_PAYMENT_DUE_TO_DD, latestPaymentDate);
		} else {
			if (log.isInfoEnabled()) {
				log.info("Found payments for holding");
			}
			return sellerPaymentsDAO.findSellerPaymentsByStatus(sellerId, SellerPaymentStatusEnum.HOLD_PAYMENT_FOR_MANUAL_DISBURSEMENT, latestPaymentDate);
		}
	}

	@Override
	public void updateSellerPaymentStatus(SellerPayment sellerPayment,
			SellerPaymentStatusEnum status) {
		String updateBy = "domain-account Payments Shape API";
		sellerPayment.setSellerPaymentStatusId(status.getId());
		sellerPayment.setStatus(status.getName());
		sellerPayment.setLastUpdatedDate(Calendar.getInstance());
		sellerPayment.setLastUpdatedBy(updateBy);
		//sellerPaymentsDAO.saveSellerPayment(sellerPayment);
		if (sellerPaymentsDAO.updateSellerPaymentStatus(sellerPayment) > 0){
			sellerPaymentStatusHistDAO.updateEndDate(sellerPayment.getId());

			Calendar now = Calendar.getInstance();
			SellerPaymentStatusHist hist = new SellerPaymentStatusHist();
			hist.setCreatedBy(updateBy);
			hist.setCreatedDate(now);
			hist.setLastUpdated(now);
			hist.setLastUpdatedBy(updateBy);
			hist.setSellerPaymentDetailStatus(status.getName());
			hist.setSellerPaymentId(sellerPayment.getId());
			hist.setSellerPaymentStatusEffDate(now);
			hist.setAcknowledgementInd(0L);// non acknowledgement by default
			hist.setSellerPaymentStatusId(status.getId());
			sellerPaymentStatusHistDAO.saveSellerPaymentStatusHist(hist);
		}else {
			log.info("updateSellerPaymentStatus - no update, payment status not setting on {} sellerId={} paymentId={}" ,status.getId(), sellerPayment.getSellerId(), sellerPayment.getId());
		}
	}

	@Override
	public void saveSellerPayment(SellerPayment sellerPayment) {
		String updateBy = "Payments Shape API";
		sellerPayment.setLastUpdatedDate(Calendar.getInstance());
		sellerPayment.setLastUpdatedBy(updateBy);
		sellerPaymentsDAO.saveSellerPayment(sellerPayment);
	}
	
	
	protected String getSellerPaymentStatusIdsByStatusName(String status){
		StringBuilder sb = new StringBuilder();
		String statuses[] = status.split(" ");
		for(String sta:statuses){
		List<Long> statusIdsList = MyaSellerPaymentStatusUtil.getSellerPaymentStatusIdsByStatusName(sta);
			for(Long l : statusIdsList){
				sb.append(l);
				sb.append(" ");
			}
		}
		return sb.toString();
	}

	protected String getPaymentTypesByName(String paymentType) {
        StringBuilder sb = new StringBuilder();
        String paymentTypes[] = paymentType.split(" ");
        for (String type : paymentTypes) {
            List<SellerPaymentType> sellerPaymentTypeList = SellerPaymentType.getSellerPaymentTypesByCategory(type);
            for (SellerPaymentType sellerPaymentType : sellerPaymentTypeList) {
                sb.append(sellerPaymentType.getId()).append(" ");
            }
        }
        return sb.toString();
    }
}