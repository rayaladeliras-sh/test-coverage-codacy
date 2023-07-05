package com.stubhub.domain.account.biz.impl;

import com.stubhub.domain.account.biz.intf.TaxPayerBO;
import com.stubhub.domain.account.biz.intf.UserContactBiz;
import com.stubhub.domain.account.datamodel.dao.TaxPayerDetailsDAO;
import com.stubhub.domain.account.datamodel.dao.TinDAO;
import com.stubhub.domain.account.datamodel.dao.UserContactDAO;
import com.stubhub.domain.account.datamodel.dao.UserDAO;
import com.stubhub.domain.account.datamodel.entity.TaxPayerDetails;
import com.stubhub.domain.account.datamodel.entity.UserContact;
import com.stubhub.domain.account.datamodel.enums.TaxIdStatus;
import com.stubhub.domain.account.intf.TaxIdShouldShowResponse;
import com.stubhub.domain.account.intf.TaxPayerAlertStatusResponse;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created at 11/7/15 12:10 PM
 *
 * @author : Caron Zhao
 * @version : 1.0.0
 */
public class TaxPayerBOImplTest {
    private TaxPayerBO taxPayerBO;

    @Mock
    private TaxPayerDetailsDAO taxPayerDetailsDAO;

    @Mock
    private UserDAO userDAO;

    @Mock
    private UserContactBiz userContactBiz;

    @Mock
    private UserContactDAO userContactDAO;

    @BeforeTest
    public void setUp() {
        taxPayerBO = new TaxPayerBOImpl();
        taxPayerDetailsDAO = mock(TaxPayerDetailsDAO.class);
        userDAO = mock(UserDAO.class);
        userContactBiz = mock(UserContactBiz.class);
        userContactDAO = mock(UserContactDAO.class);
        ReflectionTestUtils.setField(taxPayerBO, "taxPayerDetailsDAO", taxPayerDetailsDAO);
        ReflectionTestUtils.setField(taxPayerBO, "userContactDAO", userContactDAO);
    }

    @Test
    public void testCalculateShowTaxPayerAlert_ShowEmpty_TINNotNeeded() {
        String userId = "1234";
        TaxPayerDetails taxPayerDetails = mock(TaxPayerDetails.class);
        when(taxPayerDetails.getTaxIdStatus()).thenReturn(Integer.valueOf(TaxIdStatus.TIN_NOT_NEEDED.getId()));
        when(taxPayerDetailsDAO.getTaxPayerDetailsByUserId(Long.valueOf(userId))).thenReturn(taxPayerDetails);
        TaxPayerAlertStatusResponse result = taxPayerBO.calculateShowTaxPayerAlert(userId);
        assertNotNull(result);
        assertNull(result.getAlert());
    }

    @Test
    public void testCalculateShowTaxPayerAlert_ShowEmpty_YearReset() {
        String userId = "1234";
        TaxPayerDetails taxPayerDetails = mock(TaxPayerDetails.class);
        when(taxPayerDetails.getTaxIdStatus()).thenReturn(Integer.valueOf(TaxIdStatus.TAX_YEAR_RESET.getId()));
        when(taxPayerDetailsDAO.getTaxPayerDetailsByUserId(Long.valueOf(userId))).thenReturn(taxPayerDetails);
        TaxPayerAlertStatusResponse result = taxPayerBO.calculateShowTaxPayerAlert(userId);
        assertNotNull(result);
        assertNull(result.getAlert());
    }

    @Test
    public void testCalculateShowTaxPayerAlert_ShowAlert_TINNeeded() {
        String userId = "1234";
        TaxPayerDetails taxPayerDetails = mock(TaxPayerDetails.class);
        when(taxPayerDetails.getTaxIdStatus()).thenReturn(Integer.valueOf(TaxIdStatus.TIN_NEEDED.getId()));
        when(taxPayerDetailsDAO.getTaxPayerDetailsByUserId(Long.valueOf(userId))).thenReturn(taxPayerDetails);
        TaxPayerAlertStatusResponse result = taxPayerBO.calculateShowTaxPayerAlert(userId);
        assertNotNull(result);
        assertEquals(Boolean.TRUE, result.getAlert());
    }

    @Test
    public void testCalculateShowTaxPayerAlert_ShowAlert_TINRequired() {
        String userId = "1234";
        TaxPayerDetails taxPayerDetails = mock(TaxPayerDetails.class);
        when(taxPayerDetails.getTaxIdStatus()).thenReturn(Integer.valueOf(TaxIdStatus.TIN_REQUIRED.getId()));
        when(taxPayerDetailsDAO.getTaxPayerDetailsByUserId(Long.valueOf(userId))).thenReturn(taxPayerDetails);
        TaxPayerAlertStatusResponse result = taxPayerBO.calculateShowTaxPayerAlert(userId);
        assertNotNull(result);
        assertEquals(Boolean.TRUE, result.getAlert());
    }

    @Test
    public void testCalculateShowTaxPayerAlert_ShowAlert_TINUnblocked() {
        String userId = "1234";
        TaxPayerDetails taxPayerDetails = mock(TaxPayerDetails.class);
        when(taxPayerDetails.getTaxIdStatus()).thenReturn(Integer.valueOf(TaxIdStatus.TIN_UNBLOCKED.getId()));
        when(taxPayerDetailsDAO.getTaxPayerDetailsByUserId(Long.valueOf(userId))).thenReturn(taxPayerDetails);
        TaxPayerAlertStatusResponse result = taxPayerBO.calculateShowTaxPayerAlert(userId);
        assertNotNull(result);
        assertEquals(Boolean.TRUE, result.getAlert());
    }

    @Test
    public void testCalculateShowTaxPayerAlert_NotShowAlert_TINExist() {
        String userId = "1234";
        String tinGuid = "tinGuid";
        TaxPayerDetails taxPayerDetails = mock(TaxPayerDetails.class);
        when(taxPayerDetails.getTinGuid()).thenReturn(tinGuid);
        when(taxPayerDetails.getTaxIdStatus()).thenReturn(Integer.valueOf(TaxIdStatus.TIN_COLLECTED.getId()));
        when(taxPayerDetailsDAO.getTaxPayerDetailsByUserId(Long.valueOf(userId))).thenReturn(taxPayerDetails);
        TaxPayerAlertStatusResponse result = taxPayerBO.calculateShowTaxPayerAlert(userId);
        assertNotNull(result);
        assertEquals(Boolean.FALSE, result.getAlert());
    }

    @Test
    public void testIsTaxIdNeeded_USContact_ValidateNotExistFalse() {
        String userId = "1234";
        UserContact defaultUserContact = mock(UserContact.class);
        when(defaultUserContact.getCountry()).thenReturn("US");
        when(userContactDAO.getDefaultUserContactByOwnerId(Long.valueOf(userId))).thenReturn(defaultUserContact);
        TaxIdShouldShowResponse taxIdShouldShowResponse = taxPayerBO.isTaxIdNeeded(userId, false);
        assertNotNull(taxIdShouldShowResponse);
        assertTrue(taxIdShouldShowResponse.isShouldShow());
    }


    @Test
    public void testIsTaxIdNeeded_NotUSContact_ValidateNotExistFalse() {
        String userId = "1234";
        UserContact defaultUserContact = mock(UserContact.class);
        when(defaultUserContact.getCountry()).thenReturn("GB");
        when(userContactDAO.getDefaultUserContactByOwnerId(Long.valueOf(userId))).thenReturn(defaultUserContact);
        TaxIdShouldShowResponse taxIdShouldShowResponse = taxPayerBO.isTaxIdNeeded(userId, false);
        assertNotNull(taxIdShouldShowResponse);
        assertFalse(taxIdShouldShowResponse.isShouldShow());
    }

    @Test
    public void testIsTaxIdNeeded_USContact_ValidateNotExistTrueAndExist() {
        String userId = "1234";
        UserContact defaultUserContact = mock(UserContact.class);
        when(defaultUserContact.getCountry()).thenReturn("US");
        TaxPayerDetails taxPayerDetails = mock(TaxPayerDetails.class);
        when(userContactDAO.getDefaultUserContactByOwnerId(Long.valueOf(userId))).thenReturn(defaultUserContact);
        when(taxPayerDetailsDAO.getTaxPayerDetailsByUserId(Long.valueOf(userId))).thenReturn(taxPayerDetails);
        TaxIdShouldShowResponse taxIdShouldShowResponse = taxPayerBO.isTaxIdNeeded(userId, true);
        assertNotNull(taxIdShouldShowResponse);
        assertFalse(taxIdShouldShowResponse.isShouldShow());
    }


    @Test
    public void testIsTaxIdNeeded_USContact_ValidateNotExistTrueAndNotExist() {
        String userId = "1234";
        UserContact defaultUserContact = mock(UserContact.class);
        when(defaultUserContact.getCountry()).thenReturn("US");
        when(userContactDAO.getDefaultUserContactByOwnerId(Long.valueOf(userId))).thenReturn(defaultUserContact);
        when(taxPayerDetailsDAO.getTaxPayerDetailsByUserId(Long.valueOf(userId))).thenReturn(null);
        TaxIdShouldShowResponse taxIdShouldShowResponse = taxPayerBO.isTaxIdNeeded(userId, true);
        assertNotNull(taxIdShouldShowResponse);
        assertTrue(taxIdShouldShowResponse.isShouldShow());
    }
}