package com.stubhub.domain.account.impl;

import static org.mockito.Mockito.mock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.stubhub.common.exception.UserNotAuthorizedException;
import com.stubhub.domain.account.biz.impl.CreditMemoBOImpl;
import com.stubhub.domain.account.biz.intf.CreditMemoBO;
import com.stubhub.domain.account.common.exception.AccountException;
import com.stubhub.domain.account.common.util.SecurityContextUtil;
import com.stubhub.domain.account.datamodel.entity.SellerPaymentStatusEnum;
import com.stubhub.domain.account.exception.UnauthorizedException;
import com.stubhub.domain.account.intf.CreditMemoResponse;
import com.stubhub.domain.account.intf.CreditMemosResponse;
import com.stubhub.domain.account.intf.CreditMemosService;
import com.stubhub.domain.account.intf.IndyCreditMemosService;
import com.stubhub.domain.infrastructure.soa.core.context.SHServiceContext;
import com.stubhub.newplatform.common.entity.Money;
import com.stubhub.platform.utilities.webservice.security.ExtendedSecurityContext;

public class CreditMemosServiceTest {
	
	private ExtendedSecurityContext securityContext;
	private SHServiceContext serviceContext;
	private CreditMemosService service;	
	private IndyCreditMemosService indyService;
	private CreditMemoBO creditMemoBO;
	
	@BeforeMethod
	public void setUp() throws Exception {
		service = new CreditMemosServiceImpl();
		
		securityContext = Mockito.mock(ExtendedSecurityContext.class);		
		serviceContext = Mockito.mock(SHServiceContext.class);		
		creditMemoBO = Mockito.mock(CreditMemoBO.class);
		
		ReflectionTestUtils.setField(service, "creditMemoBO", creditMemoBO);
		
		indyService = new IndyCreditMemosServiceImpl();
		
		securityContext = Mockito.mock(ExtendedSecurityContext.class);		
		serviceContext = Mockito.mock(SHServiceContext.class);		
		creditMemoBO = Mockito.mock(CreditMemoBO.class);
		
		ReflectionTestUtils.setField(indyService, "creditMemoBO", creditMemoBO);
				
		Mockito.when(securityContext.getUserId()).thenReturn("12345");
		Mockito.when(securityContext.getUserGuid()).thenReturn("B5D14E323CD55E9FE04400144F8AE084");
	}
	/**
	 * Test methods for getCreditMemo
	 */
	@Test
	public void testGetCreditMemos() {
		String sellerGuid = "B5D14E323CD55E9FE04400144F8AE084";		
		String sortType = "asc";
		String filters = "filters";
		SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
		CreditMemoBOImpl creditMemoBO = mock(CreditMemoBOImpl.class);
		ReflectionTestUtils.setField(service, "securityContextUtil", securityContextUtil);
		ReflectionTestUtils.setField(service, "creditMemoBO", creditMemoBO);
		CreditMemosResponse creditMemosResponse = new CreditMemosResponse();
		
		CreditMemoResponse creditMemoResponse = new CreditMemoResponse();
		creditMemoResponse.setCreditAmount(new Money(new BigDecimal(10), "USD"));
		creditMemoResponse.setAppliedDate("2012-09-03T12:00:00Z");
		creditMemoResponse.setBookOfBusinessId("1");
		creditMemoResponse.setCreatedDate("2012-09-03T12:00:00Z");
		creditMemoResponse.setEventDate("2012-09-03T12:00:00Z");
		creditMemoResponse.setEventId("12345");
		creditMemoResponse.setId("12345");
		creditMemoResponse.setOrderId("12345");
		creditMemoResponse.setOrderStatus("Sold");
		creditMemoResponse.setReferenceNumber("12345");
		creditMemoResponse.setStatus(SellerPaymentStatusEnum.COMPLETED);
		creditMemoResponse.setReason("reason");
		
		List<CreditMemoResponse> creditMemos = new ArrayList<CreditMemoResponse>();
		creditMemos.add(creditMemoResponse);
		creditMemosResponse.setCreditMemos(creditMemos);
		Mockito.when(creditMemoBO.getSellerCreditMemos(Mockito.anyLong(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString())).thenReturn(creditMemosResponse);
		String createdFromDate = "";
		String createdToDate = "";
		CreditMemosResponse actualResponse = service.getCreditMemos(securityContext, sellerGuid, sortType, createdFromDate, createdToDate, null, null, "USD");
		Assert.assertNotNull(actualResponse);
		Assert.assertNotNull(creditMemosResponse.getCreditMemos());
		Assert.assertNotNull(creditMemosResponse.getCreditMemos().get(0).getCreditAmount());
		Assert.assertNotNull(creditMemosResponse.getCreditMemos().get(0).getAppliedDate());
		Assert.assertNotNull(creditMemosResponse.getCreditMemos().get(0).getBookOfBusinessId());
		Assert.assertNotNull(creditMemosResponse.getCreditMemos().get(0).getCreatedDate());
		Assert.assertNotNull(creditMemosResponse.getCreditMemos().get(0).getEventDate());
		Assert.assertNotNull(creditMemosResponse.getCreditMemos().get(0).getEventId());
		Assert.assertNotNull(creditMemosResponse.getCreditMemos().get(0).getId());
		Assert.assertNotNull(creditMemosResponse.getCreditMemos().get(0).getOrderId());
		Assert.assertNotNull(creditMemosResponse.getCreditMemos().get(0).getOrderStatus());
		Assert.assertNotNull(creditMemosResponse.getCreditMemos().get(0).getReferenceNumber());
		Assert.assertNotNull(creditMemosResponse.getCreditMemos().get(0).getStatus());
		Assert.assertNotNull(creditMemosResponse.getCreditMemos().get(0).getReason());
	}
	
	/**
	 * Test methods for getCreditMemo
	 */
	@Test
	public void testGetCreditMemosIndy() {
	
		SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
		CreditMemoBOImpl creditMemoBO = mock(CreditMemoBOImpl.class);
		ReflectionTestUtils.setField(service, "securityContextUtil", securityContextUtil);
		ReflectionTestUtils.setField(service, "creditMemoBO", creditMemoBO);
		CreditMemosResponse creditMemosResponse = new CreditMemosResponse();
		
		CreditMemoResponse creditMemoResponse = new CreditMemoResponse();
		creditMemoResponse.setCreditAmount(new Money(new BigDecimal(10), "USD"));
		creditMemoResponse.setAppliedDate("2012-09-03T12:00:00Z");
		creditMemoResponse.setBookOfBusinessId("1");
		creditMemoResponse.setCreatedDate("2012-09-03T12:00:00Z");
		creditMemoResponse.setEventDate("2012-09-03T12:00:00Z");
		creditMemoResponse.setEventId("12345");
		creditMemoResponse.setId("12345");
		creditMemoResponse.setOrderId("12345");
		creditMemoResponse.setSellerId("12345");
		creditMemoResponse.setOrderStatus("Sold");
		creditMemoResponse.setReferenceNumber("12345");
		creditMemoResponse.setStatus(SellerPaymentStatusEnum.COMPLETED);
		creditMemoResponse.setReason("reason");
		
		List<CreditMemoResponse> creditMemos = new ArrayList<CreditMemoResponse>();
		creditMemos.add(creditMemoResponse);
		creditMemosResponse.setCreditMemos(creditMemos);
		Mockito.when(creditMemoBO.getSellersCreditMemos( Mockito.anyString(), Mockito.anyString())).thenReturn(creditMemosResponse);
		String createdFromDate = "";
		String createdToDate = "";
		CreditMemosResponse actualResponse = indyService.getIndySellersCreditMemos(serviceContext, securityContext, createdFromDate, createdToDate);
		Assert.assertNotNull(actualResponse);
		Assert.assertNotNull(creditMemosResponse.getCreditMemos());
		Assert.assertNotNull(creditMemosResponse.getCreditMemos().get(0).getCreditAmount());
		Assert.assertNotNull(creditMemosResponse.getCreditMemos().get(0).getAppliedDate());
		Assert.assertNotNull(creditMemosResponse.getCreditMemos().get(0).getBookOfBusinessId());
		Assert.assertNotNull(creditMemosResponse.getCreditMemos().get(0).getCreatedDate());
		Assert.assertNotNull(creditMemosResponse.getCreditMemos().get(0).getEventDate());
		Assert.assertNotNull(creditMemosResponse.getCreditMemos().get(0).getEventId());
		Assert.assertNotNull(creditMemosResponse.getCreditMemos().get(0).getId());
		Assert.assertNotNull(creditMemosResponse.getCreditMemos().get(0).getOrderId());
		Assert.assertNotNull(creditMemosResponse.getCreditMemos().get(0).getOrderStatus());
		Assert.assertNotNull(creditMemosResponse.getCreditMemos().get(0).getReferenceNumber());
		Assert.assertNotNull(creditMemosResponse.getCreditMemos().get(0).getStatus());
		Assert.assertNotNull(creditMemosResponse.getCreditMemos().get(0).getReason());
		Assert.assertNotNull(creditMemosResponse.getCreditMemos().get(0).getSellerId());
	}
	
	
	@Test
	public void testGetCreditMemos_InvalidUser() throws UserNotAuthorizedException {
		String sellerGuid = "123456";		
		String sortType = "asc";
		String filters = "filters";
		
		SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
		CreditMemoBOImpl creditMemoBO = mock(CreditMemoBOImpl.class);
		ReflectionTestUtils.setField(service, "securityContextUtil", securityContextUtil);
		ReflectionTestUtils.setField(service, "creditMemoBO", creditMemoBO);
		
		Mockito.when(securityContext.getUserId()).thenReturn("abcdef");		
		String createdFromDate = "";
		String createdToDate = "";
		CreditMemosResponse actualResponse = service.getCreditMemos(securityContext, sellerGuid, sortType, createdFromDate, createdToDate, null, null, null);
		Assert.assertNotNull(actualResponse.getErrors());
		
		Mockito.doThrow(new UserNotAuthorizedException()).when(securityContextUtil).validateUserGuid((ExtendedSecurityContext)Mockito.anyObject(), Mockito.anyString());
		Mockito.when(securityContext.getUserId()).thenReturn("12345");		
		
		actualResponse = service.getCreditMemos(securityContext, sellerGuid, sortType, createdFromDate, createdToDate, null, null, null);
		Assert.assertTrue(actualResponse.getErrors().size()==1);
	}
	
	
	@Test
	public void testGetCreditMemosIndy_InvalidUser() throws UserNotAuthorizedException {
	
		
		SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
		CreditMemoBOImpl creditMemoBO = mock(CreditMemoBOImpl.class);
		ReflectionTestUtils.setField(indyService, "securityContextUtil", securityContextUtil);
		ReflectionTestUtils.setField(indyService, "creditMemoBO", creditMemoBO);
		
		Mockito.when(securityContext.getUserId()).thenReturn(null);		
		String createdFromDate = "";
		String createdToDate = "";
		CreditMemosResponse actualResponse = indyService.getIndySellersCreditMemos(serviceContext, securityContext, createdFromDate, createdToDate);
		Assert.assertNotNull(actualResponse.getErrors());
		
		Mockito.doThrow(new UserNotAuthorizedException()).when(securityContextUtil).authenticateUser((SHServiceContext)Mockito.anyObject(),(ExtendedSecurityContext)Mockito.anyObject());
		Mockito.when(securityContext.getUserId()).thenReturn("12345");		
		
		actualResponse = indyService.getIndySellersCreditMemos(serviceContext, securityContext, createdFromDate, createdToDate);
		Assert.assertTrue(actualResponse.getErrors().size()==1);
	}
	
	
	@Test
	public void testGetCreditMemosIndy_AccountException() {
		
		
		SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
		CreditMemoBOImpl creditMemoBO = mock(CreditMemoBOImpl.class);
		ReflectionTestUtils.setField(indyService, "securityContextUtil", securityContextUtil);
		ReflectionTestUtils.setField(indyService, "creditMemoBO", creditMemoBO);
		Mockito.when(creditMemoBO.getSellersCreditMemos(Mockito.anyString(), Mockito.anyString())).thenThrow(new AccountException());
			
		String createdFromDate = "";
		String createdToDate = "";
		CreditMemosResponse actualResponse =  indyService.getIndySellersCreditMemos(serviceContext, securityContext, createdFromDate, createdToDate);
		Assert.assertNotNull(actualResponse.getErrors());
	}
	
	@Test
	public void testGetCreditMemosIndy_Exception() {

		
		SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
		CreditMemoBOImpl creditMemoBO = mock(CreditMemoBOImpl.class);
		ReflectionTestUtils.setField(indyService, "securityContextUtil", securityContextUtil);
		ReflectionTestUtils.setField(indyService, "creditMemoBO", creditMemoBO);
		
		Mockito.when(creditMemoBO.getSellersCreditMemos(Mockito.anyString(), Mockito.anyString())).thenThrow(new RuntimeException());		
		String createdFromDate = "";
		String createdToDate = "";
		CreditMemosResponse actualResponse =  indyService.getIndySellersCreditMemos(serviceContext, securityContext, createdFromDate, createdToDate);
		Assert.assertNotNull(actualResponse.getErrors());
	}
		
	
	
	@Test
	public void testGetCreditMemos_AccountException() {
		String sellerGuid = "123456";		
		String sortType = "asc";
		String filters = "filters";
		
		SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
		CreditMemoBOImpl creditMemoBO = mock(CreditMemoBOImpl.class);
		ReflectionTestUtils.setField(service, "securityContextUtil", securityContextUtil);
		ReflectionTestUtils.setField(service, "creditMemoBO", creditMemoBO);
		Mockito.when(creditMemoBO.getSellerCreditMemos(Mockito.anyLong(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString())).thenThrow(new AccountException());
			
		String createdFromDate = "";
		String createdToDate = "";
		CreditMemosResponse actualResponse = service.getCreditMemos(securityContext, sellerGuid, sortType, createdFromDate, createdToDate, null, null, "GBP");
		Assert.assertNotNull(actualResponse.getErrors());
	}
	
	
	@Test
	public void testGetCreditMemos_Exception() {
		String sellerGuid = "123456";	
		String sortType = "asc";
		String filters = "filters";
		
		SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
		CreditMemoBOImpl creditMemoBO = mock(CreditMemoBOImpl.class);
		ReflectionTestUtils.setField(service, "securityContextUtil", securityContextUtil);
		ReflectionTestUtils.setField(service, "creditMemoBO", creditMemoBO);
		
		Mockito.when(creditMemoBO.getSellerCreditMemos(Mockito.anyLong(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString())).thenThrow(new RuntimeException());
		String createdFromDate = "";
		String createdToDate = "";
		CreditMemosResponse actualResponse = service.getCreditMemos(securityContext, sellerGuid, sortType, createdFromDate, createdToDate, null, null, "USD");
		Assert.assertNotNull(actualResponse.getErrors());
	}
		
}
