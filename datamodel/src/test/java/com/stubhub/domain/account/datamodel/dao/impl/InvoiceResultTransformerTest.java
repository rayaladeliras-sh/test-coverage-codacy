package com.stubhub.domain.account.datamodel.dao.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

import com.stubhub.domain.account.datamodel.entity.InvoiceDO;

public class InvoiceResultTransformerTest {

    private InvoiceResultTransformer transformer;

    private static String[] aliases = new String[] { "TID", "REFERENCE_NUMBER", "ORDER_DATE", "EVENT_NAME", "VENUE",
            "SELLER_ID", "EVENT_DATE", "QUANTITY", "SECTION", "ROW_DESC", "SEATS", "TICKET_FEATURES",
            "TICKET_DISCLOSURES", "COMMENTS", "TICKET_COST", "PRICE_PER_TICKET", "SELLER_FEE_COST",
            "SHIPPING_FEE_COST", "SELLER_PAYOUT_AMOUNT", "SELLER_PAYMENT_TYPE_ID", "DELIVERY_METHOD", "PAYEE_NAME",
            "CURRENCY_CODE", "VAT_SELL_FEE", "VAT_SELL_PRCNT", "VAT_LOG_FEE", "VAT_LOG_PRCNT", "VAT_BUY_FEE",
            "VAT_BUY_PRCNT", "EVENT_ID" };

    @BeforeMethod
    public void setUp() {
        transformer = new InvoiceResultTransformer();
    }

    @Test
    public void testTransformTuple() {
        Object[] tuple = new Object[30];
        tuple[0] = new BigDecimal("1");
        tuple[1] = "REFERENCE_NUMBER";
        tuple[2] = new Timestamp(2L);
        tuple[3] = "EVENT_NAME";
        tuple[4] = "VENUE";
        tuple[5] = new BigDecimal("3");
        tuple[6] = new Timestamp(4L);
        tuple[7] = new BigDecimal("5");
        tuple[8] = "SECTION";
        tuple[9] = "ROW_DESC";
        tuple[10] = "SEATS";
        tuple[11] = "TICKET_FEATURES";
        tuple[12] = "TICKET_DISCLOSURES";
        tuple[13] = "COMMENTS";
        tuple[14] = new BigDecimal("6");
        tuple[15] = new BigDecimal("7");
        tuple[16] = new BigDecimal("8");
        tuple[17] = new BigDecimal("9");
        tuple[18] = new BigDecimal("10");
        tuple[19] = new BigDecimal("11");
        tuple[20] = "DELIVERY_METHOD";
        tuple[21] = "PAYEE_NAME";
        tuple[22] = "CURRENCY_CODE";
        tuple[23] = new BigDecimal("12");
        tuple[24] = new BigDecimal("13");
        tuple[25] = new BigDecimal("14");
        tuple[26] = new BigDecimal("15");
        tuple[27] = new BigDecimal("16");
        tuple[28] = new BigDecimal("17");
        tuple[29] = new BigDecimal("18");

        InvoiceDO invoice = (InvoiceDO) transformer.transformTuple(tuple, aliases);

        assertEquals(invoice.getComments(), "COMMENTS");
        assertEquals(invoice.getCurrencyCode(), "CURRENCY_CODE");
        assertEquals(invoice.getDeliveryMethod(), "DELIVERY_METHOD");
//        assertEquals(invoice.getEventDate().getTime().getTime(), 4L);
//        assertEquals(invoice.getEventName(), "EVENT_NAME");
        assertEquals(invoice.getOrderDate().getTime().getTime(), 2L);
        assertEquals(invoice.getOrderId(), Long.valueOf(1L));
        assertEquals(invoice.getPayeeName(), "PAYEE_NAME");
        assertEquals(invoice.getPricePerTicket(), new BigDecimal("7"));
        assertEquals(invoice.getQuantity(), Integer.valueOf(5));
        assertEquals(invoice.getRefNumber(), "REFERENCE_NUMBER");
        assertEquals(invoice.getRowDesc(), "ROW_DESC");
        assertEquals(invoice.getSeats(), "SEATS");
        assertEquals(invoice.getSection(), "SECTION");
        assertEquals(invoice.getSellerFeeCost(), new BigDecimal("8"));
        assertEquals(invoice.getSellerId(), Long.valueOf(3L));
        assertEquals(invoice.getSellerPaymentTypeId(), Long.valueOf(11L));
        assertEquals(invoice.getSellerPayoutAmount(), new BigDecimal("10"));
        assertEquals(invoice.getShippingFeeCost(), new BigDecimal("9"));
        assertEquals(invoice.getTicketCost(), new BigDecimal("6"));
        assertEquals(invoice.getTicketDisclosures(), "TICKET_DISCLOSURES");
        assertEquals(invoice.getTicketFeatures(), "TICKET_FEATURES");
        assertEquals(invoice.getVatBuyFee(), new BigDecimal("16"));
        assertEquals(invoice.getVatBuyPercentage(), new BigDecimal("17"));
        assertEquals(invoice.getVatLogFee(), new BigDecimal("14"));
        assertEquals(invoice.getVatLogPercentage(), new BigDecimal("15"));
        assertEquals(invoice.getVatSellFee(), new BigDecimal("12"));
        assertEquals(invoice.getVatSellPercentage(), new BigDecimal("13"));
        assertEquals(invoice.getEventId(), Long.valueOf(18L));
//        assertEquals(invoice.getVenue(), "VENUE");
    }
}
