package com.stubhub.domain.account.datamodel.dao;

import com.stubhub.domain.account.datamodel.entity.BrokerLicense;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by mengli on 11/14/18.
 */
public interface BrokerLicenseDAO {

    long saveBrokerLicense(BrokerLicense brokerLicense);

    List<BrokerLicense> getBrokerLicensesBySellerGuid(String sellerGuid);

    BrokerLicense getBrokerLicense(long brokerLicenseInfoId);

    int updateBrokerLicenseBySellerGuid(String sellerGuid, String brokerLicenseNumber, String stateCode, String countryCode, Integer active, long brokerLicenseInfoId);
}
