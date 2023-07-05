package com.stubhub.domain.account.common;


public class SummaryInput {

    private Boolean includeVenueSummary;
    private Boolean includeGenreSummary;
    private Boolean includeEventSummary;

    public Boolean getIncludeVenueSummary() {
        return includeVenueSummary;
    }

    public void setIncludeVenueSummary(Boolean includeVenueSummary) {
        this.includeVenueSummary = includeVenueSummary;
    }

    public Boolean getIncludeGenreSummary() {
        return includeGenreSummary;
    }

    public void setIncludeGenreSummary(Boolean includeGenreSummary) {
        this.includeGenreSummary = includeGenreSummary;
    }

    public Boolean getIncludeEventSummary() {
        return includeEventSummary;
    }

    public void setIncludeEventSummary(Boolean includeEventSummary) {
        this.includeEventSummary = includeEventSummary;
    }
}
