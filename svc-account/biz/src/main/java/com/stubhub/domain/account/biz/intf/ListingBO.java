package com.stubhub.domain.account.biz.intf;

import java.util.List;

import javax.ws.rs.core.Response;

import com.stubhub.common.exception.RecordNotFoundForIdException;
import com.stubhub.common.exception.UserNotAuthorizedException;
import com.stubhub.domain.account.biz.impl.TicketSeatUtil;
import com.stubhub.domain.account.datamodel.entity.StubTrans;
import com.stubhub.domain.account.datamodel.entity.TicketSeat;
import com.stubhub.domain.account.datamodel.entity.UserContact;
import com.stubhub.domain.account.intf.SubstitutionRequest;
import com.stubhub.domain.inventory.v2.DTO.ListingResponse;

public interface ListingBO {

	public Response getListing(Long listingId) throws UserNotAuthorizedException, RecordNotFoundForIdException, Exception;
	public ListingResponse validateListingforSubstitution(SubstitutionRequest request) throws RecordNotFoundForIdException, UserNotAuthorizedException, Exception;
	public List<TicketSeatUtil> allocateSeats(ListingResponse newListing, SubstitutionRequest request, String[] seatList);

    public List<TicketSeat> getTicketSeatInfo(Long ticketId);
	void callListingControllerPurchase(StubTrans newStubTrans, UserContact buyerUsrCont, ListingResponse newListing,
			SubstitutionRequest request, Long existingOrderId,List<TicketSeatUtil> selectedSeat) throws Exception;
	void callListingControllerRelease(StubTrans newStubTrans, ListingResponse newListing, Long existingOrderId, List<TicketSeatUtil> selectedSeat)
			throws Exception;
	
}
