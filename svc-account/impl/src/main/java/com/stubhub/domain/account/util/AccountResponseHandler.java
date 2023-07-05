package com.stubhub.domain.account.util;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.jaxrs.ext.ResponseHandler;
import org.apache.cxf.jaxrs.model.OperationResourceInfo;
import org.apache.cxf.message.Message;
import org.springframework.stereotype.Component;

import com.stubhub.domain.account.common.enums.ErrorType;

@Component("accountResponseHandler")
public class AccountResponseHandler implements ResponseHandler {

	private final static Log log = LogFactory.getLog(AccountResponseHandler.class);
	
	@Override
	public Response handleResponse(Message message, OperationResourceInfo arg1, Response response) {

		Object retEntity = response.getEntity();

		if (log.isDebugEnabled()) {
			log.debug("enter handleResponse, responseEntity=" + response.getEntity());
		}
	
		if (retEntity != null) {
			if (retEntity instanceof com.stubhub.domain.account.common.Response) {
				if ((((com.stubhub.domain.account.common.Response) retEntity).getErrors() != null)
						&& (((com.stubhub.domain.account.common.Response) retEntity).getErrors().size() > 0)) {
					com.stubhub.domain.account.common.Error error = ((com.stubhub.domain.account.common.Response) retEntity).getErrors().get(0);
					if (log.isDebugEnabled()) {
						log.debug("try change response status, error=" + error);
					}
					ResponseBuilder responseBuilder = Response.fromResponse(response);
					if(error.getType()== ErrorType.INPUTERROR){
						responseBuilder.status(Status.BAD_REQUEST.getStatusCode());
					}else if(error.getType()== ErrorType.NOT_FOUND){
						responseBuilder.status(Status.NOT_FOUND.getStatusCode());
					}else if(error.getType()== ErrorType.BUSINESSERROR){
						responseBuilder.status(Status.CONFLICT.getStatusCode());
					}else if(error.getType()== ErrorType.AUTHENTICATIONERROR){
						responseBuilder.status(Status.FORBIDDEN.getStatusCode());
					}else if(error.getType()== ErrorType.AUTHORIZATIONERROR){
						responseBuilder.status(Status.UNAUTHORIZED.getStatusCode());
					}else if(error.getType()== ErrorType.NO_CONTENT){
						responseBuilder.status(Status.NO_CONTENT.getStatusCode());
					}
					else{
						responseBuilder.status(Status.INTERNAL_SERVER_ERROR.getStatusCode());
					}
					Response realResponse = responseBuilder.build();
					if (log.isDebugEnabled()) {
						log.debug("after change response status, status " + response.getStatus() + " -> " + realResponse.getStatus());
					}
					return realResponse;
				}
			}
		}
		return response;
	}
	
}
