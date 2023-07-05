/**
 * 
 */
package com.stubhub.domain.account.helper;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLEncoder;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.stubhub.common.exception.ErrorType;
import com.stubhub.domain.inventory.common.util.ErrorCode;
import com.stubhub.domain.inventory.common.util.ListingBusinessException;
import com.stubhub.domain.inventory.common.util.ListingError;
import com.stubhub.domain.inventory.eligibility.dto.response.Reason;
import com.stubhub.domain.inventory.eligibility.dto.response.SellerEligibilityRulesResponse;
import com.stubhub.newplatform.property.MasterStubHubProperties;
import com.stubhub.platform.utilities.webservice.svclocator.SvcLocator;


/**
 * @author sjayaswal
 *
 */
@Component("sellerEligibiltyHelper")
public class SellerEligibilityHelper {

	private final static Logger log = LoggerFactory.getLogger(SellerEligibilityHelper.class);

	@Autowired
	private SvcLocator svcLocator;
	
	public boolean checkSellerEligibility() throws Exception {
		String sellerEligibilityApiUrl = getProperty("seller.rules.api.url", "http://apx.stubprod.com/inventorynew/eligibility/v1");
		sellerEligibilityApiUrl = sellerEligibilityApiUrl + "/rules/" + URLEncoder.encode("mmi", "UTF-8");
//		log.debug("seller eligibility api url=" + sellerEligibilityApiUrl);
		
		WebClient webClient =  svcLocator.locate(sellerEligibilityApiUrl);
		webClient.accept(MediaType.APPLICATION_JSON);
		
		Response response = webClient.get();
//		log.debug("sellerEligibilityApiUrl=" + sellerEligibilityApiUrl+" :: responseStatus=" + response.getStatus());
	    
		if(Response.Status.OK.getStatusCode() == response.getStatus()) {
			InputStream responseStream = (InputStream)response.getEntity();
	    	byte[] data = new byte[1024];
	    	ByteArrayOutputStream bos = new ByteArrayOutputStream();
	    	while(true){
	    		int n = responseStream.read(data);
	    		if(n == -1) break;
	    		bos.write(data,0,n);
	    	}
	    	String responseString = bos.toString();
	    	SellerEligibilityRulesResponse sellerEligibilityRulesResponse = null;
	    	if(responseString != null) {
				ObjectMapper mapper = new ObjectMapper();
				sellerEligibilityRulesResponse = mapper.readValue(responseString, SellerEligibilityRulesResponse.class);
			}
			
			if(sellerEligibilityRulesResponse != null) {
				if("YES".equalsIgnoreCase(sellerEligibilityRulesResponse.getAllowed())) {
//					log.debug("sellerEligibility check is success.");
					return true;
				} else if("NO".equalsIgnoreCase(sellerEligibilityRulesResponse.getAllowed())) {
					// BSF does not make SellerEligibility check fail
					if (sellerEligibilityRulesResponse.getReasons() != null && sellerEligibilityRulesResponse.getReasons().size() > 0) {
						for (Reason reason : sellerEligibilityRulesResponse.getReasons()) {
							if (!reason.getReasonCode().equalsIgnoreCase("SellerInfoBlock") && !reason.getReasonCode().equalsIgnoreCase("SellerInfoForCurrCountryBlock")) {
//								log.debug("sellerEligibility check is fail with ==> " + reason.getReasonCode());
								return false;
							}
						}

//						log.debug("sellerEligibility check can be pass because it is one bsf case.");
						
						return true;
					}
					
//					log.debug("sellerEligibility check is fail.");

					return false;
				}
			}
		} else {
			InputStream is = (InputStream) response.getEntity();
			if(is != null) {
				is.close();
			}
			ListingError listingError = new ListingError(ErrorType.SYSTEMERROR, ErrorCode.SYSTEM_ERROR, "System error occured", null);
			throw new ListingBusinessException(listingError);
		}
		return false;
	}
	
	/**
	 * Returns property value for the given propertyName. This protected method has been created to get
	 * around the static nature of the MasterStubHubProperties' methods for Unit tests. The test classes are expected to 
	 * override this method with custom implementation.
	 * @param propertyName
	 * @param defaultValue
	 * @return
	 */
	protected String getProperty(String propertyName, String defaultValue) {
		return MasterStubHubProperties.getProperty(propertyName, defaultValue);
	}
	
	
}
