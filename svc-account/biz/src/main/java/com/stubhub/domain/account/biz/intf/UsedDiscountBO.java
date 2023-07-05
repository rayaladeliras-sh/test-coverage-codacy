package com.stubhub.domain.account.biz.intf;

import java.util.List;

import com.stubhub.domain.account.datamodel.entity.UsedDiscount;

public interface UsedDiscountBO {

	public List<List> getDiscounts(String tid);
	public List<UsedDiscount> copyDiscountsforSubsOrder(Long oldOrderId, Long newOrderId, String stubnetUser);
}
