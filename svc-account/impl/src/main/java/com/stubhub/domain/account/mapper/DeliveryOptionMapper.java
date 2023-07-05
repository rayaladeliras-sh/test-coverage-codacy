package com.stubhub.domain.account.mapper;

import org.apache.solr.common.SolrDocument;

import com.stubhub.domain.account.common.enums.DeliveryOption;
import com.stubhub.domain.account.common.enums.LMSOption;
import com.stubhub.domain.account.common.enums.TicketMedium;

public class DeliveryOptionMapper implements DataMapper {

	private static String FULFILLMENT_METHOD_UPS = "10";
	private static String FULFILLMENT_METHOD_FEDEX = "6";
	private static String FULFILLMENT_METHOD_ROYALMAIL = "11";
	private static String FULFILLMENT_METHOD_DEUTSCHEPOST = "12";
	private static String FULFILLMENT_METHOD_OTHER_PREDELIVERY = "8";
	
	@Override
	public Object map(SolrDocument row, RowMeta meta) throws Exception {
		Object obj;
		//1."FULFILLMENT_METHOD_ID" 2.DELIVERY_OPTION_ID, 3.TICKET_MEDIUM, 4.LMS_APPROVAL_STATUS_ID
		String fulfillmentMethod = null;		
		obj = row.getFieldValue(meta.getFieldNames()[0]);
		if (obj != null) {
			fulfillmentMethod = (String) obj;
		}

		obj = row.getFieldValue(meta.getFieldNames()[1]);
		
		int ticketMediumId = 0;
		int lmsStatus = 0;
		if (obj != null) {			
			obj = row.getFieldValue(meta.getFieldNames()[2]);			
			if (obj != null)
				ticketMediumId = new Integer(obj.toString());

			obj = row.getFieldValue(meta.getFieldNames()[3]);
			if (obj != null)
				lmsStatus = new Integer( obj.toString());
		}
		if(FULFILLMENT_METHOD_OTHER_PREDELIVERY.equalsIgnoreCase(fulfillmentMethod))
			return DeliveryOption.WILLCALL;
		if (TicketMedium.getTicketMedium(ticketMediumId) == TicketMedium.PAPER) {
			if (LMSOption.getLMSOption(lmsStatus) == LMSOption.NONE) {
				if (FULFILLMENT_METHOD_UPS.equals(fulfillmentMethod))
					return DeliveryOption.UPS;
				else if (FULFILLMENT_METHOD_FEDEX.equals(fulfillmentMethod))
					return DeliveryOption.FEDEX;
				else if (FULFILLMENT_METHOD_ROYALMAIL.equals(fulfillmentMethod))
					return DeliveryOption.ROYALMAIL;
				else if (FULFILLMENT_METHOD_DEUTSCHEPOST.equals(fulfillmentMethod))
					return DeliveryOption.DEUTSCHEPOST;
				else
					return DeliveryOption.LMS;
			}
			return DeliveryOption.LMS;
		}

		if (TicketMedium.getTicketMedium(ticketMediumId) == TicketMedium.PDF) {
			return DeliveryOption.PDF;
		}

		if (TicketMedium.getTicketMedium(ticketMediumId) == TicketMedium.BARCODE) {
			return DeliveryOption.BARCODE;
		}

		if (TicketMedium.getTicketMedium(ticketMediumId) == TicketMedium.MOBILE) {
			return DeliveryOption.MOBILE_TICKET;
		}

		if (TicketMedium.getTicketMedium(ticketMediumId) == TicketMedium.EXTMOBILE) {
			return DeliveryOption.EXTERNAL_TRANSFER;
		}

		if (TicketMedium.getTicketMedium(ticketMediumId) == TicketMedium.EXTFLASH) {
			return DeliveryOption.EXTERNAL_TRANSFER;
		}

		return null;
	}
	

}
