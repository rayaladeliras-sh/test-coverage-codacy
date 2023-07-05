package com.stubhub.domain.account.helper.pricerec.entities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

public class PricingRecResponseTest {
	
	private static final Double TWENTY = Double.valueOf(20);
	private static final Double FOURTY = Double.valueOf(40);
	private static final Double FIFTY = Double.valueOf(50);
	private static final Double SIXTY = Double.valueOf(60);
	private static final String ALGORITHM = "Alg1";
	private static final int FIFTY_INT = 50;
	private static final int TWENTY_INT = 20;
	private static final Long TWENTY_LONG = 20L;


	@Test
	public void testPricingRecResponseTest() {
		PricingRecResponse pricingRecResponse = new PricingRecResponse();
		
		PricingConfig pricingConfig = new PricingConfig();
		pricingConfig.setAlgorithm(ALGORITHM);
		pricingConfig.setListingKeepPercent(FIFTY_INT);
		pricingConfig.setLowerPct(TWENTY);
		pricingConfig.setMedianPct(FOURTY);
		pricingConfig.setMinQualityScore(FIFTY_INT);
		pricingConfig.setMinTransaction(TWENTY_INT);
		pricingConfig.setTransactionKeepPercent(TWENTY_INT);
		pricingConfig.setUpperPct(SIXTY);
		
		
		List<PricingRec> recoList = new ArrayList<PricingRec>();
		PricingRec pricingRec = new PricingRec();
		pricingRec.setCurrentPrice(TWENTY);
		pricingRec.setEventId(TWENTY_INT);
		pricingRec.setEventMaxPrice(SIXTY);
		pricingRec.setEventMinPrice(TWENTY);
		pricingRec.setIsOverPriced(false);
		pricingRec.setIsUnderPriced(true);
		pricingRec.setListingId(TWENTY_LONG);
		pricingRec.setLowerboundPrice(TWENTY_LONG);
		pricingRec.setRow("20");
		pricingRec.setSeats("2-3");
		pricingRec.setSectionId(TWENTY_INT);
		pricingRec.setSectionMaxPrice(SIXTY);
		pricingRec.setSectionMinPrice(TWENTY);
		pricingRec.setSuggestedPrice(TWENTY_LONG);
		pricingRec.setUpperboundPrice(TWENTY_LONG);
		pricingRec.setZoneMaxPrice(SIXTY);
		pricingRec.setZoneMinPrice(TWENTY);
		recoList.add(pricingRec);
		
		pricingRecResponse.setImpressionToken("ImpressionToken");
		pricingRecResponse.setPricingConfig(pricingConfig);
		pricingRecResponse.setPricingRecommendations(recoList);

		pricingRec = pricingRecResponse.getPricingRecommendations().get(0);
		
		Assert.assertEquals(TWENTY_LONG, pricingRec.getSuggestedPrice());
		Assert.assertNotNull(pricingRec.toString());
		Assert.assertNotNull(pricingRecResponse.toString());
		Assert.assertNotNull(pricingRecResponse.getImpressionToken());
		Assert.assertNotNull(pricingRecResponse.getPricingConfig());
		Assert.assertNotNull(pricingRecResponse.getPricingConfig().toString());

	}

}