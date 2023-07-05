package com.stubhub.domain.account.intf;

import com.stubhub.domain.account.common.Response;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * Created by jicui on 8/31/15.
 */
@XmlRootElement(name = "DeliverySpecificationResponse")
@XmlType(name = "DeliverySpecificationResponse", propOrder = {"deliverySpecifications"})
@XmlSeeAlso(value={DeliverySpecification.class})
public class DeliverySpecificationResponse extends Response {
    private List<DeliverySpecification> deliverySpecifications;

    public List<DeliverySpecification> getDeliverySpecifications() {
        return deliverySpecifications;
    }

    public void setDeliverySpecifications(List<DeliverySpecification> deliverySpecifications) {
        this.deliverySpecifications = deliverySpecifications;
    }
}
