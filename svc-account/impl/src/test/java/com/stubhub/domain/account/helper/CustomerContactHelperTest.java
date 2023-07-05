package com.stubhub.domain.account.helper;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.stubhub.domain.user.contactsV2.intf.CustomerContactV2Details;
import com.stubhub.platform.utilities.webservice.svclocator.SvcLocator;

public class CustomerContactHelperTest {

	private SvcLocator svcLocator;
	private WebClient webClient;
	private CustomerContactHelper customerContactHelper;

	@BeforeMethod
	public void setUp(){
		svcLocator = Mockito.mock(SvcLocator.class);
		webClient = Mockito.mock(WebClient.class);
		customerContactHelper = new CustomerContactHelper() {
			protected String getProperty(String propertyName, String defaultValue) {
					return "http://api-int.${default_domain}/user/customers/v2/{customerguid}/contactsV2/{contactId}";
			}
		};
		ReflectionTestUtils.setField(customerContactHelper, "svcLocator", svcLocator);
	}
	
	@Test
	public void testgetCustomerContactDetails_HappyPath() {
		Mockito.when(svcLocator.locate(Mockito.anyString(), Mockito.anyList())).thenReturn(webClient);
		Mockito.when(webClient.get()).thenReturn(getResponse_200());
		CustomerContactV2Details customerContactDetails = null;
		try {
			customerContactDetails = customerContactHelper.getCustomerContactDetails("guid","uid");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Assert.assertNotNull(customerContactDetails);
	}
	
	@Test 
	public void testgetCustomerContactDetails_BadReq() {
		Mockito.when(svcLocator.locate(Mockito.anyString(), Mockito.anyList())).thenReturn(webClient);
		Mockito.when(webClient.get()).thenReturn(getResponse_400());
		CustomerContactV2Details customerContactDetails = null;
		try {
			customerContactDetails = customerContactHelper.getCustomerContactDetails("guid","uid");
		} catch (Exception e) {
				e.printStackTrace();
		}
		Assert.assertNull(customerContactDetails);
	}
	
	@Test 
	public void testgetCustomerContactDetails_NotFound() {
		Mockito.when(svcLocator.locate(Mockito.anyString(), Mockito.anyList())).thenReturn(webClient);
		Mockito.when(webClient.get()).thenReturn(getResponse_404());
		CustomerContactV2Details customerContactDetails = null;
		try {
			customerContactDetails = customerContactHelper.getCustomerContactDetails("guid","uid");
		} catch (Exception e) {
				e.printStackTrace();
		}
		Assert.assertNull(customerContactDetails);
	}
	
	@Test 
	public void testgetCustomerContactDetails_SysError() {
		Mockito.when(svcLocator.locate(Mockito.anyString(), Mockito.anyList())).thenReturn(webClient);
		Mockito.when(webClient.get()).thenReturn(getResponse_500());
		CustomerContactV2Details customerContactDetails = null;
		try {
			customerContactDetails = customerContactHelper.getCustomerContactDetails("guid","uid");
		} catch (Exception e) {
				e.printStackTrace();
		}
		Assert.assertNull(customerContactDetails);
	}
	
	private Response getResponse_200 () {
		Response response =  new Response() {
		
			@Override
			public int getStatus() { return 200; }
			
			@Override
			public MultivaluedMap<String, Object> getMetadata() { return null; }
			
			@Override
			public Object getEntity() {
				CustomerContactV2Details  customerContactDetails = new CustomerContactV2Details();	
	    		return customerContactDetails;
			}
		};	
		return response;
	}
	
	private Response getResponse_400 () {
		Response response =  new Response() {
		
			@Override
			public int getStatus() { return 400; }
			
			@Override
			public MultivaluedMap<String, Object> getMetadata() { return null; }
			
			@Override
			public Object getEntity() {
				CustomerContactV2Details  customerContactDetails = new CustomerContactV2Details();	
	    		return customerContactDetails;
			}
		};	
		return response;
	}

	private Response getResponse_404 () {
		Response response =  new Response() {
		
			@Override
			public int getStatus() { return 404; }
			
			@Override
			public MultivaluedMap<String, Object> getMetadata() { return null; }
			
			@Override
			public Object getEntity() {
				CustomerContactV2Details  customerContactDetails = new CustomerContactV2Details();	
	    		return customerContactDetails;
			}
		};	
		return response;
	}

	private Response getResponse_500 () {
		Response response =  new Response() {
		
			@Override
			public int getStatus() { return 500; }
			
			@Override
			public MultivaluedMap<String, Object> getMetadata() { return null; }
			
			@Override
			public Object getEntity() {
				CustomerContactV2Details  customerContactDetails = new CustomerContactV2Details();	
	    		return customerContactDetails;
			}
		};	
		return response;
	}

}
