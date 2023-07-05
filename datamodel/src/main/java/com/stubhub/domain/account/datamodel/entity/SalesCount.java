package com.stubhub.domain.account.datamodel.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;

@Entity
@NamedNativeQuery(name = "SalesCount.getCountByUserId", query = "" +
		"SELECT " + 
		"U.ID as user_id, " +
		"TO_CHAR ( NVL ( SUM ( CASE WHEN ORDER_PROC_SUB_STATUS_CODE = 1 THEN 1 ELSE 0 END), 0)) AS unconfirmed_count, " +
		"TO_CHAR ( NVL ( SUM ( CASE WHEN ORDER_PROC_SUB_STATUS_CODE IN (38, 39) THEN 1 ELSE 0 END), 0)) AS cancelled_count, " +
		"TO_CHAR ( NVL ( SUM ( CASE WHEN ORDER_PROC_SUB_STATUS_CODE NOT IN (1, 38, 39) AND DATE_ADDED > (SYSDATE - 30) THEN 1 ELSE 0 END), 0)) AS completed_30_days_count, " +
		"TO_CHAR ( NVL ( SUM ( CASE WHEN ORDER_PROC_SUB_STATUS_CODE NOT IN (1, 38, 39) AND DATE_ADDED > (SYSDATE - 180) THEN 1 ELSE 0 END), 0)) AS completed_180_days_count " +
		"FROM STUB_TRANS ST, USERS U " +
		"WHERE U.ID = ST.SELLER_ID " +
		"AND U.USER_COOKIE_GUID=:arg1 GROUP BY U.ID ", resultClass=SalesCount.class)		

public class SalesCount implements java.io.Serializable {
	
	@Id
	@Column(name = "user_id")
	private Long userId;
	@Column(name = "unconfirmed_count")
	private Long unconfirmedCount;
	@Column(name = "cancelled_count")
	private Long cancelledCount;
	@Column(name = "completed_30_days_count")
	private Long completed30DaysCount;
	@Column(name = "completed_180_days_count")
	private Long completed180DaysCount;
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getUnconfirmedCount() {
		return unconfirmedCount;
	}
	public void setUnconfirmedCount(Long unconfirmedCount) {
		this.unconfirmedCount = unconfirmedCount;
	}
	public Long getCancelledCount() {
		return cancelledCount;
	}
	public void setCancelledCount(Long cancelledCount) {
		this.cancelledCount = cancelledCount;
	}
	public Long getCompleted30DaysCount() {
		return completed30DaysCount;
	}
	public void setCompleted30DaysCount(Long completed30DaysCount) {
		this.completed30DaysCount = completed30DaysCount;
	}
	public Long getCompleted180DaysCount() {
		return completed180DaysCount;
	}
	public void setCompleted180DaysCount(Long completed180DaysCount) {
		this.completed180DaysCount = completed180DaysCount;
	}	
	
}
