package com.stubhub.domain.account.adapter;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;
import java.util.function.Predicate;

import com.stubhub.domain.account.intf.*;
import com.stubhub.domain.account.util.ThreadLocalUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.common.SolrDocument;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stubhub.domain.account.biz.impl.MCIRequestUtil;
import com.stubhub.domain.account.common.enums.DeliveryMethod;
import com.stubhub.domain.account.common.enums.DeliveryOption;
import com.stubhub.domain.account.common.enums.ListingStatus;
import com.stubhub.domain.account.common.enums.OrderStatus;
import com.stubhub.domain.account.common.enums.SaleMethod;
import com.stubhub.domain.account.common.enums.SaleStatus;
import com.stubhub.domain.account.common.enums.SalesSubStatus;
import com.stubhub.domain.account.common.enums.SplitOption;
import com.stubhub.domain.account.common.enums.TicketTrait;
import com.stubhub.domain.account.common.enums.UserType;
import com.stubhub.domain.account.common.util.ValidationUtil;
import com.stubhub.domain.account.dto.MyOrderResponseDTO;
import com.stubhub.domain.account.intf.MyOrderResponse.Seat;
import com.stubhub.domain.account.mapper.DeliveryOptionMapper;
import com.stubhub.domain.account.mapper.RowMeta;
import com.stubhub.domain.account.mapper.SeatsMapper;
import com.stubhub.domain.account.mapper.TicketTraitMapper;
import com.stubhub.newplatform.common.entity.Money;
import com.stubhub.newplatform.common.util.DateUtil;
import com.stubhub.newplatform.property.MasterStubHubProperties;

public class AccountResponseJsonAdapter {
	private final static Logger log = LoggerFactory.getLogger(AccountResponseJsonAdapter.class);

	private static final String DATE_FORMAT_WITHOUT_TIME_ZONE = "yyyy-MM-dd'T'HH:mm:ss";
	
	private static final int BUYER_SUPPLYMENT_TYPE_ATTENDEE = 1;
	private static final int BUYER_SUPPLYMENT_TYPE_LOCAL_ADDRESS = 2;

	private static final int BUYER_SUPPLYMENT_NOT_COMPLETE = 0;
	private static final int BUYER_SUPPLYMENT_COMPLETE = 1;
	
	private static final int SALES_TAX_PAID = 1;

	private static final Map<String, String> eventStatusMap = new HashMap<String, String>();
	static {
		eventStatusMap.put("dataentry", "1");
		eventStatusMap.put("active", "2");
		eventStatusMap.put("contingent", "3");
		eventStatusMap.put("cancelled", "4");
		eventStatusMap.put("completed", "5");
		eventStatusMap.put("postponed", "6");
		eventStatusMap.put("scheduled", "7");
	}

	public static String convertEventStatusNameToId(String status) {
		return eventStatusMap.get(status);
	}

	public static String convertToShortUTCDateFormat(String utcDate) {
		if (utcDate == null || utcDate.length() < 19) {
			return utcDate;
		}
		return utcDate.substring(0, 19) + "Z";
	}

	public static OrdersResponse addSubbedOrderInfo(OrdersResponse ordersResponse, JsonNode jsonNode) {
		if (jsonNode == null || !jsonNode.isObject()) {
			return ordersResponse;
		}

		JsonNode responseNode = jsonNode.get("response");
		if (!notNull(responseNode)) {
			return ordersResponse;
		}

		ArrayNode docsNode = (ArrayNode) responseNode.get("docs");
		if (notNull(docsNode)) {
			List<CSOrderDetailsResponse> list = ordersResponse.getOrder();
			for (CSOrderDetailsResponse csOrderDetailsResponse : list) {
				for (int i = 0; i < docsNode.size(); i++) {
					ObjectNode docNode = (ObjectNode) docsNode.get(i);
					JsonNode subbedTidNode = docNode.get("subbedTid");
					JsonNode tidNode = docNode.get("id");
					if (notNull(subbedTidNode) && notNull(tidNode)) {
						if (csOrderDetailsResponse.getTransaction().getOrderId().equals(subbedTidNode.asText())) {
							csOrderDetailsResponse.getSubs().setSubbedOrderId(tidNode.asText());
							csOrderDetailsResponse.getSubs().setSubbedFlag(true);
						}
					}
				}
			}
		}

		return ordersResponse;
	}

	public static OrdersResponse convertJsonNodeToOrdersResponse(JsonNode jsonNode, String maxRows, UserType userType)
			throws ParseException {

		OrdersResponse ordersResponse = new OrdersResponse();
		List<CSOrderDetailsResponse> list = new ArrayList<CSOrderDetailsResponse>();
		ordersResponse.setOrder(list);

		if (jsonNode == null || !jsonNode.isObject()) {
			return ordersResponse;
		}

		JsonNode responseNode = jsonNode.get("response");
		if (!notNull(responseNode)) {
			return ordersResponse;
		}

		JsonNode numFoundNode = responseNode.get("numFound");
		int ordersFound = 0;
		if (notNull(numFoundNode)) {
			ordersFound = numFoundNode.asInt();
		}
		ArrayNode docsNode = (ArrayNode) responseNode.get("docs");

		ordersResponse.setOrdersFound(ordersFound);
		String row = maxRows;
		if (row == null) {
      row = MasterStubHubProperties.getProperty("cs.siebel.default.rows", "4");
    }
		int rowsToDisplay = Math.min(Integer.parseInt(row), ordersFound);
		for (int i = 0; i < rowsToDisplay; i++) {
			ObjectNode docNode = (ObjectNode) docsNode.get(i);
			CSOrderDetailsResponse orderResponse = new CSOrderDetailsResponse();
			TransactionResponse transactionResponse = new TransactionResponse();
			JsonNode obj = docNode.get("id");
			if (notNull(obj)) {
				transactionResponse.setOrderId(obj.asText());
			}
			String currency = "USD";
			obj = docNode.get("currencyCode");
			if (notNull(obj)) {
				currency = obj.asText();
			}
			obj = docNode.get("ticketId");
			if (notNull(obj)) {
				transactionResponse.setListingId(obj.asText());
			}
			obj = docNode.get("totalCost");
			if (notNull(obj)) {
				transactionResponse.setTotalCost(new Money(obj.asText(), currency));
			}
			obj = docNode.get("quantity");
			if (notNull(obj)) {
				transactionResponse.setQuantityPurchased(obj.asText());
			}
			obj = docNode.get("section");
			if (notNull(obj)) {
				transactionResponse.setSection(obj.asText());
			}
			obj = docNode.get("rowDesc");
			if (notNull(obj)) {
				transactionResponse.setRow(obj.asText());
			}
			obj = docNode.get("seats");
			if (notNull(obj)) {
				if (transactionResponse.getRow() != null && transactionResponse.getRow().contains(",")) {
					String[] rows = transactionResponse.getRow().split(",");
					String seats = (obj.asText()).replaceAll(rows[0] + "-", "");
					seats = (seats).replaceAll(rows[1] + "-", "");
					transactionResponse.setSeats(seats);
				} else {
					transactionResponse.setSeats(obj.asText());
				}
			}
			obj = docNode.get("cancelled");
			if (notNull(obj)) {
				transactionResponse.setCancelled("1".equals(obj.asText()));
			}
			obj = docNode.get("transactionDate");
			if (notNull(obj)) {
				transactionResponse.setSaleDateUTC(convertToShortUTCDateFormat(obj.asText()));
			}
			EventResponse eventResponse = new EventResponse();
			obj = docNode.get("eventId");
			if (notNull(obj)) {
				eventResponse.setEventId(obj.asText());
			}
			obj = docNode.get("eventNameSort");
			if (notNull(obj)) {
				eventResponse.setEventDescription(obj.asText());
			}
			obj = docNode.get("eventStatus");
			if (notNull(obj)) {
				eventResponse.setEventStatus(convertEventStatusNameToId(obj.asText().toLowerCase()));
			}
			obj = docNode.get("eventDate");
			if (notNull(obj)) {
				eventResponse.setEventDateUTC(convertToShortUTCDateFormat(obj.asText()));
			}
			obj = docNode.get("eventDateLocal");
			if (notNull(obj)) {
				eventResponse.setEventDateLocal(convertToShortUTCDateFormat(obj.asText()));
			}
			obj = docNode.get("hideEventDate");
			if (notNull(obj)) {
				eventResponse.setHideEventDate(obj.asBoolean());
			}
			obj = docNode.get("venueNameSort");
			if (notNull(obj)) {
				eventResponse.setVenueDescription(obj.asText());
			}
			DeliveryResponse deliveryResponse = new DeliveryResponse();
			int deliveryMethodId = 0;
			obj = docNode.get("deliveryMethodId");
			if (notNull(obj)) {
				deliveryResponse.setDeliveryMethodId(obj.asText());
				deliveryMethodId = Integer.parseInt(obj.asText());
				deliveryResponse.setDeliveryMethodDescription(
						DeliveryMethod.getDeliveryMethodById(deliveryMethodId).getDeliveryMethodDesc());
				deliveryResponse.setDeliveryTypeId(
						String.valueOf(DeliveryMethod.getDeliveryMethodById(deliveryMethodId).getDeliveryTypeId()));
				deliveryResponse.setDeliveryTypeDescription(
						DeliveryMethod.getDeliveryMethodById(deliveryMethodId).getDeliveryTypeDesc());
			}
			obj = docNode.get("expectedDeliveryDate");
			if (notNull(obj)) {
				deliveryResponse.setExpectedArrivalDateUTC(convertToShortUTCDateFormat(obj.asText()));
			}
			obj = docNode.get("shipDate");
			if (notNull(obj)) {
				deliveryResponse.setShipDateUTC(convertToShortUTCDateFormat(obj.asText()));
			}
			obj = docNode.get("inhandInd");
			if (notNull(obj)) {
				deliveryResponse.setNotInHand(!"1".equals(obj.asText()));
			}
			obj = docNode.get("fedexTrackingNumber");
			if (notNull(obj)) {
				deliveryResponse.setTrackingNumber(obj.asText());
			}
			obj = docNode.get("orderProcSubStatusCode");
			if (notNull(obj)) {
				deliveryResponse.setOrderProcSubStatusCode(obj.asText());
			}
			obj = docNode.get("orderProcSubStatusDesc");
			if (notNull(obj)) {
				deliveryResponse.setOrderProcSubStatusDesc(obj.asText());
			}
			if (UserType.BUYER == userType) {
				SubResponse subResponse = new SubResponse();
				obj = docNode.get("ticketCost");
				if (notNull(obj)) {
					transactionResponse.setPricePerTicket(new Money(obj.asText(), currency));
				}
				obj = docNode.get("buyerId");
				if (notNull(obj)) {
					transactionResponse.setBuyerId(obj.asText());
				}
				obj = docNode.get("buyerShippingContactId");
				if (notNull(obj)) {
					transactionResponse.setBuyerContactId(obj.asText());
				}
				obj = docNode.get("inhandDate");
				if (notNull(obj)) {
					deliveryResponse.setInHandDateUTC(convertToShortUTCDateFormat(obj.asText()));
				}
				subResponse.setSubbedFlag(false);
				orderResponse.setSubs(subResponse);
			}
			if (UserType.SELLER == userType) {
				SellerPayments sellerPayments = new SellerPayments();
				obj = docNode.get("sellerId");
				if (notNull(obj)) {
					transactionResponse.setSellerId(obj.asText());
				}
				obj = docNode.get("confirmedInd");
				if (notNull(obj)) {
					transactionResponse.setSellerConfirmed("1".equals(obj.asText()));
				}
				obj = docNode.get("sellerPayoutAmount");
				if (notNull(obj)) {
					transactionResponse.setSellerPayoutAmount(new Money(obj.asText(), currency));
				}
				obj = docNode.get("sellerPaymentTypeId");
				if (notNull(obj)) {
					transactionResponse.setSellerPaymentTypeId(obj.asText());
				}
				orderResponse.setSellerPayments(sellerPayments);
			}
			orderResponse.setTransaction(transactionResponse);
			orderResponse.setEvent(eventResponse);
			orderResponse.setDelivery(deliveryResponse);
			list.add(orderResponse);
		}
		return ordersResponse;
	}

	/**
	 * This method converts the QueryResponse to OrderResponse objects
	 * 
	 * @param jsonNode
	 * @return SalesResponse
	 */
	public static SalesResponse convertJsonNodeToSalesResponse(JsonNode jsonNode) {
		SalesResponse salesResponse = new SalesResponse();
		salesResponse.setSales(new ArrayList<SaleResponse>());
		if (jsonNode == null || !jsonNode.isObject()) {
			return salesResponse;
		}
		JsonNode responseNode = jsonNode.get("response");
		if (!notNull(responseNode)) {
			return salesResponse;
		}
		JsonNode numFoundNode = responseNode.get("numFound");
		if (notNull(numFoundNode)) {
			salesResponse.setNumFound(numFoundNode.asLong());
		} else {
			salesResponse.setNumFound(0L);
		}
		ArrayNode docsNode = (ArrayNode) responseNode.get("docs");
		if (notNull(docsNode)) {
			int size = docsNode.size();
			if (size <= 0) {
				return salesResponse;
			}
			for (int i = 0; i < size; i++) {
				ObjectNode docNode = (ObjectNode) docsNode.get(i);
				SaleResponse saleResponse = new SaleResponse();
				JsonNode obj = docNode.get("id");
				if (notNull(obj)) {
					saleResponse.setSaleId(obj.asText());
				}
				obj = docNode.get("orderProcStatusId");
				if (notNull(obj)) {
					JsonNode obj1 = docNode.get("orderProcSubStatusCode");
					if (notNull(obj1)) {
						saleResponse.setStatus(SaleStatus.fromNumber(obj.asLong(), obj1.asLong()));
					}
				}

				obj = docNode.get("newUrlTransInd");
				if(notNull(obj)){
					saleResponse.setDeliveriesFlag(obj.asLong());
				}

				obj = docNode.get("saleUrlTransInd");
				if(notNull(obj)&&obj.asInt()>=0){//per testing,if saleUrlTransInd, actual return value is -1
					saleResponse.setUrlTransInd(obj.asBoolean());
				}

				obj = docNode.get("transEmailAddr");
				if(notNull(obj)){
					saleResponse.setTransEmailAddr(obj.asText());
				}

				obj = docNode.get("quantity");
				if (notNull(obj)) {
					saleResponse.setQuantity(obj.asInt());
				}
				String currency = "USD";
				obj = docNode.get("currencyCode");
				if (notNull(obj)) {
					currency = obj.asText();
				}
				obj = docNode.get("pricePerTicket");
				if (notNull(obj)) {
					saleResponse.setPricePerTicket(convertToMoney(obj.asText(), currency));
				}

				obj = docNode.get("sellerPayoutAmount");
				if (notNull(obj)) {
					saleResponse.setTotalPayout(convertToMoney(obj.asText(), currency));
				}
				if (notNull(obj) && saleResponse.getQuantity() != null) {
					Float sellerPayoutPerTicket = (float) (obj.asDouble() / saleResponse.getQuantity());
					saleResponse.setPayoutPerTicket(convertToMoney(sellerPayoutPerTicket.toString(), currency));
				}
				String fulfillmentMethod = null;
				obj = docNode.get("fulfillmentMethodId");
				if (notNull(obj)) {
					fulfillmentMethod = obj.asText();
					Integer fulfillmentMethodId = Integer.valueOf(fulfillmentMethod);
					saleResponse.setFulfillmentMethodId(fulfillmentMethodId);
				}
				
				String deliveryMethod = null;
				obj = docNode.get("deliveryMethodId");
				if (notNull(obj)) {
					deliveryMethod = obj.asText();
					if(!StringUtils.isBlank(deliveryMethod) && StringUtils.isNumeric(deliveryMethod.trim())) {
						Integer deliveryMethodId = Integer.valueOf(deliveryMethod.trim());
						saleResponse.setDeliveryMethodId(deliveryMethodId);
					}
				}

				obj = docNode.get("deliveryOptionId");
				int deliveryOptionId = 0;
				int ticketMediumId = 0;
				int lmsStatus = 0;
                Boolean preDelivery = null;
				if (notNull(obj)) {
					deliveryOptionId = obj.asInt();
                    preDelivery = deliveryOptionId == 1 ? true : false;
				}

				obj = docNode.get("ticketMediumId");
				if (notNull(obj)) {
					ticketMediumId = obj.asInt();
					saleResponse.setTicketMediumId(ticketMediumId);
				}

				obj = docNode.get("lmsApprovalStatusId");
				if (notNull(obj)) {
					lmsStatus = obj.asInt();
				}

				saleResponse.setDeliveryOption(AccountResponseAdapter.getDeliveryOption(ticketMediumId,
						deliveryOptionId, lmsStatus, fulfillmentMethod));
				saleResponse.setPreDelivery(preDelivery);

				obj = docNode.get("courierAccountId");
				if (notNull(obj)) {
					saleResponse.setCourierAccountId(obj.asLong());
				}

				obj = docNode.get("trackingUrl");
				if (notNull(obj)) {
					saleResponse.setTrackingUrl(obj.asText());
				}

				obj = docNode.get("courierId");
				if (notNull(obj)) {
					saleResponse.setCourierId(obj.asLong());
				}

				obj = docNode.get("courierName");
				if (notNull(obj)) {
					saleResponse.setCourierName(obj.asText());
				}

				obj = docNode.get("courierSmallLogo");
				if (notNull(obj)) {
					saleResponse.setCourierSmallLogo(obj.asText());
				}

				obj = docNode.get("jdkTimezone");
				TimeZone tz = MCIRequestUtil.UTC_TIME_ZONE;
				if (notNull(obj)) {
					tz = TimeZone.getTimeZone(obj.asText());
					saleResponse.setEventTimeZone(tz.getDisplayName());
				}

				// Fix for MYA-619
				obj = docNode.get("deliveriesInhandDate");
				if (!notNull(obj)) {
					obj = docNode.get("inhandDate");
				}

				if (notNull(obj)) {
					saleResponse.setInhandDate(utc2localDateTimeFormat(obj.asText(), tz));
					saleResponse.setInhandDatePST(
									utc2localDateTimeFormat(obj.asText(), TimeZone.getTimeZone("PST")));
				}

				obj = docNode.get("earliestPossibleInhandDate");
				if (notNull(obj)) {
					String dateStr = obj.asText();
					saleResponse.setEarliestInhandDate(utc2localDateTimeFormat(dateStr, tz));
					Date date = parseStringToDate(dateStr, tz);
					if (date != null) {
						saleResponse.seteInhandDateLaterThanNow(date.after(new Date()) ? true : false);
					}
				}

				obj = docNode.get("sellerContactId");
				if (notNull(obj)) {
					saleResponse.setSellerContactId(obj.asLong());
				}

				obj = docNode.get("transactionDate");
				if (notNull(obj)) {
					saleResponse.setSaleDate(utc2localDateTimeFormat(obj.asText(), tz));
				}
				obj = docNode.get("dateLastModified");
				if (notNull(obj)){
					saleResponse.setDateLastModified(utc2localDateTimeFormat(obj.asText(), tz));
				}
				obj = docNode.get("ticketId");
				if (notNull(obj)) {
					saleResponse.setListingId(obj.asText());
				}

				obj = docNode.get("externalListingId");
				if (notNull(obj)) {
					saleResponse.setExternalListingId(obj.asText());
				}

				obj = docNode.get("section");
				if (notNull(obj)) {
					saleResponse.setSection(obj.asText());
				}
				obj = docNode.get("venueConfigSectionsId");
				if (notNull(obj)) {
					saleResponse.setSectionId(obj.asLong());
				}
				obj = docNode.get("rowDesc");
				if (notNull(obj)) {
					saleResponse.setRows(obj.asText());
					saleResponse.setRow(obj.asText());
				}

				obj = docNode.get("seats");
				if (notNull(obj)) {
					saleResponse.setSeats(obj.asText());
				}
				obj = docNode.get("seatTraits");
				if (notNull(obj)) {
					ArrayNode arrayNode = (ArrayNode) obj;
					if (arrayNode.size() > 0) {
						String seatTrait = arrayNode.get(0).asText();
						Set<TicketTrait> ticketTraits = AccountResponseAdapter.convertToTicketTraits(seatTrait);
						if (ticketTraits != null) {
							saleResponse.setTicketTraits(ticketTraits);
						}
					}
				}
				obj = docNode.get("sellerNotes");
				if (notNull(obj)) {
					saleResponse.setInternalNotes(obj.asText());
				}
				obj = docNode.get("eventNameSort");
				if (notNull(obj)) {
					saleResponse.setEventDescription(obj.asText());
				}
				obj = docNode.get("eventDate");
				if (notNull(obj)) {
					saleResponse.setEventDate(utc2timeZoneDateTimeFormat(obj.asText(), tz));
				}
				obj = docNode.get("hideEventDate");
				if (notNull(obj)) {
					saleResponse.setHideEventDate(Boolean.valueOf(obj.asText()));
				}
				obj = docNode.get("hideEventTime");
				if (notNull(obj)) {
					saleResponse.setHideEventTime(Boolean.valueOf(obj.asText()));
				}
				obj = docNode.get("eventId");
				if (notNull(obj)) {
					saleResponse.setEventId(obj.asText());
				}
				obj = docNode.get("performerId");
				if (notNull(obj)) {
					saleResponse.setGenreId(obj.asText());
				} else {
					obj = docNode.get("groupingId");
					if (notNull(obj)) {
						saleResponse.setGenreId(obj.asText());
					} else {
						obj = docNode.get("categoryId");
						if (notNull(obj)) {
							saleResponse.setGenreId(obj.asText());
						}
					}
				}
				obj = docNode.get("venueNameSort");
				if (notNull(obj)) {
					saleResponse.setVenueDescription(obj.asText());
				}
				obj = docNode.get("sellerPaymentTypeId");
				if (notNull(obj)) {
					saleResponse.setPaymentType(obj.asText());
				}
				obj = docNode.get("inhandInd");
				if (notNull(obj)) {
					saleResponse.setInHand(AccountResponseAdapter.convertToBoolean(obj.asText()));
				}

				obj = docNode.get("fedexTrackingNumber");
				if (notNull(obj)) {
					saleResponse.setTrackingNumber(obj.asText());
				}

				saleResponse.setSubStatus(convertToSalesSubStatus(docNode));

				obj = docNode.get("city");
				if (notNull(obj)) {
					saleResponse.setCity(obj.asText());
				}

				obj = docNode.get("state");
				if (notNull(obj)) {
					saleResponse.setState(obj.asText());
				}

				obj = docNode.get("country");
				if (notNull(obj)) {
					saleResponse.setCountry(obj.asText());
				}

				obj = docNode.get("deliveryStatus");
				if (notNull(obj)) {
					saleResponse.setUrlPartialTransferred("MOBILE_URL_PARTIAL_TRANSFERED".equalsIgnoreCase(obj.asText()));
				}

				obj = docNode.get("buyerId");
				if (notNull(obj)) {
					saleResponse.setBuyerId(obj.asText());
				}

				JsonNode buyerSupplymentType = docNode.get("buyerSupplymentTypeList");
				JsonNode buyerSupplymentCompleted = docNode.get("buyerSupplymentCompletedList");

				if (notNull(buyerSupplymentType) && notNull(buyerSupplymentCompleted)) {
					ArrayNode buyerSupplymentTypeNodes = (ArrayNode) buyerSupplymentType;
					ArrayNode buyerSupplymentCompletedNodes = (ArrayNode) buyerSupplymentCompleted;

					for (int j = 0; j < buyerSupplymentTypeNodes.size(); j++) {
						JsonNode buyerSupplymentTypeNode = buyerSupplymentTypeNodes.get(j);
						JsonNode buyerSupplymentCompletedNode = buyerSupplymentCompletedNodes.get(j);

						if (BUYER_SUPPLYMENT_TYPE_ATTENDEE == buyerSupplymentTypeNode.asInt()) {
							if (BUYER_SUPPLYMENT_NOT_COMPLETE == buyerSupplymentCompletedNode.asInt()) {
								saleResponse.setAttendeeCompleted(false);
							} else if (BUYER_SUPPLYMENT_COMPLETE == buyerSupplymentCompletedNode.asInt()) {
								saleResponse.setAttendeeCompleted(true);
							}
						} else if (BUYER_SUPPLYMENT_TYPE_LOCAL_ADDRESS == buyerSupplymentTypeNode.asInt()) {
							if (BUYER_SUPPLYMENT_NOT_COMPLETE == buyerSupplymentCompletedNode.asInt()) {
								saleResponse.setLocalAddressCompleted(false);
							} else if (BUYER_SUPPLYMENT_COMPLETE == buyerSupplymentCompletedNode.asInt()) {
								saleResponse.setLocalAddressCompleted(true);
							}
						}
					}
				}

				obj = docNode.get("gaInd");
				if (notNull(obj)) {
					saleResponse.setGA(obj.asText().contains("1"));
				}

				// delivery proof needed
				JsonNode obj1 = docNode.get("orderProcSubStatusCode");
				if (notNull(obj1)) {
						if (Arrays.asList(52L, 56L, 57L).contains(obj1.asLong())) {
								List<Link> links = new ArrayList<Link>();
								Link downloadTemplate = new Link();
								downloadTemplate.setRel("sale.downloadProofTemplate");
								downloadTemplate.setUri("/shape/fulfillment/delivery/v1/sale/"+saleResponse.getSaleId()+"/deliveryReceipt");
								Link uploadProof = new Link();
								uploadProof.setRel("sale.uploadProof");
								uploadProof.setUri("/shape/fulfillment/delivery/v1/sale/"+saleResponse.getSaleId()+"/confirmation");
								links.add(downloadTemplate);
								links.add(uploadProof);
								saleResponse.setLinks(links);
						}
					if (obj1.asLong() == 55l) {
						List<Link> links = new ArrayList<Link>();
						Link downloadProof = new Link();
						downloadProof.setRel("sale.downloadProof");
						downloadProof.setUri("/shape/fulfillment/delivery/v1/sale/"+saleResponse.getSaleId()+"/confirmation");
						links.add(downloadProof);
						saleResponse.setLinks(links);
					}
				}

				if (StringUtils.isNotEmpty(saleResponse.getSaleId())) {
					salesResponse.getSales().add(saleResponse);
				}
			}
		}

		JsonNode aggregationsNode = responseNode.get("aggregations");

		if (notNull(aggregationsNode)) {
			// Venue
			JsonNode facetJsonNode = aggregationsNode.get("venueIdNameFacet");
			if (notNull(facetJsonNode)) {
				salesResponse.setVenueSummary(convertToSummaryList(facetJsonNode, 1));
			}
			// Genre
			facetJsonNode = aggregationsNode.get("performerIdNameFacet");
			if (notNull(facetJsonNode)) {
				salesResponse.setGenreSummary(convertToSummaryList(facetJsonNode, 2));
			}

			// Event
			facetJsonNode = aggregationsNode.get("eventIdNameFacet");
			if (notNull(facetJsonNode)) {
				salesResponse.setEventSummary(convertToSummaryList(facetJsonNode, 1));
			}
		}
		return salesResponse;
	}

	/**
	 * This method converts the QueryResponse to ListingResponse objects
	 *
	 * @param jsonNode
	 * @return ListingResponse
	 */
	public static ListingsResponse convertJsonNodeToListingsResponse(JsonNode jsonNode) {
		ListingsResponse listingsResponse = new ListingsResponse();
		listingsResponse.setListings(new ArrayList<ListingResponse>());
		if (jsonNode == null || !jsonNode.isObject()) {
			return listingsResponse;
		}
		JsonNode responseNode = jsonNode.get("response");
		if (!notNull(responseNode)) {
			return listingsResponse;
		}
		JsonNode numFoundNode = responseNode.get("numFound");
		if (notNull(numFoundNode)) {
			listingsResponse.setNumFound(numFoundNode.asLong());
		} else {
			listingsResponse.setNumFound(0L);
		}
		ArrayNode docsNode = (ArrayNode) responseNode.get("docs");
		if (notNull(docsNode)) {
			int size = docsNode.size();
			if (size <= 0) {
				return listingsResponse;
			}
			for (int i = 0; i < size; i++) {
				ObjectNode docNode = (ObjectNode) docsNode.get(i);
				ListingResponse listingResponse = new ListingResponse();
				JsonNode obj = docNode.get("id");
				if (notNull(obj)) {
					listingResponse.setId(obj.asText());
				}

				obj = docNode.get("ticketSystemStatus");
				if (notNull(obj)) {
					convertToListingStatus(listingResponse, obj.asText());
				}

				obj = docNode.get("externalListingId");
				if (notNull(obj)) {
					listingResponse.setExternalListingId(obj.asText());
				}

				obj = docNode.get("section");
				if (notNull(obj)) {
					listingResponse.setSection(obj.asText());
				}

				obj = docNode.get("venueConfigSectionsId");
				if (notNull(obj)) {
					listingResponse.setVenueConfigSectionsId(obj.asLong());
				}

				obj = docNode.get("rowDesc");
				if (notNull(obj)) {
					listingResponse.setRows(obj.asText());
				}

				obj = docNode.get("seats");

				if (notNull(obj)) {
					listingResponse.setSeats(obj.asText());
					if (StringUtils.contains(listingResponse.getRows(), ",")) {
						String[] rows = listingResponse.getRows().split(",");
						if (rows.length >= 2) {
							String seats = (obj.asText()).replaceAll(rows[0] + "-", "");
							seats = (seats).replaceAll(rows[1] + "-", "");
							listingResponse.setSeats(seats);
						}
					}
				}

				listingResponse.setGA(ValidationUtil.isGA(listingResponse.getSection(), listingResponse.getRows(),
						listingResponse.getSeats()));

				obj = docNode.get("quantityRemain");
				if (notNull(obj)) {
					listingResponse.setQuantityRemain(obj.asInt());
				}

				obj = docNode.get("quantity");
				if (notNull(obj)) {
					listingResponse.setQuantity(obj.asInt());
				}

				obj = docNode.get("sellerCcId");
				if (notNull(obj)) {
					listingResponse.setCcId(obj.asLong());
				}

				obj = docNode.get("saleMethodId");
				if (notNull(obj)) {
					listingResponse.setSaleMethod(SaleMethod.getSaleMethod(Long.valueOf(obj.asInt())));
				}

				String currency = "USD";
				obj = docNode.get("currencyCode");
				if (notNull(obj)) {
					currency = obj.asText();
				}

//				String faceCurrency = currency;
//				obj = docNode.get("faceCurrencyCode");
//				if (notNull(obj)) {
//					faceCurrency = obj.asText();
//				}

				obj = docNode.get("faceValue");
				if (notNull(obj)) {
					listingResponse.setFaceValue(convertToMoney(obj.asText(), currency));
				}

				obj = docNode.get("ticketPrice");
				if (notNull(obj)) {
					listingResponse.setPricePerTicket(convertToMoney(obj.asText(), currency));
				}

				obj = docNode.get("sellerPayoutAmtPerTicket");
				if (notNull(obj)) {
					listingResponse.setPayoutPerTicket(convertToMoney(obj.asText(), currency));
				}

				obj = docNode.get("sellerPurchasePrice");
				if (notNull(obj)) {
					listingResponse.setPurchasePrice(convertToMoney(obj.asText(), currency));
				}
        
        //Changes for SELLAPI-3984
        String sellerInputCurrency ="USD";
				obj = docNode.get("sellerInputCurrencyCode");
				if (notNull(obj)) {
					sellerInputCurrency=obj.asText();
				}
				obj = docNode.get("sellerInputPrice");
				if (notNull(obj)) {
					listingResponse.setSellerInputPrice(convertToMoney(obj.asText(), sellerInputCurrency));	
				}
				obj = docNode.get("sellerInputPriceType");
				if (notNull(obj)) {
					listingResponse.setSellerInputPriceType(obj.asText());
				}
        
				List<String> fulfillmentMethods = new ArrayList<String>();
				obj = docNode.get("fulfillmentMethodId");
				String fulfillmentMethodStr;
				
				if (notNull(obj)) {
					ArrayNode objArr = (ArrayNode) obj;
					List<FulfillmentMethod> fmLst = new ArrayList<FulfillmentMethod>();
					if (objArr.size() > 0) {
						Iterator<JsonNode> it = objArr.iterator();
						while (it.hasNext()) {
							fulfillmentMethodStr = it.next().asText();
							fulfillmentMethods.add(fulfillmentMethodStr);
							
							if(!StringUtils.isBlank(fulfillmentMethodStr) && StringUtils.isNumeric(fulfillmentMethodStr.trim())) {
								FulfillmentMethod fm = new FulfillmentMethod();
								fm.setFulfillmentMethodId(Long.parseLong(fulfillmentMethodStr));
								fmLst.add(fm);
							}
						}

					}
					listingResponse.setFulfillmentMethod(fmLst);
				}

				int deliveryOptionId = 0;
				int ticketMediumId = 0;
				int autoPricingEnabledInd = 0;
				int lmsStatus = 0;

				obj = docNode.get("deliveryOptionId");
				if (notNull(obj)) {
					deliveryOptionId = obj.asInt();
					if (deliveryOptionId == com.stubhub.domain.account.datamodel.entity.DeliveryOption.PREDELIVERY
							.getValue()) {
						listingResponse.setPreDelivered(true);
					} else {
						listingResponse.setPreDelivered(false);
					}
				}

				obj = docNode.get("ticketMedium");
				if (notNull(obj)) {
					ticketMediumId = obj.asInt();
					listingResponse.setTicketMediumId(ticketMediumId);
				}

				obj = docNode.get("autoPricingEnabledInd");
				if (notNull(obj)) {
					autoPricingEnabledInd = obj.asInt();					
				} 
				listingResponse.setAutoPricingEnabledInd(autoPricingEnabledInd);

				obj = docNode.get("lmsApprovalStatusId");
				if (notNull(obj)) {
					lmsStatus = obj.asInt();
					listingResponse.setLmsApprovalStatus(lmsStatus);
				}

				listingResponse.setDeliveryOption(AccountResponseAdapter.getDeliveryOption(ticketMediumId,
						deliveryOptionId, lmsStatus, fulfillmentMethods, listingResponse.getId()));

				// new logic for request LMS
				// Ticket medium id =1 and LMS approval is null and fm_dm_list
				// contains 9,17 or 9,12.
				List<String> deliveryMethodIds = new ArrayList<String>();
				obj = docNode.get("deliveryMethodId");
				String deliveryMethodStr;
				
				if (notNull(obj)) {
					ArrayNode objArr = (ArrayNode) obj;
					List<com.stubhub.domain.account.intf.DeliveryMethod> dmLst = 
							new ArrayList<com.stubhub.domain.account.intf.DeliveryMethod>();
					
					if (objArr.size() > 0) {
						Iterator<JsonNode> it = objArr.iterator();
						while (it.hasNext()) {
							deliveryMethodStr = it.next().asText();
							deliveryMethodIds.add(deliveryMethodStr);
							
							if(!StringUtils.isBlank(deliveryMethodStr) && StringUtils.isNumeric(deliveryMethodStr.trim())) {
								com.stubhub.domain.account.intf.DeliveryMethod dm = new com.stubhub.domain.account.intf.DeliveryMethod();
								dm.setDeliveryMethodId(Long.parseLong(deliveryMethodStr));
								dmLst.add(dm);
							}
						}

					}
					listingResponse.setDeliveryMethod(dmLst);
				}

//				if(ticketMediumId == 1
//						&& lmsStatus == 0
//						&& fulfillmentMethods.contains(FULFILLMENT_METHOD_LMS_PREDELIVERY)
//						&& (deliveryMethodIds.contains(DELIVERY_METHOD_PICK_UP) || deliveryMethodIds.contains(DELIVERY_METHOD_PICK_UP_EVENT_DAY))){
//					listingResponse.setAbleToRequestLMS(true);
//				}else{
//					listingResponse.setAbleToRequestLMS(false);
//				}

			//	 If it's paper ticket and haven't request LMS, set ableToRequestLMS to true,
			//	 but it doesn't mean it can request successfully as there could be no LMS-pre window to the event.
			//	 The definition of field is not like what is in old mci as mci cloud doesn't provide the accurate logistic info any more
				if(ticketMediumId == 1 && lmsStatus == 0){
					listingResponse.setAbleToRequestLMS(true);
				}else{
					listingResponse.setAbleToRequestLMS(false);
				}

				obj = docNode.get("jdkTimezone");
				TimeZone tz = MCIRequestUtil.UTC_TIME_ZONE;
				if (notNull(obj)) {
					tz = TimeZone.getTimeZone(obj.asText());
				}

				obj = docNode.get("expectedInhandDate");
				if (notNull(obj)) {
                    listingResponse.setInhandDate(utc2timeZoneDateTimeFormat(obj.asText(), tz));
				}

				obj = docNode.get("earliestPossibleInhandDate");
				if (notNull(obj)) {
					String dateStr = obj.asText();
                    listingResponse.setEarliestInhandDate(utc2localDateTimeFormat(dateStr, tz));
					Date date = parseStringToDate(dateStr, tz);
					if (date != null) {
						listingResponse.seteInhandDateLaterThanNow(
								date.after(DateUtil.getNowCalUTC().getTime()) ? true : false);
					}
				}

				obj = docNode.get("dateLastModified");
				if (notNull(obj)) {
					listingResponse.setDateLastModified(utc2timeZoneDateTimeFormat(obj.asText(), tz));
				}

				obj = docNode.get("saleEndDate");
				if (notNull(obj)) {
                    listingResponse.setSaleEndDate(utc2timeZoneDateTimeFormat(obj.asText(), tz));
				}

				obj = docNode.get("split");
				if (notNull(obj)) {
					listingResponse.setSplitQuantity(obj.asInt());
				}

				obj = docNode.get("splitOption");
				if (notNull(obj)) {
					listingResponse.setSplitOption(SplitOption.fromString(obj.asText()));
				}
				if (listingResponse.getSplitQuantity() == null) {
					listingResponse.setSplitQuantity(0);
				}

				obj = docNode.get("seatTraits");
				if (notNull(obj)) {
					ArrayNode arrayNode = (ArrayNode) obj;
					if (arrayNode.size() > 0) {
						String seatTrait = arrayNode.get(0).asText();
						Set<TicketTrait> ticketTraits = AccountResponseAdapter.convertToTicketTraits(seatTrait);
						if (ticketTraits != null) {
							listingResponse.setTicketTraits(ticketTraits);
						}
					}
				}

				obj = docNode.get("eventId");
				if (notNull(obj)) {
					listingResponse.setEventId(obj.asText());
				}

				obj = docNode.get("performerId");
				if (notNull(obj)) {
					listingResponse.setGenreId(obj.asText());
					listingResponse.setPerformerId(obj.asText());
				} else {
					obj = docNode.get("groupingId");
					if (notNull(obj)) {
						listingResponse.setGenreId(obj.asText());
					} else {
						obj = docNode.get("categoryId");
						if (notNull(obj)) {
							listingResponse.setGenreId(obj.asText());
						}
					}
				}

				obj = docNode.get("groupingId");
				if (notNull(obj)) {
					listingResponse.setGroupingId(obj.asText());
				}

				obj = docNode.get("categoryId");
				if (notNull(obj)) {
					listingResponse.setCategoryId(obj.asText());
 				}

				obj = docNode.get("performerIdNameFacet");
				if (notNull(obj)) {
					listingResponse.setGenreDescription(getNameFromFacetFiled(obj.asText(), 2));
				}

				String eventDate = null;
				obj = docNode.get("eventDate");
				if (notNull(obj)) {
					eventDate = obj.asText();
                    listingResponse.setEventDate(utc2timeZoneDateTimeFormat(eventDate, tz));
				}
				obj = docNode.get("eventNameSort");
				if (notNull(obj)) {
					listingResponse.setEventDescription(obj.asText());
				}

				obj = docNode.get("eventStatus");
				if (notNull(obj)) {
					listingResponse.setEventActive("active".equalsIgnoreCase(obj.asText()));
				}

				obj = docNode.get("city");
				if (notNull(obj)) {
					listingResponse.setCity(obj.asText());
				}

				obj = docNode.get("state");
				if (notNull(obj)) {
					listingResponse.setState(obj.asText());
				}

				obj = docNode.get("country");
				if (notNull(obj)) {
					listingResponse.setCountry(obj.asText());
				}

				// add event time zone
				boolean daylightTime = false;

				if (eventDate != null) {
					daylightTime = tz.inDaylightTime(parseStringToDate(eventDate, tz));
				}

				listingResponse.setEventTimeZone(tz.getDisplayName(daylightTime, 0));

				obj = docNode.get("venueNameSort");
				if (notNull(obj)) {
					listingResponse.setVenueDescription(obj.asText());
				}

				obj = docNode.get("comments");
				if (notNull(obj)) {
					listingResponse.setInternalNotes(obj.asText());
				}

				obj = docNode.get("sellerContactId");
				if (notNull(obj)) {
					listingResponse.setContactId(obj.asLong());
				}

				obj = docNode.get("sellerPaymentTypeId");
				if (notNull(obj)) {
					listingResponse.setPaymentType(obj.toString());
				}

				obj = docNode.get("displayPricePerTicket");
				if (notNull(obj)) {
					listingResponse.setDisplayPricePerTicket(convertToMoney(obj.toString(), currency));
				}
				
				//US Sales Tax align the logic with listings/v2 in services-listing-v2 ListingResponseAdapter.java SELLAPI-4262
				obj = docNode.get("purchasePrice");
				
				if (notNull(obj) && BigDecimal.valueOf(Double.valueOf(obj.toString())).compareTo(BigDecimal.ZERO) >= 0) {
					
					String purchasePricePerProduct = obj.toString();
					String purchasePriceCurrencyCode = "USD";
					obj = docNode.get("purchasePriceCurrencyCode");
					if (notNull(obj)) {
						purchasePriceCurrencyCode = obj.asText();
					}
					listingResponse.setPurchasePricePerProduct(convertToMoney(purchasePricePerProduct, purchasePriceCurrencyCode));
					
					obj = docNode.get("isSalesTaxPaid");
					if (notNull(obj)) {
						listingResponse.setSalesTaxPaid(obj.asInt() == SALES_TAX_PAID?true:false);
					}
					else
					{
						listingResponse.setSalesTaxPaid(true);
					}
				}
				
				obj = docNode.get("isScrubbingEnabled");
				Integer isScrubbingEnabled = 0;
				if (notNull(obj)) {
					isScrubbingEnabled = obj.asInt();
				}
				if (isScrubbingEnabled == 1) {
					listingResponse.setIsScrubbingEnabled(true);
				} else {
					listingResponse.setIsScrubbingEnabled(false);
				}

				Boolean isSectionScrubScheduled = false;
				obj = docNode.get("sectionScrubSchedule");
				if (notNull(obj)) {
					ArrayNode objArr = (ArrayNode) obj;
					String sectionScrubSchedule = "";
					if (notNull(objArr) && objArr.size() > 0) {
						sectionScrubSchedule = objArr.get(0).asText();
						isSectionScrubScheduled = "0".equals(sectionScrubSchedule);
					}
				}

				if (listingResponse.getVenueConfigSectionsId() == null && isScrubbingEnabled == 1
						&& isSectionScrubScheduled) {
					listingResponse.setSectionMappingRequired(true);
				} else {
					listingResponse.setSectionMappingRequired(false);
				}

				// SIP-190
				if (ThreadLocalUtil.isCallerTypeInternal()) {
					obj = docNode.get("zoneId");
					if (notNull(obj)) {
						listingResponse.setZoneId(obj.asText());
					}
					obj = docNode.get("zoneName");
					if (notNull(obj)) {
						listingResponse.setZoneName(obj.asText());
					}
					obj = docNode.get("deliveryTypeId");
					if (notNull(obj)) {
						ArrayNode arrayNode = (ArrayNode) obj;
						List<Integer> deliveryTypeIds = new ArrayList<Integer>();
						for (int idx = 0; idx < arrayNode.size(); ++idx) {
							Integer deliveryTypeId = arrayNode.get(idx).asInt();
							deliveryTypeIds.add(deliveryTypeId);
						}
						listingResponse.setDeliveryTypeIds(deliveryTypeIds);
					}
				}

				listingsResponse.getListings().add(listingResponse);
			}

		}

		JsonNode aggregationsNode = responseNode.get("aggregations");

		if (notNull(aggregationsNode)) {
			// Venue
			JsonNode facetJsonNode = aggregationsNode.get("venueIdNameFacet");
			if (notNull(facetJsonNode)) {
				listingsResponse.setVenueSummary(convertToSummaryList(facetJsonNode, 1));
			}
			// Genre
			facetJsonNode = aggregationsNode.get("performerIdNameFacet");
			if (notNull(facetJsonNode)) {
				listingsResponse.setGenreSummary(convertToSummaryList(facetJsonNode, 2));
			}

			// Event
			facetJsonNode = aggregationsNode.get("eventIdNameFacet");
			if (notNull(facetJsonNode)) {
				listingsResponse.setEventSummary(convertToSummaryList(facetJsonNode, 1));
			}
		}

		return listingsResponse;

	}

	static final SalesSubStatus convertToSalesSubStatus(ObjectNode docNode) {
		Long subStatusCode = getLongValue(docNode, "orderProcSubStatusCode");
		if (subStatusCode == null) {
			return null;
		}
		if (subStatusCode == 2L) {
			// 2 subs in process: seller
			String trackID = getStringValue(docNode, "confirmFlowTrackId");
			if ("4".equals(trackID)) {
				// 4 Seller cannot fulfill, offered subs
				return SalesSubStatus.REPLACEMENT_TICKETS_OFFERED;
			} else if ("5".equals(trackID)) {
				// 5 Seller cannot fulfill, no subs
				return SalesSubStatus.DROPPED_SALE;
			}
			else{
				return SalesSubStatus.SUBS_BY_SYSTEM;
			}
		} else if (subStatusCode == 3L) {
			// 3 subs in process: CSR
			String subbed = getStringValue(docNode, "subbedInd");
			if ("0".equals(subbed)) {
				return SalesSubStatus.SUBS_REJECTED;
			} else if ("1".equals(subbed)) {
				return SalesSubStatus.SUBS_OFFERED;
			}
		} else if (subStatusCode == 48L) {
			// 48 Inhand Date Change Requested
			return SalesSubStatus.CHANGE_REQUESTED_DELIVERY_DATE;
		} else if (subStatusCode == 49L) {
			// 49 Delivery Method Change Requested
			return SalesSubStatus.CHANGE_REQUESTED_DELIVERY_METHOD;
		} else if (subStatusCode == 6L || subStatusCode == 7L) {
			// 6 Pending Label Generation: Not In Hand
			// 7 Pending Label Generation: In Hand
			return SalesSubStatus.GENERATE_SHIPPING_LABEL;
		} else if (subStatusCode == 8L || subStatusCode == 9L || subStatusCode == 10L) {
			// 8 Pending Label View PENDING
			// 9 pending shipment: lmt PENDING
			// 10 Pending Shipment PENDING
			return SalesSubStatus.REPRINT_SHIPPING_LABEL;
		} else if (subStatusCode == 32L) {
			// 32 Pending Upload (PDF)
			return SalesSubStatus.PENDING_UPLOAD_PDF;
		}
		return null;
	}

	static final void convertToListingStatus(ListingResponse listing, String status) {
		if (com.stubhub.domain.account.datamodel.entity.ListingStatus.ACTIVE.getDescription().equals(status)) {
			listing.setStatus(ListingStatus.ACTIVE);
		} else if (com.stubhub.domain.account.datamodel.entity.ListingStatus.INACTIVE.getDescription().equals(status)) {
			listing.setStatus(ListingStatus.INACTIVE);
		} else if (com.stubhub.domain.account.datamodel.entity.ListingStatus.INCOMPLETE.getDescription()
				.equals(status)) {
			listing.setStatus(ListingStatus.INCOMPLETE);
		} else if (com.stubhub.domain.account.datamodel.entity.ListingStatus.PENDING_LOCK.getDescription()
				.equals(status)
				|| com.stubhub.domain.account.datamodel.entity.ListingStatus.PENDING_PDF_REVIEW.getDescription()
						.equals(status)) {
			listing.setStatus(ListingStatus.PENDING);
		} else if (com.stubhub.domain.account.datamodel.entity.ListingStatus.DELETED.getDescription().equals(status)) {
			listing.setStatus(ListingStatus.DELETED);
		}
	}

	private static String getStringValue(ObjectNode docNode, String fieldName) {
		JsonNode jsonNode = docNode.get(fieldName);
		if (notNull(jsonNode)) {
			return jsonNode.asText();
		}
		return null;
	}

	private static Long getLongValue(ObjectNode docNode, String fieldName) {
		JsonNode jsonNode = docNode.get(fieldName);
		if (notNull(jsonNode)) {
			return jsonNode.asLong();
		}
		return null;
	}

	private static Date parseStringToDate(String dateStr, TimeZone tz) {
        SimpleDateFormat sf = DATE_FORMAT_WITH_TIME_ZONE_FORMATTER.get();
		sf.setTimeZone(tz);
		Date date = null;
		try {
			date = sf.parse(dateStr);
		} catch (ParseException e) {
			log.error("Error While parsing the date=" + dateStr);
		}
		return date;
	}

    private static final ThreadLocal<SimpleDateFormat> DATE_FORMAT_WITH_TIME_ZONE_FORMATTER = new ThreadLocal<SimpleDateFormat>() {

        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat(MCIRequestUtil.DATE_FORMAT_WITH_TIME_ZONE);
        }
	    
	};
    
    private static final ThreadLocal<SimpleDateFormat> TIME_ZONE_FORMATTER = new ThreadLocal<SimpleDateFormat>() {
        
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("z");
        }
        
    };
    private static final ThreadLocal<DateTimeFormatter> format = new ThreadLocal<DateTimeFormatter>(){

        @Override
        protected DateTimeFormatter initialValue() {
            return DateTimeFormat.forPattern(MCIRequestUtil.DATE_FORMAT_WITH_TIME_ZONE).withZoneUTC();
        }
            
    };
    private static final ThreadLocal<Map<TimeZone, DateTimeFormatter>> localFormats = new ThreadLocal<Map<TimeZone, DateTimeFormatter>>() {
        
        @Override
        protected Map<TimeZone, DateTimeFormatter> initialValue() {
            return new HashMap<TimeZone, DateTimeFormatter>();
        }
        
    };
    
    private static final ThreadLocal<Map<TimeZone, DateTimeFormatter>> zoneFormats = new ThreadLocal<Map<TimeZone, DateTimeFormatter>>() {
        
        @Override
        protected Map<TimeZone, DateTimeFormatter> initialValue() {
            return new HashMap<TimeZone, DateTimeFormatter>();
        }
        
    };
    
    public static String utc2localDateTimeFormat(String dateStr,TimeZone timeZone) {
        DateTime dateTime = format.get().parseDateTime(dateStr);
        Map<TimeZone, DateTimeFormatter> localFormatterMap = localFormats.get();
        if (!localFormatterMap.containsKey(timeZone)) {
            localFormatterMap.put(timeZone, DateTimeFormat.forPattern(DATE_FORMAT_WITHOUT_TIME_ZONE)
                    .withZone(DateTimeZone.forTimeZone(timeZone)));
        }
        return localFormatterMap.get(timeZone).print(dateTime);
    }
    
    private static String utc2timeZoneDateTimeFormat(String dateStr, TimeZone targetTz) {
        DateTime dateTime = format.get().parseDateTime(dateStr);
        Map<TimeZone, DateTimeFormatter> formatMap = zoneFormats.get();
        if (!formatMap.containsKey(targetTz)) {
            formatMap.put(targetTz, DateTimeFormat.forPattern(MCIRequestUtil.DATE_FORMAT_WITH_TIME_ZONE)
                    .withZone(DateTimeZone.forTimeZone(targetTz)));
        }
        return formatMap.get(targetTz).print(dateTime);
    }

	private static String formatStringToEventLocalFormat(String dateStr) {
		return StringUtils.substringBefore(dateStr, "+0000");
	}

	public static boolean notNull(JsonNode jsonNode) {
		return jsonNode != null && !jsonNode.isNull();
	}

	private static Money convertToMoney(String price, String currency) {
		return new Money(price, currency);
	}

	private static List<Summary> convertToSummaryList(JsonNode facetJsonNode, int nameIndex) {
		List<Summary> summary = new ArrayList<Summary>();
		Iterator<Entry<String, JsonNode>> it = facetJsonNode.getFields();
		while (it.hasNext()) {
			Entry<String, JsonNode> entry = it.next();
			String key = entry.getKey();
			String[] split = StringUtils.split(key, '|');
			if (split.length > nameIndex) {
				Summary s = new Summary();
				s.setId(Long.valueOf(split[0]));
				s.setCount(entry.getValue().asInt());
				s.setDescription(split[nameIndex]);
				summary.add(s);
			}
		}
		return summary;
	}

	public static FulfillmentSpecificationResponse convertToFulfillmentSpec(JsonNode jsonNode) {
		FulfillmentSpecificationResponse fulfillmentSpecificationResponse = new FulfillmentSpecificationResponse();
		List<FulfillmentSpecification> fulfillmentSpecificationList = new ArrayList<FulfillmentSpecification>();
		fulfillmentSpecificationResponse.setFulfillmentSpecifications(fulfillmentSpecificationList);
		JsonNode responseNode = jsonNode.get("response");
		if (notNull(responseNode)) {
			ArrayNode docsNode = (ArrayNode) responseNode.get("docs");
			if (notNull(docsNode)) {
				for (int i = 0; i < docsNode.size(); i++) {
					ObjectNode docNode = (ObjectNode) docsNode.get(i);
					FulfillmentSpecification fs = new FulfillmentSpecification();
					JsonNode obj = docNode.get("id");
					if (notNull(obj)) {
						fs.setTicketId(obj.asLong());
					}
					obj = docNode.get("sellerId");
					if (notNull(obj)) {
						fs.setSellerId(obj.asLong());
					}
					obj = docNode.get("jdkTimezone");
					if (notNull(obj)) {
						fs.setJdkTimeZone(obj.asText());
					}
					obj = docNode.get("ticketSystemStatus");
					if (notNull(obj)) {
						fs.setSystemStatus(obj.asText());
					}
					obj = docNode.get("split");
					if (notNull(obj)) {
						fs.setSplitOption(obj.asLong());
					}
					obj = docNode.get("saleEndDate");
					if (notNull(obj)) {
						String dateStr = obj.asText();
						fs.setSaleEndDate(dateStr);
					}
					obj = docNode.get("quantityRemain");
					if (notNull(obj)) {
						fs.setQuantityRemain(obj.asLong());
					}
					obj = docNode.get("ticketMedium");
					if (notNull(obj)) {
						fs.setTicketMedium(obj.asLong());
					}
					obj = docNode.get("listingSourceId");
					if (notNull(obj)) {
						fs.setListingSourceId(obj.asLong());
					}
					obj = docNode.get("expectedInhandDate");
					if (notNull(obj)) {
						String dateStr = obj.asText();
						fs.setInHandDate(dateStr);
					}
					obj = docNode.get("deliveryOptionId");
					if (notNull(obj)) {
						fs.setDeliveryOptionId(obj.asLong());
					}
					obj = docNode.get("country");
					if (notNull(obj)) {
						fs.setEventCountry(obj.asText());
					}
					obj = docNode.get("eventId");
					if (notNull(obj)) {
						fs.setEventId(obj.asLong());
					}
					obj = docNode.get("eventDate");
					if (notNull(obj)) {
						String dateStr = obj.asText();
						fs.setEventDateUTC(dateStr);
					}
					obj = docNode.get("eventDateLocal");
					if (notNull(obj)) {
						String dateStr = obj.asText();
						fs.setEventDateLocal(StringUtils.substringBefore(dateStr, "+0000") + "Z");
					}
					obj = docNode.get("sellerContactId");
					if (notNull(obj)) {
						UserContactDetail userContactDetail = new UserContactDetail();
						userContactDetail.setContactId(obj.asLong());
						fs.setSellerContact(userContactDetail);
					}
					obj = docNode.get("lmsApprovalStatusId");
					if (notNull(obj)) {
						fs.setLmsApprovalStatusId(obj.asLong());
					}
					fulfillmentSpecificationList.add(fs);
				}
			}
		}
		return fulfillmentSpecificationResponse;
	}

	public static DeliverySpecificationResponse convertJsonNodeToDeliverySpecResponse(JsonNode jsonNode) {
		DeliverySpecificationResponse deliverySpecificationResponse = new DeliverySpecificationResponse();
		deliverySpecificationResponse.setDeliverySpecifications(new ArrayList<DeliverySpecification>());

		if (jsonNode == null || !jsonNode.isObject()) {
			return deliverySpecificationResponse;
		}
		JsonNode responseNode = jsonNode.get("response");
		if (!notNull(responseNode)) {
			return deliverySpecificationResponse;
		}
		ArrayNode docsNode = (ArrayNode) responseNode.get("docs");
		if (notNull(docsNode)) {
			for (int i = 0; i < docsNode.size(); i++) {
				ObjectNode docNode = (ObjectNode) docsNode.get(i);
				DeliverySpecification deliverySpecification = new DeliverySpecification();

				JsonNode obj = docNode.get("id");
				if (notNull(obj)) {
					deliverySpecification.setTid(obj.asLong());
				}

				obj = docNode.get("sellerId");
				if (notNull(obj)) {
					deliverySpecification.setSellerId(obj.asLong());
				}

				obj = docNode.get("deliveryOptionId");
				if (notNull(obj)) {
					deliverySpecification.setDeliveryOptionId(obj.asLong());
				}

				obj = docNode.get("fulfillmentMethodId");
				if (notNull(obj)) {
					deliverySpecification.setFulfillmentMethodId(obj.asLong());
				}

				obj = docNode.get("deliveryMethodId");
				if (notNull(obj)) {
					deliverySpecification.setDeliveryMethodId(obj.asLong());
				}

				obj = docNode.get("jdkTimezone");
				if (notNull(obj)) {
					deliverySpecification.setJdkTimeZone(obj.asText());
				}

				obj = docNode.get("buyerId");
				if (notNull(obj)) {
					deliverySpecification.setBuyerId(obj.asLong());
				}

				obj = docNode.get("inhandDate");
				if (notNull(obj)) {
					deliverySpecification.setInHandDate(formatStringToEventLocalFormat(obj.asText()) + "Z");
				}

				obj = docNode.get("orderProcSubStatusCode");
				if (notNull(obj)) {
					deliverySpecification.setOrderProcSubStatusCode(obj.asText());
				}

				obj = docNode.get("buyerShippingContactId");
				if (notNull(obj)) {
					UserContactDetail userContactDetail = new UserContactDetail();
					userContactDetail.setContactId(obj.asLong());
					deliverySpecification.setBuyerContact(userContactDetail);
				}

				obj = docNode.get("eventId");
				if (notNull(obj)) {
					deliverySpecification.setEventId(obj.asLong());
				}

				obj = docNode.get("ticketId");
				if (notNull(obj)) {
					deliverySpecification.setTicketId(obj.asLong());
				}

				obj = docNode.get("ticketMediumId");
				if (notNull(obj)) {
					deliverySpecification.setTicketMedium(obj.asLong());
				}

				obj = docNode.get("cancelled");
				if (notNull(obj)) {
					deliverySpecification.setCancelled(obj.asLong());
				}

				obj = docNode.get("eventDate");
				if (notNull(obj)) {
					String eventDate = obj.asText();
					deliverySpecification.setEventDateUTC(eventDate);
				}

				obj = docNode.get("eventDateLocal");
				if (notNull(obj)) {
					String eventDateLocal = obj.asText();
					deliverySpecification.setEventDateLocal(formatStringToEventLocalFormat(eventDateLocal) + "Z");
				}

				deliverySpecificationResponse.getDeliverySpecifications().add(deliverySpecification);
			}
		}
		return deliverySpecificationResponse;
	}

	private static String getNameFromFacetFiled(String value, int nameIndex) {
		String[] split = StringUtils.split(value, '|');
		if (split.length > nameIndex) {
			return split[nameIndex];
		}
		return null;
	}

	public static OrdersResponse addSellerPaymentInfo(OrdersResponse ordersResponse, JsonNode paymentResponse)
			throws ParseException {
		if (paymentResponse == null || !paymentResponse.isObject()) {
			return ordersResponse;
		}

		JsonNode responseNode = paymentResponse.get("response");
		if (!notNull(responseNode)) {
			return ordersResponse;
		}

		ArrayNode docsNode = (ArrayNode) responseNode.get("docs");

		List<CSOrderDetailsResponse> list = ordersResponse.getOrder();
		List<SellerPayment> list2 = null;
		String currency = "USD";
		for (CSOrderDetailsResponse csOrderDetailsResponse : list) {
			int numFound = 0;
			list2 = new ArrayList<SellerPayment>();
			if (notNull(docsNode)) {
				for (int i = 0; i < docsNode.size(); i++) {
					ObjectNode docNode = (ObjectNode) docsNode.get(i);
					JsonNode tidNode = docNode.get("transactionId");
					JsonNode currencyNode = docNode.get("currencyCode");
					JsonNode createdDateNode = docNode.get("paymentCreatedDate");
					JsonNode paymentAmountNode = docNode.get("paymentAmount");
					JsonNode sellerPaymentStatusNode = docNode.get("sellerPaymentStatus");
					if (notNull(tidNode)) {
						if (csOrderDetailsResponse.getTransaction().getOrderId().equals(tidNode.asText())) {
							SellerPayment sellerPayment = new SellerPayment();
							currency = currencyNode.asText();
							if (notNull(createdDateNode)) {
                sellerPayment.setPaymentDate(convertToShortUTCDateFormat(createdDateNode.asText()));
              }
							if (notNull(paymentAmountNode)) {
                sellerPayment.setPaymentAmount(new Money(paymentAmountNode.asText(), currency));
              }
							if (notNull(sellerPaymentStatusNode)) {
                sellerPayment.setStatus(sellerPaymentStatusNode.asText());
              }
							list2.add(sellerPayment);
							numFound++;
						}
					}
				}
			}

			csOrderDetailsResponse.getSellerPayments().setPayments(list2);
			csOrderDetailsResponse.getSellerPayments().setNumFound(numFound);
		}

		return ordersResponse;
	}

	private static String getCurrencyForSalesHistory(JsonNode responseNode) {
		String currency = "USD";// by default
		ArrayNode docsNode = (ArrayNode) responseNode.get("docs");
		if (notNull(docsNode)) {
			if (docsNode.size() > 0) {
				ObjectNode docNode = (ObjectNode) docsNode.get(0);
				JsonNode currencyNode = docNode.get("currencyCode");
				if (notNull(currencyNode)) {
					currency = currencyNode.asText();
				}
			}
		}
		return currency;
	}

	private static String getPriceField(String priceType) {
		if ("listprice".equalsIgnoreCase(priceType)) {
      return "statsPricePerTicket";
    } else {
      return "statsCostPerTicket";
    }
	}

	private static Double getDoubleValue(JsonNode node, String field) {
		JsonNode obj = node.get(field);
		if (notNull(obj) && obj.isNumber()) {
		    return obj.asDouble();
		}
		return null;
	}

	private static Double facetMedian(JsonNode facetNode) {
		JsonNode percentiles = facetNode.get("percentiles");
		if(notNull(percentiles)){
			return getDoubleValue(percentiles, "50.0");
		}
		return null;
	}

	private static Money toMoney(Double amount, String currency) {
		return amount == null ? null : new Money(new BigDecimal(amount), currency);
	}

	public static SalesHistoryResponse convertJsonNodeToSalesHistoryResponse(JsonNode jsonNode, String priceType) {
		SalesHistoryResponse response = new SalesHistoryResponse();
		String priceField = getPriceField(priceType);
		log.info("priceType=" + priceType + " priceField=" + priceField);
		if (jsonNode != null) {
			JsonNode responseNode = jsonNode.get("response");
			if (notNull(responseNode)) {
				// Get currency code
				String currency = getCurrencyForSalesHistory(responseNode);

				JsonNode aggregationsNode = responseNode.get("aggregations");
				if (notNull(aggregationsNode)) {
					JsonNode facetJsonNode = aggregationsNode.get(priceField);
					if (notNull(facetJsonNode)) {
						response.setAveragePricePerTicket(toMoney(getDoubleValue(facetJsonNode, "mean"), currency));
						response.setMaxPricePerTicket(toMoney(getDoubleValue(facetJsonNode, "max"), currency));
						response.setMedianPricePerTicket(toMoney(facetMedian(facetJsonNode), currency));
						response.setMinPricePerTicket(toMoney(getDoubleValue(facetJsonNode, "min"), currency));
					}
				}
			}
		}
		return response;
	}

	@SuppressWarnings("unchecked")
	public static MyOrderListResponse convertJsonNodeToMyOrderListEntities(JsonNode jsonNode) {
		MyOrderListResponse response = new MyOrderListResponse();
		if (jsonNode == null || !jsonNode.isObject()) {
			return response;
		}
		JsonNode responseNode = jsonNode.get("response");
		if (!notNull(responseNode)) {
			return response;
		}
		JsonNode numFoundNode = responseNode.get("numFound");
		if (notNull(numFoundNode)) {
			response.setNumFound(numFoundNode.asLong());
		} else {
			response.setNumFound(0L);
		}
		ArrayNode docsNode = (ArrayNode) responseNode.get("docs");

		if (notNull(docsNode)) {
			int size = docsNode.size();
			if (size <= 0) {
				return response;
			}
			RowMeta deliveryOptionRowMeta = new RowMeta(new String[] { "FULFILLMENT_METHOD_ID", "DELIVERY_OPTION_ID",
					"TICKET_MEDIUM_ID", "LMS_APPROVAL_STATUS_ID" }, null, null, null);
			DeliveryOptionMapper deliveryOptionMapper = new DeliveryOptionMapper();
			RowMeta seatRowMeta = new RowMeta(new String[] { "SECTION", "ROW_DESC", "SEATS" }, null, null, null);
			SeatsMapper seatsMapper = new SeatsMapper();
			RowMeta ticketTraitRowMeta = new RowMeta(new String[] { "SEAT_TRAITS" }, null, null, null);
			TicketTraitMapper ticketTraitMapper = new TicketTraitMapper();

			for (int i = 0; i < size; i++) {
				ObjectNode docNode = (ObjectNode) docsNode.get(i);
				MyOrderResponseDTO dto = new MyOrderResponseDTO();
				JsonNode obj = docNode.get("city");
				if (notNull(obj)) {
					dto.setCity(obj.asText());
				}
				String currency = "USD";
				obj = docNode.get("currencyCode");
				if (notNull(obj)) {
					currency = obj.asText();
				}

				obj = docNode.get("costPerTicket");
				if (notNull(obj)) {
					dto.setCostPerTicket(convertToMoney(obj.asText(), currency));
				}

				obj = docNode.get("pricePerTicket");
				if (notNull(obj)) {
					dto.setPricePerTicket(convertToMoney(obj.asText(), currency));
				}

				obj = docNode.get("totalCost");
				if (notNull(obj)) {
					dto.setTotalCost(convertToMoney(obj.asText(), currency));
				}

				obj = docNode.get("state");
				if (notNull(obj)) {
					dto.setState(obj.asText());
				}

				obj = docNode.get("country");
				if (notNull(obj)) {
					dto.setCountry(obj.asText());
				}

				TimeZone tz = MCIRequestUtil.UTC_TIME_ZONE;
				obj = docNode.get("jdkTimezone");
				if (notNull(obj)) {
					tz = TimeZone.getTimeZone(obj.asText());
				}

				obj = docNode.get("eventDate");
				if (notNull(obj)) {
					Calendar cal = getUTCTime(obj);
					dto.setEventDate(cal);
                    SimpleDateFormat sdf = TIME_ZONE_FORMATTER.get();
					sdf.setTimeZone(tz);
					dto.setEventDateTimeZone(sdf.format(cal.getTime()));

				}
				obj = docNode.get("eventDateLocal");
				if (notNull(obj)) {
					Calendar cal = getUTCTime(obj);
					dto.setEventDateLocal(cal);
				}
				boolean hideEventDate = false;
				obj = docNode.get("hideEventDate");
				if (notNull(obj)) {
					hideEventDate = Boolean.valueOf(obj.asText());
				}
				if (hideEventDate) {
					dto.setHideEventDate("1");
				} else {
					dto.setHideEventDate("0");
				}

				boolean hideEventTime = false;
				obj = docNode.get("hideEventTime");
				if (notNull(obj)) {
					hideEventTime = Boolean.valueOf(obj.asText());
				}
				if (hideEventTime) {
					dto.setHideEventTime("1");
				} else {
					dto.setHideEventTime("0");
				}

				obj = docNode.get("eventNameSort");
				if (notNull(obj)) {
					dto.setEventDescription(obj.asText());
				}

				obj = docNode.get("venueNameSort");
				if (notNull(obj)) {
					dto.setVenueDescription(obj.asText());
				}

				obj = docNode.get("eventId");
				if (notNull(obj)) {
					dto.setEventId(obj.asLong());
				}

				obj = docNode.get("venueId");
				if (notNull(obj)) {
					dto.setVenueId(obj.asLong());
				}

				obj = docNode.get("performerId");
				if (notNull(obj)) {
					dto.setGenreId(obj.asLong());
				} else {
					obj = docNode.get("groupingId");
					if (notNull(obj)) {
						dto.setGenreId(obj.asLong());
					} else {
						obj = docNode.get("categoryId");
						if (notNull(obj)) {
							dto.setGenreId(obj.asLong());
						}
					}
				}

				obj = docNode.get("expectedDeliveryDate");
				if (notNull(obj)) {
					Calendar srcCal = getUTCTime(obj);
					srcCal.add(Calendar.MILLISECOND, tz.getOffset(srcCal.getTimeInMillis()));
					dto.setExpectedDeliveryDate(srcCal);
				}
				obj = docNode.get("inhandDate");
				if (notNull(obj)) {
					dto.setInhandDate(getUTCTime(obj));
				}
				obj = docNode.get("transactionDate");
				if (notNull(obj)) {
					dto.setOrderDate(getUTCTime(obj));
				}
				obj = docNode.get("id");
				if (notNull(obj)) {
					dto.setOrderId(obj.asLong());
				}
				obj = docNode.get("quantity");
				if (notNull(obj)) {
					dto.setQuantity(obj.asLong());
				}

				SolrDocument row = new SolrDocument();
				obj = docNode.get("fulfillmentMethodId");
				if (notNull(obj)) {
					row.put("FULFILLMENT_METHOD_ID", obj.asText());
				}
				obj = docNode.get("deliveryOptionId");
				if (notNull(obj)) {
					row.put("DELIVERY_OPTION_ID", obj.asText());
				}
				obj = docNode.get("ticketMediumId");
				if (notNull(obj)) {
					row.put("TICKET_MEDIUM_ID", obj.asText());
				}
				obj = docNode.get("lmsApprovalStatusId");
				if (notNull(obj)) {
					row.put("LMS_APPROVAL_STATUS_ID", obj.asText());
				}

				obj = docNode.get("section");
				if (notNull(obj)) {
					row.put("SECTION", obj.asText());
				}
				obj = docNode.get("rowDesc");
				if (notNull(obj)) {
					row.put("ROW_DESC", obj.asText());
				}
				obj = docNode.get("seats");
				if (notNull(obj)) {
					row.put("SEATS", obj.asText());
				}

				obj = docNode.get("orderProcStatusId");
				if (notNull(obj)) {
					dto.setStatus(OrderStatus.getById(obj.asInt()).name());
				}

				obj = docNode.get("orderProcSubStatusCode");
				if (notNull(obj)) {
					dto.setSubStatusCode(obj.asLong());
				}

				obj = docNode.get("seats");
				if (notNull(obj)) {
					row.put("SEATS", obj.asText());
				}

				obj = docNode.get("seatTraits");
				if (notNull(obj)) {
					row.put("SEAT_TRAITS", Arrays.asList(((ArrayNode)obj).get(0).asText()));
				}
				try {
					dto.setDeliveryOption((DeliveryOption) deliveryOptionMapper.map(row, deliveryOptionRowMeta));
				} catch (Exception e) {
					log.error("exception while setDeliveryOption", e);
				}

				try {
					dto.setSeats((List<Seat>) seatsMapper.map(row, seatRowMeta));
				} catch (Exception e) {
					log.error("exception while setSeats", e);
				}

				try {
					dto.setTicketTraits((Set<TicketTrait>) ticketTraitMapper.map(row, ticketTraitRowMeta));
				} catch (Exception e) {
					log.error("exception while setTicketTraits", e);
				}

				obj = docNode.get("fedexTrackingNumber");
				if (notNull(obj)) {
					dto.setTrackingNumber(obj.asText());
				}

				MyOrderResponse resp = dto.wrap2MyOrderResponse();
				response.getMyOrderList().add(resp);
			}
		}
		return response;
	}

	private static Calendar getUTCTime(JsonNode obj) {
		Date date = parseStringToDate(obj.asText(), MCIRequestUtil.UTC_TIME_ZONE);
		Calendar cal = Calendar.getInstance(MCIRequestUtil.UTC_TIME_ZONE);
		cal.setTime(date);
		return cal;
	}


	public static SalesResponse convertJsonNodeToEventSalesData(
			JsonNode jsonNode, String filters, String priceType, String sellerId) throws ParseException {
		SalesResponse salesResponse = new SalesResponse();
		salesResponse.setSales(new ArrayList<SaleResponse>());
		if (jsonNode == null || !jsonNode.isObject()) {
			return salesResponse;
		}
		JsonNode responseNode = jsonNode.get("response");
		if (!notNull(responseNode)) {
			return salesResponse;
		}
		JsonNode numFoundNode = responseNode.get("numFound");
		if (notNull(numFoundNode)) {
			salesResponse.setNumFound(numFoundNode.asLong());
		} else {
			salesResponse.setNumFound(0L);
		}
		ArrayNode docsNode = (ArrayNode) responseNode.get("docs");
		if (notNull(docsNode)) {
			int size = docsNode.size();
			if (size <= 0) {
				return salesResponse;
			}
			String deliveryOptionFilter = null;
			/*if (filters != null) {
				deliveryOptionFilter = AccountResponseAdapter.filterResponse(filters);
			}*/

			for (int i = 0; i < size; i++) {
				ObjectNode docNode = (ObjectNode) docsNode.get(i);
				SaleResponse saleResponse = new SaleResponse();
				JsonNode obj = docNode.get("deliveryOptionId");
				int deliveryOptionId = 0;
				int ticketMediumId = 0;
				int lmsStatus = 0;
				if (notNull(obj)) {
					deliveryOptionId = new Integer(obj.asText());
				}
				obj = docNode.get("ticketMediumId");
				if (notNull(obj)) {
					ticketMediumId = new Integer(obj.asText());
				}
				obj = docNode.get("lmsApprovalStatusId");
				if (notNull(obj)) {
					lmsStatus = new Integer(obj.asText());
				}
				String fulfillmentMethod = null;
				obj = docNode.get("fulfillmentMethodId");
				if (notNull(obj)) {
					fulfillmentMethod = obj.asText();
				}
				DeliveryOption deliveryOption = AccountResponseAdapter.getDeliveryOption(ticketMediumId,
						deliveryOptionId, lmsStatus, fulfillmentMethod);
				if (deliveryOptionFilter != null) {
					if (deliveryOption != null && deliveryOption.name().equalsIgnoreCase(deliveryOptionFilter)) {
						saleResponse.setDeliveryOption(deliveryOption);
					} else {
						continue;
					}

				} else {
					saleResponse.setDeliveryOption(deliveryOption);
				}
				obj = docNode.get("deliveryMethodId");
				if(notNull(obj)){
					int deliveryMethodId = obj.asInt();
					saleResponse.setDeliveryMethodId(deliveryMethodId);
					DeliveryMethod dm = DeliveryMethod.getDeliveryMethodById(deliveryMethodId);
					if(dm != null){
						int deliveryTypeId = dm.getDeliveryTypeId();
						saleResponse.setDeliveryTypeId(deliveryTypeId);
					}
				}
				obj = docNode.get("quantity");
				if (notNull(obj)) {
					saleResponse.setQuantity(obj.asInt());
				}
				String currency = "USD";
				obj = docNode.get("currencyCode");
				if (notNull(obj)) {
					currency = obj.asText();
				}

				obj = docNode.get(getPriceFieldForEventSales(priceType));
				if (notNull(obj)) {
					saleResponse.setDisplayPricePerTicket(convertToMoney(
							obj.asText(), currency));
				}

				obj = docNode.get("transactionDate");
				if (notNull(obj)) {
					saleResponse.setTransactionDate(getUTCTime(obj));
				}

				obj = docNode.get("section");
				if (notNull(obj)) {
					saleResponse.setSection(obj.asText());
				}
				
				obj = docNode.get("venueConfigSectionsId");
				if (notNull(obj)) {
					saleResponse.setSectionId(obj.asLong());
				}

				obj = docNode.get("rowDesc");
				if (notNull(obj)) {
					saleResponse.setRows(obj.asText());
				}

				obj =docNode.get("hideSeatInfoInd");
				boolean hideSeats=false;
				if(notNull(obj)){
					hideSeats=(1==obj.asInt());
				}

				obj = docNode.get("seats");
				if (notNull(obj)&&!hideSeats) {
					if (saleResponse.getRows() != null
							&& saleResponse.getRows().contains(",")) {
						String[] rows = saleResponse.getRows().split(",");
						String seats = obj.asText().replaceAll(rows[0] + "-", "");
						seats = (seats).replaceAll(rows[1] + "-", "");
						saleResponse.setSeats(seats);
					} else {
						saleResponse.setSeats(obj.asText());
					}
				}

				if (ThreadLocalUtil.isCallerTypeInternal()) {
					obj = docNode.get("ticketId");
					if (notNull(obj)) {
						saleResponse.setListingId(obj.asText());
					}
					obj = docNode.get("venueConfigSectionsId");
					if (notNull(obj)) {
						saleResponse.setVenueConfigSectionId(obj.asLong());
					}
					obj = docNode.get("eventId");
					if (notNull(obj)) {
						saleResponse.setEventId(obj.asText());
					}
					obj = docNode.get("venueConfigSectionsId");
					if (notNull(obj)) {
						saleResponse.setVenueConfigSectionId(obj.asLong());
					}
					obj = docNode.get("zoneId");
					if (notNull(obj)) {
						saleResponse.setZoneId(obj.asLong());
					}
					obj = docNode.get("zoneName");
					if (notNull(obj)) {
						saleResponse.setZoneName(obj.asText());
					}

					obj = docNode.get("sellerId");
					if (notNull(obj)) {
						saleResponse.setSellerOwnSale(obj.asText().equals(sellerId));
					}

					obj = docNode.get("seatTraits");
					if (notNull(obj)) {
						ArrayNode arrayNode = (ArrayNode) obj;
						if (arrayNode.size() > 0) {
							String seatTrait = arrayNode.get(0).asText();
							Set<TicketTrait> ticketTraits = AccountResponseAdapter.convertToTicketTraits(seatTrait);
							if (ticketTraits != null) {
								saleResponse.setTicketTraits(ticketTraits);
							}
						}
					}
				}
				salesResponse.getSales().add(saleResponse);
			}
		}
		return salesResponse;
	}

	private static String getPriceFieldForEventSales(String priceType) {
		if (AccountResponseAdapter.PRICE_TYPE_LISTPRICE.equalsIgnoreCase(priceType)) {
			return "pricePerTicket";
		} else {
			return "costPerTicket";
		}
	}
}