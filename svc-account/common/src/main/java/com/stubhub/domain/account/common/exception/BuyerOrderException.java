package com.stubhub.domain.account.common.exception;

import java.util.Map;

import com.stubhub.domain.infrastructure.common.exception.derived.SHBizException;

public class BuyerOrderException extends SHBizException {

	private static final long serialVersionUID = 1L;
	
	private static final String ERROR_CODE_PREFIX = "accountmanagement.buyerOrder.";

	  public static enum Error {
	    InvalidInput ("Invalid input criteria", 400), 
	    Unauthorized ("Unauthorized access", 401),
	    InternalServerError("Internal Server Error", 500);

	    private String description;
	    private int httpCode;

	    Error(String description, int httpCode) {
	      this.description = description;
	      this.httpCode = httpCode;
	    }

	    public String getDescription() {
	      return description;
	    }

	    public int getHttpCode() {
	      return httpCode;
	    }

	    public String getErrorCode() {
	      return ERROR_CODE_PREFIX + name();
	    }
	  }

	  private final Error error;
	  
	  public BuyerOrderException(Error error) {
		    super(error.getErrorCode(), error.getDescription(), error.getHttpCode());
		    this.error = error;
		  }

		  public BuyerOrderException(Error error, Throwable e) {
		    super(error.getErrorCode(), error.getDescription(), error.getHttpCode(), e);
		    this.error = error;
		  }

		  public BuyerOrderException(Error error, Map<String, String> data) {
		    super(error.getErrorCode(), error.getDescription(), error.getHttpCode());
		    this.error = error;
		    this.data = data;
		  }

		  public Error getError() {
		    return error;
		  }
}
