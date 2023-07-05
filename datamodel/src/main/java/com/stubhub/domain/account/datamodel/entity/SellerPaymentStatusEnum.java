package com.stubhub.domain.account.datamodel.entity;

import com.stubhub.domain.account.datamodel.enums.PaymentsStatusEnum;

public enum SellerPaymentStatusEnum implements PaymentsStatusEnum {
	READY_TO_PAY(1L, "Ready To Pay"),
	PAYMENT_ASSIGNED_TO_BATCH(2L,"Payment Assigned To Batch"),
	PAYMENT_BATCHED_TO_BE_SENT(3L,"Payment Batched To Be Sent"),
	PAYMENT_CANCELLED_WITHIN_STUBHUB(4L, "Payment Cancelled Within Stubhub"),
	PAYMENT_SENT_TO_GP(5L, "Payment Sent To GP"),
	PAYMENT_SENT_TO_GP_FAILED(6L, "Payment Sent To GP Failed"),
	PAYMENT_SENT_TO_GP_BUT_GP_THREW_EXCEPTION(7L,"Payment Sent To GP But GP Threw Exception"),
	GP_EXECUTION_THROWN(8L, "GP Payment Exception Thrown"),
	GP_PAYMENT_SENT_TO_GATEWAY(9L, "GP Payment Sent To Payment Gateway"),
	GP_PAYMENT_COMPLETED(10L, "GP Payment Completed"),
	GP_PAYMENT_VOIDED(11L, "GP Payment Voided"),
	GP_PAYMENT_UNCLAIMED(12L, "GP Payment Unclaimed"),
	ACCESS_IMPORT_FILE_GENERATED(13L,"Access Import File Generated"),
	PAYMENT_NOTIFIED(14L, "Payment Notified"),
	READY_TO_SEND_CM(15L, "Ready to send Credit Memo"),
	CM_ASSIGNED_TO_BATCH(16L, "Credit Memo Assigned To Batch"),
	CM_BATCHED_TO_BE_SEND(17L, "Credit Memo Batched To Be Sent"),
	CM_SEND_TO_GP(18L, "Credit Memo Sent To GP"),
	CM_SEND_TO_GP_FAILED(19L, "Credit Memo Sent To GP Failed"),
	CM_SENT_TO_GP_BUT_GP_THREW_EXCEPTION(20L, "Credit Memo Sent To GP But GP Threw Exception"),
	CM_EXECUTION_THROWN(21L, "GP Credit Memo Exception Thrown"),
	COMPLETED(22L, "GP Credit Memo Completed"), 
	PAYMENT_FAILURE_NOTIFIED(23L, "Payment Failure Notified"),
	PAYMENT_UNCLAIMED_NOTIFIED (24L, "Payment Unclaimed Notified"),
	VOIDED(25L, "GP Credit Memo Voided"), 
	FUND_CAPTURE(26L, "Fund Capture"),
	PAYMENT_CANCELLED_BEFORE_READY_TO_PAY(27L, "Payment Cancelled before ready to pay"),
	HOLD_PAYMENT_DUE_TO_DD(28L, "Hold Payment due to DD"),
	HOLD_PAYMENT_FOR_MANUAL_DISBURSEMENT(29L, "Hold Payment for manual disbursement"),
	PAYMENT_REJECTED_BY_GATEWAY(41L, "Payment Rejected By Gateway"),
	INPROCESS(0L, "others");
	

	private Long id;
	private String name;

	SellerPaymentStatusEnum(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	public static SellerPaymentStatusEnum getSellerPaymentStatusEnum(String name) {
		SellerPaymentStatusEnum list[] = SellerPaymentStatusEnum.values();
		for (int i = 0; i < list.length; i++) {
			SellerPaymentStatusEnum obj = list[i];
			if (name != null && obj.getName().equalsIgnoreCase(name)) {
				return obj;
			}
		}
		//Return inprocess for all other status
		if(name != null) {
			return SellerPaymentStatusEnum.INPROCESS;
		}
		return null;
	}
	
	public static SellerPaymentStatusEnum getSellerPaymentStatusEnum(Long id) {
		SellerPaymentStatusEnum list[] = SellerPaymentStatusEnum.values();
		for (int i = 0; i < list.length; i++) {
			SellerPaymentStatusEnum obj = list[i];
			if (id != null && obj.getId().equals(id)) {
				return obj;
			}
		}
		//Return inprocess for all other status
		if(id != null) {
			return SellerPaymentStatusEnum.INPROCESS;
		}
		return null;
	}

}
