package com.stubhub.domain.account.biz.impl;

import java.util.ArrayList;
import java.util.List;

import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.stubhub.domain.account.biz.intf.UpsTrackingBO;
import com.stubhub.domain.account.datamodel.dao.UpsTrackingDAO;
import com.stubhub.domain.account.datamodel.dao.impl.UpsTrackingDAOImpl;
import com.stubhub.domain.account.datamodel.entity.UpsTracking;

public class UpsTrackingBOImplTest {

	private UpsTrackingBO upsTrackingBO;
	private UpsTrackingDAO upsTrackingDAO;
	private Long l1 = 200L;

	@BeforeTest
	public void setUp() {
		upsTrackingBO = new UpsTrackingBOImpl();
		upsTrackingDAO = Mockito.mock(UpsTrackingDAOImpl.class);
		ReflectionTestUtils.setField(upsTrackingBO, "upsTrackingDAO", upsTrackingDAO);
	}

	@Test
	public void testCheckUPSOrder(){
		List<UpsTracking> list = new ArrayList<UpsTracking>();
		UpsTracking upsTracking = new UpsTracking();
		upsTracking.setOrderId(l1);
		list.add(upsTracking);

		Mockito.when(upsTrackingDAO.checkUPSOrder(l1)).thenReturn(list);
		Assert.assertNotNull(upsTrackingBO.checkUPSOrder(l1));
	}
}




