package com.stubhub.domain.account.intf;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.testng.annotations.Test;

import com.stubhub.integration.partnerorder.entity.PartnerOrderTicket;
import com.stubhub.integration.partnerorder.entity.PurchaseInventoryRequest;
import com.stubhub.integration.partnerorder.entity.UserContact;

public class PurchaseInventoryRequestTest {
	
	 @Test
	  public void testPurchaseInventoryRequest() {
		  PurchaseInventoryRequest request = buildPurchaseInventoryRequest();
		  Assert.assertEquals("buyer first name",request.getBuyerFirstName());
		  Assert.assertEquals("buyer last name",request.getBuyerLastName());
		  Assert.assertEquals("external list id",request.getExternalListingId());
		  Assert.assertEquals(Long.valueOf(1), request.getListingId());
		  Assert.assertEquals(Long.valueOf(1),request.getOrderId());
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
		  Assert.assertEquals("12Xdqwer33$$",request.getBuyerGUID());
		  Assert.assertEquals("10.00",request.getPayoutAmount());
		  Assert.assertEquals(1L,request.getSellerId());
		  Assert.assertEquals("Street 1",request.getUserContact().getStreet());
		  Assert.assertEquals(1L,request.getOrderStatusId());
	  }
	  
	  public PurchaseInventoryRequest buildPurchaseInventoryRequest(){
		  	PurchaseInventoryRequest request = new PurchaseInventoryRequest();
			request.setBuyerFirstName("buyer first name");
			request.setBuyerLastName("buyer last name");
			request.setExternalListingId("external list id");
			request.setListingId(1l);
			request.setOrderId(1L);
			request.setSellerId(1L);
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
			request.addTicket(ticket);
			List<PartnerOrderTicket> listTicks = new ArrayList<PartnerOrderTicket>();
			listTicks.add(ticket);
			request.setPayoutAmount("10.00");
			request.setBuyerGUID("12Xdqwer33$$");
			request.setTickets(listTicks);
			UserContact user = new UserContact();
			user.setStreet("Street 1");
			request.setUserContact(user);
			request.setOrderStatusId(1l);
			return request;
		}
}
