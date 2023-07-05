package com.stubhub.domain.account.exception;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created at 12/30/2014 7:52 AM
 *
 * @author : Caron Zhao
 * @version : 1.0.0
 * @since : PI
 */
public class InvoiceGenerateFailedExceptionTest {

    @Test
    public void testInvoiceGenerateFailedException() {
        String expectedErrorCode = "accountmanagement.api.invoice.generate.failure";
        String expectedDescription = "Unexpected error occured when generating invoice";
        int expectedStatusCode = 500;
        InvoiceGenerateFailedException exception = new InvoiceGenerateFailedException();
        Assert.assertEquals(exception.getErrorCode(), expectedErrorCode);
        Assert.assertEquals(exception.getDescription(), expectedDescription);
        Assert.assertEquals(exception.getStatusCode(), expectedStatusCode);
        Assert.assertEquals(exception.getData().size(), 0);
    }
}