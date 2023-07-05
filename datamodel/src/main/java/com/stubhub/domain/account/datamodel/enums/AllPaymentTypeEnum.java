package com.stubhub.domain.account.datamodel.enums;

public enum AllPaymentTypeEnum {
  PAYMENTS(1L, "Payments", "payments"),
  CREDIT_CARD_CHARGES(2L, "Credit Card Charges", "cardCharge"),
  ACCOUNT_CHARGES(3L, "Account Charges", "accountCharge");

  private Long id;
  private String name;
  private String customName;

  AllPaymentTypeEnum(Long id, String name, String customName) {
    this.id = id;
    this.name = name;
    this.customName = customName;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getCustomName() {
    return customName;
  }

  public static String getCustomNameByName(String name) {
    AllPaymentTypeEnum[] values = AllPaymentTypeEnum.values();
    for (AllPaymentTypeEnum allPaymentTypeEnum : values) {
      if (allPaymentTypeEnum.getName().equals(name)) {
        return allPaymentTypeEnum.getCustomName();
      }
    }
    return "";
  }
}
