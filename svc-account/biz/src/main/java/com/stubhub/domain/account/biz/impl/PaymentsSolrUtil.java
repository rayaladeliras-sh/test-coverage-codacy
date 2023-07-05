package com.stubhub.domain.account.biz.impl;
import java.net.MalformedURLException;

import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.springframework.stereotype.Component;

import com.stubhub.newplatform.property.MasterStubHubProperties;
import com.stubhub.newplatform.solr.SolrUtil;



@Component
public class PaymentsSolrUtil extends SolrUtil {

	private HttpSolrServer paymentsSolrServer;
	
	/**
	 * Initialize new order solr server, it happen only once during bean post construct
	 */
	public PaymentsSolrUtil() {
		try {
			paymentsSolrServer = createCommonsHttpSolrServer(MasterStubHubProperties.getProperty("mci.payments.solr.url"));
		} catch (MalformedURLException e) {
			log.error(String.format("Error occured while creating Order Solr Server:%s", e.getMessage()), e);
		}
	}
	
	/**
	 * Get inventory solr server instance
	 */
	protected HttpSolrServer getServer(){
		return paymentsSolrServer;
	}

}
