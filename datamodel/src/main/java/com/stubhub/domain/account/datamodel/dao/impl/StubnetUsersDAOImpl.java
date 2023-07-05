package com.stubhub.domain.account.datamodel.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.stubhub.domain.account.datamodel.dao.StubnetUsersDAO;
import com.stubhub.domain.account.datamodel.entity.StubnetUsers;

@Component("stubnetUsersDAO")
public class StubnetUsersDAOImpl implements StubnetUsersDAO{

	@Autowired
	@Qualifier("accountHibernateTemplate")
	private HibernateTemplate hibernateTemplate;

	@Transactional
	public List<StubnetUsers> getStubnetUserByLoginName(String loginName) {
		Query query = hibernateTemplate.getSessionFactory().getCurrentSession().getNamedQuery("stubnetUsers.getUserByLoginName");
		query.setString("arg1", loginName);
		List<StubnetUsers> resultSet = query.list();
		return resultSet;
	}

}
