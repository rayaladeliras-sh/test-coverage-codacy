package com.stubhub.domain.account.exception;

import com.stubhub.domain.infrastructure.common.exception.base.SHMappableException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created at 12/26/2014 9:35 AM
 *
 * @author : Caron Zhao
 * @version : 1.0.0
 * @since : PI
 */
public class SellerUnauthorizedException extends RuntimeException implements SHMappableException {
    private final String sellerId;

    public SellerUnauthorizedException(String sellerId) {
        this.sellerId = sellerId;
    }

    @Override
    public String getErrorCode() {
        return "accountmanagement.api.invoice.unauthorized";
    }

    @Override
    public String getDescription() {
        return "No permission to access this API as the payment is not owned by this seller:" + sellerId;
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