package com.stubhub.domain.account.exception;

import com.stubhub.domain.infrastructure.common.exception.base.SHMappableException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created at 12/26/2014 9:39 AM
 *
 * @author : Caron Zhao
 * @version : 1.0.0
 * @since : PI
 */
public class SellerValidationFailedException extends RuntimeException implements SHMappableException {
    private final String sellerId;

    public SellerValidationFailedException(String sellerId) {
        this.sellerId = sellerId;
    }

    @Override
    public String getErrorCode() {
        return "accountmanagement.api.invoice.unauthorized";
    }

    @Override
    public String getDescription() {
        return "No permission to access this API as failed to validate this seller:" + sellerId + " via Solr";
    }

    @Override
    public int getStatusCode() {
        return 401;
    }

    @Override
    public Map<String, String> getData() {
        return new HashMap<String, String>();
    }
}