package com.stubhub.domain.account.biz.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.stubhub.common.exception.InvalidArgumentException;
import com.stubhub.domain.account.biz.intf.StubnetUserBO;
import com.stubhub.domain.account.datamodel.dao.StubnetUsersDAO;
import com.stubhub.domain.account.datamodel.entity.StubnetUsers;

@Component("stubnetUserBO")
public class StubnetUserBOImpl implements StubnetUserBO{

	@Autowired
	@Qualifier("stubnetUsersDAO")
	private StubnetUsersDAO stubnetUsersDAO;

	@Override
	public Long isStubnetUser(String loginName) throws InvalidArgumentException {
		List<StubnetUsers> stubnetUser = stubnetUsersDAO.getStubnetUserByLoginName(loginName);
		if (stubnetUser == null || stubnetUser.size() == 0){
			throw new InvalidArgumentException("operatorId is not found. loginName", loginName);
		}
		if (stubnetUser.get(0).getActive() == false){
			throw new InvalidArgumentException("operatorId is not active. loginName", loginName);
		}
		return stubnetUser.get(0).getId();
	}

}
