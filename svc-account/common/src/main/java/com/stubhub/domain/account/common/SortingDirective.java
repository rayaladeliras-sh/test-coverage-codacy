package com.stubhub.domain.account.common;

import com.stubhub.domain.account.common.enums.SortColumnType;

public class SortingDirective {
	private SortColumnType sortColumnName;
	private Integer sortDirection;
	public SortColumnType getSortColumnType() {
		return sortColumnName;
	}
	public void setSortColumnType(SortColumnType sortColumnName) {
		this.sortColumnName = sortColumnName;
	}
	public Integer getSortDirection() {
		return sortDirection;
	}
	public void setSortDirection(Integer sortDirection) {
		this.sortDirection = sortDirection;
	}
}
