package com.stubhub.domain.account.biz.intf;

import com.stubhub.common.exception.InvalidArgumentException;

public interface StubnetUserBO {

	public Long isStubnetUser(String loginName) throws InvalidArgumentException;

}
