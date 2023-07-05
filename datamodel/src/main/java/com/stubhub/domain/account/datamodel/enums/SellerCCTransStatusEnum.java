package com.stubhub.domain.account.datamodel.enums;

import com.stubhub.domain.account.datamodel.entity.SellerPaymentStatusEnum;

public enum SellerCCTransStatusEnum implements PaymentsStatusEnum {

  FAILED_AVS(1L, "Failed - AVS Error-Zip"),
  FAILED_CARD(2L, "Failed - Card Declined"),
  FAILED_GATEWAY(3L, "  Failed - Gateway Down"),
  FAILED_REFUND(4L, "Failed - Refund Already Processed"),
  GATEWAY_REJECTED(5L, "GATEWAY_REJECTED"),
  DECLINED(6L, "declined"),
  FAILED(7L, "failed"),
  SUCCESS(8L, "success"),
  INPROCESS(0L, "others");


  private Long id;
  private String name;

  SellerCCTransStatusEnum(Long id, String name) {
    this.id = id;
    this.name = name;
  }

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  public static SellerCCTransStatusEnum getSellerCCTransStatusEnum(String name) {
    SellerCCTransStatusEnum list[] = SellerCCTransStatusEnum.values();
    for (SellerCCTransStatusEnum obj : list) {
      if (obj.getName().equalsIgnoreCase(name)) {
        return obj;
      }
    }

    if (name != null) {
      return SellerCCTransStatusEnum.INPROCESS;
    }
    return null;
  }

  public static SellerCCTransStatusEnum getSellerCCTransStatusEnum(Long id) {
    SellerCCTransStatusEnum list[] = SellerCCTransStatusEnum.values();
    for (SellerCCTransStatusEnum obj : list) {
      if (obj.getId().equals(id)) {
        return obj;
      }
    }

    if (id != null) {
      return SellerCCTransStatusEnum.INPROCESS;
    }
    return null;
  }
}
