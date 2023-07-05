package com.stubhub.domain.account.intf;

import com.stubhub.domain.account.common.Response;

import javax.persistence.Column;
import javax.xml.bind.annotation.*;
import java.util.Calendar;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InvoiceResponse", propOrder = { "invoice", "subTotal", "totalChkPayment", "orderNumber",
        "orderDate", "eventDescription", "venue", "eventDateLocal", "quantity", "orderAmount", "section", "rowDesc", "seats",
        "comment", "ticketFeatures", "ticketDisclosers", "deliveryMethod", "pricePerTicket", "commission",
        "shippingFee", "payeeEmailId", "paymentSentToGatewayDate", "acctLastFourDigits", "bankName", "sellerPaymentTypeId" })
@XmlRootElement(name = "InvoiceResponse")
public class InvoiceResponse extends Response {
    @XmlElement(name="Invoice", required = false)
    private Invoice invoice;

    @XmlElement(name = "SubTotal", required = false)
    private Money subTotal = new Money();

    @XmlElement(name = "TotalChkPayment", required = false)
    private Money totalChkPayment = new Money();

    @XmlElement(name = "OrderNumber", required = false)
    private Long orderNumber;

    @XmlElement(name = "OrderDate", required = false)
    private Calendar orderDate;

    @XmlElement(name = "EventName", required = false)
    private String eventDescription;

    @XmlElement(name = "Venue", required = false)
    private String venue;

    @XmlElement(name = "EventDate", required = false)
    private Calendar eventDateLocal;

    @XmlElement(name = "Quantity", required = false)
    private Integer quantity;

    @XmlElement(name = "OrderAmount", required = false)
    private Money orderAmount = new Money();

    @XmlElement(name = "Section", required = false)
    private String section;

    @XmlElement(name = "RowDesc", required = false)
    private String rowDesc;

    @XmlElement(name = "Seats", required = false)
    private String seats;

    @XmlElement(name = "Comment", required = false)
    private String comment;

    @XmlElement(name = "TicketFeatures", required = false)
    private String ticketFeatures;

    @XmlElement(name = "TicketDisclosers", required = false)
    private String ticketDisclosers;

    @XmlElement(name = "DeliveryMethod", required = false)
    private String deliveryMethod;

    @XmlElement(name = "PricePerTicket", required = false)
    private Money pricePerTicket;

    @XmlElement(name = "Commission", required = false)
    private Money commission;

    @XmlElement(name = "ShippingFee", required = false)
    private Money shippingFee;

    @XmlElement(name = "PayeeEmailID")
    private String payeeEmailId;

    @XmlElement(name = "PaymentSentToPaypalDate")
    private Calendar paymentSentToGatewayDate;

    @XmlElement(name = "acctLastFourDigits")
    private String acctLastFourDigits;

    @XmlElement(name = "bankName")
    private String bankName;

    @XmlElement(name = "sellerPaymentTypeId")
    private Long sellerPaymentTypeId;

    private Long sellerId;

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public Money getTotalChkPayment() {
        return totalChkPayment;
    }

    public void setTotalChkPayment(Money totalChkPayment) {
        this.totalChkPayment = totalChkPayment;
    }

    public Money getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Money subTotal) {
        this.subTotal = subTotal;
    }

    public Long getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Long orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Calendar getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Calendar orderDate) {
        this.orderDate = orderDate;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public Calendar getEventDateLocal() {
        return eventDateLocal;
    }

    public void setEventDateLocal(Calendar eventDateLocal) {
        this.eventDateLocal = eventDateLocal;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Money getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Money orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getRowDesc() {
        return rowDesc;
    }

    public void setRowDesc(String rowDesc) {
        this.rowDesc = rowDesc;
    }

    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Money getPricePerTicket() {
        return pricePerTicket;
    }

    public void setPricePerTicket(Money pricePerTicket) {
        this.pricePerTicket = pricePerTicket;
    }

    public Money getCommission() {
        return commission;
    }

    public void setCommission(Money commission) {
        this.commission = commission;
    }

    public String getTicketFeatures() {
        return ticketFeatures;
    }

    public void setTicketFeatures(String ticketFeatures) {
        this.ticketFeatures = ticketFeatures;
    }

    public String getTicketDisclosers() {
        return ticketDisclosers;
    }

    public void setTicketDisclosers(String ticketDisclosers) {
        this.ticketDisclosers = ticketDisclosers;
    }

    public String getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(String deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public Money getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(Money shippingFee) {
        this.shippingFee = shippingFee;
    }

    public String getPayeeEmailId() {
        return payeeEmailId;
    }

    public void setPayeeEmailId(String payeeEmailId) {
        this.payeeEmailId = payeeEmailId;
    }

    public Calendar getPaymentSentToGatewayDate() {
        return paymentSentToGatewayDate;
    }

    public void setPaymentSentToGatewayDate(Calendar paymentSentToGatewayDate) {
        this.paymentSentToGatewayDate = paymentSentToGatewayDate;
    }

	public Long getSellerId() {
		return sellerId;
	}

	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}

    public String getAcctLastFourDigits() {
        return acctLastFourDigits;
    }

    public void setAcctLastFourDigits(String acctLastFourDigits) {
        this.acctLastFourDigits = acctLastFourDigits;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    public Long getSellerPaymentTypeId() { return sellerPaymentTypeId; }

    public void setSellerPaymentTypeId(Long sellerPaymentTypeId) { this.sellerPaymentTypeId = sellerPaymentTypeId; }

}
