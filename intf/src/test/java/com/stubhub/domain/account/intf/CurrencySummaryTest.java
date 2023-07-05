package com.stubhub.domain.account.intf;


import com.stubhub.newplatform.common.entity.Money;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigDecimal;

public class CurrencySummaryTest {


    @Test
    public void test(){
        CurrencySummary cs = new CurrencySummary();
        cs.setCount(100);
        cs.setCurrency("USD");
        cs.setTotalAmount(new Money(new BigDecimal(100),"USD"));


        Assert.assertEquals(cs.getCount(), new Integer(100));
        Assert.assertEquals(cs.getCurrency(), "USD");
        Assert.assertTrue(cs.getTotalAmount().getAmount().intValue() == 100);
        Assert.assertEquals(cs.getTotalAmount().getCurrency(), "USD");
    }
}
