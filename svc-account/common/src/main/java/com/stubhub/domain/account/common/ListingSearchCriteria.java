package com.stubhub.domain.account.common;

import java.util.Calendar;
import java.util.List;

import com.stubhub.domain.account.common.enums.DeliveryOption;
import com.stubhub.domain.account.common.enums.ListingStatus;





public class ListingSearchCriteria {
    private Long sellerId;
    private PaginationInput paginationInput;
    private List<SortingDirective> sortingDirectives;    
	private Integer start;
	private Integer rows;
	private String q;
	private String eventId;
	private String[] geographyId;
	private Double price;
	private Double priceMin;
	private Double priceMax;
	private Calendar eventDateMin;
	private Calendar eventDateMax;
	private Calendar inHandDateMin;
	private Calendar inHandDateMax;	
	private Calendar saleEndDateMin;
	private Calendar saleEndDateMax;
	private Calendar dateLastModifiedMin;
	private Calendar dateLastModifiedMax;
	private String sortField;
	private boolean ascending;
	private ListingStatus status;
	private DeliveryOption deliveryOption;	
	private Long listingId;
	private String venueId;
	private String genreId;
	private String externalListingId;

	private boolean includeVenueSummary = false;
	private boolean includeGenreSummary = false;
	private boolean includeEventSummary = false;
	private boolean includeDeleted = false;
	private boolean includeSold = false;

	public Calendar getDateLastModifiedMin() {
		return dateLastModifiedMin;
	}

	public void setDateLastModifiedMin(Calendar dateLastModifiedMin) {
		this.dateLastModifiedMin = dateLastModifiedMin;
	}

	public Calendar getDateLastModifiedMax() {
		return dateLastModifiedMax;
	}

	public void setDateLastModifiedMax(Calendar dateLastModifiedMax) {
		this.dateLastModifiedMax = dateLastModifiedMax;
	}

	public ListingStatus getStatus() {
		return status;
	}
	public void setStatus(ListingStatus status) {
		this.status = status;
	}
	
	public DeliveryOption getDeliveryOption() {
		return deliveryOption;
	}
	public void setDeliveryOption(DeliveryOption deliveryOption) {
		this.deliveryOption = deliveryOption;
	}
	public Long getSellerId() {
		return sellerId;
	}
	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}	
	public PaginationInput getPaginationInput() {
		return paginationInput;
	}
	public void setPaginationInput(PaginationInput paginationInput) {
		this.paginationInput = paginationInput;
	}
	public List<SortingDirective> getSortingDirectives() {
		return sortingDirectives;
	}
	public void setSortingDirectives(List<SortingDirective> sortingDirectives) {
		this.sortingDirectives = sortingDirectives;
	}
	public Integer getStart() {
		return start;
	}
	public void setStart(Integer start) {
		this.start = start;
	}
	public Integer getRows() {
		return rows;
	}
	public void setRows(Integer rows) {
		this.rows = rows;
	}
	public String getQ() {
		return q;
	}
	public void setQ(String q) {
		this.q = q;
	}
	public String getEventId() {
		return eventId;
	}
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	public String[] getGeographyId() {
		return geographyId;
	}
	public void setGeographyId(String[] geographyId) {
		this.geographyId = geographyId;
	}
	public Double getPriceMin() {
		return priceMin;
	}
	public void setPriceMin(Double priceMin) {
		this.priceMin = priceMin;
	}
	public Double getPriceMax() {
		return priceMax;
	}
	public void setPriceMax(Double priceMax) {
		this.priceMax = priceMax;
	}
	
	public Calendar getEventDateMin() {
		return eventDateMin;
	}
	public void setEventDateMin(Calendar eventDateMin) {
		this.eventDateMin = eventDateMin;
	}
	public Calendar getEventDateMax() {
		return eventDateMax;
	}
	public void setEventDateMax(Calendar eventDateMax) {
		this.eventDateMax = eventDateMax;
	}
	public String getSortField() {
		return sortField;
	}
	public void setSortField(String sortField) {
		this.sortField = sortField;
	}
	public boolean isAscending() {
		return ascending;
	}
	public void setAscending(boolean ascending) {
		this.ascending = ascending;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Calendar getInHandDateMin() {
		return inHandDateMin;
	}
	public void setInHandDateMin(Calendar inHandDateMin) {
		this.inHandDateMin = inHandDateMin;
	}
	public Calendar getInHandDateMax() {
		return inHandDateMax;
	}
	public void setInHandDateMax(Calendar inHandDateMax) {
		this.inHandDateMax = inHandDateMax;
	}
	public Calendar getSaleEndDateMin() {
		return saleEndDateMin;
	}
	public void setSaleEndDateMin(Calendar saleEndDateMin) {
		this.saleEndDateMin = saleEndDateMin;
	}
	public Calendar getSaleEndDateMax() {
		return saleEndDateMax;
	}
	public void setSaleEndDateMax(Calendar saleEndDateMax) {
		this.saleEndDateMax = saleEndDateMax;
	}
	public Long getListingId() {
		return listingId;
	}
	public void setListingId(Long listingId) {
		this.listingId = listingId;
	}

	public String getVenueId() {
		return venueId;
	}

	public void setVenueId(String venueId) {
		this.venueId = venueId;
	}

	public String getGenreId() {
		return genreId;
	}

	public void setGenreId(String genreId) {
		this.genreId = genreId;
	}

	public boolean isIncludeVenueSummary() {
		return includeVenueSummary;
	}

	public void setIncludeVenueSummary(boolean includeVenueSummary) {
		this.includeVenueSummary = includeVenueSummary;
	}

	public boolean isIncludeGenreSummary() {
		return includeGenreSummary;
	}

	public void setIncludeGenreSummary(boolean includeGenreSummary) {
		this.includeGenreSummary = includeGenreSummary;
	}

	public boolean isIncludeEventSummary() {
		return includeEventSummary;
	}

	public void setIncludeEventSummary(boolean includeEventSummary) {
		this.includeEventSummary = includeEventSummary;
	}

	public boolean isIncludeDeleted() {
		return includeDeleted;
	}

	public void setIncludeDeleted(boolean includeDeleted) {
		this.includeDeleted = includeDeleted;
	}

	public boolean isIncludeSold() {
		return includeSold;
	}

	public void setIncludeSold(boolean includeSold) {
		this.includeSold = includeSold;
	}

	public String getExternalListingId() {
		return externalListingId;
	}
	public void setExternalListingId(String externalListingId) {
		this.externalListingId = externalListingId;
	}
}
