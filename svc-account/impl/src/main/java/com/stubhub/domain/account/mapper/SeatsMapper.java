package com.stubhub.domain.account.mapper;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.common.SolrDocument;

import com.stubhub.domain.account.common.util.ValidationUtil;
import com.stubhub.domain.account.intf.MyOrderResponse.Seat;


public class SeatsMapper implements DataMapper {

	@Override
	public Object map(SolrDocument row, RowMeta meta) throws Exception {
		List<Seat> result = new ArrayList<Seat>();
		String section = row.get(meta.getFieldNames()[0])!=null?row.get(meta.getFieldNames()[0]).toString():"";
		String ROW = row.get(meta.getFieldNames()[1])!=null?row.get(meta.getFieldNames()[1]).toString():"";
		String seats = row.get(meta.getFieldNames()[2])!=null?row.get(meta.getFieldNames()[2]).toString():"";		
		String[] seatArray = StringUtils.split(seats, ",");		
		for(String seat : seatArray){
			boolean isGA = ValidationUtil.isGA(section, ROW, seats);
			result.add(new Seat(StringUtils.trimToEmpty(seat),StringUtils.trimToEmpty(section), StringUtils.trimToEmpty(ROW),isGA));
		}		
		return result;
	}

}
