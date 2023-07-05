package com.stubhub.domain.account.common.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * Created at 10/28/15 5:17 PM
 *
 * @author : Caron Zhao
 * @version : 1.0.0
 */
public enum SellerPaymentType {
    PAYPAL(1L, "PayPal"),
    CHECK(2L, "Check"),
    ACH(5L,"ACH"),
    CHECK_LARGE_SELLER(821L, "Check"),
    CREDIT_MY_TEAM_ACCOUNT(3L, "Credit"),
    AMERICAN_RED_CROSS(621L, "Charity"),
    SAVE_THE_CHILDREN(1500L, "Charity"),
    HUMAN_SOCIETY_OF_US(1501L, "Charity"),
    THE_SALVATION_ARMY(1502L, "Charity"),
    STUBHUB_FOUNDATION(1736L, "Charity"),
    ROBIN_HOOD_RELIEF_FUND(1636L, "Charity");

    private Long id;
    private String category;

    SellerPaymentType(Long id, String category) {
        this.id = id;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public static List<SellerPaymentType> getSellerPaymentTypesByCategory(String category) {
        List<SellerPaymentType> sellerPaymentTypeList = new ArrayList<SellerPaymentType>();
        if (category == null) {
            return sellerPaymentTypeList;
        }

        SellerPaymentType list[] = SellerPaymentType.values();
        for (SellerPaymentType sellerPaymentType : list) {
            if (sellerPaymentType.getCategory().equalsIgnoreCase(category)) {
                sellerPaymentTypeList.add(sellerPaymentType);
            }
        }

        return sellerPaymentTypeList;
    }
}