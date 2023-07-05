/**
 * 
 */
package com.stubhub.domain.account.helper;

import java.io.ByteArrayInputStream;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.stubhub.platform.utilities.webservice.svclocator.SvcLocator;

/**
 * @author sjayaswal
 *
 */
public class SellerEligibilityHelperTest {

	private SvcLocator svcLocator;
	private WebClient webClient;
	private SellerEligibilityHelper helper ;
	
	@BeforeMethod
	public void setUp(){
		helper = new SellerEligibilityHelper() {
			protected String getProperty(String propertyName, String defaultValue) {
				if ("seller.rules.api.url".equals(propertyName)) {
					return "https://api-int.srwd59.com/inventorynew/inventorynew/eligibility/v1";
				}
				return "";	
			}			
		};
		svcLocator = Mockito.mock(SvcLocator.class);
		webClient  = Mockito.mock(WebClient.class);
		ReflectionTestUtils.setField(helper, "svcLocator", svcLocator);
	}
	
	@Test
	public void checkSellerEligibilityTest() throws Exception{
		Mockito.when(svcLocator.locate(Mockito.anyString())).thenReturn(webClient);
		Mockito.when(webClient.get()).thenReturn(getResponse());

		helper.checkSellerEligibility();
	}
	
	@Test
	public void checkSellerEligibilityTest_negative() throws Exception{
		Mockito.when(svcLocator.locate(Mockito.anyString())).thenReturn(webClient);
		Mockito.when(webClient.get()).thenReturn(getResponseError());

		helper.checkSellerEligibility();
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
				String responseString = "{\"allowed\": \"YES\", \"reasons\": []}";
				return new ByteArrayInputStream(responseString.getBytes());
			}
		};
		return response;
	}
	
	private Response getResponseError() {
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
				String responseString = "{\"allowed\": \"NO\", \"reasons\": []}";
				return new ByteArrayInputStream(responseString.getBytes());
			}
		};
		return response;
	}
	/*
	private Response getResponse( final boolean isSellerEligible) {
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
				return populateResponse(isSellerEligible);
			}
		};
		return response;
	}
	
	private SellerEligibilityRulesResponse populateResponse( boolean isSellerEligible){
		
		SellerEligibilityRulesResponse response = new SellerEligibilityRulesResponse();
		if(isSellerEligible){
			response.setAllowed("YES");
		}else{
			response.setAllowed("NO");
			List<Reason> reasons = new ArrayList<Reason>();
			Reason reason = new Reason();
			reason.setDescription("some description");
			reason.setReasonCode("reason code");
			
			reasons.add(reason);
			response.setReasons(reasons);
			
		}
		return response;
	}*/
}
