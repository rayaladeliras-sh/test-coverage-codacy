package com.stubhub.domain.account.biz.intf;

import com.stubhub.domain.account.intf.InvoiceResponse;

public interface InvoiceBO {
    InvoiceResponse getByReferenceNumber(String refNumber, String pid, String acceptLanguage);
}