package com.stubhub.domain.account.mapper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.solr.common.SolrDocument;

import com.stubhub.newplatform.common.entity.Money;

public class DefaultDataMapper implements DataMapper{	
	public Object map(SolrDocument row, RowMeta meta) throws Exception{		
		Object value = row.getFieldValue(meta.getFieldNames()[0]);
		if(value == null)
			return null;
		Class<?> clazz = meta.getFieldClass();
		if(Long.class.equals(clazz)){
			value = Long.parseLong(value.toString());
		}else if(Double.class.equals(clazz)){
			value = Double.parseDouble(value.toString());
		}else if(Money.class.equals(clazz)){
			Object currency = row.getFieldValue(meta.getFieldNames()[1]);
			Money money;
			if(currency!=null)
				money = new Money(value.toString(), currency.toString());
			else
				money = new Money(value.toString());
			value = money;
		}else if(Calendar.class.equals(clazz)){
			Calendar c = Calendar.getInstance();
			if(value instanceof Date){				
				c.setTime((Date)value);				
			}else{
				c.setTime(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(value.toString()));
			}
			value = c;
		}
		return value;
	}	
}
