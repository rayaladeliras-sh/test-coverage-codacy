package com.stubhub.domain.account.impl;

import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.io.InputStream;
import java.util.*;

import com.stubhub.domain.account.biz.impl.AccountSolrCloudBizImpl;
import com.stubhub.domain.account.biz.intf.AccountSolrCloudBiz;
import com.stubhub.newplatform.property.MasterStubHubProperties;
import com.stubhub.newplatform.property.loader.IConfigLoader;
import junit.framework.Assert;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.stubhub.domain.account.biz.impl.EventUtil;
import com.stubhub.domain.account.biz.intf.CreditCardChargeBO;
import com.stubhub.domain.account.biz.intf.OrderSolrBO;
import com.stubhub.domain.account.common.SalesSearchCriteria;
import com.stubhub.domain.account.datamodel.entity.CcTransReason;
import com.stubhub.domain.account.datamodel.entity.SellerCcTrans;
import com.stubhub.domain.account.exception.UnauthorizedException;
import com.stubhub.domain.account.helper.PaymentHelper;
import com.stubhub.domain.account.intf.CreditCardChargesResponse;
import com.stubhub.domain.account.intf.CreditCardChargesService;
import com.stubhub.domain.user.payments.intf.CreditCardDetails;
import com.stubhub.platform.utilities.webservice.security.ExtendedSecurityContext;

public class CreditCardChargesServiceTest {
	
	private ExtendedSecurityContext securityContext;
	private CreditCardChargesService service;
	private PaymentHelper helper;
	private CreditCardChargeBO creditCardChargeBO;
	private OrderSolrBO orderSolrBO;
	private EventUtil eventUtil;
	private AccountSolrCloudBiz accountSolrCloudBiz;
	
	@BeforeMethod
	public void setUp() throws Exception {
		service = new CreditCardChargesServiceImpl();
		
		securityContext = Mockito.mock(ExtendedSecurityContext.class);
		
		creditCardChargeBO = Mockito.mock(CreditCardChargeBO.class);
		ReflectionTestUtils.setField(service, "creditCardChargeBO", creditCardChargeBO);
		
		helper = Mockito.mock(PaymentHelper.class);
		ReflectionTestUtils.setField(service, "paymentHelper", helper);

		orderSolrBO = Mockito.mock(OrderSolrBO.class);
		ReflectionTestUtils.setField(service, "orderSolrBO", orderSolrBO);

		eventUtil = Mockito.mock(EventUtil.class);
		ReflectionTestUtils.setField(service, "eventUtil", eventUtil);

		accountSolrCloudBiz = Mockito.mock(AccountSolrCloudBizImpl.class);
		ReflectionTestUtils.setField(service,"accountSolrCloudBiz",accountSolrCloudBiz);

		
		Mockito.when(securityContext.getUserId()).thenReturn("12345");
		Mockito.when(securityContext.getUserGuid()).thenReturn("B5D14E323CD55E9FE04400144F8AE084");
	}


	@Test
	public void testGetCreditCardChargesWithSolrCloud() throws Exception {
		String sellerGuid = "B5D14E323CD55E9FE04400144F8AE084";
		String sort = null;
		String fromDate = "2015-01-02";
		String toDate = "2015-01-01";
		String transactionType = "C,D";
		Long eventId = 9101780L;
		String eventName = "test event";

		Long eventId1 = 9101781L;
		String eventName1 = "test event1";
		Map<Long, String> eventNameMap = new HashMap<Long, String>();
		eventNameMap.put(eventId, eventName);
		eventNameMap.put(eventId1,eventName1);

		Map<Long, CreditCardDetails> sellerCcMap = new HashMap<Long, CreditCardDetails>();
		CreditCardDetails ccDetails = new CreditCardDetails();
		ccDetails.setCardNumber("1234234543456789");
		ccDetails.setLastFourDigits("6749");
		ccDetails.setCardType("VISA");
		sellerCcMap.put(12345L, ccDetails);

		List<SellerCcTrans> sellerCcTransList = new ArrayList<SellerCcTrans>();
		SellerCcTrans sellerCcTrans = new SellerCcTrans();
		sellerCcTrans.setAmount(12.12);
		sellerCcTrans.setCurrencyCode("USD");
		sellerCcTrans.setId(12345L);
		sellerCcTrans.setSellerCcId(12345L);
		CcTransReason ccReason = new CcTransReason();
		ccReason.setReasonCode(123L);
		ccReason.setReasonDescription("aaa");
		//sellerCcTrans.setReasonCode(123L);
		sellerCcTrans.setStatus("success");
		sellerCcTrans.setTid(166225627L);
		sellerCcTrans.setLastUpdatedDate(Calendar.getInstance());
		sellerCcTrans.setTransacionType("C");
		sellerCcTransList.add(sellerCcTrans);

		SellerCcTrans sellerCcTrans1 = new SellerCcTrans();
		sellerCcTrans1.setAmount(12.12);
		sellerCcTrans1.setCurrencyCode("USD");
		sellerCcTrans1.setId(12345L);
		sellerCcTrans1.setSellerCcId(12345L);
		CcTransReason ccReason1 = new CcTransReason();
		ccReason1.setReasonCode(123L);
		ccReason1.setReasonDescription("aaa");
		//sellerCcTrans.setReasonCode(123L);
		sellerCcTrans1.setStatus("success");
		sellerCcTrans1.setTid(166509460L);
		sellerCcTrans1.setLastUpdatedDate(Calendar.getInstance());
		sellerCcTrans1.setTransacionType("C");
		sellerCcTransList.add(sellerCcTrans1);
		ObjectMapper om = new ObjectMapper();
		InputStream is = CreditCardChargesServiceTest.class.getClassLoader().getResourceAsStream("saleResponseForCreditCardCharge.json");
		JsonNode jsonNode = om.readTree(is);
		MasterStubHubProperties.setLoaders(Arrays.<IConfigLoader>asList(new IConfigLoader() {

			@Override
			public Map<String, String> load() throws Exception {
				return Collections.emptyMap();
			}

		}));
		MasterStubHubProperties.load();
		MasterStubHubProperties.setProperty("account.v1.creditCardCharges.useSolrCloud", "true");
		ObjectNode createObjectNode = om.createObjectNode();
		ObjectNode respNode = om.createObjectNode();
		createObjectNode.put("response", respNode);

		ObjectNode createObjectNode2 = om.createObjectNode();
		ObjectNode respNode2 = om.createObjectNode();
		createObjectNode2.put("response", respNode2);
		respNode2.put("docs", om.createArrayNode());

		when(accountSolrCloudBiz.getByIdList(anyString(),anyString(),anyList())).thenReturn(null).thenReturn(om.createObjectNode()).thenReturn(createObjectNode).thenReturn(createObjectNode2).thenReturn(jsonNode);

		when(eventUtil.getEventNames(Mockito.anySet())).thenReturn(eventNameMap);


		when(helper.getMappedValidSellerCcId(Mockito.anyString())).thenReturn(sellerCcMap);
		when(creditCardChargeBO.getCreditCardCharges(Mockito.anySet(), Mockito.anyString(), Mockito.any(Calendar.class), Mockito.any(Calendar.class), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyList(), Mockito.anyString())).thenReturn(sellerCcTransList);

		// null json node
		CreditCardChargesResponse response = service.getCreditCardCharges(securityContext, sellerGuid, sort, fromDate, toDate, null, null, transactionType, "USD");
		Assert.assertNotNull(response);
		// non response node
		response = service.getCreditCardCharges(securityContext, sellerGuid, sort, fromDate, toDate, null, null, transactionType, "USD");
		Assert.assertNotNull(response);
		// no doc node
		response = service.getCreditCardCharges(securityContext, sellerGuid, sort, fromDate, toDate, null, null, transactionType, "USD");
		Assert.assertNotNull(response);
		// zero docs
		response = service.getCreditCardCharges(securityContext, sellerGuid, sort, fromDate, toDate, null, null, transactionType, "USD");
		Assert.assertNotNull(response);

		response = service.getCreditCardCharges(securityContext, sellerGuid, sort, fromDate, toDate, null, null, transactionType, "USD");
		Assert.assertNotNull(response);

		Assert.assertNotNull(response.getCreditCardCharges());
		Assert.assertNotNull(response.getCreditCardCharges().get(0).getChargedAmount());
		Assert.assertNotNull(response.getCreditCardCharges().get(0).getChargedDate());
		Assert.assertNotNull(response.getCreditCardCharges().get(0).getId());
		Assert.assertNotNull(response.getCreditCardCharges().get(0).getOrderID());
		Assert.assertNotNull(response.getCreditCardCharges().get(0).getStatus());
		Assert.assertNotNull(response.getCreditCardCharges().get(0).getTransactionType());
		Assert.assertNotNull(response.getCreditCardCharges().get(0).getCreditCardNumber());
		Assert.assertEquals(response.getCreditCardCharges().get(0).getEventName(), eventName1);
		Assert.assertEquals(response.getCreditCardCharges().get(1).getEventName(), eventName);

		// work with wrong date format
		response = service.getCreditCardCharges(securityContext, sellerGuid, sort, "abcde", toDate, null, null, transactionType, null);
		Assert.assertNotNull(response);
		Assert.assertNotNull(response.getCreditCardCharges());
		// work with default transtype
		response = service.getCreditCardCharges(securityContext, sellerGuid, sort, fromDate, toDate, null, null, null, null);
		Assert.assertNotNull(response);
		Assert.assertNotNull(response.getCreditCardCharges());
	}



	@Test
	public void testGetCreditCardChargesWithSolrCloudAndJsonNodeIsNULL() throws Exception {
		String sellerGuid = "B5D14E323CD55E9FE04400144F8AE084";
		String sort = null;
		String fromDate = "2015-01-02";
		String toDate = "2015-01-01";
		String transactionType = "C,D";
		Long eventId = 9101780L;
		String eventName = "test event";

		Long eventId1 = 9101781L;
		String eventName1 = "test event1";
		Map<Long, String> eventNameMap = new HashMap<Long, String>();
		eventNameMap.put(eventId, eventName);
		eventNameMap.put(eventId1,eventName1);

		Map<Long, CreditCardDetails> sellerCcMap = new HashMap<Long, CreditCardDetails>();
		CreditCardDetails ccDetails = new CreditCardDetails();
		ccDetails.setCardNumber("1234234543456789");
		ccDetails.setLastFourDigits("6749");
		ccDetails.setCardType("VISA");
		sellerCcMap.put(12345L, ccDetails);

		List<SellerCcTrans> sellerCcTransList = new ArrayList<SellerCcTrans>();
		SellerCcTrans sellerCcTrans = new SellerCcTrans();
		sellerCcTrans.setAmount(12.12);
		sellerCcTrans.setCurrencyCode("USD");
		sellerCcTrans.setId(12345L);
		sellerCcTrans.setSellerCcId(12345L);
		CcTransReason ccReason = new CcTransReason();
		ccReason.setReasonCode(123L);
		ccReason.setReasonDescription("aaa");
		//sellerCcTrans.setReasonCode(123L);
		sellerCcTrans.setStatus("success");
		sellerCcTrans.setTid(166225627L);
		sellerCcTrans.setLastUpdatedDate(Calendar.getInstance());
		sellerCcTrans.setTransacionType("C");
		sellerCcTransList.add(sellerCcTrans);

		SellerCcTrans sellerCcTrans1 = new SellerCcTrans();
		sellerCcTrans1.setAmount(12.12);
		sellerCcTrans1.setCurrencyCode("USD");
		sellerCcTrans1.setId(12345L);
		sellerCcTrans1.setSellerCcId(12345L);
		CcTransReason ccReason1 = new CcTransReason();
		ccReason1.setReasonCode(123L);
		ccReason1.setReasonDescription("aaa");
		//sellerCcTrans.setReasonCode(123L);
		sellerCcTrans1.setStatus("success");
		sellerCcTrans1.setTid(166509460L);
		sellerCcTrans1.setLastUpdatedDate(Calendar.getInstance());
		sellerCcTrans1.setTransacionType("C");
		sellerCcTransList.add(sellerCcTrans1);
		ObjectMapper om = new ObjectMapper();
		//InputStream is = CreditCardChargesServiceTest.class.getClassLoader().getResourceAsStream("saleResponseForCreditCardCharge.json");
		JsonNode jsonNode = null;
		MasterStubHubProperties.setLoaders(Arrays.<IConfigLoader>asList(new IConfigLoader() {

			@Override
			public Map<String, String> load() throws Exception {
				return Collections.emptyMap();
			}

		}));
		MasterStubHubProperties.load();
		MasterStubHubProperties.setProperty("account.v1.creditCardCharges.useSolrCloud", "true");
		when(accountSolrCloudBiz.getByIdList(anyString(),anyString(),anyList())).thenReturn(jsonNode);

		when(eventUtil.getEventNames(Mockito.anySet())).thenReturn(eventNameMap);


		when(helper.getMappedValidSellerCcId(Mockito.anyString())).thenReturn(sellerCcMap);
		when(creditCardChargeBO.getCreditCardCharges(Mockito.anySet(), Mockito.anyString(), Mockito.any(Calendar.class), Mockito.any(Calendar.class), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyList(), Mockito.anyString())).thenReturn(sellerCcTransList);

		CreditCardChargesResponse response = service.getCreditCardCharges(securityContext, sellerGuid, sort, fromDate, toDate, null, null, transactionType, "USD");

		Assert.assertNotNull(response);
		Assert.assertNotNull(response.getCreditCardCharges());
		Assert.assertNotNull(response.getCreditCardCharges().get(0).getChargedAmount());
		Assert.assertNotNull(response.getCreditCardCharges().get(0).getChargedDate());
		Assert.assertNotNull(response.getCreditCardCharges().get(0).getId());
		Assert.assertNotNull(response.getCreditCardCharges().get(0).getOrderID());
		Assert.assertNotNull(response.getCreditCardCharges().get(0).getStatus());
		Assert.assertNotNull(response.getCreditCardCharges().get(0).getTransactionType());
		Assert.assertNotNull(response.getCreditCardCharges().get(0).getCreditCardNumber());
		Assert.assertEquals(response.getCreditCardCharges().get(0).getEventName(), null);
		Assert.assertEquals(response.getCreditCardCharges().get(1).getEventName(), null);

		ObjectNode on = om.createObjectNode();
		JsonNode nullNode = null;
		on.put("response",nullNode);
		jsonNode = on;
		when(accountSolrCloudBiz.getByIdList(anyString(),anyString(),anyList())).thenReturn(jsonNode);
		response = service.getCreditCardCharges(securityContext, sellerGuid, sort, fromDate, toDate, null, null, transactionType, "USD");

		Assert.assertEquals(response.getCreditCardCharges().get(0).getEventName(), null);
		Assert.assertEquals(response.getCreditCardCharges().get(1).getEventName(), null);

		ArrayNode docNodes = om.createArrayNode();
		ObjectNode responseNode = om.createObjectNode();
		responseNode.put("docs",docNodes);
		on.put("response",responseNode);
		jsonNode = on;
		when(accountSolrCloudBiz.getByIdList(anyString(),anyString(),anyList())).thenReturn(jsonNode);
		response = service.getCreditCardCharges(securityContext, sellerGuid, sort, fromDate, toDate, null, null, transactionType, "USD");

		Assert.assertEquals(response.getCreditCardCharges().get(0).getEventName(), null);
		Assert.assertEquals(response.getCreditCardCharges().get(1).getEventName(), null);

	}


	@Test
	public void testGetCreditCardChargesAuthorizationError() {
		String sellerGuid = "B5D14E323CD55E9FE04400144F8AE084";
		CreditCardChargesResponse response = service.getCreditCardCharges(null, sellerGuid, null, null, null, null, null, null, "GBP");
		Assert.assertTrue(response.getErrors().size()==1);
	}

}
