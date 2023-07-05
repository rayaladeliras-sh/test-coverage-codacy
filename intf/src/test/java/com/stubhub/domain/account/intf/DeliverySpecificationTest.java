package com.stubhub.domain.account.intf;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.testng.annotations.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jicui on 8/31/15.
 */
public class DeliverySpecificationTest {

    @Test
    public void testXml(){
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();//向OutPutStream中写入，如 message.writeTo(baos);
            JAXBContext jc = JAXBContext.newInstance(DeliverySpecificationResponse.class);//create an target object to marshal/unmarshal
            DeliverySpecificationResponse efw=buildMock();
            Marshaller marshaller = null;//create Marshaller handler
            marshaller = jc.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);//print the out put with nice look and formatted
            marshaller.marshal(efw, baos);//marshal the error instance and print it out to the system.out
            System.out.print(baos.toString());
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testJson(){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectMapper mapper = getObjectMapper();
        DeliverySpecificationResponse efw=buildMock();
        try {
            mapper.writeValue(baos,efw);
            JSONObject jsonObject=new JSONObject(baos.toString());
            System.out.print(baos.toString());
            //Assert.assertEquals(123L, Long.valueOf(eventId).longValue());
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testJsonForRequest(){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectMapper mapper = getObjectMapper();
        DeliverySpecificationRequest efw=buildMockRequest();
        try {
            mapper.writeValue(baos,efw);
            JSONObject jsonObject=new JSONObject(baos.toString());
            System.out.print(baos.toString());
            //Assert.assertEquals(123L, Long.valueOf(eventId).longValue());
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private DeliverySpecificationRequest buildMockRequest() {
        DeliverySpecificationRequest deliverySpecificationRequest=new DeliverySpecificationRequest();
        deliverySpecificationRequest.setOrders("1234,3456,6789,5678");
        deliverySpecificationRequest.setWithSeatDetails(1L);
        deliverySpecificationRequest.setWithUserDetails(0L);
        return deliverySpecificationRequest;
    }


    private ObjectMapper getObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();//jackson instance
        mapper.configure(org.codehaus.jackson.map.SerializationConfig.Feature.WRAP_ROOT_VALUE, false);//DO not wrap the top root node
        mapper.configure(org.codehaus.jackson.map.DeserializationConfig.Feature.UNWRAP_ROOT_VALUE, true);//For deserialization ,unwrap the top level node and parse his properties.
        mapper.configure(org.codehaus.jackson.map.SerializationConfig.Feature.INDENT_OUTPUT, true);//nice out put printer
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        mapper.setDateFormat(sdf);
        return mapper;
    }

    private DeliverySpecificationResponse buildMock() {
        DeliverySpecificationResponse deliverySpecificationResponse=new DeliverySpecificationResponse();
        DeliverySpecification ds1=new DeliverySpecification();
        ds1.setEventId(123L);
        ds1.setTicketId(1112234L);
        ds1.setCancelled(0L);
        ds1.setJdkTimeZone("Europe/London");
        ds1.getJdkTimeZone();
        ds1.setOrderProcSubStatusCode("3000");
        ds1.setEventDateLocal("2015-08-21'T'12:34:00+07:00");
        ds1.setEventDateUTC("2015-08-21'T'12:34:00Z");
        ds1.setInHandDate("2015-08-15'T'12:00:00Z");
        ds1.setTicketMedium(2L);
        ds1.setDeliveryOptionId(1L);
        ds1.setFulfillmentMethodId(5L);
        ds1.setDeliveryMethodId(10L);

        ds1.getDeliveryMethodId();
        ds1.getFulfillmentMethodId();
        ds1.getDeliveryOptionId();

        UserContactDetail buyerContactDetail=new UserContactDetail();
        buyerContactDetail.setContactId(345L);
        buyerContactDetail.setCountryCode("GB");
        buyerContactDetail.setLocality("London");
        buyerContactDetail.setPostalcode("0P89K");
        buyerContactDetail.setProvinceOrStateCode("MT");
        ds1.setBuyerContact(buyerContactDetail);
        ds1.setPurchasedSeatDetails(buildSeatDetails());



        DeliverySpecification ds2=new DeliverySpecification();
        ds2.setEventId(123L);
        ds2.setTicketId(1112234L);
        ds2.setCancelled(0L);
        ds2.setOrderProcSubStatusCode("3000");
        ds2.setEventDateLocal("2015-08-21'T'12:34:00+07:00");
        ds2.setEventDateUTC("2015-08-21'T'12:34:00Z");
        ds2.setInHandDate("2015-08-15'T'12:00:00Z");
        ds2.setTicketMedium(2L);
        UserContactDetail buyerContactDetail2=new UserContactDetail();
        buyerContactDetail2.setContactId(345L);
        buyerContactDetail2.setCountryCode("GB");
        buyerContactDetail2.setLocality("London");
        buyerContactDetail2.setPostalcode("0P89K");
        buyerContactDetail2.setProvinceOrStateCode("MT");
        ds2.setBuyerContact(buyerContactDetail2);
        ds2.setPurchasedSeatDetails(buildSeatDetails());

        List<DeliverySpecification> fsList=new ArrayList<DeliverySpecification>();
        fsList.add(ds1);
        fsList.add(ds2);
        deliverySpecificationResponse.setDeliverySpecifications(fsList);
        return deliverySpecificationResponse;
    }

    private List<SeatDetail> buildSeatDetails() {
        List<SeatDetail> seatDetails=new ArrayList<SeatDetail>();
        SeatDetail sd=new SeatDetail();
        sd.setSeatId(100001L);
        sd.setSeatNum("123");
        sd.setSeatStatusId(2L);
        sd.setSection("testsection");
        sd.setTixListTypeId(3L);
        seatDetails.add(sd);
        return seatDetails;
    }
}
