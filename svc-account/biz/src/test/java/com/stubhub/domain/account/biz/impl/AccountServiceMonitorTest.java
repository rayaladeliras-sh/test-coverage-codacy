package com.stubhub.domain.account.biz.impl;

import java.io.ByteArrayInputStream;


import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.cxf.jaxrs.client.WebClient;
import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class AccountServiceMonitorTest {
	
	private AccountServiceMonitor monitor;
	
	
	
	@Test
	public void testExecute(){
		AccountServiceMonitor monitor = new AccountServiceMonitor(){
			protected String getProperty(String propertyName, String defaultValue) {
				return defaultValue;
			}
			protected WebClient getWebClient(String uri){
				WebClient client = Mockito.mock(WebClient.class);
				Mockito.when(client.get()).thenReturn(getResponse());
				return client;
			}
		};
		monitor .execute();
		
	}
	
	
	@Test
	public void testExecute_ResponseNull(){
		AccountServiceMonitor monitor = new AccountServiceMonitor(){
			protected String getProperty(String propertyName, String defaultValue) {
				return defaultValue;
			}
			protected WebClient getWebClient(String uri){
				WebClient client = Mockito.mock(WebClient.class);
				Mockito.when(client.get()).thenReturn(null);
				return client;
			}
		};
		monitor .execute();
		
	}
	
	
	private Response getResponse() {
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
				byte[] b = new byte[100];
				return new ByteArrayInputStream(b);
			}
		};
		return response;
	}

}
