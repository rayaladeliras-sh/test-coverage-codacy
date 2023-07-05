package com.stubhub.domain.account.helper;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.annotations.Test;

import com.stubhub.domain.account.common.enums.DeliveryOption;
import com.stubhub.domain.account.intf.ListingResponse;
import com.stubhub.domain.fulfillment.window.v1.intf.FulfillmentMethodResponse;
import com.stubhub.domain.fulfillment.window.v1.intf.FulfillmentWindowResponse;
import com.stubhub.domain.fulfillment.window.v1.intf.ListingFulfillmentWindowResponse;
import com.stubhub.platform.utilities.webservice.shcontext.SHAPIContext;

public class GetFulfillmentWindowTaskTest {
	@Test
	public void testCall() throws Exception{
		GetFulfillmentWindowTask task = new GetFulfillmentWindowTask();
		FulfillmentWindowHelper helper = Mockito.mock(FulfillmentWindowHelper.class);
		ReflectionTestUtils.setField(task, "helper", helper);
		ListingResponse listing = new ListingResponse();
		listing.setId("123");
		SHAPIContext context = new SHAPIContext();
		task.setAllContext(listing, 3, null,null, context);
		
		listing = task.call();
		Assert.assertEquals(DeliveryOption.BARCODE, listing.getDeliveryOption());
		
		task.setAllContext(listing, 2, null,null, context);
		listing = task.call();
		Assert.assertEquals(DeliveryOption.PDF, listing.getDeliveryOption());

		task.setAllContext(listing, 7, null,null, context);
		listing = task.call();
		Assert.assertEquals(DeliveryOption.EXTERNAL_TRANSFER, listing.getDeliveryOption());

		task.setAllContext(listing, 8, null,null, context);
		listing = task.call();
		Assert.assertEquals(DeliveryOption.EXTERNAL_TRANSFER, listing.getDeliveryOption());

		task.setAllContext(listing, 9, null,null, context);
		listing = task.call();
		Assert.assertEquals(DeliveryOption.MOBILE_TICKET, listing.getDeliveryOption());

		ListingFulfillmentWindowResponse response = new ListingFulfillmentWindowResponse();
		List<FulfillmentWindowResponse> windowList = new ArrayList<FulfillmentWindowResponse>();
		response.setFulfillmentWindows(windowList);
		FulfillmentWindowResponse window = new FulfillmentWindowResponse();
		FulfillmentMethodResponse dm1 = new FulfillmentMethodResponse();
		dm1.setId(7L);
		window.setFulfillmentMethod(dm1);
		windowList.add(window);
		
		FulfillmentWindowResponse window2 = new FulfillmentWindowResponse();
		FulfillmentMethodResponse dm2 = new FulfillmentMethodResponse();
		dm2.setId(11L);
		window2.setFulfillmentMethod(dm2);
		windowList.add(window2);		
		Mockito.when(helper.getFulfillmentWindowByListingId(1234L)).thenReturn(response);
		listing.setId("1234");
		task.setAllContext(listing, 1, null,null, context);
		listing = task.call();
		Assert.assertEquals(DeliveryOption.ROYALMAIL, listing.getDeliveryOption());
		
		List<String> mciFulfillmentMethodId = new ArrayList<String>();
		mciFulfillmentMethodId.add("7");
		mciFulfillmentMethodId.add("10");
		listing.setId("12");
		task.setAllContext(listing, 1, mciFulfillmentMethodId,null, context);
		listing = task.call();
		Assert.assertEquals(DeliveryOption.UPS, listing.getDeliveryOption());
	}
}
