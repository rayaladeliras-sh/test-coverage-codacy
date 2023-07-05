package com.stubhub.domain.account.common.util;

import com.stubhub.domain.account.common.ListingFulfillmentWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by weili5 on 2015/9/14.
 */
public class ListingFulfillmentWindowUtil {

    private static final Logger logger = LoggerFactory.getLogger(ListingFulfillmentWindowUtil.class);

    private static final char CONSTANT_COMMA = ',';
    private static final char CONSTANT_PIPE = '|';

    /**
     * Get Fulfillment window list from a specified string separated by |
     * @param windowListStr
     * @return
     */
    public static List<ListingFulfillmentWindow> getFulfillmentWindowList(String windowListStr){

        logger.debug("Converting string to List<FulfillmentWindow> for {}", windowListStr);

        if(windowListStr != null && windowListStr.isEmpty() == false){
            List<ListingFulfillmentWindow> fulfillmentWindowList = new ArrayList<ListingFulfillmentWindow>();

            StringTokenizer windowTokens = new StringTokenizer(windowListStr, String.valueOf(CONSTANT_PIPE));
            while(windowTokens.hasMoreTokens()){
                fulfillmentWindowList.add(getFulfillmentWindow(windowTokens.nextToken()));
            }
            return fulfillmentWindowList;
        }

        return null;
    }

    /**
     * Create a FulfillmentWindow from specified string in format "fm,dm,basecos,startdate,enddate"
     *
     * @param str
     * @return
     */
    private static ListingFulfillmentWindow getFulfillmentWindow(String str){
        logger.debug("Converting token: {} to FulfillmentWindow", str);
        String[] windowElements = str.split(String.valueOf(CONSTANT_COMMA));
        Long fmId = Long.valueOf(windowElements[0]);
        Long dmId = Long.valueOf(windowElements[1]);
        Double baseCost =  Double.parseDouble(windowElements[2]);
        Calendar startDate = null;

        if(windowElements.length > 3 && windowElements[3].isEmpty() == false){
            startDate = getDate(windowElements[3]);
        }
        Calendar endDate =  null;
        if(windowElements.length > 4 && windowElements[4].isEmpty() == false){
            endDate =  getDate(windowElements[4]);
        }

        ListingFulfillmentWindow window = new ListingFulfillmentWindow();
        window.setBaseCost(baseCost);
        window.setStartDate(startDate);
        window.setEndDate(endDate);

        window.setFulfillmentMethodId(fmId);
        window.setDeliveryMethodId(dmId);
        return window;
    }

    private static Calendar getDate(String dateStr){
        Calendar date = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        try{
            date.setTime(getDateFormat().parse(dateStr));
        }catch(ParseException e){
            logger.error("Error occurred while parsing string to Date", e);
        }
        return date;
    }

    private static SimpleDateFormat getDateFormat(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        sdf.setLenient(false);
        return sdf;
    }
}
