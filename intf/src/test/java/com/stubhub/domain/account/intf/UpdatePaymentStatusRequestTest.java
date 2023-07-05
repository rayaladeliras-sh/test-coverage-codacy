package com.stubhub.domain.account.intf;

import org.junit.Assert;
import org.testng.annotations.Test;

import java.util.Calendar;

public class UpdatePaymentStatusRequestTest {
    @Test
    public void test(){
        UpdatePaymentStatusRequest updatePaymentStatusRequest = new UpdatePaymentStatusRequest();
        updatePaymentStatusRequest.setAction(UpdatePaymentStatusRequest.Action.HOLD_PAYMENT_FOR_DUE_DILIGENCE);
        updatePaymentStatusRequest.setLatestPaymentDate(Calendar.getInstance());
        updatePaymentStatusRequest.setUserId(1L);
        Assert.assertNotNull(updatePaymentStatusRequest.getAction());
        Assert.assertNotNull(updatePaymentStatusRequest.getLatestPaymentDate());
        Assert.assertNotNull(updatePaymentStatusRequest.getUserId());

        UpdatePaymentStatusRequest.Action action1 = UpdatePaymentStatusRequest.Action.HOLD_PAYMENT_FOR_DUE_DILIGENCE;
        UpdatePaymentStatusRequest.Action action2 = UpdatePaymentStatusRequest.Action.RELEASE_PAYMENT_FOR_DUE_DILIGENCE;

        action1.getValue();
        action2.getValue();
    }
}
