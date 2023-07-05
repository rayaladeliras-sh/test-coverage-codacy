package com.stubhub.domain.account.helper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.jaxrs.client.ResponseReader;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.stubhub.domain.account.common.enums.ErrorCode;
import com.stubhub.domain.account.common.enums.ErrorType;
import com.stubhub.domain.account.common.enums.LocalizedMessageTypes;
import com.stubhub.domain.account.common.enums.TicketTrait;
import com.stubhub.domain.account.datamodel.entity.StubTransFmDm;
import com.stubhub.domain.account.enums.FeeType;
import com.stubhub.domain.account.intf.DeliveryMethod;
import com.stubhub.domain.account.intf.Fee;
import com.stubhub.domain.account.intf.FulfillmentMethod;
import com.stubhub.domain.account.intf.ListingResponse;
import com.stubhub.domain.common.util.StringUtils;
import com.stubhub.domain.fulfillment.clients.fulfillmentlabel.util.MessageSourceBundle;
import com.stubhub.newplatform.property.MasterStubHubProperties;
import com.stubhub.platform.utilities.webservice.svclocator.SvcLocator;


@Component("listingHelper")
public class ListingHelper {	
	
	@Autowired
	private SvcLocator svcLocator;
	
	@Autowired
	@Qualifier("messageSourceBundle")
	private MessageSourceBundle messageSourceBundle;
	
	private static final Log log = LogFactory.getLog(ListingHelper.class);
	
	private static final Long DEFAULT_CC_ID = 48411L;
	
	public void addLocalizedMessagesForFmDm(ListingResponse resp, Locale locale) {
		
	if(resp.getDeliveryMethod() != null && ! resp.getDeliveryMethod().isEmpty() &&
			resp.getFulfillmentMethod() != null && ! resp.getFulfillmentMethod().isEmpty() &&
			resp.getDeliveryMethod().size() == resp.getFulfillmentMethod().size()) {
		List<DeliveryMethod> listOfDeliveryMethod = resp.getDeliveryMethod();
		List<FulfillmentMethod> listOfFulfillmentMethod	= resp.getFulfillmentMethod();
			for(int i=0 ; i< listOfDeliveryMethod.size() ; i++ ){
				listOfDeliveryMethod.get(i).setDeliveryMethodDisplayName(messageSourceBundle.getWindowMessage(listOfFulfillmentMethod.get(i).getFulfillmentMethodId(),
						listOfDeliveryMethod.get(i).getDeliveryMethodId(), LocalizedMessageTypes.DELIVERY_METHOD_DISPLAY_NAME.toString(), locale));
				listOfDeliveryMethod.get(i).setDeliveryMethodLongAppInstruction(messageSourceBundle.getDeliveryMessage(
						listOfDeliveryMethod.get(i).getDeliveryMethodId(), LocalizedMessageTypes.LONG_APP_INSTRUCTION.toString(), locale));
				listOfDeliveryMethod.get(i).setDeliveryMethodLongInstruction(messageSourceBundle.getDeliveryMessage(
						listOfDeliveryMethod.get(i).getDeliveryMethodId(), LocalizedMessageTypes.LONG_INSTRUCTION.toString(), locale));
				listOfDeliveryMethod.get(i).setDeliveryMethodShortAppInstruction(messageSourceBundle.getDeliveryMessage(
						listOfDeliveryMethod.get(i).getDeliveryMethodId(), LocalizedMessageTypes.SHORT_APP_INSTRUCTION.toString(), locale));
				listOfDeliveryMethod.get(i).setDeliveryMethodShortInstruction(messageSourceBundle.getDeliveryMessage(
						listOfDeliveryMethod.get(i).getDeliveryMethodId(), LocalizedMessageTypes.SHORT_INSTRUCTION.toString(), locale));
				listOfFulfillmentMethod.get(i).setFulfillmentMethodDisplayName(messageSourceBundle.getFulfillmentMessage(
						listOfFulfillmentMethod.get(i).getFulfillmentMethodId(), LocalizedMessageTypes.NAME.toString(), locale));
				listOfFulfillmentMethod.get(i).setFulfillmentMethodLongAppInstruction(messageSourceBundle.getFulfillmentMessage(
						listOfFulfillmentMethod.get(i).getFulfillmentMethodId(), LocalizedMessageTypes.LONG_APP_INSTRUCTION.toString(), locale));
				listOfFulfillmentMethod.get(i).setFulfillmentMethodLongInstruction(messageSourceBundle.getFulfillmentMessage(
						listOfFulfillmentMethod.get(i).getFulfillmentMethodId(), LocalizedMessageTypes.LONG_INSTRUCTION.toString(), locale));
				listOfFulfillmentMethod.get(i).setFulfillmentMethodShortAppInstruction(messageSourceBundle.getFulfillmentMessage(
						listOfFulfillmentMethod.get(i).getFulfillmentMethodId(), LocalizedMessageTypes.SHORT_APP_INSTRUCTION.toString(), locale));
				listOfFulfillmentMethod.get(i).setFulfillmentMethodShortInstruction(messageSourceBundle.getFulfillmentMessage(
						listOfFulfillmentMethod.get(i).getFulfillmentMethodId(), LocalizedMessageTypes.SHORT_INSTRUCTION.toString(), locale));
				if(listOfFulfillmentMethod.get(i).getFulfillmentMethodId().compareTo(18L)==0){
					listOfDeliveryMethod.get(i).setDeliverPrimaryName(messageSourceBundle.getDeliveryMethodName(listOfDeliveryMethod.get(i).getDeliveryMethodId(),locale));
				}
			}
		}
	}
	
	public ListingResponse convertInventoryListingToAccountListing(com.stubhub.domain.inventory.DTO.ListingResponse inventoryListingResponse) {
		ListingResponse listingResponse = null;
		if(inventoryListingResponse != null){
			listingResponse = new ListingResponse();
			if(inventoryListingResponse.getCcId() != null && !inventoryListingResponse.getCcId().equals(DEFAULT_CC_ID)) {
				listingResponse.setCcId(inventoryListingResponse.getCcId());
			}
			if(inventoryListingResponse.getPreDelivered() != null) listingResponse.setPreDelivered(inventoryListingResponse.getPreDelivered());
			if(inventoryListingResponse.getContactId() != null) listingResponse.setContactId(inventoryListingResponse.getContactId());		
			if(inventoryListingResponse.getDisplayPricePerTicket() != null) listingResponse.setDisplayPricePerTicket(inventoryListingResponse.getDisplayPricePerTicket());
			if(inventoryListingResponse.getEndPricePerTicket() != null) listingResponse.setEndPricePerTicket(inventoryListingResponse.getEndPricePerTicket());
			if(inventoryListingResponse.getEventDescription() != null) listingResponse.setEventDescription(inventoryListingResponse.getEventDescription());
			if(inventoryListingResponse.getEventId() != null) listingResponse.setEventId(inventoryListingResponse.getEventId());
			if(inventoryListingResponse.getExternalListingId() != null) listingResponse.setExternalListingId(inventoryListingResponse.getExternalListingId());
			if(inventoryListingResponse.getFaceValue() != null) listingResponse.setFaceValue(inventoryListingResponse.getFaceValue());
			if(inventoryListingResponse.getId() != null) listingResponse.setId(inventoryListingResponse.getId());
			if(inventoryListingResponse.getEventDate() != null) listingResponse.setEventDate(inventoryListingResponse.getEventDate());
			if(inventoryListingResponse.getInhandDate() != null) listingResponse.setInhandDate(inventoryListingResponse.getInhandDate());
			if(inventoryListingResponse.getSaleEndDate() != null) listingResponse.setSaleEndDate(inventoryListingResponse.getSaleEndDate());
			if(inventoryListingResponse.getInternalNotes() != null) listingResponse.setInternalNotes(inventoryListingResponse.getInternalNotes());	
			if(inventoryListingResponse.getPayoutPerTicket() != null) listingResponse.setPayoutPerTicket(inventoryListingResponse.getPayoutPerTicket());
			if(inventoryListingResponse.getPricePerTicket() != null) listingResponse.setPricePerTicket(inventoryListingResponse.getPricePerTicket());
			if(inventoryListingResponse.getVenueDescription() != null) listingResponse.setVenueDescription(inventoryListingResponse.getVenueDescription());//			
			if(inventoryListingResponse.getQuantity() != null) listingResponse.setQuantity(inventoryListingResponse.getQuantity());
			if(inventoryListingResponse.getQuantityRemain() != null) listingResponse.setQuantityRemain(inventoryListingResponse.getQuantityRemain());
			if(inventoryListingResponse.getDeliveryOption() != null){
				listingResponse.setDeliveryOption(com.stubhub.domain.account.common.enums.DeliveryOption.fromString(inventoryListingResponse.getDeliveryOption().name()));					
			}
			if(inventoryListingResponse.getSaleMethod() != null){
				listingResponse.setSaleMethod(com.stubhub.domain.account.common.enums.SaleMethod.getSaleMethod(inventoryListingResponse.getSaleMethod().getValue()));	
			}
			if(inventoryListingResponse.getSplitOption() != null){
				listingResponse.setSplitOption(com.stubhub.domain.account.common.enums.SplitOption.fromString(inventoryListingResponse.getSplitOption().name()));				
			}
			if(inventoryListingResponse.getStatus() != null) {
				listingResponse.setStatus(com.stubhub.domain.account.common.enums.ListingStatus.fromValue(inventoryListingResponse.getStatus().name()));				
			}
			if(inventoryListingResponse.getPaymentType() != null){
				listingResponse.setPaymentType(inventoryListingResponse.getPaymentType());				
			}
			if(inventoryListingResponse.getTicketTraits() != null) {
				Set<TicketTrait> ticketTraits = new HashSet<TicketTrait>();				
				for(com.stubhub.domain.inventory.common.entity.TicketTrait tt : inventoryListingResponse.getTicketTraits()){
					TicketTrait accountTT = new TicketTrait();
					if(tt != null){
						if(tt.getId() != null ) accountTT.setId(tt.getId());
						if(tt.getName() != null ) accountTT.setName(tt.getName());
						if(tt.getType() != null ) accountTT.setType(tt.getType());
						ticketTraits.add(accountTT);
					}
				}
				if(ticketTraits != null) listingResponse.setTicketTraits(ticketTraits);
			}
			if(inventoryListingResponse.getSeats() != null) listingResponse.setSeats(inventoryListingResponse.getSeats());
			if(inventoryListingResponse.getSection() != null) listingResponse.setSection(inventoryListingResponse.getSection());			
			if(inventoryListingResponse.getRows() != null) listingResponse.setRows(inventoryListingResponse.getRows());
			if(inventoryListingResponse.getSplitQuantity() != null) listingResponse.setSplitQuantity(inventoryListingResponse.getSplitQuantity());
			if(inventoryListingResponse.getStartPricePerTicket() != null) listingResponse.setStartPricePerTicket(inventoryListingResponse.getStartPricePerTicket());	
			if(inventoryListingResponse.getVenueConfigSectionId() != null) listingResponse.setVenueConfigSectionsId(inventoryListingResponse.getVenueConfigSectionId());		
			if(inventoryListingResponse.getPurchasePrice() != null) listingResponse.setPurchasePrice(inventoryListingResponse.getPurchasePrice());	
			
			if(inventoryListingResponse.getFees() != null && !inventoryListingResponse.getFees().isEmpty()){
				List<Fee> fees = new ArrayList<Fee>();
				for(com.stubhub.domain.inventory.DTO.Fee fee : inventoryListingResponse.getFees()){
					Fee accountFee = new Fee();
					accountFee.setType(FeeType.SELL);
					accountFee.setAmount(fee.getAmount());
					accountFee.setDescription(fee.getDescription());
					fees.add(accountFee);
				}
				listingResponse.setFees(fees);
			}
			if(inventoryListingResponse.getTotalPayout() != null) listingResponse.setTotalPayout(inventoryListingResponse.getTotalPayout());
		}		
		return listingResponse;		
	}
	
    private final List<ResponseReader> responseReader;
    {
        ResponseReader reader = new ResponseReader();
        reader.setEntityClass(com.stubhub.domain.inventory.DTO.ListingResponse.class);
        responseReader = new ArrayList<ResponseReader>();
        responseReader.add(reader);
    }
	public ListingResponse getListingById(String sellerGuId, String listingId, String includeFees) {		
		String inventorygetListingApiUrl = getProperty("inventoryListing.api.url", "http://apx.stubprod.com/inventorynew/listings/v1/{listingId}/seller");			
		inventorygetListingApiUrl = inventorygetListingApiUrl.replace("{listingId}", listingId);		
        
	    log.debug("inventorygetListingApiUrl= " + inventorygetListingApiUrl);
	    WebClient webClient = svcLocator.locate(inventorygetListingApiUrl, responseReader);
	    webClient.query("sellerGUID", sellerGuId);
	    if(!StringUtils.isNullorEmpty(includeFees)){
	    	webClient.query("includeFees", includeFees);
	    }
	    webClient.accept(MediaType.APPLICATION_JSON);	    
	    Response response = webClient.get();
	    log.debug(response.getStatus());
	    if(Response.Status.OK.getStatusCode() == response.getStatus()) {
	    	return convertInventoryListingToAccountListing((com.stubhub.domain.inventory.DTO.ListingResponse) response.getEntity());	    	
		} else if(Response.Status.NOT_FOUND.getStatusCode() == response.getStatus()){
			log.error("Invalid listingId=" + listingId);			
			ListingResponse listingsResponse = new ListingResponse();
			listingsResponse.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
			listingsResponse.getErrors().add(new com.stubhub.domain.account.common.Error(ErrorType.INPUTERROR, ErrorCode.INVALID_LISTINGID, "No listing can be associated with this listingId", "listingId"));
			return listingsResponse;			
		} else if(Response.Status.UNAUTHORIZED.getStatusCode() == response.getStatus()){
			log.error("Invalid listingId=" + listingId);			
			ListingResponse listingsResponse = new ListingResponse();
			listingsResponse.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
			listingsResponse.getErrors().add(new com.stubhub.domain.account.common.Error(ErrorType.AUTHENTICATIONERROR, ErrorCode.SELLER_NOT_AUTHORIZED, "Seller not authorized", "listingId"));
			return listingsResponse;			
		} else if(Response.Status.BAD_REQUEST.getStatusCode() == response.getStatus()){
			log.error("Invalid listingId=" + listingId);			
			ListingResponse listingsResponse = new ListingResponse();
			listingsResponse.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
			listingsResponse.getErrors().add(new com.stubhub.domain.account.common.Error(ErrorType.INPUTERROR, ErrorCode.INVALID_LISTINGID, "Invalid listing ID", "listingId"));
			return listingsResponse;			
		} else {
			log.error("System error occured while calling getListingById api  sellerGuId=" + sellerGuId + " listingId=" +listingId );
			ListingResponse listingsResponse = new ListingResponse();
			listingsResponse.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
			listingsResponse.getErrors().add(new com.stubhub.domain.account.common.Error(ErrorType.SYSTEMERROR, ErrorCode.SYSTEM_ERROR, "System Error Occured", ""));
			return listingsResponse;	
		}
	   	
	}
	protected String getProperty (String propertyName, String defaultValue) {
		return MasterStubHubProperties.getProperty(propertyName, defaultValue);
	}
	
	
}
