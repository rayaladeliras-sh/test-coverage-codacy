package com.stubhub.domain.account.datamodel.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;

@Entity
@NamedNativeQuery(name = "ListingsCount.getCountByUserId", query = "" +
		"SELECT " + 
		"U.ID AS user_id, " +
		"TO_CHAR (NVL (SUM (CASE WHEN TIX.SYSTEM_STATUS = 'ACTIVE'AND TIX.END_DATE >= SYSDATE AND QUANTITY_REMAIN > 0 THEN 1 ELSE 0 END), 0)) AS active_count, " +
		"TO_CHAR (NVL (SUM (CASE WHEN TIX.SYSTEM_STATUS = 'INACTIVE' THEN 1 ELSE 0 END), 0)) AS inactive_count, " +
		"TO_CHAR (NVL (SUM (CASE WHEN TIX.SYSTEM_STATUS = 'PENDING LOCK' THEN 1 ELSE 0 END), 0)) AS pending_lock_count, " +
		"TO_CHAR (NVL (SUM (CASE WHEN TIX.SYSTEM_STATUS = 'INCOMPLETE' THEN 1 ELSE 0 END), 0)) AS incomplete_count, " +
		"TO_CHAR (NVL (SUM (CASE WHEN TIX.LMS_APPROVAL_STATUS_ID = 1 THEN 1 ELSE 0 END), 0)) AS pending_lms_approval_count " +
		"FROM USERS U, TICKETS TIX " +
		"WHERE U.ID = TIX.SELLER_ID " +
		"AND U.USER_COOKIE_GUID =:arg1 GROUP BY U.ID ", resultClass=ListingsCount.class)		

public class ListingsCount implements java.io.Serializable {
	
	@Id
	@Column(name = "user_id")
	private Long userId;
	@Column(name = "active_count")
	private Long activeCount;
	@Column(name = "inactive_count")
	private Long inactiveCount;
	@Column(name = "pending_lock_count")
	private Long pendingLockCount;
	@Column(name = "incomplete_count")
	private Long incompleteCount;
	@Column(name = "pending_lms_approval_count")
	private Long pendingLmsApprovalCount;
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getActiveCount() {
		return activeCount;
	}
	public void setActiveCount(Long activeCount) {
		this.activeCount = activeCount;
	}
	public Long getInactiveCount() {
		return inactiveCount;
	}
	public void setInactiveCount(Long inactiveCount) {
		this.inactiveCount = inactiveCount;
	}
	public Long getPendingLockCount() {
		return pendingLockCount;
	}
	public void setPendingLockCount(Long pendingLockCount) {
		this.pendingLockCount = pendingLockCount;
	}
	public Long getIncompleteCount() {
		return incompleteCount;
	}
	public void setIncompleteCount(Long incompleteCount) {
		this.incompleteCount = incompleteCount;
	}
	public Long getPendingLmsApprovalCount() {
		return pendingLmsApprovalCount;
	}
	public void setPendingLmsApprovalCount(Long pendingLmsApprovalCount) {
		this.pendingLmsApprovalCount = pendingLmsApprovalCount;
	}
	
}
