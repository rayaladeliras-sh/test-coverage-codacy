package com.stubhub.domain.account.helper;


import java.util.ArrayList;
import java.util.List;

import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.stubhub.domain.account.intf.PricingRecommendation;
import com.stubhub.platform.utilities.webservice.shcontext.SHAPIContext;

import junit.framework.Assert;

public class PricingRecTaskTest {

    private PricingRecTask pricingRecTask;
    private PricingRecHelper pricingRecHelper;

    private SHAPIContext apiContext;
    private String sellerGuid;


    @BeforeMethod
    public void setup(){
        pricingRecHelper = Mockito.mock(PricingRecHelper.class);
        apiContext = new SHAPIContext();
        sellerGuid = "C779915579FC5E14E04400212861B256";
    }


    @Test
    public void testRun(){
        List<Long> listings = new ArrayList<Long>();
        pricingRecTask = new PricingRecTask(listings,pricingRecHelper,apiContext, sellerGuid);
        Mockito.when(pricingRecHelper.handlePriceRecommendations(listings, sellerGuid)).thenReturn( new ArrayList<PricingRecommendation>());

        List<PricingRecommendation> result = pricingRecTask.call();
        Assert.assertTrue(result != null);
    }
}