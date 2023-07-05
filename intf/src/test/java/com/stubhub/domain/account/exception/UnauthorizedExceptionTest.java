package com.stubhub.domain.account.exception;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created at 12/29/2014 8:41 AM
 *
 * @author : Caron Zhao
 * @version : 1.0.0
 * @since : PI
 */
public class UnauthorizedExceptionTest {

    @Test
    public void testUnauthorizedException() {
        String expectedErrorCode = "accountmanagement.api.unauthorized";
        String expectedDescription = "No permission to access this API";
        int expectedStatusCode = 401;
        UnauthorizedException exception = new UnauthorizedException();
        Assert.assertEquals(exception.getErrorCode(), expectedErrorCode);
        Assert.assertEquals(exception.getDescription(), expectedDescription);
        Assert.assertEquals(exception.getStatusCode(), expectedStatusCode);
        Assert.assertEquals(exception.getData().size(), 0);
    }
}