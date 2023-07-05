package com.stubhub.domain.account.exception;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created at 12/29/2014 8:45 AM
 *
 * @author : Caron Zhao
 * @version : 1.0.0
 * @since : PI
 */
public class SellerUnauthorizedExceptionTest {

    @Test
    public void testSellerUnauthorizedException() {
        String expectedErrorCode = "accountmanagement.api.invoice.unauthorized";
        String sellerId = "mockedSellerId";
        String expectedDescription = "No permission to access this API as the payment is not owned by this seller:" + sellerId;
        int expectedStatusCode = 401;
        SellerUnauthorizedException exception = new SellerUnauthorizedException(sellerId);
        Assert.assertEquals(exception.getErrorCode(), expectedErrorCode);
        Assert.assertEquals(exception.getDescription(), expectedDescription);
        Assert.assertEquals(exception.getStatusCode(), expectedStatusCode);
        Assert.assertEquals(exception.getData().size(), 0);
    }
}