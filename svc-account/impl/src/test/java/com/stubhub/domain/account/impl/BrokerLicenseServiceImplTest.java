package com.stubhub.domain.account.impl;

import com.stubhub.domain.account.common.util.SecurityContextUtil;
import com.stubhub.domain.account.datamodel.dao.BrokerLicenseDAO;
import com.stubhub.domain.account.datamodel.entity.BrokerLicense;
import com.stubhub.domain.account.datamodel.entity.BrokerLicenseSuccessEntity;
import com.stubhub.domain.account.intf.BrokerLicenseRequest;
import com.stubhub.domain.account.intf.BrokerLicenseResponse;
import com.stubhub.platform.utilities.webservice.security.ExtendedSecurityContext;
import org.junit.Assert;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static javax.ws.rs.core.Response.Status.*;
import static org.mockito.Mockito.when;


/**
 * Created by mengli on 11/20/18.
 */
public class BrokerLicenseServiceImplTest {

    private BrokerLicenseServiceImpl brokerLicenseServiceImpl = new BrokerLicenseServiceImpl();
    private SecurityContextUtil securityContextUtil = new SecurityContextUtil();
    private ExtendedSecurityContext securityContext;
    private String sellerGuid = "sellerGuid";
    private String country = "US";
    private String state = "NY";
    private Integer active = 1;
    private long brokerLicenseInfoId = 123456789123L;
    private String brokerLicenseNumber = "abc123efg456";

    @BeforeMethod
    public void setUp(){
        BrokerLicenseDAO brokerLicenseDAO = Mockito.mock(BrokerLicenseDAO.class);
        securityContextUtil = new SecurityContextUtil();

        securityContext = Mockito.mock(ExtendedSecurityContext.class);
        when(securityContext.getUserGuid()).thenReturn("sellerGuid");
        when(securityContext.getUserId()).thenReturn("userId");
        when(securityContext.getUserName()).thenReturn("userName");
        List<BrokerLicense> brokerLicenses = new ArrayList<BrokerLicense>();
        brokerLicenses.add(BrokerLicense.builder().build());
        when(brokerLicenseDAO.getBrokerLicensesBySellerGuid(Mockito.anyString())).thenReturn(brokerLicenses);
        when(brokerLicenseDAO.saveBrokerLicense(Mockito.any(BrokerLicense.class))).thenReturn(brokerLicenseInfoId);
        BrokerLicense brokerLicense = BrokerLicense.builder().brokerLicenseNumber(brokerLicenseNumber).userBrokerLicenseId(brokerLicenseInfoId).active(active).stateCode(state).countryCode(country).build();
        when(brokerLicenseDAO.getBrokerLicense(Mockito.anyLong())).thenReturn(brokerLicense);
        ReflectionTestUtils.setField(brokerLicenseServiceImpl, "brokerLicenseDAO", brokerLicenseDAO);
        ReflectionTestUtils.setField(brokerLicenseServiceImpl, "securityContextUtil", securityContextUtil);
    }

    @Test
    public void testGetBrokerLicenses() {
        Response response = brokerLicenseServiceImpl.getBrokerLicenses(sellerGuid, securityContext);
        Assert.assertNotNull(response);
        Assert.assertEquals(OK.getStatusCode(), response.getStatus());
        Assert.assertNotNull(response.getEntity());
    }

    @Test
    public void testGetBrokerLicensesWithNull() {
        Response response = brokerLicenseServiceImpl.getBrokerLicenses(null, securityContext);
        Assert.assertNotNull(response);
        Assert.assertEquals(BAD_REQUEST.getStatusCode(), response.getStatus());
        BrokerLicenseResponse brokerLicenseResponse = (BrokerLicenseResponse) response.getEntity();
        Assert.assertNotNull(brokerLicenseResponse.getErrors());
        Assert.assertEquals("Seller Guid should not be null or empty", brokerLicenseResponse.getErrors().get(0).getMessage());
    }

    @Test
    public void testCreateBrokerLicense() {
        BrokerLicenseRequest brokerLicenseRequest = BrokerLicenseRequest.builder().active(active).countryCode(country).stateCode(state).brokerLicenseNumber(brokerLicenseNumber).build();
        Response response = brokerLicenseServiceImpl.createBrokerLicense(sellerGuid, brokerLicenseRequest, securityContext);
        Assert.assertEquals(CREATED.getStatusCode(), response.getStatus());
        Assert.assertNotNull(response.getEntity());
        BrokerLicenseSuccessEntity entity = (BrokerLicenseSuccessEntity) response.getEntity();
        Assert.assertEquals(brokerLicenseInfoId, entity.getUserBrokerLicenseId());
    }

    @Test
    public void testCreateBrokerLicenseWithNullSellerGuid() {
        BrokerLicenseRequest brokerLicenseRequest = BrokerLicenseRequest.builder().active(active).countryCode(country).stateCode(state).brokerLicenseNumber(brokerLicenseNumber).build();
        Response response = brokerLicenseServiceImpl.createBrokerLicense(null, brokerLicenseRequest, securityContext);
        Assert.assertEquals(UNAUTHORIZED.getStatusCode(), response.getStatus());
        BrokerLicenseResponse brokerLicenseResponse = (BrokerLicenseResponse) response.getEntity();
        Assert.assertEquals(brokerLicenseResponse.getErrors().get(0).getMessage(), "Invalid Seller");
    }

    @Test
    public void testCreateBrokerLicenseWithNullRequest() {
        Response response = brokerLicenseServiceImpl.createBrokerLicense(sellerGuid, null, securityContext);
        Assert.assertEquals(BAD_REQUEST.getStatusCode(), response.getStatus());
        BrokerLicenseResponse brokerLicenseResponse = (BrokerLicenseResponse) response.getEntity();
        Assert.assertEquals(brokerLicenseResponse.getErrors().get(0).getMessage(), "No request payload found");
    }

    @Test
    public void testCreateBrokerLicenseWithNullParameters() {
        BrokerLicenseRequest brokerLicenseRequest = BrokerLicenseRequest.builder().active(active).countryCode(country).stateCode(state).build();
        Response response = brokerLicenseServiceImpl.createBrokerLicense(sellerGuid, brokerLicenseRequest, securityContext);
        Assert.assertEquals(BAD_REQUEST.getStatusCode(), response.getStatus());
        BrokerLicenseResponse brokerLicenseResponse = (BrokerLicenseResponse) response.getEntity();
        Assert.assertEquals(brokerLicenseResponse.getErrors().get(0).getMessage(), "brokerLicenseNumber can't be null");

        brokerLicenseRequest = BrokerLicenseRequest.builder().active(active).stateCode(state).brokerLicenseNumber(brokerLicenseNumber).build();
        response = brokerLicenseServiceImpl.createBrokerLicense(sellerGuid, brokerLicenseRequest, securityContext);
        Assert.assertEquals(BAD_REQUEST.getStatusCode(), response.getStatus());
        brokerLicenseResponse = (BrokerLicenseResponse) response.getEntity();
        Assert.assertEquals(brokerLicenseResponse.getErrors().get(0).getMessage(), "country can't be null");

        brokerLicenseRequest = BrokerLicenseRequest.builder().countryCode(country).brokerLicenseNumber(brokerLicenseNumber).build();
        response = brokerLicenseServiceImpl.createBrokerLicense(sellerGuid, brokerLicenseRequest, securityContext);
        Assert.assertEquals(BAD_REQUEST.getStatusCode(), response.getStatus());
        brokerLicenseResponse = (BrokerLicenseResponse) response.getEntity();
        Assert.assertEquals(brokerLicenseResponse.getErrors().get(0).getMessage(), "state can't be null");
    }

    @Test
    public void testCreateBrokerLicenseWithTooLongNumber() {
        BrokerLicenseRequest brokerLicenseRequest = BrokerLicenseRequest.builder().active(active).countryCode(country).stateCode(state).brokerLicenseNumber("123qwe123rytu38294030fjgmdko").build();
        Response response = brokerLicenseServiceImpl.createBrokerLicense(sellerGuid, brokerLicenseRequest, securityContext);
        Assert.assertEquals(BAD_REQUEST.getStatusCode(), response.getStatus());
        BrokerLicenseResponse brokerLicenseResponse = (BrokerLicenseResponse) response.getEntity();
        Assert.assertEquals(brokerLicenseResponse.getErrors().get(0).getMessage(), "Invalid broker license number, maximum size is 20");
    }

    @Test
    public void testUpdateBrokerLicense() {
        BrokerLicenseRequest brokerLicenseRequest = BrokerLicenseRequest.builder().active(active).countryCode(country).stateCode(state).brokerLicenseNumber(brokerLicenseNumber).userBrokerLicenseId(brokerLicenseInfoId).build();
        Response response = brokerLicenseServiceImpl.updateBrokerLicense(sellerGuid, brokerLicenseRequest, securityContext);
        Assert.assertEquals(OK.getStatusCode(), response.getStatus());
        Assert.assertNotNull(response.getEntity());
        BrokerLicenseSuccessEntity entity = (BrokerLicenseSuccessEntity) response.getEntity();
        Assert.assertEquals(brokerLicenseInfoId, entity.getUserBrokerLicenseId());
    }

    @Test
    public void testUpdateBrokerLicenseWithNullSellerGuid() {
        BrokerLicenseRequest brokerLicenseRequest = BrokerLicenseRequest.builder().active(active).countryCode(country).stateCode(state).brokerLicenseNumber(brokerLicenseNumber).build();
        Response response = brokerLicenseServiceImpl.updateBrokerLicense(null, brokerLicenseRequest, securityContext);
        Assert.assertEquals(UNAUTHORIZED.getStatusCode(), response.getStatus());
        BrokerLicenseResponse brokerLicenseResponse = (BrokerLicenseResponse) response.getEntity();
        Assert.assertEquals(brokerLicenseResponse.getErrors().get(0).getMessage(), "Invalid Seller");
    }

    @Test
    public void testUpdateBrokerLicenseWithNullRequest() {
        Response response = brokerLicenseServiceImpl.updateBrokerLicense(sellerGuid, null, securityContext);
        Assert.assertEquals(BAD_REQUEST.getStatusCode(), response.getStatus());
        BrokerLicenseResponse brokerLicenseResponse = (BrokerLicenseResponse) response.getEntity();
        Assert.assertEquals(brokerLicenseResponse.getErrors().get(0).getMessage(), "No request payload found");
    }

    @Test
    public void testUpdateBrokerLicenseWithNullParameters() {
        BrokerLicenseRequest brokerLicenseRequest = BrokerLicenseRequest.builder().active(active).countryCode(country).stateCode(state).build();
        Response response = brokerLicenseServiceImpl.updateBrokerLicense(sellerGuid, brokerLicenseRequest, securityContext);
        Assert.assertEquals(BAD_REQUEST.getStatusCode(), response.getStatus());
        BrokerLicenseResponse brokerLicenseResponse = (BrokerLicenseResponse) response.getEntity();
        Assert.assertEquals(brokerLicenseResponse.getErrors().get(0).getMessage(), "brokerLicenseNumber can't be null");

        brokerLicenseRequest = BrokerLicenseRequest.builder().active(active).stateCode(state).brokerLicenseNumber(brokerLicenseNumber).build();
        response = brokerLicenseServiceImpl.updateBrokerLicense(sellerGuid, brokerLicenseRequest, securityContext);
        Assert.assertEquals(BAD_REQUEST.getStatusCode(), response.getStatus());
        brokerLicenseResponse = (BrokerLicenseResponse) response.getEntity();
        Assert.assertEquals(brokerLicenseResponse.getErrors().get(0).getMessage(), "country can't be null");

        brokerLicenseRequest = BrokerLicenseRequest.builder().countryCode(country).brokerLicenseNumber(brokerLicenseNumber).build();
        response = brokerLicenseServiceImpl.updateBrokerLicense(sellerGuid, brokerLicenseRequest, securityContext);
        Assert.assertEquals(BAD_REQUEST.getStatusCode(), response.getStatus());
        brokerLicenseResponse = (BrokerLicenseResponse) response.getEntity();
        Assert.assertEquals(brokerLicenseResponse.getErrors().get(0).getMessage(), "state can't be null");
    }

    @Test
    public void testDeactivateBrokerLicense() {
        BrokerLicenseRequest brokerLicenseRequest = BrokerLicenseRequest.builder().active(0).countryCode(country).stateCode(state).brokerLicenseNumber(brokerLicenseNumber).userBrokerLicenseId(brokerLicenseInfoId).build();
        Response response = brokerLicenseServiceImpl.updateBrokerLicense(sellerGuid, brokerLicenseRequest, securityContext);
        Assert.assertEquals(OK.getStatusCode(), response.getStatus());
        Assert.assertNotNull(response.getEntity());
        BrokerLicenseSuccessEntity entity = (BrokerLicenseSuccessEntity) response.getEntity();
        Assert.assertEquals(brokerLicenseInfoId, entity.getUserBrokerLicenseId());
    }
}
