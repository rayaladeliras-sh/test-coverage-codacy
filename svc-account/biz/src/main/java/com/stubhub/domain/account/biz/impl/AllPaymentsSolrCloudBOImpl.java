package com.stubhub.domain.account.biz.impl;

import com.stubhub.domain.account.biz.intf.AllPaymentsSolrCloudBO;
import com.stubhub.domain.account.common.AllPaymentsSearchCriteria;
import com.stubhub.domain.account.common.PaymentsSearchCriteria;
import com.stubhub.newplatform.http.util.HttpClient4Util;
import com.stubhub.newplatform.http.util.HttpClient4UtilHelper;
import com.stubhub.newplatform.property.MasterStubhubPropertiesWrapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.stubhub.domain.account.biz.impl.MCIRequestUtil.*;

@Component("allPaymentsSolrCloudBO")
public class AllPaymentsSolrCloudBOImpl implements AllPaymentsSolrCloudBO, InitializingBean {
  private final static Log log = LogFactory.getLog(AllPaymentsSolrCloudBOImpl.class);
  private static final String CURRENCY_CODE = "currencyCode";
  private static final String DATE_ADDED = "dateAdded";
  public static final String DESC = "desc";
  public static final String ASC = "asc";
  public static final String SUM = "sum";
  public static final String FACET = "facet";
  public static final String FIELD = "field";
  public static final String STATUS = "status";
  public static final String TYPE = "type";
  public static final String NAME_FACET_PAYMENTS = "payments";
  public static final String NAME_FACET_CURRENCY = "currency";

  @Autowired
  private MasterStubhubPropertiesWrapper masterStubhubProperties;

  @Autowired
  private HttpClient4UtilHelper httpClient4UtilHelper;

  private ObjectMapper om;

  @Override
  public void afterPropertiesSet() {
    om = new ObjectMapper();
  }
  @Override
  public JsonNode getAllPayments(AllPaymentsSearchCriteria criteria) throws SolrServerException {
    StringBuilder sb = new StringBuilder();
    sb.append(masterStubhubProperties.getProperty(SEARCH_MCI_API_URL_PROP_NAME, DEFAULT_SEARCH_MCI_API_V1));
    sb.append("/seller/").append(criteria.getSellerId()).append("/allpayments");
    log.debug("message=\"Before query search mci\" url= " + sb);
    int timeout = getTimeOut();

    StopWatch sw = new StopWatch();
    sw.start();
    try {
      ObjectNode queryNode = buildQueryRequest(criteria);

      HttpClient4Util.SimpleHttpResponse response = httpClient4UtilHelper.postToUrl(sb.toString(), MediaType.APPLICATION_JSON,
              queryNode.toString(), null, timeout, true);

      return om.readTree(response.getContent());
    } catch (IOException e) {
      throw new SolrServerException(e.getMessage(), e);
    } catch (Exception e) {
      throw new SolrServerException(e.getMessage(), e);
    } finally {
      sw.stop();
    }
  }

  private int getTimeOut() {
    int timeout = DEFAULT_SEARCH_MCI_API_TIME_OUT_INT;
    String timeoutString = masterStubhubProperties.getProperty(SEARCH_MCI_API_TIME_OUT_PROP_NAME, DEFAULT_SEARCH_MCI_API_TIME_OUT);
    if (StringUtils.isNumeric(timeoutString)) {
      timeout = Integer.parseInt(timeoutString);
    }
    return timeout;
  }

  protected ObjectNode buildQueryRequest(AllPaymentsSearchCriteria request) {
    ObjectNode queryNode = om.createObjectNode();
    ArrayNode mustNodes = om.createArrayNode();
    handlePagination(request, queryNode);

    String q = StringUtils.trim(request.getQ());
    if (StringUtils.isNotEmpty(q)) {
    /*
      TODO: We need to add the valid filters fields
     */
    }

    String status = request.getStatus();
    if (StringUtils.isNotEmpty(status)) {
      String statuses[] = status.split(" ");
      List<String> sellerPaymentsStatusNameByMyaPackedStatus = new ArrayList<>();
      for (String sta: statuses) {
        sellerPaymentsStatusNameByMyaPackedStatus.addAll(MyaSellerPaymentStatusUtil.getSellerPaymentsStatusNameByMyaPackedStatus(sta));
      }
      mustNodes.add(in(STATUS, sellerPaymentsStatusNameByMyaPackedStatus));
    }

    if (StringUtils.isNotEmpty(request.getCurrency())){
      mustNodes.add(match(CURRENCY_CODE, request.getCurrency()));
    }

    ObjectNode filterNode = om.createObjectNode();
    filterNode.put(MUST, mustNodes);
    queryNode.put(FILTER, filterNode);

    handleSort(request, queryNode);

    ArrayNode aggNodes = om.createArrayNode();
    handlePaymentSummary(request, aggNodes);
    handleCurrencySummary(request, aggNodes);
    if (aggNodes.size() > 0) {
      queryNode.put(AGGREGATIONS, aggNodes);
    }

    return queryNode;
  }

  private void handleSort(AllPaymentsSearchCriteria request, ObjectNode queryNode) {
    ArrayNode sortNodes = om.createArrayNode();
    if (request.getSort() == null) {
      sortNodes.add(sort(DATE_ADDED, SolrQuery.ORDER.asc));
    } else {
      String sortParams[] = StringUtils.trim(request.getSort()).split(" ");
      SolrQuery.ORDER order = null;
      if (sortParams.length > 1) {
        if (sortParams[1].equals(DESC)) {
          order = SolrQuery.ORDER.desc;
        } else if (sortParams[1].equals(ASC)) {
          order = SolrQuery.ORDER.asc;
        }
        String key = StringUtils.trim(sortParams[0]);
        sortNodes.add(sort(key, order));
      }
    }
    queryNode.put(SORT, sortNodes);
  }

  private void handleCurrencySummary(AllPaymentsSearchCriteria request, ArrayNode aggNodes) {
    if (request.isIncludeCurrencySummary()) {
      ObjectNode objNode = om.createObjectNode();
      ObjectNode jsonFacetNode = om.createObjectNode();
      objNode.put(NAME_FACET_CURRENCY, jsonFacetNode);
      jsonFacetNode.put(TYPE, "terms");
      jsonFacetNode.put(FIELD, "currencyCode");
      aggNodes.add(jsonFacet(objNode));
    }
  }

  private void handlePaymentSummary(AllPaymentsSearchCriteria request, ArrayNode aggNodes) {
    if (request.isIncludePaymentSummary()) {
      ObjectNode objNode = om.createObjectNode();
      ObjectNode jsonFacetNode = om.createObjectNode();
      ObjectNode subObjNode = om.createObjectNode();
      objNode.put(NAME_FACET_PAYMENTS, jsonFacetNode);
      jsonFacetNode.put(TYPE, "terms");
      jsonFacetNode.put(FIELD, "status");
      jsonFacetNode.put(FACET, subObjNode);
      subObjNode.put(SUM, "sum(amount)");
      aggNodes.add(jsonFacet(objNode));
    }
  }

  private void handlePagination(AllPaymentsSearchCriteria request, ObjectNode queryNode) {
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

}
