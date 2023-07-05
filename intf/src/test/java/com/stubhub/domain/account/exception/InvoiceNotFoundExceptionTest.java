package com.stubhub.domain.account.exception;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created at 12/29/2014 8:43 AM
 *
 * @author : Caron Zhao
 * @version : 1.0.0
 * @since : PI
 */
public class InvoiceNotFoundExceptionTest {

    @Test
    public void testNoInvoiceFoundException() {
        String expectedErrorCode = "accountmanagement.api.invoice.notfound";
        String referenceNumber = "refNumber";
        String expectedDescription = "No invoice found for this reference number:" + referenceNumber;
        int expectedStatusCode = 404;
        InvoiceNotFoundException exception = new InvoiceNotFoundException(referenceNumber);
        Assert.assertEquals(exception.getErrorCode(), expectedErrorCode);
        Assert.assertEquals(exception.getDescription(), expectedDescription);
        Assert.assertEquals(exception.getStatusCode(), expectedStatusCode);
        Assert.assertEquals(exception.getData().size(), 0);
    }
}