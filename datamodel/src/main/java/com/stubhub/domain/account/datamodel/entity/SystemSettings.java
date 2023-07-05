package com.stubhub.domain.account.datamodel.entity;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Calendar;

/**
 * @author yuntzhao
 */
@Entity(name = "com.stubhub.domain.account.datamodel.entity.SystemSettings")
@Table(name = "SYSTEM_SETTINGS")
public class SystemSettings {
    @Id
    @Column(name = "NAME")
    private String Name;

    @Column(name = "VALUE")
    private String Value;

    @Column(name = "DESCRIPTION")
    private String Description;

    @Type(type = "com.stubhub.newplatform.persistence.adapter.UTCCalendarToTimestampAdapter")
    @Column(name = "CREATED_DATE")
    private Calendar createdDate;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Type(type = "com.stubhub.newplatform.persistence.adapter.UTCCalendarToTimestampAdapter")
    @Column(name = "LAST_UPDATED_DATE")
    private Calendar lastUpdatedDate;

    @Column(name = "LAST_UPDATED_BY")
    private String lastUpdatedBy;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public Calendar getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Calendar createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Calendar getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(Calendar lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

}

