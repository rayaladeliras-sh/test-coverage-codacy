package com.stubhub.domain.account.helper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.stubhub.domain.account.common.enums.DeliveryOption;
import com.stubhub.domain.account.common.enums.LMSOption;
import com.stubhub.domain.account.common.enums.ListingStatus;
import com.stubhub.domain.account.common.enums.TicketMedium;
import com.stubhub.domain.account.intf.ListingResponse;
import com.stubhub.domain.fulfillment.window.v1.intf.FulfillmentWindowResponse;
import com.stubhub.domain.fulfillment.window.v1.intf.ListingFulfillmentWindowResponse;
import com.stubhub.platform.utilities.webservice.shcontext.SHAPIContext;
import com.stubhub.platform.utilities.webservice.shcontext.SHAPIThreadLocal;




@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GetFulfillmentWindowTask implements Callable<ListingResponse>{
	private final static Logger log = LoggerFactory.getLogger(GetFulfillmentWindowTask.class);
	private static LinkedHashMap<String,DeliveryOption> PAPER_TICKET_FM_DO_MAP = new LinkedHashMap<String, DeliveryOption>();
	static{		
		PAPER_TICKET_FM_DO_MAP.put("10", DeliveryOption.UPS);
		PAPER_TICKET_FM_DO_MAP.put("11", DeliveryOption.ROYALMAIL);
		PAPER_TICKET_FM_DO_MAP.put("12", DeliveryOption.DEUTSCHEPOST);
		PAPER_TICKET_FM_DO_MAP.put("6", DeliveryOption.FEDEX);
		PAPER_TICKET_FM_DO_MAP.put("7", DeliveryOption.LMS);
		PAPER_TICKET_FM_DO_MAP.put("9", DeliveryOption.LMS);	
	}
	
	@Autowired
	private FulfillmentWindowHelper helper;
	
	private ListingResponse listing;	

	private int ticketMediumId;
	
	private List<String> mciFulfillmentMethods;
	
	private SHAPIContext apiContext;
	
	private LMSOption lmsOption;
	
	public void setAllContext(ListingResponse listing, int ticketMediumId, List<String> mciFulfillmentMethods,LMSOption lmsOption, SHAPIContext apiContext){
		this.listing = listing;
		this.ticketMediumId = ticketMediumId;
		this.mciFulfillmentMethods = mciFulfillmentMethods;
		this.apiContext = apiContext;
		this.lmsOption = lmsOption;
	}

	@Override
	public ListingResponse call() throws Exception {
		SHAPIThreadLocal.set(apiContext);
		if (TicketMedium.getTicketMedium(ticketMediumId) == TicketMedium.PAPER) {
			if(LMSOption.APPROVED == lmsOption){
				listing.setDeliveryOption(DeliveryOption.LMS);
				return listing;
			}			
			//get listing's all active fw, if there is other fw then LMS, use that one as deliveryOption
			ListingFulfillmentWindowResponse fulfillmentWindowResponse = helper.getFulfillmentWindowByListingId(NumberUtils.toLong(listing.getId()));
			if(fulfillmentWindowResponse!=null && !CollectionUtils.isEmpty(fulfillmentWindowResponse.getFulfillmentWindows())){
				//loop paper ticket fulfillment method, UPS,Royalmail,FEDEX has higher priority 
				loop: for(String fulfillmentMethodIdStr : PAPER_TICKET_FM_DO_MAP.keySet()){
					for(FulfillmentWindowResponse fw : fulfillmentWindowResponse.getFulfillmentWindows()){
						//fulfillment window API ensure all return window is opening,
						if(fw.getFulfillmentMethod()!=null && fulfillmentMethodIdStr.equals(fw.getFulfillmentMethod().getId().toString())){
							log.debug("get delivery option by fulfillment window API, listingId=" + listing.getId() + ", fulfillmentMethodId=" + fulfillmentMethodIdStr);
							listing.setDeliveryOption(PAPER_TICKET_FM_DO_MAP.get(fulfillmentMethodIdStr));
							break loop;
						}
					}
				}
			}else if(!CollectionUtils.isEmpty(mciFulfillmentMethods)){
				//if no active windows(API down or data issue), use mci fulfillment method list instead. 
				//And priority depends on whether listing is active
				String[] orderedFMIds = PAPER_TICKET_FM_DO_MAP.keySet().toArray(new String[PAPER_TICKET_FM_DO_MAP.size()]);
				if(ListingStatus.EXPIRED.equals(listing.getStatus())){
					ArrayUtils.reverse(orderedFMIds);
				}
				for(String fulfillmentMethodId: orderedFMIds){
					if(mciFulfillmentMethods.contains(fulfillmentMethodId)){
						log.debug("get delivery option by mciFulfillmentMethod, listingId=" + listing.getId() + ", fulfillmentMethodId=" + fulfillmentMethodId);
						listing.setDeliveryOption(PAPER_TICKET_FM_DO_MAP.get(fulfillmentMethodId));
						break;
					}
				}
			}else{
				//worst case
				listing.setDeliveryOption(DeliveryOption.LMS);
				log.debug("can not find right delivery option, use LMS. listingId=" + listing.getId());
			}
		}
		else if (TicketMedium.getTicketMedium(ticketMediumId) == TicketMedium.PDF) {
			listing.setDeliveryOption(DeliveryOption.PDF);
		}
		else if (TicketMedium.getTicketMedium(ticketMediumId) == TicketMedium.BARCODE) {
			listing.setDeliveryOption(DeliveryOption.BARCODE);
		}
		else if (TicketMedium.getTicketMedium(ticketMediumId) == TicketMedium.MOBILE) {
			listing.setDeliveryOption(DeliveryOption.MOBILE_TICKET);
		}
		else if (TicketMedium.getTicketMedium(ticketMediumId) == TicketMedium.EXTMOBILE) {
			listing.setDeliveryOption(DeliveryOption.EXTERNAL_TRANSFER);
		}
		else if (TicketMedium.getTicketMedium(ticketMediumId) == TicketMedium.EXTFLASH) {
			listing.setDeliveryOption(DeliveryOption.EXTERNAL_TRANSFER);
		}

		return listing;
	}
}
