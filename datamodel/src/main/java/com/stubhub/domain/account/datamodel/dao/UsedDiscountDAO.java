package com.stubhub.domain.account.datamodel.dao;

import java.util.List;

import com.stubhub.domain.account.datamodel.entity.UsedDiscount;

public interface UsedDiscountDAO {

	public List<List> findDetailByTid(Long tid);
	public List<UsedDiscount> findByTid(Long tid);
	public List<UsedDiscount> persist(List<UsedDiscount> usedDiscount);
}
