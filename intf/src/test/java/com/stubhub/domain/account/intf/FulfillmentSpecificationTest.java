package com.stubhub.domain.account.intf;

import com.beust.jcommander.internal.Lists;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.testng.Assert;
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
public class FulfillmentSpecificationTest {

    @Test
    public void testXml(){
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();//向OutPutStream中写入，如 message.writeTo(baos);
            JAXBContext jc = JAXBContext.newInstance(FulfillmentSpecificationResponse.class);//create an target object to marshal/unmarshal
            FulfillmentSpecificationResponse efw=buildMock();
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
        FulfillmentSpecificationResponse efw=buildMock();
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
        FulfillmentSpecificationRequest efw=buildMockRequest();
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

    private FulfillmentSpecificationRequest buildMockRequest() {
        FulfillmentSpecificationRequest fulfillmentSpecificationRequest=new FulfillmentSpecificationRequest();
        fulfillmentSpecificationRequest.setListings("1234,3456,6789,5678");
        fulfillmentSpecificationRequest.setWithSeatDetails(1L);
        fulfillmentSpecificationRequest.setWithUserDetails(0L);
        return fulfillmentSpecificationRequest;
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

    private FulfillmentSpecificationResponse buildMock() {
        FulfillmentSpecificationResponse fulfillmentSpecificationResponse=new FulfillmentSpecificationResponse();
        FulfillmentSpecification fs1=new FulfillmentSpecification();
        fs1.setEventId(123L);
        fs1.setDeliveryOptionId(1L);
        fs1.setSellerId(123L);
        fs1.getSellerId();
        fs1.setJdkTimeZone("Europe/London");
        fs1.getJdkTimeZone();
        fs1.setEventDateLocal("2015-08-21'T'12:34:00+07:00");
        fs1.setEventDateUTC("2015-08-21'T'12:34:00Z");
        fs1.setInHandDate("2015-08-15'T'12:00:00Z");
        fs1.setTicketMedium(2L);
        fs1.setListingSourceId(1L);
        fs1.setLmsApprovalStatusId(1L);
        fs1.setQuantityRemain(10L);
        fs1.setSaleEndDate("2015-09-11'T'12:00:00Z");
        fs1.setSplitOption(2L);
        fs1.setSystemStatus("ACTIVE");
        UserContactDetail sellerContactDetail=new UserContactDetail();
        sellerContactDetail.setContactId(345L);
        sellerContactDetail.setCountryCode("GB");
        sellerContactDetail.setLocality("London");
        sellerContactDetail.setPostalcode("0P89K");
        sellerContactDetail.setProvinceOrStateCode("MT");
        fs1.setSellerContact(sellerContactDetail);
        fs1.setSeatDetailList(buildSeatDetails());



        FulfillmentSpecification fs2=new FulfillmentSpecification();
        fs2.setEventCountry("GB");
        fs2.getEventCountry();
        fs2.setTicketId(1234L);
        fs2.setEventId(123L);
        fs2.setDeliveryOptionId(1L);
        fs2.setEventDateLocal("2015-08-21'T'12:34:00+07:00");
        fs2.setEventDateUTC("2015-08-21'T'12:34:00Z");
        fs2.setInHandDate("2015-08-15'T'12:00:00Z");
        fs2.setTicketMedium(2L);
        fs2.setListingSourceId(1L);
        fs2.setLmsApprovalStatusId(1L);
        fs2.setQuantityRemain(10L);
        fs2.setSaleEndDate("2015-09-11'T'12:00:00Z");
        fs2.setSplitOption(2L);
        fs2.setSystemStatus("ACTIVE");
        UserContactDetail sellerContactDetail2=new UserContactDetail();
        sellerContactDetail2.setContactId(345L);
        sellerContactDetail2.setCountryCode("GB");
        sellerContactDetail2.setLocality("London");
        sellerContactDetail2.setPostalcode("0P89K");
        sellerContactDetail2.setProvinceOrStateCode("MT");
        fs2.setSellerContact(sellerContactDetail2);
        fs2.setSeatDetailList(buildSeatDetails());

        List<FulfillmentSpecification> fsList=new ArrayList<FulfillmentSpecification>();
        fsList.add(fs1);
        fsList.add(fs2);
        fulfillmentSpecificationResponse.setFulfillmentSpecifications(fsList);
        return fulfillmentSpecificationResponse;
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
