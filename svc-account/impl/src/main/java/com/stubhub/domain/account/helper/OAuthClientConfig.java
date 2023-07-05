package com.stubhub.domain.account.helper;

//import java.util.ArrayList;
//import java.util.List;
//import java.util.function.Consumer;

//import org.apache.htrace.fasterxml.jackson.databind.DeserializationFeature;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
//import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.stereotype.Component;
//import org.codehaus.jackson.map.DeserializationConfig;
//import org.codehaus.jackson.map.ObjectMapper;
//import org.springframework.http.converter.HttpMessageConverter;


@Component
public class OAuthClientConfig {

    @Value("${security.oauth2.client.client-id:64afaca0a5384d4da2224794d3ba2b4d}")
    private String clientId;

    @Value("${security.oauth2.client.client-secret:9b017e21b9504e56bcfedd8e7bcd8b83}")
    private String clientSecret;

    @Value("${security.oauth2.client.grant-type:client_credentials}")
    private String grantType;

    @Value("${security.oauth2.client.access-token-uri:https://api-pcf.stubhub.com:443/identity/oauth/v1/token?grant_type=client_credentials}")
    private String accessTokenUrl;

    @Value("${stubhub.eproxy.host:eproxy.stubprod.com}")
    private String stubhubEproxyHost;

    @Value("${stubhub.eproxy.port:3128}")
    private int stubhubEproxyPort;

    @Value("${oAuth.client.connection.request.timeout:3000}")
    private int connectRequestTimeOut;

    @Value("${oAuth.client.connection.timeout:3000}")
    private int connectTimeOut;

    @Value("${oAuth.client.socket.timeout:5000}")
    private int socketTimeOut;
    
    @Value("${oAuth.client.max.con.per.route:20}")
    private int maxConPerRoute;
    
    @Value("${oAuth.client.max.con.num:40}")
    private int maxCon;

    private final static String proxyHostScheme = "http";

    private static final Logger logger = LoggerFactory.getLogger(OAuthClientConfig.class);


    /**
     * For Oauth Provider Configuration
     * configuration in applicaiton.yml file
     * clientId : auto_client
     * clientSecret : auto_client
     * grant_type : client_credentials
     * access-token-uri : https://token-mgt-release-qa.cf01.useast4.devstubapp.cloud/identity/oauth/v1/token
     *
     * @return
     */
    @Bean
    public OAuth2RestTemplate oAuth2RestTemplate() {
        ClientCredentialsResourceDetails details = new ClientCredentialsResourceDetails();
        details.setClientId(clientId);
        details.setClientSecret(clientSecret);
        details.setGrantType(grantType);
        details.setAccessTokenUri(accessTokenUrl);
        RequestConfig config = RequestConfig
                .custom()
                .setConnectionRequestTimeout(connectRequestTimeOut)
                .setConnectTimeout(connectTimeOut)
                .setSocketTimeout(socketTimeOut)
                .build();

        CloseableHttpClient httpClient = HttpClients
                .custom()
                .setDefaultRequestConfig(config)
                .setRoutePlanner(new DefaultProxyRoutePlanner(new HttpHost(stubhubEproxyHost, stubhubEproxyPort, proxyHostScheme)))
                .setMaxConnPerRoute(maxConPerRoute)
                .setMaxConnTotal(maxCon)
                .build();

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        ClientCredentialsAccessTokenProvider clientCredentialsAccessTokenProvider = new ClientCredentialsAccessTokenProvider();
        clientCredentialsAccessTokenProvider.setRequestFactory(requestFactory);
        OAuth2RestTemplate oAuth2RestTemplate = new OAuth2RestTemplate(details, new DefaultOAuth2ClientContext());
        oAuth2RestTemplate.setRequestFactory(requestFactory);
        oAuth2RestTemplate.setAccessTokenProvider(clientCredentialsAccessTokenProvider);
        
     /*   final ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
       
        MappingJacksonHttpMessageConverter mapConvert = new MappingJacksonHttpMessageConverter();
        mapConvert.setObjectMapper(mapper);
        for(int i = 0; i<oAuth2RestTemplate.getMessageConverters().size(); i++)
        {
        	if(oAuth2RestTemplate.getMessageConverters().get(i)  instanceof MappingJacksonHttpMessageConverter)
        	{
        		((MappingJacksonHttpMessageConverter)oAuth2RestTemplate.getMessageConverters().get(i)).setObjectMapper(mapper);
        	}
        }*/
        return oAuth2RestTemplate;
    }

}
