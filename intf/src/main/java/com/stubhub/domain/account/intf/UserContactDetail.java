package com.stubhub.domain.account.intf;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by jicui on 8/31/15.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UserContactDetail", propOrder = {
        "contactId",
        "countryCode",
        "provinceOrStateCode",
        "locality",
        "postalcode"
})
public class UserContactDetail {
    private Long contactId;
    private String countryCode;
    private String provinceOrStateCode;
    private String locality;
    private String postalcode;

    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getProvinceOrStateCode() {
        return provinceOrStateCode;
    }

    public void setProvinceOrStateCode(String provinceOrStateCode) {
        this.provinceOrStateCode = provinceOrStateCode;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }
}
