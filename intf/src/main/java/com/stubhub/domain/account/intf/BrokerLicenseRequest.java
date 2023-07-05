package com.stubhub.domain.account.intf;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.*;

/**
 * Created by mengli on 11/13/18.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "brokerLicenseRequest")
@XmlType(name = "", propOrder = {
        "userBrokerLicenseId", "brokerLicenseNumber", "countryCode", "stateCode", "active"
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BrokerLicenseRequest {
    @XmlElement(name = "userBrokerLicenseId")
    private long userBrokerLicenseId;

    @XmlElement(name = "brokerLicenseNumber", required = true)
    private String brokerLicenseNumber;

    @XmlElement(name = "countryCode", required = true)
    private String countryCode;

    @XmlElement(name = "stateCode", required = true)
    private String stateCode;

    @XmlElement(name = "active", defaultValue = "1")
    private Integer active;
}