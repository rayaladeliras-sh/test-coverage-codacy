package com.stubhub.domain.account.mapper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.solr.common.SolrDocument;
import org.testng.Assert;
import org.testng.annotations.Test;




public class LocalTimeZoneMapperTest {
	@Test
	public void testMap() throws Exception{
		LocalTimeZoneMapper mapper = new LocalTimeZoneMapper ();
		RowMeta rm = new RowMeta(new String[]{"EXPECTED_DELIVERY_DATE","JDK_TIMEZONE"}, null, null, null);
		SolrDocument sd = new SolrDocument();
		sd.setField("JDK_TIMEZONE", "PST");
		sd.setField("EXPECTED_DELIVERY_DATE", "2014-09-27T02:00:00Z");
		
				
		Assert.assertEquals(((Calendar)mapper.map(sd, rm)).get(Calendar.HOUR_OF_DAY), 19);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date d = sdf.parse("2014-09-27T02:00:00Z");
		sd.setField("EXPECTED_DELIVERY_DATE", d);
		Assert.assertEquals(((Calendar)mapper.map(sd, rm)).get(Calendar.HOUR_OF_DAY), 19);
		
	}
}
