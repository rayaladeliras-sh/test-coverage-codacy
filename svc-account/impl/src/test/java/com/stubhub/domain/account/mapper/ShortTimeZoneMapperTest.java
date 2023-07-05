package com.stubhub.domain.account.mapper;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.solr.common.SolrDocument;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ShortTimeZoneMapperTest {
	@Test
	public void testMap() throws Exception{
		ShortTimeZoneMapper mapper = new ShortTimeZoneMapper();
		RowMeta meta = new RowMeta(new String[]{"zone","time"}, null, null, mapper);
		SolrDocument row = new SolrDocument();
		row.setField("time", "2014-09-27T02:00:00Z");
		row.setField("zone", "PST");
		Assert.assertEquals(mapper.map(row, meta).toString(), "PDT");
		
		row.setField("time", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse("2014-09-27T02:00:00Z"));
		Assert.assertEquals(mapper.map(row, meta).toString(), "PDT");
		
		Calendar c = Calendar.getInstance();
		c.set(Calendar.MONTH, 6);
		row.setField("time", c);
		Assert.assertEquals(mapper.map(row, meta).toString(), "PDT");
	}
}
