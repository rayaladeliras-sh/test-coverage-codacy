package com.stubhub.domain.account.biz.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.stubhub.common.exception.InvalidArgumentException;
import com.stubhub.common.exception.RecordNotFoundForIdException;
import com.stubhub.domain.account.biz.intf.StubTransBO;
import com.stubhub.domain.account.biz.intf.StubTransUpdateRequest;
import com.stubhub.domain.account.common.util.OrderConstants;
import com.stubhub.domain.account.datamodel.dao.ListingSeatTraitDAO;
import com.stubhub.domain.account.datamodel.dao.OrderProcStatusAdapterDAO;
import com.stubhub.domain.account.datamodel.dao.StubTransAdjHistDAO;
import com.stubhub.domain.account.datamodel.dao.StubTransDAO;
import com.stubhub.domain.account.datamodel.dao.StubTransDetailDAO;
import com.stubhub.domain.account.datamodel.dao.StubTransFmDmDAO;
import com.stubhub.domain.account.datamodel.dao.StubTransSeatTraitDAO;
import com.stubhub.domain.account.datamodel.dao.StubTransTmpDAO;
import com.stubhub.domain.account.datamodel.dao.TransactionCancellationDAO;
import com.stubhub.domain.account.datamodel.entity.ListingSeatTrait;
import com.stubhub.domain.account.datamodel.entity.StubTrans;
import com.stubhub.domain.account.datamodel.entity.StubTransAdjHist;
import com.stubhub.domain.account.datamodel.entity.StubTransDetail;
import com.stubhub.domain.account.datamodel.entity.StubTransFmDm;
import com.stubhub.domain.account.datamodel.entity.StubTransSeatTrait;
import com.stubhub.domain.account.datamodel.entity.StubTransTmp;
import com.stubhub.domain.account.datamodel.entity.TransactionCancellation;
import com.stubhub.domain.account.intf.CSOrderDetailsResponse;
import com.stubhub.domain.account.intf.DeliveryResponse;
import com.stubhub.domain.account.intf.OrdersResponse;
import com.stubhub.domain.account.intf.SellerPayment;
import com.stubhub.domain.account.intf.SubstitutionRequest;
import com.stubhub.domain.account.intf.TransactionResponse;
import com.stubhub.domain.inventory.v2.DTO.ListingResponse;
import com.stubhub.domain.inventory.v2.listings.intf.DeliveryMethod;
import com.stubhub.newplatform.common.entity.Money;
import com.stubhub.newplatform.persistence.adapter.UTCCalendarToTimestampAdapter;

@Component("stubTransBO")
public class StubTransBOImpl implements StubTransBO {

	private final static Logger log = LoggerFactory.getLogger(StubTransBOImpl.class);

	@Autowired
	@Qualifier("stubTransDAO")
	private StubTransDAO stubTransDAO;
	@Autowired
	@Qualifier("stubTransFmDmDAO")
	private StubTransFmDmDAO stubTransFmDmDAO;
	@Autowired
	@Qualifier("stubTransSeatTraitDAO")
	private StubTransSeatTraitDAO stubTransSeatTraitDAO;
	@Autowired
	@Qualifier("stubTransDetailDAO")
	private StubTransDetailDAO stubTransDetailDAO;
	@Autowired
	@Qualifier("listingSeatTraitDAO")
	private ListingSeatTraitDAO listingSeatTraitDAO;
	@Autowired
	@Qualifier("orderProcStatusAdapterDAO")
	private OrderProcStatusAdapterDAO orderProcStatusAdapterDAO;
	@Autowired
	@Qualifier("transactionCancellationDAO")
	private TransactionCancellationDAO transactionCancellationDAO;
	@Autowired
	@Qualifier("stubTransAdjHistDAO")
	private StubTransAdjHistDAO stubTransAdjHistDAO;

	@Autowired
	@Qualifier("stubTransTmpDAO")
	private StubTransTmpDAO stubTransTmpDAO;

	@Override
    public List<StubTrans> getOrderProcSubStatus(Long tid) {
		log.info("api_domain=account api_resource=orders api_method=getOrderProcSubStatus status=success message=Getting order proc sub status code for the orderId="
				+ tid);
		List<StubTrans> stubTrans = stubTransDAO.getOrderProcSubStatusCode(tid);
		return stubTrans;
	}

	@Override
    public int updateBuyerContactId(Long tid, Long contactId, String addedBy,
			Calendar dateAdded) {
		log.info("api_domain=account api_resource=orders api_method=updateBuyerContactId status=success message=Update buyer contact id for the orderId="
				+ tid);
		int result = stubTransDAO.updateBuyerContactId(tid, contactId, addedBy,
				dateAdded);
		return result;
	}

	@Override
	public int getBuyerFlipCount(Long userId) {
		return stubTransDAO.getBuyerFlipCount(userId);
	}

	@Override
	public StubTrans getStubTransById(Long orderId) throws RecordNotFoundForIdException {
		return stubTransDAO.getById(orderId);
	}

	@Override
	public Long updateOrder(String operatorId, OrdersResponse request)
			throws RecordNotFoundForIdException {
		CSOrderDetailsResponse csOrderDetailsResponse = request.getOrder().get(
				0);
		DeliveryResponse delivery = csOrderDetailsResponse.getDelivery();
		TransactionResponse transaction = csOrderDetailsResponse
				.getTransaction();
		SellerPayment sellerPayment = csOrderDetailsResponse
				.getSellerPayments() == null ? null : csOrderDetailsResponse
				.getSellerPayments().getPayments().get(0);

		Long orderId = Long.parseLong(transaction.getOrderId());
		Boolean cancelled = transaction.getCancelled();
		Long orderProcSubStatusCode = (delivery == null || delivery
				.getOrderProcSubStatusCode() == null) ? null : Long
				.parseLong(delivery.getOrderProcSubStatusCode());
		Long sellerPaymentTypeId = (sellerPayment == null || sellerPayment
				.getPaymentTypeId() == null) ? null : Long
				.parseLong(sellerPayment.getPaymentTypeId());

		StubTransUpdateRequest realRequest = new StubTransUpdateRequest(orderId);
		realRequest.setCancelled(cancelled);
		realRequest.setOrderProcSubStatusCode(orderProcSubStatusCode);
		realRequest.setSellerPaymentTypeId(sellerPaymentTypeId);

		return updateOrder(operatorId, realRequest);
	}

	@Override
	public Long updateOrder(String operatorId, StubTransUpdateRequest request)
			throws RecordNotFoundForIdException {

		Long orderId = request.getOrderId();
		Boolean cancelled = request.getCancelled();
		Long orderProcSubStatusCode = request.getOrderProcSubStatusCode();
		Long sellerPaymentTypeId = request.getSellerPaymentTypeId();

		StubTrans stubTrans = stubTransDAO.getById(orderId);

		if (orderProcSubStatusCode != null) {
			Long latestOrderProcStatusId = orderProcStatusAdapterDAO.updateOrderStatusByTransId(orderId, operatorId,
							orderProcSubStatusCode);
			stubTrans.setLatestOrderProcStatusId(latestOrderProcStatusId);
			stubTrans.setOrderProcSubStatusCode(orderProcSubStatusCode);
			stubTrans.setOrderProcStatusEffDate(UTCCalendarToTimestampAdapter
					.getNewUTCCalendarInstanceStatic());
		}
		if (cancelled != null) {
			stubTrans.setCancelled(cancelled);
		}
		if (sellerPaymentTypeId != null) {
			stubTrans.setSellerPaymentTypeId(sellerPaymentTypeId);
		}
		if (request.getActiveCsFlag() != null) {
			stubTrans.setActiveCsFlag(request.getActiveCsFlag());
			stubTrans.setAttentionFlag(Boolean.TRUE);
		}
		if (request.getConfirmFlowTrackId() != null) {
			stubTrans.setConfirmFlowTrackId(request.getConfirmFlowTrackId());
		}
		if (request.getSellerConfirmed() != null) {
			stubTrans.setSellerConfirmed(request.getSellerConfirmed());
		}
		stubTrans.setLastUpdatedBy(operatorId);
		stubTrans.setLastUpdatedTS(UTCCalendarToTimestampAdapter
				.getNewUTCCalendarInstanceStatic());

		stubTransDAO.persist(stubTrans);

		return stubTrans.getOrderId();
	}

	@Override
    public StubTrans validateOrderforSubstitution(Long oldOrderId)
			throws RecordNotFoundForIdException, InvalidArgumentException {
		StubTrans existingOrder = stubTransDAO.getById(oldOrderId);
		// Check if the order is already cancelled
		if (existingOrder.getCancelled()) {
			throw new InvalidArgumentException("orderId is already cancelled",
					oldOrderId.toString());
		}
		return existingOrder;
	}


	@Override
	public StubTrans createStubTrans(SubstitutionRequest request,
			StubTrans existingOrder, ListingResponse newListing,
			String selectedSeat, QueryResponse response, Long orderSourceId) {
		log.info("api_domain=account api_resource=orders api_method=createStubTrans message=creating stubTrans/subs for orderId="
				+ existingOrder.getOrderId());
		StubTrans newStubTrans = new StubTrans();
		newStubTrans.setOrderSourceId(orderSourceId);
		Money zeroMoney = new Money(new BigDecimal(0), existingOrder
				.getCurrency().toString());
		/* Required columns in STUB_TRANS */
		if (existingOrder.getBuyerId() != null)
			newStubTrans.setBuyerId(existingOrder.getBuyerId());
		if (request.getListingId() != null)
			newStubTrans.setTicketId(Long.parseLong(request.getListingId()));
		if (newListing.getSaleMethod() != null)
			newStubTrans.setSaleMethod(newListing.getSaleMethod().getValue());
		newStubTrans.setDateAdded((UTCCalendarToTimestampAdapter
				.getNewUTCCalendarInstanceStatic()));
		if (request.getQuantity() != null)
			newStubTrans.setQuantity(Long.parseLong(request.getQuantity()));
		if (existingOrder.getCcId() != null)
			newStubTrans.setCcId(existingOrder.getCcId());
		if (existingOrder.getBidderCobrand() != null)
			newStubTrans.setBidderCobrand(existingOrder.getBidderCobrand());
		if (newListing.getSection() != null)
			newStubTrans.setSection(newListing.getSection());
		if (newListing.getProducts() != null
				&& newListing.getProducts().get(0).getRow() != null)
			newStubTrans.setRowDesc(newListing.getProducts().get(0).getRow());
		if (selectedSeat != null)
			newStubTrans.setSeats(selectedSeat);
		newStubTrans.setCancelled(Boolean.FALSE);
		newStubTrans.setAttentionFlag(Boolean.FALSE);
		if (existingOrder.getContactId() != null)
			newStubTrans.setContactId(existingOrder.getContactId());
		if (request.getSellerPayoutAmount() != null)
			newStubTrans.setSellerPayoutAmount(request.getSellerPayoutAmount());
		newStubTrans.setDateLastModified((UTCCalendarToTimestampAdapter
				.getNewUTCCalendarInstanceStatic()));
		newStubTrans.setSellerConfirmed(Boolean.FALSE);
		newStubTrans
				.setLatestOrderProcStatusId(OrderConstants.ORDER_PROC_STATUS_PURCHASED); // Initial
																									// value
		newStubTrans.setCreatedTS((UTCCalendarToTimestampAdapter
				.getNewUTCCalendarInstanceStatic()));
		/* Optional columns in STUB_TRANS */
		if (existingOrder.getAffiliateId() != null)
			newStubTrans.setAffiliateId(existingOrder.getAffiliateId());
		// if (existingOrder.getLogisticsMethod() != null)
		// newStubTrans.setLogisticsMethod(null); // not required
		if (existingOrder.getGroupComments() != null)
			newStubTrans.setGroupComments(existingOrder.getGroupComments());
		if (request.getTicketCost() != null)
			newStubTrans.setTicketCost(request.getTicketCost());
		if (request.getShipCost() != null)
			newStubTrans.setShipCost(request.getShipCost());
		if (existingOrder.getBuyerFee() != null)
			newStubTrans.setBuyerFee(existingOrder.getBuyerFee());
		if (request.getSellerFeeVal() != null)
			newStubTrans.setSellerFeeVal(request.getSellerFeeVal());
		if (existingOrder.getBobId() != null)
			newStubTrans.setBobId(existingOrder.getBobId());
		if (newListing.getSellerId() != null)
			newStubTrans.setSellerId(newListing.getSellerId());
		if (request.getVatSellFee() != null)
			newStubTrans.setSellVAT(request.getVatSellFee());
		newStubTrans.setSellerFee(""); // need to check
		if (request.getDiscountCost() != null)
			newStubTrans.setDiscountCost(request.getDiscountCost());
		if (request.getTotalCost() != null)
			newStubTrans.setTotalCost(request.getTotalCost());
		newStubTrans.setSellIncrementalFee(zeroMoney);
		newStubTrans.setCreatedBy(request.getOperatorId());
		newStubTrans.setLastUpdatedBy(request.getOperatorId());
		newStubTrans
				.setFraudCheckStatusId(OrderConstants.FRAUD_CHECK_STATUS_SUBBED_ORDER);
		newStubTrans.setPremiumFees(zeroMoney);
		// if (response != null && response.getResults() != null &&
		// response.getResults().get(0) != null &&
		// response.getResults().get(0).getFieldValue("CONFIRM_OPTION_ID") !=
		// null)
		// newStubTrans.setConfirmOptionId(Long.parseLong(response.getResults().get(0).getFieldValue("CONFIRM_OPTION_ID").toString()));
		if (request.getBuyerFeeVal() != null)
			newStubTrans.setBuyerFeeVal(request.getBuyerFeeVal());
		if (request.getAddOnFee() != null)
			newStubTrans.setAddOnFee(request.getAddOnFee());
		if (existingOrder.getCurrency() != null)
			newStubTrans.setCurrency(existingOrder.getCurrency());
		if (request.getVatBuyFee() != null)
			newStubTrans.setBuyVAT(request.getVatBuyFee());
		if (existingOrder.getLogisticVAT() != null)
			newStubTrans.setLogisticVAT(existingOrder.getLogisticVAT());
		// String edd =
		// response.getResults().get(0).getFieldValue("EXPECTED_INHAND_DATE").toString();
		// newStubTrans.setExpectedDeliveryDate(); // need to check
		if (existingOrder.getBuyDomainId() != null)
			newStubTrans.setBuyDomainId(existingOrder.getBuyDomainId());
		if (existingOrder.getBuyerAuthenticatedSessionGuid() != null)
			newStubTrans.setBuyerAuthenticatedSessionGuid(existingOrder
					.getBuyerAuthenticatedSessionGuid());
		// insert expected delivery date
		List<DeliveryMethod> deliveryMethods = newListing.getDeliveryMethods();
		try {
			if (deliveryMethods != null && deliveryMethods.size() > 0) {
				log.debug("deliveryMethod size:" + deliveryMethods.size());
				for (DeliveryMethod deliveryMethod : deliveryMethods) {
					// log.debug("Long.valueOf(request.getDeliveryMethodId():" +
					// Long.valueOf(request.getDeliveryMethodId()));
					// log.debug("Long.valueOf(deliveryMethod.getId():" +
					// deliveryMethod.getId());
					if (Long.valueOf(request.getDeliveryMethodId()).compareTo(
							deliveryMethod.getId()) == 0) {
						String expectedDeliveryDate = deliveryMethod
								.getEstimatedDeliveryTime();
						// log.debug("expectedDeliveryDate:" +
						// expectedDeliveryDate);
						String dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
								dateFormat);
						Date date = simpleDateFormat
								.parse(expectedDeliveryDate);
						Calendar cal = Calendar.getInstance(TimeZone
								.getTimeZone("UTC"));
						cal.setTime(date);
						newStubTrans.setExpectedDeliveryDate(cal);
					}
				}
			}
		} catch (Exception ex) {
			log.error(
					"api_domain=account api_resource=acceptSub api_method=createStubTrans status=success_with_error error_message="
							+ ex.getLocalizedMessage(), ex);
		}
		//create stub_strans_temp row which also create the  orderid /transactioni which is then used for stubtrans table.
		Long newOrderId = createStubTransTmp(newStubTrans);
		newStubTrans.setOrderId(newOrderId);
		StubTrans newSubTrans = stubTransDAO.persist(newStubTrans);
		log.info("created subs orderId=" + newOrderId
				+ " for existing orderId=" + existingOrder.getOrderId());
		return newSubTrans;
	}

	@Override
	public Long createStubTransFmDm(SubstitutionRequest request, Long newOrderId) {
		log.info("api_domain=account api_resource=orders api_method=createStubTransFmDm message=creating stubTransFmDm for orderId="
				+ newOrderId);
		StubTransFmDm stubTransFmDm = new StubTransFmDm();
		GregorianCalendar calender = UTCCalendarToTimestampAdapter
				.getNewUTCCalendarInstanceStatic();
		if (newOrderId != null)
			stubTransFmDm.setTid(newOrderId);
		if (request.getFulfillmentMethodId() != null)
			stubTransFmDm.setFulfillmentMethodId(Long.parseLong(request
					.getFulfillmentMethodId()));
		if (request.getDeliveryMethodId() != null)
			stubTransFmDm.setDeliveryMethodId(Long.parseLong(request
					.getDeliveryMethodId()));
		if (request.getLmsLocationId() != null)
			stubTransFmDm.setLmsLocationId(Long.parseLong(request
					.getLmsLocationId()));
		stubTransFmDm.setActive(Boolean.TRUE);
		stubTransFmDm.setCreatedDate(calender);
		stubTransFmDm.setCreatedBy(request.getOperatorId());
		stubTransFmDm.setLastUpdatedDate(calender);
		stubTransFmDm.setLastUpdatedBy(request.getOperatorId());
		Long id = stubTransFmDmDAO.persist(stubTransFmDm);
		return id;
	}

	@Override
	public List<StubTransSeatTrait> createStubTransSeatTrait(Long newOrderId,
			Long listingId, String stubnetUser) {
		log.info("api_domain=account api_resource=orders api_method=createStubTransSeatTrait message=creating stubTransSeatTrait for orderId="
				+ newOrderId);
		List<StubTransSeatTrait> stSeatTransList = new ArrayList<StubTransSeatTrait>();
		Calendar calender = UTCCalendarToTimestampAdapter
				.getNewUTCCalendarInstanceStatic();
		List<ListingSeatTrait> listingSeatTraitList = listingSeatTraitDAO
				.getByListingId(listingId);
		if (listingSeatTraitList != null && (!listingSeatTraitList.isEmpty())) {
			for (ListingSeatTrait listingSeatTrait : listingSeatTraitList) {
				StubTransSeatTrait stubTransSeatTrait = new StubTransSeatTrait();
				stubTransSeatTrait.setOrderId(newOrderId);
				stubTransSeatTrait.setSupplementSeatTraitId(listingSeatTrait
						.getSupplementSeatTraitId());
				if (listingSeatTrait.getSellerSpecifiedInd() != null)
					stubTransSeatTrait.setSellerSpecifiedInd(listingSeatTrait
							.getSellerSpecifiedInd());
				else
					stubTransSeatTrait.setSellerSpecifiedInd(true);
				if (listingSeatTrait.getExtSystemSpecifiedInd() != null)
					stubTransSeatTrait
							.setExtSystemSpecifiedInd(listingSeatTrait
									.getExtSystemSpecifiedInd());
				else
					stubTransSeatTrait.setExtSystemSpecifiedInd(true);
				stubTransSeatTrait.setCreatedBy(stubnetUser);
				stubTransSeatTrait.setLastUpdatedBy(stubnetUser);
				stubTransSeatTrait.setCreatedDate(calender);
				stubTransSeatTrait.setLastUpdatedDate(calender);
				stSeatTransList.add(stubTransSeatTrait);
			}
			stubTransSeatTraitDAO.persist(stSeatTransList);
		}
		return stSeatTransList;
	}

	@Override
	public Long createStubTransDetail(SubstitutionRequest request,
			Long newOrderId, ListingResponse newListing,
			List<TicketSeatUtil> selectedSeats) {
		log.info("api_domain=account api_resource=orders api_method=createStubTransDetail message=creating stubTransDetail for orderId="
				+ newOrderId);
		Long id = null;
		for (TicketSeatUtil bizTicketSeat : selectedSeats) {
			/** STUB_TRANS_DETAIL Update Starts **/
			StubTransDetail stubTransDetail = new StubTransDetail();
			stubTransDetail.setTid(newOrderId);
			stubTransDetail.setTicketSeatId(bizTicketSeat.getSeatId());

			stubTransDetail.setSectionName(bizTicketSeat.getSection());
			stubTransDetail.setRowNumber(bizTicketSeat.getRow());
			stubTransDetail.setSeatNumber(bizTicketSeat.getSeatNumber());
			if (newListing.getSection().equalsIgnoreCase("General Admission")) {
				stubTransDetail.setGeneralAdmissionIndicator(1L);
			} else {
				stubTransDetail.setGeneralAdmissionIndicator(0L);
			}
			stubTransDetail.setTicketListTypeId(bizTicketSeat
					.getTicketListTypeId());
			stubTransDetail.setCreatedDate(UTCCalendarToTimestampAdapter
					.getNewUTCCalendarInstanceStatic());
			stubTransDetail.setCreatedBy(request.getOperatorId());
			stubTransDetail.setLastUpdatedDate(UTCCalendarToTimestampAdapter
					.getNewUTCCalendarInstanceStatic());
			stubTransDetail.setLastUpdatedBy(request.getOperatorId());
			id = stubTransDetailDAO.persist(stubTransDetail);
		}
		return id;
	}

	@Override
	public boolean cancelOrder(StubTrans existingOrder, String operatorId) {
		log.info("api_domain=account api_resource=orders api_method=cancelOrder message=cancelling orderId="
				+ existingOrder.getOrderId());
		boolean cancelled = false;
		// Update old Order to CANCELLED status
		existingOrder.setCancelled(true);
		existingOrder.setHoldPaymentTypeId(2L);
		existingOrder
				.setOrderProcSubStatusCode(OrderConstants.CANCELLED_AND_SUBBED);
		existingOrder
				.setOrderProcStatusCode(OrderConstants.OrderProcStatusCodeEnum_Cancelled_ID);
		existingOrder.setOrderProcStatusEffDate(UTCCalendarToTimestampAdapter
				.getNewUTCCalendarInstanceStatic());
		stubTransDAO.persist(existingOrder);

		// Updating the existing OrderProcStatus
		Long newStatus = orderProcStatusAdapterDAO.updateOrderStatusByTransId(
				existingOrder.getOrderId(), operatorId,
				OrderConstants.CANCELLED_AND_SUBBED);

		// Updating stub_trans table
		stubTransDAO.updateOrderProcStatus(existingOrder.getOrderId(),
				newStatus);

		// insert transaction cancellation notes
		TransactionCancellation tc = new TransactionCancellation();
		tc.setReasonId(OrderConstants.CANCELLATION_REASON_SUB_ACCEPTED);
		tc.setTid(existingOrder.getOrderId());
		tc.setDateAdded(UTCCalendarToTimestampAdapter
				.getNewUTCCalendarInstanceStatic());
		tc.setCancelledBy(operatorId);
		tc.setExtraInfo(OrderConstants.TRANSACTION_CANCELLATION_EXTRA_INFO);
		tc.setLastUpdatedBy(operatorId);
		tc.setLastUpdatedDate(UTCCalendarToTimestampAdapter
				.getNewUTCCalendarInstanceStatic());
		transactionCancellationDAO.persist(tc);

		// insert stubtrans adjustment history
		createStubTransAdjHist(existingOrder);
		cancelled = true;
		return cancelled;
	}

	private void createStubTransAdjHist(StubTrans order) {
		StubTransAdjHist history = new StubTransAdjHist();
		history.setTid(order.getOrderId());
		history.setBuyerId(order.getBuyerId());
		history.setTicketId(order.getTicketId());
		history.setSaleMethod(order.getSaleMethod());
		history.setDateAdded(order.getDateAdded());
		history.setQuantity(order.getQuantity());
		history.setCcId(order.getCcId());
		history.setBidderCobrand(order.getBidderCobrand());
		history.setSection(order.getSection());
		history.setRowDesc(order.getRowDesc());
		history.setSeats(order.getSeats());
		history.setCancelled(order.getCancelled());
		history.setSellerFee(order.getSellerFee());
		history.setBuyerFee(order.getBuyerFee());
		history.setLogisticsMethodId(order.getLogisticsMethod());
		history.setAttentionFlag(order.getAttentionFlag());
		history.setContactId(order.getContactId());
		history.setOrderDateLastModified(order.getDateLastModified());
		history.setSellerConfirmed(order.getSellerConfirmed());
		history.setLatestOrdProcStatus(order.getLatestOrderProcStatusId());
		history.setOrderCreatedBy(order.getCreatedBy());
		history.setOrderLastUpdatedBy(order.getLastUpdatedBy());
		history.setCreatedDate(UTCCalendarToTimestampAdapter
				.getNewUTCCalendarInstanceStatic());
		history.setTotalCostBeforeAdj(order.getTotalCost());
		history.setTotalCostAfterAdj(order.getTotalCost());
		history.setTicketCostBeforeAdj(order.getTicketCost());
		history.setTicketCostAfterAdj(order.getTicketCost());
		history.setShipCostBeforeAdj(order.getShipCost());
		history.setShipCostAfterAdj(order.getShipCost());
		history.setDiscountCostBeforeAdj(order.getDiscountCost());
		history.setDiscountCostAfterAdj(order.getDiscountCost());
		history.setSellerFeeValBeforeAdj(order.getSellerFeeVal());
		history.setSellFeeValAfterAdj(order.getSellerFeeVal());
		history.setPremiumFeesBeforeAdj(order.getPremiumFees());
		history.setPremiumFeesAfterAdj(order.getPremiumFees());
		history.setSellerPayoutAmtBeforeAdj(order.getSellerPayoutAmount());
		history.setSellerPayoutAmtAfterAdj(order.getSellerPayoutAmount());
		history.setSellerFee(order.getSellerFee());
		history.setBuyerFee(order.getBuyerFee());
		history.setBuyerFeeValBeforeAdj(order.getBuyerFeeVal());
		history.setBuyerFeeValAfterAdj(order.getBuyerFeeVal());
		history.setGroupComments(order.getGroupComments());
		history.setAffiliateId(order.getAffiliateId());
		history.setSellerId(order.getSellerId());
		history.setSellerPaymentTypeId(order.getSellerPaymentTypeId());
		history.setSellerCobrand(order.getSellerCobrand());
		history.setEventId(order.getEventId());
		history.setHoldPaymentTypeId(order.getHoldPaymentTypeId());
		history.setPaymentTermsId(order.getPaymentTermId());
		history.setCreatedDate(UTCCalendarToTimestampAdapter
				.getNewUTCCalendarInstanceStatic());
		history.setOrderCreatedBy(order.getCreatedBy());
		history.setOrderDateLastModified(order.getDateLastModified());
		history.setOrderLastUpdatedBy(order.getLastUpdatedBy());
		history.setSellerPayoutAmountAtConfrm(order
				.getSellerPayoutAmountAtConfrm() == null ? null : order
				.getSellerPayoutAmountAtConfrm());
		history.setCurrency(order.getCurrency().toString());
		history.setBuyVAT(order.getBuyVAT());
		history.setSellVAT(order.getSellVAT());
		history.setLogisticVAT(order.getLogisticVAT());
		history.setReasonCode(41L);

		stubTransAdjHistDAO.persist(history);
	}

	@Override
	public Long createStubTransTmp(SubstitutionRequest request,
			StubTrans existingOrder, String selectedSeatList,
			String newListingEventId, String currency, String saleMethod) {
		// log.info("api_domain=account api_resource=orders api_method=createStubTransTmp message=creating stubTransT for orderId="
		// + existingOrder.getOrderId());
		// //Long id = stubTransTmpDAO.getNextSeqStubTransTempNumber();
		// //log.info("api_domain=account api_resource=orders api_method=createStubTransTmp message=creating stubTransT for new orderId="
		// + id);
		StubTransTmp transTemp = new StubTransTmp();
		// //transTemp.setId(id);
		// GregorianCalendar calender =
		// UTCCalendarToTimestampAdapter.getNewUTCCalendarInstanceStatic();
		// //transTemp.setBuyerFee(request.getBuyerFeeVal().);
		// //transTemp.setBuyerFeeVal(request.getBuyerFeeVal().getAmount());
		// transTemp.setBidderCobrand(existingOrder.getBidderCobrand());
		// transTemp.setBuyerId(existingOrder.getBuyerId());
		// //transTemp.setCcId(request.get.getUserCcId());
		// transTemp.setContactId(existingOrder.getContactId());
		// transTemp.setDateAdded(calender);
		// //transTemp.setDiscountCost(totalDiscount);
		// //if (logisticsType != null) {
		// //
		// transTemp.setLogisticsMethod(logisticsType.getLogisticsMethodId());
		// //}
		// if (existingOrder.getGroupComments() != null) {
		// transTemp.setGroupComments(existingOrder.getGroupComments());
		// }
		// transTemp.setQuantity(Long.parseLong(request.getQuantity()));
		// transTemp.setTicketId(Long.parseLong(request.getListingId()));
		// transTemp.setCurrency(currency);
		// if(saleMethod != ""){
		// transTemp.setSaleMethod(Long.valueOf(saleMethod));
		// }
		// transTemp.setSellerFee(existingOrder.getSellerFee());
		// if(existingOrder.getSellerFeeVal().getAmount() != null){
		// transTemp.setSellerFeeVal(existingOrder.getSellerFeeVal().getAmount().longValue());
		// }
		// if(existingOrder.getShipCost().getAmount() != null){
		// transTemp.setShipCost(existingOrder.getShipCost().getAmount().longValue());
		// }
		// transTemp.setDateLastModified(calender);
		// // set TICKET_COST
		// if(existingOrder.getTicketCost().getAmount() != null){
		// transTemp.setTicketCost(existingOrder.getTicketCost().getAmount().longValue());
		// }
		// // set seat lists
		// transTemp.setSeats(selectedSeatList);
		// // set buyer cc id
		// transTemp.setCcId(existingOrder.getCcId());
		// // set buyer_fee_val
		// if(existingOrder.getBuyerFeeVal().getAmount() != null)
		// transTemp.setBuyerFeeVal(existingOrder.getBuyerFeeVal().getAmount().longValue());
		//
		// BigDecimal totalDiscount = BigDecimal.ZERO;
		// if(existingOrder.getDiscountCost() != null && request.getTicketCost()
		// != null){
		// totalDiscount =
		// existingOrder.getDiscountCost().getAmount().subtract(request.getTicketCostDifference().getAmount());
		// }
		// if (existingOrder.getEventId().longValue() ==
		// Long.valueOf(newListingEventId) && existingOrder.getAddOnFee()!= null
		// && existingOrder.getAddOnFee().getAmount().longValue() > 0) {
		// totalDiscount =
		// totalDiscount.subtract(existingOrder.getAddOnFee().getAmount());
		// }
		// transTemp.setDiscountCost(totalDiscount.longValue());

		long id = stubTransTmpDAO.persist(transTemp);
		return id;
	}

	private Long getCostFromMoney(Money val) {
		if (val != null) {
			BigDecimal amount = val.getAmount();
			if (amount != null)
				return amount.longValue();
		}

		return null;
	}





	private Long createStubTransTmp(StubTrans stubtrans) {
		if (stubtrans == null)
			return null;

		StubTransTmp transTemp = new StubTransTmp();
		transTemp.setId(stubtrans.getOrderId());
		transTemp.setBuyerId(stubtrans.getBuyerId());
		transTemp.setTicketId(stubtrans.getTicketId());
		transTemp.setSaleMethod(stubtrans.getSaleMethod());
		transTemp.setTicketCost(getCostFromMoney(stubtrans.getTicketCost()));
		transTemp.setShipCost(getCostFromMoney(stubtrans.getShipCost()));
		transTemp.setDateAdded(stubtrans.getDateAdded());
		transTemp.setQuantity(stubtrans.getQuantity());
		transTemp.setCcId(stubtrans.getCcId());
		transTemp.setBidderCobrand(stubtrans.getBidderCobrand());
		transTemp.setSeats(stubtrans.getSeats());
		transTemp.setDiscountCost(getCostFromMoney(stubtrans.getDiscountCost()));
		transTemp.setSellerFee(stubtrans.getSellerFee());
		transTemp.setBuyerFee(stubtrans.getBuyerFee());
		transTemp.setSellerFeeVal(getCostFromMoney(stubtrans.getSellerFeeVal()));
		transTemp.setBuyerFeeVal(getCostFromMoney(stubtrans.getBuyerFeeVal()));
		transTemp.setLogisticsMethod(stubtrans.getLogisticsMethod());
		// transTemp.setLogisticsMethodDescription();
		// transTemp.setDiscountList();
		transTemp.setContactId(stubtrans.getContactId());
		// transTemp.setDateLastModified(stubtrans.getDateLastModified());
		Currency currency = stubtrans.getCurrency();
		if (currency != null)
			transTemp.setCurrency(currency.getCurrencyCode());

		transTemp.setGroupComments(stubtrans.getGroupComments());

		return stubTransTmpDAO.persist(transTemp);
	}

	@Override
	public BigDecimal getDrpOrderCount(Long sellerId) {
		BigDecimal drpOrderRate = new BigDecimal(0);

		int stubTikcnt = stubTransDAO.getSelStubTicketCount(sellerId);
		int selTranTikCnt = stubTransDAO.getSelTransTikCount(sellerId);
		int selPayTikcnt = stubTransDAO.getSelPayTicketCount(sellerId);

		if ((selTranTikCnt + selPayTikcnt) != 0 && stubTikcnt != 0) {
			float drpRate = (float) (selTranTikCnt + selPayTikcnt) / stubTikcnt;

			BigDecimal bigDecimal = new BigDecimal(drpRate);
			BigDecimal roundedWithScale = bigDecimal.setScale(2,
					BigDecimal.ROUND_HALF_UP);

			return roundedWithScale;

		}

		return drpOrderRate;
	}

	@Override
	public Long getFulfillmentMethodIdByTid(Long tid) {
		List<Long> tids = new ArrayList<Long>();
		tids.add(tid);
		List<StubTransFmDm> fmDms =  stubTransFmDmDAO.getFmDmByTids(tids);
		if(fmDms != null && fmDms.size() > 0){
			return fmDms.get(0).getFulfillmentMethodId();
		}
		return null;
	}

	@Override
	public Map getUserSummaryOrderStats(Long buyerId) {
		return stubTransDAO.getUserSummaryOrderStats(buyerId);
	}

	@Override
	public Map getUserSummarySaleStats(Long sellerId) {
		return stubTransDAO.getUserSummarySaleStats(sellerId);
	}

}
