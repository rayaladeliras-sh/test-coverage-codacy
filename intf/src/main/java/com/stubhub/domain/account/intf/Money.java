package com.stubhub.domain.account.intf;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Money", propOrder = { "amount", "currency" })
@XmlRootElement(name = "Money")
public class Money {
    @XmlElement(name = "Amount", required = true)
    private BigDecimal amount;

    @XmlElement(name = "Currency", required = false)
    private String currency;

    public Money() {

    }

    public Money(BigDecimal amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public Money(String moneyAmt) {
        if (null == moneyAmt) {
            return;
        }
        setAmount(new BigDecimal(moneyAmt));
        setCurrency("USD");
    }

    public Money(String moneyAmt, String currency) {
        if (null == moneyAmt) {
            return;
        }
        setAmount(new BigDecimal(moneyAmt));
        setCurrency(currency);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
