package com.stubhub.domain.account.biz.intf;

import com.stubhub.domain.account.intf.TaxIdRequest;
import com.stubhub.domain.account.intf.TaxIdResponse;
import com.stubhub.domain.account.intf.TaxIdShouldShowResponse;
import com.stubhub.domain.account.intf.TaxPayerAlertStatusResponse;

/**
 * Created at 11/4/15 6:06 PM
 *
 * @author : Caron Zhao
 * @version : 1.0.0
 */
public interface TaxPayerBO {
    TaxPayerAlertStatusResponse calculateShowTaxPayerAlert(String userId);

    TaxIdShouldShowResponse isTaxIdNeeded(String userId, Boolean validateNotExist);

    void createTaxId(TaxIdRequest taxIdRequest, String userId);

    TaxIdResponse getTaxId(String userId);

    void updateTaxId(TaxIdRequest taxIdRequest, String userId);

    boolean userHasTaxId(String userId);
}