package com.stubhub.domain.account.datamodel.entity;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Calendar;

/**
 * Created by mengli on 11/20/18.
 */
public class BrokerLicenseTest {

    private long userBrokerLicenseId = 1234567892345L;
    private String brokerLicenseNumber = "brokerLicenseNumber";
    private String sellerGuid = "sellerGuid";
    private String sellerId = "sellerId";
    private String stateCode = "stateCode";
    private String countryCode = "countryCode";
    private Integer active = 1;
    private String createdBy = "createdBy";
    private Calendar createdOn = Calendar.getInstance();
    private String lastModifiedBy = "lastModifiedBy";
    private Calendar lastModifiedOn = Calendar.getInstance();

    @Test
    public void testGetSet() {
        BrokerLicense brokerLicense = BrokerLicense.builder().userBrokerLicenseId(userBrokerLicenseId).brokerLicenseNumber(brokerLicenseNumber).sellerGuid(sellerGuid)
                .sellerId(sellerId).stateCode(stateCode).countryCode(countryCode).active(active).createdBy(createdBy).createdOn(createdOn).lastModifiedBy(lastModifiedBy)
                .lastModifiedOn(lastModifiedOn).build();

        Assert.assertEquals(brokerLicense.getUserBrokerLicenseId(), userBrokerLicenseId);
        Assert.assertEquals(brokerLicense.getBrokerLicenseNumber(), brokerLicenseNumber);
        Assert.assertEquals(brokerLicense.getSellerGuid(), sellerGuid);
        Assert.assertEquals(brokerLicense.getSellerId(), sellerId);
        Assert.assertEquals(brokerLicense.getStateCode(), stateCode);
        Assert.assertEquals(brokerLicense.getCountryCode(), countryCode);
        Assert.assertEquals(brokerLicense.getActive(), active);
        Assert.assertEquals(brokerLicense.getCreatedBy(), createdBy);
        Assert.assertEquals(brokerLicense.getCreatedOn(), createdOn);
        Assert.assertEquals(brokerLicense.getLastModifiedBy(), lastModifiedBy);
        Assert.assertEquals(brokerLicense.getLastModifiedOn(), lastModifiedOn);
    }
}
