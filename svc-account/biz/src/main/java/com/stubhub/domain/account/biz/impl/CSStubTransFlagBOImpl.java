package com.stubhub.domain.account.biz.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.ResponseReader;
import org.apache.cxf.jaxrs.client.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.stubhub.domain.account.biz.intf.CSStubTransFlagBO;
import com.stubhub.domain.cs.intf.TransactionFlagResponse;
import com.stubhub.newplatform.property.MasterStubHubProperties;
import com.stubhub.platform.utilities.webservice.svclocator.SvcLocator;

@Component("csStubTransFlagBO")
public class CSStubTransFlagBOImpl implements CSStubTransFlagBO {
	private final static Logger log = LoggerFactory.getLogger(CSStubTransFlagBOImpl.class);
	
	@Autowired
	private SvcLocator svcLocator;
    
    private List<ResponseReader> responseReader;
    {
        ResponseReader reader = new ResponseReader();
        reader.setEntityClass(TransactionFlagResponse.class);
        responseReader = new ArrayList<ResponseReader>();
        responseReader.add(reader);
    }
    
	@Override
	public Map<String, Boolean> getCSStubTransFlag(List<String> orderIds) {
		Map<String, Boolean> csFlag = new HashMap<String, Boolean>();
		for (String orderId : orderIds){
			String transactionFlagApiUrl = getProperty("cs.get.transactionFlag.api.url", "http://api-int.stubhub.com/cs/orders/v1/transactionFlag/{orderId}");			
			transactionFlagApiUrl = transactionFlagApiUrl.replace("{orderId}", orderId);		
            
			log.debug("transactionFlagApiUrl= " + transactionFlagApiUrl);
			WebClient webClient = svcLocator.locate(transactionFlagApiUrl, responseReader);
			webClient.accept(MediaType.APPLICATION_XML);	    
			Response response = webClient.get();
            log.debug("responseStatus={}", response.getStatus());
			if(Response.Status.OK.getStatusCode() == response.getStatus()) {
				TransactionFlagResponse transactionFlagResponse = (TransactionFlagResponse) response.getEntity();
				if (transactionFlagResponse.getErrors() == null){
					if (transactionFlagResponse.getTransFlag().get(0).getId() == 8)
						csFlag.put(orderId, true);
					else
						csFlag.put(orderId, false);
				}
			} 
		}
		return csFlag; 
	}
	
	protected String getProperty(String propertyName, String defaultValue) {
		return MasterStubHubProperties.getProperty(propertyName, defaultValue);
	}

}
