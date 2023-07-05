package com.stubhub.domain.account.datamodel.dao;

import java.util.List;

import com.stubhub.domain.account.datamodel.entity.StubTransSeatTrait;

public interface StubTransSeatTraitDAO {
	
	public List<StubTransSeatTrait> persist(List<StubTransSeatTrait> stSeatTransList);
}
