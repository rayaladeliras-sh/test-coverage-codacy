package com.stubhub.domain.account.common;

import java.util.Calendar;
import java.util.List;

public class SalesSearchCriteria {
	
    private PaginationInput paginationInput;
    private List<SortingDirective> sortingDirectives;   
    private Long sellerId;
	private String q;	
	private Long eventId;
	private Double price;
	private Double priceMin;
	private Double priceMax;
	private Integer soldQuantity;
	private Integer minSoldQuantity;
	private Integer maxSoldQuantity;
	private Calendar saleDateMin;
	private Calendar saleDateMax;
	private String deliveryOption;
	private Calendar eventDateMin;
	private Calendar eventDateMax;
	private String status;
	private String sellerCategoryCode;
	private Long fromSaleId;
	private Long toSaleId;
	private Long saleId;
	private String listingIds;
	private String externalListingIds;
	private boolean includePending;
	private Long buyerId;
	private String subbedId;	
	
	private String[] sectionIds;
	private String[] zoneIds;
	private String[] rowDescs;

	private String venueId;
	private String genreId;

	private Calendar inHandDateMin;
	private Calendar inHandDateMax;

	private Calendar eventDateUTCMin;
	private Calendar eventDateUTCMax;

	private Calendar dateLastModifiedMin;
	private Calendar dateLastModifiedMax;
	
	private boolean excludeCancel;
	private boolean excludeZeroCost;

	public static enum Category {
		OPEN, COMPLETE;

		public static Category fromString(String input) {
			if (OPEN.name().equalsIgnoreCase(input)) {
				return OPEN;
			}
			if (COMPLETE.name().equalsIgnoreCase(input)) {
				return COMPLETE;
			}
			return null;
		}
	}
	private Category category;

	public static enum Action {
		GENERATE, REPRINT, UPLOAD, ENTER, COURIER, PROOF, VERIFY;

		public static Action fromString(String input) {
			if (GENERATE.name().equalsIgnoreCase(input)) {
				return GENERATE;
			}
			if (REPRINT.name().equalsIgnoreCase(input)) {
				return REPRINT;
			}
			if (UPLOAD.name().equalsIgnoreCase(input)) {
				return UPLOAD;
			}
			if (ENTER.name().equalsIgnoreCase(input)) {
				return ENTER;
			}
			if (COURIER.name().equalsIgnoreCase(input)) {
				return COURIER;
			}
			if (PROOF.name().equalsIgnoreCase(input)) {
				return PROOF;
			}
			if (VERIFY.name().equalsIgnoreCase(input)) {
				return VERIFY;
			}
			return null;
		}
	}
	private Action action;

	private boolean includeVenueSummary = false;
	private boolean includeGenreSummary = false;
	private boolean includeEventSummary = false;
	private boolean includePricePerTicketSummary = false;
	private boolean includeCostPerTicketSummary = false;
	
	public String[] getSectionIds() {
		return sectionIds;
	}
	public void setSectionIds(String[] sectionIds) {
		this.sectionIds = sectionIds;
	}
	public String[] getZoneIds() {
		return zoneIds;
	}
	public void setZoneIds(String[] zoneIds) {
		this.zoneIds = zoneIds;
	}
	
	public String getSubbedId() {
		return subbedId;
	}
	public void setSubbedId(String subbedId) {
		this.subbedId = subbedId;
	}
	public Long getBuyerId() {
		return buyerId;
	}
	public void setBuyerId(Long buyerId) {
		this.buyerId = buyerId;
	}
	public String getQ() {
		return q;
	}
	public void setQ(String q) {
		this.q = q;
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
	public Integer getMinSoldQuantity() {
		return minSoldQuantity;
	}
	public void setMinSoldQuantity(Integer minSoldQuantity) {
		this.minSoldQuantity = minSoldQuantity;
	}
	public Integer getMaxSoldQuantity() {
		return maxSoldQuantity;
	}
	public void setMaxSoldQuantity(Integer maxSoldQuantity) {
		this.maxSoldQuantity = maxSoldQuantity;
	}
	
	public Calendar getSaleDateMin() {
		return saleDateMin;
	}
	public void setSaleDateMin(Calendar saleDateMin) {
		this.saleDateMin = saleDateMin;
	}
	public Calendar getSaleDateMax() {
		return saleDateMax;
	}
	public void setSaleDateMax(Calendar saleDateMax) {
		this.saleDateMax = saleDateMax;
	}
	public Calendar  getEventDateMin() {
		return eventDateMin;
	}
	public void setEventDateMin(Calendar  eventDateMin) {
		this.eventDateMin = eventDateMin;
	}
	public Calendar  getEventDateMax() {
		return eventDateMax;
	}
	public void setEventDateMax(Calendar  eventDateMax) {
		this.eventDateMax = eventDateMax;
	}
	public String getSellerCategoryCode() {
		return sellerCategoryCode;
	}
	public void setSellerCategoryCode(String sellerCategoryCode) {
		this.sellerCategoryCode = sellerCategoryCode;
	}
	public Long getFromSaleId() {
		return fromSaleId;
	}
	public void setFromSaleId(Long fromSaleId) {
		this.fromSaleId = fromSaleId;
	}
	public Long getToSaleId() {
		return toSaleId;
	}
	public void setToSaleId(Long toSaleId) {
		this.toSaleId = toSaleId;
	}
	public String getListingIds() {
		return listingIds;
	}
	public void setListingIds(String listingIds) {
		this.listingIds = listingIds;
	}
	public String getExternalListingIds() {
		return externalListingIds;
	}
	public void setExternalListingIds(String externalListingIds) {
		this.externalListingIds = externalListingIds;
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

	public Long getEventId() {
		return eventId;
	}
	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Long getSellerId() {
		return sellerId;
	}
	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}
	public String getDeliveryOption() {
		return deliveryOption;
	}
	public void setDeliveryOption(String deliveryOption) {
		this.deliveryOption = deliveryOption;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Long getSaleId() {
		return saleId;
	}
	public void setSaleId(Long saleId) {
		this.saleId = saleId;
	}
	public Integer getSoldQuantity() {
		return soldQuantity;
	}
	public void setSoldQuantity(Integer soldQuantity) {
		this.soldQuantity = soldQuantity;
	}
	public boolean isIncludePending() {
		return includePending;
	}
	public void setIncludePending(boolean includePending) {
		this.includePending = includePending;
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

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
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
	public String[] getRowDescs() {
		if(rowDescs == null){
			return new String[0];
		}
		String[] copy = new String[rowDescs.length];
		System.arraycopy(rowDescs, 0, copy, 0, rowDescs.length);
		return copy;
	}
	public void setRowDescs(String[] rowDescs) {
		if(rowDescs == null){
			this.rowDescs = null;
		} else {
			String[] copy = new String[rowDescs.length];
			System.arraycopy(rowDescs, 0, copy, 0, rowDescs.length);
			this.rowDescs = copy;
		}
	}
	public boolean isIncludePricePerTicketSummary() {
		return includePricePerTicketSummary;
	}
	public void setIncludePricePerTicketSummary(boolean includePricePerTicketSummary) {
		this.includePricePerTicketSummary = includePricePerTicketSummary;
	}
	public boolean isIncludeCostPerTicketSummary() {
		return includeCostPerTicketSummary;
	}
	public void setIncludeCostPerTicketSummary(boolean includeCostPerTicketSummary) {
		this.includeCostPerTicketSummary = includeCostPerTicketSummary;
	}
	public boolean isExcludeCancel() {
		return excludeCancel;
	}
	public void setExcludeCancel(boolean excludeCancel) {
		this.excludeCancel = excludeCancel;
	}
	public boolean isExcludeZeroCost() {
		return excludeZeroCost;
	}
	public void setExcludeZeroCost(boolean excludeZeroCost) {
		this.excludeZeroCost = excludeZeroCost;
	}

	public Calendar getEventDateUTCMin() {
		return eventDateUTCMin;
	}

	public void setEventDateUTCMin(Calendar eventDateUTCMin) {
		this.eventDateUTCMin = eventDateUTCMin;
	}

	public Calendar getEventDateUTCMax() {
		return eventDateUTCMax;
	}

	public void setEventDateUTCMax(Calendar eventDateUTCMax) {
		this.eventDateUTCMax = eventDateUTCMax;
	}

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
}
