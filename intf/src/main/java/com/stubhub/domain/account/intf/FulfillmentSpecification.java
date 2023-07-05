package com.stubhub.domain.account.intf;

import com.stubhub.domain.account.common.Response;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Created by jicui on 8/31/15.
 */

/**
 * seller_contact_id(country,state,city,postalCode)
 in_hand_date
 event_id,
 event_date,
 event_date_local,
 ticket_medium,
 listing_source,
 delivery_option
 lms_approve_status
 splitOption
 systemStatus
 saleEndDate
 quantityRemain
 ticket_seat_details(seatId,SRS,type,status)
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FulfillmentSpecification", propOrder = {
        "ticketId",
        "sellerId",
        "sellerContact",
        "eventCountry",
        "inHandDate",
        "eventId",
        "eventDateUTC",
        "eventDateLocal",
        "ticketMedium",
        "listingSourceId",
        "deliveryOptionId",
        "lmsApprovalStatusId",
        "splitOption",
        "systemStatus",
        "saleEndDate",
        "jdkTimeZone",
        "quantityRemain",
        "seatDetailList"})
@XmlSeeAlso(value={SeatDetail.class,UserContactDetail.class})
public class FulfillmentSpecification {
    private Long ticketId;
    private Long sellerId;
    private UserContactDetail sellerContact;
    private String eventCountry;
    private String inHandDate;//ISO-8601 format
    private Long eventId;
    private String eventDateUTC;
    private String eventDateLocal;
    private Long ticketMedium;
    private Long listingSourceId;
    private Long deliveryOptionId;
    private Long lmsApprovalStatusId;
    private Long splitOption;
    private String systemStatus;
    private String saleEndDate;
    private String jdkTimeZone;
    private Long quantityRemain;
    private List<SeatDetail> seatDetailList;

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public UserContactDetail getSellerContact() {
        return sellerContact;
    }

    public void setSellerContact(UserContactDetail sellerContact) {
        this.sellerContact = sellerContact;
    }

    public String getInHandDate() {
        return inHandDate;
    }

    public void setInHandDate(String inHandDate) {
        this.inHandDate = inHandDate;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getEventDateUTC() {
        return eventDateUTC;
    }

    public void setEventDateUTC(String eventDateUTC) {
        this.eventDateUTC = eventDateUTC;
    }

    public String getEventDateLocal() {
        return eventDateLocal;
    }

    public void setEventDateLocal(String eventDateLocal) {
        this.eventDateLocal = eventDateLocal;
    }

    public Long getTicketMedium() {
        return ticketMedium;
    }

    public void setTicketMedium(Long ticketMedium) {
        this.ticketMedium = ticketMedium;
    }

    public Long getListingSourceId() {
        return listingSourceId;
    }

    public void setListingSourceId(Long listingSourceId) {
        this.listingSourceId = listingSourceId;
    }

    public Long getDeliveryOptionId() {
        return deliveryOptionId;
    }

    public void setDeliveryOptionId(Long deliveryOptionId) {
        this.deliveryOptionId = deliveryOptionId;
    }

    public Long getLmsApprovalStatusId() {
        return lmsApprovalStatusId;
    }

    public void setLmsApprovalStatusId(Long lmsApprovalStatusId) {
        this.lmsApprovalStatusId = lmsApprovalStatusId;
    }

    public Long getSplitOption() {
        return splitOption;
    }

    public void setSplitOption(Long splitOption) {
        this.splitOption = splitOption;
    }

    public String getSystemStatus() {
        return systemStatus;
    }

    public void setSystemStatus(String systemStatus) {
        this.systemStatus = systemStatus;
    }

    public String getSaleEndDate() {
        return saleEndDate;
    }

    public void setSaleEndDate(String saleEndDate) {
        this.saleEndDate = saleEndDate;
    }

    public Long getQuantityRemain() {
        return quantityRemain;
    }

    public void setQuantityRemain(Long quantityRemain) {
        this.quantityRemain = quantityRemain;
    }

    public List<SeatDetail> getSeatDetailList() {
        return seatDetailList;
    }

    public void setSeatDetailList(List<SeatDetail> seatDetailList) {
        this.seatDetailList = seatDetailList;
    }

    public String getEventCountry() {
        return eventCountry;
    }

    public void setEventCountry(String eventCountry) {
        this.eventCountry = eventCountry;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public String getJdkTimeZone() {
        return jdkTimeZone;
    }

    public void setJdkTimeZone(String jdkTimeZone) {
        this.jdkTimeZone = jdkTimeZone;
    }
}
