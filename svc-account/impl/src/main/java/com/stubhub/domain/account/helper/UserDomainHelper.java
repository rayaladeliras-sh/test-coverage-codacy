/**
 * 
 */
package com.stubhub.domain.account.helper;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.ResponseReader;
import org.apache.cxf.jaxrs.client.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.stubhub.domain.infrastructure.common.core.monitor.SHMonitor;
import com.stubhub.domain.infrastructure.common.core.monitor.SHMonitorFactory;
import com.stubhub.domain.infrastructure.common.exception.base.SHSystemException;
import com.stubhub.domain.infrastructure.common.exception.derived.SHBadRequestException;
import com.stubhub.domain.infrastructure.common.exception.derived.SHResourceNotFoundException;
import com.stubhub.domain.infrastructure.common.exception.derived.SHUnauthorizedException;
import com.stubhub.domain.user.usergroup.intf.MembershipResponse;
import com.stubhub.newplatform.property.MasterStubHubProperties;
import com.stubhub.platform.utilities.webservice.svclocator.SvcLocator;

/**
 * @author yunpli
 *
 */
@Component("userDomainHelper")
public class UserDomainHelper {
    @Autowired
    private SvcLocator svcLocator;
    
    private final static Logger log = LoggerFactory.getLogger(UserDomainHelper.class);
    private static final String USER_GROUP_MEMBERSHIP_API_URL = "user.group.membership.v1.url";
    private static final String USER_GROUP_MEMBERSHIP_API_URL_DEFAULT = "https://api.stubhub.com/user/customers/v1/{userGuid}/usergroup/{userGroupId}/member";
    
    // log identifiers
    private static final String LOG_URI_PREFIX = "api_domain=account, api_resource={}, api_method={}, api_uri={}";
    private static final String LOG_INFO_PREFIX = "api_domain=account, api_resource={}, api_method={}, input_param={} message={}";
    private static final String LOG_ERROR_PREFIX = "api_domain=account, api_resource={}, api_method={}, "
            + "input_param={}, response status={}, error_message={}, time_msec={}";
    
    // logging parameter values
    private String apiResource = this.getClass().getName();
    private String apiMethod = null;
    private String inputParam;
    
    private static final List<ResponseReader> repsonseReaderList = new ArrayList<ResponseReader>();
    {
        repsonseReaderList.add(new ResponseReader(MembershipResponse.class));
    }
    public boolean isUserMemberOfGroup(String userGUID, long groupInternalID) throws Exception {
        
        inputParam = "guid: " + userGUID + " group id: " + groupInternalID;
        
        // api response objects
        SHMonitor mon = SHMonitorFactory.getMonitor().start();
        apiMethod = "isUserMemberOfGroup";
        MembershipResponse result = null;
        try {
            String ccURL = getProperty(USER_GROUP_MEMBERSHIP_API_URL,
                    USER_GROUP_MEMBERSHIP_API_URL_DEFAULT);
            ccURL = ccURL.replace("{userGuid}", userGUID);
            ccURL = ccURL.replace("{userGroupId}", String.valueOf(groupInternalID));
            
            log.info(LOG_URI_PREFIX, apiResource, apiMethod, ccURL);
            
            

            WebClient webClient = svcLocator.locate(ccURL, repsonseReaderList);
            webClient.accept(MediaType.APPLICATION_JSON);
            // invoke request and get response
            Response response = webClient.get();
            
            if (Response.Status.OK.getStatusCode() == response.getStatus()) {
                result = (MembershipResponse) response.getEntity();
            } else if (Response.Status.BAD_REQUEST.getStatusCode() == response.getStatus()) {
                log.error(LOG_ERROR_PREFIX, apiResource, apiMethod, inputParam,
                        response.getStatus(), "Invalid request", mon.getTime());
                throw new SHBadRequestException(); // 400
            } else if (Response.Status.UNAUTHORIZED.getStatusCode() == response.getStatus()) {
                log.error(LOG_ERROR_PREFIX, apiResource, apiMethod, inputParam,
                        response.getStatus(), "Not authorized to access this end point",
                        mon.getTime());
                throw new SHUnauthorizedException(
                        "Not authorized to access this end point: " + userGUID); // 401
            } else if (Response.Status.NOT_FOUND.getStatusCode() == response.getStatus()) {
                log.error(LOG_ERROR_PREFIX, apiResource, apiMethod, inputParam,
                        response.getStatus(), "not found status", mon.getTime());
                throw new SHResourceNotFoundException(
                        "user group does not exists: " + groupInternalID); // 404
            } else {
                log.error(LOG_ERROR_PREFIX, apiResource, apiMethod, inputParam,
                        response.getStatus(), "unexpected status", mon.getTime());
                throw new SHSystemException(); // 500
            }
        } catch (SHResourceNotFoundException resourceNotFound) {
            throw resourceNotFound;
        } catch (Exception ex) {
            log.error(LOG_ERROR_PREFIX, apiResource, apiMethod, inputParam, "unknown exception",
                    ex.getMessage(), "");
            throw ex;
        } finally {
            mon.stop();
            log.info(LOG_INFO_PREFIX, apiResource, apiMethod, inputParam,
                    "excution time:" + mon.getTime());
        }
        
        return result.isMember();
    }
    
    protected String getProperty(String propertyName, String defaultValue) {
        return MasterStubHubProperties.getProperty(propertyName, defaultValue);
    }
}
