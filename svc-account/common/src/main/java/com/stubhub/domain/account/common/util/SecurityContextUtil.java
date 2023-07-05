/**
 * © 2012 StubHub, Inc.  All rights reserved.
 */
package com.stubhub.domain.account.common.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.stubhub.common.exception.Error;
import com.stubhub.common.exception.ErrorConstants;
import com.stubhub.common.exception.ErrorDescription;
import com.stubhub.common.exception.ErrorType;
import com.stubhub.common.exception.UserNotAuthorizedException;
import com.stubhub.domain.infrastructure.soa.core.context.SHServiceContext;
import com.stubhub.platform.utilities.webservice.security.ExtendedSecurityContext;

@Component("securityContextUtil")
public class SecurityContextUtil {

    public void validateUserGuid(ExtendedSecurityContext securityContext, String userGuid) throws UserNotAuthorizedException {
        if ((securityContext == null) || (userGuid == null) || (securityContext.getUserGuid() == null) || (securityContext.getUserId() == null) ||
                (securityContext.getUserName() == null)) {

            Error userError = new Error(ErrorType.AUTHENTICATIONERROR, ErrorDescription.USER_NOT_AUTHORIZED, "customerId is NULL or CustomerId doesnt Match the Authenticated Users customerId Or UserId is NULL",
                    "customerId", ErrorConstants.USER_NOT_AUTHORIZED);
            throw new UserNotAuthorizedException(userError);
        }
        if (!(securityContext.getUserGuid().equals(userGuid))) {
            Error ccError = new Error(ErrorType.AUTHENTICATIONERROR, ErrorDescription.USER_NOT_AUTHORIZED, "CustomerId doesnt Match the Authenticated Users customerId",
                    "customerId", ErrorConstants.USER_NOT_AUTHORIZED);
            throw new UserNotAuthorizedException(ccError);
        }
    }

    public void authenticateUser(SHServiceContext serviceContext, ExtendedSecurityContext securityContext)
            throws UserNotAuthorizedException {
        if (serviceContext == null || serviceContext.getRole() == null)
            validateUserGuid(securityContext, securityContext.getUserGuid());
    }

    public boolean isAuthz(SHServiceContext securityContext) {
        String operatorRole = securityContext.getAttribute(SHServiceContext.ATTR_ROLE);
        String userGuid = securityContext.getProxiedId();
        return !StringUtils.isEmpty(operatorRole) && !StringUtils.isEmpty(userGuid);
    }

}

