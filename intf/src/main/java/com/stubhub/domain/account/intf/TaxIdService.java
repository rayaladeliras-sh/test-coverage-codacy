package com.stubhub.domain.account.intf;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.stubhub.platform.utilities.webservice.security.ExtendedSecurityContext;

@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public interface TaxIdService {

  @GET
  @Path("/seller/{sellerGuid}")
  TaxPayerAlertStatusResponse getTaxPayerAlertStatus(@PathParam("sellerGuid") String sellerGuid,
                                                     @Context ExtendedSecurityContext context);

  @GET
  @Path("/seller/shouldshow")
  TaxIdShouldShowResponse shouldShowTaxId(@QueryParam("validateNotExist") @DefaultValue("false") Boolean validateNotExist,
                                          @Context ExtendedSecurityContext context);

  @POST
  @Path("/seller/")
  Response addTaxId(TaxIdRequest taxIdRequest,
                    @Context ExtendedSecurityContext context);

  @GET
  @Path("/seller")
  Response getTaxId(@Context ExtendedSecurityContext context);

  @PUT
  @Path("/seller")
  Response updateTaxId(TaxIdRequest taxIdRequest, @Context ExtendedSecurityContext context);

}