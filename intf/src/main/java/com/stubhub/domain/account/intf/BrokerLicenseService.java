package com.stubhub.domain.account.intf;

import com.stubhub.platform.utilities.webservice.security.ExtendedSecurityContext;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by mengli on 11/13/18.
 */
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public interface BrokerLicenseService {
    @GET
    @Path("/seller/{sellerGUID}")
    Response getBrokerLicenses(@PathParam("sellerGUID") String sellerGUID,
                               @Context ExtendedSecurityContext context);

    @POST
    @Path("/seller/{sellerGUID}")
    Response createBrokerLicense(@PathParam("sellerGUID") String sellerGUID,
                                 BrokerLicenseRequest brokerLicenseRequest,
                                 @Context ExtendedSecurityContext context);

    @PUT
    @Path("/seller/{sellerGUID}")
    Response updateBrokerLicense(@PathParam("sellerGUID") String sellerGUID,
                                 BrokerLicenseRequest brokerLicenseRequest,
                                 @Context ExtendedSecurityContext context);

}
