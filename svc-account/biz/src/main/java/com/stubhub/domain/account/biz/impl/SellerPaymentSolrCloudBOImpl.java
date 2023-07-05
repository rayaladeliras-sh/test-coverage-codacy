/**
 * Copyright 2016 StubHub, Inc.  All rights reserved.
 */
package com.stubhub.domain.account.biz.impl;

import static com.stubhub.domain.account.biz.impl.MCIRequestUtil.AGGREGATIONS;
import static com.stubhub.domain.account.biz.impl.MCIRequestUtil.DEFAULT_SEARCH_MCI_API_TIME_OUT;
import static com.stubhub.domain.account.biz.impl.MCIRequestUtil.DEFAULT_SEARCH_MCI_API_TIME_OUT_INT;
import static com.stubhub.domain.account.biz.impl.MCIRequestUtil.DEFAULT_SEARCH_MCI_API_V1;
import static com.stubhub.domain.account.biz.impl.MCIRequestUtil.FILTER;
import static com.stubhub.domain.account.biz.impl.MCIRequestUtil.MUST;
import static com.stubhub.domain.account.biz.impl.MCIRequestUtil.SEARCH_MCI_API_TIME_OUT_PROP_NAME;
import static com.stubhub.domain.account.biz.impl.MCIRequestUtil.SEARCH_MCI_API_URL_PROP_NAME;
import static com.stubhub.domain.account.biz.impl.MCIRequestUtil.SORT;
import static com.stubhub.domain.account.biz.impl.MCIRequestUtil.in;
import static com.stubhub.domain.account.biz.impl.MCIRequestUtil.jsonFacet;
import static com.stubhub.domain.account.biz.impl.MCIRequestUtil.match;
import static com.stubhub.domain.account.biz.impl.MCIRequestUtil.pagination;
import static com.stubhub.domain.account.biz.impl.MCIRequestUtil.range;
import static com.stubhub.domain.account.biz.impl.MCIRequestUtil.sort;
import static com.stubhub.domain.account.biz.impl.MCIRequestUtil.termQuery;

import java.io.IOException;
import java.util.Iterator;

import com.stubhub.domain.account.datamodel.entity.SellerPaymentStatusEnum;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServerException;
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

import com.stubhub.domain.account.biz.intf.SellerPaymentSolrCloudBO;
import com.stubhub.domain.account.common.PaymentsSearchCriteria;
import com.stubhub.newplatform.http.util.HttpClient4Util.SimpleHttpResponse;
import com.stubhub.newplatform.http.util.HttpClient4UtilHelper;
import com.stubhub.newplatform.property.MasterStubhubPropertiesWrapper;

/**
 * SellerPaymentSolrCloudBOImpl
 * 
 * @author runiu
 *
 */
@Component("sellerPaymentSolrCloudBO")
public class SellerPaymentSolrCloudBOImpl extends SellerPaymentBOImpl
		implements SellerPaymentSolrCloudBO, InitializingBean {
	private final static Logger LOG = LoggerFactory.getLogger(SellerPaymentSolrCloudBOImpl.class);

	private static final String PAYMENT_VOUCHER = "Payment Voucher";
	private static final String NULL = "null";
	private static final String TO_WITH_SPACE = " TO ";
	private static final String CLOSE_BRACKET = "]";
	private static final String OPEN_BRACKET = "[";
	/* solr field */
	private static final String RECORD_TYPE = "recordType";
	private static final String SELLER_PAYMENT_STATUS = "sellerPaymentStatus";
	private static final String HOLD_PAYMENT_TYPE = "holdPaymentTypeId";
	private static final String CURRENCY_CODE = "currencyCode";
	private static final String PAYMENT_AMOUNT = "paymentAmount";
	private static final String PAYMENT_TYPE_ID = "paymentTypeId";
	private static final String PAYMENT_CREATED_DATE = "paymentCreatedDate";
	private static final String PAYMENT_COMPLETION_DATE = "paymentCompletionDate";
	public static final String DATE_LAST_MODIFIED = "dateLastModified";
	private static final String TRANSACTION_ID = "transactionId";
	private static final String REFERENCE_NUMBER = "referenceNumber";

	@Autowired
	private HttpClient4UtilHelper httpClient4UtilHelper;

	@Autowired
	private MasterStubhubPropertiesWrapper masterStubhubProperties;
	private ObjectMapper om;

	@Override
	public JsonNode getSellerPayments(PaymentsSearchCriteria request) throws SolrServerException {
		StringBuilder sb = new StringBuilder();
		sb.append(masterStubhubProperties.getProperty(SEARCH_MCI_API_URL_PROP_NAME, DEFAULT_SEARCH_MCI_API_V1));
			sb.append("/seller/").append(request.getSellerId()).append("/payments");
		LOG.debug("message=\"Before query search mci\"" + " url={} ", sb);
		int timeout = getTimeOut();

		StopWatch sw = new StopWatch();
		sw.start();
		try {
			ObjectNode queryNode = buildQueryRequest(request);
			excludeFundCaptureForUS(request, queryNode);

			LOG.debug("queryNode={}", queryNode);
			SimpleHttpResponse response = httpClient4UtilHelper.postToUrl(sb.toString(), "application/json",queryNode.toString(), null, timeout, true);
			LOG.debug("message=\"SUCCESS - while querying search mci api for seller payments\" sellerId={} ",
					request.getSellerId());
			return om.readTree(response.getContent());
		} catch (IOException e) {
			LOG.error("message=\"IOEXCEPTION - while querying search mci api for seller payments\" sellerId={}",
					request.getSellerId(), e);
			throw new SolrServerException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error("message=\"EXCEPTION - while querying search mci api for seller payments\" sellerId={}",
					request.getSellerId(), e);
			throw new SolrServerException(e.getMessage(), e);
		} finally {
			sw.stop();
			LOG.info("message=\"After querying search mci api for seller payments\" sellerId={} costTimeInMS={}",
					request.getSellerId(), sw.getTotalTimeMillis());
		}

	}

	private void excludeFundCaptureForUS(PaymentsSearchCriteria request, ObjectNode queryNode) {
		// exclude FundCatpure for US
		if (StringUtils.equalsIgnoreCase(request.getCurrency(), "USD")) {
            JsonNode spStatusNode = queryNode.findValue(SELLER_PAYMENT_STATUS);
            if (spStatusNode != null) {
                JsonNode spStatusValuesNode = spStatusNode.get("values");
                if (spStatusValuesNode.isArray()) {
                    Iterator<JsonNode> iterator = spStatusValuesNode.iterator();
                    while (iterator.hasNext()) {
                        JsonNode next = iterator.next();
                        if (SellerPaymentStatusEnum.FUND_CAPTURE.getId().toString().equalsIgnoreCase(next.getTextValue())) {
                            iterator.remove();
                            break;
                        }
                    }
                }
            }
        }
	}

	public String[] splitValues(String values){
		return StringUtils.split(values.replaceAll("\\(|\\)|OR|or", ""));
	}

	protected ObjectNode buildQueryRequest(PaymentsSearchCriteria request) {
		ObjectNode queryNode = om.createObjectNode();
		ArrayNode mustNodes = om.createArrayNode();

		if (!request.isIncludeCreditMemo()) {
			// exclude credit Memo
			mustNodes.add(match(RECORD_TYPE, PAYMENT_VOUCHER));
		}
		handlePagination(request, queryNode);

		String q = StringUtils.trim(request.getQ());
		if (StringUtils.isNotEmpty(q)) {
			String filters[] = q.split(",");
			// ex: paymentDate:[2013-01-01 TO 2013-09-30]
			for (String filter : filters) {
				String params[] = StringUtils.trim(filter).split(":", 2);
				if (params.length < 2) {
					continue;
				}
				String key = StringUtils.trim(params[0]);
				String value = StringUtils.trim(params[1]);
				if (key.equals("dateLastModified")){
					buildDateLastModifiedRange(mustNodes, DATE_LAST_MODIFIED, value);
				} else if (key.equals("paymentDate")) {
					buildDateRange(mustNodes, PAYMENT_COMPLETION_DATE, value);
				} else if (key.equals("paymentInitiatedDate")) {
					buildDateRange(mustNodes, PAYMENT_CREATED_DATE, value);
				} else if (key.equals("currency")) {
					mustNodes.add(match(CURRENCY_CODE, value));
				} else if (key.equals("status")) {
					String statusIds = getSellerPaymentStatusIdsByStatusName(value);
					if (StringUtils.isNotBlank(statusIds)) {
						mustNodes.add(in(SELLER_PAYMENT_STATUS, StringUtils.split(statusIds)));
					} else {
						mustNodes.add(match(SELLER_PAYMENT_STATUS, NULL));
					}
				} else if (key.equals("holdPaymentType")) {
					mustNodes.add(in(HOLD_PAYMENT_TYPE, StringUtils.split(value)));
				} else if (key.equals("paymentAmount")) {
					String amounts[] = value.split(TO_WITH_SPACE);
					if (amounts.length >= 2) {
						String lowValue = amounts[0];
						if (lowValue.startsWith(OPEN_BRACKET)) {
							lowValue = lowValue.substring(1);
						}
						String highValue = amounts[1];
						if (highValue.endsWith(CLOSE_BRACKET)) {
							highValue = highValue.substring(0, highValue.length() - 1);
						}
						mustNodes.add(range(PAYMENT_AMOUNT, lowValue, highValue));
					}
				} else if (key.equals("paymentType")) {
					String paymentTypes = getPaymentTypesByName(value);
					if (StringUtils.isNotBlank(paymentTypes)) {
						mustNodes.add(in(PAYMENT_TYPE_ID, StringUtils.split(paymentTypes)));
					} else {
						mustNodes.add(match(PAYMENT_TYPE_ID, NULL));
					}
				} else if (key.equals("keyword")) {
					termQuery(value, queryNode);
				} else if (key.equals("orderID")) {
					mustNodes.add(in(TRANSACTION_ID, splitValues(value)));
				}
			}
		} else {
		    if (!request.isIncludeCurrencySummary())
		        mustNodes.add(range(PAYMENT_CREATED_DATE, "NOW/DAY-90DAYS", null));
		}

		ObjectNode filterNode = om.createObjectNode();
		filterNode.put(MUST, mustNodes);
		queryNode.put(FILTER, filterNode);

		handleSort(request, queryNode);

		handleSummary(request, queryNode);
		return queryNode;
	}

	private void buildDateRange(ArrayNode mustNodes, String fieldName, String value) {
		if (value.startsWith(OPEN_BRACKET)) {
			String dates[] = value.split(TO_WITH_SPACE);
			if (dates.length >= 2) {
				String fromDate = dates[0].substring(1);
				String endDate = dates[1];
				if (endDate.endsWith(CLOSE_BRACKET)) {
					endDate = endDate.substring(0, endDate.length() - 1);
				}
				mustNodes.add(range(PAYMENT_COMPLETION_DATE, fromDate, endDate));
			}
		} else {
			String dates[] = value.split(TO_WITH_SPACE);
			if (dates.length >= 2) {
				mustNodes.add(range(fieldName, dates[0] + "T00:00:00.000Z", dates[1] + "T23:59:59.999Z"));
			}
		}
	}

	private void buildDateLastModifiedRange(ArrayNode mustNodes, String fieldName, String value) {
		String dates[] = value.replaceAll("\\[", "").replaceAll("\\]", "").trim().split(TO_WITH_SPACE.trim(),2);
		mustNodes.add(range(fieldName, dates[0].trim(), dates[1].trim()));
	}

	private void handleSummary(PaymentsSearchCriteria request, ObjectNode queryNode) {
		if (request.isIncludePaymentSummary()) {
			ObjectNode objNode = om.createObjectNode();
			ObjectNode jsonFacetNode = om.createObjectNode();
			ObjectNode subObjNode = om.createObjectNode();
			objNode.put("sellerPaymentStatus", jsonFacetNode);
			jsonFacetNode.put("type", "terms");
			jsonFacetNode.put("field", "sellerPaymentStatus");
			jsonFacetNode.put("limit", 100);
			jsonFacetNode.put("facet", subObjNode);
			subObjNode.put("sum", "sum(statsPaymentAmount)");
			ArrayNode aggNodes = om.createArrayNode();
			aggNodes.add(jsonFacet(objNode));

			queryNode.put(AGGREGATIONS, aggNodes);
		}

        if (request.isIncludeCurrencySummary()) {
            ObjectNode objNode = om.createObjectNode();
            ObjectNode jsonFacetNode = om.createObjectNode();
            objNode.put("currencyCodeFacet", jsonFacetNode);
            jsonFacetNode.put("type", "terms");
            jsonFacetNode.put("field", "currencyCode");
            jsonFacetNode.put("limit", 100);
            ArrayNode aggNodes = om.createArrayNode();
            aggNodes.add(jsonFacet(objNode));
            
            queryNode.put(AGGREGATIONS, aggNodes);
        }
	}

	private void handleSort(PaymentsSearchCriteria request, ObjectNode queryNode) {
		ArrayNode sortNodes = om.createArrayNode();

		boolean needSecondarySorting = true;
		if (request.getSort() == null) {
			sortNodes.add(sort(PAYMENT_CREATED_DATE, SolrQuery.ORDER.asc));
		} else {
			ORDER order = SolrQuery.ORDER.asc;
			String sortParams[] = StringUtils.trim(request.getSort()).split(" ");
			if (sortParams.length > 1) {
				if (sortParams[1].equals("desc")) {
					order = SolrQuery.ORDER.desc;
				}
				String key = StringUtils.trim(sortParams[0]);
				if (key.equals("orderID")) {
					sortNodes.add(sort(TRANSACTION_ID, order));
					needSecondarySorting = false;
				} else if (key.equals("paymentAmount")) {
					sortNodes.add(sort(PAYMENT_AMOUNT, order));
				} else if (key.equals("status")) {
					sortNodes.add(sort(SELLER_PAYMENT_STATUS, order));
				} else if (key.equals("paymentType")) {
					sortNodes.add(sort(PAYMENT_TYPE_ID, order));
				} else if (key.equals("paymentInitiatedDate")) {
					sortNodes.add(sort(PAYMENT_CREATED_DATE, order));
				} else if (key.equals("paymentDate")) {
					sortNodes.add(sort(PAYMENT_COMPLETION_DATE, order));
				} else {
					sortNodes.add(sort(PAYMENT_CREATED_DATE, order));
				}
			}
		}

		// to work properly in case of pagination
		// we need deterministic sorting
		if (needSecondarySorting) {
			sortNodes.add(sort(TRANSACTION_ID, SolrQuery.ORDER.asc));
		}
		queryNode.put(SORT, sortNodes);
	}

	private void handlePagination(PaymentsSearchCriteria request, ObjectNode queryNode) {
		int start = 0;
		int rows = -1;

		if (request.getStart() != null) {
			start = request.getStart();
		}
		if (request.getRows() != null) {
			rows = request.getRows();
		}
		pagination(start, rows, queryNode);
	}

	@Override
	public JsonNode querySellerPayment(Long sellerId, String refNumber) throws SolrServerException {
		StringBuilder sb = new StringBuilder();
		sb.append(masterStubhubProperties.getProperty(SEARCH_MCI_API_URL_PROP_NAME, DEFAULT_SEARCH_MCI_API_V1));
		sb.append("/seller/").append(sellerId).append("/payments");
		LOG.debug("message=\"Before query search mci\"" + " url={} ", sb);
		int timeout = getTimeOut();

		StopWatch sw = new StopWatch();
		sw.start();
		try {
			ObjectNode queryNode = om.createObjectNode();
			ArrayNode mustNodes = om.createArrayNode();
			mustNodes.add(match(REFERENCE_NUMBER, refNumber));

			ObjectNode filterNode = om.createObjectNode();
			filterNode.put(MUST, mustNodes);
			queryNode.put(FILTER, filterNode);
			LOG.debug("queryNode={}", queryNode);
			SimpleHttpResponse response = httpClient4UtilHelper.postToUrl(sb.toString(), "application/json",queryNode.toString(), null, timeout, true);
			LOG.debug(
					"message=\"SUCCESS - while querying search mci api for seller payments\" sellerId={} refNumber={}",
					sellerId, refNumber);
			return om.readTree(response.getContent());
		} catch (IOException e) {
			LOG.error(
					"message=\"IOEXCEPTION - while querying search mci api for seller payments\" sellerId={}  refNumber={}",
					sellerId, refNumber, e);
			throw new SolrServerException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(
					"message=\"EXCEPTION - while querying search mci api for seller payments\" sellerId={}  refNumber={}",
					sellerId, refNumber, e);
			throw new SolrServerException(e.getMessage(), e);
		} finally {
			sw.stop();
			LOG.info(
					"message=\"After querying search mci api for seller payments\" sellerId={} refNumber={} costTimeInMS={}",
					sellerId, refNumber, sw.getTotalTimeMillis());
		}
	}

	private int getTimeOut() {
		int timeout = DEFAULT_SEARCH_MCI_API_TIME_OUT_INT;
		String timeoutString = masterStubhubProperties.getProperty(SEARCH_MCI_API_TIME_OUT_PROP_NAME,
				DEFAULT_SEARCH_MCI_API_TIME_OUT);
		if (StringUtils.isNumeric(timeoutString)) {
			timeout = Integer.parseInt(timeoutString);
		}
		return timeout;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		om = new ObjectMapper();
	}

	@Override
	public JsonNode getCSSellerPayments(PaymentsSearchCriteria request)
			throws SolrServerException {
		StringBuilder sb = new StringBuilder();
		sb.append(masterStubhubProperties.getProperty(SEARCH_MCI_API_URL_PROP_NAME, DEFAULT_SEARCH_MCI_API_V1));
		sb.append("/collection/payment");
		LOG.debug("message=\"Before query search mci\"" + " url={} ", sb);

		int timeout = getTimeOut();

		StopWatch sw = new StopWatch();
		sw.start();
		try {
			ObjectNode queryNode = buildQueryRequest(request);
			LOG.debug("queryNode={}", queryNode);
			SimpleHttpResponse response = httpClient4UtilHelper.postToUrl(sb.toString(), "application/json",queryNode.toString(), null, timeout, true);
			LOG.debug("message=\"SUCCESS - while querying search mci api for seller payments\" sellerId={} ",
					request.getSellerId());
			return om.readTree(response.getContent());
		} catch (IOException e) {
			LOG.error("message=\"IOEXCEPTION - while querying search mci api for seller payments\" sellerId={}",
					request.getSellerId(), e);
			throw new SolrServerException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error("message=\"EXCEPTION - while querying search mci api for seller payments\" sellerId={}",
					request.getSellerId(), e);
			throw new SolrServerException(e.getMessage(), e);
		} finally {
			sw.stop();
			LOG.info("message=\"After querying search mci api for seller payments\" sellerId={} costTimeInMS={}",
					request.getSellerId(), sw.getTotalTimeMillis());
		}

	}
	
}
