package com.stubhub.domain.account.intf;

import java.math.BigDecimal;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.stubhub.newplatform.common.entity.Money;

public class DiscountResponseTest {
	private DiscountResponse  discountResponse;
	
	@BeforeTest
	public void setUp() {
		discountResponse = new DiscountResponse();
	}
	
	@Test
	public void testDiscountResponse(){
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
	    Assert.assertEquals(discountResponse.getId(), id);
	    Assert.assertEquals(discountResponse.getType(), type);
	    Assert.assertEquals(discountResponse.getDescription(), description);
	    Assert.assertEquals(discountResponse.getUsedDiscount(), money);
	}
}
