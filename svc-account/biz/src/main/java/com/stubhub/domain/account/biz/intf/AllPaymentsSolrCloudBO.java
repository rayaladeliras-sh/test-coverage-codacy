package com.stubhub.domain.account.biz.intf;

import com.stubhub.domain.account.common.AllPaymentsSearchCriteria;
import org.apache.solr.client.solrj.SolrServerException;
import org.codehaus.jackson.JsonNode;

public interface AllPaymentsSolrCloudBO {

  JsonNode getAllPayments(AllPaymentsSearchCriteria criteria) throws SolrServerException;

}
