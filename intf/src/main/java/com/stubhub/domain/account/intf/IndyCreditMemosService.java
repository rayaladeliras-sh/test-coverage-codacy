package com.stubhub.domain.account.intf;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.stubhub.domain.infrastructure.soa.core.context.SHServiceContext;
import com.stubhub.platform.utilities.webservice.security.ExtendedSecurityContext;

@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})

public interface IndyCreditMemosService {

  
    
    /**
     * This method returns all the credit memos for a given seller.
     */
    @GET
    @Path("/creditmemos/v1")
    public CreditMemosResponse getIndySellersCreditMemos(@Context SHServiceContext serviceContext,@Context ExtendedSecurityContext securityContext, 
            @QueryParam("createdFromDate") String createdFromDate,
            @QueryParam("createdToDate") String createdToDate
         );
}