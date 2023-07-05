package com.stubhub.domain.account.intf;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.stubhub.platform.utilities.webservice.security.ExtendedSecurityContext;

@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public interface CreditCardChargesService {
	
	/**
	 * This method returns all the credit card charges for a given seller.
	 * 
	 * @param sellerId
	 * @param sortType
	 * @param filters
	 * @return CreditCardChargesResponse
	 */
	@GET
	@Path("/seller/{sellerGUID}")
	public CreditCardChargesResponse getCreditCardCharges(@Context ExtendedSecurityContext securityContext,
														  @PathParam("sellerGUID") String sellerGUID,
														  @QueryParam("sort") String sort,
														  @QueryParam("fromDate")String fromDate,
														  @QueryParam("toDate")String toDate,
														  @QueryParam("start") Integer start,
														  @QueryParam("rows") Integer rows,
														  @QueryParam("transactionType") String transactionType,
														  @QueryParam("currencyCode") String currencyCode);

}
