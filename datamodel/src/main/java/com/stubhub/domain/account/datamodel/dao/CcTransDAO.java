package com.stubhub.domain.account.datamodel.dao;

public interface CcTransDAO {
	
	public int updateByTid(Long oldTid, Long newTid, String operatorId);
}
