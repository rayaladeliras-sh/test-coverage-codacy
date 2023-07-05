package com.stubhub.domain.account.biz.intf;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.response.QueryResponse;

import com.stubhub.common.exception.InvalidArgumentException;
import com.stubhub.common.exception.RecordNotFoundForIdException;
import com.stubhub.domain.account.biz.impl.TicketSeatUtil;
import com.stubhub.domain.account.datamodel.entity.StubTrans;
import com.stubhub.domain.account.datamodel.entity.StubTransSeatTrait;
import com.stubhub.domain.account.intf.OrdersResponse;
import com.stubhub.domain.account.intf.SubstitutionRequest;
import com.stubhub.domain.inventory.v2.DTO.ListingResponse;

public interface StubTransBO {

	public List<StubTrans> getOrderProcSubStatus(Long tid);
	public int updateBuyerContactId(Long tid, Long contactId, String addedBy, Calendar dateAdded);
	public int getBuyerFlipCount(Long buyerId);

	@Deprecated
	public Long updateOrder(String operatorId, OrdersResponse request) throws RecordNotFoundForIdException;

	public Long updateOrder(String operatorId, StubTransUpdateRequest request) throws RecordNotFoundForIdException;

	public StubTrans validateOrderforSubstitution(Long orderId) throws RecordNotFoundForIdException, InvalidArgumentException;
	public StubTrans createStubTrans(SubstitutionRequest request, StubTrans existingOrder, ListingResponse newListing, String selectedSeat, QueryResponse response, Long orderSourceId);
	public boolean cancelOrder(StubTrans existingOrder, String operatorId);
	public Long createStubTransFmDm(SubstitutionRequest request, Long newOrderId);
	public List<StubTransSeatTrait> createStubTransSeatTrait(Long newOrderId, Long listingId, String stubnetUser);
	public Long createStubTransDetail(SubstitutionRequest request, Long newOrderId, ListingResponse newListing, List<TicketSeatUtil> selectedSeats);
	public Long createStubTransTmp(SubstitutionRequest request, StubTrans existingOrder, String selectedSeatList, String newListingEventId, String currency, String sale_method_id);
    public BigDecimal getDrpOrderCount(Long sellerId);
    public StubTrans getStubTransById(Long orderId) throws RecordNotFoundForIdException;
	public Long getFulfillmentMethodIdByTid(Long tid);
	public Map<String, String> getUserSummaryOrderStats(Long buyerId);
	public Map<String, String> getUserSummarySaleStats(Long sellerId);

}
