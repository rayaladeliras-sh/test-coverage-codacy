package com.stubhub.domain.account.datamodel.dao;

import com.stubhub.domain.account.datamodel.entity.Tin;

/**
 * @author vpothuru
 * date 11/02/15
 */
public interface TinDAO {
	
	Tin getTinByGuid(String guid);
	String addTin(Tin tin);
	int updateTin(Tin tin);	
}
