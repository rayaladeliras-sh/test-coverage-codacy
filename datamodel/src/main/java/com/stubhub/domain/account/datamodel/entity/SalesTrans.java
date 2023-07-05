package com.stubhub.domain.account.datamodel.entity;


import java.util.Calendar;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.stubhub.newplatform.common.entity.Money;

@Entity
@Table(name = "SALES_TRANSACTIONS")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)	
@NamedNativeQueries({
@NamedNativeQuery(name = "SalesTrans.getByTid", query = "" +
		"SELECT " +
		"st.* FROM sales_transactions st " + 
		"WHERE st.tid =:arg1 ", resultClass=SalesTrans.class),
@NamedNativeQuery(name = "SalesTrans.getTidByBuyerID", query = "" +
		"SELECT " +
		"st.* FROM sales_transactions st " + 
		"WHERE st.buyer_id =:arg1 ", resultClass=SalesTrans.class),
@NamedNativeQuery(name = "SalesTrans.getTidByEventDate", query = "" +
		"SELECT " +
		"st.* FROM sales_transactions st " + 
		"WHERE st.event_date between :eventStartDate and :eventEndDate ", resultClass=SalesTrans.class),
@NamedNativeQuery(name = "SalesTrans.getTidByBuyerIDAndEventDate", query = "" +
		"SELECT " +
		"st.* FROM sales_transactions st " + 
		"WHERE st.buyer_id =:arg1 and st.event_date between :eventStartDate and :eventEndDate ", resultClass=SalesTrans.class)})

public class SalesTrans implements java.io.Serializable {
	
	@Id
	@Column(name = "TID")
	private Long saleId;
	
	@Column(name = "TICKET_ID")
	private String listingId;
	
	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "amount", column = @Column(name = "TOTAL_COST")),
			@AttributeOverride(name = "currency", column = @Column(name = "CURRENCY_CODE", insertable = false, updatable = false)) })
	private Money totalCost;
	
	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "amount", column = @Column(name = "TICKET_COST")),
			@AttributeOverride(name = "currency", column = @Column(name = "CURRENCY_CODE", insertable = false, updatable = false)) })
	private Money ticketCost;
	
	@Column(name = "QUANTITY")
    private Long quantityPurchased;
	
	@Column(name = "SECTION")
    private String section;
	
	@Column(name = "ROW_DESC")
    private String row;
	
	@Column(name = "BUYER_ID")
    private String buyerId;
	
	@Column(name = "CANCELLED_IND")
    private Boolean cancelled;
	
	@Column(name = "BUYER_SHIPPING_CONTACT_ID")
	private Long buyerContactId;
	
	@Column(name = "TRANSACTION_DATE")
	private Calendar saleDateUTC;
	
	@Column(name = "SUBBED_IND")
	private Boolean subbedFlag;
	
	@Column(name = "SUBBED_TID")
    private String subbedOrderId;
	
	@Column(name = "EVENT_ID")
    private String eventId;
	
	@Column(name = "EVENT_DATE")
	private Calendar eventDateUTC;
	
	@Column(name = "DELIVERY_METHOD_ID")
	private Long deliveryMethodId;
	
	@Column(name = "EXPECTED_DELIVERY_DATE")
	private Calendar expectedArrivalDateUTC;
	
	@Column(name = "INHAND_DATE")
	private Calendar inHandDateUTC;
	
	@Column(name = "SHIP_DATE")
	private Calendar shipDateUTC;
	
	@Column(name = "TRACKING_NUMBER")
	private String trackingNumber;
	
	@Column(name = "ORDER_PROC_SUB_STATUS_CODE")
	private String saleProcSubStatusCode;
	
	@Column(name = "CURRENCY_CODE")
	private String currency;
	
	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "amount", column = @Column(name = "VAT_BUY_FEE")),
			@AttributeOverride(name = "currency", column = @Column(name = "CURRENCY_CODE", insertable = false, updatable = false)) })
	private Money buyVAT;
	
	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "amount", column = @Column(name = "VAT_SELL_FEE")),
			@AttributeOverride(name = "currency", column = @Column(name = "CURRENCY_CODE", insertable = false, updatable = false)) })
	private Money sellVAT;
	
	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "amount", column = @Column(name = "SHIPPING_FEE_COST")),
			@AttributeOverride(name = "currency", column = @Column(name = "CURRENCY_CODE", insertable = false, updatable = false)) })
	private Money shippingFeeCost;
	
	
	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "amount", column = @Column(name = "DISCOUNT_COST")),
			@AttributeOverride(name = "currency", column = @Column(name = "CURRENCY_CODE", insertable = false, updatable = false)) })
	private Money discountCost;


	public Long getSaleId() {
		return saleId;
	}


	public void setSaleId(Long saleId) {
		this.saleId = saleId;
	}


	public String getListingId() {
		return listingId;
	}


	public void setListingId(String listingId) {
		this.listingId = listingId;
	}


	public Money getTotalCost() {
		return totalCost;
	}


	public void setTotalCost(Money totalCost) {
		this.totalCost = totalCost;
	}


	public Long getQuantityPurchased() {
		return quantityPurchased;
	}


	public void setQuantityPurchased(Long quantityPurchased) {
		this.quantityPurchased = quantityPurchased;
	}


	public String getSection() {
		return section;
	}


	public void setSection(String section) {
		this.section = section;
	}


	public String getRow() {
		return row;
	}


	public void setRow(String row) {
		this.row = row;
	}


	public String getBuyerId() {
		return buyerId;
	}


	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}


	public Boolean getCancelled() {
		return cancelled;
	}


	public void setCancelled(Boolean cancelled) {
		this.cancelled = cancelled;
	}


	public Long getBuyerContactId() {
		return buyerContactId;
	}


	public void setBuyerContactId(Long buyerContactId) {
		this.buyerContactId = buyerContactId;
	}


	public Calendar getSaleDateUTC() {
		return saleDateUTC;
	}


	public void setSaleDateUTC(Calendar saleDateUTC) {
		this.saleDateUTC = saleDateUTC;
	}


	public Boolean getSubbedFlag() {
		return subbedFlag;
	}


	public void setSubbedFlag(Boolean subbedFlag) {
		this.subbedFlag = subbedFlag;
	}


	public String getSubbedOrderId() {
		return subbedOrderId;
	}


	public void setSubbedOrderId(String subbedOrderId) {
		this.subbedOrderId = subbedOrderId;
	}


	public String getEventId() {
		return eventId;
	}


	public void setEventId(String eventId) {
		this.eventId = eventId;
	}


	public Calendar getEventDateUTC() {
		return eventDateUTC;
	}


	public void setEventDateUTC(Calendar eventDateUTC) {
		this.eventDateUTC = eventDateUTC;
	}


	public Long getDeliveryMethodId() {
		return deliveryMethodId;
	}


	public void setDeliveryMethodId(Long deliveryMethodId) {
		this.deliveryMethodId = deliveryMethodId;
	}


	public Calendar getExpectedArrivalDateUTC() {
		return expectedArrivalDateUTC;
	}


	public void setExpectedArrivalDateUTC(Calendar expectedArrivalDateUTC) {
		this.expectedArrivalDateUTC = expectedArrivalDateUTC;
	}


	public Calendar getInHandDateUTC() {
		return inHandDateUTC;
	}


	public void setInHandDateUTC(Calendar inHandDateUTC) {
		this.inHandDateUTC = inHandDateUTC;
	}


	public Calendar getShipDateUTC() {
		return shipDateUTC;
	}


	public void setShipDateUTC(Calendar shipDateUTC) {
		this.shipDateUTC = shipDateUTC;
	}


	public String getTrackingNumber() {
		return trackingNumber;
	}


	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}


	public String getSaleProcSubStatusCode() {
		return saleProcSubStatusCode;
	}


	public void setSaleProcSubStatusCode(String saleProcSubStatusCode) {
		this.saleProcSubStatusCode = saleProcSubStatusCode;
	}


	public String getCurrency() {
		return currency;
	}


	public void setCurrency(String currency) {
		this.currency = currency;
	}


	public Money getBuyVAT() {
		return buyVAT;
	}


	public void setBuyVAT(Money buyVAT) {
		this.buyVAT = buyVAT;
	}


	public Money getSellVAT() {
		return sellVAT;
	}


	public void setSellVAT(Money sellVAT) {
		this.sellVAT = sellVAT;
	}


	public Money getShippingFeeCost() {
		return shippingFeeCost;
	}


	public void setShippingFeeCost(Money shippingFeeCost) {
		this.shippingFeeCost = shippingFeeCost;
	}


	public Money getDiscountCost() {
		return discountCost;
	}


	public void setDiscountCost(Money discountCost) {
		this.discountCost = discountCost;
	}


	public Money getTicketCost() {
		return ticketCost;
	}


	public void setTicketCost(Money ticketCost) {
		this.ticketCost = ticketCost;
	}
	
	
	
}




