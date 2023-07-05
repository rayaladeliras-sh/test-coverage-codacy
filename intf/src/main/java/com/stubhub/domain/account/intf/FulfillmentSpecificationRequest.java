package com.stubhub.domain.account.intf;

import org.codehaus.jackson.map.annotate.JsonRootName;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by jicui on 8/31/15.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "FulfillmentSpecificationRequest")
@XmlType(name = "FulfillmentSpecificationRequest", propOrder = { "listings","withUserDetails","withSeatDetails"})
public class FulfillmentSpecificationRequest {
    private String listings;//seperate by comma
    private Long withUserDetails;
    private Long withSeatDetails;

    public String getListings() {
        return listings;
    }

    public void setListings(String listings) {
        this.listings = listings;
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
