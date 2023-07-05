package com.stubhub.domain.account.datamodel.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "CC_TRANS_REASON_CODE")
public class CcTransReason {
	
	@Id
	@Column(name = "REASON_CODE")
	@GeneratedValue(generator = "CC_TRANS_REASON_GEN")
	@GenericGenerator(name = "CC_TRANS_REASON_GEN",strategy = "assigned")
	private Long reasonCode;
	
	@Column(name = "REASON_DESCRIPTION")
	private String reasonDescription;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "REASON_CODE_GROUP_ID")
	private CcTransGroupReason ccTransGroupReason;

	public Long getReasonCode() {
		return reasonCode;
	}

	public void setReasonCode(Long reasonCode) {
		this.reasonCode = reasonCode;
	}

	public String getReasonDescription() {
		return reasonDescription;
	}

	public void setReasonDescription(String reasonDescription) {
		this.reasonDescription = reasonDescription;
	}

	public CcTransGroupReason getCcTransGroupReason() {
		return ccTransGroupReason;
	}

	public void setCcTransGroupReason(CcTransGroupReason ccTransGroupReason) {
		this.ccTransGroupReason = ccTransGroupReason;
	}
	
	
}
