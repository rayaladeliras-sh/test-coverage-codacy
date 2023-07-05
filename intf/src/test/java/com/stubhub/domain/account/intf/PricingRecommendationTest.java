package com.stubhub.domain.account.intf;

import org.testng.Assert;
import org.testng.annotations.Test;


public class PricingRecommendationTest {

	
	@Test
	 public void testGetSet() {

		PricingRecommendation pricingRecommendation = new PricingRecommendation();
		pricingRecommendation.setEventMaxPrice(new Double(121.11));
		pricingRecommendation.setEventMinPrice(new Double(11.00));
		pricingRecommendation.setIsOverPriced(true);
		pricingRecommendation.setIsUnderPriced(false);
		pricingRecommendation.setLowerboundPrice(20L);
		pricingRecommendation.setSectionMaxPrice(new Double(50));
		pricingRecommendation.setSectionMinPrice(new Double(12));
		pricingRecommendation.setSuggestedPrice(60L);
		pricingRecommendation.setUpperboundPrice(80L);
		pricingRecommendation.setZoneMaxPrice(new Double(90.21));
		pricingRecommendation.setZoneMinPrice(new Double(20.23));
		Assert.assertEquals(pricingRecommendation.getEventMaxPrice(),new Double(121.11));
		Assert.assertEquals(pricingRecommendation.getEventMinPrice(),new Double(11.00));
		Assert.assertTrue(pricingRecommendation.getIsOverPriced());
		Assert.assertTrue(!pricingRecommendation.getIsUnderPriced());
		Assert.assertEquals(pricingRecommendation.getLowerboundPrice(),new Long(20L));
		Assert.assertEquals(pricingRecommendation.getSectionMaxPrice(),new Double(50));
		Assert.assertEquals(pricingRecommendation.getSectionMinPrice(),new Double(12));
		Assert.assertEquals(pricingRecommendation.getSuggestedPrice(),new Long(60L));
		Assert.assertEquals(pricingRecommendation.getUpperboundPrice(),new Long(80L));
		Assert.assertEquals(pricingRecommendation.getZoneMaxPrice(),new Double(90.21));
		Assert.assertEquals(pricingRecommendation.getZoneMinPrice(),new Double(20.23));

   	
	   	}
}