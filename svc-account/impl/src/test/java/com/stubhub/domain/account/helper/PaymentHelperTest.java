package com.stubhub.domain.account.helper;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.stubhub.domain.user.payments.intf.CreditCardDetails;
import com.stubhub.domain.user.payments.intf.CreditCardDetails.ExpDate;
import com.stubhub.domain.user.payments.intf.CustomerPaymentInstrumentDetails;
import com.stubhub.domain.user.payments.intf.CustomerPaymentInstrumentMappingsResponse;
import com.stubhub.domain.user.payments.intf.CustomerPaymentInstrumentsResponse;
import com.stubhub.platform.utilities.webservice.svclocator.SvcLocator;

public class PaymentHelperTest {
	
	private PaymentHelper paymenthelper;
	private SvcLocator svcLocator;
	private WebClient webClient;
	
	@BeforeMethod
	public void setUp(){
		paymenthelper = new PaymentHelper() {
			protected String getProperty(String propertyName, String defaultValue) {
				if("getallsellerpaymentinstruments.api.url".equals(propertyName)) {
					return "https://api-int.srwd34.com/user/customers/v1/{sellerId}/paymentInstruments";
				} else if("getpaymentinstrumentmappings.api.url".equals(propertyName)) {
					return "https://api-int.srwd34.com/user/customers/v1/{customerid}/paymentInstrumentMappings/{paymentinstrumentmappingsid}";
				}
				return "";
			}
		};
		svcLocator = Mockito.mock(SvcLocator.class);
		webClient = Mockito.mock(WebClient.class);
		ReflectionTestUtils.setField(paymenthelper, "svcLocator", svcLocator);
	}
	
	@Test
	public void testGetAllSellerPaymentInstruments() {
		Mockito.when(svcLocator.locate(Mockito.anyString(), Mockito.anyList())).thenReturn(webClient);
		Mockito.when(webClient.get()).thenReturn(getAllSellerPaymentInstrumentsResponse());
		List<CustomerPaymentInstrumentDetails> customerPaymentInstruments = paymenthelper.getAllSellerPaymentInstruments("B5D14E323CD55E9FE04400144F8AE084");
		Assert.assertNotNull(customerPaymentInstruments);
	}
	
	@Test
	public void testGetPaymentInstrumentMappings() {
		Mockito.when(svcLocator.locate(Mockito.anyString(), Mockito.anyList())).thenReturn(webClient);
		Mockito.when(webClient.get()).thenReturn(getGetPaymentInstrumentMappingsResponse());
		CustomerPaymentInstrumentMappingsResponse customerPaymentInstrumentMappingsResponse = paymenthelper.getPaymentInstrumentMappings("B5D14E323CD55E9FE04400144F8AE084", "12");
		Assert.assertNotNull(customerPaymentInstrumentMappingsResponse);
		Assert.assertNotNull(customerPaymentInstrumentMappingsResponse.getInternalId());
	}
	
	
	private Response getAllSellerPaymentInstrumentsResponse() {
		Response response =  new Response() {
			@Override
			public int getStatus() {
				return 200;
			}
			
			@Override
			public MultivaluedMap<String, Object> getMetadata() {				
				return null;
			}
			
			@Override
			public Object getEntity() {
				CustomerPaymentInstrumentsResponse customerPaymentInstrumentsResponse = new CustomerPaymentInstrumentsResponse();
				List<CustomerPaymentInstrumentDetails> customerPaymentInstruments = new ArrayList<CustomerPaymentInstrumentDetails>();
				CustomerPaymentInstrumentDetails customerPaymentInstrument = new CustomerPaymentInstrumentDetails();
				
				CreditCardDetails cardDetails = new CreditCardDetails();
				ExpDate expirationDate = new ExpDate();
				expirationDate.setMonth("12");
				expirationDate.setYear("2019");
				cardDetails.setExpirationDate(expirationDate);
				cardDetails.setLastFourDigits("1234");
				cardDetails.setSecurityCode("123");
				customerPaymentInstrument.setCardDetails(cardDetails);
				
				customerPaymentInstrument.setActive("Y");
				customerPaymentInstrument.setId("1234");
				customerPaymentInstruments.add(customerPaymentInstrument);
				customerPaymentInstrumentsResponse.setPaymentInstruments(customerPaymentInstruments);
				
				return customerPaymentInstrumentsResponse;
			}
		};
		return response;
	}
	
	private Response getGetPaymentInstrumentMappingsResponse() {
		Response response =  new Response() {
			@Override
			public int getStatus() {
				return 200;
			}
			
			@Override
			public MultivaluedMap<String, Object> getMetadata() {				
				return null;
			}
			
			@Override
			public Object getEntity() {
				CustomerPaymentInstrumentMappingsResponse customerPaymentInstrumentMappingsResponse = new CustomerPaymentInstrumentMappingsResponse();
				customerPaymentInstrumentMappingsResponse.setId("12");
				customerPaymentInstrumentMappingsResponse.setInternalId("123456");
				return customerPaymentInstrumentMappingsResponse;
			}
		};
		return response;
	}
	
}
