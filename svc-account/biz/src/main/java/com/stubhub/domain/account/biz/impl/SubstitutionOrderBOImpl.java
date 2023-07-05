package com.stubhub.domain.account.biz.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.stubhub.common.exception.UserNotAuthorizedException;
import com.stubhub.domain.account.biz.intf.InventoryBO;
import com.stubhub.domain.account.biz.intf.ListingBO;
import com.stubhub.domain.account.biz.intf.StubTransBO;
import com.stubhub.domain.account.biz.intf.SubstitutionOrderBO;
import com.stubhub.domain.account.biz.intf.UsedDiscountBO;
import com.stubhub.domain.account.biz.intf.UserContactBiz;
import com.stubhub.domain.account.common.util.OrderConstants;
import com.stubhub.domain.account.datamodel.dao.CcTransDAO;
import com.stubhub.domain.account.datamodel.dao.OrderProcStatusAdapterDAO;
import com.stubhub.domain.account.datamodel.dao.StubTransDetailDAO;
import com.stubhub.domain.account.datamodel.dao.UserDAO;
import com.stubhub.domain.account.datamodel.dao.ListingDAO;
import com.stubhub.domain.account.datamodel.entity.StubTrans;
import com.stubhub.domain.account.datamodel.entity.UserContact;
import com.stubhub.domain.account.intf.CSOrderDetailsResponse;
import com.stubhub.domain.account.intf.DeliveryResponse;
import com.stubhub.domain.account.intf.OrdersResponse;
import com.stubhub.domain.account.intf.SellerPayment;
import com.stubhub.domain.account.intf.SellerPayments;
import com.stubhub.domain.account.intf.SubstitutionRequest;
import com.stubhub.domain.account.intf.TransactionResponse;
import com.stubhub.domain.inventory.v2.DTO.ListingResponse;
import com.stubhub.newplatform.property.MasterStubHubProperties;
import com.stubhub.platform.utilities.webservice.svclocator.SvcLocator;

@Component("substitutionOrderBO")
public class SubstitutionOrderBOImpl implements SubstitutionOrderBO {

	private final static Logger log = LoggerFactory.getLogger(SubstitutionOrderBOImpl.class);

	@Autowired
	private SvcLocator svcLocator;

	@Autowired
	@Qualifier("stubTransBO")
	private StubTransBO stubTransBO;

	@Autowired
	@Qualifier("listingBO")
	private ListingBO listingBO;

	@Autowired
	@Qualifier("ccTransDAO")
	private CcTransDAO ccTransDAO;

	@Autowired
	@Qualifier("usedDiscountBO")
	private UsedDiscountBO usedDiscountBO;

	@Autowired
	@Qualifier("stubTransDetailDAO")
	private StubTransDetailDAO stubTransDetailDAO;

	@Autowired
	@Qualifier("userContactBiz")
	private UserContactBiz userContactBiz;

	@Autowired
	@Qualifier("inventoryBO")
	private InventoryBO inventoryBO;

	@Autowired
	@Qualifier("orderProcStatusAdapterDAO")
	private OrderProcStatusAdapterDAO orderProcStatusAdapterDAO;
	
	@Autowired
	ListingDAO listingDAO;
	
	@Autowired
	@Qualifier("userDAO")
	private UserDAO userDAO;


	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW,  rollbackFor = Exception.class)
	public Long createOrder(SubstitutionRequest request, String orderId, String operatorId, Long orderSourceId) throws UserNotAuthorizedException, Exception {
		//TODO operator/orderid validation? NPE check?
		Long oldOrderId = Long.parseLong(orderId);
		Long listingId = Long.parseLong(request.getListingId());
		request.setOperatorId(operatorId);
		String[] seatList = new String[2];
		//validate old order
		log.info("api_domain=account api_resource=acceptSub api_method=createOrder message=validating order by calling validateListingforSubstitution");
		StubTrans existingOrder = stubTransBO.validateOrderforSubstitution(oldOrderId);

		//validate new listing
		log.info("api_domain=account api_resource=acceptSub api_method=createOrder message=validating listing by calling validateListingforSubstitution");
		ListingResponse newListing = listingBO.validateListingforSubstitution(request);
		log.info("api_domain=account api_resource=acceptSub api_method=createOrder message=allocating seat allocateSeats");
		List<TicketSeatUtil> selectedSeat = listingBO.allocateSeats(newListing, request, seatList);
		String selectedSeatList = seatList[0];		
		//If sellerpayment type is null after hidden listing creation
		if(newListing.getPaymentType() == null){
			Long oldSellerId = existingOrder.getSellerId();
			Long newSellerId = newListing.getSellerId();
			if (oldSellerId.compareTo(newSellerId) == 0){
				newListing.setPaymentType(existingOrder.getSellerPaymentTypeId().toString());
				listingDAO.updateListingPaymentType(Long.valueOf(newListing.getId()), existingOrder.getSellerPaymentTypeId());
				log.info("api_domain=account api_resource=acceptSub api_method=createOrder message= updated listing with sellerpayment type ");
			}else{
				Long sellerPaymentTypeId = userDAO.getUserSellerPaymentType(newSellerId);
				//newSellerPaymentTypeId = sellerPaymentTypeId.toString();
				newListing.setPaymentType(sellerPaymentTypeId.toString());
				listingDAO.updateListingPaymentType(Long.valueOf(newListing.getId()), sellerPaymentTypeId);	
				log.info("api_domain=account api_resource=acceptSub api_method=createOrder message= updated listing with sellerpayment type ");
			}
		}
		// not used any more , comment it out.
//		log.info("api_domain=account api_resource=acceptSub api_method=createOrder message=new listing additional callling solr");
//		QueryResponse newListingAdditionals = inventorySolrBO.getListingById(request.getListingId());
		// create stub_trans
		log.info("api_domain=account api_resource=acceptSub api_method=createOrder message=creating substrans by calling createStubTrans");
		StubTrans newStubTrans = stubTransBO.createStubTrans(request, existingOrder, newListing, selectedSeatList, null, orderSourceId);
		Long newOrderId = newStubTrans.getOrderId(); 
		// create stub_trans_fm_dm
		log.info("api_domain=account api_resource=acceptSub api_method=createOrder message=creating substrans_fm_dm by calling createStubTransFmDm");
		stubTransBO.createStubTransFmDm(request, newOrderId);

		// create stub_trans_seat_trait
		log.info("api_domain=account api_resource=acceptSub api_method=createOrder message=creating substrans seat trait by calling createStubTransSeatTrait");
		stubTransBO.createStubTransSeatTrait(newOrderId, listingId, operatorId);

		// create stub_trans_detail
		log.info("api_domain=account api_resource=acceptSub api_method=createOrder message=creating substrans details by calling createStubTransDetail");
		stubTransBO.createStubTransDetail(request, newOrderId, newListing, selectedSeat);
		log.info("api_domain=account api_resource=acceptSub api_method=createOrder message=getting user contact details getUserContactById ");
		UserContact buyerUsrCont =  userContactBiz.getUserContactById(existingOrder.getContactId());
		/*
		 * The Email for the Buyer contact can be null. 
		 * As the Inventory Listing API needed a valid email we replace the default contact email 
		 * incase the order buyer contact has blank email   
		 */
		if( buyerUsrCont.getEmail()==null||buyerUsrCont.getEmail().trim().length()<=0){
			UserContact buyerUsrDefaultCont = userContactBiz.getDefaultUserContactByOwernId(existingOrder.getBuyerId());
			buyerUsrCont.setEmail(buyerUsrDefaultCont.getEmail());

		}
		log.info("api_domain=account api_resource=acceptSub api_method=createOrder message=calling inventory controller purchase ");
		listingBO.callListingControllerPurchase(newStubTrans, buyerUsrCont, newListing, request, existingOrder.getOrderId(),selectedSeat);
		try{
			// cancel old order
			log.info("api_domain=account api_resource=acceptSub api_method=createOrder message=cancel previ                                                                                                                                                                                                                                                 ous order cancelOrder");
			stubTransBO.cancelOrder(existingOrder, operatorId);
			// update cc_trans record with new order
			log.info("api_domain=account api_resource=acceptSub api_method=createOrder message=updating cctrans by Tid  by callingupdateByTid ");
			ccTransDAO.updateByTid(oldOrderId, newOrderId, operatorId);
			// update stub_trans table for new order 
			log.info("api_domain=account api_resource=acceptSub api_method=createOrder message=create order response by calling createOrdersResponse ");
			//OrdersResponse orders = createOrdersResponse(newOrderId, existingOrder.getSellerId(), newListing.getSellerId());
			OrdersResponse orders = createOrdersResponse(newOrderId, existingOrder.getSellerId(), newListing.getSellerId(), existingOrder.getSellerPaymentTypeId().toString(),newListing.getPaymentType());
			log.info("api_domain=account api_resource=acceptSub api_method=createOrder message=update order by  calling updateOrder this is a depricated method");
			stubTransBO.updateOrder(operatorId, orders);	
			// copy discount detail from old order to new order
			log.info("api_domain=account api_resource=acceptSub api_method=createOrder message=update discount  by  calling copyDiscountsforSubsOrder ");
			usedDiscountBO.copyDiscountsforSubsOrder(oldOrderId, newOrderId, operatorId);
			
			// Updating the OrderProcStatus for new order to APPROVED (43) this should trigger the workflow for purchase. 
			Long newStatus = orderProcStatusAdapterDAO.updateOrderStatusByTransId(
					newOrderId, operatorId,
					OrderConstants.ORDER_PROC_STATUS_APPROVED);
		}catch(Exception ex){
			log.error("api_domain=account api_resource=acceptSub api_method=createStubTrans status=error error_message=Error updating the order " + ex.getMessage(), ex);
			Long sellerId=existingOrder !=null?existingOrder.getSellerId():null;
			log.error("Error Accept Sub : "+"-oldOrderId : "+oldOrderId+""+" -newOrderId : "+newOrderId+" -listindId : "+request.getListingId()+" -sellerId : "+sellerId);
			log.info("api_domain=account api_resource=acceptSub api_method=createStubTrans status=error error_message=Calling Listing controllr release because of the exception caused during order update " );
			listingBO.callListingControllerRelease(newStubTrans, newListing, existingOrder.getOrderId(),selectedSeat);
			throw ex ;
		}
		return newOrderId;
	}


	public OrdersResponse createOrdersResponse(Long orderId, Long oldSellerId, Long newSellerId,String oldSellerPaymentTypeId, String newSellerPaymentTypeId){
		OrdersResponse orders = new OrdersResponse();
		List<CSOrderDetailsResponse> list = new ArrayList<CSOrderDetailsResponse>();
		CSOrderDetailsResponse order = new CSOrderDetailsResponse();
		TransactionResponse transaction = new TransactionResponse();
		transaction.setOrderId(orderId.toString());
		order.setTransaction(transaction);
		DeliveryResponse delivery = new DeliveryResponse();
		delivery.setOrderProcSubStatusCode(OrderConstants.ORDER_PROC_STATUS_PURCHASED + "");
		order.setDelivery(delivery);
		SellerPayments sellerPayments = new SellerPayments();
		List<SellerPayment> sellerPaymentList = new ArrayList<SellerPayment>();
		if (oldSellerId.compareTo(newSellerId) == 0){
			SellerPayment sellerPayment = new SellerPayment();
			//sellerPayment.setPaymentTypeId(OrderConstants.SELLER_PAYMENT_TYPE_DO_NOT_PAY + "");
			sellerPayment.setPaymentTypeId(oldSellerPaymentTypeId);
			sellerPaymentList.add(sellerPayment);
			sellerPayments.setPayments(sellerPaymentList);
			order.setSellerPayments(sellerPayments);
		}else{			
			SellerPayment sellerPayment = new SellerPayment();
			sellerPayment.setPaymentTypeId(newSellerPaymentTypeId);
			sellerPaymentList.add(sellerPayment);
			sellerPayments.setPayments(sellerPaymentList);
			order.setSellerPayments(sellerPayments);					
		}
		list.add(order);
		orders.setOrder(list);
		return orders;
	}

	protected String getProperty(String propertyName, String defaultValue) {
		return MasterStubHubProperties.getProperty(propertyName, defaultValue);
	}



}
