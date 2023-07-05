package com.stubhub.domain.account.common.enums;

public enum PaymentUserDefinedPackedStatus {
  PAYOUT(1L, "payout"),
  PENDING(2L, "pending"),
  CHARGES(3L, "charges"),
  CANCELLED(4L, "cancelled"),
  PROCESSING(5L, "processing");

  private Long id;
  private String name;

  PaymentUserDefinedPackedStatus(Long id, String name) {
    this.id = id;
    this.name = name;
  }

  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getName() {
    return name;
  }

  public static PaymentUserDefinedPackedStatus getEnum(String name) {
    if (name == null) {
      return null;
    }
    PaymentUserDefinedPackedStatus list[] = PaymentUserDefinedPackedStatus.values();
    for (int i = 0; i < list.length; i++) {
      PaymentUserDefinedPackedStatus obj = list[i];
      if (obj.getName().equalsIgnoreCase(name)) {
        return obj;
      }
    }
    return null;
  }

}