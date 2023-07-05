package com.stubhub.domain.account.invoice;

import java.io.ByteArrayOutputStream;
import java.util.Locale;

import com.stubhub.domain.account.intf.InvoiceResponse;

/**
 * Created at 12/25/2014 5:10 PM
 *
 * @author : Caron Zhao
 * @version : 1.0.0
 * @since : PI
 */
public interface InvoiceGenerator {
    ByteArrayOutputStream generateInvoice(String transactionId, InvoiceResponse invoice,Locale locale);
}