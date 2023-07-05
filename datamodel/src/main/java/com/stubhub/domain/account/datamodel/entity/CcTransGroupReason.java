package com.stubhub.domain.account.datamodel.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "CC_TRANS_REASON_GROUP_CODE")
public class CcTransGroupReason {
	
	@Id
	@Column(name = "REASON_CODE_GROUP_ID")
	@GeneratedValue(generator = "CC_TRANS_REASON_GROUP_GEN")
	@GenericGenerator(name = "CC_TRANS_REASON_GROUP_GEN",strategy = "assigned")
	private Long reasonGroupCode;
	
	@Column(name = "REASON_CODE_GROUP_DESC")
	private String reasonGroupDescription;

	public Long getReasonGroupCode() {
		return reasonGroupCode;
	}

	public void setReasonGroupCode(Long reasonGroupCode) {
		this.reasonGroupCode = reasonGroupCode;
	}

	public String getReasonGroupDescription() {
		return reasonGroupDescription;
	}

	public void setReasonGroupDescription(String reasonGroupDescription) {
		this.reasonGroupDescription = reasonGroupDescription;
	}

	
	
}
