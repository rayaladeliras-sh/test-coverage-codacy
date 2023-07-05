package com.stubhub.domain.account.biz.impl;

import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anySet;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.stubhub.domain.account.biz.intf.InvoiceBO;

import com.stubhub.domain.account.exception.InvoiceNotFoundException;
import com.stubhub.domain.account.exception.PaymentNotExistException;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.stubhub.domain.account.datamodel.dao.AppliedCreditMemoDAO;
import com.stubhub.domain.account.datamodel.dao.InvoiceDAO;
import com.stubhub.domain.account.datamodel.dao.SellerPaymentsDAO;
import com.stubhub.domain.account.datamodel.entity.AppliedCreditMemoDO;
import com.stubhub.domain.account.datamodel.entity.InvoiceDO;
import com.stubhub.domain.account.datamodel.entity.SellerPayment;
import com.stubhub.domain.account.intf.InvoiceResponse;
import com.stubhub.domain.catalog.read.v3.intf.events.dto.response.Event;
import com.stubhub.domain.catalog.read.v3.intf.events.dto.response.Venue;

public class InvoiceBOImplTest {

    private InvoiceBO invoiceBO;

    private InvoiceDAO invoiceDAO;

    private SellerPaymentsDAO paymentsDAO;

    private AppliedCreditMemoDAO appliedCreditMemoDAO;

    private EventUtil eventUtil;

    @BeforeMethod
    public void setUp() {
        invoiceBO = new InvoiceBOImpl();
        invoiceDAO = mock(InvoiceDAO.class);
        ReflectionTestUtils.setField(invoiceBO, "invoiceDAO", invoiceDAO);
        paymentsDAO = mock(SellerPaymentsDAO.class);
        SellerPayment payment = new SellerPayment();
        payment.setAmount(Double.valueOf(100.00));
        payment.setAcctLastFourDigits("1234");
        payment.setBankName("bank");
        payment.setSellerPaymentTypeId(5L);
        when(paymentsDAO.getSellerPaymentByRefNumber("refNumber")).thenReturn(payment);
        ReflectionTestUtils.setField(invoiceBO, "paymentsDAO", paymentsDAO);
        appliedCreditMemoDAO = mock(AppliedCreditMemoDAO.class);
        ReflectionTestUtils.setField(invoiceBO, "appliedCreditMemoDAO", appliedCreditMemoDAO);
        eventUtil = mock(EventUtil.class);
        ReflectionTestUtils.setField(invoiceBO, "eventUtil", eventUtil);
        Event event = new Event();
        event.setVenue(new Venue());
        event.setEventDateLocal("2016-11-05T23:00:00-04:00");
        event.setTimezone("US/Eastern");
        when(eventUtil.getEventV3(anyString(), anyString())).thenReturn(event);
    }

    @Test
    public void testGetByReferenceNumberCatalogFail() {
        InvoiceDO invoiceDO = buildDbInvoice();
        when(invoiceDAO.getByReferenceNumberAndTid(eq(invoiceDO.getRefNumber()), anyLong())).thenReturn(invoiceDO);
        // simulate catalog local event date format incorrect and venue null
        eventUtil.getEventV3(String.valueOf(invoiceDO.getEventId()), "en-us").setEventDateLocal("2016-11-05T23-00-00");
        eventUtil.getEventV3(String.valueOf(invoiceDO.getEventId()), "en-us").setVenue(null);

        InvoiceResponse result = invoiceBO.getByReferenceNumber(invoiceDO.getRefNumber(), null,"en-us");
        assertNotNull(result);
        assertNotNull(result.getInvoice());
        assertEquals(result.getInvoice().getAdjustments().getAmount(), new BigDecimal("4"));
        assertEquals(result.getInvoice().getAdjustments().getCurrency(), invoiceDO.getCurrencyCode());
        assertEquals(result.getComment(), invoiceDO.getComments());
        assertEquals(result.getCommission().getAmount(), new BigDecimal("20"));
        assertEquals(result.getCommission().getCurrency(), invoiceDO.getCurrencyCode());
        assertEquals(result.getDeliveryMethod(), invoiceDO.getDeliveryMethod());
        assertEquals(result.getInvoice().getIsAdjustment(), Boolean.TRUE);
        assertEquals(result.getOrderDate(), invoiceDO.getOrderDate());
        assertEquals(result.getOrderNumber(), invoiceDO.getOrderId());
        assertEquals(result.getInvoice().getPayeeName(), invoiceDO.getPayeeName());
        assertEquals(result.getPricePerTicket().getAmount(), invoiceDO.getPricePerTicket());
        assertEquals(result.getPricePerTicket().getCurrency(), invoiceDO.getCurrencyCode());
        assertEquals(result.getQuantity(), invoiceDO.getQuantity());
        assertEquals(result.getInvoice().getReasonDesc(), null);
        assertEquals(result.getInvoice().getReferenceNumber(), invoiceDO.getRefNumber());
        assertEquals(result.getRowDesc(), invoiceDO.getRowDesc());
        assertEquals(result.getSeats(), invoiceDO.getSeats());
        assertEquals(result.getSection(), invoiceDO.getSection());
        assertEquals(result.getInvoice().getSellerPaymentType(), InvoiceBOImpl.PAY_TYPE_CHARITY);
        assertEquals(result.getShippingFee().getAmount(), invoiceDO.getShippingFeeCost());
        assertEquals(result.getShippingFee().getCurrency(), invoiceDO.getCurrencyCode());
        assertEquals(result.getInvoice().getSubtotal().getAmount(), invoiceDO.getTicketCost());
        assertEquals(result.getInvoice().getSubtotal().getCurrency(), invoiceDO.getCurrencyCode());
        assertEquals(result.getTicketDisclosers(), invoiceDO.getTicketDisclosures());
        assertEquals(result.getTicketFeatures(), invoiceDO.getTicketFeatures());
        assertEquals(result.getInvoice().getTotal().getAmount(), new BigDecimal("7"));
        assertEquals(result.getInvoice().getTotal().getCurrency(), invoiceDO.getCurrencyCode());
        assertEquals(result.getTotalChkPayment().getAmount(), new BigDecimal("7"));
        assertEquals(result.getTotalChkPayment().getCurrency(), invoiceDO.getCurrencyCode());
        assertEquals(result.getSubTotal().getAmount(), new BigDecimal("7"));
        assertEquals(result.getSubTotal().getCurrency(), invoiceDO.getCurrencyCode());

    }

    @Test
    public void testGetByReferenceNumber() {
        InvoiceDO invoiceDO = buildDbInvoice();
        when(invoiceDAO.getByReferenceNumberAndTid(eq(invoiceDO.getRefNumber()), anyLong())).thenReturn(invoiceDO);

        InvoiceResponse result = invoiceBO.getByReferenceNumber(invoiceDO.getRefNumber(), null, "en-us");
        assertNotNull(result);
        assertNotNull(result.getInvoice());
        assertEquals(result.getInvoice().getAdjustments().getAmount(), new BigDecimal("4"));
        assertEquals(result.getInvoice().getAdjustments().getCurrency(), invoiceDO.getCurrencyCode());
        assertEquals(result.getComment(), invoiceDO.getComments());
        assertEquals(result.getCommission().getAmount(), new BigDecimal("20"));
        assertEquals(result.getCommission().getCurrency(), invoiceDO.getCurrencyCode());
        assertEquals(result.getDeliveryMethod(), invoiceDO.getDeliveryMethod());
        assertEquals(result.getEventDateLocal(), invoiceDO.getEventDate());
        assertEquals(result.getEventDescription(), invoiceDO.getEventName());
        assertEquals(result.getInvoice().getIsAdjustment(), Boolean.TRUE);
        assertEquals(result.getOrderDate(), invoiceDO.getOrderDate());
        assertEquals(result.getOrderNumber(), invoiceDO.getOrderId());
        assertEquals(result.getInvoice().getPayeeName(), invoiceDO.getPayeeName());
        assertEquals(result.getPricePerTicket().getAmount(), invoiceDO.getPricePerTicket());
        assertEquals(result.getPricePerTicket().getCurrency(), invoiceDO.getCurrencyCode());
        assertEquals(result.getQuantity(), invoiceDO.getQuantity());
        assertEquals(result.getInvoice().getReasonDesc(), null);
        assertEquals(result.getInvoice().getReferenceNumber(), invoiceDO.getRefNumber());
        assertEquals(result.getRowDesc(), invoiceDO.getRowDesc());
        assertEquals(result.getSeats(), invoiceDO.getSeats());
        assertEquals(result.getSection(), invoiceDO.getSection());
        assertEquals(result.getInvoice().getSellerPaymentType(), InvoiceBOImpl.PAY_TYPE_CHARITY);
        assertEquals(result.getShippingFee().getAmount(), invoiceDO.getShippingFeeCost());
        assertEquals(result.getShippingFee().getCurrency(), invoiceDO.getCurrencyCode());
        assertEquals(result.getInvoice().getSubtotal().getAmount(), invoiceDO.getTicketCost());
        assertEquals(result.getInvoice().getSubtotal().getCurrency(), invoiceDO.getCurrencyCode());
        assertEquals(result.getTicketDisclosers(), invoiceDO.getTicketDisclosures());
        assertEquals(result.getTicketFeatures(), invoiceDO.getTicketFeatures());
        assertEquals(result.getInvoice().getTotal().getAmount(), new BigDecimal("7"));
        assertEquals(result.getInvoice().getTotal().getCurrency(), invoiceDO.getCurrencyCode());
        assertEquals(result.getVenue(), invoiceDO.getVenue());
        assertEquals(result.getTotalChkPayment().getAmount(), new BigDecimal("7"));
        assertEquals(result.getTotalChkPayment().getCurrency(), invoiceDO.getCurrencyCode());
        assertEquals(result.getSubTotal().getAmount(), new BigDecimal("7"));
        assertEquals(result.getSubTotal().getCurrency(), invoiceDO.getCurrencyCode());

        invoiceDO.setSellerPaymentTypeId(Long.valueOf(InvoiceBOImpl.PAY_ID_ACCOUNT_CREDIT));
        result = invoiceBO.getByReferenceNumber(invoiceDO.getRefNumber(), null,"en-us");
        assertEquals(result.getInvoice().getSellerPaymentType(), InvoiceBOImpl.PAY_TYPE_ACCOUNT_CREDIT);

        invoiceDO.setSellerPaymentTypeId(Long.valueOf(InvoiceBOImpl.PAY_ID_CHARITY));
        result = invoiceBO.getByReferenceNumber(invoiceDO.getRefNumber(), null,"en-us");
        assertEquals(result.getInvoice().getSellerPaymentType(), InvoiceBOImpl.PAY_TYPE_CHARITY);

        invoiceDO.setSellerPaymentTypeId(Long.valueOf(InvoiceBOImpl.PAY_ID_CHECK));
        result = invoiceBO.getByReferenceNumber(invoiceDO.getRefNumber(), null,"en-us");
        assertEquals(result.getInvoice().getSellerPaymentType(), InvoiceBOImpl.PAY_TYPE_CHECK);

        invoiceDO.setSellerPaymentTypeId(Long.valueOf(InvoiceBOImpl.PAY_ID_CHECK_LARGE_SELLER));
        result = invoiceBO.getByReferenceNumber(invoiceDO.getRefNumber(), null,"en-us");
        assertEquals(result.getInvoice().getSellerPaymentType(), InvoiceBOImpl.PAY_TYPE_CHECK);

        invoiceDO.setSellerPaymentTypeId(Long.valueOf(InvoiceBOImpl.PAY_ID_PAYPAL));
        result = invoiceBO.getByReferenceNumber(invoiceDO.getRefNumber(), null, "en-us");
        assertEquals(result.getInvoice().getSellerPaymentType(), InvoiceBOImpl.PAY_TYPE_PAYPAL);
    }

    @Test
    public void testGetByReferenceNumberAndPid() {
        InvoiceDO invoiceDO = buildDbInvoice();
        when(invoiceDAO.getByReferenceNumberAndTid(eq(invoiceDO.getRefNumber()), anyLong())).thenReturn(invoiceDO);
        SellerPayment payment = new SellerPayment();
        payment.setAmount(90.00);
        when(paymentsDAO.getSellerPaymentById(eq(100L))).thenReturn(payment);

        InvoiceResponse result = invoiceBO.getByReferenceNumber(invoiceDO.getRefNumber(), "100", "en-us");
        assertNotNull(result);
        assertNotNull(result.getInvoice());
        assertEquals(result.getInvoice().getAdjustments().getAmount(), new BigDecimal("4"));
        assertEquals(result.getInvoice().getAdjustments().getCurrency(), invoiceDO.getCurrencyCode());
        assertEquals(result.getComment(), invoiceDO.getComments());
        assertEquals(result.getCommission().getAmount(), new BigDecimal("20"));
        assertEquals(result.getCommission().getCurrency(), invoiceDO.getCurrencyCode());
        assertEquals(result.getDeliveryMethod(), invoiceDO.getDeliveryMethod());
        assertEquals(result.getEventDateLocal(), invoiceDO.getEventDate());
        assertEquals(result.getEventDescription(), invoiceDO.getEventName());
        assertEquals(result.getInvoice().getIsAdjustment(), Boolean.TRUE);
        assertEquals(result.getOrderDate(), invoiceDO.getOrderDate());
        assertEquals(result.getOrderNumber(), invoiceDO.getOrderId());
        assertEquals(result.getInvoice().getPayeeName(), invoiceDO.getPayeeName());
        assertEquals(result.getPricePerTicket().getAmount(), invoiceDO.getPricePerTicket());
        assertEquals(result.getPricePerTicket().getCurrency(), invoiceDO.getCurrencyCode());
        assertEquals(result.getQuantity(), invoiceDO.getQuantity());
        assertEquals(result.getInvoice().getReasonDesc(), null);
        assertEquals(result.getInvoice().getReferenceNumber(), invoiceDO.getRefNumber());
        assertEquals(result.getRowDesc(), invoiceDO.getRowDesc());
        assertEquals(result.getSeats(), invoiceDO.getSeats());
        assertEquals(result.getSection(), invoiceDO.getSection());
        assertEquals(result.getInvoice().getSellerPaymentType(), InvoiceBOImpl.PAY_TYPE_CHARITY);
        assertEquals(result.getShippingFee().getAmount(), invoiceDO.getShippingFeeCost());
        assertEquals(result.getShippingFee().getCurrency(), invoiceDO.getCurrencyCode());
        assertEquals(result.getInvoice().getSubtotal().getAmount(), invoiceDO.getTicketCost());
        assertEquals(result.getInvoice().getSubtotal().getCurrency(), invoiceDO.getCurrencyCode());
        assertEquals(result.getTicketDisclosers(), invoiceDO.getTicketDisclosures());
        assertEquals(result.getTicketFeatures(), invoiceDO.getTicketFeatures());
        assertEquals(result.getInvoice().getTotal().getAmount(), new BigDecimal("7"));
        assertEquals(result.getInvoice().getTotal().getCurrency(), invoiceDO.getCurrencyCode());
        assertEquals(result.getVenue(), invoiceDO.getVenue());
        assertEquals(result.getTotalChkPayment().getAmount(), new BigDecimal("7"));
        assertEquals(result.getTotalChkPayment().getCurrency(), invoiceDO.getCurrencyCode());
        assertEquals(result.getSubTotal().getAmount(), new BigDecimal("7"));
        assertEquals(result.getSubTotal().getCurrency(), invoiceDO.getCurrencyCode());
        assertEquals(result.getOrderAmount().getAmount(), BigDecimal.valueOf(payment.getAmount()));
    }
    @Test(expectedExceptions = PaymentNotExistException.class)
    public void testGetByReferenceNumberAndPidPaymentNotExsit() {
        InvoiceDO invoiceDO = buildDbInvoice();
        when(invoiceDAO.getByReferenceNumberAndTid(eq(invoiceDO.getRefNumber()), anyLong())).thenReturn(invoiceDO);
        when(paymentsDAO.getSellerPaymentById(eq(100L))).thenReturn(null);

        InvoiceResponse result = invoiceBO.getByReferenceNumber(invoiceDO.getRefNumber(), "100", "en-us");
    }

    @Test(expectedExceptions = InvoiceNotFoundException.class)
    public void testGetByReferenceNumberAndPidInvoiceNotExsit() {
        InvoiceDO invoiceDO = buildDbInvoice();
        when(invoiceDAO.getByReferenceNumberAndTid(eq(invoiceDO.getRefNumber()), anyLong())).thenReturn(null);
        SellerPayment payment  = new SellerPayment();
        payment.setOrderId(1L);
        when(paymentsDAO.getSellerPaymentById(eq(100L))).thenReturn(payment);

        InvoiceResponse result = invoiceBO.getByReferenceNumber(invoiceDO.getRefNumber(), "100", "en-us");
    }

    @Test
    public void testGetByReferenceNumberCM() {
        InvoiceDO invoiceDO = buildDbInvoice();
        when(invoiceDAO.getByReferenceNumberAndTid(eq(invoiceDO.getRefNumber()), anyLong())).thenReturn(invoiceDO);
        SellerPayment payment = new SellerPayment();
        payment.setId(123L);
        payment.setAmount(Double.valueOf(100.00));
        when(paymentsDAO.getSellerPaymentByRefNumber(invoiceDO.getRefNumber())).thenReturn(payment);
        List<AppliedCreditMemoDO> cms = new ArrayList<AppliedCreditMemoDO>();
        AppliedCreditMemoDO cm = buildCreditMemo();
        cms.add(cm);
        when(appliedCreditMemoDAO.findByAppliedPaymentId(payment.getId())).thenReturn(cms);
        SellerPayment appliedPayment = new SellerPayment();
        appliedPayment.setId(cm.getCmPid());
        appliedPayment.setEventDate(Calendar.getInstance());
        appliedPayment.setEventId(222L);
        appliedPayment.setReferenceNumber("appliedRefNumber");
        appliedPayment.setCurrencyCode("USD");
        when(paymentsDAO.getSellerPaymentsByIds(anyListOf(Long.class))).thenReturn(Arrays.asList(appliedPayment));
        Map<Long, String> eventNames = new HashMap<Long, String>();
        eventNames.put(appliedPayment.getEventId(), "appliedEventName");
        when(eventUtil.getEventNames(anySet())).thenReturn(eventNames);

        InvoiceResponse response = invoiceBO.getByReferenceNumber(invoiceDO.getRefNumber(), null,"en-us");
        assertNotNull(response);
        assertNotNull(response.getInvoice());
        assertNotNull(response.getInvoice().getAppliedCreditMemos());
        assertEquals(response.getInvoice().getAppliedCreditMemos().size(), 1);
        com.stubhub.domain.account.intf.AppliedCreditMemo result = response.getInvoice().getAppliedCreditMemos().get(0);
        assertEquals(result.getAmount(), cm.getAppliedAmount());
        assertEquals(result.getAppliedDate(), cm.getAppliedDate());
        assertEquals(result.getCurrency(), appliedPayment.getCurrencyCode());
        assertEquals(result.getEventId(), appliedPayment.getEventId());
        assertEquals(result.getEventName(), eventNames.get(appliedPayment.getEventId()));
        assertEquals(result.getEvetnDate(), appliedPayment.getEventDate());
        assertEquals(result.getOrderId(), cm.getCmTid());
        assertEquals(result.getReferenceNumber(), appliedPayment.getReferenceNumber());
    }

    private AppliedCreditMemoDO buildCreditMemo() {
        AppliedCreditMemoDO cm = new AppliedCreditMemoDO();
        cm.setAppliedAmount(new BigDecimal("1"));
        cm.setAppliedDate(Calendar.getInstance());
        cm.setAppliedPid(2L);
        cm.setAppliedTid(3L);
        cm.setCmPid(4L);
        cm.setCmTid(5L);
        cm.setId(6L);
        cm.setMessageSeq("7");
        return cm;
    }

    private InvoiceDO buildDbInvoice() {
        InvoiceDO invoice = new InvoiceDO();
        invoice.setComments("comments");
        invoice.setCurrencyCode("currencyCode");
        invoice.setDeliveryMethod("deliveryMethod");
        invoice.setEventDate(Calendar.getInstance());
        invoice.setEventName("eventName");
        invoice.setOrderDate(Calendar.getInstance());
        invoice.setOrderId(1L);
        invoice.setPayeeName("payeeName");
        invoice.setPricePerTicket(new BigDecimal("5"));
        invoice.setQuantity(2);
        invoice.setRefNumber("refNumber");
        invoice.setRowDesc("rowDesc");
        invoice.setSeats("seats");
        invoice.setSection("section");
        invoice.setSellerFeeCost(new BigDecimal("6"));
        invoice.setSellerId(3L);
        invoice.setSellerPaymentTypeId(4L);
        invoice.setSellerPayoutAmount(new BigDecimal("7"));
        invoice.setShippingFeeCost(new BigDecimal("8"));
        invoice.setTicketCost(new BigDecimal("9"));
        invoice.setTicketDisclosures("ticketDisclosures");
        invoice.setTicketFeatures("ticketFeatures");
        invoice.setVatBuyPercentage(new BigDecimal("10"));
        invoice.setVatBuyPercentage(new BigDecimal("11"));
        invoice.setVatLogFee(new BigDecimal("12"));
        invoice.setVatLogPercentage(new BigDecimal("13"));
        invoice.setVatSellFee(new BigDecimal("14"));
        invoice.setVatSellPercentage(new BigDecimal("15"));
        invoice.setVenue("venue");
        return invoice;
    }
}