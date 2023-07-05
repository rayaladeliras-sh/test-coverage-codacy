package com.stubhub.domain.account.adapter;

import com.stubhub.domain.account.common.enums.DeliveryOption;
import com.stubhub.domain.account.common.enums.LMSOption;
import com.stubhub.domain.account.common.enums.SalesSubStatus;
import com.stubhub.domain.account.common.enums.TicketTrait;
import com.stubhub.domain.account.common.util.ValidationUtil;
import com.stubhub.domain.account.datamodel.entity.EmailLog;
import com.stubhub.domain.account.datamodel.entity.TicketMedium;
import com.stubhub.domain.account.intf.CSSaleDetailsResponse;
import com.stubhub.domain.account.intf.CSSaleTransactionResponse;
import com.stubhub.domain.account.intf.CSSalesResponse;
import com.stubhub.domain.account.intf.EmailResponse;
import com.stubhub.domain.account.intf.ListingsResponse;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.util.NamedList;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.text.ParseException;
import java.util.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AccountResponseAdapterTest {
	
	private static final String COMMON_STR = "value";
	
	@Test
	public void testConvertEmailBizResponseToWebEntities() throws Exception {
		EmailLog log = new EmailLog();

		EmailResponse response = AccountResponseAdapter
				.convertEmailBizResponseToWebEntities(log);
		Assert.assertNotNull(response);

		log.setAddressBcc(COMMON_STR);
		log.setAddressCc(COMMON_STR);
		log.setAddressFrom(COMMON_STR);
		log.setAddressTo(COMMON_STR);
		log.setDateAdded(Calendar.getInstance());
		log.setDateSent(Calendar.getInstance());
		log.setEmailId(1l);
		log.setFormat(COMMON_STR);
		log.setSubject(COMMON_STR);
		log.settId(COMMON_STR);
		log.setUserId(COMMON_STR);
		log.setbuyerOrderId(COMMON_STR);

		response = AccountResponseAdapter
				.convertEmailBizResponseToWebEntities(log);
		Assert.assertNotNull(response);
	}

	@Test
	public void testGetDeliveryOption() {
		Assert.assertEquals(AccountResponseAdapter.getDeliveryOption(TicketMedium.PAPER.getValue(), 0, LMSOption.NONE.getValue(), "11"), DeliveryOption.ROYALMAIL);
		Assert.assertEquals(AccountResponseAdapter.getDeliveryOption(TicketMedium.PAPER.getValue(), 0, LMSOption.NONE.getValue(), "12"), DeliveryOption.DEUTSCHEPOST);
		Assert.assertEquals(AccountResponseAdapter.getDeliveryOption(TicketMedium.PAPER.getValue(), 0, LMSOption.NONE.getValue(), "10"), DeliveryOption.UPS);
		Assert.assertEquals(AccountResponseAdapter.getDeliveryOption(TicketMedium.PAPER.getValue(), 0, LMSOption.NONE.getValue(), "6"), DeliveryOption.FEDEX);
		Assert.assertEquals(AccountResponseAdapter.getDeliveryOption(TicketMedium.PAPER.getValue(), 0, LMSOption.NONE.getValue(), "17"), DeliveryOption.LOCALDELIVERY);
		Assert.assertEquals(AccountResponseAdapter.getDeliveryOption(TicketMedium.PAPER.getValue(), 0, LMSOption.NONE.getValue(), "7"), DeliveryOption.LMS);
		
		Assert.assertEquals(AccountResponseAdapter.getDeliveryOption(TicketMedium.PAPER.getValue(), 0, LMSOption.NONE.getValue(), Arrays.asList("11"),"123"), DeliveryOption.ROYALMAIL);
		Assert.assertEquals(AccountResponseAdapter.getDeliveryOption(TicketMedium.PAPER.getValue(), 0, LMSOption.NONE.getValue(), Arrays.asList("12"),"123"), DeliveryOption.DEUTSCHEPOST);
		Assert.assertEquals(AccountResponseAdapter.getDeliveryOption(TicketMedium.PAPER.getValue(), 0, LMSOption.NONE.getValue(), Arrays.asList("10"),"123"), DeliveryOption.UPS);
		Assert.assertEquals(AccountResponseAdapter.getDeliveryOption(TicketMedium.PAPER.getValue(), 0, LMSOption.NONE.getValue(), Arrays.asList("6"),"123"), DeliveryOption.FEDEX);
		Assert.assertEquals(AccountResponseAdapter.getDeliveryOption(TicketMedium.PAPER.getValue(), 0, LMSOption.NONE.getValue(), Arrays.asList("17"),"123"), DeliveryOption.LOCALDELIVERY);
		Assert.assertEquals(AccountResponseAdapter.getDeliveryOption(TicketMedium.PAPER.getValue(), 0, LMSOption.NONE.getValue(), Arrays.asList("7"),"123"), DeliveryOption.LMS);

		Assert.assertEquals(AccountResponseAdapter.getDeliveryOption(TicketMedium.MOBILE.getValue(), 0, LMSOption.NONE.getValue(), Arrays.asList("7"),"123"), DeliveryOption.MOBILE_TICKET);
		Assert.assertEquals(AccountResponseAdapter.getDeliveryOption(TicketMedium.EXTMOBILE.getValue(), 0, LMSOption.NONE.getValue(), Arrays.asList("7"),"123"), DeliveryOption.EXTERNAL_TRANSFER);
		Assert.assertEquals(AccountResponseAdapter.getDeliveryOption(TicketMedium.EXTFLASH.getValue(), 0, LMSOption.NONE.getValue(), Arrays.asList("7"),"123"), DeliveryOption.EXTERNAL_TRANSFER);
	}

	@Test
	public void testConvertToTicketTraits() {

		Set<TicketTrait> ticketTraits = AccountResponseAdapter.convertToTicketTraits("959,966,102,203,601,101|Actual 4th row,50 yd line,Parking Pass,Alcohol-free seating,Student Ticket,Aisle|3,3,1,2,2,1|Seller Comments,Seller Comments,Ticket Feature,Listing Disclosure,Listing Disclosure,Ticket Feature");

		Assert.assertNotNull(ticketTraits);
		Assert.assertEquals(ticketTraits.size(), 6);

		ticketTraits = AccountResponseAdapter.convertToTicketTraits("|||");
		Assert.assertNull(ticketTraits);

		ticketTraits = AccountResponseAdapter.convertToTicketTraits("102|Parking Pass|1|Ticket Feature");
		Assert.assertEquals(ticketTraits.size(), 1);
		Assert.assertEquals(ticketTraits.iterator().next().getName(), "Parking Pass");
		Assert.assertEquals(ticketTraits.iterator().next().getId(), "102");
		Assert.assertEquals(ticketTraits.iterator().next().getType(), "Ticket Feature");

		ticketTraits = AccountResponseAdapter.convertToTicketTraits("102|Parking,Pass|1|Ticket Feature");
		Assert.assertEquals(ticketTraits.size(), 1);
		Assert.assertEquals(ticketTraits.iterator().next().getName(), "Parking,Pass");

		ticketTraits = AccountResponseAdapter.convertToTicketTraits("102,101|Parking,Pass,Aisle|1,1|Ticket Feature,Ticket Feature");
		Assert.assertEquals(ticketTraits.size(), 2);
		Assert.assertEquals(ticketTraits.iterator().next().getName(), null);

	}





	
	@Test
	public void testAddSubFlag() throws Exception {
		AccountResponseAdapter adapter = new AccountResponseAdapter();
		Map<String, Boolean> csStubTransFlag = new HashMap<String, Boolean>();
		csStubTransFlag.put("111", Boolean.TRUE);
		csStubTransFlag.put("222", Boolean.FALSE);
		CSSalesResponse salesResponse = new CSSalesResponse();
		List<CSSaleDetailsResponse> list = new ArrayList<CSSaleDetailsResponse>();
		CSSaleDetailsResponse sale1 = new CSSaleDetailsResponse();
		CSSaleTransactionResponse cssale1 = new CSSaleTransactionResponse();
		cssale1.setSaleId("111");
		sale1.setTransaction(cssale1);
		CSSaleDetailsResponse sale2 = new CSSaleDetailsResponse();
		CSSaleTransactionResponse cssale2 = new CSSaleTransactionResponse();
		cssale2.setSaleId("222");
		sale2.setTransaction(cssale2);
		
		list.add(sale1);
		list.add(sale2);
		
		salesResponse.setSale(list);
		
		Assert.assertEquals(adapter.addCSStubTransFlag(salesResponse, csStubTransFlag).getSale().get(0).getTransaction().getCsFlagged(),Boolean.TRUE);
		Assert.assertEquals(adapter.addCSStubTransFlag(salesResponse, csStubTransFlag).getSale().get(1).getTransaction().getCsFlagged(),Boolean.FALSE);
		
	}

	@Test
	public void testShippingEnabled(){
		Assert.assertTrue(AccountResponseAdapter.shippingEnabled(com.stubhub.domain.account.common.enums.TicketMedium.PAPER));
		Assert.assertTrue(AccountResponseAdapter.shippingEnabled(com.stubhub.domain.account.common.enums.TicketMedium.SEASONCARD));
		Assert.assertTrue(AccountResponseAdapter.shippingEnabled(com.stubhub.domain.account.common.enums.TicketMedium.RFID));
		Assert.assertTrue(AccountResponseAdapter.shippingEnabled(com.stubhub.domain.account.common.enums.TicketMedium.WRISTBAND));
		Assert.assertTrue(AccountResponseAdapter.shippingEnabled(com.stubhub.domain.account.common.enums.TicketMedium.EVENTCARD));
		Assert.assertFalse(AccountResponseAdapter.shippingEnabled(com.stubhub.domain.account.common.enums.TicketMedium.PDF));
	}
}
