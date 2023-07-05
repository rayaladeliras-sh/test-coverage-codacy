package com.stubhub.domain.account.datamodel.entity;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


import org.hibernate.annotations.Type;

@Entity
@Table(name = "STUB_TRANS_TEMP")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
//@NamedNativeQueries({
//@NamedNativeQuery(name = "StubTransTemp.getNextSequenceNumber", query = "" +
//		"SELECT " +
//		"TRANSACTIONS_SEQ.nextval as id" + 
//		"from DUAL")
//		})
public class StubTransTmp implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name="TRANSACTIONS_SEQ", sequenceName="TRANSACTIONS_SEQ", allocationSize=1, initialValue=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TRANSACTIONS_SEQ")
	private Long id;
	
	@Column(name = "BUYER_ID")
	private Long buyerId;
	@Column(name = "TICKET_ID")
	private Long ticketId;
	@Column(name = "SALE_METHOD")
	private Long saleMethod;
	@Column(name = "TICKET_COST")
	private Long ticketCost;
	@Column(name = "SHIP_COST")
	private Long shipCost;	
	@Column(name = "DATE_ADDED")
	@Type(type="com.stubhub.newplatform.persistence.adapter.UTCCalendarToTimestampAdapter")
	private Calendar dateAdded;
	@Column(name = "QUANTITY")
	private Long quantity;
	@Column(name = "CC_ID")
	private Long ccId;
	@Column(name = "BIDDER_COBRAND")
	private String bidderCobrand;
	@Column(name = "SEATS")
	private String seats;
	@Column(name = "DISCOUNT_COST")
	private Long discountCost;
	
	@Column(name = "SELLER_FEE")
	private String sellerFee;
	
	@Column(name = "BUYER_FEE")
	private String buyerFee;
	
	@Column(name = "SELLER_FEE_VAL")
	private Long sellerFeeVal;
	
	@Column(name = "BUYER_FEE_VAL")
	private Long buyerFeeVal;
	
	@Column(name = "LOGISTICS_METHOD_ID")
	private Long logisticsMethod;
	
	@Column(name = "LOGISTICS_METHOD_DESCRIPTION")
	private String logisticsMethodDescription;
	
	@Column(name = "DISCOUNT_LIST")
	private String discountList;
	
	@Column(name = "CONTACT_ID")
	private Long contactId;
	
	@Column(name = "DATE_LAST_MODIFIED")
	@Type(type="com.stubhub.newplatform.persistence.adapter.UTCCalendarToTimestampAdapter")
	private Calendar dateLastModified;
	
	@Column(name = "CURRENCY_CODE")
	private String currency;
	
	@Column(name = "GROUP_COMMENTS")
	private String groupComments;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(Long buyerId) {
		this.buyerId = buyerId;
	}

	public Long getTicketId() {
		return ticketId;
	}

	public void setTicketId(Long ticketId) {
		this.ticketId = ticketId;
	}

	public Long getSaleMethod() {
		return saleMethod;
	}

	public void setSaleMethod(Long saleMethod) {
		this.saleMethod = saleMethod;
	}

	public Long getTicketCost() {
		return ticketCost;
	}

	public void setTicketCost(Long ticketCost) {
		this.ticketCost = ticketCost;
	}

	public Long getShipCost() {
		return shipCost;
	}

	public void setShipCost(Long shipCost) {
		this.shipCost = shipCost;
	}

	public Calendar getDateAdded() {
		return dateAdded;
	}

	public void setDateAdded(Calendar dateAdded) {
		this.dateAdded = dateAdded;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public Long getCcId() {
		return ccId;
	}

	public void setCcId(Long ccId) {
		this.ccId = ccId;
	}

	public String getBidderCobrand() {
		return bidderCobrand;
	}

	public void setBidderCobrand(String bidderCobrand) {
		this.bidderCobrand = bidderCobrand;
	}

	public String getSeats() {
		return seats;
	}

	public void setSeats(String seats) {
		this.seats = seats;
	}

	public Long getDiscountCost() {
		return discountCost;
	}

	public void setDiscountCost(Long discountCost) {
		this.discountCost = discountCost;
	}

	public String getSellerFee() {
		return sellerFee;
	}

	public void setSellerFee(String sellerFee) {
		this.sellerFee = sellerFee;
	}

	public String getBuyerFee() {
		return buyerFee;
	}

	public void setBuyerFee(String buyerFee) {
		this.buyerFee = buyerFee;
	}

	public Long getSellerFeeVal() {
		return sellerFeeVal;
	}

	public void setSellerFeeVal(Long sellerFeeVal) {
		this.sellerFeeVal = sellerFeeVal;
	}

	public Long getBuyerFeeVal() {
		return buyerFeeVal;
	}

	public void setBuyerFeeVal(Long buyerFeeVal) {
		this.buyerFeeVal = buyerFeeVal;
	}

	public Long getLogisticsMethod() {
		return logisticsMethod;
	}

	public void setLogisticsMethod(Long logisticsMethod) {
		this.logisticsMethod = logisticsMethod;
	}

	public String getLogisticsMethodDescription() {
		return logisticsMethodDescription;
	}

	public void setLogisticsMethodDescription(String logisticsMethodDescription) {
		this.logisticsMethodDescription = logisticsMethodDescription;
	}

	public String getDiscountList() {
		return discountList;
	}

	public void setDiscountList(String discountList) {
		this.discountList = discountList;
	}

	public Long getContactId() {
		return contactId;
	}

	public void setContactId(Long contactId) {
		this.contactId = contactId;
	}

	public Calendar getDateLastModified() {
		return dateLastModified;
	}

	public void setDateLastModified(Calendar dateLastModified) {
		this.dateLastModified = dateLastModified;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getGroupComments() {
		return groupComments;
	}

	public void setGroupComments(String groupComments) {
		this.groupComments = groupComments;
	}
	
	
	
	
	
}