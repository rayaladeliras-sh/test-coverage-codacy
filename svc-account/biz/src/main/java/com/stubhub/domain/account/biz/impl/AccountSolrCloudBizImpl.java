/**
 * Copyright 2016 StubHub, Inc.  All rights reserved.
 */
package com.stubhub.domain.account.biz.impl;

import static com.stubhub.domain.account.biz.impl.MCIRequestUtil.AGGREGATIONS;
import static com.stubhub.domain.account.biz.impl.MCIRequestUtil.BLANK;
import static com.stubhub.domain.account.biz.impl.MCIRequestUtil.COMMA;
import static com.stubhub.domain.account.biz.impl.MCIRequestUtil.DEFAULT_SEARCH_MCI_API_TIME_OUT;
import static com.stubhub.domain.account.biz.impl.MCIRequestUtil.DEFAULT_SEARCH_MCI_API_TIME_OUT_INT;
import static com.stubhub.domain.account.biz.impl.MCIRequestUtil.DEFAULT_SEARCH_MCI_API_V1;
import static com.stubhub.domain.account.biz.impl.MCIRequestUtil.FILTER;
import static com.stubhub.domain.account.biz.impl.MCIRequestUtil.LEFT_BRACKET;
import static com.stubhub.domain.account.biz.impl.MCIRequestUtil.MUST;
import static com.stubhub.domain.account.biz.impl.MCIRequestUtil.NON_ALPHABET_RGEX;
import static com.stubhub.domain.account.biz.impl.MCIRequestUtil.RIGHT_BRACKET;
import static com.stubhub.domain.account.biz.impl.MCIRequestUtil.SEARCH_MCI_API_TIME_OUT_PROP_NAME;
import static com.stubhub.domain.account.biz.impl.MCIRequestUtil.SEARCH_MCI_API_URL_PROP_NAME;
import static com.stubhub.domain.account.biz.impl.MCIRequestUtil.SHOULD;
import static com.stubhub.domain.account.biz.impl.MCIRequestUtil.SORT;
import static com.stubhub.domain.account.biz.impl.MCIRequestUtil.createNode;
import static com.stubhub.domain.account.biz.impl.MCIRequestUtil.facet;
import static com.stubhub.domain.account.biz.impl.MCIRequestUtil.in;
import static com.stubhub.domain.account.biz.impl.MCIRequestUtil.match;
import static com.stubhub.domain.account.biz.impl.MCIRequestUtil.not;
import static com.stubhub.domain.account.biz.impl.MCIRequestUtil.pagination;
import static com.stubhub.domain.account.biz.impl.MCIRequestUtil.range;
import static com.stubhub.domain.account.biz.impl.MCIRequestUtil.sort;
import static com.stubhub.domain.account.biz.impl.MCIRequestUtil.stats;
import static com.stubhub.domain.account.biz.impl.MCIRequestUtil.termQuery;
import static com.stubhub.domain.account.biz.impl.MCIRequestUtil.toCamelCase;
import static com.stubhub.domain.account.datamodel.entity.ListingStatus.*;
import static java.lang.Long.parseLong;
import static org.apache.commons.lang3.StringUtils.isNumeric;

import com.stubhub.domain.account.biz.intf.AccountSolrCloudBiz;
import com.stubhub.domain.account.common.ListingSearchCriteria;
import com.stubhub.domain.account.common.MyOrderSearchCriteria;
import com.stubhub.domain.account.common.PaginationInput;
import com.stubhub.domain.account.common.SalesSearchCriteria;
import com.stubhub.domain.account.common.SortingDirective;
import com.stubhub.domain.account.common.enums.DeliveryOption;
import com.stubhub.domain.account.common.enums.ErrorCode;
import com.stubhub.domain.account.common.enums.ErrorType;
import com.stubhub.domain.account.common.enums.LMSOption;
import com.stubhub.domain.account.common.enums.ListingStatus;
import com.stubhub.domain.account.common.enums.SaleStatus;
import com.stubhub.domain.account.common.enums.SortColumnType;
import com.stubhub.domain.account.common.enums.TicketMedium;
import com.stubhub.domain.account.common.exception.AccountException;
import com.stubhub.domain.account.context.MCIHeaderThreadLocal;
import com.stubhub.domain.account.context.MCIQueryParamThreadLocal;
import com.stubhub.domain.account.datamodel.dao.VenueConfigZoneSectionDAO;
import com.stubhub.domain.account.datamodel.entity.VenueConfigZoneSection;
import com.stubhub.domain.catalog.common.util.DateUtil;
import com.stubhub.domain.catalog.read.v3.intf.events.dto.response.Event;
import com.stubhub.newplatform.http.util.HttpClient4Util;
import com.stubhub.newplatform.http.util.HttpClient4Util.SimpleHttpResponse;
import com.stubhub.newplatform.http.util.HttpClient4UtilHelper;
import com.stubhub.newplatform.property.MasterStubhubPropertiesWrapper;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.ws.rs.core.MediaType;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.EntityEnclosingRequestWrapper;
import org.apache.http.protocol.HttpContext;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 * AccountSolrCloudBizImpl
 * 
 * @author runiu
 *
 */
@Component("accountSolrCloudBiz")
public class AccountSolrCloudBizImpl implements AccountSolrCloudBiz, InitializingBean {

	/* solr field */
	private static final String EVENT_ID_NAME_FACET = "eventIdNameFacet";
	private static final String PERFORMER_ID_NAME_FACET = "performerIdNameFacet";
	private static final String VENUE_ID_NAME_FACET = "venueIdNameFacet";
	private static final String EVENT_NAME_SORT = "eventNameSort";
	private static final String SALE_CATEGORY = "saleCategory";
	private static final String TICKET_MEDIUM_ID = "ticketMediumId";
	private static final String TRANSACTION_DATE = "transactionDate";
	private static final String DATE_LAST_MODIFIED = "dateLastModified";
	private static final String INHAND_DATE = "inhandDate";
	private static final String EVENT_DATE_LOCAL = "eventDateLocal";
	private static final String FULFILLMENT_METHOD_ID = "fulfillmentMethodId";
	private static final String EXTERNAL_LISTING_ID = "externalListingId";
	private static final String TICKET_ID = "ticketId";
	private static final String CATEGORY_ID = "categoryId";
	private static final String GROUPING_ID = "groupingId";
	private static final String PERFORMER_ID = "performerId";
	private static final String VENUE_ID = "venueId";
	private static final String EVENT_ID = "eventId";
	private static final String SOLD_QUANTITY = "soldQuantity";
	private static final String PRICE_PER_TICKET = "pricePerTicket";
	private static final String STATS_PRICE_PER_TICKET = "statsPricePerTicket";
	private static final String STATS_COST_PER_TICKET = "statsCostPerTicket";
	private static final String ID = "id";
	private static final String EXTERNALLISTINGID = "externalListingId";
	private static final String ORDER_PROC_STATUS_ID = "orderProcStatusId";
	private static final String FRAUD_CHECK_STATUS_ID = "fraudCheckStatusId";
	private static final String EVENT_DATE = "eventDate";
	private static final String CANCELLED = "cancelled";
	private static final String EVENT_STATUS = "eventStatus";
	private static final String ORDER_PROC_SUB_STATUS_CODE = "orderProcSubStatusCode";
	private static final String SUBBED_TID = "subbedTid";
	private static final String SELLER_ID = "sellerId";
	private static final String BUYER_ID = "buyerId";
	private static final String SECTION_ID = "venueConfigSectionsId";
	private static final String ZONE_ID = "zoneId";
	private static final String ROW_DESC = "rowDesc";


	private static final String QUANTITY_REMAIN = "quantityRemain";
	private static final String TICKET_SYSTEM_STATUS = "ticketSystemStatus";
	private static final String LMS_APPROVAL_STATUS_ID = "lmsApprovalStatusId";
	private static final String SALE_END_DATE = "saleEndDate";
	private static final String LISTING_SOURCE_ID = "listingSourceId";
	private static final String TICKET_PRICE = "ticketPrice";
	private static final String EXPECTED_INHAND_DATE = "expectedInhandDate";
	private static final String TICKET_MEDIUM = "ticketMedium";



	private static String FULFILLMENT_METHOD_UPS = "10";
	private static String FULFILLMENT_METHOD_FEDEX = "6";
	private static String FULFILLMENT_METHOD_ROYALMAIL = "11";
	private static String FULFILLMENT_METHOD_DEUTSCHEPOST = "12";
	private static String FULFILLMENT_METHOD_LMS = "7";
	private static String FULFILLMENT_METHOD_LMSPREDELIVERY = "9";
	private static String FULFILLMENT_METHOD_COURIER = "15";
	private static String FULFILLMENT_METHOD_EXTERNALMOBILETRANSFER = "18";
	private static String FULFILLMENT_METHOD_EXTERNALFLASHTRANSFER = "19";
	private static String FULFILLMENT_METHOD_MOBILEPREDELIVERY = "20";
	private static String FULFILLMENT_METHOD_MOBILE = "21";
	private static String FULFILLMENT_METHOD_LOCALDELIVERY = "17";

	private static final String TO_WITH_SPACE = " TO ";
	private static final String CLOSE_BRACKET = "]";
	private static final String OPEN_BRACKET = "[";

	private static final Logger LOG = LoggerFactory.getLogger(AccountSolrCloudBizImpl.class);

	@Autowired
	private HttpClient4UtilHelper httpClient4UtilHelper;

	@Autowired
	private MasterStubhubPropertiesWrapper masterStubhubProperties;
	private ObjectMapper om;

	@Autowired
	private EventUtil eventUtil;

	@Autowired
	private VenueConfigZoneSectionDAO venueConfigZoneSectionDAO;
	
    private static final ThreadLocal<SimpleDateFormat> DATE_FORMAT_TZ = new ThreadLocal<SimpleDateFormat>() {
        
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        }
        
    };

	static{
		DefaultHttpClient client= (DefaultHttpClient) HttpClient4Util.getHttpClient();
		client.addRequestInterceptor(new HttpRequestInterceptor(){

			@Override
            public void process(HttpRequest httpRequest, HttpContext httpContext) {
                Map<String,Enumeration<String>> headers= MCIHeaderThreadLocal.get();
                if(null!=headers){
                    Enumeration<String> values;
                    String value;
                    for(Map.Entry<String,Enumeration<String>> entry:headers.entrySet()){
                        values=entry.getValue();
                        while(values.hasMoreElements()){
                            value=values.nextElement();
                            LOG.info("method=accountSolrCloudBiz.HttpRequestInterceptor.process message=addingHeader-{}:{}",entry.getKey(),value);
                            httpRequest.addHeader(entry.getKey(),value);
                        }

                    }
                }
                if (httpRequest instanceof EntityEnclosingRequestWrapper && !MCIQueryParamThreadLocal.get().isEmpty()) {
                    Map<String, String> mciQueryParam = MCIQueryParamThreadLocal.get();

                    StringBuilder newURL = new StringBuilder();
                    EntityEnclosingRequestWrapper postRequest = (EntityEnclosingRequestWrapper) httpRequest;
                    URI uri = postRequest.getURI();

                    String uriString = uri.toString();
                    LOG.info("method=accountSolrCloudBiz.HttpRequestInterceptor.process message=current URL is {}", uriString);
                    newURL.append(uriString);
                    if (null != uri.getQuery()) {
                        newURL.append("&");
                    } else {
                        newURL.append("?");
                    }
                    appendMciQueryParam(newURL, mciQueryParam);
                    LOG.info("method=accountSolrCloudBiz.HttpRequestInterceptor.process message=new URL is {}", newURL);

                    try {
                        postRequest.setURI(new URI(newURL.toString()));
                    } catch (URISyntaxException e) {
                        LOG.error("method=accountSolrCloudBiz.HttpRequestInterceptor.process message=error when processing query");
                    }
                }
            }

            private void appendMciQueryParam(StringBuilder url, Map<String, String> params) {
                Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, String> entry = iterator.next();
                    url.append(entry.getKey());
                    url.append("=");
                    url.append(entry.getValue());
                    if (iterator.hasNext()) {
                        url.append("&");
                    }
                }
            }
		});
	}

	enum MyOrdersSearchOrderByEnum{
		EVENTDESC("EVENT_DESCRIPTION", EVENT_NAME_SORT), ORDERDATE("TRANSACTION_DATE", TRANSACTION_DATE), EVENTDATE("EVENT_DATE", EVENT_DATE);
		private String fieldname;
		private String newFieldName;
		MyOrdersSearchOrderByEnum(String fieldname, String newFieldName) {
			this.fieldname = fieldname;
			this.newFieldName = newFieldName;
		}
		public String getFieldname() {
			return fieldname;
		}
		public String getNewFieldName() {
			return newFieldName;
		}
	}
    
	@Override
	public JsonNode getSellerSales(SalesSearchCriteria request) {

		StringBuilder sb = new StringBuilder();
		sb.append(masterStubhubProperties.getProperty(SEARCH_MCI_API_URL_PROP_NAME, DEFAULT_SEARCH_MCI_API_V1));
		sb.append("/seller/").append(request.getSellerId()).append("/sales");
		LOG.debug("message=\"Before query search mci\" url={} ", sb);
		StopWatch sw = new StopWatch();
		sw.start();
		try {
			String jsonRequestStr = buildRequest(request);
			int timeout = DEFAULT_SEARCH_MCI_API_TIME_OUT_INT;
			String timeoutString = masterStubhubProperties.getProperty(SEARCH_MCI_API_TIME_OUT_PROP_NAME,
					DEFAULT_SEARCH_MCI_API_TIME_OUT);
			if (isNumeric(timeoutString)) {
				timeout = Integer.parseInt(timeoutString);
			}
			SimpleHttpResponse response = httpClient4UtilHelper.postToUrl(sb.toString(), MediaType.APPLICATION_JSON,jsonRequestStr, null, timeout, true);
			LOG.debug("message=\"SUCCESS - while querying search mci api for seller sales\" sellerId={} ",
					request.getSellerId());
			return om.readTree(response.getContent());
		} catch (IOException e) {
			LOG.error("message=\"IOEXCEPTION - while querying search mci api for seller sales\" sellerId={}",
					request.getSellerId(), e);
			throw new RuntimeException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error("message=\"EXCEPTION - while querying search mci api for seller sales\" sellerId={}",
					request.getSellerId(), e);
			com.stubhub.domain.account.common.Error listingError = new com.stubhub.domain.account.common.Error(
					ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT, e.getMessage(), BLANK);
			throw new AccountException(listingError);
		} finally {
			sw.stop();
			LOG.info("message=\"After querying search mci api for seller sales\" sellerId={} costTimeInMS={}",
					request.getSellerId(), sw.getTotalTimeMillis());
		}
	}
	
	@Override
	public JsonNode getSellerEventSales(SalesSearchCriteria criteria) {
		StringBuilder sb = new StringBuilder();
		sb.append(masterStubhubProperties.getProperty(SEARCH_MCI_API_URL_PROP_NAME, DEFAULT_SEARCH_MCI_API_V1));
		sb.append("/collection/sale");
		LOG.debug("message=\"Before query search mci\" url={} ", sb);
		StopWatch sw = new StopWatch();
		sw.start();
		try {
			ArrayNode mustNodes = om.createArrayNode();
			// NOT (ORDER_PROC_STATUS_ID:8000 OR ORDER_PROC_SUB_STATUS_CODE:38
			// OR ORDER_PROC_SUB_STATUS_CODE:39)
			mustNodes.add(not(match(ORDER_PROC_STATUS_ID, "8000")));
			mustNodes.add(not(in(ORDER_PROC_SUB_STATUS_CODE, "38", "39")));
			// exclude transferred sales
			mustNodes.add(not(match("ticketCost", "0")));

			setSectionIdFilters(criteria, mustNodes);
			setZoneIdFilters(criteria, mustNodes);

			String jsonRequestStr = buildRequest(criteria, mustNodes);
			int timeout = DEFAULT_SEARCH_MCI_API_TIME_OUT_INT;
			String timeoutString = masterStubhubProperties.getProperty(SEARCH_MCI_API_TIME_OUT_PROP_NAME,
					DEFAULT_SEARCH_MCI_API_TIME_OUT);
			if (isNumeric(timeoutString)) {
				timeout = Integer.parseInt(timeoutString);
			}
			SimpleHttpResponse response = httpClient4UtilHelper.postToUrl(sb.toString(), MediaType.APPLICATION_JSON, jsonRequestStr, null, timeout, true);
			LOG.debug("message=\"SUCCESS - while querying search mci api for event sales\" eventId={}",
					criteria.getEventId());
			return om.readTree(response.getContent());
		} catch (IOException e) {
			LOG.error("message=\"IOEXCEPTION - while querying search mci api for event sales\" eventId={}",
					criteria.getSellerId(), e);
			com.stubhub.domain.account.common.Error listingError = new com.stubhub.domain.account.common.Error(
					ErrorType.SYSTEMERROR, ErrorCode.SYSTEM_ERROR, e.getMessage(), BLANK);
			throw new AccountException(listingError);
		} catch (Exception e) {
			LOG.error("message=\"EXCEPTION - while querying search mci api for event sales\" eventId={}",
					criteria.getSellerId(), e);
			com.stubhub.domain.account.common.Error listingError = new com.stubhub.domain.account.common.Error(
					ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT, e.getMessage(), BLANK);
			throw new AccountException(listingError);
		} finally {
			sw.stop();
			LOG.info("message=\"After querying search mci api for event sales\" eventId={} costTimeInMS={}",
					criteria.getSellerId(), sw.getTotalTimeMillis());
		}
	}

	private void setZoneIdFilters(SalesSearchCriteria criteria, ArrayNode mustNodes) {
		String[] zoneIds = criteria.getZoneIds();
		if (zoneIds != null) {
			List<String> ids = new ArrayList<String>();
			for (String zoneId : zoneIds) {
				zoneId = zoneId.replaceAll("[^\\d]", "");
				if (isNumeric(zoneId) && parseLong(zoneId) > 0) {
					ids.add(zoneId);
				}
			}
			mustNodes.add(in(ZONE_ID, ids));
			//make sure no duplicated queries
			criteria.setZoneIds(null);
		}
	}

	private void setSectionIdFilters(SalesSearchCriteria criteria, ArrayNode mustNodes) {
		String[] sectionIds = criteria.getSectionIds();
		if (sectionIds != null) {
			List<String> ids = new ArrayList<String>();
			for (String sectionId : sectionIds) {
				sectionId = sectionId.replaceAll("[^\\d]", "");
				if (isNumeric(sectionId) && parseLong(sectionId) > 0) {
					ids.add(sectionId);
				}
			}
			mustNodes.add(in(SECTION_ID, ids));
			//make sure no duplicated queries
			criteria.setSectionIds(null);
		}
	}


	@Override
	public JsonNode getSellerListings(ListingSearchCriteria request) {
		StringBuilder sb = new StringBuilder();
		sb.append(masterStubhubProperties.getProperty(SEARCH_MCI_API_URL_PROP_NAME, DEFAULT_SEARCH_MCI_API_V1));
		sb.append("/seller/").append(request.getSellerId()).append("/inventories");
		LOG.debug("message=\"Before query search mci inventories\" url={} ", sb);
		StopWatch sw = new StopWatch();
		sw.start();
		try {
			String jsonRequestStr = buildRequest(request);
			LOG.debug("message=Mci request body={}", jsonRequestStr);
			int timeout = DEFAULT_SEARCH_MCI_API_TIME_OUT_INT;
			String timeoutString = masterStubhubProperties.getProperty(SEARCH_MCI_API_TIME_OUT_PROP_NAME,
					DEFAULT_SEARCH_MCI_API_TIME_OUT);
			if (isNumeric(timeoutString)) {
				timeout = Integer.parseInt(timeoutString);
			}
			SimpleHttpResponse response = httpClient4UtilHelper.postToUrl(sb.toString(), MediaType.APPLICATION_JSON, jsonRequestStr, null, timeout, true);
			LOG.debug("message=\"SUCCESS - while querying search mci api for getMyListing\" sellerId={} ",
					request.getSellerId());
			return om.readTree(response.getContent());
		} catch (IOException e) {
			LOG.error("message=\"IOEXCEPTION - while querying search mci api for seller sales\" sellerId={}",
					request.getSellerId(), e);
			throw new RuntimeException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error("message=\"EXCEPTION - while querying search mci api for seller sales\" sellerId={}",
					request.getSellerId(), e);
			com.stubhub.domain.account.common.Error listingError = new com.stubhub.domain.account.common.Error(
					ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT, e.getMessage(), "");
			throw new AccountException(listingError);
		} finally {
			sw.stop();
			LOG.info("message=\"After querying search mci api for getMyListing\" sellerId={} costTimeInMS={}",
					request.getSellerId(), sw.getTotalTimeMillis());
		}
	}
	
	
	@Override
	public JsonNode getSellerListings(String sellerIds, String eventIds, String status, PaginationInput paginationInput) {
		StringBuilder sb = new StringBuilder();
		sb.append(masterStubhubProperties.getProperty(SEARCH_MCI_API_URL_PROP_NAME, DEFAULT_SEARCH_MCI_API_V1));
		sb.append("/collection/inventory");
		LOG.debug("message=\"Before query search mci\" url={} ", sb);
		StopWatch sw = new StopWatch();
		sw.start();
		try {
			ObjectNode queryNode = om.createObjectNode();
			if (paginationInput!= null) {
				pagination(paginationInput.getStart(), paginationInput.getRows(), queryNode);
			}
			ArrayNode mustNodes = om.createArrayNode();
			ObjectNode filterNode = om.createObjectNode();
			filterNode.put(MUST, mustNodes);
			queryNode.put(FILTER, filterNode);
			// EVENT_ACTIVE:1
			mustNodes.add(in(EVENT_STATUS, EventStatus.ACTIVE.getValue(), EventStatus.CONTINGENT.getValue(),
					EventStatus.POSTPONED.getValue()));
			// QUANTITY_REMAIN:[1 TO *]
			mustNodes.add(range(QUANTITY_REMAIN, "1", null));
			// EVENT_DATE:[NOW TO *]
			mustNodes.add(range(EVENT_DATE, "NOW", null));
			// SALE_END_DATE:[NOW TO *]
			mustNodes.add(range(SALE_END_DATE, "NOW", null));
			if (StringUtils.isNotBlank(sellerIds)) {
				List<String> ids = parseStringToIdList(sellerIds);
				if (!ids.isEmpty()) {
					// SELLERID:(xxx OR xxx)
					mustNodes.add(in(SELLER_ID, ids));
				}
			}
			if(StringUtils.isNotBlank(eventIds)){
				List<String> ids = parseStringToIdList(eventIds);
				if (!ids.isEmpty()) {
					// EVENT_ID:(xxx OR xxx)
					mustNodes.add(in(EVENT_ID, ids));
				}
			}
			if(StringUtils.isNotBlank(status)){
				// -(TICKET_SYSTEM_STATUS:DELETED);
				mustNodes.add(not(match(TICKET_SYSTEM_STATUS, "DELETED")));
				StringTokenizer token = new StringTokenizer(status, ",");
				Set<String> statusSet = new HashSet<String>();
				while (token.hasMoreElements()) {
					String listingStatusStr = token.nextToken().trim();
					if (listingStatusStr.equalsIgnoreCase("PENDING")) {
						statusSet.add("PENDING LOCK");
						statusSet.add("PENDING PDF REVIEW");
					} else {
						com.stubhub.domain.account.datamodel.entity.ListingStatus listingStatus = getListingStatus(listingStatusStr);
						if (listingStatus != null) {
							statusSet.add(listingStatus.getDescription());
						}
					}
				}
				if (!statusSet.isEmpty()) {
					// TICKET_SYSTEM_STATUS:(xxx OR xxx)
					mustNodes.add(in(TICKET_SYSTEM_STATUS, statusSet));
				}
			} else {
				// "(TICKET_SYSTEM_STATUS:ACTIVE OR
				// TICKET_SYSTEM_STATUS:INACTIVE OR
				// TICKET_SYSTEM_STATUS:\"PENDING LOCK\" OR
				// TICKET_SYSTEM_STATUS:\"PENDING PDF REVIEW\" OR
				// (TICKET_SYSTEM_STATUS:INCOMPLETE AND LISTING_SOURCE_ID:10) OR
				// (TICKET_SYSTEM_STATUS:INCOMPLETE AND
				// LMS_APPROVAL_STATUS_ID:1))";

				ArrayNode shouldNodes = om.createArrayNode();
				shouldNodes.add(in(TICKET_SYSTEM_STATUS, "ACTIVE", "INACTIVE", "PENDING LOCK", "PENDING PDF REVIEW"));
				ArrayNode innerMustNode = om.createArrayNode();
				innerMustNode.add(match(TICKET_SYSTEM_STATUS, "INCOMPLETE"));
				innerMustNode.add(match(LISTING_SOURCE_ID, "10"));
				shouldNodes.add(createNode(MUST, innerMustNode));
				ArrayNode innerMustNode2 = om.createArrayNode();
				innerMustNode2.add(match(TICKET_SYSTEM_STATUS, "INCOMPLETE"));
				innerMustNode2.add(match(LMS_APPROVAL_STATUS_ID, "1"));
				shouldNodes.add(createNode(MUST, innerMustNode2));
				mustNodes.add(createNode(SHOULD, shouldNodes));
			}
			LOG.debug("After build queryNode={}", queryNode);
			String jsonRequestStr = queryNode.toString();
			int timeout = DEFAULT_SEARCH_MCI_API_TIME_OUT_INT;
			String timeoutString = masterStubhubProperties.getProperty(SEARCH_MCI_API_TIME_OUT_PROP_NAME,
					DEFAULT_SEARCH_MCI_API_TIME_OUT);
			if (isNumeric(timeoutString)) {
				timeout = Integer.parseInt(timeoutString);
			}
			SimpleHttpResponse response = httpClient4UtilHelper.postToUrl(sb.toString(), MediaType.APPLICATION_JSON ,jsonRequestStr, null, timeout, true);
			LOG.debug("message=\"SUCCESS - while querying search mci api for getListings\" sellerIds={} eventIds={} status={}",
					sellerIds, eventIds, status);
			return om.readTree(response.getContent());
		} catch (IOException e) {
			LOG.error("message=\"IOEXCEPTION - while querying search mci api for getListings\" sellerIds={} eventIds={} status={}",
					sellerIds, eventIds, status, e);
			com.stubhub.domain.account.common.Error error = new com.stubhub.domain.account.common.Error(
					ErrorType.SYSTEMERROR, ErrorCode.SYSTEM_ERROR, e.getMessage(), BLANK);
			throw new AccountException(error);
		} catch (Exception e) {
			LOG.error("message=\"EXCEPTION - while querying search mci api for getListings\" sellerId={} eventIds={} status={}",
					sellerIds, eventIds, status, e);
			com.stubhub.domain.account.common.Error listingError = new com.stubhub.domain.account.common.Error(
					ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT, e.getMessage(), "");
			throw new AccountException(listingError);
		} finally {
			sw.stop();
			LOG.info("message=\"After querying search mci api for getListings\" sellerId={} eventIds={} status={} costTimeInMS={}",
					sellerIds, eventIds, status, sw.getTotalTimeMillis());
		}
	}

	private List<String> parseStringToIdList(String sellerIds) {
		List<String> ids = new ArrayList<String>();
		StringTokenizer token = new StringTokenizer(sellerIds, ",");
		while (token.hasMoreElements()) {
			String nextToken = token.nextToken().trim();
			if (isNumeric(nextToken)) {
				ids.add(nextToken);
			}
		}
		return ids;
	}


	public String[] splitValues(String values){
		return StringUtils.split(values.replaceAll("\\(|\\)|OR|or", ""));
	}
	protected String buildRequest(SalesSearchCriteria request) {
		return buildRequest(request, null);
	}

	protected String buildRequest(SalesSearchCriteria request, ArrayNode extraMustNodes) {
		ObjectNode queryNode = om.createObjectNode();
		termQuery(StringUtils.trim(request.getQ()), queryNode);
		if (request.getPaginationInput() != null) {
			pagination(request.getPaginationInput().getStart(), request.getPaginationInput().getRows(), queryNode);
		}
		ArrayNode mustNodes = om.createArrayNode();
		if (extraMustNodes != null) {
			for (int i = 0; i < extraMustNodes.size(); i++) {
				mustNodes.add(extraMustNodes.get(i));
			}
		}
		if (SalesSearchCriteria.Category.OPEN == request.getCategory()) {
			ArrayNode shouldNodes = om.createArrayNode();
			shouldNodes.add(range(ORDER_PROC_SUB_STATUS_CODE, null, "1"));
			shouldNodes.add(in(ORDER_PROC_SUB_STATUS_CODE, "2", "48"));
			shouldNodes.add(not(in(ORDER_PROC_SUB_STATUS_CODE, "42", "43", "50", "54")));
			shouldNodes.add(range(ORDER_PROC_STATUS_ID, "1000", "4000"));
			shouldNodes.add(match(ORDER_PROC_STATUS_ID, "9000"));

			mustNodes.add(createNode(SHOULD, shouldNodes));

			mustNodes.add(not(match(CANCELLED, "1")));
			mustNodes.add(not(match(EVENT_STATUS, CANCELLED)));
			mustNodes.add(range(EVENT_DATE, "NOW", null));
			mustNodes.add(not(match(ORDER_PROC_SUB_STATUS_CODE, "11")));
		} else if (SalesSearchCriteria.Category.COMPLETE == request.getCategory()) {
			ArrayNode shouldNodes = om.createArrayNode();
			shouldNodes.add(range(ORDER_PROC_STATUS_ID, "5000", "8000"));
			shouldNodes.add(match(EVENT_STATUS, CANCELLED));
			shouldNodes.add(not(in(ORDER_PROC_SUB_STATUS_CODE, "2", "42", "43", "54")));
			shouldNodes.add(match(CANCELLED, "1"));
			shouldNodes.add(range(EVENT_DATE, null, "NOW"));
			shouldNodes.add(match(ORDER_PROC_SUB_STATUS_CODE, "11"));
			mustNodes.add(createNode(SHOULD, shouldNodes));
			mustNodes.add(not(in(FRAUD_CHECK_STATUS_ID, "900", "1000")));
		}
		if (request.getStatus() != null) {
			String[] saleStatuses = request.getStatus().split(COMMA);
			if (!(saleStatuses.length == 1 && saleStatuses[0].trim().equalsIgnoreCase("ALL"))) {
				ArrayNode shouldNodes = om.createArrayNode();
				if (request.getStatus().contains("CONFIRMED") || request.getStatus().contains("SHIPPED")
						|| request.getStatus().contains("DELIVERED") || request.getStatus().contains("SUBSOFFERED")
						|| request.getStatus().contains("CANCELLED") || request.getStatus().contains("ONHOLD")) {
					List<String> codes = new ArrayList<String>();
					for (String saleStatus : saleStatuses) {
						saleStatus = saleStatus.trim();
						if (saleStatus.equalsIgnoreCase(SaleStatus.CONFIRMED.name())) {
							codes.add("2000");
						} else if (saleStatus.equalsIgnoreCase(SaleStatus.SHIPPED.name())) {
							codes.add("4000");
						} else if (saleStatus.equalsIgnoreCase(SaleStatus.DELIVERED.name())) {
							codes.add("5000");
							codes.add("6000");
						} else if (saleStatus.equalsIgnoreCase(SaleStatus.SUBSOFFERED.name())) {
							codes.add("7000");
						} else if (saleStatus.equalsIgnoreCase(SaleStatus.CANCELLED.name())) {
							codes.add("8000");
						} else if (saleStatus.equalsIgnoreCase(SaleStatus.ONHOLD.name())) {
							codes.add("10000");
						}
					}
					shouldNodes.add(in(ORDER_PROC_STATUS_ID, codes));
				}
				for (String saleStatus : saleStatuses) {
					saleStatus = saleStatus.trim();
					if (saleStatus.equalsIgnoreCase(SaleStatus.PENDING.name())) {
						ArrayNode innerMustNode = om.createArrayNode();
						innerMustNode.add(match(ORDER_PROC_STATUS_ID, "1000"));
						innerMustNode.add(not(match(ORDER_PROC_SUB_STATUS_CODE, "50")));
						shouldNodes.add(createNode(MUST, innerMustNode));
						shouldNodes.add(match(ORDER_PROC_STATUS_ID, "3000"));
					} else if (saleStatus.equalsIgnoreCase(SaleStatus.PENDINGREVIEW.name())) {
						ArrayNode innerMustNode = om.createArrayNode();
						innerMustNode.add(match(ORDER_PROC_STATUS_ID, "1000"));
						innerMustNode.add(match(ORDER_PROC_SUB_STATUS_CODE, "50"));
						shouldNodes.add(createNode(MUST, innerMustNode));
						ArrayNode innerMustNode2 = om.createArrayNode();
						innerMustNode2.add(match(ORDER_PROC_STATUS_ID, "9000"));
						innerMustNode2.add(match(ORDER_PROC_SUB_STATUS_CODE, "44"));
						shouldNodes.add(createNode(MUST, innerMustNode2));
					} else if (saleStatus.equalsIgnoreCase(SaleStatus.DELIVERYEXCEPTION.name())) {
						ArrayNode innerMustNode = om.createArrayNode();
						innerMustNode.add(match(ORDER_PROC_STATUS_ID, "9000"));
						innerMustNode.add(not(match(ORDER_PROC_SUB_STATUS_CODE, "44")));
						shouldNodes.add(createNode(MUST, innerMustNode));
					}

				}
				mustNodes.add(createNode(SHOULD, shouldNodes));
			}
		}
		if (SalesSearchCriteria.Category.OPEN != request.getCategory()
				&& SalesSearchCriteria.Category.COMPLETE != request.getCategory() && request.getStatus() == null) {
			if (!request.isIncludePending()) {
				// AND NOT (ORDER_PROC_STATUS_ID:1000 AND
				// (ORDER_PROC_SUB_STATUS_CODE:(42 43 45 46 50 54)))
				ArrayNode shouldNodes = om.createArrayNode();
				shouldNodes.add(not(match(ORDER_PROC_STATUS_ID, "1000")));
				shouldNodes.add(not(in(ORDER_PROC_SUB_STATUS_CODE, "42", "43", "45", "46", "50", "54")));
				mustNodes.add(createNode(SHOULD, shouldNodes));
			} else {
				// AND NOT (ORDER_PROC_STATUS_ID:1000 AND
				// (ORDER_PROC_SUB_STATUS_CODE:(42 43 45 46 54)))
				// ACTM-554 fix for solr parse issue
				ArrayNode subMustNodes = om.createArrayNode();
				subMustNodes.add((match(ORDER_PROC_STATUS_ID, "1000")));
				subMustNodes.add(in(ORDER_PROC_SUB_STATUS_CODE, "42", "43", "45", "46", "54"));
				mustNodes.add(not(createNode(MUST, subMustNodes)));
			}
			
			if(request.isExcludeCancel()){
				// AND NOT (ORDER_PROC_STATUS_ID:8000 OR ORDER_PROC_SUB_STATUS_CODE:38 OR ORDER_PROC_SUB_STATUS_CODE:39)
				mustNodes.add(not(match(ORDER_PROC_STATUS_ID, "8000")));
				mustNodes.add(not(in(ORDER_PROC_SUB_STATUS_CODE, "38", "39")));
			}
		}
		
		if(request.isExcludeZeroCost()){
			// AND -STATS_COST_PER_TICKET:0
			mustNodes.add(not(match(STATS_COST_PER_TICKET, "0")));
		}

		if (request.getAction() != null) {
			ArrayNode shouldNodes = om.createArrayNode();
			switch (request.getAction()) {
			case GENERATE:
				ArrayNode innerMustNode = om.createArrayNode();
				innerMustNode.add(range(ORDER_PROC_SUB_STATUS_CODE, "4", "5"));
				innerMustNode.add(in(TICKET_MEDIUM_ID, "1", "5"));
				innerMustNode.add(not(match(FULFILLMENT_METHOD_ID, FULFILLMENT_METHOD_COURIER)));
				shouldNodes.add(createNode(MUST, innerMustNode));
				shouldNodes.add(in(ORDER_PROC_SUB_STATUS_CODE, "6", "7"));
				mustNodes.add(createNode(SHOULD, shouldNodes));
				break;
			case REPRINT:
				shouldNodes.add(in(ORDER_PROC_SUB_STATUS_CODE, "8", "9", "10"));
				mustNodes.add(createNode(SHOULD, shouldNodes));
				break;
			case UPLOAD:
				ArrayNode innerMustNode2 = om.createArrayNode();
				innerMustNode2.add(range(ORDER_PROC_SUB_STATUS_CODE, "4", "5"));
				innerMustNode2.add(match(TICKET_MEDIUM_ID, "2"));
				shouldNodes.add(createNode(MUST, innerMustNode2));
				shouldNodes.add(in(ORDER_PROC_SUB_STATUS_CODE, "32", "33"));
				mustNodes.add(createNode(SHOULD, shouldNodes));
				break;
			case ENTER:
				mustNodes.add(range(ORDER_PROC_SUB_STATUS_CODE, "4", "5"));
				mustNodes.add(match(TICKET_MEDIUM_ID, "3"));
				break;
			case COURIER:
				mustNodes.add(range(ORDER_PROC_SUB_STATUS_CODE, "4", "5"));
				mustNodes.add(match(FULFILLMENT_METHOD_ID, FULFILLMENT_METHOD_COURIER));
				break;
			case PROOF:
				mustNodes.add(match(ORDER_PROC_SUB_STATUS_CODE, "55"));
				break;
			case VERIFY:
				mustNodes.add(in(ORDER_PROC_SUB_STATUS_CODE, "52", "56", "57"));
				break;
			default:
				break;
			}
		}
		if ((request.getSaleDateMin() != null) && (request.getSaleDateMax() != null)) {
            DateFormat format = DATE_FORMAT_TZ.get();
			String fromDate = format.format(request.getSaleDateMin().getTime());
			Calendar cal = request.getSaleDateMax();
			cal = setToMidNight(cal);
			String endDate = format.format(cal.getTime());
			mustNodes.add(range(TRANSACTION_DATE, fromDate, endDate));
		} else if ((request.getSaleDateMin() == null) && (request.getSaleDateMax() != null)) {
            DateFormat format = DATE_FORMAT_TZ.get();
			Calendar cal = request.getSaleDateMax();
			cal = setToMidNight(cal);
			String endDate = format.format(cal.getTime());
			mustNodes.add(range(TRANSACTION_DATE, null, endDate));
		} else if ((request.getSaleDateMin() != null) && (request.getSaleDateMax() == null)) {
            DateFormat format = DATE_FORMAT_TZ.get();
			String fromDate = format.format(request.getSaleDateMin().getTime());
			mustNodes.add(range(TRANSACTION_DATE, fromDate, null));
		}

		Calendar dateLastModifiedMin = request.getDateLastModifiedMin();
		Calendar dateLastModifiedMax = request.getDateLastModifiedMax();
		if (dateLastModifiedMin!=null || dateLastModifiedMax!=null){
			mustNodes.add(
				range(
					DATE_LAST_MODIFIED,
					dateLastModifiedMin == null? null : DATE_FORMAT_TZ.get().format(dateLastModifiedMin.getTime()),
					dateLastModifiedMax == null? null : DATE_FORMAT_TZ.get().format(setToMidNight(dateLastModifiedMax).getTime())
				)
			);
		}

		if ((request.getInHandDateMin() != null) && (request.getInHandDateMax() != null)) {
            DateFormat format = DATE_FORMAT_TZ.get();
			String fromDate = format.format(request.getInHandDateMin().getTime());
			Calendar cal = request.getInHandDateMax();
			cal = setToMidNight(cal);
			String endDate = format.format(cal.getTime());
			mustNodes.add(range(INHAND_DATE, fromDate, endDate));
		} else if ((request.getInHandDateMin() == null) && (request.getInHandDateMax() != null)) {
            DateFormat format = DATE_FORMAT_TZ.get();
			Calendar cal = request.getInHandDateMax();
			cal = setToMidNight(cal);
			String endDate = format.format(cal.getTime());
					mustNodes.add(range(INHAND_DATE, null, endDate));
		} else if ((request.getInHandDateMin() != null) && (request.getInHandDateMax() == null)) {
            DateFormat format = DATE_FORMAT_TZ.get();
			String fromDate = format.format(request.getInHandDateMin().getTime());
			mustNodes.add(range(INHAND_DATE, fromDate, null));
		}

		if ((request.getEventDateMin() != null) && (request.getEventDateMax() != null)) {
            DateFormat format = DATE_FORMAT_TZ.get();
			String fromDate = format.format(request.getEventDateMin().getTime());
			Calendar cal = request.getEventDateMax();
			cal = setToMidNight(cal);
			String endDate = format.format(cal.getTime());
			mustNodes.add(range(EVENT_DATE_LOCAL, fromDate, endDate));
		} else if ((request.getEventDateMin() == null) && (request.getEventDateMax() != null)) {
            DateFormat format = DATE_FORMAT_TZ.get();
			Calendar cal = request.getEventDateMax();
			cal = setToMidNight(cal);
			String endDate = format.format(cal.getTime());
			mustNodes.add(range(EVENT_DATE_LOCAL, null, endDate));
		} else if ((request.getEventDateMin() != null) && (request.getEventDateMax() == null)) {
            DateFormat format = DATE_FORMAT_TZ.get();
			String fromDate = format.format(request.getEventDateMin().getTime());
			mustNodes.add(range(EVENT_DATE_LOCAL, fromDate, null));
		}

		if ((request.getEventDateUTCMin() != null) && (request.getEventDateUTCMax() != null)) {
			DateFormat format = DATE_FORMAT_TZ.get();
			String fromDate = format.format(request.getEventDateUTCMin().getTime());
			Calendar cal = request.getEventDateUTCMax();
			cal = setToMidNight(cal);
			String endDate = format.format(cal.getTime());
			mustNodes.add(range(EVENT_DATE, fromDate, endDate));
		} else if ((request.getEventDateUTCMin() == null) && (request.getEventDateUTCMax() != null)) {
			DateFormat format = DATE_FORMAT_TZ.get();
			Calendar cal = request.getEventDateUTCMax();
			cal = setToMidNight(cal);
			String endDate = format.format(cal.getTime());
			mustNodes.add(range(EVENT_DATE, null, endDate));
		} else if ((request.getEventDateUTCMin() != null) && (request.getEventDateUTCMax() == null)) {
			DateFormat format = DATE_FORMAT_TZ.get();
			String fromDate = format.format(request.getEventDateUTCMin().getTime());
			mustNodes.add(range(EVENT_DATE, fromDate, null));
		}

		if (StringUtils.isNotEmpty(request.getDeliveryOption())) {
			String[] deliveryOptions = request.getDeliveryOption().split(COMMA);
			if (!((deliveryOptions.length == 1) && (deliveryOptions[0].trim().equalsIgnoreCase("ALL")))) {
				List<String> codes = new ArrayList<String>();
				for (String deliveryOption : deliveryOptions) {
					String deliveryOptionToUse = StringUtils.trimToEmpty(deliveryOption);
					if (deliveryOptionToUse.equalsIgnoreCase(DeliveryOption.BARCODE.name())) {
						codes.add("1");
						codes.add("2");
						codes.add("3");
					} else if (deliveryOptionToUse.equalsIgnoreCase(DeliveryOption.PDF.name())) {
						codes.add("4");
						codes.add("5");
					} else if (deliveryOptionToUse.equalsIgnoreCase(DeliveryOption.FEDEX.name())) {
						codes.add("6");
					} else if (deliveryOptionToUse.equalsIgnoreCase(DeliveryOption.LMS.name())) {
						codes.add("7");
						codes.add("9");
					} else if (deliveryOptionToUse.equalsIgnoreCase(DeliveryOption.UPS.name())) {
						codes.add("10");
					} else if (deliveryOptionToUse.equalsIgnoreCase(DeliveryOption.ROYALMAIL.name())) {
						codes.add("11");
					} else if (deliveryOptionToUse.equalsIgnoreCase(DeliveryOption.DEUTSCHEPOST.name())) {
						codes.add("12");
					} else if (deliveryOptionToUse.equalsIgnoreCase(DeliveryOption.COURIER.name())){
						codes.add(FULFILLMENT_METHOD_COURIER);
					} else if (deliveryOptionToUse.equalsIgnoreCase(DeliveryOption.MOBILE_TICKET.name())){
						codes.add(FULFILLMENT_METHOD_MOBILEPREDELIVERY);
						codes.add(FULFILLMENT_METHOD_MOBILE);
					} else if (deliveryOptionToUse.equalsIgnoreCase(DeliveryOption.EXTERNAL_TRANSFER.name())){
						codes.add(FULFILLMENT_METHOD_EXTERNALMOBILETRANSFER);
						codes.add(FULFILLMENT_METHOD_EXTERNALFLASHTRANSFER);
					}
				}
				mustNodes.add(in(FULFILLMENT_METHOD_ID, codes));
			}
		}
		if (StringUtils.isNotEmpty(request.getSellerCategoryCode())) {
			String[] sellerCategoryCodes = request.getSellerCategoryCode().split(COMMA);
			if (!((sellerCategoryCodes.length == 1) && (sellerCategoryCodes[0].trim().equalsIgnoreCase("ALL")))) {
				List<String> codes = new ArrayList<String>();
				for (String sellerCategoryCode : sellerCategoryCodes) {
					codes.add(StringUtils.trimToEmpty(sellerCategoryCode));
				}
				mustNodes.add(in(SALE_CATEGORY, codes));
			}
		}
		if ((request.getPriceMin() != null) && (request.getPriceMax() != null)) {
			mustNodes.add(range(PRICE_PER_TICKET, String.valueOf(request.getPriceMin().floatValue()),
					String.valueOf(request.getPriceMax().floatValue())));
		} else if ((request.getPriceMin() == null) && (request.getPriceMax() != null)) {
			mustNodes.add(range(PRICE_PER_TICKET, null, String.valueOf(request.getPriceMax().floatValue())));
		} else if ((request.getPriceMin() != null) && (request.getPriceMax() == null)) {
			mustNodes.add(range(PRICE_PER_TICKET, String.valueOf(request.getPriceMin().floatValue()), null));
		} else if (request.getPrice() != null) {
			mustNodes.add(match(PRICE_PER_TICKET, String.valueOf(request.getPrice().floatValue())));
		}

		if ((request.getMinSoldQuantity() != null) && (request.getMaxSoldQuantity() != null)) {
			mustNodes.add(range(SOLD_QUANTITY, String.valueOf(request.getMinSoldQuantity()),
					String.valueOf(request.getMaxSoldQuantity())));
		} else if ((request.getMinSoldQuantity() == null) && (request.getMaxSoldQuantity() != null)) {
			mustNodes.add(range(SOLD_QUANTITY, null, String.valueOf(request.getMaxSoldQuantity())));
		} else if ((request.getMinSoldQuantity() != null) && (request.getMaxSoldQuantity() == null)) {
			mustNodes.add(range(SOLD_QUANTITY, String.valueOf(request.getMinSoldQuantity()), null));
		} else if (request.getSoldQuantity() != null) {
			mustNodes.add(match(SOLD_QUANTITY, String.valueOf(request.getSoldQuantity())));
		}
		if (request.getEventId() != null) {
			mustNodes.add(match(EVENT_ID, String.valueOf(request.getEventId())));
		}
		if (request.getGenreId() != null) {
			ArrayNode shouldNodes = om.createArrayNode();
			String genreId = StringUtils.trimToEmpty(request.getGenreId());
			if (genreId.startsWith(LEFT_BRACKET)) {
				genreId = genreId.substring(1);
			}
			if (genreId.endsWith(RIGHT_BRACKET)) {
				genreId = genreId.substring(0, genreId.length() - 1);
			}
			genreId = genreId.trim();
			if (genreId.length() > 0) {
				String[] genreIds = StringUtils.split(genreId, ' ');
				shouldNodes.add(in(PERFORMER_ID, genreIds));
				shouldNodes.add(in(GROUPING_ID, genreIds));
				shouldNodes.add(in(CATEGORY_ID, genreIds));
				mustNodes.add(createNode(SHOULD, shouldNodes));
			}
		}
		if (request.getVenueId() != null) {
			String venueId = StringUtils.trimToEmpty(request.getVenueId());
			if (venueId.startsWith(LEFT_BRACKET)) {
				venueId = venueId.substring(1);
			}
			if (venueId.endsWith(RIGHT_BRACKET)) {
				venueId = venueId.substring(0, venueId.length() - 1);
			}
			venueId = venueId.trim();
			if (venueId.length() > 0) {
				String[] venueIds = StringUtils.split(venueId, ' ');
				mustNodes.add(in(VENUE_ID, venueIds));
			}
		}
		if(request.getZoneIds() != null && request.getZoneIds().length > 0){
			String[] zoneIds = request.getZoneIds();
			List<String> sectionIdList = new ArrayList<String>();
			Map<Long, String> zoneToSectionMap;
			if (request.getEventId() != null) {
				Event eventResponse = eventUtil.getEventV3(request.getEventId().toString(), null);
				if(eventResponse == null) {
					com.stubhub.domain.account.common.Error listingError = new com.stubhub.domain.account.common.Error(ErrorType.INPUTERROR, ErrorCode.INVALID_EVENT_ID, "Event inactive OR expired", EVENT_ID);
					throw new AccountException(listingError);
				}
				if (eventResponse != null && eventResponse.getVenue() != null) {
					Long venueConfigId = parseLong(eventResponse.getVenue().getConfigurationId());
					if (venueConfigId != null) {
						 zoneToSectionMap = this.getZoneToSectionMapByVenue(venueConfigId);
						 for(String zoneId : zoneIds) {
							zoneId = zoneId.replaceAll("[^\\d]", "");
							String sectionIdString = "";
							if (!zoneId.isEmpty()) {
								sectionIdString = zoneToSectionMap.get(new Long(zoneId.trim()));
							}
							if ((sectionIdString != null) && !(sectionIdString.isEmpty())) {
								String[] sectionIds = sectionIdString.split(",");
								Collections.addAll(sectionIdList, sectionIds);
							}
						}
					}
				}
			}
			if (sectionIdList != null && !sectionIdList.isEmpty()) {
				mustNodes.add(in(SECTION_ID, sectionIdList.toArray(new String[sectionIdList.size()])));
			}
		}
		if(request.getSectionIds() != null && request.getSectionIds().length > 0){
			mustNodes.add(in(SECTION_ID, request.getSectionIds()));
		}
		if(request.getRowDescs() != null && request.getRowDescs().length > 0){
			mustNodes.add(in(ROW_DESC, request.getRowDescs()));
		}
		if (request.getListingIds() != null) {
			String[] ids = request.getListingIds().split(COMMA);
			List<String> codes = new ArrayList<String>();
			for (String id1 : ids) {
				String id = id1.replaceAll(NON_ALPHABET_RGEX, BLANK);
				if (StringUtils.isNotEmpty(id)) {
					codes.add(id);
				}
			}
			mustNodes.add(in(TICKET_ID, codes));
		}
		if (request.getExternalListingIds() != null) {
			String[] ids = request.getExternalListingIds().split(COMMA);
			List<String> codes = new ArrayList<String>();
			for (String id1 : ids) {
				String id = id1.replaceAll(NON_ALPHABET_RGEX, BLANK);
				if (StringUtils.isNotEmpty(id)) {
					codes.add(id);
				}
			}
			mustNodes.add(in(EXTERNAL_LISTING_ID, codes));
		}
		if ((request.getFromSaleId() != null) && (request.getToSaleId() != null)) {
			mustNodes.add(range(ID, String.valueOf(request.getFromSaleId()), String.valueOf(request.getToSaleId())));
		} else if ((request.getFromSaleId() == null) && (request.getToSaleId() != null)) {
			mustNodes.add(range(ID, null, String.valueOf(request.getToSaleId())));
		} else if ((request.getFromSaleId() != null) && (request.getToSaleId() == null)) {
			mustNodes.add(range(ID, String.valueOf(request.getFromSaleId()), null));
		} else if (request.getSaleId() != null) {
			mustNodes.add(match(ID, String.valueOf(request.getSaleId())));
		}

		if (request.getSubbedId() != null) {
			mustNodes.add(in(SUBBED_TID, splitValues(request.getSubbedId())));
		}
		if (request.getSellerId() != null) {
			mustNodes.add(match(SELLER_ID, String.valueOf(request.getSellerId())));
		}
		if (request.getBuyerId() != null) {
			mustNodes.add(match(BUYER_ID, String.valueOf(request.getBuyerId())));
		}

		ObjectNode filterNode = om.createObjectNode();
		filterNode.put(MUST, mustNodes);
		queryNode.put(FILTER, filterNode);
		handleSummary(request, queryNode);

		handleSort(request, queryNode);

		LOG.info("After build queryNode={}", queryNode);
		return queryNode.toString();
	}


	protected String buildRequest(ListingSearchCriteria request) {
		ObjectNode queryNode = om.createObjectNode();
		termQuery(StringUtils.trim(request.getQ()), queryNode);
		if (request.getPaginationInput() != null) {
			pagination(request.getPaginationInput().getStart(), request.getPaginationInput().getRows(), queryNode);
		}
		ArrayNode mustNodes = om.createArrayNode();

		if (!ListingStatus.SOLD.equals(request.getStatus())) {
			if(request.isIncludeSold()) {
				mustNodes.add(range(QUANTITY_REMAIN, "0", null));
			}else {
				mustNodes.add(range(QUANTITY_REMAIN, "1", null));
			}
			if (!ListingStatus.EXPIRED.equals(request.getStatus())) {
				ArrayNode shouldNodes = om.createArrayNode();
				shouldNodes.add(in(EVENT_STATUS, EventStatus.ACTIVE.getValue(), EventStatus.CONTINGENT.getValue(), EventStatus.POSTPONED.getValue()));
				mustNodes.add(createNode(SHOULD, shouldNodes));
			}
		}

		if (request.getStatus() != null) {
			if (ListingStatus.PENDING == request.getStatus()) {
				ArrayNode shouldNodes = om.createArrayNode();
				ArrayNode mustNodeWithLMSApproval = om.createArrayNode();
				mustNodeWithLMSApproval.add(match(TICKET_SYSTEM_STATUS, TicketSystemStatus.INCOMPLETE.name()));
				mustNodeWithLMSApproval.add(match(LMS_APPROVAL_STATUS_ID, "1"));
				shouldNodes.add(createNode(MUST, mustNodeWithLMSApproval));
				shouldNodes.add(in(TICKET_SYSTEM_STATUS, PENDING_PDF_REVIEW.getDescription(), PENDING_LOCK.getDescription()));
				mustNodes.add(range(SALE_END_DATE, "NOW", null));
				mustNodes.add(createNode(SHOULD, shouldNodes));

			} else if (ListingStatus.EXPIRED.equals(request.getStatus())) {
				ArrayNode shouldNodes = om.createArrayNode();
				shouldNodes.add(in(TICKET_SYSTEM_STATUS, ACTIVE.getDescription(), INACTIVE.getDescription()));
				mustNodes.add(createNode(SHOULD, shouldNodes));
			} else if (ListingStatus.SOLD.equals(request.getStatus())) {
				mustNodes.add(match(QUANTITY_REMAIN, "0"));
			} else {
				mustNodes.add(match(TICKET_SYSTEM_STATUS, request.getStatus().name()));
			}
		} else {
			ArrayNode shouldNodes = om.createArrayNode();
			if(request.isIncludeDeleted()) {
				shouldNodes.add(in(TICKET_SYSTEM_STATUS, ACTIVE.getDescription(), INACTIVE.getDescription(), PENDING_LOCK.getDescription(), PENDING_PDF_REVIEW.getDescription(), DELETED.getDescription()));
			}else {
				shouldNodes.add(in(TICKET_SYSTEM_STATUS, ACTIVE.getDescription(), INACTIVE.getDescription(), PENDING_LOCK.getDescription(), PENDING_PDF_REVIEW.getDescription()));
			}
			ArrayNode mustNodeWithListingFromStubPro = om.createArrayNode();
			mustNodeWithListingFromStubPro.add(match(TICKET_SYSTEM_STATUS, INCOMPLETE.getDescription()));
			mustNodeWithListingFromStubPro.add(match(LISTING_SOURCE_ID, "10"));
			ArrayNode mustNodeWithLMSApproval = om.createArrayNode();
			mustNodeWithLMSApproval.add(match(TICKET_SYSTEM_STATUS, TicketSystemStatus.INCOMPLETE.name()));
			mustNodeWithLMSApproval.add(match(LMS_APPROVAL_STATUS_ID, "1"));
			shouldNodes.add(createNode(MUST, mustNodeWithListingFromStubPro));
			shouldNodes.add(createNode(MUST, mustNodeWithLMSApproval));
			mustNodes.add(createNode(SHOULD, shouldNodes));

		}
		//listingId
		if (request.getListingId() != null) {
			mustNodes.add(match(ID, request.getListingId().toString()));
		}
		//eventId
		if (request.getEventId() != null) {
			mustNodes.add(match(EVENT_ID, request.getEventId().trim()));
		}
		//externalListingId
		if (request.getExternalListingId() != null) {
			mustNodes.add(match(EXTERNALLISTINGID, request.getExternalListingId()));
		}
		//genreId
		if (request.getGenreId() != null) {
			ArrayNode shouldNodes = om.createArrayNode();
			String genreId = StringUtils.trimToEmpty(request.getGenreId());
			if (genreId.startsWith(LEFT_BRACKET)) {
				genreId = genreId.substring(1);
			}
			if (genreId.endsWith(RIGHT_BRACKET)) {
				genreId = genreId.substring(0, genreId.length() - 1);
			}
			genreId = genreId.trim();
			if (genreId.length() > 0) {
				String[] genreIds = StringUtils.split(genreId, ' ');
				shouldNodes.add(in(PERFORMER_ID, genreIds));
				shouldNodes.add(in(GROUPING_ID, genreIds));
				shouldNodes.add(in(CATEGORY_ID, genreIds));
				mustNodes.add(createNode(SHOULD, shouldNodes));
			}
		}
		//venueId
		if (request.getVenueId() != null) {
			String venueId = StringUtils.trimToEmpty(request.getVenueId());
			if (venueId.startsWith(LEFT_BRACKET)) {
				venueId = venueId.substring(1);
			}
			if (venueId.endsWith(RIGHT_BRACKET)) {
				venueId = venueId.substring(0, venueId.length() - 1);
			}
			venueId = venueId.trim();
			if (venueId.length() > 0) {
				String[] venueIds = StringUtils.split(venueId, ' ');
				mustNodes.add(in(VENUE_ID, venueIds));
			}
		}
		//eventDate
		handleDate(mustNodes, request.getEventDateMin(), request.getEventDateMax(), EVENT_DATE);
		handleDate(mustNodes, request.getDateLastModifiedMin(), request.getDateLastModifiedMax(), DATE_LAST_MODIFIED);

		if (ListingStatus.EXPIRED.equals(request.getStatus())
				|| ListingStatus.INACTIVE.equals(request.getStatus())) {
			mustNodes.add(range(EVENT_DATE, "NOW-30DAYS", null));
		} else if (!ListingStatus.ACTIVE.equals(request.getStatus())) {
			mustNodes.add(range(EVENT_DATE, "NOW", null));
		}
		//ticketPrice
		if ((request.getPriceMin() != null) && (request.getPriceMax() != null)) {
			mustNodes.add(range(TICKET_PRICE, String.valueOf(request.getPriceMin().floatValue()),
					String.valueOf(request.getPriceMax().floatValue())));
		} else if ((request.getPriceMin() == null) && (request.getPriceMax() != null)) {
			mustNodes.add(range(TICKET_PRICE, null, String.valueOf(request.getPriceMax().floatValue())));
		} else if ((request.getPriceMin() != null) && (request.getPriceMax() == null)) {
			mustNodes.add(range(TICKET_PRICE, String.valueOf(request.getPriceMin().floatValue()), null));
		}

		//deliveryOption
		if (request.getDeliveryOption() != null) {
			switch (request.getDeliveryOption()) {
				case BARCODE:
					mustNodes.add(match(TICKET_MEDIUM, String.valueOf(TicketMedium.BARCODE.getValue())));
					break;
				case PDF:
					mustNodes.add(match(TICKET_MEDIUM, String.valueOf(TicketMedium.PDF.getValue())));
					break;
				case FEDEX:
					mustNodes.add(in(TICKET_MEDIUM, String.valueOf(TicketMedium.PAPER.getValue()),
						String.valueOf(TicketMedium.SEASONCARD.getValue())));
					mustNodes.add(match(FULFILLMENT_METHOD_ID, FULFILLMENT_METHOD_FEDEX));
					break;
				case UPS:
					mustNodes.add(in(TICKET_MEDIUM, String.valueOf(TicketMedium.PAPER.getValue()),
						String.valueOf(TicketMedium.SEASONCARD.getValue())));
					mustNodes.add(match(FULFILLMENT_METHOD_ID, FULFILLMENT_METHOD_UPS));
					break;
				case ROYALMAIL:
					mustNodes.add(in(TICKET_MEDIUM, String.valueOf(TicketMedium.PAPER.getValue()),
						String.valueOf(TicketMedium.SEASONCARD.getValue())));
					mustNodes.add(match(FULFILLMENT_METHOD_ID, FULFILLMENT_METHOD_ROYALMAIL));
					break;
				case DEUTSCHEPOST:
					mustNodes.add(in(TICKET_MEDIUM, String.valueOf(TicketMedium.PAPER.getValue()),
						String.valueOf(TicketMedium.SEASONCARD.getValue())));
					mustNodes.add(match(FULFILLMENT_METHOD_ID, FULFILLMENT_METHOD_DEUTSCHEPOST));
					break;
				case LMS:
					mustNodes.add(in(TICKET_MEDIUM, String.valueOf(TicketMedium.PAPER.getValue()),
						String.valueOf(TicketMedium.SEASONCARD.getValue())));
					String[] fmIds = { FULFILLMENT_METHOD_LMS, FULFILLMENT_METHOD_LMSPREDELIVERY };
					mustNodes.add(in(FULFILLMENT_METHOD_ID, fmIds));
					String[] lmsOptions = { String.valueOf(LMSOption.APPROVED.getValue()), String.valueOf(LMSOption.PENDINGAPPROVAL.getValue()) };
					mustNodes.add(in(LMS_APPROVAL_STATUS_ID, lmsOptions));
					break;
				case FLASHSEAT:
					mustNodes.add(match(TICKET_MEDIUM, String.valueOf(TicketMedium.FLASHSEAT.getValue())));
					break;
				case COURIER:
					mustNodes.add(in(FULFILLMENT_METHOD_ID, FULFILLMENT_METHOD_COURIER));
					break;
				case LOCALDELIVERY:
					mustNodes.add(in(FULFILLMENT_METHOD_ID, FULFILLMENT_METHOD_LOCALDELIVERY));
					break;
				default:
					break;
			}
		}


		//expectedInhandDate
		handleDate(mustNodes, request.getInHandDateMin(), request.getInHandDateMax(), EXPECTED_INHAND_DATE);

		//saleEndDate
		boolean hasSaleEndDate= handleDate(mustNodes, request.getSaleEndDateMin(), request.getSaleEndDateMax(), SALE_END_DATE);
		if(!hasSaleEndDate && !ListingStatus.SOLD.equals(request.getStatus())) {
			if (ListingStatus.EXPIRED.equals(request.getStatus())) {
				mustNodes.add(range(SALE_END_DATE, null, "NOW"));
			} else if (!ListingStatus.PENDING.equals(request.getStatus())) {
				mustNodes.add(range(SALE_END_DATE, "NOW", null));
			}
		}

        //exclude sell-it-now
		mustNodes.add(not(match("snowInd", "1")));

		ObjectNode filterNode = om.createObjectNode();
		filterNode.put(MUST, mustNodes);
		queryNode.put(FILTER, filterNode);
		handleSummary(request, queryNode);

		handleSort(request, queryNode);

		LOG.debug("After listing build queryNode={}", queryNode);
		return queryNode.toString();

	}


	private void handleSort(SalesSearchCriteria request, ObjectNode rootNode) {
		ArrayNode sortNodes = om.createArrayNode();
		if (request.getSortingDirectives() != null) {
			for (SortingDirective directive : request.getSortingDirectives()) {
				ORDER sortOrder = SolrQuery.ORDER.desc;
				if (directive.getSortDirection() > 0) {
					sortOrder = SolrQuery.ORDER.asc;
				}
				SortColumnType sortColumnType = directive.getSortColumnType();
				if (sortColumnType == SortColumnType.EVENT_DESCRIPTION) {
					sortNodes.add(sort("eventNameSort", sortOrder));
				} else {
					sortNodes.add(sort(toCamelCase(sortColumnType.name()), sortOrder));
				}
			}

		}
		// to work properly in case of pagination
		// we need deterministic sorting
		sortNodes.add(sort(ID, SolrQuery.ORDER.asc));
		rootNode.put(SORT, sortNodes);
	}

	private void handleSort(ListingSearchCriteria request, ObjectNode rootNode) {
		ArrayNode sortNodes = om.createArrayNode();
		if (request.getSortingDirectives() != null) {
			for (SortingDirective directive : request.getSortingDirectives()) {
				ORDER sortOrder = SolrQuery.ORDER.desc;
				if (directive.getSortDirection() > 0) {
					sortOrder = SolrQuery.ORDER.asc;
				}
				SortColumnType sortColumnType = directive.getSortColumnType();
				if (sortColumnType == SortColumnType.EVENT_DESCRIPTION) {
					sortNodes.add(sort(EVENT_NAME_SORT, sortOrder));
				} else {
					sortNodes.add(sort(toCamelCase(sortColumnType.name()), sortOrder));
				}
			}

		}
		// to work properly in case of pagination
		// we need deterministic sorting
		sortNodes.add(sort(ID, SolrQuery.ORDER.asc));
		rootNode.put(SORT, sortNodes);
	}

	private void handleSummary(SalesSearchCriteria request, ObjectNode rootNode) {
		ArrayNode aggNodes = om.createArrayNode();

		if (request.isIncludeVenueSummary()) {
			aggNodes.add(facet(VENUE_ID_NAME_FACET));
		}
		if (request.isIncludeGenreSummary()) {
			aggNodes.add(facet(PERFORMER_ID_NAME_FACET));
		}
		if (request.isIncludeEventSummary()) {
			aggNodes.add(facet(EVENT_ID_NAME_FACET));
		}
		if(request.isIncludeCostPerTicketSummary()){
			aggNodes.add(stats(STATS_COST_PER_TICKET));
		}
		if(request.isIncludePricePerTicketSummary()){
			aggNodes.add(stats(STATS_PRICE_PER_TICKET));
		}
		if (aggNodes.size() > 0) {
			rootNode.put(AGGREGATIONS, aggNodes);
		}
	}

	private void handleSummary(ListingSearchCriteria request, ObjectNode rootNode) {
		ArrayNode aggNodes = om.createArrayNode();

		if (request.isIncludeVenueSummary()) {
			aggNodes.add(facet(VENUE_ID_NAME_FACET));
		}

		if (request.isIncludeGenreSummary()) {
			aggNodes.add(facet(PERFORMER_ID_NAME_FACET));
		}
		if (request.isIncludeEventSummary()) {
			aggNodes.add(facet(EVENT_ID_NAME_FACET));
		}
		if (aggNodes.size() > 0) {
			rootNode.put(AGGREGATIONS, aggNodes);
		}
	}

	private boolean handleDate(ArrayNode mustNodes,Calendar minDate,Calendar maxDate,String fieldName){
		String fromDate = null;
		String endDate = null;
        DateFormat format = DATE_FORMAT_TZ.get();
		if((minDate != null) && (maxDate != null)){
			fromDate = format.format(minDate.getTime());
			setToMidNight(maxDate);
			endDate = format.format(maxDate.getTime());
		}else if((minDate == null) && (maxDate != null)){
			setToMidNight(maxDate);
			endDate = format.format(maxDate.getTime());
		}else if((minDate != null)){
			fromDate = format.format(minDate.getTime());
		}
		if(fromDate != null || endDate != null) {
			mustNodes.add(range(fieldName, fromDate, endDate));
			return true;
		}
		return false;
	}

	@Override
	public void afterPropertiesSet() {
		om = new ObjectMapper();
	}

	private enum EventStatus{
		ACTIVE("active"),
		CANCELLED("cancelled"),
		CONTINGENT("contingent"),
		POSTPONED("postponed");

		private String value;

		EventStatus(String value){
			this.value = value;
		}

		public String getValue(){
			return value;
		}
	}

	private enum TicketSystemStatus{
		ACTIVE,
		INCOMPLETE,
		INACTIVE
	}

	@Override
	public JsonNode getByIdList(String collection, String idField,
			List<String> idList) {
		StringBuilder sb = new StringBuilder();
		sb.append(masterStubhubProperties.getProperty(SEARCH_MCI_API_URL_PROP_NAME, DEFAULT_SEARCH_MCI_API_V1));
		sb.append("/collection/").append(collection);
		LOG.debug("message=\"Before query search mci\" url={} ", sb);
		StopWatch sw = new StopWatch();
		sw.start();
		try {
			// build query request
			ObjectNode queryNode = om.createObjectNode();
			pagination(0, idList.size(), queryNode);
			ArrayNode shouldNodes = om.createArrayNode();
			for(String id:idList){
				shouldNodes.add(match(idField, id));
			}
			ObjectNode filterNode = om.createObjectNode();
			filterNode.put(SHOULD, shouldNodes);
			queryNode.put(FILTER, filterNode);
			String jsonRequestStr = queryNode.toString();
			
			// send http query
			int timeout = DEFAULT_SEARCH_MCI_API_TIME_OUT_INT;
			String timeoutString = masterStubhubProperties.getProperty(SEARCH_MCI_API_TIME_OUT_PROP_NAME,
					DEFAULT_SEARCH_MCI_API_TIME_OUT);
			if (isNumeric(timeoutString)) {
				timeout = Integer.parseInt(timeoutString);
			}
			SimpleHttpResponse response = httpClient4UtilHelper.postToUrl(sb.toString(), MediaType.APPLICATION_JSON, jsonRequestStr, null, timeout, true);
			LOG.debug("message=\"SUCCESS - while querying search mci api for idList\" collection={} idField={} idList={} ",
					collection, idField, idList);
			return om.readTree(response.getContent());
		} catch (IOException e) {
			LOG.error("message=\"IOEXCEPTION - while querying search mci api for idList\" collection={} idField={} idList={} ",
					collection, idField, idList, e);
			throw new RuntimeException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error("message=\"EXCEPTION - while querying search mci api for idList\" collection={} idField={} idList={} ",
					collection, idField, idList, e);
			com.stubhub.domain.account.common.Error queryError = new com.stubhub.domain.account.common.Error(
					ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT, e.getMessage(), BLANK);
			throw new AccountException(queryError);
		} finally {
			sw.stop();
			LOG.info("message=\"After querying search mci api for idList\" collection={} idField={} idList={} costTimeInMS={}",
					collection, idField, idList, sw.getTotalTimeMillis());
		}
	}

	@Override
	public JsonNode getCSOrderDetails(SalesSearchCriteria request) {
		StringBuilder sb = new StringBuilder();
		sb.append(masterStubhubProperties.getProperty(SEARCH_MCI_API_URL_PROP_NAME, DEFAULT_SEARCH_MCI_API_V1));
		sb.append("/collection/sale");
		LOG.debug("message=\"Before query search mci\" url={} ", sb);

		StopWatch sw = new StopWatch();
		sw.start();
		try {
			String jsonRequestStr = buildRequest(request);
			int timeout = DEFAULT_SEARCH_MCI_API_TIME_OUT_INT;
			String timeoutString = masterStubhubProperties.getProperty(SEARCH_MCI_API_TIME_OUT_PROP_NAME,
					DEFAULT_SEARCH_MCI_API_TIME_OUT);
			if (isNumeric(timeoutString)) {
				timeout = Integer.parseInt(timeoutString);
			}
			SimpleHttpResponse response = httpClient4UtilHelper.postToUrl(sb.toString(), MediaType.APPLICATION_JSON ,jsonRequestStr, null, timeout, true);
			LOG.debug("message=\"SUCCESS - while querying search mci api for sales of buyer \" buyerId={} ",
					request.getBuyerId());
			return om.readTree(response.getContent());
		} catch (IOException e) {
			LOG.error("message=\"IOEXCEPTION - while querying search mci api for sales of buyer \" buyerId={}",
					request.getBuyerId(), e);
			com.stubhub.domain.account.common.Error listingError = new com.stubhub.domain.account.common.Error(
					ErrorType.SYSTEMERROR, ErrorCode.SYSTEM_ERROR, e.getMessage(), BLANK);
			throw new AccountException(listingError);
		} catch (Exception e) {
			LOG.error("message=\"EXCEPTION - while querying search mci api for sales of buyer \" buyerId={}",
					request.getBuyerId(), e);
			com.stubhub.domain.account.common.Error listingError = new com.stubhub.domain.account.common.Error(
					ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT, e.getMessage(), BLANK);
			throw new AccountException(listingError);
		} finally {
			sw.stop();
			LOG.info("message=\"After querying search mci api for sales of buyer \" buyerId={} costTimeInMS={}",
					request.getBuyerId(), sw.getTotalTimeMillis());
		}
	}

	@Override
	public JsonNode getBuyerOrders(MyOrderSearchCriteria criteria) {
		StringBuilder sb = new StringBuilder();
		sb.append(masterStubhubProperties.getProperty(SEARCH_MCI_API_URL_PROP_NAME, DEFAULT_SEARCH_MCI_API_V1));
		sb.append("/collection/sale");
		LOG.debug("message=\"Before query search mci\" url={} ", sb);
		StopWatch sw = new StopWatch();
		sw.start();
		try {
			String jsonRequestStr = buildRequest(criteria);
			int timeout = DEFAULT_SEARCH_MCI_API_TIME_OUT_INT;
			String timeoutString = masterStubhubProperties.getProperty(SEARCH_MCI_API_TIME_OUT_PROP_NAME,
					DEFAULT_SEARCH_MCI_API_TIME_OUT);
			if (isNumeric(timeoutString)) {
				timeout = Integer.parseInt(timeoutString);
			}
			SimpleHttpResponse response = httpClient4UtilHelper.postToUrl(sb.toString(), MediaType.APPLICATION_JSON, jsonRequestStr, null, timeout, true);
			LOG.debug("message=\"SUCCESS - while querying search mci api for buyer orders\" buyerId={} ",
					criteria.getBuyerId());
			return om.readTree(response.getContent());
		} catch (IOException e) {
			LOG.error("message=\"IOEXCEPTION - while querying search mci api for buyer orders\" sellerId={}",
					criteria.getBuyerId(), e);
			com.stubhub.domain.account.common.Error error = new com.stubhub.domain.account.common.Error(
					ErrorType.SYSTEMERROR, ErrorCode.SYSTEM_ERROR, e.getMessage(), BLANK);
			throw new AccountException(error);
		} catch (Exception e) {
			LOG.error("message=\"EXCEPTION - while querying search mci api for seller sales\" sellerId={}",
					criteria.getBuyerId(), e);
			com.stubhub.domain.account.common.Error error = new com.stubhub.domain.account.common.Error(
					ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT, e.getMessage(), BLANK);
			throw new AccountException(error);
		} finally {
			sw.stop();
			LOG.info("message=\"After querying search mci api for seller sales\" sellerId={} costTimeInMS={}",
					criteria.getBuyerId(), sw.getTotalTimeMillis());
		}
	}

	private String buildRequest(MyOrderSearchCriteria criteria) {
		ObjectNode queryNode = om.createObjectNode();
		pagination(criteria.getStart(), criteria.getRow(), queryNode);
		
		ArrayNode mustNodes = om.createArrayNode();
		if (StringUtils.isNotBlank(criteria.getBuyerId())) {
			mustNodes.add(match(BUYER_ID, criteria.getBuyerId()));
		}
		
		if (StringUtils.isNotBlank(criteria.getEventId())) {
			mustNodes.add(match(EVENT_ID, criteria.getEventId()));
		}

        SimpleDateFormat toSdf = DATE_FORMAT_TZ.get();
		// 24hours buffer for ongoing orders
		Calendar c = DateUtil.getNowCalUTC();
		c.add(Calendar.DAY_OF_MONTH, -1);
        String currentTime = toSdf.format(c.getTime());        
		if("ongoing".equalsIgnoreCase(criteria.getStatus())){
			// queryString.append("EVENT_DATE:[").append(currentTime).append("
			// TO * ] AND -(CANCELLED:1 OR EVENT_CANCELLED_IND:1) AND
			// -(ORDER_PROC_SUB_STATUS_CODE:42) AND
			// -(ORDER_PROC_SUB_STATUS_CODE:43) AND
			// -(ORDER_PROC_SUB_STATUS_CODE:54) AND -(EVENT_ID:24275 4180110)");

			mustNodes.add(range(EVENT_DATE, currentTime, null));
			mustNodes.add(not(match(CANCELLED, "1")));
			mustNodes.add(not(match(EVENT_STATUS, CANCELLED)));
			mustNodes.add(not(in(ORDER_PROC_SUB_STATUS_CODE, "42", "43", "54")));
			mustNodes.add(not(in(EVENT_ID, "24275", "4180110")));
		}else if("past".equals(criteria.getStatus())){
//			queryString.append("( EVENT_DATE:[* TO ").append(currentTime).append(
//			" ] OR (CANCELLED:1 OR EVENT_CANCELLED_IND:1) ) AND -(ORDER_PROC_SUB_STATUS_CODE:42) AND -(ORDER_PROC_SUB_STATUS_CODE:43) AND -(ORDER_PROC_SUB_STATUS_CODE:54) AND -(EVENT_ID:24275 4180110)");
			ArrayNode shouldNodes = om.createArrayNode();
			shouldNodes.add(range(EVENT_DATE, null, currentTime));
			shouldNodes.add(match(EVENT_STATUS, CANCELLED));
			shouldNodes.add(match(CANCELLED, "1"));
			mustNodes.add(createNode(SHOULD, shouldNodes));
			mustNodes.add(not(in(ORDER_PROC_SUB_STATUS_CODE, "42", "43", "54")));
			mustNodes.add(not(in(EVENT_ID, "24275", "4180110")));
		}

		String orderDate = criteria.getOrderDate();
		if(StringUtils.isNotBlank(orderDate)){
			String[] amounts = orderDate.split(TO_WITH_SPACE);
			if (amounts.length >= 2) {
				String lowValue = amounts[0];
				if (lowValue.startsWith(OPEN_BRACKET)) {
					lowValue = lowValue.substring(1);
				}
				String highValue = amounts[1];
				if (highValue.endsWith(CLOSE_BRACKET)) {
					highValue = highValue.substring(0, highValue.length() - 1);
				}
				mustNodes.add(range(TRANSACTION_DATE, lowValue + "T00:00:00.000Z", highValue + "T00:00:00.000Z"));
			}
//			queryString.append(" AND TRANSACTION_DATE:").append(criteria.getOrderDate());
		}

		ArrayNode sortNodes = om.createArrayNode();
		boolean hasSort = false;
		if (criteria.getOrderBy() != null) {
			for (String orderBy : criteria.getOrderBy()) {
				if (StringUtils.isNotBlank(orderBy)) {
					String[] orderByTuple = StringUtils.split(StringUtils.trim(orderBy));
					if (orderByTuple.length > 1) {
						MyOrdersSearchOrderByEnum orderEnum = MyOrdersSearchOrderByEnum.valueOf(orderByTuple[0]);
						sortNodes.add(sort(orderEnum.getNewFieldName(), ORDER.valueOf(orderByTuple[1])));
						hasSort = true;
					}
				}
			}

		}
		if(!hasSort){
			sortNodes.add(sort(EVENT_DATE, ORDER.asc));
		}

		// to work properly in case of pagination
		// we need deterministic sorting
		sortNodes.add(sort(ID, SolrQuery.ORDER.asc));
		
		ObjectNode filterNode = om.createObjectNode();
		filterNode.put(MUST, mustNodes);
		queryNode.put(FILTER, filterNode);

		queryNode.put(SORT, sortNodes);

		LOG.debug("After order build queryNode={}", queryNode);

		return queryNode.toString();
	}

	public Calendar setToMidNight(Calendar cal){
		cal.set(Calendar.HOUR_OF_DAY, 23);            // set hour to EOD
		cal.set(Calendar.MINUTE, 59);                 // set minute in hour
		cal.set(Calendar.SECOND, 59);                 // set second in minute
		return cal;
	}

	public Map<Long,String> getZoneToSectionMapByVenue(Long venueConfigId){
		Map<Long,String> zoneToSectionMap = new HashMap<Long,String>();
		if(venueConfigId != null){
			List<VenueConfigZoneSection> venueConfigZonesections =  venueConfigZoneSectionDAO.getZoneSectionByVenueConfigId(venueConfigId);
			if(venueConfigZonesections != null){
				for(VenueConfigZoneSection zoneSection:venueConfigZonesections){
					if(zoneToSectionMap.containsKey(zoneSection.getZoneId())){
						zoneToSectionMap.put(zoneSection.getZoneId(), zoneToSectionMap.get(zoneSection.getZoneId()) + "," + zoneSection.getSectionId().toString());
					}
					else{
						zoneToSectionMap.put(zoneSection.getZoneId(), zoneSection.getSectionId().toString());
					}
				}
			}
		}
		return zoneToSectionMap;
	}
}
