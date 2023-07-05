package com.stubhub.domain.account.impl;

import com.stubhub.common.exception.UserNotAuthorizedException;
import com.stubhub.domain.account.common.Error;
import com.stubhub.domain.account.common.enums.ErrorCode;
import com.stubhub.domain.account.common.enums.ErrorType;
import com.stubhub.domain.account.common.util.SecurityContextUtil;
import com.stubhub.domain.account.datamodel.dao.BrokerLicenseDAO;
import com.stubhub.domain.account.datamodel.entity.BrokerLicense;
import com.stubhub.domain.account.datamodel.entity.BrokerLicenseSuccessEntity;
import com.stubhub.domain.account.intf.BrokerLicenseRequest;
import com.stubhub.domain.account.intf.BrokerLicenseResponse;
import com.stubhub.domain.account.intf.BrokerLicenseService;
import com.stubhub.newplatform.persistence.adapter.UTCCalendarToTimestampAdapter;
import com.stubhub.platform.utilities.webservice.security.ExtendedSecurityContext;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static javax.ws.rs.core.Response.Status.*;


/**
 * Created by mengli on 11/13/18.
 */
@Component("brokerLicenseService")
public class BrokerLicenseServiceImpl implements BrokerLicenseService {

    private final static Logger log = LoggerFactory.getLogger(BrokerLicenseServiceImpl.class);

    private static final String LOG_INFO_PREFIX = "api_domain=account, api_resource={}, api_method={}, message={}";
    private static final String LOG_ERROR_PREFIX = "api_domain=account, api_resource={}, api_method={}, error_message={}";

    private String apiResource = this.getClass().getName();

    @Autowired
    private SecurityContextUtil securityContextUtil;

    @Autowired
    private BrokerLicenseDAO brokerLicenseDAO;

    @Override
    public Response getBrokerLicenses(String sellerGUID, ExtendedSecurityContext context) {

        BrokerLicenseResponse brokerLicenseResponse;

        if(StringUtils.isEmpty(sellerGUID)) {
            log.error(LOG_ERROR_PREFIX, apiResource, "getBrokerLicense", "Seller Guid should not be null or empty");
            brokerLicenseResponse = new BrokerLicenseResponse();
            brokerLicenseResponse.setErrors(new ArrayList<Error>());
            brokerLicenseResponse.getErrors().add(
                    new Error(ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT, "Seller Guid should not be null or empty", "sellerGuid"));
            return Response.status(BAD_REQUEST).entity(brokerLicenseResponse).build();
        }

        List<BrokerLicense> brokerLicenses = brokerLicenseDAO.getBrokerLicensesBySellerGuid(sellerGUID);
        brokerLicenseResponse = BrokerLicenseResponse.builder().brokerLicenses(brokerLicenses).build();
        return Response.status(OK).entity(brokerLicenseResponse).build();
    }

    @Override
    public Response createBrokerLicense(String sellerGUID, BrokerLicenseRequest brokerLicenseRequest, ExtendedSecurityContext context) {
        BrokerLicenseResponse brokerLicenseResponse;
        try {
            securityContextUtil.validateUserGuid(context, sellerGUID);

            if(brokerLicenseRequest == null) {
                brokerLicenseResponse = new BrokerLicenseResponse();
                brokerLicenseResponse.setErrors(new ArrayList<Error>());
                brokerLicenseResponse.getErrors().add(
                        new Error(ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT, "No request payload found", ""));
                return Response.status(BAD_REQUEST).entity(brokerLicenseResponse).build();
            }

            if(brokerLicenseRequest.getBrokerLicenseNumber() == null) {
                brokerLicenseResponse = new BrokerLicenseResponse();
                brokerLicenseResponse.setErrors(new ArrayList<Error>());
                brokerLicenseResponse.getErrors().add(
                        new Error(ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT, "brokerLicenseNumber can't be null", "brokerLicenseNumber"));
                return Response.status(BAD_REQUEST).entity(brokerLicenseResponse).build();
            }

            if(brokerLicenseRequest.getCountryCode() == null) {
                brokerLicenseResponse = new BrokerLicenseResponse();
                brokerLicenseResponse.setErrors(new ArrayList<Error>());
                brokerLicenseResponse.getErrors().add(
                        new Error(ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT, "country can't be null", "country"));
                return Response.status(BAD_REQUEST).entity(brokerLicenseResponse).build();
            }

            if(brokerLicenseRequest.getStateCode() == null) {
                brokerLicenseResponse = new BrokerLicenseResponse();
                brokerLicenseResponse.setErrors(new ArrayList<Error>());
                brokerLicenseResponse.getErrors().add(
                        new Error(ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT, "state can't be null", "state"));
                return Response.status(BAD_REQUEST).entity(brokerLicenseResponse).build();
            }

            BrokerLicense brokerLicense = BrokerLicense.builder().brokerLicenseNumber(brokerLicenseRequest.getBrokerLicenseNumber()).active(1).sellerId(context.getUserId()).sellerGuid(sellerGUID)
                    .countryCode(brokerLicenseRequest.getCountryCode()).stateCode(brokerLicenseRequest.getStateCode()).createdBy(context.getUserId()).createdOn(UTCCalendarToTimestampAdapter.getNewUTCCalendarInstanceStatic())
                    .lastModifiedBy(context.getUserId()).lastModifiedOn(UTCCalendarToTimestampAdapter.getNewUTCCalendarInstanceStatic()).build();

            if(brokerLicense.getBrokerLicenseNumber().trim().length() > 20) {
                brokerLicenseResponse = new BrokerLicenseResponse();
                brokerLicenseResponse.setErrors(new ArrayList<Error>());
                brokerLicenseResponse.getErrors().add(
                        new Error(ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT, "Invalid broker license number, maximum size is 20", "brokerLicenseNumber"));
                return Response.status(BAD_REQUEST).entity(brokerLicenseResponse).build();
            }

            if(brokerLicenseRequest.getBrokerLicenseNumber().trim().isEmpty()) {
                brokerLicense.setActive(0);
            }

            long brokerLicenseInfoId = brokerLicenseDAO.saveBrokerLicense(brokerLicense);

            BrokerLicenseSuccessEntity successEntity = BrokerLicenseSuccessEntity.builder().userBrokerLicenseId(brokerLicenseInfoId).message("Create broker license successfully").build();
            return Response.status(CREATED).entity(successEntity).build();

        } catch (UserNotAuthorizedException e) {
            log.error(LOG_ERROR_PREFIX, apiResource, "createBrokerLicense", "authorization error sellerGuid=" + sellerGUID, e);
            brokerLicenseResponse = new BrokerLicenseResponse();
            brokerLicenseResponse.setErrors(new ArrayList<Error>());
            brokerLicenseResponse.getErrors().add(
                    new Error(ErrorType.AUTHENTICATIONERROR, ErrorCode.INVALID_SELLER, "Invalid Seller", "sellerGuid"));
            return Response.status(UNAUTHORIZED).entity(brokerLicenseResponse).build();
        }
    }

    @Override
    public Response updateBrokerLicense(String sellerGUID, BrokerLicenseRequest brokerLicenseRequest, ExtendedSecurityContext context) {
        BrokerLicenseResponse brokerLicenseResponse;

        try {
            securityContextUtil.validateUserGuid(context, sellerGUID);

            if(brokerLicenseRequest == null) {
                brokerLicenseResponse = new BrokerLicenseResponse();
                brokerLicenseResponse.setErrors(new ArrayList<Error>());
                brokerLicenseResponse.getErrors().add(
                        new Error(ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT, "No request payload found", ""));
                return Response.status(BAD_REQUEST).entity(brokerLicenseResponse).build();
            }

            if(brokerLicenseRequest.getBrokerLicenseNumber() == null) {
                brokerLicenseResponse = new BrokerLicenseResponse();
                brokerLicenseResponse.setErrors(new ArrayList<Error>());
                brokerLicenseResponse.getErrors().add(
                        new Error(ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT, "brokerLicenseNumber can't be null", "brokerLicenseNumber"));
                return Response.status(BAD_REQUEST).entity(brokerLicenseResponse).build();
            }

            if(brokerLicenseRequest.getCountryCode() == null) {
                brokerLicenseResponse = new BrokerLicenseResponse();
                brokerLicenseResponse.setErrors(new ArrayList<Error>());
                brokerLicenseResponse.getErrors().add(
                        new Error(ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT, "country can't be null", "country"));
                return Response.status(BAD_REQUEST).entity(brokerLicenseResponse).build();
            }

            if(brokerLicenseRequest.getStateCode() == null) {
                brokerLicenseResponse = new BrokerLicenseResponse();
                brokerLicenseResponse.setErrors(new ArrayList<Error>());
                brokerLicenseResponse.getErrors().add(
                        new Error(ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT, "state can't be null", "state"));
                return Response.status(BAD_REQUEST).entity(brokerLicenseResponse).build();
            }

            if(brokerLicenseRequest.getBrokerLicenseNumber().trim().length() > 20) {
                brokerLicenseResponse = new BrokerLicenseResponse();
                brokerLicenseResponse.setErrors(new ArrayList<Error>());
                brokerLicenseResponse.getErrors().add(
                        new Error(ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT, "Invalid broker license number, maximum size is 20", "brokerLicenseNumber"));
                return Response.status(BAD_REQUEST).entity(brokerLicenseResponse).build();
            }

            if(brokerLicenseRequest.getBrokerLicenseNumber().trim().isEmpty()) {
                brokerLicenseRequest.setActive(0);
            }

            BrokerLicense brokerLicense = brokerLicenseDAO.getBrokerLicense(brokerLicenseRequest.getUserBrokerLicenseId());
            if(brokerLicense == null) {
                brokerLicenseResponse = new BrokerLicenseResponse();
                brokerLicenseResponse.setErrors(new ArrayList<Error>());
                brokerLicenseResponse.getErrors().add(
                        new Error(ErrorType.NOT_FOUND, ErrorCode.INVALID_INPUT, "Broker license can't be found", "userBrokerLicenseId"));
                return Response.status(BAD_REQUEST).entity(brokerLicenseResponse).build();
            }

            if(brokerLicenseRequest.getActive() == null) {
                brokerLicenseRequest.setActive(1);
            }

            brokerLicenseDAO.updateBrokerLicenseBySellerGuid(sellerGUID, brokerLicenseRequest.getBrokerLicenseNumber(), brokerLicenseRequest.getStateCode(), brokerLicenseRequest.getCountryCode(), brokerLicenseRequest.getActive(), brokerLicenseRequest.getUserBrokerLicenseId());

            log.debug(LOG_INFO_PREFIX, apiResource, "updateBrokerLicense", "updated broker license sellerGuid= " + sellerGUID);

            BrokerLicenseSuccessEntity successEntity = BrokerLicenseSuccessEntity.builder().userBrokerLicenseId(brokerLicenseRequest.getUserBrokerLicenseId())
                    .message("Update broker license successfully").build();

            return Response.status(OK).entity(successEntity).build();

        } catch (UserNotAuthorizedException e) {
            log.error(LOG_ERROR_PREFIX, apiResource, "updateBrokerLicense", "authorization error sellerGuid=" + sellerGUID, e);
            brokerLicenseResponse = new BrokerLicenseResponse();
            brokerLicenseResponse.setErrors(new ArrayList<Error>());
            brokerLicenseResponse.getErrors().add(
                    new Error(ErrorType.AUTHENTICATIONERROR, ErrorCode.INVALID_SELLER, "Invalid Seller", "sellerGuid"));
            return Response.status(UNAUTHORIZED).entity(brokerLicenseResponse).build();
        }
    }
}
