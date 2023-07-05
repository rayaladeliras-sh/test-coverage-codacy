package com.stubhub.domain.account.intf;

import com.stubhub.domain.account.common.Response;
import com.stubhub.domain.account.datamodel.entity.BrokerLicense;
import lombok.*;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Created by mengli on 11/13/18.
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "brokerLicenseResponse")
@XmlType(name = "", propOrder = {
       "brokerLicenses"
})
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BrokerLicenseResponse extends Response {
    @XmlElement(name = "brokerLicenses", required = true)
    private List<BrokerLicense> brokerLicenses;
}
