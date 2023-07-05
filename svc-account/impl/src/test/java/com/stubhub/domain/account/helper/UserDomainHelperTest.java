/**
 * 
 */
package com.stubhub.domain.account.helper;

import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.ResponseReader;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.stubhub.domain.infrastructure.common.exception.base.SHSystemException;
import com.stubhub.domain.infrastructure.common.exception.derived.SHBadRequestException;
import com.stubhub.domain.infrastructure.common.exception.derived.SHResourceNotFoundException;
import com.stubhub.domain.infrastructure.common.exception.derived.SHUnauthorizedException;
import com.stubhub.domain.user.usergroup.intf.MembershipResponse;
import com.stubhub.newplatform.property.MasterStubHubProperties;
import com.stubhub.newplatform.property.loader.IConfigLoader;
import com.stubhub.platform.utilities.webservice.svclocator.SvcLocator;

/**
 * @author yunpli
 *
 */
public class UserDomainHelperTest {
    
    private SvcLocator svcLocator;
    private WebClient webClient;
    private UserDomainHelper helper;
    
    @BeforeMethod
    public void setUp() {
        helper = new UserDomainHelper() {
            @Override
            protected String getProperty(String propertyName, String defaultValue) {
                return defaultValue;
            }
        };
        svcLocator = Mockito.mock(SvcLocator.class);
        webClient = Mockito.mock(WebClient.class);
        ReflectionTestUtils.setField(helper, "svcLocator", svcLocator);
        Mockito.when(
                svcLocator.locate(Mockito.anyString(), Mockito.anyListOf(ResponseReader.class)))
                .thenReturn(webClient);
    }
    
    @Test
    public void user_is_the_group_member() throws Exception {
        MembershipResponse response = new MembershipResponse();
        response.setMember(true);
        Mockito.when(webClient.get()).thenReturn(getResponse(response));
        
        assertTrue(helper.isUserMemberOfGroup("testGUID", 1234));
    }
    
    @Test
    public void user_is_NOT_the_group_member() throws Exception {
        MembershipResponse response = new MembershipResponse();
        response.setMember(false);
        Mockito.when(webClient.get()).thenReturn(getResponse(response));
        
        assertFalse(helper.isUserMemberOfGroup("testGUID", 1234));
    }
    
    @Test(expectedExceptions = { SHBadRequestException.class })
    public void expect_400() throws Exception {
        Mockito.when(webClient.get()).thenReturn(getResponseError(400));
        helper.isUserMemberOfGroup("testGUID", 1234);
    }
    
    @Test(expectedExceptions = { SHUnauthorizedException.class })
    public void expect_401() throws Exception {
        Mockito.when(webClient.get()).thenReturn(getResponseError(401));
        helper.isUserMemberOfGroup("testGUID", 1234);
    }
    
    @Test(expectedExceptions = { SHResourceNotFoundException.class })
    public void expect_404() throws Exception {
        Mockito.when(webClient.get()).thenReturn(getResponseError(404));
        helper.isUserMemberOfGroup("testGUID", 1234);
    }
    
    @Test(expectedExceptions = { SHSystemException.class })
    public void expect_500() throws Exception {
        Mockito.when(webClient.get()).thenReturn(getResponseError(500));
        helper.isUserMemberOfGroup("testGUID", 1234);
    }
    
    @Test(expectedExceptions = { SHResourceNotFoundException.class })
    public void resource_not_found_exception() throws Exception {
        Mockito.when(webClient.get()).thenThrow(new SHResourceNotFoundException());
        helper.isUserMemberOfGroup("testGUID", 1234);
    }
    
    @Test(expectedExceptions = { RuntimeException.class })
    public void unknown_exception() throws Exception {
        Mockito.when(webClient.get()).thenThrow(new RuntimeException());
        helper.isUserMemberOfGroup("testGUID", 1234);
    }
    
    // @Test(enabled = false)
    // public void test_real() throws Exception {
    // UserDomainHelper helper = new UserDomainHelper();
    // SvcLocator locator = new SvcLocator();
    // List<IConfigLoader> loaders = Arrays.<IConfigLoader>asList(new IConfigLoader() {
    //
    // @Override
    // public Map<String, String> load() throws Exception {
    // return new HashMap<String, String>();
    // }
    //
    // });
    // MasterStubHubProperties.setLoaders(loaders);
    // MasterStubHubProperties.load();
    // ReflectionTestUtils.setField(helper, "svcLocator", locator);
    // assertTrue(helper.isUserMemberOfGroup("6C21FF95120A3BC0E04400144FB7AAA6", 18009l));
    // }
    
    private Response getResponse(final Object content) {
        Response response = new Response() {
            
            @Override
            public int getStatus() {
                return 200;
            }
            
            @Override
            public MultivaluedMap<String, Object> getMetadata() {
                return null;
            }
            
            @Override
            public Object getEntity() {
                return content;
            }
        };
        return response;
    }
    
    private Response getResponseError(final int code) {
        Response response = new Response() {
            
            @Override
            public int getStatus() {
                return code;
            }
            
            @Override
            public MultivaluedMap<String, Object> getMetadata() {
                return null;
            }
            
            @Override
            public Object getEntity() {
                String responseString = "{}";
                return new ByteArrayInputStream(responseString.getBytes());
            }
        };
        return response;
    }

}
