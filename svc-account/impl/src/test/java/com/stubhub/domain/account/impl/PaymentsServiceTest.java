package com.stubhub.domain.account.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.*;

import javax.ws.rs.core.Response;

import com.stubhub.domain.infrastructure.common.exception.derived.SHUnauthorizedException;
import com.stubhub.domain.infrastructure.config.client.core.SHConfig;
import com.stubhub.domain.infrastructure.web.client.SHRestTemplate;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.util.NamedList;
import org.mockito.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.stubhub.common.exception.UserNotAuthorizedException;
import com.stubhub.domain.account.biz.impl.MyaSellerPaymentStatusUtil;
import com.stubhub.domain.account.biz.intf.SellerPaymentBO;
import com.stubhub.domain.account.biz.intf.SellerPaymentSolrCloudBO;
import com.stubhub.domain.account.common.PaymentsSearchCriteria;
import com.stubhub.domain.account.common.enums.PaymentUserDefinedStatus;
import com.stubhub.domain.account.common.exception.DisbursementOptionException;
import com.stubhub.domain.account.common.exception.InvalidPaymentException;
import com.stubhub.domain.account.common.exception.PaymentStatusException;
import com.stubhub.domain.account.common.util.SecurityContextUtil;
import com.stubhub.domain.account.datamodel.entity.DisbursementOptionEnum;
import com.stubhub.domain.account.datamodel.entity.SellerPayment;
import com.stubhub.domain.account.datamodel.entity.SellerPaymentStatusEnum;
import com.stubhub.domain.account.impl.export.ExportFile;
import com.stubhub.domain.account.impl.export.FileCreatorFactory;
import com.stubhub.domain.account.intf.PaymentsService;
import com.stubhub.domain.account.intf.UpdatePaymentRequest;
import com.stubhub.domain.account.intf.UpdatePaymentResponse;
import com.stubhub.domain.account.intf.UpdatePaymentStatusRequest;
import com.stubhub.domain.account.intf.UpdatePaymentStatusResponse;
import com.stubhub.newplatform.property.MasterStubHubProperties;
import com.stubhub.newplatform.property.loader.IConfigLoader;
import com.stubhub.platform.utilities.webservice.security.ExtendedSecurityContext;

import junit.framework.Assert;

public class PaymentsServiceTest {
	private ExtendedSecurityContext securityContext;
	private PaymentsServiceImpl service;
	private SellerPaymentBO sellerPaymentBO;
	private SellerPaymentSolrCloudBO sellerPaymentSolrCloudBO;
	private SecurityContextUtil securityContextUtil;
	@Mock private SHConfig shConfig;
	@Mock private SHRestTemplate restTemplate;

	@BeforeClass
	public static void setupClass() throws Exception {
		MasterStubHubProperties.setLoaders(Arrays.<IConfigLoader> asList(new IConfigLoader() {

			@Override
			public Map<String, String> load() throws Exception {
				return Collections.emptyMap();
			}

		}));
		MasterStubHubProperties.load();
	}

	@BeforeMethod
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		service = new PaymentsServiceImpl();
		service.init();

		securityContext = Mockito.mock(ExtendedSecurityContext.class);
		sellerPaymentBO = Mockito.mock(SellerPaymentBO.class);
		sellerPaymentSolrCloudBO = Mockito.mock(SellerPaymentSolrCloudBO.class);
		ReflectionTestUtils.setField(service, "sellerPaymentBO", sellerPaymentBO);
		ReflectionTestUtils.setField(service, "sellerPaymentSolrCloudBO", sellerPaymentSolrCloudBO);
		ReflectionTestUtils.setField(service, "shConfig", shConfig);
		ReflectionTestUtils.setField(service, "restTemplate", restTemplate);

		securityContextUtil = mock(SecurityContextUtil.class);
		ReflectionTestUtils.setField(service, "securityContextUtil", securityContextUtil);

		Mockito.when(securityContext.getUserId()).thenReturn("12345");
		Mockito.when(securityContext.getUserGuid()).thenReturn("B5D14E323CD55E9FE04400144F8AE084");
		when(restTemplate.clone()).thenReturn(restTemplate);
		when(restTemplate.setUri(anyString())).thenReturn(restTemplate);
		when(restTemplate.setHeader(anyString(), anyString())).thenReturn(restTemplate);
		when(restTemplate.setQueryParam(anyString(), anyString())).thenReturn(restTemplate);
		when(restTemplate.setQueryParams(any(Map.class))).thenReturn(restTemplate);
		when(shConfig.getProperty("paymentService.api.calculateThreshold.url", "https://api-wg.stubprod.com/payments/payables/v1/pi/"))
				.thenReturn("http://www.google.com");
	}

	@Test
	public void testGetSellerPaymentsWithSolrCloud() throws Exception {
		MasterStubHubProperties.setProperty("account.v1.payment.useSolrCloud", "true");
		try {
			when(sellerPaymentSolrCloudBO.getSellerPayments(any(PaymentsSearchCriteria.class))).thenReturn(null);
			com.stubhub.domain.account.intf.SellerPayments spayments = service.getPayments(
                    "B5D14E323CD55E9FE04400144F8AE084", "", "", "0", "200", "false", "false", null, null,
                    null, "false", null, securityContext);
			// TODO
			Assert.assertNotNull(spayments);
		} finally {
			MasterStubHubProperties.setProperty("account.v1.payment.useSolrCloud", "false");
		}
	}

	@Test
	public void testSellerPaymentStatus() throws Exception {
		Assert.assertEquals(PaymentUserDefinedStatus.PROCESSING.getName(), MyaSellerPaymentStatusUtil
				.getMyaSellerPaymentStatus(SellerPaymentStatusEnum.PAYMENT_SENT_TO_GP_FAILED.getId()));
		Assert.assertEquals(PaymentUserDefinedStatus.CANCELLED.getName(), MyaSellerPaymentStatusUtil
				.getMyaSellerPaymentStatus(SellerPaymentStatusEnum.PAYMENT_FAILURE_NOTIFIED.getId()));
		Assert.assertEquals(PaymentUserDefinedStatus.UNCLAIMED.getName(), MyaSellerPaymentStatusUtil
				.getMyaSellerPaymentStatus(SellerPaymentStatusEnum.GP_PAYMENT_UNCLAIMED.getId()));
		Assert.assertEquals(PaymentUserDefinedStatus.UNCLAIMED.getName(), MyaSellerPaymentStatusUtil
				.getMyaSellerPaymentStatus(SellerPaymentStatusEnum.PAYMENT_UNCLAIMED_NOTIFIED.getId()));
		Assert.assertEquals(PaymentUserDefinedStatus.APPLIED.getName(),
				MyaSellerPaymentStatusUtil.getMyaSellerPaymentStatus(SellerPaymentStatusEnum.READY_TO_SEND_CM.getId()));
		Assert.assertEquals(PaymentUserDefinedStatus.CHARGED.getName(),
				MyaSellerPaymentStatusUtil.getMyaSellerPaymentStatus(SellerPaymentStatusEnum.COMPLETED.getId()));
		Assert.assertEquals(PaymentUserDefinedStatus.REJECTED.getName(),
				MyaSellerPaymentStatusUtil.getMyaSellerPaymentStatus(SellerPaymentStatusEnum.PAYMENT_REJECTED_BY_GATEWAY.getId()));
		for (PaymentUserDefinedStatus s : PaymentUserDefinedStatus.values()) {
			ArrayList<Long> list = new ArrayList<Long>(
					MyaSellerPaymentStatusUtil.getSellerPaymentStatusIdsByStatusName(s.getName()));
			Collections.sort(list);

			if (s == PaymentUserDefinedStatus.PENDING) {
				Assert.assertEquals("[26, 28]", list.toString());
			} else if (s == PaymentUserDefinedStatus.CANCELLED) {
				Assert.assertEquals("[4, 11, 23, 25, 27]", list.toString());
			} else if (s == PaymentUserDefinedStatus.SENT) {
				Assert.assertEquals("[9, 10, 14]", list.toString());
			} else if (s == PaymentUserDefinedStatus.PROCESSING) {
				Assert.assertEquals("[1, 2, 3, 5, 6, 7, 8, 9, 13]", list.toString());
			} else if (s == PaymentUserDefinedStatus.AVAILABLE) {
				Assert.assertEquals("[29]", list.toString());
			} else if (s == PaymentUserDefinedStatus.UNCLAIMED) {
				Assert.assertEquals("[12, 24]", list.toString());
			} else if (s == PaymentUserDefinedStatus.APPLIED) {
				Assert.assertEquals("[15, 16, 17, 18, 19, 20, 21]", list.toString());
			} else if (s == PaymentUserDefinedStatus.CHARGED) {
				Assert.assertEquals("[22]", list.toString());
			} else if (s == PaymentUserDefinedStatus.REJECTED) {
				Assert.assertEquals("[5, 41]", list.toString());
			}
		}
	}


	@Test
	public void testGetSellerPaymentsUnAuthorizedException() {
		try {
			Mockito.doThrow(new UserNotAuthorizedException()).when(securityContextUtil)
					.validateUserGuid(Matchers.any(ExtendedSecurityContext.class), Matchers.anyString());
		} catch (UserNotAuthorizedException e) {
		}
		com.stubhub.domain.account.intf.SellerPayments spayments = service
                .getPayments("B5D14E323CD55E9FE04400144F8AE084", "", "", "", "", "false", "false", null,
                        null, null, "false", null, securityContext);
		Assert.assertTrue(spayments.getErrors().size() == 1);
	}


	private QueryResponse mockPaymentSummarySingleCurrencySolrResponse() {
		QueryResponse response = new QueryResponse();
		NamedList<Object> n11 = new NamedList<Object>();
		n11.add("sum", 12.21);
		NamedList<Object> n111 = new NamedList<Object>();
		n111.add("sum", 12.12);
		NamedList<Object> n12 = new NamedList<Object>();
		n12.add("sum", 43.98);
		NamedList<Object> n13 = new NamedList<Object>();
		n13.add("sum", 21.94);

		NamedList<Object> n7 = new NamedList<Object>();
		NamedList<NamedList<Object>> n1 = new NamedList<NamedList<Object>>();
		n1.add("26", n11);
		n1.add("28", n111);
		n1.add("1", n12);
		n1.add("29", n13);
		n7.add("SELLER_PAYMENT_STATUS", n1);

		NamedList<NamedList<Object>> nn = new NamedList<NamedList<Object>>();
		NamedList<Object> n5 = new NamedList<Object>();
		n5.add("facets", n7);
		nn.add("PAYMENT_AMOUNT", n5);

		NamedList<Object> n3 = new NamedList<Object>();
		NamedList<Object> n4 = new NamedList<Object>();
		n4.add("stats_fields", nn);
		n3.add("stats", n4);

		response.setResponse(n3);
		return response;
	}

	private QueryResponse mockSolrDocument() {
		QueryResponse response = new QueryResponse();
		SolrDocumentList list = new SolrDocumentList();
		list.setNumFound(2);
		list.setStart(0);

		NamedList responseList = new NamedList();
		responseList.add("response", list);
		response.setResponse(responseList);

		SolrDocument doc = new SolrDocument();
		doc.setField("CURRENCY_CODE", "USD");
		doc.setField("PAYPAL_PAYMENT_EMAIL", "test@test.com");
		doc.setField("BOOK_OF_BUSINESS_ID", 1);
		doc.setField("TID", "123213");
		doc.setField("PAYMENT_AMOUNT", 10.0f);
		doc.setField("PAYMENT_ID", 1231l);
		doc.setField("PAYEE", "John Adams");
		doc.setField("SELLER_PAYMENT_STATUS", "14");
		doc.setField("REFERENCE_NUMBER", "kjsdfkj324lsdf");
		doc.setField("PAYMENT_TYPE", "1");
		doc.setField("TOTAL_TICKET_PRICE", 1.0f);
		doc.setField("ADJUSTMENT_AMOUNT", 1.0d);
		doc.setField("COMMISSION", 1.0f);
		doc.setField("PAYMENT_TYPE_USER_DEFINED", "1");
		doc.setField("VENUE_DESCRIPTION", "1");
		doc.setField("STUBHUB_LISTING_ID", "1");
		doc.setField("EXTERNAL_LISTING_ID", "1");
		doc.setField("TICKET_SECTION", "1");
		doc.setField("TICKET_ROW_DESC", "1");
		doc.setField("TICKET_SEATS", "1");
		doc.setField("SELLER_PAYMENT_STATUS_USER_DEFINED", "1");
		doc.setField("REFERENCE_NUMBER", "1");
		doc.setField("EVENT_DESC", "SF Giants");
		doc.setField("EVENT_DATE", new Date());
		doc.setField("PAYMENT_CREATED_DATE", new Date());
		doc.setField("PAYMENT_COMPLETION_DATE", new Date());

		list.add(doc);

		list.add(doc);

		doc = new SolrDocument();
		doc.setField("CURRENCY_CODE", "GBP");
		doc.setField("BOOK_OF_BUSINESS_ID", 2);
		doc.setField("TID", "123456");
		doc.setField("PAYMENT_AMOUNT", 10.0f);
		doc.setField("PAYMENT_ID", 1231l);
		doc.setField("PAYEE", "John Adams");
		doc.setField("SELLER_PAYMENT_STATUS", "11");
		doc.setField("REFERENCE_NUMBER", "kjsdfkj324lsdf");
		doc.setField("PAYMENT_TYPE", "2");
		doc.setField("EVENT_DESC", "SF Giants");
		doc.setField("PAYMENT_CREATED_DATE", new Date());
		doc.setField("PAYMENT_COMPLETION_DATE", new Date());
		list.add(doc);

		return response;
	}

	private QueryResponse mockSolrDocumentByPaymentStatus() {
		QueryResponse response = new QueryResponse();
		SolrDocumentList list = new SolrDocumentList();
		list.setNumFound(2);
		list.setStart(0);

		NamedList responseList = new NamedList();
		responseList.add("response", list);
		response.setResponse(responseList);
		SolrDocument doc = new SolrDocument();
		doc.setField("CURRENCY_CODE", "USD");
		doc.setField("PAYPAL_PAYMENT_EMAIL", "test@test.com");
		doc.setField("BOOK_OF_BUSINESS_ID", 1);
		doc.setField("TID", "123213");
		doc.setField("PAYMENT_AMOUNT", 10.0f);
		doc.setField("PAYMENT_ID", 1231l);
		doc.setField("PAYEE", "John Adams");
		doc.setField("SELLER_PAYMENT_STATUS", "7");
		doc.setField("REFERENCE_NUMBER", "kjsdfkj324lsdf");
		doc.setField("PAYMENT_TYPE", "1");
		doc.setField("EVENT_DESC", "SF Giants");
		doc.setField("PAYMENT_CREATED_DATE", new Date());
		list.add(doc);

		doc = new SolrDocument();
		doc.setField("CURRENCY_CODE", "GBP");
		doc.setField("BOOK_OF_BUSINESS_ID", 2);
		doc.setField("TID", "123456");
		doc.setField("PAYMENT_AMOUNT", 10.0f);
		doc.setField("PAYMENT_ID", 1231l);
		doc.setField("PAYEE", "John Adams");
		doc.setField("SELLER_PAYMENT_STATUS", "7");
		doc.setField("REFERENCE_NUMBER", "kjsdfkj324lsdf");
		doc.setField("PAYMENT_TYPE", "2");
		doc.setField("EVENT_DESC", "SF Giants");
		list.add(doc);

		return response;
	}

	@Test
	public void testUpdatePaymentStatus() {
		UpdatePaymentStatusRequest request = new UpdatePaymentStatusRequest();
		request.setUserId(Long.valueOf(1));
		request.setAction(UpdatePaymentStatusRequest.Action.RELEASE_PAYMENT_FOR_DUE_DILIGENCE);
		List<SellerPayment> payments = new ArrayList<SellerPayment>();
		com.stubhub.domain.account.datamodel.entity.SellerPayment payment1 = new com.stubhub.domain.account.datamodel.entity.SellerPayment();
		com.stubhub.domain.account.datamodel.entity.SellerPayment payment2 = new com.stubhub.domain.account.datamodel.entity.SellerPayment();
		payment1.setId(1L);
		payment2.setId(2L);
		payment1.setStatus(SellerPaymentStatusEnum.READY_TO_PAY.getName());
		payment2.setStatus(SellerPaymentStatusEnum.READY_TO_PAY.getName());
		payments.add(payment1);
		payments.add(payment2);
		Mockito.when(sellerPaymentBO.findSellerPaymentsByAction(request.getUserId(), request.getAction()))
				.thenReturn(payments);

		// happy pass
		UpdatePaymentStatusResponse response = service.updatePaymentStatus(request, securityContext);
		Mockito.verify(sellerPaymentBO).updateSellerPaymentStatus(payment1, SellerPaymentStatusEnum.READY_TO_PAY);
		Mockito.verify(sellerPaymentBO).updateSellerPaymentStatus(payment2, SellerPaymentStatusEnum.READY_TO_PAY);
		Assert.assertEquals(response.getPayments().size(), 2);
		Assert.assertEquals(response.getPayments().get(0).getID(), payment1.getId().toString());
		Assert.assertEquals(response.getPayments().get(0).getStatus(), SellerPaymentStatusEnum.READY_TO_PAY.getName());
		Assert.assertEquals(response.getPayments().get(1).getID(), payment2.getId().toString());
		Assert.assertEquals(response.getPayments().get(1).getStatus(), SellerPaymentStatusEnum.READY_TO_PAY.getName());
	}


	@Test
	public void testUpdatePaymentStatusDate() {
		UpdatePaymentStatusRequest request = new UpdatePaymentStatusRequest();
		request.setUserId(Long.valueOf(1));
		request.setAction(UpdatePaymentStatusRequest.Action.RELEASE_PAYMENT_FOR_DUE_DILIGENCE);
		request.setLatestPaymentDate(Calendar.getInstance());
		List<SellerPayment> payments = new ArrayList<SellerPayment>();
		com.stubhub.domain.account.datamodel.entity.SellerPayment payment1 = new com.stubhub.domain.account.datamodel.entity.SellerPayment();
		com.stubhub.domain.account.datamodel.entity.SellerPayment payment2 = new com.stubhub.domain.account.datamodel.entity.SellerPayment();
		payment1.setId(1L);
		payment2.setId(2L);
		payment1.setStatus(SellerPaymentStatusEnum.READY_TO_PAY.getName());
		payment2.setStatus(SellerPaymentStatusEnum.READY_TO_PAY.getName());
		payments.add(payment1);
		payments.add(payment2);
		Mockito.when(sellerPaymentBO.findSellerPaymentsByAction(request.getUserId(), request.getAction(), request.getLatestPaymentDate()))
				.thenReturn(payments);

		// happy pass
		UpdatePaymentStatusResponse response = service.updatePaymentStatus(request, securityContext);
		Mockito.verify(sellerPaymentBO).updateSellerPaymentStatus(payment1, SellerPaymentStatusEnum.READY_TO_PAY);
		Mockito.verify(sellerPaymentBO).updateSellerPaymentStatus(payment2, SellerPaymentStatusEnum.READY_TO_PAY);
		Assert.assertEquals(response.getPayments().size(), 2);
		Assert.assertEquals(response.getPayments().get(0).getID(), payment1.getId().toString());
		Assert.assertEquals(response.getPayments().get(0).getStatus(), SellerPaymentStatusEnum.READY_TO_PAY.getName());
		Assert.assertEquals(response.getPayments().get(1).getID(), payment2.getId().toString());
		Assert.assertEquals(response.getPayments().get(1).getStatus(), SellerPaymentStatusEnum.READY_TO_PAY.getName());
	}


	@Test
	public void testUpdatePayment() {
		com.stubhub.domain.account.datamodel.entity.SellerPayment payment1 = new com.stubhub.domain.account.datamodel.entity.SellerPayment();
		com.stubhub.domain.account.datamodel.entity.SellerPayment payment2 = new com.stubhub.domain.account.datamodel.entity.SellerPayment();
		payment1.setId(1L);
		payment1.setDisbursementOptionId(DisbursementOptionEnum.MANUAL.getId());
		payment1.setSellerId(Long.valueOf(securityContext.getUserId()));
		payment1.setSellerPaymentStatusId(SellerPaymentStatusEnum.HOLD_PAYMENT_FOR_MANUAL_DISBURSEMENT.getId());
		payment2.setId(2L);
		payment2.setDisbursementOptionId(DisbursementOptionEnum.MANUAL.getId());
		payment2.setSellerId(Long.valueOf(securityContext.getUserId()));
		payment2.setSellerPaymentStatusId(SellerPaymentStatusEnum.READY_TO_PAY.getId());

		Mockito.when(sellerPaymentBO.getSellerPaymentById(payment1.getId())).thenReturn(payment1);
		Mockito.when(sellerPaymentBO.getSellerPaymentById(payment2.getId())).thenReturn(payment2);

		UpdatePaymentRequest request = new UpdatePaymentRequest();
		request.setPayeeEmailId("test@paypal.com");

		// happy pass
		UpdatePaymentResponse response = service.updatePayment(payment1.getId(), request, securityContext);
		// Mockito.verify(sellerPaymentBO).saveSellerPayment(payment1);
		Mockito.verify(sellerPaymentBO).updateSellerPaymentStatus(payment1, SellerPaymentStatusEnum.READY_TO_PAY);
		Assert.assertEquals(response.getId(), payment1.getId());
		Assert.assertEquals(request.getPayeeEmailId(), request.getPayeeEmailId());

		// invalid disbursement option
		payment1.setDisbursementOptionId(1L);
		Exception ex = null;
		try {
			response = service.updatePayment(payment1.getId(), request, securityContext);
		} catch (Exception e) {
			ex = e;
		}
		Assert.assertNotNull(ex);
		Assert.assertTrue(ex instanceof DisbursementOptionException);
		Assert.assertNotNull(((DisbursementOptionException) ex).getData());
		Assert.assertEquals(((DisbursementOptionException) ex).getStatusCode(), 409);
		Assert.assertEquals(((DisbursementOptionException) ex).getDescription(), "disbursement option is not manual");
		Assert.assertEquals(((DisbursementOptionException) ex).getErrorCode(),
				"accountmanagement.payments.invalidDisbursementOption");

		// invalid status
		ex = null;
		try {
			response = service.updatePayment(payment2.getId(), request, securityContext);
		} catch (Exception e) {
			ex = e;
		}
		Assert.assertNotNull(ex);
		Assert.assertTrue(ex instanceof PaymentStatusException);
		Assert.assertNotNull(((PaymentStatusException) ex).getData());
		Assert.assertEquals(((PaymentStatusException) ex).getStatusCode(), 409);
		Assert.assertEquals(((PaymentStatusException) ex).getDescription(), "Payment status is not correct");
		Assert.assertEquals(((PaymentStatusException) ex).getErrorCode(), "accountmanagement.payments.invalidStatus");

		// invalid payment
		ex = null;
		try {
			response = service.updatePayment(0L, request, securityContext);
		} catch (Exception e) {
			ex = e;
		}
		Assert.assertNotNull(ex);
		Assert.assertTrue(ex instanceof InvalidPaymentException);
		Assert.assertNotNull(((InvalidPaymentException) ex).getData());
		Assert.assertEquals(((InvalidPaymentException) ex).getStatusCode(), 404);
		Assert.assertEquals(((InvalidPaymentException) ex).getDescription(), "Payment id is invalid or not exist");
		Assert.assertEquals(((InvalidPaymentException) ex).getErrorCode(), "accountmanagement.payments.invalidPayment");
	}

	@Test
	public void testExportPaymentWithSolrCloud() throws Exception {
		MasterStubHubProperties.setProperty("account.v1.payment.useSolrCloud", "true");
		try {
			Mockito.when(sellerPaymentSolrCloudBO.getSellerPayments(Matchers.any(PaymentsSearchCriteria.class)))
					.thenReturn(null);
			Response response = service.exportPayments("B5D14E323CD55E9FE04400144F8AE084",
					PaymentsService.ExportFileType.CSV, 6, 3, PaymentsService.DateUnit.MONTHS, null, "USD", securityContext);
			Assert.assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
		} finally {
			MasterStubHubProperties.setProperty("account.v1.payment.useSolrCloud", "false");

		}

	}

	@Test
	public void testExportFile() throws Exception {
		ExportFile ef = new ExportFile();
		List<String> headers = new ArrayList<String>();
		headers.add("a");
		headers.add("b");
		headers.add("c");
		ef.setHeaders(headers);
		Assert.assertEquals(ef.getHeaders(), headers);

		List<String[]> data = new ArrayList<String[]>();
		data.add(new String[] { "1", "2", "3" });
		data.add(new String[] { "11", "22", "33" });
		data.add(new String[] { "111", "222", "333" });
		ef.setData(data);

		Assert.assertNull(FileCreatorFactory.getFileCreator(null, ef));

		Assert.assertNull(FileCreatorFactory.getFileCreator(PaymentsService.ExportFileType.TXT, null).createExport());
		Assert.assertNull(FileCreatorFactory.getFileCreator(PaymentsService.ExportFileType.CSV, null).createExport());
		Assert.assertNull(FileCreatorFactory.getFileCreator(PaymentsService.ExportFileType.PDF, null).createExport());

	}

	@Test
	public void testPiService() {
		when(shConfig.getProperty("newapi.accessToken")).thenReturn("token");
		SHRestTemplate.SyncInvoker invoker = Mockito.mock(SHRestTemplate.SyncInvoker.class);
		when(restTemplate.sync()).thenReturn(invoker);
		when(invoker.get(any(Class.class))).thenReturn("{\"sellerId\":32957675,\"ruleEvaluationDate\":\"2018-03-21T10:03:21.157Z\",\"exceedThreshold\":true,\"exceedPastYear\":false,\"exceedSingle\":false}");
		Response response = service.evaluatePIRule(1L, "rule", securityContext);

		Assert.assertEquals(response.getStatus(), 200);

	}

	@Test(expectedExceptions = SHUnauthorizedException.class)
	public void testPiServiceException() {
		when(securityContext.getApplicationName()).thenReturn("app");
		ReflectionTestUtils.setField(service, "allowedAppName", "App1");
		service.evaluatePIRule(1L, null, securityContext);
	}
}