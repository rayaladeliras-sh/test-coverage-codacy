package com.stubhub.domain.account.biz.impl;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stubhub.domain.account.datamodel.entity.InventoryData;
import com.stubhub.domain.account.datamodel.entity.StubTrans;
import com.stubhub.domain.account.datamodel.entity.StubTransDetail;
import com.stubhub.domain.account.datamodel.entity.UserContact;
import com.stubhub.domain.fulfillment.common.enums.TicketStatus;
import com.stubhub.domain.inventory.v2.DTO.ListingResponse;
import com.stubhub.domain.inventory.v2.DTO.Product;
import com.stubhub.domain.inventory.v2.enums.ProductType;
//import com.stubhub.common.entities.Money;
//import com.stubhub.common.util.StubHubProperties;
//import com.stubhub.fulfillment.management.business.entity.FulfillmentMethod;
//import com.stubhub.fulfillment.management.business.entity.enums.FulfillmentTypeEnum;
import com.stubhub.integration.partnerorder.entity.HoldInventoryRequest;
import com.stubhub.integration.partnerorder.entity.PartnerOrderTicket;
//import com.stubhub.integration.partnerorder.entity.PurchaseInventoryRequest;
//import com.stubhub.integration.tictec.business.entity.InventoryData;
//import com.stubhub.integration.tictec.business.entity.enums.POSSystemEnum;
/*import com.stubhub.integration.tictec.operation.entity.Error;
import com.stubhub.inventory.business.entity.Listing;
import com.stubhub.inventory.business.entity.TicketSub;
import com.stubhub.inventory.business.entity.codes.TicketStatus;
import com.stubhub.order.business.entity.Order;
import com.stubhub.order.business.entity.OrderItem;
import com.stubhub.order.persistence.entity.StubTransDetail;
import com.stubhub.primary.business.entity.ErrorDetail;
import com.stubhub.primary.business.entity.log.IntegrationAPISource;
import com.stubhub.primary.business.entity.log.IntegrationError;
import com.stubhub.primary.business.entity.log.enums.IntegrationAPISourceEnum;
import com.stubhub.user.business.entity.Address;
import com.stubhub.user.business.entity.Buyer;
import com.stubhub.user.business.entity.UserContact;*/
import com.stubhub.integration.partnerorder.entity.PurchaseInventoryRequest;
import com.stubhub.newplatform.common.entity.Money;

public class TicTecUtil {
	
	private static Logger log = LoggerFactory.getLogger(TicTecUtil.class);
	
	/**
	 * Populates the Integration Error from Response Error.
	 * 
	 * @param error
	 * @return
	 */
	/*public static ErrorDetail getErrorDetail(Error error) {
		if (error == null) {
			return null;
		}
		ErrorDetail errorDetail = null;
		errorDetail = new ErrorDetail();

		errorDetail.setErrorMessage(error.getDescription());
		errorDetail.setErrorDetail(error.getDescription());

		IntegrationError integrationError = null;
		integrationError = new IntegrationError();

		IntegrationAPISource integrationAPISource = null;
		integrationAPISource = new IntegrationAPISource();
		integrationAPISource.setIntegrationAPISourceEnum(IntegrationAPISourceEnum.TICTEC);

		integrationError.setCode(error.getCode());
		integrationError.setDescription(error.getDescription());
		integrationError.setIntegrationAPISource(integrationAPISource);
		integrationError.setIntegrationErrorCategory(null);
		errorDetail.setIntegrationError(integrationError);

		return errorDetail;
	}

	public boolean isOrderIntegrationEnabled() {
		String value = StubHubProperties.getProperty("tt_integration_enabled");

		if (value != null && (value.equalsIgnoreCase("on") || value.equalsIgnoreCase("true"))) {
			return true;
		}
		return false;
	}

	public boolean isIntegratedInventory(InventoryData inventoryData) {
		if (inventoryData == null) {
			return false;
		}
		// Order integration is enabled
		if (!isOrderIntegrationEnabled()) {
			return false;
		}

		// Broker id is not null
		if (inventoryData.getBrokerId() == null) {
			return false;
		}

		// User is opted in
		if (inventoryData.getOptIn() == null || !inventoryData.getOptIn()) {
			return false;
		}

		// Make sure pos system is POSNet
		if (inventoryData.getPosSystem() == null || inventoryData.getPosSystem().getPosSystemEnum() != POSSystemEnum.POSNet) {
			return false;
		}

		if (inventoryData.getTicTecListingId() == null) {
			return false;
		}

		return true;

	}

	*//**
	 * Utility method to check if SHIP order integration flag is enabled for seller or not.
	 * 
	 * @param inventoryData
	 * @return
	 *//*
	public boolean isPartnerOrderIntegrationEnabled(InventoryData inventoryData) {
		if (inventoryData == null) 
			return false;
		if (inventoryData.getPartnerOrderIntegrated() == null)
			return false;
		return inventoryData.getPartnerOrderIntegrated();

	}
	
	*//**
	 * Utility method to create HoldInventoryRequest from {@link Order} and {@link InventoryData} instances.
	 * 
	 * @param order
	 * @param inventoryData
	 * @return
	 *//*
	public HoldInventoryRequest createHoldInventoryRequest(Order order, InventoryData inventoryData) {
		log.info("ENTERing createHoldInventoryRequest method with order and inventoryData..");
		HoldInventoryRequest holdInventoryRequest = new HoldInventoryRequest();
		holdInventoryRequest.setSellerId(order.getSellerId()); //sellerId
		holdInventoryRequest.setListingId(order.getListingId()); //listingId
		holdInventoryRequest.setExternalListingId(inventoryData.getTicTecListingId()); //externalListingId

		OrderItem orderItem = order.getOrderItems().get(0);
		FulfillmentMethod fulfillmentMethod = orderItem.getFulfillmentMethod();
		FulfillmentTypeEnum ftEnum = fulfillmentMethod.getFulfillmentType().getFulfillmentTypeEnum();

		holdInventoryRequest.setOrderId(order.getOrderID());  // orderId
		holdInventoryRequest.setFulfillmentType(ftEnum.getName());  // fulfillmentType
		if (order.getBuyer().getBuyerShippingAddress() != null) {
			holdInventoryRequest.setBuyerFirstName(order.getBuyer().getBuyerShippingAddress().getFirstName());
			holdInventoryRequest.setBuyerLastName(order.getBuyer().getBuyerShippingAddress().getLastName());
		}

		 tickets 
		List<com.stubhub.inventory.business.entity.Ticket> ticketList  = order.getOrderItems().get(0).getOrderItemSupplies().get(0).getTickets();
		for (com.stubhub.inventory.business.entity.Ticket ticket : ticketList) {	
			PartnerOrderTicket t = new PartnerOrderTicket();
			t.setId(ticket.getExternalSeatId());
			t.setPrice(order.getOrderItems().get(0).getOrderItemSupplies().get(0).getPrice().getAmount());;
			t.setStatusId(TicketStatus.AVAILABLE_CODE);
			t.setStatusDesc(TicketStatus.AVAILABLE_DESC);	
			if(ticket.getFaceValue() == null || ticket.getFaceValue().getAmount() == null) {
				t.setFaceValue("");
			} else {
				t.setFaceValue(ticket.getFaceValue().getAmount().toString());
			}	
			t.setTicketSeatId(ticket.getId());	
			if (ticket.isRegularTicket()) {
				com.stubhub.inventory.business.entity.TicketSeat ts = (com.stubhub.inventory.business.entity.TicketSeat)ticket;
				t.setSection(ts.getSection());
				t.setRow(ts.getRow());
				t.setSeatNumberGA(inventoryData.getSeats(), ts.getSeatNumber());
			} else if(ticket.isParkingPass()) {
				com.stubhub.inventory.business.entity.ParkingPass pp = (com.stubhub.inventory.business.entity.ParkingPass)ticket;
				t.setSection(pp.getLot());
				t.setRow(pp.getRow());
				t.setSeatNumberGA(inventoryData.getSeats(), pp.getStall());
			} else if(ticket.isGiftCertificate()) {
				com.stubhub.inventory.business.entity.GiftCertificate gc = (com.stubhub.inventory.business.entity.GiftCertificate)ticket;
				t.setSection(gc.getSection());
				t.setRow(gc.getRow());
				t.setSeatNumberGA(inventoryData.getSeats(), gc.getSeatNumber());
			} else if(ticket.isServices()) {
				com.stubhub.inventory.business.entity.Services s = (com.stubhub.inventory.business.entity.Services)ticket;
				t.setSection(s.getSection());
				t.setRow(s.getRow());
				t.setSeatNumberGA(inventoryData.getSeats(), s.getSeatNumber());			
			}			
			holdInventoryRequest.addTicket(t);
		}
		
		if (order.getOrderItems().get(0).getTotalItemsAmount() == null) {
			holdInventoryRequest.setOrderTotal("0");
		} else {
			holdInventoryRequest.setOrderTotal(order.getOrderItems().get(0).getTotalItemsAmount().getAmount().toString());  // orderTotal
		}
		
		holdInventoryRequest.setSellerTotalPayout("0");  // after discuss with TT, we will not pass sellerTotalPayout as seller fee is not ready in checkout, so set it with 0 and INI will not pass it
		
		log.info("LEAVing createHoldInventoryRequest method with order and inventoryData...");
		return holdInventoryRequest;
	}
	
	public PurchaseInventoryRequest createPurchaseInventoryRequest(Order order, InventoryData inventoryData) {
		log.info("ENTERing createPurchaseInventoryRequest method with order and inventoryData..");
		PurchaseInventoryRequest purchaseInventoryRequest = new PurchaseInventoryRequest();
		purchaseInventoryRequest.setSellerId(order.getSellerId()); // sellerId
		purchaseInventoryRequest.setListingId(order.getTicketId()); // listingId
		purchaseInventoryRequest.setOrderId(order.getOrderID());   // orderId
		purchaseInventoryRequest.setExternalListingId(inventoryData.getTicTecListingId()); // externalListingId	
		purchaseInventoryRequest.setOrderStatusId(order.getOrderProcSubStatusCode());
		Buyer buyer = order.getBuyer();
		Address buyerShippingAddress = buyer.getBuyerShippingAddress();
		if (buyerShippingAddress != null) {
			purchaseInventoryRequest.setBuyerFirstName(buyerShippingAddress.getFirstName());
			purchaseInventoryRequest.setBuyerLastName(buyerShippingAddress.getLastName());
		}
		if(buyer.getUserGUID() != null){
			purchaseInventoryRequest.setBuyerGUID(buyer.getUserGUID());
		}
		Money money = order.getSellerPayoutAmount();
		if(null != money){
			purchaseInventoryRequest.setPayoutAmount(String.valueOf(money.getAmountNullSafe()));
		}
		List<com.stubhub.inventory.business.entity.Ticket> ticketList  = order.getOrderItems().get(0).getOrderItemSupplies().get(0).getTickets();
		for (com.stubhub.inventory.business.entity.Ticket ticket : ticketList) {	
			com.stubhub.integration.partnerorder.entity.PartnerOrderTicket t = new PartnerOrderTicket();
			t.setId(ticket.getExternalSeatId());
			t.setPrice(order.getOrderItems().get(0).getOrderItemSupplies().get(0).getPrice().getAmount());
			if (ticket.isRegularTicket()) {
				com.stubhub.inventory.business.entity.TicketSeat ts = (com.stubhub.inventory.business.entity.TicketSeat)ticket;
				t.setSection(ts.getSection());
				t.setRow(ts.getRow());
				t.setSeatNumberGA(inventoryData.getSeats(), ts.getSeatNumber());
			} else if(ticket.isParkingPass()) {
				com.stubhub.inventory.business.entity.ParkingPass pp = (com.stubhub.inventory.business.entity.ParkingPass)ticket;
				t.setSection(pp.getLot());
				t.setRow(pp.getRow());
				t.setSeatNumberGA(inventoryData.getSeats(), pp.getStall());
			} else if(ticket.isGiftCertificate()) {
				com.stubhub.inventory.business.entity.GiftCertificate gc = (com.stubhub.inventory.business.entity.GiftCertificate)ticket;
				t.setSection(gc.getSection());
				t.setRow(gc.getRow());
				t.setSeatNumberGA(inventoryData.getSeats(), gc.getSeatNumber());
			} else if(ticket.isServices()) {
				com.stubhub.inventory.business.entity.Services s = (com.stubhub.inventory.business.entity.Services)ticket;
				t.setSection(s.getSection());
				t.setRow(s.getRow());
				t.setSeatNumberGA(inventoryData.getSeats(), s.getSeatNumber());			
			}		
			purchaseInventoryRequest.addTicket(t);
		}
		com.stubhub.integration.partnerorder.entity.UserContact contact = new com.stubhub.integration.partnerorder.entity.UserContact();
		contact.setCity(buyerShippingAddress.getCity());
		contact.setCountry(buyerShippingAddress.getCountry());
		contact.setPhone(buyerShippingAddress.getPhoneNumber());
		contact.setState(buyerShippingAddress.getState());
		contact.setStreet(buyerShippingAddress.getStreet());
		contact.setZip(buyerShippingAddress.getZip());
		purchaseInventoryRequest.setUserContact(contact);
		return purchaseInventoryRequest;
	}
*/	
	public HoldInventoryRequest createHoldInventoryRequest(InventoryData invData, List<StubTransDetail> stubTransDetailList, ListingResponse listing,
			UserContact usrCont, StubTrans stubTrans, String fulfillmentType) {
		log.info("==========1==");
		HoldInventoryRequest holdInventoryRequest = new HoldInventoryRequest();
		holdInventoryRequest.setSellerId(listing.getSellerId());
		log.info("==========1.0==");
		log.info("In create HoldInventory sellerId :" +listing.getSellerId());
		holdInventoryRequest.setListingId(Long.parseLong(listing.getId()));
		holdInventoryRequest.setExternalListingId(invData.getTicTecListingId());
		log.info("In create HoldInventory invData.getTicTecListingId() :" +invData.getTicTecListingId());
		holdInventoryRequest.setOrderId(invData.getOrderId());  // orderId
		log.info("In create HoldInventory invData.getOrderId() :" +invData.getOrderId());
		holdInventoryRequest.setFulfillmentType(fulfillmentType);  // fulfillmentType
		log.info("In create HoldInventory fulfillmentType :" +fulfillmentType);
		//if (usrCont!= null && buyer.getBuyerShippingAddress() != null) {
			if (usrCont!= null ) {
			holdInventoryRequest.setBuyerFirstName(usrCont.getFirstName());
			holdInventoryRequest.setBuyerLastName(usrCont.getLastName());
			log.info("In create HoldInventory usrCont.getFirstName() :" +usrCont.getFirstName());
			log.info("In create HoldInventory usrCont.getLastName() :" +usrCont.getLastName());
		}

		log.info("==========1.5==");
	
		if( !listing.getProducts().isEmpty())
		{
		for(Product product :  listing.getProducts()) {	
			for(StubTransDetail stDetail :  stubTransDetailList) {	
			if(product.getSeatId().equals(stDetail.getTicketSeatId()))
					{
			PartnerOrderTicket t = new PartnerOrderTicket();
			t.setId(product.getExternalId());
			log.info("In create HoldInventory product.getExternalId() :" +product.getExternalId());
			 BigDecimal pricePerProd = new BigDecimal(listing.getPricePerProduct().getAmount().toString());
				log.info("In create HoldInventory pricePerProd :" +pricePerProd);
			t.setPrice(pricePerProd);
			t.setStatusId(TicketStatus.AVAILABLE_CODE);
			t.setStatusDesc(TicketStatus.AVAILABLE_DESC);	
			if(listing.getFaceValue() == null || listing.getFaceValue().getAmount() == null) {
				t.setFaceValue("");
			} else {
				t.setFaceValue(listing.getFaceValue().getAmount().toString());
				log.info("In create HoldInventory listing.getFaceValue().getAmount().toString() :" +listing.getFaceValue().getAmount().toString());
			}	
			t.setTicketSeatId(Long.parseLong(listing.getId()));	
			if (product.getProductType().equals(ProductType.TICKET) || product.getProductType().equals(ProductType.PARKING_PASS) ){
				t.setSection(listing.getSection());
				t.setRow(product.getRow());
				t.setSeatNumberGA("General Admission", product.getSeat());
				log.info("In create HoldInventory getSeatNumberGA :" +t.getSeatNumber());
			} 
			holdInventoryRequest.addTicket(t);
		}}}}
		
		
		if (stubTrans.getTotalCost() == null) {
			holdInventoryRequest.setOrderTotal("0");
		} else {
			holdInventoryRequest.setOrderTotal(stubTrans.getTotalCost().getAmount().toString());  // orderTotal
		}

		
		holdInventoryRequest.setSellerTotalPayout("0");
		

		log.info("==========5==");
		return holdInventoryRequest;
	}
	
	public boolean isPartnerOrderIntegrationEnabled(InventoryData inventoryData) {
		if (inventoryData == null) 
			return false;
		if (inventoryData.getPartnerOrderIntegrated() == null)
			return false;
		return inventoryData.getPartnerOrderIntegrated();

	}
	

	public PurchaseInventoryRequest createPurchaseInventoryRequest(
			  UserContact buyerUsrCont, ListingResponse listing,
			InventoryData inventoryData, List<StubTransDetail> stubTransDetailList, StubTrans stubTrans) {
		PurchaseInventoryRequest purchaseInventoryRequest = new PurchaseInventoryRequest();
	//	purchaseInventoryRequest.setSellerId(listing.getSellerId());
		purchaseInventoryRequest.setListingId(Long.parseLong(listing.getId()));
		purchaseInventoryRequest.setOrderId(inventoryData.getOrderId());
		purchaseInventoryRequest.setExternalListingId(inventoryData.getTicTecListingId());	
		purchaseInventoryRequest.setMarcomOptin(false);
		
		if (buyerUsrCont != null) {
			purchaseInventoryRequest.setBuyerFirstName(buyerUsrCont.getFirstName());
			purchaseInventoryRequest.setBuyerLastName(buyerUsrCont.getLastName());
		}
		Money money = stubTrans.getSellerPayoutAmount();
		if(null != money){
			purchaseInventoryRequest.setPayoutAmount(String.valueOf(money.getAmount()));
		}
		if(stubTrans.getBuyerAuthenticatedSessionGuid() != null){
			purchaseInventoryRequest.setBuyerGUID(stubTrans.getBuyerAuthenticatedSessionGuid());
		}
		
		com.stubhub.integration.partnerorder.entity.UserContact contact = new com.stubhub.integration.partnerorder.entity.UserContact();
		contact.setCity(buyerUsrCont.getCity());
		contact.setCountry(buyerUsrCont.getCountry());
		contact.setPhone(buyerUsrCont.getPhoneNumber());
		contact.setState(buyerUsrCont.getState());
		contact.setStreet(buyerUsrCont.getStreet());
		contact.setZip(buyerUsrCont.getZip());
		purchaseInventoryRequest.setUserContact(contact);

		for(Product product :  listing.getProducts()) {
			for(StubTransDetail stDetail :  stubTransDetailList) {	
				if(product.getSeatId().equals(stDetail.getTicketSeatId()))
						{
			PartnerOrderTicket t = new PartnerOrderTicket();
			t.setId(product.getExternalId());
			 BigDecimal pricePerProd = new BigDecimal(listing.getPricePerProduct().getAmount().toString());
			t.setPrice(pricePerProd);

			if (product.getProductType().equals(ProductType.TICKET) || product.getProductType().equals(ProductType.PARKING_PASS)){
				t.setSection(listing.getSection());
				t.setRow(product.getRow());
				t.setSeatNumberGA("General Admission", product.getSeat());
			}
			purchaseInventoryRequest.addTicket(t);
		}}}
		

		return purchaseInventoryRequest;
	}
	
}
