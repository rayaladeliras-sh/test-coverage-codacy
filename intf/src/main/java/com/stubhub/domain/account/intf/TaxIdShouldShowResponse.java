package com.stubhub.domain.account.intf;


import com.stubhub.domain.account.common.Response;

public class TaxIdShouldShowResponse extends Response {
  private boolean shouldShow;

  public boolean isShouldShow() {
    return shouldShow;
  }

  public void setShouldShow(boolean shouldShow) {
    this.shouldShow = shouldShow;
  }
}
