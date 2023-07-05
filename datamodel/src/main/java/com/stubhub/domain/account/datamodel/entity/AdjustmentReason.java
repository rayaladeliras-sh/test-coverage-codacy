package com.stubhub.domain.account.datamodel.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;

@Entity
@Table(name = "CC_TRANS_REASON_CODE")
@NamedNativeQuery(name = "AdjustmentReason.getAllAdustmentReasons", query = "select p.REASON_CODE REASON_CODE, p.REASON_DESCRIPTION REASON_DESCRIPTION, p.MYA_ADJ_REASON_ID MYA_ADJ_REASON_ID from CC_TRANS_REASON_CODE p, MYA_ADJ_REASON where MYA_ADJ_REASON.active = 1 and MYA_ADJ_REASON.MYA_ADJ_REASON_ID = p.MYA_ADJ_REASON_ID", resultClass = AdjustmentReason.class)
public class AdjustmentReason {

    @Id
    @Column(name = "REASON_CODE")
    private Long reasonCode;

    @Column(name = "REASON_DESCRIPTION")
    private String reasonDescription;

    @Column(name = "MYA_ADJ_REASON_ID")
    private Long myaAdjReasonId;

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

    public Long getMyaAdjReasonId() {
        return myaAdjReasonId;
    }

    public void setMyaAdjReasonId(Long myaAdjReasonId) {
        this.myaAdjReasonId = myaAdjReasonId;
    }
}