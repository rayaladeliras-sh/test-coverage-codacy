package com.stubhub.domain.account.datamodel.dao;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.stubhub.common.exception.RecordNotFoundForIdException;
import com.stubhub.domain.account.datamodel.entity.StubTrans;

public interface StubTransDAO {

	public List<StubTrans> getOrderProcSubStatusCode(Long tid);
	public int updateBuyerContactId(Long tid, Long contactId, String addedBy, Calendar dateAdded);
	public StubTrans persist(StubTrans stubTrans);
	public StubTrans getById(Long tid) throws RecordNotFoundForIdException;
	public int getBuyerFlipCount(Long userId);
	public void updateOrderProcStatus(final Long orderId, final Long newStatus);
	public int getSelStubTicketCount(Long sellerId);
	public int getSelTransTikCount(Long sellerId);
	public int getSelPayTicketCount(Long sellerId);
	public Map<String, String> getUserSummaryOrderStats(Long buyerId);
	public Map<String, String> getUserSummarySaleStats(Long sellerId);
}
