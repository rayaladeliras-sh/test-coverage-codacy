package com.stubhub.domain.account.invoice;

/**
 * Created at 12/25/2014 5:14 PM
 *
 * @author : Caron Zhao
 * @version : 1.0.0
 * @since : PI
 */
public interface InvoiceGeneratorFactory {
    InvoiceGenerator getInvoiceGenerator(InvoiceGeneratorType type);

    enum InvoiceGeneratorType {
        PDF
    }
}
