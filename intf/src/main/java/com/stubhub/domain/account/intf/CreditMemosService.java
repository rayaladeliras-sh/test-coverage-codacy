package com.stubhub.domain.account.intf;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.stubhub.platform.utilities.webservice.security.ExtendedSecurityContext;

@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public interface CreditMemosService {

    /**
     * This method returns all the credit memos for a given seller.
     */
    @GET
    @Path("/seller/{sellerGuId}")
    public CreditMemosResponse getCreditMemos(@Context ExtendedSecurityContext securityContext,
                                              @PathParam("sellerGuId") String sellerGuid,
                                              @QueryParam("sort") String sortType,
                                              @QueryParam("createdFromDate") String createdFromDate,
                                              @QueryParam("createdToDate") String createdToDate,
                                              @QueryParam("start") Integer start,
                                              @QueryParam("rows") Integer rows,
                                              @QueryParam("currencyCode") String currencyCode);
    
    

}