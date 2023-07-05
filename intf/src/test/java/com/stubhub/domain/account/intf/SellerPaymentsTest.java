package com.stubhub.domain.account.intf;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhiyyang on 11/14/16.
 */
public class SellerPaymentsTest {

    @Test
    public void test(){

        SellerPayments sps = new SellerPayments();
        List<CurrencySummary> list = new ArrayList<CurrencySummary>();
        list.add(new CurrencySummary());
        sps.setCurrencySummary( list);
        Assert.assertNotNull(sps.getCurrencySummary());


    }
}