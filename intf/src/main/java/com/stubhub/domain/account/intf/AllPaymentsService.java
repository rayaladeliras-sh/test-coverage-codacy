package com.stubhub.domain.account.intf;

import com.stubhub.platform.utilities.webservice.security.ExtendedSecurityContext;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public interface AllPaymentsService {
  @GET
  @Path("/{sellerGUID}")
  Response getAllPayments(@PathParam("sellerGUID") String sellerGUID,
                          @QueryParam("sort") String sort,
                          @QueryParam("filters") String filters,
                          @QueryParam("status") String status,
                          @QueryParam("currency") String currency,
                          @QueryParam("start") String start,
                          @QueryParam("rows") String rows,
                          @QueryParam("includeCurrencySummary") @DefaultValue("false") Boolean includeCurrencySummary,
                          @QueryParam("includePaymentSummary") @DefaultValue("false") Boolean includePaymentSummary,
                          @Context ExtendedSecurityContext context);

}
