package com.stubhub.domain.account.intf;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.testng.annotations.Test;

import com.stubhub.integration.partnerorder.entity.PartnerOrderResponseTicket;
import com.stubhub.integration.partnerorder.entity.Price;
import com.stubhub.integration.partnerorder.entity.PurchaseInventoryResponse;

public class PurchaseInventoryResponseTest {
	 @Test
	  public void testPurchaseInventoryResponse() {
		  PurchaseInventoryResponse response = buildPurchaseInventoryResponse();
		  Assert.assertEquals(Long.valueOf(1),response.getId());
		  Assert.assertEquals("transactionId1",response.getTransactionId());
		  for(PartnerOrderResponseTicket t:response.getTickets()){
			  Assert.assertEquals("ticket1",t.getId());
			  Assert.assertEquals("center",t.getSection());
			  Assert.assertEquals("HOLD",t.getStatus());
			  Assert.assertEquals("Row 1",t.getRow());
			  Assert.assertEquals("barcode",t.getBarcode());
			  Assert.assertEquals("Barcode Display",t.getBarcodeDisplayType());
			  Assert.assertEquals("USD",t.getPrice().getCurrency());
			  Assert.assertEquals(new BigDecimal(1),t.getPrice().getAmount());
			  Assert.assertEquals("Seat 1",t.getSeat());
			  Assert.assertEquals("Price [Amount=" + t.getPrice().getAmount() + ", Currency=" + t.getPrice().getCurrency() + "]",t.getPrice().toString());
			
			  
		  }
		  Assert.assertEquals("PurchaseInventoryResponse [Id=" + response.getId() + ", TransactionId=" + response.getTransactionId() 
					+", Tickets=" + response.getTickets() +"]",response.toString());
	  }
	  
	  public PurchaseInventoryResponse buildPurchaseInventoryResponse(){
		  PurchaseInventoryResponse response = new PurchaseInventoryResponse();
			response.setId(1L);	
			response.setTransactionId("transactionId1");
			List<PartnerOrderResponseTicket> partnerTickets = new ArrayList<PartnerOrderResponseTicket>();
			PartnerOrderResponseTicket t = new PartnerOrderResponseTicket();
			t.setId("ticket1");	
			t.setSection("center");
			t.setStatus("HOLD");
			t.setRow("Row 1");
			t.setBarcode("barcode");
			t.setBarcodeDisplayType("Barcode Display");
			t.setSeat("Seat 1");
			Price price =  new Price();
			price.setCurrency("USD");
			BigDecimal amount = new BigDecimal(1);
			price.setAmount(amount);
			t.setPrice(price);
			partnerTickets.add(t);
			response.setTickets(partnerTickets);
			return response;	
		}
}
