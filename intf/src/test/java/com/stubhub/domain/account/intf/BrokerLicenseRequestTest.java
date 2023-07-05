package com.stubhub.domain.account.intf;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

/**
 * Created by mengli on 11/20/18.
 */
public class BrokerLicenseRequestTest {

    @Test
    public void testGetSet() throws IOException {
        BrokerLicenseRequest brokerLicenseRequest = BrokerLicenseRequest.builder().userBrokerLicenseId(17483920586796L).brokerLicenseNumber("brokerLicenseNumber").stateCode("NY").countryCode("US").build();
        Assert.assertEquals(brokerLicenseRequest.getUserBrokerLicenseId(), 17483920586796L);
        Assert.assertEquals(brokerLicenseRequest.getBrokerLicenseNumber(), "brokerLicenseNumber");
        Assert.assertEquals(brokerLicenseRequest.getStateCode(), "NY");
        Assert.assertEquals(brokerLicenseRequest.getCountryCode(), "US");
    }
}
