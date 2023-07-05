package com.stubhub.domain.account.intf;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.ext.search.SearchContext;

import com.stubhub.platform.utilities.webservice.security.ExtendedSecurityContext;

@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public interface PaymentsService {

	@GET
	@Path("/seller/{sellerGUID}")
    public SellerPayments getPayments(@PathParam("sellerGUID") String sellerGUID,
            @QueryParam("sort") String sort, @QueryParam("filters") String filters,
            @QueryParam("start") String start, @QueryParam("rows") String rows,
            @QueryParam("includePaymentSummary") String includePaymentSummary,
            @QueryParam("includeCurrencySummary") String includeCurrencySummary,
            @QueryParam("includeCreditMemo") String includeCreditMemo,
            @QueryParam("includeChargeAmtCMAmt") String includeChargeAmtCMAmt,
            @QueryParam("includeRecordType") @DefaultValue(value="false") String includeRecordType,
            @QueryParam("includeCreditMemoHistories") @DefaultValue(value="false") String includeCMAppliedHistories,
            @Context SearchContext searchContext, @Context ExtendedSecurityContext context);

	enum DateUnit {
		DAY, MONTH, YEAR, DAYS, MONTHS, YEARS;
	}

	enum ExportFileType {
		CSV, PDF, TXT;
	}

	@GET
	@Path("/export/seller/{sellerGUID}")
	@Produces({ MediaType.APPLICATION_OCTET_STREAM })
	public Response exportPayments(@PathParam("sellerGUID") String sellerGUID, @QueryParam("fileType") ExportFileType fileType, @QueryParam("fromDateUnits") Integer fromDateUnits, @QueryParam("toDateUnits") Integer toDateUnits, @QueryParam("dateUnit") DateUnit dateUnit, @QueryParam("dateFormat") String dateFormat, @QueryParam("currencyCode") String currencyCode, @Context ExtendedSecurityContext context);

	@POST
	@Path("/statusController")
	public UpdatePaymentStatusResponse updatePaymentStatus(UpdatePaymentStatusRequest request, @Context ExtendedSecurityContext context);

	@GET
	@Path("/pi/{sellerId}")
	public Response evaluatePIRule(@PathParam("sellerId")Long sellerId, @QueryParam("rule") String rule, @Context ExtendedSecurityContext context);
	
	@PUT
	@Path("/{paymentId}")
	public UpdatePaymentResponse updatePayment(@PathParam("paymentId") Long paymentId, UpdatePaymentRequest request, @Context ExtendedSecurityContext context);
}
