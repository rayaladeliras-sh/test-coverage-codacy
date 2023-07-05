package com.stubhub.domain.account.invoice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created at 12/25/2014 5:16 PM
 *
 * @author : Caron Zhao
 * @version : 1.0.0
 * @since : PI
 */
@Component("invoiceGeneratorFactory")
public class InvoiceGeneratorFactoryImpl implements InvoiceGeneratorFactory {
	
	@Autowired
	private PdfInvoiceGenerator pdfInvoiceGenerator;

    @Override
    public InvoiceGenerator getInvoiceGenerator(InvoiceGeneratorType generatorType) {
        switch (generatorType) {
            case PDF:
                return pdfInvoiceGenerator;
            default:
                return pdfInvoiceGenerator;
        }
    }
}