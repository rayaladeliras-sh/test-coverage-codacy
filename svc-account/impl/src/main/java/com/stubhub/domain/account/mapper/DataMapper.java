package com.stubhub.domain.account.mapper;

import org.apache.solr.common.SolrDocument;


public interface DataMapper {
	Object map(SolrDocument row, RowMeta meta) throws Exception;
}
