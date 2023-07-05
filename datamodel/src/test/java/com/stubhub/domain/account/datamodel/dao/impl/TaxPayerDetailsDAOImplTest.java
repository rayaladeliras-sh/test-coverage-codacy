package com.stubhub.domain.account.datamodel.dao.impl;

import com.stubhub.domain.account.datamodel.dao.TaxPayerDetailsDAO;
import com.stubhub.domain.account.datamodel.entity.TaxPayerDetails;

import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TaxPayerDetailsDAOImplTest {
  private TaxPayerDetailsDAO taxPayerDetailsDAO;

  @Mock
  private HibernateTemplate hibernateTemplate;
  @Mock
  private SessionFactory sessionFactory;
  @Mock
  private Session currentSession;
	@Mock
  private SQLQuery sqlQuery;

  @BeforeMethod
  public void setUp() {
    taxPayerDetailsDAO = new TaxPayerDetailsDAOImpl();
    hibernateTemplate = mock(HibernateTemplate.class);
    sessionFactory = mock(SessionFactory.class);
    currentSession = mock(Session.class);
    sqlQuery = mock(SQLQuery.class);

    ReflectionTestUtils.setField(taxPayerDetailsDAO, "hibernateTemplate", hibernateTemplate);

    when(hibernateTemplate.getSessionFactory()).thenReturn(sessionFactory);
    when(sessionFactory.getCurrentSession()).thenReturn(currentSession);
    when(currentSession.getNamedQuery(Mockito.anyString())).thenReturn(sqlQuery);
  }

  @Test
  public void testGetTaxPayerDetailsByUserId() {
    TaxPayerDetails taxPayerDetails = new TaxPayerDetails();
    when(sqlQuery.uniqueResult()).thenReturn(taxPayerDetails);
    TaxPayerDetails newTaxPayerDetails = taxPayerDetailsDAO.getTaxPayerDetailsByUserId(1L);
    assertNotNull(newTaxPayerDetails);
  }

  @Test
  public void testGetTaxPayerDetailsByUserIdNull() {
    Long userId = null;
    TaxPayerDetails newTaxPayerDetails = taxPayerDetailsDAO.getTaxPayerDetailsByUserId(userId);
    assertNull(newTaxPayerDetails);
  }

  @Test
  public void testGetTaxPayerDetailsNullObj() {
    when(sqlQuery.uniqueResult()).thenReturn(null);
    TaxPayerDetails newTaxPayerDetails = taxPayerDetailsDAO.getTaxPayerDetailsByUserId(1L);
    assertNull(newTaxPayerDetails);
  }

  @Test
  public void testUpdateTaxPayerDetails() {
    when(sqlQuery.executeUpdate()).thenReturn(1);
    int count = taxPayerDetailsDAO.updateTaxPayerDetails(getTaxPayerDetails(), true);
    assertEquals(1, count);
  }

  @Test
  public void testUpdateTaxPayerDetailsAddFalse() {
    when(sqlQuery.executeUpdate()).thenReturn(1);
    int count = taxPayerDetailsDAO.updateTaxPayerDetails(getTaxPayerDetails(), false);
    assertEquals(1, count);
  }

  @Test
  public void testUpdateTaxPayerDetailsParamNull() {
    when(sqlQuery.executeUpdate()).thenReturn(0);
    int count = taxPayerDetailsDAO.updateTaxPayerDetails(null, true);
    assertEquals(0, count);
  }

  @Test
  public void testGetTaxPayerDetailsByUserGuid() {
    String userGuid = "guid";
    String sqlQuery = "SELECT * FROM TAXPAYER WHERE user_id in(SELECT ID FROM USERS WHERE user_cookie_guid=:userGuid)";
    SQLQuery query = mock(SQLQuery.class);
    when(currentSession.createSQLQuery(sqlQuery)).thenReturn(query);
    when(query.addEntity(TaxPayerDetails.class)).thenReturn(query);
    TaxPayerDetails expected = new TaxPayerDetails();
    when(query.uniqueResult()).thenReturn(expected);
    TaxPayerDetails result = taxPayerDetailsDAO.getTaxPayerDetailsByUserGuid(userGuid);
    assertNotNull(result);
    assertEquals(expected, result);
  }

  @Test
  public void testGetTaxPayerDetailsByUserGuid_NotFound() {
    String userGuid = "guid";
    String sqlQuery = "SELECT * FROM TAXPAYER WHERE user_id in(SELECT ID FROM USERS WHERE user_cookie_guid=:userGuid)";
    SQLQuery query = mock(SQLQuery.class);
    when(currentSession.createSQLQuery(sqlQuery)).thenReturn(query);
    when(query.addEntity(TaxPayerDetails.class)).thenReturn(query);
    when(query.uniqueResult()).thenReturn(null);
    TaxPayerDetails result = taxPayerDetailsDAO.getTaxPayerDetailsByUserGuid(userGuid);
    assertNull(result);
  }

  @Test
  public void testAddTaxPayerDetails_TaxPayerNull() {
    long taxPayerDetailsId = taxPayerDetailsDAO.addTaxPayerDetails(null);
    assertEquals(0, taxPayerDetailsId);
  }

  private TaxPayerDetails getTaxPayerDetails () {
    TaxPayerDetails taxPayerDetails = new TaxPayerDetails();

    taxPayerDetails.setTaxIdStatus(1);
    taxPayerDetails.setTinType("tinType");
    taxPayerDetails.setUserId(1L);

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
    taxPayerDetails.setTaxCountry("taxCountryCode");
    taxPayerDetails.setAddrCountry("countryCallingCode");
    return taxPayerDetails;
  }

}