package com.stubhub.domain.account.exception;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created at 12/29/2014 8:47 AM
 *
 * @author : Caron Zhao
 * @version : 1.0.0
 * @since : PI
 */
public class SellerValidationFailedExceptionTest {

    @Test
    public void testSellerValidationFailedException() {
        String expectedErrorCode = "accountmanagement.api.invoice.unauthorized";
        String sellerId = "mockedSellerId";
        String expectedDescription = "No permission to access this API as failed to validate this seller:" + sellerId + " via Solr";
        int expectedStatusCode = 401;
        SellerValidationFailedException exception = new SellerValidationFailedException(sellerId);
        Assert.assertEquals(exception.getErrorCode(), expectedErrorCode);
        Assert.assertEquals(exception.getDescription(), expectedDescription);
        Assert.assertEquals(exception.getStatusCode(), expectedStatusCode);
        Assert.assertEquals(exception.getData().size(), 0);
    }
}