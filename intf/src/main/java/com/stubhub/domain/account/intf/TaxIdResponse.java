package com.stubhub.domain.account.intf;

import com.stubhub.domain.account.common.Response;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TaxIdResponse extends Response {
    private String taxId;
}
