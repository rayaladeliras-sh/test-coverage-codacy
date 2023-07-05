package com.stubhub.domain.account.helper;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.ResponseReader;
import org.apache.cxf.jaxrs.client.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.stubhub.domain.infrastructure.common.core.monitor.SHMonitor;
import com.stubhub.domain.infrastructure.common.core.monitor.SHMonitorFactory;
import com.stubhub.domain.infrastructure.common.exception.base.SHSystemException;
import com.stubhub.domain.infrastructure.common.exception.derived.SHBadRequestException;
import com.stubhub.domain.infrastructure.common.exception.derived.SHResourceNotFoundException;
import com.stubhub.domain.user.contactsV2.intf.CustomerContactV2Details;
import com.stubhub.newplatform.property.MasterStubHubProperties;
import com.stubhub.platform.utilities.webservice.svclocator.SvcLocator;

/**
 * @author vpothuru
 * date 11/04/15
 *
 */
@Component("customerContactHelper")
public class CustomerContactHelper {

	@Autowired
	private SvcLocator svcLocator;
	
	private final static Logger log = LoggerFactory.getLogger(CustomerContactHelper.class);
	private static final String CC_API_URL = "customer.contactdetails.v2.url";
	private static final String CC_API_URL_DEFAULT = "https://api.stubhub.com/user/customers/v1/{customerGuid}/contactsV2/{contactGuid}";

	//log identifiers
	private static final String LOG_URI_PREFIX = "api_domain=account, api_resource={}, api_method={}, api_uri={}";
	private static final String LOG_INFO_PREFIX = "api_domain=account, api_resource={}, api_method={}, input_param={} message={}";
	private static final String LOG_ERROR_PREFIX = "api_domain=account, api_resource={}, api_method={}, " +
			                                       "input_param={}, response status={}, error_message={}, time_msec={}";

	//logging parameter values
	private String apiResource = this.getClass().getName();
	private String apiMethod = null;
	private String inputParam;


	private static final List<ResponseReader> repsonseReaderList = new ArrayList<ResponseReader>();
    {
        repsonseReaderList.add(new ResponseReader(CustomerContactV2Details.class));
    }
	public CustomerContactV2Details getCustomerContactDetails(String customerGuid, String contactGuid) throws Exception {
		
		inputParam = "guid: " + customerGuid + " contact id: " + contactGuid;
		
		//api response objects
		CustomerContactV2Details customerContactDetails = null;
		
		SHMonitor mon = SHMonitorFactory.getMonitor().start();
		apiMethod = "getCustomerContactDetails";
		
    	try {
    		
    		String ccURL = getProperty(CC_API_URL, CC_API_URL_DEFAULT);
    		ccURL = ccURL.replace("{customerGuid}", customerGuid);
    		ccURL = ccURL.replace("{contactGuid}", contactGuid);
    		
    		log.info(LOG_URI_PREFIX, apiResource, apiMethod, ccURL);


			WebClient webClient = svcLocator.locate(ccURL, repsonseReaderList);
			webClient.accept(MediaType.APPLICATION_JSON);

			//invoke request and get response
	    	Response response = webClient.get();
	    	
	    	if (Response.Status.OK.getStatusCode() == response.getStatus()) {
	    		
	    		customerContactDetails = (CustomerContactV2Details) response.getEntity();
		    	
	    	} else if (Response.Status.BAD_REQUEST.getStatusCode() == response.getStatus()){
	    		log.error(LOG_ERROR_PREFIX, apiResource, apiMethod, inputParam, response.getStatus(), "bad request status", mon.getTime());
	    		throw new SHBadRequestException(); //400
	    	} else if (Response.Status.NOT_FOUND.getStatusCode() == response.getStatus()){
	    		log.error(LOG_ERROR_PREFIX, apiResource, apiMethod, inputParam, response.getStatus(), "not found status",  mon.getTime());
	    		throw new SHResourceNotFoundException("Unable to find customer contact detials for seller tax contact guid: " + contactGuid); //404
	    	} else {
	    		log.error(LOG_ERROR_PREFIX, apiResource, apiMethod, inputParam, response.getStatus(), "unexpected status", mon.getTime());
	    		throw new SHSystemException(); //500
	    	}
    	} catch (SHResourceNotFoundException resourceNotFound) {
			throw resourceNotFound;
		}catch (Exception ex){
    		log.error(LOG_ERROR_PREFIX, apiResource, apiMethod, inputParam, "unknown exception", ex.getMessage(), "");
    		throw ex;
    	} finally {
    		mon.stop();
    		log.info(LOG_INFO_PREFIX, apiResource, apiMethod , inputParam, "excution time:" + mon.getTime());
    	}
    	
    	return customerContactDetails;
	}	

    protected String getProperty(String propertyName, String defaultValue) {
		return MasterStubHubProperties.getProperty(propertyName, defaultValue);
	}

}
