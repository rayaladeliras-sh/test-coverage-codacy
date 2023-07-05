package com.stubhub.domain.account.intf;

import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.HashSet;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.stubhub.domain.account.common.enums.DeliveryOption;
import com.stubhub.domain.account.common.enums.SaleStatus;
import com.stubhub.domain.account.common.enums.SalesSubStatus;
import com.stubhub.domain.account.common.enums.TicketTrait;
import com.stubhub.newplatform.common.entity.Money;

public class SaleResponseTest {

    @Test
    public void testGetSet() {
        SaleResponse saleResponse = new SaleResponse();
        String comments = "COMMENT";
        String externalId = "231";
        String id = "34";

        int quantity = 1;
        String row = "Middle";
        String seats = "2345";
        String section = "G";
        int split = 2;
        Money m = new Money();

        try {
            GregorianCalendar gcal = new GregorianCalendar();
            XMLGregorianCalendar inhandDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
            XMLGregorianCalendar saleEndDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
            saleResponse.setInternalNotes(comments);
            Assert.assertEquals(saleResponse.getInternalNotes(), comments);
            saleResponse.setDeliveryOption(DeliveryOption.BARCODE);
            Assert.assertEquals(saleResponse.getDeliveryOption(), DeliveryOption.BARCODE);
            saleResponse.setEventId("10001");
            Assert.assertEquals(saleResponse.getEventId(), "10001");
            saleResponse.setExternalListingId(externalId);
            Assert.assertEquals(saleResponse.getExternalListingId(), externalId);

            saleResponse.setQuantity(quantity);
            Assert.assertEquals(saleResponse.getQuantity(), Integer.valueOf(quantity));
            saleResponse.setSeats(seats);
            Assert.assertEquals(saleResponse.getSeats(), seats);
            saleResponse.setSection(section);
            Assert.assertEquals(saleResponse.getSection(), section);
            saleResponse.setContactId(2L);
            Assert.assertEquals(saleResponse.getContactId(), new Long(2L));
            saleResponse.setPaymentType("2");
            Assert.assertEquals(saleResponse.getPaymentType(), "2");
            saleResponse.setTicketTraits(new HashSet<TicketTrait>());
            Assert.assertNotNull(saleResponse.getTicketTraits());
            saleResponse.setDisplayPricePerTicket(m);
            Assert.assertEquals(saleResponse.getDisplayPricePerTicket(), m);
            saleResponse.setVenueDescription("test");
            Assert.assertEquals(saleResponse.getVenueDescription(), "test");
            saleResponse.setEventDescription("test");
            Assert.assertEquals(saleResponse.getEventDescription(), "test");
            saleResponse.setEventDate("2013-04-29T16:00:00.000Z");
            Assert.assertEquals(saleResponse.getEventDate(), "2013-04-29T16:00:00.000Z");
            saleResponse.setHideEventDate(Boolean.FALSE);
            Assert.assertEquals(saleResponse.getHideEventDate(), Boolean.FALSE);
            saleResponse.setHideEventTime(Boolean.FALSE);
            Assert.assertEquals(saleResponse.getHideEventTime(), Boolean.FALSE);

            saleResponse.setSaleId("123456");
            Assert.assertEquals(saleResponse.getSaleId(), "123456");
            saleResponse.setInhandDate("2013-04-29T16:00:00.000Z");
            Assert.assertEquals(saleResponse.getInhandDate(), "2013-04-29T16:00:00.000Z");
            saleResponse.setListingId("123456");
            Assert.assertEquals(saleResponse.getListingId(), "123456");
            saleResponse.setEventTimeZone("PST");
            Assert.assertEquals(saleResponse.getEventTimeZone(), "PST");

            saleResponse.setSaleDate("2013-04-29T16:00:00.000Z");
            Assert.assertEquals(saleResponse.getSaleDate(), "2013-04-29T16:00:00.000Z");
            saleResponse.setDateLastModified("2013-04-29T16:00:00.000Z");
            Assert.assertEquals(saleResponse.getDateLastModified(), "2013-04-29T16:00:00.000Z");
            saleResponse.setPayoutPerTicket(m);
            Assert.assertEquals(saleResponse.getPayoutPerTicket(), m);
            saleResponse.setPricePerTicket(m);
            Assert.assertEquals(saleResponse.getPricePerTicket(), m);
            saleResponse.setRows("1");
            saleResponse.setRow("1");
            Assert.assertEquals(saleResponse.getRows(), "1");
            Assert.assertEquals(saleResponse.getRow(), "1");
            saleResponse.setStatus(SaleStatus.CONFIRMED);
            Assert.assertEquals(saleResponse.getStatus(), SaleStatus.CONFIRMED);

            saleResponse.setEarliestInhandDate("2013-04-29T16:00:00.000Z");
            Assert.assertEquals(saleResponse.getEarliestInhandDate(),"2013-04-29T16:00:00.000Z");
            saleResponse.seteInhandDateLaterThanNow(true);
            Assert.assertTrue(saleResponse.geteInhandDateLaterThanNow());

            saleResponse.setFulfillmentMethodId(10);
            Assert.assertEquals(saleResponse.getFulfillmentMethodId(), new Integer(10));

            saleResponse.setSellerContactId(100000L);
            Assert.assertEquals(saleResponse.getSellerContactId(), new Long(100000L));

            saleResponse.setCity("SF");
            Assert.assertEquals(saleResponse.getCity(), "SF");

            saleResponse.setState("CA");
            Assert.assertEquals(saleResponse.getState(), "CA");

            saleResponse.setTrackingNumber("ABCDEFGH");
            Assert.assertEquals(saleResponse.getTrackingNumber(), "ABCDEFGH");

            saleResponse.setSubStatus(SalesSubStatus.DROPPED_SALE);
            Assert.assertEquals(saleResponse.getSubStatus(), SalesSubStatus.DROPPED_SALE);

            saleResponse.setInHand(Boolean.TRUE);
            Assert.assertEquals(saleResponse.getInHand(), Boolean.TRUE);

            saleResponse.setInhandDatePST("");
            saleResponse.getInhandDatePST();
            
            saleResponse.setLinks(null);
			Assert.assertNull(saleResponse.getLinks());		
            
            saleResponse.setLinks(Arrays.asList(new Link()));
			Assert.assertNotNull(saleResponse.getLinks());		

            //TODO: this is useless test. Need to first implement hashCode in Response class and then override hashcode/equals in SaleResponse.
            Assert.assertNotNull(saleResponse.hashCode());
            Assert.assertNotNull(new SaleResponse().hashCode());
            Assert.assertTrue(saleResponse.equals(saleResponse));
            Assert.assertFalse(saleResponse.equals(new SaleResponse()));
            Assert.assertFalse(new SaleResponse().equals(saleResponse));
            Assert.assertFalse(new SaleResponse().equals(null));
            Assert.assertEquals(saleResponse, saleResponse);
            Assert.assertNotEquals(saleResponse, new Integer(0));

            Object other = new SaleResponse();
            Object different = new String();

            Assert.assertNotNull(saleResponse.hashCode());
            Assert.assertFalse(saleResponse.equals(different));
            Assert.assertFalse(saleResponse.equals(other));
            Assert.assertFalse(saleResponse.equals(null));
            saleResponse.setAttendeeCompleted(saleResponse.getAttendeeCompleted());
            saleResponse.setAttendeeConfigId(saleResponse.getAttendeeConfigId());
            Assert.assertFalse(saleResponse.getAttendeeCompleted());
            Assert.assertNull(saleResponse.getAttendeeConfigId());
            saleResponse.setBuyerPhoneNumber(saleResponse.getBuyerPhoneNumber());
            saleResponse.setBuyerPhoneCallingCode(saleResponse.getBuyerPhoneCallingCode());
            } catch (Exception e) {
        }
    }

}
