package com.stubhub.domain.account.intf;

import com.stubhub.domain.account.common.Response;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created at 11/4/15 2:10 PM
 *
 * @author : Caron Zhao
 * @version : 1.0.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "taxPayerAlertStatus")
@JsonRootName("taxPayerAlertStatus")
public class TaxPayerAlertStatusResponse extends Response {
    @XmlElement(name = "alert", required = true)
    @JsonProperty("alert")
    private Boolean alert;
    public Boolean getAlert() {
        return alert;
    }

    public TaxPayerAlertStatusResponse setAlert(Boolean alert) {
        this.alert = alert;
        return this;
    }
}