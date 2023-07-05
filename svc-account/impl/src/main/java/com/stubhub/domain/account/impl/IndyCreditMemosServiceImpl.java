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
import com.stubhub.domain.account.intf.CreditMemosResponse;
import com.stubhub.domain.account.intf.IndyCreditMemosService;
import com.stubhub.domain.infrastructure.soa.core.context.SHServiceContext;
import com.stubhub.domain.inventory.common.entity.ErrorEnum;
import com.stubhub.platform.utilities.webservice.security.ExtendedSecurityContext;

@Component("indyCreditMemosService")
public class IndyCreditMemosServiceImpl implements IndyCreditMemosService {
	
	private final static Log log = LogFactory.getLog(IndyCreditMemosServiceImpl.class);
	private static String api_domain = "account";
	private static String api_resource = "creditMemos";
	 
	@Autowired
	private SecurityContextUtil securityContextUtil;
	
	@Autowired	
	private CreditMemoBO creditMemoBO;
	
	@Override
	public CreditMemosResponse getIndySellersCreditMemos(
			SHServiceContext serviceContext,
			ExtendedSecurityContext securityContext,
			 String createdFromDate, String createdToDate) {
		CreditMemosResponse creditMemosResponse = new CreditMemosResponse();
		try{
			

				if(securityContext == null || securityContext.getUserId() == null) {
					log.error("getSellersCreditMemos - Unauthorized access, UserId=" + securityContext.getUserId());
					creditMemosResponse.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
					creditMemosResponse.getErrors().add(new com.stubhub.domain.account.common.Error(ErrorType.AUTHENTICATIONERROR, ErrorCode.USER_NOT_AUTHORIZED, "", ""));
				
					return creditMemosResponse;
					}
				
					securityContextUtil.authenticateUser(serviceContext, securityContext);
			        creditMemosResponse = creditMemoBO.getSellersCreditMemos(createdFromDate,createdToDate);
			log.info("api_domain="
					+ api_domain
					+ " api_resource="
					+ api_resource
					+ " api_method=getCreditMemos status=success message=Successfully got IndySellersCreditMemos for Sellers");	
		}catch(UserNotAuthorizedException e){			
			log.error(
					"api_domain="
							+ api_domain
							+ " api_resource="
							+ api_resource
							+ " api_method=getCreditMemos status=error error_message=UserNotAuthorizedException occured while getting getIndySellersCreditMemos"
							, e);			
			creditMemosResponse.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
			creditMemosResponse.getErrors().add(new com.stubhub.domain.account.common.Error(ErrorType.AUTHENTICATIONERROR, ErrorCode.USER_NOT_AUTHORIZED, "", ""));
			return creditMemosResponse;
        } catch(AccountException e) {			
			log.error(
					"api_domain="
							+ api_domain
							+ " api_resource="
							+ api_resource
							+ " api_method=getCreditMemos status=succes_with_error error_message=AccountException occured while getting getIndySellersCreditMemos"
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
							+ " api_method=getCreditMemos status=error error_message=Exception occured while getting getIndySellersCreditMemos"
							+ securityContext.getUserId(), e);			
			creditMemosResponse.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
			creditMemosResponse.getErrors().add(new com.stubhub.domain.account.common.Error(ErrorType.SYSTEMERROR, ErrorCode.SYSTEM_ERROR,ErrorEnum.SYSTEM_ERROR.getMessage(), ""));
			return creditMemosResponse;
		}	
		return creditMemosResponse;
	}


}
