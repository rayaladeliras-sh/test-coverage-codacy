package com.stubhub.domain.account.intf;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.stubhub.newplatform.common.entity.Money;

public class DiscountsResponseTest {
	private DiscountsResponse discountsResponse;
	
	@BeforeTest
	public void setUp() {
		discountsResponse = new DiscountsResponse();
	}
	
	@Test
	public void testDiscountsResponse(){
		DiscountResponse discountResponse = new DiscountResponse();
		String id = "200";
	    String type = "Dollar Discount with Balance";
	    String description = "Welcome Back!";
	    BigDecimal amount = new BigDecimal(25.00);
	    String currency = "USD";
	    discountResponse.setId(id);
	    discountResponse.setType(type);
	    discountResponse.setDescription(description);
	    Money money = new Money(amount,currency);
	    discountResponse.setUsedDiscount(money);
	    
	    List<DiscountResponse> list = new ArrayList<DiscountResponse>();
	    list.add(discountResponse);
	    discountsResponse.setDiscounts(list);
	    
	    Assert.assertEquals(discountsResponse.getDiscounts(), list);
	}

}
