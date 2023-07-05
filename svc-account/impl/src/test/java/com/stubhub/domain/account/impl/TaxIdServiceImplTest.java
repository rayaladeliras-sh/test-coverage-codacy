package com.stubhub.domain.account.impl;

import com.stubhub.common.exception.UserNotAuthorizedException;
import com.stubhub.domain.account.biz.intf.TaxPayerBO;
import com.stubhub.domain.account.common.Error;
import com.stubhub.domain.account.common.enums.ErrorCode;
import com.stubhub.domain.account.common.enums.ErrorType;
import com.stubhub.domain.account.common.util.SecurityContextUtil;
import com.stubhub.domain.account.datamodel.dao.TaxPayerDetailsDAO;
import com.stubhub.domain.account.datamodel.dao.TinDAO;
import com.stubhub.domain.account.datamodel.entity.TaxPayerDetails;
import com.stubhub.domain.account.datamodel.entity.Tin;
import com.stubhub.domain.account.helper.CustomerContactHelper;
import com.stubhub.domain.account.intf.*;
import com.stubhub.domain.account.common.util.RSAEncryptionUtil;
import com.stubhub.domain.infrastructure.common.exception.base.SHSystemException;
import com.stubhub.domain.infrastructure.common.exception.derived.SHBadRequestException;
import com.stubhub.domain.infrastructure.common.exception.derived.SHResourceNotFoundException;
import com.stubhub.domain.user.contactsV2.intf.CustomerContactV2Details;
import com.stubhub.platform.utilities.webservice.security.ExtendedSecurityContext;
import com.stubhub.platform.utilities.webservice.svclocator.SvcLocator;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.ws.rs.core.Response;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TaxIdServiceImplTest {

	private SvcLocator svcLocator;
	private TinDAO tinDAO;
	private TaxPayerDetailsDAO taxPayerDetailsDAO;
	private ExtendedSecurityContext securityContext;
	private CustomerContactHelper customerContactHelper;
	private TaxIdServiceImpl taxIdServiceImpl;
	
	@Mock
	private RSAEncryptionUtil encryptionUtil;
	
	@Mock
	private TaxPayerBO taxPayerBO;

	@Mock
	private SecurityContextUtil securityContextUtil;

	@BeforeMethod
	public void setUp(){
		svcLocator = Mockito.mock(SvcLocator.class);
		customerContactHelper = Mockito.mock(CustomerContactHelper.class);
		tinDAO = Mockito.mock(TinDAO.class);

		taxPayerDetailsDAO = Mockito.mock(TaxPayerDetailsDAO.class);
		when(tinDAO.getTinByGuid(Mockito.anyString())).thenReturn(getTinDAO());
		when(taxPayerDetailsDAO.getTaxPayerDetailsByUserId(Mockito.anyLong())).thenReturn(getTaxPayerDetails());
						
		securityContext = Mockito.mock(ExtendedSecurityContext.class);
		when(securityContext.getUserId()).thenReturn("12345");
		when(securityContext.getUserGuid()).thenReturn("AB12345");
		when(securityContext.getUserName()).thenReturn("name");

		securityContextUtil = new SecurityContextUtil();
		
		taxIdServiceImpl = new TaxIdServiceImpl() {
			protected byte[] encryptTin(String taxIdNumber) {
				byte[] encryptedTin = {'A', 'B', 'C', 'D'};
				return encryptedTin;
			}
		};
		ReflectionTestUtils.setField(taxIdServiceImpl, "svcLocator", svcLocator);
		ReflectionTestUtils.setField(taxIdServiceImpl, "customerContactHelper", customerContactHelper);
		ReflectionTestUtils.setField(taxIdServiceImpl, "tinDAO", tinDAO);
		ReflectionTestUtils.setField(taxIdServiceImpl, "taxPayerDetailsDAO", taxPayerDetailsDAO);
		ReflectionTestUtils.setField(taxIdServiceImpl, "securityContextUtil", securityContextUtil);
		try {
			Mockito.when(customerContactHelper.getCustomerContactDetails("AB12345", "100")).thenReturn(getCustomerContactV2());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		taxPayerBO = mock(TaxPayerBO.class);
		ReflectionTestUtils.setField(taxIdServiceImpl, "taxPayerBO", taxPayerBO);
	}

	@Test
	public void testUpdateTaxId_PayloadNull() {
		Response response = taxIdServiceImpl.updateTaxId(null, securityContext);
		assertNotNull(response);
		Error error = ((TaxIdResponse) response.getEntity()).getErrors().get(0);
		assertTrue(ErrorType.INPUTERROR.equals(error.getType()) && ErrorCode.INVALID_INPUT.equals(error.getCode()));
	}

	@Test
	public void testUpdateTaxId_TaxIdEmpty() {
		TaxIdRequest taxIdRequest = new TaxIdRequest();
		taxIdRequest.setTaxId("");
		Response response = taxIdServiceImpl.updateTaxId(taxIdRequest, securityContext);
		assertNotNull(response);
		Error error = ((TaxIdResponse) response.getEntity()).getErrors().get(0);
		assertTrue(ErrorType.INPUTERROR.equals(error.getType()) && ErrorCode.INVALID_INPUT.equals(error.getCode()));
	}

	@Test
	public void testUpdateTaxId_TaxIdLengthIncorrect() {
		TaxIdRequest taxIdRequest = new TaxIdRequest();
		taxIdRequest.setTaxId("123");
		Response response = taxIdServiceImpl.updateTaxId(taxIdRequest, securityContext);
		assertNotNull(response);
		Error error = ((TaxIdResponse) response.getEntity()).getErrors().get(0);
		assertTrue(ErrorType.INPUTERROR.equals(error.getType()) && ErrorCode.INVALID_INPUT.equals(error.getCode()));
	}

	@Test
	public void testGetTaxPayerAlertStatus_ShowAlert() throws UserNotAuthorizedException {
		String sellerGuid = "AB12345";
		TaxPayerAlertStatusResponse taxPayerAlertStatusResponse = new TaxPayerAlertStatusResponse();
		taxPayerAlertStatusResponse.setAlert(Boolean.TRUE);
		when(taxPayerBO.calculateShowTaxPayerAlert("12345")).thenReturn(taxPayerAlertStatusResponse);
		TaxPayerAlertStatusResponse response = taxIdServiceImpl.getTaxPayerAlertStatus(sellerGuid, securityContext);
		assertNotNull(response);
		assertEquals(Boolean.TRUE, response.getAlert());
	}

	@Test
	public void testGetTaxPayerAlertStatus_NoShowAlert() {
		String sellerGuid = "AB12345";
		TaxPayerAlertStatusResponse taxPayerAlertStatusResponse = new TaxPayerAlertStatusResponse();
		taxPayerAlertStatusResponse.setAlert(Boolean.FALSE);
		when(taxPayerBO.calculateShowTaxPayerAlert("12345")).thenReturn(taxPayerAlertStatusResponse);
		TaxPayerAlertStatusResponse response = taxIdServiceImpl.getTaxPayerAlertStatus(sellerGuid, securityContext);
		assertNotNull(response);
		assertEquals(Boolean.FALSE, response.getAlert());
	}

	@Test
	public void testGetTaxPayerAlertStatus_ShowEmpty() {
		String sellerGuid = "B5D14E323CD55E9FE04400144F8AE084";
		when(taxPayerBO.calculateShowTaxPayerAlert(sellerGuid)).thenReturn(null);
		TaxPayerAlertStatusResponse response = taxIdServiceImpl.getTaxPayerAlertStatus(sellerGuid, securityContext);
		assertNotNull(response);
		assertNull(response.getAlert());
	}

	@Test
	public void testGetTaxPayerAlertStatus_NotAuthorized() {
		String sellerGuid = "sellerGuid";
		TaxPayerAlertStatusResponse response = taxIdServiceImpl.getTaxPayerAlertStatus(sellerGuid, securityContext);
		assertNotNull(response);
		assertTrue(response.getErrors().size() > 0);
	}

	@Test
	public void testAddTaxId_PayloadNull() {
		Response response = taxIdServiceImpl.addTaxId(null, securityContext);
		assertNotNull(response);
		Error error = ((TaxIdResponse) response.getEntity()).getErrors().get(0);
		assertTrue(ErrorType.INPUTERROR.equals(error.getType()) && ErrorCode.INVALID_INPUT.equals(error.getCode()));
	}

	@Test
	public void testAddTaxId_TaxIdEmpty() {
		TaxIdRequest taxIdRequest = new TaxIdRequest();
		Response response = taxIdServiceImpl.addTaxId(taxIdRequest, securityContext);
		assertNotNull(response);
		Error error = ((TaxIdResponse) response.getEntity()).getErrors().get(0);
		assertTrue(ErrorType.INPUTERROR.equals(error.getType()) && ErrorCode.INVALID_INPUT.equals(error.getCode()));
	}

	@Test
	public void testAddTaxId_TaxIdLengthIncorrect() {
		TaxIdRequest taxIdRequest = new TaxIdRequest();
		taxIdRequest.setTaxId("123");
		Response response = taxIdServiceImpl.addTaxId(taxIdRequest, securityContext);
		assertNotNull(response);
		Error error = ((TaxIdResponse) response.getEntity()).getErrors().get(0);
		assertTrue(ErrorType.INPUTERROR.equals(error.getType()) && ErrorCode.INVALID_INPUT.equals(error.getCode()));
	}

	private Tin getTinDAO() {
		Tin tin = new Tin();
		
		tin.setTin("tin");
		tin.setTinGuid("tinGuid");
		tin.setLastUpdatedBy("lastUpdatedBy");
		
		return tin;
	}
	
	private TaxPayerDetails getTaxPayerDetails() {
		TaxPayerDetails taxPayerDetails = new TaxPayerDetails();
		
		taxPayerDetails.setTaxpayerId(100L);
		taxPayerDetails.setUserId(200L);
		taxPayerDetails.setTaxIdStatus(7);
		taxPayerDetails.setTinGuid("tinGuid");
		taxPayerDetails.setTinType("tinType");
		taxPayerDetails.setTaxPayerNotes("taxPayerNotes");
		taxPayerDetails.setNameFirst("nameFirst");
		taxPayerDetails.setNameLast("nameLast");
		taxPayerDetails.setCompany("company");
		taxPayerDetails.setAddr1("addr1");
		taxPayerDetails.setAddr2("addr2");
		taxPayerDetails.setAddrCity("addrCity");
		taxPayerDetails.setAddrState("addrState"); 
		taxPayerDetails.setAddrZip("addrZip");
		taxPayerDetails.setPhone1("phone1");
		taxPayerDetails.setAddrCountry("addrCountry");
		taxPayerDetails.setActive(1);
		taxPayerDetails.setTaxCountry("taxCountry");
		taxPayerDetails.setCountryCallingCode("countryCallingCode");
		taxPayerDetails.setCreatedBy("createdBy");
		taxPayerDetails.setLastUpdatedBy("lastUpdatedBy");
		
		return taxPayerDetails;
	}

	
	private CustomerContactV2Details getCustomerContactV2() {
		CustomerContactV2Details customerContactDetails = new CustomerContactV2Details();

		customerContactDetails.setCompanyName("Company Name");
		customerContactDetails.setPhoneNumber("Phone Number");
		customerContactDetails.setPhoneCallingCode("Calling Code");
		
		customerContactDetails.setName(new CustomerContactV2Details.Name());
		customerContactDetails.getName().setFirstName("First Name");
		customerContactDetails.getName().setLastName("Last Name");
		
		customerContactDetails.setAddress(new CustomerContactV2Details.Address());
		customerContactDetails.getAddress().setLine1("Address Line 1");
		customerContactDetails.getAddress().setLine2("Address Line 2");
		customerContactDetails.getAddress().setCity("City");
		customerContactDetails.getAddress().setState("State");
		customerContactDetails.getAddress().setCountry("Country");
		customerContactDetails.getAddress().setZipOrPostalCode("ZipCode");

		return customerContactDetails;
	}
}
