package com.stubhub.domain.account.datamodel.entity;

import lombok.*;

/**
 * Created by mengli on 11/29/18.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class BrokerLicenseSuccessEntity {
    private long userBrokerLicenseId;
    private String message;
}
