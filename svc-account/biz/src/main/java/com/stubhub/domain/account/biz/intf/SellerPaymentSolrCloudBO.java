/**
 * Copyright 2016 StubHub, Inc.  All rights reserved.
 */
package com.stubhub.domain.account.biz.intf;

import org.apache.solr.client.solrj.SolrServerException;
import org.codehaus.jackson.JsonNode;

import com.stubhub.domain.account.common.PaymentsSearchCriteria;

/**
 * SellerPaymentSolrCloudBO
 * 
 * @author runiu
 *
 */
public interface SellerPaymentSolrCloudBO {
	JsonNode getSellerPayments(PaymentsSearchCriteria criteria) throws SolrServerException;

	JsonNode getCSSellerPayments(PaymentsSearchCriteria criteria) throws SolrServerException;

	JsonNode querySellerPayment(Long sellerId, String refNumber) throws SolrServerException;
}