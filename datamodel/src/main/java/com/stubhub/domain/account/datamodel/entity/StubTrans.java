package com.stubhub.domain.account.datamodel.entity;

import java.util.Calendar;
import java.util.Currency;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.stubhub.newplatform.common.entity.Money;

@Entity
@Table(name = "STUB_TRANS")
@org.hibernate.annotations.Entity(dynamicUpdate = true)
@NamedNativeQueries(value = {
		@NamedNativeQuery(name = "Stubtrans.updateBuyerContactId", query = ""
				+ "UPDATE stub_trans st " + "SET st.contact_id =:contactId, "
				+ "st.last_updated_ts =:dateAdded, "
				+ "st.last_updated_by =:addedBy " + "WHERE st.id =:orderId ", resultClass = StubTrans.class),
		@NamedNativeQuery(name = "StubTrans.updateSTOrderProcStatusById", query = ""
				+ "UPDATE stub_trans st "
				+ "SET st.latest_ord_proc_status =:newStatus, "
				+ "st.date_last_modified = sysdate " + "WHERE st.id =:orderId ", resultClass = StubTrans.class),
		@NamedNativeQuery(name = "Stubtrans.getById", query = "" + "SELECT * "
				+ "FROM stub_trans st " + "WHERE st.id =:orderId ", resultClass = StubTrans.class),
		@NamedNativeQuery(name = "Stubtrans.getBuyerFlipCount", query = "SELECT COUNT (1) AS cnt FROM "
				+ "(SELECT * FROM stub.stub_trans WHERE date_added > SYSDATE - 365 "
				+ "AND section <> 'General Admission' "
				+ "AND buyer_id =:userId "
				+ "AND EXISTS ( "
				+ "SELECT * FROM stub.tickets "
				+ "WHERE tickets.seller_id = stub_trans.buyer_id "
				+ "AND tickets.date_added > stub_trans.date_added "
				+ "AND stub_trans.section = tickets.section "
				+ "AND stub_trans.row_desc = tickets.row_desc "
				+ "AND stub_trans.seats = tickets.seats "
				+ "AND stub_trans.event_id = tickets.event_id ))", resultSetMapping = "rsBuyerFlipCount"),

		@NamedNativeQuery(name = "Stubtrans.getSelStubTicketCount", query = ""
				+ "select COUNT(ID) as "
				+ "tikCount from STUB_TRANS where date_added > SYSDATE - 365 and SELLER_ID=:sellerId", resultSetMapping = "rsSelStubTicketCount"),

		@NamedNativeQuery(name = "Stubtrans.getSelTransTikCount", query = ""
				+ "select count(DISTINCT SELLER_CC_TRANS.TID) as selTransTikCount from SELLER_CC_TRANS, STUB_TRANS "
				+ "where SELLER_CC_TRANS.STATUS='success' and SELLER_CC_TRANS.REASON_CODE=697 "
				+ "and STUB_TRANS.ID = SELLER_CC_TRANS.TID "
				+ "and STUB_TRANS.date_added > SYSDATE - 365 "
				+ "and STUB_TRANS.SELLER_ID=:sellerId", resultSetMapping = "rsSelTransTikCount"),

		@NamedNativeQuery(name = "Stubtrans.getSelPayTicketCount", query = "" +
				"select count(DISTINCT SELLER_PAYMENTS.TID) as selPayTicketCount "
				+"from SELLER_PAYMENTS, STUB_TRANS "
		        +"where SELLER_PAYMENTS.RECORD_TYPE='Credit Memo' and "
				+"SELLER_PAYMENTS.TID=STUB_TRANS.ID and "
				+"STUB_TRANS.date_added > SYSDATE - 365 and "
		        +"SELLER_PAYMENTS.SELLER_ID=:sellerId", resultSetMapping = "rsSelPayTicketCount"),

		@NamedNativeQuery(name = "Stubtrans.getUserSummaryOrderStats", query = "" +
				 "SELECT "
				+"COUNT (CASE WHEN ST.ORDER_PROC_STATUS_code IN (8000) THEN 1 ELSE NULL END) AS CANCELLED_ORDER_COUNT, "
				+"COUNT (CASE WHEN ST.ORDER_PROC_STATUS_code IN (5000, 6000) THEN 1 ELSE NULL END) AS COMPLETED_ORDER_COUNT,"
				+"COUNT (CASE WHEN ST.ORDER_PROC_STATUS_code = 1000 OR ORDER_PROC_SUB_STATUS_CODE = 44 THEN 1 ELSE NULL END) AS UNCONFIRMED_ORDER_COUNT "
				+"FROM STUB_TRANS ST "
				+"WHERE ST.DATE_ADDED > SYSDATE - 90 AND ST.TICKET_COST > 0 AND "
				+"ST.BUYER_ID=:buyerId", resultSetMapping = "rsUserSummaryOrderStats"),

		@NamedNativeQuery(name = "Stubtrans.getUserSummarySaleStats", query = "" +
				 "SELECT "
				+"COUNT (CASE WHEN ST.ORDER_PROC_STATUS_code IN (8000) THEN 1 ELSE NULL END) AS CANCELLED_SALE_COUNT, "
				+"COUNT (CASE WHEN ST.ORDER_PROC_STATUS_code IN (5000, 6000) THEN 1 ELSE NULL END) AS COMPLETED_SALE_COUNT,"
				+"COUNT (CASE WHEN ST.ORDER_PROC_STATUS_code = 1000 OR ORDER_PROC_SUB_STATUS_CODE = 44 THEN 1 ELSE NULL END) AS UNCONFIRMED_SALE_COUNT "
				+"FROM STUB_TRANS ST "
				+"WHERE ST.DATE_ADDED > SYSDATE - 90 AND ST.TICKET_COST > 0 AND "
				+"ST.SELLER_ID=:sellerId", resultSetMapping = "rsUserSummarySaleStats")




})
@SqlResultSetMappings({
		@SqlResultSetMapping(name = "rsBuyerFlipCount", columns = { @ColumnResult(name = "cnt") }),
		@SqlResultSetMapping(name = "rsSelStubTicketCount", columns = { @ColumnResult(name = "tikCount") }),
		@SqlResultSetMapping(name = "rsSelTransTikCount", columns = { @ColumnResult(name = "selTransTikCount") }),
		@SqlResultSetMapping(name = "rsSelPayTicketCount", columns = { @ColumnResult(name = "selPayTicketCount") }),
		@SqlResultSetMapping(name = "rsUserSummaryOrderStats", columns = { @ColumnResult(name = "CANCELLED_ORDER_COUNT"),@ColumnResult(name = "COMPLETED_ORDER_COUNT"),@ColumnResult(name = "UNCONFIRMED_ORDER_COUNT")}),
		@SqlResultSetMapping(name = "rsUserSummarySaleStats", columns = { @ColumnResult(name = "CANCELLED_SALE_COUNT"),@ColumnResult(name = "COMPLETED_SALE_COUNT"),@ColumnResult(name = "UNCONFIRMED_SALE_COUNT")})})
public class StubTrans implements java.io.Serializable {
	@Id
	@Column(name = "ID")
	private Long orderId;
	@Column(name = "BUYER_ID")
	private Long buyerId;
	@Column(name = "TICKET_ID")
	private Long ticketId;
	@Column(name = "SALE_METHOD")
	private Long saleMethod;
	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "amount", column = @Column(name = "TICKET_COST")),
			@AttributeOverride(name = "currency", column = @Column(name = "CURRENCY_CODE", insertable = false, updatable = false)) })
	private Money ticketCost;
	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "amount", column = @Column(name = "SHIP_COST")),
			@AttributeOverride(name = "currency", column = @Column(name = "CURRENCY_CODE", insertable = false, updatable = false)) })
	private Money shipCost;
	@Column(name = "DATE_ADDED")
	@Type(type = "com.stubhub.newplatform.persistence.adapter.UTCCalendarToTimestampAdapter")
	private Calendar dateAdded;
	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "amount", column = @Column(name = "TOTAL_COST")),
			@AttributeOverride(name = "currency", column = @Column(name = "CURRENCY_CODE", insertable = false, updatable = false)) })
	private Money totalCost;
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
	private Boolean cancelled;
	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "amount", column = @Column(name = "DISCOUNT_COST")),
			@AttributeOverride(name = "currency", column = @Column(name = "CURRENCY_CODE", insertable = false, updatable = false)) })
	private Money discountCost;
	@Column(name = "SELLER_FEE")
	private String sellerFee;
	@Column(name = "BUYER_FEE")
	private String buyerFee;
	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "amount", column = @Column(name = "SELLER_FEE_VAL")),
			@AttributeOverride(name = "currency", column = @Column(name = "CURRENCY_CODE", insertable = false, updatable = false)) })
	private Money sellerFeeVal;
	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "amount", column = @Column(name = "BUYER_FEE_VAL")),
			@AttributeOverride(name = "currency", column = @Column(name = "CURRENCY_CODE", insertable = false, updatable = false)) })
	private Money buyerFeeVal;
	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "amount", column = @Column(name = "PREMIUM_FEES")),
			@AttributeOverride(name = "currency", column = @Column(name = "CURRENCY_CODE", insertable = false, updatable = false)) })
	private Money premiumFees;
	@Column(name = "LOGISTICS_METHOD_ID")
	private Long logisticsMethod;
	@Column(name = "ATTENTION_FLAG")
	private Boolean attentionFlag;
	@Column(name = "GROUP_COMMENTS")
	private String groupComments;
	@Column(name = "CONTACT_ID")
	private Long contactId;
	@Column(name = "AFFILIATE_ID")
	private Long affiliateId;
	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "amount", column = @Column(name = "SELLER_PAYOUT_AMOUNT")),
			@AttributeOverride(name = "currency", column = @Column(name = "CURRENCY_CODE", insertable = false, updatable = false)) })
	private Money sellerPayoutAmount;
	@Column(name = "DATE_LAST_MODIFIED")
	@Type(type = "com.stubhub.newplatform.persistence.adapter.UTCCalendarToTimestampAdapter")
	private Calendar dateLastModified;
	@Column(name = "SELLER_CONFIRMED")
	private Boolean sellerConfirmed;
	@Column(name = "SELLER_ID")
	private Long sellerId;
	@Column(name = "SELLER_PAYMENT_TYPE_ID")
	private Long sellerPaymentTypeId;
	@Column(name = "SELLER_COBRAND")
	private String sellerCobrand;
	@Column(name = "ACTIVE_CS_FLAG")
	private Long activeCsFlag;
	@Column(name = "EVENT_ID")
	private Long eventId;
	@Column(name = "LATEST_ORD_PROC_STATUS")
	private Long latestOrderProcStatusId;
	@Column(name = "VENUE_CONFIG_SECTIONS_ID")
	private Long venueConfigSectionsId;
	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "amount", column = @Column(name = "SELLER_PAYOUT_AMOUNT_AT_CONFRM")),
			@AttributeOverride(name = "currency", column = @Column(name = "CURRENCY_CODE", insertable = false, updatable = false)) })
	private Money sellerPayoutAmountAtConfrm;
	@Column(name = "PAYMENT_TERM_ID")
	private Long paymentTermId;
	@Column(name = "HOLD_PAYMENT_TYPE_ID")
	private Long holdPaymentTypeId;
	@Column(name = "SELLER_COMMENTS")
	private String sellerComments;
	@Column(name = "CREATED_BY")
	private String createdBy;
	@Column(name = "LAST_UPDATED_BY")
	private String lastUpdatedBy;
	@Column(name = "CREATED_TS")
	@Type(type = "com.stubhub.newplatform.persistence.adapter.UTCCalendarToTimestampAdapter")
	private Calendar createdTS;
	@Column(name = "LAST_UPDATED_TS")
	@Type(type = "com.stubhub.newplatform.persistence.adapter.UTCCalendarToTimestampAdapter")
	private Calendar lastUpdatedTS;
	@Column(name = "TICKET_MEDIUM_ID")
	private Long ticketMediumId;
	@Column(name = "CONFIRM_OPTION_ID")
	private Long confirmOptionId;
	@Column(name = "DELIVERY_OPTION_ID")
	private Long deliveryOptionId;
	@Column(name = "EVENT_STATUS_ID_AT_CONFIRM")
	private Long eventStatusIdAtConfirm;
	@Column(name = "ORDER_PROC_STATUS_CODE")
	private Long orderProcStatusCode;
	@Column(name = "ORDER_PROC_SUB_STATUS_CODE")
	private Long orderProcSubStatusCode;
	@Column(name = "ORDER_PROC_STATUS_SUB_EFF_DATE")
	@Type(type = "com.stubhub.newplatform.persistence.adapter.UTCCalendarToTimestampAdapter")
	private Calendar orderProcStatusEffDate;
	@Column(name = "EXPECTED_DELIVERY_DATE")
	@Type(type = "com.stubhub.newplatform.persistence.adapter.UTCCalendarToTimestampAdapter")
	private Calendar expectedDeliveryDate;
	@Column(name = "CONFIRM_FLOW_TRACK_ID")
	private Long confirmFlowTrackId;
	@Column(name = "INHAND_DATE")
	private Calendar inHandDate;
	@Column(name = "SELLER_CC_ID")
	private Long sellerCCId;
	@Column(name = "FRAUD_CHECK_STATUS_ID")
	private Long fraudCheckStatusId;
	@Column(name = "BUYER_IP_ADDRESS")
	private String buyerIpAddress;
	@Column(name = "BUYER_TEALEAF_SESSION_GUID")
	private String buyerTealeafSessionGuid;
	@Column(name = "ORDER_SOURCE_ID")
	private Long orderSourceId;
	@Column(name = "LAST_MODIFIED_STUBNET_USER_ID")
	private Long stubnetUserId;
	@Column(name = "BUYER_THREATMETRICS_REFID")
	private String buyerThreatMetrixRefId;
	@Column(name = "CONFIRM_SOURCE_ID")
	private Long confirmSource;
	@Column(name = "LISTING_RECORD_VERSION_NUMBER")
	private Long listingVersion;
	@Column(name = "FRAUD_RESOLUTION_ID")
	private Long fraudResolutionId;
	@Column(name = "MKTNG_PARTNER_ID")
	private Long mktngPartnerId;
	@Column(name = "MKTNG_COMM_OPTIN")
	private Boolean mktngCommOptin;
	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "amount", column = @Column(name = "ADD_ON_FEE")),
			@AttributeOverride(name = "currency", column = @Column(name = "CURRENCY_CODE", insertable = false, updatable = false)) })
	private Money addOnFee;
	@Column(name = "CURRENCY_CODE")
	private Currency currency = Currency.getInstance("USD");
	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "amount", column = @Column(name = "VAT_BUY_FEE")),
			@AttributeOverride(name = "currency", column = @Column(name = "CURRENCY_CODE", insertable = false, updatable = false)) })
	private Money buyVAT;
	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "amount", column = @Column(name = "VAT_LOG_FEE")),
			@AttributeOverride(name = "currency", column = @Column(name = "CURRENCY_CODE", insertable = false, updatable = false)) })
	private Money logisticVAT;
	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "amount", column = @Column(name = "VAT_SELL_FEE")),
			@AttributeOverride(name = "currency", column = @Column(name = "CURRENCY_CODE", insertable = false, updatable = false)) })
	private Money sellVAT;
	@Column(name = "BOOK_OF_BUSINESS_ID")
	private Long bobId;
	@Column(name = "CVV_CHECK_STATUS_ID")
	private Long cvvCheckStatusId;
	@Column(name = "BUY_DOMAIN_ID")
	private Long buyDomainId;
	@Column(name = "SELL_DOMAIN_ID")
	private Long sellDomainId;
	@Column(name = "BUYER_AUTH_SESSION_GUID")
	private String buyerAuthenticatedSessionGuid;
	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "amount", column = @Column(name = "SELL_INCREMENTAL_FEE")),
			@AttributeOverride(name = "currency", column = @Column(name = "CURRENCY_CODE", insertable = false, updatable = false)) })
	private Money sellIncrementalFee;
	@Column(name = "FRAUD_ORDER_REVIEW_HOLD_TIME")
	@Type(type = "com.stubhub.newplatform.persistence.adapter.UTCCalendarToTimestampAdapter")
	private Calendar fraudOrderReviewHoldTime;
	@Column(name = "ACCERTIFY_USER_NAME")
	private String accertifyUserName;
	@Column(name = "PAYMENT_BUFFER_IN_HOURS")
	private Double paymentBufferInHours;

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
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

	public Money getTicketCost() {
		return ticketCost;
	}

	public void setTicketCost(Money ticketCost) {
		this.ticketCost = ticketCost;
	}

	public Money getShipCost() {
		return shipCost;
	}

	public void setShipCost(Money shipCost) {
		this.shipCost = shipCost;
	}

	public Calendar getDateAdded() {
		return dateAdded;
	}

	public void setDateAdded(Calendar dateAdded) {
		this.dateAdded = dateAdded;
	}

	public Money getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(Money totalCost) {
		this.totalCost = totalCost;
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

	public Boolean getCancelled() {
		return cancelled;
	}

	public void setCancelled(Boolean cancelled) {
		this.cancelled = cancelled;
	}

	public Money getDiscountCost() {
		return discountCost;
	}

	public void setDiscountCost(Money discountCost) {
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

	public Money getSellerFeeVal() {
		return sellerFeeVal;
	}

	public void setSellerFeeVal(Money sellerFeeVal) {
		this.sellerFeeVal = sellerFeeVal;
	}

	public Money getBuyerFeeVal() {
		return buyerFeeVal;
	}

	public void setBuyerFeeVal(Money buyerFeeVal) {
		this.buyerFeeVal = buyerFeeVal;
	}

	public Money getPremiumFees() {
		return premiumFees;
	}

	public void setPremiumFees(Money premiumFees) {
		this.premiumFees = premiumFees;
	}

	public Long getLogisticsMethod() {
		return logisticsMethod;
	}

	public void setLogisticsMethod(Long logisticsMethod) {
		this.logisticsMethod = logisticsMethod;
	}

	public Boolean getAttentionFlag() {
		return attentionFlag;
	}

	public void setAttentionFlag(Boolean attentionFlag) {
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

	public Money getSellerPayoutAmount() {
		return sellerPayoutAmount;
	}

	public void setSellerPayoutAmount(Money sellerPayoutAmount) {
		this.sellerPayoutAmount = sellerPayoutAmount;
	}

	public Calendar getDateLastModified() {
		return dateLastModified;
	}

	public void setDateLastModified(Calendar dateLastModified) {
		this.dateLastModified = dateLastModified;
	}

	public Boolean getSellerConfirmed() {
		return sellerConfirmed;
	}

	public void setSellerConfirmed(Boolean sellerConfirmed) {
		this.sellerConfirmed = sellerConfirmed;
	}

	public Long getSellerId() {
		return sellerId;
	}

	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
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

	public Long getLatestOrderProcStatusId() {
		return latestOrderProcStatusId;
	}

	public void setLatestOrderProcStatusId(Long latestOrderProcStatusId) {
		this.latestOrderProcStatusId = latestOrderProcStatusId;
	}

	public Long getVenueConfigSectionsId() {
		return venueConfigSectionsId;
	}

	public void setVenueConfigSectionsId(Long venueConfigSectionsId) {
		this.venueConfigSectionsId = venueConfigSectionsId;
	}

	public Money getSellerPayoutAmountAtConfrm() {
		return sellerPayoutAmountAtConfrm;
	}

	public void setSellerPayoutAmountAtConfrm(Money sellerPayoutAmountAtConfrm) {
		this.sellerPayoutAmountAtConfrm = sellerPayoutAmountAtConfrm;
	}

	public Long getPaymentTermId() {
		return paymentTermId;
	}

	public void setPaymentTermId(Long paymentTermId) {
		this.paymentTermId = paymentTermId;
	}

	public Long getHoldPaymentTypeId() {
		return holdPaymentTypeId;
	}

	public void setHoldPaymentTypeId(Long holdPaymentTypeId) {
		this.holdPaymentTypeId = holdPaymentTypeId;
	}

	public String getSellerComments() {
		return sellerComments;
	}

	public void setSellerComments(String sellerComments) {
		this.sellerComments = sellerComments;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Calendar getCreatedTS() {
		return createdTS;
	}

	public void setCreatedTS(Calendar createdTS) {
		this.createdTS = createdTS;
	}

	public Calendar getLastUpdatedTS() {
		return lastUpdatedTS;
	}

	public void setLastUpdatedTS(Calendar lastUpdatedTS) {
		this.lastUpdatedTS = lastUpdatedTS;
	}

	public Long getTicketMediumId() {
		return ticketMediumId;
	}

	public void setTicketMediumId(Long ticketMediumId) {
		this.ticketMediumId = ticketMediumId;
	}

	public Long getConfirmOptionId() {
		return confirmOptionId;
	}

	public void setConfirmOptionId(Long confirmOptionId) {
		this.confirmOptionId = confirmOptionId;
	}

	public Long getDeliveryOptionId() {
		return deliveryOptionId;
	}

	public void setDeliveryOptionId(Long deliveryOptionId) {
		this.deliveryOptionId = deliveryOptionId;
	}

	public Long getEventStatusIdAtConfirm() {
		return eventStatusIdAtConfirm;
	}

	public void setEventStatusIdAtConfirm(Long eventStatusIdAtConfirm) {
		this.eventStatusIdAtConfirm = eventStatusIdAtConfirm;
	}

	public Long getOrderProcStatusCode() {
		return orderProcStatusCode;
	}

	public void setOrderProcStatusCode(Long orderProcStatusCode) {
		this.orderProcStatusCode = orderProcStatusCode;
	}

	public Long getOrderProcSubStatusCode() {
		return orderProcSubStatusCode;
	}

	public void setOrderProcSubStatusCode(Long orderProcSubStatusCode) {
		this.orderProcSubStatusCode = orderProcSubStatusCode;
	}

	public Calendar getOrderProcStatusEffDate() {
		return orderProcStatusEffDate;
	}

	public void setOrderProcStatusEffDate(Calendar orderProcStatusEffDate) {
		this.orderProcStatusEffDate = orderProcStatusEffDate;
	}

	public Calendar getExpectedDeliveryDate() {
		return expectedDeliveryDate;
	}

	public void setExpectedDeliveryDate(Calendar expectedDeliveryDate) {
		this.expectedDeliveryDate = expectedDeliveryDate;
	}

	public Long getConfirmFlowTrackId() {
		return confirmFlowTrackId;
	}

	public void setConfirmFlowTrackId(Long confirmFlowTrackId) {
		this.confirmFlowTrackId = confirmFlowTrackId;
	}

	public Calendar getInHandDate() {
		return inHandDate;
	}

	public void setInHandDate(Calendar inHandDate) {
		this.inHandDate = inHandDate;
	}

	public Long getSellerCCId() {
		return sellerCCId;
	}

	public void setSellerCCId(Long sellerCCId) {
		this.sellerCCId = sellerCCId;
	}

	public Long getFraudCheckStatusId() {
		return fraudCheckStatusId;
	}

	public void setFraudCheckStatusId(Long fraudCheckStatusId) {
		this.fraudCheckStatusId = fraudCheckStatusId;
	}

	public String getBuyerIpAddress() {
		return buyerIpAddress;
	}

	public void setBuyerIpAddress(String buyerIpAddress) {
		this.buyerIpAddress = buyerIpAddress;
	}

	public String getBuyerTealeafSessionGuid() {
		return buyerTealeafSessionGuid;
	}

	public void setBuyerTealeafSessionGuid(String buyerTealeafSessionGuid) {
		this.buyerTealeafSessionGuid = buyerTealeafSessionGuid;
	}

	public Long getOrderSourceId() {
		return orderSourceId;
	}

	public void setOrderSourceId(Long orderSourceId) {
		this.orderSourceId = orderSourceId;
	}

	public Long getStubnetUserId() {
		return stubnetUserId;
	}

	public void setStubnetUserId(Long stubnetUserId) {
		this.stubnetUserId = stubnetUserId;
	}

	public String getBuyerThreatMetrixRefId() {
		return buyerThreatMetrixRefId;
	}

	public void setBuyerThreatMetrixRefId(String buyerThreatMetrixRefId) {
		this.buyerThreatMetrixRefId = buyerThreatMetrixRefId;
	}

	public Long getConfirmSource() {
		return confirmSource;
	}

	public void setConfirmSource(Long confirmSource) {
		this.confirmSource = confirmSource;
	}

	public Long getListingVersion() {
		return listingVersion;
	}

	public void setListingVersion(Long listingVersion) {
		this.listingVersion = listingVersion;
	}

	public Long getFraudResolutionId() {
		return fraudResolutionId;
	}

	public void setFraudResolutionId(Long fraudResolutionId) {
		this.fraudResolutionId = fraudResolutionId;
	}

	public Long getMktngPartnerId() {
		return mktngPartnerId;
	}

	public void setMktngPartnerId(Long mktngPartnerId) {
		this.mktngPartnerId = mktngPartnerId;
	}

	public Boolean getMktngCommOptin() {
		return mktngCommOptin;
	}

	public void setMktngCommOptin(Boolean mktngCommOptin) {
		this.mktngCommOptin = mktngCommOptin;
	}

	public Money getAddOnFee() {
		return addOnFee;
	}

	public void setAddOnFee(Money addOnFee) {
		this.addOnFee = addOnFee;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
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

	public Long getBobId() {
		return bobId;
	}

	public void setBobId(Long bobId) {
		this.bobId = bobId;
	}

	public Long getCvvCheckStatusId() {
		return cvvCheckStatusId;
	}

	public void setCvvCheckStatusId(Long cvvCheckStatusId) {
		this.cvvCheckStatusId = cvvCheckStatusId;
	}

	public Long getBuyDomainId() {
		return buyDomainId;
	}

	public void setBuyDomainId(Long buyDomainId) {
		this.buyDomainId = buyDomainId;
	}

	public Long getSellDomainId() {
		return sellDomainId;
	}

	public void setSellDomainId(Long sellDomainId) {
		this.sellDomainId = sellDomainId;
	}

	public String getBuyerAuthenticatedSessionGuid() {
		return buyerAuthenticatedSessionGuid;
	}

	public void setBuyerAuthenticatedSessionGuid(
			String buyerAuthenticatedSessionGuid) {
		this.buyerAuthenticatedSessionGuid = buyerAuthenticatedSessionGuid;
	}

	public Money getSellIncrementalFee() {
		return sellIncrementalFee;
	}

	public void setSellIncrementalFee(Money sellIncrementalFee) {
		this.sellIncrementalFee = sellIncrementalFee;
	}

	public Calendar getFraudOrderReviewHoldTime() {
		return fraudOrderReviewHoldTime;
	}

	public void setFraudOrderReviewHoldTime(Calendar fraudOrderReviewHoldTime) {
		this.fraudOrderReviewHoldTime = fraudOrderReviewHoldTime;
	}

	public String getAccertifyUserName() {
		return accertifyUserName;
	}

	public void setAccertifyUserName(String accertifyUserName) {
		this.accertifyUserName = accertifyUserName;
	}

	public Double getPaymentBufferInHours() {
		return paymentBufferInHours;
	}

	public void setPaymentBufferInHours(Double paymentBufferInHours) {
		this.paymentBufferInHours = paymentBufferInHours;
	}
}
