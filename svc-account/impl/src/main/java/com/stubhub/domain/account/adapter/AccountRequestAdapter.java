package com.stubhub.domain.account.adapter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import com.stubhub.domain.account.common.*;
import com.stubhub.domain.account.common.Error;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stubhub.domain.account.common.enums.DeliveryOption;
import com.stubhub.domain.account.common.enums.ErrorCode;
import com.stubhub.domain.account.common.enums.ErrorType;
import com.stubhub.domain.account.common.enums.ListingFilterType;
import com.stubhub.domain.account.common.enums.ListingSortType;
import com.stubhub.domain.account.common.enums.ListingStatus;
import com.stubhub.domain.account.common.enums.SaleSortType;
import com.stubhub.domain.account.common.enums.SalesFilterType;
import com.stubhub.domain.account.common.enums.SortColumnType;
import com.stubhub.domain.account.common.enums.SortOrderType;
import com.stubhub.domain.account.common.exception.AccountException;
import com.stubhub.domain.account.common.util.AccountConstants;
import com.stubhub.domain.account.common.util.ValidationUtil;
import com.stubhub.domain.account.intf.CSOrderDetailsRequest;
import com.stubhub.domain.account.intf.CSOrderDetailsResponse;
import com.stubhub.domain.account.intf.CSSaleDetailsResponse;
import com.stubhub.domain.account.intf.CSSalesResponse;
import com.stubhub.domain.account.intf.OrdersResponse;
import com.stubhub.domain.account.intf.SalesHistoryRequest;
import com.stubhub.domain.common.util.StringUtils;
import com.stubhub.domain.i18n.services.localization.v1.utility.DataSourceMessageSource;
import com.stubhub.domain.infrastructure.soa.core.context.SHServiceContext;
import com.stubhub.newplatform.common.util.DateUtil;
import com.stubhub.newplatform.common.util.SecurityUtil;

public class AccountRequestAdapter {
	

	private final static Logger log = LoggerFactory.getLogger(AccountRequestAdapter.class);

	private static final String PRICE_TYPE_LISTPRICE = "listprice";
	private static final String PRICE_TYPE_ALLINPRICE = "allinprice";

	public static ListingSearchCriteria convertRequestToListingSearchCriteria(String sellerId,
																			  PaginationInput paginationInput, String listingSortColumnOrder, String filters, SummaryInput summaryInput, ListingInput listingInput, SHServiceContext serviceContext, DataSourceMessageSource messageSource) throws ParseException {
		if(paginationInput == null){
			paginationInput = new PaginationInput();
			paginationInput.setRows(PaginationInput.DEFAULT_ENTRIES_PERPAGE);
			paginationInput.setStart(0);
		}else{
			if (paginationInput.getRows() <= 0
					|| paginationInput.getRows() > PaginationInput.DEFAULT_ENTRIES_PERPAGE) {
				paginationInput.setRows(PaginationInput.DEFAULT_ENTRIES_PERPAGE);
			}
			if (paginationInput.getStart() <= 0) {
				paginationInput.setStart(0);
			}			
		}
		if(listingSortColumnOrder == null){
			listingSortColumnOrder = "EVENT_DATE_LOCAL ASC";
		}
		ListingSearchCriteria lsc = new ListingSearchCriteria();
		lsc.setPaginationInput(paginationInput);
		lsc.setSortingDirectives(new ArrayList<SortingDirective>());		
		String[] listingSortColumnOrders = listingSortColumnOrder.split(","); 
		for(String sortColumnOrder: listingSortColumnOrders){
			String[] sortColumnOrders = sortColumnOrder.trim().split(" ");
			ListingSortType sortType = ListingSortType.fromString(sortColumnOrders[0]);
			SortOrderType sortOrder = SortOrderType.fromString(sortColumnOrders[1]);			
			SortingDirective sd = new SortingDirective();
			sd.setSortDirection(1);
			if (sortOrder == SortOrderType.DESCENDING) {
				sd.setSortDirection(0);
			}			
			if (sortType == ListingSortType.EVENT) {
				sd.setSortColumnType(SortColumnType.EVENT_DESCRIPTION);
			} else if (sortType == ListingSortType.EVENTDATE) {
				sd.setSortColumnType(SortColumnType.EVENT_DATE_LOCAL);
			} else if (sortType == ListingSortType.PRICE) {
				sd.setSortColumnType(SortColumnType.TICKET_PRICE);
			} else if (sortType == ListingSortType.QUANTITY) {
				sd.setSortColumnType(SortColumnType.QUANTITY);
			} else if (sortType == ListingSortType.QUANTITY_REMAIN) {
				sd.setSortColumnType(SortColumnType.QUANTITY_REMAIN);
			} else if (sortType == ListingSortType.SECTION) {
				sd.setSortColumnType(SortColumnType.SECTION);
			} else if (sortType == ListingSortType.INHANDDATE) {
				sd.setSortColumnType(SortColumnType.EXPECTED_INHAND_DATE);
			} else if (sortType == ListingSortType.STATUS) {
				sd.setSortColumnType(SortColumnType.TICKET_SYSTEM_STATUS);
			} else if (sortType == ListingSortType.DELIVERYOPTION) {
				sd.setSortColumnType(SortColumnType.TICKET_MEDIUM);
			}else if (sortType == ListingSortType.SALEENDDATE) {
				sd.setSortColumnType(SortColumnType.SALE_END_DATE);
			}  else {
				sd.setSortColumnType(SortColumnType.EVENT_DATE_LOCAL);
			}	
			lsc.getSortingDirectives().add(sd);		
		}	
		if(filters != null && !filters.trim().isEmpty()){			
			String[] filterArr = filters.split(",");
			for(String filter : filterArr){
				String[] filterNameValue = filter.split(":");
				if (filter.contains(SalesFilterType.DATELASTMODIFIED.name())){
					filterNameValue = filter.trim().split(":",2);
				}
				if(filterNameValue.length == 2){
					ListingFilterType filterName = ListingFilterType.fromString(filterNameValue[0].trim());
					String filterValue = filterNameValue[1].trim();	
					if(!filterValue.trim().isEmpty()){				
						if(filterName == ListingFilterType.EVENT){
							filterValue = filterValue.replaceAll("[\\[\\]]", "").trim();	
							lsc.setEventId(filterValue);	
							continue;
						}
						if(filterName == ListingFilterType.STATUS){
							filterValue = filterValue.replaceAll("[\\[\\]]", "").trim();	
							lsc.setStatus(ListingStatus.fromValue(filterValue));
							continue;
						}
						if(filterName == ListingFilterType.Q){
							String unsafeTextRegex = getRegexFromResourceBundle(AccountConstants.VALIDATION_ACCOUNTSEARCH_UNSAFETEXTREGEX, SecurityUtil.UNSAFE_TEXT_REGEXP, serviceContext.getLocale(), messageSource);
							lsc.setQ(filterValue.replaceAll(unsafeTextRegex, "").trim());
							continue;
						}
						if(filterName == ListingFilterType.GEOGRAPYID){
							filterValue = filterValue.replaceAll("[\\(\\)]", "").trim();	
							lsc.setGeographyId(filterValue.split(";"));	
							continue;
						}
						if(filterName == ListingFilterType.DELIVERYOPTION){
							filterValue = filterValue.replaceAll("[\\[\\]]", "").trim();	
							lsc.setDeliveryOption(DeliveryOption.fromString(filterValue));
							continue;
						}
						if(filterName == ListingFilterType.TICKETID){
							filterValue = filterValue.replaceAll("[\\[\\]]", "").trim();	
							lsc.setListingId(new Long(filterValue));
							continue;
						}
						if(filterName == ListingFilterType.VENUEID){
							filterValue = filterValue.replaceAll("[\\[\\]]", "").trim();
							lsc.setVenueId(filterValue);
							continue;
						}
						if(filterName == ListingFilterType.GENREID){
							filterValue = filterValue.replaceAll("[\\[\\]]", "").trim();
							lsc.setGenreId(filterValue);
							continue;
						}
						if(filterName == ListingFilterType.EXTERNALLISTINGID){
							filterValue = filterValue.replaceAll("[\\[\\]]", "").trim();
							lsc.setExternalListingId(filterValue);
							continue;
						}
						if(filterName == ListingFilterType.EVENTDATE){
							int rangeIndex = filterValue.indexOf("TO");
							if(rangeIndex > 0){
								String from = filterValue.substring(0, rangeIndex);
								from = from.replaceAll("\\[", "").trim();
								String to = filterValue.substring(rangeIndex+2, filterValue.length());
								to = to.replaceAll("\\]", "").trim();
								GregorianCalendar xmlDate = convertToXMLCalendar(from);
								if (xmlDate != null) {
									lsc.setEventDateMin(xmlDate);
								}
								xmlDate = convertToXMLCalendar(to);
								if (xmlDate != null) {
									lsc.setEventDateMax(xmlDate);
								}					
							}else{
								filterValue = filterValue.replaceAll("[\\[\\]]", "").trim();	
								GregorianCalendar xmlDate = convertToXMLCalendar(filterValue);
								if (xmlDate != null) {
									Calendar eventDate = xmlDate;
									lsc.setEventDateMin(eventDate);
									lsc.setEventDateMax(eventDate);								
								}
							}
							continue;
						}
						if(filterName == ListingFilterType.SALEENDDATE){
							int rangeIndex = filterValue.indexOf("TO");
							if(rangeIndex > 0){
								String from = filterValue.substring(0, rangeIndex);
								from = from.replaceAll("\\[", "").trim();
								String to = filterValue.substring(rangeIndex+2, filterValue.length());
								to = to.replaceAll("\\]", "").trim();
								GregorianCalendar xmlDate = convertToXMLCalendar(from);
								if (xmlDate != null) {
									lsc.setSaleEndDateMin(xmlDate);
								}
								xmlDate = convertToXMLCalendar(to);
								if (xmlDate != null) {
									lsc.setSaleEndDateMax(xmlDate);
								}					
							}else{
								filterValue = filterValue.replaceAll("[\\[\\]]", "").trim();	
								GregorianCalendar xmlDate = convertToXMLCalendar(filterValue);
								if (xmlDate != null) {
									Calendar saleEndDate = xmlDate;
									lsc.setSaleEndDateMin(saleEndDate);
									lsc.setSaleEndDateMax(saleEndDate);								
								}
							}
							continue;
						}

						if(filterName == ListingFilterType.DATELASTMODIFIED){
							int rangeIndex = filterValue.indexOf("TO");
							if(rangeIndex > 0){
								String from = filterValue.substring(0, rangeIndex);
								from = from.replaceAll("\\[", "").trim();
								String to = filterValue.substring(rangeIndex+2, filterValue.length());
								to = to.replaceAll("\\]", "").trim();
								GregorianCalendar xmlDate = convertToXMLCalendarT(from);
								if (xmlDate != null) {
									lsc.setDateLastModifiedMin(xmlDate);
								}
								xmlDate = convertToXMLCalendarT(to);
								if (xmlDate != null) {
									lsc.setDateLastModifiedMax(xmlDate);
								}
							}else{
								filterValue = filterValue.replaceAll("[\\[\\]]", "").trim();
								GregorianCalendar xmlDate = convertToXMLCalendarT(filterValue);
								if (xmlDate != null) {
									Calendar saleEndDate = xmlDate;
									lsc.setDateLastModifiedMin(saleEndDate);
									lsc.setDateLastModifiedMax(saleEndDate);
								}
							}
							continue;
						}

						if(filterName == ListingFilterType.INHANDDATE){
							int rangeIndex = filterValue.indexOf("TO");
							if(rangeIndex > 0){
								String from = filterValue.substring(0, rangeIndex);
								from = from.replaceAll("\\[", "").trim();
								String to = filterValue.substring(rangeIndex+2, filterValue.length());
								to = to.replaceAll("\\]", "").trim();
								GregorianCalendar xmlDate = convertToXMLCalendar(from);
								if (xmlDate != null) {
									lsc.setInHandDateMin(xmlDate);
								}
								xmlDate = convertToXMLCalendar(to);
								if (xmlDate != null) {
									lsc.setInHandDateMax(xmlDate);
								}					
							}else{
								filterValue = filterValue.replaceAll("[\\[\\]]", "").trim();	
								GregorianCalendar xmlDate = convertToXMLCalendar(filterValue);
								if (xmlDate != null) {
									Calendar inhandDate = xmlDate;
									lsc.setInHandDateMin(inhandDate);
									lsc.setInHandDateMax(inhandDate);								
								}
							}
							continue;
						}
						if(filterName == ListingFilterType.PRICE){
							int rangeIndex = filterValue.indexOf("TO");
							if(rangeIndex > 0){
								String from = filterValue.substring(0, rangeIndex);
								from = from.replaceAll("\\[", "").trim();
								String to = filterValue.substring(rangeIndex+2, filterValue.length());
								to = to.replaceAll("\\]", "").trim();
								lsc.setPriceMin(Double.valueOf(from));
								lsc.setPriceMax(Double.valueOf(to));
							}else{
								filterValue = filterValue.replaceAll("[\\[\\]]", "").trim();	
								lsc.setPrice(Double.valueOf(filterValue));
							}
							continue;
						}	
					}else{
						log.error("get listings exception for sellerId=" +sellerId );
						com.stubhub.domain.account.common.Error listingError = new com.stubhub.domain.account.common.Error(ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT, "", filterNameValue[0].trim());
						throw new AccountException(listingError);	
					}
				}	else{
					log.error("get listings exception for sellerId=" +sellerId);
					com.stubhub.domain.account.common.Error listingError = new com.stubhub.domain.account.common.Error(ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT, "", "");
					throw new AccountException(listingError);			
				}
			}
		}
		if (summaryInput != null) {
			if (summaryInput.getIncludeVenueSummary() != null) {
				lsc.setIncludeVenueSummary(summaryInput.getIncludeVenueSummary());
			}
			if (summaryInput.getIncludeGenreSummary() != null) {
				lsc.setIncludeGenreSummary(summaryInput.getIncludeGenreSummary());
			}
			if (summaryInput.getIncludeEventSummary() != null) {
				lsc.setIncludeEventSummary(summaryInput.getIncludeEventSummary());
			}
		}

		if (listingInput != null) {
			if (listingInput.getIncludeDeleted() != null) {
				lsc.setIncludeDeleted(listingInput.getIncludeDeleted());
			}
			if (listingInput.getIncludeSold() != null) {
				lsc.setIncludeSold(listingInput.getIncludeSold());
			}
		}
		return lsc;
	}
	
	public static MyOrderSearchCriteria convertRequestToMyOrderSearchCriteria(
			String buyerId, PaginationInput paginationInput,
			String[] orderSortColumnOrder, String filters)
			throws ParseException {
		MyOrderSearchCriteria msc = new MyOrderSearchCriteria();
		msc.setBuyerId(buyerId);
		msc.setRow(paginationInput.getRows());
		msc.setStart(paginationInput.getStart());
		msc.setOrderBy(orderSortColumnOrder);
		
		String[] filterPairs = org.apache.commons.lang.StringUtils.split(filters, "AND");
		if (filterPairs != null && filterPairs.length > 0) {
			for (String pair : filterPairs) {
				String[] keyValues = org.apache.commons.lang.StringUtils.split(
						org.apache.commons.lang.StringUtils.trimToEmpty(pair),
						":",2);
				if(keyValues!=null && keyValues.length==2){
					if(keyValues[0].equalsIgnoreCase("orderDate")){
						msc.setOrderDate(keyValues[1]);
					}else if(keyValues[0].equalsIgnoreCase("status")){
						msc.setStatus(keyValues[1]);
					} else if(keyValues[0].equalsIgnoreCase("eventId")){
						msc.setEventId(keyValues[1]);
					}
					
				}
				
			}
		}

		return msc;
	}
	
	public static SalesSearchCriteria convertRequestToSalesSearchCriteria(String sellerId,
			PaginationInput paginationInput, String listingSortColumnOrder, String filters, String includePending, SummaryInput summaryInput, SHServiceContext serviceContext, DataSourceMessageSource messageSource) throws ParseException {
		if(paginationInput == null){
			paginationInput = new PaginationInput();
			paginationInput.setRows(PaginationInput.DEFAULT_ENTRIES_PERPAGE);
			paginationInput.setStart(0);
		}else{
			if (paginationInput.getRows() <= 0
					|| paginationInput.getRows() > PaginationInput.DEFAULT_ENTRIES_PERPAGE) {
				paginationInput.setRows(PaginationInput.DEFAULT_ENTRIES_PERPAGE);
			}
			if (paginationInput.getStart() <= 0) {
				paginationInput.setStart(0);
			}			
		}
		if(listingSortColumnOrder == null){
			listingSortColumnOrder = "EVENT_DATE_LOCAL ASC";
		}
		SalesSearchCriteria ssc = new SalesSearchCriteria();
		if(includePending != null && includePending.equalsIgnoreCase("true")){
			ssc.setIncludePending(true);
		}else{
			ssc.setIncludePending(false);
		}
		ssc.setPaginationInput(paginationInput);
		ssc.setSortingDirectives(new ArrayList<SortingDirective>());		
		String[] listingSortColumnOrders = listingSortColumnOrder.split(","); 
		for(String sortColumnOrder: listingSortColumnOrders){
			String[] sortColumnOrders = sortColumnOrder.trim().split(" ");
			SaleSortType sortType = SaleSortType.fromString(sortColumnOrders[0]);
			SortOrderType sortOrder = SortOrderType.fromString(sortColumnOrders[1]);			
			SortingDirective sd = new SortingDirective();
			sd.setSortDirection(1);
			if (sortOrder == SortOrderType.DESCENDING) {
				sd.setSortDirection(0);
			}			
			if (sortType == SaleSortType.EVENT) {
				sd.setSortColumnType(SortColumnType.EVENT_DESCRIPTION);
			} else if (sortType == SaleSortType.EVENTDATE) {
				sd.setSortColumnType(SortColumnType.EVENT_DATE_LOCAL);
			} else if (sortType == SaleSortType.PRICE) {
				sd.setSortColumnType(SortColumnType.PRICE_PER_TICKET);
			} else if (sortType == SaleSortType.STATUS) {
				sd.setSortColumnType(SortColumnType.ORDER_PROC_STATUS_ID);
			} else if (sortType == SaleSortType.DELIVERYOPTION) {
				sd.setSortColumnType(SortColumnType.TICKET_MEDIUM_ID);
			} else if (sortType == SaleSortType.QUANTITY) {
				sd.setSortColumnType(SortColumnType.SOLD_QUANTITY);
			} else if (sortType == SaleSortType.SALECATEGORY) {
				sd.setSortColumnType(SortColumnType.SALE_CATEGORY);
			} else if (sortType == SaleSortType.PAYOUT) {
				sd.setSortColumnType(SortColumnType.SELLER_PAYOUT_AMOUNT);
			} else if (sortType == SaleSortType.INHANDDATE) {
				sd.setSortColumnType(SortColumnType.INHAND_DATE);
            } else if (sortType == SaleSortType.SECTION) {
                sd.setSortColumnType(SortColumnType.SECTION);
            } else if (sortType == SaleSortType.ROW) {
                sd.setSortColumnType(SortColumnType.ROW_DESC);
            } else {
                sd.setSortColumnType(SortColumnType.SOLD_DATE);
            }
			ssc.getSortingDirectives().add(sd);		
		}	
		if(filters != null && !filters.trim().isEmpty()){
			String[] filterArr = filters.split(" AND ");
			for(String filter : filterArr){
				String[] filterNameValue = filter.trim().split(":");
				if (filter.contains(SalesFilterType.DATELASTMODIFIED.name())){
					filterNameValue = filter.trim().split(":",2);
				}
				if(filterNameValue.length == 2){
					SalesFilterType filterName = SalesFilterType.fromString(filterNameValue[0].trim());
					String filterValue = filterNameValue[1].trim();	
					if(!filterValue.trim().isEmpty()){				
						if(filterName == SalesFilterType.EVENT){
							filterValue = filterValue.replaceAll("[\\[\\]]", "").trim();
							try{
								ssc.setEventId(new Long(filterValue));
							}catch(NumberFormatException e){
								log.error("EXCEPTION - getSales for sellerId="+sellerId );
								com.stubhub.domain.account.common.Error listingError = new com.stubhub.domain.account.common.Error(ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT, "Invalid Event", "EVENT");
								throw new AccountException(listingError);		
							}
							continue;
						}						
						if(filterName == SalesFilterType.STATUS){
							filterValue = filterValue.replaceAll("[\\[\\]]", "").trim();	
							ssc.setStatus(filterValue);
							continue;
						}
						if(filterName == SalesFilterType.Q){
							String unsafeTextRegex = getRegexFromResourceBundle(AccountConstants.VALIDATION_ACCOUNTSEARCH_UNSAFETEXTREGEX, SecurityUtil.UNSAFE_TEXT_REGEXP, serviceContext.getLocale(), messageSource);
							ssc.setQ(filterValue.replaceAll(unsafeTextRegex, "").trim());
							continue;
						}
						if(filterName == SalesFilterType.DELIVERYOPTION){
							filterValue = filterValue.replaceAll("[\\[\\]]", "").trim();	
							ssc.setDeliveryOption(filterValue);
							continue;
						}
						if(filterName == SalesFilterType.LISTINGIDS){
							filterValue = filterValue.replaceAll("[\\[\\]]", "").trim();	
							ssc.setListingIds(filterValue);
							continue;
						}
						if(filterName == SalesFilterType.EXTERNALLISTINGIDS){
							filterValue = filterValue.replaceAll("[\\[\\]]", "").trim();	
							ssc.setExternalListingIds(filterValue);
							continue;
						}
						if(filterName == SalesFilterType.VENUEID){
							filterValue = filterValue.replaceAll("[\\[\\]]", "").trim();
							ssc.setVenueId(filterValue);
							continue;
						}
						if(filterName == SalesFilterType.GENREID){
							filterValue = filterValue.replaceAll("[\\[\\]]", "").trim();
							ssc.setGenreId(filterValue);
							continue;
						}
						if(filterName == SalesFilterType.CATEGORY){
							filterValue = filterValue.replaceAll("[\\[\\]]", "").trim();
							ssc.setCategory(SalesSearchCriteria.Category.fromString(filterValue));
							continue;
						}
						if(filterName == SalesFilterType.ACTION){
							filterValue = filterValue.replaceAll("[\\[\\]]", "").trim();
							ssc.setAction(SalesSearchCriteria.Action.fromString(filterValue));
							continue;
						}
						if(filterName == SalesFilterType.EVENTDATE){
							try{
								int rangeIndex = filterValue.indexOf("TO");
								if(rangeIndex > 0){
									String from = filterValue.substring(0, rangeIndex);
									from = from.replaceAll("\\[", "").trim();
									String to = filterValue.substring(rangeIndex+2, filterValue.length());
									to = to.replaceAll("\\]", "").trim();
									GregorianCalendar xmlDate = convertToXMLCalendar(from);
									if (xmlDate != null) {
										ssc.setEventDateMin(xmlDate);
									}
									xmlDate = convertToXMLCalendar(to);								
									if (xmlDate != null) {
										ssc.setEventDateMax(xmlDate);
									}					
								}else{
									filterValue = filterValue.replaceAll("[\\[\\]]", "").trim();	
									GregorianCalendar xmlDate = convertToXMLCalendar(filterValue);
									if (xmlDate != null) {
										Calendar eventDate = xmlDate;
										ssc.setEventDateMin(eventDate);
										ssc.setEventDateMax(eventDate);								
									}
								}
							}catch(ParseException e){
								log.error("EXCEPTION - EVENTDATE - ParseException - getSales for sellerId="+sellerId );
								com.stubhub.domain.account.common.Error listingError = new com.stubhub.domain.account.common.Error(ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT, "Invalid EventDate", "EVENTDATE");
								throw new AccountException(listingError);		
							}
							continue;
						}
						if(filterName == SalesFilterType.SALEDATE){
							try{
								int rangeIndex = filterValue.indexOf("TO");
								if(rangeIndex > 0){
									String from = filterValue.substring(0, rangeIndex);
									from = from.replaceAll("\\[", "").trim();
									String to = filterValue.substring(rangeIndex+2, filterValue.length());
									to = to.replaceAll("\\]", "").trim();
									GregorianCalendar xmlDate = convertToXMLCalendar(from);
									if (xmlDate != null) {
										ssc.setSaleDateMin(xmlDate);
									}
									xmlDate = convertToXMLCalendar(to);
									if (xmlDate != null) {
										ssc.setSaleDateMax(xmlDate);
									}					
								}else{
									filterValue = filterValue.replaceAll("[\\[\\]]", "").trim();								
									GregorianCalendar xmlDate = convertToXMLCalendar(filterValue);
									if (xmlDate != null) {
										Calendar saleEndDate = xmlDate;
										ssc.setSaleDateMin(saleEndDate);
										ssc.setSaleDateMax(saleEndDate);								
									}
								}
							}catch(ParseException e){
								log.error("EXCEPTION - SALEDATE - ParseException - getSales for sellerId="+sellerId );
								com.stubhub.domain.account.common.Error listingError = new com.stubhub.domain.account.common.Error(ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT, "Invalid SALEDATE", "SALEDATE");
								throw new AccountException(listingError);		
							}
							continue;
						}
						if(filterName == SalesFilterType.DATELASTMODIFIED){
							try{
								int rangeIndex = filterValue.indexOf("TO");
								if(rangeIndex > 0){
									String from = filterValue.substring(0, rangeIndex);
									from = from.replaceAll("\\[", "").trim();
									String to = filterValue.substring(rangeIndex+2, filterValue.length());
									to = to.replaceAll("\\]", "").trim();
									GregorianCalendar xmlDate = convertToXMLCalendarT(from);
									if (xmlDate != null) {
										ssc.setDateLastModifiedMin(xmlDate);
									}
									xmlDate = convertToXMLCalendarT(to);
									if (xmlDate != null) {
										ssc.setDateLastModifiedMax(xmlDate);
									}
								}else{
									filterValue = filterValue.replaceAll("[\\[\\]]", "").trim();
									GregorianCalendar xmlDate = convertToXMLCalendarT(filterValue);
									if (xmlDate != null) {
										Calendar saleEndDate = xmlDate;
										ssc.setDateLastModifiedMin(saleEndDate);
										ssc.setDateLastModifiedMax(saleEndDate);
									}
								}
							}catch(ParseException e){
								log.error("EXCEPTION - DATELASTMODIFIED - ParseException - getSales for sellerId="+sellerId );
								com.stubhub.domain.account.common.Error listingError = new com.stubhub.domain.account.common.Error(ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT, "Invalid DATELASTMODIFIED", "DATELASTMODIFIED");
								throw new AccountException(listingError);
							}
							continue;
						}
						if (filterName == SalesFilterType.INHANDDATE) {
							try {
								int rangeIndex = filterValue.indexOf("TO");
								if (rangeIndex > 0) {
									String from = filterValue.substring(0, rangeIndex);
									from = from.replaceAll("\\[", "").trim();
									String to = filterValue.substring(rangeIndex + 2, filterValue.length());
									to = to.replaceAll("\\]", "").trim();
									GregorianCalendar xmlDate = convertToXMLCalendar(from);
									if (xmlDate != null) {
										ssc.setInHandDateMin(xmlDate);
									}
									xmlDate = convertToXMLCalendar(to);
									if (xmlDate != null) {
										ssc.setInHandDateMax(xmlDate);
									}
								} else {
									filterValue = filterValue.replaceAll("[\\[\\]]", "").trim();
									GregorianCalendar xmlDate = convertToXMLCalendar(filterValue);
									if (xmlDate != null) {
										Calendar saleEndDate = xmlDate;
										ssc.setInHandDateMin(saleEndDate);
										ssc.setInHandDateMax(saleEndDate);
									}
								}
							} catch (ParseException e) {
								log.error("EXCEPTION - INHANDDATE - ParseException - getSales for sellerId=" + sellerId );
								com.stubhub.domain.account.common.Error listingError = new com.stubhub.domain.account.common.Error(ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT, "Invalid INHANDDATE", "INHANDDATE");
								throw new AccountException(listingError);
							}
							continue;
						}
						if (filterName == SalesFilterType.EVENTDATEUTC) {
							try {
								int rangeIndex = filterValue.indexOf("TO");
								if (rangeIndex > 0) {
									String from = filterValue.substring(0, rangeIndex);
									from = from.replaceAll("\\[", "").trim();
									String to = filterValue.substring(rangeIndex + 2, filterValue.length());
									to = to.replaceAll("\\]", "").trim();
									GregorianCalendar xmlDate = convertToXMLCalendar(from);
									if (xmlDate != null) {
										ssc.setEventDateUTCMin(xmlDate);
									}
									xmlDate = convertToXMLCalendar(to);
									if (xmlDate != null) {
										ssc.setEventDateUTCMax(xmlDate);
									}
								} else {
									filterValue = filterValue.replaceAll("[\\[\\]]", "").trim();
									GregorianCalendar xmlDate = convertToXMLCalendar(filterValue);
									if (xmlDate != null) {
										Calendar eventDateUTC = xmlDate;
										ssc.setEventDateUTCMin(eventDateUTC);
										ssc.setEventDateUTCMax(eventDateUTC);
									}
								}
							} catch (ParseException e) {
								log.error("EXCEPTION - EVENTDATEUTC - ParseException - getSales for sellerId=" + sellerId );
								com.stubhub.domain.account.common.Error listingError = new com.stubhub.domain.account.common.Error(ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT, "Invalid EVENTDATEUTC", "EVENTDATEUTC");
								throw new AccountException(listingError);
							}
							continue;
						}
						if(filterName == SalesFilterType.PRICE){
							try{
								int rangeIndex = filterValue.indexOf("TO");
								if(rangeIndex > 0){
									String from = filterValue.substring(0, rangeIndex);
									from = from.replaceAll("\\[", "").trim();
									String to = filterValue.substring(rangeIndex+2, filterValue.length());
									to = to.replaceAll("\\]", "").trim();
									ssc.setPriceMin(Double.valueOf(from));
									ssc.setPriceMax(Double.valueOf(to));
								}else{
									filterValue = filterValue.replaceAll("[\\[\\]]", "").trim();	
									ssc.setPrice(Double.valueOf(filterValue));
								}
							}catch(NumberFormatException e){
								log.error("EXCEPTION - NumberFormatException getSales for sellerId="+sellerId );
								com.stubhub.domain.account.common.Error listingError = new com.stubhub.domain.account.common.Error(ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT, "Invalid PRICE", "PRICE");
								throw new AccountException(listingError);		
							}
							continue;
						}						
						if(filterName == SalesFilterType.SALEID){
							try{
								int rangeIndex = filterValue.indexOf("TO");
								if(rangeIndex > 0){
									String from = filterValue.substring(0, rangeIndex);
									from = from.replaceAll("\\[", "").trim();
									String to = filterValue.substring(rangeIndex+2, filterValue.length());
									to = to.replaceAll("\\]", "").trim();
									ssc.setFromSaleId(Long.valueOf(from));
									ssc.setToSaleId(Long.valueOf(to));
								}else{
									filterValue = filterValue.replaceAll("[\\[\\]]", "").trim();	
									ssc.setSaleId(Long.valueOf(filterValue));
								}
							}catch(NumberFormatException e){
								log.error("EXCEPTION - NumberFormatException getSales for sellerId="+sellerId );
								com.stubhub.domain.account.common.Error listingError = new com.stubhub.domain.account.common.Error(ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT, "Invalid SALEID", "SALEID");
								throw new AccountException(listingError);		
							}
							continue;
						}						
						if(filterName == SalesFilterType.QUANTITY){
							try{						
								int rangeIndex = filterValue.indexOf("TO");
								if(rangeIndex > 0){
									String from = filterValue.substring(0, rangeIndex);
									from = from.replaceAll("\\[", "").trim();
									String to = filterValue.substring(rangeIndex+2, filterValue.length());
									to = to.replaceAll("\\]", "").trim();
									ssc.setMinSoldQuantity(Integer.valueOf(from));
									ssc.setMaxSoldQuantity(Integer.valueOf(to));
								}else{
									filterValue = filterValue.replaceAll("[\\[\\]]", "").trim();	
									ssc.setSoldQuantity(Integer.valueOf(filterValue));
								}
							}catch(NumberFormatException e){
								log.error("EXCEPTION - NumberFormatException getSales for sellerId="+sellerId );
								com.stubhub.domain.account.common.Error listingError = new com.stubhub.domain.account.common.Error(ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT, "Invalid QUANTITY", "QUANTITY");
								throw new AccountException(listingError);		
							}
							continue;
						}						
					}else{
						log.error("EXCEPTION - getSales for sellerId="+sellerId );
						com.stubhub.domain.account.common.Error error = new com.stubhub.domain.account.common.Error(ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT, "Incorrect Filter Value", filterNameValue[0].trim());
						throw new AccountException(error);	
					}
				}else{
					log.error("EXCEPTION - getSales for sellerId="+sellerId  );
					com.stubhub.domain.account.common.Error error = new com.stubhub.domain.account.common.Error(ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT, "Missing name:value pair for filters", filter);
					throw new AccountException(error);			
				}
			}
		}
		if (summaryInput != null) {
			if (summaryInput.getIncludeVenueSummary() != null) {
				ssc.setIncludeVenueSummary(summaryInput.getIncludeVenueSummary());
			}
			if (summaryInput.getIncludeGenreSummary() != null) {
				ssc.setIncludeGenreSummary(summaryInput.getIncludeGenreSummary());
			}
			if (summaryInput.getIncludeEventSummary() != null) {
				ssc.setIncludeEventSummary(summaryInput.getIncludeEventSummary());
			}
		}

		return ssc;
	}	
	
	public static SalesSearchCriteria convertEventSalesRequestToSalesSearchCriteria(String sellerId,
 PaginationInput paginationInput,
			String listingSortColumnOrder, String filters, int totalNumberOfRows, String priceType)
			throws ParseException {
		log.info("_message=\"Get Sales by Event for SellerId={} methodName={}, filters={}", sellerId,"convertEventSalesRequestToSalesSearchCriteria",filters);
		if (paginationInput == null) {
			paginationInput = new PaginationInput();
			paginationInput.setRows(totalNumberOfRows);
			paginationInput.setStart(0);
		} else {
			if (paginationInput.getRows() <= 0
					|| paginationInput.getRows() > totalNumberOfRows) {
				paginationInput
						.setRows(totalNumberOfRows);
			}
			if (paginationInput.getStart() <= 0) {
				paginationInput.setStart(0);
			}
		}
		if (listingSortColumnOrder == null) {
			listingSortColumnOrder = "SOLD_DATE DESC";
		}
		SalesSearchCriteria ssc = new SalesSearchCriteria();

		ssc.setPaginationInput(paginationInput);
		ssc.setSortingDirectives(new ArrayList<SortingDirective>());
		String[] listingSortColumnOrders = listingSortColumnOrder.split(",");
		for (String sortColumnOrder : listingSortColumnOrders) {
			String[] sortColumnOrders = sortColumnOrder.trim().split(" |%20");

			SaleSortType sortType = SaleSortType
					.fromString(sortColumnOrders[0]);
			SortOrderType sortOrder = SortOrderType
					.fromString(sortColumnOrders[1]);
			SortingDirective sd = new SortingDirective();
			sd.setSortDirection(1);
			if (sortOrder == 
					SortOrderType.DESCENDING) {
				sd.setSortDirection(0);
			}
			if (sortType == SaleSortType.SALEDATE) {
				sd.setSortColumnType(SortColumnType.SOLD_DATE);
			} else if (sortType == 
					SaleSortType.PRICE) {
				sd.setSortColumnType(getPriceSortColumnByPriceType(priceType));
			} else if (sortType == 
					SaleSortType.DELIVERYOPTION) {
				sd.setSortColumnType(SortColumnType.TICKET_MEDIUM_ID);
			} else if (sortType == SaleSortType.QUANTITY) {
				sd.setSortColumnType(SortColumnType.SOLD_QUANTITY);
			} else if (sortType == SaleSortType.SECTION) {
				sd.setSortColumnType(SortColumnType.SECTION);
			} else if (sortType == SaleSortType.ROW) {
				sd.setSortColumnType(SortColumnType.ROW_DESC);
			} else {
				sd.setSortColumnType(SortColumnType.SOLD_DATE);
			}
			ssc.getSortingDirectives().add(sd);
		}		
		if (filters != null && !filters.trim().isEmpty()) {
			String[] filterArr = filters.split(" AND ");
			for (String filter : filterArr) {
				String[] filterNameValue = filter.trim().split(":");
				if (filter.contains(SalesFilterType.DATELASTMODIFIED.name())) {
					filterNameValue = filter.trim().split(":", 2);
				}
				if (filterNameValue.length == 2) {
					SalesFilterType filterName = SalesFilterType.fromString(filterNameValue[0].trim());
					String filterValue = filterNameValue[1].trim();
					if (!filterValue.trim().isEmpty()) {
						if (filterName == SalesFilterType.SALEDATE) {
							try {
								int rangeIndex = filterValue.indexOf("TO");
								if (rangeIndex > 0) {
									String from = filterValue.substring(0, rangeIndex);
									from = from.replaceAll("\\[", "").trim();
									String to = filterValue.substring(rangeIndex + 2, filterValue.length());
									to = to.replaceAll("\\]", "").trim();
									GregorianCalendar xmlDate = convertToXMLCalendar(from);
									if (xmlDate != null) {
										ssc.setSaleDateMin(xmlDate);
									}
									xmlDate = convertToXMLCalendar(to);
									if (xmlDate != null) {
										ssc.setSaleDateMax(xmlDate);
									}
								} else {
									filterValue = filterValue.replaceAll("[\\[\\]]", "").trim();
									GregorianCalendar xmlDate = convertToXMLCalendar(filterValue);
									if (xmlDate != null) {
										Calendar saleEndDate = xmlDate;
										ssc.setSaleDateMin(saleEndDate);
										ssc.setSaleDateMax(saleEndDate);
									}
								}
							} catch (ParseException e) {
								log.error("EXCEPTION - SALEDATE - ParseException - getSales for sellerId=" + sellerId);
								com.stubhub.domain.account.common.Error listingError = new com.stubhub.domain.account.common.Error(
										ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT, "Invalid SALEDATE", "SALEDATE");
								throw new AccountException(listingError);
							}
							continue;
						}
					}
				}
			}
		}
		return ssc;
	}
	private static SortColumnType getPriceSortColumnByPriceType(String priceType) {
		if(PRICE_TYPE_LISTPRICE.equalsIgnoreCase(priceType))
			return SortColumnType.PRICE_PER_TICKET;
		else
			return SortColumnType.COST_PER_TICKET;
	}

	private static GregorianCalendar convertToXMLCalendar(String dateStr) throws ParseException {
		GregorianCalendar cal = new GregorianCalendar();		
		if(dateStr.trim().equalsIgnoreCase("NOW")){
			Calendar utcNowCal = DateUtil.getNowCalUTC();			
			cal.setTimeInMillis(utcNowCal.getTimeInMillis());	
		}else if (dateStr.trim().equalsIgnoreCase("NOW-1")){
            Calendar utcNowCal = DateUtil.getNowCalUTC();
            utcNowCal.add(Calendar.HOUR_OF_DAY, -24);          
            cal.setTimeInMillis(utcNowCal.getTimeInMillis());
        } else if(dateStr.trim().equalsIgnoreCase("TODAY")){
			Calendar currentDate = Calendar.getInstance();;
			currentDate.set(Calendar.HOUR_OF_DAY, 0); //anything 0 - 23
			currentDate.set(Calendar.MINUTE, 0);
			currentDate.set(Calendar.SECOND, 0);
		    cal.setTimeInMillis(currentDate.getTimeInMillis());
		} else{		
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date date = format.parse(dateStr);
			cal.setTime(date);
		}			
		return cal;
	}

    private static GregorianCalendar convertToXMLCalendarT(String dateStr) throws ParseException {
        GregorianCalendar cal = new GregorianCalendar();
        if(dateStr.trim().equalsIgnoreCase("NOW")){
            Calendar utcNowCal = DateUtil.getNowCalUTC();
            cal.setTimeInMillis(utcNowCal.getTimeInMillis());
        }else if(dateStr.trim().equalsIgnoreCase("TODAY")){
            Calendar currentDate = Calendar.getInstance();;
            currentDate.set(Calendar.HOUR_OF_DAY, 0); //anything 0 - 23
            currentDate.set(Calendar.MINUTE, 0);
            currentDate.set(Calendar.SECOND, 0);
            cal.setTimeInMillis(currentDate.getTimeInMillis());
        } else{
            String dateFormat = "yyyy-MM-dd";
            String dateFormatT = "yyyy-MM-dd'T'HH:mm:ss";
            DateFormat format = null;
            if (dateStr.trim().length() > dateFormat.length()){
                format = new SimpleDateFormat(dateFormatT);
            }else{
                format = new SimpleDateFormat(dateFormat);
            }
            Date date = format.parse(dateStr);
            cal.setTime(date);
        }
        return cal;
    }

	public static SalesSearchCriteria convertRequestToSaleSearchCriteria(SalesHistoryRequest request) throws ParseException, NumberFormatException {
		SalesSearchCriteria ssc = new SalesSearchCriteria();
		ssc.setStatus(null);
		ssc.setCategory(null);
		ssc.setIncludePending(false);
		ssc.setExcludeCancel(true);
		ssc.setExcludeZeroCost(true);
		PaginationInput pagination = new PaginationInput();
		pagination.setStart(0);
		pagination.setRows(1);
		ssc.setPaginationInput(pagination);
		if("listprice".equalsIgnoreCase(request.getPriceType())){
			ssc.setIncludePricePerTicketSummary(true);
		} else {
			ssc.setIncludeCostPerTicketSummary(true);
		}
		if(!StringUtils.isNullorEmpty(request.getEventId())){
			if(ValidationUtil.isValidLong(request.getEventId())) {
				ssc.setEventId(Long.valueOf(request.getEventId()));
			} else {
				log.error("message=\"Invalid event ID in the input\"" + " eventId=" + request.getEventId());
				Error error = new Error(ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT, "Invalid input", "eventId");
				throw new AccountException(error);
			}
		}else{
			com.stubhub.domain.account.common.Error listingError = new com.stubhub.domain.account.common.Error(ErrorType.INPUTERROR, ErrorCode.MISSING_EVENT_ID, "EventId is required", "eventId");
			throw new AccountException(listingError);
		}
		
		if(!StringUtils.isNullorEmpty(request.getSectionIds())){				
			String sectionIdInput = request.getSectionIds();
			ssc.setSectionIds(sectionIdInput.split(","));
		}
		
		if(!StringUtils.isNullorEmpty(request.getZoneIds())){			
			ssc.setZoneIds(request.getZoneIds().split(","));
		}
		
		if(!StringUtils.isNullorEmpty(request.getRows())){			
			ssc.setRowDescs(request.getRows().split(","));
		}
		
		if(!StringUtils.isNullorEmpty(request.getDeliveryOptions())){
			ssc.setDeliveryOption(request.getDeliveryOptions());
		}
		
		if(!StringUtils.isNullorEmpty(request.getQuantity())){
			if(ValidationUtil.isValidLong(request.getQuantity())) {
				ssc.setSoldQuantity(Integer.valueOf(request.getQuantity()));
			} else {
				log.error("message=\"Invalid quantity in the input\"" + " quantity=" + request.getQuantity() + " eventId=" + request.getEventId());
				Error error = new Error(ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT, "Invalid input", "quantity");
				throw new AccountException(error);
			}
			
		}
		
		try {
			if(!StringUtils.isNullorEmpty(request.getFromDate())){
				ssc.setSaleDateMin(convertToXMLCalendar(request.getFromDate()));
			}
			if(!StringUtils.isNullorEmpty(request.getToDate())){
				ssc.setSaleDateMax(convertToXMLCalendar(request.getToDate()));
			}
		} catch(ParseException e) {					
			log.error("message=\"ParseException occured while parsing the input dates\"" + " eventId=" + request.getEventId(), e);
			Error error = new Error(ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT, "Invalid from/to date", "soldDate");
			throw new AccountException(error);
		}
		
		return ssc;
	}
	
	public static SalesSearchCriteria convertCSOrderDetailsRequestToSalesSearchCriteria(CSOrderDetailsRequest csOrderDetailsRequest) throws ParseException {
		String orderId = csOrderDetailsRequest.getOrderId();
		String eventStartDate = csOrderDetailsRequest.getEventStartDate();
		String eventEndDate = csOrderDetailsRequest.getEventEndDate();
		String start = csOrderDetailsRequest.getStart();
		String row = csOrderDetailsRequest.getRow();		
		SalesSearchCriteria ssc = new SalesSearchCriteria();

		if (orderId != null){
			ssc.setSaleId(new Long(orderId));
			return ssc;
		} else {
			ssc.setSortingDirectives(new ArrayList<SortingDirective>());		
			SortingDirective sd = new SortingDirective();
			sd.setSortDirection(1); // 1-ASCENDING, 0-DESCENDING
			sd.setSortColumnType(SortColumnType.EVENT_DATE_LOCAL);
			ssc.getSortingDirectives().add(sd);
			if (start != null && row != null){				
				PaginationInput paginationInput = new PaginationInput();
				paginationInput.setRows(Integer.parseInt(row));
				paginationInput.setStart(Integer.parseInt(start));
				ssc.setPaginationInput(paginationInput);
			}			
			if (eventStartDate != null){
				GregorianCalendar xmlDate = convertToXMLCalendar(eventStartDate);
				if (xmlDate != null) ssc.setEventDateMin(xmlDate);
			}			
			if (eventEndDate != null){
				GregorianCalendar xmlDate = convertToXMLCalendar(eventEndDate);
				if (xmlDate != null) ssc.setEventDateMax(xmlDate);
			}
			if (eventStartDate == null && eventEndDate == null){
				GregorianCalendar xmlDate = convertToXMLCalendar("TODAY");
				if (xmlDate != null) ssc.setEventDateMin(xmlDate);
			}			
			return ssc;
		}
	}
	
	public static List<String> getOrderIds(OrdersResponse ordersResponse){
		List<CSOrderDetailsResponse> list = ordersResponse.getOrder();
		List<String> orderList = new ArrayList<String>();	
		for (int i=0; i<list.size(); i++){
			if (list.get(i) != null && list.get(i).getTransaction().getOrderId() != null){				
				orderList.add(list.get(i).getTransaction().getOrderId());
			}
		}
		return orderList;
	}
	
	public static List<String> getSaleIds(CSSalesResponse salesResponse){
		List<CSSaleDetailsResponse> list = salesResponse.getSale();
		List<String> saleList = new ArrayList<String>();
		for (int i=0; i<list.size(); i++){
			if (list.get(i) != null && list.get(i).getTransaction().getSaleId() != null){				
				saleList.add(list.get(i).getTransaction().getSaleId());
			}
		}
		return saleList;
	}
	
	public static String concatenateOrderIds(OrdersResponse ordersResponse){
		List<CSOrderDetailsResponse> list = ordersResponse.getOrder();
		StringBuilder sb = new StringBuilder();	
		sb.append("( ");
		for (int i=0; i<list.size(); i++){
			if (list.get(i) != null && list.get(i).getTransaction().getOrderId() != null){				
				sb.append(list.get(i).getTransaction().getOrderId());
				if(i < list.size()-1) sb.append(" OR ");
			}
		}
		sb.append(" ) ");
		return sb.toString();
	}
	
	 private static String getRegexFromResourceBundle(String key, String defaultValue, Locale locale, DataSourceMessageSource messageSource) {
	    	String regex = null;
	    	try{
	    		regex = messageSource.getMessage(key, null, defaultValue, locale);

	    		log.debug("_message=key=" + key + " value=\"" + regex + "\"");

				if (regex == null) {
					return defaultValue;
				}

	    	} catch (IllegalArgumentException ex){
	    		log.error("api_domain=account api_resource=account api_method=getRegexFromResourceBundle" +
	            		" status=error error_message=\"Exception occured while getting regex from resource bundle utility for locale=" + locale + "\""
	                      + " key=" + key);
	    	}
	    	return regex;
	 }
}
