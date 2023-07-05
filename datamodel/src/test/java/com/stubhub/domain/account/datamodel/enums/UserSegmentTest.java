package com.stubhub.domain.account.datamodel.enums;

import org.testng.annotations.Test;

/**
 * Created at 11/7/15 12:05 PM
 *
 * @author : Caron Zhao
 * @version : 1.0.0
 */
public class UserSegmentTest {

    @Test
    public void testUserSegment() {
        UserSegment.DIRECT_TO_HOST.getId();
        UserSegment.DIRECT_TO_HOST.getDescription();
    }
}