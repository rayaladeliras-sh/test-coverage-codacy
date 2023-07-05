package com.stubhub.domain.account.biz.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.ResponseReader;
import org.apache.cxf.jaxrs.client.WebClient;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.stubhub.common.exception.InvalidArgumentException;
import com.stubhub.common.exception.RecordNotFoundForIdException;
import com.stubhub.common.exception.UserNotAuthorizedException;
import com.stubhub.domain.account.biz.intf.InventoryBO;
import com.stubhub.domain.account.biz.intf.ListingBO;
import com.stubhub.domain.account.common.enums.ListingType;
import com.stubhub.domain.account.datamodel.dao.ListingDAO;
import com.stubhub.domain.account.datamodel.dao.ListingSeatsDAO;
import com.stubhub.domain.account.datamodel.entity.StubTrans;
import com.stubhub.domain.account.datamodel.entity.TicketSeat;
import com.stubhub.domain.account.datamodel.entity.UserContact;
import com.stubhub.domain.account.intf.SubstitutionRequest;
import com.stubhub.domain.inventory.common.entity.ListingStatus;
import com.stubhub.domain.inventory.common.util.ErrorCode;
import com.stubhub.domain.inventory.common.util.ListingBusinessException;
import com.stubhub.domain.inventory.v2.DTO.Buyer;
import com.stubhub.domain.inventory.v2.DTO.ListingControllerRequest;
import com.stubhub.domain.inventory.v2.DTO.ListingResponse;
import com.stubhub.domain.inventory.v2.DTO.ListingsControllerRequest;
import com.stubhub.domain.inventory.v2.DTO.ListingsControllerResponse;
import com.stubhub.domain.inventory.v2.DTO.Product;
import com.stubhub.domain.inventory.v2.enums.ProductType;
import com.stubhub.newplatform.property.MasterStubHubProperties;
import com.stubhub.platform.utilities.webservice.svclocator.SvcLocator;

@Component("listingBO")
public class ListingBOImpl implements ListingBO {

	private static final String GENERAL_ADMISSION = "General Admission";
	private final static Logger log = LoggerFactory.getLogger(ListingBOImpl.class);

	@Autowired
	private SvcLocator svcLocator;
	@Autowired
	@Qualifier("listingDAO")
	private ListingDAO listingDAO;
	@Autowired
	@Qualifier("listingSeatsDAO")
	private ListingSeatsDAO listingSeatsDAO;


	@Autowired
	@Qualifier("inventoryBO")
	private InventoryBO inventoryBO;

    private final List<ResponseReader> responseReader;
    {
        ResponseReader reader = new ResponseReader();
        reader.setEntityClass(ListingResponse.class);
        responseReader = new ArrayList<ResponseReader>();
        responseReader.add(reader);
    }
	@Override
	public Response getListing(Long listingId) throws UserNotAuthorizedException, RecordNotFoundForIdException, Exception {
		String inventoryApiUrl = getProperty("inventory.listings.api.url","http://api-int.stubhub.com/inventoryv2/listings/v2/{listingId}");
		inventoryApiUrl = inventoryApiUrl.replace("{listingId}", listingId.toString());
        
		log.info("listingId=" + listingId + " calling inventoryApiUrl= " + inventoryApiUrl);



		WebClient webClient = svcLocator.locate(inventoryApiUrl, responseReader);
		webClient.accept(MediaType.APPLICATION_XML);
		webClient.header("Content-Type", MediaType.APPLICATION_XML);
		webClient.header("X-SH-Service-Context", "{role=R3}");
		//
		//		ClientConfiguration config = WebClient.getConfig(webClient);
		//		if(config!=null){
		//			config.getInInterceptors().add(new LoggingInInterceptor());
		//			config.getOutInterceptors().add(new LoggingOutInterceptor());
		//		}

		Response response = webClient.get();
		log.info("listingId=" + listingId + " inventoryApi response status=" + response.getStatus());
		if(Response.Status.OK.getStatusCode() == response.getStatus()){
			return response;
		} else if (Response.Status.NOT_FOUND.getStatusCode() == response.getStatus()) {
			throw new RecordNotFoundForIdException("listingId", listingId);
		} else if (Response.Status.BAD_REQUEST.getStatusCode() == response.getStatus()) {
			throw new InvalidArgumentException("Either the listing has expired or already sold. listingId", listingId.toString());
		} else {
			throw new Exception("listingId=" + listingId + " inventoryApi response status=" + response.getStatus());	
		}
	}

	@Override
    public ListingResponse validateListingforSubstitution(SubstitutionRequest request)
			throws RecordNotFoundForIdException, UserNotAuthorizedException, InvalidArgumentException, Exception {

		Long listingId = Long.parseLong(request.getListingId());
		int subbedQuantity = Integer.parseInt(request.getQuantity());
		ListingResponse listingResponse = null;
		Response response = this.getListing(listingId);
		listingResponse = (ListingResponse)response.getEntity();
		// validate if new listing has sufficient quantity
		if((listingResponse.getQuantityRemain() == null) || (listingResponse.getQuantityRemain() < 1) || (listingResponse.getQuantityRemain() < subbedQuantity)){
			throw new InvalidArgumentException("insufficient ticket quantity for sub", listingId.toString());
		}
		// Validate if ticket can be purchased for the desirable quantity - split validation		
		if (listingResponse.getSplitVector() != null){
			String splitVector = listingResponse.getSplitVector();
			boolean validSubbedQuantity = false;
			Scanner scan = new Scanner(splitVector).useDelimiter(",");
			while(scan.hasNext()){
				if (scan.nextInt() == subbedQuantity){
					validSubbedQuantity = true;
					break;
				}
			}
			if (!validSubbedQuantity){
				throw new InvalidArgumentException("invalid subbed quantity to split", listingId.toString());
			}				
		}
		if (listingResponse.getProducts() == null || listingResponse.getProducts().size() < 1) {
			throw new ListingBusinessException(
					new com.stubhub.domain.inventory.common.util.ListingError(
							com.stubhub.common.exception.ErrorType.BUSINESSERROR,
							ErrorCode.SEAT_CANNOT_BE_NULL,"No Seats Found in this Listing","Seats"));

		}

		return listingResponse;
	}

	@Override
	public void callListingControllerPurchase(StubTrans newStubTrans,UserContact buyerUsrCont,
			ListingResponse newListing,SubstitutionRequest request,Long existingOrderId,List<TicketSeatUtil> selectedSeat) throws Exception {
		String listControllerPurchaseAPIUrl = getProperty("inventory.purchase.api.url","http://api-int.stubhub.com/inventoryv2/listingcontroller/v2/?action=purchase");

		List<ResponseReader> responseReader = null;

		ListingsControllerRequest listingsControllerRequest = new ListingsControllerRequest();

		Buyer buyer = new Buyer();
		buyer.setId(newStubTrans.getBuyerId());

		buyer.setEmail(buyerUsrCont.getEmail());
		buyer.setFirstName(buyerUsrCont.getFirstName());
		buyer.setLastName(buyerUsrCont.getLastName());

		listingsControllerRequest.setBuyer(buyer );

		String fulfillmentType=null;

		if (request.getFulfillmentMethodId() != null) {
			fulfillmentType = inventoryBO.getFulFillmentType(Long.valueOf(request.getFulfillmentMethodId()));
			if (fulfillmentType == null || fulfillmentType.trim().length() <= 0)
				fulfillmentType="LMS";
		}

		List<ListingControllerRequest> listings = new ArrayList<ListingControllerRequest>();
		for (TicketSeatUtil eachSeat: selectedSeat) {
			ListingControllerRequest listingRequest = new ListingControllerRequest();
			listingRequest.setListingId(Long.parseLong(newListing.getId()));
			listingRequest.setProductId(eachSeat.getSeatId());
			listingRequest.setSaleId(newStubTrans.getOrderId());
			listingRequest.setFulfillmentType(fulfillmentType);

			listings.add(listingRequest);
		}

		listingsControllerRequest.setListings(listings);
		//This is a unique identifier suppose to be sessionid but as cs does not have any sessionid we are sending in the existingOrderId
		listingsControllerRequest.setRequestKey(existingOrderId);


		WebClient webClient = svcLocator.locate(listControllerPurchaseAPIUrl, responseReader);
		webClient.accept(MediaType.APPLICATION_JSON);
		webClient.header("Content-Type", MediaType.APPLICATION_JSON);
		webClient.header("X-SH-Service-Context", "{role=R1}");

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationConfig.Feature.UNWRAP_ROOT_VALUE, false);
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		String jsonRequest = mapper.writeValueAsString(listingsControllerRequest);
		Response listingResponse = webClient.post(jsonRequest);
		
		if (!(ListingStatus.HIDDEN.toString()).equalsIgnoreCase(newListing.getStatus().toString())) {
			if (listingResponse.getStatus() == 200) {
				checkResponseObject(listingResponse, mapper, existingOrderId);
			} else {

				log.error("api_domain=account api_resource=orders api_method=createStubTrans message= error creating sub as the call to listing api purchase failed  for orderId=" + existingOrderId);
				if(listingResponse.getEntity()!=null){
					log.error("error from inventory api :"+listingResponse.getEntity().toString());
				}
				throw new Exception("error creating sub as the call to listing api purchase failed  for orderId="+ 
						existingOrderId + "with status code= " + listingResponse.getStatus());
			}
		}
	}
	
	class PurchaseCallException extends Exception {

		private static final long serialVersionUID = 1L;
		
		PurchaseCallException(String message) {
			super(message);
		}
		
	}
		
	void checkResponseObject(Response response, ObjectMapper mapper, Long existingOrderId) throws PurchaseCallException, JsonParseException, JsonMappingException, IOException {
	
		InputStream is = (InputStream) response.getEntity();
		
		ListingsControllerResponse resp = mapper.readValue(is, ListingsControllerResponse.class);

		if (resp == null) {
			log.warn("api_domain=account api_resource=orders api_method=createStubTrans message=error creating sub as the call to listing api purchase returned 200 OK with empty body orderId=" + existingOrderId);
			throw new PurchaseCallException(
					"error creating sub as the call to listing api purchase returned 200 OK with empty body");
		}

		String status = resp.getStatus();

		if ((status == null) || ("SUCCESS".compareToIgnoreCase(status) != 0)) {
			status = (status != null ? status : "NULL");
			log.warn("api_domain=account api_resource=orders api_method=createStubTrans message=error creating sub as the call to listing api purchase returned 200 OK with status='" + status + "' orderId=" + existingOrderId);
			throw new PurchaseCallException("error creating sub as the call to listing api purchase returned 200 OK with status: " + status);
		}
	}


	@Override
	public void callListingControllerRelease(StubTrans newStubTrans,
			ListingResponse newListing,Long existingOrderId,List<TicketSeatUtil>  selectedSeat) throws Exception {
		String listControllerPurchaseAPIUrl = getProperty("inventory.release.api.url","http://api-int.stubhub.com/inventoryv2/listingcontroller/v2/?action=release");

		List<ResponseReader> responseReader = null;

		ListingsControllerRequest listingsControllerRequest = new ListingsControllerRequest();

		//listingsControllerRequest.setOrderId(newOrderId);
		List<ListingControllerRequest> listings = new ArrayList<ListingControllerRequest>();

		for(TicketSeatUtil seatUtil : selectedSeat){
			ListingControllerRequest listingRequest = new ListingControllerRequest();
			listingRequest.setListingId(Long.parseLong(newListing.getId()));
			listingRequest.setSaleId(newStubTrans.getOrderId());
			listingRequest.setProductId(seatUtil.getSeatId());
			listings.add(listingRequest);
		}
		listingsControllerRequest.setListings(listings);
		listingsControllerRequest.setRequestKey(existingOrderId);
		WebClient webClient = svcLocator.locate(listControllerPurchaseAPIUrl, responseReader);
		webClient.accept(MediaType.APPLICATION_JSON);
		webClient.header("Content-Type", MediaType.APPLICATION_JSON);
		webClient.header("X-SH-Service-Context", "{role=R1}");
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationConfig.Feature.UNWRAP_ROOT_VALUE, false);
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		String jsonRequest = mapper.writeValueAsString(listingsControllerRequest);
		Response listingResponse = webClient.post(jsonRequest);

		if(listingResponse.getStatus()!=200){
			log.error("api_domain=account api_resource=orders api_method=createStubTrans message= error releasing listing call failed  for orderId="
					+ existingOrderId);

			throw new Exception("error creating sub as the call to listing api purchase failed  for orderId="+ 
					existingOrderId + "with status code= " + listingResponse.getStatus());
			
		}
	}

	@Override
	public List<TicketSeatUtil> allocateSeats(ListingResponse newListing, SubstitutionRequest request, String[] seatList){
		List<TicketSeatUtil> selectedSeats = new ArrayList<TicketSeatUtil>();
		List<Product> lstTickets = ProductSeatUtil.sortBySeat(newListing.getProducts());
		
		int i = 0;
		//int count = 0;
		TicketSeatUtil ticketSeat = null;
		List<String> selectedSeatsList = new ArrayList<String>();
		if(lstTickets!=null)
		{
			boolean isMixedOrder = isMixedTicketOrder(lstTickets);
			for (Product bizTicket : lstTickets) {
				if (bizTicket.getProductType().equals(ProductType.TICKET) || bizTicket.getProductType().equals(ProductType.PARKING_PASS)){	
					ticketSeat = new TicketSeatUtil();
					ticketSeat.setSeatId(bizTicket.getSeatId());
					ticketSeat.setTicketListTypeId(ListingType.getEnum(bizTicket.getProductType().toString()).getId());
					ticketSeat.setVenueConfigSectionId(newListing.getVenueConfigSectionId());
					ticketSeat.setSection(newListing.getSection());
					ticketSeat.setRow(bizTicket.getRow());
					ticketSeat.setSeatNumber(bizTicket.getSeat());

				}
				if (isMixedOrder || i < Long.parseLong(request.getQuantity())) {
					selectedSeats.add(ticketSeat);
					if(ticketSeat.getSeatNumber() != null && bizTicket.getSeat() != null){
						selectedSeatsList.add(ticketSeat.getSeatNumber());
					}
				} else {
					break;
				}
				i++;
			}}

		Iterator<String> iter = selectedSeatsList.iterator();
		StringBuilder selectedSeat = new StringBuilder();
		if (iter.hasNext()) {
			selectedSeat.append(iter.next());
		}        
		while (iter.hasNext()) {
			selectedSeat.append(",");
			selectedSeat.append(iter.next());
		}
		if (selectedSeat != null && selectedSeat.length() > 0) {
			seatList[0] = selectedSeat.toString();
		}else{
			seatList[0] = GENERAL_ADMISSION;
		}	
		return selectedSeats;
	}
	
	private boolean isMixedTicketOrder(List<Product> lstTickets){
		boolean isParkingPassIncluded = false;
		boolean isTicketIncluded = false;
		boolean isMixedTicket = false;
		for (Product bizTicket : lstTickets) {
		 if(ProductType.PARKING_PASS.equals(bizTicket.getProductType())){
			 isParkingPassIncluded = true;
		 }
		 
		 if(ProductType.TICKET.equals(bizTicket.getProductType())){
			 isTicketIncluded = true;
		 }
		 if(isParkingPassIncluded && isTicketIncluded){
			 isMixedTicket = true;
			 break;
		 }
		}
		return isMixedTicket;
	}

	@Override
	public List<TicketSeat> getTicketSeatInfo(Long ticketId) {
		return listingSeatsDAO.getTicketSeats(ticketId);
	}

	protected String getProperty(String propertyName, String defaultValue) {
		return MasterStubHubProperties.getProperty(propertyName, defaultValue);
	}

}
