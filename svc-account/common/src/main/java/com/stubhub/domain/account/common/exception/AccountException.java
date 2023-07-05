package com.stubhub.domain.account.common.exception;

import com.stubhub.domain.account.common.Error;

public class AccountException extends RuntimeException {
	private static final long serialVersionUID = -5598234852111675574L;

	private Error listingError;
	public AccountException(){
		super();
	}
	
	public AccountException(Error listingError){
		super(listingError.getMessage());
		this.listingError = listingError;
	}

	public Error getListingError() {
		return listingError;
	}
	
}
