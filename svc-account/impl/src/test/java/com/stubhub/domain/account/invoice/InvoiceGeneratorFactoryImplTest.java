package com.stubhub.domain.account.invoice;

import junit.framework.Assert;
import org.testng.annotations.Test;

/**
 * Created at 12/30/2014 8:11 AM
 *
 * @author : Caron Zhao
 * @version : 1.0.0
 * @since : PI
 */
public class InvoiceGeneratorFactoryImplTest {

    @Test
    public void testInvoiceGeneratorFactoryImpl() {
        InvoiceGeneratorFactory invoiceGeneratorFactory = new InvoiceGeneratorFactoryImpl();
        InvoiceGenerator invoiceGenerator = invoiceGeneratorFactory.getInvoiceGenerator(InvoiceGeneratorFactory.InvoiceGeneratorType.PDF);
        Assert.assertNull(invoiceGenerator);
    }
}