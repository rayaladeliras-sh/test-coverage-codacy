package com.stubhub.domain.account.exception;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created at 12/31/2014 8:09 AM
 *
 * @author : Caron Zhao
 * @version : 1.0.0
 * @since : PI
 */
public class PaymentNotExistExceptionTest {

    @Test
    public void testPaymentNotExistException() {
        String expectedErrorCode = "accountmanagement.api.invoice.paymentNotFound";
        String referenceNumber = "refNumber";
        String expectedDescription = "No payment found for this reference number:" + referenceNumber;
        int expectedStatusCode = 404;
        PaymentNotExistException exception = new PaymentNotExistException(referenceNumber);
        Assert.assertEquals(exception.getErrorCode(), expectedErrorCode);
        Assert.assertEquals(exception.getDescription(), expectedDescription);
        Assert.assertEquals(exception.getStatusCode(), expectedStatusCode);
        Assert.assertEquals(exception.getData().size(), 0);
    }
}