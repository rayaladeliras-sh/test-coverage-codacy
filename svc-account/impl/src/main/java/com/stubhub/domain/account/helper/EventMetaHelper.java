package com.stubhub.domain.account.helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.stubhub.newplatform.http.util.HttpClient4Util;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.jaxrs.client.ResponseReader;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.protocol.HTTP;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.stubhub.domain.account.intf.TranslatableEventData;
import com.stubhub.domain.search.catalog.v3.intf.dto.response.event.Event;
import com.stubhub.domain.search.catalog.v3.intf.dto.response.event.Events;
import com.stubhub.newplatform.property.MasterStubhubPropertiesWrapper;
import com.stubhub.platform.utilities.webservice.svclocator.SvcLocator;

@Component("eventMetaHelper")
public class EventMetaHelper {
	@Autowired
	private SvcLocator svcLocator;
	
	@Autowired
	private MasterStubhubPropertiesWrapper properties;
	
	private static final Log log = LogFactory.getLog(EventMetaHelper.class);
	
    private static final ThreadLocal<List> providers = new ThreadLocal<List>() {
        
        @Override
        protected List initialValue() {
            JacksonJsonProvider jsonProvider = new JacksonJsonProvider();
            ResponseReader reader = new ResponseReader();
            reader.setEntityClass(Events.class);
            List providers = new ArrayList();
            jsonProvider.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            providers.add(reader);
            providers.add(jsonProvider);
            return providers;
        }
        
    };
    
    public void setEventDescByLocaleAndMOT(List<? extends TranslatableEventData> datas, String locale){
    	int limit = Integer.parseInt(properties.getProperty("mci.query.items.limit", "150"));
    	List<TranslatableEventData> tmp = new ArrayList<TranslatableEventData>();
    	for(TranslatableEventData data : datas){
    		tmp.add(data);
    		if(tmp.size() == limit){
    			setEventDescByLocaleAndMOTSingle(tmp, locale);
    			tmp.clear();
    		}
    	}
    	if(!tmp.isEmpty()){
    		setEventDescByLocaleAndMOTSingle(tmp, locale);
    	}
    }
    
	private void setEventDescByLocaleAndMOTSingle(List<? extends TranslatableEventData> datas, String locale){
		if(locale==null || StringUtils.isBlank(locale) || StringUtils.isEmpty(locale))
			locale = Locale.US.toString();
		 Map<Long, Set<TranslatableEventData>> eventIdDescMap = new HashMap<Long, Set<TranslatableEventData>>();
		 for(TranslatableEventData data : datas){
			 if(data.getEventId()==null)
				 continue;
			 Long eventId = Long.parseLong(data.getEventId().toString().trim());
			 Set<TranslatableEventData> set = eventIdDescMap.get(eventId);
			 if(set==null){
				set = new HashSet<TranslatableEventData>();
			 }
			 long key = Long.parseLong(data.getEventId().toString());		
			 eventIdDescMap.put(key, set);
		     set.add(data);
		 }
		 
		 if(eventIdDescMap.isEmpty())
			 return;
		 
		 String eventSearchAPIUrl = properties.getProperty("unified.search.catalog.event.api.url");
		 boolean appendOR = false;
		 
		 StringBuilder ids = new StringBuilder(); 
		 for(Long eventId : eventIdDescMap.keySet()){
			 if(appendOR)
				 ids.append(" |");
			 ids.append(eventId.toString());
			 appendOR = true;			
		 }		 
		 log.info("eventId search String=" + ids.toString());



        WebClient webClient = svcLocator.locate(eventSearchAPIUrl.toString(), providers.get());
		 webClient.query("id", ids.toString());
		 webClient.query("locale",locale.replace('-', '_'));
		 webClient.accept(MediaType.APPLICATION_JSON);
		 webClient.acceptLanguage(locale);
		 String token = properties.getProperty("newapi.accessToken");
		 webClient.header("Authorization", "Bearer " + token);		
		 
		 try{
			 Response response = webClient.get();
			 if(Response.Status.OK.getStatusCode() == response.getStatus()) {		    	
				Events events = (Events)response.getEntity();
				log.info("number of Events found=" + events.getNumFound());
				handleEventResponse(eventIdDescMap, events);
			 }else{
				log.error(" getEvent v3 call retrun status not 200, status=" + response.getStatus());
			 }
		  }catch (Exception e) {
			 log.error("exception while call getEvent API", e);
		  }
	}

	private void handleEventResponse(Map<Long, Set<TranslatableEventData>> eventIdDescMap, Events events){		
		for(Event event:events.getEvents()){
			Set<TranslatableEventData> datas = eventIdDescMap.get(new Long(event.getId().intValue()));
			if(datas != null){
				for(TranslatableEventData data:datas){
					data.setEventDescription(event.getName());
					int stubhubMobileTicket = 0;
					if ( event.getMobileAttributes() != null &&
							event.getMobileAttributes().getStubhubMobileTicket() != null ) {
						log.debug("Mobile Attribute=" + event.getMobileAttributes().getStubhubMobileTicket());
						stubhubMobileTicket = event.getMobileAttributes().getStubhubMobileTicket() == true ? 1 : 0;
					}
					data.setStubhubMobileTicket(stubhubMobileTicket);
					if(null!=event.getVenue()){
						data.setVenueConfigId(event.getVenue().getVenueConfigId());
					}
				}
			}
		}
	}

	public com.stubhub.domain.catalog.read.v3.intf.events.dto.response.Event getEventDetail(Long eventId)  {
		StringBuilder eventSearchAPIUrl = new StringBuilder(properties.getProperty("catalog.event.api.url"));
		eventSearchAPIUrl.append("/").append(eventId);
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Accept", MediaType.APPLICATION_JSON);
		String token = properties.getProperty("newapi.accessToken");
		headers.put("Authorization", "Bearer " + token);
		headers.put("X-SH-Service-Context", "{ role=R2}");
		try {
			HttpClient4Util.SimpleHttpResponse shr = HttpClient4Util.getFromUrl(eventSearchAPIUrl.toString(), headers, null);
			if (null != shr) {
				return (com.stubhub.domain.catalog.read.v3.intf.events.dto.response.Event)
						getResponseObject(shr.getContent(),
								new com.stubhub.domain.catalog.read.v3.intf.events.dto.response.Event(), false);
			} else {
				return null;
			}
		}catch (Exception e) {
			log.error("getEventDetailException", e);
		}
		return null;
	}
	public Object getResponseObject(String responseJson, Object responseObject, boolean unwrapRootValue) {
		ObjectMapper mapper = new ObjectMapper();
		Object response;
		try {
			mapper.configure(DeserializationConfig.Feature.UNWRAP_ROOT_VALUE, unwrapRootValue);
			mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			response = mapper.readValue(responseJson, responseObject.getClass());
			return response;
		} catch (IOException e) {
			log.error("getResponseObjectIOException", e);
		}
		return null;
	}
}
