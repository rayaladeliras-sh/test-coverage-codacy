package com.stubhub.domain.account.impl;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.util.NamedList;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.stubhub.domain.account.biz.intf.AccountSolrCloudBiz;
import com.stubhub.domain.account.common.Error;
import com.stubhub.domain.account.common.OrderSearchCriteria;
import com.stubhub.domain.account.common.SalesSearchCriteria;
import com.stubhub.domain.account.common.exception.AccountException;
import com.stubhub.domain.account.intf.SalesHistoryRequest;
import com.stubhub.domain.account.intf.SalesHistoryResponse;
import com.stubhub.domain.account.intf.SalesHistoryService;
import com.stubhub.newplatform.property.MasterStubHubProperties;
import com.stubhub.newplatform.property.loader.IConfigLoader;
import com.stubhub.platform.utilities.webservice.security.ExtendedSecurityContext;

public class SalesHistoryServiceTest {
	
	private ExtendedSecurityContext securityContext;
	private SalesHistoryService service;
	private QueryResponse queryResponse;
    private AccountSolrCloudBiz accountSolrCloudBiz;

    ObjectMapper om = new ObjectMapper();

    @BeforeClass
    public static void setupClass() throws Exception {
        MasterStubHubProperties.setLoaders(Arrays.<IConfigLoader>asList(new IConfigLoader() {
            
            @Override
            public Map<String, String> load() throws Exception {
                return Collections.emptyMap();
            }
            
        }));
        MasterStubHubProperties.load();
    }
    
	@BeforeMethod
	public void setUp() throws Exception {
		service = new SalesHistoryServiceImpl();
		securityContext = Mockito.mock(ExtendedSecurityContext.class);
		
		accountSolrCloudBiz = Mockito.mock(AccountSolrCloudBiz.class);
		ReflectionTestUtils.setField(service, "accountSolrCloudBiz", accountSolrCloudBiz);
		
		queryResponse = Mockito.mock(QueryResponse.class);
		MasterStubHubProperties.setProperty("account.v1.saleshistory.useSolrCloud", "true");
		Mockito.when(securityContext.getUserId()).thenReturn("12345");
		Mockito.when(securityContext.getUserGuid()).thenReturn("B5D14E323CD55E9FE04400144F8AE084");
	}

	@Test
	public void testGetSalesSummarySolrCloud(){		
		SalesHistoryRequest request = getSalesSummaryRequest();

			JsonNode jsonNode = om.createObjectNode();
			when(accountSolrCloudBiz.getCSOrderDetails(Mockito.any(SalesSearchCriteria.class))).thenReturn(jsonNode);

			SalesHistoryResponse response = service.getSalesHistory(request, securityContext);
			assertNotNull(response);
			
			request.setPriceType("listprice");
			response = service.getSalesHistory(request, securityContext);
			assertNotNull(response);
			
			request = new SalesHistoryRequest();
			request.setEventId("12345");
			request.setPriceType("listprice");
			response = service.getSalesHistory(request, securityContext);
			assertNotNull(response);

	}
	
	@Test
	public void testGetSalesSummary_InvalidSecurityContext(){		
		SalesHistoryRequest request = getSalesSummaryRequest();		
		SalesHistoryResponse response = service.getSalesHistory(request, null);
		assertNotNull(response.getErrors());
	}
	
	private SalesHistoryRequest getSalesSummaryRequest(){
		SalesHistoryRequest request = new SalesHistoryRequest();
		request.setDeliveryOptions("fedex");
		request.setEventId("12345");
		request.setFromDate("2013-03-12");
		request.setQuantity("2");
		request.setRows("rows");
		request.setSectionIds("1,2,3");
		request.setZoneIds("1,2,3");
		request.setToDate("2013-04-23");
		return request;
	}
	
	private SolrDocument mockOrderSolrDocument(long id, String status, long eventId, float price, String deliveryOptionId, String fulfillmentMethod, String ticketMedium) {
		SolrDocument doc = new SolrDocument();
		doc.setField("TICKET_ID", id + "");
		doc.setField("TID", id + "");
		doc.setField("TICKET_STATUS", status);
		doc.setField("EXTERNAL_LISTING_ID", "10000");
		doc.setField("SECTION", "Lower");
		doc.setField("ROW_DESC", "1,2");
		doc.setField("SEATS", "1,2,3");
	
		doc.setField("QUANTITY", "2");
		doc.setField("SOLD_QUANTITY", 2);
		doc.setField("CURRENCY_CODE", "USD");
		doc.setField("VAT_SELL_FEE", price + "");
		doc.setField("SELLER_FEE_VAL", price + "");
		doc.setField("TICKET_COST", price + "");
		doc.setField("TOTAL_COST", price + "");
		doc.setField("PRICE_PER_TICKET", price);
		doc.setField("STATS_COST_PER_TICKET", price);
		doc.setField("DELIVERY_OPTION_ID", deliveryOptionId);
		doc.setField("TICKET_MEDIUM_ID",ticketMedium);
		doc.setField("LMS_APPROVAL_STATUS_ID", "0");
		doc.setField("SELLER_PAYOUT_AMOUNT", 80.00f);		
		doc.setField("EXPECTED_INHAND_DATE", new Date());
		doc.setField("INHAND_DATE", "2012-11-03T12:00:00Z");
		doc.setField("TRANSACTION_DATE", "2012-09-03T12:00:00Z");
	
		doc.setField("SOLD_DATE", new Date());
	
		doc.setField("SALE_END_DATE", "2013-11-03T12:00:00Z");
		
		List<String> traits = new ArrayList<String>();
		traits.add("959,966,102,203,601,101|Actual 4th row,50 yd line,Parking Pass,Alcohol-free seating,Student Ticket,Aisle|3,3,1,2,2,1|Seller Comments,Seller Comments,Ticket Feature,Listing Disclosure,Listing Disclosure,Ticket Feature");
		doc.setField("SEAT_TRAITS", traits);
		doc.setField("EVENT_DESCRIPTION", "U2");
		doc.setField("EVENT_ID", eventId + "");
		doc.setField("EVENT_DATE_LOCAL", "2014-04-03T12:00:00Z");
		doc.setField("VENUE_DESCRIPTION", "O2 Arena");
		doc.setField("VENUE_CONFIG_SECTIONS_ID", 271756L);
		doc.setField("FULFILLMENT_METHOD_ID", fulfillmentMethod);
		doc.setField("ORDER_PROC_STATUS_ID", 4000L);
		doc.setField("ORDER_PROC_SUB_STATUS_CODE", 50L);
		doc.setField("SALE_CATEGORY","3");
		doc.setField("SELLER_NOTES","TEST");
		doc.setField("CITY","San Francisco");
		doc.setField("COUNTRY","United States");
		return doc;
	}
	
	@Test
	public void testGetSalesSummaryInputErrorsMissingEventId(){		
		SalesHistoryRequest request = getSalesSummaryRequest();
		request.setEventId("");
		SalesHistoryResponse response = service.getSalesHistory(request, securityContext);
		assertNotNull(response.getErrors());
	}
	
	@Test
	public void testGetSalesSummaryInputErrorsMissingEventIdSolrCloud(){

		
		SalesHistoryRequest request = getSalesSummaryRequest();
		request.setEventId("");

			JsonNode jsonNode = om.createObjectNode();
			when(accountSolrCloudBiz.getCSOrderDetails(Mockito.any(SalesSearchCriteria.class))).thenReturn(jsonNode);

			SalesHistoryResponse response = service.getSalesHistory(request, securityContext);
			assertNotNull(response);
			

	
	}
	
	@Test
	public void testGetSalesSummaryInputErrorsInvalidEventId(){		
		SalesHistoryRequest request = getSalesSummaryRequest();
		request.setEventId("abc");
		SalesHistoryResponse response = service.getSalesHistory(request, securityContext);
		assertNotNull(response.getErrors());
	}
	
	@Test
	public void testGetSalesSummaryInputErrorsInvalidEventIdSolrCloud(){

		
		SalesHistoryRequest request = getSalesSummaryRequest();
		request.setEventId("abc");

			JsonNode jsonNode = om.createObjectNode();
			when(accountSolrCloudBiz.getCSOrderDetails(Mockito.any(SalesSearchCriteria.class))).thenReturn(jsonNode);

			SalesHistoryResponse response = service.getSalesHistory(request, securityContext);
			assertNotNull(response);
			

	
	}
	
	@Test
	public void testGetSalesSummaryInputErrorsInvaliQuantity(){		
		SalesHistoryRequest request = getSalesSummaryRequest();
		request.setQuantity("a");
		SalesHistoryResponse response = service.getSalesHistory(request, securityContext);
		assertNotNull(response.getErrors());
	}
	
	@Test
	public void testGetSalesSummaryInputErrorsInvalidQuantitySolrCloud(){

		
		SalesHistoryRequest request = getSalesSummaryRequest();
		request.setQuantity("a");

			JsonNode jsonNode = om.createObjectNode();
			when(accountSolrCloudBiz.getCSOrderDetails(Mockito.any(SalesSearchCriteria.class))).thenReturn(jsonNode);

			SalesHistoryResponse response = service.getSalesHistory(request, securityContext);
			assertNotNull(response);
			

	
	}
	
	@Test
	public void testGetSalesSummaryInputErrorsInvaliDate(){		
		SalesHistoryRequest request = getSalesSummaryRequest();
		request.setFromDate("abcd");
		SalesHistoryResponse response = service.getSalesHistory(request, securityContext);
		assertNotNull(response.getErrors());
	}
	
	@Test
	public void testGetSalesSummaryInputErrorsInvalidDateSolrCloud(){
		
		SalesHistoryRequest request = getSalesSummaryRequest();
		request.setFromDate("abcd");

			JsonNode jsonNode = om.createObjectNode();
			when(accountSolrCloudBiz.getCSOrderDetails(Mockito.any(SalesSearchCriteria.class))).thenReturn(jsonNode);

			SalesHistoryResponse response = service.getSalesHistory(request, securityContext);
			assertNotNull(response);
			

	}
	
}
