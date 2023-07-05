package com.stubhub.domain.account.impl;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.stubhub.common.exception.UserNotAuthorizedException;
import com.stubhub.domain.account.biz.intf.CreditMemoBO;
import com.stubhub.domain.account.common.enums.ErrorCode;
import com.stubhub.domain.account.common.enums.ErrorType;
import com.stubhub.domain.account.common.exception.AccountException;
import com.stubhub.domain.account.common.util.SecurityContextUtil;
import com.stubhub.domain.account.common.util.ValidationUtil;
import com.stubhub.domain.account.intf.CreditMemosResponse;
import com.stubhub.domain.account.intf.CreditMemosService;
import com.stubhub.domain.account.intf.IndyCreditMemosService;
import com.stubhub.domain.infrastructure.soa.core.context.SHServiceContext;
import com.stubhub.domain.inventory.common.entity.ErrorEnum;
import com.stubhub.platform.utilities.webservice.security.ExtendedSecurityContext;

@Component("creditMemosService")
public class CreditMemosServiceImpl implements CreditMemosService {

	private final static Log log = LogFactory.getLog(CreditMemosServiceImpl.class);
	private static String api_domain = "account";
	private static String api_resource = "creditMemos";
	 
	@Autowired
	private SecurityContextUtil securityContextUtil;
	
	@Autowired	
	private CreditMemoBO creditMemoBO;
	 
	@Override
	public CreditMemosResponse getCreditMemos(
			ExtendedSecurityContext securityContext, String sellerGuid,
			String sortType, String createdFromDate, String createdToDate, Integer start, Integer rows, String currencyCode) {
		CreditMemosResponse creditMemosResponse = new CreditMemosResponse();
		try{
			securityContextUtil.validateUserGuid(securityContext, sellerGuid);
			String sellerId = securityContext.getUserId();
			if(!ValidationUtil.isValidLong(sellerId)) {
				log.error("api_domain="
						+ api_domain
						+ " api_resource="+api_resource+" api_method=getCreditMemos status=error error_message=Invalid sellerId in the context"+" sellerId="
						+ sellerId);	
				creditMemosResponse.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
				creditMemosResponse.getErrors().add(new com.stubhub.domain.account.common.Error(ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT, "Invalid sellerId", sellerId));
				return creditMemosResponse;
			}

			creditMemosResponse = creditMemoBO.getSellerCreditMemos(Long.valueOf(sellerId), sortType, createdFromDate, createdToDate, start, rows, currencyCode);
			log.info("api_domain="
					+ api_domain
					+ " api_resource="
					+ api_resource
					+ " api_method=getCreditMemos status=success message=Successfully got creditMemos"+" sellerId="
					+ sellerId);	
		}catch(UserNotAuthorizedException e){			
			log.error(
					"api_domain="
							+ api_domain
							+ " api_resource="
							+ api_resource
							+ " api_method=getCreditMemos status=error error_message=UserNotAuthorizedException occured while getting creditMemos"+" sellerGuid="
							+ sellerGuid, e);			
			creditMemosResponse.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
			creditMemosResponse.getErrors().add(new com.stubhub.domain.account.common.Error(ErrorType.AUTHENTICATIONERROR, ErrorCode.INVALID_SELLER, "", ""));
			return creditMemosResponse;
        } catch(AccountException e) {			
			log.error(
					"api_domain="
							+ api_domain
							+ " api_resource="
							+ api_resource
							+ " api_method=getCreditMemos status=succes_with_error error_message=AccountException occured while getting creditMemos"+" sellerId="
							+ securityContext.getUserId(), e);			
			creditMemosResponse.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
			creditMemosResponse.getErrors().add(e.getListingError());
			return creditMemosResponse;
		} catch(Exception e) {			
			log.error(
					"api_domain="
							+ api_domain
							+ " api_resource="
							+ api_resource
							+ " api_method=getCreditMemos status=error error_message=Exception occured while getting creditMemos"+" sellerId="
							+ securityContext.getUserId(), e);			
			creditMemosResponse.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
			creditMemosResponse.getErrors().add(new com.stubhub.domain.account.common.Error(ErrorType.SYSTEMERROR, ErrorCode.SYSTEM_ERROR,ErrorEnum.SYSTEM_ERROR.getMessage(), ""));
			return creditMemosResponse;
		}	
		return creditMemosResponse;
	}



}