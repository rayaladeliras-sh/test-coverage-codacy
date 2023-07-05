package com.stubhub.domain.account.intf;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * Created by jicui on 8/31/15.
 * buyer_contact_id
 ticket_id,
 in_hand_date,
 event_id,
 event_date,
 event_date_local,
 ticket_medium
 purchased_seat_details(seatId,SRS,type,status)
 order_proc_sub_status
 isCancelled
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DeliverySpecification", propOrder = {
        "tid",
        "sellerId",
        "deliveryOptionId",
        "fulfillmentMethodId",
        "deliveryMethodId",
        "buyerId",
        "buyerContact",
        "ticketId",
        "inHandDate",
        "eventId",
        "eventDateUTC",
        "eventDateLocal",
        "ticketMedium",
        "orderProcSubStatusCode",
        "cancelled",
        "jdkTimeZone",
        "purchasedSeatDetails"
})
@XmlSeeAlso(value={SeatDetail.class,UserContactDetail.class})
public class DeliverySpecification {
    private Long tid;
    private Long sellerId;
    private Long deliveryOptionId;
    private Long fulfillmentMethodId;
    private Long deliveryMethodId;
    private Long buyerId;
    private UserContactDetail buyerContact;
    private Long ticketId;
    private String inHandDate;
    private Long eventId;
    private String eventDateUTC;
    private String eventDateLocal;
    private Long ticketMedium;
    private String orderProcSubStatusCode;
    private Long cancelled;
    private String jdkTimeZone;
    private List<SeatDetail> purchasedSeatDetails;

    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
    }

    public UserContactDetail getBuyerContact() {
        return buyerContact;
    }

    public void setBuyerContact(UserContactDetail buyerContact) {
        this.buyerContact = buyerContact;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
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

    public String getOrderProcSubStatusCode() {
        return orderProcSubStatusCode;
    }

    public void setOrderProcSubStatusCode(String orderProcSubStatusCode) {
        this.orderProcSubStatusCode = orderProcSubStatusCode;
    }

    public Long getCancelled() {
        return cancelled;
    }

    public void setCancelled(Long cancelled) {
        this.cancelled = cancelled;
    }

    public List<SeatDetail> getPurchasedSeatDetails() {
        return purchasedSeatDetails;
    }

    public void setPurchasedSeatDetails(List<SeatDetail> purchasedSeatDetails) {
        this.purchasedSeatDetails = purchasedSeatDetails;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public Long getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Long buyerId) {
        this.buyerId = buyerId;
    }

    public String getJdkTimeZone() {
        return jdkTimeZone;
    }

    public void setJdkTimeZone(String jdkTimeZone) {
        this.jdkTimeZone = jdkTimeZone;
    }

    public Long getDeliveryOptionId() {
        return deliveryOptionId;
    }

    public void setDeliveryOptionId(Long deliveryOptionId) {
        this.deliveryOptionId = deliveryOptionId;
    }

    public Long getFulfillmentMethodId() {
        return fulfillmentMethodId;
    }

    public void setFulfillmentMethodId(Long fulfillmentMethodId) {
        this.fulfillmentMethodId = fulfillmentMethodId;
    }

    public Long getDeliveryMethodId() {
        return deliveryMethodId;
    }

    public void setDeliveryMethodId(Long deliveryMethodId) {
        this.deliveryMethodId = deliveryMethodId;
    }
}
