package com.stubhub.domain.account.datamodel.enums;

/**
 * Created at 11/5/15 1:47 PM
 *
 * @author : Caron Zhao
 * @version : 1.0.0
 */
public enum UserSegment {
    DIRECT_TO_HOST(1323, "Direct to Host");

    private Integer id;
    private String description;

    UserSegment (Integer id, String description) {
        this.id = id;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
}