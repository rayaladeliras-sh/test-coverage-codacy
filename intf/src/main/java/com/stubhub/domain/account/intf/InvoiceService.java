package com.stubhub.domain.account.intf;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.stubhub.platform.utilities.webservice.security.ExtendedSecurityContext;

public interface InvoiceService {
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @GET
    @Path("seller/{sellerGuid}/ref/{refNumber}")
    public Response getInvoiceByReferenceNumber(@Context ExtendedSecurityContext context,
                                                    @PathParam("sellerGuid") String sellerGuid,
                                                    @PathParam("refNumber") String refNumber,
                                                    @QueryParam("pid") String pid,
                                       				@HeaderParam("accept-language") String acceptLanguage);

    @Produces({"application/pdf", MediaType.APPLICATION_JSON})
    @GET
   
    @Path("/seller/{sellerGuid}/ref/{refNumber}/pdf")
    public Response getPdfInvoiceByReferenceNumber(@Context ExtendedSecurityContext context,
                                                   @PathParam("sellerGuid") String sellerGuid,
                                                   @PathParam("refNumber") String refNumber,
                                                   @QueryParam("locale") String qLocale,
                                                   @QueryParam("pid") String pid,
                                                   @HeaderParam("Accept-Language")String locale);
}