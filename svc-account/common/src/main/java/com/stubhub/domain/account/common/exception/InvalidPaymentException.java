package com.stubhub.domain.account.common.exception;

import java.util.HashMap;
import java.util.Map;

import com.stubhub.domain.infrastructure.common.exception.base.SHMappableException;

public class InvalidPaymentException extends RuntimeException implements
		SHMappableException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8167699857667644814L;
	private Map<String, String> data = new HashMap<String, String>();

	@Override
	public Map<String, String> getData() {
		return data;
	}

	@Override
	public String getDescription() {
		return "Payment id is invalid or not exist";
	}

	@Override
	public String getErrorCode() {
		return "accountmanagement.payments.invalidPayment";
	}

	@Override
	public int getStatusCode() {
		return 404;
	}

}
