/**
 * Copyright 2016 StubHub, Inc.  All rights reserved.
 */
package com.stubhub.domain.account.adapter;

import static com.stubhub.domain.account.adapter.AccountResponseJsonAdapter.notNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.node.TextNode;

import com.stubhub.domain.account.biz.impl.MCIRequestUtil;
import com.stubhub.domain.account.biz.impl.MyaSellerPaymentStatusUtil;
import com.stubhub.domain.account.common.enums.PaymentUserDefinedStatus;
import com.stubhub.domain.account.datamodel.entity.PaymentType;
import com.stubhub.domain.account.intf.CMAppliedHistory;
import com.stubhub.domain.account.intf.ChargeSellerDetail;
import com.stubhub.domain.account.intf.CurrencySummary;
import com.stubhub.domain.account.intf.PaymentSummary;
import com.stubhub.domain.account.intf.SellerPayment;
import com.stubhub.domain.account.intf.SellerPayments;
import com.stubhub.newplatform.common.entity.Money;

/**
 * PaymentResponseJsonAdpater
 * 
 * @author runiu
 *
 */
public class PaymentResponseJsonAdpater {
	
    /**
     * This method converts the QueryResponse to OrderResponse objects
     * 
     * @param jsonNode
     * @param currency
     * @return SalesResponse
     */
    public static SellerPayments convertJsonNodeToResponse(JsonNode jsonNode, String currency, Boolean includeRecordType, Boolean includeCMAppliedHistories) {
        SellerPayments payments = new SellerPayments();
        payments.setPayments(new ArrayList<SellerPayment>());
        if (jsonNode == null || !jsonNode.isObject()) { return payments; }
        JsonNode responseNode = jsonNode.get("response");
        if (!notNull(responseNode)) { return payments; }
        JsonNode numFoundNode = responseNode.get("numFound");
        if (notNull(numFoundNode)) {
            payments.setNumFound(numFoundNode.asInt());
        }
        ArrayNode docsNode = (ArrayNode) responseNode.get("docs");
        if (notNull(docsNode)) {
            int size = docsNode.size();
            if (size <= 0) { return payments; }
            
            for (int i = 0; i < size; i++) {
                ObjectNode docNode = (ObjectNode) docsNode.get(i);
                SellerPayment payment = new SellerPayment();
                JsonNode obj = docNode.get("currencyCode");
                String currencyCode = obj.asText();
                if (notNull(obj)) {
                    payment.setCurrencyCode(obj.asText());
                }
                
                obj = docNode.get("paypalPaymentEmail");
                if (notNull(obj)) {
                    payment.setPaypalEmail(obj.asText());
                }
                
                obj = docNode.get("bobId");
                if (notNull(obj)) {
                    payment.setBookOfBusinessID(obj.asText());
                }
                
                obj = docNode.get("transactionId");
                if (notNull(obj)) {
                    payment.setOrderID(obj.asText());
                }
                obj = docNode.get("paymentAmount");
                if (notNull(obj)) {
                    payment.setPaymentAmount(
                            new Money(BigDecimal.valueOf(obj.asDouble()), currencyCode));
                }
                obj = docNode.get("id");
                if (notNull(obj)) {
                    payment.setID(obj.asText());
                }
                obj = docNode.get("payeeName");
                if (notNull(obj)) {
                    payment.setPayeeName(obj.asText());
                }
                obj = docNode.get("sellerPaymentStatus");
                if (notNull(obj)) {
                    String paymentStatus = obj.asText();
                    Long statusLong = Long.valueOf(paymentStatus);
                    String status = MyaSellerPaymentStatusUtil
                            .getMyaSellerPaymentStatus(statusLong);
                    payment.setStatus(status);
                    payment.setSellerPaymentStatus(paymentStatus);
                }
                
                obj = docNode.get("referenceNumber");
                if (notNull(obj)) {
                    String refNum = obj.asText();
                    if (StringUtils.isNotBlank(refNum)) { // fix for the data
                                                          // issue.
                        payment.setReferenceNumber(refNum);
                    }
                }
                
                obj = docNode.get("paymentTypeId");
                if (notNull(obj)) {
                    PaymentType pt = PaymentType.getPaymentType(obj.asLong());
                    if (pt != null) {
                        payment.setPaymentMode(pt.name());
                    } else {
                        payment.setPaymentMode(obj.asText());
                    }
                }
                
                obj = docNode.get("eventNameSort");
                if (notNull(obj)) {
                    payment.setEventName(obj.asText());
                }
                obj = docNode.get("paymentCompletionDate");
                if (notNull(obj)) {
                    payment.setPaymentDate(obj.asText());
                }
                obj = docNode.get("paymentCreatedDate");
                if (notNull(obj)) {
                    payment.setPaymentInitiatedDate(obj.asText());
                }
                obj = docNode.get("disbursementOptionId");
                if (notNull(obj)) {
                    payment.setDisbursementOptionID(obj.asText());
                }
                
                obj = docNode.get("payeeEmailId");
                if (notNull(obj)) {
                    payment.setPayeeEmailID(obj.asText());
                }
                
                obj = docNode.get("paymentTermId");
                if (notNull(obj)) {
                    payment.setPaymentTermId(obj.asText());
                }
                
                obj = docNode.get("cmApplied");
                if (notNull(obj)) {
                    Integer cmApplied = obj.asInt();
                    payment.setCmApplied(cmApplied.equals(1));
                }
                
                obj = docNode.get("bankName");
                if (notNull(obj)) {
                    payment.setBankName(obj.asText());
                }
                
                obj = docNode.get("acctLastFourDigits");
                if (notNull(obj)) {
                    payment.setLastFourDigits(obj.asText());
                }

                obj = docNode.get("dateLastModified");
                if (notNull(obj)) {
                    payment.setDateLastModified(obj.asText());
                }

                obj = docNode.get("fxDate");
                if (notNull(obj)) {
                    payment.setFxDate(obj.asText());
                }

                obj = docNode.get("fxRate");
                if (notNull(obj)) {
                    payment.setFxRate(obj.asText());
                }

                obj = docNode.get("fxFromCurrency");
                if (notNull(obj)) {
                    payment.setFxFromCurrency(obj.asText());
                }

                obj = docNode.get("fxToCurrency");
                String fxToCurrency = "";
                if (notNull(obj)) {
                    fxToCurrency = obj.asText();
                    payment.setFxToCurrency(fxToCurrency);
                }

                obj = docNode.get("fxPostedAmount");
                if (notNull(obj) && !StringUtils.isEmpty(fxToCurrency)) {
                    payment.setFxPostedAmount(new Money(BigDecimal.valueOf(obj.asDouble()), fxToCurrency));
                }

                obj = docNode.get("paymentCondition");
                if (notNull(obj)) {
                    payment.setPaymentCondition(obj.asText());
                    if ("Addtional Payment".equals(obj.asText())){
                        JsonNode amount = docNode.get("paymentAmount");
                        payment.setAdditonalPayAmt(new Money(BigDecimal.valueOf(notNull(amount)?amount.asDouble():0), currencyCode));
                    }
                }
                
                obj = docNode.get("chargeSellerDetails");
                if (notNull(obj) && obj.isArray()) {
                    List<ChargeSellerDetail> list = new ArrayList<ChargeSellerDetail>(obj.size());
                    ObjectMapper om = new ObjectMapper();
                    for (JsonNode node : obj) {
                        // this node is TextNode
                        try {
                            ChargeSellerDetail detail = om.readValue(node.asText(), ChargeSellerDetail.class);
                            list.add(detail);
                        } catch (Exception e) {
                            continue;
                        }
                    }
                    payment.setChargeSellerDetails(list);
                }
				obj = docNode.get("amtPaidAfterCreditMemo");
				if (notNull(obj)) {
					BigDecimal amtPaidAfterCreditMemo = BigDecimal.valueOf(obj.asDouble());
					payment.setAmtPaidAfterCreditMemo(new Money(amtPaidAfterCreditMemo, currencyCode));
					JsonNode paymentAmountNode = docNode.get("paymentAmount");
					if (notNull(paymentAmountNode)) {
						BigDecimal paymentAmount = BigDecimal.valueOf(paymentAmountNode.asDouble());
						if (!amtPaidAfterCreditMemo.equals(paymentAmount)) {
							payment.setCreditMemoAmt(
									new Money(paymentAmount.subtract(amtPaidAfterCreditMemo), currencyCode));
						}
					}
				}
				obj = docNode.get("reasonDescription");
				if (notNull(obj)) {
					payment.setReasonDescription(obj.asText());
				}
				
				if(includeRecordType) {
				    obj = docNode.get("recordType");
				    if(notNull(obj)) {
				    	payment.setRecordType(obj.asText());
				    }
				}
				
				if(includeCMAppliedHistories) {
					obj = docNode.get("cmAppliedHistories");
					if(notNull(obj) && obj.isArray()) {
						List<CMAppliedHistory> cmAppliedHistories = new ArrayList<CMAppliedHistory>(obj.size());
						ArrayNode cmAppliedHistoriesNode = (ArrayNode)obj;
						int cmAppliedHistoriesSize = cmAppliedHistoriesNode.size();
						ObjectMapper cmAppliedHistoriesOm = new ObjectMapper();
						for(int j=0;j<cmAppliedHistoriesSize;j++) {
							CMAppliedHistory cmAppliedHistory = new CMAppliedHistory();
							TextNode on = (TextNode) cmAppliedHistoriesNode.get(j);
							try {
								CMAppliedHistory.RawSolrCMAppliedHistory rawSolrCMAppliedHistory = cmAppliedHistoriesOm.readValue(on.asText(), CMAppliedHistory.RawSolrCMAppliedHistory.class);
								rawSolrCMAppliedHistory.mapToResponse(cmAppliedHistory);
								cmAppliedHistories.add(cmAppliedHistory);
							} catch ( IOException e) {
								continue;
							} 
						}
						payment.setCreditMemosApplied((cmAppliedHistories));
					}
				}
                
                payments.getPayments().add(payment);
            }
        }
        JsonNode aggregationsNode = responseNode.get("aggregations");
        if (notNull(aggregationsNode)) {
            PaymentSummary summary = getPaymentSummary(aggregationsNode, currency);
            if (summary != null) {
                payments.setPaymentsSummary(Arrays.asList(summary));
            }
            
            payments.setCurrencySummary(getCurrencySummary(aggregationsNode));
            
        }
        return payments;
        
    }
    
    private static List<CurrencySummary> getCurrencySummary(JsonNode aggregationsNode) {
        JsonNode jsonNode = aggregationsNode.get("currencyCodeFacet");
        if (notNull(jsonNode)) {
            ArrayNode bucketNodes = (ArrayNode) jsonNode.get("buckets");
            if (notNull(bucketNodes)) {
                List<CurrencySummary> result = new ArrayList<CurrencySummary>();
                for (int i = 0; i < bucketNodes.size(); i++) {
                    JsonNode bucketNode = bucketNodes.get(i);
                    JsonNode valNode = bucketNode.get("val");
                    JsonNode countNode = bucketNode.get("count");
                    CurrencySummary summary = new CurrencySummary();
                    summary.setCount(countNode.asInt());
                    summary.setCurrency(valNode.asText());
                    result.add(summary);
                }
                return result;
            }
            
        }
        return Collections.emptyList();
        
    }
    
    private static PaymentSummary getPaymentSummary(JsonNode aggregationsNode, String currency) {
        JsonNode jsonNode = aggregationsNode.get("sellerPaymentStatus");
        if (notNull(jsonNode)) {
            ArrayNode bucketNodes = (ArrayNode) jsonNode.get("buckets");
            if (notNull(bucketNodes)) {
                PaymentSummary paymentSummary = new PaymentSummary();
                BigDecimal totalBigDec = new BigDecimal(0);
                BigDecimal pendingBigDec = new BigDecimal(0);
                BigDecimal processingBigDec = new BigDecimal(0);
                BigDecimal availableBigDec = new BigDecimal(0);
                for (int i = 0; i < bucketNodes.size(); i++) {
                    JsonNode bucketNode = bucketNodes.get(i);
                    JsonNode valNode = bucketNode.get("val");
                    if (notNull(valNode)) {
                        String status = valNode.asText();
                        JsonNode sumNode = bucketNode.get("sum");
                        if (notNull(sumNode)) {
                            BigDecimal sum = BigDecimal.valueOf(sumNode.asDouble());
                            String statusName = MyaSellerPaymentStatusUtil
                                    .getMyaSellerPaymentStatus(Long.valueOf(status));
                            totalBigDec = totalBigDec.add(sum);
                            if (PaymentUserDefinedStatus.PENDING.getName().equals(statusName)) {
                                pendingBigDec = pendingBigDec.add(sum);
                            }
                            if (PaymentUserDefinedStatus.PROCESSING.getName().equals(statusName)) {
                                processingBigDec = processingBigDec.add(sum);
                            }
                            if (PaymentUserDefinedStatus.AVAILABLE.getName().equals(statusName)) {
                                availableBigDec = availableBigDec.add(sum);
                            }
                        }
                        
                    }
                }
                Money totalAmount = new Money(totalBigDec, currency);
                Money pendingAmount = new Money(pendingBigDec, currency);
                Money processingAmount = new Money(processingBigDec, currency);
                Money availableAmount = new Money(availableBigDec, currency);
                paymentSummary.setCurrency(currency);
                paymentSummary.setTotalAmount(totalAmount);
                paymentSummary.setPendingAmount(pendingAmount);
                paymentSummary.setProcessingAmount(processingAmount);
                paymentSummary.setAvailableAmount(availableAmount);
                return paymentSummary;
            }
            
        }
        
        return null;
    }
    
    public static void convertJsonNodeToSellerPaymentList(List<String[]> sellerPaymentList,
            JsonNode jsonNode, int size, SimpleDateFormat dateFormat) {
        if (jsonNode == null || !jsonNode.isObject()) { return; }
        JsonNode responseNode = jsonNode.get("response");
        if (!notNull(responseNode)) { return; }
        ArrayNode docsNode = (ArrayNode) responseNode.get("docs");
        if (notNull(docsNode)) {
            if (docsNode.size() <= 0) { return; }
            dateFormat.setTimeZone(MCIRequestUtil.UTC_TIME_ZONE);
            String lastOrderNumber = null;
            String[] lastEntry = null;
            for (int i = 0; i < docsNode.size(); i++) {
                ObjectNode docNode = (ObjectNode) docsNode.get(i);
                String[] entry = new String[size];
                Arrays.fill(entry, 0, size - 1, " ");
                
                JsonNode obj = docNode.get("sellerPaymentStatus");
                String paymentStatus = null;
                if (notNull(obj)) {
                    paymentStatus = obj.asText();
                    // 22: GP Credit Memo Completed; 10: GP Payment Completed;
                    // 14: Payment Notified; 9: GP Payment Sent To Payment
                    // Gateway
                    if (!"22".equals(paymentStatus) && !"10".equals(paymentStatus)
                            && !"14".equals(paymentStatus) && !"9".equals(paymentStatus)) {
                        continue;
                    }
                } else {
                    continue;
                }
                
                obj = docNode.get("paymentCompletionDate");
                if (notNull(obj)) {
                    Date date = getDate(obj);
                    if (date != null) {
                        entry[0] = dateFormat.format(date);
                    }
                }
                
                obj = docNode.get("transactionId");
                if (notNull(obj)) {
                    entry[1] = obj.asText();
                }
                obj = docNode.get("totalTicketPrice");
                if (notNull(obj)) {
                    entry[2] = obj.asText();
                }
                obj = docNode.get("commission");
                if (notNull(obj)) {
                    entry[3] = obj.asText();
                }
                double adjustment = 0.0;
                obj = docNode.get("adjustmentAmount");
                if (notNull(obj)) {
                    adjustment = obj.asDouble();
                    adjustment *= -1;
                }
                entry[4] = String.valueOf(adjustment);
                
                double payout = 0.0d;
                obj = docNode.get("paymentAmount");
                if (notNull(obj)) {
                    payout = obj.asDouble();
                    if ("22".equals(paymentStatus)) {
                        payout *= -1;
                    }
                }
                entry[5] = String.valueOf(payout);
                
                obj = docNode.get("payeeName");
                if (notNull(obj)) {
                    entry[6] = obj.asText();
                }
                obj = docNode.get("paymentTypeUserDefined");
                if (notNull(obj)) {
                    entry[7] = obj.asText();
                }
                obj = docNode.get("eventNameSort");
                if (notNull(obj)) {
                    entry[8] = obj.asText();
                }
                obj = docNode.get("eventDateLocal");
                if (notNull(obj)) {
                    Date date = getDate(obj);
                    if (date != null) {
                        entry[9] = dateFormat.format(date);
                    }
                }
                
                obj = docNode.get("venueNameSort");
                if (notNull(obj)) {
                    entry[10] = obj.asText();
                }
                obj = docNode.get("stubhubListingId");
                if (notNull(obj)) {
                    entry[11] = obj.asText();
                }
                obj = docNode.get("externalListingId");
                if (notNull(obj)) {
                    entry[12] = obj.asText();
                }
                obj = docNode.get("ticketQuantity");
                int quantity = 0;
                if (notNull(obj)) {
                    quantity = obj.asInt();
                    entry[13] = String.valueOf(quantity);
                }
                obj = docNode.get("ticketSection");
                if (notNull(obj)) {
                    entry[14] = obj.asText();
                }
                obj = docNode.get("ticketRowDesc");
                if (notNull(obj)) {
                    entry[15] = obj.asText();
                }
                obj = docNode.get("ticketSeats");
                if (notNull(obj)) {
                    entry[16] = obj.asText();
                }
                obj = docNode.get("sellerPaymentStatusUserDefined");
                if (notNull(obj)) {
                    entry[17] = obj.asText();
                }
                obj = docNode.get("referenceNumber");
                if (notNull(obj)) {
                    entry[18] = obj.asText();
                }
                if (entry[1].equals(lastOrderNumber)) {
                    float lastPayout = Float.parseFloat(lastEntry[5]);
                    payout += lastPayout;
                    payout = Math.round(payout * 100) / 100.0f;
                    lastEntry[5] = String.valueOf(payout);
                    
                    // adjustment = ticket_price - payout - commission
                    double ticket_price = 0;
                    double commission = 0;
                    if (StringUtils.isNotEmpty(lastEntry[2])){
                        ticket_price = Double.parseDouble(lastEntry[2]);
                    }
                    if (StringUtils.isNotEmpty(lastEntry[3])){
                        commission = Double.parseDouble(lastEntry[3]);
                    }
                    adjustment = ticket_price - payout
                            - commission;
                    adjustment = Math.round(adjustment * 100) / 100.0d;
                    lastEntry[4] = String.valueOf(adjustment * (-1));
                } else {
                    lastOrderNumber = entry[1];
                    lastEntry = entry;
                    sellerPaymentList.add(entry);
                }
            }
        }
    }
    
    private static Date getDate(JsonNode obj) {
        SimpleDateFormat sf = new SimpleDateFormat(MCIRequestUtil.DATE_FORMAT_WITH_TIME_ZONE);
        sf.setTimeZone(MCIRequestUtil.UTC_TIME_ZONE);
        try {
            return sf.parse(obj.asText());
        }
        catch (ParseException e) {
            return null;
        }
    }
    
}


