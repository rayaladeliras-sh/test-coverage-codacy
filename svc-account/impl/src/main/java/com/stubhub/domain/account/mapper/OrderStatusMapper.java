package com.stubhub.domain.account.mapper;

import org.apache.solr.common.SolrDocument;

import com.stubhub.domain.account.common.enums.OrderStatus;

public class OrderStatusMapper implements DataMapper {

	@Override
	public Object map(SolrDocument row, RowMeta meta) throws Exception {
		Long value = (Long)row.get(meta.getFieldNames()[0]);
		if(value !=null)
			return OrderStatus.getById(value.intValue()).name();
		return null;
	}

}
