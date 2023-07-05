package com.stubhub.domain.account.intf;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.stubhub.domain.account.common.Response;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "creditMemos")
@XmlType(name = "", propOrder = {
		"creditMemos", "totalCount"
		})
public class CreditMemosResponse extends Response {
	@XmlElement(name = "creditMemo", required = true)	
	private List<CreditMemoResponse> creditMemos;

	@XmlElement(name = "totalCount", required = true)
	private Long totalCount;

	public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public List<CreditMemoResponse> getCreditMemos() {
		return creditMemos;
	}

	public void setCreditMemos(List<CreditMemoResponse> creditMemos) {
		this.creditMemos = creditMemos;
	}
}

