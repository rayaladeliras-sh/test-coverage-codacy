package com.stubhub.domain.account.datamodel.entity;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by mengli on 11/14/18.
 */
@Entity
@Table(name = "USER_BROKER_LICENSE")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
@NamedNativeQueries({
        @NamedNativeQuery(
                name="BrokerLicense.getBrokerLicensesBySellerGuid",
                query="SELECT ubl.* FROM USER_BROKER_LICENSE ubl WHERE USER_COOKIE_GUID = :brokerLicenseSellerGuid AND ACTIVE=1", resultClass=BrokerLicense.class),
        @NamedNativeQuery(
                name="BrokerLicense.getBrokerLicense",
                query="SELECT ubl.* FROM USER_BROKER_LICENSE ubl WHERE USER_BROKER_LICENSE_ID = :userBrokerLicenseId", resultClass=BrokerLicense.class)
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class BrokerLicense implements Serializable {

    @Id
    @Column(name = "USER_BROKER_LICENSE_ID")
    @SequenceGenerator(name="USER_BROKER_LICENSE_SEQ", sequenceName="USER_BROKER_LICENSE_SEQ")
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="USER_BROKER_LICENSE_SEQ")
    private long userBrokerLicenseId;

    @Column(name = "USER_ID")
    private String sellerId;

    @Column(name = "USER_COOKIE_GUID")
    private String sellerGuid;

    @Column(name = "BROKER_LICENSE_NUMBER")
    private String brokerLicenseNumber;

    @Column(name = "STATE_CODE")
    private String stateCode;

    @Column(name = "COUNTRY_CODE")
    private String countryCode;

    @Column(name = "ACTIVE")
    private Integer active;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Type(type="com.stubhub.newplatform.persistence.adapter.UTCCalendarToTimestampAdapter")
    @Column(name = "CREATED_DATE")
    private Calendar createdOn;

    @Column(name = "LAST_UPDATED_BY")
    private String lastModifiedBy;

    @Type(type="com.stubhub.newplatform.persistence.adapter.UTCCalendarToTimestampAdapter")
    @Column(name = "LAST_UPDATED_DATE")
    private Calendar lastModifiedOn;
}
