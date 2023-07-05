package com.stubhub.domain.account.datamodel.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;

@Entity
@NamedNativeQuery(name = "BuysCount.getCountByUserId", query = "" +
		"SELECT " +
		"U.ID AS user_id, " +
		"TO_CHAR (NVL (SUM (CASE WHEN ORDER_PROC_SUB_STATUS_CODE IN (38, 39) THEN 1 ELSE 0 END), 0)) AS cancelled, " +
		"TO_CHAR (NVL (SUM (CASE WHEN ORDER_PROC_SUB_STATUS_CODE NOT IN (1, 38, 39) AND DATE_ADDED > (SYSDATE - 30) THEN 1 ELSE 0 END), 0)) AS completed_30_days, " +
		"TO_CHAR (NVL (SUM (CASE WHEN ORDER_PROC_SUB_STATUS_CODE NOT IN (1, 38, 39) AND DATE_ADDED > (SYSDATE - 180) THEN 1 ELSE 0 END), 0)) AS completed_180_days " +
		"FROM STUB_TRANS ST, USERS U " +
		"WHERE U.ID = ST.BUYER_ID " +
		"AND U.USER_COOKIE_GUID=:arg1 GROUP BY U.ID ", resultClass=BuysCount.class)

public class BuysCount implements java.io.Serializable {
	
	@Id
	@Column(name = "user_id")
	private Long userId;
	@Column(name = "cancelled")
	private Long cancelled;
	@Column(name = "completed_30_days")
	private Long completed30Days;
	@Column(name = "completed_180_days")
	private Long completed180Days;
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getCancelled() {
		return cancelled;
	}
	public void setCancelled(Long cancelled) {
		this.cancelled = cancelled;
	}
	public Long getCompleted30Days() {
		return completed30Days;
	}
	public void setCompleted30Days(Long completed30Days) {
		this.completed30Days = completed30Days;
	}
	public Long getCompleted180Days() {
		return completed180Days;
	}
	public void setCompleted180Days(Long completed180Days) {
		this.completed180Days = completed180Days;
	}
	
}
