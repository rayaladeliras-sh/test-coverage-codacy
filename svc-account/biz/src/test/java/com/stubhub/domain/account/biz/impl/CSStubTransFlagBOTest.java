package com.stubhub.domain.account.biz.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import junit.framework.Assert;

import org.apache.cxf.jaxrs.client.WebClient;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.stubhub.domain.cs.datamodel.entity.TransactionFlag;
import com.stubhub.domain.cs.intf.TransactionFlagResponse;
import com.stubhub.platform.utilities.webservice.svclocator.SvcLocator;

public class CSStubTransFlagBOTest {
	private SvcLocator svcLocator;
	private WebClient webClient;
	private CSStubTransFlagBOImpl csStubTransFlagBOImpl ;
	
	@BeforeMethod
	public void setUp(){
		csStubTransFlagBOImpl = new CSStubTransFlagBOImpl() {
			protected String getProperty(String propertyName, String defaultValue) {
				if ("cs.get.transactionFlag.api.url".equals(propertyName)) {
					return "http://api-int.stubhub.com/cs/orders/v1/transactionFlag/{orderId}";
				}
				return "";	
			}			
		};
		svcLocator = Mockito.mock(SvcLocator.class);
		webClient  = Mockito.mock(WebClient.class);
		ReflectionTestUtils.setField(csStubTransFlagBOImpl, "svcLocator", svcLocator);
	}
	
	@Test
	public void testGetCSStubTransFlag() throws Exception {
		TransactionFlagResponse transactionFlagResponse = new TransactionFlagResponse();
		List<TransactionFlag> transactionFlagList = new ArrayList<TransactionFlag>();
		TransactionFlag transactionFlag = new TransactionFlag();
		transactionFlag.setId(8L);
		transactionFlagList.add(transactionFlag);
		transactionFlagResponse.setTransFlag(transactionFlagList);
		Response response = Response.status(Status.NOT_FOUND).entity(transactionFlagResponse).build();
		Mockito.when(svcLocator.locate(Mockito.anyString(), Mockito.anyList())).thenReturn(webClient);
		Mockito.when(webClient.get()).thenReturn(response);
		List<String> orderIds = new ArrayList<String>();
		orderIds.add("1234");
		orderIds.add("5678");
		Map<String, Boolean> flags = csStubTransFlagBOImpl.getCSStubTransFlag(orderIds);
		Assert.assertNotNull(flags);	
	}
}
