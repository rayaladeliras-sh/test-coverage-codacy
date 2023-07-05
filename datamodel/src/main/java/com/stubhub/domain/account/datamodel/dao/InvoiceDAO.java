package com.stubhub.domain.account.datamodel.dao;

import java.util.List;
import java.util.Map;

import com.stubhub.domain.account.datamodel.entity.AdjustmentReason;
import com.stubhub.domain.account.datamodel.entity.InvoiceDO;
import com.stubhub.domain.account.datamodel.entity.OrderAdjustment;

public interface InvoiceDAO {
    InvoiceDO getByReferenceNumber(String refNumber);

    InvoiceDO getByReferenceNumberAndTid(String refNumber, Long tid);

    List<OrderAdjustment> getOrderAdjustmentByOrderId(Long orderID);

    Map<Long, AdjustmentReason> getAllAdjustmentReasons();
}