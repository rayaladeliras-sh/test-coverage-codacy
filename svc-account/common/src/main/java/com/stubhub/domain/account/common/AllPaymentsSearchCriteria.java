package com.stubhub.domain.account.common;

public class AllPaymentsSearchCriteria {

  private String sellerId;
  private String sort;
  private String q;
  private String status;
  private Integer start;
  private Integer rows;
  private String currency;
  private boolean includeCurrencySummary;
  private boolean includePaymentSummary;

  public String getSellerId() {
    return sellerId;
  }

  public void setSellerId(String sellerId) {
    this.sellerId = sellerId;
  }

  public String getSort() {
    return sort;
  }

  public void setSort(String sort) {
    this.sort = sort;
  }

  public String getQ() {
    return q;
  }

  public void setQ(String q) {
    this.q = q;
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

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public boolean isIncludeCurrencySummary() {
    return includeCurrencySummary;
  }

  public void setIncludeCurrencySummary(boolean includeCurrencySummary) {
    this.includeCurrencySummary = includeCurrencySummary;
  }

  public boolean isIncludePaymentSummary() {
    return includePaymentSummary;
  }

  public void setIncludePaymentSummary(boolean includePaymentSummary) {
    this.includePaymentSummary = includePaymentSummary;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
