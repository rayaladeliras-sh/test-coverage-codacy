package com.stubhub.domain.account.intf;

import com.stubhub.domain.account.common.Response;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AllPaymentResponse extends Response {
  private List<AllPayment> payments;

  private int numFound = 0;
  private Map<String, Double> allPaymentSummary;
  private List<CurrencySummary> currencySummary;

  public List<AllPayment> getPayments() {
    return payments;
  }

  public void setPayments(List<AllPayment> payments) {
    this.payments = payments;
  }

  public int getNumFound() {
    return numFound;
  }

  public void setNumFound(int numFound) {
    this.numFound = numFound;
  }

  public Map<String, Double> getAllPaymentsSummary() {
    return allPaymentSummary;
  }

  public void setAllPaymentsSummary(Map<String, Double> allPaymentSummary) {
    this.allPaymentSummary = allPaymentSummary;
  }

  public List<CurrencySummary> getCurrencySummary() {
    return currencySummary;
  }

  public void setCurrencySummary(List<CurrencySummary> currencySummary) {
    this.currencySummary = currencySummary;
  }
}