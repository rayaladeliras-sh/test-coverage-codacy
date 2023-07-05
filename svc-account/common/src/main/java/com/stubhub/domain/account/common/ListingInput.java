package com.stubhub.domain.account.common;

/**
 * Created by mengli on 4/10/19.
 */
public class ListingInput {
    private Boolean includeDeleted;
    private Boolean includeSold;

    public Boolean getIncludeDeleted() {
        return includeDeleted;
    }

    public void setIncludeDeleted(Boolean includeDeleted) {
        this.includeDeleted = includeDeleted;
    }

    public Boolean getIncludeSold() {
        return includeSold;
    }

    public void setIncludeSold(Boolean includeSold) {
        this.includeSold = includeSold;
    }
}
