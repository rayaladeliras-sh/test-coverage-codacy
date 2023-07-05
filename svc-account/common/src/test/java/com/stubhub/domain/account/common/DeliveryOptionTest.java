package com.stubhub.domain.account.common;

import junit.framework.Assert;

import org.testng.annotations.Test;

import com.stubhub.domain.account.common.enums.DeliveryOption;

public class DeliveryOptionTest {
	@Test
	public void testFromString() {
		Assert.assertEquals(DeliveryOption.FEDEX, DeliveryOption.fromString("FEDEX"));
		Assert.assertEquals(DeliveryOption.UPS, DeliveryOption.fromString("UPS"));
		Assert.assertEquals(DeliveryOption.BARCODE, DeliveryOption.fromString("BARCODE"));
		Assert.assertEquals(DeliveryOption.PDF, DeliveryOption.fromString("PDF"));
		Assert.assertEquals(DeliveryOption.ROYALMAIL, DeliveryOption.fromString("ROYALMAIL"));
		Assert.assertEquals(DeliveryOption.DEUTSCHEPOST, DeliveryOption.fromString("DEUTSCHEPOST"));
		Assert.assertEquals(DeliveryOption.LMS, DeliveryOption.fromString("LMS"));
		Assert.assertEquals(DeliveryOption.OTHER, DeliveryOption.fromString("OTHER"));
		Assert.assertEquals(DeliveryOption.COURIER, DeliveryOption.fromString("COURIER"));
		Assert.assertEquals(DeliveryOption.MOBILE_TICKET, DeliveryOption.fromString("MOBILE_TICKET"));
		Assert.assertEquals(DeliveryOption.EXTERNAL_TRANSFER, DeliveryOption.fromString("EXTERNAL_TRANSFER"));
		Assert.assertEquals(DeliveryOption.LOCALDELIVERY, DeliveryOption.fromString("LOCALDELIVERY"));
		Assert.assertNull(DeliveryOption.fromString(null));
	}
	
}
