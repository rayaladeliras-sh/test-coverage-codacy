package com.stubhub.domain.account.biz.impl;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServerException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.stubhub.domain.account.common.PaymentsSearchCriteria;
import com.stubhub.newplatform.http.util.HttpClient4Util.SimpleHttpResponse;
import com.stubhub.newplatform.http.util.HttpClient4UtilHelper;
import com.stubhub.newplatform.property.MasterStubhubPropertiesWrapper;

public class SellerPaymentSolrCloudBOImplTest {
	@InjectMocks
	private SellerPaymentSolrCloudBOImpl sellerPaymentBiz;
	@Mock
	private HttpClient4UtilHelper httpClient4UtilHelper;
	@Mock
	private MasterStubhubPropertiesWrapper masterStubhubProperties;

	@BeforeMethod
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		sellerPaymentBiz.afterPropertiesSet();
		Mockito.when(masterStubhubProperties.getProperty(MCIRequestUtil.SEARCH_MCI_API_URL_PROP_NAME,
				MCIRequestUtil.DEFAULT_SEARCH_MCI_API_V1)).thenReturn("http://api-int.slcq048.com/search/mci/v1");
		Mockito.when(masterStubhubProperties.getProperty(MCIRequestUtil.SEARCH_MCI_API_TIME_OUT_PROP_NAME,
				MCIRequestUtil.DEFAULT_SEARCH_MCI_API_TIME_OUT))
				.thenReturn(MCIRequestUtil.DEFAULT_SEARCH_MCI_API_TIME_OUT).thenReturn("abc");
	}

	@Test
	public void testGetSellerPayments() throws Exception {
		SimpleHttpResponse value = Mockito.mock(SimpleHttpResponse.class);
		Mockito.when(value.getContent()).thenReturn("{}");
		Mockito.when(httpClient4UtilHelper.postToUrl(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyList(), Mockito.anyInt(),
				Mockito.anyBoolean())).thenReturn(value);
		PaymentsSearchCriteria request = new PaymentsSearchCriteria();
		request.setSellerId("1234");
		request.setStart(0);
		request.setRows(10);
		JsonNode node = sellerPaymentBiz.getSellerPayments(request);
		Assert.assertNotNull(node);

		node = sellerPaymentBiz.getSellerPayments(request);
		Assert.assertNotNull(node);
	}

	@Test
	public void testGetSellerPaymentsExcludeFundCapture() throws Exception {
		SimpleHttpResponse value = Mockito.mock(SimpleHttpResponse.class);
		Mockito.when(value.getContent()).thenReturn("{}");
		Mockito.when(httpClient4UtilHelper.postToUrl(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyList(), Mockito.anyInt(),
				Mockito.anyBoolean())).thenReturn(value);
		PaymentsSearchCriteria request = new PaymentsSearchCriteria();
		request.setSellerId("1234");
		request.setStart(0);
		request.setRows(10);
		request.setCurrency("USD");
		request.setQ("currency:USD,status:Pending");
		JsonNode node = sellerPaymentBiz.getSellerPayments(request);
		Assert.assertNotNull(node);
	}

	@Test
	public void testGetSellerPaymentsExcludeFundCaptureWithoutStatusFilter() throws Exception {
		SimpleHttpResponse value = Mockito.mock(SimpleHttpResponse.class);
		Mockito.when(value.getContent()).thenReturn("{}");
		Mockito.when(httpClient4UtilHelper.postToUrl(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyList(), Mockito.anyInt(),
				Mockito.anyBoolean())).thenReturn(value);
		PaymentsSearchCriteria request = new PaymentsSearchCriteria();
		request.setSellerId("1234");
		request.setStart(0);
		request.setRows(10);
		request.setCurrency("USD");
		request.setQ("currency:USD");
		JsonNode node = sellerPaymentBiz.getSellerPayments(request);
		Assert.assertNotNull(node);
	}

	@Test(expectedExceptions = { SolrServerException.class })
	public void testGetSellerPaymentsWithIOException() throws Exception {
		SimpleHttpResponse value = Mockito.mock(SimpleHttpResponse.class);
		Mockito.when(value.getContent()).thenReturn("{}");
		Mockito.when(httpClient4UtilHelper.postToUrl(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyList(), Mockito.anyInt(),
				Mockito.anyBoolean())).thenThrow(new IOException());
		PaymentsSearchCriteria request = new PaymentsSearchCriteria();
		request.setSellerId("1234");
		sellerPaymentBiz.getSellerPayments(request);
	}

	@Test(expectedExceptions = { SolrServerException.class })
	public void testGetSellerPaymentsWithRuntimeException() throws Exception {
		SimpleHttpResponse value = Mockito.mock(SimpleHttpResponse.class);
		Mockito.when(value.getContent()).thenReturn("{}");
		Mockito.when(httpClient4UtilHelper.postToUrl(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyList(), Mockito.anyInt(),
				Mockito.anyBoolean())).thenThrow(new RuntimeException());
		PaymentsSearchCriteria request = new PaymentsSearchCriteria();
		request.setSellerId("1234");
		sellerPaymentBiz.getSellerPayments(request);
	}

	@Test
	public void testBuildRequest() throws Exception {
		PaymentsSearchCriteria request = new PaymentsSearchCriteria();
		request.setSellerId("1234");
		request.setIncludeCreditMemo(true);
		request.setQ(
				"keyword:abc, abc:123,paymentDate:[2013-01-01 TO 2013-09-30],paymentInitiatedDate:[2013-01-01 TO 2013-09-30],currency:USD,paymentAmount:[20 TO *],paymentType:PayPal,status:Sent,holdPaymentType:2");
		request.setIncludePaymentSummary(true);
		ObjectNode node = sellerPaymentBiz.buildQueryRequest(request);
		Assert.assertNotNull(node.get("filter"));
		Assert.assertNotNull(node.get("query"));
		ArrayNode sortNode = (ArrayNode) node.get("sort");
		Assert.assertNotNull(sortNode);
		Assert.assertEquals(sortNode.size(), 2);
		Assert.assertNotNull(node.get("aggregations"));

		request.setQ(
				"paymentDate:2013-01-01 TO 2013-09-30,status:Wrong,paymentType:Wrong,paymentAmount:* TO *,paymentInitiatedDate:2013-01-01 TO 2013-09-30,abc");
		node = sellerPaymentBiz.buildQueryRequest(request);
		Assert.assertNotNull(node.get("filter"));
		Assert.assertNull(node.get("query"));
		sortNode = (ArrayNode) node.get("sort");
		Assert.assertNotNull(sortNode);
		Assert.assertEquals(sortNode.size(), 2);

		request.setQ(
				"paymentDate:2013-01-01,status:Wrong,paymentType:Wrong,paymentAmount:* TO *,paymentInitiatedDate:2013-01-01");
		node = sellerPaymentBiz.buildQueryRequest(request);
		Assert.assertNotNull(node.get("filter"));
		Assert.assertNull(node.get("query"));
		sortNode = (ArrayNode) node.get("sort");
		Assert.assertNotNull(sortNode);
		Assert.assertEquals(sortNode.size(), 2);

		request.setQ(
				"paymentDate:[2013-01-01,status:Wrong,paymentType:Wrong,paymentAmount:* TO *,paymentInitiatedDate:[2013-01-01 TO 2015-01-01,currency:,paymentAmount:1,holdPaymentType:2");
		request.setSort("orderID desc");
		node = sellerPaymentBiz.buildQueryRequest(request);
		Assert.assertNotNull(node.get("filter"));
		Assert.assertNull(node.get("query"));
		sortNode = (ArrayNode) node.get("sort");
		Assert.assertNotNull(sortNode);
		Assert.assertEquals(sortNode.size(), 1);

		request.setSort("paymentAmount asc");
		node = sellerPaymentBiz.buildQueryRequest(request);
		sortNode = (ArrayNode) node.get("sort");
		Assert.assertNotNull(sortNode);
		Assert.assertEquals(sortNode.size(), 2);

		request.setSort("status asc");
		node = sellerPaymentBiz.buildQueryRequest(request);
		sortNode = (ArrayNode) node.get("sort");
		Assert.assertNotNull(sortNode);
		Assert.assertEquals(sortNode.size(), 2);

		request.setSort("paymentType asc");
		node = sellerPaymentBiz.buildQueryRequest(request);
		sortNode = (ArrayNode) node.get("sort");
		Assert.assertNotNull(sortNode);
		Assert.assertEquals(sortNode.size(), 2);

		request.setSort("paymentInitiatedDate asc");
		node = sellerPaymentBiz.buildQueryRequest(request);
		sortNode = (ArrayNode) node.get("sort");
		Assert.assertNotNull(sortNode);
		Assert.assertEquals(sortNode.size(), 2);

		request.setSort("paymentInitiatedDate");
		node = sellerPaymentBiz.buildQueryRequest(request);
		sortNode = (ArrayNode) node.get("sort");
		Assert.assertNotNull(sortNode);
		Assert.assertEquals(sortNode.size(), 1);

		request.setSort("paymentInitiatedDateWrong asc");
		node = sellerPaymentBiz.buildQueryRequest(request);
		sortNode = (ArrayNode) node.get("sort");
		Assert.assertNotNull(sortNode);
		Assert.assertEquals(sortNode.size(), 2);
        request.setIncludeCurrencySummary(request.isIncludeCurrencySummary());
        
        request.setIncludeCurrencySummary(true);
		node = sellerPaymentBiz.buildQueryRequest(request);
		ArrayNode aggregations = (ArrayNode)node.get("aggregations");
		Assert.assertNotNull(aggregations);

		request.setSort("holdPaymentType asc");
		node = sellerPaymentBiz.buildQueryRequest(request);
		sortNode = (ArrayNode) node.get("sort");
		Assert.assertNotNull(sortNode);
		Assert.assertEquals(sortNode.size(), 2);
	}

	@Test
	public void testQuerySellerPayments() throws Exception {
		SimpleHttpResponse value = Mockito.mock(SimpleHttpResponse.class);
		Mockito.when(value.getContent()).thenReturn("{}");
		Mockito.when(httpClient4UtilHelper.postToUrl(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyList(), Mockito.anyInt(),
				Mockito.anyBoolean())).thenReturn(value);
		JsonNode node = sellerPaymentBiz.querySellerPayment(1234L, "99M88263TK138600N");
		Assert.assertNotNull(node);
	}

	@Test(expectedExceptions = { SolrServerException.class })
	public void testQuerySellerPaymentsWithIOException() throws Exception {
		SimpleHttpResponse value = Mockito.mock(SimpleHttpResponse.class);
		Mockito.when(value.getContent()).thenReturn("{}");
		Mockito.when(httpClient4UtilHelper.postToUrl(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyList(), Mockito.anyInt(),
				Mockito.anyBoolean())).thenThrow(new IOException());
		sellerPaymentBiz.querySellerPayment(1234L, "99M88263TK138600N");
	}

	@Test(expectedExceptions = { SolrServerException.class })
	public void testQuerySellerPaymentsWithRuntimeException() throws Exception {
		SimpleHttpResponse value = Mockito.mock(SimpleHttpResponse.class);
		Mockito.when(value.getContent()).thenReturn("{}");
		Mockito.when(httpClient4UtilHelper.postToUrl(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyList(), Mockito.anyInt(),
				Mockito.anyBoolean())).thenThrow(new RuntimeException());
		sellerPaymentBiz.querySellerPayment(1234L, "99M88263TK138600N");
	}

	@Test
	public void testGetCSSellerPayments() throws Exception {
    	PaymentsSearchCriteria paymentSC = new PaymentsSearchCriteria();
    	paymentSC.setQ("orderID:(12345 OR 12346)");
    	paymentSC.setIncludeCreditMemo(true);
    	
		SimpleHttpResponse value = Mockito.mock(SimpleHttpResponse.class);
		Mockito.when(value.getContent()).thenReturn("{}");
		Mockito.when(httpClient4UtilHelper.postToUrl(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyList(), Mockito.anyInt(),
				Mockito.anyBoolean())).thenReturn(value);
		JsonNode node = sellerPaymentBiz.getCSSellerPayments(paymentSC);
		Assert.assertNotNull(node);
	}

	@Test(expectedExceptions = { SolrServerException.class })
	public void testGetCSSellerPaymentsWithIOException() throws Exception {
    	PaymentsSearchCriteria paymentSC = new PaymentsSearchCriteria();
    	paymentSC.setQ("orderID:(12345 OR 12346)");
    	paymentSC.setIncludeCreditMemo(true);
		SimpleHttpResponse value = Mockito.mock(SimpleHttpResponse.class);
		Mockito.when(value.getContent()).thenReturn("{}");
		Mockito.when(httpClient4UtilHelper.postToUrl(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyList(), Mockito.anyInt(),
				Mockito.anyBoolean())).thenThrow(new IOException());
		JsonNode node = sellerPaymentBiz.getCSSellerPayments(paymentSC);
	}

	@Test(expectedExceptions = { SolrServerException.class })
	public void testGetCSSellerPaymentsWithRuntimeException() throws Exception {
    	PaymentsSearchCriteria paymentSC = new PaymentsSearchCriteria();
    	paymentSC.setQ("orderID:(12345 OR 12346)");
    	paymentSC.setIncludeCreditMemo(true);
		SimpleHttpResponse value = Mockito.mock(SimpleHttpResponse.class);
		Mockito.when(value.getContent()).thenReturn("{}");
		Mockito.when(httpClient4UtilHelper.postToUrl(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyList(), Mockito.anyInt(),
				Mockito.anyBoolean())).thenThrow(new RuntimeException());
		JsonNode node = sellerPaymentBiz.getCSSellerPayments(paymentSC);
	}

}
