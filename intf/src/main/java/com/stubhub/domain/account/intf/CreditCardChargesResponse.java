package com.stubhub.domain.account.intf;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.stubhub.domain.account.common.Response;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "creditCardCharges")
@XmlType(name = "", propOrder = {
		"creditCardCharges", "totalCount"
		})
public class CreditCardChargesResponse extends Response {
	
	@XmlElement(name = "creditCardCharge", required = true)
	private List<CreditCardCharge> creditCardCharges;

    @XmlElement(name = "totalCount", required = false)
	private Long totalCount;

	public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public List<CreditCardCharge> getCreditCardCharges() {
		return creditCardCharges;
	}

	public void setCreditCardCharges(List<CreditCardCharge> creditCardCharges) {
		this.creditCardCharges = creditCardCharges;
	}
	
}
