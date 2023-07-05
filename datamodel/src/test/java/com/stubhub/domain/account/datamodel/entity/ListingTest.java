package com.stubhub.domain.account.datamodel.entity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.stubhub.newplatform.common.entity.Money;

public class ListingTest {
	
	@Test
	 public void testGetSet() {
		
		Listing listing=new Listing();
		String comments="COMMENTS";
		int confirmOption=1;
		String correlationId="23";
		Calendar cal=Calendar.getInstance();
		int deliveryOption=1;
		Money displayPricePerTicket=new Money();
		Calendar endDate=Calendar.getInstance();
		String externalId="23";
		Money faceValue=new Money();
		String fulfillmentDeliveryMethods="PDF";
		Long id=3L;
		String ipAddress="123.2.3.2";
		int listingSource=1;
		Long listingType=7L;
		int lmsApprovalStatus=2;
		int quantity=2;
		int quantityRemain=3;
		String row="34";
		Long saleMethod=8L;
		String seats="55";
		String section="GGG";
		Long sellerCCId=9L;
		String sellerCobrand="INDY";
		Long sellerContactId=10L;
		Long sellerId=11L;
		Long sellerPaymentTypeId=12L;
		String sellFeeDescription="SELLFEE";
		short splitOption=1;
		int splitQuantity=1;
		String structuredComments="comments";
		String systemStatus="Status";
		String tealeafSessionGuid="we1232311ASD";
		String lastUpdatedBy = "bijain";
		listing.setLastUpdatedBy(lastUpdatedBy);
		Assert.assertEquals(listing.getLastUpdatedBy(),"bijain");
		
		
		listing.setComments(comments);
		Assert.assertEquals(listing.getComments(),comments);
		listing.setConfirmOption(confirmOption);
		Assert.assertEquals(listing.getConfirmOption(),Integer.valueOf(confirmOption));
		listing.setCorrelationId(correlationId);
		Assert.assertEquals(listing.getCorrelationId(),correlationId);
		listing.setCreatedDate(cal);
		Assert.assertEquals(listing.getCreatedDate(),cal);
		/*listing.setCurrency(Currency.getInstance("en_US"));
		Assert.assertEquals(listing.getCurrency(),Currency.getInstance("en_US"));*/
		listing.setDeclaredInhandDate(cal);
		Assert.assertEquals(listing.getDeclaredInhandDate(),cal);
		listing.setDeferedActivationDate(cal);
		Assert.assertEquals(listing.getDeferedActivationDate(),cal);
		listing.setDeliveryOption(deliveryOption);
		Assert.assertEquals(listing.getDeliveryOption(),Integer.valueOf(deliveryOption));
		listing.setDisplayPricePerTicket(displayPricePerTicket);
		Assert.assertEquals(listing.getDisplayPricePerTicket(), displayPricePerTicket);
		listing.setEndDate(endDate);
		Assert.assertEquals(listing.getEndDate(),endDate);
		listing.setExternalId(externalId);
		Assert.assertEquals(listing.getExternalId(),externalId);
		listing.setFaceValue(faceValue);
		Assert.assertEquals(listing.getFaceValue(),faceValue);
		listing.setFulfillmentDeliveryMethods(fulfillmentDeliveryMethods);
		Assert.assertEquals(listing.getFulfillmentDeliveryMethods(),fulfillmentDeliveryMethods);	
		listing.setFulfillmentMethod(FulfillmentMethod.Barcode);
		Assert.assertEquals(listing.getFulfillmentMethod(), FulfillmentMethod.Barcode);
		listing.setId(id);
		Assert.assertEquals(listing.getId(),Long.valueOf(id));
		listing.setInhandDate(cal);
		Assert.assertEquals(listing.getInhandDate(),cal);
		listing.setIpAddress(ipAddress);
		Assert.assertEquals(listing.getIpAddress(),ipAddress);
		listing.setIsETicket(true);
		Assert.assertNotNull(listing.getIsETicket());
		listing.setLastModifiedDate(cal);
		Assert.assertEquals(listing.getLastModifiedDate(),cal);
		listing.setListingSource(listingSource);
		Assert.assertEquals(listing.getListingSource(),Integer.valueOf(listingSource));
		listing.setListingType(listingType);
		Assert.assertEquals(listing.getListingType(),Long.valueOf(listingType));
		Money listPrice=new Money();
		listing.setListPrice(listPrice);
		Assert.assertEquals(listing.getListPrice(),listPrice);
		listing.setLmsApprovalStatus(lmsApprovalStatus);
		Assert.assertEquals(listing.getLmsApprovalStatus(),Integer.valueOf(lmsApprovalStatus));
		listing.setQuantity(quantity);
		Assert.assertEquals(listing.getQuantity(),Integer.valueOf(quantity));
		listing.setQuantityRemain(quantityRemain);
		Assert.assertEquals(listing.getQuantityRemain(),Integer.valueOf(quantityRemain));
		listing.setRow(row);
		Assert.assertEquals(listing.getRow(),row);
		listing.setSaleMethod(saleMethod);
		Assert.assertEquals(listing.getSaleMethod(),Long.valueOf(saleMethod));
		listing.setSeats(seats);
		Assert.assertEquals(listing.getSeats(),seats);
		
		listing.setSection(section);
		Assert.assertEquals(listing.getSection(), section);
		listing.setSectionScrubExcluded(true);
		Assert.assertNotNull(listing.getSectionScrubExcluded());
		listing.setSectionScrubSchedule(true);
		Assert.assertNotNull(listing.getSectionScrubSchedule());
		listing.setSellerCCId(sellerCCId);
		Assert.assertEquals(listing.getSellerCCId(),Long.valueOf(sellerCCId));
		listing.setSellerCobrand(sellerCobrand);
		Assert.assertEquals(listing.getSellerCobrand(),sellerCobrand);
		listing.setSellerContactId(sellerContactId);
		Assert.assertEquals(listing.getSellerContactId(),Long.valueOf(sellerContactId));
		listing.setSellerId(sellerId);
		Assert.assertEquals(listing.getSellerId(),Long.valueOf(sellerId));
		listing.setSellerPaymentTypeId(sellerPaymentTypeId);
		Assert.assertEquals(listing.getSellerPaymentTypeId(),Long.valueOf(sellerPaymentTypeId));
		Money sellerPayoutAmountPerTicket=new Money();
		listing.setSellerPayoutAmountPerTicket(sellerPayoutAmountPerTicket);
		Assert.assertEquals(listing.getSellerPayoutAmountPerTicket(),sellerPayoutAmountPerTicket);
		listing.setSellFeeDescription(sellFeeDescription);
		Assert.assertEquals(listing.getSellFeeDescription(),sellFeeDescription);
		Money sellFeeValuePerTicket=new Money();
		listing.setSellFeeValuePerTicket(sellFeeValuePerTicket);
		Assert.assertEquals(listing.getSellFeeValuePerTicket(),sellFeeValuePerTicket);
		listing.setSplitOption(splitOption);
		Assert.assertEquals(listing.getSplitOption(),Short.valueOf(splitOption));
		listing.setSplitQuantity(splitQuantity);
		Assert.assertEquals(listing.getSplitQuantity(),Integer.valueOf(splitQuantity));
		listing.setStructuredComments(structuredComments);
		Assert.assertEquals(listing.getStructuredComments(),structuredComments);
		listing.setSystemStatus(systemStatus);
		Assert.assertEquals(listing.getSystemStatus(),systemStatus);
		listing.setTealeafSessionGuid(tealeafSessionGuid);
		Assert.assertEquals(listing.getTealeafSessionGuid(),tealeafSessionGuid);
		Money ticketCost=new Money();
		listing.setTicketCost(ticketCost);
		Assert.assertEquals(listing.getTicketCost(),ticketCost);
		int ticketMedium=2;
		listing.setTicketMedium(ticketMedium);
		Assert.assertEquals(listing.getTicketMedium(),Integer.valueOf(ticketMedium));
		
		Money totalListingPrice=new Money();
		listing.setTotalListingPrice(totalListingPrice);
		Assert.assertEquals(listing.getTotalListingPrice(),totalListingPrice);
		listing.setTotalSellerPayoutAmt(totalListingPrice);
		Assert.assertEquals(listing.getTotalSellerPayoutAmt(),totalListingPrice);
		listing.setTotalSellFeeValue(totalListingPrice);
		Assert.assertEquals(listing.getTotalSellFeeValue(),totalListingPrice);
		listing.setMinPricePerTicket(totalListingPrice);
		Assert.assertEquals(listing.getMinPricePerTicket(),totalListingPrice);
		listing.setMaxPricePerTicket(totalListingPrice);
		Assert.assertEquals(listing.getMaxPricePerTicket(),totalListingPrice);
		listing.setEventId(1L);
		Assert.assertEquals(listing.getEventId(),Long.valueOf("1"));
		Currency currency = Currency.getInstance("USD");
		listing.setCurrency(currency );
		Assert.assertEquals(listing.getCurrency(),currency);
		listing.setVenueConfigSectionsId(1L);
		Assert.assertEquals(listing.getVenueConfigSectionsId(),Long.valueOf("1"));
		listing.setVersion(1);
		Assert.assertEquals(listing.getVersion(),Integer.valueOf(1));
		listing.setSellerGuid("10001");
		Assert.assertEquals(listing.getSellerGuid(),"10001");
		List<Long> structuredCommentIds = new ArrayList<Long>();
		structuredCommentIds.add(101L);
		listing.setStructuredCommentIds(structuredCommentIds);
		Assert.assertEquals(listing.getStructuredCommentIds(),structuredCommentIds);
		

		Assert.assertNotNull(listing.hashCode());
		Assert.assertNotNull(new Listing().hashCode());
		Assert.assertTrue(listing.equals(listing));
		Assert.assertFalse(listing.equals(new Listing()));
		Assert.assertFalse(listing.equals(new Listing()));
		Assert.assertFalse(new Listing().equals(listing));
		Assert.assertFalse(new Listing().equals(null));
		Assert.assertEquals(listing, listing);
			
			
	
		Object other = new Listing();
		Object different = new String();
		
		
		Assert.assertNotNull(listing.hashCode());
		Assert.assertFalse(listing.equals(different));
		Assert.assertFalse(listing.equals(other));
		Assert.assertFalse(listing.equals(null));
		
		
		
		
		
		
		
		
		
		
	}

}
