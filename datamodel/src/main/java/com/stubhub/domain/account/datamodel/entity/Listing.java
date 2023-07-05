package com.stubhub.domain.account.datamodel.entity;

import java.util.Calendar;
import java.util.Currency;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.Type;

import com.stubhub.newplatform.common.entity.Money;

@Entity
@Table(name = "TICKETS")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert=true)
@NamedNativeQueries(value ={
		@NamedNativeQuery(name = "Listing.updateQuantityRemain", query = "" +
				"UPDATE tickets t " +
				"SET t.QUANTITY_REMAIN =:quantityRemain, " +
				"t.LAST_UPDATED_BY =:lastUpdatedBy, " +
				"t.LAST_UPDATED_TS =:lastUpdatedTS,  " +
				"t.SEATS =:seats " +
				"WHERE t.ID =:listingId ", resultClass=Listing.class),
		@NamedNativeQuery(name = "Listing.getUserSummaryTicketStats", query = "" +
				 "SELECT "
				+"COUNT (CASE WHEN SYSTEM_STATUS = 'ACTIVE' AND QUANTITY_REMAIN > 0 AND END_DATE >= SYSDATE THEN 1 ELSE NULL END) AS ACTIVE_TICKET_COUNT, "
				+"COUNT (CASE WHEN SYSTEM_STATUS = 'INACTIVE' AND QUANTITY_REMAIN > 0 AND END_DATE >= SYSDATE THEN 1 ELSE NULL END) AS INACTIVE_TICKET_COUNT,"
				+"COUNT (CASE WHEN SYSTEM_STATUS = 'DELETED' THEN 1 ELSE NULL END) AS DELETED_TICKET_COUNT, "
				+"COUNT (CASE WHEN SYSTEM_STATUS = 'INCOMPLETE' AND QUANTITY_REMAIN > 0 AND END_DATE >= SYSDATE AND lms_approval_status_id = 1 THEN 1 ELSE NULL END) AS Pending_LMS_COUNT "
				+"FROM TICKETS "
				+"WHERE DATE_ADDED > SYSDATE - 90 AND "
				+"SELLER_ID=:sellerId", resultSetMapping = "rsUserSummaryTicketStats")
})
@SqlResultSetMappings({
		@SqlResultSetMapping(name = "rsUserSummaryTicketStats", columns = { @ColumnResult(name = "ACTIVE_TICKET_COUNT"),@ColumnResult(name = "INACTIVE_TICKET_COUNT"),@ColumnResult(name = "DELETED_TICKET_COUNT"),@ColumnResult(name = "Pending_LMS_COUNT")})})
public final class Listing implements java.io.Serializable {

	@Id
	@SequenceGenerator(name="TICKETS_SEQ", sequenceName="TICKETS_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TICKETS_SEQ")
	private Long id;
	@Column(name = "LAST_UPDATED_BY")
	private String lastUpdatedBy;
	@Column(name = "EVENT_ID")
	private Long eventId;
	@Column(name = "SELLER_ID")
	private Long sellerId;
	@Column(name = "SYSTEM_STATUS")
	private String systemStatus;

	@Column(name = "SECTION")
	private String section;

	@Column(name = "ROW_DESC")
	private String row;
	@Column(name = "SEATS")
	private String seats;

	@Column(name = "QUANTITY")
	private Integer quantity;
	@Column(name = "QUANTITY_REMAIN")
	private Integer quantityRemain;
	@Column(name = "SPLIT_OPTION")
	private Short splitOption;
	@Column(name = "SPLIT")
	private Integer splitQuantity;

	@Column(name = "SALE_METHOD")
	private Long saleMethod;

	@Column(name = "END_DATE")
	@Type(type="com.stubhub.newplatform.persistence.adapter.UTCCalendarToTimestampAdapter")
	private Calendar endDate;

	@Column(name = "SELLER_IP_ADDRESS")
	private String ipAddress;

	@Column(name = "SELLER_TEALEAF_SESSION_GUID")
	private String tealeafSessionGuid;



    public Money getDisplayPricePerTicket() {
		return displayPricePerTicket;
	}
	public void setDisplayPricePerTicket(Money displayPricePerTicket) {
		this.displayPricePerTicket = displayPricePerTicket;
	}
	@Embedded
    @AttributeOverrides( {
            @AttributeOverride(name="amount", column = @Column(name="DISPLAY_PRICE_PER_TICKET") ),
            @AttributeOverride(name="currency", column = @Column(name="CURRENCY_CODE",insertable=false, updatable=false) )
    } )
	private Money displayPricePerTicket;


    @Embedded
    @AttributeOverrides( {
            @AttributeOverride(name="amount", column = @Column(name="CURR_PRICE") ),
            @AttributeOverride(name="currency", column = @Column(name="CURRENCY_CODE",insertable=false, updatable=false) )
    } )
	private Money listPrice;

    @Embedded
    @AttributeOverrides( {
            @AttributeOverride(name="amount", column = @Column(name="TOTAL_LISTING_PRICE") ),
            @AttributeOverride(name="currency", column = @Column(name="CURRENCY_CODE",insertable=false, updatable=false) )
    } )
	private Money totalListingPrice;

    @Embedded
    @AttributeOverrides( {
            @AttributeOverride(name="amount", column = @Column(name="SELL_FEE_VAL_PER_TICKET") ),
            @AttributeOverride(name="currency", column = @Column(name="CURRENCY_CODE",insertable=false, updatable=false) )
    } )
    private Money sellFeeValuePerTicket;

    @Column(name = "SELL_FEE_DESC")
	private String sellFeeDescription;

    @Embedded
    @AttributeOverrides( {
            @AttributeOverride(name="amount", column = @Column(name="TOTAL_SELL_FEE_VAL") ),
            @AttributeOverride(name="currency", column = @Column(name="CURRENCY_CODE",insertable=false, updatable=false) )
    } )
	private Money totalSellFeeValue;

    @Embedded
    @AttributeOverrides( {
            @AttributeOverride(name="amount", column = @Column(name="TOTAL_SELLER_PAYOUT_AMT") ),
            @AttributeOverride(name="currency", column = @Column(name="CURRENCY_CODE",insertable=false, updatable=false) )
    } )
	private Money totalSellerPayoutAmt;

    @Embedded
    @AttributeOverrides( {
            @AttributeOverride(name="amount", column = @Column(name="SELLER_PAYOUT_AMT_PER_TICKET") ),
            @AttributeOverride(name="currency", column = @Column(name="CURRENCY_CODE",insertable=false, updatable=false) )
    } )
    private Money sellerPayoutAmountPerTicket;

    @Embedded
    @AttributeOverrides( {
            @AttributeOverride(name="amount", column = @Column(name="FACE_VALUE") ),
            @AttributeOverride(name="currency", column = @Column(name="CURRENCY_CODE",insertable=false, updatable=false) )
    } )
    private Money faceValue;

    @Embedded
    @AttributeOverrides( {
            @AttributeOverride(name="amount", column = @Column(name="SELLER_PURCHASE_PRICE") ),
            @AttributeOverride(name="currency", column = @Column(name="CURRENCY_CODE",insertable=false, updatable=false) )
    } )
    private Money ticketCost;

    @Embedded
	@AttributeOverrides( {
		@AttributeOverride(name="amount", column = @Column(name="MIN_DECAY_PRICE") ),
		@AttributeOverride(name="currency", column = @Column(name="CURRENCY_CODE",insertable=false, updatable=false) )
	} )
	private Money minPricePerTicket;

	@Embedded
	@AttributeOverrides( {
		@AttributeOverride(name="amount", column = @Column(name="MAX_DECAY_PRICE") ),
		@AttributeOverride(name="currency", column = @Column(name="CURRENCY_CODE",insertable=false, updatable=false) )
	} )
	private Money maxPricePerTicket;


    @Column(name = "COMMENTS")
	private String comments;
    @Column(name = "SELLER_CC_ID")
	private Long sellerCCId;
    @Column(name = "SELLER_CONTACT_ID")
	private Long sellerContactId;
    @Column(name = "SELLER_PAYMENT_TYPE_ID")
	private Long sellerPaymentTypeId;

    @Column(name = "CONFIRM_OPTION_ID")
	private Integer confirmOption;
    @Column(name = "DELIVERY_OPTION_ID")
	private Integer deliveryOption;
    @Column(name = "LISTING_SOURCE_ID")
	private Integer listingSource;

    @Column(name = "TICKET_MEDIUM_ID")
    private Integer ticketMedium;
    @Column(name = "LMS_APPROVAL_STATUS_ID")
	private Integer lmsApprovalStatus;

    @Column(name = "INHAND_DATE")
    @Type(type="com.stubhub.newplatform.persistence.adapter.UTCCalendarToTimestampAdapter")
    private Calendar inhandDate;

    @Column(name = "DECLARED_INHAND_DATE")
    @Type(type="com.stubhub.newplatform.persistence.adapter.UTCCalendarToTimestampAdapter")
    private Calendar declaredInhandDate;

	@Column(name = "CURRENCY_CODE")
	private Currency currency;

	@Column(name = "EXTERNAL_LISTING_ID")
	private String externalId;

	@Column(name = "DATE_ADDED")
	@Type(type="com.stubhub.newplatform.persistence.adapter.UTCCalendarToTimestampAdapter")
	private Calendar createdDate;

	@Column(name = "DATE_LAST_MODIFIED")
	@Type(type="com.stubhub.newplatform.persistence.adapter.UTCCalendarToTimestampAdapter")
	private Calendar lastModifiedDate;

	@Column(name = "DEFERED_ACTIVATION_DATE")
	@Type(type="com.stubhub.newplatform.persistence.adapter.UTCCalendarToTimestampAdapter")
	private Calendar deferedActivationDate;

	@Column(name = "TIX_LIST_TYPE_ID")
	private Long listingType;

	@Column(name = "IS_SECTION_SCRUB_EXCLUDED")
	private Boolean sectionScrubExcluded;

	@Column(name = "SECTION_SCRUB_SCHEDULE")
	private Boolean sectionScrubSchedule;

	@Column(name = "IS_ETICKET")
	private Boolean isETicket;

	@Column(name = "SELLER_COBRAND")
	private String sellerCobrand;

	@Column(name = "VENUE_CONFIG_SECTIONS_ID")
	private Long venueConfigSectionsId;

	@Column(name = "FM_DM_LIST")
	private String fulfillmentDeliveryMethods;

	@Column(name = "RECORD_VERSION_NUMBER")
	@Version
	private Integer Version;

	@Transient
	private String structuredComments;

	@Transient
	private List<Long> structuredCommentIds;

	@Transient
	private FulfillmentMethod fulfillmentMethod;

	@Transient
	private String correlationId;

	/* Not Part of SFC phase 1 release
	@Transient
	private Long paymentContactId;*/

	@Transient
	private String sellerGuid;


	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getEventId() {
		return eventId;
	}
	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}
	public Long getSellerId() {
		return sellerId;
	}
	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}
	public String getSystemStatus() {
		return systemStatus;
	}
	public void setSystemStatus(String systemStatus) {
		this.systemStatus = systemStatus;
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
	public String getSeats() {
		return seats;
	}
	public void setSeats(String seats) {
		this.seats = seats;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Integer getQuantityRemain() {
		return quantityRemain;
	}
	public void setQuantityRemain(Integer quantityRemain) {
		this.quantityRemain = quantityRemain;
	}
	public Short getSplitOption() {
		return splitOption;
	}
	public void setSplitOption(Short splitOption) {
		this.splitOption = splitOption;
	}
	public Integer getSplitQuantity() {
		return splitQuantity;
	}
	public void setSplitQuantity(Integer splitQuantity) {
		this.splitQuantity = splitQuantity;
	}
	public Money getListPrice() {
		return listPrice;
	}
	public void setListPrice(Money listPrice) {
		this.listPrice = listPrice;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public Long getSellerCCId() {
		return sellerCCId;
	}
	public void setSellerCCId(Long sellerCCId) {
		this.sellerCCId = sellerCCId;
	}
	public Long getSellerContactId() {
		return sellerContactId;
	}
	public void setSellerContactId(Long sellerContactId) {
		this.sellerContactId = sellerContactId;
	}
	public Long getSellerPaymentTypeId() {
		return sellerPaymentTypeId;
	}
	public void setSellerPaymentTypeId(Long sellerPaymentTypeId) {
		this.sellerPaymentTypeId = sellerPaymentTypeId;
	}
	public Integer getConfirmOption() {
		return confirmOption;
	}
	public void setConfirmOption(Integer confirmOption) {
		this.confirmOption = confirmOption;
	}
	public Integer getDeliveryOption() {
		return deliveryOption;
	}
	public void setDeliveryOption(Integer deliveryOption) {
		this.deliveryOption = deliveryOption;
	}
	public Integer getListingSource() {
		return listingSource;
	}
	public void setListingSource(Integer listingSource) {
		this.listingSource = listingSource;
	}
	public Integer getTicketMedium() {
		return ticketMedium;
	}
	public void setTicketMedium(Integer ticketMedium) {
		this.ticketMedium = ticketMedium;
	}
	public Integer getLmsApprovalStatus() {
		return lmsApprovalStatus;
	}
	public void setLmsApprovalStatus(Integer lmsApprovalStatus) {
		this.lmsApprovalStatus = lmsApprovalStatus;
	}
	public Currency getCurrency() {
		return currency;
	}
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}
	public String getExternalId() {
		return externalId;
	}
	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}
	public Long getSaleMethod() {
		return saleMethod;
	}
	public void setSaleMethod(Long saleMethod) {
		this.saleMethod = saleMethod;
	}
	public Calendar getEndDate() {
		return endDate;
	}
	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;
	}
	public Calendar getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Calendar createdDate) {
		this.createdDate = createdDate;
	}
	public Calendar getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(Calendar lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	public Money getTotalListingPrice() {
		return totalListingPrice;
	}
	public void setTotalListingPrice(Money totalListingPrice) {
		this.totalListingPrice = totalListingPrice;
	}
	public Long getListingType() {
		return listingType;
	}
	public void setListingType(Long listingType) {
		this.listingType = listingType;
	}
	public Money getFaceValue() {
		return faceValue;
	}
	public void setFaceValue(Money faceValue) {
		this.faceValue = faceValue;
	}

	public Calendar getInhandDate() {
		return inhandDate;
	}
	public void setInhandDate(Calendar inhandDate) {
		this.inhandDate = inhandDate;
	}
	public Boolean getSectionScrubExcluded() {
		return sectionScrubExcluded;
	}
	public void setSectionScrubExcluded(Boolean sectionScrubExcluded) {
		this.sectionScrubExcluded = sectionScrubExcluded;
	}
	public Boolean getSectionScrubSchedule() {
		return sectionScrubSchedule;
	}
	public void setSectionScrubSchedule(Boolean sectionScrubSchedule) {
		this.sectionScrubSchedule = sectionScrubSchedule;
	}
	public Money getSellFeeValuePerTicket() {
		return sellFeeValuePerTicket;
	}
	public void setSellFeeValuePerTicket(Money sellFeeValuePerTicket) {
		this.sellFeeValuePerTicket = sellFeeValuePerTicket;
	}
	public String getSellFeeDescription() {
		return sellFeeDescription;
	}
	public void setSellFeeDescription(String sellFeeDescription) {
		this.sellFeeDescription = sellFeeDescription;
	}
	public Money getTotalSellFeeValue() {
		return totalSellFeeValue;
	}
	public void setTotalSellFeeValue(Money totalSellFeeValue) {
		this.totalSellFeeValue = totalSellFeeValue;
	}
	public Money getTotalSellerPayoutAmt() {
		return totalSellerPayoutAmt;
	}
	public void setTotalSellerPayoutAmt(Money totalSellerPayoutAmt) {
		this.totalSellerPayoutAmt = totalSellerPayoutAmt;
	}
	public Money getSellerPayoutAmountPerTicket() {
		return sellerPayoutAmountPerTicket;
	}
	public void setSellerPayoutAmountPerTicket(Money sellerPayoutAmountPerTicket) {
		this.sellerPayoutAmountPerTicket = sellerPayoutAmountPerTicket;
	}
	public Calendar getDeclaredInhandDate() {
		return declaredInhandDate;
	}
	public void setDeclaredInhandDate(Calendar declaredInhandDate) {
		this.declaredInhandDate = declaredInhandDate;
	}
	public Boolean getIsETicket() {
		return isETicket;
	}
	public void setIsETicket(Boolean isETicket) {
		this.isETicket = isETicket;
	}
	public String getStructuredComments() {
		return structuredComments;
	}
	public void setStructuredComments(String structuredComments) {
		this.structuredComments = structuredComments;
	}
	public FulfillmentMethod getFulfillmentMethod() {
		return fulfillmentMethod;
	}
	public void setFulfillmentMethod(FulfillmentMethod fulfillmentMethod) {
		this.fulfillmentMethod = fulfillmentMethod;
	}
	public String getSellerCobrand() {
		return sellerCobrand;
	}
	public void setSellerCobrand(String sellerCobrand) {
		this.sellerCobrand = sellerCobrand;
	}
	public Money getTicketCost() {
		return ticketCost;
	}
	public void setTicketCost(Money ticketCost) {
		this.ticketCost = ticketCost;
	}
	public Long getVenueConfigSectionsId() {
		return venueConfigSectionsId;
	}
	public void setVenueConfigSectionsId(Long venueConfigSectionsId) {
		this.venueConfigSectionsId = venueConfigSectionsId;
	}
	public String getFulfillmentDeliveryMethods() {
		return fulfillmentDeliveryMethods;
	}
	public void setFulfillmentDeliveryMethods(String fulfillmentDeliveryMethods) {
		this.fulfillmentDeliveryMethods = fulfillmentDeliveryMethods;
	}
	public Integer getVersion() {
		return Version;
	}
	public void setVersion(Integer version) {
		Version = version;
	}
	public Calendar getDeferedActivationDate() {
		return deferedActivationDate;
	}
	public void setDeferedActivationDate(Calendar deferedActivationDate) {
		this.deferedActivationDate = deferedActivationDate;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getTealeafSessionGuid() {
		return tealeafSessionGuid;
	}
	public void setTealeafSessionGuid(String tealeafSessionGuid) {
		this.tealeafSessionGuid = tealeafSessionGuid;
	}
	public String getCorrelationId() {
		return correlationId;
	}
	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}
	public Money getMinPricePerTicket() {
		return minPricePerTicket;
	}
	public void setMinPricePerTicket(Money minPricePerTicket) {
		this.minPricePerTicket = minPricePerTicket;
	}
	public Money getMaxPricePerTicket() {
		return maxPricePerTicket;
	}
	public void setMaxPricePerTicket(Money maxPricePerTicket) {
		this.maxPricePerTicket = maxPricePerTicket;
	}
	/*public Long getPaymentContactId() {
		return paymentContactId;
	}
	public void setPaymentContactId(Long paymentContactId) {
		this.paymentContactId = paymentContactId;
	}*/
	public String getSellerGuid() {
		return sellerGuid;
	}
	public void setSellerGuid(String sellerGuid) {
		this.sellerGuid = sellerGuid;
	}
	public List<Long> getStructuredCommentIds() {
		return structuredCommentIds;
	}
	public void setStructuredCommentIds(List<Long> structuredCommentIds) {
		this.structuredCommentIds = structuredCommentIds;
	}
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

}
