package com.stubhub.domain.account.datamodel.entity;

import org.testng.annotations.Test;

import java.util.Calendar;

/**
 * Created at 12/30/2014 10:56 AM
 *
 * @author : Caron Zhao
 * @version : 1.0.0
 * @since : PI
 */
public class SellerPaymentTest {

    @Test
    public void testSellerPayment() {
        SellerPayment sellerPayment = new SellerPayment();
        sellerPayment.setAmount(new Double(100));
        sellerPayment.getAmount();
        sellerPayment.setBobId(new Long(1));
        sellerPayment.getBobId();
        sellerPayment.setCurrencyCode("USD");
        sellerPayment.getCurrencyCode();
        sellerPayment.setDateAdded(Calendar.getInstance());
        sellerPayment.getDateAdded();
        sellerPayment.setDisbursementOptionId(new Long(1));
        sellerPayment.getDisbursementOptionId();
        sellerPayment.setEventDate(Calendar.getInstance());
        sellerPayment.getEventDate();
        sellerPayment.setEventDateLocal("");
        sellerPayment.getEventDateLocal();
        sellerPayment.setEventId(new Long(1));
        sellerPayment.getEventId();
        sellerPayment.setId(new Long(1));
        sellerPayment.getId();
        sellerPayment.setLastUpdatedBy("");
        sellerPayment.getLastUpdatedBy();
        sellerPayment.setLastUpdatedDate(Calendar.getInstance());
        sellerPayment.getLastUpdatedDate();
        sellerPayment.setOrderId(new Long(1));
        sellerPayment.getOrderId();
        sellerPayment.setOrderStatus("");
        sellerPayment.getOrderStatus();
        sellerPayment.setPayeeEmailId("");
        sellerPayment.getPayeeEmailId();
        sellerPayment.setPaymentSent2GatewayDate(Calendar.getInstance());
        sellerPayment.getPaymentSent2GatewayDate();
        sellerPayment.setReasonDescription("");
        sellerPayment.getReasonDescription();
        sellerPayment.setReferenceNumber("");
        sellerPayment.getReferenceNumber();
        sellerPayment.setSellerId(new Long(1));
        sellerPayment.getSellerId();
        sellerPayment.setSellerPaymentStatusId(new Long(1));
        sellerPayment.getSellerPaymentStatusId();
        sellerPayment.setStatus("");
        sellerPayment.getStatus();
        sellerPayment.setPayeeName("");
        sellerPayment.getPayeeName();
        sellerPayment.setAcctLastFourDigits("1111");
        sellerPayment.getAcctLastFourDigits();
        sellerPayment.setBankName("daa");
        sellerPayment.getBankName();
        sellerPayment.setSellerPaymentTypeId(5L);
        sellerPayment.getSellerPaymentTypeId();
    }
}