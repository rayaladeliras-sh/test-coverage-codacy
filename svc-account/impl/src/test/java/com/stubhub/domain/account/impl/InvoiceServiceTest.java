package com.stubhub.domain.account.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertNotNull;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.stubhub.domain.account.biz.intf.InvoiceBO;
import com.stubhub.domain.account.common.util.SecurityContextUtil;
import com.stubhub.domain.account.exception.InvoiceNotFoundException;
import com.stubhub.domain.account.exception.SellerUnauthorizedException;
import com.stubhub.domain.account.exception.UnauthorizedException;
import com.stubhub.domain.account.intf.AppliedCreditMemo;
import com.stubhub.domain.account.intf.Invoice;
import com.stubhub.domain.account.intf.InvoiceResponse;
import com.stubhub.domain.account.intf.Money;
import com.stubhub.domain.account.invoice.InvoiceGenerator;
import com.stubhub.domain.account.invoice.InvoiceGeneratorFactory;
import com.stubhub.newplatform.property.MasterStubHubProperties;
import com.stubhub.platform.utilities.webservice.security.ExtendedSecurityContext;

public class InvoiceServiceTest {

    private InvoiceServiceImpl service;

    private InvoiceBO invoiceBO;

    private SecurityContextUtil securityContextUtil;

    private InvoiceGeneratorFactory invoiceGeneratorFactory;
    
    private InvoiceGenerator invoiceGenerator;
    
    @BeforeMethod
    public void setUp() throws Exception {
        service = new InvoiceServiceImpl();
        invoiceBO = mock(InvoiceBO.class);
        securityContextUtil = new SecurityContextUtil();
        invoiceGeneratorFactory = mock(InvoiceGeneratorFactory.class);
        invoiceGenerator = mock(InvoiceGenerator.class);
        ReflectionTestUtils.setField(service, "invoiceBO", invoiceBO);
        ReflectionTestUtils.setField(service, "securityContextUtil", securityContextUtil);
        ReflectionTestUtils.setField(service, "invoiceGeneratorFactory", invoiceGeneratorFactory);
        when(invoiceGeneratorFactory.getInvoiceGenerator(InvoiceGeneratorFactory.InvoiceGeneratorType.PDF)).thenReturn(invoiceGenerator);
        Map<String, String> props = new HashMap<String, String>();
        Field field = MasterStubHubProperties.class.getDeclaredField("props");
        field.setAccessible(true);
        field.set(null, props);
        MasterStubHubProperties.setProperty("cs.siebel.userName.qa", null);
        MasterStubHubProperties.setProperty("cs.siebel.userName.prod", null);
    }

    @Test
    public void testGetInvoiceByReferenceNumber() throws Exception {
        Long userId = 1234L;
        String refNumber = "refNumber";
        String guid = "guid";
        ExtendedSecurityContext context = mock(ExtendedSecurityContext.class);
        when(context.getUserGuid()).thenReturn(guid);
        when(context.getUserId()).thenReturn(userId.toString());
        when(context.getUserName()).thenReturn("userName");
        Invoice invoice = new Invoice();
        invoice.setTotal(new Money("5", "USD"));
        InvoiceResponse response = new InvoiceResponse();
        response.setInvoice(invoice);
        response.setSellerId(userId);
        when(invoiceBO.getByReferenceNumber(refNumber, null, "en-us")).thenReturn(response);

        Response result = service.getInvoiceByReferenceNumber(context, guid, refNumber, null,"en-us");
        InvoiceResponse invoiceResponse = (InvoiceResponse)result.getEntity();
        assertNotNull(invoiceResponse.getInvoice());
    }
    
    
    @Test
    public void testGetPdfInvoiceByReferenceNumberWithLocale() throws Exception {
        Long userId = 1234L;
        String refNumber = "refNumber";
        String guid = "guid";
        ExtendedSecurityContext context = mock(ExtendedSecurityContext.class);
        when(context.getUserGuid()).thenReturn(guid);
        when(context.getUserId()).thenReturn(userId.toString());
        when(context.getUserName()).thenReturn("userName");
        Invoice invoice = new Invoice();
        invoice.setTotal(new Money("5", "USD"));
        InvoiceResponse response = new InvoiceResponse();
        response.setInvoice(invoice);
        response.setSellerId(userId);
        when(invoiceBO.getByReferenceNumber(refNumber, null,"en-us")).thenReturn(response);
        ByteArrayOutputStream baosPDF = new ByteArrayOutputStream();
        when(invoiceGenerator.generateInvoice(Mockito.anyString(), Mockito.any(InvoiceResponse.class), Mockito.any(Locale.class))).thenReturn(baosPDF);
        Response result = service.getPdfInvoiceByReferenceNumber(context,guid, refNumber,"en-us",null,"en-us");
        assertNotNull(result);
    }
    
    

    @Test(expectedExceptions = UnauthorizedException.class)
    public void testGetInvoiceByReferenceNumberNotAuthorized() {
        String refNumber = "refNumber";
        String guid = "guid";
        ExtendedSecurityContext context = null;
        service.getInvoiceByReferenceNumber(context, guid, refNumber, null, "en-us");
    }

    @Test(expectedExceptions = InvoiceNotFoundException.class)
    public void testGetInvoiceByReferenceNumberNoInvoice() throws Exception {
        Long userId = 1234L;
        String refNumber = "refNumber";
        String guid = "guid";
        ExtendedSecurityContext context = mock(ExtendedSecurityContext.class);
        when(context.getUserGuid()).thenReturn(guid);
        when(context.getUserId()).thenReturn(userId.toString());
        when(context.getUserName()).thenReturn("userName");
        InvoiceResponse response = new InvoiceResponse();
        response.setInvoice(null);
        when(invoiceBO.getByReferenceNumber(refNumber, null, "en-us")).thenReturn(response);
        service.getInvoiceByReferenceNumber(context, guid, refNumber, null, "en-us");
    }

    @Test(expectedExceptions = SellerUnauthorizedException.class)
    public void testGetInvoiceByReferenceNumberSellerValidationException() throws Exception{
        Long userId = 1234L;
        String refNumber = "refNumber";
        String guid = "guid";
        ExtendedSecurityContext context = mock(ExtendedSecurityContext.class);
        when(context.getUserGuid()).thenReturn(guid);
        when(context.getUserId()).thenReturn(userId.toString());
        when(context.getUserName()).thenReturn("userName");
        Invoice invoice = new Invoice();
        invoice.setTotal(new Money("5", "USD"));
        InvoiceResponse response = new InvoiceResponse();
        response.setInvoice(invoice);
        response.setSellerId(userId+1);
        when(invoiceBO.getByReferenceNumber(refNumber, null,"en-us")).thenReturn(response);

        Response result = service.getInvoiceByReferenceNumber(context, guid, refNumber, null, "en-us");
        InvoiceResponse invoiceResponse = (InvoiceResponse)result.getEntity();
        assertNotNull(invoiceResponse.getInvoice());
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testGetPdfInvoiceByReferenceNumber() throws Exception {
        Long userId = 1234L;
        String refNumber = "refNumber";
        String guid = "guid";
        ExtendedSecurityContext context = mock(ExtendedSecurityContext.class);
        when(context.getUserGuid()).thenReturn(guid);
        when(context.getUserId()).thenReturn(userId.toString());
        when(context.getUserName()).thenReturn("userName");
        InvoiceResponse invoiceResponse = Mockito.mock(InvoiceResponse.class);
        when(invoiceResponse.getOrderNumber()).thenReturn(Long.parseLong("166509959"));
        when(invoiceResponse.getEventDescription()).thenReturn("The ARTPOP Ball - Lady Gaga Tickets");
        Calendar calendar = Calendar.getInstance();
        when(invoiceResponse.getEventDateLocal()).thenReturn(calendar);
        when(invoiceResponse.getOrderAmount()).thenReturn(new Money("1.0", "GBP"));
        Invoice invoice = Mockito.mock(Invoice.class);
        when(invoiceResponse.getInvoice()).thenReturn(invoice);
        when(invoiceResponse.getSellerId()).thenReturn(userId);
        when(invoice.getAppliedCreditMemos()).thenReturn(new ArrayList<AppliedCreditMemo>());
        when(invoiceResponse.getPayeeEmailId()).thenReturn("test@123.com");
        when(invoiceResponse.getPaymentSentToGatewayDate()).thenReturn(null);
        when(invoiceBO.getByReferenceNumber(refNumber, null, "en-us")).thenReturn(invoiceResponse);
        service.getPdfInvoiceByReferenceNumber(context, guid, refNumber,"",null, "en-us");
    }
}