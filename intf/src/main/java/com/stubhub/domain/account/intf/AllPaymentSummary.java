package com.stubhub.domain.account.intf;

import com.stubhub.domain.account.common.Response;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AllPaymentSummary extends Response {
  private String status;

  private int count;
  private double sum;

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public double getSum() {
    return sum;
  }

  public void setSum(double sum) {
    this.sum = sum;
  }
}
