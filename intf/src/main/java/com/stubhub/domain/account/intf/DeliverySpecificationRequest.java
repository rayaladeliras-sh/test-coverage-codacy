package com.stubhub.domain.account.intf;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by jicui on 8/31/15.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "DeliverySpecificationRequest")
@XmlType(name = "DeliverySpecificationRequest", propOrder = { "orders","withUserDetails","withSeatDetails"})
public class DeliverySpecificationRequest {
    private String orders;
    private Long withUserDetails;
    private Long withSeatDetails;

    public String getOrders() {
        return orders;
    }

    public void setOrders(String orders) {
        this.orders = orders;
    }

    public Long getWithUserDetails() {
        return withUserDetails;
    }

    public void setWithUserDetails(Long withUserDetails) {
        this.withUserDetails = withUserDetails;
    }

    public Long getWithSeatDetails() {
        return withSeatDetails;
    }

    public void setWithSeatDetails(Long withSeatDetails) {
        this.withSeatDetails = withSeatDetails;
    }
}
