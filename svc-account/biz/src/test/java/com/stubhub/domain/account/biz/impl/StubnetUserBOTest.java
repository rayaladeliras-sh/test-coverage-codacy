package com.stubhub.domain.account.biz.impl;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.stubhub.common.exception.InvalidArgumentException;
import com.stubhub.common.exception.RecordNotFoundForIdException;
import com.stubhub.domain.account.biz.intf.StubnetUserBO;
import com.stubhub.domain.account.datamodel.dao.StubnetUsersDAO;
import com.stubhub.domain.account.datamodel.entity.StubnetUsers;

public class StubnetUserBOTest {
	private StubnetUserBO stubnetUserBO;
	private StubnetUsersDAO stubnetUsersDAO;
	private Long l1 = 200L;

	@BeforeTest
	public void setUp() {
		stubnetUserBO = new StubnetUserBOImpl();
		stubnetUsersDAO = Mockito.mock(StubnetUsersDAO.class);
		ReflectionTestUtils.setField(stubnetUserBO, "stubnetUsersDAO", stubnetUsersDAO);
	}

	@Test
	public void testStubnetUserBO() throws RecordNotFoundForIdException, InvalidArgumentException{
		List<StubnetUsers> userList = new ArrayList<StubnetUsers>();
		StubnetUsers user = new StubnetUsers();
		user.setActive(true);
		user.setId(l1);
		user.setNameLogin("bijain");
		userList.add(user);
		
		Mockito.when(stubnetUsersDAO.getStubnetUserByLoginName(Mockito.anyString())).thenReturn(userList);
		Long response = stubnetUserBO.isStubnetUser("bijain");
		Assert.assertNotNull(response);
	}
}

