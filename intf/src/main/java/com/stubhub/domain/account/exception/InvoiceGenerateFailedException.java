package com.stubhub.domain.account.exception;

import com.stubhub.domain.infrastructure.common.exception.base.SHMappableException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created at 12/30/2014 7:47 AM
 *
 * @author : Caron Zhao
 * @version : 1.0.0
 * @since : PI
 */
public class InvoiceGenerateFailedException extends RuntimeException implements SHMappableException {
    @Override
    public String getErrorCode() {
        return "accountmanagement.api.invoice.generate.failure";
    }

    @Override
    public String getDescription() {
        return "Unexpected error occured when generating invoice";
    }

    @Override
    public int getStatusCode() {
        return 500;
    }

    @Override
    public Map<String, String> getData() {
        return new HashMap<String, String>();
    }
}