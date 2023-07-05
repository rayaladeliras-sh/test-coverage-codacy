package com.stubhub.domain.account.mapper;

import junit.framework.Assert;

import org.apache.solr.common.SolrDocument;
import org.testng.annotations.Test;

import com.stubhub.domain.account.common.enums.OrderStatus;

public class OrderStatusMapperTest {
	@Test
	public void testMap() throws Exception{
		OrderStatusMapper osm = new OrderStatusMapper();
		
		RowMeta rm = new RowMeta(new String[]{"ORDER_PROC_STATUS_ID"}, null, null, null);
		SolrDocument sd = new SolrDocument();
		sd.setField("ORDER_PROC_STATUS_ID", 2000L);
		
		Assert.assertEquals(OrderStatus.CONFIRMED.toString(), osm.map(sd, rm).toString());
	}
}
