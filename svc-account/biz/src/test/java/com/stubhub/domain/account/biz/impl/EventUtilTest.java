package com.stubhub.domain.account.biz.impl;

import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;
import org.mockito.Mockito;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.stubhub.domain.catalog.events.intf.DeliveryOption;
import com.stubhub.domain.catalog.events.intf.EventMetaResponse;
import com.stubhub.domain.catalog.events.intf.FulfillmentMethod;
import com.stubhub.domain.catalog.events.intf.TicketTrait;
import com.stubhub.domain.catalog.events.intf.Venue;
import com.stubhub.domain.catalog.read.v3.intf.events.dto.response.Event;
import com.stubhub.domain.catalog.read.v3.intf.events.dto.response.Events;
import com.stubhub.domain.catalog.read.v3.intf.events.dto.response.SeatTrait;
import com.stubhub.domain.inventory.metadata.v1.event.DTO.EventMetadataResponse;
import com.stubhub.newplatform.property.MasterStubhubPropertiesWrapper;
import com.stubhub.platform.utilities.webservice.svclocator.SvcLocator;

public class EventUtilTest {
    
    private EventUtil eventUtil;
    private SvcLocator svcLocator;
    private WebClient webClient;
    private MasterStubhubPropertiesWrapper wrapper;
    private CacheManager cacheManager;
    private Cache cache;
    
    @BeforeMethod
    public void setUp() {
        eventUtil = new EventUtil() {
            @Override
            protected String getProperty(String propertyName, String defaultValue) {
                if ("catalog.api.url"
                        .equals(propertyName)) { return "https://api.srwd34.com/catalog/events/v1/{eventId}/metadata"; }
                return "";
            }
        };
        svcLocator = Mockito.mock(SvcLocator.class);
        webClient = Mockito.mock(WebClient.class);
        cacheManager = Mockito.mock(CacheManager.class);
        cache = Mockito.mock(Cache.class);
        Mockito.when(cacheManager.getCache(Mockito.anyString())).thenReturn(cache);
        wrapper = Mockito.mock(MasterStubhubPropertiesWrapper.class);
        ReflectionTestUtils.setField(eventUtil, "svcLocator", svcLocator);
        ReflectionTestUtils.setField(eventUtil, "properties", wrapper);
        ReflectionTestUtils.setField(eventUtil, "cacheManager", cacheManager);
    }
    
    @Test
    public void testGetEventById() throws Exception {
        Mockito.when(svcLocator.locate(Mockito.anyString(), Mockito.anyList())).thenReturn(webClient);
        Mockito.when(webClient.get()).thenReturn(getResponse());
        
        EventMetaResponse event = eventUtil.getEventDetails(12345L, "event");
        Assert.assertNotNull(event);
    }
    
    @Test
    public void testGetEventV2ById() throws Exception {
        Mockito.when(svcLocator.locate(Mockito.anyString(), Mockito.anyList())).thenReturn(webClient);
        
        Mockito.when(webClient.get()).thenReturn(getEventDetailV2Response());
        
        EventMetadataResponse event = eventUtil.getEventDetailsV2(12345L);
        Assert.assertNotNull(event);
    }
    
    @Test(expectedExceptions = Exception.class)
    public void testGetEventV2ByIdWith400() throws Exception {
        Mockito.when(svcLocator.locate(Mockito.anyString(), Mockito.anyList())).thenReturn(webClient);
        
        Mockito.when(webClient.get()).thenReturn(getEventDetailV2ResponseWithBadStatus(400));
        
        eventUtil.getEventDetailsV2(12345L);
        
    }
    
    @Test(expectedExceptions = Exception.class)
    public void testGetEventV2ByIdWith404() throws Exception {
        Mockito.when(svcLocator.locate(Mockito.anyString(), Mockito.anyList())).thenReturn(webClient);
        
        Mockito.when(webClient.get()).thenReturn(getEventDetailV2ResponseWithBadStatus(404));
        
        eventUtil.getEventDetailsV2(12345L);
        
    }
    
    @Test(expectedExceptions = Exception.class)
    public void testGetEventV2ByIdWith500() throws Exception {
        Mockito.when(svcLocator.locate(Mockito.anyString(), Mockito.anyList())).thenReturn(webClient);
        
        Mockito.when(webClient.get()).thenReturn(getEventDetailV2ResponseWithBadStatus(500));
        
        eventUtil.getEventDetailsV2(12345L);
        
    }
    
    private Response getEventDetailV2Response() {
        Response response = new Response() {
            @Override
            public Object getEntity() {
                EventMetadataResponse eventMetadataResponse = new EventMetadataResponse();
                return eventMetadataResponse;
            }
            
            @Override
            public int getStatus() {
                return 200;
            }
            
            @Override
            public MultivaluedMap<String, Object> getMetadata() {
                return null;
            }
        };
        return response;
    }
    
    private Response getEventDetailV2ResponseWithBadStatus(final int staus) {
        Response response = new Response() {
            @Override
            public Object getEntity() {
                EventMetadataResponse eventMetadataResponse = new EventMetadataResponse();
                return eventMetadataResponse;
            }
            
            @Override
            public int getStatus() {
                return staus;
            }
            
            @Override
            public MultivaluedMap<String, Object> getMetadata() {
                return null;
            }
        };
        return response;
    }
    
    private Response getResponse() {
        Response response = new Response() {
            
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
                EventMetaResponse eventMetaResponse = new EventMetaResponse();
                eventMetaResponse.setEventId("12345");
                eventMetaResponse.setDescription("Test Event");
                eventMetaResponse.setBobId(1L);
                eventMetaResponse.setCurrency("USD");
                eventMetaResponse.setTimeZone("PST");
                eventMetaResponse.setCreditToTeamAccountSupported(1L);
                Venue venue = new Venue();
                venue.setVenueConfigId(1234L);
                venue.setGeoDescription("All Events");
                venue.setGaIndicator(1L);
                eventMetaResponse.setVenue(venue);
                eventMetaResponse.setGenrePath("986/123456");
                eventMetaResponse.setGeoPath("235/45678");
                List<FulfillmentMethod> fulfillmentMethods = new ArrayList<FulfillmentMethod>();
                FulfillmentMethod fulfillmentMethod = new FulfillmentMethod();
                fulfillmentMethod.setName(DeliveryOption.LMS);
                fulfillmentMethods.add(fulfillmentMethod);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                fulfillmentMethod.setEndDate(sdf.format(Calendar.getInstance().getTime()));
                eventMetaResponse.setFulfillmentMethods(fulfillmentMethods);
                List<TicketTrait> eventTicketTrait = new ArrayList<TicketTrait>();
                TicketTrait ticketTrait = new TicketTrait();
                ticketTrait.setId(1234l);
                eventTicketTrait.add(ticketTrait);
                eventMetaResponse.setTicketTraits(eventTicketTrait);
                try {
                    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                    eventMetaResponse.setEventDate(sf.format(Calendar.getInstance().getTime()));
                    Calendar eih = Calendar.getInstance();
                    eih.add(Calendar.DAY_OF_MONTH, -3);
                    eventMetaResponse.setEarliestPossibleInhandDate(sf.format(eih.getTime()));
                    Calendar lih = Calendar.getInstance();
                    lih.add(Calendar.DAY_OF_MONTH, 3);
                    eventMetaResponse.setLatestPossibleInhandDate(sf.format(lih.getTime()));
                }
                catch (Exception e) {
                
                }
                return eventMetaResponse;
            }
        };
        return response;
    }
    
    @Test
    public void testGetEventNames() {
        Mockito.when(svcLocator.locate(Mockito.anyString())).thenReturn(webClient);
        Response response = Mockito.mock(Response.class);
        Mockito.when(webClient.get()).thenReturn(response);
        Mockito.when(response.getStatus()).thenReturn(Response.Status.OK.getStatusCode());
        String json = "{\"events\": [ {\"id\": 12345, \"name\": \"event 12345\"}, {\"id\": 123456, \"name\": \"event 123456\"}]}";
        InputStream entity = new ByteArrayInputStream(json.getBytes());
        Mockito.when(response.getEntity()).thenReturn(entity);
        
        Set<Long> ids = new HashSet<Long>();
        ids.add(12345L);
        ids.add(123456L);
        
        Map<Long, String> names = eventUtil.getEventNames(ids);
        
        Assert.assertNotNull(names);
        Assert.assertEquals(names.size(), 2);
        Assert.assertEquals(names.get(12345L), "event 12345");
        Assert.assertEquals(names.get(123456L), "event 123456");
    }
    
    @Test
    public void testGetEventNames_Error() {
        Mockito.when(svcLocator.locate(Mockito.anyString())).thenReturn(webClient);
        Response response = Mockito.mock(Response.class);
        Mockito.when(webClient.get()).thenReturn(response);
        Mockito.when(response.getStatus()).thenReturn(Response.Status.BAD_REQUEST.getStatusCode());
        
        Set<Long> ids = new HashSet<Long>();
        ids.add(12345L);
        ids.add(123456L);
        
        Map<Long, String> names = eventUtil.getEventNames(ids);
        
        Assert.assertNotNull(names);
        Assert.assertTrue(names.isEmpty());
        Mockito.verify(response, Mockito.never()).getEntity();
        
        Mockito.when(response.getStatus()).thenReturn(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        Assert.assertNotNull(names);
        Assert.assertTrue(names.isEmpty());
        Mockito.verify(response, Mockito.never()).getEntity();
    }
    
    @Test
    public void testGetLocalizedSeatTraitName() {
        final Event eventV3 = new Event();
        List<SeatTrait> seatTraits = new ArrayList<SeatTrait>();
        {
            SeatTrait st = new SeatTrait();
            st.setId(1L);
            st.setName("A");
            seatTraits.add(st);
        }
        {
            SeatTrait st = new SeatTrait();
            st.setId(2L);
            st.setName("B");
            seatTraits.add(st);
        }
        eventV3.setSeatTraits(seatTraits);
        eventV3.setExpiredInd(false);
        eventV3.setLocale("en_US");
        
        Response response = new Response() {
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
                return eventV3;
            }
        };
        
        Response badResponse = new Response() {
            @Override
            public int getStatus() {
                return 404;
            }
            
            @Override
            public MultivaluedMap<String, Object> getMetadata() {
                return null;
            }
            
            @Override
            public Object getEntity() {
                return null;
            }
        };
        
        Mockito.when(svcLocator.locate(Mockito.anyString(), Mockito.anyList())).thenReturn(webClient);
        Mockito.when(webClient.get()).thenReturn(response);
        
        Assert.assertEquals(eventUtil.getLocalizedSeatTraitName(1L, "en-us", "123"), "A");
        Assert.assertEquals(eventUtil.getLocalizedSeatTraitName(2L, null, "123"), "B");
        Assert.assertEquals(eventUtil.getLocalizedSeatTraitName(1L, "en-us", "123"), "A");
        
        Assert.assertEquals(eventUtil.getLocalizedSeatTraitName(1L, "en-gb", "123"), "A");
        Assert.assertEquals(eventUtil.getLocalizedSeatTraitName(3L, "en-gb", "123"), null);
        
        eventV3.setSeatTraits(null);
        Assert.assertEquals(eventUtil.getLocalizedSeatTraitName(1L, "en-gb", "123"), "A");
        
        Mockito.when(webClient.get()).thenReturn(badResponse);
        
        Assert.assertEquals(eventUtil.getLocalizedSeatTraitName(1L, "de-de", "123"), null);
    }
    
    @Test
    public void coverage_get_events() throws Exception {
        final Events eventsV3 = new Events();
        eventsV3.setEvents(Arrays.<Event>asList(new Event()));
        eventsV3.getEvents().iterator().next().setId(1l);
        Response response = new Response() {
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
                return eventsV3;
            }
        };
        
        Response badResponse = new Response() {
            @Override
            public int getStatus() {
                return 404;
            }
            
            @Override
            public MultivaluedMap<String, Object> getMetadata() {
                return null;
            }
            
            @Override
            public Object getEntity() {
                return null;
            }
        };
        Mockito.when(svcLocator.locate(Mockito.anyString(), Mockito.anyList())).thenReturn(webClient);
        Mockito.when(webClient.get()).thenReturn(response);
        
        eventUtil.getEventsV3(Arrays.asList("1", "2"), "en_US");
        
        Mockito.when(svcLocator.locate(Mockito.anyString(), Mockito.anyList())).thenReturn(webClient);
        Mockito.when(webClient.get()).thenReturn(badResponse);
        try {
            eventUtil.getEventsV3(Arrays.asList("1", "2"), "en_US");
            fail();
        }
        catch (Exception e) {
            
        }
        
        ValueWrapper i = new ValueWrapper() {
            
            @Override
            public Object get() {
                return eventsV3.getEvents().iterator().next();
            }
            
        };
        Mockito.when(cache.get(Mockito.anyObject())).thenReturn(i);
        
        eventUtil.getEventsV3(Arrays.asList("1", "2"), "en_US");
    }
}
