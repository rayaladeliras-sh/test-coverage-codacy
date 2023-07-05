package com.stubhub.domain.account.biz.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.stubhub.domain.account.biz.intf.TransactionSummaryBO;
import com.stubhub.domain.account.datamodel.dao.TransactionSummaryDAO;
import com.stubhub.domain.catalog.read.v3.intf.events.dto.response.Event;


@Component("transactionSummaryBO")
public class TransactionSummaryBOImpl implements TransactionSummaryBO {
	
	@Autowired
	TransactionSummaryDAO transactionSummaryDAO;
	
	@Autowired
	EventUtil eventUtil;

	public List<HashMap<String,String>> getUserTransactionSummary(Long userId, String currencyCode, boolean fullSummaryDetails)
	{
		List<HashMap<String,String>> summaryDetailsList = null;
		if(null != userId){
			summaryDetailsList = transactionSummaryDAO.getSummaryDetails(userId, currencyCode, fullSummaryDetails);
			if(summaryDetailsList != null && !summaryDetailsList.isEmpty()){
				// event date come from catalog v3 api
				HashMap<String,String> orderStats = summaryDetailsList.get(0);
				if(fullSummaryDetails){
					if(orderStats.get("EVENT_ID") != null){
						String eventId = orderStats.get("EVENT_ID");
						if(!StringUtils.isEmpty(eventId)){
							Event event = eventUtil.getEventV3(eventId, null);
							if(event != null){
								orderStats.put("EARLIEST_EVENT_DATE_UTC", event.getEventDateUTC());
								orderStats.put("EARLIEST_EVENT_DATE_LOCAL", event.getEventDateLocal().replaceAll("T", " ").replaceAll("Z", "").substring(0, 19));
							}
						}
					}
				}
			}
		}
			
		return summaryDetailsList;
		
	}

}
