package com.stubhub.domain.account.datamodel.entity;

import java.math.BigDecimal;
import java.util.GregorianCalendar;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.stubhub.newplatform.persistence.adapter.UTCCalendarToTimestampAdapter;

public class UsedDiscountTest {

	private UsedDiscount usedDiscount;
	
	@BeforeTest
	public void setUp() {
		usedDiscount = new UsedDiscount();
	}

	@Test
	public void testUsedDiscount(){
		GregorianCalendar calender = UTCCalendarToTimestampAdapter.getNewUTCCalendarInstanceStatic();
	    Long id = 2000L;
	    BigDecimal amount = new BigDecimal(25.00);
	    String currency = "USD";
	    usedDiscount.setDiscountId(id);
	    usedDiscount.setActive(0);
	    usedDiscount.setCreatedBy("bijain");
	    usedDiscount.setDateAdded(calender);
	    usedDiscount.setAmountUsed(amount);
	    usedDiscount.setCurrencyCode(currency);
	    usedDiscount.setId(id);
	    usedDiscount.setLastUpdateBy("bijain");
	    usedDiscount.setLastUpdatedDate(calender);
	    usedDiscount.setTid(id);	    
		Assert.assertEquals(usedDiscount.getDiscountId(), id);
		Assert.assertEquals(usedDiscount.getAmountUsed(), amount);
		Assert.assertEquals(usedDiscount.getCurrencyCode(), currency);
		Assert.assertEquals(usedDiscount.getActive(),0);
		Assert.assertEquals(usedDiscount.getCreatedBy(),"bijain");
		Assert.assertEquals(usedDiscount.getLastUpdateBy(),"bijain");
		Assert.assertEquals(usedDiscount.getDateAdded(),calender);
		Assert.assertEquals(usedDiscount.getId(),id);
		Assert.assertEquals(usedDiscount.getLastUpdatedDate(),calender);
		Assert.assertEquals(usedDiscount.getTid(),id);		
	}
}