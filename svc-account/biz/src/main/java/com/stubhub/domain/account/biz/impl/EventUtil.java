package com.stubhub.domain.account.biz.impl;
 
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.apache.cxf.jaxrs.client.ResponseReader;
import org.apache.cxf.jaxrs.client.WebClient;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.codehaus.jackson.map.DeserializationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import com.stubhub.domain.catalog.events.intf.EventMetaResponse;
import com.stubhub.domain.catalog.read.v3.intf.events.dto.response.Event;
import com.stubhub.domain.catalog.read.v3.intf.events.dto.response.Events;
import com.stubhub.domain.catalog.read.v3.intf.events.dto.response.SeatTrait;
import com.stubhub.domain.infrastructure.common.exception.base.SHRuntimeException;
import com.stubhub.domain.infrastructure.common.exception.base.SHSystemException;
import com.stubhub.domain.inventory.metadata.v1.event.DTO.EventMetadataResponse;
import com.stubhub.newplatform.property.MasterStubHubProperties;
import com.stubhub.newplatform.property.MasterStubhubPropertiesWrapper;
import com.stubhub.platform.utilities.webservice.svclocator.SvcLocator;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

@Component("eventUtil")
public class EventUtil {
	
private final static Logger log = LoggerFactory.getLogger(EventUtil.class);
	

	@Autowired
	private SvcLocator svcLocator;
	
    @Autowired
    private MasterStubhubPropertiesWrapper properties;
    
    @Autowired
    private CacheManager cacheManager;
    
    private List<ResponseReader> eventMetaReader;
    private List<ResponseReader> eventMetadataReader;
    {
        ResponseReader reader = new ResponseReader();
        reader.setEntityClass(EventMetaResponse.class);
        eventMetaReader = new ArrayList<ResponseReader>();
        eventMetaReader.add(reader);
        
        reader = new ResponseReader();
        reader.setEntityClass(EventMetadataResponse.class);
        eventMetadataReader = new ArrayList<ResponseReader>();
        eventMetadataReader.add(reader);
    }
	public EventMetaResponse getEventDetails(long eventId, String filters) throws Exception {
		String catalogURI = getProperty("catalog.api.url", "http://api-int.stubprod.com/catalog/events/v1/{eventId}/metadata");
		catalogURI = catalogURI.replace("{eventId}", Long.toString(eventId));
		if(filters != null){
			catalogURI = catalogURI + "?fl="+ URLEncoder.encode(filters, "UTF-8");
		}
        
        WebClient webClient = svcLocator.locate(catalogURI, eventMetaReader);
	    webClient.accept(MediaType.APPLICATION_JSON);
	    Response response = webClient.get();
	    if(Response.Status.OK.getStatusCode() == response.getStatus()) {
	    	log.info("message=\"getEventMeta api call successful\"" +"eventId=" + eventId + "filters=" + filters);
	    	return (EventMetaResponse) response.getEntity();
		} else if(Response.Status.BAD_REQUEST.getStatusCode() == response.getStatus()){
			log.error("message=\"Invalid eventId passed\"" +"eventId=" + eventId);
		} else {
			log.error("message=\"System error occured while calling getEventMeta api\""+"eventId=" + eventId+ " responseCode="+response.getStatus());
			throw new Exception("System error occured while calling getEventMeta api  eventId=" + eventId);
		}
	    return null;
	}

	//https://wiki.stubcorp.com/display/api/Get+Event+Metadata+v1
	public EventMetadataResponse getEventDetailsV2(long eventId) throws Exception{
		String eventMetadataURL = getProperty("event.metadata.api.url","http://api-int.stubprod.com/inventory/metadata/v1?eventId={eventId}");
		eventMetadataURL = eventMetadataURL.replace("{eventId}",Long.toString(eventId));
		
        WebClient webClient = svcLocator.locate(eventMetadataURL, eventMetadataReader);
		webClient.accept(MediaType.APPLICATION_XML);
		Response  response = webClient.get();
		if(Response.Status.OK.getStatusCode() == response.getStatus()){
			log.info("message=\"getEventMetadata api call successfully\"" + "eventId=" + eventId);
			return (EventMetadataResponse) response.getEntity();
		}else if(Response.Status.BAD_REQUEST.getStatusCode() == response.getStatus()){
			log.error("message=\"Event is not active or event is in the past\"" +"eventId=" + eventId);
			throw new Exception("Event is not active or event is in the past\\ eventId=" + eventId);
		}else if (Response.Status.NOT_FOUND.getStatusCode() == response.getStatus()){
			log.error("message=\"No event metadata found for given event id\"" +"eventId=" + eventId);
			throw new Exception("No event metadata found   eventId=" + eventId);
		}else {
			log.error("message=\"System error occured while calling getEventMetadata api\""+"eventId=" + eventId+ " responseCode="+response.getStatus());
			throw new Exception("System error occured while calling getEventMetadata api  eventId=" + eventId);
		}


	}
	protected String getProperty(String propertyName, String defaultValue) {
		return MasterStubHubProperties.getProperty(propertyName, defaultValue);
	}
	
	public Map<Long, String> getEventNames(Set<Long> ids) {
		String endpoint = getProperty("getEvents.v3.api.url", "http://api-int.stubprod.com/catalog-read/v3/events");
		String url = endpoint + "?" + "ids=" + convertToString(ids);
        log.debug("message=\"Build getEvents API URL\" url=" + url);
        WebClient webClient = svcLocator.locate(url);
        webClient.accept(MediaType.APPLICATION_JSON);

        Response response = webClient.get();
        try{
	        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
	            log.info("message=\"getEvents api call successful\"" + "eventIds=" + ids);
				JSONArray events = (JSONArray) ((JSONObject) JSONValue.parse(new InputStreamReader((InputStream) response.getEntity(), "utf-8"))).get("events");
	            Map<Long, String> result = new HashMap<Long, String>();
	            for (Object obj : events) {
	                JSONObject event = (JSONObject) obj;
					result.put(((Integer) event.get("id")).longValue(), (String) event.get("name"));
	            }
	            return result;
	        } else if (Response.Status.BAD_REQUEST.getStatusCode() == response.getStatus()) {
	            log.error("message=\"Invalid eventIds passed\"" + "eventIds=" + ids);
	        } else {
	            log.error("message=\"System error occured while calling getEvents api\"" + "eventIds=" + ids
	                    + " responseCode=" + response.getStatus());
	        }
	      
        }catch(UnsupportedEncodingException e){
        	 log.error("message=\"Encoding error while parsing event name\"" + "eventIds=" + ids);
        }
        return Collections.emptyMap();
    }

    private String convertToString(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (Long id : ids) {
            builder.append(id + ",");
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }

	//The cache for localized seat trait <Locale, <EventID_SeatTraitID, SeatTraitName>>
	private final Map<String, Map<Long, String>> localizedSeatTraitNameLocaleMap = new ConcurrentHashMap<String, Map<Long, String>>();

	//<EventID_Locale, InvokeTimeMillis>
	private final Map<String, Long> invokeEventV3TrackMap = new ConcurrentHashMap<String, Long>();

	private final long trackMapMaxSize = 100000;

	/**
	 *
	 * @param traitId
	 * @param locale
	 * @param eventId can be null, if you don't want load seat traits from EventV3
	 * @return
	 */
	public String getLocalizedSeatTraitName(Long traitId, String locale, String eventId) {

		if (log.isDebugEnabled()) {
			log.debug("getLocalizedSeatTraitName traitId=" + traitId + " locale=" + locale + " eventId=" + eventId);
		}

		if (locale == null) {
			String localeUS = "en-us";
			locale = localeUS;
		}

		Map<Long, String> localizedSeatTraitNameIdMap = localizedSeatTraitNameLocaleMap.get(locale);

		if (localizedSeatTraitNameIdMap == null) {
			localizedSeatTraitNameIdMap = new ConcurrentHashMap<Long, String>();
			localizedSeatTraitNameLocaleMap.put(locale, localizedSeatTraitNameIdMap);
		}

		String name = localizedSeatTraitNameIdMap.get(traitId);

		if (name == null) {
			if (eventId != null) {
				try {
					String key = eventId + locale;
					Long lastInvokeTimeMillis = invokeEventV3TrackMap.get(key);

					// We should prevent access same event again and again in a short period
					boolean needAccess = lastInvokeTimeMillis == null ? true: (System.currentTimeMillis() > lastInvokeTimeMillis + 20 * 1000 * 60);

					if (needAccess) {
						Event eventV3 = getEventV3(eventId, locale);

						if (invokeEventV3TrackMap.size() > trackMapMaxSize) {
							// prevent non-stop increasing
							invokeEventV3TrackMap.clear();
						}

						invokeEventV3TrackMap.put(key, System.currentTimeMillis());

						// put into cache only
						if (eventV3 != null && eventV3.getSeatTraits() != null) {
							for (SeatTrait trait : eventV3.getSeatTraits()) {
								localizedSeatTraitNameIdMap.put(trait.getId(), trait.getName());
							}
							log.info("batch load localized seat trait from eventId=" + eventId + " locale=" + locale +
									" size=" + eventV3.getSeatTraits().size() + " totalSizeNow=" + localizedSeatTraitNameIdMap.size());
							if (log.isDebugEnabled()) {
								log.debug("load SeatTraits=" + eventV3.getSeatTraits());
							}
							name = localizedSeatTraitNameIdMap.get(traitId);
						} else {
							log.warn("cannot load localized seat trait from eventId=" + eventId + " locale=" + locale + " due to " +
									(eventV3 == null ? " event=null " : (" eventLocale=" + eventV3.getLocale() + " seatTraitsSize=" + eventV3.getSeatTraits().size())));
						}
					} else {
						log.debug("No need access same getEventV3 API again in a short period! lastInvokeTimeMillis=" + lastInvokeTimeMillis);
					}
				} catch (Exception e) {
					log.warn("getEventV3", e);
				}
			}

			if (name == null) {
				log.warn("failed to load localized seat trait value for id=" + traitId);
			} else {
				if (log.isDebugEnabled()) {
					log.debug("load localized seat trait value=" + name + " for id=" + traitId + " locale=" + locale);
				}
			}
		}

		return name;
	}

	private static final String CATALOG_API_URL = "catalog.get.event.v3.api.url";
	private static final String CATALOG_API_URL_DEFAULT = "http://api.stubhub.com/catalog-read/v3/events/{eventId}?mode=internal";

    private static final String CATALOG_API_EVENTS_URL = "catalog.get.events.v3.api.url";
    
    private static final String CATALOG_API_URL_EVENTS_DEFAULT = "https://api.stubhub.com/catalog/events/v3?ids={eventIds}&mode=internal&isSeatTraitsRequired=true";
    
    private static final ThreadLocal<List> EVENT_PROVIDER = new ThreadLocal<List>() {
        
        @Override
        protected List initialValue() {
            JacksonJsonProvider jsonProvider = new JacksonJsonProvider();
            ResponseReader reader = new ResponseReader();
            reader.setEntityClass(Event.class);
            List providers = new ArrayList();
            jsonProvider.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            providers.add(reader);
            providers.add(jsonProvider);
            return providers;
        }
        
    };
    
    private static final ThreadLocal<List> EVENTS_PROVIDER = new ThreadLocal<List>() {
        
        @Override
        protected List initialValue() {
            ResponseReader reader = new ResponseReader();
            reader.setEntityClass(Events.class);
            List providers = new ArrayList();
            JacksonJsonProvider jsonProvider = new JacksonJsonProvider();
            jsonProvider.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            providers.add(reader);
            providers.add(jsonProvider);
            return providers;
        }
        
    };
    
	//primary method to invoke catalog Get Event By Id - v3 api
    @Cacheable("getEventV3")
	public Event getEventV3(String eventId, String locale) {

		log.info("getEventV3 eventId=" + eventId + " locale=" + locale);

		StopWatch sw = new StopWatch();
		sw.start();
		//api response objects
		try {
			String catalogEventURL = getProperty(CATALOG_API_URL, CATALOG_API_URL_DEFAULT);

			catalogEventURL = catalogEventURL.replace("{eventId}", eventId);

			log.info("catalogEventURL=" + catalogEventURL);


			//prepare webclient for api call
            WebClient webClient = svcLocator.locate(catalogEventURL, EVENT_PROVIDER.get());
			if (locale != null) {
				webClient.header(HttpHeaders.ACCEPT_LANGUAGE, locale);
			}
			webClient.header("source", "seller");

			//set response accept type and invoke api
            webClient.accept(MediaType.APPLICATION_JSON);
			Response response = webClient.get();

			log.info("getEventV3 response=" + response.getStatus());

			//if response status is successful, continue processing
			if (Response.Status.OK.getStatusCode() == response.getStatus()) {
				return (Event) response.getEntity();
			} else {
				throw new SHSystemException(); //500
			}

		} catch (Exception ex) {
			throw new SHRuntimeException(); //500
		} finally {
			sw.stop();
			log.info("message=\"After querying catalog v3 api for event\" eventId="+eventId+" locale="+locale+" costTimeInMS="+sw.getTotalTimeMillis());
		}
	}
    
    public Map<Long, Event> getEventsV3(Collection<String> eventId, String locale) {
        log.info("getEventsV3 eventIds=" + StringUtils.join(eventId, ",") + " locale=" + locale);
        
        StopWatch sw = new StopWatch();
        sw.start();
        // api response objects
        try {
            Set<String> toQuery = new HashSet<String>();
            Map<Long, Event> result = new HashMap<Long, Event>();
            for (Iterator<String> iter = eventId.iterator(); iter.hasNext();) {
                String next = iter.next();
                ValueWrapper event = cacheManager.getCache("getEventV3").get(next.trim());
                if (event != null && event.get() != null) {
                    result.put(((Event) event.get()).getId(), ((Event) event.get()));
                } else {
                    toQuery.add(next);
                }
            }
            
            log.info(
                    "getEventV3 api query counter=" + toQuery.size() + " eventsId=" + StringUtils.join(toQuery, ","));
            if (toQuery.isEmpty())
                return result;
            String catalogEventURL = getProperty(CATALOG_API_EVENTS_URL, CATALOG_API_URL_EVENTS_DEFAULT);
            
            catalogEventURL = catalogEventURL.replace("{eventIds}", StringUtils.join(toQuery, ","));
            
            log.info("catalogEventURL=" + catalogEventURL);
            

            // prepare webclient for api call
            WebClient webClient = svcLocator.locate(catalogEventURL, EVENTS_PROVIDER.get());
            if (locale != null) {
                webClient.header(HttpHeaders.ACCEPT_LANGUAGE, locale);
            }
			webClient.header("source", "seller");
            
            // set response accept type and invoke api
            webClient.accept(MediaType.APPLICATION_JSON);
            String token = properties.getProperty("newapi.accessToken");
            webClient.header("Authorization", "Bearer " + token);
            Response response = webClient.get();
            
            log.info("getEventsV3 response=" + response.getStatus());
            
            // if response status is successful, continue processing
            if (Response.Status.OK.getStatusCode() == response.getStatus()) {
                
                for (Event e : ((Events) response.getEntity()).getEvents()) {
                    result.put(e.getId(), e);
                    cacheManager.getCache("getEventV3").put(e.getId().toString(), e);
                }
                return result;
            } else {
                throw new SHSystemException(); // 500
            }
            
        }
        catch (Exception ex) {
            throw new SHRuntimeException(); // 500
        } finally {
            sw.stop();
            log.info("message=\"After querying catalog v3 api for event\" eventId=" + eventId + " locale=" + locale
                    + " costTimeInMS=" + sw.getTotalTimeMillis());
        }
    }
}
