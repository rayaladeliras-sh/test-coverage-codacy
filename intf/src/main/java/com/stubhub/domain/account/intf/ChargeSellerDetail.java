package com.stubhub.domain.account.intf;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ChargeSellerDetail {
    private BigDecimal amount;
    private String currencyCode;
    private String reasonCode;
    private String reasonDescription;
    
}
