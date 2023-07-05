package com.stubhub.domain.account.datamodel.dao;

import java.util.List;

import com.stubhub.domain.account.datamodel.entity.TicketSeat;

public interface ListingSeatsDAO {
	
	public int updateTicketSeatStatus(List<TicketSeat> lst);

    public List<TicketSeat> getTicketSeats(Long ticketId);
}
