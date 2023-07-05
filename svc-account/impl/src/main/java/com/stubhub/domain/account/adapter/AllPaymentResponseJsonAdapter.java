package com.stubhub.domain.account.adapter;

import com.stubhub.domain.account.biz.impl.MyaSellerPaymentStatusUtil;
import com.stubhub.domain.account.datamodel.enums.AllPaymentTypeEnum;
import com.stubhub.domain.account.impl.AllPaymentsServiceImpl;
import com.stubhub.domain.account.intf.*;
import com.stubhub.newplatform.common.entity.Money;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.node.TextNode;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

import static com.stubhub.domain.account.adapter.AccountResponseJsonAdapter.notNull;

public class AllPaymentResponseJsonAdapter {
  private final static Log log = LogFactory.getLog(AllPaymentResponseJsonAdapter.class);
  public static AllPaymentResponse convertJsonNodeToResponse(JsonNode jsonNode, String currency) {
    AllPaymentResponse allPaymentResponse = new AllPaymentResponse();
    allPaymentResponse.setPayments(new ArrayList<AllPayment>());

    if (jsonNode == null || !jsonNode.isObject()) {
      return allPaymentResponse;
    }

    JsonNode responseNode = jsonNode.get("response");
    if (!notNull(responseNode)) {
      return allPaymentResponse;
    }

    JsonNode numFoundNode = responseNode.get("numFound");
    if (notNull(numFoundNode)) {
      allPaymentResponse.setNumFound(numFoundNode.asInt());
    }

    ArrayNode docsNode = (ArrayNode) responseNode.get("docs");
    if (notNull(docsNode)) {
      int size = docsNode.size();

      for (int i = 0; i < size; i++) {
        ObjectNode docNode = (ObjectNode) docsNode.get(i);
        AllPayment allPayment = new AllPayment();
        JsonNode obj = docNode.get("allPaymentId");
        if (notNull(obj)) {
          allPayment.setAllPaymentsId(obj.asText());
        }

        obj = docNode.get("id");
        if (notNull(obj)) {
          allPayment.setId(obj.asLong());
        }

        obj = docNode.get("sellerId");
        if (notNull(obj)) {
          allPayment.setSellerId(obj.asLong());
        }

        obj = docNode.get("transactionId");
        if (notNull(obj)) {
          allPayment.setTransactionId(obj.asLong());
        }

        obj = docNode.get("type");
        if (notNull(obj)) {
          allPayment.setType(AllPaymentTypeEnum.getCustomNameByName(obj.asText()));
        }

        obj = docNode.get("status");
        if (notNull(obj)) {
          if (!AllPaymentTypeEnum.CREDIT_CARD_CHARGES.getCustomName().equals(allPayment.getType())) {
            allPayment.setStatus(MyaSellerPaymentStatusUtil.getMyaSellerPaymentStatus(obj.asText()).toLowerCase());
          } else {
            allPayment.setStatus(MyaSellerPaymentStatusUtil.getMyaSellerCCTransStatus(obj.asText()).toLowerCase());
          }
        }

        obj = docNode.get("amount");
        if (notNull(obj)) {
          allPayment.setAmount(BigDecimal.valueOf(obj.asDouble()));
        }

        obj = docNode.get("currencyCode");
        if (notNull(obj)) {
          allPayment.setCurrency(obj.asText());
        }

        obj = docNode.get("payeeEmailId");
        if (notNull(obj)) {
          allPayment.setPayeeEmailId(obj.asText());
        }

        obj = docNode.get("payeeId");
        if (notNull(obj)) {
          allPayment.setPayeeId(obj.asLong());
        }

        obj = docNode.get("payeeName");
        if (notNull(obj)) {
          allPayment.setPayeeName(obj.asText());
        }

        obj = docNode.get("paymentMethod");
        if (notNull(obj)) {
          allPayment.setPaymentMethod(obj.asText().toLowerCase());
        }

        obj = docNode.get("bankName");
        if (notNull(obj)) {
          allPayment.setBankName(obj.asText());
        }

        obj = docNode.get("cmApplied");
        if (notNull(obj)) {
          Integer cmApplied = obj.asInt();
          allPayment.setCmApplied(cmApplied.equals(1));
        }

        obj = docNode.get("cmAppliedHistories");
        if(notNull(obj) && obj.isArray()) {
          List<CMAppliedHistory> cmAppliedHistories = new ArrayList<CMAppliedHistory>(obj.size());
          ArrayNode cmAppliedHistoriesNode = (ArrayNode) obj;
          int cmAppliedHistoriesSize = cmAppliedHistoriesNode.size();
          ObjectMapper cmAppliedHistoriesOm = new ObjectMapper();
          for (int j = 0; j < cmAppliedHistoriesSize; j++) {
            CMAppliedHistory cmAppliedHistory = new CMAppliedHistory();
            TextNode on = (TextNode) cmAppliedHistoriesNode.get(j);
            try {
              CMAppliedHistory.RawSolrCMAppliedHistory rawSolrCMAppliedHistory = cmAppliedHistoriesOm.readValue(on.asText(), CMAppliedHistory.RawSolrCMAppliedHistory.class);
              rawSolrCMAppliedHistory.mapToResponse(cmAppliedHistory);
              cmAppliedHistories.add(cmAppliedHistory);
            } catch (IOException e) {
              continue;
            }
            allPayment.setCreditMemosApplied(cmAppliedHistories);
          }
        }

        obj = docNode.get("acctLastFourDigits");
        if (notNull(obj)) {
          allPayment.setLastFourDigits(obj.asText());
        }

        obj = docNode.get("referenceNumber");
        if (notNull(obj)) {
          allPayment.setReferenceNumber(obj.asText());
        }

        obj = docNode.get("eventId");
        if (notNull(obj)) {
          allPayment.setEventId(obj.asLong());
        }

        obj = docNode.get("eventDate");
        if (notNull(obj)) {
          allPayment.setEventDate(obj.asText());
        }

        obj = docNode.get("dateAdded");
        if (notNull(obj)) {
          allPayment.setCreatedDate(obj.asText());
        }

        obj = docNode.get("dateCharged");
        if (notNull(obj)) {
          allPayment.setChargedDate(obj.asText());
        }

        obj = docNode.get("dateLastUpdate");
        if (notNull(obj)) {
          allPayment.setLastUpdateDate(obj.asText());
        }

        allPaymentResponse.getPayments().add(allPayment);
      }
    }

    JsonNode aggregationsNode = responseNode.get("aggregations");
    if (notNull(aggregationsNode)) {
      Map<String, Double> allPaymentsSummary = getAllPaymentSummary(aggregationsNode);
      if (!allPaymentsSummary.isEmpty()) {
        allPaymentResponse.setAllPaymentsSummary(allPaymentsSummary);
      }
      List<CurrencySummary> currencySummaryList = getCurrencySummary(aggregationsNode);
      if (!currencySummaryList.isEmpty()) {
        allPaymentResponse.setCurrencySummary(currencySummaryList);
      }
    }

    return allPaymentResponse;
  }

  private static List<CurrencySummary> getCurrencySummary(JsonNode aggregationsNode) {
    JsonNode jsonNode = aggregationsNode.get("currency");
    List<CurrencySummary> currencySummaryList= new ArrayList<>();
    if (notNull(jsonNode)) {
      ArrayNode bucketNodes = (ArrayNode) jsonNode.get("buckets");
      if (notNull(bucketNodes)) {
        for (int i = 0; i < bucketNodes.size(); i++) {
          JsonNode bucketNode = bucketNodes.get(i);
          JsonNode valNode = bucketNode.get("val");
          JsonNode countNode = bucketNode.get("count");
          CurrencySummary summary = new CurrencySummary();
          summary.setCount(countNode.asInt());
          summary.setCurrency(valNode.asText());
          currencySummaryList.add(summary);
        }
      }
    }
    return currencySummaryList;
  }

  private static Map<String, Double> getAllPaymentSummary(JsonNode aggregationsNode) {
    JsonNode jsonNode = aggregationsNode.get("payments");
    Map<String, Double> allPaymentSummaries= new HashMap<>();
    if (notNull(jsonNode)) {
      ArrayNode bucketNodes = (ArrayNode) jsonNode.get("buckets");
      if (notNull(bucketNodes)) {
        for (int i = 0; i < bucketNodes.size(); i++) {
          JsonNode bucketNode = bucketNodes.get(i);
          JsonNode valNode = bucketNode.get("val");
          if (notNull(valNode)) {
            String status = MyaSellerPaymentStatusUtil.getMyaPackedStatusByStatusName(valNode.asText());
            JsonNode countNode = bucketNode.get("count");
            if (notNull(countNode)) {
              JsonNode sumNode = bucketNode.get("sum");
              if (notNull(sumNode)) {
                double sum = sumNode.asDouble();
                if (allPaymentSummaries.containsKey(status)) {
                  allPaymentSummaries.put(status, allPaymentSummaries.get(status) + sum);
                } else {
                  allPaymentSummaries.put(status, sum);
                }
              }
            }
          }
        }
      }
    }
    return allPaymentSummaries;
  }

}