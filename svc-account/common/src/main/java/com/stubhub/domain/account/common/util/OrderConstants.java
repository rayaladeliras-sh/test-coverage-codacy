package com.stubhub.domain.account.common.util;

public interface OrderConstants {
    long CANCELLATION_REASON_SUB_ACCEPTED = 41L;
    long CANCELLED_AND_SUBBED = 39L;
    long ORDER_PROC_STATUS_PENDING_CONFIRMATION = 1L;
    long FRAUD_CHECK_STATUS_SUBBED_ORDER = 101L;
    long SELLER_PAYMENT_TYPE_DO_NOT_PAY = 21L;
    long ORDER_PROC_STATUS_CONFIRM_CSR = 5L;
    long ORDER_PROC_STATUS_APPROVED = 43L;
    long ORDER_PROC_STATUS_PURCHASED = 42L;

    //DB:ORDER_PROC_SUB_STATUS_CODE
    Long OrderProcSubStatusCodeEnum_SubsInProcessSeller_ID = 2L;
    Long OrderProcSubStatusCodeEnum_InhandDateChangeRequested_ID = 48L;

    Long OrderProcStatusCodeEnum_Shipped_ID = 4000L;
    Long OrderProcStatusCodeEnum_Delivered_ID = 5000L;
    Long OrderProcStatusCodeEnum_Fulfilled_ID = 6000L;
    Long OrderProcStatusCodeEnum_SubsOffered_ID = 7000L;
    Long OrderProcStatusCodeEnum_Cancelled_ID = 8000L;

    //DB: CONFIRM_FLOW_TRACK
    Long ConfirmFlowTrack_SELLER_CONFIRMED_TICKETS_IN_HAND = 1L;
    Long ConfirmFlowTrack_SELLER_CONFIRMED_TICKETS_NOT_IN_HAND = 2L;
    Long ConfirmFlowTrack_INHAND_DATE_CHANGED_ID = 3L;
    Long ConfirmFlowTrack_SUBS_OFFERED_ID = 4L;
    Long ConfirmFlowTrack_NO_TICKETS_ID = 5L;

    //DB: cs_stub_trans_flags_desc
    Long CsStubTransFlagsDesc_Sent_to_Sourcing_ID = 2L;//"Sent to Sourcing"
    Long CsStubTransFlagsDesc_Late_Fulfillment_Notification_Seller_ID = 1622L;//Late Fulfillment Notification: Seller

    String TRANSACTION_CANCELLATION_EXTRA_INFO = "See subs section for details";
}
