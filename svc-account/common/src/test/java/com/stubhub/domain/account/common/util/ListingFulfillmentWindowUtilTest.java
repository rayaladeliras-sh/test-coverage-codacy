package com.stubhub.domain.account.common.util;

import com.stubhub.domain.account.common.ListingFulfillmentWindow;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.*;

public class ListingFulfillmentWindowUtilTest {

    @Test
    public void testGetFulfillmentWindowList() throws Exception {
        List<ListingFulfillmentWindow> fulfillmentWindowList = ListingFulfillmentWindowUtil.getFulfillmentWindowList("3,1,2,,|1,2,2,,|2,2,2,,");
        assertNotNull(fulfillmentWindowList);
        assertEquals(fulfillmentWindowList.size(), 3);
        assertEquals(fulfillmentWindowList.get(0).getFulfillmentMethodId(), Long.valueOf(3L));
        assertEquals(fulfillmentWindowList.get(0).getDeliveryMethodId(), Long.valueOf(1L));
        assertEquals(fulfillmentWindowList.get(0).getBaseCost(), Double.valueOf(2d));
        assertEquals(fulfillmentWindowList.get(0).getStartDate(), null);
        assertEquals(fulfillmentWindowList.get(0).getEndDate(), null);

        fulfillmentWindowList = ListingFulfillmentWindowUtil.getFulfillmentWindowList("3,1,2,2000-01-02T03:04:05Z,3000-01-02T03:04:05Z");

        assertNotNull(fulfillmentWindowList);
        assertEquals(fulfillmentWindowList.size(), 1);
        assertNotNull(fulfillmentWindowList.get(0).getStartDate());
        assertNotNull(fulfillmentWindowList.get(0).getEndDate());
    }
}