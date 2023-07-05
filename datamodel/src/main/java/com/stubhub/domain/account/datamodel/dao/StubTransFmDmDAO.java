package com.stubhub.domain.account.datamodel.dao;

import com.stubhub.domain.account.datamodel.entity.StubTransFmDm;

import java.util.List;

public interface StubTransFmDmDAO {
	
	public Long persist(StubTransFmDm stubTransFmDm);
	public List<StubTransFmDm> getFmDmByTids(List<Long> tids);
}
