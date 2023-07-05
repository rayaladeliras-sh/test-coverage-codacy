package com.stubhub.domain.account.datamodel.dao;

import com.stubhub.domain.account.datamodel.entity.UserDO;

/**
 * Created at 11/5/15 2:11 PM
 *
 * @author : Caron Zhao
 * @version : 1.0.0
 */
public interface UserDAO {
    UserDO findUserByGuid(String userGuid);
    Long getUserSellerPaymentType(Long userId);

    UserDO findUserById(String id);
}