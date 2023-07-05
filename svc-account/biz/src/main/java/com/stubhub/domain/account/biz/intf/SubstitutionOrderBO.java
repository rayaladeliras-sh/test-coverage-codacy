package com.stubhub.domain.account.biz.intf;

import com.stubhub.common.exception.InvalidArgumentException;
import com.stubhub.common.exception.RecordNotFoundForIdException;
import com.stubhub.common.exception.UserNotAuthorizedException;
import com.stubhub.domain.account.intf.SubstitutionRequest;

public interface SubstitutionOrderBO {
	
	public Long createOrder(SubstitutionRequest request, String orderId, String operatorId, Long orderSourceId) 
		throws NumberFormatException, RecordNotFoundForIdException, InvalidArgumentException, UserNotAuthorizedException, Exception;

	
}
