package com.stubhub.domain.account.biz.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.stubhub.domain.account.biz.intf.InvoiceBO;
import com.stubhub.domain.account.datamodel.dao.AppliedCreditMemoDAO;
import com.stubhub.domain.account.datamodel.dao.InvoiceDAO;
import com.stubhub.domain.account.datamodel.dao.SellerPaymentsDAO;
import com.stubhub.domain.account.datamodel.entity.AdjustmentReason;
import com.stubhub.domain.account.datamodel.entity.AppliedCreditMemoDO;
import com.stubhub.domain.account.datamodel.entity.InvoiceDO;
import com.stubhub.domain.account.datamodel.entity.OrderAdjustment;
import com.stubhub.domain.account.datamodel.entity.SellerPayment;
import com.stubhub.domain.account.exception.InvoiceNotFoundException;
import com.stubhub.domain.account.exception.PaymentNotExistException;
import com.stubhub.domain.account.intf.AppliedCreditMemo;
import com.stubhub.domain.account.intf.Invoice;
import com.stubhub.domain.account.intf.InvoiceResponse;
import com.stubhub.domain.account.intf.Money;
import com.stubhub.domain.catalog.read.v3.intf.events.dto.response.Event;

@Component("invoiceBOImpl")
public class InvoiceBOImpl implements InvoiceBO {
	
	private final static Logger logger = LoggerFactory.getLogger(InvoiceBOImpl.class);
	
    @Autowired
    private InvoiceDAO invoiceDAO;

    @Autowired
    private SellerPaymentsDAO paymentsDAO;

    @Autowired
    private AppliedCreditMemoDAO appliedCreditMemoDAO;

    @Autowired
    private EventUtil eventUtil;

    public static final String PAY_TYPE_PAYPAL = "PayPal";
    public static final String PAY_TYPE_CHECK = "Check";
    public static final String PAY_TYPE_CHARITY = "Charity";
    public static final String PAY_TYPE_ACCOUNT_CREDIT = "Account Credits";
    public static final int PAY_ID_PAYPAL = 1;
    public static final int PAY_ID_CHECK = 2;
    public static final int PAY_ID_CHARITY = 41;
    public static final int PAY_ID_CHECK_LARGE_SELLER = 821;
    public static final int PAY_ID_ACCOUNT_CREDIT = 3;

    @Override
    public InvoiceResponse getByReferenceNumber(String refNumber, String pid, String acceptLanguage) {
        SellerPayment payment;
        if (StringUtils.isNumeric(pid)) {
            payment = paymentsDAO.getSellerPaymentById(Long.parseLong(pid));
        } else {
            payment = paymentsDAO.getSellerPaymentByRefNumber(refNumber);
        }
        if (null == payment) {
            throw new PaymentNotExistException(refNumber);
        }

        Long tid = payment.getOrderId();
        InvoiceDO invoiceDO = invoiceDAO.getByReferenceNumberAndTid(refNumber, tid);
        if (null == invoiceDO) {
            throw new InvoiceNotFoundException(refNumber);
        }

        Event event = eventUtil.getEventV3(String.valueOf(invoiceDO.getEventId()), acceptLanguage);
        invoiceDO.setEventName(event.getName());
        // keep same as before, parse local date as UTC
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            Date eventDate = df.parse(event.getEventDateLocal().substring(0, 19));
            Calendar eventDateLocal = Calendar.getInstance();
            eventDateLocal.setTime(eventDate);
            invoiceDO.setEventDate(eventDateLocal);
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
        }
        if(event.getVenue() != null){
            invoiceDO.setVenue(event.getVenue().getName());
        }

        InvoiceResponse response = convert2Response(invoiceDO, payment);
        List<AppliedCreditMemo> appliedCreditMemos = buildCreditMemo(payment);
        response.getInvoice().setAppliedCreditMemos(appliedCreditMemos);
        return response;
    }

    private List<AppliedCreditMemo> buildCreditMemo(SellerPayment payment) {
        List<AppliedCreditMemoDO> appliedCreditMemoDOs = appliedCreditMemoDAO.findByAppliedPaymentId(payment.getId());
        Set<Long> cmIds = new HashSet<Long>();
        for (AppliedCreditMemoDO appliedCreditMemoDO : appliedCreditMemoDOs) {
            cmIds.add(appliedCreditMemoDO.getCmPid());
        }
        List<SellerPayment> sellerPayments = paymentsDAO.getSellerPaymentsByIds(new ArrayList<Long>(cmIds));
        Map<Long, SellerPayment> payments = new HashMap<Long, SellerPayment>();
        for (SellerPayment sellerPayment : sellerPayments) {
            payments.put(sellerPayment.getId(), sellerPayment);
        }

        List<AppliedCreditMemo> appliedCreditMemos = new ArrayList<AppliedCreditMemo>();
        for (AppliedCreditMemoDO appliedCreditMemoDO : appliedCreditMemoDOs) {
            AppliedCreditMemo appliedCreditMemo = new AppliedCreditMemo();
            appliedCreditMemo.setAmount(appliedCreditMemoDO.getAppliedAmount());
            appliedCreditMemo.setCurrency(payments.get(appliedCreditMemoDO.getCmPid()).getCurrencyCode());
            appliedCreditMemo.setOrderId(appliedCreditMemoDO.getCmTid());
            appliedCreditMemo.setEvetnDate(payments.get(appliedCreditMemoDO.getCmPid()).getEventDate());
            appliedCreditMemo.setReferenceNumber(payments.get(appliedCreditMemoDO.getCmPid()).getReferenceNumber());
            appliedCreditMemo.setEventId(payments.get(appliedCreditMemoDO.getCmPid()).getEventId());
            appliedCreditMemo.setAppliedDate(appliedCreditMemoDO.getAppliedDate());
            appliedCreditMemos.add(appliedCreditMemo);
        }

        populateEventName(appliedCreditMemos);
        return appliedCreditMemos;
    }

    private void populateEventName(List<AppliedCreditMemo> appliedCreditMemos) {
        Set<Long> ids = new HashSet<Long>();
        for (AppliedCreditMemo appliedCreditMemo : appliedCreditMemos) {
            if (appliedCreditMemo.getEventId() != null) {
                ids.add(appliedCreditMemo.getEventId());
            }
        }

        Map<Long, String> names = eventUtil.getEventNames(ids);
        for (AppliedCreditMemo appliedCreditMemo : appliedCreditMemos) {
            if (appliedCreditMemo.getEventId() != null) {
                appliedCreditMemo.setEventName(names.get(appliedCreditMemo.getEventId()));
            }
        }
    }

    private InvoiceResponse convert2Response(InvoiceDO invoiceDO, SellerPayment payment) {
        InvoiceResponse response = new InvoiceResponse();
        BigDecimal total = BigDecimal.ZERO;
        String currency = null;

        Invoice invoice = convert2Invoice(invoiceDO);
        response.setInvoice(invoice);
        response.setComment(invoiceDO.getComments());
        BigDecimal sellerFeeCost = invoiceDO.getSellerFeeCost() == null ? BigDecimal.ZERO : invoiceDO.getSellerFeeCost();
        BigDecimal commission = invoiceDO.getVatSellFee() != null ? invoiceDO.getVatSellFee().add(sellerFeeCost) : sellerFeeCost;
        if (currency == null && invoiceDO.getCurrencyCode() != null) {
            currency = invoiceDO.getCurrencyCode();
        }
        response.setCommission(new Money(commission, currency));
        response.setDeliveryMethod(invoiceDO.getDeliveryMethod());
        response.setEventDateLocal(invoiceDO.getEventDate());
        response.setEventDescription(invoiceDO.getEventName());
        response.setOrderDate(invoiceDO.getOrderDate());
        response.setOrderNumber(invoiceDO.getOrderId());
        response.setPricePerTicket(new Money(invoiceDO.getPricePerTicket(), currency));
        response.setQuantity(invoiceDO.getQuantity());
        response.setOrderAmount(new Money(BigDecimal.valueOf(payment.getAmount()) ,currency));
        response.setRowDesc(invoiceDO.getRowDesc());
        response.setSeats(invoiceDO.getSeats());
        response.setSection(invoiceDO.getSection());
        response.setShippingFee(new Money(invoiceDO.getShippingFeeCost(), currency));
        response.setTicketDisclosers(invoiceDO.getTicketDisclosures());
        response.setTicketFeatures(invoiceDO.getTicketFeatures());
        response.setVenue(invoiceDO.getVenue());
        total = total.add(invoice.getTotal().getAmount());
        Money money = new Money();
        money.setAmount(total);
        money.setCurrency(currency);
        response.setSubTotal(money);
        response.setTotalChkPayment(money);
        response.setPayeeEmailId(payment.getPayeeEmailId());
        response.setPaymentSentToGatewayDate(payment.getPaymentSent2GatewayDate());
        response.setSellerId(invoiceDO.getSellerId());
        response.setAcctLastFourDigits(payment.getAcctLastFourDigits());
        response.setBankName(payment.getBankName());
        response.setSellerPaymentTypeId(payment.getSellerPaymentTypeId());
        return response;
    }

    private Invoice convert2Invoice(InvoiceDO invoiceDO) {
        Invoice invoice = new Invoice();
        String currency = invoiceDO.getCurrencyCode();
        invoice.setSubtotal(new Money(invoiceDO.getTicketCost(), currency));

        BigDecimal adjustmentAmount = BigDecimal.ZERO;
        String adjCurrency = null;
        Map<Long, AdjustmentReason> reasons = invoiceDAO.getAllAdjustmentReasons();

        StringBuilder reason = new StringBuilder();
        for (OrderAdjustment adj : invoiceDAO.getOrderAdjustmentByOrderId(invoiceDO.getOrderId())) {
            if (reasons.get(adj.getReasonCode()) != null && adj.getAmount() != null) {
                reason.append(reasons.get(adj.getReasonCode()).getReasonDescription()).append(",");
                adjustmentAmount = adjustmentAmount.add(adj.getAmount());
                adjCurrency = adj.getCurrencyCode();
            }
        }
        if (reasons.isEmpty() || adjustmentAmount.compareTo(BigDecimal.ZERO) == 0) {
            invoice.setAdjustments(null);
            invoice.setReasonDesc(null);
        } else {
            invoice.setAdjustments(new Money(adjustmentAmount, adjCurrency));
            invoice.setReasonDesc(reason.toString());
        }

        BigDecimal total = invoiceDO.getTicketCost().subtract(invoiceDO.getSellerFeeCost());
        if (invoice.getAdjustments() != null
                && nullToZero(invoice.getAdjustments().getAmount()).compareTo(BigDecimal.ZERO) != 0) {
            total = total.add(invoice.getAdjustments().getAmount());
            invoice.setIsAdjustment(true);
        } else {
            BigDecimal tmp = total.subtract(nullToZero(invoiceDO.getSellerPayoutAmount()));
            if (tmp.compareTo(BigDecimal.ZERO) < 0) {
                BigDecimal adj = tmp.multiply(new BigDecimal("-1"));
                invoice.setAdjustments(new Money(adj, currency));
                invoice.setIsAdjustment(true);
                total = invoiceDO.getSellerPayoutAmount();
            } else {
                invoice.setAdjustments(new Money(tmp, currency));
                total = total.subtract(tmp);
            }
        }

        invoice.setTotal(new Money(nullToZero(total), currency));
        invoice.setPayeeName(invoiceDO.getPayeeName());
        invoice.setReferenceNumber(invoiceDO.getRefNumber());
        invoice.setSellerPaymentType(toSellerPaymentType(invoiceDO.getSellerPaymentTypeId()));
        return invoice;
    }

    private BigDecimal nullToZero(BigDecimal data) {
        return data == null ? BigDecimal.ZERO : data;
    }

    private String toSellerPaymentType(Long sellerPaymentTypeId) {
        if (sellerPaymentTypeId == null) {
            return PAY_TYPE_CHARITY;
        }
        switch (sellerPaymentTypeId.intValue()) {
        case PAY_ID_PAYPAL:
            return PAY_TYPE_PAYPAL;
        case PAY_ID_CHECK:
        case PAY_ID_CHECK_LARGE_SELLER:
            return PAY_TYPE_CHECK;
        case PAY_ID_ACCOUNT_CREDIT:
            return PAY_TYPE_ACCOUNT_CREDIT;
        default:
            return PAY_TYPE_CHARITY;
        }
    }
}