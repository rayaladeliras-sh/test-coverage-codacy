package com.stubhub.domain.account.impl;

import com.stubhub.common.exception.UserNotAuthorizedException;
import com.stubhub.domain.account.adapter.AllPaymentResponseJsonAdapter;
import com.stubhub.domain.account.biz.impl.SellerPaymentSolrCloudBOImpl;
import com.stubhub.domain.account.biz.intf.AllPaymentsSolrCloudBO;
import com.stubhub.domain.account.common.AllPaymentsSearchCriteria;
import com.stubhub.domain.account.common.Error;
import com.stubhub.domain.account.common.enums.ErrorCode;
import com.stubhub.domain.account.common.enums.ErrorType;
import com.stubhub.domain.account.common.util.SecurityContextUtil;
import com.stubhub.domain.account.intf.AllPaymentResponse;
import com.stubhub.domain.account.intf.AllPaymentsService;
import com.stubhub.domain.infrastructure.common.core.monitor.SHMonitor;
import com.stubhub.domain.infrastructure.common.core.monitor.SHMonitorFactory;
import com.stubhub.platform.utilities.webservice.security.ExtendedSecurityContext;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrServerException;
import org.codehaus.jackson.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static javax.ws.rs.core.Response.Status.*;

@Component("allPaymentsService")
public class AllPaymentsServiceImpl implements AllPaymentsService {
  private final static Log log = LogFactory.getLog(AllPaymentsServiceImpl.class);
  private static final Set<String> validFilters = initValidFilters();
  private static final int maxNumberOfRows = 200;

  @Autowired
  private SecurityContextUtil securityContextUtil;

  @Autowired
  @Qualifier("allPaymentsSolrCloudBO")
  private AllPaymentsSolrCloudBO allPaymentsSolrCloudBO;


  @Override
  public Response getAllPayments(String sellerGUID,
                                 String sort,
                                 String filters,
                                 String status,
                                 String currency,
                                 String start,
                                 String rows,
                                 Boolean includeCurrencySummary,
                                 Boolean includePaymentSummary,
                                 ExtendedSecurityContext context) {
    SHMonitor monitor =  SHMonitorFactory.getMonitor();
    monitor.start();
    AllPaymentResponse allPaymentResponse = new AllPaymentResponse();
    try {
      securityContextUtil.validateUserGuid(context, sellerGUID);
      if (StringUtils.isNotEmpty(filters)){
        validateFiltersFormat(filters);
        validateFilters(filters);
      }

      String sellerId = context.getUserId();
      AllPaymentsSearchCriteria criteria = new AllPaymentsSearchCriteria();
      criteria.setSellerId(sellerId);
      criteria.setIncludePaymentSummary(includePaymentSummary);
      criteria.setIncludeCurrencySummary(includeCurrencySummary);
      criteria.setSort(sort);
      criteria.setStatus(status);
      criteria.setQ(filters);
      criteria.setCurrency(currency);

      if (start != null) {
        criteria.setStart(Integer.parseInt(start));
      }
      if (rows != null) {
        int parsedRows = Integer.parseInt(rows);
        if (parsedRows > maxNumberOfRows) {
          parsedRows = maxNumberOfRows;
        }
        criteria.setRows(parsedRows);
      } else {
        criteria.setRows(maxNumberOfRows);
      }

      JsonNode response = allPaymentsSolrCloudBO.getAllPayments(criteria);

      allPaymentResponse = AllPaymentResponseJsonAdapter.convertJsonNodeToResponse(response, currency);

      return Response.status(OK).entity(allPaymentResponse).build();
    } catch (UserNotAuthorizedException e) {
      allPaymentResponse.setErrors(new ArrayList<Error>());
      allPaymentResponse.getErrors().add(
              new Error(ErrorType.AUTHENTICATIONERROR, ErrorCode.INVALID_SELLER, "Invalid Seller", "sellerGuid"));
      log.error(
              "api_domain=account api_resource=allPayments api_method=getAllPayments status=error error_message=authorization error"
                      + " sellerGuid=" + sellerGUID, e);
      return Response.status(UNAUTHORIZED).entity(allPaymentResponse).build();
    } catch (SolrServerException e) {
      allPaymentResponse.setErrors(new ArrayList<Error>());
      allPaymentResponse.getErrors().add(
              new Error(ErrorType.SYSTEMERROR, ErrorCode.SYSTEM_ERROR, "Unexpected error occurred", ""));
      log.error(
              "api_domain=account api_resource=allPayments api_method=getAllPayments status=error error_message=SolrServerException"
                      + " sellerGuid=" + sellerGUID, e);
      return Response.status(INTERNAL_SERVER_ERROR).entity(allPaymentResponse).build();
    } catch (IllegalArgumentException e) {
      allPaymentResponse.setErrors(new ArrayList<Error>());
      allPaymentResponse.getErrors().add(
              new Error(ErrorType.SYSTEMERROR, ErrorCode.SYSTEM_ERROR, "Problems with the filters", ""));
      log.error(
              "api_domain=account api_resource=allPayments api_method=getAllPayments status=error error_message=Problems with the filters"
                      + " sellerGuid=" + sellerGUID + " filters=" + filters, e);
      return Response.status(INTERNAL_SERVER_ERROR).entity(allPaymentResponse).build();
    } finally {
      log.info("api_domain=account api_resource=allPayments api_method=getAllPayments execute time" + monitor.getTime());
    }
  }

  private  void validateFiltersFormat(String filters){
    if(StringUtils.isNotBlank(filters)){
      String pattern = "^([^,:]+:[^,:]+,)*[^,:]+:[^,:]+$";
      Pattern r = Pattern.compile(pattern);
      Matcher m = r.matcher(filters);
      if(!m.find()){
        throw new IllegalArgumentException(
                "The valid format of filters should be like 'a:b,c:d'");
      }
    }
  }

  private static Set<String> initValidFilters() {
    Set<String> filters = new HashSet<String>();
    /*
    TODO: We need to add the valid filters fields
     */
    return Collections.unmodifiableSet(filters);
  }

  private void validateFilters(String filters) {
    if (StringUtils.isNotBlank(filters)) {
      String filtersArray[] = filters.split(",");
      for (String filter : filtersArray) {
        String params[] = filter.split(":");
        if (!validFilters.contains(params[0].trim())) {
          throw new IllegalArgumentException(
                  "Invalid filter parameter input. Filter name is:" + params[0]);

        }
      }
    }
  }
}
