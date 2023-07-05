package com.stubhub.domain.account.common.exception;

import java.util.HashMap;
import java.util.Map;

import com.stubhub.domain.infrastructure.common.exception.base.SHMappableException;

public class PaymentStatusException extends RuntimeException implements
		SHMappableException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7382101867021089775L;
	private Map<String, String> data = new HashMap<String, String>();

	@Override
	public Map<String, String> getData() {
		return data;
	}

	@Override
	public String getDescription() {
		return "Payment status is not correct";
	}

	@Override
	public String getErrorCode() {
		return "accountmanagement.payments.invalidStatus";
	}

	@Override
	public int getStatusCode() {
		return 409;
	}

}
