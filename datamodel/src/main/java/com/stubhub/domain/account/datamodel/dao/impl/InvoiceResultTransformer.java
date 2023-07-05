package com.stubhub.domain.account.datamodel.dao.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.hibernate.transform.ResultTransformer;

import com.stubhub.domain.account.datamodel.entity.InvoiceDO;

public class InvoiceResultTransformer implements ResultTransformer {

    private static final long serialVersionUID = 1915767416066042842L;

    @Override
    public Object transformTuple(Object[] tuple, String[] aliases) {
        Map<String, Integer> columns = new HashMap<String, Integer>();
        for (int i = 0; i < aliases.length; ++i) {
            columns.put(aliases[i], i);
        }
        InvoiceDO invoice = new InvoiceDO();
        invoice.setComments((String) getValue("COMMENTS", columns, tuple));
        invoice.setCurrencyCode((String) getValue("CURRENCY_CODE", columns, tuple));
        invoice.setDeliveryMethod((String) getValue("DELIVERY_METHOD", columns, tuple));
        invoice.setEventId(toLong(getValue("EVENT_ID", columns, tuple)));
//        invoice.setEventDate(toCalendar(getValue("EVENT_DATE", columns, tuple)));
//        invoice.setEventName((String) getValue("EVENT_NAME", columns, tuple));
        invoice.setOrderDate(toCalendar(getValue("ORDER_DATE", columns, tuple)));
        invoice.setOrderId(toLong(getValue("TID", columns, tuple)));
        invoice.setPayeeName((String) getValue("PAYEE_NAME", columns, tuple));
        invoice.setPricePerTicket((BigDecimal) getValue("PRICE_PER_TICKET", columns, tuple));
        invoice.setQuantity(toInt(getValue("QUANTITY", columns, tuple)));
        invoice.setRefNumber((String) getValue("REFERENCE_NUMBER", columns, tuple));
        invoice.setRowDesc((String) getValue("ROW_DESC", columns, tuple));
        invoice.setSeats((String) getValue("SEATS", columns, tuple));
        invoice.setSection((String) getValue("SECTION", columns, tuple));
        invoice.setSellerFeeCost((BigDecimal) getValue("SELLER_FEE_COST", columns, tuple));
        invoice.setSellerId(toLong(getValue("SELLER_ID", columns, tuple)));
        invoice.setSellerPaymentTypeId(toLong(getValue("SELLER_PAYMENT_TYPE_ID", columns, tuple)));
        invoice.setSellerPayoutAmount((BigDecimal) getValue("SELLER_PAYOUT_AMOUNT", columns, tuple));
        invoice.setShippingFeeCost((BigDecimal) getValue("SHIPPING_FEE_COST", columns, tuple));
        invoice.setTicketCost((BigDecimal) getValue("TICKET_COST", columns, tuple));
        invoice.setTicketDisclosures((String) getValue("TICKET_DISCLOSURES", columns, tuple));
        invoice.setTicketFeatures((String) getValue("TICKET_FEATURES", columns, tuple));
        invoice.setVatBuyFee((BigDecimal) getValue("VAT_BUY_FEE", columns, tuple));
        invoice.setVatBuyPercentage((BigDecimal) getValue("VAT_BUY_PRCNT", columns, tuple));
        invoice.setVatLogFee((BigDecimal) getValue("VAT_LOG_FEE", columns, tuple));
        invoice.setVatLogPercentage((BigDecimal) getValue("VAT_LOG_PRCNT", columns, tuple));
        invoice.setVatSellFee((BigDecimal) getValue("VAT_SELL_FEE", columns, tuple));
        invoice.setVatSellPercentage((BigDecimal) getValue("VAT_SELL_PRCNT", columns, tuple));
//        invoice.setVenue((String) getValue("VENUE", columns, tuple));
        return invoice;
    }

    @Override
    public List transformList(List collection) {
        return collection;
    }

    private Object getValue(String name, Map<String, Integer> columns, Object[] tuple) {
        Integer index = columns.get(name);
        if (index == null) {
            return null;
        }
        return tuple[index.intValue()];
    }

    private Integer toInt(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).intValue();
        }
        if (value instanceof BigInteger) {
            return ((BigInteger) value).intValue();
        }
        if (value instanceof Long) {
            return ((Long) value).intValue();
        }
        if (value instanceof Integer) {
            return ((Integer) value).intValue();
        }
        return null;
    }

    private Long toLong(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).longValue();
        }
        if (value instanceof BigInteger) {
            return ((BigInteger) value).longValue();
        }
        if (value instanceof Long) {
            return ((Long) value).longValue();
        }
        if (value instanceof Integer) {
            return ((Integer) value).longValue();
        }
        return null;
    }

    private Calendar toCalendar(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Timestamp) {
            Timestamp ts = (Timestamp) value;
            GregorianCalendar cal = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
            cal.setTime(ts);
            return cal;
        }
        return null;
    }
}
