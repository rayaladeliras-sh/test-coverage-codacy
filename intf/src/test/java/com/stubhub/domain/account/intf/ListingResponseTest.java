package com.stubhub.domain.account.intf;

import java.util.GregorianCalendar;
import java.util.HashSet;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.stubhub.domain.account.common.enums.DeliveryOption;
import com.stubhub.domain.account.common.enums.ListingStatus;
import com.stubhub.domain.account.common.enums.SaleMethod;
import com.stubhub.domain.account.common.enums.SplitOption;
import com.stubhub.domain.account.common.enums.TicketTrait;
import com.stubhub.newplatform.common.entity.Money;

public class ListingResponseTest {

	
	@Test
	 public void testGetSet() {
		ListingResponse listingResponse =new ListingResponse();
		String comments="COMMENT";
	 	String externalId="231";
		String id= "34";
	   	
		int quantity=1;
		String row="Middle";
	 	String seats="2345";
		String section="G";
		int split=2;
	   	Money m=new Money();
			   	
        try{
			GregorianCalendar gcal = new GregorianCalendar();
			XMLGregorianCalendar inhandDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
			XMLGregorianCalendar saleEndDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
			listingResponse.setInternalNotes(comments);
			Assert.assertEquals(listingResponse.getInternalNotes(),comments);
			listingResponse.setDeliveryOption(DeliveryOption.BARCODE);
		 	Assert.assertEquals(listingResponse.getDeliveryOption(),DeliveryOption.BARCODE);
		  	listingResponse.setEventId("10001");
		 	Assert.assertEquals(listingResponse.getEventId(),"10001");
		 	listingResponse.setExternalListingId(externalId);
		 	Assert.assertEquals(listingResponse.getExternalListingId(),externalId);
		  	listingResponse.setFaceValue(m);
		  	Assert.assertEquals(listingResponse.getFaceValue(),m);
		  	
		  	listingResponse.setId(id);
		  	Assert.assertEquals(listingResponse.getId(),id);
		  	listingResponse.setInhandDate("2013-04-29T16:00:00.000Z");
		  	Assert.assertEquals(listingResponse.getInhandDate(),"2013-04-29T16:00:00.000Z");
			listingResponse.setEarliestInhandDate("2013-04-29T16:00:00.000Z");
			Assert.assertEquals(listingResponse.getEarliestInhandDate(),"2013-04-29T16:00:00.000Z");
			listingResponse.seteInhandDateLaterThanNow(true);
			Assert.assertTrue(listingResponse.geteInhandDateLaterThanNow());
		 	listingResponse.setPayoutPerTicket(m);
		  	Assert.assertEquals(listingResponse.getPayoutPerTicket(), m);
		 	listingResponse.setPreDelivered(true);
		  	Assert.assertNotNull(listingResponse.getPreDelivered());
			listingResponse.setPricePerTicket(m);
		  	Assert.assertEquals(listingResponse.getPricePerTicket(),m);
		 	listingResponse.setQuantity(quantity);
		  	Assert.assertEquals(listingResponse.getQuantity(),Integer.valueOf(quantity));
		  	listingResponse.setQuantityRemain(quantity);
			Assert.assertEquals(listingResponse.getQuantityRemain(),Integer.valueOf(quantity));
 			listingResponse.setRows(row);
 			Assert.assertEquals(listingResponse.getRows(), row);
 			listingResponse.setSaleEndDate("2013-04-29T16:00:00.000Z");
 			Assert.assertEquals(listingResponse.getSaleEndDate(), "2013-04-29T16:00:00.000Z");
 			listingResponse.setSeats(seats);
 			Assert.assertEquals(listingResponse.getSeats(),seats);
 			listingResponse.setSection(section);
 			Assert.assertEquals(listingResponse.getSection(), section);
 			listingResponse.setSplitQuantity(split);
 			Assert.assertEquals(listingResponse.getSplitQuantity(),Integer.valueOf(split));
 			listingResponse.setSplitOption(SplitOption.NOSINGLES);
   			Assert.assertEquals(listingResponse.getSplitOption(),SplitOption.NOSINGLES);
   			listingResponse.setStatus(ListingStatus.ACTIVE);
	   		Assert.assertEquals(listingResponse.getStatus(),ListingStatus.ACTIVE);
	   		listingResponse.setCcId(1L);
	   		Assert.assertEquals(listingResponse.getCcId(),new Long(1L));
	   		listingResponse.setContactId(2L);
	   		Assert.assertEquals(listingResponse.getContactId(),new Long(2L));
	   		listingResponse.setPaymentType("2");
	   		Assert.assertEquals(listingResponse.getPaymentType(),"2");
	   		listingResponse.setSaleMethod(SaleMethod.FixedPrice);
	   		Assert.assertEquals(listingResponse.getSaleMethod(),SaleMethod.FixedPrice);
	   		listingResponse.setStartPricePerTicket(m);
	   		Assert.assertEquals(listingResponse.getStartPricePerTicket(),m);
	   		listingResponse.setEndPricePerTicket(m);
	   		Assert.assertEquals(listingResponse.getEndPricePerTicket(),m);
	   		listingResponse.setTicketTraits(new HashSet<TicketTrait>());
			Assert.assertNotNull(listingResponse.getTicketTraits());
			listingResponse.setVenueConfigSectionsId(1L);
			Assert.assertEquals(listingResponse.getVenueConfigSectionsId(),new Long(1L));
			listingResponse.setPurchasePrice(m);
	   		Assert.assertEquals(listingResponse.getPurchasePrice(),m);
	   		listingResponse.setDisplayPricePerTicket(m);
	   		Assert.assertEquals(listingResponse.getDisplayPricePerTicket(),m);		   		
	   		listingResponse.setVenueDescription("test");
	   		Assert.assertEquals(listingResponse.getVenueDescription(),"test");		   		
	   		listingResponse.setEventDescription("test");
	   		Assert.assertEquals(listingResponse.getEventDescription(),"test");
	   		listingResponse.setEventDate("2013-04-29T16:00:00.000Z");
	   		Assert.assertEquals(listingResponse.getEventDate(),"2013-04-29T16:00:00.000Z");		
	   		listingResponse.setEventTimeZone("PST");
	   		Assert.assertEquals(listingResponse.getEventTimeZone(),"PST");
 	   		listingResponse.setLmsApprovalStatus(1);
			Assert.assertEquals(listingResponse.getLmsApprovalStatus(),new Integer(1));
 			Assert.assertNotNull(listingResponse.hashCode());
		 	Assert.assertNotNull(new ListingResponse().hashCode());
			Assert.assertTrue(listingResponse.equals(listingResponse));
		  	Assert.assertFalse(listingResponse.equals(new ListingResponse()));
		  	Assert.assertFalse(new ListingResponse().equals(listingResponse));
		  	Assert.assertFalse(new ListingResponse().equals(null));
		 	Assert.assertEquals(listingResponse, listingResponse);
		 	Assert.assertNotEquals(listingResponse, new Integer(0));
			listingResponse.setIsScrubbingEnabled(true);
		  	Assert.assertTrue(listingResponse.getIsScrubbingEnabled());
			listingResponse.setSectionMappingRequired(true);
		  	Assert.assertTrue(listingResponse.getSectionMappingRequired());
			listingResponse.setPricingRecommendation(new PricingRecommendation());
			Assert.assertNotNull(listingResponse.getPricingRecommendation());
		  	
	   	
		 	Object other = new ListingResponse();
		 	Object different = new String();
			   	
			Assert.assertNotNull(listingResponse.hashCode());
		 	Assert.assertFalse(listingResponse.equals(different));
 			Assert.assertFalse(listingResponse.equals(other));
 		    Assert.assertFalse(listingResponse.equals(null));
 		    
 		    
   	  	
   	
	   	} catch(Exception e){} 
	}
}