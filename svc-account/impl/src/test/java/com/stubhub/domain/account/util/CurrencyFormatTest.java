package com.stubhub.domain.account.util;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Locale;

/**
 * Created at 12/30/2014 9:30 AM
 *
 * @author : Caron Zhao
 * @version : 1.0
 * @since : PI
 */
public class CurrencyFormatTest {

    @Test
    public void testFormat() {
        CurrencyFormat currencyFormat = new CurrencyFormat();
        String result = currencyFormat.format(Locale.US, 100);
        Assert.assertEquals(result, "$100.00");
    }
}