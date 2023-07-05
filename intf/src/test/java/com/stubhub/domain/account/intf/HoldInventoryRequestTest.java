package com.stubhub.domain.account.intf;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.testng.annotations.Test;

import com.stubhub.domain.account.datamodel.entity.FulfillmentTypeEnum;
import com.stubhub.integration.partnerorder.entity.HoldInventoryRequest;
import com.stubhub.integration.partnerorder.entity.PartnerOrderTicket;

public class HoldInventoryRequestTest {

  @Test
  public void testHoldInventoryRequest() {
	  HoldInventoryRequest request = buildHoldInventoryRequest();
	  Assert.assertEquals("buyer first name",request.getBuyerFirstName());
	  Assert.assertEquals("buyer last name",request.getBuyerLastName());
	  Assert.assertEquals("external list id",request.getExternalListingId());
	  Assert.assertEquals(Long.valueOf(1), request.getListingId());
	  Assert.assertEquals(FulfillmentTypeEnum.Barcode.toString(),request.getFulfillmentType());
	  Assert.assertEquals(Long.valueOf(1),request.getOrderId());
	  Assert.assertEquals("2",request.getOrderTotal());
	  Assert.assertEquals("200",request.getSellerTotalPayout());
	  for(PartnerOrderTicket t:request.getTickets()){
		  Assert.assertEquals("1",t.getId());
		  Assert.assertEquals("100",t.getFaceValue());
		  Assert.assertEquals(BigDecimal.valueOf(100),t.getPrice());
		  Assert.assertEquals("1",t.getRow());
		  Assert.assertEquals("1",t.getSeatNumber());
		  Assert.assertEquals("center",t.getSection());
		  Assert.assertEquals("HOLD",t.getStatusDesc());
		  Assert.assertEquals(Long.valueOf(2),t.getStatusId());
		  Assert.assertEquals(Long.valueOf(2),t.getTicketSeatId());
	  }
	  Assert.assertEquals(Long.valueOf(1),request.getSellerId());
  }
  
  public HoldInventoryRequest buildHoldInventoryRequest(){
		HoldInventoryRequest request = new HoldInventoryRequest();
		request.setBuyerFirstName("buyer first name");
		request.setBuyerLastName("buyer last name");
		request.setExternalListingId("external list id");
		request.setListingId(1l);
		request.setFulfillmentType(FulfillmentTypeEnum.Barcode.toString());
		request.setOrderId(1L);
		request.setOrderTotal("2");
		request.setSellerId(1L);
		request.setSellerTotalPayout("200");
		PartnerOrderTicket ticket = new PartnerOrderTicket();
		ticket.setId("1");
		ticket.setFaceValue("100");
		ticket.setPrice(BigDecimal.valueOf(100));
		ticket.setRow("1");
		ticket.setSeatNumber("1");
		ticket.setSection("center");
		ticket.setStatusDesc("HOLD");
		ticket.setStatusId(2L);
		ticket.setTicketSeatId(2L);
		List<PartnerOrderTicket> tics =  new ArrayList<PartnerOrderTicket>();
		request.addTicket(ticket);
		tics.add(ticket);
		request.setTickets(tics);
		return request;
	}
}
