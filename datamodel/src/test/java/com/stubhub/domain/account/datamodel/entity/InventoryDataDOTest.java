package com.stubhub.domain.account.datamodel.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

import com.stubhub.newplatform.common.entity.Money;

/**
 * Created at 12/30/2014 10:36 AM
 * 
 * @author : Kalyan Keenala
 * @version :
 * @since :
 */

public class InventoryDataDOTest {

	private Long val = 1l;

	@Test
	public void testInventoryDataDO() {
		InventoryData inv = new InventoryData();
	
		inv.setListingId(1L);
		inv.getListingId();
		inv.setTicTecListingId("1234");
		inv.getTicTecListingId();
		inv.setSellerId(10133681L);
		inv.getSellerId();
		inv.setBrokerId(val);
		inv.getBrokerId();
		inv.setOrderId(val);
		inv.getOrderId();
	
		
	
		inv.setOptIn(true);
		inv.getOptIn();
		inv.setPartnerOrderIntegrated(true);
		inv.getPartnerOrderIntegrated();
	
		inv.setSection("Section 1");
		inv.getSection();
		inv.setRow("Row 1");
		inv.getRow();
		inv.setSeats("1,2,3");
		inv.getSeats();
		inv.setEventId(123l);
		inv.getEventId();
	

		
	}

}