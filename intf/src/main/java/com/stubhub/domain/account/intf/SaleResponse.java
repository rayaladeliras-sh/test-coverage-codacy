package com.stubhub.domain.account.intf;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.map.annotate.JsonRootName;

import com.stubhub.domain.account.common.Response;
import com.stubhub.domain.account.common.enums.DeliveryOption;
import com.stubhub.domain.account.common.enums.SaleStatus;
import com.stubhub.domain.account.common.enums.SalesSubStatus;
import com.stubhub.domain.account.common.enums.TicketTrait;
import com.stubhub.newplatform.common.entity.Money;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "sale")
@JsonRootName("sale")
@XmlType(name = "", propOrder = {
		"eventId",
		"genreId",
		"quantity",
		"pricePerTicket",
		"inhandDate",
		"inhandDatePST",
		"earliestInhandDate",
		"eInhandDateLaterThanNow",
		"section",
		"sectionId",
		"rows",
		"row",
		"seats",
		"seatDetails",
		"deliveryOption",
		"deliveryTypeId",
		"deliveryMethodId",
		"saleDate",
		"dateLastModified",
		"ticketTraits",
		"internalNotes",
		"externalListingId",		
		"status",		
		"paymentType",
		"contactId",
		"displayPricePerTicket",
		"payoutPerTicket",		
		"eventDescription",
		"eventDate",
		"hideEventDate",
		"hideEventTime",
		"eventTimeZone",
		"venueDescription",
		"venueConfigId",
		"listingId",
		"saleId",
		"totalPayout",
		"transactionDate",
		"inHand",
		"trackingNumber",
		"subStatus",
		"city",
		"state",
		"country",
        "GA",
        "buyerEmail",
        "buyerFirstName",
        "buyerLastName",
        "stubhubMobileTicket",
		"fulfillmentMethodId",
		"deliveryMethodDisplayName",
		"deliveryMethodShortInstruction",
		"deliveryMethodShortAppInstruction",
		"deliveryMethodLongInstruction",
		"deliveryMethodLongAppInstruction",
		"fulfillmentMethodDisplayName",
		"fulfillmentMethodShortInstruction",
		"fulfillmentMethodShortAppInstruction",
		"fulfillmentMethodLongInstruction",
		"fulfillmentMethodLongAppInstruction",
		"externalTransfer",
		"buyerPhoneNumber",
		"buyerPhoneCallingCode",
        "sellerContactId",
        "preDelivery",
        "ticketMediumId",
        "courierAccountId",
        "courierId",
        "trackingUrl",
        "courierName",
        "courierSmallLogo",
        "attendeeConfigId",
        "attendeeCompleted",
		"localAddressCompleted",
		"links",
		"deliveriesFlag",
		"transEmailAddr",
		"deliverPrimaryName",
		"urlPartialTransferred",
        "urlTransInd","seatQualityScore","bestValueScore","venueConfigSectionId",
		"zoneId", "zoneName",
		"sellerOwnSale",
		})

public final class SaleResponse extends Response implements TranslatableEventData {
	
	@XmlElement(name = "saleId", required = true)
	private String saleId;
	@XmlElement(name = "eventId", required = false)
	private String eventId;
	@XmlElement(name = "genreId", required = false)
	private String genreId;
	@XmlElement(name = "quantity", required = false)
	private Integer quantity;
	@XmlElement(name = "pricePerTicket", required = false)
	private Money pricePerTicket;
	// inhandDate format is "YYYY-MM-DDTHH24:MI:SS" in event local timezone
	@XmlElement(name = "inhandDate", required = false)
	private String inhandDate;
	@XmlElement(name = "inhandDatePST", required = false)
	private String inhandDatePST;
	@XmlElement(name = "earliestInhandDate", required = false)
	private String earliestInhandDate;
	@XmlElement(name = "eInhandDateLaterThanNow", required = false)
	private Boolean eInhandDateLaterThanNow;
	@XmlElement(name = "section", required = false)
	private String section;
	@XmlElement(name = "rows", required = false)
	private String rows;
	@XmlElement(name = "row", required = false)
	private String row;
	@XmlElement(name = "seats", required = false)
	private String seats;
	@XmlElement(name="seatDetails",required = false)
	private List<SeatDetail> seatDetails;
	@XmlElement(name = "deliveryOption", required = false)
	private DeliveryOption deliveryOption;
	@XmlElement(name = "deliveryTypeId", required = false)
	private Integer deliveryTypeId;
	@XmlElement(name = "deliveryMethodId", required = false)
	private Integer deliveryMethodId;
	//saleDate format is "YYYY-MM-DDTHH24:MI:SS" in event local timezone
	@XmlElement(name = "saleDate", required = false)
	private String saleDate;
	@XmlElement(name="dateLastModified", required = false)
	private String dateLastModified;
	@XmlElement(name = "ticketTraits", required = false)
	private Set<TicketTrait> ticketTraits;
	@XmlElement(name = "internalNotes", required = false)
	private String internalNotes;
	@XmlElement(name = "externalListingId", required = false)
	private String externalListingId;
	@XmlElement(name = "status", required = true)
	private SaleStatus status;
	@XmlElement(name = "paymentType", required = false)
	private String paymentType;
	@XmlElement(name = "contactId", required = false)
	private Long contactId;
	@XmlElement(name = "displayPricePerTicket", required = false)
	private Money displayPricePerTicket;
	@XmlElement(name = "payoutPerTicket", required = false)
	private Money payoutPerTicket;
	@XmlElement(name = "totalPayout", required = false)
	private Money totalPayout;
	@XmlElement(name = "eventDescription", required = false)
	private String eventDescription;
	//eventDate format is "YYYY-MM-DDTHH24:MI:SS" in event local timezone
	@XmlElement(name = "eventDate", required = false)
	private String eventDate;
	@XmlElement(name = "hideEventDate", required = false)
	private Boolean hideEventDate;
	@XmlElement(name = "hideEventTime", required = false)
	private Boolean hideEventTime;
	@XmlElement(name = "eventTimeZone", required = false)
	private String eventTimeZone;	
	@XmlElement(name = "venueDescription", required = false)
	private String venueDescription;	
	@XmlElement(name = "listingId", required = false)
	private String listingId;

    @XmlElement(name = "stubhubMobileTicket", required = false)
	private Integer stubhubMobileTicket = 0;

	@XmlElement(name = "fulfillmentMethodId", required = false)
	private Integer fulfillmentMethodId;

	@XmlElement(name = "sellerContactId", required = false)
	private Long sellerContactId;
	
    @XmlElement(name = "preDelivery", required = false)
    private Boolean preDelivery;
    
    @XmlElement(name = "sectionId", required = false)
    private Long sectionId;

	@XmlElement(name = "ticketMediumId", required = false)
	private Integer ticketMediumId;

	@XmlElement(name = "courierAccountId", required = false)
	private Long courierAccountId;

	@XmlElement(name = "courierId", required = false)
	private Long courierId;

	@XmlElement(name = "trackingUrl", required = false)
	private String trackingUrl;

	@XmlElement(name = "courierName", required = false)
	private String courierName;

	@XmlElement(name = "courierSmallLogo", required = false)
	private String courierSmallLogo;

    //Localized messages for delivery method id and fulfillment method id
    @XmlElement(name = "deliveryMethodDisplayName", required = false)
	private String deliveryMethodDisplayName;
    @XmlElement(name = "deliveryMethodShortInstruction", required = false)
	private String deliveryMethodShortInstruction;
    @XmlElement(name = "deliveryMethodShortAppInstruction", required = false)
	private String deliveryMethodShortAppInstruction;
    @XmlElement(name = "deliveryMethodLongInstruction", required = false)
	private String deliveryMethodLongInstruction;
    @XmlElement(name = "deliveryMethodLongAppInstruction", required = false)
	private String deliveryMethodLongAppInstruction;
    @XmlElement(name = "fulfillmentMethodDisplayName", required = false)
	private String fulfillmentMethodDisplayName;
    @XmlElement(name = "fulfillmentMethodShortInstruction", required = false)
	private String fulfillmentMethodShortInstruction;
    @XmlElement(name = "fulfillmentMethodShortAppInstruction", required = false)
	private String fulfillmentMethodShortAppInstruction;
    @XmlElement(name = "fulfillmentMethodLongInstruction", required = false)
	private String fulfillmentMethodLongInstruction;
    @XmlElement(name = "fulfillmentMethodLongAppInstruction", required = false)
	private String fulfillmentMethodLongAppInstruction;
    
    @XmlElement(name = "externalTransfer", required = false)
    private Boolean externalTransfer;

    @XmlElement(name = "attendeeConfigId", required = false)
    private Long attendeeConfigId;

    @XmlElement(name = "attendeeCompleted", required = false)
    private Boolean attendeeCompleted = null;

    @XmlElement(name = "localAddressCompleted", required = false)
	private Boolean localAddressCompleted = null;
    
	@XmlElement(name = "links", required = false)
    private List<Link> links = new ArrayList<Link>();

	@XmlElement(name = "venueConfigId", required = false)
	private Long venueConfigId;

	@XmlElement(name = "deliveriesFlag", required = false)
	private Long deliveriesFlag;

	@XmlElement(name = "urlTransInd", required = false)
	private Boolean urlTransInd;

	@XmlElement(name = "transEmailAddr" , required = false)
	private String transEmailAddr;

	@XmlElement(name = "deliverPrimaryName" , required = false)
	private String deliverPrimaryName;

	@XmlElement(name = "seatQualityScore" , required = false)
	private  Double seatQualityScore;

	@XmlElement(name = "seatQualityScore" , required = false)
	private  Double bestValueScore;

	private Long venueConfigSectionId;

	@XmlElement(name = "zoneId" , required = false)
	private Long zoneId;

	@XmlElement(name = "zoneName" , required = false)
	private String zoneName;

	@XmlElement(name = "sellerOwnSale" , required = false)
	private Boolean sellerOwnSale;

	public String getTransEmailAddr() {
		return transEmailAddr;
	}

	public void setTransEmailAddr(String transEmailAddr) {
		this.transEmailAddr = transEmailAddr;
	}

	public Long getDeliveriesFlag() {
		return deliveriesFlag;
	}

	public void setDeliveriesFlag(Long deliveriesFlag) {
		this.deliveriesFlag = deliveriesFlag;
	}

	public String getDateLastModified() {
		return dateLastModified;
	}

	public void setDateLastModified(String dateLastModified) {
		this.dateLastModified = dateLastModified;
	}

	public Long getAttendeeConfigId() {
      return attendeeConfigId;
    }

    public void setAttendeeConfigId(Long attendeeConfigId) {
      this.attendeeConfigId = attendeeConfigId;
    }

    public Boolean getAttendeeCompleted() {
      return attendeeCompleted;
    }

    public void setAttendeeCompleted(Boolean attendeeCompleted) {
      this.attendeeCompleted = attendeeCompleted;
    }

    public String getDeliveryMethodDisplayName() {
		return deliveryMethodDisplayName;
	}

	public void setDeliveryMethodDisplayName(String deliveryMethodDisplayName) {
		this.deliveryMethodDisplayName = deliveryMethodDisplayName;
	}

	public String getDeliveryMethodShortInstruction() {
		return deliveryMethodShortInstruction;
	}

	public void setDeliveryMethodShortInstruction(
			String deliveryMethodShortInstruction) {
		this.deliveryMethodShortInstruction = deliveryMethodShortInstruction;
	}

	public String getDeliveryMethodShortAppInstruction() {
		return deliveryMethodShortAppInstruction;
	}

	public void setDeliveryMethodShortAppInstruction(
			String deliveryMethodShortAppInstruction) {
		this.deliveryMethodShortAppInstruction = deliveryMethodShortAppInstruction;
	}

	public String getDeliveryMethodLongInstruction() {
		return deliveryMethodLongInstruction;
	}

	public void setDeliveryMethodLongInstruction(
			String deliveryMethodLongInstruction) {
		this.deliveryMethodLongInstruction = deliveryMethodLongInstruction;
	}

	public String getDeliveryMethodLongAppInstruction() {
		return deliveryMethodLongAppInstruction;
	}

	public void setDeliveryMethodLongAppInstruction(
			String deliveryMethodLongAppInstruction) {
		this.deliveryMethodLongAppInstruction = deliveryMethodLongAppInstruction;
	}

	public String getFulfillmentMethodDisplayName() {
		return fulfillmentMethodDisplayName;
	}

	public void setFulfillmentMethodDisplayName(String fulfillmentMethodDisplayName) {
		this.fulfillmentMethodDisplayName = fulfillmentMethodDisplayName;
	}

	public String getFulfillmentMethodShortInstruction() {
		return fulfillmentMethodShortInstruction;
	}

	public void setFulfillmentMethodShortInstruction(
			String fulfillmentMethodShortInstruction) {
		this.fulfillmentMethodShortInstruction = fulfillmentMethodShortInstruction;
	}

	public String getFulfillmentMethodShortAppInstruction() {
		return fulfillmentMethodShortAppInstruction;
	}

	public void setFulfillmentMethodShortAppInstruction(
			String fulfillmentMethodShortAppInstruction) {
		this.fulfillmentMethodShortAppInstruction = fulfillmentMethodShortAppInstruction;
	}

	public String getFulfillmentMethodLongInstruction() {
		return fulfillmentMethodLongInstruction;
	}

	public void setFulfillmentMethodLongInstruction(
			String fulfillmentMethodLongInstruction) {
		this.fulfillmentMethodLongInstruction = fulfillmentMethodLongInstruction;
	}

	public String getFulfillmentMethodLongAppInstruction() {
		return fulfillmentMethodLongAppInstruction;
	}

	public void setFulfillmentMethodLongAppInstruction(
			String fulfillmentMethodLongAppInstruction) {
		this.fulfillmentMethodLongAppInstruction = fulfillmentMethodLongAppInstruction;
	}

	public Boolean getPreDelivery() {
        return preDelivery;
    }
    
    public void setPreDelivery(Boolean preDelivery) {
        this.preDelivery = preDelivery;
    }
    
	public Integer getStubhubMobileTicket() {
		return stubhubMobileTicket;
	}

	@Override
    public void setStubhubMobileTicket(Integer stubhubMobileTicket) {
		this.stubhubMobileTicket = stubhubMobileTicket;
	}

	@XmlElement(name = "transactionDate", required = false)
	private Calendar transactionDate;
	
	@XmlElement(name = "inHand", required = false)
	private Boolean inHand;
	
	@XmlElement(name = "trackingNumber", required = false)
	private String trackingNumber;
	
	@XmlElement(name = "subStatus", required = false)
	private SalesSubStatus subStatus;
	
	@XmlElement(name = "city", required = false)
	private String city;
	
	@XmlElement(name = "state", required = false)
	private String state;

	@XmlElement(name = "country", required = false)
	private String country;
	
	@XmlElement(name = "GA", required = false)
	private Boolean GA;
    
    @XmlElement(name = "buyerEmail", required = false)
    private String buyerEmail;
    
    @XmlTransient
    private String buyerId;
    
    @XmlElement(name = "buyerFirstName", required = false)
    private String buyerFirstName;
    
    @XmlElement(name = "buyerLastName", required = false)
    private String buyerLastName;

	@XmlElement(name = "buyerPhoneNumber", required = false)
	private String buyerPhoneNumber;

	@XmlElement(name = "buyerPhoneCallingCode", required = false)
	private String buyerPhoneCallingCode;

	@XmlElement(name = "urlPartialTransferred", required = false)
	private Boolean urlPartialTransferred;

    public String getBuyerId() {
        return buyerId;
    }
    
    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getBuyerEmail() {
        return buyerEmail;
    }
    
    public void setBuyerEmail(String buyerEmail) {
        this.buyerEmail = buyerEmail;
    }
    
    public String getBuyerFirstName() {
		return buyerFirstName;
	}

	public void setBuyerFirstName(String buyerFirstName) {
		this.buyerFirstName = buyerFirstName;
	}

	public String getBuyerLastName() {
		return buyerLastName;
	}

	public void setBuyerLastName(String buyerLastName) {
		this.buyerLastName = buyerLastName;
	}

	public Boolean getGA() {
		return GA;
	}

	public void setGA(Boolean gA) {
		GA = gA;
	}
	
	public Calendar getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(Calendar transactionDate) {
		this.transactionDate = transactionDate;
	}
	public Money getPricePerTicket() {
		return pricePerTicket;
	}
	public void setPricePerTicket(Money pricePerTicket) {
		this.pricePerTicket = pricePerTicket;
	}
	public String getInhandDate() {
		return inhandDate;
	}
	public void setInhandDate(String inhandDate) {
		this.inhandDate = inhandDate;
	}
	public String getInhandDatePST() {
		return inhandDatePST;
	}
	public void setInhandDatePST(String inhandDatePST) {
		this.inhandDatePST = inhandDatePST;
	}
	public String getEarliestInhandDate() { return earliestInhandDate; }
	public void setEarliestInhandDate(String earliestInhandDate) { this.earliestInhandDate = earliestInhandDate; }
	public Boolean geteInhandDateLaterThanNow() { return eInhandDateLaterThanNow; }
	public void seteInhandDateLaterThanNow(Boolean eInhandDateLaterThanNow) { this.eInhandDateLaterThanNow = eInhandDateLaterThanNow; }
	public String getExternalListingId() {
		return externalListingId;
	}
	public void setExternalListingId(String externalListingId) {
		this.externalListingId = externalListingId;
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

	public List<SeatDetail> getSeatDetails() { return seatDetails; }

	public void setSeatDetails(List<SeatDetail> seatDetails) { this.seatDetails = seatDetails; }

	public DeliveryOption getDeliveryOption() {
		return deliveryOption;
	}
	public void setDeliveryOption(DeliveryOption deliveryOption) {
		this.deliveryOption = deliveryOption;
	}
	public Set<TicketTrait> getTicketTraits() {
		return ticketTraits;
	}
	public void setTicketTraits(Set<TicketTrait> ticketTraits) {
		this.ticketTraits = ticketTraits;
	}
	public String getSaleDate() {
		return saleDate;
	}
	public void setSaleDate(String saleDate) {
		this.saleDate = saleDate;
	}
	public String getSaleId() {
		return saleId;
	}
	public void setSaleId(String saleId) {
		this.saleId = saleId;
	}
	public SaleStatus getStatus() {
		return status;
	}
	public void setStatus(SaleStatus status) {
		this.status = status;
	}
	public void setVenueDescription(String venueDescription) {
		this.venueDescription = venueDescription;
	}
	public String getVenueDescription() {
		return venueDescription;
	}
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	@Override
    public String getEventId() {
		return eventId;
	}
	public void setInternalNotes(String internalNotes) {
		this.internalNotes = internalNotes;
	}
	public String getInternalNotes() {
		return internalNotes;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setContactId(Long contactId) {
		this.contactId = contactId;
	}
	public Long getContactId() {
		return contactId;
	}
	public void setDisplayPricePerTicket(Money displayPricePerTicket) {
		this.displayPricePerTicket = displayPricePerTicket;
	}
	public Money getDisplayPricePerTicket() {
		return displayPricePerTicket;
	}
	public void setPayoutPerTicket(Money payoutPerTicket) {
		this.payoutPerTicket = payoutPerTicket;
	}
	public Money getPayoutPerTicket() {
		return payoutPerTicket;
	}
	public Money getTotalPayout() {
		return totalPayout;
	}
	public void setTotalPayout(Money totalPayout) {
		this.totalPayout = totalPayout;
	}
	@Override
    public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}
	@Override
    public String getEventDescription() {
		return eventDescription;
	}

	public void setEventDate(String eventDate) {
		this.eventDate = eventDate;
	}
	public String getEventDate() {
		return eventDate;
	}

	public Boolean getHideEventDate() {
		return hideEventDate;
	}

	public void setHideEventDate(Boolean hideEventDate) {
		this.hideEventDate = hideEventDate;
	}

	public Boolean getHideEventTime() {
		return hideEventTime;
	}

	public void setHideEventTime(Boolean hideEventTime) {
		this.hideEventTime = hideEventTime;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setListingId(String listingId) {
		this.listingId = listingId;
	}
	public String getListingId() {
		return listingId;
	}
	public String getEventTimeZone() {
		return eventTimeZone;
	}
	public void setEventTimeZone(String eventTimeZone) {
		this.eventTimeZone = eventTimeZone;
	}
	public Boolean getInHand() {
		return inHand;
	}
	public void setInHand(Boolean inHand) {
		this.inHand = inHand;
	}
	public String getTrackingNumber() {
		return trackingNumber;
	}
	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}
	public SalesSubStatus getSubStatus() {
		return subStatus;
	}
	public void setSubStatus(SalesSubStatus subStatus) {
		this.subStatus = subStatus;
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
	public String getGenreId() {
		return genreId;
	}

	public void setGenreId(String genreId) {
		this.genreId = genreId;
	}

	public Integer getFulfillmentMethodId() {
		return fulfillmentMethodId;
	}

	public void setFulfillmentMethodId(Integer fulfillmentMethodId) {
		this.fulfillmentMethodId = fulfillmentMethodId;
	}

	public Long getSellerContactId() {
		return sellerContactId;
	}

	public void setSellerContactId(Long sellerContactId) {
		this.sellerContactId = sellerContactId;
	}

	public Integer getDeliveryTypeId() {
		return deliveryTypeId;
	}

	public void setDeliveryTypeId(Integer deliveryTypeId) {
		this.deliveryTypeId = deliveryTypeId;
	}

	public Integer getDeliveryMethodId() {
		return deliveryMethodId;
	}

	public void setDeliveryMethodId(Integer deliveryMethodId) {
		this.deliveryMethodId = deliveryMethodId;
	}

	public Long getSectionId() {
		return sectionId;
	}

	public void setSectionId(Long sectionId) {
		this.sectionId = sectionId;
	}

	public Integer getTicketMediumId() {
		return ticketMediumId;
	}

	public void setTicketMediumId(Integer ticketMediumId) {
		this.ticketMediumId = ticketMediumId;
	}

	public Long getCourierAccountId() {
		return courierAccountId;
	}

	public void setCourierAccountId(Long courierAccountId) {
		this.courierAccountId = courierAccountId;
	}

	public Long getCourierId() {
		return courierId;
	}

	public void setCourierId(Long courierId) {
		this.courierId = courierId;
	}

	public String getTrackingUrl() {
		return trackingUrl;
	}

	public void setTrackingUrl(String trackingUrl) {
		this.trackingUrl = trackingUrl;
	}

	public String getCourierName() {
		return courierName;
	}

	public void setCourierName(String courierName) {
		this.courierName = courierName;
	}

	public String getCourierSmallLogo() {
		return courierSmallLogo;
	}

	public void setCourierSmallLogo(String courierSmallLogo) {
		this.courierSmallLogo = courierSmallLogo;
	}

	public Boolean getExternalTransfer() {
		return externalTransfer;
	}

	public void setExternalTransfer(Boolean externalTransfer) {
		this.externalTransfer = externalTransfer;
	}

	public Boolean getLocalAddressCompleted() {
		return localAddressCompleted;
	}

	public void setLocalAddressCompleted(Boolean localAddressCompleted) {
		this.localAddressCompleted = localAddressCompleted;
	}
	
	public List<Link> getLinks() {
		return links != null ? Collections.unmodifiableList(links) : null;
	}

	public void setLinks(List<Link> links) {
		this.links = links != null ? Collections.unmodifiableList(links) : null;
	}

	public String getBuyerPhoneNumber() {
		return buyerPhoneNumber;
	}

	public void setBuyerPhoneNumber(String buyerPhoneNumber) {
		this.buyerPhoneNumber = buyerPhoneNumber;
	}

	public String getBuyerPhoneCallingCode() {
		return buyerPhoneCallingCode;
	}

	public void setBuyerPhoneCallingCode(String buyerPhoneCallingCode) {
		this.buyerPhoneCallingCode = buyerPhoneCallingCode;
	}

	@Override
	public void setVenueConfigId(Long venueConfigId) {
		this.venueConfigId = venueConfigId;
	}

	public void setVenueConfigSectionId(Long venueConfigSectionId) {
    	this.venueConfigSectionId=venueConfigSectionId;

	}

	public Long getVenueConfigSectionId() {
		return venueConfigSectionId;
	}

	@Override
	public Long getVenueConfigId() {
		return venueConfigId;
	}

	public String getDeliverPrimaryName() {
		return deliverPrimaryName;
	}

	public void setDeliverPrimaryName(String deliverPrimaryName) {
		this.deliverPrimaryName = deliverPrimaryName;
	}

	public Boolean getUrlPartialTransferred() {
		return urlPartialTransferred;
	}

	public void setUrlPartialTransferred(Boolean urlPartialTransferred) {
		this.urlPartialTransferred = urlPartialTransferred;
	}

	public Boolean getUrlTransInd() {
		return urlTransInd;
	}

	public void setUrlTransInd(Boolean urlTransInd) {
		this.urlTransInd = urlTransInd;
	}

	public Double getSeatQualityScore() {
		return seatQualityScore;
	}

	public void setSeatQualityScore(Double seatQualityScore) {
		this.seatQualityScore = seatQualityScore;
	}

	public Double getBestValueScore() {
		return bestValueScore;
	}

	public void setBestValueScore(Double bestValueScore) {
		this.bestValueScore = bestValueScore;
	}

	public Long getZoneId() {
		return zoneId;
	}

	public void setZoneId(Long zoneId) {
		this.zoneId = zoneId;
	}

	public String getZoneName() {
		return zoneName;
	}

	public void setSellerOwnSale(Boolean sellerOwnSale) {
		this.sellerOwnSale = sellerOwnSale;
	}

	public Boolean getSellerOwnSale() {
		return sellerOwnSale;
	}

	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}

	@Override
	public String toString() {
		return "SaleResponse{" +
				"saleId='" + saleId + '\'' +
				", eventId='" + eventId + '\'' +
				", genreId='" + genreId + '\'' +
				", quantity=" + quantity +
				", pricePerTicket=" + pricePerTicket +
				", section='" + section + '\'' +
				", rows='" + rows + '\'' +
				", row='" + row + '\'' +
				", seats='" + seats + '\'' +
				", deliveryTypeId=" + deliveryTypeId +
				", deliveryMethodId=" + deliveryMethodId +
				", saleDate='" + saleDate + '\'' +
				", dateLastModified='" + dateLastModified + '\'' +
				", externalListingId='" + externalListingId + '\'' +
				", status=" + status +
				", displayPricePerTicket=" + displayPricePerTicket +
				", eventDescription='" + eventDescription + '\'' +
				", eventDate='" + eventDate + '\'' +
				", hideEventDate=" + hideEventDate +
				", hideEventTime=" + hideEventTime +
				", eventTimeZone='" + eventTimeZone + '\'' +
				", venueDescription='" + venueDescription + '\'' +
				", listingId='" + listingId + '\'' +
				", venueConfigId=" + venueConfigId +
				", seatQualityScore=" + seatQualityScore +
				", bestValueScore=" + bestValueScore +
				", venueConfigSectionId=" + venueConfigSectionId +
				", zoneId=" + zoneId +
				", zoneName=" + zoneName +
				", sellerOwnSale=" + sellerOwnSale +
				'}';
	}
}