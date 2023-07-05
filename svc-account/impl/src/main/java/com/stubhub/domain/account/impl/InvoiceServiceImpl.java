package com.stubhub.domain.account.impl;

import java.io.ByteArrayOutputStream;
import java.util.Locale;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.stubhub.common.exception.UserNotAuthorizedException;
import com.stubhub.domain.account.biz.intf.InvoiceBO;
import com.stubhub.domain.account.common.util.SecurityContextUtil;
import com.stubhub.domain.account.exception.InvoiceNotFoundException;
import com.stubhub.domain.account.exception.SellerUnauthorizedException;
import com.stubhub.domain.account.exception.UnauthorizedException;
import com.stubhub.domain.account.intf.InvoiceResponse;
import com.stubhub.domain.account.intf.InvoiceService;
import com.stubhub.domain.account.invoice.InvoiceGenerator;
import com.stubhub.domain.account.invoice.InvoiceGeneratorFactory;
import com.stubhub.platform.utilities.webservice.security.ExtendedSecurityContext;

@Component("invoiceService")
public class InvoiceServiceImpl implements InvoiceService {
	
	private static final Locale DEFAULT_LOCALE = Locale.US;
    @Autowired
    private InvoiceBO invoiceBO;

    @Autowired
    private SecurityContextUtil securityContextUtil;

    @Autowired
    private InvoiceGeneratorFactory invoiceGeneratorFactory;

    @Override
    public Response getInvoiceByReferenceNumber(ExtendedSecurityContext context, String sellerGuid, String refNumber, String pid, String acceptLanguage) {
        try {
            securityContextUtil.validateUserGuid(context, sellerGuid);
        } catch (UserNotAuthorizedException e) {
            throw new UnauthorizedException();
        }

        Long sellerId = context.getUserId() == null ? null : Long.valueOf(context.getUserId());

        InvoiceResponse invoiceResponse = invoiceBO.getByReferenceNumber(refNumber, pid, acceptLanguage);
        if (null == invoiceResponse.getInvoice()) {
            throw new InvoiceNotFoundException(sellerId.toString());
        }
        if (!invoiceResponse.getSellerId().equals(sellerId)){
            throw new SellerUnauthorizedException(String.valueOf(sellerId));
        }

        // hide sellerid to client after validation
        invoiceResponse.setSellerId(null);
        return Response.ok(invoiceResponse).build();
    }

    @Override
    public Response getPdfInvoiceByReferenceNumber(ExtendedSecurityContext context, String sellerGuid, String refNumber,String qLocale, String pid, String localeStr) {
        Response result = getInvoiceByReferenceNumber(context, sellerGuid, refNumber, pid, localeStr);
        InvoiceGenerator invoiceGenerator = invoiceGeneratorFactory.getInvoiceGenerator(InvoiceGeneratorFactory.InvoiceGeneratorType.PDF);
        Locale locale = DEFAULT_LOCALE;
        if(qLocale != null && qLocale.trim() != ""){
            locale = getLocale(qLocale);
        }else{
        	locale = getLocale(localeStr);
        }
        ByteArrayOutputStream baosPDF = invoiceGenerator.generateInvoice(refNumber, (InvoiceResponse)result.getEntity(),locale);
        return Response.ok(baosPDF.toByteArray(), "application/pdf").header("Content-disposition", "inline; filename=" + refNumber + ".pdf").build();
    }

    private Locale getLocale(String localeStr){
    	String[] array = localeStr.split("-");
    	if(array.length == 2){
    		return new Locale(array[0],array[1]);
    	}
    	return DEFAULT_LOCALE;	
    }
}