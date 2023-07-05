package com.stubhub.domain.account.biz.impl;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.stubhub.newplatform.property.MasterStubHubProperties;
import com.stubhub.platform.utilities.webservice.shcontext.SHAPIContext;
import com.stubhub.platform.utilities.webservice.shcontext.SHAPIThreadLocal;
@Configuration
@EnableScheduling
@Component
public class AccountServiceMonitor {
    private final static Logger log = LoggerFactory
			.getLogger(AccountServiceMonitor.class);
	public boolean accountServiceHealth = true;

	@Scheduled(cron = "0 0/5 * * * ?")
	public void execute() {
		boolean status = true;
		status = status
				&& getClient(getProperty("USER_DOMAIN_WADL",
						"http://api-int.stubprod.com/catalog/events/v1?_wadl"));
		status = status
				&& getClient(getProperty("INVENTORY_DOMAIN_WADL",
						"http://api-int.stubprod.com/inventorynew/listings/v1?_wadl"));
		accountServiceHealth = status;
		log.info("AccountServiceHealth status=" + status);
	}

	protected String getProperty(String property, String defaultValue) {
		return MasterStubHubProperties.getProperty(property, defaultValue);
	}

	protected WebClient getWebClient(String uri) {
		try{
			SHAPIThreadLocal shapithreadLocal = new SHAPIThreadLocal();
			SHAPIContext apiContext = new SHAPIContext();
			shapithreadLocal.set(apiContext);
			WebClient webClient = WebClient.create(uri);
			return webClient;
		}catch(Exception e){
			return null;
		}
	}

	private boolean getClient(String uri) {
		WebClient webClient = getWebClient(uri);
		webClient.accept(MediaType.APPLICATION_XML);
		Response response = webClient.get();
		if (response == null)
			return false;
		try {
			((InputStream) response.getEntity()).close();
		} catch (IOException e) {
			log.error("InventoryServiceHealth error", e);
		}
		log.info("InventoryServiceHealth check status=" + response.getStatus()
				+ "  URI" + uri);
		if (response.getStatus() != 200) {
			return false;
		}
		return true;
	}

}
