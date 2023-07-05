package com.stubhub.domain.account.exception;

import com.stubhub.domain.infrastructure.common.exception.base.SHMappableException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created at 12/26/2014 9:42 AM
 *
 * @author : Caron Zhao
 * @version : 1.0.0
 * @since : PI
 */
public class InvoiceNotFoundException extends RuntimeException implements SHMappableException {
    private final String referenceNumber;

    public InvoiceNotFoundException(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    @Override
    public String getErrorCode() {
        return "accountmanagement.api.invoice.notfound";
    }

    @Override
    public String getDescription() {
        return "No invoice found for this reference number:" + referenceNumber;
    }

    @Override
    public int getStatusCode() {
        return 404;
    }

    @Override
    public Map<String, String> getData() {
        return new HashMap<String, String>();
    }
}