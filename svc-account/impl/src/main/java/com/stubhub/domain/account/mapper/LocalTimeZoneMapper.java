package com.stubhub.domain.account.mapper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.solr.common.SolrDocument;

import com.stubhub.newplatform.common.util.DateUtil;



public class LocalTimeZoneMapper implements DataMapper{
	@Override
	public Object map(SolrDocument row, RowMeta meta) throws Exception {
		String timezoneString = meta.getFieldNames()[1];
		String dateString = meta.getFieldNames()[0];
		Object timezone = row.getFieldValue(timezoneString);
		if(timezone==null)
			return null;
		TimeZone tz = TimeZone.getTimeZone(timezone.toString());
		Object object = row.getFieldValue(dateString);
		Date date = null;
		if(object instanceof Date){
			date = (Date)object;
		}else{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
			date = sdf.parse(object.toString());			
		}
		Calendar srcCal = DateUtil.getNowCalUTC();
		srcCal.setTime(date);
		srcCal.add(Calendar.MILLISECOND, tz.getOffset(srcCal.getTimeInMillis()));
		return srcCal;
	}
}
