package com.stubhub.domain.account.helper;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.stubhub.domain.common.util.exceptions.ClientException;
import com.stubhub.domain.cs.dto.CsrNotesRequest;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.jaxrs.client.WebClient;
import org.codehaus.jackson.map.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.stubhub.domain.cs.dto.BPMMessageRequest;
import com.stubhub.newplatform.property.MasterStubhubPropertiesWrapper;
import com.stubhub.platform.utilities.webservice.svclocator.SvcLocator;

import java.io.InputStream;

@Component("customerServiceHelper")
public class CustomerServiceHelper {

	@Autowired
	private SvcLocator svcLocator;

	@Autowired
	private MasterStubhubPropertiesWrapper properties;
	
	private final ObjectMapper mapper = new ObjectMapper();

	private final static Log log = LogFactory.getLog(CustomerServiceHelper.class);

	public void sendBPMMessage(BPMMessageRequest request, String sellerGuid, String operatorId) throws Exception {

		String apiUrl = properties.getProperty("unified.cs.bpm.message.api.url");

		WebClient webClient = svcLocator.locate(apiUrl);
		webClient.accept(MediaType.APPLICATION_JSON);
	    webClient.type(MediaType.APPLICATION_JSON);
		String token = properties.getProperty("newapi.accessToken");
		webClient.header("Authorization", "Bearer " + token);
		webClient.header("X-SH-Service-Context", "{role=R2, operatorId=" + operatorId + ", proxiedId=" + sellerGuid + "}");

		long startTS = System.currentTimeMillis();
		log.info("begin invoke csBPMMessageApiUrl=" + apiUrl);
		Response response = webClient.post(request);
		log.info("end invoke, takes ms=" + (System.currentTimeMillis() - startTS));

		Object responseContent = (response.getEntity() instanceof InputStream) ? IOUtils.toString((InputStream) response.getEntity(), "UTF-8") : response.getEntity() ;

		if (log.isDebugEnabled()) {
			String requestPayload = mapper.writeValueAsString(request);
			log.debug("csBPMMessageApiUrl=" + apiUrl + " + request=" + requestPayload + " -> " + response.getStatus() + " " + responseContent);
		}

		if (Response.Status.CREATED.getStatusCode() != response.getStatus()) {
			log.error("cs bpm message call return status=" + response.getStatus());
			throw new ClientException("bad response:" + response.getStatus() + " body:" + responseContent);
		}
	}

	public void addCSRNote(Long orderId, CsrNotesRequest csrNotesRequest, String sellerGuid, String operatorId) throws Exception {
		String apiUrl = properties.getProperty("unified.cs.csrnotes.api.url");

		apiUrl = apiUrl.replace("{orderId}", String.valueOf(orderId));

		WebClient webClient = svcLocator.locate(apiUrl);
		webClient.accept(MediaType.APPLICATION_JSON);
		webClient.type(MediaType.APPLICATION_JSON);
		String token = properties.getProperty("newapi.accessToken");
		webClient.header("Authorization", "Bearer " + token);
		webClient.header("X-SH-Service-Context", "{role=R2, operatorId=" + operatorId + ", proxiedId=" + sellerGuid + "}");

		long startTS = System.currentTimeMillis();
		log.info("begin invoke csrNoteAPIUrl=" + apiUrl);
		Response response = webClient.post(csrNotesRequest);
		log.info("end invoke, takes ms=" + (System.currentTimeMillis() - startTS));

		Object responseContent = (response.getEntity() instanceof InputStream) ? IOUtils.toString((InputStream) response.getEntity(), "UTF-8") : response.getEntity() ;

		if (log.isDebugEnabled()) {
			String requestPayload = mapper.writeValueAsString(csrNotesRequest);
			log.debug("csrNoteAPIUrl=" + apiUrl + " + request=" + requestPayload + " -> " + response.getStatus() + " " + responseContent);
		}

		if (Response.Status.CREATED.getStatusCode() != response.getStatus()) {
			log.error("csrNoteAPIUrl call return status=" + response.getStatus());
			throw new ClientException("bad response:" + response.getStatus() + " body:" + responseContent);
		}
	}
}
