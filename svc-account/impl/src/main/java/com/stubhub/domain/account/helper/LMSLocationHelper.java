package com.stubhub.domain.account.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.jaxrs.client.ResponseReader;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.stubhub.domain.account.common.enums.DeliveryOption;
import com.stubhub.domain.account.datamodel.dao.StubTransFmDmDAO;
import com.stubhub.domain.account.datamodel.entity.StubTransFmDm;
import com.stubhub.domain.account.intf.MyOrderResponse;
import com.stubhub.domain.fulfillment.lms.v1.intf.LMSLocation;
import com.stubhub.domain.fulfillment.lms.v1.intf.LMSLocationsResponse;
import com.stubhub.newplatform.property.MasterStubhubPropertiesWrapper;
import com.stubhub.platform.utilities.webservice.svclocator.SvcLocator;

@Component
public class LMSLocationHelper {
	
	@Autowired
	private SvcLocator svcLocator;

	@Autowired
	private StubTransFmDmDAO stubTransFmDmDAO;
	
	@Autowired
	private MasterStubhubPropertiesWrapper properties;
	
	private static final Log log = LogFactory.getLog(LMSLocationHelper.class);
	
	public void setLMSLocation4MyOrders(List<MyOrderResponse> orders, String locale){
		if(orders==null)
			return;
		String lmsLocationAPIUrl = properties.getProperty("pro.fulfillment.LMSLocation.api.url");

		boolean noLMS = true;
		
		Map<Long, Set<MyOrderResponse>> lmsLocationIdOrderMap = new HashMap<Long, Set<MyOrderResponse>>();

		Map<Long, MyOrderResponse> lmsOrderIdOrderMap = new HashMap<Long, MyOrderResponse>();
		for(MyOrderResponse order : orders){
			if(DeliveryOption.LMS.equals(order.getDeliveryOption())){

				lmsOrderIdOrderMap.put(order.getOrderId(),order);
				noLMS = false;
			}
		}
		if(noLMS)
			return;

		List<StubTransFmDm> fmDms = stubTransFmDmDAO.getFmDmByTids(new ArrayList<Long>(lmsOrderIdOrderMap.keySet()));

		if(fmDms != null && fmDms.size() > 0) {
			for (StubTransFmDm fmdm : fmDms) {
				Long lmsLocationID = fmdm.getLmsLocationId();
				Set<MyOrderResponse> orderResponses = lmsLocationIdOrderMap.get(lmsLocationID);
				if (orderResponses == null) {
					Set<MyOrderResponse> set = new HashSet<MyOrderResponse>();
					set.add(lmsOrderIdOrderMap.get(fmdm.getTid()));
					lmsLocationIdOrderMap.put(lmsLocationID, set);
				} else {
					orderResponses.add(lmsOrderIdOrderMap.get(fmdm.getTid()));
				}
			}
		}

		int lmsLocationNum = lmsLocationIdOrderMap.size();

		if(lmsLocationNum > 0){
			if(lmsLocationNum > 2){
				log.warn("api_domain=account api_resource=order api_method=setLMSLocation4MyOrders message=Multiple calls to fulfillment domain,number=" +  lmsLocationNum);
			}
			for(Long lmsLocationId : lmsLocationIdOrderMap.keySet()){
				setLMSLocation(lmsLocationAPIUrl,lmsLocationId,lmsLocationIdOrderMap,locale);
			}
		}



	}

    private final List<ResponseReader> responseReader;
    {
        ResponseReader reader = new ResponseReader();
        reader.setEntityClass(LMSLocationsResponse.class);
        responseReader = new ArrayList<ResponseReader>();
        responseReader.add(reader);
    }
	private void setLMSLocation(String lmsLocationAPIUrl,Long lmsLocationId,Map<Long, Set<MyOrderResponse>> lmsOrderIdOrderMap,String locale){
		StringBuilder sb = new StringBuilder(lmsLocationAPIUrl).append("/").append(lmsLocationId);
        
		log.debug("get LMS info, api call" + sb.toString());
		WebClient webClient = svcLocator.locate(sb.toString(), responseReader);
		webClient.accept(MediaType.APPLICATION_XML);
		webClient.acceptLanguage(locale);
		String token = properties.getProperty("newapi.accessToken");
		webClient.header("Authorization", "Bearer " + token);
		try{
			Response response = webClient.get();
			if(Response.Status.OK.getStatusCode() == response.getStatus()) {
				LMSLocationsResponse LMSLocation = (LMSLocationsResponse)response.getEntity();
				handleLmsLocationResponse(lmsOrderIdOrderMap, LMSLocation);
			}else{
				log.error(" getLMSLocation call return status not 200, status=" + response.getStatus());
			}
		}catch (Exception e) {
			log.error("exception while call getLMSLocation API", e);
		}
	}

	private void handleLmsLocationResponse(Map<Long, Set<MyOrderResponse>> lmsOrderIdOrderMap, LMSLocationsResponse lmsResponse){
		if(lmsResponse == null || lmsResponse.getLmsLocations() == null || lmsResponse.getLmsLocations().size() == 0)
			return;
		LMSLocation lmsLocation = lmsResponse.getLmsLocations().get(0);
		String LmsLocationString = buildLMSLocationString(lmsLocation);

		for(MyOrderResponse order:lmsOrderIdOrderMap.get(lmsLocation.getId())){
			order.setLmsLocation(LmsLocationString);
		}

	}



	
	private String buildLMSLocationString(LMSLocation location){
		StringBuilder sb = new StringBuilder(StringUtils.trimToEmpty(location.getAddress2())).append(" ").append(StringUtils.trimToEmpty(location.getAddress3()));
		if(StringUtils.isBlank(StringUtils.trimToEmpty(sb.toString()))){
			sb.append(StringUtils.trimToEmpty(location.getAddress1()));
		}		
		appendStringWithComma(sb, location.getCity());
		appendStringWithComma(sb, location.getState());
		
		return StringUtils.trimToEmpty(sb.toString());
	}
	
	private void appendStringWithComma(StringBuilder sb, String value){
		if(StringUtils.isNotBlank(value)){
			if(StringUtils.isNotBlank(sb.toString())){
				sb.append(", ");
			}
			sb.append(StringUtils.trimToEmpty(value));
		}	
	}
}
