package com.stubhub.domain.account.impl;

import com.google.common.cache.LoadingCache;
import com.stubhub.common.exception.InvalidArgumentException;
import com.stubhub.common.exception.RecordNotFoundForIdException;
import com.stubhub.common.exception.UserNotAuthorizedException;
import com.stubhub.domain.account.adapter.AccountRequestAdapter;
import com.stubhub.domain.account.adapter.AccountResponseAdapter;
import com.stubhub.domain.account.adapter.AccountResponseJsonAdapterTest;
import com.stubhub.domain.account.biz.impl.EventUtil;
import com.stubhub.domain.account.biz.impl.StubTransBOImpl;
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
import com.stubhub.domain.account.common.Error;
import com.stubhub.domain.account.common.ListingSearchCriteria;
import com.stubhub.domain.account.common.MyOrderSearchCriteria;
import com.stubhub.domain.account.common.PaginationInput;
import com.stubhub.domain.account.common.PaymentsSearchCriteria;
import com.stubhub.domain.account.common.SalesSearchCriteria;
import com.stubhub.domain.account.common.SummaryInput;
import com.stubhub.domain.account.common.enums.DeliveryMethod;
import com.stubhub.domain.account.common.enums.ErrorCode;
import com.stubhub.domain.account.common.enums.ErrorType;
import com.stubhub.domain.account.common.enums.ListingFilterType;
import com.stubhub.domain.account.common.enums.SaleSortType;
import com.stubhub.domain.account.common.enums.SalesFilterType;
import com.stubhub.domain.account.common.enums.UserType;
import com.stubhub.domain.account.common.exception.AccountException;
import com.stubhub.domain.account.common.exception.BuyerOrderException;
import com.stubhub.domain.account.common.util.SecurityContextUtil;
import com.stubhub.domain.account.datamodel.dao.StubTransDAO;
import com.stubhub.domain.account.datamodel.dao.StubTransDetailDAO;
import com.stubhub.domain.account.datamodel.dao.SystemSettingsDAO;
import com.stubhub.domain.account.datamodel.dao.UserDAO;
import com.stubhub.domain.account.datamodel.dao.impl.StubTransDetailDAOImpl;
import com.stubhub.domain.account.datamodel.dao.impl.SystemSettingsDAOImpl;
import com.stubhub.domain.account.datamodel.entity.EmailLog;
import com.stubhub.domain.account.datamodel.entity.OrderProcStatus;
import com.stubhub.domain.account.datamodel.entity.SalesTrans;
import com.stubhub.domain.account.datamodel.entity.StubTrans;
import com.stubhub.domain.account.datamodel.entity.StubTransDetail;
import com.stubhub.domain.account.datamodel.entity.StubTransFmDm;
import com.stubhub.domain.account.datamodel.entity.SystemSettings;
import com.stubhub.domain.account.datamodel.entity.TicketSeat;
import com.stubhub.domain.account.datamodel.entity.UpsTracking;
import com.stubhub.domain.account.datamodel.entity.UserContact;
import com.stubhub.domain.account.datamodel.entity.UserDO;
import com.stubhub.domain.account.helper.BatchScoreServiceHelper;
import com.stubhub.domain.account.helper.CSSaleRequestValidator;
import com.stubhub.domain.account.helper.CustomerServiceHelper;
import com.stubhub.domain.account.helper.EventMetaHelper;
import com.stubhub.domain.account.helper.LMSLocationHelper;
import com.stubhub.domain.account.helper.ListingHelper;
import com.stubhub.domain.account.helper.PricingRecHelper;
import com.stubhub.domain.account.helper.PricingRecTask;
import com.stubhub.domain.account.helper.RequestValidator;
import com.stubhub.domain.account.helper.SellerEligibilityHelper;
import com.stubhub.domain.account.helper.UserDomainHelper;
import com.stubhub.domain.account.intf.AccountService;
import com.stubhub.domain.account.intf.BuyerContactRequest;
import com.stubhub.domain.account.intf.CSOrderDetailsRequest;
import com.stubhub.domain.account.intf.CSOrderDetailsResponse;
import com.stubhub.domain.account.intf.CSSaleDetailsRequest;
import com.stubhub.domain.account.intf.CSSaleDetailsResponse;
import com.stubhub.domain.account.intf.CSSaleTransactionResponse;
import com.stubhub.domain.account.intf.CSSalesResponse;
import com.stubhub.domain.account.intf.DeliveryResponse;
import com.stubhub.domain.account.intf.DeliverySpecificationRequest;
import com.stubhub.domain.account.intf.DeliverySpecificationResponse;
import com.stubhub.domain.account.intf.DiscountResponse;
import com.stubhub.domain.account.intf.DiscountsResponse;
import com.stubhub.domain.account.intf.FulfillmentSpecificationRequest;
import com.stubhub.domain.account.intf.FulfillmentSpecificationResponse;
import com.stubhub.domain.account.intf.ListingsResponse;
import com.stubhub.domain.account.intf.MyOrderListResponse;
import com.stubhub.domain.account.intf.OrderStatusResponse;
import com.stubhub.domain.account.intf.OrdersResponse;
import com.stubhub.domain.account.intf.PricingRecommendation;
import com.stubhub.domain.account.intf.ReportAnIssueRequest;
import com.stubhub.domain.account.intf.SalesResponse;
import com.stubhub.domain.account.intf.SearchEmailCriteria;
import com.stubhub.domain.account.intf.SubstitutionRequest;
import com.stubhub.domain.account.intf.SubstitutionResponse;
import com.stubhub.domain.account.intf.TransactionResponse;
import com.stubhub.domain.account.intf.TransactionSummaryRequest;
import com.stubhub.domain.account.intf.TransactionSummaryResponse;
import com.stubhub.domain.catalog.read.v3.intf.common.dto.response.CommonAttribute;
import com.stubhub.domain.catalog.read.v3.intf.events.dto.response.Event;
import com.stubhub.domain.catalog.read.v3.intf.events.dto.response.SeatTrait;
import com.stubhub.domain.fulfillment.clients.fulfillmentlabel.util.MessageSourceBundle;
import com.stubhub.domain.i18n.infra.soa.core.I18nServiceContext;
import com.stubhub.domain.i18n.services.localization.v1.utility.DataSourceMessageSource;
import com.stubhub.domain.infrastructure.soa.core.context.SHServiceContext;
import com.stubhub.domain.inventory.metadata.v1.event.DTO.EventMetadataResponse;
import com.stubhub.domain.inventory.metadata.v1.event.DTO.InvFulfillmentDetails;
import com.stubhub.newplatform.common.entity.Money;
import com.stubhub.newplatform.property.MasterStubHubProperties;
import com.stubhub.newplatform.property.MasterStubhubPropertiesWrapper;
import com.stubhub.newplatform.property.loader.IConfigLoader;
import com.stubhub.platform.utilities.webservice.security.ExtendedSecurityContext;
import com.stubhub.platform.utilities.webservice.svclocator.SvcLocator;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.response.PivotField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.util.NamedList;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anySet;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertNull;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

public class AccountServiceTest {

    private final static String TRANSFER_EMAIL_ADDRESS = "transferEmailAddress";
    ObjectMapper om = new ObjectMapper();
    private ExtendedSecurityContext securityContext;
    private AccountService accountService;
    private UserContactBiz userContactBiz;
    private ListingBO listingBO;
    @Mock
    private StubTransDetailDAO stubTransDetailDAO;
    private StubTransDAO stubTransDAO;
    private SellerEligibilityHelper sellerEligibilityHelper;
    private UserDomainHelper userDomainHelper;
    private ListingHelper listingHelper;
    private boolean isFirstTime = false;
    private SHServiceContext serviceContext;
    private I18nServiceContext i18nContext;
    private DataSourceMessageSource messageSource;
    private MessageSourceBundle messageSourceBundle;
    private UserDAO userDAO;
    private AccountSolrCloudBiz accountSolrCloudBiz;
    private BatchScoreServiceHelper batchScoreServiceHelper;
    @Mock
    private TransactionSummaryBO transactionSummaryBO;
    @Mock
    private SystemSettingsDAO systemSettingsDAO;
    @Mock
    private LoadingCache<String, String> settingCache;

    @BeforeClass
    public static void setupClass() throws Exception {
        MasterStubHubProperties.setLoaders(Arrays.<IConfigLoader>asList(new IConfigLoader() {

            @Override
            public Map<String, String> load() throws Exception {
                return Collections.emptyMap();
            }

        }));
        MasterStubHubProperties.load();
    }

    @BeforeMethod
    public void setUp() throws Exception {
        accountService = new AccountServiceImpl() {
            @Override
            protected String getProperty(String propertyName, String defaultValue) {
                if ("cs.ivr.top.buyer.criteria.purchase.timeframe.in.days"
                        .equalsIgnoreCase(propertyName)) {
                    return "365";
                }
                if ("updateBuyerContactId.order.uri".equalsIgnoreCase(
                        propertyName)) {
                    return "https://api.stubhub.com/accountmanagement/orders/v1/{orderId}/buyerContactId";
                }
                if ("updateBuyerContactId.order.uri".equalsIgnoreCase(
                        propertyName)) {
                    return "https://api.stubhub.com/accountmanagement/orders/v1/{orderId}/buyerContactId";
                }
                return "";
            }
        };
        accountSolrCloudBiz = mock(AccountSolrCloudBiz.class);
        batchScoreServiceHelper = mock(BatchScoreServiceHelper.class);
        ReflectionTestUtils.setField(accountService, "accountSolrCloudBiz", accountSolrCloudBiz);
        listingHelper = new ListingHelper();
        messageSourceBundle = Mockito.mock(MessageSourceBundle.class);
        securityContext = Mockito.mock(ExtendedSecurityContext.class);
        i18nContext = Mockito.mock(I18nServiceContext.class);
        userContactBiz = Mockito.mock(UserContactBiz.class);
        listingBO = Mockito.mock(ListingBO.class);
        sellerEligibilityHelper = mock(SellerEligibilityHelper.class);
        stubTransDetailDAO = mock(StubTransDetailDAO.class);
        stubTransDAO = mock(StubTransDAO.class);
        serviceContext = Mockito.mock(SHServiceContext.class);
        userDomainHelper = Mockito.mock(UserDomainHelper.class);
        transactionSummaryBO = mock(TransactionSummaryBO.class);
        systemSettingsDAO = Mockito.mock(SystemSettingsDAO.class);
        settingCache = Mockito.mock(LoadingCache.class);
        userDAO = mock(UserDAO.class);
        ReflectionTestUtils.setField(accountService, "messageSourceBundle", messageSourceBundle);
        ReflectionTestUtils.setField(accountService, "sellerEligibilityHelper",
                sellerEligibilityHelper);
        ReflectionTestUtils.setField(accountService, "userContactBiz", userContactBiz);
        ReflectionTestUtils.setField(accountService, "listingBO", listingBO);
        ReflectionTestUtils.setField(accountService, "stubTransDetailDAO", stubTransDetailDAO);
        ReflectionTestUtils.setField(accountService, "userDomainHelper", userDomainHelper);
        messageSource = Mockito.mock(DataSourceMessageSource.class);
        ReflectionTestUtils.setField(accountService, "messageSource", messageSource);
        ReflectionTestUtils.setField(accountService, "listingHelper", listingHelper);
        ReflectionTestUtils.setField(accountService, "userDAO", userDAO);
        ReflectionTestUtils.setField(accountService, "systemSettingsDAO", systemSettingsDAO);
        ReflectionTestUtils.setField(accountService, "settingCache", settingCache);
        ReflectionTestUtils.setField(accountService, "batchScoreServiceHelper", batchScoreServiceHelper);

        Mockito.when(messageSource.getMessage(anyString(), null, anyString(), (Locale) anyObject()))
                .thenReturn("");

        Mockito.when(securityContext.getUserId()).thenReturn("12345");
        Mockito.when(securityContext.getUserName()).thenReturn("12345");
        Mockito.when(securityContext.getUserGuid()).thenReturn("B5D14E323CD55E9FE04400144F8AE084");
        Mockito.when(serviceContext.getProxiedId()).thenReturn("707");
        Mockito.when(serviceContext.getProxiedId()).thenReturn("274291");
        Mockito.when(serviceContext.getOperatorId()).thenReturn("bijain");
        Mockito.when(serviceContext.getOperatorId()).thenReturn("");
        Mockito.when(serviceContext.getRole()).thenReturn("R1");
        Mockito.when(serviceContext.getExtendedSecurityContext()).thenReturn(securityContext);
        UserDO userDO = new UserDO();
        userDO.setUserId(12345L);
        Mockito.when(userDAO.findUserByGuid(anyString())).thenReturn(userDO);
    }

    private JsonNode covertQueryResponse(QueryResponse response) {
        ObjectNode objectNode = new ObjectNode(JsonNodeFactory.instance);
        SolrDocumentList results = response.getResults();
//        if(null!=results){
        ObjectNode responseNode = objectNode.putObject("response");
        responseNode.put("numFound", results.getNumFound());
        ArrayNode docs = responseNode.putArray("docs");
        for (SolrDocument doc : results) {
            ObjectNode docNode = new ObjectNode(JsonNodeFactory.instance);
            for (Map.Entry<String, Object> entry : doc.entrySet()) {
                if (entry.getValue() instanceof ArrayList) {
                    covertArrayList(docNode, entry.getKey(), (ArrayList) entry.getValue());
                } else {
                    docNode.putPOJO(covertKey(entry.getKey()), entry.getValue());
                }
            }
            docs.add(docNode);
        }
//        }
        return objectNode;
    }

    private void covertArrayList(ObjectNode node, String key, ArrayList list) {
        ArrayNode arrayNode = node.putArray(key);
        for (Object object : list) {
            ObjectNode objNode = new ObjectNode(JsonNodeFactory.instance);
            objNode.putPOJO(key, object);
            arrayNode.add(objNode);
        }
    }

    private String covertKey(String key) {
        key = key.toLowerCase();
        if (key.contains("_")) {
            int index;
            while ((index = key.indexOf("_")) > 0) {
                key = key.replaceFirst("_", "");
                key = key.substring(0, index).concat(key.substring(index, index + 1).toUpperCase()).concat(key.substring(index + 1));
            }
        }
        return key;
    }

    @Test
    public void testGetMyOrders() {

        SecurityContextUtil scu = new SecurityContextUtil();
        ReflectionTestUtils.setField(accountService, "securityContextUtil", scu);
        QueryResponse qr = new QueryResponse();

        PaginationInput pi = new PaginationInput();
        pi.setRows(10);
        pi.setStart(1);
        try {
            accountService.getMyOrders(securityContext, "B5D14E323CD55E9FE04400144F8AE084", new String[]{"EVENTDESC asc"},
                    "status:ongoing", pi, "en-US");
        } catch (BuyerOrderException e) {
            Assert.assertEquals(e.getMessage(), "Internal Server Error");
        }
        try {
            accountService.getMyOrders(securityContext, "test", new String[]{"INHAND_DATE asc"},
                    "status:ongoing", pi, "en-US");
        } catch (BuyerOrderException e) {
            Assert.assertEquals(e.getMessage(), "Unauthorized access");
        }

//        qr = Mockito.mock(QueryResponse.class);
        SolrDocumentList docl = new SolrDocumentList();
        NamedList responseList = new NamedList();
        responseList.add("response", docl);
        qr.setResponse(responseList);
        SolrDocument doc = new SolrDocument();
//        doc.put("INHAND_DATE", "2014-05-04T00:00:00Z");
        doc.put("CURRENCY_CODE", "USD");
        doc.put("PRICE_PER_TICKET", "20");
        doc.put("COST_PER_TICKET", "20");
        doc.put("FULFILLMENT_METHOD_ID", "11");
        doc.put("DELIVERY_OPTION_ID", "2");
        doc.put("TICKET_MEDIUM_ID", "2");
        doc.put("JDK_TIMEZONE", "US/Eastern");
        doc.put("EVENT_ID", "1234");
        doc.put("TID", "11");

        ArrayList<String> list = new ArrayList<String>();
        list.add("1,2,3|4,5,6,|7,8,9|");
        doc.addField("SEAT_TRAITS", list);

        docl.add(doc);
        docl.setNumFound(1L);
        docl.setStart(1);
        LMSLocationHelper helper = Mockito.mock(LMSLocationHelper.class);
        EventMetaHelper eventHelper = Mockito.mock(EventMetaHelper.class);
        ReflectionTestUtils.setField(accountService, "eventMetaHelper", eventHelper);
        ReflectionTestUtils.setField(accountService, "LmsLocationHelper", helper);

        doc.put("ORDER_PROC_STATUS_ID", 5000L);
        doc.put("ORDER_PROC_SUB_STATUS_CODE", 12L);
        Mockito.when(accountSolrCloudBiz.getBuyerOrders(Mockito.any(MyOrderSearchCriteria.class)))
                .thenReturn(covertQueryResponse(qr));
        MyOrderListResponse myOrders = accountService.getMyOrders(securityContext,
                "B5D14E323CD55E9FE04400144F8AE084", new String[]{"EVENTDESC asc"},
                "status:ongoing", pi, "en-US");
        Assert.assertEquals(myOrders.getNumFound(), 1L);
        Assert.assertTrue(!myOrders.getMyOrderList().get(0).isAllowViewTicket());

        doc.put("ORDER_PROC_SUB_STATUS_CODE", 31L);

        Mockito.when(accountSolrCloudBiz.getBuyerOrders(Mockito.any(MyOrderSearchCriteria.class)))
                .thenReturn(covertQueryResponse(qr));
        myOrders = accountService.getMyOrders(securityContext, "B5D14E323CD55E9FE04400144F8AE084",
                new String[]{"EVENTDESC asc"}, "status:ongoing", pi, "en-US");

        Assert.assertEquals(myOrders.getNumFound(), 1L);
        Assert.assertTrue(myOrders.getMyOrderList().get(0).isAllowViewTicket());

    }

    @Test
    public void testGetMyOrdersForSolrCloud() throws Exception {
        AccountServiceImpl listingResource = new AccountServiceImpl();
        AccountSolrCloudBiz accountSolrCloudBiz = mock(AccountSolrCloudBiz.class);
        SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
        ReflectionTestUtils.setField(listingResource, "accountSolrCloudBiz", accountSolrCloudBiz);
        ReflectionTestUtils.setField(listingResource, "securityContextUtil", securityContextUtil);
        ReflectionTestUtils.setField(listingResource, "messageSource", messageSource);
        MasterStubHubProperties.setProperty("account.v1.order.useSolrCloud", "true");
        try {
            EventMetaHelper eventHelper = Mockito.mock(EventMetaHelper.class);
            ReflectionTestUtils.setField(listingResource, "eventMetaHelper", eventHelper);

            LMSLocationHelper lmsLocationHelper = Mockito.mock(LMSLocationHelper.class);
            ReflectionTestUtils.setField(listingResource, "LmsLocationHelper", lmsLocationHelper);

            EventUtil eventUtil = Mockito.mock(EventUtil.class);
            Mockito.when(
                    eventUtil.getLocalizedSeatTraitName(Mockito.anyLong(), Mockito.anyString(), Mockito.anyString()))
                    .thenReturn("test").thenReturn(null);
            ReflectionTestUtils.setField(listingResource, "eventUtil", eventUtil);

            InputStream in = AccountResponseJsonAdapterTest.class.getClassLoader().getResourceAsStream("orderResponse.json");
            ObjectMapper om = new ObjectMapper();

            JsonNode response = om.readTree(in);
            when(accountSolrCloudBiz.getBuyerOrders(any(MyOrderSearchCriteria.class))).thenReturn(response);

            MyOrderListResponse myOrders = listingResource.getMyOrders(securityContext, "test", new String[]{"INHAND_DATE asc"},
                    "status:ongoing", new PaginationInput(), "en-US");
            Assert.assertNotNull(myOrders);

            myOrders = listingResource.getMyOrders(securityContext, "test", new String[]{"INHAND_DATE asc"},
                    "status:ongoing", new PaginationInput(), "en-gb");
            Assert.assertNotNull(myOrders);
        } finally {
            MasterStubHubProperties.setProperty("account.v1.order.useSolrCloud", "false");
        }
    }


    @Test
    public void testGetOrderStatus() {
        OrderProcStatusBO orderProcStatusBO = mock(OrderProcStatusBO.class);
        ReflectionTestUtils.setField(accountService, "orderProcStatusBO", orderProcStatusBO);
        OrderStatusResponse response = mock(OrderStatusResponse.class);
        List<OrderProcStatus> list = new ArrayList<OrderProcStatus>();
        OrderProcStatus orderStatus = mock(OrderProcStatus.class);
        String l1 = "200";
        String l2 = "404";

        orderStatus.setStatusCode(2000L);
        orderStatus.setStatusDescription("Confirmed");
        orderStatus.setSubStatusCode(5L);
        orderStatus.setSubStatusDescription("confirmed: CSR");
        orderStatus.setStatusEffectiveDate("2012-03-05T20:03:12+0000");
        list.add(orderStatus);
        response.setOrderProc(list);
        Mockito.when(orderProcStatusBO.getOrderStatus(Mockito.any(String.class))).thenReturn(list);

        // SecurityContext is null
        securityContext = null;
        assertNull(accountService.getOrderStatus(l2, securityContext).getOrderProc());

        // SecurityContext is NOT null
        securityContext = Mockito.mock(ExtendedSecurityContext.class);
        // Mockito.when(securityContext.getUserId()).thenReturn("12345");

        // Invalid Long value
        Assert.assertNotNull(accountService.getOrderStatus("abc", securityContext));

        // Valid Long value
        Assert.assertNotNull(accountService.getOrderStatus(l1, securityContext));

        // NPE
        Mockito.when(orderProcStatusBO.getOrderStatus(l2)).thenReturn(null);
        assertNull(accountService.getOrderStatus(l2, securityContext).getOrderProc());
    }

    @Test
    public void testGetDiscount() {
        String l1 = "200";
        String l2 = "404";

        Long id = 2000L;
        String type = "Dollar Discount with Balance";
        String description = "Welcome Back!";
        BigDecimal amount = new BigDecimal(25.00);
        String currency = "USD";

        UsedDiscountBO usedDiscountBO = mock(UsedDiscountBO.class);
        ReflectionTestUtils.setField(accountService, "usedDiscountBO", usedDiscountBO);

        List<List> discounts = new ArrayList<List>();
        List discount = new ArrayList();
        discount.add(0, "1.2");
        discount.add(1, "USD");
        discount.add(2, "1");
        discount.add(3, "1");
        discount.add(4, "abc");
        discounts.add(discount);
        // UsedDiscount usedDiscount = mock(UsedDiscount.class);
        // usedDiscount.setDiscountId(id);
        // usedDiscount.setAmountUsed(amount);
        // usedDiscount.setCurrencyCode(currency);
        // list.add(usedDiscount);

        List<DiscountResponse> responseList = new ArrayList<DiscountResponse>();
        DiscountResponse discountResponse = new DiscountResponse();
        discountResponse.setId(id.toString());
        discountResponse.setType(type);
        discountResponse.setDescription(description);
        Money money = new Money(amount, currency);
        discountResponse.setUsedDiscount(money);
        responseList.add(discountResponse);

        DiscountsResponse response = mock(DiscountsResponse.class);

        response.setDiscounts(responseList);
        Mockito.when(usedDiscountBO.getDiscounts(Mockito.any(String.class))).thenReturn(discounts);

        // SecurityContext is null
        securityContext = null;
        assertNull(accountService.getDiscount(l2, securityContext).getDiscounts());

        // SecurityContext is NOT null
        securityContext = Mockito.mock(ExtendedSecurityContext.class);

        // Invalid Long value
        Assert.assertNotNull(accountService.getDiscount("abc", securityContext));

        // Valid Long value
        Assert.assertNotNull(accountService.getDiscount(l1, securityContext));

        // NPE
        Mockito.when(usedDiscountBO.getDiscounts(l2)).thenReturn(null);
        assertNull(accountService.getDiscount(l2, securityContext).getDiscounts());
    }

    @Test
    public void testUpdateBuyerContactId_NullSecurity() {
        BuyerContactRequest buyerContactRequest = mock(BuyerContactRequest.class);
        buyerContactRequest.setContactId("987");
        buyerContactRequest.setCsrRepresentative("BJ");
        accountService.updateBuyerContactId("123", buyerContactRequest, null);
    }

    @Test
    public void testUpdateBuyerContactId_NullOrderId() {
        BuyerContactRequest buyerContactRequest = mock(BuyerContactRequest.class);
        buyerContactRequest.setContactId("987");
        buyerContactRequest.setCsrRepresentative("BJ");
        accountService.updateBuyerContactId(null, buyerContactRequest, securityContext);
    }

    @Test
    public void testUpdateBuyerContactId_NullRequestPayload() {
        BuyerContactRequest buyerContactRequest = mock(BuyerContactRequest.class);
        buyerContactRequest.setContactId(null);
        accountService.updateBuyerContactId("123", buyerContactRequest, securityContext);
    }

    @Test
    public void testUpdateBuyerContactId_CsrRepMaxLength() {
        BuyerContactRequest buyerContactRequest = new BuyerContactRequest();
        buyerContactRequest.setContactId("987");
        buyerContactRequest.setCsrRepresentative(
                "VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV");
        accountService.updateBuyerContactId("123456", buyerContactRequest, securityContext);
    }

    @Test
    public void testUpdateBuyerContactId_InvalidContactId() {
        BuyerContactRequest buyerContactRequest = new BuyerContactRequest();
        buyerContactRequest.setContactId("abc");
        buyerContactRequest.setCsrRepresentative("Geroge Williams");
        accountService.updateBuyerContactId("123", buyerContactRequest, securityContext);
    }

    @Test
    public void testUpdateBuyerContactId_NullOrderStatus() {
        StubTransBO stubTransBO = mock(StubTransBO.class);
        ReflectionTestUtils.setField(accountService, "stubTransBO", stubTransBO);
        BuyerContactRequest buyerContactRequest = new BuyerContactRequest();
        buyerContactRequest.setContactId("987");
        buyerContactRequest.setCsrRepresentative("Geroge Williams");
        Long orderId = 123L;
        Mockito.when(stubTransBO.getOrderProcSubStatus(orderId)).thenReturn(null);
        accountService.updateBuyerContactId(orderId.toString(), buyerContactRequest,
                securityContext);
    }

    @Test
    public void testUpdateBuyerContactId_InvalidOrderStatus() {
        StubTransBO stubTransBO = mock(StubTransBO.class);
        ReflectionTestUtils.setField(accountService, "stubTransBO", stubTransBO);
        BuyerContactRequest buyerContactRequest = new BuyerContactRequest();
        buyerContactRequest.setContactId("987");
        buyerContactRequest.setCsrRepresentative("Geroge Williams");
        List<StubTrans> list = new ArrayList<StubTrans>();
        StubTrans stubTrans = mock(StubTrans.class);
        Long orderId = 123L;
        stubTrans.setOrderId(orderId);
        stubTrans.setOrderProcSubStatusCode(1L);
        list.add(stubTrans);
        Mockito.when(stubTransBO.getOrderProcSubStatus(orderId)).thenReturn(list);
        accountService.updateBuyerContactId(orderId.toString(), buyerContactRequest,
                securityContext);
    }

    @Test
    public void testUpdateBuyerContactId_InvalidUPSStatus() {
        StubTransBO stubTransBO = mock(StubTransBO.class);
        ReflectionTestUtils.setField(accountService, "stubTransBO", stubTransBO);
        BuyerContactRequest buyerContactRequest = new BuyerContactRequest();
        buyerContactRequest.setContactId("987");
        buyerContactRequest.setCsrRepresentative("Geroge Williams");
        Long orderId = 123L;
        List<StubTrans> list = new ArrayList<StubTrans>();
        StubTrans stubTrans = new StubTrans();
        stubTrans.setOrderId(orderId);
        stubTrans.setOrderProcSubStatusCode(45L);
        list.add(stubTrans);
        Mockito.when(stubTransBO.getOrderProcSubStatus(orderId)).thenReturn(list);
        UpsTrackingBO upsTrackingBO = mock(UpsTrackingBO.class);
        ReflectionTestUtils.setField(accountService, "upsTrackingBO", upsTrackingBO);
        Mockito.when(upsTrackingBO.checkUPSOrder(orderId)).thenReturn(null);
        accountService.updateBuyerContactId(orderId.toString(), buyerContactRequest,
                securityContext);
    }

    @Test
    public void testUpdateBuyerContactId() {
        StubTransBO stubTransBO = mock(StubTransBO.class);
        ReflectionTestUtils.setField(accountService, "stubTransBO", stubTransBO);
        BuyerContactRequest buyerContactRequest = new BuyerContactRequest();
        buyerContactRequest.setContactId("987");
        buyerContactRequest.setCsrRepresentative("Geroge Williams");
        Long orderId = 123L;
        List<StubTrans> list = new ArrayList<StubTrans>();
        StubTrans stubTrans = new StubTrans();
        stubTrans.setOrderId(orderId);
        stubTrans.setOrderProcSubStatusCode(45L);
        list.add(stubTrans);
        Mockito.when(stubTransBO.getOrderProcSubStatus(orderId)).thenReturn(list);
        UpsTrackingBO upsTrackingBO = mock(UpsTrackingBO.class);
        ReflectionTestUtils.setField(accountService, "upsTrackingBO", upsTrackingBO);
        List<UpsTracking> list2 = new ArrayList<UpsTracking>();
        UpsTracking upsTracking = new UpsTracking();
        upsTracking.setOrderId(200L);
        list2.add(upsTracking);
        Mockito.when(upsTrackingBO.checkUPSOrder(orderId)).thenReturn(list2);
        int results = 1;
        Mockito.when(stubTransBO.updateBuyerContactId(123L, 987L, "Geroge Williams",
                Calendar.getInstance())).thenReturn(results);
        Response apiResponse = accountService.updateBuyerContactId(orderId.toString(),
                buyerContactRequest, securityContext);
        Assert.assertEquals(apiResponse.getStatus(), 200);
    }

    @Test
    public void getListings() throws Exception {
        AccountServiceImpl listingResource = new AccountServiceImpl();
//        AccountBiz accountBiz = mock(AccountBiz.class);
        SecurityContextUtil securityContextUtil = new SecurityContextUtil();
//        setBeanProperty(listingResource, "accountBiz", accountBiz);
        setBeanProperty(listingResource, "securityContextUtil", securityContextUtil);
        setBeanProperty(listingResource, "eventMetaHelper", mock(EventMetaHelper.class));
        setBeanProperty(listingResource, "listingHelper", listingHelper);
        setBeanProperty(listingResource, "userDAO", userDAO);
        setBeanProperty(listingHelper, "messageSourceBundle", messageSourceBundle);
        setBeanProperty(listingResource, "batchScoreServiceHelper", batchScoreServiceHelper);
//        List<StubTransFmDm> stubTransFmDmLst = new ArrayList<StubTransFmDm>();
//        StubTransFmDm stubTransFmDm = new StubTransFmDm();
//        stubTransFmDm.setDeliveryMethodId(2l);
//        stubTransFmDm.setFulfillmentMethodId(1l);
//        Mockito.when(stubTransFmDmDao.getFmDmByTids(Mockito.anyList())).thenReturn(stubTransFmDmLst);

        EventUtil eventUtil = mock(EventUtil.class);
        setBeanProperty(listingResource, "eventUtil", eventUtil);
        when(eventUtil.getLocalizedSeatTraitName((Long) anyObject(), anyString(), anyString()))
                .thenReturn("A");

        QueryResponse response = new QueryResponse();
        SolrDocumentList list = new SolrDocumentList();
        list.setNumFound(5);
        list.setStart(0);
        list.add(mockSolrDocument(100, "DELETED", 10001, "3", "7", "1"));
        list.add(mockSolrDocument(200, "ACTIVE", 10002, "2", "10", "0"));
        list.add(mockSolrDocument(300, "PENDING_LOCK", 10003, "1", "6", "1"));
        list.add(mockSolrDocument(400, "INCOMPLETE", 10004, "1", "4", "1"));
        list.add(mockSolrDocument(500, "INACTIVE", 10005, "3", "1", "0"));
        list.get(0).setField("SELLER_CONTACT_ID", null);
        NamedList responseList = new NamedList();
        responseList.add("response", list);
        response.setResponse(responseList);
//        when(accountBiz.getMyListings((ListingSearchCriteria) argThat(getMatcher())))
//                .thenReturn(response);
        listingSetup(listingResource, response);
        ListingsResponse listResponse = listingResource.getMyListings(serviceContext,
                securityContext, i18nContext, "B5D14E323CD55E9FE04400144F8AE084", null, null, null, null, null, null, null, "false");
        assertNotNull(listResponse);
//        assertNotNull(listResponse.getListings().get(0).getGenreId());
//        assertNotNull(listResponse.getListings().get(0).getEventActive());
//        Assert.assertEquals(new Long(2l), listResponse.getListings().get(0).getDeliveryMethodId());
//        assertNotNull(listResponse.getListings().get(0).getFulfillmentMethodId());
        Mockito.when(serviceContext.getExtendedSecurityContext().getUserId()).thenReturn(null);
        Mockito.when(serviceContext.getExtendedSecurityContext().getApplicationName()).thenReturn("Test");
        Mockito.when(serviceContext.getAttribute(Mockito.eq(SHServiceContext.ATTR_PROXIED_ID))).thenReturn("274291");
        listResponse = listingResource.getMyListings(serviceContext,
                securityContext, i18nContext, "B5D14E323CD55E9FE04400144F8AE084", null, null, null, null, null, null, null, "false");
        assertNotNull(listResponse);

        // test authz
        Mockito.when(serviceContext.getAttribute(SHServiceContext.ATTR_ROLE)).thenReturn("R1");
        Mockito.when(serviceContext.getProxiedId()).thenReturn("B5D14E323CD55E9FE04400144F8AE084");
        listResponse = listingResource.getMyListings(serviceContext,
                securityContext, i18nContext, "B5D14E323CD55E9FE04400144F8AE084", null, null, null, null, null, null, null, "false");
        assertNotNull(listResponse);

        Mockito.verify(batchScoreServiceHelper, times(0)).fetchAndPopulateListingsScores(any(ListingsResponse.class), eq("false"));
    }

    @Test
    public void getInternalListings() throws Exception {
        AccountServiceImpl listingResource = new AccountServiceImpl();
        SecurityContextUtil securityContextUtil = new SecurityContextUtil();
        setBeanProperty(listingResource, "securityContextUtil", securityContextUtil);
        setBeanProperty(listingResource, "eventMetaHelper", mock(EventMetaHelper.class));
        setBeanProperty(listingResource, "listingHelper", listingHelper);
        setBeanProperty(listingResource, "userDAO", userDAO);
        setBeanProperty(listingHelper, "messageSourceBundle", messageSourceBundle);
        setBeanProperty(listingResource, "batchScoreServiceHelper", batchScoreServiceHelper);



        EventUtil eventUtil = mock(EventUtil.class);
        setBeanProperty(listingResource, "eventUtil", eventUtil);
        when(eventUtil.getLocalizedSeatTraitName((Long) anyObject(), anyString(), anyString()))
                .thenReturn("A");

        QueryResponse response = new QueryResponse();
        SolrDocumentList list = new SolrDocumentList();
        list.setNumFound(5);
        list.setStart(0);
        list.add(mockSolrDocument(100, "DELETED", 10001, "3", "7", "1"));
        list.add(mockSolrDocument(200, "ACTIVE", 10002, "2", "10", "0"));
        list.add(mockSolrDocument(300, "PENDING_LOCK", 10003, "1", "6", "1"));
        list.add(mockSolrDocument(400, "INCOMPLETE", 10004, "1", "4", "1"));
        list.add(mockSolrDocument(500, "INACTIVE", 10005, "3", "1", "0"));
        list.get(0).setField("SELLER_CONTACT_ID", null);
        NamedList responseList = new NamedList();
        responseList.add("response", list);
        response.setResponse(responseList);
        listingSetup(listingResource, response);
        Mockito.when(securityContext.getApplicationName()).thenReturn("PosLite");
        ListingsResponse listResponse = listingResource.getMyListingsInternal(serviceContext,
                securityContext, i18nContext, "B5D14E323CD55E9FE04400144F8AE084", null, null, null, null, null, null, null, "true");
        assertNotNull(listResponse);
        Mockito.verify(batchScoreServiceHelper, times(1)).fetchAndPopulateListingsScores(any(ListingsResponse.class), eq("true"));

        Mockito.when(serviceContext.getExtendedSecurityContext().getUserId()).thenReturn(null);
        Mockito.when(serviceContext.getAttribute(Mockito.eq(SHServiceContext.ATTR_PROXIED_ID))).thenReturn("274291");
        Mockito.when(securityContext.getApplicationName()).thenReturn("PosLite");
        listResponse = listingResource.getMyListingsInternal(serviceContext,
                securityContext, i18nContext, "B5D14E323CD55E9FE04400144F8AE084", null, null, null, null, null, null, null, "false");
        assertNotNull(listResponse);
        Mockito.verify(batchScoreServiceHelper, times(0)).fetchAndPopulateListingsScores(any(ListingsResponse.class), eq("false"));

        Mockito.when(serviceContext.getAttribute(SHServiceContext.ATTR_ROLE)).thenReturn("R1");
        Mockito.when(serviceContext.getProxiedId()).thenReturn("B5D14E323CD55E9FE04400144F8AE084");
        Mockito.when(securityContext.getApplicationName()).thenReturn("PosLite");
        listResponse = listingResource.getMyListingsInternal(serviceContext,
                securityContext, i18nContext, "B5D14E323CD55E9FE04400144F8AE084", null, null, null, null, null, null, null, "false");
        assertNotNull(listResponse);

        Mockito.verify(batchScoreServiceHelper, times(1)).fetchAndPopulateListingsScores(any(ListingsResponse.class), eq("true"));
    }

    private void listingSetup(AccountServiceImpl listingResource, QueryResponse response) throws Exception {
        setBeanProperty(listingResource, "accountSolrCloudBiz", accountSolrCloudBiz);
        Mockito.when(accountSolrCloudBiz.getSellerListings(Mockito.any(ListingSearchCriteria.class)))
                .thenReturn(null != response ? covertQueryResponse(response) : null);
    }

    private void saleSetup(AccountService listingResource, QueryResponse response) throws Exception {
        setBeanProperty(listingResource, "accountSolrCloudBiz", accountSolrCloudBiz);
        Mockito.when(accountSolrCloudBiz.getSellerSales(Mockito.any(SalesSearchCriteria.class)))
                .thenReturn(null != response ? covertQueryResponse(response) : null);
    }

    private void cssaleSetup(AccountService listingResource, QueryResponse response) throws Exception {
        setBeanProperty(listingResource, "accountSolrCloudBiz", accountSolrCloudBiz);
        Mockito.when(accountSolrCloudBiz.getCSOrderDetails(Mockito.any(SalesSearchCriteria.class)))
                .thenReturn(null != response ? covertQueryResponse(response) : null);
    }

    @Test
    public void getListingsWithSummary() throws Exception {
        AccountServiceImpl listingResource = new AccountServiceImpl();
//        AccountBiz accountBiz = mock(AccountBiz.class);
        SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
//        setBeanProperty(listingResource, "accountBiz", accountBiz);
        setBeanProperty(listingResource, "securityContextUtil", securityContextUtil);
        setBeanProperty(listingResource, "eventMetaHelper", mock(EventMetaHelper.class));
        setBeanProperty(listingResource, "listingHelper", mock(ListingHelper.class));

        EventUtil eventUtil = mock(EventUtil.class);
        setBeanProperty(listingResource, "eventUtil", eventUtil);
        when(eventUtil.getLocalizedSeatTraitName((Long) anyObject(), anyString(), anyString()))
                .thenReturn("A");

        QueryResponse response = new QueryResponse();
        SolrDocumentList list = new SolrDocumentList();
        list.setNumFound(5);
        list.setStart(0);
        list.add(mockSolrDocument(100, "DELETED", 10001, "3", "7", "1"));
        list.add(mockSolrDocument(200, "ACTIVE", 10002, "2", "10", "0"));
        list.add(mockSolrDocument(300, "PENDING_LOCK", 10003, "1", "6", "1"));
        list.add(mockSolrDocument(400, "INCOMPLETE", 10004, "1", "4", "0"));
        list.add(mockSolrDocument(500, "INACTIVE", 10005, "3", "1", "1"));
        NamedList responseList = new NamedList();
        responseList.add("response", list);
        response.setResponse(responseList);
//        when(accountBiz.getMyListings((ListingSearchCriteria) argThat(getMatcher())))
//                .thenReturn(response);

        NamedList<List<PivotField>> facetPivot = new NamedList<List<PivotField>>();
        List<PivotField> pivotFields = new ArrayList<PivotField>();
        List<PivotField> subpivotFields = new ArrayList<PivotField>();
        subpivotFields.add(new PivotField("VENUE_DESCRIPTION", "Gershwin Theatre", 2, null));
        pivotFields.add(new PivotField("VENUE_ID", "5548", 2, subpivotFields));
        facetPivot.add("VENUE_ID,VENUE_DESCRIPTION", pivotFields);
        ReflectionTestUtils.setField(response, "_facetPivot", facetPivot);

        SummaryInput summaryInput = new SummaryInput();
        summaryInput.setIncludeEventSummary(false);
        summaryInput.setIncludeGenreSummary(false);
        summaryInput.setIncludeVenueSummary(true);

        listingSetup(listingResource, response);
        ListingsResponse listResponse = listingResource.getMyListings(serviceContext,
                securityContext, i18nContext, "B5D14E323CD55E9FE04400144F8AE084", null, null, null, null, null,
                summaryInput, null, "false");
        assertNotNull(listResponse);
//        assertNotNull(listResponse.getListings().get(0).getGenreId());
//        assertNotNull(listResponse.getVenueSummary());
//        assertEquals(listResponse.getVenueSummary().get(0).getId().longValue(), 5548L);
//        assertEquals(listResponse.getVenueSummary().get(0).getDescription(), "Gershwin Theatre");
//        assertEquals(listResponse.getVenueSummary().get(0).getCount(), 2);

        assertNull(listResponse.getEventSummary());
        assertNull(listResponse.getGenreSummary());

        listResponse.setEventSummary(null);
        listResponse.setGenreSummary(null);
        listResponse.setVenueSummary(null);
    }

    @Test
    public void getListings_Incomplete() throws Exception {
        AccountServiceImpl listingResource = new AccountServiceImpl();
//        AccountBiz accountBiz = mock(AccountBiz.class);
        SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
//        setBeanProperty(listingResource, "accountBiz", accountBiz);
        setBeanProperty(listingResource, "securityContextUtil", securityContextUtil);
        setBeanProperty(listingResource, "eventMetaHelper", mock(EventMetaHelper.class));
        QueryResponse response = new QueryResponse();
        SolrDocumentList list = new SolrDocumentList();
        list.setNumFound(5);
        list.setStart(0);
        list.add(mockSolrDocument(100, "ACTIVE", 10001, "3", "7", "1"));
        list.add(mockSolrDocument(200, "ACTIVE", 10002, "2", "10", "0"));
        list.add(mockSolrDocument(300, "ACTIVE", 10003, "1", "6", "1"));
        list.add(mockSolrDocument(400, "ACTIVE", 10004, "1", "4", "0"));
        list.add(mockSolrDocument(500, "INACTIVE", 10005, "3", "1", "1"));
        NamedList responseList = new NamedList();
        responseList.add("response", list);
        response.setResponse(responseList);
//        when(accountBiz.getMyListings((ListingSearchCriteria) argThat(getMatcher())))
//                .thenReturn(response);
        listingSetup(listingResource, response);
        ListingsResponse listResponse = listingResource.getMyListings(serviceContext,
                securityContext, i18nContext, "B5D14E323CD55E9FE04400144F8AE084", null, null, null, null, null, null, null, "false");
        assertNotNull(listResponse);
    }

    @Test
    public void getListings_AccounException() throws Exception {
        AccountServiceImpl listingResource = new AccountServiceImpl();
//        AccountBiz accountBiz = mock(AccountBiz.class);
        SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
//        setBeanProperty(listingResource, "accountBiz", accountBiz);
        setBeanProperty(listingResource, "securityContextUtil", securityContextUtil);
        setBeanProperty(listingResource, "eventMetaHelper", mock(EventMetaHelper.class));
        QueryResponse response = new QueryResponse();
        SolrDocumentList list = new SolrDocumentList();
        list.setNumFound(5);
        list.setStart(0);
        list.add(mockSolrDocument(100, "ACTIVE", 10001, "3", "7", "1"));
        list.add(mockSolrDocument(200, "ACTIVE", 10002, "2", "10", "0"));
        list.add(mockSolrDocument(300, "ACTIVE", 10003, "1", "6", "1"));
        list.add(mockSolrDocument(400, "ACTIVE", 10004, "1", "4", "1"));
        list.add(mockSolrDocument(500, "INACTIVE", 10005, "3", "1", "0"));
        NamedList responseList = new NamedList();
        responseList.add("response", list);
        response.setResponse(responseList);
//        when(accountBiz.getMyListings((ListingSearchCriteria) argThat(getMatcher())))
//                .thenThrow(new AccountException());
        listingSetup(listingResource, response);
        ListingsResponse listResponse = listingResource.getMyListings(serviceContext,
                securityContext, i18nContext, "B5D14E323CD55E9FE04400144F8AE084", null, null, null, null, null, null, null, "false");
        assertNotNull(listResponse);
    }


    @Test
    public void testMyListingsWithPriceRecommendation() throws Exception {
        AccountServiceImpl listingResource = new AccountServiceImpl();
        listingResource.setup();
//        AccountBiz accountBiz = mock(AccountBiz.class);
        SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
//        setBeanProperty(listingResource, "accountBiz", accountBiz);
        setBeanProperty(listingResource, "securityContextUtil", securityContextUtil);
        setBeanProperty(listingResource, "eventMetaHelper", mock(EventMetaHelper.class));
        setBeanProperty(listingResource, "listingHelper", listingHelper);
        PricingRecHelper pricingRecHelper = mock(PricingRecHelper.class);
        setBeanProperty(listingResource, "pricingRecHelper", pricingRecHelper);
        EventUtil eventUtil = mock(EventUtil.class);
        setBeanProperty(listingResource, "eventUtil", eventUtil);
        when(eventUtil.getLocalizedSeatTraitName((Long) anyObject(), anyString(), anyString()))
                .thenReturn("A");
        List<StubTransFmDm> stubTransFmDmLst = new ArrayList<StubTransFmDm>();
        StubTransFmDm stubTransFmDm = new StubTransFmDm();
        stubTransFmDm.setDeliveryMethodId(2l);
        stubTransFmDm.setFulfillmentMethodId(1l);

        QueryResponse response = new QueryResponse();
        SolrDocumentList list = new SolrDocumentList();
        list.setNumFound(5);
        list.setStart(0);
        list.add(mockSolrDocument(100, "DELETED", 10001, "3", "7", "1"));
        list.add(mockSolrDocument(200, "ACTIVE", 10002, "2", "10", "0"));
        list.add(mockSolrDocument(300, "PENDING_LOCK", 10003, "1", "6", "1"));
        list.add(mockSolrDocument(400, "INCOMPLETE", 10004, "1", "4", "1"));
        list.add(mockSolrDocument(500, "INACTIVE", 10005, "3", "1", "0"));
        list.get(0).setField("SELLER_CONTACT_ID", null);
        NamedList responseList = new NamedList();
        responseList.add("response", list);
        response.setResponse(responseList);
//        when(accountBiz.getMyListings((ListingSearchCriteria) argThat(getMatcher())))
//                .thenReturn(response);
        listingSetup(listingResource, response);
        List<PricingRecommendation> recommendations = new ArrayList<PricingRecommendation>();

        PricingRecommendation pr1 = new PricingRecommendation();
        pr1.setListingId(100L);
        recommendations.add(pr1);
        Mockito.when(pricingRecHelper.handlePriceRecommendations(anyList(), anyString())).thenReturn(recommendations);
        ListingsResponse listResponse = listingResource.getMyListings(serviceContext,
                securityContext, null, "B5D14E323CD55E9FE04400144F8AE084", null, null, null, "true", null, null, null, "false");
        assertNotNull(listResponse);
    }


    @Test
    public void testMyListingsWithPriceRecommendationCallTimeout() throws Exception {
        AccountServiceImpl listingResource = new AccountServiceImpl();
        listingResource.setup();
//        AccountBiz accountBiz = mock(AccountBiz.class);
        SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
//        setBeanProperty(listingResource, "accountBiz", accountBiz);
        setBeanProperty(listingResource, "securityContextUtil", securityContextUtil);
        setBeanProperty(listingResource, "eventMetaHelper", mock(EventMetaHelper.class));
        PricingRecHelper pricingRecHelper = mock(PricingRecHelper.class);
        setBeanProperty(listingResource, "pricingRecHelper", pricingRecHelper);
        EventUtil eventUtil = mock(EventUtil.class);
        setBeanProperty(listingResource, "eventUtil", eventUtil);
        when(eventUtil.getLocalizedSeatTraitName((Long) anyObject(), anyString(), anyString()))
                .thenReturn("A");

        QueryResponse response = new QueryResponse();
        SolrDocumentList list = new SolrDocumentList();
        list.setNumFound(5);
        list.setStart(0);
        list.add(mockSolrDocument(100, "DELETED", 10001, "3", "7", "1"));
        list.add(mockSolrDocument(200, "ACTIVE", 10002, "2", "10", "0"));
        list.add(mockSolrDocument(300, "PENDING_LOCK", 10003, "1", "6", "1"));
        list.add(mockSolrDocument(400, "INCOMPLETE", 10004, "1", "4", "1"));
        list.add(mockSolrDocument(500, "INACTIVE", 10005, "3", "1", "0"));
        list.get(0).setField("SELLER_CONTACT_ID", null);
        NamedList responseList = new NamedList();
        responseList.add("response", list);
        response.setResponse(responseList);
//        when(accountBiz.getMyListings((ListingSearchCriteria) argThat(getMatcher())))
//                .thenReturn(response);

        Answer answer = new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                Thread.sleep(15000);
                return null;
            }
        };
        doAnswer(answer).when(pricingRecHelper).handlePriceRecommendations(anyList(), anyString());
        listingSetup(listingResource, response);
        ListingsResponse listResponse = listingResource.getMyListings(serviceContext,
                securityContext, i18nContext, "B5D14E323CD55E9FE04400144F8AE084", null, null, null, "true", null, null, null, "false");
        assertNotNull(listResponse);
    }

    @Test
    public void testMyListingsWithPriceRecommendationThrowExceptionForFutures() throws Exception {
        AccountServiceImpl listingResource = new AccountServiceImpl();
        listingResource.setup();
//        AccountBiz accountBiz = mock(AccountBiz.class);
        SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
//        setBeanProperty(listingResource, "accountBiz", accountBiz);
        setBeanProperty(listingResource, "securityContextUtil", securityContextUtil);
        setBeanProperty(listingResource, "eventMetaHelper", mock(EventMetaHelper.class));
        PricingRecHelper pricingRecHelper = mock(PricingRecHelper.class);
        setBeanProperty(listingResource, "pricingRecHelper", pricingRecHelper);
        EventUtil eventUtil = mock(EventUtil.class);
        setBeanProperty(listingResource, "eventUtil", eventUtil);
        when(eventUtil.getLocalizedSeatTraitName((Long) anyObject(), anyString(), anyString()))
                .thenReturn("A");

        QueryResponse response = new QueryResponse();
        SolrDocumentList list = new SolrDocumentList();
        list.setNumFound(5);
        list.setStart(0);
        list.add(mockSolrDocument(100, "DELETED", 10001, "3", "7", "1"));
        list.add(mockSolrDocument(200, "ACTIVE", 10002, "2", "10", "0"));
        list.add(mockSolrDocument(300, "PENDING_LOCK", 10003, "1", "6", "1"));
        list.add(mockSolrDocument(400, "INCOMPLETE", 10004, "1", "4", "1"));
        list.add(mockSolrDocument(500, "INACTIVE", 10005, "3", "1", "0"));
        list.get(0).setField("SELLER_CONTACT_ID", null);
        NamedList responseList = new NamedList();
        responseList.add("response", list);
        response.setResponse(responseList);
//        when(accountBiz.getMyListings((ListingSearchCriteria) argThat(getMatcher())))
//                .thenReturn(response);
        listingSetup(listingResource, response);
        Answer answer = new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                throw new Exception();
            }
        };
        doAnswer(answer).when(pricingRecHelper).handlePriceRecommendations(anyList(), anyString());
        ListingsResponse listResponse = listingResource.getMyListings(serviceContext,
                securityContext, i18nContext, "B5D14E323CD55E9FE04400144F8AE084", null, null, null, "true", null, null, null, "false");
        assertNotNull(listResponse);
    }

    @Test
    public void testMyListingsWithPriceRecommendationWithNoListing() throws Exception {
        AccountServiceImpl listingResource = new AccountServiceImpl();
        listingResource.setup();
//        AccountBiz accountBiz = mock(AccountBiz.class);
        SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
//        setBeanProperty(listingResource, "accountBiz", accountBiz);
        setBeanProperty(listingResource, "securityContextUtil", securityContextUtil);
        setBeanProperty(listingResource, "eventMetaHelper", mock(EventMetaHelper.class));
        PricingRecHelper pricingRecHelper = mock(PricingRecHelper.class);
        setBeanProperty(listingResource, "pricingRecHelper", pricingRecHelper);
        EventUtil eventUtil = mock(EventUtil.class);
        setBeanProperty(listingResource, "eventUtil", eventUtil);
        when(eventUtil.getLocalizedSeatTraitName((Long) anyObject(), anyString(), anyString()))
                .thenReturn("A");

        QueryResponse response = new QueryResponse();
        SolrDocumentList list = new SolrDocumentList();
        list.setNumFound(0);
        list.setStart(0);
        NamedList responseList = new NamedList();
        responseList.add("response", list);
        response.setResponse(responseList);
        Mockito.when(pricingRecHelper.handlePriceRecommendations(anyList(), anyString())).thenReturn(null);
//        when(accountBiz.getMyListings((ListingSearchCriteria) argThat(getMatcher())))
//                .thenReturn(response);
        listingSetup(listingResource, response);
        ListingsResponse listResponse = listingResource.getMyListings(serviceContext,
                securityContext, i18nContext, "B5D14E323CD55E9FE04400144F8AE084", null, null, null, "true", null, null, null, "false");
        assertNotNull(listResponse);

//        when(accountBiz.getMyListings((ListingSearchCriteria) argThat(getMatcher())))
//                .thenReturn(null);
        listingSetup(listingResource, null);
        listResponse = listingResource.getMyListings(serviceContext,
                securityContext, i18nContext, "B5D14E323CD55E9FE04400144F8AE084", null, null, null, "true", null, null, null, "false");
//        assertNull(listResponse);
    }

    @Test
    public void testMyListingsWithPriceRecommendationThrowsRejectException() throws Exception {
        AccountServiceImpl listingResource = new AccountServiceImpl();
        ThreadPoolTaskExecutor listingThreadPool = mock(ThreadPoolTaskExecutor.class);
//        AccountBiz accountBiz = mock(AccountBiz.class);
        SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
//        setBeanProperty(listingResource, "accountBiz", accountBiz);
        setBeanProperty(listingResource, "securityContextUtil", securityContextUtil);
        setBeanProperty(listingResource, "eventMetaHelper", mock(EventMetaHelper.class));
        setBeanProperty(listingResource, "listingThreadPool", listingThreadPool);
        PricingRecHelper pricingRecHelper = mock(PricingRecHelper.class);
        setBeanProperty(listingResource, "pricingRecHelper", pricingRecHelper);
        EventUtil eventUtil = mock(EventUtil.class);
        setBeanProperty(listingResource, "eventUtil", eventUtil);
        when(eventUtil.getLocalizedSeatTraitName((Long) anyObject(), anyString(), anyString()))
                .thenReturn("A");

        QueryResponse response = new QueryResponse();
        SolrDocumentList list = new SolrDocumentList();
        list.setNumFound(5);
        list.setStart(0);
        list.add(mockSolrDocument(100, "DELETED", 10001, "3", "7", "1"));
        list.add(mockSolrDocument(200, "ACTIVE", 10002, "2", "10", "0"));
        list.add(mockSolrDocument(300, "PENDING_LOCK", 10003, "1", "6", "1"));
        list.add(mockSolrDocument(400, "INCOMPLETE", 10004, "1", "4", "1"));
        list.add(mockSolrDocument(500, "INACTIVE", 10005, "3", "1", "0"));
        list.get(0).setField("SELLER_CONTACT_ID", null);
        NamedList responseList = new NamedList();
        responseList.add("response", list);
        response.setResponse(responseList);
//        when(accountBiz.getMyListings((ListingSearchCriteria) argThat(getMatcher())))
//                .thenReturn(response);
        listingSetup(listingResource, response);
        Mockito.when(pricingRecHelper.handlePriceRecommendations(anyList(), anyString())).thenReturn(null);

        when(listingThreadPool.submit(any(PricingRecTask.class))).thenThrow(TaskRejectedException.class);
        ListingsResponse listResponse = listingResource.getMyListings(serviceContext,
                securityContext, i18nContext, "B5D14E323CD55E9FE04400144F8AE084", null, null, null, "true", null, null, null, "false");

        assertNotNull(listResponse);
    }


    @Test
    public void testMyListingsWithPriceRecommendationThrowsException() throws Exception {
        AccountServiceImpl listingResource = new AccountServiceImpl();
        ThreadPoolTaskExecutor listingThreadPool = mock(ThreadPoolTaskExecutor.class);
//        AccountBiz accountBiz = mock(AccountBiz.class);
        SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
//        setBeanProperty(listingResource, "accountBiz", accountBiz);
        setBeanProperty(listingResource, "securityContextUtil", securityContextUtil);
        setBeanProperty(listingResource, "eventMetaHelper", mock(EventMetaHelper.class));
        setBeanProperty(listingResource, "listingThreadPool", listingThreadPool);
        PricingRecHelper pricingRecHelper = mock(PricingRecHelper.class);
        setBeanProperty(listingResource, "pricingRecHelper", pricingRecHelper);
        EventUtil eventUtil = mock(EventUtil.class);
        setBeanProperty(listingResource, "eventUtil", eventUtil);
        when(eventUtil.getLocalizedSeatTraitName((Long) anyObject(), anyString(), anyString()))
                .thenReturn("A");

        QueryResponse response = new QueryResponse();
        SolrDocumentList list = new SolrDocumentList();
        list.setNumFound(5);
        list.setStart(0);
        list.add(mockSolrDocument(100, "DELETED", 10001, "3", "7", "1"));
        list.add(mockSolrDocument(200, "ACTIVE", 10002, "2", "10", "0"));
        list.add(mockSolrDocument(300, "PENDING_LOCK", 10003, "1", "6", "1"));
        list.add(mockSolrDocument(400, "INCOMPLETE", 10004, "1", "4", "1"));
        list.add(mockSolrDocument(500, "INACTIVE", 10005, "3", "1", "0"));
        list.get(0).setField("SELLER_CONTACT_ID", null);
        NamedList responseList = new NamedList();
        responseList.add("response", list);
        response.setResponse(responseList);
//        when(accountBiz.getMyListings((ListingSearchCriteria) argThat(getMatcher())))
//                .thenReturn(response);
        listingSetup(listingResource, response);
        Mockito.when(pricingRecHelper.handlePriceRecommendations(anyList(), anyString())).thenReturn(null);

        when(listingThreadPool.submit(any(PricingRecTask.class))).thenThrow(Exception.class);
        ListingsResponse listResponse = listingResource.getMyListings(serviceContext,
                securityContext, i18nContext, "B5D14E323CD55E9FE04400144F8AE084", null, null, null, "true", null, null, null, "false");

        assertNotNull(listResponse);
    }

    @Test
    public void getListings_User_NotAuthenticated() throws Exception {
        AccountServiceImpl listingResource = new AccountServiceImpl();
//        AccountBiz accountBiz = mock(AccountBiz.class);
        SecurityContextUtil securityContextUtil = new SecurityContextUtil();
//        setBeanProperty(listingResource, "accountBiz", accountBiz);
        setBeanProperty(listingResource, "securityContextUtil", securityContextUtil);
        setBeanProperty(listingResource, "eventMetaHelper", mock(EventMetaHelper.class));
        QueryResponse response = new QueryResponse();
        SolrDocumentList list = new SolrDocumentList();
        list.setNumFound(5);
        list.setStart(0);
        list.add(mockSolrDocument(100, "ACTIVE", 10001, "3", "7", "1"));
        list.add(mockSolrDocument(200, "ACTIVE", 10002, "2", "10", "1"));
        list.add(mockSolrDocument(300, "ACTIVE", 10003, "1", "6", "1"));
        list.add(mockSolrDocument(400, "ACTIVE", 10004, "1", "4", "1"));
        list.add(mockSolrDocument(500, "INACTIVE", 10005, "3", "1", "1"));
        NamedList responseList = new NamedList();
        responseList.add("response", list);
        response.setResponse(responseList);
//        when(accountBiz.getMyListings((ListingSearchCriteria) argThat(getMatcher())))
//                .thenReturn(response);
        listingSetup(listingResource, response);
        ListingsResponse listResponse = listingResource.getMyListings(serviceContext,
                securityContext, i18nContext, "eeeddd", null, null, null, null, null, null, null, "false");
    }

    @Test
    public void getListings_User_NullServiceContext() throws Exception {
        AccountServiceImpl listingResource = new AccountServiceImpl();
//        AccountBiz accountBiz = mock(AccountBiz.class);
        SecurityContextUtil securityContextUtil = new SecurityContextUtil();
//        setBeanProperty(listingResource, "accountBiz", accountBiz);
        setBeanProperty(listingResource, "securityContextUtil", securityContextUtil);
        setBeanProperty(listingResource, "eventMetaHelper", mock(EventMetaHelper.class));
        QueryResponse response = new QueryResponse();
        SolrDocumentList list = new SolrDocumentList();
        list.setNumFound(5);
        list.setStart(0);
        list.add(mockSolrDocument(100, "ACTIVE", 10001, "3", "7", "1"));
        list.add(mockSolrDocument(200, "ACTIVE", 10002, "2", "10", "1"));
        list.add(mockSolrDocument(300, "ACTIVE", 10003, "1", "6", "1"));
        list.add(mockSolrDocument(400, "ACTIVE", 10004, "1", "4", "1"));
        list.add(mockSolrDocument(500, "INACTIVE", 10005, "3", "1", "1"));
        NamedList responseList = new NamedList();
        responseList.add("response", list);
        response.setResponse(responseList);
//        when(accountBiz.getMyListings((ListingSearchCriteria) argThat(getMatcher())))
//                .thenReturn(response);
        listingSetup(listingResource, response);
        Mockito.when(serviceContext.getExtendedSecurityContext().getUserId()).thenReturn(null);
        ListingsResponse listResponseNull = listingResource.getMyListings(serviceContext,
                securityContext, i18nContext, "eeeddd", null, null, null, null, null, null, null, "false");
        assertNotNull(listResponseNull);
        Mockito.when(serviceContext.getExtendedSecurityContext()).thenReturn(null);
        ListingsResponse listResponseESCNull = listingResource.getMyListings(serviceContext,
                securityContext, i18nContext, "eeeddd", null, null, null, null, null, null, null, "false");
        assertNotNull(listResponseESCNull);
    }

    @Test
    public void getListings_WithFilters() throws Exception {
        AccountServiceImpl listingResource = new AccountServiceImpl();
//        AccountBiz accountBiz = mock(AccountBiz.class);
        SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
//        setBeanProperty(listingResource, "accountBiz", accountBiz);
        setBeanProperty(listingResource, "securityContextUtil", securityContextUtil);
        setBeanProperty(listingResource, "eventMetaHelper", mock(EventMetaHelper.class));
        setBeanProperty(listingResource, "messageSource", messageSource);
        QueryResponse response = new QueryResponse();
        SolrDocumentList list = new SolrDocumentList();
        list.setNumFound(5);
        list.setStart(0);
        list.add(mockSolrDocument(100, "ACTIVE", 10001, "1", "4", "1"));
        list.add(mockSolrDocument(200, "ACTIVE", 10002, "3", "6", "1"));
        NamedList responseList = new NamedList();
        responseList.add("response", list);
        response.setResponse(responseList);
//        when(accountBiz.getMyListings((ListingSearchCriteria) argThat(getMatcher())))
//                .thenReturn(response);
//        when(accountBiz.getMyListings((ListingSearchCriteria) argThat(getMatcher())))
//                .thenReturn(response);
        listingSetup(listingResource, response);
        PaginationInput input = new PaginationInput();
        input.setRows(2);
        input.setStart(0);

        Assert.assertEquals(ListingFilterType.Q, ListingFilterType.fromString(null));
        ListingsResponse listResponse = listingResource.getMyListings(serviceContext,
                securityContext, i18nContext, "B5D14E323CD55E9FE04400144F8AE084", input, "EVENT DESC",
                "EVENT:123455,EVENTDATE:[2012-09-01 TO 2014-09-01],PRICE:[20.00TO40.00],DELIVERYOPTION:BARCODE,STATUS:ACTIVE,GEOGRAPYID:(196976;197077),Q:10001,SALEENDDATE:[2012-09-01 TO 2014-09-01],INHANDDATE:[2012-09-01 TO 2014-09-01],GENREID:222,VENUEID:333,EXTERNALLISTINGID:abc123",
                null, null, null, null, "false");
        assertNotNull(listResponse);

        listResponse = listingResource.getMyListings(serviceContext, securityContext, i18nContext,
                "B5D14E323CD55E9FE04400144F8AE084", input, "EVENT DESC", "EVENTNAME:abc,VENUE:bcd",
                null, null, null, null, "false");
        assertNotNull(listResponse);
    }

    @Test
    public void getListings_sorting() throws Exception {
        AccountServiceImpl listingResource = new AccountServiceImpl();
//        AccountBiz accountBiz = mock(AccountBiz.class);
        SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
//        setBeanProperty(listingResource, "accountBiz", accountBiz);
        setBeanProperty(listingResource, "securityContextUtil", securityContextUtil);
        setBeanProperty(listingResource, "eventMetaHelper", mock(EventMetaHelper.class));
        QueryResponse response = new QueryResponse();
        SolrDocumentList list = new SolrDocumentList();
        list.setNumFound(5);
        list.setStart(0);
        list.add(mockSolrDocument(100, "ACTIVE", 10001, "3", "7", "1"));
        list.add(mockSolrDocument(200, "ACTIVE", 10002, "2", "10", "0"));
        list.add(mockSolrDocument(300, "ACTIVE", 10003, "1", "6", "1"));
        list.add(mockSolrDocument(400, "ACTIVE", 10004, "1", "4", "1"));
        list.add(mockSolrDocument(500, "INACTIVE", 10005, "3", "1", "1"));
        NamedList responseList = new NamedList();
        responseList.add("response", list);
        response.setResponse(responseList);
//        when(accountBiz.getMyListings((ListingSearchCriteria) argThat(getMatcher())))
//                .thenReturn(response);
        listingSetup(listingResource, response);
        PaginationInput input = new PaginationInput();
        input.setRows(-1);
        input.setStart(-1);
        ListingsResponse listResponse = listingResource.getMyListings(serviceContext,
                securityContext, i18nContext, "B5D14E323CD55E9FE04400144F8AE084", input,
                "SECTION DESC,PRICE ASC,QUANTITY DESC,INHANDDATE ASC,STATUS ASC, SALEENDDATE ASC, DELIVERYOPTION DESC, QUANTITY_REMAIN ASC, SALEENDDATE DESC",
                "EVENT:123455,EVENTDATE:2012-09-01,PRICE:40.00,DELIVERYOPTION:BARCODE,STATUS:ACTIVE,GEOGRAPYID:(196976;197077),Q:10001,SALEENDDATE:2012-09-01,INHANDDATE:2014-09-01",
                null, null, null, null, "false");
        assertNotNull(listResponse);
    }

    @Test
    public void getLmsListings() throws Exception {
        AccountServiceImpl listingResource = new AccountServiceImpl();
//        AccountBiz accountBiz = mock(AccountBiz.class);
        SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
//        setBeanProperty(listingResource, "accountBiz", accountBiz);
        setBeanProperty(listingResource, "securityContextUtil", securityContextUtil);
        QueryResponse response = new QueryResponse();
        SolrDocumentList list = new SolrDocumentList();
        list.setNumFound(5);
        list.setStart(0);
        list.add(mockSolrDocument(100, "ACTIVE", 10001, "3", "7", "1"));
        list.add(mockSolrDocument(200, "ACTIVE", 10002, "2", "10", "0"));
        list.add(mockSolrDocument(300, "ACTIVE", 10003, "1", "6", "1"));
        list.add(mockSolrDocument(400, "ACTIVE", 10004, "1", "4", "1"));
        list.add(mockSolrDocument(500, "INACTIVE", 10005, "3", "1", "1"));
        NamedList responseList = new NamedList();
        responseList.add("response", list);
        response.setResponse(responseList);
//        when(accountBiz.getListings(any(String.class), any(String.class), any(String.class),
//                any(PaginationInput.class))).thenReturn(response);
        listingSetup(listingResource, response);
        PaginationInput input = new PaginationInput();
        input.setRows(-1);
        input.setStart(-1);
        ListingsResponse listResponse = listingResource.getListings(securityContext, "123456,23455",
                "123456,23455", "ACTIVE", input);
        Assert.assertNotNull(listResponse);

    }

    @Test
    public void getLmsListings_NoPagination() throws Exception {
        AccountServiceImpl listingResource = new AccountServiceImpl();
//        AccountBiz accountBiz = mock(AccountBiz.class);
        SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
//        setBeanProperty(listingResource, "accountBiz", accountBiz);
        setBeanProperty(listingResource, "securityContextUtil", securityContextUtil);
        QueryResponse response = new QueryResponse();
        SolrDocumentList list = new SolrDocumentList();
        list.setNumFound(5);
        list.setStart(0);
        list.add(mockSolrDocument(100, "ACTIVE", 10001, "3", "7", "1"));
        list.add(mockSolrDocument(200, "ACTIVE", 10002, "2", "10", "0"));
        list.add(mockSolrDocument(300, "ACTIVE", 10003, "1", "6", "1"));
        list.add(mockSolrDocument(400, "ACTIVE", 10004, "1", "4", "1"));
        list.add(mockSolrDocument(500, "INACTIVE", 10005, "3", "1", "1"));
        NamedList responseList = new NamedList();
        responseList.add("response", list);
        response.setResponse(responseList);
//        when(accountBiz.getListings(any(String.class), any(String.class), any(String.class),
//                any(PaginationInput.class))).thenReturn(response);
        listingSetup(listingResource, response);
        PaginationInput input = null;
        ListingsResponse listResponse = listingResource.getListings(securityContext, "123456,23455",
                "123456,23455", "ACTIVE", input);
        Assert.assertNotNull(listResponse);

    }

    @Test
    public void getLmsListings_NoEventAndSeller() throws Exception {
        AccountServiceImpl listingResource = new AccountServiceImpl();
//        AccountBiz accountBiz = mock(AccountBiz.class);
        SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
//        setBeanProperty(listingResource, "accountBiz", accountBiz);
        setBeanProperty(listingResource, "securityContextUtil", securityContextUtil);
        QueryResponse response = new QueryResponse();
        SolrDocumentList list = new SolrDocumentList();
        list.setNumFound(5);
        list.setStart(0);
        list.add(mockSolrDocument(100, "ACTIVE", 10001, "3", "7", "1"));
        list.add(mockSolrDocument(200, "ACTIVE", 10002, "2", "10", "1"));
        list.add(mockSolrDocument(300, "ACTIVE", 10003, "1", "6", "1"));
        list.add(mockSolrDocument(400, "ACTIVE", 10004, "1", "4", "1"));
        list.add(mockSolrDocument(500, "INACTIVE", 10005, "3", "1", "1"));
        NamedList responseList = new NamedList();
        responseList.add("response", list);
        response.setResponse(responseList);
//        when(accountBiz.getListings(any(String.class), any(String.class), any(String.class),
//                any(PaginationInput.class))).thenReturn(response);
        listingSetup(listingResource, response);
        PaginationInput input = null;
        ListingsResponse listResponse = listingResource.getListings(securityContext, "", "",
                "ACTIVE", input);
        Assert.assertNotNull(listResponse);

    }

    @Test
    public void getLmsListings_AccountException() throws Exception {
        AccountServiceImpl listingResource = new AccountServiceImpl();
//        AccountBiz accountBiz = mock(AccountBiz.class);
        SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
//        setBeanProperty(listingResource, "accountBiz", accountBiz);
        setBeanProperty(listingResource, "securityContextUtil", securityContextUtil);
        QueryResponse response = new QueryResponse();
        SolrDocumentList list = new SolrDocumentList();
        list.setNumFound(5);
        list.setStart(0);
        list.add(mockSolrDocument(100, "ACTIVE", 10001, "3", "7", "1"));
        list.add(mockSolrDocument(200, "ACTIVE", 10002, "2", "10", "0"));
        list.add(mockSolrDocument(300, "ACTIVE", 10003, "1", "6", "1"));
        list.add(mockSolrDocument(400, "ACTIVE", 10004, "1", "4", "0"));
        list.add(mockSolrDocument(500, "INACTIVE", 10005, "3", "1", "1"));
        NamedList responseList = new NamedList();
        responseList.add("response", list);
        response.setResponse(responseList);
//        when(accountBiz.getListings(any(String.class), any(String.class), any(String.class),
//                any(PaginationInput.class))).thenThrow(new AccountException());
        listingSetup(listingResource, response);
        PaginationInput input = null;
        ListingsResponse listResponse = listingResource.getListings(securityContext, "123456,23455",
                "123456,23455", "ACTIVE", input);
        Assert.assertNotNull(listResponse);

    }

    @Test
    public void getLmsListings_Exception() throws Exception {
        AccountServiceImpl listingResource = new AccountServiceImpl();
//        AccountBiz accountBiz = mock(AccountBiz.class);
        SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
//        setBeanProperty(listingResource, "accountBiz", accountBiz);
        setBeanProperty(listingResource, "securityContextUtil", securityContextUtil);
        QueryResponse response = new QueryResponse();
        SolrDocumentList list = new SolrDocumentList();
        list.setNumFound(5);
        list.setStart(0);
        list.add(mockSolrDocument(100, "ACTIVE", 10001, "3", "7", "1"));
        list.add(mockSolrDocument(200, "ACTIVE", 10002, "2", "10", "0"));
        list.add(mockSolrDocument(300, "ACTIVE", 10003, "1", "6", "1"));
        list.add(mockSolrDocument(400, "ACTIVE", 10004, "1", "4", "0"));
        list.add(mockSolrDocument(500, "INACTIVE", 10005, "3", "1", "1"));
        NamedList responseList = new NamedList();
        responseList.add("response", list);
        response.setResponse(responseList);
//        when(accountBiz.getListings(any(String.class), any(String.class), any(String.class),
//                any(PaginationInput.class))).thenThrow(new Exception());
        listingSetup(listingResource, response);
        PaginationInput input = null;
        ListingsResponse listResponse = listingResource.getListings(securityContext, "123456,23455",
                "123456,23455", "ACTIVE", input);
        Assert.assertNotNull(listResponse);

    }

    @Test
    public void getLmsListings_SecurityContextNull() throws Exception {
        AccountServiceImpl listingResource = new AccountServiceImpl();
//        AccountBiz accountBiz = mock(AccountBiz.class);
        SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
//        setBeanProperty(listingResource, "accountBiz", accountBiz);
        setBeanProperty(listingResource, "securityContextUtil", securityContextUtil);
        QueryResponse response = new QueryResponse();
        SolrDocumentList list = new SolrDocumentList();
        list.setNumFound(5);
        list.setStart(0);
        list.add(mockSolrDocument(100, "ACTIVE", 10001, "3", "7", "1"));
        list.add(mockSolrDocument(200, "ACTIVE", 10002, "2", "10", "0"));
        list.add(mockSolrDocument(300, "ACTIVE", 10003, "1", "6", "1"));
        list.add(mockSolrDocument(400, "ACTIVE", 10004, "1", "4", "0"));
        list.add(mockSolrDocument(500, "INACTIVE", 10005, "3", "1", "1"));
        NamedList responseList = new NamedList();
        responseList.add("response", list);
        response.setResponse(responseList);
//        when(accountBiz.getListings(any(String.class), any(String.class), any(String.class),
//                any(PaginationInput.class))).thenReturn(response);
        listingSetup(listingResource, response);
        PaginationInput input = null;
        ListingsResponse listResponse = listingResource.getListings(null, "", "", "ACTIVE", input);
        Assert.assertNotNull(listResponse);

    }

    // TODO: Work on it in detail. This test is only to make coverage happy!
    @Test
    public void getMySales() throws Exception {
        AccountServiceImpl listingResource = new AccountServiceImpl();
        SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
        setBeanProperty(listingResource, "securityContextUtil", securityContextUtil);
        setBeanProperty(listingResource, "listingHelper", listingHelper);
        ReflectionTestUtils.setField(listingResource, "messageSource", messageSource);
        ReflectionTestUtils.setField(listingResource, "messageSourceBundle", messageSourceBundle);
        ReflectionTestUtils.setField(listingResource, "userDAO", userDAO);

        EventMetaHelper eventHelper = Mockito.mock(EventMetaHelper.class);
        ReflectionTestUtils.setField(listingResource, "eventMetaHelper", eventHelper);

        UserDomainHelper userDomainHelper = Mockito.mock(UserDomainHelper.class);
        ReflectionTestUtils.setField(listingResource, "userDomainHelper", userDomainHelper);

        EventUtil eventUtil = mock(EventUtil.class);
        ReflectionTestUtils.setField(listingResource, "eventUtil", eventUtil);
        Event event = new Event();
        Long eventId = 10001L;
        event.setId(eventId);
        event.setSeatTraits(new ArrayList<SeatTrait>());

        List<CommonAttribute> dynamicAttributes = new ArrayList<CommonAttribute>();
        CommonAttribute commonAttribute = new CommonAttribute();
        commonAttribute.setName(TRANSFER_EMAIL_ADDRESS);
        String buyerEmailAddress = "buyerEmailAddress@test.com";
        commonAttribute.setValue(buyerEmailAddress);
        dynamicAttributes.add(commonAttribute);
        event.setDynamicAttributes(dynamicAttributes);

        SeatTrait st = new SeatTrait();
        st.setId(66666L);
        SeatTrait st1 = new SeatTrait();
        st1.setId(959L);
        event.getSeatTraits().add(st);
        event.getSeatTraits().add(st1);

        Map<Long, Event> events = new HashMap<Long, Event>();
        events.put(eventId, event);
        when(eventUtil.getEventsV3(anySet(), anyString())).thenReturn(events);

        QueryResponse response = new QueryResponse();
        SolrDocumentList list = new SolrDocumentList();
        list.setNumFound(5);
        list.setStart(0);
        SolrDocument mockOrderSolrDocument = mockOrderSolrDocument(100, "ACTIVE", eventId, 100f, "1", "1", "3");
        mockOrderSolrDocument.setField("externalTransfer", true);
        list.add(mockOrderSolrDocument);
        list.add(mockOrderSolrDocument(200, "ACTIVE", 10002, 200f, "0", "4", "2"));
        list.add(mockOrderSolrDocument(300, "ACTIVE", 10003, 300f, "0", "6", "1"));
        list.add(mockOrderSolrDocument(400, "ACTIVE", 10004, 400f, "0", "10", "1"));
        list.add(mockOrderSolrDocument(500, "INACTIVE", 10005, 500f, "0", "7", "1"));
        list.add(mockOrderSolrDocument(600, "ACTIVE", 10001, 100f, "1", "1", "3"));
        list.add(mockOrderSolrDocument(700, "ACTIVE", 10001, 100f, "4", "1", "4"));

        NamedList responseList = new NamedList();
        responseList.add("response", list);
        response.setResponse(responseList);
        saleSetup(listingResource, response);

        SalesResponse salesResponse = listingResource.getMySales(serviceContext, securityContext, i18nContext,
                "B5D14E323CD55E9FE04400144F8AE084", null, null, null, null, null, null, null, null);

        Assert.assertNotNull(salesResponse);
        Assert.assertTrue(!salesResponse.getSales().get(0).getGA());

        //pojo coverage
        salesResponse.getSales().get(0).getDeliveryMethodId();
        salesResponse.getSales().get(0).getDeliveryMethodDisplayName();
        salesResponse.getSales().get(0).getDeliveryMethodLongAppInstruction();
        salesResponse.getSales().get(0).getDeliveryMethodLongInstruction();
        salesResponse.getSales().get(0).getDeliveryMethodShortAppInstruction();
        salesResponse.getSales().get(0).getDeliveryMethodShortInstruction();
        salesResponse.getSales().get(0).getFulfillmentMethodId();
        salesResponse.getSales().get(0).getFulfillmentMethodDisplayName();
        salesResponse.getSales().get(0).getFulfillmentMethodLongAppInstruction();
        salesResponse.getSales().get(0).getFulfillmentMethodLongInstruction();
        salesResponse.getSales().get(0).getFulfillmentMethodShortAppInstruction();
        salesResponse.getSales().get(0).getFulfillmentMethodShortInstruction();

        // verify Authz
        when(securityContextUtil.isAuthz(serviceContext)).thenReturn(true);
        salesResponse = listingResource.getMySales(serviceContext, securityContext, i18nContext,
                "B5D14E323CD55E9FE04400144F8AE084", null, null, null, null, null, null, null, null);
        Assert.assertNotNull(salesResponse);
        Assert.assertTrue(!salesResponse.getSales().get(0).getGA());
        // verify seller not found
        when(userDAO.findUserByGuid(anyString())).thenReturn(null);
        salesResponse = listingResource.getMySales(serviceContext, securityContext, i18nContext,
                "B5D14E323CD55E9FE04400144F8AE084", null, null, null, null, null, null, null, null);
        Assert.assertEquals(salesResponse.getErrors().get(0).getCode(), ErrorCode.USER_NOT_FOUND);

    }

    @Test
    public void getMySalesWithDeliveriesFlag() throws Exception {
        AccountService accountService = new AccountServiceImpl();
        SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
        setBeanProperty(accountService, "securityContextUtil", securityContextUtil);
        setBeanProperty(accountService, "listingHelper", listingHelper);
        setBeanProperty(accountService, "userContactBiz", userContactBiz);
        ReflectionTestUtils.setField(accountService, "messageSource", messageSource);
        ReflectionTestUtils.setField(accountService, "messageSourceBundle", messageSourceBundle);
        ReflectionTestUtils.setField(accountService, "userDAO", userDAO);
        EventMetaHelper eventHelper = Mockito.mock(EventMetaHelper.class);
        ReflectionTestUtils.setField(accountService, "eventMetaHelper", eventHelper);

        UserDomainHelper userDomainHelper = Mockito.mock(UserDomainHelper.class);
        ReflectionTestUtils.setField(accountService, "userDomainHelper", userDomainHelper);

        EventUtil eventUtil = mock(EventUtil.class);
        ReflectionTestUtils.setField(accountService, "eventUtil", eventUtil);
        Event event = new Event();
        Long eventId = 10001L;
        event.setId(eventId);
        event.setSeatTraits(new ArrayList<SeatTrait>());


        List<CommonAttribute> dynamicAttributes = new ArrayList<CommonAttribute>();
        CommonAttribute commonAttribute = new CommonAttribute();
        commonAttribute.setName(TRANSFER_EMAIL_ADDRESS);
        String buyerEmailAddress = "buyerEmailAddress@test.com";
        commonAttribute.setValue(buyerEmailAddress);
        dynamicAttributes.add(commonAttribute);
        event.setDynamicAttributes(dynamicAttributes);

        SeatTrait st = new SeatTrait();
        st.setId(66666L);
        SeatTrait st1 = new SeatTrait();
        st1.setId(959L);
        event.getSeatTraits().add(st);
        event.getSeatTraits().add(st1);

        Map<Long, Event> events = new HashMap<Long, Event>();
        events.put(eventId, event);
        when(eventUtil.getEventsV3(anySet(), anyString())).thenReturn(events);

        QueryResponse response = new QueryResponse();
        SolrDocumentList list = new SolrDocumentList();
        list.setNumFound(5);
        list.setStart(0);
        list.add(mockSolrDocumentWithDeliveriesflag(100, "ACTIVE", eventId, 100f, "1", "1", "3"));
        list.add(mockSolrDocumentWithDeliveriesflag(100, "ACTIVE", 10001, 100f, "1", "1", "3"));
        list.add(mockSolrDocumentWithDeliveriesflag(200, "ACTIVE", 10002, 200f, "1", "1", "3"));
        list.add(mockSolrDocumentWithDeliveriesflag(300, "ACTIVE", 10003, 300f, "1", "1", "3"));
        list.add(mockSolrDocumentWithDeliveriesflag(400, "ACTIVE", 10004, 400f, "1", "1", "3"));
        list.add(mockSolrDocumentWithDeliveriesflag(500, "ACTIVE", 10005, 500f, "1", "1", "3"));
        list.add(mockSolrDocumentWithDeliveriesflag(600, "ACTIVE", 10006, 600f, "1", "1", "3"));
        list.add(mockSolrDocumentWithDeliveriesflag(700, "ACTIVE", 10007, 700f, "1", "1", "3"));
        list.add(mockSolrDocumentWithDeliveriesflag(800, "ACTIVE", 10009, 800f, "1", "1", "3"));


        NamedList responseList = new NamedList();
        responseList.add("response", list);
        response.setResponse(responseList);
        saleSetup(accountService, response);
        SalesResponse salesResponse = accountService.getMySales(serviceContext, securityContext, i18nContext,
                "B5D14E323CD55E9FE04400144F8AE084", null, null, null, null, null, null, null, null);
        Assert.assertNotNull(salesResponse);
        Assert.assertNull(salesResponse.getErrors());
//        Assert.assertEquals(salesResponse.getSales().get(0).getDeliveriesFlag().longValue(),1L);
//        Assert.assertNotEquals(salesResponse.getSales().get(0).getLinks().get().longValue(),1L);
//        Assert.assertEquals(salesResponse.getSales().get(0).getSellerConfirm().longValue(),2L);


    }

    @Test
    public void getMySalesWithTransferredEmail() throws Exception {
        AccountServiceImpl accountService = new AccountServiceImpl();
        SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
        setBeanProperty(accountService, "securityContextUtil", securityContextUtil);
        setBeanProperty(accountService, "listingHelper", listingHelper);
        setBeanProperty(accountService, "userContactBiz", userContactBiz);
        ReflectionTestUtils.setField(accountService, "messageSource", messageSource);
        ReflectionTestUtils.setField(accountService, "messageSourceBundle", messageSourceBundle);
        ReflectionTestUtils.setField(accountService, "userDAO", userDAO);

        EventMetaHelper eventHelper = Mockito.mock(EventMetaHelper.class);
        ReflectionTestUtils.setField(accountService, "eventMetaHelper", eventHelper);

        UserDomainHelper userDomainHelper = Mockito.mock(UserDomainHelper.class);
        ReflectionTestUtils.setField(accountService, "userDomainHelper", userDomainHelper);

        EventUtil eventUtil = mock(EventUtil.class);
        ReflectionTestUtils.setField(accountService, "eventUtil", eventUtil);
        Event event = new Event();
        Long eventId = 10001L;
        event.setId(eventId);
        event.setSeatTraits(new ArrayList<SeatTrait>());

        List<CommonAttribute> dynamicAttributes = new ArrayList<CommonAttribute>();
        CommonAttribute commonAttribute = new CommonAttribute();
        commonAttribute.setName(TRANSFER_EMAIL_ADDRESS);
        String buyerEmailAddress = "buyerEmailAddress@test.com";
        commonAttribute.setValue(buyerEmailAddress);
        dynamicAttributes.add(commonAttribute);
        event.setDynamicAttributes(dynamicAttributes);

        SeatTrait st = new SeatTrait();
        st.setId(66666L);
        SeatTrait st1 = new SeatTrait();
        st1.setId(959L);
        event.getSeatTraits().add(st);
        event.getSeatTraits().add(st1);

        Map<Long, Event> events = new HashMap<Long, Event>();
        events.put(eventId, event);
        when(eventUtil.getEventsV3(anySet(), anyString())).thenReturn(events);

        QueryResponse response = new QueryResponse();
        SolrDocumentList list = new SolrDocumentList();
        list.setNumFound(5);
        list.setStart(0);
        SolrDocument mockOrderSolrDocument = mockOrderSolrDocument(100, "ACTIVE", eventId, 100f, "1", "1", "3");
        mockOrderSolrDocument.setField("externalTransfer", true);
        mockOrderSolrDocument.setField("DELIVERY_METHOD_ID", DeliveryMethod.Mobile_Transfer.getDeliveryMethodId());
        mockOrderSolrDocument.setField("BUYER_ID", 1544058L);
        list.add(mockOrderSolrDocument);
        NamedList responseList = new NamedList();
        responseList.add("response", list);
        response.setResponse(responseList);
        saleSetup(accountService, response);

        UserContact buyerContact = new UserContact();
        buyerContact.setEmail("abc@gmail.com");
        buyerContact.setFirstName("FirstName");
        buyerContact.setLastName("LastName");
        buyerContact.setPhoneNumber("12345678901");
        buyerContact.setPhoneCountryCd("1");
        when(userContactBiz.getDefaultUserContactByOwernId(1544058L)).thenReturn(buyerContact);

        SalesResponse salesResponse = accountService.getMySales(serviceContext, securityContext, i18nContext,
                "B5D14E323CD55E9FE04400144F8AE084", null, null, null, null, null, null, null, null);
        Assert.assertNull(salesResponse.getErrors());
    }

    @Test
    public void getMySalesWithDeliveryDisplayName() throws Exception {
        AccountServiceImpl listingResource = new AccountServiceImpl();
//        AccountBiz accountBiz = mock(AccountBiz.class);
        SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
//        setBeanProperty(listingResource, "accountBiz", accountBiz);
        setBeanProperty(listingResource, "securityContextUtil", securityContextUtil);
        setBeanProperty(listingResource, "listingHelper", listingHelper);
        ReflectionTestUtils.setField(listingResource, "messageSource", messageSource);
        ReflectionTestUtils.setField(listingResource, "messageSourceBundle", messageSourceBundle);

        Mockito.when(messageSourceBundle.getWindowMessage(anyLong(), anyLong(), anyString(), any(Locale.class))).thenReturn("Test");

        EventMetaHelper eventHelper = Mockito.mock(EventMetaHelper.class);
        ReflectionTestUtils.setField(listingResource, "eventMetaHelper", eventHelper);

        UserDomainHelper userDomainHelper = Mockito.mock(UserDomainHelper.class);
        ReflectionTestUtils.setField(listingResource, "userDomainHelper", userDomainHelper);

        EventUtil eventUtil = mock(EventUtil.class);
        ReflectionTestUtils.setField(listingResource, "eventUtil", eventUtil);
        Event event = new Event();
        event.setSeatTraits(new ArrayList<SeatTrait>());
        SeatTrait st = new SeatTrait();
        st.setId(66666L);
        SeatTrait st1 = new SeatTrait();
        st1.setId(959L);
        event.getSeatTraits().add(st);
        event.getSeatTraits().add(st1);
        when(eventUtil.getEventV3(anyString(), anyString())).thenReturn(event);

        QueryResponse response = new QueryResponse();
        SolrDocumentList list = new SolrDocumentList();
        list.setNumFound(5);
        list.setStart(0);
        list.add(mockOrderSolrDocument(100, "ACTIVE", 10001, 100f, "1", "1", "3"));
        list.add(mockOrderSolrDocument(200, "ACTIVE", 10002, 200f, "0", "4", "2"));
        list.add(mockOrderSolrDocument(300, "ACTIVE", 10003, 300f, "0", "6", "1"));
        list.add(mockOrderSolrDocument(400, "ACTIVE", 10004, 400f, "0", "10", "1"));
        list.add(mockOrderSolrDocument(500, "INACTIVE", 10005, 500f, "0", "7", "1"));
        list.add(mockOrderSolrDocument(600, "ACTIVE", 10001, 100f, "1", "1", "3"));
        list.add(mockOrderSolrDocument(700, "ACTIVE", 10001, 100f, "4", "1", "4"));


        NamedList responseList = new NamedList();
        responseList.add("response", list);
        response.setResponse(responseList);
//        when(accountBiz.getSales((SalesSearchCriteria) argThat(getMatcher()))).thenReturn(response);
        saleSetup(listingResource, response);
        SalesResponse salesResponse = listingResource.getMySales(serviceContext, securityContext, i18nContext,
                "B5D14E323CD55E9FE04400144F8AE084", null, null, null, null, null, null, null, null);
        // TODO
        Assert.assertNotNull(salesResponse);
        Assert.assertTrue(!salesResponse.getSales().get(0).getGA());

        //pojo coverage
        salesResponse.getSales().get(0).getDeliveryMethodId();
        salesResponse.getSales().get(0).getDeliveryMethodDisplayName();
        salesResponse.getSales().get(0).getDeliveryMethodLongAppInstruction();
        salesResponse.getSales().get(0).getDeliveryMethodLongInstruction();
        salesResponse.getSales().get(0).getDeliveryMethodShortAppInstruction();
        salesResponse.getSales().get(0).getDeliveryMethodShortInstruction();
        salesResponse.getSales().get(0).getFulfillmentMethodId();
        salesResponse.getSales().get(0).getFulfillmentMethodDisplayName();
        salesResponse.getSales().get(0).getFulfillmentMethodLongAppInstruction();
        salesResponse.getSales().get(0).getFulfillmentMethodLongInstruction();
        salesResponse.getSales().get(0).getFulfillmentMethodShortAppInstruction();
        salesResponse.getSales().get(0).getFulfillmentMethodShortInstruction();

        Assert.assertEquals(salesResponse.getSales().get(0).getDeliveryMethodDisplayName(), "Test");
    }


    @Test
    public void getMySalesForSolrCloud() throws Exception {
        AccountServiceImpl listingResource = new AccountServiceImpl();
        AccountSolrCloudBiz accountSolrCloudBiz = mock(AccountSolrCloudBiz.class);
        SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
        setBeanProperty(listingResource, "accountSolrCloudBiz", accountSolrCloudBiz);
        setBeanProperty(listingResource, "securityContextUtil", securityContextUtil);
        ReflectionTestUtils.setField(listingResource, "messageSource", messageSource);
        MasterStubHubProperties.setProperty("account.v1.sale.useSolrCloud", "true");
        try {
            EventMetaHelper eventHelper = Mockito.mock(EventMetaHelper.class);
            ReflectionTestUtils.setField(listingResource, "eventMetaHelper", eventHelper);

            UserDomainHelper userDomainHelper = Mockito.mock(UserDomainHelper.class);
            ReflectionTestUtils.setField(listingResource, "userDomainHelper", userDomainHelper);

            JsonNode response = null;
            when(accountSolrCloudBiz.getSellerSales(any(SalesSearchCriteria.class))).thenReturn(response);

            SalesResponse salesResponse = listingResource.getMySales(serviceContext, securityContext, i18nContext,
                    "B5D14E323CD55E9FE04400144F8AE084", null, null, null, null, null, null, null, null);
            // TODO
            Assert.assertNotNull(salesResponse);
        } finally {
            MasterStubHubProperties.setProperty("account.v1.sale.useSolrCloud", "false");
        }
    }

    @Test
    public void getMySalesForSolrCloudWithFulfillmentMethod() throws Exception {
        AccountServiceImpl listingResource = new AccountServiceImpl();
        AccountSolrCloudBiz accountSolrCloudBiz = mock(AccountSolrCloudBiz.class);
        SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
        setBeanProperty(listingResource, "accountSolrCloudBiz", accountSolrCloudBiz);
        setBeanProperty(listingResource, "securityContextUtil", securityContextUtil);
        ReflectionTestUtils.setField(listingResource, "messageSource", messageSource);
        MasterStubHubProperties.setProperty("account.v1.sale.useSolrCloud", "true");
        try {
            EventMetaHelper eventHelper = Mockito.mock(EventMetaHelper.class);
            ReflectionTestUtils.setField(listingResource, "eventMetaHelper", eventHelper);

            UserDomainHelper userDomainHelper = Mockito.mock(UserDomainHelper.class);
            ReflectionTestUtils.setField(listingResource, "userDomainHelper", userDomainHelper);

            JSONObject root = new JSONObject();
            JSONArray docs = new JSONArray();
            JSONObject doc1 = new JSONObject();
            doc1.append("fulfillmentMethodId", 2);
            docs.put(doc1);
            root.append("response", docs);
            root.append("numFound", 1);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode response = mapper.readTree(root.toString());
//			JsonNode response = mapper.convertValue(root, JsonNode.class);

            when(accountSolrCloudBiz.getSellerSales(any(SalesSearchCriteria.class))).thenReturn(response);

            SalesResponse salesResponse = listingResource.getMySales(serviceContext, securityContext, i18nContext,
                    "B5D14E323CD55E9FE04400144F8AE084", null, null, null, null, null, null, null, null);
            // TODO
            Assert.assertNotNull(salesResponse);
        } finally {
            MasterStubHubProperties.setProperty("account.v1.sale.useSolrCloud", "false");
        }
    }

    @Test
    public void getMySalesWithTicketDetail() throws Exception {
        AccountServiceImpl listingResource = new AccountServiceImpl();
//        AccountBiz accountBiz = mock(AccountBiz.class);
        StubTransDetailDAOImpl stubTransDetailDAO = mock(StubTransDetailDAOImpl.class);
        SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
//        setBeanProperty(listingResource, "accountBiz", accountBiz);
        setBeanProperty(listingResource, "stubTransDetailDAO", stubTransDetailDAO);
        setBeanProperty(listingResource, "securityContextUtil", securityContextUtil);
        setBeanProperty(listingResource, "listingHelper", listingHelper);
        ReflectionTestUtils.setField(listingResource, "messageSource", messageSource);
        ReflectionTestUtils.setField(listingResource, "messageSourceBundle", messageSourceBundle);

        EventMetaHelper eventHelper = Mockito.mock(EventMetaHelper.class);
        ReflectionTestUtils.setField(listingResource, "eventMetaHelper", eventHelper);

        EventUtil eventUtil = mock(EventUtil.class);
        setBeanProperty(listingResource, "eventUtil", eventUtil);

        UserDomainHelper userDomainHelper = Mockito.mock(UserDomainHelper.class);
        ReflectionTestUtils.setField(listingResource, "userDomainHelper", userDomainHelper);
        QueryResponse response = new QueryResponse();
        SolrDocumentList list = new SolrDocumentList();
        list.setNumFound(5);
        list.setStart(0);
        list.add(mockOrderSolrDocumentWithoutInhandDate(100, "ACTIVE", 10001, 100f, "1", "1", "3"));
        list.add(mockOrderSolrDocumentWithoutInhandDate(200, "ACTIVE", 10002, 200f, "0", "4", "2"));
        list.add(mockOrderSolrDocumentWithoutInhandDate(300, "ACTIVE", 10003, 300f, "0", "6", "1"));
        list.add(mockOrderSolrDocumentWithoutInhandDate(400, "ACTIVE", 10004, 400f, "0", "1", "1"));
        list.add(mockOrderSolrDocumentWithoutInhandDate(500, "INACTIVE", 10005, 500f, "0", "7", "1"));
        NamedList responseList = new NamedList();
        responseList.add("response", list);
        response.setResponse(responseList);
        List<StubTransDetail> stubTransDetail = new ArrayList<StubTransDetail>();
        StubTransDetail sd = new StubTransDetail();
        sd.setTid(100L);
        sd.setSeatNumber("1");
        sd.setTicketSeatId(20000L);
        stubTransDetail.add(sd);
        when(stubTransDetailDAO.getSeatDetails(anyLong())).thenReturn(stubTransDetail);
//        when(accountBiz.getSales((SalesSearchCriteria) argThat(getMatcher()))).thenReturn(response);
        saleSetup(listingResource, response);
        SalesResponse salesResponse = listingResource.getMySales(serviceContext, securityContext, i18nContext,
                "B5D14E323CD55E9FE04400144F8AE084", null, null, null, null, "true", null, null, null);
        // TODO
        Assert.assertNotNull(salesResponse);
        Assert.assertTrue(!salesResponse.getSales().get(0).getGA());
    }


    @Test
    public void getMySalesWithNonTicketDetail() throws Exception {
        AccountServiceImpl listingResource = new AccountServiceImpl();
//        AccountBiz accountBiz = mock(AccountBiz.class);
        StubTransDetailDAOImpl stubTransDetailDAO = mock(StubTransDetailDAOImpl.class);
        SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
//        setBeanProperty(listingResource, "accountBiz", accountBiz);
        setBeanProperty(listingResource, "stubTransDetailDAO", stubTransDetailDAO);
        setBeanProperty(listingResource, "securityContextUtil", securityContextUtil);
        setBeanProperty(listingResource, "listingHelper", listingHelper);
        ReflectionTestUtils.setField(listingResource, "messageSource", messageSource);
        ReflectionTestUtils.setField(listingResource, "messageSourceBundle", messageSourceBundle);

        EventUtil eventUtil = mock(EventUtil.class);
        setBeanProperty(listingResource, "eventUtil", eventUtil);

        EventMetaHelper eventHelper = Mockito.mock(EventMetaHelper.class);
        ReflectionTestUtils.setField(listingResource, "eventMetaHelper", eventHelper);
        UserDomainHelper userDomainHelper = Mockito.mock(UserDomainHelper.class);
        ReflectionTestUtils.setField(listingResource, "userDomainHelper", userDomainHelper);
        QueryResponse response = new QueryResponse();
        SolrDocumentList list = new SolrDocumentList();
        list.setNumFound(5);
        list.setStart(0);
        list.add(mockOrderSolrDocumentWithoutInhandDate(100, "ACTIVE", 10001, 100f, "1", "1", "3"));
        list.add(mockOrderSolrDocumentWithoutInhandDate(200, "ACTIVE", 10002, 200f, "0", "4", "2"));
        list.add(mockOrderSolrDocumentWithoutInhandDate(300, "ACTIVE", 10003, 300f, "0", "6", "1"));
        list.add(
                mockOrderSolrDocumentWithoutInhandDate(400, "ACTIVE", 10004, 400f, "0", "10", "1"));
        list.add(mockOrderSolrDocumentWithoutInhandDate(500, "INACTIVE", 10005, 500f, "0", "7",
                "1"));
        NamedList responseList = new NamedList();
        responseList.add("response", list);
        response.setResponse(responseList);

        when(stubTransDetailDAO.getSeatDetails(anyLong())).thenReturn(null);
//        when(accountBiz.getSales((SalesSearchCriteria) argThat(getMatcher()))).thenReturn(response);
        saleSetup(listingResource, response);
        SalesResponse salesResponse = listingResource.getMySales(serviceContext, securityContext, i18nContext,
                "B5D14E323CD55E9FE04400144F8AE084", null, null, null, null, "true", null, null, null);
        // TODO
        Assert.assertNotNull(salesResponse);
        Assert.assertTrue(!salesResponse.getSales().get(0).getGA());
    }

    @Test
    public void getMySalesWithoutnoConfirmBtn() throws Exception {
        AccountServiceImpl accountService = new AccountServiceImpl();
        SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
        StubTransDetailDAOImpl stubTransDetailDAO = mock(StubTransDetailDAOImpl.class);
        SystemSettingsDAOImpl systemSettingsDAO = mock(SystemSettingsDAOImpl.class);
        setBeanProperty(accountService, "securityContextUtil", securityContextUtil);
        setBeanProperty(accountService, "listingHelper", listingHelper);
        setBeanProperty(accountService, "userContactBiz", userContactBiz);
        ReflectionTestUtils.setField(accountService, "messageSource", messageSource);
        ReflectionTestUtils.setField(accountService, "messageSourceBundle", messageSourceBundle);
        ReflectionTestUtils.setField(accountService, "userDAO", userDAO);
        ReflectionTestUtils.setField(accountService, "stubTransDetailDAO", stubTransDetailDAO);
        ReflectionTestUtils.setField(accountService, "systemSettingsDAO", systemSettingsDAO);
        ReflectionTestUtils.setField(accountService, "settingCache", settingCache);
        SystemSettings systemSettings = new SystemSettings();
        systemSettings.setValue("true");
        systemSettings.setName("user_group_ctl_btn");
        EventMetaHelper eventHelper = Mockito.mock(EventMetaHelper.class);
        ReflectionTestUtils.setField(accountService, "eventMetaHelper", eventHelper);

        UserDomainHelper userDomainHelper = Mockito.mock(UserDomainHelper.class);
        ReflectionTestUtils.setField(accountService, "userDomainHelper", userDomainHelper);

        EventUtil eventUtil = mock(EventUtil.class);
        ReflectionTestUtils.setField(accountService, "eventUtil", eventUtil);
        Event event = new Event();
        Long eventId = 10001L;
        event.setId(eventId);
        event.setSeatTraits(new ArrayList<SeatTrait>());

        List<CommonAttribute> dynamicAttributes = new ArrayList<CommonAttribute>();
        CommonAttribute commonAttribute = new CommonAttribute();
        commonAttribute.setName(TRANSFER_EMAIL_ADDRESS);
        String buyerEmailAddress = "buyerEmailAddress@test.com";
        commonAttribute.setValue(buyerEmailAddress);
        dynamicAttributes.add(commonAttribute);
        event.setDynamicAttributes(dynamicAttributes);

        SeatTrait st = new SeatTrait();
        st.setId(66666L);
        SeatTrait st1 = new SeatTrait();
        st1.setId(959L);
        event.getSeatTraits().add(st);
        event.getSeatTraits().add(st1);

        Map<Long, Event> events = new HashMap<Long, Event>();
        events.put(eventId, event);
        when(eventUtil.getEventsV3(anySet(), anyString())).thenReturn(events);

        QueryResponse response = new QueryResponse();
        SolrDocumentList list = new SolrDocumentList();
        list.setNumFound(5);
        list.setStart(0);
        SolrDocument mockOrderSolrDocument = mockOrderSolrDocument(100, "ACTIVE", eventId, 100f, "1", "18", "7");
        mockOrderSolrDocument.setField("externalTransfer", true);
        mockOrderSolrDocument.setField("DELIVERY_METHOD_ID", DeliveryMethod.Mobile_Transfer.getDeliveryMethodId());
        mockOrderSolrDocument.setField("BUYER_ID", 1544058L);
        mockOrderSolrDocument.setField("fulfillmentMethodId", 18L);
        mockOrderSolrDocument.setField("newUrlTransInd", 1L);
        list.add(mockOrderSolrDocument);
        NamedList responseList = new NamedList();
        responseList.add("response", list);
        response.setResponse(responseList);
        saleSetup(accountService, response);

        UserContact buyerContact = new UserContact();
        buyerContact.setEmail("abc@gmail.com");
        buyerContact.setFirstName("FirstName");
        buyerContact.setLastName("LastName");
        buyerContact.setPhoneNumber("12345678901");
        buyerContact.setPhoneCountryCd("1");
        when(userContactBiz.getDefaultUserContactByOwernId(1544058L)).thenReturn(buyerContact);
        List<StubTransDetail> stubTransDetail = new ArrayList<StubTransDetail>();
        StubTransDetail sd = new StubTransDetail();
        sd.setTid(100L);
        sd.setSeatNumber("1");
        sd.setTicketSeatId(20000L);
        stubTransDetail.add(sd);
        when(stubTransDetailDAO.getSeatDetails(anyLong())).thenReturn(stubTransDetail);
        when(systemSettingsDAO.findByName(anyString())).thenReturn(systemSettings);
        when(settingCache.get(anyString())).thenReturn("true");
//        when(accountBiz.getSales((SalesSearchCriteria) argThat(getMatcher()))).thenReturn(response);
        saleSetup(accountService, response);

        SalesResponse salesResponse = accountService.getMySales(serviceContext, securityContext, i18nContext,
                "B5D14E323CD55E9FE04400144F8AE084", null, null, null, null, null, null, null, null);
        Assert.assertEquals(salesResponse.getSales().get(0).getLinks().get(0).getRel(), "sale.sellerConfirmForOneButton");

        when(userDomainHelper.isUserMemberOfGroup(anyString(), anyLong())).thenReturn(false);
        SalesResponse salesResponse1 = accountService.getMySales(serviceContext, securityContext, i18nContext,
                "B5D14E323CD55E9FE04400144F8AE084", null, null, null, null, null, null, null, "true");
        Assert.assertEquals(salesResponse1.getSales().get(0).getLinks().get(0).getRel(), "sale.NONE_BTN");

        when(userDomainHelper.isUserMemberOfGroup(anyString(), anyLong())).thenReturn(true);
        SalesResponse salesResponse2 = accountService.getMySales(serviceContext, securityContext, i18nContext,
                "B5D14E323CD55E9FE04400144F8AE084", null, null, null, null, null, null, null, "true");
        Assert.assertEquals(salesResponse2.getSales().get(0).getLinks().get(0).getRel(), "sale.CLM_BTN");
    }

    @Test
    public void getMySalesWithSummary() throws Exception {
        AccountServiceImpl listingResource = new AccountServiceImpl();
//        AccountBiz accountBiz = mock(AccountBiz.class);
        SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
//        setBeanProperty(listingResource, "accountBiz", accountBiz);
        setBeanProperty(listingResource, "securityContextUtil", securityContextUtil);
        setBeanProperty(listingResource, "listingHelper", listingHelper);
        ReflectionTestUtils.setField(listingResource, "messageSource", messageSource);
        ReflectionTestUtils.setField(listingResource, "messageSourceBundle", messageSourceBundle);

        EventUtil eventUtil = mock(EventUtil.class);
        setBeanProperty(listingResource, "eventUtil", eventUtil);

        EventMetaHelper eventHelper = Mockito.mock(EventMetaHelper.class);
        ReflectionTestUtils.setField(listingResource, "eventMetaHelper", eventHelper);
        UserDomainHelper userDomainHelper = Mockito.mock(UserDomainHelper.class);
        ReflectionTestUtils.setField(listingResource, "userDomainHelper", userDomainHelper);
        QueryResponse response = new QueryResponse();
        SolrDocumentList list = new SolrDocumentList();
        list.setNumFound(5);
        list.setStart(0);
        list.add(mockOrderSolrDocument(100, "ACTIVE", 10001, 100f, "1", "1", "3"));
        list.add(mockOrderSolrDocument(200, "ACTIVE", 10002, 200f, "0", "4", "2"));
        list.add(mockOrderSolrDocument(300, "ACTIVE", 10003, 300f, "0", "6", "1"));
        list.add(mockOrderSolrDocument(400, "ACTIVE", 10004, 400f, "0", "10", "1"));
        list.add(mockOrderSolrDocument(500, "INACTIVE", 10005, 500f, "0", "7", "1"));
        NamedList responseList = new NamedList();
        responseList.add("response", list);
        response.setResponse(responseList);
//        when(accountBiz.getSales((SalesSearchCriteria) argThat(getMatcher()))).thenReturn(response);

        NamedList<List<PivotField>> facetPivot = new NamedList<List<PivotField>>();
        List<PivotField> pivotFields = new ArrayList<PivotField>();
        List<PivotField> subpivotFields = new ArrayList<PivotField>();
        subpivotFields.add(new PivotField("VENUE_DESCRIPTION", "Gershwin Theatre", 2, null));
        pivotFields.add(new PivotField("VENUE_ID", "5548", 2, subpivotFields));
        facetPivot.add("VENUE_ID,VENUE_DESCRIPTION", pivotFields);
        ReflectionTestUtils.setField(response, "_facetPivot", facetPivot);

        SummaryInput summaryInput = new SummaryInput();
        summaryInput.setIncludeEventSummary(false);
        summaryInput.setIncludeGenreSummary(false);
        summaryInput.setIncludeVenueSummary(true);
        saleSetup(listingResource, response);
        SalesResponse salesResponse = listingResource.getMySales(serviceContext, securityContext, i18nContext,
                "B5D14E323CD55E9FE04400144F8AE084", null, null, null, null, null, null,
                summaryInput, null);

        Assert.assertNotNull(salesResponse);
        Assert.assertTrue(!salesResponse.getSales().get(0).getGA());

//        assertNotNull(salesResponse.getVenueSummary());
//        assertEquals(salesResponse.getVenueSummary().get(0).getId().longValue(), 5548L);
//        assertEquals(salesResponse.getVenueSummary().get(0).getDescription(), "Gershwin Theatre");
//        assertEquals(salesResponse.getVenueSummary().get(0).getCount(), 2);

        assertNull(salesResponse.getEventSummary());
        assertNull(salesResponse.getGenreSummary());

        salesResponse.setEventSummary(null);
        salesResponse.setGenreSummary(null);
        salesResponse.setVenueSummary(null);
    }

    @Test
    public void getMySales_User_NotAuthenticated() throws Exception {
        AccountServiceImpl listingResource = new AccountServiceImpl();
//        AccountBiz accountBiz = mock(AccountBiz.class);
        SecurityContextUtil securityContextUtil = new SecurityContextUtil();
//        setBeanProperty(listingResource, "accountBiz", accountBiz);
        setBeanProperty(listingResource, "securityContextUtil", securityContextUtil);
        ReflectionTestUtils.setField(listingResource, "messageSource", messageSource);

        QueryResponse response = new QueryResponse();
        SolrDocumentList list = new SolrDocumentList();
        list.setNumFound(5);
        list.setStart(0);
        list.add(mockOrderSolrDocument(100, "ACTIVE", 10001, 100f, "1", "1", "3"));
        list.add(mockOrderSolrDocument(200, "ACTIVE", 10002, 200f, "0", "4", "2"));
        list.add(mockOrderSolrDocument(300, "ACTIVE", 10003, 300f, "0", "6", "1"));
        list.add(mockOrderSolrDocument(400, "ACTIVE", 10004, 400f, "0", "10", "1"));
        list.add(mockOrderSolrDocument(500, "INACTIVE", 10005, 500f, "0", "7", "1"));
        NamedList responseList = new NamedList();
        responseList.add("response", list);
        response.setResponse(responseList);
//        when(accountBiz.getSales((SalesSearchCriteria) argThat(getMatcher()))).thenReturn(response);
        saleSetup(listingResource, response);
        SalesResponse salesResponse = listingResource.getMySales(serviceContext, securityContext, i18nContext,
                "eeeddd", null, null, null, null, null, null, null, null);
    }

    @Test
    public void getMySales_AccounException() throws Exception {
        AccountServiceImpl listingResource = new AccountServiceImpl();
//        AccountBiz accountBiz = mock(AccountBiz.class);
        SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
//        setBeanProperty(listingResource, "accountBiz", accountBiz);
        setBeanProperty(listingResource, "securityContextUtil", securityContextUtil);
        ReflectionTestUtils.setField(listingResource, "messageSource", messageSource);

        QueryResponse response = new QueryResponse();
        SolrDocumentList list = new SolrDocumentList();
        list.setNumFound(5);
        list.setStart(0);
        list.add(mockOrderSolrDocument(100, "ACTIVE", 10001, 100f, "1", "1", "3"));
        list.add(mockOrderSolrDocument(200, "ACTIVE", 10002, 200f, "0", "4", "2"));
        list.add(mockOrderSolrDocument(300, "ACTIVE", 10003, 300f, "0", "6", "1"));
        list.add(mockOrderSolrDocument(400, "ACTIVE", 10004, 400f, "0", "10", "1"));
        list.add(mockOrderSolrDocument(500, "INACTIVE", 10005, 500f, "0", "7", "1"));
        NamedList responseList = new NamedList();
        responseList.add("response", list);
        response.setResponse(responseList);
        com.stubhub.domain.account.common.Error listingError = new com.stubhub.domain.account.common.Error(
                ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT, "", "");
        AccountException ae = new AccountException(listingError);
//        when(accountBiz.getSales((SalesSearchCriteria) argThat(getMatcher()))).thenThrow(ae);
        saleSetup(listingResource, response);
        SalesResponse salesResponse = listingResource.getMySales(serviceContext, securityContext, i18nContext,
                "B5D14E323CD55E9FE04400144F8AE084", null, null, null, null, null, null, null, null);
        // TODO:
        // assertNotNull(salesResponse);
    }

    @Test
    public void getMySales_sorting() throws Exception {
        AccountServiceImpl listingResource = new AccountServiceImpl();
//        AccountBiz accountBiz = mock(AccountBiz.class);
        SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
//        setBeanProperty(listingResource, "accountBiz", accountBiz);
        setBeanProperty(listingResource, "securityContextUtil", securityContextUtil);
        ReflectionTestUtils.setField(listingResource, "messageSource", messageSource);

        QueryResponse response = new QueryResponse();
        SolrDocumentList list = new SolrDocumentList();
        list.setNumFound(5);
        list.setStart(0);
        list.add(mockOrderSolrDocument(100, "ACTIVE", 10001, 100f, "1", "1", "3"));
        list.add(mockOrderSolrDocument(200, "ACTIVE", 10002, 200f, "0", "4", "2"));
        list.add(mockOrderSolrDocument(300, "ACTIVE", 10003, 300f, "0", "6", "1"));
        list.add(mockOrderSolrDocument(400, "ACTIVE", 10004, 400f, "0", "10", "1"));
        list.add(mockOrderSolrDocument(500, "INACTIVE", 10005, 500f, "0", "7", "1"));
        NamedList responseList = new NamedList();
        responseList.add("response", list);
        response.setResponse(responseList);
//        when(accountBiz.getSales((SalesSearchCriteria) argThat(getMatcher()))).thenReturn(response);
        PaginationInput input = new PaginationInput();
        input.setRows(10);
        input.setStart(0);

        Assert.assertEquals(SaleSortType.SALEDATE, SaleSortType.fromString(null));
        SaleSortType.fromString("SECTION");
        SaleSortType.fromString("ROW");
        saleSetup(listingResource, response);
        SalesResponse salesResponse = listingResource.getMySales(serviceContext, securityContext, i18nContext,
                "B5D14E323CD55E9FE04400144F8AE084", input,
                "PRICE ASC,STATUS ASC,DELIVERYOPTION DESC,QUANTITY DESC,SALECATEGORY ASC,INHANDDATE ASC, EVENT ASC, EVENTDATE DESC, SECTION desc, ROW asc, SALEDATE asc, PAYOUT desc",
                "EVENT:123455 AND EVENTDATE:2012-09-01 AND PRICE:40.00 AND DELIVERYOPTION:BARCODE AND SALESTATUS:SHIPPED AND SALEDATE:2012-09-01 AND INHANDDATE:2012-09-01 AND Q:Giants",
                "true", null, null, null, null);
        // TODO
        // assertNotNull(salesResponse);
    }

    @Test
    public void getMySales_filetrs() throws Exception {
        AccountServiceImpl listingResource = new AccountServiceImpl();
//        AccountBiz accountBiz = mock(AccountBiz.class);
        SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
//        setBeanProperty(listingResource, "accountBiz", accountBiz);
        setBeanProperty(listingResource, "securityContextUtil", securityContextUtil);
        ReflectionTestUtils.setField(listingResource, "messageSource", messageSource);

        QueryResponse response = new QueryResponse();
        SolrDocumentList list = new SolrDocumentList();
        list.setNumFound(5);
        list.setStart(0);
        list.add(mockOrderSolrDocument(100, "ACTIVE", 10001, 100f, "1", "1", "3"));
        list.add(mockOrderSolrDocument(200, "ACTIVE", 10002, 200f, "0", "4", "2"));
        list.add(mockOrderSolrDocument(300, "ACTIVE", 10003, 300f, "0", "6", "1"));
        list.add(mockOrderSolrDocument(400, "ACTIVE", 10004, 400f, "0", "10", "1"));
        list.add(mockOrderSolrDocument(500, "INACTIVE", 10005, 500f, "0", "7", "1"));
        NamedList responseList = new NamedList();
        responseList.add("response", list);
        response.setResponse(responseList);
        // TODO
//        when(accountBiz.getSales((SalesSearchCriteria) argThat(getMatcher()))).thenReturn(response);
        PaginationInput input = new PaginationInput();
        input.setRows(10);
        input.setStart(0);

        Assert.assertEquals(SalesFilterType.Q, SalesFilterType.fromString(null));
        saleSetup(listingResource, response);
        SalesResponse salesResponse = listingResource.getMySales(serviceContext, securityContext, i18nContext,
                "B5D14E323CD55E9FE04400144F8AE084", input, "junk asc",
                "EVENT:123455 AND EVENTDATE:[2012-09-01 TO 2012-09-30] AND PRICE:[40.00 TO 30.00] AND DELIVERYOPTION:BARCODE AND SALESTATUS:SHIPPED AND LISTINGID:12345 AND SALEDATE:[2012-09-01 TO 2012-09-30] AND INHANDDATE:[2012-09-01 TO 2012-09-30] AND EVENTDATEUTC:[2012-09-01 TO 2012-09-30]"
                        + " AND GENREID:22 AND VENUEID:333 AND CATEGORY:OPEN AND ACTION:ENTER AND ACTION:GENERATE AND ACTION:REPRINT AND ACTION:UPLOAD",
                "false", null, null, null, null);
        // TODO
        // assertNotNull(salesResponse);
    }

    @Test
    public void getMySalesPrimarySellerWithBuyerEmail() throws Exception {
        String replyXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<response>" + ""
                + "<lst name=\"responseHeader\">" + "  <int name=\"status\">0</int>"
                + "  <int name=\"QTime\">1</int>" + "</lst>"
                + "<result name=\"response\" numFound=\"1\" start=\"0\">" + "  <doc>"
                + "<long name=\"ID\">600</long>"
//                + "    <str name=\"INHAND_DATE\">2015-12-28T00:00:00Z</str>"
                + "    <str name=\"INHAND_IND\">1</str>"
//                + "    <date name=\"LAST_UPDATED_TS\">2016-04-15T11:18:40Z</date>"
                + "    <long name=\"ORDER_PROC_STATUS_ID\">5000</long>"
                + "    <str name=\"ORDER_PROC_SORTING_SEQUENCE\">7</str>"
                + "    <long name=\"ORDER_PROC_SUB_STATUS_CODE\">34</long>"
                + "    <str name=\"ORDER_PROC_SUB_STATUS_DESC\">Delivered (PDF)</str>"
                + "    <arr name=\"SEAT_TRAITS\">"
                + "      <str>13688|You will need to set up a Flash Seats account to access your tickets (instructions will be emailed)|1|Ticket Feature</str><str/>"
                + "    </arr>"
//                + "    <str name=\"TRANSACTION_DATE\">2016-04-13T09:03:52Z</str>"
//                + "    <str name=\"SHIP_DATE\">2016-04-13T09:03:52Z</str>"
//                + "    <str name=\"EXPECTED_DELIVERY_DATE\">2016-04-13T09:03:47Z</str>"
                + "    <str name=\"TID\">194438858</str>"
                + "    <str name=\"SALE_METHOD_ID\">1</str>"
                + "    <str name=\"SALE_METHOD\">Fixed Price</str>"
                + "    <str name=\"FULFILLMENT_METHOD_ID\">4</str>"
                + "    <str name=\"FULFILLMENT_METHOD_DESC\">PDF - PreDelivery</str>"
                + "    <str name=\"LISTING_SOURCE_ID\">10</str>"
                + "    <str name=\"DISCOUNT_COST\">0</str>"
                + "    <str name=\"BUYER_SHIPPING_CONTACT_ID\">247288836</str>"
                + "    <float name=\"TOTAL_COST\">75.52</float>"
//                + "    <float name=\"SELLER_PAYOUT_AMOUNT\">60.16</float>"
                + "    <str name=\"SELLER_FEE_VAL\">3.2</str>"
                + "    <str name=\"SHIPPING_FEE_COST\">0</str>"
                + "    <str name=\"BUYER_FEE_COST\">9.6</str>"
                + "    <str name=\"TICKET_COST\">64</str>"
                + "    <str name=\"EVENT_ID\">9391144</str>"
                + "    <str name=\"SECTION\">Upper 331</str>"
                + "    <str name=\"TICKET_ID\">1163381357</str>"
                + "    <str name=\"QUANTITY\">2</str>" + "    <str name=\"ROW_DESC\">X</str>"
                + "    <str name=\"SEATS\">226,227</str>"
                + "    <str name=\"SELLER_ID\">29400764</str>"
                + "    <str name=\"BUYER_ID\">87052271</str>"
                + "    <str name=\"TICKET_MEDIUM_ID\">2</str>"
                + "    <str name=\"DELIVERY_OPTION_ID\">1</str>"
                + "    <str name=\"DELIVERY_METHOD_ID\">2</str>"
                + "    <str name=\"CONFIRMED_IND\">1</str>"
                + "    <str name=\"CONFIRM_OPTION_ID\">2</str>"
                + "    <str name=\"CURRENCY_CODE\">GBP</str>"
                + "    <str name=\"VAT_SELL_FEE\">.64</str>"
                + "    <str name=\"VAT_SELL_PRCNT\">.2</str>"
                + "    <str name=\"VAT_LOG_FEE\">0</str>"
                + "    <str name=\"VAT_LOG_PRCNT\">.2</str>"
                + "    <str name=\"VAT_BUY_FEE\">1.92</str>"
                + "    <str name=\"VAT_BUY_PRCNT\">.2</str>"
                + "    <str name=\"SUBBED_IND\">0</str>" + "    <str name=\"CANCELLED\">0</str>"
                + "    <long name=\"FRAUD_CHECK_STATUS_ID\">600</long>"
                + "    <str name=\"SELLER_PAYMENT_TYPE_ID\">1</str>"
                + "    <long name=\"VENUE_CONFIG_SECTIONS_ID\">1664329</long>"
                + "    <str name=\"SELLER_NOTES\"> </str>"
//                + "    <date name=\"SOLD_DATE\">2016-04-13T09:03:52Z</date>"
                + "    <int name=\"SOLD_QUANTITY\">2</int>"
                + "    <float name=\"PRICE_PER_TICKET\">32.0</float>"
                + "    <float name=\"COST_PER_TICKET\">37.76</float>"
                + "    <float name=\"STATS_COST_PER_TICKET\">37.76</float>"
//                + "    <date name=\"EVENT_DATE\">2016-04-17T17:00:00Z</date>"
//                + "    <str name=\"EVENT_DATE_LOCAL\">2016-04-17T18:00:00Z</str>"
                + "    <str name=\"EVENT_DESCRIPTION\">Muse Tickets</str>"
                + "    <str name=\"EVENT_STATUS_ID\">2</str>"
                + "    <str name=\"EVENT_STATUS_DESC\"/>"
//                + "    <str name=\"EARLIEST_POSSIBLE_INHAND_DATE\">2015-09-11T09:38:21Z</str>"
                + "    <str name=\"EVENT_CANCELLED_IND\">0</str>"
                + "    <str name=\"HIDE_EVENT_DATE\">0</str>"
                + "    <str name=\"HIDE_EVENT_TIME\">0</str>"
                + "    <int name=\"BOOK_OF_BUSINESS_ID\">2</int>"
                + "    <str name=\"JDK_TIMEZONE\">Europe/London</str>"
                + "    <str name=\"GENRE_ID\">27293</str>"
                + "    <str name=\"GENRE_DESCRIPTION\">Muse Tickets</str>"
                + "    <str name=\"GENRE_PATH\">1/63912/27293/</str>"
                + "    <str name=\"GENRE_ACTIVE\">1</str>"
                + "    <str name=\"VENUE_ID\">213386</str>"
                + "    <str name=\"GEOGRAPHY_ACTIVE\">1</str>"
                + "    <str name=\"VENUE_DESCRIPTION\">SSE Hydro Arena Glasgow</str>"
                + "    <str name=\"GEOGRAPHY_PATH\">6628/10923/10925/213386/</str>"
                + "    <str name=\"STATE\">GLG</str>" + "    <str name=\"COUNTRY\">GB</str>"
                + "    <str name=\"CITY\">Glasgow</str>"
//                + "    <date name=\"timestamp\">2016-04-15T11:18:41Z</date>"
                + "    <long name=\"_version_\">1531675013842731008</long></doc>" + "</result>"
                + "</response>";
        XMLResponseParser parser = new XMLResponseParser();
        Reader reader = new StringReader(replyXML);

        SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
        ReflectionTestUtils.setField(accountService, "securityContextUtil", securityContextUtil);
        EventMetaHelper eventHelper = mock(EventMetaHelper.class);
        ReflectionTestUtils.setField(accountService, "eventMetaHelper", eventHelper);

        EventUtil eventUtil = mock(EventUtil.class);
        ReflectionTestUtils.setField(accountService, "eventUtil", eventUtil);

        QueryResponse response = new QueryResponse();
        SolrDocumentList list = new SolrDocumentList();
        response.setResponse(parser.processResponse(reader));

        assertEquals(1, response.getResults().getNumFound());
        assertEquals("29400764", response.getResults().get(0).getFieldValue("SELLER_ID"));

        PaginationInput input = new PaginationInput();
        input.setRows(10);
        input.setStart(0);

        UserContact contact = new UserContact();
        String expected = "test@stubhub.com";
        contact.setEmail(expected);
        Mockito.when(userContactBiz.getDefaultUserContactByOwernId(anyLong())).thenReturn(contact);
//        Mockito.when(accountBiz.getSales(Mockito.any(SalesSearchCriteria.class)))
//                .thenReturn(response);
        Mockito.when(userDomainHelper.isUserMemberOfGroup(anyString(), anyLong())).thenReturn(true);
        saleSetup(accountService, response);
        SalesResponse salesResponse = accountService.getMySales(serviceContext, securityContext, null,
                "B5D14E323CD55E9FE04400144F8AE084", input, "junk asc", "EVENT_ID:9391144", "false",
                null, null, null, null);
        assertEquals(1L, salesResponse.getNumFound());
        assertEquals(expected, salesResponse.getSales().get(0).getBuyerEmail());

        // test npe defence on buyercontact

        Mockito.when(userContactBiz.getDefaultUserContactByOwernId(anyLong())).thenReturn(null);
//        Mockito.when(accountBiz.getSales(Mockito.any(SalesSearchCriteria.class)))
//                .thenReturn(response);
        salesResponse = accountService.getMySales(serviceContext, securityContext, null,
                "B5D14E323CD55E9FE04400144F8AE084", input, "junk asc", "EVENT_ID:9391144", "false",
                null, null, null, null);
        assertEquals(1L, salesResponse.getNumFound());
        assertEquals(null, salesResponse.getSales().get(0).getBuyerEmail());
    }


    @Test
    public void getMySalesPDFFulfillmentWithSeatTraitsWithBuyerEmail() throws Exception {
        String replyXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<response>" + ""
                + "<lst name=\"responseHeader\">" + "  <int name=\"status\">0</int>"
                + "  <int name=\"QTime\">1</int>" + "</lst>"
                + "<result name=\"response\" numFound=\"1\" start=\"0\">" + "  <doc>"
//                + "    <str name=\"INHAND_DATE\">2015-12-28T00:00:00Z</str>"
                + "    <str name=\"INHAND_IND\">1</str>"
                + "<long name=\"ID\">600</long>"
//                + "    <date name=\"LAST_UPDATED_TS\">2016-04-15T11:18:40Z</date>"
                + "    <long name=\"ORDER_PROC_STATUS_ID\">5000</long>"
                + "    <str name=\"ORDER_PROC_SORTING_SEQUENCE\">7</str>"
                + "    <long name=\"ORDER_PROC_SUB_STATUS_CODE\">34</long>"
                + "    <str name=\"ORDER_PROC_SUB_STATUS_DESC\">Delivered (PDF)</str>"
                + "    <arr name=\"SEAT_TRAITS\">"
                + "      <str>13688|You will need to set up a Flash Seats account to access your tickets (instructions will be emailed)|1|Ticket Feature</str><str/>"
                + "    </arr>" + "    <str name=\"TRANSACTION_DATE\">2016-04-13T09:03:52Z</str>"
//                + "    <str name=\"SHIP_DATE\">2016-04-13T09:03:52Z</str>"
//                + "    <str name=\"EXPECTED_DELIVERY_DATE\">2016-04-13T09:03:47Z</str>"
                + "    <str name=\"TID\">194438858</str>"
                + "    <str name=\"SALE_METHOD_ID\">1</str>"
                + "    <str name=\"SALE_METHOD\">Fixed Price</str>"
                + "    <str name=\"FULFILLMENT_METHOD_ID\">4</str>"
                + "    <str name=\"FULFILLMENT_METHOD_DESC\">PDF - PreDelivery</str>"
                + "    <str name=\"LISTING_SOURCE_ID\">10</str>"
                + "    <str name=\"DISCOUNT_COST\">0</str>"
                + "    <str name=\"BUYER_SHIPPING_CONTACT_ID\">247288836</str>"
                + "    <float name=\"TOTAL_COST\">75.52</float>"
//                + "    <float name=\"SELLER_PAYOUT_AMOUNT\">60.16</float>"
                + "    <str name=\"SELLER_FEE_VAL\">3.2</str>"
                + "    <str name=\"SHIPPING_FEE_COST\">0</str>"
                + "    <str name=\"BUYER_FEE_COST\">9.6</str>"
                + "    <str name=\"TICKET_COST\">64</str>"
                + "    <str name=\"EVENT_ID\">9391144</str>"
                + "    <str name=\"SECTION\">Upper 331</str>"
                + "    <str name=\"TICKET_ID\">1163381357</str>"
                + "    <str name=\"QUANTITY\">2</str>" + "    <str name=\"ROW_DESC\">X</str>"
                + "    <str name=\"SEATS\">226,227</str>"
                + "    <str name=\"SELLER_ID\">29400764</str>"
                + "    <str name=\"BUYER_ID\">87052271</str>"
                + "    <str name=\"TICKET_MEDIUM_ID\">2</str>"
                + "    <str name=\"DELIVERY_OPTION_ID\">1</str>"
                + "    <str name=\"DELIVERY_METHOD_ID\">2</str>"
                + "    <str name=\"CONFIRMED_IND\">1</str>"
                + "    <str name=\"CONFIRM_OPTION_ID\">2</str>"
                + "    <str name=\"CURRENCY_CODE\">GBP</str>"
                + "    <str name=\"VAT_SELL_FEE\">.64</str>"
                + "    <str name=\"VAT_SELL_PRCNT\">.2</str>"
                + "    <str name=\"VAT_LOG_FEE\">0</str>"
                + "    <str name=\"VAT_LOG_PRCNT\">.2</str>"
                + "    <str name=\"VAT_BUY_FEE\">1.92</str>"
                + "    <str name=\"VAT_BUY_PRCNT\">.2</str>"
                + "    <str name=\"SUBBED_IND\">0</str>" + "    <str name=\"CANCELLED\">0</str>"
                + "    <long name=\"FRAUD_CHECK_STATUS_ID\">600</long>"
                + "    <str name=\"SELLER_PAYMENT_TYPE_ID\">1</str>"
                + "    <long name=\"VENUE_CONFIG_SECTIONS_ID\">1664329</long>"
                + "    <str name=\"SELLER_NOTES\"> </str>"
//                + "    <date name=\"SOLD_DATE\">2016-04-13T09:03:52Z</date>"
                + "    <int name=\"SOLD_QUANTITY\">2</int>"
                + "    <float name=\"PRICE_PER_TICKET\">32.0</float>"
                + "    <float name=\"COST_PER_TICKET\">37.76</float>"
                + "    <float name=\"STATS_COST_PER_TICKET\">37.76</float>"
//                + "    <date name=\"EVENT_DATE\">2016-04-17T17:00:00Z</date>"
//                + "    <str name=\"EVENT_DATE_LOCAL\">2016-04-17T18:00:00Z</str>"
                + "    <str name=\"EVENT_DESCRIPTION\">Muse Tickets</str>"
                + "    <str name=\"EVENT_STATUS_ID\">2</str>"
                + "    <str name=\"EVENT_STATUS_DESC\"/>"
//                + "    <str name=\"EARLIEST_POSSIBLE_INHAND_DATE\">2015-09-11T09:38:21Z</str>"
                + "    <str name=\"EVENT_CANCELLED_IND\">0</str>"
                + "    <str name=\"HIDE_EVENT_DATE\">0</str>"
                + "    <str name=\"HIDE_EVENT_TIME\">0</str>"
                + "    <int name=\"BOOK_OF_BUSINESS_ID\">2</int>"
                + "    <str name=\"JDK_TIMEZONE\">Europe/London</str>"
                + "    <str name=\"GENRE_ID\">27293</str>"
                + "    <str name=\"GENRE_DESCRIPTION\">Muse Tickets</str>"
                + "    <str name=\"GENRE_PATH\">1/63912/27293/</str>"
                + "    <str name=\"GENRE_ACTIVE\">1</str>"
                + "    <str name=\"VENUE_ID\">213386</str>"
                + "    <str name=\"GEOGRAPHY_ACTIVE\">1</str>"
                + "    <str name=\"VENUE_DESCRIPTION\">SSE Hydro Arena Glasgow</str>"
                + "    <str name=\"GEOGRAPHY_PATH\">6628/10923/10925/213386/</str>"
                + "    <str name=\"STATE\">GLG</str>" + "    <str name=\"COUNTRY\">GB</str>"
                + "    <str name=\"CITY\">Glasgow</str>"
                + "    <date name=\"timestamp\">2016-04-15T11:18:41Z</date>"
                + "    <long name=\"_version_\">1531675013842731008</long></doc>" + "</result>"
                + "</response>";
        XMLResponseParser parser = new XMLResponseParser();
        Reader reader = new StringReader(replyXML);

        SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
        ReflectionTestUtils.setField(accountService, "securityContextUtil", securityContextUtil);
        EventMetaHelper eventHelper = mock(EventMetaHelper.class);
        ReflectionTestUtils.setField(accountService, "eventMetaHelper", eventHelper);

        EventUtil eventUtil = mock(EventUtil.class);
        ReflectionTestUtils.setField(accountService, "eventUtil", eventUtil);

        QueryResponse response = new QueryResponse();
        SolrDocumentList list = new SolrDocumentList();
        response.setResponse(parser.processResponse(reader));

        assertEquals(1, response.getResults().getNumFound());
        assertEquals("29400764", response.getResults().get(0).getFieldValue("SELLER_ID"));

        PaginationInput input = new PaginationInput();
        input.setRows(10);
        input.setStart(0);

        UserContact contact = new UserContact();
        String expected = "test@stubhub.com";
        String expectedFirstName = "testFirstName";
        String expectedLastName = "testLastName";

        contact.setEmail(expected);
        contact.setFirstName(expectedFirstName);
        contact.setLastName(expectedLastName);
        Mockito.when(userContactBiz.getDefaultUserContactByOwernId(anyLong())).thenReturn(contact);
//        Mockito.when(accountBiz.getSales(Mockito.any(SalesSearchCriteria.class)))
//                .thenReturn(response);
        Mockito.when(userDomainHelper.isUserMemberOfGroup(anyString(), anyLong())).thenReturn(false);
        saleSetup(accountService, response);
        SalesResponse salesResponse = accountService.getMySales(serviceContext, securityContext, i18nContext,
                "B5D14E323CD55E9FE04400144F8AE084", input, "junk asc", "EVENT_ID:9391144", "false",
                null, null, null, null);
        assertEquals(1L, salesResponse.getNumFound());
//        assertEquals(expected, salesResponse.getSales().get(0).getBuyerEmail());
//        assertEquals(expectedFirstName, salesResponse.getSales().get(0).getBuyerFirstName());
//        assertEquals(expectedLastName, salesResponse.getSales().get(0).getBuyerLastName());
//        assertEquals(true, salesResponse.getSales().get(0).getExternalTransfer().booleanValue());


    }

//    @Test
//    public void getMySalesLocalDeliveryWithBuyerEmail() throws Exception {
//        String replyXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<response>" + ""
//                + "<lst name=\"responseHeader\">" + "  <int name=\"status\">0</int>"
//                + "  <int name=\"QTime\">1</int>" + "</lst>"
//                + "<result name=\"response\" numFound=\"1\" start=\"0\">" + "  <doc>"
//                + "    <str name=\"INHAND_DATE\">2015-12-28T00:00:00Z</str>"
//                + "    <str name=\"INHAND_IND\">1</str>"
//                + "    <date name=\"LAST_UPDATED_TS\">2016-04-15T11:18:40Z</date>"
//                + "    <long name=\"ORDER_PROC_STATUS_ID\">5000</long>"
//                + "    <str name=\"ORDER_PROC_SORTING_SEQUENCE\">7</str>"
//                + "    <long name=\"ORDER_PROC_SUB_STATUS_CODE\">34</long>"
//                + "    <str name=\"ORDER_PROC_SUB_STATUS_DESC\">Delivered (PDF)</str>"
//                + "    <arr name=\"SEAT_TRAITS\">"
//                + "      <str>13688|You will need to set up a Flash Seats account to access your tickets (instructions will be emailed)|1|Ticket Feature</str><str/>"
//                + "    </arr>" + "    <str name=\"TRANSACTION_DATE\">2016-04-13T09:03:52Z</str>"
//                + "    <str name=\"SHIP_DATE\">2016-04-13T09:03:52Z</str>"
//                + "    <str name=\"EXPECTED_DELIVERY_DATE\">2016-04-13T09:03:47Z</str>"
//                + "    <str name=\"TID\">194438858</str>"
//                + "    <str name=\"SALE_METHOD_ID\">1</str>"
//                + "    <str name=\"SALE_METHOD\">Fixed Price</str>"
//                + "    <str name=\"FULFILLMENT_METHOD_ID\">17</str>"
//                + "    <str name=\"FULFILLMENT_METHOD_DESC\">PDF - PreDelivery</str>"
//                + "    <str name=\"LISTING_SOURCE_ID\">10</str>"
//                + "    <str name=\"DISCOUNT_COST\">0</str>"
//                + "    <str name=\"BUYER_SHIPPING_CONTACT_ID\">247288836</str>"
//                + "    <float name=\"TOTAL_COST\">75.52</float>"
//                + "    <float name=\"SELLER_PAYOUT_AMOUNT\">60.16</float>"
//                + "    <str name=\"SELLER_FEE_VAL\">3.2</str>"
//                + "    <str name=\"SHIPPING_FEE_COST\">0</str>"
//                + "    <str name=\"BUYER_FEE_COST\">9.6</str>"
//                + "    <str name=\"TICKET_COST\">64</str>"
//                + "    <str name=\"EVENT_ID\">9391144</str>"
//                + "    <str name=\"SECTION\">Upper 331</str>"
//                + "    <str name=\"TICKET_ID\">1163381357</str>"
//                + "    <str name=\"QUANTITY\">2</str>" + "    <str name=\"ROW_DESC\">X</str>"
//                + "    <str name=\"SEATS\">226,227</str>"
//                + "    <str name=\"SELLER_ID\">29400764</str>"
//                + "    <str name=\"BUYER_ID\">87052271</str>"
//                + "    <str name=\"TICKET_MEDIUM_ID\">1</str>"
//                + "    <str name=\"DELIVERY_OPTION_ID\">1</str>"
//                + "    <str name=\"DELIVERY_METHOD_ID\">49</str>"
//                + "    <str name=\"CONFIRMED_IND\">1</str>"
//                + "    <str name=\"CONFIRM_OPTION_ID\">2</str>"
//                + "    <str name=\"CURRENCY_CODE\">GBP</str>"
//                + "    <str name=\"VAT_SELL_FEE\">.64</str>"
//                + "    <str name=\"VAT_SELL_PRCNT\">.2</str>"
//                + "    <str name=\"VAT_LOG_FEE\">0</str>"
//                + "    <str name=\"VAT_LOG_PRCNT\">.2</str>"
//                + "    <str name=\"VAT_BUY_FEE\">1.92</str>"
//                + "    <str name=\"VAT_BUY_PRCNT\">.2</str>"
//                + "    <str name=\"SUBBED_IND\">0</str>" + "    <str name=\"CANCELLED\">0</str>"
//                + "    <long name=\"FRAUD_CHECK_STATUS_ID\">600</long>"
//                + "    <str name=\"SELLER_PAYMENT_TYPE_ID\">1</str>"
//                + "    <long name=\"VENUE_CONFIG_SECTIONS_ID\">1664329</long>"
//                + "    <str name=\"SELLER_NOTES\"> </str>"
//                + "    <date name=\"SOLD_DATE\">2016-04-13T09:03:52Z</date>"
//                + "    <int name=\"SOLD_QUANTITY\">2</int>"
//                + "    <float name=\"PRICE_PER_TICKET\">32.0</float>"
//                + "    <float name=\"COST_PER_TICKET\">37.76</float>"
//                + "    <float name=\"STATS_COST_PER_TICKET\">37.76</float>"
//                + "    <date name=\"EVENT_DATE\">2016-04-17T17:00:00Z</date>"
//                + "    <str name=\"EVENT_DATE_LOCAL\">2016-04-17T18:00:00Z</str>"
//                + "    <str name=\"EVENT_DESCRIPTION\">Muse Tickets</str>"
//                + "    <str name=\"EVENT_STATUS_ID\">2</str>"
//                + "    <str name=\"EVENT_STATUS_DESC\"/>"
//                + "    <str name=\"EARLIEST_POSSIBLE_INHAND_DATE\">2015-09-11T09:38:21Z</str>"
//                + "    <str name=\"EVENT_CANCELLED_IND\">0</str>"
//                + "    <str name=\"HIDE_EVENT_DATE\">0</str>"
//                + "    <str name=\"HIDE_EVENT_TIME\">0</str>"
//                + "    <int name=\"BOOK_OF_BUSINESS_ID\">2</int>"
//                + "    <str name=\"JDK_TIMEZONE\">Europe/London</str>"
//                + "    <str name=\"GENRE_ID\">27293</str>"
//                + "    <str name=\"GENRE_DESCRIPTION\">Muse Tickets</str>"
//                + "    <str name=\"GENRE_PATH\">1/63912/27293/</str>"
//                + "    <str name=\"GENRE_ACTIVE\">1</str>"
//                + "    <str name=\"VENUE_ID\">213386</str>"
//                + "    <str name=\"GEOGRAPHY_ACTIVE\">1</str>"
//                + "    <str name=\"VENUE_DESCRIPTION\">SSE Hydro Arena Glasgow</str>"
//                + "    <str name=\"GEOGRAPHY_PATH\">6628/10923/10925/213386/</str>"
//                + "    <str name=\"STATE\">GLG</str>" + "    <str name=\"COUNTRY\">GB</str>"
//                + "    <str name=\"CITY\">Glasgow</str>"
//                + "    <date name=\"timestamp\">2016-04-15T11:18:41Z</date>"
//                + "    <long name=\"_version_\">1531675013842731008</long></doc>" + "</result>"
//                + "</response>";
//        XMLResponseParser parser = new XMLResponseParser();
//        Reader reader = new StringReader(replyXML);
//
//        SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
//        ReflectionTestUtils.setField(accountService, "securityContextUtil", securityContextUtil);
//        EventMetaHelper eventHelper = mock(EventMetaHelper.class);
//        ReflectionTestUtils.setField(accountService, "eventMetaHelper", eventHelper);
//
//        EventUtil eventUtil = mock(EventUtil.class);
//        ReflectionTestUtils.setField(accountService, "eventUtil", eventUtil);
//
//        QueryResponse response = new QueryResponse();
//        SolrDocumentList list = new SolrDocumentList();
//        response.setResponse(parser.processResponse(reader));
//
//        assertEquals(1, response.getResults().getNumFound());
//        assertEquals("29400764", response.getResults().get(0).getFieldValue("SELLER_ID"));
//
//        PaginationInput input = new PaginationInput();
//        input.setRows(10);
//        input.setStart(0);
//
//        UserContact contact = new UserContact();
//        contact.setPhoneCountryCd("1");
//        contact.setPhoneNumber("123");
//        String expected = "test@stubhub.com";
//        String expectedFirstName="testFirstName";
//        String expectedLastName="testLastName";
//
//        contact.setEmail(expected);
//        contact.setFirstName(expectedFirstName);
//        contact.setLastName(expectedLastName);
//        Mockito.when(userContactBiz.getDefaultUserContactByOwernId(anyLong())).thenReturn(contact);
//        Mockito.when(accountBiz.getSales(Mockito.any(SalesSearchCriteria.class)))
//                .thenReturn(response);
//        Mockito.when(userDomainHelper.isUserMemberOfGroup(anyString(), anyLong())).thenReturn(false);
//
//        SalesResponse salesResponse = accountService.getMySales(serviceContext, securityContext, i18nContext,
//                "B5D14E323CD55E9FE04400144F8AE084", input, "junk asc", "EVENT_ID:9391144", "false",
//                null, null, null);
//        assertEquals(1L, salesResponse.getNumFound());
//        assertEquals(expected, salesResponse.getSales().get(0).getBuyerEmail());
//        assertEquals(expectedFirstName, salesResponse.getSales().get(0).getBuyerFirstName());
//        assertEquals(expectedLastName, salesResponse.getSales().get(0).getBuyerLastName());
//        assertEquals(false, salesResponse.getSales().get(0).getExternalTransfer().booleanValue());
//        assertNotNull(salesResponse.getSales().get(0).getBuyerPhoneCallingCode());
//        assertNotNull(salesResponse.getSales().get(0).getBuyerPhoneNumber());
//    }
//
//
//    @Test
//    public void getMySalesFlashSeatWithBuyerEmail() throws Exception {
//        String replyXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<response>" + ""
//                + "<lst name=\"responseHeader\">" + "  <int name=\"status\">0</int>"
//                + "  <int name=\"QTime\">1</int>" + "</lst>"
//                + "<result name=\"response\" numFound=\"1\" start=\"0\">" + "  <doc>"
//                + "    <str name=\"INHAND_DATE\">2015-12-28T00:00:00Z</str>"
//                + "    <str name=\"INHAND_IND\">1</str>"
//                + "    <date name=\"LAST_UPDATED_TS\">2016-04-15T11:18:40Z</date>"
//                + "    <long name=\"ORDER_PROC_STATUS_ID\">5000</long>"
//                + "    <str name=\"ORDER_PROC_SORTING_SEQUENCE\">7</str>"
//                + "    <long name=\"ORDER_PROC_SUB_STATUS_CODE\">34</long>"
//                + "    <str name=\"ORDER_PROC_SUB_STATUS_DESC\">Delivered (PDF)</str>"
//                + "    <str name=\"TRANSACTION_DATE\">2016-04-13T09:03:52Z</str>"
//                + "    <str name=\"SHIP_DATE\">2016-04-13T09:03:52Z</str>"
//                + "    <str name=\"EXPECTED_DELIVERY_DATE\">2016-04-13T09:03:47Z</str>"
//                + "    <str name=\"TID\">194438858</str>"
//                + "    <str name=\"SALE_METHOD_ID\">1</str>"
//                + "    <str name=\"SALE_METHOD\">Fixed Price</str>"
//                + "    <str name=\"LISTING_SOURCE_ID\">10</str>"
//                + "    <str name=\"DISCOUNT_COST\">0</str>"
//                + "    <str name=\"BUYER_SHIPPING_CONTACT_ID\">247288836</str>"
//                + "    <float name=\"TOTAL_COST\">75.52</float>"
//                + "    <float name=\"SELLER_PAYOUT_AMOUNT\">60.16</float>"
//                + "    <str name=\"SELLER_FEE_VAL\">3.2</str>"
//                + "    <str name=\"SHIPPING_FEE_COST\">0</str>"
//                + "    <str name=\"BUYER_FEE_COST\">9.6</str>"
//                + "    <str name=\"TICKET_COST\">64</str>"
//                + "    <str name=\"EVENT_ID\">9391144</str>"
//                + "    <str name=\"SECTION\">Upper 331</str>"
//                + "    <str name=\"TICKET_ID\">1163381357</str>"
//                + "    <str name=\"QUANTITY\">2</str>" + "    <str name=\"ROW_DESC\">X</str>"
//                + "    <str name=\"SEATS\">226,227</str>"
//                + "    <str name=\"SELLER_ID\">29400764</str>"
//                + "    <str name=\"BUYER_ID\">87052271</str>"
//                + "    <str name=\"TICKET_MEDIUM_ID\">2</str>"
//                + "    <str name=\"DELIVERY_OPTION_ID\">10</str>"
//                + "    <str name=\"DELIVERY_METHOD_ID\">44</str>"
//                + "    <str name=\"FULFILLMENT_METHOD_ID\">2</str>"
//                + "    <str name=\"CONFIRMED_IND\">1</str>"
//                + "    <str name=\"CONFIRM_OPTION_ID\">2</str>"
//                + "    <str name=\"CURRENCY_CODE\">GBP</str>"
//                + "    <str name=\"VAT_SELL_FEE\">.64</str>"
//                + "    <str name=\"VAT_SELL_PRCNT\">.2</str>"
//                + "    <str name=\"VAT_LOG_FEE\">0</str>"
//                + "    <str name=\"VAT_LOG_PRCNT\">.2</str>"
//                + "    <str name=\"VAT_BUY_FEE\">1.92</str>"
//                + "    <str name=\"VAT_BUY_PRCNT\">.2</str>"
//                + "    <str name=\"SUBBED_IND\">0</str>" + "    <str name=\"CANCELLED\">0</str>"
//                + "    <long name=\"FRAUD_CHECK_STATUS_ID\">600</long>"
//                + "    <str name=\"SELLER_PAYMENT_TYPE_ID\">1</str>"
//                + "    <long name=\"VENUE_CONFIG_SECTIONS_ID\">1664329</long>"
//                + "    <str name=\"SELLER_NOTES\"> </str>"
//                + "    <date name=\"SOLD_DATE\">2016-04-13T09:03:52Z</date>"
//                + "    <int name=\"SOLD_QUANTITY\">2</int>"
//                + "    <float name=\"PRICE_PER_TICKET\">32.0</float>"
//                + "    <float name=\"COST_PER_TICKET\">37.76</float>"
//                + "    <float name=\"STATS_COST_PER_TICKET\">37.76</float>"
//                + "    <date name=\"EVENT_DATE\">2016-04-17T17:00:00Z</date>"
//                + "    <str name=\"EVENT_DATE_LOCAL\">2016-04-17T18:00:00Z</str>"
//                + "    <str name=\"EVENT_DESCRIPTION\">Muse Tickets</str>"
//                + "    <str name=\"EVENT_STATUS_ID\">2</str>"
//                + "    <str name=\"EVENT_STATUS_DESC\"/>"
//                + "    <str name=\"EARLIEST_POSSIBLE_INHAND_DATE\">2015-09-11T09:38:21Z</str>"
//                + "    <str name=\"EVENT_CANCELLED_IND\">0</str>"
//                + "    <str name=\"HIDE_EVENT_DATE\">0</str>"
//                + "    <str name=\"HIDE_EVENT_TIME\">0</str>"
//                + "    <int name=\"BOOK_OF_BUSINESS_ID\">2</int>"
//                + "    <str name=\"JDK_TIMEZONE\">Europe/London</str>"
//                + "    <str name=\"GENRE_ID\">27293</str>"
//                + "    <str name=\"GENRE_DESCRIPTION\">Muse Tickets</str>"
//                + "    <str name=\"GENRE_PATH\">1/63912/27293/</str>"
//                + "    <str name=\"GENRE_ACTIVE\">1</str>"
//                + "    <str name=\"VENUE_ID\">213386</str>"
//                + "    <str name=\"GEOGRAPHY_ACTIVE\">1</str>"
//                + "    <str name=\"VENUE_DESCRIPTION\">SSE Hydro Arena Glasgow</str>"
//                + "    <str name=\"GEOGRAPHY_PATH\">6628/10923/10925/213386/</str>"
//                + "    <str name=\"STATE\">GLG</str>" + "    <str name=\"COUNTRY\">GB</str>"
//                + "    <str name=\"CITY\">Glasgow</str>"
//                + "    <date name=\"timestamp\">2016-04-15T11:18:41Z</date>"
//                + "    <long name=\"_version_\">1531675013842731008</long></doc>" + "</result>"
//                + "</response>";
//        XMLResponseParser parser = new XMLResponseParser();
//        Reader reader = new StringReader(replyXML);
//
//        SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
//        ReflectionTestUtils.setField(accountService, "securityContextUtil", securityContextUtil);
//        EventMetaHelper eventHelper = mock(EventMetaHelper.class);
//        ReflectionTestUtils.setField(accountService, "eventMetaHelper", eventHelper);
//
//        EventUtil eventUtil = mock(EventUtil.class);
//        ReflectionTestUtils.setField(accountService, "eventUtil", eventUtil);
//        Mockito.when(messageSourceBundle.getWindowMessage(anyLong(), anyLong(), anyString(), any(Locale.class))).thenReturn("Test");
//
//
//        QueryResponse response = new QueryResponse();
//        SolrDocumentList list = new SolrDocumentList();
//        response.setResponse(parser.processResponse(reader));
//
//        assertEquals(1, response.getResults().getNumFound());
//        assertEquals("29400764", response.getResults().get(0).getFieldValue("SELLER_ID"));
//
//        PaginationInput input = new PaginationInput();
//        input.setRows(10);
//        input.setStart(0);
//
//        UserContact contact = new UserContact();
//        String expected = "test@stubhub.com";
//        String expectedFirstName="testFirstName";
//        String expectedLastName="testLastName";
//
//        contact.setEmail(expected);
//        contact.setFirstName(expectedFirstName);
//        contact.setLastName(expectedLastName);
//        Mockito.when(userContactBiz.getDefaultUserContactByOwernId(anyLong())).thenReturn(contact);
//        Mockito.when(accountBiz.getSales(Mockito.any(SalesSearchCriteria.class)))
//                .thenReturn(response);
//        Mockito.when(userDomainHelper.isUserMemberOfGroup(anyString(), anyLong())).thenReturn(false);
//
//        SalesResponse salesResponse = accountService.getMySales(serviceContext, securityContext, i18nContext,
//                "B5D14E323CD55E9FE04400144F8AE084", input, "junk asc", "EVENT_ID:9391144", "false",
//                null, null, null);
//        assertEquals(1L, salesResponse.getNumFound());
//        assertEquals(expected, salesResponse.getSales().get(0).getBuyerEmail());
//        assertEquals(expectedFirstName, salesResponse.getSales().get(0).getBuyerFirstName());
//        assertEquals(expectedLastName, salesResponse.getSales().get(0).getBuyerLastName());
//        assertEquals(true, salesResponse.getSales().get(0).getExternalTransfer().booleanValue());
//
//    }
//
//

    @Test
    public void isPrimaryGroup_case() throws Exception {
        Mockito.when(userDomainHelper.isUserMemberOfGroup("T",
                AccountServiceImpl.PRIMARY_SELLER_GROUP_ID)).thenReturn(true);
        Mockito.when(userDomainHelper.isUserMemberOfGroup("F",
                AccountServiceImpl.PRIMARY_SELLER_GROUP_ID)).thenReturn(false);
        Mockito.when(userDomainHelper.isUserMemberOfGroup("E",
                AccountServiceImpl.PRIMARY_SELLER_GROUP_ID))
                .thenThrow(new Exception("test exception"));
        assertTrue(
                (Boolean) ReflectionTestUtils.invokeMethod(accountService, "isPrimarySeller", "T"));
        assertFalse(
                (Boolean) ReflectionTestUtils.invokeMethod(accountService, "isPrimarySeller", "F"));
        assertFalse(
                (Boolean) ReflectionTestUtils.invokeMethod(accountService, "isPrimarySeller", "E"));
    }
//
//    @Test(enabled = false)
//    public void getMySales_filetrs_real_solr() throws Exception {
//        AccountServiceImpl listingResource = new AccountServiceImpl();
//        AccountBiz accountBiz = new AccountBizImpl();
//        SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
//        setBeanProperty(listingResource, "accountBiz", accountBiz);
//        setBeanProperty(listingResource, "securityContextUtil", securityContextUtil);
//        ReflectionTestUtils.setField(listingResource, "messageSource", messageSource);
//        EventMetaHelper eventHelper = Mockito.mock(EventMetaHelper.class);
//        ReflectionTestUtils.setField(listingResource, "eventMetaHelper", eventHelper);
//        UserDomainHelper userDomainHelper = Mockito.mock(UserDomainHelper.class);
//        ReflectionTestUtils.setField(listingResource, "userDomainHelper", userDomainHelper);
//        PaginationInput input = new PaginationInput();
//        input.setRows(10);
//        input.setStart(0);
//
//        Assert.assertEquals(SalesFilterType.Q, SalesFilterType.fromString(null));
//        MasterStubHubProperties.setLoaders(Arrays.<IConfigLoader>asList(new IConfigLoader() {
//
//            @Override
//            public Map<String, String> load() throws Exception {
//                Map<String, String> result = new HashMap<String, String>();
//                result.put("pro.mci.order.solr.url", "http://localhost:8080/solr/orders");
//                return result;
//            }
//
//        }));
//        Mockito.when(securityContext.getUserId()).thenReturn("29400764");
//        Mockito.when(securityContext.getUserName()).thenReturn("29400764");
//        MasterStubHubProperties.load();
//        SalesSolrUtil salesSolrUtil = new SalesSolrUtil();
//        setBeanProperty(accountBiz, "salesSolrUtil", salesSolrUtil);
//        SalesResponse salesResponse = listingResource.getMySales(serviceContext, securityContext, i18nContext,
//                "B5D14E323CD55E9FE04400144F8AE084", input, "junk asc", "EVENT_ID:9391144", "false",
//                null, null, null);
//        // TODO
//        // assertNotNull(salesResponse);
//        assertEquals(1L, salesResponse.getNumFound());
//    }

    private SolrDocument mockSolrDocument(long id, String status, long eventId, String ticketMedium,
                                          String fulfillmentMethod, String isSrubbingEnabled) {

        SolrDocument doc = new SolrDocument();
        float price = 100f;
        doc.setField("TICKET_ID", id + "");
        doc.setField("GENRE_ID", id + "");
        doc.setField("TICKET_SYSTEM_STATUS", status);
        doc.setField("EXTERNAL_LISTING_ID", "10000");
        doc.setField("SECTION", "Lower");
        doc.setField("ROW_DESC", "1,2");
        doc.setField("SEATS", "1,2,3,4");
        doc.setField("QUANTITY_REMAIN", 2);
        doc.setField("QUANTITY", 2);
        doc.setField("CURRENCY_CODE", "USD");
        doc.setField("FACE_VALUE", 100.00f);
        doc.setField("TICKET_PRICE", 120.00f);
        doc.setField("SELLER_PAYOUT_AMT_PER_TICKET", 1250.f);
        doc.setField("DELIVERY_OPTION_ID", "1");

        doc.setField("TICKET_MEDIUM", ticketMedium);
        doc.setField("LMS_APPROVAL_STATUS_ID", "1");
//        doc.setField("EXPECTED_INHAND_DATE", new Date());
        doc.setField("SPLIT", "2");
//        doc.setField("SALE_END_DATE", new Date());
        List<String> traits = new ArrayList<String>();
        traits.add(
                "959,966,102,203,601,101|Actual 4th row,50 yd line,Parking Pass,Alcohol-free seating,Student Ticket,Aisle|3,3,1,2,2,1|Seller Comments,Seller Comments,Ticket Feature,Listing Disclosure,Listing Disclosure,Ticket Feature");
        doc.setField("TICKET_TRAIT_INFO", traits);
        doc.setField("SELLER_CC_ID", 12345L);
        doc.setField("SALE_METHOD_ID", "1");
        doc.setField("SELLER_PURCHASE_PRICE", 120.00f);
        doc.setField("SPLIT_OPTION", "NONE");
        doc.setField("COMMENTS", "NONE");
        doc.setField("CURR_PRICE", price + "");
        doc.setField("SELLER_CONTACT_ID", 3456L);
        doc.setField("EVENT_DESCRIPTION", "U2");
        doc.setField("EVENT_ID", eventId + "");
        doc.setField("EVENT_ACTIVE", "1");
//        doc.setField("EVENT_DATE_LOCAL", "2014-04-03T12:00:00Z");
        doc.setField("VENUE_DESCRIPTION", "O2 Arena");
        doc.setField("VENUE_CONFIG_SECTIONS_ID", 12345);
        doc.setField("LMS_APPROVAL_STATUS_ID", "0");
//        doc.setField("EVENT_DATE", new Date());
        List<String> fulfillMethod = new ArrayList<String>();
        fulfillMethod.add(fulfillmentMethod);
        doc.setField("FULFILLMENT_METHOD_ID", fulfillMethod);
        doc.setField("SELLER_PAYMENT_TYPE_ID", "1");
        doc.setField("IS_SCRUBBING_ENABLED", Integer.parseInt(isSrubbingEnabled));
        return doc;
    }

    private SolrDocument mockSolrDocumentWithDeliveriesflag(long id, String status,
                                                            long eventId, float price, String deliveryOptionId, String fulfillmentMethod,
                                                            String ticketMedium) {
        SolrDocument doc = new SolrDocument();
        doc.addField("DELIVERIES_FLAG", 1L);
        doc.setField("ID", "1");
        doc.setField("SELLER_ID", "1");
        doc.setField("TICKET_ID", id + "");
        doc.setField("TID", id + "");
        doc.setField("TICKET_STATUS", status);
        doc.setField("EXTERNAL_LISTING_ID", "10000");
        doc.setField("SECTION", "Lower");
        doc.setField("ROW_DESC", "1,2");
        doc.setField("SEATS", "1,2,3");

        doc.setField("QUANTITY", "2");
        doc.setField("SOLD_QUANTITY", 2);
        doc.setField("CURRENCY_CODE", "USD");
        doc.setField("DELIVERY_OPTION_ID", deliveryOptionId);
        doc.setField("TICKET_MEDIUM_ID", ticketMedium);
        if (!isFirstTime) {
            doc.setField("LMS_APPROVAL_STATUS_ID", "0");
            isFirstTime = true;
        } else {
            doc.setField("LMS_APPROVAL_STATUS_ID", "2");
        }
        doc.setField("SELLER_PAYMENT_TYPE_ID", "1");

        List<String> traits = new ArrayList<String>();
        traits.add(
                "959,966,102,203,601,101|Actual 4th row,50 yd line,Parking Pass,Alcohol-free seating,Student Ticket,Aisle|3,3,1,2,2,1|Seller Comments,Seller Comments,Ticket Feature,Listing Disclosure,Listing Disclosure,Ticket Feature");
        doc.setField("SEAT_TRAITS", traits);
        doc.setField("EVENT_DESCRIPTION", "U2");
        doc.setField("EVENT_ID", eventId + "");
        doc.setField("VENUE_DESCRIPTION", "O2 Arena");
        doc.setField("VENUE_CONFIG_SECTIONS_ID", 271756L);
        doc.setField("FULFILLMENT_METHOD_ID", fulfillmentMethod);
        doc.setField("ORDER_PROC_STATUS_ID", 4000L);
        doc.setField("ORDER_PROC_SUB_STATUS_CODE", 50L);
        doc.setField("SALE_CATEGORY", "3");
        doc.setField("SELLER_NOTES", "TEST");
        doc.setField("CITY", "San Francisco");
        doc.setField("COUNTRY", "United States");

        doc.setField("DELIVERY_METHOD_ID", "1");
        doc.setField("SUBBED_TID", id + "");

        return doc;


    }

    private SolrDocument mockOrderSolrDocument(long id, String status, long eventId, float price,
                                               String deliveryOptionId, String fulfillmentMethod, String ticketMedium) {
        SolrDocument doc = new SolrDocument();
        doc.setField("ID", "1");
        doc.setField("SELLER_ID", "1");
        doc.setField("TICKET_ID", id + "");
        doc.setField("TID", id + "");
        doc.setField("TICKET_STATUS", status);
        doc.setField("EXTERNAL_LISTING_ID", "10000");
        doc.setField("SECTION", "Lower");
        doc.setField("ROW_DESC", "1,2");
        doc.setField("SEATS", "1,2,3");

        doc.setField("QUANTITY", "2");
        doc.setField("SOLD_QUANTITY", 2);
        doc.setField("CURRENCY_CODE", "USD");
//        doc.setField("VAT_SELL_FEE", price + "");
//        doc.setField("SELLER_FEE_VAL", price + "");
//        doc.setField("TICKET_COST", price + "");
//        doc.setField("TOTAL_COST", price + "");
//        doc.setField("CURR_PRICE", price + "");
//        doc.setField("PRICE_PER_TICKET", price);
//        doc.setField("STATS_COST_PER_TICKET", price);
        doc.setField("DELIVERY_OPTION_ID", deliveryOptionId);
        doc.setField("TICKET_MEDIUM_ID", ticketMedium);
        if (!isFirstTime) {
            doc.setField("LMS_APPROVAL_STATUS_ID", "0");
            isFirstTime = true;
        } else {
            doc.setField("LMS_APPROVAL_STATUS_ID", "2");
        }
//        doc.setField("SELLER_PAYOUT_AMOUNT", 80.00f);
//        doc.setField("EXPECTED_INHAND_DATE", new Date());
//        doc.setField("INHAND_DATE", "2012-11-03T12:00:00Z");
//        doc.setField("TRANSACTION_DATE", "2012-09-03T12:00:00Z");

//        doc.setField("SOLD_DATE", new Date());

//        doc.setField("SALE_END_DATE", "2013-11-03T12:00:00Z");
//        doc.setField("EVENT_DATE", "Wed Dec 31 12:00:00 PST 2014");
        doc.setField("SELLER_PAYMENT_TYPE_ID", "1");

        List<String> traits = new ArrayList<String>();
        traits.add(
                "959,966,102,203,601,101|Actual 4th row,50 yd line,Parking Pass,Alcohol-free seating,Student Ticket,Aisle|3,3,1,2,2,1|Seller Comments,Seller Comments,Ticket Feature,Listing Disclosure,Listing Disclosure,Ticket Feature");
        doc.setField("SEAT_TRAITS", traits);
        doc.setField("EVENT_DESCRIPTION", "U2");
        doc.setField("EVENT_ID", eventId + "");
//        doc.setField("EVENT_DATE_LOCAL", "2014-04-03T12:00:00Z");
        doc.setField("VENUE_DESCRIPTION", "O2 Arena");
        doc.setField("VENUE_CONFIG_SECTIONS_ID", 271756L);
        doc.setField("FULFILLMENT_METHOD_ID", fulfillmentMethod);
        doc.setField("ORDER_PROC_STATUS_ID", 4000L);
        doc.setField("ORDER_PROC_SUB_STATUS_CODE", 50L);
        doc.setField("SALE_CATEGORY", "3");
        doc.setField("SELLER_NOTES", "TEST");
        doc.setField("CITY", "San Francisco");
        doc.setField("COUNTRY", "United States");

        doc.setField("DELIVERY_METHOD_ID", "1");
        doc.setField("SUBBED_TID", id + "");
        doc.setField("GA_IND", "[0]");
        return doc;
    }

    private SolrDocument mockOrderSolrDocumentWithoutInhandDate(long id, String status,
                                                                long eventId, float price, String deliveryOptionId, String fulfillmentMethod,
                                                                String ticketMedium) {
        SolrDocument doc = new SolrDocument();
        doc.setField("SELLER_ID", "1");
        doc.setField("ID", "1");
        doc.setField("TICKET_ID", id + "");
        doc.setField("TID", id + "");
        doc.setField("TICKET_STATUS", status);
        doc.setField("EXTERNAL_LISTING_ID", "10000");
        doc.setField("SECTION", "Lower");
        doc.setField("ROW_DESC", "1,2");
        doc.setField("SEATS", "1,2,3");

        doc.setField("QUANTITY", "2");
        doc.setField("SOLD_QUANTITY", 2);
        doc.setField("CURRENCY_CODE", "USD");
        doc.setField("VAT_SELL_FEE", price + "");
        doc.setField("SELLER_FEE_VAL", price + "");
        doc.setField("TICKET_COST", price + "");
        doc.setField("TOTAL_COST", price + "");
        doc.setField("CURR_PRICE", price + "");
        doc.setField("PRICE_PER_TICKET", price);
        doc.setField("STATS_COST_PER_TICKET", price);
        doc.setField("DELIVERY_OPTION_ID", deliveryOptionId);
        doc.setField("TICKET_MEDIUM_ID", ticketMedium);
        if (!isFirstTime) {
            doc.setField("LMS_APPROVAL_STATUS_ID", "0");
            isFirstTime = true;
        } else {
            doc.setField("LMS_APPROVAL_STATUS_ID", "2");
        }
//        doc.setField("SELLER_PAYOUT_AMOUNT", 80.00f);
        doc.setField("EXPECTED_INHAND_DATE", new Date());
//        doc.setField("TRANSACTION_DATE", "2012-09-03T12:00:00Z");

        doc.setField("SOLD_DATE", new Date());

//        doc.setField("SALE_END_DATE", "2013-11-03T12:00:00Z");
//        doc.setField("EVENT_DATE", "Wed Dec 31 12:00:00 PST 2014");
        doc.setField("SELLER_PAYMENT_TYPE_ID", "1");

        List<String> traits = new ArrayList<String>();
        traits.add(
                "959,966,102,203,601,101|Actual 4th row,50 yd line,Parking Pass,Alcohol-free seating,Student Ticket,Aisle|3,3,1,2,2,1|Seller Comments,Seller Comments,Ticket Feature,Listing Disclosure,Listing Disclosure,Ticket Feature");
        doc.setField("SEAT_TRAITS", traits);
        doc.setField("EVENT_DESCRIPTION", "U2");
        doc.setField("EVENT_ID", eventId + "");
//        doc.setField("EVENT_DATE_LOCAL", "2014-04-03T12:00:00Z");
        doc.setField("VENUE_DESCRIPTION", "O2 Arena");
        doc.setField("VENUE_CONFIG_SECTIONS_ID", 271756L);
        doc.setField("FULFILLMENT_METHOD_ID", fulfillmentMethod);
        doc.setField("ORDER_PROC_STATUS_ID", 4000L);
        doc.setField("ORDER_PROC_SUB_STATUS_CODE", 50L);
        doc.setField("SALE_CATEGORY", "3");
        doc.setField("SELLER_NOTES", "TEST");
        doc.setField("CITY", "San Francisco");
        doc.setField("COUNTRY", "United States");

        doc.setField("DELIVERY_METHOD_ID", "1");
        doc.setField("SUBBED_TID", id + "");
        doc.setField("GA_IND", "[0]");

        return doc;
    }

    private BaseMatcher getMatcher() {
        BaseMatcher matcher = new BaseMatcher() {
            @Override
            public boolean matches(Object item) {
                return true;
            }

            @Override
            public void describeTo(Description description) {
            }
        };
        return matcher;
    }

    /**
     * TODO - move to a test utility class
     *
     * @param objInstance
     * @param propertyName
     * @param newVal
     * @throws Exception
     */
    public void setBeanProperty(Object objInstance, String propertyName, Object newVal)
            throws Exception {
        Field[] fields = objInstance.getClass().getDeclaredFields();
        objInstance.getClass().getDeclaredMethods();

        if (fields != null) {
            for (Field field : fields) {
                if (field.getName().equalsIgnoreCase(propertyName)) {
                    field.setAccessible(true);
                    field.set(objInstance, newVal);
                }
            }
        }
    }

    @Test
    public void testGetCSSalesForSolrCloud() throws Exception {
        AccountServiceImpl listingResource = new AccountServiceImpl();
        AccountSolrCloudBiz accountSolrCloudBiz = mock(AccountSolrCloudBiz.class);
        SellerPaymentSolrCloudBO sellerPaymentSolrCloudBO = mock(SellerPaymentSolrCloudBO.class);
        SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
        setBeanProperty(listingResource, "accountSolrCloudBiz", accountSolrCloudBiz);
        setBeanProperty(listingResource, "securityContextUtil", securityContextUtil);
        setBeanProperty(listingResource, "sellerPaymentSolrCloudBO", sellerPaymentSolrCloudBO);
        ReflectionTestUtils.setField(listingResource, "messageSource", messageSource);
        RequestValidator requestValidator = mock(RequestValidator.class);
        setBeanProperty(listingResource, "requestValidator", requestValidator);
        Mockito.when(requestValidator
                .validateCSOrderDetailsRequestFields((CSOrderDetailsRequest) argThat(getMatcher())))
                .thenReturn(null);
        OrderSolrBO orderSolrBO = mock(OrderSolrBO.class);
        setBeanProperty(listingResource, "orderSolrBO", orderSolrBO);
        CSStubTransFlagBO csStubTransFlagBO = mock(CSStubTransFlagBO.class);
        setBeanProperty(listingResource, "csStubTransFlagBO", csStubTransFlagBO);
        MasterStubHubProperties.setProperty("account.v1.cssales.useSolrCloud", "true");
        try {
            JsonNode response = null;
            when(accountSolrCloudBiz.getCSOrderDetails(any(SalesSearchCriteria.class))).thenReturn(response);
            CSOrderDetailsRequest req = new CSOrderDetailsRequest();
            req.setOrderId("12345");

            Response salesResponse = listingResource.getCSSales(req, securityContext);
            Assert.assertNotNull(salesResponse);
            Assert.assertEquals(salesResponse.getStatus(), 404);

            InputStream in = AccountResponseJsonAdapterTest.class.getClassLoader()
                    .getResourceAsStream("saleResponseWithoutSummary.json");
            response = om.readTree(in);
            when(accountSolrCloudBiz.getCSOrderDetails(any(SalesSearchCriteria.class))).thenReturn(response);
            InputStream paymentsIn = AccountResponseJsonAdapterTest.class.getClassLoader()
                    .getResourceAsStream("paymentResponseWithoutSummary.json");
            JsonNode paymentsResponse = om.readTree(paymentsIn);
            when(sellerPaymentSolrCloudBO.getCSSellerPayments(any(PaymentsSearchCriteria.class))).thenReturn(paymentsResponse);

            salesResponse = listingResource.getCSSales(req, securityContext);
            Assert.assertNotNull(salesResponse);
            Assert.assertEquals(salesResponse.getStatus(), 200);
        } finally {
            MasterStubHubProperties.setProperty("account.v1.cssales.useSolrCloud", "false");
        }
    }

    @Test
    public void testGetCSOrderDetailsForSolrCloud() throws Exception {
        AccountServiceImpl listingResource = new AccountServiceImpl();
        AccountSolrCloudBiz accountSolrCloudBiz = mock(AccountSolrCloudBiz.class);
        SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
        setBeanProperty(listingResource, "accountSolrCloudBiz", accountSolrCloudBiz);
        setBeanProperty(listingResource, "securityContextUtil", securityContextUtil);
        ReflectionTestUtils.setField(listingResource, "messageSource", messageSource);
        RequestValidator requestValidator = mock(RequestValidator.class);
        setBeanProperty(listingResource, "requestValidator", requestValidator);
        Mockito.when(requestValidator
                .validateCSOrderDetailsRequestFields((CSOrderDetailsRequest) argThat(getMatcher())))
                .thenReturn(null);
        OrderSolrBO orderSolrBO = mock(OrderSolrBO.class);
        setBeanProperty(listingResource, "orderSolrBO", orderSolrBO);
        CSStubTransFlagBO csStubTransFlagBO = mock(CSStubTransFlagBO.class);
        setBeanProperty(listingResource, "csStubTransFlagBO", csStubTransFlagBO);
        MasterStubHubProperties.setProperty("account.v1.csorders.useSolrCloud", "true");
        try {
            JsonNode response = null;
            when(accountSolrCloudBiz.getCSOrderDetails(any(SalesSearchCriteria.class))).thenReturn(response);
            CSOrderDetailsRequest req = new CSOrderDetailsRequest();
            req.setOrderId("12345");

            Response salesResponse = listingResource.getCSOrderDetails(req, securityContext);
            Assert.assertNotNull(salesResponse);
            Assert.assertEquals(salesResponse.getStatus(), 404);

            InputStream in = AccountResponseJsonAdapterTest.class.getClassLoader()
                    .getResourceAsStream("saleResponseWithoutSummary.json");
            response = om.readTree(in);
            when(accountSolrCloudBiz.getCSOrderDetails(any(SalesSearchCriteria.class))).thenReturn(response);
            salesResponse = listingResource.getCSOrderDetails(req, securityContext);
            Assert.assertNotNull(salesResponse);
            Assert.assertEquals(salesResponse.getStatus(), 200);
        } finally {
            MasterStubHubProperties.setProperty("account.v1.csorders.useSolrCloud", "false");
        }
    }

    @Test
    public void testGetCSOrderDetails() throws Exception {
        AccountServiceImpl accountService = new AccountServiceImpl();
        SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
        setBeanProperty(accountService, "securityContextUtil", securityContextUtil);
        RequestValidator requestValidator = mock(RequestValidator.class);
        setBeanProperty(accountService, "requestValidator", requestValidator);
        OrderSolrBO orderSolrBO = mock(OrderSolrBO.class);
        setBeanProperty(accountService, "orderSolrBO", orderSolrBO);
        CSStubTransFlagBO csStubTransFlagBO = mock(CSStubTransFlagBO.class);
        setBeanProperty(accountService, "csStubTransFlagBO", csStubTransFlagBO);
        AccountResponseAdapter accountResponseAdapter = mock(AccountResponseAdapter.class);

        CSOrderDetailsRequest csOrderDetailsRequest = new CSOrderDetailsRequest();
        csOrderDetailsRequest.setProxiedId("1");
        csOrderDetailsRequest.setRow("1");
        csOrderDetailsRequest.setStart("0");

        QueryResponse queryResponse = new QueryResponse();
        NamedList responseList = new NamedList();
        SolrDocumentList list = new SolrDocumentList();
        list.setNumFound(2);
        list.setStart(0);
        list.add(mockOrderSolrDocument(100, "ACTIVE", 10001, 100f, "1", "1", "3"));
        list.add(mockOrderSolrDocument(200, "ACTIVE", 10002, 200f, "0", "4", "2"));
        responseList.add("response", list);
        queryResponse.setResponse(responseList);

        Map<String, Boolean> csFlag = new HashMap<String, Boolean>();
        csFlag.put("100", true);
        csFlag.put("200", true);

        OrdersResponse ordersResponse = new OrdersResponse();
        List<CSOrderDetailsResponse> listCSOrderDetailsResponse = new ArrayList<CSOrderDetailsResponse>();
        CSOrderDetailsResponse csOrderDetailsResponse = new CSOrderDetailsResponse();
        TransactionResponse transactionResponse = new TransactionResponse();
        transactionResponse.setOrderId("2");
        csOrderDetailsResponse.setTransaction(transactionResponse);
        listCSOrderDetailsResponse.add(csOrderDetailsResponse);
        ordersResponse.setOrder(listCSOrderDetailsResponse);
        ordersResponse.setOrdersFound(1);

        SalesSearchCriteria ssc = new SalesSearchCriteria();
        ssc.setSellerId(1L);
        ssc.setSubbedId("123");

        List<Error> errorList = new LinkedList<Error>();
        errorList.add(new Error(ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT, "No input provided",
                null));
        Mockito.when(requestValidator.validateCSOrderDetailsRequestFields(csOrderDetailsRequest))
                .thenReturn(errorList);
        Response response = accountService.getCSOrderDetails(csOrderDetailsRequest,
                securityContext);

        Mockito.when(requestValidator
                .validateCSOrderDetailsRequestFields((CSOrderDetailsRequest) argThat(getMatcher())))
                .thenReturn(null);
//        Mockito.when(orderSolrBO.getCSOrderDetails((SalesSearchCriteria) argThat(getMatcher())))
//                .thenReturn(queryResponse);
        Mockito.when(csStubTransFlagBO.getCSStubTransFlag((List<String>) argThat(getMatcher())))
                .thenReturn(csFlag);
        Mockito.when(
                accountResponseAdapter.addCSStubTransFlag((OrdersResponse) argThat(getMatcher()),
                        (Map<String, Boolean>) argThat(getMatcher())))
                .thenReturn(ordersResponse);
//        Mockito.when(accountResponseAdapter.addSubbedOrderInfo(
//                (OrdersResponse) argThat(getMatcher()), (QueryResponse) argThat(getMatcher())))
//                .thenReturn(ordersResponse);
        cssaleSetup(accountService, queryResponse);
        response = accountService.getCSOrderDetails(csOrderDetailsRequest, securityContext);
        assertNotNull(response);
    }

    @Test
    public void testGetCSOrderDetails_Exceptions() throws Exception {
        AccountServiceImpl accountService = new AccountServiceImpl();
        SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
        setBeanProperty(accountService, "securityContextUtil", securityContextUtil);
        CSOrderDetailsRequest csOrderDetailsRequest = new CSOrderDetailsRequest();
        when(accountService.getCSOrderDetails(csOrderDetailsRequest, securityContext))
                .thenThrow(new UserNotAuthorizedException());
        Response ordersResponse = accountService.getCSOrderDetails(csOrderDetailsRequest,
                securityContext);
        assertNotNull(ordersResponse);
        when(accountService.getCSOrderDetails(csOrderDetailsRequest, securityContext))
                .thenThrow(new AccountException());
        ordersResponse = accountService.getCSOrderDetails(csOrderDetailsRequest, securityContext);
        assertNotNull(ordersResponse);
        when(accountService.getCSOrderDetails(csOrderDetailsRequest, securityContext))
                .thenThrow(new NullPointerException());
        ordersResponse = accountService.getCSOrderDetails(csOrderDetailsRequest, securityContext);
        assertNotNull(ordersResponse);
    }

//    @Test
//    public void testGetSalesDetails() throws Exception {
//        AccountServiceImpl accountService = new AccountServiceImpl();
//        SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
//        setBeanProperty(accountService, "securityContextUtil", securityContextUtil);
//        RequestValidator requestValidator = mock(RequestValidator.class);
//        setBeanProperty(accountService, "requestValidator", requestValidator);
//        OrderSolrBO orderSolrBO = mock(OrderSolrBO.class);
//        setBeanProperty(accountService, "orderSolrBO", orderSolrBO);
//        CSStubTransFlagBO csStubTransFlagBO = mock(CSStubTransFlagBO.class);
//        setBeanProperty(accountService, "csStubTransFlagBO", csStubTransFlagBO);
//        AccountResponseAdapter accountResponseAdapter = mock(AccountResponseAdapter.class);
//        PaymentsSolrBO paymentsSolrBO = mock(PaymentsSolrBO.class);
//        setBeanProperty(accountService, "paymentsSolrBO", paymentsSolrBO);
//
//        CSOrderDetailsRequest csOrderDetailsRequest = new CSOrderDetailsRequest();
//        csOrderDetailsRequest.setProxiedId("1131159");
//        csOrderDetailsRequest.setRow("4");
//        csOrderDetailsRequest.setStart("0");
//
//        QueryResponse queryResponse = new QueryResponse();
//        NamedList responseList = new NamedList();
//        SolrDocumentList list = new SolrDocumentList();
//        list.setNumFound(4);
//        list.setStart(0);
//        list.add(mockOrderSolrDocument(100, "ACTIVE", 10001, 100f, "1", "1", "3"));
//        list.add(mockOrderSolrDocument(200, "ACTIVE", 10002, 200f, "0", "4", "2"));
//        responseList.add("response", list);
//        queryResponse.setResponse(responseList);
//
//        Map<String, Boolean> csFlag = new HashMap<String, Boolean>();
//        csFlag.put("100", true);
//        csFlag.put("200", true);
//
//        OrdersResponse ordersResponse = new OrdersResponse();
//        List<CSOrderDetailsResponse> listCSOrderDetailsResponse = new ArrayList<CSOrderDetailsResponse>();
//        CSOrderDetailsResponse csOrderDetailsResponse = new CSOrderDetailsResponse();
//        TransactionResponse transactionResponse = new TransactionResponse();
//        transactionResponse.setOrderId("2");
//        csOrderDetailsResponse.setTransaction(transactionResponse);
//        listCSOrderDetailsResponse.add(csOrderDetailsResponse);
//        ordersResponse.setOrder(listCSOrderDetailsResponse);
//        ordersResponse.setOrdersFound(1);
//
//        List<Error> errorList = new LinkedList<Error>();
//        errorList.add(new Error(ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT, "No input provided",
//                null));
//        Mockito.when(requestValidator.validateCSOrderDetailsRequestFields(csOrderDetailsRequest))
//                .thenReturn(errorList);
//        Response response = accountService.getCSSales(csOrderDetailsRequest, securityContext);
//
//        Mockito.when(requestValidator
//                .validateCSOrderDetailsRequestFields((CSOrderDetailsRequest) argThat(getMatcher())))
//                .thenReturn(null);
//        Mockito.when(orderSolrBO.getCSOrderDetails((SalesSearchCriteria) argThat(getMatcher())))
//                .thenReturn(queryResponse);
//        Mockito.when(csStubTransFlagBO.getCSStubTransFlag((List<String>) argThat(getMatcher())))
//                .thenReturn(csFlag);
//        Mockito.when(
//                accountResponseAdapter.addCSStubTransFlag((OrdersResponse) argThat(getMatcher()),
//                        (Map<String, Boolean>) argThat(getMatcher())))
//                .thenReturn(ordersResponse);
//        Mockito.when(paymentsSolrBO.getCSSales((String) argThat(getMatcher())))
//                .thenReturn(queryResponse);
//
//        response = accountService.getCSSales(csOrderDetailsRequest, securityContext);
//        assertNotNull(response);
//    }

    @Test
    public void testGetSalesDetails_Exceptions() throws Exception {
        AccountServiceImpl accountService = new AccountServiceImpl();
        SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
        setBeanProperty(accountService, "securityContextUtil", securityContextUtil);
        CSOrderDetailsRequest csOrderDetailsRequest = new CSOrderDetailsRequest();
        when(accountService.getCSOrderDetails(csOrderDetailsRequest, securityContext))
                .thenThrow(new UserNotAuthorizedException());
        Response ordersResponse = accountService.getCSSales(csOrderDetailsRequest, securityContext);
        assertNotNull(ordersResponse);
        when(accountService.getCSOrderDetails(csOrderDetailsRequest, securityContext))
                .thenThrow(new AccountException());
        ordersResponse = accountService.getCSSales(csOrderDetailsRequest, securityContext);
        assertNotNull(ordersResponse);
        when(accountService.getCSOrderDetails(csOrderDetailsRequest, securityContext))
                .thenThrow(new NullPointerException());
        ordersResponse = accountService.getCSSales(csOrderDetailsRequest, securityContext);
        assertNotNull(ordersResponse);
    }

    @Test
    public void getMyEventSales() throws Exception {
        AccountServiceImpl listingResource = new AccountServiceImpl();
//        AccountBiz accountBiz = mock(AccountBiz.class);
        SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
//        setBeanProperty(listingResource, "accountBiz", accountBiz);
        setBeanProperty(listingResource, "securityContextUtil", securityContextUtil);
        setBeanProperty(listingResource, "sellerEligibilityHelper", sellerEligibilityHelper);
        QueryResponse response = new QueryResponse();
        SolrDocumentList list = new SolrDocumentList();
        list.setNumFound(5);
        list.setStart(0);
        list.add(mockOrderSolrDocument(100, "ACTIVE", 10001, 100f, "1", "1", "3"));
        list.add(mockOrderSolrDocument(200, "ACTIVE", 10002, 200f, "0", "4", "2"));
        list.add(mockOrderSolrDocument(300, "ACTIVE", 10003, 300f, "0", "6", "1"));
        list.add(mockOrderSolrDocument(400, "ACTIVE", 10004, 400f, "0", "10", "1"));
        list.add(mockOrderSolrDocument(500, "INACTIVE", 10005, 500f, "0", "7", "1"));
        NamedList responseList = new NamedList();
        responseList.add("response", list);
        response.setResponse(responseList);
        when(sellerEligibilityHelper.checkSellerEligibility()).thenReturn(true);

//        when(accountBiz.getEventSales((SalesSearchCriteria) argThat(getMatcher())))
//                .thenReturn(response);
        setBeanProperty(listingResource, "accountSolrCloudBiz", accountSolrCloudBiz);
        Mockito.when(accountSolrCloudBiz.getSellerEventSales(Mockito.any(SalesSearchCriteria.class)))
                .thenReturn(null != response ? covertQueryResponse(response) : null);
        SalesResponse salesResponse = listingResource.getEventSalesData(securityContext, "",
                "434234", null, null, null, null, null, null);
        assertNotNull(salesResponse);
    }

    @Test
    public void getMyEventSalesData_User_NotAuthenticated() throws Exception {
        AccountServiceImpl listingResource = new AccountServiceImpl();
//        AccountBiz accountBiz = mock(AccountBiz.class);
        SecurityContextUtil securityContextUtil = new SecurityContextUtil();
//        setBeanProperty(listingResource, "accountBiz", accountBiz);
        setBeanProperty(listingResource, "securityContextUtil", securityContextUtil);
        setBeanProperty(listingResource, "sellerEligibilityHelper", sellerEligibilityHelper);
        QueryResponse response = new QueryResponse();
        SolrDocumentList list = new SolrDocumentList();
        list.setNumFound(5);
        list.setStart(0);
        list.add(mockOrderSolrDocument(100, "ACTIVE", 10001, 100f, "1", "1", "3"));
        list.add(mockOrderSolrDocument(200, "ACTIVE", 10002, 200f, "0", "4", "2"));
        list.add(mockOrderSolrDocument(300, "ACTIVE", 10003, 300f, "0", "6", "1"));
        list.add(mockOrderSolrDocument(400, "ACTIVE", 10004, 400f, "0", "10", "1"));
        list.add(mockOrderSolrDocument(500, "INACTIVE", 10005, 500f, "0", "7", "1"));
        NamedList responseList = new NamedList();
        responseList.add("response", list);
        response.setResponse(responseList);
        when(securityContext.getUserId()).thenReturn("8762167213");
        when(sellerEligibilityHelper.checkSellerEligibility()).thenReturn(false);
//        when(accountBiz.getEventSales((SalesSearchCriteria) argThat(getMatcher())))
//                .thenReturn(response);
        SalesResponse salesResponse = listingResource.getMySales(serviceContext, securityContext, i18nContext,
                "eeeddd", null, null, null, null, null, null, null, null);
    }

    @Test
    public void getMyEventSalesData_AccounException() throws Exception {
        AccountServiceImpl listingResource = new AccountServiceImpl();
//        AccountBiz accountBiz = mock(AccountBiz.class);
        SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
//        setBeanProperty(listingResource, "accountBiz", accountBiz);
        setBeanProperty(listingResource, "securityContextUtil", securityContextUtil);
        setBeanProperty(listingResource, "sellerEligibilityHelper", sellerEligibilityHelper);
        QueryResponse response = new QueryResponse();
        SolrDocumentList list = new SolrDocumentList();
        list.setNumFound(5);
        list.setStart(0);
        list.add(mockOrderSolrDocument(100, "ACTIVE", 10001, 100f, "1", "1", "3"));
        list.add(mockOrderSolrDocument(200, "ACTIVE", 10002, 200f, "0", "4", "2"));
        list.add(mockOrderSolrDocument(300, "ACTIVE", 10003, 300f, "0", "6", "1"));
        list.add(mockOrderSolrDocument(400, "ACTIVE", 10004, 400f, "0", "10", "1"));
        list.add(mockOrderSolrDocument(500, "INACTIVE", 10005, 500f, "0", "7", "1"));
        NamedList responseList = new NamedList();
        responseList.add("response", list);
        response.setResponse(responseList);
        com.stubhub.domain.account.common.Error listingError = new com.stubhub.domain.account.common.Error(
                ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT, "", "");
        AccountException ae = new AccountException(listingError);
        when(securityContext.getUserId()).thenReturn("8762167213");

        when(sellerEligibilityHelper.checkSellerEligibility()).thenReturn(true);
//        when(accountBiz.getEventSales((SalesSearchCriteria) argThat(getMatcher()))).thenThrow(ae);
        SalesResponse salesResponse = listingResource.getEventSalesData(securityContext, "",
                "32432423", null, null, null, null, null, null);
        assertNotNull(salesResponse);
    }

    @Test
    public void getMyEventSalesData_sorting() throws Exception {
        AccountServiceImpl listingResource = new AccountServiceImpl();
//        AccountBiz accountBiz = mock(AccountBiz.class);
        SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
//        setBeanProperty(listingResource, "accountBiz", accountBiz);
        setBeanProperty(listingResource, "securityContextUtil", securityContextUtil);
        setBeanProperty(listingResource, "sellerEligibilityHelper", sellerEligibilityHelper);
        QueryResponse response = new QueryResponse();
        SolrDocumentList list = new SolrDocumentList();
        list.setNumFound(5);
        list.setStart(0);
        list.add(mockOrderSolrDocument(100, "ACTIVE", 10001, 100f, "1", "1", "3"));
        list.add(mockOrderSolrDocument(200, "ACTIVE", 10002, 200f, "0", "4", "2"));
        list.add(mockOrderSolrDocument(300, "ACTIVE", 10003, 300f, "0", "6", "1"));
        list.add(mockOrderSolrDocument(400, "ACTIVE", 10004, 400f, "0", "10", "1"));
        list.add(mockOrderSolrDocument(500, "INACTIVE", 10005, 500f, "0", "7", "1"));
        NamedList responseList = new NamedList();
        responseList.add("response", list);
        response.setResponse(responseList);
        when(securityContext.getUserId()).thenReturn("8762167213");

        when(sellerEligibilityHelper.checkSellerEligibility()).thenReturn(true);

//        when(accountBiz.getEventSales((SalesSearchCriteria) argThat(getMatcher())))
//                .thenReturn(response);
        setBeanProperty(listingResource, "accountSolrCloudBiz", accountSolrCloudBiz);
        Mockito.when(accountSolrCloudBiz.getSellerEventSales(Mockito.any(SalesSearchCriteria.class)))
                .thenReturn(null != response ? covertQueryResponse(response) : null);
        PaginationInput input = new PaginationInput();
        input.setRows(10);
        input.setStart(0);
        SalesResponse salesResponse = listingResource.getEventSalesData(securityContext, "",
                "32432432", input, "PRICE ASC, SECTION DESC, ROW ASC, QUANTITY DESC", null, "5353434242432", null, null);
        assertNotNull(salesResponse);
    }

    @Test
    public void getMyEventSalesData_filetrs() throws Exception {
        AccountServiceImpl listingResource = new AccountServiceImpl();
//        AccountBiz accountBiz = mock(AccountBiz.class);
        SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
//        setBeanProperty(listingResource, "accountBiz", accountBiz);
        setBeanProperty(listingResource, "securityContextUtil", securityContextUtil);
        setBeanProperty(listingResource, "sellerEligibilityHelper", sellerEligibilityHelper);
        QueryResponse response = new QueryResponse();
        SolrDocumentList list = new SolrDocumentList();
        list.setNumFound(5);
        list.setStart(0);
        list.add(mockOrderSolrDocument(100, "ACTIVE", 10001, 100f, "1", "1", "3"));
        list.add(mockOrderSolrDocument(200, "ACTIVE", 10002, 200f, "0", "4", "2"));
        list.add(mockOrderSolrDocument(300, "ACTIVE", 10003, 300f, "0", "6", "1"));
        list.add(mockOrderSolrDocument(400, "ACTIVE", 10004, 400f, "0", "10", "1"));
        list.add(mockOrderSolrDocument(500, "INACTIVE", 10005, 500f, "0", "7", "1"));
        NamedList responseList = new NamedList();
        responseList.add("response", list);
        response.setResponse(responseList);
        when(securityContext.getUserId()).thenReturn("8762167213");
        when(sellerEligibilityHelper.checkSellerEligibility()).thenReturn(true);

//        when(accountBiz.getEventSales((SalesSearchCriteria) argThat(getMatcher())))
//                .thenReturn(response);
        setBeanProperty(listingResource, "accountSolrCloudBiz", accountSolrCloudBiz);
        Mockito.when(accountSolrCloudBiz.getSellerEventSales(Mockito.any(SalesSearchCriteria.class)))
                .thenReturn(null != response ? covertQueryResponse(response) : null);
        PaginationInput input = new PaginationInput();
        input.setRows(10);
        input.setStart(0);
        SalesResponse salesResponse = listingResource.getEventSalesData(securityContext, "",
                "B5D14E323CD55E9FE04400144F8AE084", input, "junk asc",
                "EVENT:123455 AND EVENTDATE:[2012-09-01 TO 2012-09-30] AND PRICE:[40.00 TO 30.00] AND DELIVERYOPTION:BARCODE AND SALESTATUS:SHIPPED AND LISTINGID:12345 AND SALEDATE:[2012-09-01 TO 2012-09-30]",
                null, null, null);
        assertNotNull(salesResponse);
    }

    @Test
    public void testGetTransactionSummary() throws Exception {
        AccountServiceImpl accountService = new AccountServiceImpl();
        SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
        setBeanProperty(accountService, "securityContextUtil", securityContextUtil);
        RequestValidator requestValidator = mock(RequestValidator.class);
        setBeanProperty(accountService, "requestValidator", requestValidator);
        OrderSolrBO orderSolrBO = mock(OrderSolrBO.class);
//        InventorySolrBO inventorySolrBO = mock(InventorySolrBO.class);
        StubTransBO stubTransBO = mock(StubTransBO.class);
        ReflectionTestUtils.setField(accountService, "transactionSummaryBO", transactionSummaryBO);
        setBeanProperty(accountService, "orderSolrBO", orderSolrBO);
//        setBeanProperty(accountService, "inventorySolrBO", inventorySolrBO);
        setBeanProperty(accountService, "stubTransBO", stubTransBO);
        AccountResponseAdapter accountResponseAdapter = mock(AccountResponseAdapter.class);

        TransactionSummaryRequest transactionSummaryRequest = new TransactionSummaryRequest();
        transactionSummaryRequest.setProxiedId("1");
        transactionSummaryRequest.setBuyerFlip("false");
        Mockito.when(
                requestValidator.validateTransactionSummaryRequestFields(transactionSummaryRequest))
                .thenReturn(null);
        Response response = accountService.getTransactionSummary(transactionSummaryRequest,serviceContext,
                securityContext);
        assertNotNull(response);

        transactionSummaryRequest = new TransactionSummaryRequest();
        transactionSummaryRequest.setProxiedId("1");
        transactionSummaryRequest.setBuyerFlip(null);
        Mockito.when(
                requestValidator.validateTransactionSummaryRequestFields(transactionSummaryRequest))
                .thenReturn(null);
        response = accountService.getTransactionSummary(transactionSummaryRequest, serviceContext, securityContext);
        assertNotNull(response);

        transactionSummaryRequest = new TransactionSummaryRequest();
        transactionSummaryRequest.setProxiedId("1");
        transactionSummaryRequest.setBuyerFlip("true");
        List<Error> errorList = new LinkedList<Error>();
        errorList.add(new Error(ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT, "No input provided",
                null));
        Mockito.when(
                requestValidator.validateTransactionSummaryRequestFields(transactionSummaryRequest))
                .thenReturn(errorList);
        response = accountService.getTransactionSummary(transactionSummaryRequest,serviceContext, securityContext);
        assertNotNull(response);

        HashMap<String, String> orderStats = new HashMap<String, String>();
        orderStats.put("IS_TOP_BUYER", "true");
        orderStats.put("PURCHASE_COUNT", "2");
        orderStats.put("PURCHASE_TOTAL", "2000");
        orderStats.put("OPEN_ORDER_PURCHASE_TOTAL", "2000");
        orderStats.put("AVERAGE_ORDER_SIZE", "2000");
        orderStats.put("CANCELLED_BUYS_COUNT", "2");
        orderStats.put("COMPLETED_BUYS_COUNT", "2");
        orderStats.put("UNCONFIRMED_BUYS_COUNT", "2");
        orderStats.put("EARLIEST_EVENT_DATE_UTC", "2014-09-09T01:01:00Z");
        orderStats.put("EARLIEST_EVENT_DATE_LOCAL", "2014-09-09T01:01:00Z");
        orderStats.put("CURRENCY", "USD");
        orderStats.put("BUYER_FLIP_COUNT", "2");
        orderStats.put("DROP_ORDER_RATE", "0.01");
        HashMap<String, String> listingStats = new HashMap<String, String>();
        listingStats.put("ACTIVE_USER_LISTING_COUNT", "2");
        listingStats.put("INACTIVE_USER_LISTING_COUNT", "2");
        listingStats.put("PENDING_LOCK_USER_LISTING_COUNT", "2");
        listingStats.put("DELETED_USER_LISTING_COUNT", "2");
        listingStats.put("INCOMPLETE_USER_LISTING_COUNT", "2");
        listingStats.put("PENDING_LMS_APPROVAL_USER_LISTING_COUNT", "2");
        Mockito.when(requestValidator.validateTransactionSummaryRequestFields(
                (TransactionSummaryRequest) argThat(getMatcher()))).thenReturn(null);
//        Mockito.when(orderSolrBO.getTransactionSummary((String) argThat(getMatcher())))
//                .thenReturn(orderStats);
//        Mockito.when(inventorySolrBO.getListingStatsBySellerId((String) argThat(getMatcher())))
//                .thenReturn(listingStats);
        Mockito.when(stubTransBO.getBuyerFlipCount((Long) argThat(getMatcher()))).thenReturn(2);
        response = accountService.getTransactionSummary(transactionSummaryRequest,serviceContext, securityContext);
        assertNotNull(response);

        List<HashMap<String, String>> summaryStats = new ArrayList<HashMap<String, String>>();
        summaryStats.add(0, orderStats);
        summaryStats.add(1, listingStats);

        when(transactionSummaryBO.getUserTransactionSummary(1L, null, true)).thenReturn(summaryStats);
        response = accountService.getTransactionSummary(transactionSummaryRequest,serviceContext, securityContext);
        assertNotNull(response);

        transactionSummaryRequest.setCurrencyCode("EUR");
        when(transactionSummaryBO.getUserTransactionSummary(1L, "EUR", true)).thenReturn(summaryStats);
        response = accountService.getTransactionSummary(transactionSummaryRequest,serviceContext, securityContext);
        assertNotNull(response);
    }

    @Test
    public void testGetTransactionSummaryWithDropOrderCount() throws Exception {
        AccountServiceImpl accountService = new AccountServiceImpl();
        SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
        setBeanProperty(accountService, "securityContextUtil", securityContextUtil);
        RequestValidator requestValidator = mock(RequestValidator.class);
        setBeanProperty(accountService, "requestValidator", requestValidator);
        OrderSolrBO orderSolrBO = mock(OrderSolrBO.class);
//        InventorySolrBO inventorySolrBO = mock(InventorySolrBO.class);
        StubTransBO stubTransBO = new StubTransBOImpl();

        ReflectionTestUtils.setField(accountService, "transactionSummaryBO", transactionSummaryBO);
        setBeanProperty(accountService, "orderSolrBO", orderSolrBO);
//        setBeanProperty(accountService, "inventorySolrBO", inventorySolrBO);
        setBeanProperty(accountService, "stubTransBO", stubTransBO);
        setBeanProperty(stubTransBO, "stubTransDAO", stubTransDAO);
        AccountResponseAdapter accountResponseAdapter = mock(AccountResponseAdapter.class);

        TransactionSummaryRequest transactionSummaryRequest = new TransactionSummaryRequest();
        transactionSummaryRequest.setProxiedId("1");
        transactionSummaryRequest.setBuyerFlip("false");
        Mockito.when(
                requestValidator.validateTransactionSummaryRequestFields(transactionSummaryRequest))
                .thenReturn(null);
        Response response = accountService.getTransactionSummary(transactionSummaryRequest,serviceContext,
                securityContext);
        assertNotNull(response);

        Map<String, String> orderStats = new HashMap<String, String>();
        orderStats.put("IS_TOP_BUYER", "true");
        orderStats.put("PURCHASE_COUNT", "2");
        orderStats.put("PURCHASE_TOTAL", "2000");
        orderStats.put("OPEN_ORDER_PURCHASE_TOTAL", "2000");
        orderStats.put("AVERAGE_ORDER_SIZE", "2000");
        orderStats.put("CANCELLED_BUYS_COUNT", "2");
        orderStats.put("COMPLETED_BUYS_COUNT", "2");
        orderStats.put("UNCONFIRMED_BUYS_COUNT", "2");
        orderStats.put("EARLIEST_EVENT_DATE_UTC", "2014-09-09T01:01:00Z");
        orderStats.put("EARLIEST_EVENT_DATE_LOCAL", "2014-09-09T01:01:00Z");
        orderStats.put("CURRENCY", "USD");
        Map<String, String> listingStats = new HashMap<String, String>();
        listingStats.put("ACTIVE_USER_LISTING_COUNT", "2");
        listingStats.put("INACTIVE_USER_LISTING_COUNT", "2");
        listingStats.put("PENDING_LOCK_USER_LISTING_COUNT", "2");
        listingStats.put("DELETED_USER_LISTING_COUNT", "2");
        listingStats.put("INCOMPLETE_USER_LISTING_COUNT", "2");
        listingStats.put("PENDING_LMS_APPROVAL_USER_LISTING_COUNT", "2");
        Mockito.when(requestValidator.validateTransactionSummaryRequestFields(
                (TransactionSummaryRequest) argThat(getMatcher()))).thenReturn(null);
//        Mockito.when(orderSolrBO.getTransactionSummary((String) argThat(getMatcher())))
//                .thenReturn(orderStats);
//        Mockito.when(inventorySolrBO.getListingStatsBySellerId((String) argThat(getMatcher())))
//                .thenReturn(listingStats);
        Mockito.when(stubTransBO.getBuyerFlipCount((Long) argThat(getMatcher()))).thenReturn(2);

        Mockito.when(stubTransDAO.getSelStubTicketCount((Long) argThat(getMatcher())))
                .thenReturn(0);
        Mockito.when(stubTransDAO.getSelTransTikCount((Long) argThat(getMatcher()))).thenReturn(0);
        Mockito.when(stubTransDAO.getSelPayTicketCount((Long) argThat(getMatcher()))).thenReturn(0);

        stubTransBO.getDrpOrderCount((Long) argThat(getMatcher()));
        response = accountService.getTransactionSummary(transactionSummaryRequest, serviceContext, securityContext);
        assertNotNull(response);
        Assert.assertEquals(response.getStatus(), 200);

        Mockito.when(stubTransDAO.getSelStubTicketCount((Long) argThat(getMatcher())))
                .thenReturn(98746);
        Mockito.when(stubTransDAO.getSelTransTikCount((Long) argThat(getMatcher()))).thenReturn(0);
        Mockito.when(stubTransDAO.getSelPayTicketCount((Long) argThat(getMatcher())))
                .thenReturn(10000);
        response = accountService.getTransactionSummary(transactionSummaryRequest,serviceContext, securityContext);
        assertNotNull(response);
        Assert.assertEquals(response.getStatus(), 200);

    }

    @Test
    public void testGetTransactionSummary_Exceptions() throws Exception {
        AccountServiceImpl accountService = new AccountServiceImpl();
        SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
        setBeanProperty(accountService, "securityContextUtil", securityContextUtil);
        TransactionSummaryRequest transactionSummaryResponse = new TransactionSummaryRequest();
        when(accountService.getTransactionSummary(transactionSummaryResponse, serviceContext,securityContext))
                .thenThrow(new UserNotAuthorizedException());
        Response ordersResponse = accountService.getTransactionSummary(transactionSummaryResponse,serviceContext,
                securityContext);
        assertNotNull(ordersResponse);
        when(accountService.getTransactionSummary(transactionSummaryResponse,serviceContext, securityContext))
                .thenThrow(new AccountException());
        ordersResponse = accountService.getTransactionSummary(transactionSummaryResponse,serviceContext,
                securityContext);
        assertNotNull(ordersResponse);
        when(accountService.getTransactionSummary(transactionSummaryResponse,serviceContext, securityContext))
                .thenThrow(new NullPointerException());
        ordersResponse = accountService.getTransactionSummary(transactionSummaryResponse,serviceContext,
                securityContext);
        assertNotNull(ordersResponse);
    }

    @Test
    public void testConvertBizTransactionSummaryResponseToWebEntities() throws Exception {
        Map<String, String> orderStats = new HashMap<String, String>();
        orderStats.put("IS_TOP_BUYER", "true");
        orderStats.put("PURCHASE_COUNT", "2");
        orderStats.put("PURCHASE_TOTAL", "2000");
        orderStats.put("OPEN_ORDER_PURCHASE_TOTAL", "2000");
        orderStats.put("AVERAGE_ORDER_SIZE", "2000");
        orderStats.put("CANCELLED_BUYS_COUNT", "2");
        orderStats.put("COMPLETED_BUYS_COUNT", "2");
        orderStats.put("UNCONFIRMED_BUYS_COUNT", "2");
        orderStats.put("EARLIEST_EVENT_DATE_UTC", "2014-09-09T01:01:00Z");
        orderStats.put("EARLIEST_EVENT_DATE_LOCAL", "2014-09-09T01:01:00Z");
        orderStats.put("CURRENCY", "USD");
        orderStats.put("CANCELLED_SALES_COUNT", "2");
        orderStats.put("COMPLETED_SALES_COUNT", "2");
        orderStats.put("UNCONFIRMED_SALES_COUNT", "2");
        Map<String, String> listingStats = new HashMap<String, String>();
        listingStats.put("ACTIVE_USER_LISTING_COUNT", "2");
        listingStats.put("INACTIVE_USER_LISTING_COUNT", "2");
        listingStats.put("PENDING_LOCK_USER_LISTING_COUNT", "2");
        listingStats.put("DELETED_USER_LISTING_COUNT", "2");
        listingStats.put("INCOMPLETE_USER_LISTING_COUNT", "2");
        listingStats.put("PENDING_LMS_APPROVAL_USER_LISTING_COUNT", "2");
        TransactionSummaryResponse response = AccountResponseAdapter
                .convertBizTransactionSummaryResponseToWebEntities(orderStats, listingStats);
        assertNotNull(response);
    }

//    @Test
//    public void testConvertRequestToOrderSearchCriteria()
//            throws NumberFormatException, ParseException {
//        SalesHistoryRequest shr = new SalesHistoryRequest();
//        shr.setEventId("123");
//        OrderSearchCriteria osc = AccountRequestAdapter.convertRequestToOrderSearchCriteria(shr);
//        shr.setSectionIds("123");
//        AccountRequestAdapter.convertRequestToOrderSearchCriteria(shr);
//        shr.setDeliveryOptions("abc");
//        AccountRequestAdapter.convertRequestToOrderSearchCriteria(shr);
//        shr.setQuantity("2");
//        AccountRequestAdapter.convertRequestToOrderSearchCriteria(shr);
//        shr.setQuantity(null);
//        AccountRequestAdapter.convertRequestToOrderSearchCriteria(shr);
//        shr.setRows("2");
//        AccountRequestAdapter.convertRequestToOrderSearchCriteria(shr);
//        shr.setZoneIds("2");
//        AccountRequestAdapter.convertRequestToOrderSearchCriteria(shr);
//        shr.setFromDate("2014-09-09");
//        AccountRequestAdapter.convertRequestToOrderSearchCriteria(shr);
//        shr.setToDate("2014-09-09");
//        osc = AccountRequestAdapter.convertRequestToOrderSearchCriteria(shr);
//        Assert.assertNotNull(osc);
//    }

    @Test
    public void testConvertRequestToSales() {
        List<CSSaleDetailsResponse> list = new ArrayList<CSSaleDetailsResponse>();
        CSSaleDetailsResponse cs = new CSSaleDetailsResponse();
        CSSaleTransactionResponse trans = new CSSaleTransactionResponse();
        trans.setSaleId("111");
        cs.setTransaction(trans);
        list.add(cs);
        CSSalesResponse salesResponse = new CSSalesResponse();
        salesResponse.setSale(list);
        Assert.assertEquals(AccountRequestAdapter.getSaleIds(salesResponse).size(), list.size());
    }

    @Test
    public void getMyEventSalesData_5sectionIds() throws Exception {
        AccountServiceImpl listingResource = new AccountServiceImpl();
//        AccountBiz accountBiz = mock(AccountBiz.class);
        SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
//        setBeanProperty(listingResource, "accountBiz", accountBiz);
        setBeanProperty(listingResource, "securityContextUtil", securityContextUtil);
        setBeanProperty(listingResource, "sellerEligibilityHelper", sellerEligibilityHelper);
        QueryResponse response = new QueryResponse();
        SolrDocumentList list = new SolrDocumentList();
        list.setNumFound(5);
        list.setStart(0);
        list.add(mockOrderSolrDocument(100, "ACTIVE", 10001, 100f, "1", "1", "3"));
        list.add(mockOrderSolrDocument(200, "ACTIVE", 10002, 200f, "0", "4", "2"));
        list.add(mockOrderSolrDocument(300, "ACTIVE", 10003, 300f, "0", "6", "1"));
        list.add(mockOrderSolrDocument(400, "ACTIVE", 10004, 400f, "0", "10", "1"));
        list.add(mockOrderSolrDocument(500, "INACTIVE", 10005, 500f, "0", "7", "1"));
        NamedList responseList = new NamedList();
        responseList.add("response", list);
        response.setResponse(responseList);
        when(securityContext.getUserId()).thenReturn("8762167213");

        when(sellerEligibilityHelper.checkSellerEligibility()).thenReturn(true);

//        when(accountBiz.getEventSales((SalesSearchCriteria) argThat(getMatcher())))
//                .thenReturn(response);
        setBeanProperty(listingResource, "accountSolrCloudBiz", accountSolrCloudBiz);
        Mockito.when(accountSolrCloudBiz.getSellerEventSales(Mockito.any(SalesSearchCriteria.class)))
                .thenReturn(null != response ? covertQueryResponse(response) : null);
        PaginationInput input = new PaginationInput();
        input.setRows(10);
        input.setStart(0);
        SalesResponse salesResponse = listingResource.getEventSalesData(securityContext, "",
                "32432432", input, "PRICE ASC", null,
                "6242342,234324,234234324,32432432,324234324,324234324,324324234,324234324", null,
                null);
        assertNotNull(salesResponse);
    }

    @Test
    public void getMyEventSalesData_deliveryOptionFilter() throws Exception {
        AccountServiceImpl listingResource = new AccountServiceImpl();
//        AccountBiz accountBiz = mock(AccountBiz.class);
        SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
//        setBeanProperty(listingResource, "accountBiz", accountBiz);
        setBeanProperty(listingResource, "securityContextUtil", securityContextUtil);
        setBeanProperty(listingResource, "sellerEligibilityHelper", sellerEligibilityHelper);
        QueryResponse response = new QueryResponse();
        SolrDocumentList list = new SolrDocumentList();
        list.setNumFound(5);
        list.setStart(0);
        list.add(mockOrderSolrDocument(100, "ACTIVE", 10001, 100f, "1", "1", "3"));
        list.add(mockOrderSolrDocument(200, "ACTIVE", 10002, 200f, "0", "4", "2"));
        list.add(mockOrderSolrDocument(300, "ACTIVE", 10003, 300f, "0", "6", "1"));
        list.add(mockOrderSolrDocument(400, "ACTIVE", 10004, 400f, "0", "10", "1"));
        list.add(mockOrderSolrDocument(500, "INACTIVE", 10005, 500f, "0", "7", "1"));
        NamedList responseList = new NamedList();
        responseList.add("response", list);
        response.setResponse(responseList);
        when(securityContext.getUserId()).thenReturn("8762167213");

        when(sellerEligibilityHelper.checkSellerEligibility()).thenReturn(true);

//        when(accountBiz.getEventSales((SalesSearchCriteria) argThat(getMatcher())))
//                .thenReturn(response);
        setBeanProperty(listingResource, "accountSolrCloudBiz", accountSolrCloudBiz);
        Mockito.when(accountSolrCloudBiz.getSellerEventSales(Mockito.any(SalesSearchCriteria.class)))
                .thenReturn(null != response ? covertQueryResponse(response) : null);
        PaginationInput input = new PaginationInput();
        input.setRows(10);
        input.setStart(0);
        SalesResponse salesResponse = listingResource.getEventSalesData(securityContext, "",
                "32432432", input, "PRICE ASC", "DELIVERYOPTION:BARCODE",
                "6242342,234324,234234324,32432432,324234324,324234324,324324234,324234324", null,
                null);
        assertNotNull(salesResponse);
    }

    @Test
    public void testGetEmail() throws Exception {
        AccountServiceImpl accountService = new AccountServiceImpl();
        SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
        setBeanProperty(accountService, "securityContextUtil", securityContextUtil);
        RequestValidator requestValidator = mock(RequestValidator.class);
        setBeanProperty(accountService, "requestValidator", requestValidator);
        EmailLogsBO emailLogsBO = mock(EmailLogsBO.class);
        setBeanProperty(accountService, "emailLogsBO", emailLogsBO);
        AccountResponseAdapter accountResponseAdapter = mock(AccountResponseAdapter.class);

        String emailId = "123456";
        List<Error> errorList = new LinkedList<Error>();
        errorList.add(new Error(ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT, "No input provided",
                null));
        Mockito.when(requestValidator.isValidLong("emailId", emailId)).thenReturn(false);
        Response response = accountService.getEmail(emailId, null, securityContext);
        assertNotNull(response);

        Mockito.when(requestValidator.isValidLong("emailId", emailId)).thenReturn(true);
        response = accountService.getEmail(emailId, null, securityContext);
        assertNotNull(response);

        EmailLog emailLog = new EmailLog();
        emailLog.setAddressBcc("bijain@stubhub.com");
        emailLog.setAddressCc("bijain1@stubhub.com");
        emailLog.setAddressFrom("bijain2@stubhub.com");
        emailLog.setAddressTo("bijain3@stubhub.com");
        emailLog.setDateAdded(Calendar.getInstance());
        emailLog.setDateSent(Calendar.getInstance());
        emailLog.setEmailId(Long.valueOf("123456"));
        emailLog.setFormat("text");
        emailLog.setSubject("deliver the tickets");
        emailLog.settId("1234");
        emailLog.setUserId("12345");

        Mockito.when(requestValidator.isValidLong((String) argThat(getMatcher()),
                (String) argThat(getMatcher()))).thenReturn(true);
        Mockito.when(emailLogsBO.getEmailById((Long) argThat(getMatcher()))).thenReturn(emailLog);
        response = accountService.getEmail(emailId, null, securityContext);
        assertNotNull(response);
    }

    @Test
    public void testGetEmail_Exceptions() throws Exception {
        AccountServiceImpl accountService = new AccountServiceImpl();
        SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
        setBeanProperty(accountService, "securityContextUtil", securityContextUtil);
        String emailId = "123456";
        when(accountService.getEmail(emailId, null, securityContext))
                .thenThrow(new UserNotAuthorizedException());
        Response ordersResponse = accountService.getEmail(emailId, null, securityContext);
        assertNotNull(ordersResponse);
        when(accountService.getEmail(emailId, null, securityContext))
                .thenThrow(new AccountException());
        ordersResponse = accountService.getEmail(emailId, null, securityContext);
        assertNotNull(ordersResponse);
        when(accountService.getEmail(emailId, null, securityContext))
                .thenThrow(new NullPointerException());
        ordersResponse = accountService.getEmail(emailId, null, securityContext);
        assertNotNull(ordersResponse);
    }

    @Test
    public void testGetEmailLogs() throws Exception {
        AccountServiceImpl accountService = new AccountServiceImpl();
        SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
        setBeanProperty(accountService, "securityContextUtil", securityContextUtil);
        RequestValidator requestValidator = mock(RequestValidator.class);
        setBeanProperty(accountService, "requestValidator", requestValidator);
        EmailLogsBO emailLogsBO = mock(EmailLogsBO.class);
        setBeanProperty(accountService, "emailLogsBO", emailLogsBO);
        AccountResponseAdapter accountResponseAdapter = mock(AccountResponseAdapter.class);

        SearchEmailCriteria sc = new SearchEmailCriteria();
        sc.setFromDate("2015-02-02");
        sc.setOrderId("1234");
        sc.setRows("2");
        sc.setStart("1");
        sc.setSubject("welcome");
        sc.setToDate("2015-02-04");

        String proxiedId = "1234";
        List<Error> errorList = new LinkedList<Error>();
        errorList.add(new Error(ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT, "No input provided",
                null));
        Mockito.when(requestValidator.validateEmailHistoryRequestField(proxiedId, sc))
                .thenReturn(errorList);
        Response response = accountService.getEmailLogs(sc, serviceContext, securityContext);
        assertNotNull(response);

        Mockito.when(requestValidator.validateEmailHistoryRequestField(proxiedId, sc))
                .thenReturn(null);
        response = accountService.getEmailLogs(sc, serviceContext, securityContext);
        assertNotNull(response);

        EmailLog emailLog = new EmailLog();
        emailLog.setAddressBcc("bijain@stubhub.com");
        emailLog.setAddressCc("bijain1@stubhub.com");
        emailLog.setAddressFrom("bijain2@stubhub.com");
        emailLog.setAddressTo("bijain3@stubhub.com");
        emailLog.setDateAdded(Calendar.getInstance());
        emailLog.setDateSent(Calendar.getInstance());
        emailLog.setEmailId(Long.valueOf("123456"));
        emailLog.setFormat("text");
        emailLog.setSubject("deliver the tickets");
        emailLog.settId("1234");
        emailLog.setUserId("12345");
        List<EmailLog> emailLogs = new ArrayList<EmailLog>();
        emailLogs.add(emailLog);

        Mockito.when(requestValidator.validateEmailHistoryRequestField(
                (String) argThat(getMatcher()), (SearchEmailCriteria) argThat(getMatcher())))
                .thenReturn(null);
        Mockito.when(emailLogsBO.getEmailLogs((Long) argThat(getMatcher()),
                (SearchEmailCriteria) argThat(getMatcher()))).thenReturn(null);
        response = accountService.getEmailLogs(sc, serviceContext, securityContext);
        assertNotNull(response);
        Mockito.when(emailLogsBO.getEmailLogs((Long) argThat(getMatcher()),
                (SearchEmailCriteria) argThat(getMatcher()))).thenReturn(emailLogs);
        response = accountService.getEmailLogs(sc, serviceContext, securityContext);
        assertNotNull(response);
    }

    @Test
    public void testGetEmailLogs_Exceptions() throws Exception {
        AccountServiceImpl accountService = new AccountServiceImpl();
        SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
        setBeanProperty(accountService, "securityContextUtil", securityContextUtil);
        SearchEmailCriteria sc = new SearchEmailCriteria();
        sc.setFromDate("2015-02-02");
        sc.setOrderId("1234");
        sc.setRows("2");
        sc.setStart("1");
        sc.setSubject("welcome");
        sc.setToDate("2015-02-04");
        when(accountService.getEmailLogs(sc, serviceContext, securityContext))
                .thenThrow(new UserNotAuthorizedException());
        Response response = accountService.getEmailLogs(sc, serviceContext, securityContext);
        assertNotNull(response);
        when(accountService.getEmailLogs(sc, serviceContext, securityContext))
                .thenThrow(new AccountException());
        response = accountService.getEmailLogs(sc, serviceContext, securityContext);
        assertNotNull(response);
        when(accountService.getEmailLogs(sc, serviceContext, securityContext))
                .thenThrow(new NullPointerException());
        response = accountService.getEmailLogs(sc, serviceContext, securityContext);
        assertNotNull(response);
    }

    @Test
    public void testUpdateOrder() throws Exception {
        AccountServiceImpl accountService = new AccountServiceImpl();
        SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
        setBeanProperty(accountService, "securityContextUtil", securityContextUtil);
        RequestValidator requestValidator = mock(RequestValidator.class);
        setBeanProperty(accountService, "requestValidator", requestValidator);
        StubTransBO stubTransBO = mock(StubTransBO.class);
        setBeanProperty(accountService, "stubTransBO", stubTransBO);
        StubnetUserBO stubnetUserBO = mock(StubnetUserBO.class);
        ReflectionTestUtils.setField(accountService, "stubnetUserBO", stubnetUserBO);

        OrdersResponse request = new OrdersResponse();
        List<CSOrderDetailsResponse> list = new ArrayList<CSOrderDetailsResponse>();
        CSOrderDetailsResponse order = new CSOrderDetailsResponse();
        DeliveryResponse delivery = new DeliveryResponse();
        delivery.setOrderProcSubStatusCode("7");
        TransactionResponse transaction = new TransactionResponse();
        transaction.setOrderId("12");
        transaction.setCancelled(true);
        order.setDelivery(delivery);
        order.setTransaction(transaction);
        list.add(order);
        request.setOrder(list);

        Mockito.when(stubnetUserBO.isStubnetUser(serviceContext.getOperatorId())).thenReturn(1L);
        List<Error> errorList = new LinkedList<Error>();
        errorList.add(new Error(ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT, "No input provided",
                null));
        Mockito.when(requestValidator.validateUpdateCSOrderDetails(serviceContext, request))
                .thenReturn(errorList);
        Response response = accountService.updateCSOrderDetails(request, serviceContext,
                securityContext);
        assertNotNull(response);

        Mockito.when(requestValidator.validateUpdateCSOrderDetails(serviceContext, request))
                .thenReturn(null);
        response = accountService.updateCSOrderDetails(request, serviceContext, securityContext);
        assertNotNull(response);
    }

    @Test
    public void testUpdateOrder_Exceptions() throws Exception {
        AccountServiceImpl accountService = new AccountServiceImpl();
        OrdersResponse request = new OrdersResponse();
        SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
        setBeanProperty(accountService, "securityContextUtil", securityContextUtil);
        String emailId = "123456";
        when(accountService.updateCSOrderDetails(request, null, securityContext))
                .thenThrow(new UserNotAuthorizedException());
        Response ordersResponse = accountService.updateCSOrderDetails(request, null,
                securityContext);
        assertNotNull(ordersResponse);
        when(accountService.updateCSOrderDetails(request, null, securityContext))
                .thenThrow(new AccountException());
        ordersResponse = accountService.updateCSOrderDetails(request, null, securityContext);
        assertNotNull(ordersResponse);
        when(accountService.updateCSOrderDetails(request, null, securityContext))
                .thenThrow(new NullPointerException());
        ordersResponse = accountService.updateCSOrderDetails(request, null, securityContext);
        assertNotNull(ordersResponse);
    }

    @Test
    public void testCreateSubstitutionOrder() throws Exception {
        AccountServiceImpl accountService = new AccountServiceImpl();
        SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
        setBeanProperty(accountService, "securityContextUtil", securityContextUtil);
        RequestValidator requestValidator = mock(RequestValidator.class);
        setBeanProperty(accountService, "requestValidator", requestValidator);
        SubstitutionOrderBO substitutionOrderBO = mock(SubstitutionOrderBO.class);
        setBeanProperty(accountService, "substitutionOrderBO", substitutionOrderBO);
        StubnetUserBO stubnetUserBO = mock(StubnetUserBO.class);
        ReflectionTestUtils.setField(accountService, "stubnetUserBO", stubnetUserBO);

        String orderId = "175571218";
        SubstitutionRequest request = createSubsOrderRequest();
        SubstitutionResponse subsResponse = new SubstitutionResponse();
        subsResponse.setNewOrderId(2L);

        Mockito.when(stubnetUserBO.isStubnetUser(serviceContext.getOperatorId())).thenReturn(1L);
        List<Error> errorList = new LinkedList<Error>();
        errorList.add(new Error(ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT, "No input provided",
                null));
        Mockito.when(
                requestValidator.validateCreateSubOrderRequest(request, orderId, serviceContext))
                .thenReturn(errorList);
        Response response = accountService.createSubstitutionOrder(request, orderId, serviceContext,
                securityContext);
        assertNotNull(response);

        Mockito.when(
                requestValidator.validateCreateSubOrderRequest(request, orderId, serviceContext))
                .thenReturn(null);
        response = accountService.createSubstitutionOrder(request, orderId, serviceContext,
                securityContext);
        assertNotNull(response);
    }

    @Test
    public void testCreateSubstitutionOrder_Exception()
            throws Exception {
        AccountServiceImpl accountService = new AccountServiceImpl();
        SubstitutionRequest request = new SubstitutionRequest();
        String orderId = "123";
        SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
        setBeanProperty(accountService, "securityContextUtil", securityContextUtil);
        RequestValidator requestValidator = mock(RequestValidator.class);
        setBeanProperty(accountService, "requestValidator", requestValidator);
        SubstitutionOrderBO substitutionOrderBO = mock(SubstitutionOrderBO.class);
        setBeanProperty(accountService, "substitutionOrderBO", substitutionOrderBO);
        StubnetUserBO stubnetUserBO = mock(StubnetUserBO.class);
        ReflectionTestUtils.setField(accountService, "stubnetUserBO", stubnetUserBO);
        Mockito.when(stubnetUserBO.isStubnetUser("bijain")).thenReturn(1L);
        Mockito.when(accountService.createSubstitutionOrder(request, orderId, serviceContext,
                securityContext)).thenThrow(new RecordNotFoundForIdException("", 1L));
        Mockito.when(accountService.createSubstitutionOrder(request, orderId, serviceContext,
                securityContext)).thenThrow(new NullPointerException());
        Response response = accountService.createSubstitutionOrder(request, orderId, serviceContext,
                securityContext);
        Assert.assertNotNull(response);
        Mockito.when(accountService.createSubstitutionOrder(request, orderId, serviceContext,
                securityContext)).thenThrow(new UserNotAuthorizedException());
        response = accountService.createSubstitutionOrder(request, orderId, serviceContext,
                securityContext);
        Assert.assertNotNull(response);
        Mockito.when(accountService.createSubstitutionOrder(request, orderId, serviceContext,
                securityContext)).thenThrow(new InvalidArgumentException("", ""));
        response = accountService.createSubstitutionOrder(request, orderId, serviceContext,
                securityContext);
        Assert.assertNotNull(response);
    }

    private SubstitutionRequest createSubsOrderRequest() {
        SubstitutionRequest request = new SubstitutionRequest();
        request.setListingId("1140386353");
        request.setQuantity("2");
        request.setTicketCostDifference(new Money("11.11", "USD"));
        request.setSellerPayoutDifference(new Money("11.11", "USD"));
        request.setSubsReasonId("10");
        request.setDeliveryMethodId("1");
        request.setFulfillmentMethodId("5");
        request.setLmsLocationId("10000");
        request.setInHandDate("2015-05-05");
        request.setTicketCost(new Money("11.11", "USD"));
        request.setShipCost(new Money("11.11", "USD"));
        request.setTotalCost(new Money("11.11", "USD"));
        request.setDiscountCost(new Money("11.11", "USD"));
        request.setSellerFeeVal(new Money("11.11", "USD"));
        request.setBuyerFeeVal(new Money("11.11", "USD"));
        request.setPremiumFees(new Money("11.11", "USD"));
        request.setSellerPayoutAmount(new Money("11.11", "USD"));
        request.setSellerPayoutAtConfirm(new Money("11.11", "USD"));
        request.setAddOnFee(new Money("11.11", "USD"));
        request.setVatBuyFee(new Money("11.11", "USD"));
        request.setVatLogFee(new Money("11.11", "USD"));
        request.setVatSellFee(new Money("11.11", "USD"));
        request.setAdditionalSellFeePerTicket(new Money("11.11", "USD"));
        return request;
    }

    @Test
    public void testReportAnIssue() throws Throwable {

        org.codehaus.jackson.map.ObjectMapper mapper = new org.codehaus.jackson.map.ObjectMapper();

        SvcLocator svcLocator = mock(SvcLocator.class);
        WebClient webClient = mock(WebClient.class);

        CustomerServiceHelper customerServiceHelper = new CustomerServiceHelper();
        ReflectionTestUtils.setField(customerServiceHelper, "svcLocator", svcLocator);

        MasterStubhubPropertiesWrapper properties = mock(MasterStubhubPropertiesWrapper.class);
        ReflectionTestUtils.setField(customerServiceHelper, "properties", properties);
        when(properties.getProperty("unified.cs.bpm.message.api.url"))
                .thenReturn("https://api-int.stubhub.com/cs/bpm");
        when(properties.getProperty("unified.cs.csrnotes.api.url"))
                .thenReturn("http://api-int.${default_domain}/cs/csrnotes/v1/orders/{orderId}");

        SecurityContextUtil scu = new SecurityContextUtil();
        ReflectionTestUtils.setField(accountService, "securityContextUtil", scu);

        ReflectionTestUtils.setField(accountService, "customerServiceHelper",
                customerServiceHelper);

        StubTransBO stubTransBO = mock(StubTransBO.class);
        when(stubTransBO.updateOrder(anyString(), (StubTransUpdateRequest) anyObject()))
                .thenReturn(1L);
        List<StubTrans> stubTransList = new ArrayList<StubTrans>();
        StubTrans stubTrans = new StubTrans();
        stubTrans.setSellerId(12345L);
        stubTrans.setEventId(321L);
        stubTransList.add(stubTrans);
        when(stubTransBO.getOrderProcSubStatus(10000L)).thenReturn(stubTransList);

        ReflectionTestUtils.setField(accountService, "stubTransBO", stubTransBO);

        Response responseCreated = Response.status(Response.Status.CREATED).build();

        when(svcLocator.locate(anyString())).thenReturn(webClient);
        when(svcLocator.locate(anyString(), anyList())).thenReturn(webClient);

        when(webClient.post(anyString())).thenReturn(responseCreated);

        EventUtil eventUtil = mock(EventUtil.class);
        ReflectionTestUtils.setField(accountService, "eventUtil", eventUtil);


        when(eventUtil.getEventDetailsV2(321L)).thenReturn(getEventDetailsV2Response());


        when(stubTransBO.getFulfillmentMethodIdByTid(10000L)).thenReturn(10L);

        {
            ReportAnIssueRequest r = new ReportAnIssueRequest();
            r.setType(ReportAnIssueRequest.Type.INHANDDATECHANGE);
            ReportAnIssueRequest.InHandDateChangeData inHandDateChangeData = new ReportAnIssueRequest.InHandDateChangeData();
            inHandDateChangeData.setNewDate("2015-06-15T02:51:16.000Z");
            inHandDateChangeData.setOriginalDate("2015-05-15T02:51:16.000Z");
            r.setInHandDateChangeData(inHandDateChangeData);
            // System.out.println(mapper.writeValueAsString(r));
            Assert.assertEquals(
                    accountService.reportAnIssue(securityContext, "10000", r).getStatus(),
                    Response.Status.OK.getStatusCode());

            inHandDateChangeData.setNewDate("2015-07-15T02:51:16.000Z");

            Assert.assertEquals(
                    accountService.reportAnIssue(securityContext, "10000", r).getStatus(),
                    Response.Status.BAD_REQUEST.getStatusCode());

            when(eventUtil.getEventDetailsV2(321L)).thenReturn(getEventDetailsV2ResponseWithEmptyInHandDate());
            Assert.assertEquals(
                    accountService.reportAnIssue(securityContext, "10000", r).getStatus(),
                    Response.Status.OK.getStatusCode());


            when(stubTransBO.getFulfillmentMethodIdByTid(10000L)).thenReturn(5L);

            Assert.assertEquals(
                    accountService.reportAnIssue(securityContext, "10000", r).getStatus(),
                    Response.Status.BAD_REQUEST.getStatusCode());

        }

        {
            ReportAnIssueRequest r = new ReportAnIssueRequest();
            r.setType(ReportAnIssueRequest.Type.REPLACEMENT);
            ReportAnIssueRequest.ReplacementData replacementData = new ReportAnIssueRequest.ReplacementData();
            replacementData.setRow("NewRow");
            replacementData.setSeats(null);
            replacementData.setSection("NewSection");
            r.setReplacementData(replacementData);
            // System.out.println(mapper.writeValueAsString(r));

            Assert.assertEquals(
                    accountService.reportAnIssue(securityContext, "10000", r).getStatus(),
                    Response.Status.OK.getStatusCode());

            replacementData.setSeats("1,2,3,4");
            Assert.assertEquals(
                    accountService.reportAnIssue(securityContext, "10000", r).getStatus(),
                    Response.Status.OK.getStatusCode());

        }

        {
            ReportAnIssueRequest r = new ReportAnIssueRequest();
            r.setType(ReportAnIssueRequest.Type.NOTICKET);

            Response response = null;

            // case : internal API failed
            com.stubhub.domain.account.common.Response resp = new com.stubhub.domain.account.common.Response();
            resp.setErrors(new ArrayList<com.stubhub.domain.account.common.Error>());
            Response responseError = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(resp)
                    .build();
            when(webClient.post(anyString())).thenReturn(responseError);
            response = accountService.reportAnIssue(securityContext, "10000", r);

            // System.out.println(mapper.writeValueAsString(response.getEntity()));

            Assert.assertEquals(response.getStatus(), Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());

            // case : sale not found
            response = accountService.reportAnIssue(securityContext, "10001", r);
            Assert.assertEquals(response.getStatus(), Response.Status.NOT_FOUND.getStatusCode());

            // case : sale not belong to seller
            stubTrans.setSellerId(12346L);
            response = accountService.reportAnIssue(securityContext, "10000", r);
            Assert.assertEquals(response.getStatus(), Response.Status.BAD_REQUEST.getStatusCode());

            stubTrans.setSellerId(12345L);

        }

    }


    private EventMetadataResponse getEventDetailsV2Response() {
        EventMetadataResponse eventMetaResponse = new EventMetadataResponse();
        List<InvFulfillmentDetails> fmds = new ArrayList<InvFulfillmentDetails>();
        InvFulfillmentDetails ifd = new InvFulfillmentDetails();
        ifd.setEarliestInHandDate("2015-02-12T12:43:21-0700");
        ifd.setLatestInHandDate("2015-06-18T13:10:00-0600");
        ifd.setId(10L);
        fmds.add(ifd);
        eventMetaResponse.setFulfillmentMethods(fmds);
        return eventMetaResponse;
    }


    private EventMetadataResponse getEventDetailsV2ResponseWithEmptyInHandDate() {
        EventMetadataResponse eventMetaResponse = new EventMetadataResponse();
        List<InvFulfillmentDetails> fmds = new ArrayList<InvFulfillmentDetails>();
        InvFulfillmentDetails ifd = new InvFulfillmentDetails();
        ifd.setEarliestInHandDate(null);
        ifd.setLatestInHandDate(null);
        ifd.setId(10L);
        fmds.add(ifd);
        eventMetaResponse.setFulfillmentMethods(fmds);
        return eventMetaResponse;
    }

    @Test
    void test_get_fulfillment_specification() throws Exception {
        FulfillmentSpecificationRequest fulfillmentSpecificationRequest = new FulfillmentSpecificationRequest();
        fulfillmentSpecificationRequest.setListings("1234,2345");
        fulfillmentSpecificationRequest.setWithUserDetails(1L);
        fulfillmentSpecificationRequest.setWithSeatDetails(1L);
        QueryResponse qr = new QueryResponse();
        NamedList responseList = new NamedList();
        responseList.add("response", mockInventorySolrDocumentList());
        qr.setResponse(responseList);
//        Mockito.when(qr.getResults()).thenReturn(mockInventorySolrDocumentList());
//        Mockito.when(accountBiz.getFulfillmentSpecs(Mockito.anyString())).thenReturn(qr);
        UserContact uc = new UserContact();
        uc.setCity("SH");
        Mockito.when(userContactBiz.getUserContactById(Mockito.anyLong())).thenReturn(uc);
        List<TicketSeat> tsList = new ArrayList<TicketSeat>();
        TicketSeat ts = new TicketSeat();
        ts.setRowNumber("123");
        ts.setGeneralAdmissionInd(Boolean.TRUE);
        tsList.add(ts);

        setBeanProperty(accountService, "accountSolrCloudBiz", accountSolrCloudBiz);
        Mockito.when(accountSolrCloudBiz.getByIdList(Mockito.anyString(), anyString(), anyList()))
                .thenReturn(null != qr ? covertQueryResponse(qr) : null);
        Mockito.when(listingBO.getTicketSeatInfo(Mockito.anyLong())).thenReturn(tsList);
        FulfillmentSpecificationResponse fr = accountService
                .getFulfillmentSpecification(fulfillmentSpecificationRequest);
        Assert.assertNotNull(fr);
    }

    @Test
    void test_get_fulfillment_specification2() throws Exception {
        FulfillmentSpecificationRequest fulfillmentSpecificationRequest = new FulfillmentSpecificationRequest();
        fulfillmentSpecificationRequest.setListings("1234,2345");
        fulfillmentSpecificationRequest.setWithUserDetails(1L);
        fulfillmentSpecificationRequest.setWithSeatDetails(1L);
        QueryResponse qr = new QueryResponse();
        NamedList responseList = new NamedList();
        responseList.add("response", mockInventorySolrDocumentList2());
        qr.setResponse(responseList);
//        Mockito.when(accountBiz.getFulfillmentSpecs(Mockito.anyString())).thenReturn(qr);
        UserContact uc = new UserContact();
        uc.setCity("SH");
        Mockito.when(userContactBiz.getUserContactById(Mockito.anyLong())).thenReturn(uc);
        List<TicketSeat> tsList = new ArrayList<TicketSeat>();
        TicketSeat ts = new TicketSeat();
        ts.setRowNumber("123");
        ts.setGeneralAdmissionInd(Boolean.TRUE);
        tsList.add(ts);
        setBeanProperty(accountService, "accountSolrCloudBiz", accountSolrCloudBiz);
        Mockito.when(accountSolrCloudBiz.getByIdList(Mockito.anyString(), anyString(), anyList()))
                .thenReturn(null != qr ? covertQueryResponse(qr) : null);
        Mockito.when(listingBO.getTicketSeatInfo(Mockito.anyLong())).thenReturn(tsList);
        FulfillmentSpecificationResponse fr = accountService
                .getFulfillmentSpecification(fulfillmentSpecificationRequest);
        Assert.assertNotNull(fr);
    }

    @Test(enabled = false)
    void test_get_fulfillment_specification_integ() throws Exception {
        if (System.getProperty("os.name").startsWith("Windows")) {
            System.setProperty("NAS_PROPERTY_HOME", "X:/etc/stubhub/properties");
        } else {
            System.setProperty("NAS_PROPERTY_HOME", "/etc/stubhub/properties");
        }
        AccountService accountService = new AccountServiceImpl();
        MasterStubHubProperties.loadSystemProperties();
        MasterStubHubProperties.setProperty("pro.mci.inventory.solr.url",
                "http://api-int.slcq007.com/solr/inventory");
//        AccountBizImpl accountBiz = new AccountBizImpl();
//        InventorySolrUtil inventorySolrUtil = new InventorySolrUtil();
//        setBeanProperty(accountBiz, "inventorySolrUtil", inventorySolrUtil);
//        setBeanProperty(accountService, "accountBiz", accountBiz);
        FulfillmentSpecificationRequest fulfillmentSpecificationRequest = new FulfillmentSpecificationRequest();
        fulfillmentSpecificationRequest.setListings("530375833,1154885771");
        FulfillmentSpecificationResponse fulfillmentSpecificationResponse = accountService
                .getFulfillmentSpecification(fulfillmentSpecificationRequest);
    }

    @Test
    void test_deliverySpecification() {
        DeliverySpecificationRequest deliverySpecificationRequest = new DeliverySpecificationRequest();
        deliverySpecificationRequest.setOrders("123,234");
        deliverySpecificationRequest.setWithUserDetails(1L);
        deliverySpecificationRequest.setWithSeatDetails(1L);
        QueryResponse qr = Mockito.mock(QueryResponse.class);
        Mockito.when(qr.getResults()).thenReturn(mockOrderSolrDocumentList());
//        Mockito.when(accountBiz.getDeliverySpecs(Mockito.anyString())).thenReturn(qr);
        UserContact uc = new UserContact();
        uc.setCity("SH");
        Mockito.when(userContactBiz.getUserContactById(Mockito.anyLong())).thenReturn(uc);
        List<StubTransDetail> tsList = new ArrayList<StubTransDetail>();
        StubTransDetail ts = new StubTransDetail();
        ts.setRowNumber("123");
        ts.setGeneralAdmissionIndicator(1L);
        tsList.add(ts);
        Mockito.when(stubTransDetailDAO.getSeatDetails(Mockito.anyLong())).thenReturn(tsList);
        DeliverySpecificationResponse fr = accountService
                .getDeliverySpecification(deliverySpecificationRequest);
        Assert.assertNotNull(fr);
    }

    @Test
    void test_deliverySpecification2() {
        DeliverySpecificationRequest deliverySpecificationRequest = new DeliverySpecificationRequest();
        deliverySpecificationRequest.setOrders("123,234");
        deliverySpecificationRequest.setWithUserDetails(1L);
        deliverySpecificationRequest.setWithSeatDetails(1L);
        QueryResponse qr = Mockito.mock(QueryResponse.class);
        Mockito.when(qr.getResults()).thenReturn(mockOrderSolrDocumentList2());
//        Mockito.when(accountBiz.getDeliverySpecs(Mockito.anyString())).thenReturn(qr);
        UserContact uc = new UserContact();
        uc.setCity("SH");
        Mockito.when(userContactBiz.getUserContactById(Mockito.anyLong())).thenReturn(uc);
        List<StubTransDetail> tsList = new ArrayList<StubTransDetail>();
        StubTransDetail ts = new StubTransDetail();
        ts.setRowNumber("123");
        ts.setGeneralAdmissionIndicator(1L);
        tsList.add(ts);
        Mockito.when(stubTransDetailDAO.getSeatDetails(Mockito.anyLong())).thenReturn(tsList);
        DeliverySpecificationResponse fr = accountService
                .getDeliverySpecification(deliverySpecificationRequest);
        Assert.assertNotNull(fr);
    }

    /**
     * "INHAND_DATE" -> "2015-07-06T04:00:00Z" "ORDER_PROC_SUB_STATUS_CODE" -> "43" "TID" ->
     * "181204510" "BUYER_SHIPPING_CONTACT_ID" -> "236056900" "EVENT_ID" -> "9178348" "TICKET_ID" ->
     * "1154622152" "TICKET_MEDIUM_ID" -> "2" "CANCELLED" -> "0" "EVENT_DATE" -> "Sat Jul 11
     * 07:00:00 CST 2015" "EVENT_DATE_LOCAL" -> "2015-07-10T19:00:00Z"
     *
     * @return
     */
    private SolrDocumentList mockOrderSolrDocumentList() {
        SolrDocumentList solrDocuments = new SolrDocumentList();
        SolrDocument sd = new SolrDocument();
        sd.setField("INHAND_DATE", "2015-07-06T04:00:00Z");
        sd.setField("ORDER_PROC_SUB_STATUS_CODE", "43");
        sd.setField("TID", "181204510");
        sd.setField("SELLER_ID", "181204510");
        sd.setField("DELIVERY_OPTION_ID", "1");
        sd.setField("FULFILLMENT_METHOD_ID", "1");
        sd.setField("DELIVERY_METHOD_ID", "1");
        sd.setField("BUYER_ID", "181204510");
        sd.setField("BUYER_SHIPPING_CONTACT_ID", "236056900");
        sd.setField("EVENT_ID", "9178348");
        sd.setField("TICKET_ID", "1154622152");
        sd.setField("TICKET_MEDIUM_ID", "2");
        sd.setField("CANCELLED", "0");
        sd.setField("DELIVERY_OPTION_ID", "2");
        sd.setField("EVENT_DATE", new Date());
        sd.setField("EVENT_DATE_LOCAL", "2015-09-09T19:30:00Z");
        solrDocuments.add(sd);
        return solrDocuments;
    }

    private SolrDocumentList mockOrderSolrDocumentList2() {
        SolrDocumentList solrDocuments = new SolrDocumentList();
        SolrDocument sd = new SolrDocument();
        sd.setField("INHAND_DATE", "2015-07-06T04:00:00Z");
        sd.setField("ORDER_PROC_SUB_STATUS_CODE", "43");
        sd.setField("TID", "181204510");
        sd.setField("BUYER_SHIPPING_CONTACT_ID", "236056900");
        sd.setField("EVENT_ID", "9178348");
        sd.setField("TICKET_ID", "1154622152");
        sd.setField("TICKET_MEDIUM_ID", "2");
        sd.setField("CANCELLED", "0");
        sd.setField("DELIVERY_OPTION_ID", "2");
        sd.setField("EVENT_DATE", new Date());
        sd.setField("EVENT_DATE_LOCAL", "2015-09-09T19:30:00Z");
        solrDocuments.add(sd);
        return solrDocuments;
    }

    /**
     * "TICKET_ID" -> "530375833" "QUANTITY_REMAIN" -> "5" "SPLIT" -> "1" "TICKET_SYSTEM_STATUS" ->
     * "ACTIVE" "TICKET_MEDIUM" -> "1" "SALE_END_DATE" -> "Fri Sep 04 03:00:00 CST 2015"
     * "EXPECTED_INHAND_DATE" -> "Wed Sep 02 14:00:00 CST 2015" "LISTING_SOURCE_ID" -> "7"
     * "DELIVERY_OPTION_ID" -> "2" "EVENT_ID" -> "4417008" "EVENT_DATE" -> "Thu Sep 10 09:30:00 CST
     * 2015" "EVENT_DATE_LOCAL" -> "2015-09-09T19:30:00Z" size = 12
     *
     * @return
     */
    private SolrDocumentList mockInventorySolrDocumentList() {
        SolrDocumentList solrDocuments = new SolrDocumentList();
        SolrDocument sd = new SolrDocument();
        sd.setField("TICKET_ID", "530375833");
        sd.setField("SELLER_ID", "1234");
        sd.setField("QUANTITY_REMAIN", "5");
        sd.setField("SPLIT", "1");
        sd.setField("TICKET_SYSTEM_STATUS", "ACTIVE");
        sd.setField("TICKET_MEDIUM", "1");
        sd.setField("SALE_END_DATE", new Date());
        sd.setField("EXPECTED_INHAND_DATE", new Date());
        sd.setField("LISTING_SOURCE_ID", "7");
        sd.setField("DELIVERY_OPTION_ID", "2");
        sd.setField("EVENT_ID", "4417008");
        sd.setField("COUNTRY", "GB");
        sd.setField("EVENT_DATE", new Date());
        sd.setField("EVENT_DATE_LOCAL", "2015-09-09T19:30:00Z");
        sd.setField("SELLER_CONTACT_ID", 1111L);
        sd.setField("LMS_APPROVAL_STATUS_ID", "1");
        solrDocuments.add(sd);
        return solrDocuments;
    }

    private SolrDocumentList mockInventorySolrDocumentList2() {
        SolrDocumentList solrDocuments = new SolrDocumentList();
        SolrDocument sd = new SolrDocument();
        sd.setField("TICKET_ID", "530375833");
        // sd.setField("SELLER_ID","1234");

        sd.setField("QUANTITY_REMAIN", "5");
        sd.setField("SPLIT", "1");
        sd.setField("TICKET_SYSTEM_STATUS", "ACTIVE");
        sd.setField("TICKET_MEDIUM", "1");
        sd.setField("SALE_END_DATE", new Date());
        sd.setField("EXPECTED_INHAND_DATE", new Date());
        sd.setField("LISTING_SOURCE_ID", "7");
        sd.setField("DELIVERY_OPTION_ID", "2");
        sd.setField("EVENT_ID", "4417008");
        sd.setField("COUNTRY", "GB");
        sd.setField("EVENT_DATE", new Date());
        sd.setField("EVENT_DATE_LOCAL", "2015-09-09T19:30:00Z");
        sd.setField("SELLER_CONTACT_ID", 1111L);
        sd.setField("LMS_APPROVAL_STATUS_ID", "1");
        solrDocuments.add(sd);
        return solrDocuments;
    }

    @Test
    public void testConvertBizSaleResponseToWebOrderEntities() throws Exception {
        AccountResponseAdapter ara = new AccountResponseAdapter();
        List<SalesTrans> salesTransLst = new ArrayList<SalesTrans>();

        String buyerId = "1234";
        Long saleId = 11111l;
        String listingId = "1111";
        Long quantityPurchased = 1l;
        String section = "1";
        String row = "1";
        Boolean cancelled = false;
        Long buyerContactId = 1l;
        Calendar saleDateUTC = Mockito.mock(Calendar.class);
        Boolean subbedFlag = false;
        String subbedOrderId = "1";
        String eventId = "1";
        Calendar eventDateUTC = Mockito.mock(Calendar.class);
        Long deliveryMethodId = 1l;
        Calendar expectedArrival = Mockito.mock(Calendar.class);
        Calendar inHandDateUTC = Mockito.mock(Calendar.class);
        Calendar shipDateUTC = Mockito.mock(Calendar.class);
        String trackingNumber = "1";
        String saleProcSubStatus = "1";
        String currency = "USD";

        BigDecimal amount = new BigDecimal("1");

        SalesTrans salesTrans = new SalesTrans();
        salesTrans.setBuyerId(buyerId);
        salesTrans.setSaleId(saleId);
        salesTrans.setListingId(listingId);
        salesTrans.setQuantityPurchased(quantityPurchased);
        salesTrans.setSection(section);
        salesTrans.setRow(row);
        salesTrans.setCancelled(cancelled);
        salesTrans.setBuyerContactId(buyerContactId);
        salesTrans.setSaleDateUTC(saleDateUTC);
        salesTrans.setSubbedFlag(subbedFlag);
        salesTrans.setSubbedOrderId(subbedOrderId);
        salesTrans.setEventId(eventId);
        salesTrans.setEventDateUTC(eventDateUTC);
        salesTrans.setDeliveryMethodId(deliveryMethodId);
        salesTrans.setExpectedArrivalDateUTC(expectedArrival);
        salesTrans.setInHandDateUTC(inHandDateUTC);
        salesTrans.setShipDateUTC(shipDateUTC);
        salesTrans.setTrackingNumber(trackingNumber);
        salesTrans.setSaleProcSubStatusCode(saleProcSubStatus);
        salesTrans.setCurrency(currency);

        com.stubhub.newplatform.common.entity.Money money = new com.stubhub.newplatform.common.entity.Money();
        money.setAmount(amount);
        money.setCurrency(currency);

        salesTrans.setTotalCost(money);
        salesTrans.setBuyVAT(money);
        salesTrans.setSellVAT(money);
        salesTrans.setShippingFeeCost(money);
        salesTrans.setDiscountCost(money);
        salesTrans.setTicketCost(money);

        salesTransLst.add(salesTrans);

        List<CSSaleDetailsResponse> response = ara
                .convertBizSaleResponseToWebOrderEntities(salesTransLst, UserType.BUYER);
        assertNotNull(response);

        salesTrans.setTotalCost(null);
        salesTrans.setBuyVAT(null);
        salesTrans.setSellVAT(null);
        salesTrans.setShippingFeeCost(null);
        salesTrans.setDiscountCost(null);
        salesTrans.setTicketCost(null);

        List<CSSaleDetailsResponse> response3 = ara
                .convertBizSaleResponseToWebOrderEntities(salesTransLst, UserType.BUYER);
        assertNotNull(response3);

        List<CSSaleDetailsResponse> response2 = ara
                .convertBizSaleResponseToWebOrderEntities(salesTransLst, UserType.SELLER);
        assertNotNull(response2);
    }

    @Test
    public void testGetCSSaleDetails() throws Exception {
        AccountServiceImpl accountService = new AccountServiceImpl();
        SHServiceContext serviceContext = new SHServiceContext();
        serviceContext.setAttribute(SHServiceContext.ATTR_ROLE, "R1");
        SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
        setBeanProperty(accountService, "securityContextUtil", securityContextUtil);
        CSSaleRequestValidator requestValidator = mock(CSSaleRequestValidator.class);
        setBeanProperty(accountService, "csSaleRequestValidator", requestValidator);
        SalesTransBO saleTransBO = mock(SalesTransBO.class);
        setBeanProperty(accountService, "saleTransBO", saleTransBO);
        CSStubTransFlagBO csStubTransFlagBO = mock(CSStubTransFlagBO.class);
        setBeanProperty(accountService, "csStubTransFlagBO", csStubTransFlagBO);
        StubTransBO stubTransBO = mock(StubTransBO.class);
        setBeanProperty(accountService, "stubTransBO", stubTransBO);

        AccountResponseAdapter accountResponseAdapter = mock(AccountResponseAdapter.class);

        CSSaleDetailsRequest csSaleDetailsRequest = new CSSaleDetailsRequest();
        csSaleDetailsRequest.setProxiedId("1");
        csSaleDetailsRequest.setRow("1");
        csSaleDetailsRequest.setStart("0");

        csSaleDetailsRequest.setSaleId("100");

        String buyerId = "1234";
        Long saleId = 100l;
        String listingId = "1111";
        Long quantityPurchased = 1l;
        String section = "1";
        String row = "1";
        Boolean cancelled = false;
        Long buyerContactId = 1l;
        Calendar saleDateUTC = Mockito.mock(Calendar.class);
        Boolean subbedFlag = false;
        String subbedOrderId = "1";
        String eventId = "1";
        Calendar eventDateUTC = Mockito.mock(Calendar.class);
        Long deliveryMethodId = 1l;
        Calendar expectedArrival = Mockito.mock(Calendar.class);
        Calendar inHandDateUTC = Mockito.mock(Calendar.class);
        Calendar shipDateUTC = Mockito.mock(Calendar.class);
        String trackingNumber = "1";
        String saleProcSubStatus = "1";
        String currency = "USD";

        BigDecimal amount = new BigDecimal("1");

        SalesTrans salesTrans = new SalesTrans();
        salesTrans.setBuyerId(buyerId);
        salesTrans.setSaleId(saleId);
        salesTrans.setListingId(listingId);
        salesTrans.setQuantityPurchased(quantityPurchased);
        salesTrans.setSection(section);
        salesTrans.setRow(row);
        salesTrans.setCancelled(cancelled);
        salesTrans.setBuyerContactId(buyerContactId);
        salesTrans.setSaleDateUTC(saleDateUTC);
        salesTrans.setSubbedFlag(subbedFlag);
        salesTrans.setSubbedOrderId(subbedOrderId);
        salesTrans.setEventId(eventId);
        salesTrans.setEventDateUTC(eventDateUTC);
        salesTrans.setDeliveryMethodId(deliveryMethodId);
        salesTrans.setExpectedArrivalDateUTC(expectedArrival);
        salesTrans.setInHandDateUTC(inHandDateUTC);
        salesTrans.setShipDateUTC(shipDateUTC);
        salesTrans.setTrackingNumber(trackingNumber);
        salesTrans.setSaleProcSubStatusCode(saleProcSubStatus);
        salesTrans.setCurrency(currency);

        com.stubhub.newplatform.common.entity.Money money = new com.stubhub.newplatform.common.entity.Money();
        money.setAmount(amount);
        money.setCurrency(currency);

        salesTrans.setTotalCost(money);
        salesTrans.setBuyVAT(money);
        salesTrans.setSellVAT(money);
        salesTrans.setShippingFeeCost(money);
        salesTrans.setDiscountCost(money);
        salesTrans.setTicketCost(money);

        List<SalesTrans> salesTransLst = new ArrayList<SalesTrans>();
        salesTransLst.add(salesTrans);

        Map<String, Boolean> csFlag = new HashMap<String, Boolean>();
        csFlag.put("100", true);
        csFlag.put("200", true);

        CSSalesResponse csSalesResponse = new CSSalesResponse();
        List<CSSaleDetailsResponse> listCSSaleDetailsResponse = new ArrayList<CSSaleDetailsResponse>();
        CSSaleDetailsResponse csSaleDetailsResponse = new CSSaleDetailsResponse();
        CSSaleTransactionResponse transactionResponse = new CSSaleTransactionResponse();
        transactionResponse.setSaleId("2");
        transactionResponse.setBuyVAT(money);
        transactionResponse.setSellVAT(money);
        transactionResponse.setShippingFeeCost(money);
        transactionResponse.setDiscountCost(money);
        csSaleDetailsResponse.setTransaction(transactionResponse);
        listCSSaleDetailsResponse.add(csSaleDetailsResponse);
        csSalesResponse.setSale(listCSSaleDetailsResponse);
        csSalesResponse.setSalesFound(1);

        StubTrans st = new StubTrans();
        st.setSeats("AAA");
        st.setEventStatusIdAtConfirm(2l);

        List<Error> errorList = new LinkedList<Error>();
        errorList.add(new Error(ErrorType.INPUTERROR, ErrorCode.INVALID_INPUT, "No input provided",
                null));
        Mockito.when(requestValidator.validateCSSaleDetailsRequestFields(csSaleDetailsRequest))
                .thenReturn(errorList);
        Response response = accountService.getCSSaleDetails(csSaleDetailsRequest, serviceContext,
                securityContext);

        Mockito.when(accountService.getCSSaleDetails(csSaleDetailsRequest, serviceContext,
                securityContext)).thenThrow(new NullPointerException());
        response = accountService.getCSSaleDetails(csSaleDetailsRequest, serviceContext,
                securityContext);
        assertNotNull(response);

        Mockito.when(requestValidator
                .validateCSSaleDetailsRequestFields((CSSaleDetailsRequest) argThat(getMatcher())))
                .thenReturn(null);
        Mockito.when(saleTransBO.getSaleTransById((Long) argThat(getMatcher())))
                .thenReturn(salesTrans);
        Mockito.when(stubTransBO.getStubTransById((Long) argThat(getMatcher()))).thenReturn(st);
        Mockito.when(csStubTransFlagBO.getCSStubTransFlag((List<String>) argThat(getMatcher())))
                .thenReturn(csFlag);
        Mockito.when(
                accountResponseAdapter.addCSStubTransFlag((CSSalesResponse) argThat(getMatcher()),
                        (Map<String, Boolean>) argThat(getMatcher())))
                .thenReturn(csSalesResponse);

        response = accountService.getCSSaleDetails(csSaleDetailsRequest, serviceContext,
                securityContext);
        Assert.assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());

        CSSaleDetailsRequest csSaleDetailsRequest2 = new CSSaleDetailsRequest();
        csSaleDetailsRequest2.setProxiedId("1");
        csSaleDetailsRequest2.setRow("4");
        csSaleDetailsRequest2.setStart("0");
        response = accountService.getCSSaleDetails(csSaleDetailsRequest2, serviceContext,
                securityContext);
        Assert.assertEquals(response.getStatus(), Response.Status.NOT_FOUND.getStatusCode());

        CSSaleDetailsRequest csSaleDetailsRequest3 = new CSSaleDetailsRequest();
        csSaleDetailsRequest3.setEventStartDate("2014-09-09");
        csSaleDetailsRequest3.setEventEndDate("2014-08-09");
        csSaleDetailsRequest3.setRow("4");
        csSaleDetailsRequest3.setStart("0");
        response = accountService.getCSSaleDetails(csSaleDetailsRequest3, serviceContext,
                securityContext);
        Assert.assertEquals(response.getStatus(), Response.Status.NOT_FOUND.getStatusCode());

        Mockito.when(saleTransBO.getSaleTransByEventDate((Calendar) argThat(getMatcher()),
                (Calendar) argThat(getMatcher()), (Integer) argThat(getMatcher()),
                (Integer) argThat(getMatcher()))).thenReturn(salesTransLst);

        CSSaleDetailsRequest csSaleDetailsRequest32 = new CSSaleDetailsRequest();
        csSaleDetailsRequest32.setEventStartDate("2014-09-09");
        csSaleDetailsRequest32.setEventEndDate("2014-08-09");
        csSaleDetailsRequest32.setRow("4");
        csSaleDetailsRequest32.setStart("0");
        response = accountService.getCSSaleDetails(csSaleDetailsRequest32, serviceContext,
                securityContext);
        Assert.assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());

        Mockito.when(saleTransBO.getSaleTransByBuyerId((Long) argThat(getMatcher()),
                (Integer) argThat(getMatcher()), (Integer) argThat(getMatcher())))
                .thenReturn(salesTransLst);

        CSSaleDetailsRequest csSaleDetailsRequest31 = new CSSaleDetailsRequest();
        csSaleDetailsRequest31.setProxiedId("1");
        csSaleDetailsRequest31.setRow("4");
        csSaleDetailsRequest31.setStart("0");
        response = accountService.getCSSaleDetails(csSaleDetailsRequest31, serviceContext,
                securityContext);
        Assert.assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());

        CSSaleDetailsRequest csSaleDetailsRequest4 = new CSSaleDetailsRequest();
        csSaleDetailsRequest4.setProxiedId("1");
        csSaleDetailsRequest4.setEventStartDate("2014-09-09");
        csSaleDetailsRequest4.setEventEndDate("2014-08-09");
        csSaleDetailsRequest4.setRow("4");
        csSaleDetailsRequest4.setStart("0");
        response = accountService.getCSSaleDetails(csSaleDetailsRequest4, serviceContext,
                securityContext);
        Assert.assertEquals(response.getStatus(), Response.Status.NOT_FOUND.getStatusCode());

        Mockito.when(saleTransBO.getSaleTransByBuyerIDAndEventDate((Long) argThat(getMatcher()),
                (Calendar) argThat(getMatcher()), (Calendar) argThat(getMatcher()),
                (Integer) argThat(getMatcher()), (Integer) argThat(getMatcher())))
                .thenReturn(salesTransLst);

        CSSaleDetailsRequest csSaleDetailsRequest41 = new CSSaleDetailsRequest();
        csSaleDetailsRequest41.setProxiedId("1");
        csSaleDetailsRequest41.setEventStartDate("2014-09-09");
        csSaleDetailsRequest41.setEventEndDate("2014-08-09");
        csSaleDetailsRequest41.setRow("4");
        csSaleDetailsRequest41.setStart("0");
        response = accountService.getCSSaleDetails(csSaleDetailsRequest41, serviceContext,
                securityContext);
        Assert.assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());

        SHServiceContext serviceContext1 = new SHServiceContext();
        serviceContext1.setAttribute(SHServiceContext.ATTR_ROLE, null);
        response = accountService.getCSSaleDetails(csSaleDetailsRequest4, serviceContext1,
                securityContext);

        Assert.assertEquals(response.getStatus(), Response.Status.FORBIDDEN.getStatusCode());
    }

    @Test
    public void testGetListingsForSolrCloud() throws Exception {
        AccountServiceImpl listingResource = new AccountServiceImpl();
        AccountSolrCloudBiz accountSolrCloudBiz = mock(AccountSolrCloudBiz.class);
        SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
        setBeanProperty(listingResource, "accountSolrCloudBiz", accountSolrCloudBiz);
        setBeanProperty(listingResource, "securityContextUtil", securityContextUtil);
        ReflectionTestUtils.setField(listingResource, "messageSource", messageSource);
        MasterStubHubProperties.setProperty("account.v1.listing.withoutSeller.useSolrCloud", "true");
        try {
            ListingsResponse listings = listingResource.getListings(securityContext, "234", null, null, null);
            Assert.assertNotNull(listings);
            PaginationInput paginationInput = new PaginationInput();
            paginationInput.setStart(-1);
            paginationInput.setRows(10000);
            listings = listingResource.getListings(securityContext, "234", null, null, paginationInput);
            Assert.assertNotNull(listings);
            paginationInput.setStart(1);
            paginationInput.setRows(1);
            listings = listingResource.getListings(securityContext, "234", null, null, paginationInput);
            Assert.assertNotNull(listings);
            listings = listingResource.getListings(securityContext, null, "", null, paginationInput);
            Assert.assertNotNull(listings);
        } finally {
            MasterStubHubProperties.setProperty("account.v1.listing.withoutSeller.useSolrCloud", "false");
        }
    }

    @Test
    public void testGetEventSalesDataForSolrCloud() throws Exception {
        AccountServiceImpl accountService = new AccountServiceImpl();
        AccountSolrCloudBiz accountSolrCloudBiz = mock(AccountSolrCloudBiz.class);
        SecurityContextUtil securityContextUtil = mock(SecurityContextUtil.class);
        setBeanProperty(accountService, "accountSolrCloudBiz", accountSolrCloudBiz);
        setBeanProperty(accountService, "securityContextUtil", securityContextUtil);
        setBeanProperty(accountService, "sellerEligibilityHelper", sellerEligibilityHelper);
        when(sellerEligibilityHelper.checkSellerEligibility()).thenReturn(false).thenReturn(true);

        MasterStubHubProperties.setProperty("account.v1.sale.event.useSolrCloud", "true");
        try {
            SalesResponse sales = accountService.getEventSalesData(securityContext, "en_US", "1234", null, "", "", "1234", "1,2", "allinprice");
            Assert.assertNotNull(sales);
            PaginationInput paginationInput = new PaginationInput();
            paginationInput.setStart(-1);
            paginationInput.setRows(10000);
            String sectionId = "";
            for (int i = 0; i < 100; i++) {
                sectionId += (i + ",");
            }
            sales = accountService.getEventSalesData(securityContext, "en_US", "1234", paginationInput, null, "", sectionId, "1,2,4", "allinprice");
            Assert.assertNotNull(sales);
            paginationInput.setStart(1);
            paginationInput.setRows(1);
            sales = accountService.getEventSalesData(securityContext, "en_US", "1234", paginationInput, null, "", "1234", "", "allinprice");
            Assert.assertNotNull(sales);
            sales = accountService.getEventSalesData(securityContext, "en_US", "1234", paginationInput, null, "", "1234", "", "allinprice");
            Assert.assertNotNull(sales);
        } finally {
            MasterStubHubProperties.setProperty("account.v1.sale.event.useSolrCloud", "false");
        }
    }


}

