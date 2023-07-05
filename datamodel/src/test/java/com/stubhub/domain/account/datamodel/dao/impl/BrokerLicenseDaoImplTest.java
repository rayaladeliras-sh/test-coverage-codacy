package com.stubhub.domain.account.datamodel.dao.impl;

import com.stubhub.domain.account.datamodel.entity.BrokerLicense;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by mengli on 11/20/18.
 */
public class BrokerLicenseDaoImplTest {
    private BrokerLicenseDAOImpl brokerLicenseDAOImpl;

    @Mock private HibernateTemplate hibernateTemplate;
    @Mock private SessionFactory sessionFactory;
    @Mock private Session currentSession;
    @Mock private SQLQuery sqlQuery;

    @BeforeMethod
    public void setUp(){
        brokerLicenseDAOImpl = new BrokerLicenseDAOImpl();
        hibernateTemplate = mock(HibernateTemplate.class);
        sessionFactory = mock(SessionFactory.class);
        currentSession = mock(Session.class);
        sqlQuery = mock(SQLQuery.class);

        ReflectionTestUtils.setField(brokerLicenseDAOImpl, "hibernateTemplate", hibernateTemplate);

        when(hibernateTemplate.getSessionFactory()).thenReturn(sessionFactory);
        when(sessionFactory.getCurrentSession()).thenReturn(currentSession);
        when(currentSession.getNamedQuery(Mockito.anyString())).thenReturn(sqlQuery);
        when(currentSession.save(Mockito.any(BrokerLicense.class))).thenReturn(123456789123L);
    }

    @Test
    public void testGetBrokeLicense() {
        BrokerLicense brokerLicense = new BrokerLicense();
        when(sqlQuery.uniqueResult()).thenReturn(brokerLicense);
        BrokerLicense newBrokerLicense = brokerLicenseDAOImpl.getBrokerLicense(123456789123L);
        Assert.assertNotNull(newBrokerLicense);
    }

    @Test
    public void testGetBrokeLicensesBySellerGuid() {
        BrokerLicense brokerLicense = new BrokerLicense();
        List<BrokerLicense> brokerLicenses = new ArrayList<BrokerLicense>();
        brokerLicenses.add(brokerLicense);
        when(sqlQuery.list()).thenReturn(brokerLicenses);
        List<BrokerLicense> newBrokerLicenses = brokerLicenseDAOImpl.getBrokerLicensesBySellerGuid("sellerGuid");
        Assert.assertNotNull(newBrokerLicenses);
        Assert.assertTrue(newBrokerLicenses.size() > 0);
    }

    @Test
    public void testSaveBrokerLicense() {
        BrokerLicense brokerLicense = BrokerLicense.builder().brokerLicenseNumber("7483937493").build();
        long brokerLicenseInfoId = brokerLicenseDAOImpl.saveBrokerLicense(brokerLicense);
        Assert.assertEquals(brokerLicenseInfoId, 123456789123L);
    }

    @Test
    public void testSaveNullBrokerLicense() {
        long brokerLicenseInfoId = brokerLicenseDAOImpl.saveBrokerLicense(null);
        Assert.assertEquals(brokerLicenseInfoId, 0);
    }

    @Test
    public void testSaveBrokerLicenseWithTooLongNumber() {
        BrokerLicense brokerLicense = BrokerLicense.builder().brokerLicenseNumber("7483937493729137fhw837953gdj").build();
        long brokerLicenseInfoId = brokerLicenseDAOImpl.saveBrokerLicense(brokerLicense);
        Assert.assertEquals(brokerLicenseInfoId, 0);
    }

    @Test
    public void testUpdateBrokerLicense() {
        BrokerLicense brokerLicense = new BrokerLicense();
        when(sqlQuery.uniqueResult()).thenReturn(brokerLicense);
        int result = brokerLicenseDAOImpl.updateBrokerLicenseBySellerGuid("sellerGuid", "abc123", "NY", "US", 1, 123456789123L);
        Assert.assertEquals(result, 1);
    }

    @Test
    public void testUpdateBrokerLicenseWithEmptySellerGuid() {
        int result = brokerLicenseDAOImpl.updateBrokerLicenseBySellerGuid("", "abc123", "NY", "US", 1, 123456789123L);
        Assert.assertEquals(result, 0);
    }

    @Test
    public void testUpdateBrokerLicenseWithTooLongNumber() {
        when(sqlQuery.executeUpdate()).thenReturn(1);
        int result = brokerLicenseDAOImpl.updateBrokerLicenseBySellerGuid("sellerGuid", "7483937493729137fhw837953gdj", "NY", "US", 1, 123456789123L);
        Assert.assertEquals(result, 0);
    }
}
