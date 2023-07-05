package com.stubhub.domain.account.datamodel.dao.impl;

import java.util.List;

import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.stubhub.domain.account.datamodel.dao.ListingSeatTraitDAO;
import com.stubhub.domain.account.datamodel.entity.ListingSeatTrait;

@Component("listingSeatTraitDAO")
public class ListingSeatTraitDAOImpl implements ListingSeatTraitDAO {

	@Autowired
	@Qualifier("accountHibernateTemplate")
	private HibernateTemplate hibernateTemplate;

	@Transactional
	public List<ListingSeatTrait> getByListingId(Long ListingId) {
		List<ListingSeatTrait> result = null;
		SQLQuery sqlQuery = (SQLQuery)this.hibernateTemplate.getSessionFactory().getCurrentSession().getNamedQuery("ListingSeatTrait.getByListingId");
		sqlQuery.setLong("ticketId", ListingId);
		result = sqlQuery.list();
		return result;
	}
}
