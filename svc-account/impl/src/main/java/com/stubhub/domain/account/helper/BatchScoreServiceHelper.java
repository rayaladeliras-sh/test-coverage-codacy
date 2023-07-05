/**
 * Copyright 2019 StubHub, Inc.  All rights reserved.
 */
package com.stubhub.domain.account.helper;

import com.stubhub.domain.account.datamodel.dao.impl.CommonDAO;
import com.stubhub.domain.account.helper.dto.ListingScoreRequest;
import com.stubhub.domain.account.helper.dto.TicketScoreRecResponse;
import com.stubhub.domain.account.intf.ListingResponse;
import com.stubhub.domain.account.intf.ListingsResponse;
import com.stubhub.domain.account.intf.SaleResponse;
import com.stubhub.domain.account.intf.SalesResponse;
import com.stubhub.domain.catalog.read.v3.intf.events.dto.response.Event;
import com.stubhub.domain.infrastructure.config.client.core.SHConfig;
import com.stubhub.newplatform.common.entity.Money;
import com.stubhub.platform.utilities.webservice.svclocator.SvcLocator;
import org.apache.commons.io.IOUtils;
import org.apache.cxf.jaxrs.client.ClientConfiguration;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;
import org.codehaus.jackson.map.SerializationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static java.lang.Long.valueOf;
import static java.util.Collections.singletonList;

/**
 * BatchScoreServiceHelper
 *
 * @author hvisvanathan
 */
@Component("batchScoreServiceHelper")
public class BatchScoreServiceHelper {

    private static final Logger log = LoggerFactory.getLogger(BatchScoreServiceHelper.class);
    private static final int MAX_TRIES = 5;

    @Autowired
    protected SvcLocator svcLocator;

    @Autowired
    protected SHConfig shConfig;

    @Autowired
    CommonDAO commonDAO;

    @Autowired
    EventMetaHelper eventMetaHelper;

    private ObjectReader reader;
    private ObjectMapper mapper;

    public void fetchAndPopulateSalesScores(Long eventId, SalesResponse salesResponse, String mockScores) {
        Map<String, SaleResponse> saleResponseMap = new HashMap<String, SaleResponse>();
        if (null == salesResponse || null == salesResponse.getSales()) {
            log.info("method={} message={}", "fetchAndPopulateScores", "emptysalesResponse");
            return;
        }

        for (SaleResponse saleResponse : salesResponse.getSales()) {
            saleResponseMap.put(saleResponse.getListingId(), saleResponse);
        }

        log.info("method={} sales={}", "getListingScoreRequest", salesResponse.getSales());
        Integer coefficientVersion = commonDAO.getCoefficientVersion();
        log.info("coefficientVersion={}", coefficientVersion);
        String venueConfigId = eventMetaHelper.getEventDetail(eventId).getVenue().getConfigurationId();
        log.info("venueConfigId={}", venueConfigId);

        List<ListingScoreRequest.Listing> listings = new ArrayList<ListingScoreRequest.Listing>();
        for (SaleResponse saleResponse : salesResponse.getSales()) {
            listings.add(getListingScoreRequest(eventId, venueConfigId, valueOf(saleResponse.getListingId()),
                    saleResponse.getVenueConfigSectionId(), saleResponse.getRows(), saleResponse.getSeats(),
                    saleResponse.getDisplayPricePerTicket(), singletonList(saleResponse.getDeliveryTypeId())));
        }

        TicketScoreRecResponse ticketScoreRecResponse = getTicketScoreRecResponse(mockScores, coefficientVersion, listings);
        if (ticketScoreRecResponse != null) {
            for (TicketScoreRecResponse.ListingScore listingScore : ticketScoreRecResponse.getScores()) {
                SaleResponse saleResponseToUpdate =
                        saleResponseMap.get(String.valueOf(listingScore.getListingId()));
                if (null != saleResponseToUpdate) {
                    saleResponseToUpdate.setSeatQualityScore(listingScore.getSeatQualityScore());
                    saleResponseToUpdate.setBestValueScore(listingScore.getBestValueScore());
                }
            }
            log.info("method={} finalSalesResponse={}", "fetchAndPopulateScores", salesResponse);
        }
    }

    public void fetchAndPopulateListingsScores(ListingsResponse listingsResponse, String mockScores) {
        Map<String, ListingResponse> saleResponseMap = new HashMap<String, ListingResponse>();
        if (listingsResponse == null || listingsResponse.getListings() == null) {
            log.info("method={} message={}", "fetchAndPopulateListingsScores", "emptysalesResponse");
            return;
        }

        for (ListingResponse listingResponse : listingsResponse.getListings()) {
            saleResponseMap.put(listingResponse.getId(), listingResponse);
        }

        log.info("method={} sales={}", "fetchAndPopulateListingsScores", listingsResponse.getListings());
        Integer coefficientVersion = commonDAO.getCoefficientVersion();
        log.info("coefficientVersion={}", coefficientVersion);

        List<ListingScoreRequest.Listing> listings = new ArrayList<ListingScoreRequest.Listing>();
        for (ListingResponse listingResponse : listingsResponse.getListings()) {
            listings.add(getListingScoreRequest(valueOf(listingResponse.getEventId()), null, valueOf(listingResponse.getId()),
                    listingResponse.getVenueConfigSectionsId(), listingResponse.getRows(), listingResponse.getSeats(),
                    listingResponse.getDisplayPricePerTicket(), listingResponse.getDeliveryTypeIds()));
        }

        TicketScoreRecResponse ticketScoreRecResponse = getTicketScoreRecResponse(mockScores, coefficientVersion, listings);
        if (ticketScoreRecResponse != null) {
            for (TicketScoreRecResponse.ListingScore listingScore : ticketScoreRecResponse.getScores()) {
                ListingResponse listingResponseToUpdate = saleResponseMap.get(String.valueOf(listingScore.getListingId()));
                if (listingResponseToUpdate != null) {
                    listingResponseToUpdate.setValueScore(listingScore.getBestValueScore());
                }
            }
            log.info("method={} finalSalesResponse={}", "fetchAndPopulateListingsScores", listingsResponse);
        }
    }

    private TicketScoreRecResponse getTicketScoreRecResponse(String mockScores, Integer coefficientVersion, List<ListingScoreRequest.Listing> listings) {
        ListingScoreRequest batchScoreRequest = new ListingScoreRequest();
        batchScoreRequest.setCoefficientVersion(coefficientVersion);
        batchScoreRequest.setListings(listings);

        TicketScoreRecResponse ticketScoreRecResponse = getTicketScores(batchScoreRequest);
        log.info("method={} ticketScoreRecResponse={}", "fetchAndPopulateScores", ticketScoreRecResponse);

        if (null == ticketScoreRecResponse || CollectionUtils.isEmpty(ticketScoreRecResponse.getScores())) {
            if (null == mockScores) {
                log.info("method={} message={}", "fetchAndPopulateScores", "emptyscoresResponse");
                return null;
            } else {
                log.info("method={} message={}", "fetchAndPopulateScores", "mockingScore");
                ticketScoreRecResponse = getMockScores(batchScoreRequest);
            }
        }

        return ticketScoreRecResponse;
    }

    private ListingScoreRequest.Listing getListingScoreRequest(Long eventId, String venueConfigId, Long listingId, Long sectionId, String row, String seats, Money price, List<Integer> deliveryTypeIds) {
        ListingScoreRequest.Listing listing = new ListingScoreRequest.Listing();
        listing.setEventId(eventId);
        listing.setListingId(listingId);
        if (price != null) {
            listing.setPrice(price.getAmount().doubleValue());
        }
        listing.setRow(row);
        listing.setSectionId(sectionId);
        listing.setSeats(seats);

        listing.setDeliveryTypeIds(deliveryTypeIds);

        if (venueConfigId == null) {
            Event eventDetail = eventMetaHelper.getEventDetail(eventId);
            if (eventDetail != null && eventDetail.getVenue() != null && eventDetail.getVenue().getConfigurationId() != null) {
                venueConfigId = eventDetail.getVenue().getConfigurationId();
                log.info("venueConfigId={}", venueConfigId);

                listing.setVenueConfigId(valueOf(venueConfigId));
            }
        }

        return listing;
    }

    private TicketScoreRecResponse getMockScores(ListingScoreRequest batchScoreRequest) {
        TicketScoreRecResponse ticketScoreRecResponse = new TicketScoreRecResponse();
        List<TicketScoreRecResponse.ListingScore> listingScores =
                new ArrayList<TicketScoreRecResponse.ListingScore>();
        for (ListingScoreRequest.Listing listing : batchScoreRequest.getListings()) {
            TicketScoreRecResponse.ListingScore listingScore = new TicketScoreRecResponse.ListingScore();
            listingScore.setListingId(listing.getListingId());
            listingScore.setSeatQualityScore(new Random().nextDouble());
            listingScore.setBestValueScore(new Random().nextDouble());
            listingScores.add(listingScore);
        }
        ticketScoreRecResponse.setScores(listingScores);
        return ticketScoreRecResponse;
    }

//    private ListingScoreRequest getListingScoreRequest(Long eventId, SalesResponse salesResponse) {
//        List<ListingScoreRequest.Listing> listings = new ArrayList<ListingScoreRequest.Listing>();
//        log.info("method={} sales={}", "getListingScoreRequest", salesResponse.getSales());
//        Integer coefficientVersion = commonDAO.getCoefficientVersion();
//        log.info("coefficientVersion={}", coefficientVersion);
//        String venueConfigId = eventMetaHelper.getEventDetail(eventId).getVenue().getConfigurationId();
//        log.info("venueConfigId={}", venueConfigId);
//
//        for (SaleResponse saleResponse : salesResponse.getSales()) {
//            ListingScoreRequest.Listing listing = new ListingScoreRequest.Listing();
//            Long listingId = Long.valueOf(saleResponse.getListingId());
//            Long sectionId = saleResponse.getVenueConfigSectionId();
//            String row = saleResponse.getRows();
//            String seats = saleResponse.getSeats();
//            Number price = saleResponse.getDisplayPricePerTicket().getAmount();
//            listing.setEventId(eventId);
//            listing.setListingId(listingId);
//            if (price != null) {
//                listing.setPrice(price.doubleValue());
//            }
//            listing.setRow(row);
//            listing.setSectionId(sectionId);
//            listing.setSeats(seats);
//            List<Integer> deliveryTypeIdsInt = new ArrayList<Integer>();
//            deliveryTypeIdsInt.add(saleResponse.getDeliveryTypeId());
//            listing.setDeliveryTypeIds(deliveryTypeIdsInt);
//            listing.setVenueConfigId(Long.valueOf(venueConfigId));
//            listings.add(listing);
//
//        }
//
//        ListingScoreRequest batchScoreRequest = new ListingScoreRequest();
//        batchScoreRequest.setCoefficientVersion(coefficientVersion);
//        batchScoreRequest.setListings(listings);
//        return batchScoreRequest;
//    }

    public TicketScoreRecResponse getTicketScores(ListingScoreRequest batchScoreRequest) {
        String url = shConfig.getProperty("reco.ticketscore.url");
        log.info("scoreApiUrl={}", url);
        log.info("batchScoreRequest={}", batchScoreRequest);

        WebClient webClient = svcLocator.locate(url);
        webClient.header("Authorization", shConfig.getProperty("reco.ticketscore.accessToken"));
        webClient.accept(MediaType.APPLICATION_JSON);
        webClient.header("Content-Type", MediaType.APPLICATION_JSON);
        ClientConfiguration config = WebClient.getConfig(webClient);
        if (config != null) {
            HTTPConduit http = (HTTPConduit) config.getConduit();
            HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();
            httpClientPolicy.setConnectionTimeout(5000L);
            httpClientPolicy.setReceiveTimeout(5000L);
            http.setClient(httpClientPolicy);
        }
        String requestString = null;
        Response response = null;
        int count = 1;
        TicketScoreRecResponse ticketScoreResponse = null;
        while (true) { // NOSONAR
            try {
                requestString = mapper.writeValueAsString(batchScoreRequest);
                log.info("scoreJsonRequest={}", requestString);
                response = webClient.post(requestString);
                log.info("scoreResponse={}", response);
                if (null != response) {
                    log.info("responseCode={} scoreResponseDetails={}", response.getStatus(), response.getEntity());
                }

                if (response.getStatus() != Response.Status.OK.getStatusCode()) {
                    if (++count > MAX_TRIES) {
                        log.error("message={} count={} status={} url={} response={}",
                                "scoreApiCallFailed", count, response.getStatus(), webClient.getBaseURI(),
                                getErrorResponse(response));
                        break;
                    }
                } else {// we don't have to throw any exception here
                    try {
                        ticketScoreResponse = reader.readValue((InputStream) response.getEntity());
                    } catch (IOException e) {
                        log.error("message={}", "errorReadValue", e);
                    }
                    break;
                }
            } catch (Exception e) { // we have to do the retry here as well, in
                // case of exception
                if (++count > MAX_TRIES) {
                    log.error("_message=\"TicketScore API failure - count={} \"", count, e);
                    break;
                }
            }
        }
        return ticketScoreResponse;
    }

    private String getErrorResponse(Response response) throws IOException {
        InputStream in = (InputStream) response.getEntity();
        try {
            if (in != null) {
                return IOUtils.toString(in);
            }
            return null;
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    @PostConstruct
    private void init() {
        mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationConfig.Feature.WRAP_ROOT_VALUE, true);
        reader = mapper.reader(TicketScoreRecResponse.class).withType(TicketScoreRecResponse.class);
    }

}
