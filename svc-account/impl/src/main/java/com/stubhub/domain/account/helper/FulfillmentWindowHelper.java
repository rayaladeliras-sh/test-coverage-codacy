package com.stubhub.domain.account.helper;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.jaxrs.client.ResponseReader;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.stubhub.domain.fulfillment.window.v1.intf.ListingFulfillmentWindowResponse;
import com.stubhub.newplatform.property.MasterStubhubPropertiesWrapper;
import com.stubhub.platform.utilities.webservice.svclocator.SvcLocator;

@Component
public class FulfillmentWindowHelper {
	@Autowired
	private SvcLocator svcLocator;
	
	@Autowired
	private MasterStubhubPropertiesWrapper properties;
	
	private static final Log log = LogFactory.getLog(FulfillmentWindowHelper.class);
	
    private static final ThreadLocal<List> responseReader = new ThreadLocal<List>() {
        
        @Override
        protected List initialValue() {
            ResponseReader reader = new ResponseReader();
            reader.setEntityClass(ListingFulfillmentWindowResponse.class);
            List<ResponseReader> responseReader = new ArrayList<ResponseReader>();
            responseReader.add(reader);
            return responseReader;
        }
        
    };
    
	public ListingFulfillmentWindowResponse getFulfillmentWindowByListingId(Long listingId){
		StringBuilder fulfillmentWindowAPIUrl = new StringBuilder(properties.getProperty("fulfillment.listingWindow.api.url"));
		fulfillmentWindowAPIUrl.append("/").append(listingId);
        
        WebClient webClient = svcLocator.locate(fulfillmentWindowAPIUrl.toString(), responseReader.get());
//		String token = properties.getProperty("newapi.accessToken");
//		webClient.header("Authorization", "Bearer " + token);
		webClient.accept(MediaType.APPLICATION_XML);
		log.debug(" call getListingFulfillmentWindow api, url=" + fulfillmentWindowAPIUrl.toString());
		try{
			 Response response = webClient.get();
			 if(Response.Status.OK.getStatusCode() == response.getStatus()) {		    	
				 ListingFulfillmentWindowResponse listingFulfillmentWindow = (ListingFulfillmentWindowResponse)response.getEntity();
				 return listingFulfillmentWindow; 
			 }else{
				log.error("api domain=account, getListingFulfillmentWindow v1 call retrun status not 200, status=" + response.getStatus());
			 }
		  }catch (Exception e) {
			 log.error("api domain=account, exception while call getListingFulfillmentWindow v1 API", e);
		  }		
		return null;
	}
}
