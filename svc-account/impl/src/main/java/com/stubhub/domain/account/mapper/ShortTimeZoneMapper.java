package com.stubhub.domain.account.mapper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.solr.common.SolrDocument;






public class ShortTimeZoneMapper implements DataMapper{
	public Object map(SolrDocument row, RowMeta meta) throws Exception {
		String timezoneString = meta.getFieldNames()[0];
		String dateString = meta.getFieldNames()[1];
		Object timezone = row.getFieldValue(timezoneString);
		if(timezone==null)
			return null;
		TimeZone tz = TimeZone.getTimeZone(timezone.toString());
		Object date = row.getFieldValue(dateString);
		
		boolean daylightTime = false;
		if(date!=null){
			Date d = null;
			if(date instanceof Date)
				d = (Date)date;
			else if(date instanceof Calendar)
				d = ((Calendar)date).getTime();
			else if(date instanceof String)
				d = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse((String)date);
			if(d !=null)
				daylightTime = tz.inDaylightTime(d);
		}
		return tz.getDisplayName(daylightTime, 0);
	}
}
