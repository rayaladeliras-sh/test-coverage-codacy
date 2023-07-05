package com.stubhub.domain.account.helper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Cookie;
import org.springframework.http.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.AccessTokenProvider;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.stubhub.domain.account.helper.pricerec.entities.PricingRec;
import com.stubhub.domain.account.helper.pricerec.entities.PricingRecResponse;
import com.stubhub.domain.account.helper.pricerec.entities.PricingRequest;
//import com.stubhub.domain.recommendations.services.core.intf.pricing.entities.PricingRequest
import com.stubhub.domain.account.intf.PricingRecommendation;
import com.stubhub.newplatform.property.MasterStubhubPropertiesWrapper;
import com.stubhub.platform.utilities.webservice.svclocator.SvcLocator;

@Component("pricingRecHelper")
public class PricingRecHelper {

	@Autowired
    OAuth2RestTemplate oAuth2RestTemplate;
	
    @Autowired
    private SvcLocator svcLocator;

    @Autowired
    private MasterStubhubPropertiesWrapper properties;

    private ObjectMapper mapper;

    private static final Logger log = LoggerFactory.getLogger(PricingRecHelper.class);

    public PricingRecHelper() {
        mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationConfig.Feature.UNWRAP_ROOT_VALUE, false);
        mapper.configure(SerializationConfig.Feature.WRAP_ROOT_VALUE,true);
        mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
    }



    public List<PricingRecommendation> handlePriceRecommendations(List<Long> listings, String sellerGuid){
        PricingRequest request = buildPricingRecRequest(listings);
        PricingRecResponse pricingRecResponse = getPricingRecForListingsOAuth(request, sellerGuid);
        
        if(pricingRecResponse == null)
        {
        	log.warn("_message=pricingRecommendation, call price recommend gcp failed with 3 retries");
        	//pricingRecResponse = getPricingRecForListings(request, sellerGuid);
        }
        
        
        return getPricingRecommendation(pricingRecResponse);

    }


    private List<PricingRecommendation> getPricingRecommendation(PricingRecResponse pricingRecResponse){
        List<PricingRecommendation> recommendations = new ArrayList<PricingRecommendation>();
        if(pricingRecResponse != null) {
            List<PricingRec> pricingRecs = pricingRecResponse.getPricingRecommendations();
            if(pricingRecs != null && pricingRecs.size() > 0) {
                for (PricingRec pricingRec : pricingRecs) {
                    PricingRecommendation pricingRecommendation = new PricingRecommendation();
                    convertPricingRecToAccountRec(pricingRec, pricingRecommendation);
                    recommendations.add(pricingRecommendation);
                }
            }
        }
        return recommendations;
    }



    private void convertPricingRecToAccountRec(PricingRec from,PricingRecommendation to){

        to.setListingId(from.getListingId());
        to.setEventMaxPrice(from.getEventMaxPrice());
        to.setEventMinPrice(from.getEventMinPrice());
        to.setIsOverPriced(from.getIsOverPriced());
        to.setIsUnderPriced(from.getIsUnderPriced());
        to.setLowerboundPrice(from.getLowerboundPrice());
        to.setSectionMaxPrice(from.getSectionMaxPrice());
        to.setSectionMinPrice(from.getSectionMinPrice());
        to.setSuggestedPrice(from.getSuggestedPrice());
        to.setUpperboundPrice(from.getUpperboundPrice());
        to.setZoneMaxPrice(from.getZoneMaxPrice());
        to.setZoneMinPrice(from.getZoneMinPrice());
    }
    
    private PricingRecResponse getPricingRecForListingsOAuth(PricingRequest request, String sellerGuid){

        PricingRecResponse pricingRecResponse = null;
        String priceRecommendationsAPIUrl = properties.getProperty("recommendation.price.ticket.v2.cloud.api.url","https://apc.stubhub.com:443/ai/marketplacedynamics/core/v2/pricing");       
        HttpHeaders headers = new HttpHeaders();
        //headers.add("Accept-Language", "en-us");
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "application/json");
        headers.add("Cookie", "session_userGUID="+sellerGuid );
      
       try{
        
    	    String jsonPost = mapper.writeValueAsString(request);
        	log.info("_message=pricingRecommendation request json={}", jsonPost);
        	
        	//HttpEntity<PricingRequest> httpEntity = new HttpEntity<PricingRequest>(request, headers);
        	HttpEntity<String> httpEntity = new HttpEntity<String>(jsonPost, headers);
  
        	//log.debug("_message=pricingRecommendation, oAuth2RestTemplate access token vale={}", oAuth2RestTemplate.getAccessToken()!=null?oAuth2RestTemplate.getAccessToken().getValue():"null");
            	
            	/*log.info("token type: "+oAuth2RestTemplate.getAccessToken().getTokenType());
            	log.info("token additional info start: ");
            	for(String key : oAuth2RestTemplate.getAccessToken().getAdditionalInformation().keySet())
            	{
            		String value = oAuth2RestTemplate.getAccessToken().getAdditionalInformation().get(key).toString();
            		log.info(key+" : "+value);
            	}
            	
            	log.info("oAuth2RestTemplate access token end");
            	log.info("token scope info start: ");
            	
            	for(String s : oAuth2RestTemplate.getAccessToken().getScope())
            	{
            		log.info(s);
            	}
            	
            	log.info("token end info end");*/
        	
        	  log.info("_message=pricingRecommendation start to call recommendations pricing API, url={}", priceRecommendationsAPIUrl);
        	  
        	  final int maxTryCount = 3;
              int triedTimes = 0;
              long retryInterval = 1000;
              
              while(triedTimes<maxTryCount)
              {
            	  try
            	  {
            		
                  	ResponseEntity<PricingRecResponse> responseEntity = oAuth2RestTemplate.postForEntity(priceRecommendationsAPIUrl, httpEntity, PricingRecResponse.class);
                  	
                      if(HttpStatus.OK.equals(responseEntity.getStatusCode())) {
                           log.info("_message=\"pricingRecommendation GCP API response status={} pricingRecResponse body={} retryTimes={}", responseEntity.getStatusCode(), pricingRecResponse, triedTimes);
                           return responseEntity.getBody();
                      }else{
                          log.error("api_domain=account"
                                  + " status=error error_message=recommendations pricing GCP API call return status is not 200, status="
                                  + responseEntity.getStatusCode());
                      }
            	  }
            	  catch(Exception e)
            	  {
            		  log.error("api_domain=account"
            	                + " status=error error_message=unknown exception occurs when call recommendations pricing GCP API.",e);
            		  
            	  }
            	  
            	  triedTimes++;
                  try {
                      Thread.sleep(retryInterval);
                  } catch (InterruptedException e) {
                      log.error(ExceptionUtils.getFullStackTrace(e));
                  }
              }
              
            
        }catch (Exception e) {
            log.error("api_domain=account"
                + " status=error error_message=unknown exception occurs when call getPricingRecForListingsOAuth.",e);

        }
        return null;
    }

    private PricingRecResponse getPricingRecForListings(PricingRequest request, String sellerGuid){

        PricingRecResponse pricingRecResponse = null;
        String priceRecommendationsAPIUrl = properties.getProperty("recommendation.price.ticket.v2.api.url","https://api-int.stubprod.com/marketplacedynamics/core/v1/pricing");

        WebClient webClient = svcLocator.locate(priceRecommendationsAPIUrl.toString());
        webClient.accept(MediaType.APPLICATION_JSON);
        webClient.type(MediaType.APPLICATION_JSON);
        Cookie cookie = new Cookie("session_userGUID",sellerGuid);
        webClient.cookie(cookie);
        InputStream inStream = null;
        try{
            Response response = webClient.post(mapper.writeValueAsString(request));
            if(Response.Status.OK.getStatusCode() == response.getStatus()) {

                inStream = (InputStream) response.getEntity();

                pricingRecResponse = unMarshall(inStream);
                if (log.isDebugEnabled()) {
                    log.debug("_message=\"pricingRecommendation response status={} pricingRecResponse body={}", response.getStatus(), pricingRecResponse);
                }

            }else{
                log.error("api_domain=account"
                        + " status=error error_message=recommendations pricing API call return status is not 200, status="
                        + response.getStatus());
            }
        }catch (Exception e) {
            log.error("api_domain=account"
                + " status=error error_message=unknown exception occurs when call recommendations pricing API.",e);

        }
        return pricingRecResponse;
    }

    private PricingRecResponse unMarshall(InputStream is){
        try{
            return mapper.readValue(is,PricingRecResponse.class);
        }catch (Exception ex){
            log.error("_message=\"an exception has occured while parsing buyer cost response\" exception=" + ex.getMessage());
        }
        return null;

    }

    private PricingRequest buildPricingRecRequest(List<Long> listingIds){
        PricingRequest request = new PricingRequest();
        request.setListingIds(listingIds);        
        log.info("_message=\"build pricingRecommendation request. listingIds={}\"",listingIds);
        return request;
    }


}
