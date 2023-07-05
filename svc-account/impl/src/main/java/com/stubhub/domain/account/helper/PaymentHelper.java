package com.stubhub.domain.account.helper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.ResponseReader;
import org.apache.cxf.jaxrs.client.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.stubhub.domain.user.payments.intf.CreditCardDetails;
import com.stubhub.domain.user.payments.intf.CreditCardDetails.ExpDate;
import com.stubhub.domain.user.payments.intf.CustomerPaymentInstrumentDetails;
import com.stubhub.domain.user.payments.intf.CustomerPaymentInstrumentMappingsResponse;
import com.stubhub.domain.user.payments.intf.CustomerPaymentInstrumentsResponse;
import com.stubhub.newplatform.property.MasterStubHubProperties;
import com.stubhub.platform.utilities.webservice.svclocator.SvcLocator;

@Component("paymentHelper")
public class PaymentHelper {
	
	private final static Logger log = LoggerFactory.getLogger(PaymentHelper.class);
	
	@Autowired
	private SvcLocator svcLocator;
	
	public Map<Long, CreditCardDetails> getMappedValidSellerCcId(String sellerGUID) {
		Map<Long, CreditCardDetails> sellerCcMap = new HashMap<Long, CreditCardDetails>();
		List<CustomerPaymentInstrumentDetails> customerPaymentInstruments = getAllSellerPaymentInstruments(sellerGUID);
		if(customerPaymentInstruments != null && customerPaymentInstruments.size() > 0) {
			for(CustomerPaymentInstrumentDetails customerPaymentInstrument : customerPaymentInstruments) {
				if((customerPaymentInstrument != null) && (customerPaymentInstrument.getCardDetails() != null)) {
					CreditCardDetails  cardDetails = customerPaymentInstrument.getCardDetails();
					if(isCCValid(cardDetails.getExpirationDate())) {
						CustomerPaymentInstrumentMappingsResponse  customerPaymentInstrumentMappingsResponse  = getPaymentInstrumentMappings(sellerGUID, customerPaymentInstrument.getId());
						if(customerPaymentInstrumentMappingsResponse != null) {
							sellerCcMap.put(Long.parseLong(customerPaymentInstrumentMappingsResponse.getInternalId()), cardDetails);
						}
					}				
				}
			}		
		}
		return sellerCcMap;
	}
	
    private final List<ResponseReader> customerPIReader;
    {
        ResponseReader reader = new ResponseReader();
        reader.setEntityClass(CustomerPaymentInstrumentsResponse.class);
        customerPIReader = new ArrayList<ResponseReader>();
        customerPIReader.add(reader);
    }
	public List<CustomerPaymentInstrumentDetails> getAllSellerPaymentInstruments(String sellerGUID){
		String getAllPaymentInstrumentsApiUrl = getProperty("getallsellerpaymentinstruments.api.url", "https://api-int.stubprod.com/user/customers/v1/{sellerId}/paymentInstruments");
		getAllPaymentInstrumentsApiUrl = getAllPaymentInstrumentsApiUrl.replace("{sellerId}", sellerGUID);
		log.debug("getAllPaymentInstrumentsApiUrl=" + getAllPaymentInstrumentsApiUrl);
		

	    
        WebClient webClient = svcLocator.locate(getAllPaymentInstrumentsApiUrl, customerPIReader);
	    webClient.accept(MediaType.APPLICATION_JSON);	    
	    Response response = webClient.get();
	    
	    if(Response.Status.OK.getStatusCode() == response.getStatus()) {
	    	log.info("getAllPaymentInstruments api call successful for sellerGUID="+sellerGUID);
	    	CustomerPaymentInstrumentsResponse customerPaymentInstrumentsResponse = (CustomerPaymentInstrumentsResponse) response.getEntity();
	    	if(customerPaymentInstrumentsResponse != null){
	    		return customerPaymentInstrumentsResponse.getPaymentInstruments();
	    	}
		}  else {
			log.error("Error occured while calling getAllPaymentInstruments api for  sellerGUID=" + sellerGUID + " responseCode=" + response.getStatus());
		}
	    return null;
	}
	
    private final List<ResponseReader> customerPIMappingReader;
    {
        ResponseReader reader = new ResponseReader();
        reader.setEntityClass(CustomerPaymentInstrumentMappingsResponse.class);
        customerPIMappingReader = new ArrayList<ResponseReader>();
        customerPIMappingReader.add(reader);
    }
	public CustomerPaymentInstrumentMappingsResponse getPaymentInstrumentMappings(String userGUID, String pid) {		
		String paymentInstrumentMappingsApiUrl = getProperty("getpaymentinstrumentmappings.api.url", "https://api-int.stubprod.com/user/customers/v1/{customerid}/paymentInstrumentMappings/{paymentinstrumentmappingsid}");			
		paymentInstrumentMappingsApiUrl = paymentInstrumentMappingsApiUrl.replace("{customerid}", userGUID);
		paymentInstrumentMappingsApiUrl = paymentInstrumentMappingsApiUrl.replace("{paymentinstrumentmappingsid}", pid);
		log.debug("paymentInstrumentMappingsApiUrl= " + paymentInstrumentMappingsApiUrl);
		

	    
        WebClient webClient = svcLocator.locate(paymentInstrumentMappingsApiUrl, customerPIMappingReader);
	    webClient.accept(MediaType.APPLICATION_JSON);	    
	    Response response = webClient.get();
	    
	    if(Response.Status.OK.getStatusCode() == response.getStatus()) {
	    	log.info("getPaymentInstrumentMappings api call successful for pid=" + pid + " userGUID=" + userGUID);
	    	return (CustomerPaymentInstrumentMappingsResponse) response.getEntity();
		} else if(Response.Status.NOT_FOUND.getStatusCode() == response.getStatus()){
			log.error("Invalid pid=" + pid + ", userGUID=" + userGUID);
		} else {
			log.error("System error occured while calling getPaymentInstrumentMappings api  userGUID=" + userGUID + " pid=" + pid + " responseCode="+response.getStatus());
		}
	    return null;		
	}

	
	
	private boolean isCCValid(ExpDate expirationDate) {
		Calendar today = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		if(today.get(Calendar.YEAR) < Integer.parseInt(expirationDate.getYear())) {
			return true;
		} else if(today.get(Calendar.YEAR) == Integer.parseInt(expirationDate.getYear()) && (today.get(Calendar.MONTH) + 1) <= Integer.parseInt(expirationDate.getMonth())) {
			return true;
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
	protected String getProperty (String propertyName, String defaultValue) {
		return MasterStubHubProperties.getProperty(propertyName, defaultValue);
	}
	
}
