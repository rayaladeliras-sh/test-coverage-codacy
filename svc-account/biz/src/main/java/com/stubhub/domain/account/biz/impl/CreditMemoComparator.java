package com.stubhub.domain.account.biz.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import com.stubhub.domain.account.intf.CreditMemoResponse;

public class CreditMemoComparator implements Comparator<CreditMemoResponse> {
	
	private String sort = "";
	
    public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	@Override
    public int compare(CreditMemoResponse o1, CreditMemoResponse o2) {
        Comparable value1 = null;
        Comparable value2 = null;
        int result = 0;
        boolean isDate = false;
        if(sort.equalsIgnoreCase("ORDERID DESC")) {
        	value1 = Long.parseLong(o2.getOrderId());
        	value2 = Long.parseLong(o1.getOrderId());
        } else if(sort.equalsIgnoreCase("AMOUNT DESC")) {
        	value1 = o2.getCreditAmount().getAmount();
        	value2 = o1.getCreditAmount().getAmount();
        } else if(sort.equalsIgnoreCase("APPLIEDDATE DESC")) {
        	value1 = o2.getAppliedDate();
        	value2 = o1.getAppliedDate();
        	isDate = true;
        } else if(sort.equalsIgnoreCase("CREATEDDATE DESC")) {
        	value1 = o2.getCreatedDate();
        	value2 = o1.getCreatedDate();
        	isDate = true;
        } else if(sort.equalsIgnoreCase("ORDERID ASC")) {
        	value1 = Long.parseLong(o1.getOrderId());
        	value2 = Long.parseLong(o2.getOrderId());
        } else if(sort.equalsIgnoreCase("AMOUNT ASC")) {
        	value1 = o1.getCreditAmount().getAmount();
        	value2 = o2.getCreditAmount().getAmount();
        } else if(sort.equalsIgnoreCase("APPLIEDDATE ASC")) {
        	value1 = o1.getAppliedDate();
        	value2 = o2.getAppliedDate();
        	isDate = true;
        } else if(sort.equalsIgnoreCase("CREATEDDATE ASC")) {
        	value1 = o1.getCreatedDate();
        	value2 = o2.getCreatedDate();
        	isDate = true;
        }
        
        if(isDate == false) {
        	if (value1 != null ^ value2 != null) {
                result = value1 != null ? 1 : -1;
            } else if (value1 != null) {
                result = value1.compareTo(value2);
            }
        }
        else {
        	String format = "yyyy-MM-dd'T'HH:mm:ssZ";		
    		SimpleDateFormat df = new SimpleDateFormat(format);
    		try {
    			if(value1 != null && value2 != null){
    				Date date1 = df.parse((String) value1);
    				Date date2 = df.parse((String) value2);
    				if(date1.after(date2)){
    					result = 1;
    				}
    				if(date1.before(date2)) {
    					result = -1;
    				}
    			}					
    		} catch (ParseException e) {
    		}
        }
		return result;
    }
}
