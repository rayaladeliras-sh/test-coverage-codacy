package com.stubhub.domain.account.mapper;

import java.util.Calendar;
import java.util.Date;

import org.apache.solr.common.SolrDocument;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.stubhub.newplatform.common.entity.Money;


public class DefaultDataMapperTest {
	@Test
	public void testMap() throws Exception{
		DefaultDataMapper mapper = new DefaultDataMapper();
		RowMeta meta = new RowMeta(new String[]{"TID"}, Long.class, null, mapper);
		SolrDocument row = new SolrDocument();
		Assert.assertNull(mapper.map(row, meta));
		
		row.setField("TID", "1234");
		Assert.assertEquals(mapper.map(row, meta).toString(), "1234");
		
		meta.setFieldClass(Double.class);
		row.setField("TID", "1234.34");
		Assert.assertEquals((Double)mapper.map(row, meta), new Double(1234.34));
		
		meta.setFieldClass(Money.class);
		meta.setFieldNames(new String[]{"price","currencyCode"});
		row.setField("price", "1111");
		
		Money result = (Money)mapper.map(row, meta);
		Assert.assertEquals(result.getAmount().toString(), "1111.00");
		Assert.assertEquals(result.getCurrency(), "USD");
		
		row.setField("currencyCode", "GBP");
		result = (Money)mapper.map(row, meta);
		Assert.assertEquals(result.getCurrency(), "GBP");
		
		meta.setFieldClass(Calendar.class);
		meta.setFieldNames(new String[]{"time"});
		row.setField("time", "2014-09-27T02:00:00Z");
		Calendar c = (Calendar)mapper.map(row, meta);
		Assert.assertEquals(c.get(Calendar.YEAR), 2014);
		
		row.setField("time", new Date());
		c = (Calendar)mapper.map(row, meta);		
		Assert.assertEquals(c.get(Calendar.YEAR), new Date().getYear() + 1900);
	}
}
