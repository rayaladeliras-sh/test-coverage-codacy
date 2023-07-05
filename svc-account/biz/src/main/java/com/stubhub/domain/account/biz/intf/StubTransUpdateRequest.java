package com.stubhub.domain.account.biz.intf;

public class StubTransUpdateRequest {

    private Long orderId;
    private Boolean cancelled;
    private Long orderProcSubStatusCode;
    private Long sellerPaymentTypeId;
    private Long activeCsFlag;
    private Long confirmFlowTrackId;
    private Boolean sellerConfirmed;

    public StubTransUpdateRequest(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Boolean getCancelled() {
        return cancelled;
    }

    public void setCancelled(Boolean cancelled) {
        this.cancelled = cancelled;
    }

    public Long getOrderProcSubStatusCode() {
        return orderProcSubStatusCode;
    }

    public void setOrderProcSubStatusCode(Long orderProcSubStatusCode) {
        this.orderProcSubStatusCode = orderProcSubStatusCode;
    }

    public Long getSellerPaymentTypeId() {
        return sellerPaymentTypeId;
    }

    public void setSellerPaymentTypeId(Long sellerPaymentTypeId) {
        this.sellerPaymentTypeId = sellerPaymentTypeId;
    }

    public Long getActiveCsFlag() {
        return activeCsFlag;
    }

    public void setActiveCsFlag(Long activeCsFlag) {
        this.activeCsFlag = activeCsFlag;
    }

    public Long getConfirmFlowTrackId() {
        return confirmFlowTrackId;
    }

    public void setConfirmFlowTrackId(Long confirmFlowTrackId) {
        this.confirmFlowTrackId = confirmFlowTrackId;
    }

    public Boolean getSellerConfirmed() {
        return sellerConfirmed;
    }

    public void setSellerConfirmed(Boolean sellerConfirmed) {
        this.sellerConfirmed = sellerConfirmed;
    }
}