package com.stubhub.domain.account.datamodel.entity;

import org.testng.annotations.Test;

import junit.framework.Assert;

public class TicketTest {
	Long val = 1l;
	
	public Ticket buildTicket()
	{
		Ticket tic = new Ticket();
		tic.setStatusId(1l);
		tic.setStatusDesc("status");
		tic.setFaceValue("face value");
		tic.setTicketSeatId(1l);
		tic.setSection("Section");
		tic.setRow("Row");
		tic.setSeatNumber("1");
		return tic;
		
	}
	
	@Test
	public void testTicket()
	{
		 Assert.assertEquals("status",buildTicket().getStatusDesc());
		 Assert.assertEquals("face value",buildTicket().getFaceValue());
		 Assert.assertEquals(val,buildTicket().getTicketSeatId());
		 Assert.assertEquals("Row",buildTicket().getRow());
		 Assert.assertEquals("1",buildTicket().getSeatNumber());
		 Assert.assertEquals(val,buildTicket().getStatusId());
		 Assert.assertEquals("Section",buildTicket().getSection());
	
	}

}
