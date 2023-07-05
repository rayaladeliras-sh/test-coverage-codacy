package com.stubhub.domain.account.invoice;

import com.stubhub.domain.account.intf.AppliedCreditMemo;
import com.stubhub.domain.account.intf.Invoice;
import com.stubhub.domain.account.intf.InvoiceResponse;
import com.stubhub.domain.account.intf.Money;
import com.stubhub.domain.i18n.services.localization.v1.utility.DataSourceMessageSource;

import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static org.mockito.Mockito.when;

/**
 * Created at 12/30/2014 8:15 AM
 *
 * @author : Caron Zhao
 * @version : 1.0.0
 * @since : PI
 */
public class InvoiceGeneratorTest {
	
	private DataSourceMessageSource messageSource;
	private InvoiceGenerator invoiceGenerator = new PdfInvoiceGenerator();
	
	@BeforeMethod
	public void setUp(){
		messageSource = Mockito.mock(DataSourceMessageSource.class);
		ReflectionTestUtils.setField(invoiceGenerator, "messageSource", messageSource);
		Mockito.when(messageSource.getMessage("invoice.paymentReceipt",null,"Payment receipt", Locale.UK)).thenReturn("Payment receipt");
		Mockito.when(messageSource.getMessage("invoice.orderNum",null,"Order #", Locale.UK)).thenReturn("Order #");
		Mockito.when(messageSource.getMessage("invoice.event",null,"Event", Locale.UK)).thenReturn("Event");
		Mockito.when(messageSource.getMessage("invoice.eventDate",null,"Event date", Locale.UK)).thenReturn("Event date");
		Mockito.when(messageSource.getMessage("invoice.originalOrderPayout",null,"Original order payout", Locale.UK)).thenReturn("Original order payout");
		Mockito.when(messageSource.getMessage("invoice.creditMemoCharge",null,"Credit memo charge", Locale.UK)).thenReturn("Credit memo charge");
		Mockito.when(messageSource.getMessage("invoice.creditMemoTakenFromOrderNum",null,"Credit memo taken from order #", Locale.UK)).thenReturn("Credit memo taken from order #");
		Mockito.when(messageSource.getMessage("invoice.chargeAmount",null,"Charge amount", Locale.UK)).thenReturn("Charge amount");
		Mockito.when(messageSource.getMessage("invoice.totalAmountDeducted",null,"Total amount deducted", Locale.UK)).thenReturn("Total amount deducted");
		Mockito.when(messageSource.getMessage("invoice.netPayout",null,"Net payout", Locale.UK)).thenReturn("Net payout");
		Mockito.when(messageSource.getMessage("invoice.sentToPaypalOn",null,"Sent to Paypal on", Locale.UK)).thenReturn("Sent to Paypal on");
        Mockito.when(messageSource.getMessage("invoice.sentOn",null,"Sent on", Locale.UK)).thenReturn("Sent on");
        Mockito.when(messageSource.getMessage("invoice.sentTo",null,"Sent to", Locale.UK)).thenReturn("Sent to");
		Mockito.when(messageSource.getMessage("invoice.sentToPaypalAccount",null,"Sent to Paypal account", Locale.UK)).thenReturn("Sent to Paypal account");
		Mockito.when(messageSource.getMessage("invoice.transactionId",null,"Transaction ID", Locale.UK)).thenReturn("Transaction ID");
	}

    @Test
    public void testGenerateInvoiceWithEmptyAppliedCreditMemo() {
        InvoiceResponse invoiceResponse = Mockito.mock(InvoiceResponse.class);
        when(invoiceResponse.getOrderNumber()).thenReturn(Long.parseLong("166509959"));
        when(invoiceResponse.getSellerPaymentTypeId()).thenReturn(Long.parseLong("1"));
        when(invoiceResponse.getEventDescription()).thenReturn("The ARTPOP Ball - Lady Gaga Tickets");
        Calendar calendar = Calendar.getInstance();
        when(invoiceResponse.getEventDateLocal()).thenReturn(calendar);
        when(invoiceResponse.getOrderAmount()).thenReturn(new Money("1.0", "GBP"));
        Invoice invoice = Mockito.mock(Invoice.class);
        when(invoiceResponse.getInvoice()).thenReturn(invoice);
        when(invoice.getAppliedCreditMemos()).thenReturn(new ArrayList<AppliedCreditMemo>());
        when(invoiceResponse.getPayeeEmailId()).thenReturn("test@123.com");
        when(invoiceResponse.getPaymentSentToGatewayDate()).thenReturn(null);

        
        invoiceGenerator.generateInvoice("1234", invoiceResponse,Locale.UK);
    }
    
    
    
    @Test
    public void testGenerateInvoiceWithGatewayDate() {
        InvoiceResponse invoiceResponse = Mockito.mock(InvoiceResponse.class);
        when(invoiceResponse.getOrderNumber()).thenReturn(Long.parseLong("166509959"));
        when(invoiceResponse.getEventDescription()).thenReturn("The ARTPOP Ball - Lady Gaga Tickets");
        Calendar calendar = Calendar.getInstance();
        when(invoiceResponse.getEventDateLocal()).thenReturn(calendar);
        when(invoiceResponse.getOrderAmount()).thenReturn(new Money("1.0", "GBP"));
        Invoice invoice = Mockito.mock(Invoice.class);
        when(invoiceResponse.getInvoice()).thenReturn(invoice);
        when(invoice.getAppliedCreditMemos()).thenReturn(new ArrayList<AppliedCreditMemo>());
        when(invoiceResponse.getPayeeEmailId()).thenReturn("test@123.com");
        when(invoiceResponse.getPaymentSentToGatewayDate()).thenReturn(Calendar.getInstance());

        
        invoiceGenerator.generateInvoice("1234", invoiceResponse,Locale.UK);
    }

    @Test
    public void testGenerateInvoiceWithAppliedCreditMemo() {
        InvoiceResponse invoiceResponse = Mockito.mock(InvoiceResponse.class);
        when(invoiceResponse.getOrderNumber()).thenReturn(Long.parseLong("166509959"));
        when(invoiceResponse.getEventDescription()).thenReturn("The ARTPOP Ball - Lady Gaga Tickets");
        Calendar calendar = Calendar.getInstance();
        when(invoiceResponse.getEventDateLocal()).thenReturn(calendar);
        when(invoiceResponse.getOrderAmount()).thenReturn(new Money("1.0", "GBP"));
        Invoice invoice = Mockito.mock(Invoice.class);
        when(invoiceResponse.getInvoice()).thenReturn(invoice);
        when(invoice.getAppliedCreditMemos()).thenReturn(mockAppliedCreditMemos());
        when(invoiceResponse.getPayeeEmailId()).thenReturn("test@123.com");
        when(invoiceResponse.getPaymentSentToGatewayDate()).thenReturn(null);
        invoiceGenerator.generateInvoice("1234", invoiceResponse,Locale.UK);
    }

    private List<AppliedCreditMemo> mockAppliedCreditMemos() {
        List<AppliedCreditMemo> appliedCreditMemos = new ArrayList<AppliedCreditMemo>();
        AppliedCreditMemo appliedCreditMemo = new AppliedCreditMemo();
        appliedCreditMemo.setEventId(Long.valueOf(1));
        appliedCreditMemo.setEventName("ABC");
        appliedCreditMemo.setEvetnDate(Calendar.getInstance());
        appliedCreditMemo.setAmount(new BigDecimal(0));
        appliedCreditMemo.setCurrency("GBP");
        appliedCreditMemo.setOrderId(Long.valueOf(1));
        appliedCreditMemos.add(appliedCreditMemo);
        return appliedCreditMemos;
    }
}