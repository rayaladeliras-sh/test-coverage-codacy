package com.stubhub.domain.account.helper;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import junit.framework.Assert;

import org.apache.cxf.jaxrs.client.ResponseReader;
import org.apache.cxf.jaxrs.client.WebClient;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.annotations.Test;

import com.stubhub.domain.account.intf.MyOrderResponse;
import com.stubhub.domain.search.catalog.v3.intf.dto.response.event.Event;
import com.stubhub.domain.search.catalog.v3.intf.dto.response.event.Events;
import com.stubhub.newplatform.property.MasterStubhubPropertiesWrapper;
import com.stubhub.platform.utilities.webservice.svclocator.SvcLocator;

public class EventMetaHelperTest {
	@Test 
	public void testsetEventDescByLocaleAndMOT(){
		EventMetaHelper helper = new EventMetaHelper();
		SvcLocator svcLocator = Mockito.mock(SvcLocator.class);
		MasterStubhubPropertiesWrapper properties = Mockito.mock(MasterStubhubPropertiesWrapper.class);
		ReflectionTestUtils.setField(helper, "properties", properties);
		ReflectionTestUtils.setField(helper, "svcLocator", svcLocator);
		
		Mockito.when(properties.getProperty("unified.search.catalog.event.api.url")).thenReturn("http://www.test.com");
		Mockito.when(properties.getProperty(Mockito.eq("mci.query.items.limit"), Mockito.anyString())).thenReturn("2");
		
		List<MyOrderResponse> orders = new ArrayList<MyOrderResponse>();
		helper.setEventDescByLocaleAndMOT(orders, null);
		helper.setEventDescByLocaleAndMOT(orders, null);
		Assert.assertEquals(orders.size(), 0);
		MyOrderResponse resp = new MyOrderResponse();
		resp.setEventId(1234L);
		orders.add(resp);
		
		MyOrderResponse resp2 = new MyOrderResponse();
		resp2.setEventId(4321L);
		orders.add(resp2);
		
		WebClient webClient = Mockito.mock(WebClient.class);
		Mockito.when(svcLocator.locate(Mockito.anyString(), Mockito.anyListOf(ResponseReader.class))).thenReturn(webClient);
		Response response = Mockito.mock(Response.class);
		Events events = new Events();		
		List<Event> list = new ArrayList<Event>();
		events.setEvents(list);
		Event e1 = new Event();
		e1.setId(1234);
		e1.setName("event 1");
		e1.setVenueConfigurationId("1234");
		list.add(e1);
		Event e2 = new Event();
		e2.setId(4321);
		e2.setName("event 2");
		list.add(e2);
		
		Mockito.when(response.getEntity()).thenReturn(events);
		Mockito.when(response.getStatus()).thenReturn(200);
		Mockito.when(webClient.get()).thenReturn(response);	
		
		helper.setEventDescByLocaleAndMOT(orders, "en-GB");
		Assert.assertEquals("event 1", orders.get(0).getEventDescription());
		Assert.assertEquals("event 2", orders.get(1).getEventDescription());

		Mockito.when(properties.getProperty(Mockito.eq("mci.query.items.limit"), Mockito.anyString())).thenReturn("3");
		helper.setEventDescByLocaleAndMOT(orders, "en-GB");
		Assert.assertEquals("event 1", orders.get(0).getEventDescription());
		Assert.assertEquals("event 2", orders.get(1).getEventDescription());
	}
	
	@Test 
	public void testsetEventDescByLocaleAndMOTNot200(){
		EventMetaHelper helper = new EventMetaHelper();
		SvcLocator svcLocator = Mockito.mock(SvcLocator.class);
		MasterStubhubPropertiesWrapper properties = Mockito.mock(MasterStubhubPropertiesWrapper.class);
		ReflectionTestUtils.setField(helper, "properties", properties);
		ReflectionTestUtils.setField(helper, "svcLocator", svcLocator);
		
		Mockito.when(properties.getProperty("unified.search.catalog.event.api.url")).thenReturn("http://www.test.com");
		Mockito.when(properties.getProperty(Mockito.eq("mci.query.items.limit"), Mockito.anyString())).thenReturn("2");
		
		List<MyOrderResponse> orders = new ArrayList<MyOrderResponse>();
		helper.setEventDescByLocaleAndMOT(orders, null);
		helper.setEventDescByLocaleAndMOT(orders, null);
		Assert.assertEquals(orders.size(), 0);
		MyOrderResponse resp = new MyOrderResponse();
		resp.setEventId(1234L);
		orders.add(resp);
		
		MyOrderResponse resp2 = new MyOrderResponse();
		resp2.setEventId(4321L);
		orders.add(resp2);
		
		WebClient webClient = Mockito.mock(WebClient.class);
		Mockito.when(svcLocator.locate(Mockito.anyString(), Mockito.anyListOf(ResponseReader.class))).thenReturn(webClient);
		Response response = Mockito.mock(Response.class);
		Events events = new Events();		
		List<Event> list = new ArrayList<Event>();
		events.setEvents(list);
		Event e1 = new Event();
		e1.setId(1234);
		e1.setName("event 1");
		list.add(e1);
		Event e2 = new Event();
		e2.setId(4321);
		e2.setName("event 2");
		list.add(e2);
		
		Mockito.when(response.getEntity()).thenReturn(events);
		Mockito.when(response.getStatus()).thenReturn(404);
		Mockito.when(webClient.get()).thenReturn(response);	
		
		helper.setEventDescByLocaleAndMOT(orders, "en-GB");
		Assert.assertEquals(null, orders.get(0).getEventDescription());
		Assert.assertEquals(null, orders.get(1).getEventDescription());
	}
}
