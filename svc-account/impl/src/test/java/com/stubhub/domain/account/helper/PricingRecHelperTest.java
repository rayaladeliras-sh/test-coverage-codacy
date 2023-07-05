package com.stubhub.domain.account.helper;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.stubhub.domain.account.intf.PricingRecommendation;
import com.stubhub.domain.account.helper.pricerec.entities.PricingRec;
import com.stubhub.domain.account.helper.pricerec.entities.PricingRecResponse;
import com.stubhub.domain.account.helper.pricerec.entities.PricingRequest;
import com.stubhub.newplatform.property.MasterStubhubPropertiesWrapper;
import com.stubhub.platform.utilities.webservice.svclocator.SvcLocator;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import junit.framework.Assert;

public class PricingRecHelperTest {
    private SvcLocator svcLocator;
    private MasterStubhubPropertiesWrapper properties;
    
    @InjectMocks
    private PricingRecHelper pricingRecHelper;
    private List<Long> listings;
    private String sellerGuid = "C779915579FC5E14E04400212861B256";
  
    @Mock
    OAuth2RestTemplate oAuth2RestTemplate;
    
    @Mock
    ResponseEntity<PricingRecResponse> responseEntity;

    @BeforeMethod
    public void setup(){
    	MockitoAnnotations.initMocks(this);
      
        svcLocator = Mockito.mock(SvcLocator.class);
    
        properties = Mockito.mock(MasterStubhubPropertiesWrapper.class);
        ReflectionTestUtils.setField(pricingRecHelper, "properties", properties);
        ReflectionTestUtils.setField(pricingRecHelper, "svcLocator", svcLocator);

        Mockito.when(properties.getProperty("recommendation.price.ticket.v2.api.url","https://api-int.stubprod.com/marketplacedynamics/core/v1/pricing")).thenReturn("https://api-int.stubprod.com/marketplacedynamics/core/v1/pricing");
    }


    @Test
    public void testHandlePriceRecommendations(){

        listings = new ArrayList<Long>();
        listings.add(1212732400L);
        listings.add(1209269254L);
        listings.add(12092692540L);
        listings.add(12092692540L);
        listings.add(12092692541L);
        listings.add(120926925412L);
        listings.add(12092692543L);
        listings.add(12092692544L);
        listings.add(12092692545L);
        listings.add(12092692540L);
        listings.add(12092692546L);
        listings.add(12092692547L);
        listings.add(12092692548L);
        listings.add(12092692549L);
        listings.add(120926925410L);

      //  WebClient webClient = Mockito.mock(WebClient.class);
      //  Mockito.when(svcLocator.locate(Mockito.anyString())).thenReturn(webClient);
      //  Mockito.when(webClient.post(Mockito.any(PricingRequest.class))).thenReturn(mockResponse());
      
        PricingRecResponse priceRecResp = new PricingRecResponse();
        PricingRec priceRec = new PricingRec();
       
        priceRec.setEventMaxPrice(new Double(515.02));
        priceRec.setEventMinPrice(new Double(67));
        priceRec.setZoneMaxPrice(new Double(609.56));
        priceRec.setZoneMinPrice(new Double(225));
        priceRec.setSectionMaxPrice(new Double(609.56));
        priceRec.setSectionMinPrice(new Double(225));
        priceRec.setSuggestedPrice(new Long(234));
        priceRec.setIsOverPriced(true);
        priceRec.setIsUnderPriced(null);
        priceRec.setUpperboundPrice(new Long(243));
        priceRec.setLowerboundPrice(new Long(169));
        List<PricingRec> listPriceRec = new ArrayList<PricingRec>();
        listPriceRec.add(priceRec);
        priceRecResp.setPricingRecommendations(listPriceRec);
        Mockito.when(responseEntity.getStatusCode()).thenReturn(HttpStatus.OK);
        Mockito.when(responseEntity.getBody()).thenReturn(priceRecResp);
        Mockito.when(oAuth2RestTemplate.postForEntity(Mockito.anyString(), Mockito.any(HttpEntity.class), Mockito.eq(PricingRecResponse.class)))
        .thenReturn(responseEntity);
        Mockito.when(oAuth2RestTemplate.getAccessToken()).thenReturn(new DefaultOAuth2AccessToken("default"));
        

        List<PricingRecommendation> result  = pricingRecHelper.handlePriceRecommendations(listings, sellerGuid);
        PricingRecommendation pricingRecommendation = result.get(0);
        Assert.assertNotNull(pricingRecommendation);
        Assert.assertEquals(pricingRecommendation.getEventMaxPrice(),new Double(515.02));
        Assert.assertEquals(pricingRecommendation.getEventMinPrice(),new Double(67));
        Assert.assertEquals(pricingRecommendation.getZoneMaxPrice(),new Double(609.56));
        Assert.assertEquals(pricingRecommendation.getZoneMinPrice(),new Double(225));
        Assert.assertEquals(pricingRecommendation.getSectionMaxPrice(),new Double(609.56));
        Assert.assertEquals(pricingRecommendation.getSectionMinPrice(),new Double(225));
        Assert.assertEquals(pricingRecommendation.getSuggestedPrice(),new Long(234));
        Assert.assertTrue(pricingRecommendation.getIsOverPriced());
        Assert.assertNull(pricingRecommendation.getIsUnderPriced());
        Assert.assertEquals(pricingRecommendation.getUpperboundPrice(),new Long(243));
        Assert.assertEquals(pricingRecommendation.getLowerboundPrice(),new Long(169));

    }
    
  /*  @Test
    public void testHandlePriceRecommendationsFallBack()
    {

        listings = new ArrayList<Long>();
        listings.add(1212732400L);
        listings.add(1209269254L);
        listings.add(12092692540L);
        listings.add(12092692540L);
        listings.add(12092692541L);
        listings.add(120926925412L);
        listings.add(12092692543L);
        listings.add(12092692544L);
        listings.add(12092692545L);
        listings.add(12092692540L);
        listings.add(12092692546L);
        listings.add(12092692547L);
        listings.add(12092692548L);
        listings.add(12092692549L);
        listings.add(120926925410L);

        WebClient webClient = Mockito.mock(WebClient.class);
        Mockito.when(svcLocator.locate(Mockito.anyString())).thenReturn(webClient);
        Mockito.when(webClient.post(Mockito.any(PricingRequest.class))).thenReturn(mockResponse());
      
        PricingRecResponse priceRecResp = new PricingRecResponse();
        PricingRec priceRec = new PricingRec();
       
        priceRec.setEventMaxPrice(new Double(515.02));
        priceRec.setEventMinPrice(new Double(67));
        priceRec.setZoneMaxPrice(new Double(609.56));
        priceRec.setZoneMinPrice(new Double(225));
        priceRec.setSectionMaxPrice(new Double(609.56));
        priceRec.setSectionMinPrice(new Double(225));
        priceRec.setSuggestedPrice(new Long(234));
        priceRec.setIsOverPriced(true);
        priceRec.setIsUnderPriced(null);
        priceRec.setUpperboundPrice(new Long(243));
        priceRec.setLowerboundPrice(new Long(169));
        List<PricingRec> listPriceRec = new ArrayList<PricingRec>();
        listPriceRec.add(priceRec);
        priceRecResp.setPricingRecommendations(listPriceRec);
        Mockito.when(responseEntity.getStatusCode()).thenReturn(HttpStatus.BAD_REQUEST);
        //Mockito.when(responseEntity.getBody()).thenReturn(priceRecResp);
        Mockito.when(oAuth2RestTemplate.postForEntity(Mockito.anyString(), Mockito.any(HttpEntity.class), Mockito.eq(PricingRecResponse.class)))
        .thenReturn(responseEntity);
        Mockito.when(oAuth2RestTemplate.getAccessToken()).thenReturn(new DefaultOAuth2AccessToken("default"));
        

        List<PricingRecommendation> result  = pricingRecHelper.handlePriceRecommendations(listings, sellerGuid);
        PricingRecommendation pricingRecommendation = result.get(0);
        Assert.assertNotNull(pricingRecommendation);
        Assert.assertEquals(pricingRecommendation.getEventMaxPrice(),new Double(515.02));
        Assert.assertEquals(pricingRecommendation.getEventMinPrice(),new Double(67));
        Assert.assertEquals(pricingRecommendation.getZoneMaxPrice(),new Double(609.56));
        Assert.assertEquals(pricingRecommendation.getZoneMinPrice(),new Double(225));
        Assert.assertEquals(pricingRecommendation.getSectionMaxPrice(),new Double(609.56));
        Assert.assertEquals(pricingRecommendation.getSectionMinPrice(),new Double(225));
        Assert.assertEquals(pricingRecommendation.getSuggestedPrice(),new Long(234));
        Assert.assertTrue(pricingRecommendation.getIsOverPriced());
        Assert.assertNull(pricingRecommendation.getIsUnderPriced());
        Assert.assertEquals(pricingRecommendation.getUpperboundPrice(),new Long(243));
        Assert.assertEquals(pricingRecommendation.getLowerboundPrice(),new Long(169));

    }*/
    
    @Test
    public void testHandlePriceRecommendationsNullOauth(){

        listings = new ArrayList<Long>();
        listings.add(1212732400L);
        listings.add(1209269254L);
        listings.add(12092692540L);
        listings.add(12092692540L);
        listings.add(12092692541L);
        listings.add(120926925412L);
        listings.add(12092692543L);
        listings.add(12092692544L);
        listings.add(12092692545L);
        listings.add(12092692540L);
        listings.add(12092692546L);
        listings.add(12092692547L);
        listings.add(12092692548L);
        listings.add(12092692549L);
        listings.add(120926925410L);

        PricingRecResponse priceRecResp = new PricingRecResponse();
        PricingRec priceRec = new PricingRec();
       
        priceRec.setEventMaxPrice(new Double(515.02));
        priceRec.setEventMinPrice(new Double(67));
        priceRec.setZoneMaxPrice(new Double(609.56));
        priceRec.setZoneMinPrice(new Double(225));
        priceRec.setSectionMaxPrice(new Double(609.56));
        priceRec.setSectionMinPrice(new Double(225));
        priceRec.setSuggestedPrice(new Long(234));
        priceRec.setIsOverPriced(true);
        priceRec.setIsUnderPriced(null);
        priceRec.setUpperboundPrice(new Long(243));
        priceRec.setLowerboundPrice(new Long(169));
        List<PricingRec> listPriceRec = new ArrayList<PricingRec>();
        listPriceRec.add(priceRec);
        priceRecResp.setPricingRecommendations(listPriceRec);
        Mockito.when(responseEntity.getStatusCode()).thenReturn(HttpStatus.OK);
        Mockito.when(responseEntity.getBody()).thenReturn(priceRecResp);
        Mockito.when(oAuth2RestTemplate.postForEntity(Mockito.anyString(), Mockito.any(HttpEntity.class), Mockito.eq(PricingRecResponse.class)))
        .thenReturn(responseEntity);
      
        List<PricingRecommendation> result  = pricingRecHelper.handlePriceRecommendations(listings, sellerGuid);
        PricingRecommendation pricingRecommendation = result.get(0);
        Assert.assertNotNull(pricingRecommendation);
        Assert.assertEquals(pricingRecommendation.getEventMaxPrice(),new Double(515.02));
        Assert.assertEquals(pricingRecommendation.getEventMinPrice(),new Double(67));
        Assert.assertEquals(pricingRecommendation.getZoneMaxPrice(),new Double(609.56));
        Assert.assertEquals(pricingRecommendation.getZoneMinPrice(),new Double(225));
        Assert.assertEquals(pricingRecommendation.getSectionMaxPrice(),new Double(609.56));
        Assert.assertEquals(pricingRecommendation.getSectionMinPrice(),new Double(225));
        Assert.assertEquals(pricingRecommendation.getSuggestedPrice(),new Long(234));
        Assert.assertTrue(pricingRecommendation.getIsOverPriced());
        Assert.assertNull(pricingRecommendation.getIsUnderPriced());
        Assert.assertEquals(pricingRecommendation.getUpperboundPrice(),new Long(243));
        Assert.assertEquals(pricingRecommendation.getLowerboundPrice(),new Long(169));

    }


    @Test
    public void testHandlePriceRecommendationsWithNo200ResponseFromPricingRec(){

        listings = new ArrayList<Long>();

        listings.add(1212732400L);
        listings.add(1209269254L);

        WebClient webClient = Mockito.mock(WebClient.class);
        Mockito.when(svcLocator.locate(Mockito.anyString())).thenReturn(webClient);
        Mockito.when(webClient.post(Mockito.any(PricingRequest.class))).thenReturn(mockNon200Response());

        List<PricingRecommendation> result =  pricingRecHelper.handlePriceRecommendations(listings, sellerGuid);

        Assert.assertTrue(result.size() == 0);

    }


    private Response mockResponse(){
        Response response = new Response() {
            @Override
            public Object getEntity() {
                InputStream in = PricingRecHelperTest.class.getClassLoader().getResourceAsStream("pricingRecommendationResponse.json");
                return in;
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
    
    private Response mockNon200Response(){
        Response response = new Response() {
            @Override
            public Object getEntity() {
                InputStream in = PricingRecHelperTest.class.getClassLoader().getResourceAsStream("pricingRecommendationResponse.json");
                return in;
            }

            @Override
            public int getStatus() {
                return 500;
            }

            @Override
            public MultivaluedMap<String, Object> getMetadata() {
                return null;
            }
        };
        return response;
    }




}
