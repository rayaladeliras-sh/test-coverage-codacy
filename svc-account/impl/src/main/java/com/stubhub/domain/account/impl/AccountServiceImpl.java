package com.stubhub.domain.account.impl;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.annotation.PostConstruct;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.stubhub.domain.account.common.*;
import com.stubhub.domain.account.common.Error;
import com.stubhub.domain.account.datamodel.dao.ListingDAO;
import com.stubhub.domain.account.datamodel.dao.SystemSettingsDAO;
import com.stubhub.domain.account.datamodel.dao.UserDAO;
import com.stubhub.domain.account.datamodel.entity.*;
import com.stubhub.domain.account.helper.*;
import com.stubhub.domain.account.intf.*;
import com.stubhub.domain.account.util.ThreadLocalUtil;
import com.stubhub.domain.catalog.read.v3.intf.common.dto.response.CommonAttribute;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.codehaus.jackson.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import com.google.common.base.Predicate;
import com.stubhub.common.exception.InvalidArgumentException;
import com.stubhub.common.exception.RecordNotFoundForIdException;
import com.stubhub.common.exception.UserNotAuthorizedException;
import com.stubhub.domain.account.adapter.AccountRequestAdapter;
import com.stubhub.domain.account.adapter.AccountResponseAdapter;
import com.stubhub.domain.account.adapter.AccountResponseJsonAdapter;
import com.stubhub.domain.account.biz.impl.EventUtil;
import com.stubhub.domain.account.biz.intf.AccountSolrCloudBiz;
import com.stubhub.domain.account.biz.intf.CSStubTransFlagBO;
import com.stubhub.domain.account.biz.intf.EmailLogsBO;
import com.stubhub.domain.account.biz.intf.ListingBO;
import com.stubhub.domain.account.biz.intf.OrderProcStatusBO;
import com.stubhub.domain.account.biz.intf.OrderSolrBO;
import com.stubhub.domain.account.biz.intf.SalesTransBO;
import com.stubhub.domain.account.biz.intf.SellerPaymentSolrCloudBO;
import com.stubhub.domain.account.biz.intf.StubTransBO;
import com.stubhub.domain.account.biz.intf.StubTransUpdateRequest;
import com.stubhub.domain.account.biz.intf.StubnetUserBO;
import com.stubhub.domain.account.biz.intf.SubstitutionOrderBO;
import com.stubhub.domain.account.biz.intf.TransactionSummaryBO;
import com.stubhub.domain.account.biz.intf.UpsTrackingBO;
import com.stubhub.domain.account.biz.intf.UsedDiscountBO;
import com.stubhub.domain.account.biz.intf.UserContactBiz;
import com.stubhub.domain.account.common.enums.DeliveryMethod;
import com.stubhub.domain.account.common.enums.DeliveryOption;
import com.stubhub.domain.account.common.enums.ErrorCode;
import com.stubhub.domain.account.common.enums.ErrorType;
import com.stubhub.domain.account.common.enums.LocalizedMessageTypes;
import com.stubhub.domain.account.common.enums.TicketTrait;
import com.stubhub.domain.account.common.enums.UserType;
import com.stubhub.domain.account.common.exception.AccountException;
import com.stubhub.domain.account.common.exception.BuyerOrderException;
import com.stubhub.domain.account.common.util.OrderConstants;
import com.stubhub.domain.account.common.util.SecurityContextUtil;
import com.stubhub.domain.account.common.util.ValidationUtil;
import com.stubhub.domain.account.datamodel.dao.StubTransDetailDAO;
import com.stubhub.domain.account.intf.ReportAnIssueRequest.InHandDateChangeData;
import com.stubhub.domain.account.intf.ReportAnIssueRequest.ReplacementData;
import com.stubhub.domain.catalog.read.v3.intf.events.dto.response.Event;
import com.stubhub.domain.catalog.read.v3.intf.events.dto.response.SeatTrait;
import com.stubhub.domain.cs.common.util.MessageType;
import com.stubhub.domain.cs.dto.BPMMessageRequest;
import com.stubhub.domain.cs.dto.CsrNotesRequest;
import com.stubhub.domain.cs.dto.Parameter;
import com.stubhub.domain.fulfillment.clients.fulfillmentlabel.util.MessageSourceBundle;
import com.stubhub.domain.i18n.infra.soa.core.I18nServiceContext;
import com.stubhub.domain.i18n.services.localization.v1.utility.DataSourceMessageSource;
import com.stubhub.domain.infrastructure.soa.core.context.SHServiceContext;
import com.stubhub.domain.inventory.metadata.v1.event.DTO.EventMetadataResponse;
import com.stubhub.domain.inventory.metadata.v1.event.DTO.InvFulfillmentDetails;
import com.stubhub.newplatform.common.entity.Money;
import com.stubhub.newplatform.common.util.SecurityUtil;
import com.stubhub.newplatform.property.MasterStubHubProperties;
import com.stubhub.platform.utilities.webservice.security.ExtendedSecurityContext;
import com.stubhub.platform.utilities.webservice.shcontext.SHAPIContext;
import com.stubhub.platform.utilities.webservice.shcontext.SHAPIThreadLocal;

import static com.stubhub.domain.common.util.StringUtils.isNullorEmpty;

@Component("accountService")
public class AccountServiceImpl implements AccountService {
    private final static Logger log = LoggerFactory.getLogger(AccountServiceImpl.class);
    private static String api_domain = "account";
    private static String api_resource = "listings";

    private static int MAX_ALLOWED_CSR_REP_LENGTH = 100;

	private final static Long ORDER_SOURCE_ID_SUBBED = 8L;
	private final static String TRANSFER_EMAIL_ADDRESS = "transferEmailAddress";

    @Autowired
	@Qualifier("accountSolrCloudBiz")
    private AccountSolrCloudBiz accountSolrCloudBiz;

    @Autowired
    private ListingHelper listingHelper;

    @Autowired
    private SellerEligibilityHelper sellerEligibilityHelper;

    @Autowired
    private SecurityContextUtil securityContextUtil;

    @Autowired
    private ListingDAO listingDAO;

    @Autowired(required = false)
    @Qualifier("orderProcStatusBO")
    private OrderProcStatusBO orderProcStatusBO;

    @Autowired(required = false)
    @Qualifier("usedDiscountBO")
    private UsedDiscountBO usedDiscountBO;

    @Autowired(required = false)
    @Qualifier("stubTransBO")
    private StubTransBO stubTransBO;

    @Autowired(required = false)
    @Qualifier("upsTrackingBO")
    private UpsTrackingBO upsTrackingBO;

    @Autowired(required = false)
    @Qualifier("requestValidator")
    private RequestValidator requestValidator;

    @Autowired(required = false)
    @Qualifier("csSaleRequestValidator")
    private CSSaleRequestValidator csSaleRequestValidator;

    @Autowired(required = false)
    @Qualifier("saleTransBO")
    private SalesTransBO saleTransBO;

    @Autowired
    private OrderSolrBO orderSolrBO;

    @Autowired
    private CSStubTransFlagBO csStubTransFlagBO;

    @Autowired
    private LMSLocationHelper LmsLocationHelper;

    @Autowired
    private EventMetaHelper eventMetaHelper;

    @Autowired
    private CustomerServiceHelper customerServiceHelper;

    @Autowired
    private EmailLogsBO emailLogsBO;

    @Autowired
    private StubnetUserBO stubnetUserBO;

    @Autowired
    private EventUtil eventUtil;

    @Autowired(required = false)
    @Qualifier("substitutionOrderBO")
    private SubstitutionOrderBO substitutionOrderBO;

    @Autowired
    DataSourceMessageSource messageSource;

    @Autowired
    @Qualifier("userContactBiz")
    private UserContactBiz userContactBiz;

    @Autowired
    @Qualifier("listingBO")
    private ListingBO listingBO;
    @Autowired
    private StubTransDetailDAO stubTransDetailDAO;

    @Value("${dropOrderRate.enabled}")
    private boolean dropOrderRate;

    @Autowired
    @Qualifier("userDomainHelper")
    private UserDomainHelper userDomainHelper;

    @Autowired
    @Qualifier("transactionSummaryBO")
    private TransactionSummaryBO transactionSummaryBO;

    @Autowired
    @Qualifier("pricingRecHelper")
    private PricingRecHelper pricingRecHelper;

	@Autowired
	@Qualifier("sellerPaymentSolrCloudBO")
	private SellerPaymentSolrCloudBO sellerPaymentSolrCloudBO;

	@Autowired
	@Qualifier("messageSourceBundle")
	private MessageSourceBundle messageSourceBundle;

	@Autowired
    UserDAO userDAO;

    @Autowired
    private SystemSettingsDAO systemSettingsDAO;


    private ThreadPoolTaskExecutor listingThreadPool;

    @Autowired(required = false)
    BatchScoreServiceHelper batchScoreServiceHelper;

    private static final String INTERNAL_API_ALLOWED_APP="PosLite";

    private static final List<Long> VALID_STATUS_TO_UPDATE_BUYER_CONTACT = new ArrayList<Long>();
    public static final long PRIMARY_SELLER_GROUP_ID;
    private static final String PRIMARY_SELLER_GROUP_ID_NAME = "user.group.primary.seller.group.id";
    private static final String PRIMARY_SELLER_GROUP_ID_DEFAULT = "228369";
    protected static final String FLASH_SEAT_TRAIT_IDS;
    //protected static final Long FLASH_SEAT_TRAIT_ID;
    private static final String FLASH_SEAT_TRAIT_ID_NAME = "user.group.flash.seat.trait.id";
    private static final String FLASH_SEAT_TRAIT_ID_DEFAULT = "14912,15292,13701,15098,15090,14907,13688";
    private static final Long FLASH_SEAT_TRAIT_ID_EXTERNAL = 14912L;
    //LDD-2522 Don’t send transfer email address in get my sales and emails - for Ga, no seats and sellers in A user group
    private static final Long NOT_USE_TRANSFER_EMAIL_GROUP = 249404L;
    private static final Long TICKET_CLAIM_GROUP = 267912L;

    private LoadingCache<String, String> settingCache;
    private static final Long PARKING_PASS_TYPE_ID = 2L;
    private static final String PARKING_PASS = "Parking Pass";
    private static final String LOT = "LOT";
    private static final String TICKET_WITH_PARKING_PASS_FLAG = "ticket.with.parking.pass.flag";
    private static final String USER_GROUP_CTL_BTN = "user_group_ctl_btn";

    static {
        PRIMARY_SELLER_GROUP_ID = Long.parseLong(MasterStubHubProperties
                .getProperty(PRIMARY_SELLER_GROUP_ID_NAME, PRIMARY_SELLER_GROUP_ID_DEFAULT));
        FLASH_SEAT_TRAIT_IDS = MasterStubHubProperties
                .getProperty(FLASH_SEAT_TRAIT_ID_NAME, FLASH_SEAT_TRAIT_ID_DEFAULT);
        VALID_STATUS_TO_UPDATE_BUYER_CONTACT.add(45L);
        VALID_STATUS_TO_UPDATE_BUYER_CONTACT.add(42L);
        VALID_STATUS_TO_UPDATE_BUYER_CONTACT.add(43L);
        VALID_STATUS_TO_UPDATE_BUYER_CONTACT.add(1L);
        VALID_STATUS_TO_UPDATE_BUYER_CONTACT.add(46L);
        VALID_STATUS_TO_UPDATE_BUYER_CONTACT.add(4L);
        VALID_STATUS_TO_UPDATE_BUYER_CONTACT.add(5L);
        VALID_STATUS_TO_UPDATE_BUYER_CONTACT.add(6L);
        VALID_STATUS_TO_UPDATE_BUYER_CONTACT.add(7L);
    }

    @PostConstruct
    public void setup(){

        listingThreadPool = new ThreadPoolTaskExecutor();
        listingThreadPool.setMaxPoolSize(MasterStubHubProperties
                .getPropertyAsInt("account.listing.threadpool.poolsize", 100));
        listingThreadPool.setKeepAliveSeconds(MasterStubHubProperties
                .getPropertyAsInt("account.listing.threadpool.timeout", 15));
        listingThreadPool.setCorePoolSize(20);
        listingThreadPool.setQueueCapacity(0);
        listingThreadPool.setAllowCoreThreadTimeOut(true);
        listingThreadPool.setWaitForTasksToCompleteOnShutdown(true);
        listingThreadPool.setThreadGroup(Thread.currentThread().getThreadGroup());
        listingThreadPool.initialize();

        settingCache = CacheBuilder.newBuilder()
                .maximumSize(2)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build(new CacheLoader<String, String>() {
                    @Override
                    public String load(String key) throws Exception {
                        SystemSettings setting = systemSettingsDAO.findByName(key);
                        return setting == null ? "TRUE" : setting.getValue();
                    }
                });

    }

    @Override
    public ListingResponse getListing(String listingId, String includeFees,
            ExtendedSecurityContext securityContext) {
        log.info("get a listing listingId=" + listingId);
        return listingHelper.getListingById(securityContext.getUserGuid(), listingId, includeFees);
    }

    @Override
    public ListingsResponse getMyListings(SHServiceContext serviceContext,
                                          ExtendedSecurityContext securityContext, I18nServiceContext i18nContext, String sellerGuid,
                                          PaginationInput paginationInput, String sortType, String filters, String pricingRec, String locale,
                                          SummaryInput summaryInput, ListingInput listingInput, String mockScores) {
        ListingsResponse listingsResponse = null;
        Locale i18nLocale = i18nContext == null ? Locale.US : i18nContext.getLocale();
        try {

            String sellerId = null;
            if (securityContextUtil.isAuthz(serviceContext)){
                // from AuthZ
                UserDO userDO = userDAO.findUserByGuid(sellerGuid);
                if (userDO != null){
                    sellerId = String.valueOf(userDO.getUserId());
                }
            } else if (serviceContext.getExtendedSecurityContext() != null && StringUtils.equals(sellerGuid, serviceContext.getExtendedSecurityContext().getUserGuid())){
                // from FE user
                sellerId = serviceContext.getExtendedSecurityContext().getUserId();
            } else {
                // Neither Authz nor seller signin
                throw new UserNotAuthorizedException();
            }

            if (StringUtils.isEmpty(sellerId)) {
                log.error("api_domain=" + api_domain + " api_resource=" + api_resource
                        + " api_method=getMyListings status=error error_message=\"seller not found\"");
                ListingsResponse response = new ListingsResponse();
                response.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
                response.getErrors().add(new com.stubhub.domain.account.common.Error(
                        ErrorType.NOT_FOUND, ErrorCode.USER_NOT_FOUND, "", ""));
                return response;
            }

            log.debug("get listings for sellerId={} and locale={}",sellerId, locale);
            try {
                ListingSearchCriteria lsc = AccountRequestAdapter
                        .convertRequestToListingSearchCriteria(sellerId, paginationInput,
                                sortType, filters, summaryInput, listingInput, serviceContext, messageSource);
                lsc.setSellerId(new Long(sellerId));

                JsonNode jsonNode = accountSolrCloudBiz.getSellerListings(lsc);
                listingsResponse = AccountResponseJsonAdapter.convertJsonNodeToListingsResponse(jsonNode);

                // SIP-190 - ValueScore population added here due to
                // the overhead it would add to keeping Solr up-to-date
                if (ThreadLocalUtil.isCallerTypeInternal()) {
                    batchScoreServiceHelper.fetchAndPopulateListingsScores(listingsResponse, mockScores);
                }

                if (listingsResponse != null) {
                    List<Future<List<PricingRecommendation>>> pricingRecFutures = null;

                    if("true".equals(pricingRec)){
                        pricingRecFutures = getPricingRec(listingsResponse, sellerId, sellerGuid);
                    }
                    eventMetaHelper.setEventDescByLocaleAndMOT(listingsResponse.getListings(),
                            locale);

                    // localize seat trait name or fill the correct one if missing
                    // add localized content for delivery and fulfillment method
                    if (listingsResponse.getListings() != null) {
                        boolean needLocalizeSeatTrait = !"en-us".equalsIgnoreCase(locale);
                        List<ListingResponse> listings = listingsResponse.getListings();
                        Set<String> listingIds = new HashSet<String>();
                        for(ListingResponse listingResponse : listings) {
                            listingIds.add(listingResponse.getId());
                        }
                        Map<String, String> listingLastUpdatedBys = listingDAO.getListingsLastUpdates(listingIds);

                        for (ListingResponse resp : listings) {
                            if(!StringUtils.isEmpty(resp.getId()) && StringUtils.isNumeric(resp.getId())) {
                                 String lastUpdatedBy = listingLastUpdatedBys.get(resp.getId());
                                 if(lastUpdatedBy != null) {
                                     resp.setLastUpdatedBy(lastUpdatedBy);
                                 }
                            }
                        	listingHelper.addLocalizedMessagesForFmDm(resp, i18nLocale);
                            if (resp.getTicketTraits() != null) {
                            String eventId = resp.getEventId();
                            for (TicketTrait trait : resp.getTicketTraits()) {
                                if (trait.getName() == null || needLocalizeSeatTrait) {
                                    String localizedSeatTraitName = eventUtil
                                            .getLocalizedSeatTraitName(
                                                    Long.parseLong(trait.getId()), locale,
                                                    eventId);
                                    if (localizedSeatTraitName != null) {
                                        trait.setName(localizedSeatTraitName);
                                    }
                                }
                            }
                        }
                    }
                    }

                    if(pricingRecFutures != null && pricingRecFutures.size() > 0) {
                        getPricingRecFutures(listingsResponse,pricingRecFutures, sellerId);
                    }
                }
                log.info("api_domain=" + api_domain + " api_resource=" + api_resource
                        + " api_method=getMyListings status=success message=\"Successfully got listings\""
                        + " sellerId=" + sellerId);
            } catch (AccountException e) {
                log.error("api_domain=" + api_domain + " api_resource=" + api_resource
                        + " api_method=getMyListings status=succes_with_error error_message=AccountException occured while getting listings"
                        + " sellerId=" + sellerId, e);
                ListingsResponse response = new ListingsResponse();
                response.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
                response.getErrors().add(new com.stubhub.domain.account.common.Error(
                        ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT, "", ""));
                return response;
            } catch (Exception e) {
                log.error("api_domain=" + api_domain + " api_resource=" + api_resource
                        + " api_method=getMyListings status=error error_message=Exception occured while getting listings"
                        + " sellerId=" + sellerId, e);
                ListingsResponse response = new ListingsResponse();
                response.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
                response.getErrors().add(new com.stubhub.domain.account.common.Error(
                        ErrorType.SYSTEMERROR, ErrorCode.SYSTEM_ERROR, "", ""));
                return response;
            }
        } catch (UserNotAuthorizedException e) {
            log.error("api_domain=" + api_domain + " api_resource=" + api_resource
                    + " api_method=getMyListings status=error error_message=UserNotAuthorizedException occured while getting listings"
                    + " sellerGuid=" + sellerGuid, e);
            listingsResponse = new ListingsResponse();
            listingsResponse.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
            listingsResponse.getErrors().add(new com.stubhub.domain.account.common.Error(
                    ErrorType.AUTHENTICATIONERROR, ErrorCode.INVALID_SELLER, "", ""));
            return listingsResponse;
        }
        return listingsResponse;
    }

    @Override
    public ListingsResponse getMyListingsInternal(SHServiceContext serviceContext, ExtendedSecurityContext securityContext,
                                                  I18nServiceContext i18nContext, String sellerGuid, PaginationInput paginationInput,
                                                  String sortType, String listingsRequest, String pricingRec, String locale,
                                                  SummaryInput summaryInput, ListingInput listingInput, String mockScores) {
        long start= System.currentTimeMillis();

        log.info("getMyListingsInternal sellerGUID={} paginationInput={} sortType={}",
                sellerGuid, paginationInput, sortType);

        ListingsResponse listingsResponse = new ListingsResponse();
        if (!isAllowed(securityContext))  {
            log.info("method={} message={} sellerGUID={}","getMyListingsInternal","forbidden",sellerGuid);
            listingsResponse.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
            listingsResponse.getErrors().add(new com.stubhub.domain.account.common.Error(ErrorType.AUTHORIZATIONERROR,
                    ErrorCode.INVALID_SELLER, "", ""));
            return listingsResponse;
        }
        log.info("method={} message={} sellerGUID={}","getEventSalesDataInternal","authorized",sellerGuid);
        try {
            ThreadLocalUtil.setCallerTypeToInternal(true);
            listingsResponse = getMyListings(serviceContext, securityContext, i18nContext, sellerGuid, paginationInput,
                    sortType, listingsRequest, pricingRec, locale, summaryInput, listingInput, mockScores);
        } finally {
            ThreadLocalUtil.setCallerTypeToInternal(false);
            log.info("getMyListingsInternal sellerGUID={} paginationInput={} sortType={} message={} respTime={}",
                    sellerGuid, paginationInput, sortType, "CompletedRequest", System.currentTimeMillis() - start);
        }

        return listingsResponse;
    }


    @Override
    public ListingsResponse getListings(ExtendedSecurityContext securityContext, String sellerIds,
            String eventIds, String status, PaginationInput paginationInput) {
        ListingsResponse listingsResponse = null;
        if (securityContext == null || securityContext.getUserId() == null) {
            log.error("api_domain=" + api_domain + " api_resource=" + api_resource
                    + " api_method=getListings status=error error_message=User is not authorized to getListings"
                    + " sellerIds=" + sellerIds);
            listingsResponse = new ListingsResponse();
            listingsResponse.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
            listingsResponse.getErrors().add(new com.stubhub.domain.account.common.Error(
                    ErrorType.AUTHENTICATIONERROR, ErrorCode.INVALID_SELLER, "", ""));
            return listingsResponse;
        }
        try {
            if ((eventIds == null || "".equals(eventIds.trim())) && (sellerIds == null
                    || "".equals(sellerIds.trim()))) { return new ListingsResponse(); }
            if (paginationInput == null) {
                paginationInput = new PaginationInput();
                paginationInput.setRows(PaginationInput.DEFAULT_ENTRIES_PERPAGE);
                paginationInput.setStart(0);
            } else {
                if (paginationInput.getRows() <= 0
                        || paginationInput.getRows() > PaginationInput.DEFAULT_ENTRIES_PERPAGE) {
                    paginationInput.setRows(PaginationInput.DEFAULT_ENTRIES_PERPAGE);
                }
                if (paginationInput.getStart() <= 0) {
                    paginationInput.setStart(0);
                }
            }
            JsonNode jsonNode = accountSolrCloudBiz.getSellerListings(sellerIds, eventIds, status, paginationInput);
            listingsResponse = AccountResponseJsonAdapter.convertJsonNodeToListingsResponse(jsonNode);
            log.info("api_domain=" + api_domain + " api_resource=" + api_resource
                    + " api_method=getListings status=success message=\"Successfully got listings\""
                    + " sellerIds=" + sellerIds + " eventIds=" + eventIds);

        } catch (AccountException e) {
            log.error("api_domain=" + api_domain + " api_resource=" + api_resource
                    + " api_method=getListings status=succes_with_error error_message=AccountException occured while getting listings"
                    + " sellerIds=" + sellerIds + " eventIds=" + eventIds, e);
            ListingsResponse response = new ListingsResponse();
            response.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
            response.getErrors().add(new com.stubhub.domain.account.common.Error(
                    ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT, "", ""));
            return response;
        } catch (Exception e) {
            log.error("api_domain=" + api_domain + " api_resource=" + api_resource
                    + " api_method=getListings status=error error_message=Exception occured while getting listings"
                    + " sellerIds=" + sellerIds + " eventIds=" + eventIds, e);
            ListingsResponse response = new ListingsResponse();
            response.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
            response.getErrors().add(new com.stubhub.domain.account.common.Error(
                    ErrorType.SYSTEMERROR, ErrorCode.SYSTEM_ERROR, "", ""));
            return response;
        }
        return listingsResponse;
    }

    @Override
    public MyOrderListResponse getMyOrders(ExtendedSecurityContext securityContext,
            String buyerGUID, String[] sortType, String filter, PaginationInput paginationInput,
            String locale) {
        MyOrderListResponse myOrderListResponse = null;
        String buyerId = null;
        try {
            // Validate if User is Logged in
            securityContextUtil.validateUserGuid(securityContext, buyerGUID);
            buyerId = securityContext.getUserId();
            MyOrderSearchCriteria osc = AccountRequestAdapter.convertRequestToMyOrderSearchCriteria(
                    buyerId, paginationInput, sortType, filter);

            JsonNode jsonNode = accountSolrCloudBiz.getBuyerOrders(osc);
            myOrderListResponse = AccountResponseJsonAdapter.convertJsonNodeToMyOrderListEntities(jsonNode);
			if(myOrderListResponse!=null){
	            LmsLocationHelper.setLMSLocation4MyOrders(myOrderListResponse.getMyOrderList(),
	                    locale);
	            // i18n support, set event meta desc
	            eventMetaHelper.setEventDescByLocaleAndMOT(myOrderListResponse.getMyOrderList(), locale);

	            // localize seat trait name or fill the correct one if missing
	            if (myOrderListResponse.getMyOrderList() != null) {
	                boolean needLocalizeSeatTrait = !"en-us".equalsIgnoreCase(locale);
	                for (MyOrderResponse resp : myOrderListResponse.getMyOrderList()) {
	                    if (resp.getTicketTraits() != null) {
	                        Long eventId = resp.getEventId();
	                        for (TicketTrait trait : resp.getTicketTraits()) {
	                            if (trait.getName() == null || needLocalizeSeatTrait) {
	                                String localizedSeatTraitName = eventUtil
	                                        .getLocalizedSeatTraitName(
	                                                Long.parseLong(trait.getId()), locale,
	                                                String.valueOf(eventId));
	                                if (localizedSeatTraitName != null) {
	                                    trait.setName(localizedSeatTraitName);
	                                }
	                            }
	                        }
	                    }
	                    if(resp.getStubhubMobileTicket()==1){
							if(resp.getDeliveryOption() != null && resp.getDeliveryOption().equals(com.stubhub.domain.account.common.enums.DeliveryOption.BARCODE)){
								resp.setStubhubMobileTicket(1);
							}else if(resp.getDeliveryOption() != null && resp.getDeliveryOption().equals(com.stubhub.domain.account.common.enums.DeliveryOption.PDF) && resp.isAllowViewTicket()){
								resp.setStubhubMobileTicket(1);
							}else{
								resp.setStubhubMobileTicket(0);
							}
						}
	                }
	            }

	            log.info("api_domain=" + api_domain
	                    + " api_resource=myOrders api_method=getMyOrderList status=success message=\"Successfully got orders\" buyerId="
	                    + buyerId);
			}
        } catch (UserNotAuthorizedException e) {
            log.error("api_domain=" + api_domain
                    + " api_resource=myOrders api_method=getMyOrderList status exception message="
                    + e.getMessage(), e);
            throw new BuyerOrderException(
                    com.stubhub.domain.account.common.exception.BuyerOrderException.Error.Unauthorized);
        } catch (AccountException e) {
            log.error("api_domain=" + api_domain
                    + " api_resource=myOrders api_method=getMyOrderList status exception message="
                    + e.getMessage(), e);
            throw new BuyerOrderException(
                    com.stubhub.domain.account.common.exception.BuyerOrderException.Error.InvalidInput);
        } catch (Exception e) {
            log.error("api_domain=" + api_domain
                    + " api_resource=myOrders api_method=getMyOrderList status exception message="
                    + e.getMessage(), e);
            throw new BuyerOrderException(
                    com.stubhub.domain.account.common.exception.BuyerOrderException.Error.InternalServerError);
        }
        return myOrderListResponse;
    }

    private void populateSeatTraits(SaleResponse sale, Event event){
    	if(event != null){
			List<SeatTrait> eventSeatTraits = event.getSeatTraits();
			if(eventSeatTraits != null){
				for(TicketTrait ticketTrait:sale.getTicketTraits()){
    				for(SeatTrait eventSeatTrait:eventSeatTraits){
    					if(eventSeatTrait.getId().toString().equals(ticketTrait.getId())){
    						ticketTrait.setName(eventSeatTrait.getName());
    						ticketTrait.setType(eventSeatTrait.getType());
    						break;
    					}
    				}
				}
			}
    	}
    }

    private final Predicate<TicketTrait> flashSeatPredicate = new Predicate<TicketTrait>() {

		@Override
		public boolean apply(TicketTrait input) {
	        return FLASH_SEAT_TRAIT_ID_EXTERNAL.equals(Long.valueOf(input.getId()))
	                || FLASH_SEAT_TRAIT_IDS.contains(input.getId());
		}
	};

    private boolean getUserGroupCtlBtn(){
        boolean result = false;
        try {
            result = "TRUE".equalsIgnoreCase(settingCache.get(USER_GROUP_CTL_BTN));
            log.info("api_domain={} api_resource=sales api_method=getMySales, getUserGroupCtlBtn={}",api_domain, result);
        } catch (ExecutionException e) {
            String fullStackTrace = ExceptionUtils.getFullStackTrace(e);
            log.error("api_domain={} api_resource=sales api_method=getMySales status=error message=\"Exception occured in getUserGroupCtlBtn\", e.fullStackTrace={}",api_domain, fullStackTrace);
        }
        return result;
    }


    /**
     * DNFBE-559: 'Ticket with Parking Pass', should display buyer email address
     * @return
     */
    private boolean getTicketWithParkingPassFlag() {
        boolean ticketWithParkingPass = true;
        try {
            ticketWithParkingPass = "TRUE".equals(settingCache.get(TICKET_WITH_PARKING_PASS_FLAG));
            log.info("domain=domain.account method=getTicketWithParkingPassFlag ticketWithParkingPass={}", ticketWithParkingPass);
        } catch (Exception e) {
            String fullStackTrace = ExceptionUtils.getFullStackTrace(e);
            log.error("domain=domain.account method=getTicketWithParkingPassFlag _message=getting Ticket With Parking Pass Flag goes wrong, e={}", e);
        }
        return ticketWithParkingPass;
    }

    /**
     * DNFBE-559: 'Ticket with Parking Pass', should display buyer email address
     * when seatDetails have two kinds of tic_list_type_id (1 and 2), regard as ticket with parking pass.
     * @param saleResponse
     * @return
     */
    private boolean isParkingPass(SaleResponse saleResponse) {
        List<StubTransDetail> stubTransDetails = stubTransDetailDAO.getSeatDetails(Long.parseLong(saleResponse.getSaleId()));
        if (stubTransDetails != null && !stubTransDetails.isEmpty()) {
            for (StubTransDetail stubTransDetail : stubTransDetails) {
                Long tixListTypeId = stubTransDetail.getTicketListTypeId();
                String section = stubTransDetail.getSectionName();
                boolean result = PARKING_PASS_TYPE_ID.equals(tixListTypeId)
                        || LOT.equalsIgnoreCase(section)
                        || PARKING_PASS.equalsIgnoreCase(stubTransDetail.getSeatNumber());
                log.info("api_domain={} api_resource=sales api_method=getMySales method=isParkingPass, saleId={}, stubTransDtlId={}, tixListTypeId={}, sectionName={}",
                        api_domain, saleResponse.getSaleId(), stubTransDetail.getStubTransDtlId(), tixListTypeId, section);
                if (result) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * whether the user is in the NOT_USE_TRANSFER_EMAIL_GROUP
     * @param sellerGuid
     * @param sellerId
     * @return
     */
    private boolean isSellerInTheGroup(String sellerGuid, String sellerId) {
        boolean isSellerInTheGroup = false;
        try {
            isSellerInTheGroup = userDomainHelper.isUserMemberOfGroup(sellerGuid, NOT_USE_TRANSFER_EMAIL_GROUP);
        } catch (Exception e) {
            String fullStackTrace = ExceptionUtils.getFullStackTrace(e);
            log.error("api_domain={} api_resource=sales api_method=getMySales status=error message=\"Exception occured in isSellerInTheGroup\", sellerId={}, sellerGuid={}, e.fullStackTrace={}",
                    api_domain, sellerId, sellerGuid, fullStackTrace);
        }
        return isSellerInTheGroup;
    }

    private boolean isSellerInTicketClaimGroup(String sellerGuid, String sellerId) {
        boolean result = false;
        try {
            result = userDomainHelper.isUserMemberOfGroup(sellerGuid, TICKET_CLAIM_GROUP);
        } catch (Exception e) {
            String fullStackTrace = ExceptionUtils.getFullStackTrace(e);
            log.error("api_domain={} api_resource=sales api_method=getMySales status=error  message=\"Exception occured in isSellerInTicketClaimGroup\", sellerId={}, sellerGuid={}, e.fullStackTrace={}",
                    api_domain, sellerId, sellerGuid, fullStackTrace);
        }
        return result;
    }

    /**
     * DNFBE-559: if listing type is 'Ticket with Parking Pass', should display buyer email address
     * @param sale
     * @param isSellerInTheGroup
     * @return
     */
    private boolean isGAOrNoSeatsOrPP(SaleResponse sale, Boolean isSellerInTheGroup) {
        String seats = sale.getSeats();
        boolean ticWithPPFlag = getTicketWithParkingPassFlag();
        boolean isTicWithPP = isParkingPass(sale);

        boolean isGA = Boolean.TRUE.equals(sale.getGA()) || sale.getSeats().equalsIgnoreCase("General Admission");
        boolean isNoSeats = StringUtils.isEmpty(seats) || seats.equalsIgnoreCase("N/A");
        //LDD-2659: Domain account changes to support new flow

        log.info("api_domain={} api_resource=sales api_method=getMySales isGAOrNoSeatsOrPP saleId={}, isGA={}, isNoSeats={}, isTicWithPP={}, ticWithPPFlag={}, isSellerInTheGroup={}",
                api_domain, sale.getSaleId(), isGA, isNoSeats, isTicWithPP, ticWithPPFlag, isSellerInTheGroup);
        return isGA || isNoSeats || isSellerInTheGroup || (ticWithPPFlag && isTicWithPP);
    }

    /**
     * if deliveriesFlag is null , fail-back to call event-api as above
     * @param sale
     * @param isGAOrNoSeatsOrPP
     * @param event
     * @return
     */
    private String failOverToEventApi(SaleResponse sale, boolean isGAOrNoSeatsOrPP, Event event) {
        String transferEmailAddress = null;
        log.info("api_domain=" + api_domain
                        + " api_resource=sales api_method=getMySales failOverToEventApi message=can't get the deliveriesFlag via calling search API,fail over to call event api ,sale.getSaleId()={} sale.getEventId={}",
                sale.getSaleId(), sale.getEventId());

        if (event != null && sale.getExternalTransfer()) {
            for (CommonAttribute dynamicAttribute : event.getDynamicAttributes()) {
                if (dynamicAttribute.getName().equalsIgnoreCase(TRANSFER_EMAIL_ADDRESS)) {
                    transferEmailAddress = dynamicAttribute.getValue();
                }
            }
            log.info("api_domain=" + api_domain
                            + " api_resource=sales api_method=getMySales failOverToEventApi sale.getSaleId()={},sale.getGA()={},sale.getSeats()={},transferEmailAddress={}",
                    sale.getSaleId(), sale.getGA(), sale.getSeats() == null ? "" : sale.getSeats(), transferEmailAddress);
            if (transferEmailAddress != null) {
                // LDD-2522: Don’t send transfer email address in get my sales and emails - for Ga, no seats and sellers in A user group
                if (!isGAOrNoSeatsOrPP) {
                    sale.setBuyerEmail(transferEmailAddress);
                    log.info("api_domain=" + api_domain
                            + " api_resource=sales api_method=getMySales failOverToEventApi status=success message=\"userTransferredEmailAddress\""
                            + " saleId=" + sale.getSaleId() + " eventId=" + event.getId());
                }
            } else {
                log.info("api_domain=" + api_domain
                        + " api_resource=sales api_method=getMySales failOverToEventApi message=sale.transferEmailAddress is null ,saleId=" + sale.getSaleId() + " eventId=" + event.getId());
            }
        }
        return transferEmailAddress;
    }

  @Override
  public SalesResponse getMySales(SHServiceContext serviceContext, ExtendedSecurityContext securityContext, I18nServiceContext i18nContext,
                                  String sellerGuid, PaginationInput paginationInput, String sortType, String filters, String includePending,
                                  String includeSeatDetail, String locale, SummaryInput summaryInput, String noConfirmBtn) {
    Locale i18nLocale = i18nContext == null ? Locale.US : i18nContext.getLocale();
    SalesResponse salesResponse = null;
    try {
      String sellerId = null;
      if (securityContextUtil.isAuthz(serviceContext)) {
        // from AuthZ
        UserDO userDO = userDAO.findUserByGuid(sellerGuid);
        if (userDO != null) {
            sellerId = String.valueOf(userDO.getUserId());
        }
      } else if (serviceContext.getExtendedSecurityContext() != null && StringUtils.equals(sellerGuid, serviceContext.getExtendedSecurityContext().getUserGuid())){
        // from FE user
        sellerId = serviceContext.getExtendedSecurityContext().getUserId();
      } else {
        // Neither Authz nor seller signin
        throw new UserNotAuthorizedException();
      }

      if (StringUtils.isEmpty(sellerId)) {
        log.error("api_domain=" + api_domain + " api_resource=" + api_resource
                + " api_method=getMySales status=error error_message=\"seller not found\"");
        SalesResponse response = new SalesResponse();
        response.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
        response.getErrors().add(new com.stubhub.domain.account.common.Error(
                ErrorType.NOT_FOUND, ErrorCode.USER_NOT_FOUND, "", ""));
        return response;
      } else {
        try {
          SalesSearchCriteria ssc = AccountRequestAdapter.convertRequestToSalesSearchCriteria(sellerId,
              paginationInput, sortType, filters, includePending, summaryInput, serviceContext,
              messageSource);
          ssc.setSellerId(new Long(sellerId));
          JsonNode jsonNode = accountSolrCloudBiz.getSellerSales(ssc);
          salesResponse = AccountResponseJsonAdapter.convertJsonNodeToSalesResponse(jsonNode);
          eventMetaHelper.setEventDescByLocaleAndMOT(salesResponse.getSales(), locale);
          Set<String> eventIds = new HashSet<String>();

          for (SaleResponse sale : salesResponse.getSales()) {
            if(sale.getDeliveryMethodId() != null) {
              if(sale.getFulfillmentMethodId() != null) {
                sale.setDeliveryMethodDisplayName(messageSourceBundle.getWindowMessage(sale.getFulfillmentMethodId().longValue(),
                  sale.getDeliveryMethodId().longValue(), LocalizedMessageTypes.DELIVERY_METHOD_DISPLAY_NAME.toString(), i18nLocale));
              }
              sale.setDeliveryMethodLongAppInstruction(messageSourceBundle.getDeliveryMessage(
                  sale.getDeliveryMethodId().longValue(), LocalizedMessageTypes.LONG_APP_INSTRUCTION.toString(), i18nLocale));
              sale.setDeliveryMethodLongInstruction(messageSourceBundle.getDeliveryMessage(
                  sale.getDeliveryMethodId().longValue(), LocalizedMessageTypes.LONG_INSTRUCTION.toString(), i18nLocale));
              sale.setDeliveryMethodShortAppInstruction(messageSourceBundle.getDeliveryMessage(
                  sale.getDeliveryMethodId().longValue(), LocalizedMessageTypes.SHORT_APP_INSTRUCTION.toString(), i18nLocale));
              sale.setDeliveryMethodShortInstruction(messageSourceBundle.getDeliveryMessage(
                  sale.getDeliveryMethodId().longValue(), LocalizedMessageTypes.SHORT_INSTRUCTION.toString(), i18nLocale));
            }

            if(sale.getFulfillmentMethodId() != null) {
              sale.setFulfillmentMethodDisplayName(messageSourceBundle.getFulfillmentMessage(
                  sale.getFulfillmentMethodId().longValue(), LocalizedMessageTypes.NAME.toString(), i18nLocale));
              sale.setFulfillmentMethodLongAppInstruction(messageSourceBundle.getFulfillmentMessage(
                  sale.getFulfillmentMethodId().longValue(), LocalizedMessageTypes.LONG_APP_INSTRUCTION.toString(), i18nLocale));
              sale.setFulfillmentMethodLongInstruction(messageSourceBundle.getFulfillmentMessage(
                  sale.getFulfillmentMethodId().longValue(), LocalizedMessageTypes.LONG_INSTRUCTION.toString(), i18nLocale));
              sale.setFulfillmentMethodShortAppInstruction(messageSourceBundle.getFulfillmentMessage(
                  sale.getFulfillmentMethodId().longValue(), LocalizedMessageTypes.SHORT_APP_INSTRUCTION.toString(), i18nLocale));
              sale.setFulfillmentMethodShortInstruction(messageSourceBundle.getFulfillmentMessage(
                  sale.getFulfillmentMethodId().longValue(), LocalizedMessageTypes.SHORT_INSTRUCTION.toString(), i18nLocale));
            }

            if(!StringUtils.isEmpty(sale.getCourierName())){
              // use courier name as delivery method display name in case of courier if courier available
              sale.setDeliveryMethodDisplayName(sale.getCourierName());
            }
            if(!StringUtils.isEmpty(sale.getCourierSmallLogo())){
              String logoLocation = getProperty("courier.logo.location", "/cms/content-content/courier-icons");
              sale.setCourierSmallLogo(logoLocation+"/"+sale.getCourierSmallLogo());
            }

            eventIds.add(sale.getEventId());
          }

          Map<Long, Event> events = eventUtil.getEventsV3(eventIds, locale);
          for (SaleResponse sale : salesResponse.getSales()) {
              if (sale.getTicketTraits() != null && !sale.getTicketTraits().isEmpty())
                  populateSeatTraits(sale, events.get(Long.valueOf(sale.getEventId())));
          }

          boolean isPrimarySeller = isPrimarySeller(sellerGuid);

          for (SaleResponse sale : salesResponse.getSales()) {

            boolean outputBuyerDetailsFields = false;
            boolean externalTransfer = false;
            boolean isLocalDelivery = false;

            if (isPrimarySeller) {
              log.info("api_domain="+ api_domain +
                  " api_resource=sales api_method=getMySales message=\"Seller is a Primary seller \"");
              outputBuyerDetailsFields = true;
            } else if (sale.getDeliveryMethodId() != null
                    && Arrays.asList(
                        DeliveryMethod.Flash_Transfer.getDeliveryMethodId(),
                        DeliveryMethod.Mobile_Transfer.getDeliveryMethodId(),
                        DeliveryMethod.Mobile_Transfer_Seat_Geek.getDeliveryMethodId()
                    ).contains(sale.getDeliveryMethodId())) {
              log.info("api_domain="+ api_domain
                  + " api_resource=sales api_method=getMySales message=\"Delivery method is Flash or Mobile transfer\""
                  + " delivery_method_id=" + sale.getDeliveryMethodId());
              outputBuyerDetailsFields = true;
              externalTransfer = true;
            } else if (DeliveryOption.LOCALDELIVERY == sale.getDeliveryOption()) {
              log.info("api_domain="+ api_domain
                  + " api_resource=sales api_method=getMySales message=\"Delivery method is local delivery\""
                  + " delivery_method_id=" + sale.getDeliveryMethodId());
              outputBuyerDetailsFields = true;
              isLocalDelivery = true;
            } else if (Arrays.asList(DeliveryOption.PDF, DeliveryOption.MOBILE_TICKET).contains(sale.getDeliveryOption())) {
                log.info("api_domain="+ api_domain
                    + " api_resource=sales api_method=getMySales message=\"Delivery method is " + sale.getDeliveryOption().name() + "\""
                    + " delivery_method_id =" + sale.getDeliveryMethodId());
                outputBuyerDetailsFields = true;
            }

            //set ExternalTransfer to SaleResponse
            sale.setExternalTransfer(externalTransfer);

            if (outputBuyerDetailsFields) {
              log.debug("api_domain="
                  + api_domain
                  + " api_resource=sales api_method=getMySales status=success message=\"retrieve email, firstname and lastname of buyer : \""
                  + " buyerId=" + sale.getBuyerId());
              UserContact buyerContact = userContactBiz.getDefaultUserContactByOwernId(
                  Long.parseLong(sale.getBuyerId()));
              if (buyerContact != null) {
                sale.setBuyerEmail(buyerContact.getEmail());
                sale.setBuyerFirstName(buyerContact.getFirstName());
                sale.setBuyerLastName(buyerContact.getLastName());
                if(isLocalDelivery) {
                  sale.setBuyerPhoneNumber(buyerContact.getPhoneNumber());
                  sale.setBuyerPhoneCallingCode(buyerContact.getPhoneCountryCd());
                }
              } else {
                log.warn("api_domain="
                          + api_domain
                          + " api_resource=sales api_method=getMySales message=\"default contact not found for buyer : \""
                          + " buyerId=" + sale.getBuyerId());
              }
            }
          }

          //SELLAPI-3078: Show transfer email address from event v3 api instead of buyer email address
          Boolean isSellerInTheGroup = null;
          for (SaleResponse sale : salesResponse.getSales()) {
            Event event = events.get(Long.valueOf(sale.getEventId()));
            //  the FulfillmentMethod which is 18  can enter the process
            if(sale.getFulfillmentMethodId()!=null &&  (sale.getFulfillmentMethodId().compareTo(18) == 0)) {
              log.info("api_domain=" + api_domain + " api_resource=sales api_method=getMySales status=error message=\"start externalMobileTransfer flow\"" );
              // DNFBE-626:set deliverPrimaryName
              if(null!=sale.getDeliveryMethodId()) {
                Long leafGeo = null;
                if (event.getAncestors() != null && event.getAncestors().getGeographies() != null && !event.getAncestors().getGeographies().isEmpty()) {
                  leafGeo = event.getAncestors().getGeographies().get(event.getAncestors().getGeographies().size() - 1).getId();
                }
                sale.setDeliverPrimaryName(messageSourceBundle.getDeliveryMethodName(Long.valueOf(sale.getDeliveryMethodId()), i18nLocale, Long.valueOf(sale.getTicketMediumId()), sale.getGenreId() != null ? Long.valueOf(sale.getGenreId()) : null, leafGeo));
              }
              boolean flag=false;
              if(sale.getUrlTransInd()==null) {
                //LDD-2659: Domain account changes to support new flow
                if(isSellerInTheGroup==null)
                  isSellerInTheGroup=isSellerInTheGroup(sellerGuid, sellerId);
                boolean isGAOrNoSeatsOrPP = isGAOrNoSeatsOrPP(sale, isSellerInTheGroup);
                log.info("sale.getSaleId()={},sale.getEventId={},isGAOrNoSeatsOrPP={},isSellerInTheGroup={}", sale.getSaleId(), sale.getEventId(), isGAOrNoSeatsOrPP,isSellerInTheGroup);
                Long deliveriesFlag = sale.getDeliveriesFlag();
                String transferEmailAddress = null;
                //if deliveriesFlag is null , fail-back to call event-api as above
                if (deliveriesFlag == null) {
                  log.info("sale.getSaleId()={},delivery flag is null,fail-over to call catalog API", sale.getSaleId(), sale.getEventId(), isGAOrNoSeatsOrPP);
                  transferEmailAddress = failOverToEventApi(sale, isGAOrNoSeatsOrPP, event);
                }
                flag = !isGAOrNoSeatsOrPP && (((deliveriesFlag != null) && (deliveriesFlag == 1L)) || transferEmailAddress != null);
              } else{
                  log.info("sale={} uses new indicator={}",sale.getSaleId(), sale.getUrlTransInd());
                  flag=sale.getUrlTransInd();
              }
              if (flag)
                sale.setBuyerEmail(sale.getTransEmailAddr());
              Link sellerConfirmLink = new Link();
              boolean isSellerInTicketClaimGroup = isSellerInTicketClaimGroup(sellerGuid, sellerId);
              boolean userGroupCtlBtn = getUserGroupCtlBtn();
              boolean isNoConfirmBtn = "true".equalsIgnoreCase(noConfirmBtn);
              log.info("the userGroupCtlBtn is {}, noConfirmBtn is {}, saleId is {}",userGroupCtlBtn,isNoConfirmBtn,sale.getSaleId());
              if (flag||(sale.getUrlPartialTransferred()!=null&&sale.getUrlPartialTransferred())) {
                if (isNoConfirmBtn && userGroupCtlBtn) {
                  if (isSellerInTicketClaimGroup) {
                    sellerConfirmLink.setRel("sale.CLM_BTN");
                    sellerConfirmLink.setUri("/accountmanagement/sales/v1/seller/" + sellerGuid + "?filters=EVENTDATEUTC:[NOW TO 2099-01-01]");
                  } else {
                    sellerConfirmLink.setRel("sale.NONE_BTN");
                    sellerConfirmLink.setUri("/accountmanagement/sales/v1/seller/" + sellerGuid + "?filters=EVENTDATEUTC:[NOW TO 2099-01-01]");
                  }
                } else {
                  sellerConfirmLink.setRel("sale.sellerConfirmForOneButton");
                  sellerConfirmLink.setUri("/accountmanagement/sales/v1/seller/" + sellerGuid + "?filters=EVENTDATEUTC:[NOW TO 2099-01-01]");
                }
              } else {
                if (isNoConfirmBtn && userGroupCtlBtn) {
                  if (isSellerInTicketClaimGroup) {
                    sellerConfirmLink.setRel("sale.CLM_BTN");
                    sellerConfirmLink.setUri("/accountmanagement/sales/v1/seller/" + sellerGuid + "?filters=EVENTDATEUTC:[NOW TO 2099-01-01]");
                  } else {
                    sellerConfirmLink.setRel("sale.ACK_BTN");
                    sellerConfirmLink.setUri("/accountmanagement/sales/v1/seller/" + sellerGuid + "?filters=EVENTDATEUTC:[NOW TO 2099-01-01]");
                  }
                } else {
                  sellerConfirmLink.setRel("sale.sellerConfirmForTwoButton");
                  sellerConfirmLink.setUri("/accountmanagement/sales/v1/seller/" + sellerGuid + "?filters=EVENTDATEUTC:[NOW TO 2099-01-01]");
                }
              }
              List<Link> cpy = null;
              List<Link> links = sale.getLinks();
              if (links == null) {
                cpy = new ArrayList<Link>(1);
              } else {
                cpy=  new ArrayList<Link>(links);
              }
              cpy.add(sellerConfirmLink);
              sale.setLinks(cpy);
            }
          }

          if ("true".equalsIgnoreCase(includeSeatDetail)) {
            addTicketSeatToSales(salesResponse);
          }

          log.info("api_domain=" + api_domain
              + " api_resource=sales api_method=getMySales status=success message=\"Successfully got sales\""
              + " sellerId=" + sellerId);

        } catch (AccountException e) {
          log.error("api_domain=" + api_domain
              + " api_resource=sales api_method=getMySales status=success_with_error error_message=AccountException occured while getting sales"
              + " sellerId=" + sellerId, e);
          SalesResponse response = new SalesResponse();
          response.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
          response.getErrors().add(new com.stubhub.domain.account.common.Error(ErrorType.INPUTERROR,
              ErrorCode.INVALID_INPUT, e.getMessage(), ""));
          return response;
        } catch (Exception e) {
          log.error("api_domain=" + api_domain
              + " api_resource=sales api_method=getMySales status=error error_message=Exception occured while getting sales"
              + " sellerId=" + sellerId, e);
          SalesResponse response = new SalesResponse();
          response.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
          response.getErrors().add(new com.stubhub.domain.account.common.Error(ErrorType.SYSTEMERROR,
              ErrorCode.SYSTEM_ERROR, "", ""));
          return response;
        }

      }
    } catch (UserNotAuthorizedException e) {
      log.error("api_domain=" + api_domain
        + " api_resource=sales api_method=getMySales status=error error_message=seller Not Authorized - getSales"
        + " sellerGuId=" + sellerGuid, e);
      salesResponse = new SalesResponse();
      salesResponse.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
      salesResponse.getErrors().add(new com.stubhub.domain.account.common.Error(ErrorType.AUTHENTICATIONERROR,
        ErrorCode.INVALID_SELLER, "", ""));
      return salesResponse;
    }
    return salesResponse;
  }

    private boolean isPrimarySeller(String sellerGuid) {
        try {
        return userDomainHelper.isUserMemberOfGroup(sellerGuid,
                PRIMARY_SELLER_GROUP_ID);
        } catch (Throwable e) {
            log.warn("api_domain=" + api_domain
                    + " api_resource=sales api_method=getMySales status=error error_message=could not access user group service to check trusted seller group"
                    + " sellerGuId=" + sellerGuid, e);
            return false;
        }
    }

    private void addTicketSeatToSales(SalesResponse salesResponse) {

      if (salesResponse != null && salesResponse.getSales() != null) {
        for (SaleResponse sale : salesResponse.getSales()) {
          List<StubTransDetail> ticketSeats = stubTransDetailDAO.getSeatDetails(Long.valueOf(sale.getSaleId()));
          if (ticketSeats != null && ticketSeats.size() > 0) {
            List<SeatDetail> seatDetails = new ArrayList<SeatDetail>();
            for (StubTransDetail ts : ticketSeats) {
              SeatDetail seatDetail = new SeatDetail();
              seatDetail.setTixListTypeId(ts.getTicketListTypeId());
              seatDetail.setSection(ts.getSectionName());
              seatDetail.setSeatNum(ts.getSeatNumber());
              seatDetail.setRow(ts.getRowNumber());
              seatDetail.setIsGA(ts.getGeneralAdmissionIndicator());
              seatDetail.setSeatId(ts.getTicketSeatId());
              seatDetails.add(seatDetail);
            }
            sale.setSeatDetails(seatDetails);
          }
        }
      }
    }

    private static final ThreadLocal<SimpleDateFormat> reportAnIssueRequestDateFormat = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        }
    };

    private static final ThreadLocal<SimpleDateFormat> inhandDateFromCatalogFormat = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            // "earliestPossibleInhandDate": "2015-02-12T12:43:21-0700",
            // "latestPossibleInhandDate": "2015-06-18T13:10:00-0600",
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        }
    };

    private static final ThreadLocal<SimpleDateFormat> inhandDateToCSFormat = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                    "yyyy/MM/dd HH:mm:ss.SSS 'GMT'");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            return simpleDateFormat;
        }
    };

    private static final ThreadLocal<SimpleDateFormat> csrNoteDateFormat = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("MM/dd/yyyy");
        }
    };

    @Override
    public Response reportAnIssue(ExtendedSecurityContext securityContext, String saleIdString,
            ReportAnIssueRequest pRequest) {

        com.stubhub.domain.account.common.Response resp = new com.stubhub.domain.account.common.Response();
        resp.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());

        if (securityContext == null || securityContext.getUserId() == null) {
            log.error("api_domain=" + api_domain
                    + " api_resource=sales api_method=reportAnIssue status=error error_message=seller Not Authorized - reportAnIssue");
            resp.getErrors().add(new com.stubhub.domain.account.common.Error(
                    ErrorType.AUTHENTICATIONERROR, ErrorCode.INVALID_SELLER, "", ""));
            return Response.status(Status.UNAUTHORIZED).entity(resp).build();
        }

        String sellerGuid = securityContext.getUserGuid();

        // validate sale ID
        if (!ValidationUtil.isValidLong(saleIdString)) {
            log.error("api_domain=" + api_domain
                    + " api_resource=sales api_method=reportAnIssue status=error error_message=Invalid saleId in the url"
                    + " orderId=" + saleIdString);
            resp.getErrors().add(new com.stubhub.domain.account.common.Error(ErrorType.INPUTERROR,
                    ErrorCode.INVALID_INPUT, "Invalid sale ID", saleIdString));
            return Response.status(Status.BAD_REQUEST).entity(resp).build();
        }

        Long saleId = Long.parseLong(saleIdString);

        try {
            List<StubTrans> stubTrans = stubTransBO.getOrderProcSubStatus(saleId);

            // check sale exist or not
            if (stubTrans == null || stubTrans.isEmpty()) {
                log.error("api_domain=" + api_domain
                        + " api_resource=sales api_method=reportAnIssue status=error error_message=No matching record found"
                        + " saleId=" + saleId);
                resp.getErrors()
                        .add(new com.stubhub.domain.account.common.Error(ErrorType.NOT_FOUND,
                                ErrorCode.INVALID_INPUT, "Sale ID not found", saleIdString));
                return Response.status(Status.NOT_FOUND).entity(resp).build();
            }

            // check sale's owner
            StubTrans trans = stubTrans.get(0);
            if (!securityContext.getUserId().equals(String.valueOf(trans.getSellerId()))) {
                log.error("api_domain=" + api_domain
                        + " api_resource=sales api_method=reportAnIssue status=error error_message=sale does not belong to seller"
                        + " saleId=" + saleId + " sellerId=" + securityContext.getUserId());
                resp.getErrors()
                        .add(new com.stubhub.domain.account.common.Error(ErrorType.INPUTERROR,
                                ErrorCode.INVALID_INPUT, "sale does not belong to seller",
                                saleIdString));
                return Response.status(Status.BAD_REQUEST).entity(resp).build();
            }

            // check order's status
            Long orderProcStatusCode = trans.getOrderProcStatusCode();
            if (OrderConstants.OrderProcStatusCodeEnum_Shipped_ID.equals(orderProcStatusCode)
                    || OrderConstants.OrderProcStatusCodeEnum_Delivered_ID
                            .equals(orderProcStatusCode)
                    || OrderConstants.OrderProcStatusCodeEnum_Fulfilled_ID
                            .equals(orderProcStatusCode)
                    || OrderConstants.OrderProcStatusCodeEnum_SubsOffered_ID
                            .equals(orderProcStatusCode)
                    || OrderConstants.OrderProcStatusCodeEnum_Cancelled_ID
                            .equals(orderProcStatusCode)) {
                log.error("api_domain=" + api_domain
                        + " api_resource=sales api_method=reportAnIssue status=error error_message=Sale is in wrong status"
                        + " saleId=" + saleId);
                resp.getErrors()
                        .add(new com.stubhub.domain.account.common.Error(ErrorType.INPUTERROR,
                                ErrorCode.INVALID_INPUT, "Sale is in wrong status", saleIdString));
                return Response.status(Status.BAD_REQUEST).entity(resp).build();
            }

            Long newSubStatusCode = OrderConstants.OrderProcSubStatusCodeEnum_SubsInProcessSeller_ID;

            Long confirmFlowTrackId = null;

            Long activeCsFlag = OrderConstants.CsStubTransFlagsDesc_Sent_to_Sourcing_ID;

            String csrNote = "";

            BPMMessageRequest request = new BPMMessageRequest();
            request.setMessageType(MessageType.CONFIRM);

            List<Parameter> parameters = new ArrayList<Parameter>();

            Parameter orderIdP = new Parameter();
            orderIdP.setName("orderId");
            orderIdP.setValue(saleIdString);
            parameters.add(orderIdP);

            Parameter statusP = new Parameter();
            statusP.setName("status");
            String status = null;
            ReportAnIssueRequest.Type type = pRequest.getType();
//            Boolean sellerConfirmed = Boolean.FALSE;

            if (ReportAnIssueRequest.Type.INHANDDATECHANGE == type) {
                status = "InHandDateChange";

                newSubStatusCode = OrderConstants.OrderProcSubStatusCodeEnum_InhandDateChangeRequested_ID;
                confirmFlowTrackId = OrderConstants.ConfirmFlowTrack_INHAND_DATE_CHANGED_ID;
                activeCsFlag = OrderConstants.CsStubTransFlagsDesc_Late_Fulfillment_Notification_Seller_ID;

                Parameter dataP = new Parameter();
                dataP.setName("InHandDateChangeData");
                InHandDateChangeData data = pRequest.getInHandDateChangeData();
                if (data == null) {
                    resp.getErrors()
                            .add(new com.stubhub.domain.account.common.Error(ErrorType.INPUTERROR,
                                    ErrorCode.INVALID_INPUT, "missing", "inHandDateChangeData"));
                } else {
                    Date newDate = validateAndFetchInHandDate("newDate", data.getNewDate(), resp,
                            false);
                    if (newDate != null) {
                        StringBuilder valueBuilder = new StringBuilder("");

                        valueBuilder.append("<newDate>")
                                .append(inhandDateToCSFormat.get().format(newDate))
                                .append("</newDate>");

                        Date originalDate = validateAndFetchInHandDate("originalDate",
                                data.getOriginalDate(), resp, true);
                        if (originalDate != null) {
                            valueBuilder.append("<originalDate>")
                                    .append(inhandDateToCSFormat.get().format(originalDate))
                                    .append("</originalDate>");
                        }

                        Long eventId = trans.getEventId();
                        EventMetadataResponse eventDetails = eventUtil
                                .getEventDetailsV2(eventId);

                        Long fmId = stubTransBO.getFulfillmentMethodIdByTid(saleId);

                        String possibleInHandDateBoundary[]= new String[2];


                        boolean ableToChange = setPossibleInhandDateBoundary(eventDetails,fmId,possibleInHandDateBoundary);

                        if(!ableToChange){
                            log.error("api_domain=" + api_domain + " api_resource=sales"
                                    + " api_method=reportAnIssue" + " status=error"
                                    + " message=\"No available fulfillment method or it's pre-delivery ticket\""
                                    +"fmId=" + fmId + ",saleId=" + saleId);
                            resp.getErrors().add(new com.stubhub.domain.account.common.Error(ErrorType.SYSTEMERROR,
                                    ErrorCode.INVALID_INPUT, "No available fulfillment method or it's pre-delivery ticket", null));

                            return Response.status(Status.BAD_REQUEST).entity(resp).build();
                        }
                        String earliestPossibleInhandDate = possibleInHandDateBoundary[0];
                        String latestPossibleInhandDate = possibleInHandDateBoundary[1];
                        log.debug("earliestPossibleInhandDate =" + earliestPossibleInhandDate);
                        log.debug("latestPossibleInhandDate =" + latestPossibleInhandDate);

                        Date earliestInHandDateForEvent = null;
                        if(StringUtils.isNotEmpty(earliestPossibleInhandDate)) {
                            try {
                                earliestInHandDateForEvent = inhandDateFromCatalogFormat.get()
                                        .parse(earliestPossibleInhandDate);
                            } catch (Exception e) {
                                log.warn("when parse " + earliestPossibleInhandDate,
                                        e);
                            }
                        }

                        if (earliestInHandDateForEvent != null) {

                            if (newDate.before(earliestInHandDateForEvent)) {
                                log.error("api_domain=" + api_domain
                                        + " api_resource=sales api_method=reportAnIssue status=error error_message=new in hand date is too early, should be late than "
                                        + earliestInHandDateForEvent + " saleId=" + saleId);
                                resp.getErrors()
                                        .add(new com.stubhub.domain.account.common.Error(
                                                ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT,
                                                "newDate too early", "newDate"));
                                return Response.status(Status.BAD_REQUEST).entity(resp).build();
                            }

                            valueBuilder.append("<earliestInHandDateForEvent>")
                                    .append(inhandDateToCSFormat.get()
                                            .format(earliestInHandDateForEvent))
                                    .append("</earliestInHandDateForEvent>");
                        }

                        Date latestInHandDateForEvent = null;

                        if(StringUtils.isNotEmpty(latestPossibleInhandDate)){
                            try {
                                latestInHandDateForEvent = inhandDateFromCatalogFormat.get()
                                        .parse(latestPossibleInhandDate);
                            } catch (Exception e) {
                                log.warn("when parse " + latestPossibleInhandDate, e);
                            }
                        }



                        if (latestInHandDateForEvent != null) {

                            if (newDate.after(latestInHandDateForEvent)) {
                                log.error("api_domain=" + api_domain
                                        + " api_resource=sales api_method=reportAnIssue status=error error_message=new in hand date is too late, should be earlier than "
                                        + latestInHandDateForEvent + " saleId=" + saleId);
                                resp.getErrors()
                                        .add(new com.stubhub.domain.account.common.Error(
                                                ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT,
                                                "newDate too late", "newDate"));
                                return Response.status(Status.BAD_REQUEST).entity(resp).build();
                            }

                            valueBuilder.append("<latestInHandDateForEvent>")
                                    .append(inhandDateToCSFormat.get()
                                            .format(latestInHandDateForEvent))
                                    .append("</latestInHandDateForEvent>");
                        }
                        dataP.setValue(valueBuilder.toString());
                        parameters.add(dataP);

                        csrNote = "Seller changed the InHand date. New InhandDate : "
                                + csrNoteDateFormat.get().format(newDate);
                    }
                }

            } else if (ReportAnIssueRequest.Type.REPLACEMENT == type) {
                status = "Replacement";

                confirmFlowTrackId = OrderConstants.ConfirmFlowTrack_SUBS_OFFERED_ID;

                Parameter dataP = new Parameter();
                dataP.setName("ReplacementData");
                ReplacementData data = pRequest.getReplacementData();
                if (data == null) {
                    resp.getErrors()
                            .add(new com.stubhub.domain.account.common.Error(ErrorType.INPUTERROR,
                                    ErrorCode.INVALID_INPUT, "missing", "replacementData"));
                } else {
                    StringBuilder valueBuilder = new StringBuilder("");
                    valueBuilder.append("<ticket>");
                    valueBuilder.append("<section>").append(data.getSection()).append("</section>");
                    valueBuilder.append("<row>").append(data.getRow()).append("</row>");
                    String[] seats = data.getSeats() == null ? new String[] {}
                            : data.getSeats().split(",");
                    valueBuilder.append("<seats>");
                    for (String seat : seats) {
                        valueBuilder.append("<seat>");
                        valueBuilder.append(seat);
                        valueBuilder.append("</seat>");
                    }
                    valueBuilder.append("</seats>");
                    valueBuilder.append("</ticket>");
                    dataP.setValue(valueBuilder.toString());
                    parameters.add(dataP);
                }

                StringBuilder csrNoteStringBuilder = new StringBuilder("Substitution :");
                csrNoteStringBuilder.append("Seller has Replacements, contact seller!");
                csrNoteStringBuilder.append("Section : ").append(data.getSection());
                csrNoteStringBuilder.append(" Row : ").append(data.getRow());
                csrNoteStringBuilder.append(" Seats : ").append(data.getSeats());

                csrNote = csrNoteStringBuilder.toString();

            } else if (ReportAnIssueRequest.Type.NOTICKET == type) {
                status = "No ticket";

                confirmFlowTrackId = OrderConstants.ConfirmFlowTrack_NO_TICKETS_ID;
                csrNote = "Substitution :  Seller does not have tickets!.";
            }

            if (resp.getErrors().isEmpty()) {

                statusP.setValue(status);
                parameters.add(statusP);

                request.setParameter(parameters);

                String operatorId = getProperty("report.an.issue.operatorId", "svcacctmyaccount");

                // update stub_trans
                StubTransUpdateRequest stubTransUpdateRequest = new StubTransUpdateRequest(saleId);
                stubTransUpdateRequest.setOrderProcSubStatusCode(newSubStatusCode);
                stubTransUpdateRequest.setConfirmFlowTrackId(confirmFlowTrackId);
                stubTransUpdateRequest.setActiveCsFlag(activeCsFlag);
//                stubTransUpdateRequest.setSellerConfirmed(sellerConfirmed);
                if(ReportAnIssueRequest.Type.INHANDDATECHANGE == type){
                	stubTransUpdateRequest.setSellerConfirmed(false);
                }

                stubTransBO.updateOrder(operatorId, stubTransUpdateRequest);

                // insert CSRNotes - CS API
                CsrNotesRequest csrNotesRequest = new CsrNotesRequest();
                csrNotesRequest.setNote(csrNote);
                customerServiceHelper.addCSRNote(saleId, csrNotesRequest, sellerGuid, operatorId);

                // insert CsStubTransFlag? - confirmed with CS team no need actually

                // send bpm message to CS - CS API
                customerServiceHelper.sendBPMMessage(request, sellerGuid, operatorId);

                return Response.status(Status.OK).entity(resp).build();

            } else {

                return Response.status(Status.BAD_REQUEST).entity(resp).build();
            }

        } catch (Exception e) {
            log.error("api_domain=" + api_domain + " api_resource=sales"
                    + " api_method=reportAnIssue" + " status=error"
                    + " error_message=Exception occured while report An Issue", e);
            resp.getErrors().add(new com.stubhub.domain.account.common.Error(ErrorType.SYSTEMERROR,
                    ErrorCode.SYSTEM_ERROR, "System error occured", null));

            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(resp).build();
        }
    }

    private boolean setPossibleInhandDateBoundary(EventMetadataResponse eventMetadataResponse,Long fmId,String[] possibleInHandDateArray){

        List<InvFulfillmentDetails> fmds =  eventMetadataResponse.getFulfillmentMethods();
        for(InvFulfillmentDetails ifmd : fmds){
            if(fmId.equals(ifmd.getId())){
                if(ifmd.getEarliestInHandDate() != null){
                    possibleInHandDateArray[0] = ifmd.getEarliestInHandDate();
                }
                if(ifmd.getLatestInHandDate() != null){
                    possibleInHandDateArray[1] = ifmd.getLatestInHandDate();
                }
               return true;
            }
        }
        return false;
    }

    private Date validateAndFetchInHandDate(String field, String dateInRequest,
            com.stubhub.domain.account.common.Response resp, boolean optional) {
        if (dateInRequest == null) {
            if (!optional) {
                resp.getErrors().add(new com.stubhub.domain.account.common.Error(
                        ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT, "missing", field));
            }
        } else {
            try {
                return reportAnIssueRequestDateFormat.get().parse(dateInRequest);
            } catch (Exception e) {
                log.warn("when parse " + dateInRequest, e);
                if (!optional) {
                    resp.getErrors()
                            .add(new com.stubhub.domain.account.common.Error(ErrorType.INPUTERROR,
                                    ErrorCode.INVALID_INPUT, "wrongDateFormat", field));
                }
            }
        }
        return null;
    }

    @Override
    public OrderStatusResponse getOrderStatus(String orderId,
            ExtendedSecurityContext securityContext) {
        OrderStatusResponse response = new OrderStatusResponse();
        if (securityContext == null) {
            log.error("api_domain=" + api_domain
                    + " api_resource=orders api_method=getOrderStatus status=error error_message=get order statuses - Unauthorized access"
                    + " orderId=" + orderId);
            response.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
            response.getErrors()
                    .add(new com.stubhub.domain.account.common.Error(ErrorType.AUTHENTICATIONERROR,
                            ErrorCode.USER_NOT_AUTHORIZED, "User not authorized", null));

            return response;
        }
        if (!ValidationUtil.isValidLong(orderId)) {
            log.error("api_domain=" + api_domain
                    + " api_resource=orders api_method=getOrderStatus status=error error_message=Invalid orderId in the url"
                    + " orderId=" + orderId);
            response.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
            response.getErrors().add(new com.stubhub.domain.account.common.Error(
                    ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT, "Invalid order ID", orderId));
            return response;
        }

        try {
            List<OrderProcStatus> orderProc = orderProcStatusBO.getOrderStatus(orderId);
            if (orderProc == null || orderProc.isEmpty()) {
                log.error("api_domain=" + api_domain
                        + " api_resource=orders api_method=getOrderStatus status=succes_with_error error_message=No matching record found"
                        + " orderId=" + orderId);
                response.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
                response.getErrors()
                        .add(new com.stubhub.domain.account.common.Error(ErrorType.NOT_FOUND,
                                ErrorCode.INVALID_INPUT, "Order ID not found", orderId));
                return response;
            }
            response.setOrderProc(orderProc);
            response.setOrderId(orderId);
        } catch (Exception e) {
            log.error("api_domain=" + api_domain
                    + " api_resource=orders api_method=getOrderStatus status=succes_with_error error_message=Exception occured while getting order statuses"
                    + " orderId=" + orderId, e);
            response.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
            response.getErrors().add(new com.stubhub.domain.account.common.Error(
                    ErrorType.SYSTEMERROR, ErrorCode.SYSTEM_ERROR, "System error occured", null));
        }
        log.info("api_domain=" + api_domain
                + " api_resource=orders api_method=getOrderStatus status=succes message=\"Successfully got order status\""
                + " orderId=" + orderId);
        return response;
    }

    @Override
    public DiscountsResponse getDiscount(String orderId, ExtendedSecurityContext securityContext) {
        DiscountsResponse response = new DiscountsResponse();
        if (securityContext == null) {
            log.error("api_domain=" + api_domain + " api_resource=orders"
                    + " api_method=getDiscount" + " status=error"
                    + " error_message=Unauthorized access to getDiscount" + " orderId=" + orderId);
            response.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
            response.getErrors()
                    .add(new com.stubhub.domain.account.common.Error(ErrorType.AUTHENTICATIONERROR,
                            ErrorCode.USER_NOT_AUTHORIZED, "User not authorized", null));
            return response;
        }
        if (!ValidationUtil.isValidLong(orderId)) {
            log.error("api_domain=" + api_domain + " api_resource=orders"
                    + " api_method=getDiscount" + " status=error"
                    + " error_message=Invalid orderId in the url" + " orderId=" + orderId);
            response.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
            response.getErrors().add(new com.stubhub.domain.account.common.Error(
                    ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT, "Invalid order ID", orderId));
            return response;
        }
        try {
            List<List> discounts = usedDiscountBO.getDiscounts(orderId);
            if (discounts == null || discounts.isEmpty()) {
                log.error("api_domain=" + api_domain + " api_resource=orders"
                        + " api_method=getDiscount" + " status=success_with_error"
                        + " error_message=No discounts found" + " orderId=" + orderId);
                response.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
                response.getErrors().add(new com.stubhub.domain.account.common.Error(
                        ErrorType.NO_CONTENT, null, null, null));
                return response;
            }
            List<DiscountResponse> discountList = new ArrayList<DiscountResponse>();
            DiscountResponse discountresponse;
            for (List discount : discounts) {
                discountresponse = new DiscountResponse();
                Money money = new Money(discount.get(0).toString(), discount.get(1).toString());
                discountresponse.setUsedDiscount(money);
                discountresponse.setId(discount.get(2).toString());
                discountresponse.setType(discount.get(3).toString());
                discountresponse.setDescription(discount.get(4).toString());
                discountList.add(discountresponse);
            }
            response.setDiscounts(discountList);
            log.info("api_domain=" + api_domain + " api_resource=orders" + " api_method=getDiscount"
                    + " status=success" + " message=\"Successfully found discount details\""
                    + " orderId=" + orderId);
        } catch (Exception e) {
            log.error("api_domain=" + api_domain + " api_resource=orders"
                    + " api_method=getDiscount" + " status=error"
                    + " error_message=Exception occured while getting discount details"
                    + " orderId=" + orderId, e);
            response.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
            response.getErrors().add(new com.stubhub.domain.account.common.Error(
                    ErrorType.SYSTEMERROR, ErrorCode.SYSTEM_ERROR, "System error occured", null));
        }
        return response;
    }

    @Override
    public Response updateBuyerContactId(String orderId, BuyerContactRequest buyerContactRequest,
            ExtendedSecurityContext securityContext) {
        BuyerContactResponse response = new BuyerContactResponse();
        Response resp = null;
        resp = validateUrlParameters(securityContext, orderId);
        if (resp != null) { return resp; }
        resp = validateRequestPayload(buyerContactRequest);
        if (resp != null) { return resp; }
        try {
            List<StubTrans> stubTrans = stubTransBO.getOrderProcSubStatus(Long.valueOf(orderId));
            resp = validateOrderStatus(stubTrans, orderId);
            if (resp != null) { return resp; }
            List<UpsTracking> upsTracking = upsTrackingBO.checkUPSOrder(Long.valueOf(orderId));
            resp = validateUPSOrder(upsTracking, orderId);
            if (resp != null) { return resp; }
            int result = stubTransBO.updateBuyerContactId(Long.valueOf(orderId),
                    Long.valueOf(buyerContactRequest.getContactId()),
                    buyerContactRequest.getCsrRepresentative(), Calendar.getInstance());
        } catch (Exception e) {
            log.error("api_domain=" + api_domain + " api_resource=orders"
                    + " api_method=updateBuyerContactId" + " status=error"
                    + " error_message=Exception occured while updating buyer contact" + " orderId="
                    + orderId, e);
            response.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
            response.getErrors().add(new com.stubhub.domain.account.common.Error(
                    ErrorType.SYSTEMERROR, ErrorCode.SYSTEM_ERROR, "System error occured", null));
            return Response.status(Status.CONFLICT).entity(response).build();
        }
        URI uri;
        try {
            String uriString = getProperty("updateBuyerContactId.order.uri",
                    "https://api.stubhub.com/accountmanagement/orders/v1/{orderId}/buyerContactId");
            uriString = uriString.replace("{orderId}", orderId);
            uri = new URI(uriString);
        } catch (URISyntaxException e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
        log.info("api_domain=" + api_domain + " api_resource=orders"
                + " api_method=updateBuyerContactId status=success message=\"Buyer contact id has been updated\""
                + " orderid=" + orderId);
        return Response.status(Status.OK).location(uri).build();
    }

    public Response validateUPSOrder(List<UpsTracking> upsTracking, String orderId) {
        BuyerContactResponse response = new BuyerContactResponse();
        if (upsTracking == null || upsTracking.isEmpty()) {
            log.error("api_domain=" + api_domain + " api_resource=orders"
                    + " api_method=validateUPSOrder status=success_with_error error_message=Invalid UPS order"
                    + " orderId=" + orderId);
            response.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
            response.getErrors()
                    .add(new com.stubhub.domain.account.common.Error(ErrorType.BUSINESSERROR,
                            ErrorCode.INVALID_INPUT, "Invalid UPS order", orderId));
            return Response.status(Status.CONFLICT).entity(response).build();
        }
        return null;
    }

    public Response validateOrderStatus(List<StubTrans> stubTrans, String orderId) {
        BuyerContactResponse response = new BuyerContactResponse();
        if (stubTrans == null || stubTrans.isEmpty()) {
            log.error("api_domain=" + api_domain + " api_resource=orders"
                    + " api_method=validateOrderStatus status=success_with_error error_message=Order ID not found"
                    + " orderId=" + orderId);
            response.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
            response.getErrors().add(new com.stubhub.domain.account.common.Error(
                    ErrorType.NOT_FOUND, ErrorCode.INVALID_INPUT, "Order ID not found", orderId));
            return Response.status(Status.NOT_FOUND).entity(response).build();
        }
        Long orderProcSubStatusCode = stubTrans.get(0).getOrderProcSubStatusCode();

        if (!VALID_STATUS_TO_UPDATE_BUYER_CONTACT.contains(orderProcSubStatusCode)) {
            log.error("api_domain=" + api_domain + " api_resource=orders"
                    + " api_method=validateOrderStatus status=success_with_error error_message=Invalid Order proc sub status"
                    + " orderId=" + orderId);
            response.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
            response.getErrors()
                    .add(new com.stubhub.domain.account.common.Error(ErrorType.BUSINESSERROR,
                            ErrorCode.INVALID_INPUT, "Invalid order proc substatus", orderId));
            return Response.status(Status.CONFLICT).entity(response).build();
        }
        return null;
    }

    public Response validateRequestPayload(BuyerContactRequest buyerContactRequest) {
        String contactId = buyerContactRequest.getContactId();
        String csrRepresentative = buyerContactRequest.getCsrRepresentative();
        BuyerContactResponse response = new BuyerContactResponse();
        if (buyerContactRequest == null || StringUtils.trimToNull(contactId) == null
                || StringUtils.trimToNull(csrRepresentative) == null) {
            log.error("api_domain=" + api_domain + " api_resource=orders"
                    + " api_method=validateRequestPayload status=error error_message=Invalid request payload");
            response.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
            response.getErrors()
                    .add(new com.stubhub.domain.account.common.Error(ErrorType.INPUTERROR,
                            ErrorCode.INVALID_INPUT, "Invalid request payload", null));
            return Response.status(Status.BAD_REQUEST).entity(response).build();
        }
        if (!ValidationUtil.isValidLong(contactId)) {
            log.error("api_domain=" + api_domain + " api_resource=orders"
                    + " api_method=validateRequestPayload status=error error_message=Invalid contact id"
                    + " contactid=" + contactId);
            response.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
            response.getErrors().add(new com.stubhub.domain.account.common.Error(
                    ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT, "Invalid contactId", contactId));
            return Response.status(Status.BAD_REQUEST).entity(response).build();
        }
        buyerContactRequest.setCsrRepresentative(
                SecurityUtil.makeTextSafe(buyerContactRequest.getCsrRepresentative()));
        if (buyerContactRequest.getCsrRepresentative() != null && buyerContactRequest
                .getCsrRepresentative().length() > MAX_ALLOWED_CSR_REP_LENGTH) {
            log.error("api_domain=" + api_domain + " api_resource=orders"
                    + " api_method=validateRequestPayload status=error error_message=Max length exceeded for csr representative"
                    + "csr representative=" + csrRepresentative);
            response.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
            response.getErrors()
                    .add(new com.stubhub.domain.account.common.Error(ErrorType.INPUTERROR,
                            ErrorCode.MAX_LENGTH_EXCEEDED,
                            "Max length exceeded for csr representative", csrRepresentative));
            return Response.status(Status.BAD_REQUEST).entity(response).build();
        }
        return null;
    }

    public Response validateUrlParameters(ExtendedSecurityContext securityContext, String orderId) {
        BuyerContactResponse response = new BuyerContactResponse();
        if (securityContext == null) {
            log.error("api_domain=" + api_domain + " api_resource=orders"
                    + " api_method=validateUrlParameters status=error error_message=Unauthorized access"
                    + " orderId=" + orderId);
            response.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
            response.getErrors()
                    .add(new com.stubhub.domain.account.common.Error(ErrorType.AUTHENTICATIONERROR,
                            ErrorCode.USER_NOT_AUTHORIZED, "User not authorized", orderId));
            return Response.status(Status.UNAUTHORIZED).entity(response).build();
        }
        if (!ValidationUtil.isValidLong(orderId)) {
            log.error("api_domain=" + api_domain + " api_resource=orders"
                    + " api_method=validateUrlParameters status=error error_message=Invalid order id"
                    + " orderId=" + orderId);
            response.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
            response.getErrors().add(new com.stubhub.domain.account.common.Error(
                    ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT, "Invalid order id", orderId));
            return Response.status(Status.BAD_REQUEST).entity(response).build();
        }
        return null;
    }

    @Override
    public Response getCSOrderDetails(CSOrderDetailsRequest csOrderDetailsRequest,
            ExtendedSecurityContext securityContext) {
        String orderId = csOrderDetailsRequest.getOrderId();
        String proxiedId = csOrderDetailsRequest.getProxiedId();
        String row = csOrderDetailsRequest.getRow();
        List<Error> errorList = new LinkedList<Error>();
        OrdersResponse ordersResponse = new OrdersResponse();
        AccountResponseAdapter accountResponseAdapter = new AccountResponseAdapter();
        try {
            log.info("api_domain=" + api_domain
                    + " api_resource=csorderdetails api_method=getCSOrderDetails message=\"getting order details for\" orderId="
                    + orderId + " proxiedId=" + proxiedId);
            securityContextUtil.validateUserGuid(securityContext, securityContext.getUserGuid());
            errorList = requestValidator.validateCSOrderDetailsRequestFields(csOrderDetailsRequest);
            if (errorList != null && !errorList.isEmpty()) {
                ordersResponse.setErrors(errorList);
                return Response.status(Status.BAD_REQUEST).entity(ordersResponse).build();
            }
            SalesSearchCriteria ssc = AccountRequestAdapter
                    .convertCSOrderDetailsRequestToSalesSearchCriteria(csOrderDetailsRequest);
            if (proxiedId != null) ssc.setBuyerId(new Long(proxiedId));


				ssc.setStatus("ALL");
	            JsonNode jsonNode = accountSolrCloudBiz.getCSOrderDetails(ssc);

	            ordersResponse = AccountResponseJsonAdapter.convertJsonNodeToOrdersResponse(jsonNode, row, UserType.BUYER);

	            if (ordersResponse.getOrder().isEmpty()) {
	                return Response.status(Status.NOT_FOUND).entity(ordersResponse).build();
	            } else {
	                Map<String, Boolean> csStubTransFlag = csStubTransFlagBO
	                        .getCSStubTransFlag(AccountRequestAdapter.getOrderIds(ordersResponse));
	                ordersResponse = accountResponseAdapter.addCSStubTransFlag(ordersResponse,
	                        csStubTransFlag);
	                SalesSearchCriteria sscForSubbedOrders = new SalesSearchCriteria();
	                sscForSubbedOrders
	                        .setSubbedId(AccountRequestAdapter.concatenateOrderIds(ordersResponse));
	                sscForSubbedOrders.setStatus("ALL");
	                JsonNode mciResponseForSubbedOrders = accountSolrCloudBiz.getCSOrderDetails(sscForSubbedOrders);
	                ordersResponse = AccountResponseJsonAdapter.addSubbedOrderInfo(ordersResponse, mciResponseForSubbedOrders);
	                if (ordersResponse.getOrdersFound() > 0) {
	                    for (CSOrderDetailsResponse orderDetails : ordersResponse.getOrder()) {
	                    	// this only hit DAO
	                        String edd = orderSolrBO.getEddByTid(
	                                Long.parseLong(orderDetails.getTransaction().getOrderId()));
	                        orderDetails.getDelivery().setExpectedArrivalDateUTC(edd);
	                    }
	                }
	                log.info("api_domain=" + api_domain
	                        + " api_resource=csorderdetails api_method=getCSOrderDetails message=\"sending response for\" orderId="
	                        + orderId + " proxiedId=" + proxiedId);
	                return Response.status(Status.OK).entity(ordersResponse).build();
	            }

        } catch (UserNotAuthorizedException e) {
            log.error("api_domain=" + api_domain
                    + "api_resource=csorderdetails api_method=getOrderDetails" + "status=error "
                    + "error_message=UserNotAuthorizedException occured while getting cs order details."
                    + " orderId=" + orderId + ", proxiedId=" + proxiedId, e);
            ordersResponse.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
            ordersResponse.getErrors()
                    .add(new com.stubhub.domain.account.common.Error(ErrorType.AUTHENTICATIONERROR,
                            ErrorCode.USER_NOT_AUTHORIZED,
                            "User is not authorized to perform this operation", "userGUID"));
            return Response.status(Status.FORBIDDEN).entity(ordersResponse).build();
        } catch (AccountException e) {
            log.error("api_domain=" + api_domain
                    + "api_resource=csorderdetails api_method=getOrderDetails" + "status=error "
                    + "error_message=AccountException occured while getting cs order details."
                    + " orderId=" + orderId + ", proxiedId=" + proxiedId, e);
            ordersResponse.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
            ordersResponse.getErrors()
                    .add(new com.stubhub.domain.account.common.Error(ErrorType.INPUTERROR,
                            ErrorCode.INVALID_INPUT,
                            "AccountException occured while getting cs order details", null));
            return Response.status(Status.BAD_REQUEST).entity(ordersResponse).build();
        } catch (Exception e) {
            log.error("api_domain=" + api_domain
                    + "api_resource=csorderdetails api_method=getOrderDetails" + "status=error "
                    + "error_message=Exception occured while getting cs order details."
                    + " orderId=" + orderId + ", proxiedId=" + proxiedId, e);
            ordersResponse.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
            ordersResponse.getErrors()
                    .add(new com.stubhub.domain.account.common.Error(ErrorType.SYSTEMERROR,
                            ErrorCode.SYSTEM_ERROR,
                            "Exception occured while getting cs order details", null));
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ordersResponse).build();
        }
    }

    @Override
    public Response getCSSales(CSOrderDetailsRequest csOrderDetailsRequest,
            ExtendedSecurityContext securityContext) {
        String orderId = csOrderDetailsRequest.getOrderId();
        String proxiedId = csOrderDetailsRequest.getProxiedId();
        String row = csOrderDetailsRequest.getRow();
        List<Error> errorList = new LinkedList<Error>();
        OrdersResponse ordersResponse = new OrdersResponse();
        AccountResponseAdapter accountResponseAdapter = new AccountResponseAdapter();
        try {
            log.info("api_domain=" + api_domain
                    + " api_resource=cssales api_method=getCSSales message=\"getting order details for\" orderId="
                    + orderId + " proxiedId=" + proxiedId);
            securityContextUtil.validateUserGuid(securityContext, securityContext.getUserGuid());
            errorList = requestValidator.validateCSOrderDetailsRequestFields(csOrderDetailsRequest);
            if (errorList != null && !errorList.isEmpty()) {
                ordersResponse.setErrors(errorList);
                return Response.status(Status.BAD_REQUEST).entity(ordersResponse).build();
            }
            SalesSearchCriteria ssc = AccountRequestAdapter
                    .convertCSOrderDetailsRequestToSalesSearchCriteria(csOrderDetailsRequest);
            if (proxiedId != null) ssc.setSellerId(new Long(proxiedId));


				ssc.setStatus("ALL");
				JsonNode jsonNode = accountSolrCloudBiz.getCSOrderDetails(ssc);
				ordersResponse = AccountResponseJsonAdapter.convertJsonNodeToOrdersResponse(jsonNode, row, UserType.SELLER);

	            if (ordersResponse.getOrder().isEmpty()) {
	                ordersResponse.setOrdersFound(0);
	                return Response.status(Status.NOT_FOUND).entity(ordersResponse).build();
	            } else {
	            	PaymentsSearchCriteria paymentSC = new PaymentsSearchCriteria();
	            	paymentSC.setQ("orderID:"+AccountRequestAdapter.concatenateOrderIds(ordersResponse));
	            	paymentSC.setIncludeCreditMemo(true);

	            	JsonNode mciResponseFromPaymentsSchema = sellerPaymentSolrCloudBO.getCSSellerPayments(paymentSC);

	                ordersResponse = AccountResponseJsonAdapter.addSellerPaymentInfo(ordersResponse,
	                        mciResponseFromPaymentsSchema);
	                Map<String, Boolean> csStubTransFlag = csStubTransFlagBO
	                        .getCSStubTransFlag(AccountRequestAdapter.getOrderIds(ordersResponse));
	                ordersResponse = accountResponseAdapter.addCSStubTransFlag(ordersResponse,
	                        csStubTransFlag);
	                log.info("api_domain=" + api_domain
	                        + " api_resource=cssales api_method=getCSSales message=\"sending response for\" orderId="
	                        + orderId + " proxiedId=" + proxiedId);
	                return Response.status(Status.OK).entity(ordersResponse).build();
	            }

        } catch (UserNotAuthorizedException e) {
            log.error("api_domain=" + api_domain + " api_resource=cssales"
                    + " api_method=getCSSales" + "status=error "
                    + "error_message=UserNotAuthorizedException occured while getting cs order details."
                    + " orderId=" + orderId + ", proxiedId=" + proxiedId, e);
            ordersResponse.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
            ordersResponse.getErrors()
                    .add(new com.stubhub.domain.account.common.Error(ErrorType.AUTHENTICATIONERROR,
                            ErrorCode.USER_NOT_AUTHORIZED,
                            "User is not authorized to perform this operation", "userGUID"));
            return Response.status(Status.FORBIDDEN).entity(ordersResponse).build();
        } catch (AccountException e) {
            log.error("api_domain=" + api_domain + " api_resource=cssales"
                    + " api_method=getCSSales" + "status=error "
                    + "error_message=AccountException occured while getting cs order details."
                    + " orderId=" + orderId + ", proxiedId=" + proxiedId, e);
            ordersResponse.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
            ordersResponse.getErrors()
                    .add(new com.stubhub.domain.account.common.Error(ErrorType.INPUTERROR,
                            ErrorCode.INVALID_INPUT,
                            "AccountException occured while getting cs order details", null));
            return Response.status(Status.BAD_REQUEST).entity(ordersResponse).build();
        } catch (Exception e) {
            log.error("api_domain=" + api_domain + " api_resource=cssales"
                    + " api_method=getCSSales" + "status=error "
                    + "error_message=Exception occured while getting cs order details."
                    + " orderId=" + orderId + ", proxiedId=" + proxiedId, e);
            ordersResponse.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
            ordersResponse.getErrors()
                    .add(new com.stubhub.domain.account.common.Error(ErrorType.SYSTEMERROR,
                            ErrorCode.SYSTEM_ERROR,
                            "Exception occured while getting cs order details", null));
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ordersResponse).build();
        }
    }

    @Override
    public Response getTransactionSummary(TransactionSummaryRequest summaryRequest, SHServiceContext serviceContext,
            ExtendedSecurityContext securityContext) {
        String proxiedId = summaryRequest.getProxiedId();
        TransactionSummaryResponse summaryResponse = new TransactionSummaryResponse();
        List<Error> errorList = new LinkedList<Error>();

        try {
            log.info("api_domain=" + api_domain + " api_resource=summary"
                    + " api_method=getTransactionSummary + message=\"getting transaction summary for\" proxiedId="
                    + proxiedId);
            if(!securityContextUtil.isAuthz(serviceContext)){
                securityContextUtil.validateUserGuid(securityContext, securityContext.getUserGuid());
            }
            errorList = requestValidator.validateTransactionSummaryRequestFields(summaryRequest);
            if (errorList != null) {
                summaryResponse.setErrors(errorList);
                return Response.status(Status.BAD_REQUEST).entity(summaryResponse).build();
            }
			List<HashMap<String,String>> summaryStats = transactionSummaryBO.getUserTransactionSummary(Long.parseLong(proxiedId), summaryRequest.getCurrencyCode(),summaryRequest.getFullSummaryDetails());
			Map<String, String> orderStats = null;
			Map<String, String> listingStats = null;
			log.info("api_domain=" + api_domain + " api_resource=summary" + " api_method=getTransactionSummary  + message=\"getting transaction summary \" summaryStats.size()1=" + summaryStats.size());
			if(summaryStats != null && summaryStats.size() == 2)
			{
				log.info("api_domain=" + api_domain + " api_resource=summary" + " api_method=getTransactionSummary  + message=\"getting transaction summary \" summaryStats.size()2=" + summaryStats.size());
				orderStats = summaryStats.get(0);
				listingStats = summaryStats.get(1);
				summaryResponse = AccountResponseAdapter.convertBizTransactionSummaryResponseToWebEntities(orderStats, listingStats);
			}
			int buyerFlipCount = 0;
			if (summaryRequest.getBuyerFlip() != null && Boolean.parseBoolean(summaryRequest.getBuyerFlip())){
				if(null != orderStats.get("BUYER_FLIP_COUNT")){
					buyerFlipCount = Integer.parseInt(orderStats.get("BUYER_FLIP_COUNT").trim());
				}
				summaryResponse.setBuyerFlip(String.valueOf(buyerFlipCount));
			}
			//Changed as part of CSAPIS-336
			if(dropOrderRate){
				String dropOrderRate = "";
				if(orderStats.get("DROP_ORDER_RATE") != null)
					dropOrderRate = orderStats.get("DROP_ORDER_RATE");
				summaryResponse.setDrpOrderRate(dropOrderRate);
			}
			summaryResponse.setUserId(proxiedId);
			log.info("api_domain=" + api_domain + " api_resource=summary" + " api_method=getTransactionSummary + message=\"sending response for\" proxiedId=" + proxiedId);
			return Response.status(Status.OK).entity(summaryResponse).build();
		} catch (UserNotAuthorizedException e){
			log.error("api_domain=" + api_domain + "api_resource=summary api_method=getTransactionSummary" +
					"status=error " + "error_message=UserNotAuthorizedException occured while getting transaction summary." +
					" proxiedId=" + proxiedId, e);
			summaryResponse.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
			summaryResponse.getErrors().add(new com.stubhub.domain.account.common.Error(ErrorType.AUTHENTICATIONERROR, ErrorCode.USER_NOT_AUTHORIZED, "User is not authorized to perform this operation", "userGUID"));
			return Response.status(Status.FORBIDDEN).entity(summaryResponse).build();
		} catch (AccountException e){
			log.error("api_domain=" + api_domain + "api_resource=summary api_method=getTransactionSummary" +
					"status=error " + "error_message=AccountException occured while getting transaction summary." +
					" proxiedId=" + proxiedId, e);
			summaryResponse.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
			summaryResponse.getErrors().add(new com.stubhub.domain.account.common.Error(ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT, "AccountException occured while getting cs order details", null));
			return Response.status(Status.BAD_REQUEST).entity(summaryResponse).build();
		} catch (Exception e){
			log.error("api_domain=" + api_domain + "api_resource=summary api_method=getTransactionSummary" +
					"status=error " + "error_message=Exception occured while getting transaction summary." +
					" proxiedId=" + proxiedId, e);
			summaryResponse.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
			summaryResponse.getErrors().add(new com.stubhub.domain.account.common.Error(ErrorType.SYSTEMERROR, ErrorCode.SYSTEM_ERROR, "Exception occured while getting cs order details", null));
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(summaryResponse).build();
		}
	}

    @Override
    public Response getUserTransactionSummary(TransactionSummaryRequest summaryRequest, SHServiceContext serviceContext,
                                          ExtendedSecurityContext securityContext) {
        String proxiedId = summaryRequest.getProxiedId();
        TransactionSummaryResponse summaryResponse = new TransactionSummaryResponse();
        List<Error> errorList = new LinkedList<Error>();

        try {
            log.info("api_domain=" + api_domain + " api_resource=summary"
                    + " api_method=getTransactionSummary + message=\"getting transaction summary for\" proxiedId="
                    + proxiedId);
            if(!securityContextUtil.isAuthz(serviceContext)){
                securityContextUtil.validateUserGuid(securityContext, securityContext.getUserGuid());
            }
            errorList = requestValidator.validateTransactionSummaryRequestFields(summaryRequest);
            if (errorList != null) {
                summaryResponse.setErrors(errorList);
                return Response.status(Status.BAD_REQUEST).entity(summaryResponse).build();
            }
            UserDO user = userDAO.findUserById(proxiedId);
            Map<String, String> orderStats = new HashMap<String, String>();
            if(user.getSellerSupportTierSegMentId() == null ){
                Map orderStatsMap = stubTransBO.getUserSummaryOrderStats(Long.valueOf(proxiedId));
                if(orderStatsMap != null)
                    orderStats.putAll(orderStatsMap);
                Map saleStatsMap = stubTransBO.getUserSummarySaleStats(Long.valueOf(proxiedId));
                if(saleStatsMap != null)
                    orderStats.putAll(saleStatsMap);
                Map listingStatsMap = listingDAO.getUserSummaryTicketStats(Long.valueOf(proxiedId));
                if(listingStatsMap != null)
                    orderStats.putAll(listingStatsMap);
            }else{
                summaryResponse.setUserId(proxiedId);
                summaryResponse.setIsLargeSeller("TRUE");
            }
            if(orderStats.size() > 0){
                summaryResponse = AccountResponseAdapter.convertBizUserTransactionSummaryResponseToWebEntities(orderStats, user);
            }
            log.info("api_domain=" + api_domain + " api_resource=summary" + " api_method=getTransactionSummary + message=\"sending response for\" proxiedId=" + proxiedId);
            return Response.status(Status.OK).entity(summaryResponse).build();
        } catch (UserNotAuthorizedException e){
            log.error("api_domain=" + api_domain + "api_resource=summary api_method=getTransactionSummary" +
                    "status=error " + "error_message=UserNotAuthorizedException occured while getting transaction summary." +
                    " proxiedId=" + proxiedId, e);
            summaryResponse.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
            summaryResponse.getErrors().add(new com.stubhub.domain.account.common.Error(ErrorType.AUTHENTICATIONERROR, ErrorCode.USER_NOT_AUTHORIZED, "User is not authorized to perform this operation", "userGUID"));
            return Response.status(Status.FORBIDDEN).entity(summaryResponse).build();
        } catch (AccountException e){
            log.error("api_domain=" + api_domain + "api_resource=summary api_method=getTransactionSummary" +
                    "status=error " + "error_message=AccountException occured while getting transaction summary." +
                    " proxiedId=" + proxiedId, e);
            summaryResponse.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
            summaryResponse.getErrors().add(new com.stubhub.domain.account.common.Error(ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT, "AccountException occured while getting cs order details", null));
            return Response.status(Status.BAD_REQUEST).entity(summaryResponse).build();
        } catch (Exception e){
            log.error("api_domain=" + api_domain + "api_resource=summary api_method=getTransactionSummary" +
                    "status=error " + "error_message=Exception occured while getting transaction summary." +
                    " proxiedId=" + proxiedId, e);
            summaryResponse.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
            summaryResponse.getErrors().add(new com.stubhub.domain.account.common.Error(ErrorType.SYSTEMERROR, ErrorCode.SYSTEM_ERROR, "Exception occured while getting cs order details", null));
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(summaryResponse).build();
        }
    }

	protected String getProperty(String propertyName, String defaultValue) {
		if(defaultValue != null){
			return MasterStubHubProperties.getProperty(propertyName, defaultValue);
		}
		return MasterStubHubProperties.getProperty(propertyName);
	}


	@Override
	public SalesResponse getEventSalesData(
			ExtendedSecurityContext securityContext, String localeStr, String eventId,
			PaginationInput paginationInput, String sortType,
			String salesRequest, String sectionId, String zoneId, String priceType) {

        log.debug("getEventSalesData eventId={} paginationInput={} sortType={} salesRequest={} sectionId={} zoneId={} priceType={}",
                new Object[]{ eventId, paginationInput, sortType, salesRequest, sectionId, zoneId, priceType});

        SalesResponse salesResponse = null;
		String sellerId  = null;
		try {
			//Validate if User is Logged in
			securityContextUtil.validateUserGuid(securityContext, securityContext.getUserGuid());
			sellerId = securityContext.getUserId();
	        log.info("getEventSalesData eventId={} sellerId={} paginationInput={} sortType={} salesRequest={} sectionId={} zoneId={} priceType={}",
	                new Object[]{ eventId, sellerId, paginationInput, sortType, salesRequest, sectionId, zoneId, priceType});
			int numberOfRows = MasterStubHubProperties.getPropertyAsInt("event.sales.data.section.limit", 100);
			int totalNumberOfRows = MasterStubHubProperties.getPropertyAsInt("event.sales.data.limit", 150);
			String[] sectionIdArray = null;
			if(!isNullorEmpty(sectionId)){
				String sectionIdInput = sectionId;
				sectionIdInput.replace(",0", "");
				int sectionIdLimit = MasterStubHubProperties.getPropertyAsInt("event.sales.data.sectionId.limit", 75);
				String[] tempSectionIdArray = sectionIdInput.split(",");
				if(tempSectionIdArray.length<=sectionIdLimit){
					sectionIdArray = tempSectionIdArray;
				}else{
					sectionIdArray = Arrays.copyOfRange(sectionIdInput.split(","), 0, sectionIdLimit);
				}
				if(sectionIdArray.length>0){
					totalNumberOfRows = numberOfRows;
				}
			}

            if(paginationInput!=null){
                if(paginationInput.getRows()>totalNumberOfRows || paginationInput.getRows()==PaginationInput.DEFAULT_ENTRIES_PERPAGE){
                    paginationInput.setRows(totalNumberOfRows);
                }
//                if(paginationInput.getStart()+paginationInput.getRows()>totalNumberOfRows){
//                    salesResponse = new SalesResponse();
//                    return salesResponse;
//                }
            }
            boolean isSellerAccessAllowed=false;
			if(sellerId !=null){
				try {
					isSellerAccessAllowed = sellerEligibilityHelper.checkSellerEligibility();
					if(isSellerAccessAllowed){
						SalesSearchCriteria ssc = AccountRequestAdapter.convertEventSalesRequestToSalesSearchCriteria(sellerId,paginationInput, sortType, salesRequest, totalNumberOfRows, priceType);
						ssc.setEventId(new Long(eventId));
						if(sectionIdArray!=null && sectionIdArray.length>0){
							ssc.setSectionIds(sectionIdArray);
						}
						if(!isNullorEmpty(zoneId)){
							ssc.setZoneIds(zoneId.split(","));
						}
                        JsonNode jsonNode = accountSolrCloudBiz.getSellerEventSales(ssc);
                        salesResponse = AccountResponseJsonAdapter.convertJsonNodeToEventSalesData(jsonNode,
                                salesRequest, priceType, securityContext.getUserId());
						log.info("api_domain=" + api_domain
								+ " api_resource=sales api_method=getEventSalesData status=success message=\"Successfully got getEventSalesData\""
								+ " sellerId=" + sellerId);
					}else{
						log.error(
								"api_domain="
										+ api_domain
										+ " api_resource=sales api_method=getEventSalesData status=error error_message=seller Not Authorized - getEventSalesData"+" sellerGuId=" + securityContext.getUserGuid());
						salesResponse = new SalesResponse();
						return salesResponse;
					}

				} catch(AccountException e) {
					log.error(
							"api_domain="
									+ api_domain
									+ " api_resource=sales api_method=getEventSalesData status=success_with_error error_message=AccountException occured while getting sales"+" sellerId="
									+ sellerId, e);
					SalesResponse response = new SalesResponse();
					response.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
					response.getErrors().add(new com.stubhub.domain.account.common.Error(ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT, e.getMessage(), ""));
					return response;
				}catch(Exception e) {
					log.error(
							"api_domain="
									+ api_domain
									+ " api_resource=eventSales api_method=getEventSalesData status=error error_message=Exception occured while getting sales"+" sellerId="
									+ sellerId, e);
					SalesResponse response = new SalesResponse();
					response.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
					response.getErrors().add(new com.stubhub.domain.account.common.Error(ErrorType.SYSTEMERROR, ErrorCode.SYSTEM_ERROR, "", ""));
					return response;
				}
			}
		}catch (UserNotAuthorizedException e){
			log.error(
					"api_domain="
							+ api_domain
							+ " api_resource=sales api_method=getEventSalesData status=error error_message=seller Not Authorized - getEventSalesData"+" sellerGuId=" + securityContext.getUserGuid(), e);
			salesResponse = new SalesResponse();
			salesResponse.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
			salesResponse.getErrors().add(new com.stubhub.domain.account.common.Error(ErrorType.AUTHENTICATIONERROR, ErrorCode.INVALID_SELLER, "", ""));
			return salesResponse;
        }
        return salesResponse;
    }

    @Override
    public SalesResponse getEventSalesDataInternal(
            ExtendedSecurityContext securityContext, String localeStr, String eventId,
            PaginationInput paginationInput, String sortType,
            String salesRequest, String sectionIds, String zoneIds, String priceType, String mockScores, String quantity) {

        long start= System.currentTimeMillis();

        log.info("getEventSalesDataInternal eventId={} paginationInput={} sortType={} " +
                        "salesRequest={} sectionId={} zoneId={} priceType={}",
                eventId, paginationInput, sortType, salesRequest, sectionIds, zoneIds, priceType);

        if (!isAllowed(securityContext))  {
            log.info("method={} message={} eventId={}","getEventSalesDataInternal","forbidden",eventId);
            SalesResponse salesResponse = new SalesResponse();
            salesResponse.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
            salesResponse.getErrors().add(new com.stubhub.domain.account.common.Error(ErrorType.AUTHORIZATIONERROR,
                    ErrorCode.INVALID_SELLER, "", ""));
            return salesResponse;
        }
        log.info("method={} message={} eventId={}","getEventSalesDataInternal","authorized",eventId);

        ThreadLocalUtil.setCallerTypeToInternal(true);
        SalesResponse salesResponse = null;
        String sellerId  = null;
        try {
            sellerId = securityContext.getUserId();
            log.info("sellerIdUserId={}", sellerId);
            int numberOfRows = MasterStubHubProperties.getPropertyAsInt("event.sales.data.section.limit", 100);
            int totalNumberOfRows = MasterStubHubProperties.getPropertyAsInt("event.sales.data.limit", 150);

            String[] sectionIdArray = extractIds(sectionIds, "event.sales.data.sectionId.limit");
            String[] zoneIdArray = extractIds(zoneIds, "event.sales.data.zoneId.limit");

            if ((zoneIdArray != null && zoneIdArray.length > 0) || (sectionIdArray != null && sectionIdArray.length > 0)) {
                totalNumberOfRows = numberOfRows;
            }

            if (paginationInput!=null) {
                if(paginationInput.getRows()>totalNumberOfRows || paginationInput.getRows()==PaginationInput.DEFAULT_ENTRIES_PERPAGE){
                    paginationInput.setRows(totalNumberOfRows);
                }
            }

            try {
                SalesSearchCriteria ssc =
                        AccountRequestAdapter.convertEventSalesRequestToSalesSearchCriteria(sellerId,paginationInput,
                                                                                            sortType, salesRequest,
                                                                                        totalNumberOfRows, priceType);
                ssc.setEventId(new Long(eventId));
                if(sectionIdArray!=null && sectionIdArray.length>0){
                    ssc.setSectionIds(sectionIdArray);
                }
                if(zoneIdArray != null && zoneIdArray.length > 0){
                    ssc.setZoneIds(zoneIdArray);
                }

                if (quantity != null) {
                    ssc.setSoldQuantity(new Integer(quantity));
                }

                JsonNode jsonNode = accountSolrCloudBiz.getSellerEventSales(ssc);
                salesResponse = AccountResponseJsonAdapter.convertJsonNodeToEventSalesData(jsonNode,
                        salesRequest, priceType, securityContext.getUserId());
                batchScoreServiceHelper.fetchAndPopulateSalesScores(Long.valueOf(eventId),salesResponse,mockScores);

                log.info("method={} message={} eventId={}","getEventSalesDataInternal","completedSuccess",eventId);
            } catch(AccountException e) {
                log.error(
                        "api_domain="
                                + api_domain
                                + " api_resource=sales api_method=getEventSalesData status=success_with_error error_message=AccountException occured while getting sales"+" sellerId="
                                + sellerId, e);
                SalesResponse response = new SalesResponse();
                response.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
                response.getErrors().add(new com.stubhub.domain.account.common.Error(ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT, e.getMessage(), ""));

                return response;
            } catch(Exception e) {
                log.error(
                        "api_domain="
                                + api_domain
                                + " api_resource=eventSales api_method=getEventSalesData status=error error_message=Exception occured while getting sales"+" sellerId="
                                + sellerId, e);
                SalesResponse response = new SalesResponse();
                response.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
                response.getErrors().add(new com.stubhub.domain.account.common.Error(ErrorType.SYSTEMERROR, ErrorCode.SYSTEM_ERROR, "", ""));

                return response;
            }
        } finally {
            ThreadLocalUtil.setCallerTypeToInternal(false);
            log.info("getEventSalesDataInternal eventId={} paginationInput={} sortType={} " +
                            "salesRequest={} sectionId={} zoneId={} priceType={} message={} respTime={}",
                    eventId, paginationInput, sortType, salesRequest, sectionIds, zoneIds, priceType,
                    "CompletedRequest",System.currentTimeMillis()- start);
        }

        return salesResponse;
    }

    private String[] extractIds(String zoneIds, String propertyName) {
        String[] zoneIdArray = null;
        if (!isNullorEmpty(zoneIds)) {
            String zoneIdInput = zoneIds;
            zoneIdInput.replace(",0", "");
            int zoneIdLimit = MasterStubHubProperties.getPropertyAsInt(propertyName, 75);
            String[] tempZoneIdArray = zoneIdInput.split(",");
            if (tempZoneIdArray.length <= zoneIdLimit) {
                zoneIdArray = tempZoneIdArray;
            } else {
                zoneIdArray = Arrays.copyOfRange(zoneIdInput.split(","), 0, zoneIdLimit);
            }
        }
        return zoneIdArray;
    }

    private boolean isAllowed (ExtendedSecurityContext ctx) {
        return (null !=ctx && INTERNAL_API_ALLOWED_APP.equalsIgnoreCase(ctx.getApplicationName()));
    }


    @Override
    public Response getEmailLogs(SearchEmailCriteria searchCriteria,
            SHServiceContext serviceContext, ExtendedSecurityContext securityContext) {
        String proxiedId = serviceContext.getProxiedId();
        EmailLogsResponse emailLogsResponse = new EmailLogsResponse();
        List<Error> errorList = new LinkedList<Error>();
        try {
            log.info("api_domain={} api_resource=emails api_method=getEmailLogs message=\"getting email logs for\" proxiedId={}", api_domain, proxiedId);
            securityContextUtil.authenticateUser(serviceContext, securityContext);
            errorList = requestValidator.validateEmailHistoryRequestField(proxiedId,
                    searchCriteria);
            if (errorList != null) {
                emailLogsResponse.setErrors(errorList);
                return Response.status(Status.BAD_REQUEST).entity(emailLogsResponse).build();
            }
            List<EmailLog> emailList = emailLogsBO.getEmailLogs(Long.valueOf(proxiedId),
                    searchCriteria);
            if (emailList == null || emailList.isEmpty() || emailList.size() == 0) {
                log.error("api_domain={} api_resource=emails api_method=getEmailLogs status=error error_message=Email logs not found. proxiedId={}", api_domain, proxiedId);
                return Response.status(Status.OK).entity(emailLogsResponse).build();
            }
            emailLogsResponse = AccountResponseAdapter
                    .convertEmailLogsBizResponseToWebEntities(emailList);
            log.error(
                    "api_domain=" + api_domain + "api_resource=emails" + " api_method=getEmailLogs"
                            + "error_message=Sending response for proxiedId=" + proxiedId);
            return Response.status(Status.OK).entity(emailLogsResponse).build();
        } catch (UserNotAuthorizedException e) {
            log.error("api_domain=" + api_domain + "api_resource=emails api_method=getEmailLogs"
                    + "status=error "
                    + "error_message=UserNotAuthorizedException occured while getting email logs."
                    + " proxiedId=" + proxiedId, e);
            emailLogsResponse.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
            emailLogsResponse.getErrors()
                    .add(new com.stubhub.domain.account.common.Error(ErrorType.AUTHENTICATIONERROR,
                            ErrorCode.USER_NOT_AUTHORIZED,
                            "User is not authorized to perform this operation", "userGUID"));
            return Response.status(Status.FORBIDDEN).entity(emailLogsResponse).build();
        } catch (AccountException e) {
            log.error("api_domain=" + api_domain + "api_resource=emails api_method=getEmailLogs"
                    + "status=error "
                    + "error_message=AccountException occured while getting email logs."
                    + " proxiedId=" + proxiedId, e);
            emailLogsResponse.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
            emailLogsResponse.getErrors()
                    .add(new com.stubhub.domain.account.common.Error(ErrorType.INPUTERROR,
                            ErrorCode.INVALID_INPUT,
                            "AccountException occured while getting email logs", null));
            return Response.status(Status.BAD_REQUEST).entity(emailLogsResponse).build();
        } catch (Exception e) {
            log.error("api_domain=" + api_domain + "api_resource=emails api_method=getEmailLogs"
                    + "status=error " + "error_message=Exception occured while getting email logs."
                    + " proxiedId=" + proxiedId, e);
            emailLogsResponse.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
            emailLogsResponse.getErrors().add(new com.stubhub.domain.account.common.Error(
                    ErrorType.SYSTEMERROR, ErrorCode.SYSTEM_ERROR, e.getLocalizedMessage(), null));
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(emailLogsResponse).build();
        }
    }

    @Override
    public Response getEmail(String emailId, SHServiceContext serviceContext,
            ExtendedSecurityContext securityContext) {
        EmailResponse emailResponse = new EmailResponse();
        List<Error> errorList = new LinkedList<Error>();
        try {
            log.info("api_domain=" + api_domain + " api_resource=emails"
                    + " api_method=getEmail + message=\"getting email for\" emailId=" + emailId);
            securityContextUtil.authenticateUser(serviceContext, securityContext);
            if (!requestValidator.isValidLong("emailId", emailId)) {
                errorList.add(new Error(ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT,
                        "Invalid input provided", "emailId=" + emailId));
                emailResponse.setErrors(errorList);
                return Response.status(Status.BAD_REQUEST).entity(emailResponse).build();
            }
            EmailLog emailLog = emailLogsBO.getEmailById(Long.valueOf(emailId));
            if(securityContext == null || securityContext.getUserId() == null || !securityContext.getUserId().equals(emailLog.getUserId())){
                String userId = securityContext != null ? securityContext.getUserId() : "security context is not set";
                log.error("api_domain=" + api_domain + "api_resource=emails" + " api_method=getEmail"
                        + "message=user is Forbidden,emailId:" + emailId+",ctxUserId:"+ userId +"emailUserId:"+emailLog.getUserId());
                emailResponse.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
                emailResponse.getErrors()
                        .add(new com.stubhub.domain.account.common.Error(ErrorType.AUTHENTICATIONERROR,
                                ErrorCode.USER_NOT_AUTHORIZED,
                                "User is not authorized to perform this operation", "userId"));
                return Response.status(Status.FORBIDDEN).entity(emailResponse).build();
            }
            if (emailLog == null) {
                log.error("api_domain=" + api_domain + "api_resource=emails"
                        + " api_method=getEmailLog" + "status=error "
                        + "error_message=Email not found. emailId=" + emailId);
                return Response.status(Status.OK).entity(emailResponse).build();
            }
            emailResponse = AccountResponseAdapter.convertEmailBizResponseToWebEntities(emailLog);
            log.error("api_domain=" + api_domain + "api_resource=emails" + " api_method=getEmail"
                    + "message=Sending response for emailId=" + emailId);
            return Response.status(Status.OK).entity(emailResponse).build();
        } catch (UserNotAuthorizedException e) {
            log.error("api_domain=" + api_domain + "api_resource=emails api_method=getEmail"
                    + "status=error "
                    + "error_message=UserNotAuthorizedException occured while getting email for emailId="
                    + emailId);
            emailResponse.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
            emailResponse.getErrors()
                    .add(new com.stubhub.domain.account.common.Error(ErrorType.AUTHENTICATIONERROR,
                            ErrorCode.USER_NOT_AUTHORIZED,
                            "User is not authorized to perform this operation", "userGUID"));
            return Response.status(Status.FORBIDDEN).entity(emailResponse).build();
        } catch (AccountException e) {
            log.error("api_domain=" + api_domain + "api_resource=emails api_method=getEmail"
                    + "status=error "
                    + "error_message=AccountException occured while getting email for emailId="
                    + emailId);
            emailResponse.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
            emailResponse.getErrors()
                    .add(new com.stubhub.domain.account.common.Error(ErrorType.INPUTERROR,
                            ErrorCode.INVALID_INPUT,
                            "AccountException occured while getting email detail", null));
            return Response.status(Status.BAD_REQUEST).entity(emailResponse).build();
        } catch (Exception e) {
            log.error("api_domain=" + api_domain + "api_resource=emails api_method=getEmail"
                    + "status=error "
                    + "error_message=Exception occured while getting email for emailId=" + emailId);
            emailResponse.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
            emailResponse.getErrors()
                    .add(new com.stubhub.domain.account.common.Error(ErrorType.SYSTEMERROR,
                            ErrorCode.SYSTEM_ERROR, "Exception occured while getting email detail",
                            null));
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(emailResponse).build();
        }
    }

    @Override
    public Response updateCSOrderDetails(OrdersResponse request, SHServiceContext serviceContext,
            ExtendedSecurityContext securityContext) {
        OrdersResponse response = new OrdersResponse();
        List<Error> errorList = null;
        String orderId = null;
        try {
            log.info("api_domain=" + api_domain + " api_resource=csorderdetails"
                    + " api_method=updateCSOrderDetails + message=updating orderId=" + orderId);
            securityContextUtil.authenticateUser(serviceContext, securityContext);
            stubnetUserBO.isStubnetUser(serviceContext.getOperatorId());
            errorList = requestValidator.validateUpdateCSOrderDetails(serviceContext, request);
            orderId = request.getOrder().get(0).getTransaction().getOrderId();
            if (errorList != null && !errorList.isEmpty()) {
                errorList.add(new Error(ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT,
                        "Invalid input provided", "orderId=" + orderId));
                response.setErrors(errorList);
                return Response.status(Status.BAD_REQUEST).entity(response).build();
            }
            stubTransBO.updateOrder(serviceContext.getOperatorId(), request);
            return Response.status(Status.NO_CONTENT).build();
        } catch (UserNotAuthorizedException e) {
            log.error("api_domain=" + api_domain
                    + "api_resource=csorderdetails api_method=updateCSOrderDetails"
                    + "status=error "
                    + "error_message=UserNotAuthorizedException occured while updating orderId="
                    + orderId);
            response.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
            response.getErrors()
                    .add(new com.stubhub.domain.account.common.Error(ErrorType.AUTHENTICATIONERROR,
                            ErrorCode.USER_NOT_AUTHORIZED,
                            "User is not authorized to perform this operation", "userGUID"));
            return Response.status(Status.FORBIDDEN).entity(response).build();
        } catch (AccountException e) {
            log.error("api_domain=" + api_domain
                    + "api_resource=csorderdetails api_method=updateCSOrderDetails"
                    + "status=error "
                    + "error_message=AccountException occured while updating orderId=" + orderId);
            response.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
            response.getErrors()
                    .add(new com.stubhub.domain.account.common.Error(ErrorType.INPUTERROR,
                            ErrorCode.INVALID_INPUT,
                            "AccountException occured while updating order", null));
            return Response.status(Status.BAD_REQUEST).entity(response).build();
        } catch (Exception e) {
            log.error("api_domain=" + api_domain
                    + "api_resource=csorderdetails api_method=updateCSOrderDetails"
                    + "status=error " + "error_message=Exception occured while updating orderId="
                    + orderId);
            response.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
            response.getErrors()
                    .add(new com.stubhub.domain.account.common.Error(ErrorType.SYSTEMERROR,
                            ErrorCode.SYSTEM_ERROR, "Exception occured while updating order",
                            null));
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }

    @Override
    public Response createSubstitutionOrder(SubstitutionRequest request, String orderId,
            SHServiceContext serviceContext, ExtendedSecurityContext securityContext) {
        SubstitutionResponse response = new SubstitutionResponse();
        List<Error> errorList = null;
        try {
            log.info("api_domain=" + api_domain + " api_resource=orders"
                    + " api_method=createSubstitutionOrder + message=\"creating subs for\" orderId="
                    + orderId);
            securityContextUtil.authenticateUser(serviceContext, securityContext);
            // stubnetUserBO.isStubnetUser(serviceContext.getOperatorId());
            errorList = requestValidator.validateCreateSubOrderRequest(request, orderId,
                    serviceContext);
            if (errorList != null && !errorList.isEmpty()) {
                errorList.add(new Error(ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT,
                        "Invalid input provided", "orderId=" + orderId));
                response.setErrors(errorList);
                return Response.status(Status.BAD_REQUEST).entity(response).build();
            }
            String operatorId = serviceContext.getOperatorId();
            if (StringUtils.isEmpty(operatorId)) {
                log.info("api_domain=" + api_domain + " api_resource=orders"
                        + " api_method=createSubstitutionOrder + message=\"operatorId still null\"");
                operatorId = request.getOperatorId();
            }
            long newTid = substitutionOrderBO.createOrder(request, orderId, operatorId, ORDER_SOURCE_ID_SUBBED);
            response.setNewOrderId(newTid);
            return Response.status(Status.CREATED).entity(response).build();
        } catch (UserNotAuthorizedException e) {
            log.error("api_domain=" + api_domain
                    + "api_resource=orders api_method=createSubstitutionOrder" + "status=error "
                    + "error_message=UserNotAuthorizedException occured while creating subs for orderId="
                    + orderId, e);
            response.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
            response.getErrors()
                    .add(new com.stubhub.domain.account.common.Error(ErrorType.AUTHENTICATIONERROR,
                            ErrorCode.USER_NOT_AUTHORIZED,
                            "User is not authorized to perform this operation", "userGUID"));
            return Response.status(Status.UNAUTHORIZED).entity(response).build();
        } catch (InvalidArgumentException e) {
            log.error("api_domain=" + api_domain
                    + "api_resource=orders api_method=createSubstitutionOrder" + "status=error "
                    + "error_message=InvalidArgumentException occured while creating subs for orderId="
                    + orderId, e);
            response.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
            response.getErrors().add(new com.stubhub.domain.account.common.Error(
                    ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT, e.getLocalizedMessage(), null));
            return Response.status(Status.BAD_REQUEST).entity(response).build();
        } catch (RecordNotFoundForIdException e) {
            log.error("api_domain=" + api_domain
                    + "api_resource=orders api_method=createSubstitutionOrder" + "status=error "
                    + "error_message=RecordNotFoundForIdException occured while creating subs for orderId="
                    + orderId, e);
            response.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
            response.getErrors().add(new com.stubhub.domain.account.common.Error(
                    ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT, e.getLocalizedMessage(), null));
            return Response.status(Status.NOT_FOUND).entity(response).build();
        } catch (Exception e) {
            log.error("api_domain=" + api_domain
                    + "api_resource=orders api_method=createSubstitutionOrder" + "status=error "
                    + "error_message=Exception occured while creating subs for orderId=" + orderId,
                    e);
            response.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
            response.getErrors()
                    .add(new com.stubhub.domain.account.common.Error(ErrorType.SYSTEMERROR,
                            ErrorCode.SYSTEM_ERROR,
                            "Exception occured while creating subs for orderId=" + orderId, null));
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }

    @Override
    public FulfillmentSpecificationResponse getFulfillmentSpecification(
            FulfillmentSpecificationRequest fulfillmentSpecificationRequest) {
        // load fulfillment specification from mci
		FulfillmentSpecificationResponse fulfillmentSpecificationResponse = null;
        JsonNode jsonNode = accountSolrCloudBiz.getByIdList("inventory", "id", Arrays.asList(fulfillmentSpecificationRequest.getListings().split(",")));
        fulfillmentSpecificationResponse = AccountResponseJsonAdapter.convertToFulfillmentSpec(jsonNode);
        // if with user details,load user contact details from DB
        if (fulfillmentSpecificationRequest.getWithUserDetails() != null
                && fulfillmentSpecificationRequest.getWithUserDetails().equals(1L)) {
            mapUserContactDetails(fulfillmentSpecificationResponse);

        }
        // if with seat details ,load seat information from api
        if (fulfillmentSpecificationRequest.getWithSeatDetails() != null
                && fulfillmentSpecificationRequest.getWithSeatDetails().equals(1L)) {
            mapSeatInfo(fulfillmentSpecificationResponse);
        }
        return fulfillmentSpecificationResponse;
    }

    private void mapSeatInfo(FulfillmentSpecificationResponse fulfillmentSpecificationResponse) {
        for (FulfillmentSpecification fs : fulfillmentSpecificationResponse
                .getFulfillmentSpecifications()) {
            List<TicketSeat> ticketSeatList = listingBO.getTicketSeatInfo(fs.getTicketId());
            List<SeatDetail> seatDetailList = new ArrayList<SeatDetail>();
            for (TicketSeat ts : ticketSeatList) {
                SeatDetail seatDetail = new SeatDetail();
                seatDetail.setSeatStatusId(ts.getSeatStatusId());
                seatDetail.setTixListTypeId(ts.getTixListTypeId());
                seatDetail.setSection(ts.getSectionName());
                seatDetail.setSeatNum(ts.getSeatNumber());
                seatDetail.setRow(ts.getRowNumber());
                seatDetail.setIsGA(ts.getGeneralAdmissionInd() ? 1L : 0L);
                seatDetail.setSeatId(ts.getTicketSeatId());
                seatDetailList.add(seatDetail);
            }
            fs.setSeatDetailList(seatDetailList);
        }
    }

    protected void mapUserContactDetails(
            FulfillmentSpecificationResponse fulfillmentSpecificationResponse) {
        for (FulfillmentSpecification fs : fulfillmentSpecificationResponse
                .getFulfillmentSpecifications()) {
            if (fs.getSellerContact() != null) {
                UserContactDetail scd = fs.getSellerContact();
                UserContact userContactEntity = userContactBiz
                        .getUserContactById(scd.getContactId());
                scd.setProvinceOrStateCode(userContactEntity.getState());
                scd.setPostalcode(userContactEntity.getZip());
                scd.setLocality(userContactEntity.getCity());
                scd.setCountryCode(userContactEntity.getCountry());
            }
        }
    }

    @Override
    public DeliverySpecificationResponse getDeliverySpecification(
            DeliverySpecificationRequest deliverySpecificationRequest) {
        DeliverySpecificationResponse deliverySpecificationResponse;
        String orders = deliverySpecificationRequest.getOrders();
        if (orders == null || orders.length() == 0) {
            DeliverySpecificationResponse response = new DeliverySpecificationResponse();
            response.setDeliverySpecifications(new ArrayList<DeliverySpecification>());
            return response;
        }
        JsonNode queryResponse = this.accountSolrCloudBiz.getByIdList("sale", "id", Arrays.asList(orders.split(",")));
        deliverySpecificationResponse = AccountResponseJsonAdapter.convertJsonNodeToDeliverySpecResponse(queryResponse);
        // if with user details,load user contact details from DB
        if (deliverySpecificationRequest.getWithUserDetails() != null
                && deliverySpecificationRequest.getWithUserDetails().equals(1L)) {
            mapUserContactDetails(deliverySpecificationResponse);
        }
        // if with seat details ,load seat information from api
        if (deliverySpecificationRequest.getWithSeatDetails() != null
                && deliverySpecificationRequest.getWithSeatDetails().equals(1L)) {
            mapSeatInfo(deliverySpecificationResponse);
        }
        return deliverySpecificationResponse;
    }

    private void mapSeatInfo(DeliverySpecificationResponse deliverySpecificationResponse) {
        for (DeliverySpecification ds : deliverySpecificationResponse.getDeliverySpecifications()) {
            List<StubTransDetail> ticketSeatList = stubTransDetailDAO.getSeatDetails(ds.getTid());
            List<SeatDetail> seatDetailList = new ArrayList<SeatDetail>();
            for (StubTransDetail ts : ticketSeatList) {
                SeatDetail seatDetail = new SeatDetail();
                seatDetail.setStubTransDtlId(ts.getStubTransDtlId());
                seatDetail.setTixListTypeId(ts.getTicketListTypeId());
                seatDetail.setSection(ts.getSectionName());
                seatDetail.setSeatNum(ts.getSeatNumber());
                seatDetail.setRow(ts.getRowNumber());
                seatDetail.setIsGA(ts.getGeneralAdmissionIndicator());
                seatDetail.setSeatId(ts.getTicketSeatId());
                seatDetailList.add(seatDetail);
            }
            ds.setPurchasedSeatDetails(seatDetailList);
        }
    }

    private void mapUserContactDetails(
            DeliverySpecificationResponse deliverySpecificationResponse) {
        for (DeliverySpecification ds : deliverySpecificationResponse.getDeliverySpecifications()) {
            if (ds.getBuyerContact() != null) {
                UserContactDetail scd = ds.getBuyerContact();
                UserContact userContactEntity = userContactBiz
                        .getUserContactById(scd.getContactId());
                scd.setProvinceOrStateCode(userContactEntity.getState());
                scd.setPostalcode(userContactEntity.getZip());
                scd.setLocality(userContactEntity.getCity());
                scd.setCountryCode(userContactEntity.getCountry());
            }
        }
    }

    private String formatCalendar(Date date) {
        String dateFormat = "yyyy-MM-dd'T'HH:mm:ssZ";
        GregorianCalendar cal = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        cal.setTime(date);
        SimpleDateFormat sf = new SimpleDateFormat(dateFormat);
        sf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sf.format(cal.getTime());
    }

    @Override
    public Response getCSSaleDetails(CSSaleDetailsRequest csSaleDetailsRequest,
            SHServiceContext serviceContext, ExtendedSecurityContext securityContext) {
        String saleId = csSaleDetailsRequest.getSaleId();
        String proxiedId = csSaleDetailsRequest.getProxiedId();
        String startRow = csSaleDetailsRequest.getStart();
        String rowNumber = csSaleDetailsRequest.getRow();
        String eventStartDateSt = csSaleDetailsRequest.getEventStartDate();
        Calendar eventStartDate = null;
        String eventEndDateSt = csSaleDetailsRequest.getEventEndDate();
        Calendar eventEndDate = null;
        List<Error> errorList = new LinkedList<Error>();
        CSSalesResponse salesResponse = new CSSalesResponse();
        List<SalesTrans> salesTransLst = new ArrayList<SalesTrans>();
        SalesTrans salesTrans = null;
        List<CSSaleDetailsResponse> csSaleDetailsResponse = new ArrayList<CSSaleDetailsResponse>();
        AccountResponseAdapter accountResponseAdapter = new AccountResponseAdapter();
        // step1: field verification

        try {
            log.info("api_domain=" + api_domain
                    + " api_resource=csorderdetails api_method=getCSOrderDetails message=\"getting order details for\" saleId="
                    + saleId + " proxiedId=" + proxiedId);
            securityContextUtil.validateUserGuid(securityContext, securityContext.getUserGuid());
            String attRole = serviceContext.getAttribute(SHServiceContext.ATTR_ROLE);
            if (isNullorEmpty(attRole)) {
                log.info(
                        "_message=\"INVALID_SECURITY_CONTEXT in calling getCSSaleDetails api\" serviceContext={}");
                return Response.status(Response.Status.FORBIDDEN).build();
            }
            errorList = csSaleRequestValidator
                    .validateCSSaleDetailsRequestFields(csSaleDetailsRequest);
            if (errorList != null && !errorList.isEmpty()) {
                salesResponse.setErrors(errorList);
                return Response.status(Status.BAD_REQUEST).entity(salesResponse).build();
            }
            if (rowNumber == null) rowNumber = getProperty("cs.siebel.default.rows", "4");
            if (startRow == null) startRow = "0";
            // if saleId is not null
            if (!isNullorEmpty(saleId)) {
                salesTrans = saleTransBO.getSaleTransById(Long.parseLong(saleId));
                salesTransLst.add(salesTrans);

            } else if (!isNullorEmpty(proxiedId)) {
                if (!isNullorEmpty(eventStartDateSt)
                        && !isNullorEmpty(eventStartDateSt)) {
                    String dateFormat = "yyyy-MM-dd";
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
                    Date date = simpleDateFormat.parse(eventStartDateSt);
                    eventStartDate = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                    eventStartDate.setTime(date);

                    date = simpleDateFormat.parse(eventEndDateSt);
                    eventEndDate = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                    eventEndDate.setTime(date);
                    salesTransLst = saleTransBO.getSaleTransByBuyerIDAndEventDate(
                            Long.parseLong(proxiedId), eventStartDate, eventEndDate,
                            Integer.parseInt(startRow), Integer.parseInt(rowNumber));
                } else {
                    salesTransLst = saleTransBO.getSaleTransByBuyerId(Long.parseLong(proxiedId),
                            Integer.parseInt(startRow), Integer.parseInt(rowNumber));
                }
            } else if (!isNullorEmpty(eventStartDateSt)
                    && !isNullorEmpty(eventStartDateSt)) {
                salesTransLst = saleTransBO.getSaleTransByEventDate(eventStartDate, eventEndDate,
                        Integer.parseInt(startRow), Integer.parseInt(rowNumber));
            }
            salesResponse.setSalesFound(salesTransLst.size());
            if (salesTransLst.size() == 0) { return Response.status(Status.NOT_FOUND)
                    .entity(salesResponse).build(); }
            csSaleDetailsResponse = accountResponseAdapter
                    .convertBizSaleResponseToWebOrderEntities(salesTransLst, UserType.BUYER);
            salesResponse.setSale(csSaleDetailsResponse);
            Map<String, Boolean> csStubTransFlag = csStubTransFlagBO
                    .getCSStubTransFlag(AccountRequestAdapter.getSaleIds(salesResponse));
            salesResponse = accountResponseAdapter.addCSStubTransFlag(salesResponse,
                    csStubTransFlag);
            if (salesResponse.getSalesFound() > 0) {
                for (CSSaleDetailsResponse saleDetails : salesResponse.getSale()) {
                    StubTrans st = stubTransBO.getStubTransById(
                            Long.parseLong(saleDetails.getTransaction().getSaleId()));
                    if (st.getSeats() != null) saleDetails.getTransaction().setSeats(st.getSeats());
                    if (st.getEventStatusIdAtConfirm() != null) saleDetails.getEvent()
                            .setEventStatus(String.valueOf(st.getEventStatusIdAtConfirm()));
                }
            }

            log.info("api_domain=" + api_domain
                    + " api_resource=csorderdetails api_method=getCSOrderDetails message=\"sending response for\" saleId="
                    + saleId + " proxiedId=" + proxiedId);
            return Response.status(Status.OK).entity(salesResponse).build();
            // }
        } catch (Exception e) {
            log.error("api_domain=" + api_domain
                    + "api_resource=csorderdetails api_method=getOrderDetails" + "status=error "
                    + "error_message=Exception occured while getting cs order details." + " saleId="
                    + saleId + ", proxiedId=" + proxiedId, e);
            salesResponse.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
            salesResponse.getErrors()
                    .add(new com.stubhub.domain.account.common.Error(ErrorType.SYSTEMERROR,
                            ErrorCode.SYSTEM_ERROR,
                            "Exception occured while getting cs order details", null));
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(salesResponse).build();
        }
    }

    private  List<Future<List<PricingRecommendation>>> getPricingRec(ListingsResponse listingsResponse, String sellerId, String sellerGuid){

        List<Future<List<PricingRecommendation>>> futures = null;
        try {
            List<ListingResponse> listings = listingsResponse.getListings();
            if (listings != null && listings.size() > 0) {
                int listingNum = listingsResponse.getListings().size();
                if (listingNum > 50) {
                    return Collections.EMPTY_LIST;
                }
                SHAPIContext apiContext = SHAPIThreadLocal.getAPIContext();
                int batchSize = MasterStubHubProperties.getPropertyAsInt("account.listing.price.recommendation.batchsize", 10);

                // e.g batchSize=5, listingNum=12, initialBatchNum=2, but the actual batch num is 3.
                int initialBatchNum = listingNum / batchSize;
                futures = new ArrayList<Future<List<PricingRecommendation>>>();

                for (int i = 0; i <= initialBatchNum; i++) {

                    List<Long> list = new ArrayList<Long>();

                    for (int j = i * batchSize; j < (i == initialBatchNum ? listingNum : (i + 1) * batchSize); j++) {
                        list.add(Long.valueOf(listings.get(j).getId()));
                    }

                    if (list.size() > 0) {
                        log.debug("Start to process pricing recommendation. listingSize=" + list.size());
                        PricingRecTask task = new PricingRecTask(list, pricingRecHelper, apiContext, sellerGuid);
                        futures.add(listingThreadPool.submit(task));
                    }
                }

            }
        }catch (TaskRejectedException tre) {
            log.warn("api_domain=" + api_domain + " api_resource=" + api_resource
                    + " api_method=getMyListings"
                    + " message=\"Get price recommendations request is rejected.\" sellerId="
                    + sellerId);

        } catch (Exception e) {
            log.warn("api_domain=" + api_domain + " api_resource=" + api_resource
                    + " api_method=getMyListings"
                    + " message=\"Exception occurs when submit get price recommendations request.\"sellerId="
                    + sellerId,e);
        }
        return futures;
    }

    private void getPricingRecFutures(ListingsResponse listingsResponse,List<Future<List<PricingRecommendation>>> futures,String sellerId){
        StopWatch sw = new StopWatch();
        sw.start();

        List<ListingResponse> listings = listingsResponse.getListings();
        if(listings == null || listings.size() == 0){
            return;
        }


        List<PricingRecommendation>  recommendations = new ArrayList<PricingRecommendation>();

        int task = 0;
        for (int i = 0; i < futures.size(); i++) {
            StopWatch taskSW = new StopWatch();
            taskSW.start();
            task++;
            try {
               List<PricingRecommendation>  batchRecommendations = futures.get(i).get(2000, TimeUnit.MILLISECONDS);
                if(batchRecommendations != null && batchRecommendations.size() > 0) {
                    recommendations.addAll(batchRecommendations);
                }

            } catch (TimeoutException te) {
                for (int j = i + 1; j < futures.size(); j++) {
                    futures.get(j).cancel(true);
                }
                log.warn("api_domain=" + api_domain + " api_resource=" + api_resource
                        + " api_method=getMyListings" + " status=warning"
                        + " message=\"Timeout to process price recommendations request.\" taskNum=" + task + "sellerId="+ sellerId);
                break;
            } catch (Exception e) {
                log.error("api_domain=" + api_domain + " api_resource=" + api_resource
                        + " api_method=getMyListings" + " status=error"
                        + " message=\"Exception occurs when processing pricing recommendations.\" sellerId="
                        + sellerId,e);
            }
            taskSW.stop();
            log.debug("task" + task + ":" + taskSW.getTotalTimeSeconds());
        }

        for(ListingResponse listing : listings){

            for(PricingRecommendation pricingRecommendation : recommendations){
                if(Long.valueOf(listing.getId()).equals(pricingRecommendation.getListingId())){
                    listing.setPricingRecommendation(pricingRecommendation);
                    break;
                }
            }

        }


        sw.stop();
        log.debug("process price recommendations end, duration=" + sw.getTotalTimeSeconds());
        futures.clear();
    }
}
