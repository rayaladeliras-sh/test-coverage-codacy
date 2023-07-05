package com.stubhub.domain.account.mapper;

import org.apache.solr.common.SolrDocument;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.stubhub.domain.account.common.enums.DeliveryOption;
import com.stubhub.domain.account.common.enums.TicketMedium;

public class DeliveryOptionMapperTest {
	@Test
	public void testMap() throws Exception{
		DeliveryOptionMapper dom = new DeliveryOptionMapper();
		
		RowMeta rm = new RowMeta(new String[]{"FULFILLMENT_METHOD_ID","DELIVERY_OPTION_ID","TICKET_MEDIUM","LMS_APPROVAL_STATUS_ID"}, null, null, null);		
		SolrDocument sd = new SolrDocument();
		sd.setField("TICKET_MEDIUM", String.valueOf(TicketMedium.PDF.getValue()));
		sd.setField("DELIVERY_OPTION_ID", "1");
		Assert.assertEquals(dom.map(sd, rm).toString(), DeliveryOption.PDF.toString());
		
		sd.setField("TICKET_MEDIUM", String.valueOf(TicketMedium.BARCODE.getValue()));
		Assert.assertEquals(dom.map(sd, rm).toString(), DeliveryOption.BARCODE.toString());
		
		sd.setField("TICKET_MEDIUM", String.valueOf(TicketMedium.PAPER.getValue()));
		sd.setField("FULFILLMENT_METHOD_ID", "10");
		Assert.assertEquals(dom.map(sd, rm).toString(), DeliveryOption.UPS.toString());
		
		sd.setField("FULFILLMENT_METHOD_ID", "6");
		Assert.assertEquals(dom.map(sd, rm).toString(), DeliveryOption.FEDEX.toString());

		sd.setField("FULFILLMENT_METHOD_ID", "11");
		Assert.assertEquals(dom.map(sd, rm).toString(), DeliveryOption.ROYALMAIL.toString());

		sd.setField("FULFILLMENT_METHOD_ID", "12");
		Assert.assertEquals(dom.map(sd, rm).toString(), DeliveryOption.DEUTSCHEPOST.toString());
		
		sd.setField("LMS_APPROVAL_STATUS_ID", "2");
		Assert.assertEquals(dom.map(sd, rm).toString(), DeliveryOption.LMS.toString());

		sd.setField("TICKET_MEDIUM", String.valueOf(TicketMedium.MOBILE.getValue()));
		Assert.assertEquals(dom.map(sd, rm).toString(), DeliveryOption.MOBILE_TICKET.toString());

		sd.setField("TICKET_MEDIUM", String.valueOf(TicketMedium.EXTMOBILE.getValue()));
		Assert.assertEquals(dom.map(sd, rm).toString(), DeliveryOption.EXTERNAL_TRANSFER.toString());

		sd.setField("TICKET_MEDIUM", String.valueOf(TicketMedium.EXTFLASH.getValue()));
		Assert.assertEquals(dom.map(sd, rm).toString(), DeliveryOption.EXTERNAL_TRANSFER.toString());
	}
}
