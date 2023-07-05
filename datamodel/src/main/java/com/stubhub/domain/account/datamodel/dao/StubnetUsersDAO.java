package com.stubhub.domain.account.datamodel.dao;

import java.util.List;

import com.stubhub.domain.account.datamodel.entity.StubnetUsers;

public interface StubnetUsersDAO {

	public List<StubnetUsers> getStubnetUserByLoginName(String loginName);
}
