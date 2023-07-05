package com.stubhub.domain.account.common;

public class PaymentsSearchCriteria {
	private String sellerId;
	private String sort;
	private String q;
	private Integer start;
	private Integer rows;
	private boolean includePaymentSummary;
	private boolean includeCreditMemo;
    private boolean includeCurrencySummary;
    private boolean includeRecordType;
    public boolean isIncludeRecordType() {
		return includeRecordType;
	}

	public void setIncludeRecordType(boolean includeRecordType) {
		this.includeRecordType = includeRecordType;
	}

	private String currency;



    public boolean isIncludeCurrencySummary() {
        return includeCurrencySummary;
    }
    
    public void setIncludeCurrencySummary(boolean includeCurrencySummary) {
        this.includeCurrencySummary = includeCurrencySummary;
    }
    
    public String getSellerId() {
		return sellerId;
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}
	public boolean isIncludePaymentSummary() {
		return includePaymentSummary;
	}

	public void setIncludePaymentSummary(boolean includePaymentSummary) {
		this.includePaymentSummary = includePaymentSummary;
	}

	public boolean isIncludeCreditMemo() {
		return includeCreditMemo;
	}

	public void setIncludeCreditMemo(boolean includeCreditMemo) {
		this.includeCreditMemo = includeCreditMemo;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
}
