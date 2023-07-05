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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.stubhub.newplatform.common.entity.Money;

@Entity
@Table(name = "STUB_TRANS_ADJ_HIST")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class StubTransAdjHist implements java.io.Serializable {

	@Id
	@Column(name = "STUB_TRANS_ADJ_HIST_ID")
	@SequenceGenerator(name="STUB_TRANS_ADJ_HIST_ID", sequenceName="STUB_TRANS_ADJ_HIST_ID")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="STUB_TRANS_ADJ_HIST_ID")
	private Long stubTransAdjHistId;
	@Column(name = "TID")
	private Long tid;
	@Column(name = "BUYER_ID")
	private Long buyerId;
	@Column(name = "TICKET_ID")
	private Long ticketId;
	@Column(name = "SALE_METHOD")
	private Long saleMethod;
	@Embedded
    @AttributeOverrides({
            @AttributeOverride(name="amount", column = @Column(name="TICKET_COST_BEFORE_ADJ")),
            @AttributeOverride(name="currency", column = @Column(name="CURRENCY_CODE", insertable=false, updatable=false))
    })
	private Money ticketCostBeforeAdj;
	@Embedded
    @AttributeOverrides({
            @AttributeOverride(name="amount", column = @Column(name="TICKET_COST_AFTER_ADJ")),
            @AttributeOverride(name="currency", column = @Column(name="CURRENCY_CODE", insertable=false, updatable=false))
    })
	private Money ticketCostAfterAdj;
	@Embedded
    @AttributeOverrides({
            @AttributeOverride(name="amount", column = @Column(name="SHIP_COST_BEFORE_ADJ")),
            @AttributeOverride(name="currency", column = @Column(name="CURRENCY_CODE", insertable=false, updatable=false))
    })
	private Money shipCostBeforeAdj;
	@Embedded
    @AttributeOverrides({
            @AttributeOverride(name="amount", column = @Column(name="SHIP_COST_AFTER_ADJ")),
            @AttributeOverride(name="currency", column = @Column(name="CURRENCY_CODE", insertable=false, updatable=false))
    })
	private Money shipCostAfterAdj;
	@Column(name = "DATE_ADDED")
	@Type(type="com.stubhub.newplatform.persistence.adapter.UTCCalendarToTimestampAdapter")
	private Calendar dateAdded;
	@Embedded
    @AttributeOverrides({
            @AttributeOverride(name="amount", column = @Column(name="TOTAL_COST_BEFORE_ADJ")),
            @AttributeOverride(name="currency", column = @Column(name="CURRENCY_CODE", insertable=false, updatable=false))
    })
	private Money totalCostBeforeAdj;
	@Embedded
    @AttributeOverrides({
            @AttributeOverride(name="amount", column = @Column(name="TOTAL_COST_AFTER_ADJ")),
            @AttributeOverride(name="currency", column = @Column(name="CURRENCY_CODE", insertable=false, updatable=false))
    })
	private Money totalCostAfterAdj;
	@Column(name = "QUANTITY")
	private Long quantity;
	@Column(name = "CC_ID")
	private Long ccId;
	@Column(name = "BIDDER_COBRAND")
	private String bidderCobrand;
	@Column(name = "SECTION")
	private String section;	
	@Column(name = "ROW_DESC")
	private String rowDesc;
	@Column(name = "SEATS")
	private String seats;
	@Column(name = "CANCELLED")
	private boolean cancelled;
	@Embedded
    @AttributeOverrides({
            @AttributeOverride(name="amount", column = @Column(name="DISCOUNT_COST_BEFORE_ADJ")),
            @AttributeOverride(name="currency", column = @Column(name="CURRENCY_CODE", insertable=false, updatable=false))
    })
	private Money discountCostBeforeAdj;
	@Embedded
    @AttributeOverrides({
            @AttributeOverride(name="amount", column = @Column(name="DISCOUNT_COST_AFTER_ADJ")),
            @AttributeOverride(name="currency", column = @Column(name="CURRENCY_CODE", insertable=false, updatable=false))
    })
	private Money discountCostAfterAdj;
	@Column(name = "SELLER_FEE")
	private String sellerFee;
	@Column(name = "BUYER_FEE")
	private String buyerFee;
	@Embedded
    @AttributeOverrides({
            @AttributeOverride(name="amount", column = @Column(name="SELLER_FEE_VAL_BEFORE_ADJ")),
            @AttributeOverride(name="currency", column = @Column(name="CURRENCY_CODE", insertable=false, updatable=false))
    })
	private Money sellerFeeValBeforeAdj;
	@Embedded
    @AttributeOverrides({
            @AttributeOverride(name="amount", column = @Column(name="SELL_FEE_VAL_AFTER_ADJ")),
            @AttributeOverride(name="currency", column = @Column(name="CURRENCY_CODE", insertable=false, updatable=false))
    })
	private Money sellFeeValAfterAdj;
	@Embedded
    @AttributeOverrides({
            @AttributeOverride(name="amount", column = @Column(name="BUYER_FEE_VAL_BEFORE_ADJ")),
            @AttributeOverride(name="currency", column = @Column(name="CURRENCY_CODE", insertable=false, updatable=false))
    })
	private Money buyerFeeValBeforeAdj;
	@Embedded
    @AttributeOverrides({
            @AttributeOverride(name="amount", column = @Column(name="BUYER_FEE_VAL_AFTER_ADJ")),
            @AttributeOverride(name="currency", column = @Column(name="CURRENCY_CODE", insertable=false, updatable=false))
    })
	private Money buyerFeeValAfterAdj;
	@Embedded
    @AttributeOverrides({
            @AttributeOverride(name="amount", column = @Column(name="PREMIUM_FEES_BEFORE_ADJ")),
            @AttributeOverride(name="currency", column = @Column(name="CURRENCY_CODE", insertable=false, updatable=false))
    })
	private Money premiumFeesBeforeAdj;
	@Embedded
    @AttributeOverrides({
            @AttributeOverride(name="amount", column = @Column(name="PREMIUM_FEES_AFTER_ADJ")),
            @AttributeOverride(name="currency", column = @Column(name="CURRENCY_CODE", insertable=false, updatable=false))
    })
	private Money premiumFeesAfterAdj;
	@Column(name = "LOGISTICS_METHOD_ID")
	private Long logisticsMethodId;
	@Column(name = "ATTENTION_FLAG")
	private boolean attentionFlag;
	@Column(name = "GROUP_COMMENTS")
	private String groupComments;
	@Column(name = "CONTACT_ID")
	private Long contactId;
	@Column(name = "AFFILIATE_ID")
	private Long affiliateId;
	@Embedded
    @AttributeOverrides({
            @AttributeOverride(name="amount", column = @Column(name="SELLER_PAYOUT_AMT_BEFORE_ADJ")),
            @AttributeOverride(name="currency", column = @Column(name="CURRENCY_CODE", insertable=false, updatable=false))
    })
	private Money sellerPayoutAmtBeforeAdj;
	@Embedded
    @AttributeOverrides({
            @AttributeOverride(name="amount", column = @Column(name="SELLER_PAYOUT_AMT_AFTER_ADJ")),
            @AttributeOverride(name="currency", column = @Column(name="CURRENCY_CODE", insertable=false, updatable=false))
    })
	private Money sellerPayoutAmtAfterAdj;
	@Column(name = "ORDER_DATE_LAST_MODIFIED")
	@Type(type="com.stubhub.newplatform.persistence.adapter.UTCCalendarToTimestampAdapter")
	private Calendar orderDateLastModified;
	@Column(name = "SELLER_CONFIRMED")
	private boolean sellerConfirmed;
	@Column(name = "SELLER_ID")
	private Long sellerId;
	@Column(name = "PID")
	private Long pid;
	@Column(name = "SELLER_PAYMENT_TYPE_ID")
	private Long sellerPaymentTypeId;
	@Column(name = "SELLER_COBRAND")
	private String sellerCobrand;
	@Column(name = "ACTIVE_CS_FLAG")
	private Long activeCsFlag;
	@Column(name = "EVENT_ID")
	private Long eventId;
	@Column(name = "SELLER_COMMENTS")
	private String sellerComments;
	@Column(name = "PAYMENT_TERMS_ID")
	private Long paymentTermsId;
	@Column(name = "LATEST_ORD_PROC_STATUS")
	private Long latestOrdProcStatus;
	@Column(name = "HOLD_PAYMENT_TYPE_ID")
	private Long holdPaymentTypeId;
	@Embedded
    @AttributeOverrides({
            @AttributeOverride(name="amount", column = @Column(name="SELLER_PAYOUT_AMOUNT_AT_CONFRM")),
            @AttributeOverride(name="currency", column = @Column(name="CURRENCY_CODE", insertable=false, updatable=false))
    })
	private Money sellerPayoutAmountAtConfrm;
	@Column(name = "REASON_CODE")
	private Long reasonCode;
	@Column(name = "ORDER_CREATED_BY")
	private String orderCreatedBy;
	@Column(name = "ORDER_LAST_UPDATED_BY")
	private String orderLastUpdatedBy;
	@Column(name = "CREATED_DATE")
	@Type(type="com.stubhub.newplatform.persistence.adapter.UTCCalendarToTimestampAdapter")
	private Calendar createdDate;
	@Column(name = "CREATED_BY")
	private String createdBy;
	@Column(name = "LAST_UPDATED_DATE")
	@Type(type="com.stubhub.newplatform.persistence.adapter.UTCCalendarToTimestampAdapter")
	private Calendar lastUpdatedDate;
	@Column(name = "LAST_UPDATED_BY")
	private String lastUpdatedBy;
	@Column(name = "CURRENCY_CODE")
	private String currency;
	@Embedded
    @AttributeOverrides({
            @AttributeOverride(name="amount", column = @Column(name="VAT_BUY_FEE")),
            @AttributeOverride(name="currency", column = @Column(name="CURRENCY_CODE", insertable=false, updatable=false))
    })
	private Money buyVAT;
	@Embedded
    @AttributeOverrides({
            @AttributeOverride(name="amount", column = @Column(name="VAT_LOG_FEE")),
            @AttributeOverride(name="currency", column = @Column(name="CURRENCY_CODE", insertable=false, updatable=false))
    })
	private Money logisticVAT;
	@Embedded
    @AttributeOverrides({
            @AttributeOverride(name="amount", column = @Column(name="VAT_SELL_FEE")),
            @AttributeOverride(name="currency", column = @Column(name="CURRENCY_CODE", insertable=false, updatable=false))
    })
	private Money sellVAT;
	public Long getStubTransAdjHistId() {
		return stubTransAdjHistId;
	}
	public void setStubTransAdjHistId(Long stubTransAdjHistId) {
		this.stubTransAdjHistId = stubTransAdjHistId;
	}
	public Long getTid() {
		return tid;
	}
	public void setTid(Long tid) {
		this.tid = tid;
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
	public Money getTicketCostBeforeAdj() {
		return ticketCostBeforeAdj;
	}
	public void setTicketCostBeforeAdj(Money ticketCostBeforeAdj) {
		this.ticketCostBeforeAdj = ticketCostBeforeAdj;
	}
	public Money getTicketCostAfterAdj() {
		return ticketCostAfterAdj;
	}
	public void setTicketCostAfterAdj(Money ticketCostAfterAdj) {
		this.ticketCostAfterAdj = ticketCostAfterAdj;
	}
	public Money getShipCostBeforeAdj() {
		return shipCostBeforeAdj;
	}
	public void setShipCostBeforeAdj(Money shipCostBeforeAdj) {
		this.shipCostBeforeAdj = shipCostBeforeAdj;
	}
	public Money getShipCostAfterAdj() {
		return shipCostAfterAdj;
	}
	public void setShipCostAfterAdj(Money shipCostAfterAdj) {
		this.shipCostAfterAdj = shipCostAfterAdj;
	}
	public Calendar getDateAdded() {
		return dateAdded;
	}
	public void setDateAdded(Calendar dateAdded) {
		this.dateAdded = dateAdded;
	}
	public Money getTotalCostBeforeAdj() {
		return totalCostBeforeAdj;
	}
	public void setTotalCostBeforeAdj(Money totalCostBeforeAdj) {
		this.totalCostBeforeAdj = totalCostBeforeAdj;
	}
	public Money getTotalCostAfterAdj() {
		return totalCostAfterAdj;
	}
	public void setTotalCostAfterAdj(Money totalCostAfterAdj) {
		this.totalCostAfterAdj = totalCostAfterAdj;
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
	public boolean isCancelled() {
		return cancelled;
	}
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
	public Money getDiscountCostBeforeAdj() {
		return discountCostBeforeAdj;
	}
	public void setDiscountCostBeforeAdj(Money discountCostBeforeAdj) {
		this.discountCostBeforeAdj = discountCostBeforeAdj;
	}
	public Money getDiscountCostAfterAdj() {
		return discountCostAfterAdj;
	}
	public void setDiscountCostAfterAdj(Money discountCostAfterAdj) {
		this.discountCostAfterAdj = discountCostAfterAdj;
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
	public Money getSellerFeeValBeforeAdj() {
		return sellerFeeValBeforeAdj;
	}
	public void setSellerFeeValBeforeAdj(Money sellerFeeValBeforeAdj) {
		this.sellerFeeValBeforeAdj = sellerFeeValBeforeAdj;
	}
	public Money getSellFeeValAfterAdj() {
		return sellFeeValAfterAdj;
	}
	public void setSellFeeValAfterAdj(Money sellFeeValAfterAdj) {
		this.sellFeeValAfterAdj = sellFeeValAfterAdj;
	}
	public Money getBuyerFeeValBeforeAdj() {
		return buyerFeeValBeforeAdj;
	}
	public void setBuyerFeeValBeforeAdj(Money buyerFeeValBeforeAdj) {
		this.buyerFeeValBeforeAdj = buyerFeeValBeforeAdj;
	}
	public Money getBuyerFeeValAfterAdj() {
		return buyerFeeValAfterAdj;
	}
	public void setBuyerFeeValAfterAdj(Money buyerFeeValAfterAdj) {
		this.buyerFeeValAfterAdj = buyerFeeValAfterAdj;
	}
	public Money getPremiumFeesBeforeAdj() {
		return premiumFeesBeforeAdj;
	}
	public void setPremiumFeesBeforeAdj(Money premiumFeesBeforeAdj) {
		this.premiumFeesBeforeAdj = premiumFeesBeforeAdj;
	}
	public Money getPremiumFeesAfterAdj() {
		return premiumFeesAfterAdj;
	}
	public void setPremiumFeesAfterAdj(Money premiumFeesAfterAdj) {
		this.premiumFeesAfterAdj = premiumFeesAfterAdj;
	}
	public Long getLogisticsMethodId() {
		return logisticsMethodId;
	}
	public void setLogisticsMethodId(Long logisticsMethodId) {
		this.logisticsMethodId = logisticsMethodId;
	}
	public boolean isAttentionFlag() {
		return attentionFlag;
	}
	public void setAttentionFlag(boolean attentionFlag) {
		this.attentionFlag = attentionFlag;
	}
	public String getGroupComments() {
		return groupComments;
	}
	public void setGroupComments(String groupComments) {
		this.groupComments = groupComments;
	}
	public Long getContactId() {
		return contactId;
	}
	public void setContactId(Long contactId) {
		this.contactId = contactId;
	}
	public Long getAffiliateId() {
		return affiliateId;
	}
	public void setAffiliateId(Long affiliateId) {
		this.affiliateId = affiliateId;
	}
	public Money getSellerPayoutAmtBeforeAdj() {
		return sellerPayoutAmtBeforeAdj;
	}
	public void setSellerPayoutAmtBeforeAdj(Money sellerPayoutAmtBeforeAdj) {
		this.sellerPayoutAmtBeforeAdj = sellerPayoutAmtBeforeAdj;
	}
	public Money getSellerPayoutAmtAfterAdj() {
		return sellerPayoutAmtAfterAdj;
	}
	public void setSellerPayoutAmtAfterAdj(Money sellerPayoutAmtAfterAdj) {
		this.sellerPayoutAmtAfterAdj = sellerPayoutAmtAfterAdj;
	}
	public Calendar getOrderDateLastModified() {
		return orderDateLastModified;
	}
	public void setOrderDateLastModified(Calendar orderDateLastModified) {
		this.orderDateLastModified = orderDateLastModified;
	}
	public boolean isSellerConfirmed() {
		return sellerConfirmed;
	}
	public void setSellerConfirmed(boolean sellerConfirmed) {
		this.sellerConfirmed = sellerConfirmed;
	}
	public Long getSellerId() {
		return sellerId;
	}
	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}
	public Long getPid() {
		return pid;
	}
	public void setPid(Long pid) {
		this.pid = pid;
	}
	public Long getSellerPaymentTypeId() {
		return sellerPaymentTypeId;
	}
	public void setSellerPaymentTypeId(Long sellerPaymentTypeId) {
		this.sellerPaymentTypeId = sellerPaymentTypeId;
	}
	public String getSellerCobrand() {
		return sellerCobrand;
	}
	public void setSellerCobrand(String sellerCobrand) {
		this.sellerCobrand = sellerCobrand;
	}
	public Long getActiveCsFlag() {
		return activeCsFlag;
	}
	public void setActiveCsFlag(Long activeCsFlag) {
		this.activeCsFlag = activeCsFlag;
	}
	public Long getEventId() {
		return eventId;
	}
	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}
	public String getSellerComments() {
		return sellerComments;
	}
	public void setSellerComments(String sellerComments) {
		this.sellerComments = sellerComments;
	}
	public Long getPaymentTermsId() {
		return paymentTermsId;
	}
	public void setPaymentTermsId(Long paymentTermsId) {
		this.paymentTermsId = paymentTermsId;
	}
	public Long getLatestOrdProcStatus() {
		return latestOrdProcStatus;
	}
	public void setLatestOrdProcStatus(Long latestOrdProcStatus) {
		this.latestOrdProcStatus = latestOrdProcStatus;
	}
	public Long getHoldPaymentTypeId() {
		return holdPaymentTypeId;
	}
	public void setHoldPaymentTypeId(Long holdPaymentTypeId) {
		this.holdPaymentTypeId = holdPaymentTypeId;
	}
	public Money getSellerPayoutAmountAtConfrm() {
		return sellerPayoutAmountAtConfrm;
	}
	public void setSellerPayoutAmountAtConfrm(Money sellerPayoutAmountAtConfrm) {
		this.sellerPayoutAmountAtConfrm = sellerPayoutAmountAtConfrm;
	}
	public Long getReasonCode() {
		return reasonCode;
	}
	public void setReasonCode(Long reasonCode) {
		this.reasonCode = reasonCode;
	}
	public String getOrderCreatedBy() {
		return orderCreatedBy;
	}
	public void setOrderCreatedBy(String orderCreatedBy) {
		this.orderCreatedBy = orderCreatedBy;
	}
	public String getOrderLastUpdatedBy() {
		return orderLastUpdatedBy;
	}
	public void setOrderLastUpdatedBy(String orderLastUpdatedBy) {
		this.orderLastUpdatedBy = orderLastUpdatedBy;
	}
	public Calendar getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Calendar createdDate) {
		this.createdDate = createdDate;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Calendar getLastUpdatedDate() {
		return lastUpdatedDate;
	}
	public void setLastUpdatedDate(Calendar lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
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
	public Money getLogisticVAT() {
		return logisticVAT;
	}
	public void setLogisticVAT(Money logisticVAT) {
		this.logisticVAT = logisticVAT;
	}
	public Money getSellVAT() {
		return sellVAT;
	}
	public void setSellVAT(Money sellVAT) {
		this.sellVAT = sellVAT;
	}
}
