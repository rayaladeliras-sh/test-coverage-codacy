package com.stubhub.domain.account.common.exception;

import java.util.HashMap;
import java.util.Map;

import com.stubhub.domain.infrastructure.common.exception.base.SHMappableException;

public class DisbursementOptionException extends RuntimeException implements
		SHMappableException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7089128971205884156L;
	private Map<String, String> data = new HashMap<String, String>();

	@Override
	public Map<String, String> getData() {
		return data;
	}

	@Override
	public String getDescription() {
		return "disbursement option is not manual";
	}

	@Override
	public String getErrorCode() {
		return "accountmanagement.payments.invalidDisbursementOption";
	}

	@Override
	public int getStatusCode() {
		return 409;
	}

}
