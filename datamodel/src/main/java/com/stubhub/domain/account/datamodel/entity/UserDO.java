package com.stubhub.domain.account.datamodel.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;

/**
 * Created at 11/5/15 2:08 PM
 *
 * @author : Caron Zhao
 * @version : 1.0.0
 */

@NamedNativeQueries({@NamedNativeQuery(name = "User.getSellerPaymentType", query = ""
	+ "SELECT usr.seller_payment_type_id "
	+ "FROM USERS usr WHERE usr.id =:userId ", resultClass = UserDO.class)
})
@Entity
@Table(name ="USERS")
public class UserDO {

    @Id
    @Column(name = "ID")
    private Long userId;

    @Column(name = "USER_COOKIE_GUID")
    private String userGuid;

    @Column(name = "SELLER_SEGMENT_ID")
    private Long sellerSegmentId;

    @Column(name = "SEM_SEGMENT_ID")
    private String semSegMentId;

    @Column(name = "SEM_SEGMENT_NAME")
    private String semSegMentName;

    @Column(name = "SELLER_SUPPORT_TIER_SEGMENT_ID")
    private String sellerSupportTierSegMentId;

    @Column(name = "BEYOND_AUDIENCE_YN_FLAG")
    private String beyondAudienceYNFlag;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserGuid() {
        return userGuid;
    }

    public void setUserGuid(String userGuid) {
        this.userGuid = userGuid;
    }

    public Long getSellerSegmentId() {
        return sellerSegmentId;
    }

    public void setSellerSegmentId(Long sellerSegmentId) {
        this.sellerSegmentId = sellerSegmentId;
    }

    public String getSemSegMentId() {
        return semSegMentId;
    }

    public void setSemSegMentId(String semSegMentId) {
        this.semSegMentId = semSegMentId;
    }

    public String getSemSegMentName() {
        return semSegMentName;
    }

    public void setSemSegMentName(String semSegMentName) {
        this.semSegMentName = semSegMentName;
    }

    public String getSellerSupportTierSegMentId() {
        return sellerSupportTierSegMentId;
    }

    public void setSellerSupportTierSegMentId(String sellerSupportTierSegMentId) {
        this.sellerSupportTierSegMentId = sellerSupportTierSegMentId;
    }

    public String getBeyondAudienceYNFlag() {
        return beyondAudienceYNFlag;
    }

    public void setBeyondAudienceYNFlag(String beyondAudienceYNFlag) {
        this.beyondAudienceYNFlag = beyondAudienceYNFlag;
    }
}