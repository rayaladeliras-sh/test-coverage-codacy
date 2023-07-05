/**
 * 
 */
package com.stubhub.domain.account.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.stubhub.domain.account.intf.*;

import com.stubhub.common.exception.UserNotAuthorizedException;
import com.stubhub.domain.account.biz.intf.TaxPayerBO;
import com.stubhub.domain.account.common.util.SecurityContextUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.stubhub.domain.account.helper.CustomerContactHelper;
import com.stubhub.domain.account.common.util.RSAEncryptionUtil;
import com.stubhub.domain.account.common.Error;
import com.stubhub.domain.account.common.enums.ErrorCode;
import com.stubhub.domain.account.common.enums.ErrorType;
import com.stubhub.domain.account.datamodel.dao.TaxPayerDetailsDAO;
import com.stubhub.domain.account.datamodel.dao.TinDAO;
import com.stubhub.domain.account.datamodel.entity.TaxPayerDetails;
import com.stubhub.platform.utilities.webservice.security.ExtendedSecurityContext;
import com.stubhub.domain.infrastructure.common.exception.base.SHSystemException;
import com.stubhub.domain.infrastructure.common.exception.derived.SHBadRequestException;
import com.stubhub.domain.infrastructure.common.exception.derived.SHResourceNotFoundException;
import com.stubhub.domain.user.contactsV2.intf.CustomerContactV2Details;
import com.stubhub.platform.utilities.webservice.svclocator.SvcLocator;

import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.*;

/**
 * @author vpothuru
 * date: 11/14/15
 *
 */
@Component("taxIdService")
public class TaxIdServiceImpl implements TaxIdService {
	private final static Logger LOGGER = LoggerFactory.getLogger(TaxIdServiceImpl.class);
	//log identifiers
	private static final String LOG_INFO_PREFIX = "api_domain=account, api_resource={}, api_method={}, message={}";
	private static final String LOG_ERROR_PREFIX = "api_domain=account, api_resource={}, api_method={}, error_message={}";
	
	//logging parameter values
	private String apiResource = this.getClass().getName();
	private String apiMethod = null;

	@Autowired
	@Qualifier("tinDAO")
	private TinDAO tinDAO;
	@Autowired
	@Qualifier("taxPayerDetailsDAO")
	private TaxPayerDetailsDAO taxPayerDetailsDAO;
	
	@Autowired private SvcLocator svcLocator;
	@Autowired private CustomerContactHelper customerContactHelper;
	@Autowired
	@Qualifier("rsaEncryptionUtil")
	private RSAEncryptionUtil rsaEncryptionUtil;

	@Autowired
	private SecurityContextUtil securityContextUtil;

	@Autowired
	private TaxPayerBO taxPayerBO;

	@Override
	public TaxPayerAlertStatusResponse getTaxPayerAlertStatus(String sellerGuid, ExtendedSecurityContext context) {
		TaxPayerAlertStatusResponse taxPayerAlertStatusResponse;

		try {
			securityContextUtil.validateUserGuid(context, sellerGuid);
			String userId = context.getUserId();
			taxPayerAlertStatusResponse = taxPayerBO.calculateShowTaxPayerAlert(userId);

			LOGGER.info("api_domain=account api_resource=taxpayer api_method=getTaxPayerAlertStatus status=success"
					+ " sellerGuid=" + sellerGuid);
		} catch (UserNotAuthorizedException e) {
			LOGGER.error(
					"api_domain=account api_resource=taxpayer api_method=getTaxPayerAlertStatus status=error error_message=authorization error"
							+ " sellerGuid=" + sellerGuid, e);
			TaxPayerAlertStatusResponse alertStatusResponse = new TaxPayerAlertStatusResponse();
			alertStatusResponse.setErrors(new ArrayList<Error>());
			alertStatusResponse.getErrors().add(
					new Error(ErrorType.AUTHENTICATIONERROR, ErrorCode.INVALID_SELLER, "Invalid Seller", "sellerGuid"));
			return alertStatusResponse;
		}

		return taxPayerAlertStatusResponse;
	}

	@Override
	public TaxIdShouldShowResponse shouldShowTaxId(Boolean validateNotExist, ExtendedSecurityContext context) {
		TaxIdShouldShowResponse taxIdShouldShowResponse = new TaxIdShouldShowResponse();

		try {
			securityContextUtil.validateUserGuid(context, context.getUserGuid());
			String userId = context.getUserId();
			taxIdShouldShowResponse = taxPayerBO.isTaxIdNeeded(userId, validateNotExist);

			LOGGER.info("api_domain=account api_resource=taxpayer api_method=getTaxPayerAlertStatus status=success"
							+ " userId=" + userId);
		} catch (UserNotAuthorizedException e) {
			LOGGER.error(
							"api_domain=account api_resource=taxpayer api_method=getTaxPayerAlertStatus status=error error_message=authorization error"
											+ " userId=" + context.getUserId(), e);
			taxIdShouldShowResponse.setErrors(new ArrayList<Error>());
			taxIdShouldShowResponse.getErrors().add(
							new Error(ErrorType.AUTHENTICATIONERROR, ErrorCode.INVALID_SELLER, "Invalid Seller", "sellerGuid"));
			return taxIdShouldShowResponse;
		}

		return taxIdShouldShowResponse;
	}

	@Override
	public Response addTaxId(TaxIdRequest taxIdRequest, ExtendedSecurityContext context) {
		TaxIdResponse taxIdResponse = new TaxIdResponse();
		try {
			securityContextUtil.validateUserGuid(context, context.getUserGuid());

			if (taxIdRequest == null) {
				taxIdResponse.setErrors(new ArrayList<Error>());
				taxIdResponse.getErrors().add(
								new Error(ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT, "No request payload found", "taxIdRequest"));
				return Response.status(BAD_REQUEST).entity(taxIdResponse).build();
			}

			if (taxIdRequest.getTaxId() == null || taxIdRequest.getTaxId().isEmpty()) {
				taxIdResponse.setErrors(new ArrayList<Error>());
				taxIdResponse.getErrors().add(
								new Error(ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT, "TIN/SSD can not be null or empty", "taxId"));
				return Response.status(BAD_REQUEST).entity(taxIdResponse).build();
			}

			if (taxIdRequest.getTaxId().length() < 10 || taxIdRequest.getTaxId().length() > 11) {
				taxIdResponse.setErrors(new ArrayList<Error>());
				taxIdResponse.getErrors().add(
								new Error(ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT, "TIN/SSD can not have more than 11 or less than 10", "taxId"));
				return Response.status(BAD_REQUEST).entity(taxIdResponse).build();
			}

			if (taxPayerBO.userHasTaxId(context.getUserId())) {
				taxIdResponse.setErrors(new ArrayList<Error>());
				taxIdResponse.getErrors().add(
								new Error(ErrorType.INPUTERROR, ErrorCode.INVALID_SELLER, "The seller has a TIN/SSD saved yet", "user"));
				return Response.status(BAD_REQUEST).entity(taxIdResponse).build();
			}

			taxPayerBO.createTaxId(taxIdRequest, context.getUserId());
			LOGGER.info(LOG_INFO_PREFIX, apiResource, "createTaxId","TaxId created correctly userId=" + context.getUserId());
			return Response.status(CREATED).entity(taxIdResponse).build();

		} catch (UserNotAuthorizedException e) {
			LOGGER.error(LOG_ERROR_PREFIX, apiResource, "createTaxId", "authorization error sellerGuid=" + context.getUserGuid(), e);
			taxIdResponse.setErrors(new ArrayList<Error>());
			taxIdResponse.getErrors().add(
							new Error(ErrorType.AUTHENTICATIONERROR, ErrorCode.INVALID_SELLER, "Invalid Seller", "sellerGuid"));
			return Response.status(UNAUTHORIZED).entity(taxIdResponse).build();
		} catch (Exception e) {
			LOGGER.error(LOG_ERROR_PREFIX, apiResource, "createTaxId", e);
			taxIdResponse.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
			taxIdResponse.getErrors().add(new com.stubhub.domain.account.common.Error(
							ErrorType.SYSTEMERROR, ErrorCode.SYSTEM_ERROR, "Unexpected error occurred", ""));
			return Response.status(INTERNAL_SERVER_ERROR).entity(taxIdResponse).build();
		}
	}

	@Override
	public Response getTaxId(ExtendedSecurityContext context) {
		TaxIdResponse taxIdResponse = new TaxIdResponse();
		try {
			securityContextUtil.validateUserGuid(context, context.getUserGuid());
			String userId = context.getUserId();

			if (!taxPayerBO.userHasTaxId(userId)) {
				LOGGER.error(LOG_ERROR_PREFIX, apiResource, "getTaxId", "User has not tax id saved userId=" + userId);
				taxIdResponse.setErrors(new ArrayList<Error>());
				taxIdResponse.getErrors().add(
								new Error(ErrorType.INPUTERROR, ErrorCode.INVALID_SELLER, "Invalid Seller", "sellerGuid"));
				return Response.status(BAD_REQUEST).entity(taxIdResponse).build();
			}

			taxIdResponse = taxPayerBO.getTaxId(userId);

			LOGGER.info(LOG_INFO_PREFIX, apiResource, "getTaxId","Get Tax ID userId=" + context.getUserId());
			return Response.status(OK).entity(taxIdResponse).build();
		} catch (UserNotAuthorizedException e) {
			LOGGER.error(LOG_ERROR_PREFIX, apiResource, "getTaxId", "authorization error sellerGuid=" + context.getUserGuid(), e);
			taxIdResponse.setErrors(new ArrayList<Error>());
			taxIdResponse.getErrors().add(
							new Error(ErrorType.AUTHENTICATIONERROR, ErrorCode.INVALID_SELLER, "Invalid Seller", "sellerGuid"));
			return Response.status(UNAUTHORIZED).entity(taxIdResponse).build();
		} catch (Exception e) {
			LOGGER.error(LOG_ERROR_PREFIX, apiResource, "getTaxId", e);
			taxIdResponse.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
			taxIdResponse.getErrors().add(new com.stubhub.domain.account.common.Error(
							ErrorType.SYSTEMERROR, ErrorCode.SYSTEM_ERROR, "Unexpected error occurred", ""));
			return Response.status(INTERNAL_SERVER_ERROR).entity(taxIdResponse).build();
		}
	}


	@Override
	public Response updateTaxId(TaxIdRequest taxIdRequest, ExtendedSecurityContext context) {
		TaxIdResponse taxIdResponse = new TaxIdResponse();

		if (context == null) {
			LOGGER.error(LOG_ERROR_PREFIX, apiResource, "updateTaxId", " null service context");
			throw new SHSystemException("Unexpected error, null service context");
		}

		try {
			securityContextUtil.validateUserGuid(context, context.getUserGuid());

			if (taxIdRequest == null) {
				taxIdResponse.setErrors(new ArrayList<Error>());
				taxIdResponse.getErrors().add(
								new Error(ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT, "No request payload found", "taxIdRequest"));
				return Response.status(BAD_REQUEST).entity(taxIdResponse).build();
			}

			if (taxIdRequest.getTaxId() == null || taxIdRequest.getTaxId().isEmpty()) {
				taxIdResponse.setErrors(new ArrayList<Error>());
				taxIdResponse.getErrors().add(
								new Error(ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT, "TIN/SSD can not be null or empty", "taxId"));
				return Response.status(BAD_REQUEST).entity(taxIdResponse).build();
			}

			if (taxIdRequest.getTaxId().length() < 10 || taxIdRequest.getTaxId().length() > 11) {
				taxIdResponse.setErrors(new ArrayList<Error>());
				taxIdResponse.getErrors().add(
								new Error(ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT, "TIN/SSD can not have more than 11 or less than 10", "taxId"));
				return Response.status(BAD_REQUEST).entity(taxIdResponse).build();
			}

			if (!taxPayerBO.userHasTaxId(context.getUserId())) {
				LOGGER.error(LOG_ERROR_PREFIX, apiResource, "updateTaxId", "User has not tax id saved userId=" + context.getUserId());
				taxIdResponse.setErrors(new ArrayList<Error>());
				taxIdResponse.getErrors().add(
								new Error(ErrorType.INPUTERROR, ErrorCode.INVALID_SELLER, "Invalid Seller", "sellerGuid"));
				return Response.status(BAD_REQUEST).entity(taxIdResponse).build();
			}

			taxPayerBO.updateTaxId(taxIdRequest, context.getUserId());

			LOGGER.info(LOG_INFO_PREFIX, apiResource, "updateTaxId", "Update Tax ID userId=" + context.getUserId());
			return Response.status(OK).entity(taxIdResponse).build();
		} catch (UserNotAuthorizedException e) {
			LOGGER.error(LOG_ERROR_PREFIX, apiResource, "getTaxId", "authorization error sellerGuid=" + context.getUserGuid(), e);
			taxIdResponse.setErrors(new ArrayList<Error>());
			taxIdResponse.getErrors().add(
							new Error(ErrorType.AUTHENTICATIONERROR, ErrorCode.INVALID_SELLER, "Invalid Seller", "sellerGuid"));
			return Response.status(UNAUTHORIZED).entity(taxIdResponse).build();
		} catch (Exception e) {
			LOGGER.error(LOG_ERROR_PREFIX, apiResource, "getTaxId", e);
			taxIdResponse.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
			taxIdResponse.getErrors().add(new com.stubhub.domain.account.common.Error(
							ErrorType.SYSTEMERROR, ErrorCode.SYSTEM_ERROR, "Unexpected error occurred", ""));
			return Response.status(INTERNAL_SERVER_ERROR).entity(taxIdResponse).build();
		}
	}
}
