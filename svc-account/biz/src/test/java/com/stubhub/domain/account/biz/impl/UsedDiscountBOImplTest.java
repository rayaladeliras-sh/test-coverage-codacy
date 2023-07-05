package com.stubhub.domain.account.biz.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.stubhub.domain.account.biz.intf.UsedDiscountBO;
import com.stubhub.domain.account.datamodel.dao.UsedDiscountDAO;
import com.stubhub.domain.account.datamodel.dao.impl.UsedDiscountDAOImpl;
import com.stubhub.domain.account.datamodel.entity.UsedDiscount;

public class UsedDiscountBOImplTest {

	private UsedDiscountBO usedDiscountBO;
	private UsedDiscountDAO usedDiscountDAO;
	private Long val = 1l;

	@BeforeTest
	public void setUp() {
		usedDiscountBO = new UsedDiscountBOImpl();
		usedDiscountDAO = Mockito.mock(UsedDiscountDAOImpl.class);
		ReflectionTestUtils.setField(usedDiscountBO, "usedDiscountDAO", usedDiscountDAO);
	}

	@Test
	public void testGetDiscounts(){
		Mockito.when(usedDiscountDAO.findDetailByTid(val)).thenReturn(createUsedDiscountsDetail());
		Assert.assertEquals(usedDiscountBO.getDiscounts(val.toString()).size(),1);
	}
	
	@Test
	public void testCopyDiscountsforSubsOrder(){
		Mockito.when(usedDiscountDAO.findByTid(val)).thenReturn(createUsedDiscounts());
		Mockito.when(usedDiscountDAO.persist(createUsedDiscountsLst())).thenReturn(createUsedDiscountsLst());
		Assert.assertEquals(usedDiscountBO.copyDiscountsforSubsOrder(val,2l,"bijain").size(),2);
		Mockito.when(usedDiscountDAO.findDetailByTid(val)).thenReturn(null);
		Assert.assertNotNull(usedDiscountBO.copyDiscountsforSubsOrder(val,2l,"bijain"));
		List<List> obj = new ArrayList<List>();
		Mockito.when(usedDiscountDAO.findDetailByTid(val)).thenReturn(obj);
		Assert.assertNotNull(usedDiscountBO.copyDiscountsforSubsOrder(val,2l,"bijain"));
	}
	
	public List<UsedDiscount> createUsedDiscounts(){
		List<UsedDiscount> list = new ArrayList<UsedDiscount>(); 
		UsedDiscount usedDiscount = new UsedDiscount();
		usedDiscount.setTid(val);
		usedDiscount.setDiscountId(val);
		usedDiscount.setAmountUsed(new BigDecimal(25.00));
		usedDiscount.setCurrencyCode("USD");
		list.add(usedDiscount);
		return list;
	}
	
	public List<List> createUsedDiscountsDetail(){
		List<List> list = new ArrayList<List>(); 
		List obj = new ArrayList();
		obj.add("1.1");
		obj.add("USD");
		obj.add("1");
		obj.add("multiple");
		obj.add("corporate");
		list.add(obj);
		return list;
	}	
	
	public List<UsedDiscount> createUsedDiscountsLst(){
		List<UsedDiscount> list = new ArrayList<UsedDiscount>();
		UsedDiscount usedDiscount = new UsedDiscount();
		usedDiscount.setTid(val);
		usedDiscount.setDiscountId(val);
		usedDiscount.setAmountUsed(new BigDecimal(25.00));
		usedDiscount.setCurrencyCode("USD");
		list.add(usedDiscount);
		return list;
	}
}
