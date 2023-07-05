package com.stubhub.domain.account.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;

import com.stubhub.newplatform.common.entity.Money;

public class ValidationUtil {
	
	public  static boolean isValidLong(String value) {
		try {
			Long.parseLong(value);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static boolean isValidBoolean(String value){
		if (value != null){
		if ("true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value)){
			return true;
		} else {
			return false;
		}
		} else return false;
	}
	
	public static boolean isValidMoney(Money obj) {
		if (obj != null){
			Double.parseDouble(obj.getAmount().toString());
			if (StringUtils.trimToNull(obj.getCurrency()) == null) 
				return false;
			return true;
		} else 
			return false;
	}

	public static boolean isGA(String section, String row, String seat){
		return "General Admission".equals(section) && ("GA".equals(row) || "N/A".equals(row));
//				&& (seat == null || seat.isEmpty() || "General Admission".equals(seat))
	}
	
	public static String formatDate(Calendar cal, String timeZoneStr) {
		String dateFormat = "yyyy-MM-dd'T'HH:mm:ssZ";
		Calendar newcal = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.MINUTE),cal.get(Calendar.SECOND));
		newcal.setTimeZone(TimeZone.getTimeZone(timeZoneStr));
		SimpleDateFormat sf = new SimpleDateFormat(dateFormat);
		sf.setTimeZone(TimeZone.getTimeZone(timeZoneStr));
		return sf.format(newcal.getTime());
	}
}
