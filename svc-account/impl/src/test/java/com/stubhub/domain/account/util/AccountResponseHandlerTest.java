package com.stubhub.domain.account.util;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.stubhub.domain.account.common.enums.ErrorType;

public class AccountResponseHandlerTest {
	@Test
	 public void testHandleResponse_NotFound() {
		AccountResponseHandler accountResponseHandler = new AccountResponseHandler();
		com.stubhub.domain.account.common.Response responseErr = new com.stubhub.domain.account.common.Response();
		List<com.stubhub.domain.account.common.Error> errors = new ArrayList<com.stubhub.domain.account.common.Error>();
		com.stubhub.domain.account.common.Error error = new com.stubhub.domain.account.common.Error();
		error.setType(ErrorType.NOT_FOUND);
		errors.add(error);
		responseErr.setErrors(errors);		
		Response resp = Response.status(Response.Status.OK).entity(responseErr).build();
		Response retResp = accountResponseHandler.handleResponse(null, null, resp);
		Assert.assertNotNull(retResp);
	}
	
	@Test
	 public void testHandleResponse_InputErr() {
		AccountResponseHandler accountResponseHandler = new AccountResponseHandler();
		com.stubhub.domain.account.common.Response responseErr = new com.stubhub.domain.account.common.Response();
		List<com.stubhub.domain.account.common.Error> errors = new ArrayList<com.stubhub.domain.account.common.Error>();
		com.stubhub.domain.account.common.Error error = new com.stubhub.domain.account.common.Error();
		error.setType(ErrorType.INPUTERROR);
		errors.add(error);
		responseErr.setErrors(errors);		
		Response resp = Response.status(Response.Status.OK).entity(responseErr).build();
		Response retResp = accountResponseHandler.handleResponse(null, null, resp);
		Assert.assertNotNull(retResp);
	}
	
	@Test
	 public void testHandleResponse_BusinessErr() {
		AccountResponseHandler accountResponseHandler = new AccountResponseHandler();
		com.stubhub.domain.account.common.Response responseErr = new com.stubhub.domain.account.common.Response();
		List<com.stubhub.domain.account.common.Error> errors = new ArrayList<com.stubhub.domain.account.common.Error>();
		com.stubhub.domain.account.common.Error error = new com.stubhub.domain.account.common.Error();
		error.setType(ErrorType.BUSINESSERROR);
		errors.add(error);
		responseErr.setErrors(errors);		
		Response resp = Response.status(Response.Status.OK).entity(responseErr).build();
		Response retResp = accountResponseHandler.handleResponse(null, null, resp);
		Assert.assertNotNull(retResp);
	}
	
	@Test
	 public void testHandleResponse_AuthErr() {
		AccountResponseHandler accountResponseHandler = new AccountResponseHandler();
		com.stubhub.domain.account.common.Response responseErr = new com.stubhub.domain.account.common.Response();
		List<com.stubhub.domain.account.common.Error> errors = new ArrayList<com.stubhub.domain.account.common.Error>();
		com.stubhub.domain.account.common.Error error = new com.stubhub.domain.account.common.Error();
		error.setType(ErrorType.AUTHENTICATIONERROR);
		errors.add(error);
		responseErr.setErrors(errors);		
		Response resp = Response.status(Response.Status.OK).entity(responseErr).build();
		Response retResp = accountResponseHandler.handleResponse(null, null, resp);
		Assert.assertNotNull(retResp);
	}
	
	@Test
	 public void testHandleResponse_SysErr() {
		AccountResponseHandler accountResponseHandler = new AccountResponseHandler();
		com.stubhub.domain.account.common.Response responseErr = new com.stubhub.domain.account.common.Response();
		List<com.stubhub.domain.account.common.Error> errors = new ArrayList<com.stubhub.domain.account.common.Error>();
		com.stubhub.domain.account.common.Error error = new com.stubhub.domain.account.common.Error();
		error.setType(ErrorType.SYSTEMERROR);
		errors.add(error);
		responseErr.setErrors(errors);		
		Response resp = Response.status(Response.Status.OK).entity(responseErr).build();
		Response retResp = accountResponseHandler.handleResponse(null, null, resp);
		Assert.assertNotNull(retResp);
	}
	
	@Test
	 public void testHandleResponse_NoContent() {
		AccountResponseHandler accountResponseHandler = new AccountResponseHandler();
		com.stubhub.domain.account.common.Response responseErr = new com.stubhub.domain.account.common.Response();
		List<com.stubhub.domain.account.common.Error> errors = new ArrayList<com.stubhub.domain.account.common.Error>();
		com.stubhub.domain.account.common.Error error = new com.stubhub.domain.account.common.Error();
		error.setType(ErrorType.NO_CONTENT);
		errors.add(error);
		responseErr.setErrors(errors);		
		Response resp = Response.status(Response.Status.OK).entity(responseErr).build();
		Response retResp = accountResponseHandler.handleResponse(null, null, resp);
		Assert.assertNotNull(retResp);
	}

}
