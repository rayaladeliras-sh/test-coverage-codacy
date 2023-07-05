package com.stubhub.domain.account.datamodel.entity;

import org.testng.annotations.Test;

/**
 * Created at 11/6/15 3:28 PM
 *
 * @author : Caron Zhao
 * @version : 1.0.0
 */
public class UserDOTest {

    @Test
    public void testUserDO() {
        UserDO userDO = new UserDO();
        userDO.setUserId(Long.valueOf(1));
        userDO.getUserId();
        userDO.setUserGuid("guid");
        userDO.getUserGuid();
        userDO.setSellerSegmentId(Long.valueOf(1));
        userDO.getSellerSegmentId();
    }
}