package com.stubhub.domain.account.exception;

import com.stubhub.domain.infrastructure.common.exception.base.SHMappableException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created at 12/26/2014 9:25 AM
 *
 * @author : Caron Zhao
 * @version : 1.0.0
 * @since : PI
 */
public class UnauthorizedException extends RuntimeException implements SHMappableException {

    @Override
    public String getErrorCode() {
        return "accountmanagement.api.unauthorized";
    }

    @Override
    public String getDescription() {
        return "No permission to access this API";
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