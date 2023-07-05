package com.stubhub.domain.account.datamodel.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.stubhub.domain.account.datamodel.entity.AppliedCreditMemoDO;

public interface AppliedCreditMemoDAO {
    List<AppliedCreditMemoDO> findByAppliedPaymentId(Long pid);
    Map<Long, BigDecimal> findAppliedAmountByAppliedPaymentId(List<Long> pids);
}