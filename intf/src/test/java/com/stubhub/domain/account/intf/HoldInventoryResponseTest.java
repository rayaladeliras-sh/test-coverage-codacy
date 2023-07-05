package com.stubhub.domain.account.intf;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.testng.annotations.Test;

import com.stubhub.integration.partnerorder.entity.HoldInventoryResponse;
import com.stubhub.integration.partnerorder.entity.PartnerOrderResponseTicket;

public class HoldInventoryResponseTest {
	 @Test
	  public void testHoldInventoryResponse() {
		  HoldInventoryResponse response = buildHoldInventoryResponse();
		  Assert.assertEquals("responseId",response.getId());
		  Assert.assertEquals("5",response.getEndTime());
		  for(PartnerOrderResponseTicket t:response.getTickets()){
			  Assert.assertEquals("ticket1",t.getId());
			  Assert.assertEquals("center",t.getSection());
			  Assert.assertEquals("HOLD",t.getStatus());
			  Assert.assertFalse(response.getDeleteTicketFlag());
		  }
	  }
	  
	  public HoldInventoryResponse buildHoldInventoryResponse(){
			HoldInventoryResponse response = new HoldInventoryResponse();
			response.setId("responseId");	
			response.setEndTime("5");
			List<PartnerOrderResponseTicket> partnerTickets = new ArrayList<PartnerOrderResponseTicket>();
			PartnerOrderResponseTicket t = new PartnerOrderResponseTicket();
			t.setId("ticket1");	
			t.setSection("center");
			t.setStatus("HOLD");
			partnerTickets.add(t);
			response.setTickets(partnerTickets);
			response.setDeleteTicketFlag(false);
			return response;	
		}
}
