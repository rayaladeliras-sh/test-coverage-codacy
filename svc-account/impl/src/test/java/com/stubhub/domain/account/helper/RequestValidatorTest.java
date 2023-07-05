package com.stubhub.domain.account.helper;

import java.util.ArrayList;
import java.util.List;

import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.stubhub.common.exception.InvalidArgumentException;
import com.stubhub.domain.account.common.Error;
import com.stubhub.domain.account.intf.CSOrderDetailsRequest;
import com.stubhub.domain.account.intf.CSOrderDetailsResponse;
import com.stubhub.domain.account.intf.CSSaleDetailsRequest;
import com.stubhub.domain.account.intf.DeliveryResponse;
import com.stubhub.domain.account.intf.OrdersResponse;
import com.stubhub.domain.account.intf.SearchEmailCriteria;
import com.stubhub.domain.account.intf.SubstitutionRequest;
import com.stubhub.domain.account.intf.TransactionResponse;
import com.stubhub.domain.account.intf.TransactionSummaryRequest;
import com.stubhub.domain.infrastructure.soa.core.context.SHServiceContext;
import com.stubhub.newplatform.common.entity.Money;

public class RequestValidatorTest {
	private RequestValidator requestValidator;
	private CSSaleRequestValidator csSaleRequestValidator;
	List<Error> error;
	private SHServiceContext serviceContext;

	@BeforeMethod
	public void setUp(){
		requestValidator = new RequestValidator();
		csSaleRequestValidator = new CSSaleRequestValidator();
		error = new ArrayList<com.stubhub.domain.account.common.Error>();
		serviceContext = Mockito.mock(SHServiceContext.class);
		Mockito.when(serviceContext.getOperatorId()).thenReturn(null);
	}
	
	@Test
	public void testValidateCSOrderDetailsRequestFields() {
		CSOrderDetailsRequest csOrderDetailsRequest = new CSOrderDetailsRequest();
		
		csOrderDetailsRequest.setProxiedId("123");
		csOrderDetailsRequest.setEventStartDate("20140909");
		csOrderDetailsRequest.setEventEndDate(null);
		error = requestValidator.validateCSOrderDetailsRequestFields(csOrderDetailsRequest);
		Assert.assertNotNull(error);
		
		csOrderDetailsRequest.setProxiedId("123");
		csOrderDetailsRequest.setEventEndDate("20140909");
		csOrderDetailsRequest.setEventStartDate(null);
		error = requestValidator.validateCSOrderDetailsRequestFields(csOrderDetailsRequest);
		Assert.assertNotNull(error);
		
		csOrderDetailsRequest.setProxiedId("123");
		csOrderDetailsRequest.setEventStartDate("2014-09-09");
		csOrderDetailsRequest.setEventEndDate("2014-08-09");
		error = requestValidator.validateCSOrderDetailsRequestFields(csOrderDetailsRequest);
		Assert.assertNotNull(error);
		
		csOrderDetailsRequest.setOrderId(null);
		csOrderDetailsRequest.setProxiedId(null);
		error = requestValidator.validateCSOrderDetailsRequestFields(csOrderDetailsRequest);
		Assert.assertNotNull(error);
		
		csOrderDetailsRequest.setOrderId("1234");
		csOrderDetailsRequest.setProxiedId("123");
		error = requestValidator.validateCSOrderDetailsRequestFields(csOrderDetailsRequest);
		Assert.assertNotNull(error);
		
		csOrderDetailsRequest.setOrderId("abc");
		csOrderDetailsRequest.setProxiedId(null);
		error = requestValidator.validateCSOrderDetailsRequestFields(csOrderDetailsRequest);
		Assert.assertNotNull(error);
		
		csOrderDetailsRequest.setOrderId(null);
		csOrderDetailsRequest.setProxiedId("abc");
		csOrderDetailsRequest.setStart("0");
		csOrderDetailsRequest.setRow("2");		
		error = requestValidator.validateCSOrderDetailsRequestFields(csOrderDetailsRequest);
		Assert.assertNotNull(error);
		
		csOrderDetailsRequest.setProxiedId("123");
		csOrderDetailsRequest.setStart("-1");
		csOrderDetailsRequest.setRow(null);		
		error = requestValidator.validateCSOrderDetailsRequestFields(csOrderDetailsRequest);
		Assert.assertNotNull(error);
		
		csOrderDetailsRequest.setProxiedId("123");
		csOrderDetailsRequest.setStart(null);
		csOrderDetailsRequest.setRow("0");		
		error = requestValidator.validateCSOrderDetailsRequestFields(csOrderDetailsRequest);
		Assert.assertNotNull(error);
		
		csOrderDetailsRequest.setProxiedId("123");
		csOrderDetailsRequest.setStart(null);
		csOrderDetailsRequest.setRow("2");		
		error = requestValidator.validateCSOrderDetailsRequestFields(csOrderDetailsRequest);
		Assert.assertNotNull(error);
		
		csOrderDetailsRequest.setOrderId("1234");
		csOrderDetailsRequest.setProxiedId("123");
		csOrderDetailsRequest.setStart("0");
		csOrderDetailsRequest.setRow("2");		
		error = requestValidator.validateCSOrderDetailsRequestFields(csOrderDetailsRequest);
		Assert.assertNotNull(error);
		
		csOrderDetailsRequest.setOrderId("1234");
		csOrderDetailsRequest.setProxiedId("123");
		csOrderDetailsRequest.setStart("0");
		csOrderDetailsRequest.setRow("2");		
		error = requestValidator.validateCSOrderDetailsRequestFields(csOrderDetailsRequest);
		Assert.assertNotNull(error);
		
		csOrderDetailsRequest.setOrderId("1234");
		csOrderDetailsRequest.setProxiedId("123");
		csOrderDetailsRequest.setStart("0");
		csOrderDetailsRequest.setRow("2");		
		error = requestValidator.validateCSOrderDetailsRequestFields(csOrderDetailsRequest);
		Assert.assertNotNull(error);
		
		csOrderDetailsRequest.setOrderId("1234");
		csOrderDetailsRequest.setProxiedId("123");
		csOrderDetailsRequest.setStart("0");
		csOrderDetailsRequest.setRow("2");		
		error = requestValidator.validateCSOrderDetailsRequestFields(csOrderDetailsRequest);
		Assert.assertNotNull(error);
		
		csOrderDetailsRequest.setOrderId("1234");
		csOrderDetailsRequest.setProxiedId("123");
		csOrderDetailsRequest.setStart("0");
		csOrderDetailsRequest.setRow("2");		
		error = requestValidator.validateCSOrderDetailsRequestFields(csOrderDetailsRequest);
		Assert.assertNotNull(error);
	}
	
	@Test
	public void testValidateTransactionSummaryRequestFields() throws Exception{
		RequestValidator requestValidator = new RequestValidator();
		TransactionSummaryRequest request = new TransactionSummaryRequest();
		request.setProxiedId(null);
		Assert.assertNotNull(requestValidator.validateTransactionSummaryRequestFields(request));
		request.setProxiedId("abc");
		Assert.assertNotNull(requestValidator.validateTransactionSummaryRequestFields(request));
		request.setProxiedId("1");
		request.setBuyerFlip("abc");
		Assert.assertNotNull(requestValidator.validateTransactionSummaryRequestFields(request));
		request.setProxiedId("1");
		request.setBuyerFlip("true");
		Assert.assertNull(requestValidator.validateTransactionSummaryRequestFields(request));
	}
	
	@Test
	public void testValidateEmailHistoryRequestField() {
		SearchEmailCriteria sc = new SearchEmailCriteria();
		String proxiedId = "123456";
		sc.setFromDate("2015-02-02");
		sc.setOrderId("1234");
		sc.setBuyerOrderId("5678");
		sc.setRows("2");
		sc.setStart("1");
		sc.setSubject("welcome");
		sc.setToDate("2015-02-04");
		
		error = requestValidator.validateEmailHistoryRequestField(null, sc);
		Assert.assertNotNull(error);
		
		sc.setOrderId("abc");
		error = requestValidator.validateEmailHistoryRequestField(proxiedId, sc);
		Assert.assertNotNull(error);
		
		sc.setOrderId("1234");
		sc.setBuyerOrderId("abc");
		error = requestValidator.validateEmailHistoryRequestField(proxiedId, sc);
		Assert.assertNotNull(error);
		
		sc.setStart("abc");
		sc.setOrderId("1234");
		error = requestValidator.validateEmailHistoryRequestField(proxiedId, sc);
		Assert.assertNotNull(error);
		
		sc.setRows("abc");
		sc.setStart("1");
		error = requestValidator.validateEmailHistoryRequestField(proxiedId, sc);
		Assert.assertNotNull(error);
		
		sc.setFromDate(null);
		sc.setRows("2");
		error = requestValidator.validateEmailHistoryRequestField(proxiedId, sc);
		Assert.assertNotNull(error);
	}
	
	@Test
	public void testValidateUpdateCSOrderDetails() {
		OrdersResponse request = new OrdersResponse();
		List<CSOrderDetailsResponse> list = new ArrayList<CSOrderDetailsResponse>();
		CSOrderDetailsResponse order = new CSOrderDetailsResponse();
		DeliveryResponse delivery = new DeliveryResponse();
		delivery.setOrderProcSubStatusCode("7");
		TransactionResponse transaction = new TransactionResponse();
		transaction.setOrderId("12");
		transaction.setCancelled(true);
		order.setDelivery(delivery);
		order.setTransaction(transaction);
		list.add(order);
		request.setOrder(list);
		Mockito.when(serviceContext.getOperatorId()).thenReturn(null);
		Assert.assertNotNull(requestValidator.validateUpdateCSOrderDetails(serviceContext, request));
		Mockito.when(serviceContext.getOperatorId()).thenReturn("274291");
		request.getOrder().get(0).getTransaction().setOrderId(null);
		Assert.assertNotNull(requestValidator.validateUpdateCSOrderDetails(serviceContext, request));
		request.getOrder().get(0).getTransaction().setOrderId("12");
		request.getOrder().get(0).getDelivery().setOrderProcSubStatusCode("abc");
		Assert.assertNotNull(requestValidator.validateUpdateCSOrderDetails(serviceContext, request));
		request.getOrder().get(0).getDelivery().setOrderProcSubStatusCode("7");
		request.getOrder().get(0).getTransaction().setCancelled(true);
		Assert.assertNull(requestValidator.validateUpdateCSOrderDetails(serviceContext, request));
	}
	
	@Test
	public void testIsValidBoolean() {
		RequestValidator requestValidator = new RequestValidator();
		Assert.assertEquals(requestValidator.isValidBoolean(null), false);
		Assert.assertEquals(requestValidator.isValidBoolean("true"), true);
		Assert.assertEquals(requestValidator.isValidBoolean("false"), true);
		Assert.assertEquals(requestValidator.isValidBoolean("abc"), false);
	}
	
	@Test
	public void testValidateCreateSubInputRequest() throws InvalidArgumentException{
		SubstitutionRequest request = new SubstitutionRequest();
		String orderId = "123";
		Mockito.when(serviceContext.getOperatorId()).thenReturn("274291");
		request.setListingId("abc");
		Assert.assertNotNull(requestValidator.validateCreateSubOrderRequest(request, orderId, serviceContext));
		request.setListingId("1");
		orderId = "abc";
		Assert.assertNotNull(requestValidator.validateCreateSubOrderRequest(request, orderId, serviceContext));
		orderId = "123";
		request.setSubsReasonId("abc");
		Assert.assertNotNull(requestValidator.validateCreateSubOrderRequest(request, orderId, serviceContext));
		request.setSubsReasonId("1");
		request.setQuantity("abc");
		Assert.assertNotNull(requestValidator.validateCreateSubOrderRequest(request, orderId, serviceContext));
		request.setQuantity("1");
		request.setSellerPayoutDifference(null);
		Assert.assertNotNull(requestValidator.validateCreateSubOrderRequest(request, orderId, serviceContext));
		request.setSellerPayoutDifference(new Money("11.11","USD"));
		request.setTicketCostDifference(null);
		Assert.assertNotNull(requestValidator.validateCreateSubOrderRequest(request, orderId, serviceContext));
		request.setTicketCostDifference(new Money("11.11","USD"));
		request.setDeliveryMethodId(null);
		Assert.assertNotNull(requestValidator.validateCreateSubOrderRequest(request, orderId, serviceContext));
		request.setDeliveryMethodId("1");
		request.setFulfillmentMethodId(null);
		Assert.assertNotNull(requestValidator.validateCreateSubOrderRequest(request, orderId, serviceContext));
		request.setFulfillmentMethodId("1");
		request.setLmsLocationId("abc");
		Assert.assertNotNull(requestValidator.validateCreateSubOrderRequest(request, orderId, serviceContext));
		request.setLmsLocationId("1");
		request.setInHandDate("01-01-01");
		Assert.assertNotNull(requestValidator.validateCreateSubOrderRequest(request, orderId, serviceContext));
		request.setInHandDate("2015-07-07");
		request.setTicketCost(new Money("11.11",null));
		Assert.assertNotNull(requestValidator.validateCreateSubOrderRequest(request, orderId, serviceContext));
		request.setTicketCost(new Money("11.11","USD"));
		request.setShipCost(new Money("11.11",null));
		Assert.assertNotNull(requestValidator.validateCreateSubOrderRequest(request, orderId, serviceContext));
		request.setShipCost(new Money("11.11","USD"));
		request.setTotalCost(new Money("11.11",null));
		Assert.assertNotNull(requestValidator.validateCreateSubOrderRequest(request, orderId, serviceContext));
		request.setTotalCost(new Money("11.11","USD"));
		request.setDiscountCost(new Money("11.11",null));
		Assert.assertNotNull(requestValidator.validateCreateSubOrderRequest(request, orderId, serviceContext));
		request.setDiscountCost(new Money("11.11","USD"));
		request.setSellerFeeVal(new Money("11.11",null));
		Assert.assertNotNull(requestValidator.validateCreateSubOrderRequest(request, orderId, serviceContext));
		request.setSellerFeeVal(new Money("11.11","USD"));
		request.setBuyerFeeVal(new Money("11.11",null));
		Assert.assertNotNull(requestValidator.validateCreateSubOrderRequest(request, orderId, serviceContext));
		request.setBuyerFeeVal(new Money("11.11","USD"));
		request.setPremiumFees(new Money("11.11",null));
		Assert.assertNotNull(requestValidator.validateCreateSubOrderRequest(request, orderId, serviceContext));
		request.setPremiumFees(new Money("11.11","USD"));
		request.setSellerPayoutAmount(new Money("11.11",null));
		Assert.assertNotNull(requestValidator.validateCreateSubOrderRequest(request, orderId, serviceContext));
		request.setSellerPayoutAmount(new Money("11.11","USD"));
		request.setSellerPayoutAtConfirm(new Money("11.11",null));
		Assert.assertNotNull(requestValidator.validateCreateSubOrderRequest(request, orderId, serviceContext));
		request.setSellerPayoutAtConfirm(new Money("11.11","USD"));
		request.setAddOnFee(new Money("11.11",null));
		Assert.assertNotNull(requestValidator.validateCreateSubOrderRequest(request, orderId, serviceContext));
		request.setAddOnFee(new Money("11.11","USD"));
		request.setVatBuyFee(new Money("11.11",null));
		Assert.assertNotNull(requestValidator.validateCreateSubOrderRequest(request, orderId, serviceContext));
		request.setVatBuyFee(new Money("11.11","USD"));
		request.setVatLogFee(new Money("11.11",null));
		Assert.assertNotNull(requestValidator.validateCreateSubOrderRequest(request, orderId, serviceContext));
		request.setVatLogFee(new Money("11.11","USD"));
		request.setVatSellFee(new Money("11.11",null));
		Assert.assertNotNull(requestValidator.validateCreateSubOrderRequest(request, orderId, serviceContext));
		request.setVatSellFee(new Money("11.11","USD"));
		request.setAdditionalSellFeePerTicket(new Money("11.11","USD"));
		Assert.assertNull(requestValidator.validateCreateSubOrderRequest(request, orderId, serviceContext));
	}
	
	
	@Test
	public void testValidateCSSaleDetailsRequestFields() {
		CSSaleDetailsRequest csSaleDetailsRequest = new CSSaleDetailsRequest();
		
		csSaleDetailsRequest.setProxiedId("123");
		csSaleDetailsRequest.setEventStartDate("20140909");
		csSaleDetailsRequest.setEventEndDate(null);
		error = csSaleRequestValidator.validateCSSaleDetailsRequestFields(csSaleDetailsRequest);
		Assert.assertNotNull(error);
		
		csSaleDetailsRequest.setProxiedId("123");
		csSaleDetailsRequest.setEventEndDate("20140909");
		csSaleDetailsRequest.setEventStartDate(null);
		error = csSaleRequestValidator.validateCSSaleDetailsRequestFields(csSaleDetailsRequest);
		Assert.assertNotNull(error);
		
		csSaleDetailsRequest.setProxiedId("123");
		csSaleDetailsRequest.setEventStartDate("2014-09-09");
		csSaleDetailsRequest.setEventEndDate("2014-08-09");
		error = csSaleRequestValidator.validateCSSaleDetailsRequestFields(csSaleDetailsRequest);
		Assert.assertNotNull(error);
		
		csSaleDetailsRequest.setSaleId(null);
		csSaleDetailsRequest.setProxiedId(null);
		error = csSaleRequestValidator.validateCSSaleDetailsRequestFields(csSaleDetailsRequest);
		Assert.assertNotNull(error);
		
		csSaleDetailsRequest.setSaleId("1234");
		csSaleDetailsRequest.setProxiedId("123");
		error = csSaleRequestValidator.validateCSSaleDetailsRequestFields(csSaleDetailsRequest);
		Assert.assertNotNull(error);
		
		csSaleDetailsRequest.setSaleId("abc");
		csSaleDetailsRequest.setProxiedId(null);
		error = csSaleRequestValidator.validateCSSaleDetailsRequestFields(csSaleDetailsRequest);
		Assert.assertNotNull(error);
		
		csSaleDetailsRequest.setSaleId(null);
		csSaleDetailsRequest.setProxiedId("abc");
		csSaleDetailsRequest.setStart("0");
		csSaleDetailsRequest.setRow("2");		
		error = csSaleRequestValidator.validateCSSaleDetailsRequestFields(csSaleDetailsRequest);
		Assert.assertNotNull(error);
		
		csSaleDetailsRequest.setProxiedId("123");
		csSaleDetailsRequest.setStart("-1");
		csSaleDetailsRequest.setRow(null);		
		error = csSaleRequestValidator.validateCSSaleDetailsRequestFields(csSaleDetailsRequest);
		Assert.assertNotNull(error);
		
		csSaleDetailsRequest.setProxiedId("123");
		csSaleDetailsRequest.setStart(null);
		csSaleDetailsRequest.setRow("0");		
		error = csSaleRequestValidator.validateCSSaleDetailsRequestFields(csSaleDetailsRequest);
		Assert.assertNotNull(error);
		
		csSaleDetailsRequest.setProxiedId("123");
		csSaleDetailsRequest.setStart(null);
		csSaleDetailsRequest.setRow("2");		
		error = csSaleRequestValidator.validateCSSaleDetailsRequestFields(csSaleDetailsRequest);
		Assert.assertNotNull(error);
		
		csSaleDetailsRequest.setSaleId("1234");
		csSaleDetailsRequest.setProxiedId("123");
		csSaleDetailsRequest.setStart("0");
		csSaleDetailsRequest.setRow("2");		
		error = csSaleRequestValidator.validateCSSaleDetailsRequestFields(csSaleDetailsRequest);
		Assert.assertNotNull(error);
		
		csSaleDetailsRequest.setSaleId("1234");
		csSaleDetailsRequest.setProxiedId("123");
		csSaleDetailsRequest.setStart("0");
		csSaleDetailsRequest.setRow("2");		
		error = csSaleRequestValidator.validateCSSaleDetailsRequestFields(csSaleDetailsRequest);
		Assert.assertNotNull(error);
		
		csSaleDetailsRequest.setSaleId("1234");
		csSaleDetailsRequest.setProxiedId("123");
		csSaleDetailsRequest.setStart("0");
		csSaleDetailsRequest.setRow("2");		
		error = csSaleRequestValidator.validateCSSaleDetailsRequestFields(csSaleDetailsRequest);
		Assert.assertNotNull(error);
		
		csSaleDetailsRequest.setSaleId("1234");
		csSaleDetailsRequest.setProxiedId("123");
		csSaleDetailsRequest.setStart("0");
		csSaleDetailsRequest.setRow("2");		
		error = csSaleRequestValidator.validateCSSaleDetailsRequestFields(csSaleDetailsRequest);
		Assert.assertNotNull(error);
		
		csSaleDetailsRequest.setSaleId("1234");
		csSaleDetailsRequest.setProxiedId("123");
		csSaleDetailsRequest.setStart("0");
		csSaleDetailsRequest.setRow("2");		
		error = csSaleRequestValidator.validateCSSaleDetailsRequestFields(csSaleDetailsRequest);
		Assert.assertNotNull(error);
	}
}
