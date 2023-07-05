package com.stubhub.domain.account.intf;

import com.stubhub.domain.account.datamodel.entity.BrokerLicense;
import org.apache.xerces.impl.dv.util.Base64;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mengli on 11/20/18.
 */
public class BrokerLicenseResponseTest {
    @Test
    public void testGetSet() throws IOException {
        List<BrokerLicense> brokerLicenses = new ArrayList<BrokerLicense>();
        brokerLicenses.add(BrokerLicense.builder().userBrokerLicenseId(1234567345324L).brokerLicenseNumber("brokerLicenseNumber").countryCode("US").stateCode("NY").active(1).build());

        Assert.assertEquals(brokerLicenses.get(0).getBrokerLicenseNumber(), "brokerLicenseNumber");
        Assert.assertEquals(brokerLicenses.get(0).getUserBrokerLicenseId(), 1234567345324L);
        Assert.assertEquals(brokerLicenses.get(0).getCountryCode(), "US");
        Assert.assertEquals(brokerLicenses.get(0).getStateCode(), "NY");
        Assert.assertEquals(brokerLicenses.get(0).getActive().intValue(), 1);
    }
}
