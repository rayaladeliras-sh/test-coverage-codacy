package com.stubhub.domain.account.datamodel.dao.impl;

import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

import com.stubhub.common.exception.RecordNotFoundForIdException;
import com.stubhub.domain.account.datamodel.dao.InventoryDAO;
import com.stubhub.domain.account.datamodel.entity.InventoryData;
import com.stubhub.domain.account.datamodel.entity.TTAPIintegration;

@Component("inventoryDAO")
public class InventoryDAOImpl implements InventoryDAO{
	

	@Autowired
	@Qualifier("accountHibernateTemplate")
	private HibernateTemplate hibernateTemplate;

	@Override
	public InventoryData getInventoryDataById(Long orderId) throws RecordNotFoundForIdException {
		
		// TODO Auto-generated method stub
		InventoryData result = null;
		SQLQuery sqlQuery = (SQLQuery)hibernateTemplate.getSessionFactory().getCurrentSession().getNamedQuery("InventoryData.getSellerInfo");
		sqlQuery.setLong("orderId", orderId.longValue());
		result =(InventoryData) sqlQuery.uniqueResult();
		if(result != null)
		{
			return result ;
		}
		else
		{
			throw new RecordNotFoundForIdException("orderId", orderId.longValue());
		}
		
	}

	@Override
	public String getFulFillmentType(Long methodId) throws RecordNotFoundForIdException {
		// TODO Auto-generated method stub
		SQLQuery sqlQuery = (SQLQuery)hibernateTemplate.getSessionFactory().getCurrentSession().createSQLQuery("select FULFILLMENT_TYPE from FULFILLMENT_TYPE ft,"
				+ " Fulfillment_method fm "
				+ "where ft.FULFILLMENT_TYPE_ID = fm.FULFILLMENT_TYPE_ID "
				+ "and fm.FULFILLMENT_METHOD_ID = :methodId");
		sqlQuery.setLong("methodId", methodId.longValue());
		String result =(String) sqlQuery.uniqueResult();
		if(result != null)
		{
			return result ;
		}
		else
		{
			throw new RecordNotFoundForIdException("methodId", methodId.longValue());
		}
	}
	
	
	
	@Override
	public TTAPIintegration getTTProperties() throws RecordNotFoundForIdException {
		// TODO Auto-generated method stub
		TTAPIintegration apiInt = new TTAPIintegration();
		String QUERY_SECURE_PROPUSER = "select gen3_secure_tools.decryptsecurepropertyname(property_value) from secure_stub_property where property_name='318985.username' ";
		String QUERY_SECURE_PROPPASS = "select gen3_secure_tools.decryptsecurepropertyname(property_value) from secure_stub_property where property_name='318985.password' ";		
		
		SQLQuery sqlQuery = (SQLQuery)hibernateTemplate.getSessionFactory().getCurrentSession().createSQLQuery(QUERY_SECURE_PROPUSER);
		String result =(String) sqlQuery.uniqueResult();
		apiInt.setUsername(result);
		SQLQuery sqlQueryTT = (SQLQuery)hibernateTemplate.getSessionFactory().getCurrentSession().createSQLQuery(QUERY_SECURE_PROPPASS);
		String ttResult =(String) sqlQueryTT.uniqueResult();
		apiInt.setPass(ttResult);
		return apiInt;
	}

}
