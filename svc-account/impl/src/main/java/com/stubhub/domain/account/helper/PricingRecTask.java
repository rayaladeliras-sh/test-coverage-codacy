package com.stubhub.domain.account.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import com.stubhub.domain.account.intf.PricingRecommendation;
import com.stubhub.platform.utilities.webservice.shcontext.SHAPIContext;
import com.stubhub.platform.utilities.webservice.shcontext.SHAPIThreadLocal;

public class PricingRecTask implements Callable<List<PricingRecommendation>> {

    private List<Long> listingIds;

    private PricingRecHelper pricingRecHelper;

    private SHAPIContext apiContext;
    
    private String sellerGuid;


    public PricingRecTask(List<Long> listingIds,  PricingRecHelper pricingRecHelper,  SHAPIContext apiContext, String sellerGuid){
        this.listingIds = new ArrayList<Long>(listingIds);
        this.pricingRecHelper =  pricingRecHelper;
        this.apiContext = apiContext;
        this.sellerGuid = sellerGuid;
    }

    @Override
    public List<PricingRecommendation> call(){
        SHAPIThreadLocal.set(apiContext);
       return pricingRecHelper.handlePriceRecommendations(listingIds, sellerGuid);

    }
}