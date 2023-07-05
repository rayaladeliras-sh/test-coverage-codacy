package com.stubhub.domain.account.intf;


import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.map.annotate.JsonRootName;

import com.stubhub.domain.account.common.Response;
import com.stubhub.domain.account.common.enums.DeliveryOption;
import com.stubhub.domain.account.common.enums.ListingStatus;
import com.stubhub.domain.account.common.enums.SaleMethod;
import com.stubhub.domain.account.common.enums.SplitOption;
import com.stubhub.domain.account.common.enums.TicketTrait;
import com.stubhub.newplatform.common.entity.Money;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "listing")
@JsonRootName("listing")
@XmlType(name = "", propOrder = {
		"id",
		"status",
		"eventId",
		"genreId",
		"performerId",
		"groupingId",
		"categoryId",
		"eventDescription",
		"genreDescription",
		"eventDate",
		"eventActive",
		"inhandDate",
		"earliestInhandDate",
		"eInhandDateLaterThanNow",
		"quantity",
		"quantityRemain",
		"section",
		"rows",
		"seats",
		"venueConfigSectionsId",
		"venueConfigId",
		"venueDescription",
		"splitOption",
		"splitQuantity",
		"deliveryOption",
		"preDelivered",
		"ableToRequestLMS",
		"saleEndDate",
		"dateLastModified",
		"lastUpdatedBy",
		"saleMethod",
		"pricePerTicket",
		"faceValue",
		"displayPricePerTicket",
		"purchasePrice",
		"payoutPerTicket",
		"startPricePerTicket",
		"endPricePerTicket",
		"ticketTraits",
		"internalNotes",
		"contactId",
		"paymentType", 
		"ccId",
		"externalListingId",
		"eventTimeZone",
		"fees",
		"totalPayout",
		"city",
		"state",
		"country",
		"GA",
		"stubhubMobileTicket",
		"sectionMappingRequired",
		"isScrubbingEnabled",
		"lmsApprovalStatus",
		"pricingRecommendation",
		"ticketMediumId",
		"autoPricingEnabledInd",
		"sellerInputPrice",
		"sellerInputPriceType",
		"purchasePricePerProduct",
		"salesTaxPaid",
		"zoneName",
		"zoneId",
		"valueScore",
        "deliveryTypeIds"
		})

public class ListingResponse extends Response implements TranslatableEventData{
	
	@XmlElement(name = "id", required = true)
	private String id;
	
	@XmlElement(name = "status", required = true)
	private ListingStatus status;
	
	@XmlElement(name = "eventId", required = false)
	private String eventId;
	
	@XmlElement(name = "genreId", required = false)
	private String genreId;

	@XmlElement(name = "performerId", required = false)
	private String performerId;

	@XmlElement(name = "groupingId", required = false)
	private String groupingId;

	@XmlElement(name = "categoryId", required = false)
	private String categoryId;
	
	@XmlElement(name = "eventDescription", required = false)
	private String eventDescription;

	@XmlElement(name = "genreDescription", required = false)
	private String genreDescription;
	
	@XmlElement(name = "eventDate", required = false)
	private String eventDate;	
	
	@XmlElement(name = "eventTimeZone", required = false)
	private String eventTimeZone;

	@XmlElement(name = "eventActive", required = false)
	private Boolean eventActive;

	@XmlElement(name = "inhandDate", required = false)
	private String inhandDate;

	@XmlElement(name = "earliestInhandDate", required = false)
	private String earliestInhandDate;

	@XmlElement(name = "eInhandDateLaterThanNow", required = false)
	private Boolean eInhandDateLaterThanNow;
	
	@XmlElement(name = "quantity", required = false)
	private Integer quantity;
	
	@XmlElement(name = "quantityRemain", required = false)
	private Integer quantityRemain;
	
	@XmlElement(name = "section", required = false)
	private String section;
	
	@XmlElement(name = "rows", required = false)
	private String rows;
	
	@XmlElement(name = "seats", required = false)
	private String seats;
	
	@XmlElement(name = "venueConfigSectionsId", required = false)
	private Long venueConfigSectionsId;

	@XmlElement(name = "venueConfigId", required = false)
	private Long venueConfigId;
	
	@XmlElement(name = "venueDescription", required = false)
	private String venueDescription;	
	
	@XmlElement(name = "splitOption", required = false)
	private SplitOption splitOption;
	
	@XmlElement(name = "splitQuantity", required = false)
	private Integer splitQuantity;
	
	@XmlElement(name = "deliveryOption", required = false)
	private DeliveryOption deliveryOption;
	
	@XmlElement(name = "preDelivered", required = false)
	private Boolean preDelivered;

	@XmlElement(name = "ableToRequestLMS", required = false)
	private Boolean ableToRequestLMS;
	
	//ex: 2012-01-12T16:00:00.000-08:00
	@XmlElement(name = "saleEndDate", required = false)
	private String saleEndDate;

	@XmlElement(name = "dateLastModified", required = false)
	private String dateLastModified;

	@XmlElement(name = "lastUpdatedBy", required = false)
	private String lastUpdatedBy;

	@XmlElement(name = "saleMethod", required = false)
	private SaleMethod saleMethod;
	
	@XmlElement(name = "pricePerTicket", required = false)
	private Money pricePerTicket;
	
	@XmlElement(name = "faceValue", required = false)
	private Money faceValue;
	
	@XmlElement(name = "purchasePrice", required = false)
	private Money purchasePrice;

	@XmlElement(name = "payoutPerTicket", required = false)
	private Money payoutPerTicket;
	
	@XmlElement(name = "startPricePerTicket", required = false)
	private Money startPricePerTicket;
	
	@XmlElement(name = "endPricePerTicket", required = false)
	private Money endPricePerTicket;
	
	@XmlElement(name = "displayPricePerTicket", required = false)
	private Money displayPricePerTicket;
	
	@XmlElement(name = "sellerInputPrice", required = false)
	private Money sellerInputPrice;

	@XmlElement(name = "sellerInputCurrencyCode", required = false)
	private String sellerInputCurrencyCode;
	
	@XmlElement(name = "sellerInputPriceType", required = false)
	private String sellerInputPriceType;
	
	@XmlElement(name = "ticketTraits", required = false)
	private Set<TicketTrait> ticketTraits;
	
	@XmlElement(name = "internalNotes", required = false)
	private String internalNotes;
	
	@XmlElement(name = "contactId", required = false)
	private Long contactId;
	
	@XmlElement(name = "paymentType", required = false)
	private String paymentType;
	
	@XmlElement(name = "ccId", required = false)
	private Long ccId;
	
	@XmlElement(name = "externalListingId", required = false)
	private String externalListingId;
	
	@XmlElement(name = "fees", required = true)
	private List<Fee> fees;
	
	@XmlElement(name = "totalPayout", required = false)
	private Money totalPayout;

	@XmlElement(name = "city", required = false)
	private String city;

	@XmlElement(name = "state", required = false)
	private String state;

	@XmlElement(name = "country", required = false)
	private String country;
	
	@XmlElement(name = "GA", required = false)
	private Boolean GA;	
	
	@XmlElement(name = "stubhubMobileTicket", required = false)
	private Integer stubhubMobileTicket = 0;

	
	@XmlElement(name = "sectionMappingRequired", required = false)
	private Boolean sectionMappingRequired;
	
	
	@XmlElement(name = "isScrubbingEnabled", required = false)
	private Boolean isScrubbingEnabled;
	


	@XmlElement(name = "lmsApprovalStatus", required = false)
	private Integer lmsApprovalStatus;

	@XmlElement(name="pricingRecommendation", required = false)
	private PricingRecommendation pricingRecommendation;

	@XmlElement(name = "ticketMediumId", required = false)
	private Integer ticketMediumId;

	@XmlElement(name = "autoPricingEnabledInd", required = false)
	private Integer autoPricingEnabledInd;

	@XmlElement(name = "deliveryMethod", required = false)
	private List<DeliveryMethod> deliveryMethod;
    @XmlElement(name = "fulfillmentMethod", required = false)
	private List<FulfillmentMethod> fulfillmentMethod;

    //US Sales Tax
    @XmlElement(name = "purchasePricePerProduct", required = false)
	private Money purchasePricePerProduct;
    @XmlElement(name = "salesTaxPaid", required = false)
	private Boolean salesTaxPaid;

    public Money getPurchasePricePerProduct() {
		return purchasePricePerProduct;
	}

	public void setPurchasePricePerProduct(Money purchasePricePerProduct) {
		this.purchasePricePerProduct = purchasePricePerProduct;
	}

	public Boolean getSalesTaxPaid() {
		return salesTaxPaid;
	}

	public void setSalesTaxPaid(Boolean salesTaxPaid) {
		this.salesTaxPaid = salesTaxPaid;
	}


    @XmlElement(name = "zoneName", required = false)
	private String zoneName;

    @XmlElement(name = "zoneId", required = false)
	private String zoneId;

    @XmlElement(name = "valueScore", required = false)
	private Double valueScore;

    @XmlElement(name = "deliveryTypeIds", required = false)
    private List<Integer> deliveryTypeIds;

	public List<DeliveryMethod> getDeliveryMethod() {
		return deliveryMethod != null ? 
				Collections.unmodifiableList(deliveryMethod) : null;
	}

	public void setDeliveryMethod(List<DeliveryMethod> deliveryMethod) {
		this.deliveryMethod = deliveryMethod != null ? 
				Collections.unmodifiableList(deliveryMethod) : null;
	}

	public List<FulfillmentMethod> getFulfillmentMethod() {
		return fulfillmentMethod != null ? 
				Collections.unmodifiableList(fulfillmentMethod) : null;
	}

	public void setFulfillmentMethod(List<FulfillmentMethod> fulfillmentMethod) {
		this.fulfillmentMethod = fulfillmentMethod != null ? 
				Collections.unmodifiableList(fulfillmentMethod) : null;
	}

	public Integer getLmsApprovalStatus() {
		return lmsApprovalStatus;
	}

	public void setLmsApprovalStatus(Integer lmsApprovalStatus) {
		this.lmsApprovalStatus = lmsApprovalStatus;
	}


	public Integer getStubhubMobileTicket() {
		return stubhubMobileTicket;
	}

	public void setStubhubMobileTicket(Integer stubhubMobileTicket) {
		this.stubhubMobileTicket = stubhubMobileTicket;
	}

	public Boolean getGA() {
		return GA;
	}

	public void setGA(Boolean gA) {
		GA = gA;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ListingStatus getStatus() {
		return status;
	}

	public void setStatus(ListingStatus status) {
		this.status = status;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getGenreId() {
		return genreId;
	}

	public void setGenreId(String genreId) {
		this.genreId = genreId;
	}

	public String getPerformerId() {
		return performerId;
	}

	public void setPerformerId(String performerId) {
		this.performerId = performerId;
	}

	public String getGroupingId() {
		return groupingId;
	}

	public void setGroupingId(String groupingId) {
		this.groupingId = groupingId;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getEventDescription() {
		return eventDescription;
	}

	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}

	public String getGenreDescription() {
		return genreDescription;
	}

	public void setGenreDescription(String genreDescription) {
		this.genreDescription = genreDescription;
	}

	public String getEventDate() {
		return eventDate;
	}

	public void setEventDate(String eventDate) {
		this.eventDate = eventDate;
	}

	public Boolean getEventActive() {
		return eventActive;
	}

	public void setEventActive(Boolean eventActive) {
		this.eventActive = eventActive;
	}

	public String getInhandDate() {
		return inhandDate;
	}

	public void setInhandDate(String inhandDate) {
		this.inhandDate = inhandDate;
	}

	public String getEarliestInhandDate() { return earliestInhandDate; }

	public void setEarliestInhandDate(String earliestInhandDate) { this.earliestInhandDate = earliestInhandDate; }

	public Boolean geteInhandDateLaterThanNow() { return eInhandDateLaterThanNow; }

	public void seteInhandDateLaterThanNow(Boolean eInhandDateLaterThanNow) { this.eInhandDateLaterThanNow = eInhandDateLaterThanNow; }

	public Integer getQuantity() { return quantity; }

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getQuantityRemain() {
		return quantityRemain;
	}

	public void setQuantityRemain(Integer quantityRemain) {
		this.quantityRemain = quantityRemain;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getRows() {
		return rows;
	}

	public void setRows(String rows) {
		this.rows = rows;
	}

	public String getSeats() {
		return seats;
	}

	public void setSeats(String seats) {
		this.seats = seats;
	}

	public Long getVenueConfigSectionsId() {
		return venueConfigSectionsId;
	}

	public void setVenueConfigSectionsId(Long venueConfigSectionsId) {
		this.venueConfigSectionsId = venueConfigSectionsId;
	}

	public String getVenueDescription() {
		return venueDescription;
	}

	public void setVenueDescription(String venueDescription) {
		this.venueDescription = venueDescription;
	}

	public SplitOption getSplitOption() {
		return splitOption;
	}

	public void setSplitOption(SplitOption splitOption) {
		this.splitOption = splitOption;
	}

	public Integer getSplitQuantity() {
		return splitQuantity;
	}

	public void setSplitQuantity(Integer splitQuantity) {
		this.splitQuantity = splitQuantity;
	}

	public DeliveryOption getDeliveryOption() {
		return deliveryOption;
	}

	public void setDeliveryOption(DeliveryOption deliveryOption) {
		this.deliveryOption = deliveryOption;
	}

	public Boolean getPreDelivered() {
		return preDelivered;
	}

	public void setPreDelivered(Boolean preDelivered) {
		this.preDelivered = preDelivered;
	}

	public Boolean getAbleToRequestLMS() {
		return ableToRequestLMS;
	}

	public void setAbleToRequestLMS(Boolean ableToRequestLMS) {
		this.ableToRequestLMS = ableToRequestLMS;
	}

	public String getDateLastModified() {
		return dateLastModified;
	}

	public void setDateLastModified(String dateLastModified) {
		this.dateLastModified = dateLastModified;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Boolean getScrubbingEnabled() {
		return isScrubbingEnabled;
	}

	public void setScrubbingEnabled(Boolean scrubbingEnabled) {
		isScrubbingEnabled = scrubbingEnabled;
	}

	public String getSaleEndDate() {
		return saleEndDate;
	}

	public void setSaleEndDate(String saleEndDate) {
		this.saleEndDate = saleEndDate;
	}

	public SaleMethod getSaleMethod() {
		return saleMethod;
	}

	public void setSaleMethod(SaleMethod saleMethod) {
		this.saleMethod = saleMethod;
	}

	public Money getPricePerTicket() {
		return pricePerTicket;
	}

	public void setPricePerTicket(Money pricePerTicket) {
		this.pricePerTicket = pricePerTicket;
	}

	public Money getFaceValue() {
		return faceValue;
	}

	public void setFaceValue(Money faceValue) {
		this.faceValue = faceValue;
	}

	public Money getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(Money purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public Money getPayoutPerTicket() {
		return payoutPerTicket;
	}

	public void setPayoutPerTicket(Money payoutPerTicket) {
		this.payoutPerTicket = payoutPerTicket;
	}

	public Money getStartPricePerTicket() {
		return startPricePerTicket;
	}

	public void setStartPricePerTicket(Money startPricePerTicket) {
		this.startPricePerTicket = startPricePerTicket;
	}

	public Money getEndPricePerTicket() {
		return endPricePerTicket;
	}

	public void setEndPricePerTicket(Money endPricePerTicket) {
		this.endPricePerTicket = endPricePerTicket;
	}

	public Money getDisplayPricePerTicket() {
		return displayPricePerTicket;
	}

	public void setDisplayPricePerTicket(Money displayPricePerTicket) {
		this.displayPricePerTicket = displayPricePerTicket;
	}

	public Set<TicketTrait> getTicketTraits() {
		return ticketTraits;
	}

	public void setTicketTraits(Set<TicketTrait> ticketTraits) {
		this.ticketTraits = ticketTraits;
	}

	public String getInternalNotes() {
		return internalNotes;
	}

	public void setInternalNotes(String internalNotes) {
		this.internalNotes = internalNotes;
	}

	public Long getContactId() {
		return contactId;
	}

	public void setContactId(Long contactId) {
		this.contactId = contactId;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public Long getCcId() {
		return ccId;
	}

	public void setCcId(Long ccId) {
		this.ccId = ccId;
	}

	public String getExternalListingId() {
		return externalListingId;
	}

	public void setExternalListingId(String externalListingId) {
		this.externalListingId = externalListingId;
	}

	public String getEventTimeZone() {
		return eventTimeZone;
	}

	public void setEventTimeZone(String eventTimeZone) {
		this.eventTimeZone = eventTimeZone;
	}

	public List<Fee> getFees() {
		return fees;
	}

	public void setFees(List<Fee> fees) {
		this.fees = fees;
	}

	public Money getTotalPayout() {
		return totalPayout;
	}

	public void setTotalPayout(Money totalPayout) {
		this.totalPayout = totalPayout;
	}

	public Boolean getSectionMappingRequired() {
		return sectionMappingRequired;
	}

	public void setSectionMappingRequired(Boolean sectionMappingRequired) {
		this.sectionMappingRequired = sectionMappingRequired;
	}

	public Boolean getIsScrubbingEnabled() {
		return isScrubbingEnabled;
	}

	public void setIsScrubbingEnabled(Boolean isScrubbingEnabled) {
		this.isScrubbingEnabled = isScrubbingEnabled;
	}


	public PricingRecommendation getPricingRecommendation() {
		return pricingRecommendation;
	}

	public void setPricingRecommendation(PricingRecommendation pricingRecommendation) {
		this.pricingRecommendation = pricingRecommendation;
	}

	public Integer getTicketMediumId() {
		return ticketMediumId;
	}

	public void setTicketMediumId(Integer ticketMediumId) {
		this.ticketMediumId = ticketMediumId;
	}

	public Integer getAutoPricingEnabledInd() {
		return autoPricingEnabledInd;
	}

	public void setAutoPricingEnabledInd(Integer autoPricingEnabledInd) {
		this.autoPricingEnabledInd = autoPricingEnabledInd;
	}

	public Long getVenueConfigId() {
		return venueConfigId;
	}

	public void setVenueConfigId(Long venueConfigId) {
		this.venueConfigId = venueConfigId;
	}

	public Money getSellerInputPrice() {
		return sellerInputPrice;
	}

	public void setSellerInputPrice(Money sellerInputPrice) {
		this.sellerInputPrice = sellerInputPrice;
	}

	public String getSellerInputCurrencyCode() {
		return sellerInputCurrencyCode;
	}

	public void setSellerInputCurrencyCode(String sellerInputCurrencyCode) {
		this.sellerInputCurrencyCode = sellerInputCurrencyCode;
	}

	public String getSellerInputPriceType() {
		return sellerInputPriceType;
	}

	public void setSellerInputPriceType(String sellerInputPriceType) {
		this.sellerInputPriceType = sellerInputPriceType;
	}

	public String getZoneName() {
		return zoneName;
	}

	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}

	public String getZoneId() {
		return zoneId;
	}

	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}

	public Double getValueScore() {
		return valueScore;
	}

	public void setValueScore(Double valueScore) {
		this.valueScore = valueScore;
	}

    public List<Integer> getDeliveryTypeIds() {
        return deliveryTypeIds;
    }

    public void setDeliveryTypeIds(List<Integer> deliveryTypeIds) {
        this.deliveryTypeIds = deliveryTypeIds;
    }
}
