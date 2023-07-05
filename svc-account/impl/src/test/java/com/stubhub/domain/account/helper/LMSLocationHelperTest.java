package com.stubhub.domain.account.helper;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import com.stubhub.domain.account.datamodel.dao.StubTransFmDmDAO;
import com.stubhub.domain.account.datamodel.dao.impl.StubTransFmDmDAOImpl;
import com.stubhub.domain.account.datamodel.entity.StubTransFmDm;
import org.apache.cxf.jaxrs.client.ResponseReader;
import org.apache.cxf.jaxrs.client.WebClient;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.StringUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.stubhub.domain.account.common.enums.DeliveryOption;
import com.stubhub.domain.account.intf.MyOrderResponse;
import com.stubhub.domain.fulfillment.lms.v1.intf.EventLmsLocation;
import com.stubhub.domain.fulfillment.lms.v1.intf.LMSLocationsResponse;
import com.stubhub.newplatform.property.MasterStubhubPropertiesWrapper;
import com.stubhub.platform.utilities.webservice.svclocator.SvcLocator;

public class LMSLocationHelperTest {

	LMSLocationHelper helper;
	SvcLocator svcLocator;
	StubTransFmDmDAO stubTransFmDmDAO;
	@BeforeMethod
	public void setup(){
		helper = new LMSLocationHelper();
		svcLocator = Mockito.mock(SvcLocator.class);
		stubTransFmDmDAO = Mockito.mock(StubTransFmDmDAOImpl.class);
		MasterStubhubPropertiesWrapper properties = Mockito.mock(MasterStubhubPropertiesWrapper.class);
		ReflectionTestUtils.setField(helper, "properties", properties);
		ReflectionTestUtils.setField(helper, "svcLocator", svcLocator);
		ReflectionTestUtils.setField(helper, "stubTransFmDmDAO", stubTransFmDmDAO);
		Mockito.when(properties.getProperty("pro.fulfillment.LMSLocation.api.url")).thenReturn("http://www.test.com");

	}
	@Test
	public void testSetLMSLocation4MyOrders(){


		List<MyOrderResponse> orders = new ArrayList<MyOrderResponse>();
		helper.setLMSLocation4MyOrders(null, null);
		helper.setLMSLocation4MyOrders(orders, null);
		MyOrderResponse resp = new MyOrderResponse();
		resp.setDeliveryOption(DeliveryOption.LMS);
		resp.setEventId(1234L);
		resp.setOrderId(1111L);
		orders.add(resp);
		List<StubTransFmDm> fmDms = new ArrayList<StubTransFmDm>();
		StubTransFmDm fmdm = new StubTransFmDm();
		fmdm.setLmsLocationId(11111L);
		fmdm.setTid(1111L);
		fmDms.add(fmdm);
		Mockito.when(stubTransFmDmDAO.getFmDmByTids(Mockito.anyList())).thenReturn(fmDms);

		
		WebClient webClient = Mockito.mock(WebClient.class);
		Mockito.when(svcLocator.locate(Mockito.anyString(), Mockito.anyListOf(ResponseReader.class))).thenReturn(webClient);
		Response response = Mockito.mock(Response.class);
		LMSLocationsResponse LMSLocation = new LMSLocationsResponse();


		List<com.stubhub.domain.fulfillment.lms.v1.intf.LMSLocation> lms = new ArrayList<com.stubhub.domain.fulfillment.lms.v1.intf.LMSLocation>();
		com.stubhub.domain.fulfillment.lms.v1.intf.LMSLocation ll1 = new com.stubhub.domain.fulfillment.lms.v1.intf.LMSLocation();
		ll1.setId(11111L);
		ll1.setAddress2("lms location1 address2  ");
		ll1.setAddress3("lms location1 address3");
		ll1.setState("CA");
		ll1.setCity("jose");
		lms.add(ll1);
		LMSLocation.setLmsLocations(lms);
		Mockito.when(response.getEntity()).thenReturn(LMSLocation);
		Mockito.when(response.getStatus()).thenReturn(200);
		Mockito.when(webClient.get()).thenReturn(response);
		

		helper.setLMSLocation4MyOrders(orders, "en=US");
		Assert.assertEquals(orders.get(0).getLmsLocation(), "lms location1 address2 lms location1 address3, jose, CA");
	}

	@Test
	public void testSetLMSLocation4MyOrdersWithResponseNot200(){


		List<MyOrderResponse> orders = new ArrayList<MyOrderResponse>();

		MyOrderResponse resp = new MyOrderResponse();
		resp.setDeliveryOption(DeliveryOption.LMS);
		resp.setEventId(1234L);
		resp.setOrderId(1111L);
		orders.add(resp);
		List<StubTransFmDm> fmDms = new ArrayList<StubTransFmDm>();
		StubTransFmDm fmdm = new StubTransFmDm();
		fmdm.setLmsLocationId(11111L);
		fmdm.setTid(1111L);
		fmDms.add(fmdm);
		Mockito.when(stubTransFmDmDAO.getFmDmByTids(Mockito.anyList())).thenReturn(fmDms);


		WebClient webClient = Mockito.mock(WebClient.class);
		Mockito.when(svcLocator.locate(Mockito.anyString(), Mockito.anyListOf(ResponseReader.class))).thenReturn(webClient);
		Response response = Mockito.mock(Response.class);
		LMSLocationsResponse LMSLocation = new LMSLocationsResponse();


		List<com.stubhub.domain.fulfillment.lms.v1.intf.LMSLocation> lms = new ArrayList<com.stubhub.domain.fulfillment.lms.v1.intf.LMSLocation>();
		com.stubhub.domain.fulfillment.lms.v1.intf.LMSLocation ll1 = new com.stubhub.domain.fulfillment.lms.v1.intf.LMSLocation();
		ll1.setId(11111L);
		ll1.setAddress2("lms location1 address2  ");
		ll1.setAddress3("lms location1 address3");
		ll1.setState("CA");
		ll1.setCity("jose");
		lms.add(ll1);
		LMSLocation.setLmsLocations(lms);
		Mockito.when(response.getEntity()).thenReturn(LMSLocation);
		Mockito.when(response.getStatus()).thenReturn(400);
		Mockito.when(webClient.get()).thenReturn(response);


		helper.setLMSLocation4MyOrders(orders, "en=US");
		Assert.assertTrue(orders.get(0).getLmsLocation() == null);
	}

	@Test
	public void testSetLMSLocation4MyOrdersWithLMSLocationResponseIsNull(){


		List<MyOrderResponse> orders = new ArrayList<MyOrderResponse>();
		helper.setLMSLocation4MyOrders(null, null);
		helper.setLMSLocation4MyOrders(orders, null);
		MyOrderResponse resp = new MyOrderResponse();
		resp.setDeliveryOption(DeliveryOption.LMS);
		resp.setEventId(1234L);
		resp.setOrderId(1111L);
		orders.add(resp);
		List<StubTransFmDm> fmDms = new ArrayList<StubTransFmDm>();
		StubTransFmDm fmdm = new StubTransFmDm();
		fmdm.setLmsLocationId(11111L);
		fmdm.setTid(1111L);
		fmDms.add(fmdm);
		Mockito.when(stubTransFmDmDAO.getFmDmByTids(Mockito.anyList())).thenReturn(fmDms);


		WebClient webClient = Mockito.mock(WebClient.class);
		Mockito.when(svcLocator.locate(Mockito.anyString(), Mockito.anyListOf(ResponseReader.class))).thenReturn(webClient);
		Response response = Mockito.mock(Response.class);


		Mockito.when(response.getEntity()).thenReturn(null);
		Mockito.when(response.getStatus()).thenReturn(200);
		Mockito.when(webClient.get()).thenReturn(response);


		helper.setLMSLocation4MyOrders(orders, "en=US");
		Assert.assertTrue(orders.get(0).getLmsLocation() == null);
	}

	@Test
	public void testSetMultipleLMSLocation4MyOrders(){


		List<MyOrderResponse> orders = new ArrayList<MyOrderResponse>();
		helper.setLMSLocation4MyOrders(null, null);
		helper.setLMSLocation4MyOrders(orders, null);
		MyOrderResponse resp = new MyOrderResponse();
		resp.setDeliveryOption(DeliveryOption.LMS);
		resp.setEventId(1234L);
		resp.setOrderId(1111L);
		orders.add(resp);
		MyOrderResponse resp1 = new MyOrderResponse();
		resp1.setDeliveryOption(DeliveryOption.LMS);
		resp1.setEventId(12345L);
		resp1.setOrderId(2222L);
		orders.add(resp1);
		MyOrderResponse resp2 = new MyOrderResponse();
		resp2.setDeliveryOption(DeliveryOption.LMS);
		resp2.setEventId(123456L);
		resp2.setOrderId(3333L);
		orders.add(resp2);
		List<StubTransFmDm> fmDms = new ArrayList<StubTransFmDm>();
		StubTransFmDm fmdm = new StubTransFmDm();
		fmdm.setLmsLocationId(22222L);
		fmdm.setTid(2222L);
		StubTransFmDm fmdm1 = new StubTransFmDm();
		fmdm1.setLmsLocationId(11111L);
		fmdm1.setTid(1111L);
		StubTransFmDm fmdm2 = new StubTransFmDm();
		fmdm2.setLmsLocationId(33333L);
		fmdm2.setTid(3333L);
		fmDms.add(fmdm1);
		fmDms.add(fmdm);
		fmDms.add(fmdm2);


		Mockito.when(stubTransFmDmDAO.getFmDmByTids(Mockito.anyList())).thenReturn(fmDms);


		WebClient webClient = Mockito.mock(WebClient.class);
		Mockito.when(svcLocator.locate(Mockito.anyString(), Mockito.anyListOf(ResponseReader.class))).thenReturn(webClient);
		Response response = Mockito.mock(Response.class);
		final LMSLocationsResponse LMSLocation = new LMSLocationsResponse();
		final LMSLocationsResponse LMSLocation1 = new LMSLocationsResponse();
		final LMSLocationsResponse LMSLocation2 = new LMSLocationsResponse();
		List<com.stubhub.domain.fulfillment.lms.v1.intf.LMSLocation> lms = new ArrayList<com.stubhub.domain.fulfillment.lms.v1.intf.LMSLocation>();
		List<com.stubhub.domain.fulfillment.lms.v1.intf.LMSLocation> lms1 = new ArrayList<com.stubhub.domain.fulfillment.lms.v1.intf.LMSLocation>();
		List<com.stubhub.domain.fulfillment.lms.v1.intf.LMSLocation> lms2 = new ArrayList<com.stubhub.domain.fulfillment.lms.v1.intf.LMSLocation>();

		com.stubhub.domain.fulfillment.lms.v1.intf.LMSLocation ll1 = new com.stubhub.domain.fulfillment.lms.v1.intf.LMSLocation();
		ll1.setId(11111L);
		ll1.setAddress2("lms location1 address2  ");
		ll1.setAddress3("lms location1 address3");
		ll1.setState("CA");
		ll1.setCity("jose");
		lms.add(ll1);
		com.stubhub.domain.fulfillment.lms.v1.intf.LMSLocation ll2 = new com.stubhub.domain.fulfillment.lms.v1.intf.LMSLocation();
		ll2.setId(22222L);
		ll2.setAddress2("lms location2 address2  ");
		ll2.setAddress3("lms location2 address3");
		ll2.setState("CA");
		ll2.setCity("jose");
		lms1.add(ll2);
		com.stubhub.domain.fulfillment.lms.v1.intf.LMSLocation ll3 = new com.stubhub.domain.fulfillment.lms.v1.intf.LMSLocation();
		ll3.setId(33333L);
		ll3.setAddress2("lms location3 address2  ");
		ll3.setAddress3("lms location3 address3");
		ll3.setState("CA");
		ll3.setCity("jose");
		lms2.add(ll3);
		LMSLocation.setLmsLocations(lms);
		LMSLocation1.setLmsLocations(lms1);
		LMSLocation2.setLmsLocations(lms2);
		Mockito.when(response.getEntity()).thenAnswer(new Answer() {
			private int count = 0;

			public Object answer(InvocationOnMock invocation) {
				count ++;
				if (count == 1) {
					return LMSLocation;
				}
				else if (count ==2) {
					return LMSLocation1;
				}

				return LMSLocation2;
			}
		});


		Mockito.when(response.getStatus()).thenReturn(200);
		Mockito.when(webClient.get()).thenReturn(response);


		helper.setLMSLocation4MyOrders(orders, "en=US");
		Assert.assertEquals(orders.get(0).getLmsLocation(), "lms location1 address2 lms location1 address3, jose, CA");
		Assert.assertEquals(orders.get(1).getLmsLocation(), "lms location2 address2 lms location2 address3, jose, CA");
		Assert.assertEquals(orders.get(2).getLmsLocation(), "lms location3 address2 lms location3 address3, jose, CA");
	}

	@Test
	public void testSetMultipleLMSLocation4MyOrdersWithSameLMSLocationId(){


		List<MyOrderResponse> orders = new ArrayList<MyOrderResponse>();
		helper.setLMSLocation4MyOrders(null, null);
		helper.setLMSLocation4MyOrders(orders, null);
		MyOrderResponse resp = new MyOrderResponse();
		resp.setDeliveryOption(DeliveryOption.LMS);
		resp.setEventId(1234L);
		resp.setOrderId(1111L);
		orders.add(resp);
		MyOrderResponse resp1 = new MyOrderResponse();
		resp1.setDeliveryOption(DeliveryOption.LMS);
		resp1.setEventId(12345L);
		resp1.setOrderId(2222L);
		orders.add(resp1);
		List<StubTransFmDm> fmDms = new ArrayList<StubTransFmDm>();
		StubTransFmDm fmdm = new StubTransFmDm();
		fmdm.setLmsLocationId(22222L);
		fmdm.setTid(2222L);
		StubTransFmDm fmdm1 = new StubTransFmDm();
		fmdm1.setLmsLocationId(22222L);
		fmdm1.setTid(1111L);
		fmDms.add(fmdm1);
		fmDms.add(fmdm);



		Mockito.when(stubTransFmDmDAO.getFmDmByTids(Mockito.anyList())).thenReturn(fmDms);


		WebClient webClient = Mockito.mock(WebClient.class);
		Mockito.when(svcLocator.locate(Mockito.anyString(), Mockito.anyListOf(ResponseReader.class))).thenReturn(webClient);
		Response response = Mockito.mock(Response.class);
		final LMSLocationsResponse LMSLocation = new LMSLocationsResponse();
		List<com.stubhub.domain.fulfillment.lms.v1.intf.LMSLocation> lms1 = new ArrayList<com.stubhub.domain.fulfillment.lms.v1.intf.LMSLocation>();


		com.stubhub.domain.fulfillment.lms.v1.intf.LMSLocation ll2 = new com.stubhub.domain.fulfillment.lms.v1.intf.LMSLocation();
		ll2.setId(22222L);
		ll2.setAddress2("lms location2 address2  ");
		ll2.setAddress3("lms location2 address3");
		ll2.setState("CA");
		ll2.setCity("jose");
		lms1.add(ll2);

		LMSLocation.setLmsLocations(lms1);
		Mockito.when(response.getEntity()).thenAnswer(new Answer() {
			private int count = 0;

			public Object answer(InvocationOnMock invocation) {
				count ++;
				if (count == 1) {
					return LMSLocation;
				}
				return null;
			}
		});


		Mockito.when(response.getStatus()).thenReturn(200);
		Mockito.when(webClient.get()).thenReturn(response);


		helper.setLMSLocation4MyOrders(orders, "en=US");
		Assert.assertEquals(orders.get(0).getLmsLocation(), "lms location2 address2 lms location2 address3, jose, CA");
		Assert.assertEquals(orders.get(1).getLmsLocation(), "lms location2 address2 lms location2 address3, jose, CA");
	}
}
