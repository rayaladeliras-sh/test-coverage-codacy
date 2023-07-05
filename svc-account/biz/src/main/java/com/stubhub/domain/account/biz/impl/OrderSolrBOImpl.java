package com.stubhub.domain.account.biz.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.stubhub.domain.account.biz.intf.OrderSolrBO;
import com.stubhub.domain.account.common.SalesSearchCriteria;
import com.stubhub.domain.account.common.SortingDirective;
import com.stubhub.domain.account.datamodel.dao.DeliveriesDAO;
import com.stubhub.domain.account.datamodel.entity.Deliveries;
import com.stubhub.newplatform.property.MasterStubHubProperties;

@Component("orderSolrBO")
public class OrderSolrBOImpl implements OrderSolrBO {

	@Autowired
	private DeliveriesDAO deliveriesDAO;

	@Override
    public String getEddByTid(Long tid){
		List<Deliveries> lst = deliveriesDAO.getByTid(tid);
		Deliveries delivery = null;
		if (lst != null && lst.size() > 0) {
			delivery = lst.get(0);
			if(delivery.getExpectedArrivalDate() != null){
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
				return dateFormat.format(delivery.getExpectedArrivalDate().getTime());
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

}
