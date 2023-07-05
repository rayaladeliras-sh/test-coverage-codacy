package com.stubhub.domain.account.intf;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaxIdRequest {
  private String taxId;
}
