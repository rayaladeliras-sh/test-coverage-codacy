package com.stubhub.domain.account.datamodel.dao.impl;

import java.util.Calendar;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

import com.stubhub.domain.account.datamodel.dao.SellerPaymentStatusHistDAO;
import com.stubhub.domain.account.datamodel.entity.SellerPaymentStatusHist;

@Component("sellerPaymentStatusHistDAO")
public class SellerPaymentStatusHistDAOImpl implements
		SellerPaymentStatusHistDAO {

	@Autowired
	@Qualifier("accountHibernateTemplate")
	private HibernateTemplate hibernateTemplate;

	@Override
	public int updateEndDate(Long sellerPaymentId) {
		Query query = hibernateTemplate.getSessionFactory().getCurrentSession().getNamedQuery("SellerPaymentStatusHist.updateSellerPmtStatusEndDate");
		query.setParameter("end_date", Calendar.getInstance());
		query.setParameter("last_updated_by", "Payments Shape API");
		query.setParameter("pid", sellerPaymentId);
		return query.executeUpdate();
	}

	@Override
	public void saveSellerPaymentStatusHist(
			SellerPaymentStatusHist sellerPaymentStatusHist) {
		hibernateTemplate.getSessionFactory().getCurrentSession().saveOrUpdate(sellerPaymentStatusHist);
	}

}
