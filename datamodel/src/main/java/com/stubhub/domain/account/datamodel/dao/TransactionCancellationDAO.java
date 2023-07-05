package com.stubhub.domain.account.datamodel.dao;

import com.stubhub.domain.account.datamodel.entity.TransactionCancellation;

public interface TransactionCancellationDAO {
	
	public Long persist(TransactionCancellation transactionCancellation);
}
