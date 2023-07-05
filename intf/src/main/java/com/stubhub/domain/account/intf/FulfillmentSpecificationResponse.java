package com.stubhub.domain.account.intf;

import com.stubhub.domain.account.common.Response;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * Created by jicui on 8/31/15.
 */
@XmlRootElement(name = "FulfillmentSpecificationResponse")
@XmlType(name = "FulfillmentSpecificationResponse", propOrder = {"fulfillmentSpecifications"})
@XmlSeeAlso(value={FulfillmentSpecification.class})
public class FulfillmentSpecificationResponse extends Response {

    private List<FulfillmentSpecification> fulfillmentSpecifications;

    public List<FulfillmentSpecification> getFulfillmentSpecifications() {
        return fulfillmentSpecifications;
    }

    public void setFulfillmentSpecifications(List<FulfillmentSpecification> fulfillmentSpecifications) {
        this.fulfillmentSpecifications = fulfillmentSpecifications;
    }
}
