package com.stubhub.domain.account.datamodel.dao;

import com.stubhub.domain.account.datamodel.entity.TaxPayerDetails;

/**
 * @author vpothuru
 * date 11/02/15
 *
 */
public interface TaxPayerDetailsDAO {
	
	TaxPayerDetails getTaxPayerDetailsByUserId(Long userId);

	TaxPayerDetails getTaxPayerDetailsByUserGuid(String userGuid);

	int updateTaxPayerDetails(TaxPayerDetails taxPayerDetails, boolean updateAddress);

	long addTaxPayerDetails(TaxPayerDetails taxPayerDetails);
}