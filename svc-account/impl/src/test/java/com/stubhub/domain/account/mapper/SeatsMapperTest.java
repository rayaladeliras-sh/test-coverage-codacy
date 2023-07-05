package com.stubhub.domain.account.mapper;

import java.util.List;

import org.apache.solr.common.SolrDocument;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.stubhub.domain.account.intf.MyOrderResponse.Seat;

public class SeatsMapperTest {
	@Test
	public void testMap() throws Exception{
		SeatsMapper sm = new SeatsMapper();
		SolrDocument sd = new SolrDocument();
		sd.setField("SECTION", "   section");
		sd.setField("ROW_DESC", null);
		sd.setField("SEATS", "1, 2 , 3");
		RowMeta rm = new RowMeta(new String[]{"SECTION","ROW_DESC","SEATS"}, null, null, null);
		
		List<Seat> seats = (List<Seat>) sm.map(sd, rm);
		String seat1 = seats.get(0).seatNumber;
		String seat2 = seats.get(1).seatNumber;
		String seat3 = seats.get(2).seatNumber;
		Assert.assertEquals(seats.get(0).primarySection, "section");
		Assert.assertEquals(seats.get(0).row, "");
		Assert.assertEquals(seat1, "1");
		Assert.assertEquals(seat2, "2");
		Assert.assertEquals(seat3, "3");
	}
}
