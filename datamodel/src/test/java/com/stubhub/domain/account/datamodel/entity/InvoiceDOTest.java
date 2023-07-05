package com.stubhub.domain.account.datamodel.entity;

import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.Calendar;

/**
 * Created at 12/30/2014 10:36 AM
 *
 * @author : Caron Zhao
 * @version :
 * @since :
 */
public class InvoiceDOTest {

    @Test
    public void testInvoiceDO() {
        InvoiceDO invoiceDO = new InvoiceDO();
        invoiceDO.setComments("comments");
        invoiceDO.getComments();
        invoiceDO.setCurrencyCode("GBP");
        invoiceDO.getCurrencyCode();
        invoiceDO.setDeliveryMethod("UPS");
        invoiceDO.getDeliveryMethod();
        Calendar calendar = Calendar.getInstance();
        invoiceDO.setEventDate(calendar);
        invoiceDO.getEventDate();
        invoiceDO.setEventName("name");
        invoiceDO.getEventName();
        invoiceDO.setOrderDate(calendar);
        invoiceDO.getOrderDate();
        invoiceDO.setOrderId(new Long(1));
        invoiceDO.getOrderId();
        invoiceDO.setPayeeName("guest");
        invoiceDO.getPayeeName();
        invoiceDO.setPricePerTicket(new BigDecimal(1));
        invoiceDO.getPricePerTicket();
        invoiceDO.setQuantity(new Integer(1));
        invoiceDO.getQuantity();
        invoiceDO.setRefNumber("123");
        invoiceDO.getRefNumber();
        invoiceDO.setRowDesc("abc");
        invoiceDO.getRowDesc();
        invoiceDO.setSeats("123");
        invoiceDO.getSeats();
        invoiceDO.setSection("A");
        invoiceDO.getSection();
        invoiceDO.setSellerFeeCost(new BigDecimal(1));
        invoiceDO.getSellerFeeCost();
        invoiceDO.setSellerId(new Long(1));
        invoiceDO.getSellerId();
        invoiceDO.setSellerPaymentTypeId(new Long(1));
        invoiceDO.getSellerPaymentTypeId();
        invoiceDO.setSellerPayoutAmount(new BigDecimal(1));
        invoiceDO.getSellerPayoutAmount();
        invoiceDO.setShippingFeeCost(new BigDecimal(1));
        invoiceDO.getShippingFeeCost();
        invoiceDO.setTicketCost(new BigDecimal(1));
        invoiceDO.getTicketDisclosures();
        invoiceDO.setTicketDisclosures("abc");
        invoiceDO.setTicketFeatures("");
        invoiceDO.getTicketFeatures();
        invoiceDO.setVatBuyFee(new BigDecimal(1));
        invoiceDO.getVatBuyFee();
        invoiceDO.setVatBuyPercentage(new BigDecimal(1));
        invoiceDO.getVatBuyPercentage();
        invoiceDO.setVatSellFee(new BigDecimal(1));
        invoiceDO.getVatSellFee();
        invoiceDO.setVatLogPercentage(new BigDecimal(1));
        invoiceDO.getVatLogPercentage();
        invoiceDO.setVatLogFee(new BigDecimal(1));
        invoiceDO.getVatLogFee();
        invoiceDO.setVatSellPercentage(new BigDecimal(1));
        invoiceDO.getVatSellPercentage();
        invoiceDO.setVenue("abc");
        invoiceDO.getVenue();
    }
}