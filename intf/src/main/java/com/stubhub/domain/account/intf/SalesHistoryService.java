package com.stubhub.domain.account.intf;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.stubhub.platform.utilities.webservice.security.ExtendedSecurityContext;

@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public interface SalesHistoryService {
	
	/**
	 * 
	 * returns the pricing summary for sold data
	 * @param request
	 * @param securityContext
	 * @return SalesSummaryResponse
	 */
	@GET
	@Path("/summary")
	public SalesHistoryResponse getSalesHistory(@QueryParam("") SalesHistoryRequest request,
												@Context ExtendedSecurityContext securityContext);

}
