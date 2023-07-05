package com.stubhub.domain.account.datamodel.dao;

import com.stubhub.domain.account.datamodel.entity.StubTransDetail;

import java.util.List;

public interface StubTransDetailDAO {
	
	public Long persist(StubTransDetail stubTransDetail);

    public List<StubTransDetail> getSeatDetails(Long tid);
}
