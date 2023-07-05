package com.stubhub.domain.account.intf;

import org.testng.annotations.Test;

/**
 * Created at 11/25/15 10:23 AM
 *
 * @author : Caron Zhao
 * @version : 1.0.0
 */
public class TaxPayerAlertStatusResponseTest {

    @Test
    public void testTaxPayerAlertStatusResponse() {
        TaxPayerAlertStatusResponse response = new TaxPayerAlertStatusResponse();
        response.setAlert(Boolean.TRUE);
        response.getAlert();
//        response.setContactId("1");
//        response.getContactId();
//        response.setContactGuid("abcd");
//        response.getContactGuid();
    }
}