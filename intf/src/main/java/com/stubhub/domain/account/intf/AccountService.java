package com.stubhub.domain.account.intf;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.stubhub.domain.account.common.ListingInput;
import com.stubhub.domain.account.common.PaginationInput;
import com.stubhub.domain.account.common.SummaryInput;
import com.stubhub.domain.i18n.infra.soa.core.I18nServiceContext;
import com.stubhub.domain.infrastructure.soa.core.context.SHServiceContext;
import com.stubhub.platform.utilities.webservice.security.ExtendedSecurityContext;

@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public interface AccountService {

	@GET
	@Path("/listings/v1/{listingId}")
	public ListingResponse getListing(@PathParam("listingId") String listingId,
									  @QueryParam("includeFees") String includeFees,
									  @Context ExtendedSecurityContext securityContext);

	/**
	 * This method returns all the listings for a given seller.
	 */
	@GET
	@Path("/listings/v1/seller/{sellerGuId}")
	public ListingsResponse getMyListings(@Context SHServiceContext serviceContext,
										  @Context ExtendedSecurityContext securityContext,
										  @Context I18nServiceContext i18nContext,
										  @PathParam("sellerGuId") String sellerGuid,
										  @QueryParam("") PaginationInput paginationInput,
										  @QueryParam("sort") String sortType,
										  @QueryParam("filters")String listingsRequest,
										  @QueryParam("pricingRec") String pricingRec,
										  @HeaderParam("Accept-Language")String locale,
										  @QueryParam("") SummaryInput summaryInput,
										  @QueryParam("") ListingInput listingInput,
										  @QueryParam("mockScores") String mockScores);

	/**
	 * This method returns all the listings for a given seller.
	 * The internal also returns sensitive information, like valueScore.
	 */
	@GET
	@Path("/listings/v1/seller/internal/{sellerGuId}")
	public ListingsResponse getMyListingsInternal(@Context SHServiceContext serviceContext,
										  @Context ExtendedSecurityContext securityContext,
										  @Context I18nServiceContext i18nContext,
										  @PathParam("sellerGuId") String sellerGuid,
										  @QueryParam("") PaginationInput paginationInput,
										  @QueryParam("sort") String sortType,
										  @QueryParam("filters")String listingsRequest,
										  @QueryParam("pricingRec") String pricingRec,
										  @HeaderParam("Accept-Language")String locale,
										  @QueryParam("") SummaryInput summaryInput,
										  @QueryParam("") ListingInput listingInput,
										  @QueryParam("mockScores") String mockScores);

	@GET
	@Path("/listings/v1/")
	public ListingsResponse getListings(@Context ExtendedSecurityContext securityContext,
										@QueryParam("sellerIds") String sellerIds,
										@QueryParam("eventIds") String eventIds,
										@QueryParam("status")String status ,
										@QueryParam("") PaginationInput paginationInput);

	@GET
	@Path("/orders/v1/buyer/{buyerGUID}")
	public MyOrderListResponse getMyOrders(@Context ExtendedSecurityContext securityContext,
										@PathParam("buyerGUID") String buyerGUID,
										@QueryParam("sort") String[] sortType,
										@QueryParam("filters")String filter ,
										@QueryParam("") PaginationInput paginationInput, @HeaderParam("Accept-Language")String locale);

	/**
	 * This method returns all the orders for a given seller.
	 *
	 * @param paginationInput
	 * @return
	 */
	@GET
	@Path("/sales/v1/seller/{sellerGuId}")
	public SalesResponse getMySales(@Context SHServiceContext serviceContext,
			@Context ExtendedSecurityContext securityContext,
			@Context I18nServiceContext i18nContext,
			@PathParam("sellerGuId") String sellerGuid,
			@QueryParam("") PaginationInput paginationInput,
			@QueryParam("sort") String sortType,
			@QueryParam("filters")String salesRequest,
			@QueryParam("includePending")String includePending,@QueryParam("includeSeatDetail")String includeSeatDetail,
									@HeaderParam("Accept-Language")String locale, @QueryParam("") SummaryInput summaryInput,
			@QueryParam("noConfirmBtn") String noConfirmBtn);


	@POST
	@Path("/sales/v1/{saleId}/exception")
	public Response reportAnIssue(@Context ExtendedSecurityContext securityContext,
			@PathParam("saleId") String saleId, ReportAnIssueRequest request);

	@GET
	@Path("/orders/v1/{orderId}/orderStatus")
	public OrderStatusResponse getOrderStatus(@PathParam("orderId")String orderId,
											  @Context ExtendedSecurityContext securityContext);

	/**
	 * This method returns all the discounts used for an order.
	 *
	 * @param orderId
	 * @return
	 */
	@GET
	@Path("/orders/v1/{orderId}/discounts")
	public DiscountsResponse getDiscount(@PathParam("orderId")String orderId,
											  @Context ExtendedSecurityContext securityContext);

	/**
	 * This method updates the buyer contact id used for an order.
	 *
	 * @param orderId
	 * @return
	 */
	@POST
	@Path("/orders/v1/{orderId}/buyerContactId")
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response updateBuyerContactId(@PathParam("orderId")String orderId,
			 							 BuyerContactRequest buyerContactRequest,
										 @Context ExtendedSecurityContext securityContext);

	@POST
	@Path("/summary/v1/")
	public Response getTransactionSummary(TransactionSummaryRequest summaryRequest, @Context SHServiceContext serviceContext,
												@Context ExtendedSecurityContext securityContext);

	@POST
	@Path("/summary/v1/user")
	public Response getUserTransactionSummary(TransactionSummaryRequest summaryRequest, @Context SHServiceContext serviceContext,
										  @Context ExtendedSecurityContext securityContext);

	/*
	 *  This method gets the order information which CS tools need.
	 *  We used POST because request contains PII which we don't want to expose.
	 */
	@POST
	@Path("/csorderdetails/v1/")
	public Response getCSOrderDetails(CSOrderDetailsRequest csOrderDetailsRequest,
											@Context ExtendedSecurityContext securityContext);

	@PUT
	@Path("/csorderdetails/v1/")
	public Response updateCSOrderDetails(OrdersResponse request,
										@Context SHServiceContext serviceContext,
										@Context ExtendedSecurityContext securityContext);

	@POST
	@Path("/cssales/v1/")
	public Response getCSSales(CSOrderDetailsRequest csOrderDetailsRequest,
									@Context ExtendedSecurityContext securityContext);


	/**
	 * This method returns all the sales for a given Event. User authenticated
	 *
	 * @param eventId
	 * @param paginationInput
	 * @return
	 */
	@GET
	@Path("/sales/v1/event/{eventId}")
	public SalesResponse getEventSalesData(@Context ExtendedSecurityContext securityContext,
			@HeaderParam("accept-language") String localeStr,
			@PathParam("eventId") String eventId,
			@QueryParam("") PaginationInput paginationInput,
			@QueryParam("sort") String sortType,
			@QueryParam("filters")String salesRequest,
			@QueryParam("sectionId")String sectionId,
			@QueryParam("zoneId")String zoneId,
			@QueryParam("priceType") String priceType);

	/**
	 * This method returns all the sales for a given Event. User authenticated
	 *
	 * @param eventId
	 * @param paginationInput
	 * @return
	 */
	@GET
	@Path("/sales/v1/event/internal/{eventId}")
	public SalesResponse getEventSalesDataInternal(@Context ExtendedSecurityContext securityContext,
										   @HeaderParam("accept-language") String localeStr,
										   @PathParam("eventId") String eventId,
										   @QueryParam("") PaginationInput paginationInput,
										   @QueryParam("sort") String sortType,
										   @QueryParam("filters")String salesRequest,
										   @QueryParam("sectionId")String sectionId,
										   @QueryParam("zoneId")String zoneId,
										   @QueryParam("priceType") String priceType,
										   @QueryParam("mockScores") String mockScores,
										   @QueryParam("quantity") String hasQuantity);

	/**
	 *
	 * @param searchCriteria
	 * @param serviceContext
	 * @param securityContext
	 * @return
	 */
	@GET
	@Path("/emails/v1/")
	public Response getEmailLogs(@QueryParam("") SearchEmailCriteria searchCriteria,
			@Context SHServiceContext serviceContext,
			@Context ExtendedSecurityContext securityContext);


	/**
	 *
	 * @param emailId
	 * @param serviceContext
	 * @param securityContext
	 * @return
	 */
	@GET
	@Path("/emails/v1/{emailId}")
	public Response getEmail(@PathParam("emailId")String emailId,
			@Context SHServiceContext serviceContext,
			@Context ExtendedSecurityContext securityContext);

	/**
	 *
	 * @param request
	 * @param orderId
	 * @param serviceContext
	 * @param securityContext
	 * @return
	 */
	@PUT
	@Path("/orders/v1/{orderId}/substitutions/")
	public Response createSubstitutionOrder(SubstitutionRequest request,
			@PathParam("orderId") String orderId,
			@Context SHServiceContext serviceContext,
			@Context ExtendedSecurityContext securityContext);

    @POST
    @Path("/fulfillmentspecification/v1/")
    public FulfillmentSpecificationResponse getFulfillmentSpecification(FulfillmentSpecificationRequest fulfillmentSpecificationRequest);

    @POST
    @Path("/deliveryspecification/v1/")
    public DeliverySpecificationResponse getDeliverySpecification(DeliverySpecificationRequest deliverySpecificationRequest);


    @POST
	@Path("/cssaledetails/v1/")
	public Response getCSSaleDetails(CSSaleDetailsRequest csSaleDetailsRequest,
			@Context SHServiceContext serviceContext, @Context ExtendedSecurityContext securityContext);
}
