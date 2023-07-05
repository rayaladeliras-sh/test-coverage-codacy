package com.stubhub.domain.account.biz.impl;

import java.util.*;

import com.stubhub.domain.account.common.enums.PaymentUserDefinedPackedStatus;
import com.stubhub.domain.account.common.enums.PaymentUserDefinedStatus;
import com.stubhub.domain.account.datamodel.entity.SellerPaymentStatusEnum;
import com.stubhub.domain.account.datamodel.enums.PaymentsStatusEnum;
import com.stubhub.domain.account.datamodel.enums.SellerCCTransStatusEnum;

import static com.stubhub.domain.account.datamodel.enums.SellerCCTransStatusEnum.*;


public class MyaSellerPaymentStatusUtil {

	private final static Set<PaymentsStatusEnum> PENDING_SET = new HashSet<PaymentsStatusEnum>();
	private final static Set<PaymentsStatusEnum> CANCELLED_SET = new HashSet<PaymentsStatusEnum>();
	private final static Set<PaymentsStatusEnum> SENT_SET = new HashSet<PaymentsStatusEnum>();
	private final static Set<PaymentsStatusEnum> PROCESSING_SET = new HashSet<PaymentsStatusEnum>();
	private final static Set<PaymentsStatusEnum> AVAILABLE_SET = new HashSet<PaymentsStatusEnum>();
	private final static Set<PaymentsStatusEnum> UNCLAIMED_SET = new HashSet<PaymentsStatusEnum>();
	private final static Set<PaymentsStatusEnum> APPLIED_SET = new HashSet<PaymentsStatusEnum>();
	private final static Set<PaymentsStatusEnum> CHARGED_SET = new HashSet<PaymentsStatusEnum>();
	private final static Set<PaymentsStatusEnum> REJECTED_SET = new HashSet<PaymentsStatusEnum>();
	private final static Set<PaymentsStatusEnum> COMPLETED_SET = new HashSet<PaymentsStatusEnum>();
	private final static Set<PaymentsStatusEnum> DECLINED_SET = new HashSet<PaymentsStatusEnum>();
	private final static Set<PaymentsStatusEnum> FAILED_SET = new HashSet<PaymentsStatusEnum>();
	private final static Set<PaymentsStatusEnum> OPEN_SET = new HashSet<PaymentsStatusEnum>();
	private final static Set<PaymentsStatusEnum> SUCCESS_SET = new HashSet<PaymentsStatusEnum>();
	private final static Set<PaymentsStatusEnum> VOIDED_SET = new HashSet<PaymentsStatusEnum>();
	public static final String UNDEFINED = "UNDEFINED";

	static {
		PROCESSING_SET.add(SellerPaymentStatusEnum.READY_TO_PAY);
		PROCESSING_SET.add(SellerPaymentStatusEnum.PAYMENT_ASSIGNED_TO_BATCH);
		PROCESSING_SET.add(SellerPaymentStatusEnum.PAYMENT_BATCHED_TO_BE_SENT);
		PROCESSING_SET.add(SellerPaymentStatusEnum.PAYMENT_SENT_TO_GP);
		PROCESSING_SET.add(SellerPaymentStatusEnum.PAYMENT_SENT_TO_GP_BUT_GP_THREW_EXCEPTION);
		PROCESSING_SET.add(SellerPaymentStatusEnum.GP_EXECUTION_THROWN);
		PROCESSING_SET.add(SellerPaymentStatusEnum.GP_PAYMENT_SENT_TO_GATEWAY);
		PROCESSING_SET.add(SellerPaymentStatusEnum.ACCESS_IMPORT_FILE_GENERATED);
		PROCESSING_SET.add(SellerPaymentStatusEnum.PAYMENT_SENT_TO_GP_FAILED);
		
		CANCELLED_SET.add(SellerPaymentStatusEnum.PAYMENT_FAILURE_NOTIFIED);
		CANCELLED_SET.add(SellerPaymentStatusEnum.PAYMENT_CANCELLED_WITHIN_STUBHUB);
		CANCELLED_SET.add(SellerPaymentStatusEnum.GP_PAYMENT_VOIDED);
		CANCELLED_SET.add(SellerPaymentStatusEnum.VOIDED);
		CANCELLED_SET.add(SellerPaymentStatusEnum.PAYMENT_CANCELLED_BEFORE_READY_TO_PAY);
		
		SENT_SET.add(SellerPaymentStatusEnum.GP_PAYMENT_COMPLETED);
		SENT_SET.add(SellerPaymentStatusEnum.PAYMENT_NOTIFIED);
		SENT_SET.add(SellerPaymentStatusEnum.GP_PAYMENT_SENT_TO_GATEWAY);

		UNCLAIMED_SET.add(SellerPaymentStatusEnum.GP_PAYMENT_UNCLAIMED);
		UNCLAIMED_SET.add(SellerPaymentStatusEnum.PAYMENT_UNCLAIMED_NOTIFIED);

		PENDING_SET.add(SellerPaymentStatusEnum.FUND_CAPTURE);
		PENDING_SET.add(SellerPaymentStatusEnum.HOLD_PAYMENT_DUE_TO_DD);

		AVAILABLE_SET.add(SellerPaymentStatusEnum.HOLD_PAYMENT_FOR_MANUAL_DISBURSEMENT);

		APPLIED_SET.add(SellerPaymentStatusEnum.READY_TO_SEND_CM);
		APPLIED_SET.add(SellerPaymentStatusEnum.CM_ASSIGNED_TO_BATCH);
		APPLIED_SET.add(SellerPaymentStatusEnum.CM_BATCHED_TO_BE_SEND);
		APPLIED_SET.add(SellerPaymentStatusEnum.CM_SENT_TO_GP_BUT_GP_THREW_EXCEPTION);
		APPLIED_SET.add(SellerPaymentStatusEnum.CM_SEND_TO_GP);
		APPLIED_SET.add(SellerPaymentStatusEnum.CM_SEND_TO_GP_FAILED);
		APPLIED_SET.add(SellerPaymentStatusEnum.CM_EXECUTION_THROWN);

		CHARGED_SET.add(SellerPaymentStatusEnum.COMPLETED);

		REJECTED_SET.add(SellerPaymentStatusEnum.PAYMENT_REJECTED_BY_GATEWAY);
		REJECTED_SET.add(SellerCCTransStatusEnum.GATEWAY_REJECTED);

		SUCCESS_SET.add(SellerCCTransStatusEnum.SUCCESS);

		DECLINED_SET.add(SellerCCTransStatusEnum.DECLINED);

		FAILED_SET.add(SellerCCTransStatusEnum.FAILED);
		FAILED_SET.add(FAILED_AVS);
		FAILED_SET.add(FAILED_CARD);
		FAILED_SET.add(SellerCCTransStatusEnum.FAILED_GATEWAY);
		FAILED_SET.add(FAILED_REFUND);
	}

	public static String getMyaPackedStatusBySellerPaymentsStatusName(String sellerPaymentStatus) {
		String getMyaPayStatus = getMyaSellerPaymentStatus(sellerPaymentStatus);
		PaymentUserDefinedStatus paymentUserDefinedStatus = PaymentUserDefinedStatus.getEnum(getMyaPayStatus);
		if (paymentUserDefinedStatus != null) {
			switch (paymentUserDefinedStatus) {
				case PENDING:
				case UNCLAIMED:
					return PaymentUserDefinedPackedStatus.PENDING.getName();
				case SENT:
					return PaymentUserDefinedPackedStatus.PAYOUT.getName();
				case CANCELLED:
				case REJECTED:
					return PaymentUserDefinedPackedStatus.CANCELLED.getName();
				case CHARGED:
					return PaymentUserDefinedPackedStatus.CHARGES.getName();
				case APPLIED:
				case AVAILABLE:
				case PROCESSING:
				default:
					return PaymentUserDefinedPackedStatus.PROCESSING.getName();
			}
		} else {
			return UNDEFINED;
		}
	}

	public static String getMyaSellerPaymentStatus(String sellerPaymentStatus) {
		long sellerPaymentStatusId = SellerPaymentStatusEnum.getSellerPaymentStatusEnum(sellerPaymentStatus).getId();
		return getMyaSellerPaymentStatus(sellerPaymentStatusId);
	}

	public static String getMyaSellerPaymentStatus(Long sellerPaymentStatusId) {
		SellerPaymentStatusEnum sellerPaymentStatusEnum = SellerPaymentStatusEnum.getSellerPaymentStatusEnum(sellerPaymentStatusId);
		if (sellerPaymentStatusEnum == null) {
			return "";
		}
		
		if (PENDING_SET.contains(sellerPaymentStatusEnum)) {
			return PaymentUserDefinedStatus.PENDING.getName();
		} else if (CANCELLED_SET.contains(sellerPaymentStatusEnum)) {
			return PaymentUserDefinedStatus.CANCELLED.getName();
		} else if (PROCESSING_SET.contains(sellerPaymentStatusEnum)) {
			return PaymentUserDefinedStatus.PROCESSING.getName();
		} else if (AVAILABLE_SET.contains(sellerPaymentStatusEnum)) {
			return PaymentUserDefinedStatus.AVAILABLE.getName();
		} else if (SENT_SET.contains(sellerPaymentStatusEnum)) {
			return PaymentUserDefinedStatus.SENT.getName();
		} else if (UNCLAIMED_SET.contains(sellerPaymentStatusEnum)) {
			return PaymentUserDefinedStatus.UNCLAIMED.getName();
		} else if (APPLIED_SET.contains(sellerPaymentStatusEnum)) {
			return PaymentUserDefinedStatus.APPLIED.getName();
		} else if (CHARGED_SET.contains(sellerPaymentStatusEnum)) {
			return PaymentUserDefinedStatus.CHARGED.getName();
		} else if (REJECTED_SET.contains(sellerPaymentStatusEnum)) {
			return PaymentUserDefinedStatus.REJECTED.getName();
		}
		
		return "";
	}

	public static List<String> getSellerPaymentsStatusNameByMyaPackedStatus(String name) {
		PaymentUserDefinedPackedStatus paymentUserDefinedPackedStatus = PaymentUserDefinedPackedStatus.getEnum(name);
		List<String> sellerPaymentsStatusName = new ArrayList<>();
		if (paymentUserDefinedPackedStatus != null) {
			Set<PaymentsStatusEnum> paymentStatusEnums = new HashSet<>();
			switch (paymentUserDefinedPackedStatus) {
				case PAYOUT:
					paymentStatusEnums.addAll(SENT_SET);
					break;
				case PENDING:
					paymentStatusEnums.addAll(PENDING_SET);
					paymentStatusEnums.addAll(UNCLAIMED_SET);
					break;
				case CANCELLED:
					paymentStatusEnums.addAll(CANCELLED_SET);
					paymentStatusEnums.addAll(REJECTED_SET);
					break;
				case CHARGES:
					paymentStatusEnums.addAll(CHARGED_SET);
					paymentStatusEnums.addAll(COMPLETED_SET);
					paymentStatusEnums.addAll(FAILED_SET);
					paymentStatusEnums.addAll(OPEN_SET);
					paymentStatusEnums.addAll(SUCCESS_SET);
					paymentStatusEnums.addAll(VOIDED_SET);
					paymentStatusEnums.addAll(DECLINED_SET);
					break;
				case PROCESSING:
					paymentStatusEnums.addAll(APPLIED_SET);
					paymentStatusEnums.addAll(AVAILABLE_SET);
					paymentStatusEnums.addAll(PROCESSING_SET);
					break;
			}
			for (PaymentsStatusEnum paymentStatusEnum: paymentStatusEnums) {
				sellerPaymentsStatusName.add(paymentStatusEnum.getName());
			}
		}
		return sellerPaymentsStatusName;
	}
	
	public static List<Long> getSellerPaymentStatusIdsByStatusName(String name){
		List<Long> sellerPaymentStatusIds = new ArrayList<Long>();
		if(PaymentUserDefinedStatus.PENDING.getName().equalsIgnoreCase(name)){
			sellerPaymentStatusIds = addSellerPaymentStatusIds(PENDING_SET);
		} else if (PaymentUserDefinedStatus.CANCELLED.getName().equalsIgnoreCase(name)) {
			sellerPaymentStatusIds = addSellerPaymentStatusIds(CANCELLED_SET);
		} else if (PaymentUserDefinedStatus.SENT.getName().equalsIgnoreCase(name)) {
			sellerPaymentStatusIds = addSellerPaymentStatusIds(SENT_SET);
		} else if (PaymentUserDefinedStatus.PROCESSING.getName().equalsIgnoreCase(name)) {
			sellerPaymentStatusIds = addSellerPaymentStatusIds(PROCESSING_SET);
		} else if (PaymentUserDefinedStatus.AVAILABLE.getName().equalsIgnoreCase(name)) {
			sellerPaymentStatusIds = addSellerPaymentStatusIds(AVAILABLE_SET);
		} else if (PaymentUserDefinedStatus.UNCLAIMED.getName().equalsIgnoreCase(name)) {
			sellerPaymentStatusIds = addSellerPaymentStatusIds(UNCLAIMED_SET);
		} else if (PaymentUserDefinedStatus.APPLIED.getName().equalsIgnoreCase(name)) {
			sellerPaymentStatusIds = addSellerPaymentStatusIds(APPLIED_SET);
		} else if (PaymentUserDefinedStatus.CHARGED.getName().equalsIgnoreCase(name)) {
			sellerPaymentStatusIds = addSellerPaymentStatusIds(CHARGED_SET);
		} else if (PaymentUserDefinedStatus.REJECTED.getName().equalsIgnoreCase(name)) {
			sellerPaymentStatusIds = addSellerPaymentStatusIds(REJECTED_SET);
		}
		return sellerPaymentStatusIds;
		
	}
	
	private static List<Long> addSellerPaymentStatusIds(
			Set<PaymentsStatusEnum> SellerPaymentStatusSet) {
		List<Long> sellerPaymentStatusIds = new ArrayList<Long>();
		for (PaymentsStatusEnum sellerPaymentStatus : SellerPaymentStatusSet) {
			sellerPaymentStatusIds.add(sellerPaymentStatus.getId());
		}
		return sellerPaymentStatusIds;
	}

	public static String getMyaPackedStatusBySellerCCTransStatusName(String status) {
		return PaymentUserDefinedPackedStatus.CHARGES.getName();
	}

	public static String getMyaSellerCCTransStatus(String status) {
		SellerCCTransStatusEnum sellerCCTransStatusEnum = SellerCCTransStatusEnum.getSellerCCTransStatusEnum(status);
		if (sellerCCTransStatusEnum == null) {
			return "";
		}

		switch (sellerCCTransStatusEnum) {
			case FAILED_AVS:
			case FAILED_CARD:
			case FAILED_REFUND:
			case FAILED_GATEWAY:
			case GATEWAY_REJECTED:
			case FAILED:
				return FAILED.getName();
			case SUCCESS:
				return SUCCESS.getName();
			case DECLINED:
				return DECLINED.getName();
			default:
				return INPROCESS.getName();
		}

	}

	public static String getMyaPackedStatusByStatusName(String status) {
		String statusSellerPayments = getMyaPackedStatusBySellerPaymentsStatusName(status);
		if (UNDEFINED.equals(statusSellerPayments)) {
			return getMyaPackedStatusBySellerCCTransStatusName(status);
		}
		return statusSellerPayments;
	}
}
