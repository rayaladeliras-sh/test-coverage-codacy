package com.stubhub.domain.account.common.enums;

import org.testng.annotations.Test;

/**
 * Created at 10/29/15 4:20 PM
 *
 * @author : Caron Zhao
 * @version : 1.0.0
 */
public class SellerPaymentTypeTest {

    @Test
    public void testGetSellerPaymentTypesByCategory() {
        SellerPaymentType.getSellerPaymentTypesByCategory(null);
        SellerPaymentType.getSellerPaymentTypesByCategory("PayPal");
        SellerPaymentType.getSellerPaymentTypesByCategory("Check");
        SellerPaymentType.getSellerPaymentTypesByCategory("Credit");
        SellerPaymentType.getSellerPaymentTypesByCategory("Charity");
        SellerPaymentType.getSellerPaymentTypesByCategory("ACH");
    }
}
