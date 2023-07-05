package com.stubhub.domain.account.adapter;

import com.stubhub.domain.account.annotation.ResponseField;
import com.stubhub.domain.account.common.ListingFulfillmentWindow;
import com.stubhub.domain.account.common.enums.DeliveryMethod;
import com.stubhub.domain.account.common.enums.DeliveryOption;
import com.stubhub.domain.account.common.enums.*;
import com.stubhub.domain.account.common.enums.ListingStatus;
import com.stubhub.domain.account.common.enums.TicketMedium;
import com.stubhub.domain.account.common.exception.AccountException;
import com.stubhub.domain.account.common.util.ListingFulfillmentWindowUtil;
import com.stubhub.domain.account.common.util.ValidationUtil;
import com.stubhub.domain.account.datamodel.entity.*;
import com.stubhub.domain.account.dto.MyOrderResponseDTO;
import com.stubhub.domain.account.intf.*;
import com.stubhub.domain.account.intf.SellerPayment;
import com.stubhub.domain.account.intf.SellerPayments;
import com.stubhub.domain.account.mapper.DataMapper;
import com.stubhub.domain.account.mapper.RowMeta;
import com.stubhub.domain.user.payments.intf.CreditCardDetails;
import com.stubhub.newplatform.common.entity.Money;
import com.stubhub.newplatform.common.util.DateUtil;
import com.stubhub.newplatform.property.MasterStubHubProperties;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FieldStatsInfo;
import org.apache.solr.client.solrj.response.PivotField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.util.NamedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.io.BufferedReader;
import java.io.Reader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class AccountResponseAdapter {

	private final static Logger log = LoggerFactory.getLogger(AccountResponseAdapter.class);

	public static final String PRICE_TYPE_LISTPRICE = "listprice";
	public static final String PRICE_TYPE_ALLINPRICE = "allinprice";

	private static String FULFILLMENT_METHOD_UPS = "10";
	private static String FULFILLMENT_METHOD_FEDEX = "6";
	private static String FULFILLMENT_METHOD_ROYALMAIL = "11";
	private static String FULFILLMENT_METHOD_DEUTSCHEPOST = "12";
	private static String FULFILLMENT_METHOD_WILLCALL = "8";
	private static String FULFILLMENT_METHOD_COURIER = "15";
	private static String FULFILLMENT_METHOD_LOCALDELIVERY = "17";

	private static Map<String, String> transactionTypeMap = new HashMap<String, String>();

//	private static ThreadPoolTaskExecutor TASK_EXECUTOR = new ThreadPoolTaskExecutor();
//
//	static{
//		TASK_EXECUTOR.setMaxPoolSize(MasterStubHubProperties
//				.getPropertyAsInt("accountapi.thread.max.poolsize", 250));
//		TASK_EXECUTOR.setKeepAliveSeconds(MasterStubHubProperties
//				.getPropertyAsInt("accountapi.thread.threadpool.timeout", 15));
//		TASK_EXECUTOR.setCorePoolSize(MasterStubHubProperties
//				.getPropertyAsInt("accountapi.thread.core.poolsize", 50));
//		TASK_EXECUTOR.setQueueCapacity(MasterStubHubProperties
//				.getPropertyAsInt("accountapi.thread.queue.capacity", 500));
//		TASK_EXECUTOR.setAllowCoreThreadTimeOut(true);
//		TASK_EXECUTOR.setWaitForTasksToCompleteOnShutdown(true);
//		TASK_EXECUTOR.setThreadGroup(Thread.currentThread()
//				.getThreadGroup());
//		TASK_EXECUTOR.afterPropertiesSet();
//		TASK_EXECUTOR.initialize();
//	}


	public static DeliveryOption getDeliveryOption(int ticketMedium,
						int deliveryOptionId, int lmsOption,
						List supportedFulfillmentMethods, String listingId) {
		if (shippingEnabled(TicketMedium.getTicketMedium(ticketMedium))) {
			if (LMSOption.getLMSOption(lmsOption) != LMSOption.APPROVED) {
				if (supportedFulfillmentMethods != null
					&& supportedFulfillmentMethods
					.contains(FULFILLMENT_METHOD_UPS))
						return DeliveryOption.UPS;
				else if (supportedFulfillmentMethods != null
							&& supportedFulfillmentMethods
							.contains(FULFILLMENT_METHOD_FEDEX))
					return DeliveryOption.FEDEX;
				else if (supportedFulfillmentMethods != null && supportedFulfillmentMethods.contains(FULFILLMENT_METHOD_ROYALMAIL))
					return DeliveryOption.ROYALMAIL;
				else if (supportedFulfillmentMethods != null && supportedFulfillmentMethods.contains(FULFILLMENT_METHOD_DEUTSCHEPOST))
					return DeliveryOption.DEUTSCHEPOST;
				else if (supportedFulfillmentMethods != null && supportedFulfillmentMethods.contains(FULFILLMENT_METHOD_WILLCALL))
					return DeliveryOption.WILLCALL;
				else if (supportedFulfillmentMethods != null && supportedFulfillmentMethods.contains(FULFILLMENT_METHOD_COURIER))
					return DeliveryOption.COURIER;
				else if (supportedFulfillmentMethods != null && supportedFulfillmentMethods.contains(FULFILLMENT_METHOD_LOCALDELIVERY))
					return DeliveryOption.LOCALDELIVERY;
				else
					return DeliveryOption.LMS;
			}
			return DeliveryOption.LMS;
		}
		else if (TicketMedium.getTicketMedium(ticketMedium) == TicketMedium.PDF) {
			return DeliveryOption.PDF;
		}
		else if (TicketMedium.getTicketMedium(ticketMedium) == TicketMedium.BARCODE) {
			return DeliveryOption.BARCODE;
		}
        else if (TicketMedium
                .getTicketMedium(ticketMedium) == TicketMedium.FLASHSEAT) { return DeliveryOption.FLASHSEAT; }
		else if (TicketMedium.getTicketMedium(ticketMedium) == TicketMedium.MOBILE) {
			return DeliveryOption.MOBILE_TICKET;
		}
		else if (TicketMedium.getTicketMedium(ticketMedium) == TicketMedium.EXTMOBILE) {
			return DeliveryOption.EXTERNAL_TRANSFER;
		}
		else if (TicketMedium.getTicketMedium(ticketMedium) == TicketMedium.EXTFLASH) {
			return DeliveryOption.EXTERNAL_TRANSFER;
		}
		log.error("api domain=account, method=getDeliveryOption, " +
				"ticket medium=paper, fulfillment mehtods can not found correct delivery option," +
				" listingId=" + listingId + ", supportedFulfillmentMethods=" + supportedFulfillmentMethods);
		return null;
	}


	private static Date formatStringToDate(String dateStr, TimeZone tz) {
		String dateFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'";
		SimpleDateFormat sf = new SimpleDateFormat(dateFormat);
		sf.setTimeZone(tz);
		Date date = null;
		try{
			date = sf.parse(dateStr);
		}catch (ParseException e) {
			log.error("Error While parsing the date=" + dateStr);
		}
		return date;
	}

	protected static Set<TicketTrait> convertToTicketTraits(String traitInfo) {
		if ("|||".equals(traitInfo)) {
			return null;
		}
		Set<TicketTrait> traitSet = new HashSet<TicketTrait>();
		String[] ticketTraitIds, ticketTraits, seatTraitTypeIds, ticketTraitTypes;
		StringTokenizer tokenizer = new StringTokenizer(traitInfo, "|");
		ticketTraitIds = tokenizer.nextToken().split(",");
		String ticketTraitsNamesRaw = tokenizer.nextToken();
		ticketTraits = ticketTraitsNamesRaw.split(",");
		boolean possibleWrongTicketTrait = false;

		if (ticketTraits.length > ticketTraitIds.length) {
			if (ticketTraitIds.length == 1) {
				ticketTraits[0] = ticketTraitsNamesRaw;
			} else {
				//as the trait name itself contains ',' when MCI store in this way , it is already wrong
				possibleWrongTicketTrait = true;
			}
		}
		seatTraitTypeIds = tokenizer.nextToken().split(",");
		ticketTraitTypes = tokenizer.nextToken().split(",");
		int arraySize = ticketTraitIds.length;
		TicketTrait traitObj;
		for (int i = 0; i < arraySize; i++) {
			traitObj = new TicketTrait();
			traitObj.setId(ticketTraitIds[i]);
			if (!possibleWrongTicketTrait) {
				//in this case, not set the possible wrong name, will be handled when API response
				traitObj.setName(ticketTraits[i]);
			}
			traitObj.setType(ticketTraitTypes[i]);
			traitSet.add(traitObj);
		}
		return traitSet;
	}

	public static boolean shippingEnabled(TicketMedium ticketMedium){
		if(ticketMedium == TicketMedium.PAPER){
			return true;
		}
		if(ticketMedium == TicketMedium.SEASONCARD){
			return true;
		}
		if(ticketMedium == TicketMedium.EVENTCARD){
			return true;
		}
		if(ticketMedium == TicketMedium.WRISTBAND){
			return true;
		}
		if(ticketMedium == TicketMedium.RFID){
			return true;
		}
		return false;
	}

	public static DeliveryOption getDeliveryOption(int ticketMedium, int deliveryOptionId, int lmsOption, String fulfillmentMethod) {
		if (shippingEnabled(TicketMedium.getTicketMedium(ticketMedium))) {
			if (LMSOption.getLMSOption(lmsOption) == LMSOption.NONE) {
				if (FULFILLMENT_METHOD_UPS.equals(fulfillmentMethod))
					return DeliveryOption.UPS;
				else if (FULFILLMENT_METHOD_FEDEX.equals(fulfillmentMethod))
					return DeliveryOption.FEDEX;
				else if (FULFILLMENT_METHOD_ROYALMAIL.equals(fulfillmentMethod))
					return DeliveryOption.ROYALMAIL;
				else if (FULFILLMENT_METHOD_DEUTSCHEPOST.equals(fulfillmentMethod))
					return DeliveryOption.DEUTSCHEPOST;
				else if (FULFILLMENT_METHOD_WILLCALL.equals(fulfillmentMethod))
					return DeliveryOption.WILLCALL;
				else if (FULFILLMENT_METHOD_COURIER.equals(fulfillmentMethod))
					return DeliveryOption.COURIER;
				else if (FULFILLMENT_METHOD_LOCALDELIVERY.equals(fulfillmentMethod))
					return DeliveryOption.LOCALDELIVERY;
				else
					return DeliveryOption.LMS;
			}
			return DeliveryOption.LMS;
		}

		if (TicketMedium.getTicketMedium(ticketMedium) == TicketMedium.PDF) {
			return DeliveryOption.PDF;
		}

		if (TicketMedium.getTicketMedium(ticketMedium) == TicketMedium.BARCODE) {
			return DeliveryOption.BARCODE;
		}

		if (TicketMedium.getTicketMedium(ticketMedium) == TicketMedium.MOBILE) {
			return DeliveryOption.MOBILE_TICKET;
		}

		if (TicketMedium.getTicketMedium(ticketMedium) == TicketMedium.EXTMOBILE || TicketMedium.getTicketMedium(ticketMedium) == TicketMedium.EXTFLASH) {
			return DeliveryOption.EXTERNAL_TRANSFER;
		}

        if (TicketMedium.getTicketMedium(ticketMedium) == TicketMedium.FLASHSEAT) { return DeliveryOption.FLASHSEAT; }
		return null;
	}

	public static CreditCardChargesResponse convert(List<SellerCcTrans> sellerCcTransList, Map<Long, CreditCardDetails> sellerCcMap) {
		String dateFormat = "yyyy-MM-dd'T'HH:mm:ssZ";
		CreditCardChargesResponse creditCardChargesResponse = new CreditCardChargesResponse();
		List<CreditCardCharge> creditCardCharges = new ArrayList<CreditCardCharge>();
		if(transactionTypeMap.isEmpty()) {
			transactionTypeMap.put("A", "Authorized");
			transactionTypeMap.put("C", "Captured");
			transactionTypeMap.put("V", "Voided");
			transactionTypeMap.put("D", "Delayed Capture");
		}

		if(sellerCcTransList != null && !sellerCcTransList.isEmpty()) {
			for(SellerCcTrans sellerCcTrans : sellerCcTransList) {
				CreditCardCharge creditCardCharge = new CreditCardCharge();
				creditCardCharge.setId(sellerCcTrans.getId().toString());
				creditCardCharge.setOrderID(sellerCcTrans.getTid());
				creditCardCharge.setChargedAmount(new Money(sellerCcTrans.getAmount().toString(), sellerCcTrans.getCurrencyCode()));

				SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
				sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
				creditCardCharge.setChargedDate(sdf.format(sellerCcTrans.getLastUpdatedDate().getTime()));

				creditCardCharge.setCreditCardNumber(sellerCcMap.get(sellerCcTrans.getSellerCcId()).getLastFourDigits());
				creditCardCharge.setTransactionType(transactionTypeMap.get(sellerCcTrans.getTransacionType()));

				creditCardCharge.setStatus(sellerCcTrans.getStatus());
				CcTransReason reason = sellerCcTrans.getReason();
				if(reason !=null){
				creditCardCharge.setReasonCode(reason.getReasonCode().toString());
				creditCardCharge.setReasonDescription(reason.getReasonDescription());
				CcTransGroupReason ccTransGroupReason = sellerCcTrans.getReason().getCcTransGroupReason();
					if(ccTransGroupReason != null){
						creditCardCharge.setReasonGroupCode(ccTransGroupReason.getReasonGroupCode().toString());
						creditCardCharge.setReasonGroupDescription(ccTransGroupReason.getReasonGroupDescription());
					}

				}
				creditCardCharges.add(creditCardCharge);
			}

		}
		creditCardChargesResponse.setCreditCardCharges(creditCardCharges);
		return creditCardChargesResponse;
	}


 private static List<RowMeta> myOrderResponseRowMeta = new ArrayList<RowMeta>();

    static{
    	Map<String, DataMapper> mappers = new HashMap<String, DataMapper>();
    	for(Field f: MyOrderResponseDTO.class.getDeclaredFields()){
    		ResponseField rf = f.getAnnotation(ResponseField.class);
    		if(rf==null){
    			continue;
    		}
    		try{
	    		String classname = rf.mapperClass().getName();
	    		DataMapper mapper = mappers.get(classname);
	    		if(mapper==null){
	    			mapper = rf.mapperClass().newInstance();
	    			mappers.put(classname, mapper);
	    		}
	    		String method = "set" + f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1);
	    		Method setMethod = MyOrderResponseDTO.class.getMethod(method, f.getType());
	    		String[] fieldNames = rf.fieldName();
	    		myOrderResponseRowMeta.add(new RowMeta(fieldNames, f.getType(), setMethod, mapper));
    		}catch(Exception e){
    			e.printStackTrace();
    		}
    	}
    }


    public OrdersResponse addCSStubTransFlag (OrdersResponse ordersResponse, Map<String, Boolean> csStubTransFlag){
    	List<CSOrderDetailsResponse> list = ordersResponse.getOrder();
    	for (CSOrderDetailsResponse csOrderDetailsResponse : list){
    		String orderId = csOrderDetailsResponse.getTransaction().getOrderId();
    		for (Map.Entry<String, Boolean> entry : csStubTransFlag.entrySet()){
    			if (entry.getKey().equals(orderId))
    				csOrderDetailsResponse.getTransaction().setCsFlagged(entry.getValue());
    		}
    	}
    	return ordersResponse;
    }

    public CSSalesResponse addCSStubTransFlag (CSSalesResponse salesResponse, Map<String, Boolean> csStubTransFlag){
    	List<CSSaleDetailsResponse> list = salesResponse.getSale();
    	for (CSSaleDetailsResponse csSaleDetailsResponse : list){
    		String saleId = csSaleDetailsResponse.getTransaction().getSaleId();
    		for (Map.Entry<String, Boolean> entry : csStubTransFlag.entrySet()){
    			if (entry.getKey().equals(saleId))
    				csSaleDetailsResponse.getTransaction().setCsFlagged(entry.getValue());
    		}
    	}
    	return salesResponse;
    }

    public static TransactionSummaryResponse convertBizTransactionSummaryResponseToWebEntities(Map<String, String> orderStats, Map<String, String> listingStats){
    	TransactionSummaryResponse summaryResponse = new TransactionSummaryResponse();

    	summaryResponse.setIsTopBuyer(orderStats.get("IS_TOP_BUYER"));
    	summaryResponse.setBeyondAudienceYNFlag(orderStats.get("BEYOND_AUDIENCE_YN_FLAG"));
    	summaryResponse.setBeyondAudienceEffDate(orderStats.get("BEYOND_AUDIENCE_EFF_DATE"));
    	summaryResponse.setBeyondAudienceExpDate(orderStats.get("BEYOND_AUDIENCE_EXP_DATE"));
    	summaryResponse.setSemSegmentId(orderStats.get("SEM_SEGMENT_ID"));
    	summaryResponse.setSemSegmentName(orderStats.get("SEM_SEGMENT_NAME"));

    	BuysCountResponse buysCountResponse = new BuysCountResponse();

    	buysCountResponse.setPurchaseCount(orderStats.get("PURCHASE_COUNT"));
    	buysCountResponse.setCancelledOrders(orderStats.get("CANCELLED_BUYS_COUNT"));
    	buysCountResponse.setCompletedOrders(orderStats.get("COMPLETED_BUYS_COUNT"));
    	buysCountResponse.setUnconfirmedOrders(orderStats.get("UNCONFIRMED_BUYS_COUNT"));
    	buysCountResponse.setPurchaseTotal(new Money(orderStats.get("PURCHASE_TOTAL"), orderStats.get("CURRENCY")));
    	buysCountResponse.setOpenOrderPurchaseTotal(new Money(orderStats.get("OPEN_ORDER_PURCHASE_TOTAL"), orderStats.get("CURRENCY")));
    	buysCountResponse.setAvgOrderSize(new Money(orderStats.get("AVERAGE_ORDER_SIZE"), orderStats.get("CURRENCY")));
    	buysCountResponse.setEarliestEventDateUTC(orderStats.get("EARLIEST_EVENT_DATE_UTC"));
    	buysCountResponse.setEarliestEventDateLocal(orderStats.get("EARLIEST_EVENT_DATE_LOCAL"));

    	summaryResponse.setOrders(buysCountResponse);

    	SalesCountResponse salesCountResponse = new SalesCountResponse();

    	salesCountResponse.setCancelledSales(orderStats.get("CANCELLED_SALES_COUNT"));
    	salesCountResponse.setCompletedSales(orderStats.get("COMPLETED_SALES_COUNT"));
    	salesCountResponse.setUnconfirmedSales(orderStats.get("UNCONFIRMED_SALES_COUNT"));

    	summaryResponse.setSales(salesCountResponse);

    	if(listingStats != null) {

	    	ListingCountResponse listingCountResponse = new ListingCountResponse();

	    	listingCountResponse.setActiveListings(listingStats.get("ACTIVE_USER_LISTING_COUNT"));
	    	listingCountResponse.setDeletedListings(listingStats.get("DELETED_USER_LISTING_COUNT"));
	    	listingCountResponse.setInactiveListings(listingStats.get("INACTIVE_USER_LISTING_COUNT"));
	    	listingCountResponse.setIncompleteListings(listingStats.get("INCOMPLETE_USER_LISTING_COUNT"));
	    	listingCountResponse.setPendingLMSActivation(listingStats.get("PENDING_LMS_APPROVAL_USER_LISTING_COUNT"));
	    	listingCountResponse.setPendingLock(listingStats.get("PENDING_LOCK_USER_LISTING_COUNT"));

	    	summaryResponse.setListings(listingCountResponse);
    	}

    	return summaryResponse;
    }

	public static TransactionSummaryResponse convertBizUserTransactionSummaryResponseToWebEntities(Map<String, String> orderStats, UserDO user){
		TransactionSummaryResponse summaryResponse = new TransactionSummaryResponse();

		summaryResponse.setIsLargeSeller("FALSE");
		summaryResponse.setBeyondAudienceYNFlag(user.getBeyondAudienceYNFlag());
		summaryResponse.setSemSegmentId(user.getSemSegMentId());
		summaryResponse.setSemSegmentName(user.getSemSegMentName());

		BuysCountResponse buysCountResponse = new BuysCountResponse();

		buysCountResponse.setCancelledOrders(orderStats.get("cancelled_order_count"));
		buysCountResponse.setCompletedOrders(orderStats.get("completed_order_count"));
		buysCountResponse.setUnconfirmedOrders(orderStats.get("unconfirmed_order_count"));
		summaryResponse.setOrders(buysCountResponse);

		SalesCountResponse salesCountResponse = new SalesCountResponse();

		salesCountResponse.setCancelledSales(orderStats.get("cancelled_sale_count"));
		salesCountResponse.setCompletedSales(orderStats.get("completed_sale_count"));
		salesCountResponse.setUnconfirmedSales(orderStats.get("unconfirmed_sale_count"));
		summaryResponse.setSales(salesCountResponse);

		ListingCountResponse listingCountResponse = new ListingCountResponse();

		listingCountResponse.setActiveListings(orderStats.get("active_ticket_count"));
		listingCountResponse.setDeletedListings(orderStats.get("deleted_ticket_count"));
		listingCountResponse.setInactiveListings(orderStats.get("inactive_ticket_count"));
		listingCountResponse.setPendingLMSActivation(orderStats.get("pending_lms_count"));

		summaryResponse.setListings(listingCountResponse);
		summaryResponse.setUserId(String.valueOf(user.getUserId()));


		return summaryResponse;
	}

    public static boolean convertToBoolean(String obj){
    	boolean flag = false;
    	if (obj.equals("0")) flag = false;
    	if (obj.equals("1")) flag = true;
    	return flag;
    }

    protected static String getProperty(String propertyName, String defaultValue) {
    	if(defaultValue != null)
    		return MasterStubHubProperties.getProperty(propertyName, defaultValue);
    	return MasterStubHubProperties.getProperty(propertyName);
    }


	public static String filterResponse(String filters){
		String filterValue = null;
		if (filters != null && !filters.trim().isEmpty()) {
			String[] filterArr = filters.split(" AND ");
			for (String filter : filterArr) {
				String[] filterNameValue = filter.trim().split(":");
				if (filterNameValue.length == 2) {
					SalesFilterType filterName = SalesFilterType.fromString(filterNameValue[0].trim());
					filterValue = filterNameValue[1].trim();
					if (!filterValue.trim().isEmpty()) {
						if (filterName.name().equalsIgnoreCase(
								SalesFilterType.DELIVERYOPTION.name())) {
							filterValue = filterValue
									.replaceAll("[\\[\\]]", "").trim();
							continue;
						}
					} else {
						com.stubhub.domain.account.common.Error error = new com.stubhub.domain.account.common.Error(
								ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT,
								"Missing name:value pair for filters", filter);
						throw new AccountException(error);
					}
				}
			}
		}
		return filterValue;
	}
	public static EmailResponse convertEmailBizResponseToWebEntities(EmailLog emailLogs) throws Exception{
		EmailResponse emailLogsResponse = new EmailResponse();
		if (emailLogs.getAddressBcc() != null) emailLogsResponse.setAddressBcc(emailLogs.getAddressBcc());
		if (emailLogs.getAddressCc() != null) emailLogsResponse.setAddressCc(emailLogs.getAddressCc());
		if (emailLogs.getAddressFrom() != null) emailLogsResponse.setAddressFrom(emailLogs.getAddressFrom());
		if (emailLogs.getAddressTo() != null) emailLogsResponse.setAddressTo(emailLogs.getAddressTo());
		if (emailLogs.getDateAdded() != null) emailLogsResponse.setDateAdded(ValidationUtil.formatDate(emailLogs.getDateAdded(), emailLogs.getDateAdded().getTimeZone().getID()));
		if (emailLogs.getDateSent() != null) emailLogsResponse.setDateSent(ValidationUtil.formatDate(emailLogs.getDateSent(), emailLogs.getDateSent().getTimeZone().getID()));
		if (emailLogs.getEmailId() != null) emailLogsResponse.setEmailId(emailLogs.getEmailId());
		if (emailLogs.getFormat() != null) emailLogsResponse.setFormat(emailLogs.getFormat());
		if (emailLogs.getSubject() != null) emailLogsResponse.setSubject(emailLogs.getSubject());
		if (emailLogs.gettId() != null) emailLogsResponse.settId(emailLogs.gettId());
		if (emailLogs.getUserId() != null) emailLogsResponse.setUserId(emailLogs.getUserId());
		if (emailLogs.getbuyerOrderId() != null) emailLogsResponse.setbuyerOrderId(emailLogs.getbuyerOrderId());
		if (emailLogs.getBody() != null){
			Long clobLenth;
			try {
				clobLenth = emailLogs.getBody().length();
				if (clobLenth > Integer.MAX_VALUE){
					log.info("api_domain=account api_resource=emails api_method=getEmail status=success_with_error message=email body is truncated for emailId=" + emailLogs.getEmailId());
				}
				Reader reader = emailLogs.getBody().getCharacterStream();
				BufferedReader bReader = new BufferedReader(reader);
				StringWriter writer = new StringWriter();
				int c = -1;
				while ((c = bReader.read()) != -1)
					writer.write(c);
				writer.flush();
				writer.close();
				emailLogsResponse.setBody(writer.toString());
			} catch (Exception e) {
				throw new SQLException("Exception occured reading the email body");
			}
		}
		return emailLogsResponse;
	}
    public static EmailLogsResponse convertEmailLogsBizResponseToWebEntities(List<EmailLog> emailLogs) throws Exception{
    	 EmailLogsResponse response = new EmailLogsResponse();
    	 List<EmailResponse> list = new ArrayList<EmailResponse>();
    	  	Iterator<EmailLog> itr = emailLogs.iterator();
    	  	while (itr.hasNext()){
    	  		EmailLog emailLog = itr.next();
    	  		emailLog.setBody(null); // we don't need email body in the response
    	  		EmailResponse email = convertEmailBizResponseToWebEntities(emailLog);
    	  		list.add(email);
    	  	}
    	  	response.setEmail(list);
    	  	return response;
    }


    public List<CSSaleDetailsResponse> convertBizSaleResponseToWebOrderEntities(List<SalesTrans> salesTrans, UserType userType) throws ParseException {
    	List<CSSaleDetailsResponse> list = new ArrayList<CSSaleDetailsResponse>();
    	for (int i = 0 ; i < salesTrans.size() ; i++) {
    		SalesTrans saleTrans = salesTrans.get(i);
    		CSSaleDetailsResponse saleResponse = new CSSaleDetailsResponse();
    		CSSaleTransactionResponse transactionResponse = new CSSaleTransactionResponse();
    		String currency = "USD";
    		if (saleTrans.getCurrency() != null)  currency = saleTrans.getCurrency();
    		if (saleTrans.getSaleId() != null) transactionResponse.setSaleId(String.valueOf(saleTrans.getSaleId()));
    		if (saleTrans.getListingId() != null) transactionResponse.setListingId(saleTrans.getListingId());
    		if (saleTrans.getTotalCost() != null) transactionResponse.setTotalCost(new Money(saleTrans.getTotalCost().getAmount(), currency));
    		if (saleTrans.getBuyVAT() != null) transactionResponse.setBuyVAT(new Money(saleTrans.getBuyVAT().getAmount(), currency));
    		if (saleTrans.getSellVAT() != null) transactionResponse.setSellVAT(new Money(saleTrans.getSellVAT().getAmount(), currency));
    		if (saleTrans.getShippingFeeCost() != null) transactionResponse.setShippingFeeCost(new Money(saleTrans.getShippingFeeCost().getAmount(), currency));
    		if (saleTrans.getDiscountCost() != null) transactionResponse.setDiscountCost(new Money(saleTrans.getDiscountCost().getAmount(), currency));
    		if (saleTrans.getQuantityPurchased() != null) transactionResponse.setQuantityPurchased(String.valueOf(saleTrans.getQuantityPurchased()));
    		if (saleTrans.getTicketCost() != null && saleTrans.getQuantityPurchased() != null){
    			BigDecimal quantity = new BigDecimal(saleTrans.getQuantityPurchased());
    			BigDecimal perTicketCost = saleTrans.getTicketCost().getAmount().divide(quantity,2,BigDecimal.ROUND_HALF_UP);
    		    transactionResponse.setPricePerTicket(new Money(perTicketCost, currency));
    		}
    		if (saleTrans.getSection() != null) transactionResponse.setSection(saleTrans.getSection());
    		if (saleTrans.getRow() != null) transactionResponse.setRow(saleTrans.getRow());

    		if (saleTrans.getCancelled() != null) transactionResponse.setCancelled(saleTrans.getCancelled());
    		if (saleTrans.getSaleDateUTC() != null) transactionResponse.setSaleDateUTC(convertCalendar2String(saleTrans.getSaleDateUTC()));
    		EventResponse eventResponse = new EventResponse();
    		if (saleTrans.getEventId() != null) eventResponse.setEventId(saleTrans.getEventId());
    		if (saleTrans.getEventDateUTC() != null) eventResponse.setEventDateUTC(convertCalendar2String(saleTrans.getEventDateUTC()));
    		//if (doc.getFieldValue("EVENT_DATE_LOCAL") != null) eventResponse.setEventDateLocal(doc.getFieldValue("EVENT_DATE_LOCAL").toString());
    		//if (doc.getFieldValue("HIDE_EVENT_DATE") != null) eventResponse.setHideEventDate(convertToBoolean(doc.getFieldValue("HIDE_EVENT_DATE").toString()));
    		//if (doc.getFieldValue("VENUE_DESCRIPTION") != null) eventResponse.setVenueDescription(doc.getFieldValue("VENUE_DESCRIPTION").toString());
    		DeliveryResponse deliveryResponse = new DeliveryResponse();
    		int deliveryMethodId = 0;
    		if (saleTrans.getDeliveryMethodId() != null) {
    			deliveryResponse.setDeliveryMethodId(String.valueOf(saleTrans.getDeliveryMethodId()));
    			deliveryMethodId = Integer.valueOf(saleTrans.getDeliveryMethodId().intValue());
    			deliveryResponse.setDeliveryMethodDescription(DeliveryMethod.getDeliveryMethodById(deliveryMethodId).getDeliveryMethodDesc());
    			deliveryResponse.setDeliveryTypeId(String.valueOf(DeliveryMethod.getDeliveryMethodById(deliveryMethodId).getDeliveryTypeId()));
    			deliveryResponse.setDeliveryTypeDescription(DeliveryMethod.getDeliveryMethodById(deliveryMethodId).getDeliveryTypeDesc());
    		}
    		if (saleTrans.getExpectedArrivalDateUTC() != null) deliveryResponse.setExpectedArrivalDateUTC(convertCalendar2String(saleTrans.getExpectedArrivalDateUTC()));
    		if (saleTrans.getShipDateUTC() != null) deliveryResponse.setShipDateUTC(convertCalendar2String(saleTrans.getShipDateUTC()));
    		//if (saleTrans.getInHandDateUTC() != null) deliveryResponse.setNotInHand(!convertToBoolean(doc.getFieldValue("INHAND_IND").toString()));
    		if (saleTrans.getTrackingNumber() != null) deliveryResponse.setTrackingNumber(saleTrans.getTrackingNumber());
    		if (saleTrans.getSaleProcSubStatusCode() != null) deliveryResponse.setOrderProcSubStatusCode(saleTrans.getSaleProcSubStatusCode());
    		//if (doc.getFieldValue("ORDER_PROC_SUB_STATUS_DESC") != null) deliveryResponse.setOrderProcSubStatusDesc(doc.getFieldValue("ORDER_PROC_SUB_STATUS_DESC").toString());
    		if (UserType.BUYER == userType){
    			SubResponse subResponse = new SubResponse();
    			//if (doc.getFieldValue("TICKET_COST") != null) transactionResponse.setPricePerTicket(new Money(doc.getFieldValue("TICKET_COST").toString(), currency));
    			if (saleTrans.getBuyerId() != null) transactionResponse.setBuyerId(saleTrans.getBuyerId());
    			if (saleTrans.getBuyerContactId() != null) transactionResponse.setBuyerContactId(String.valueOf(saleTrans.getBuyerContactId()));
    			if (saleTrans.getInHandDateUTC() != null) deliveryResponse.setInHandDateUTC(convertCalendar2String(saleTrans.getInHandDateUTC()));
    			if (saleTrans.getSubbedFlag() != null) subResponse.setSubbedFlag(saleTrans.getSubbedFlag());
    			if (saleTrans.getSubbedOrderId() != null) subResponse.setSubbedOrderId(saleTrans.getSubbedOrderId());
    			saleResponse.setSubs(subResponse);
    		}
    		if (UserType.SELLER == userType){
    			SellerPayments sellerPayments = new SellerPayments();
    			//if (doc.getFieldValue("SELLER_ID") != null) transactionResponse.setSellerId(doc.getFieldValue("SELLER_ID").toString());
    			//if (doc.getFieldValue("CONFIRMED_IND") != null) transactionResponse.setSellerConfirmed(convertToBoolean(doc.getFieldValue("CONFIRMED_IND").toString()));
    			//if (doc.getFieldValue("SELLER_PAYOUT_AMOUNT") != null) transactionResponse.setSellerPayoutAmount(new Money(doc.getFieldValue("SELLER_PAYOUT_AMOUNT").toString(), currency));
    			//if (doc.getFieldValue("SELLER_PAYMENT_TYPE_ID") != null) transactionResponse.setSellerPaymentTypeId(doc.getFieldValue("SELLER_PAYMENT_TYPE_ID").toString());
    			saleResponse.setSellerPayments(sellerPayments);
    		}
    		saleResponse.setTransaction(transactionResponse);
    		saleResponse.setEvent(eventResponse);
    		saleResponse.setDelivery(deliveryResponse);
    		list.add(saleResponse);
    	}
    	return list;
    }

    private String convertCalendar2String(Calendar input){
    	String result = "";
    	String dateFormat = "yyyy-MM-dd'T'HH:mm:ssZ";
    	SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		result = sdf.format(input.getTime());
    	return result;
    }


}
