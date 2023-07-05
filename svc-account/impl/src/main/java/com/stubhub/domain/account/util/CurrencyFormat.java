package com.stubhub.domain.account.util;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created at 12/29/2014 6:14 PM
 *
 * @author : Caron Zhao
 * @version : 1.0.0
 * @since : PI
 */
public final class CurrencyFormat {

    public static String format(Locale locale, double number) {
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        return numberFormat.format(number);
    }
}