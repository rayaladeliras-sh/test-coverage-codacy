/**
 * Copyright 2016-2017 StubHub, Inc.  All rights reserved.
 */
package com.stubhub.domain.account.biz.intf;

import java.util.List;

import org.codehaus.jackson.JsonNode;

import com.stubhub.domain.account.common.ListingSearchCriteria;
import com.stubhub.domain.account.common.MyOrderSearchCriteria;
import com.stubhub.domain.account.common.PaginationInput;
import com.stubhub.domain.account.common.SalesSearchCriteria;

/**
 * AccountSolrCloudBiz
 * 
 * @author runiu
 *
 */
public interface AccountSolrCloudBiz {

	public JsonNode getSellerSales(SalesSearchCriteria criteria);
	public JsonNode getSellerEventSales(SalesSearchCriteria criteria);
	public JsonNode getSellerListings(ListingSearchCriteria criteria);
	public JsonNode getSellerListings(String sellerIds, String eventIds, String status, PaginationInput paginationInput);
	public JsonNode getBuyerOrders(MyOrderSearchCriteria criteria);

	public JsonNode getByIdList(String collection, String idField, List<String> idList);
	
	public JsonNode getCSOrderDetails(SalesSearchCriteria ssc);
}