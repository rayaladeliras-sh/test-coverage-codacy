package com.stubhub.domain.account.datamodel.entity;

import java.util.Calendar;

import javax.persistence.*;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "STUB_TRANS_FM_DM")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
@NamedNativeQueries({
		@NamedNativeQuery(
				name = "StubTransFmDm.getFmDmByTids",
				query = "SELECT * FROM STUB_TRANS_FM_DM WHERE TID in (:tids) and ACTIVE = 1 ", resultClass = StubTransFmDm.class)
})
public class StubTransFmDm {
	@Id
	@Column(name = "STUB_TRANS_FM_DM_ID")
	@SequenceGenerator(name="STUB_TRANS_FM_DM_SEQ", sequenceName="STUB_TRANS_FM_DM_SEQ", allocationSize=1, initialValue=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="STUB_TRANS_FM_DM_SEQ")
	private Long id;
	@Column(name = "TID")
	private Long tid;
	@Column(name = "FULFILLMENT_METHOD_ID")
	private Long fulfillmentMethodId;
	@Column(name = "DELIVERY_METHOD_ID")
	private Long deliveryMethodId;
	@Column(name = "LOGISTICS_TYPE_ID")
	private Long logisticsTypeId;
	@Column(name = "ACTIVE")
	private Boolean active;
	@Column(name = "LMS_LOCATION_ID")
	private Long lmsLocationId;
	@Column(name = "CREATED_DATE")
	@Type(type="com.stubhub.newplatform.persistence.adapter.UTCCalendarToTimestampAdapter")
	private Calendar createdDate;
	@Column(name = "CREATED_BY")
	private String createdBy;
	@Column(name = "LAST_UPDATED_DATE")
	@Type(type="com.stubhub.newplatform.persistence.adapter.UTCCalendarToTimestampAdapter")
	private Calendar lastUpdatedDate;
	@Column(name = "LAST_UPDATED_BY")
	private String lastUpdatedBy;
	
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
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getTid() {
		return tid;
	}
	public void setTid(Long tid) {
		this.tid = tid;
	}
	public Long getFulfillmentMethodId() {
		return fulfillmentMethodId;
	}
	public void setFulfillmentMethodId(Long fulfillmentMethodId) {
		this.fulfillmentMethodId = fulfillmentMethodId;
	}
	public Long getDeliveryMethodId() {
		return deliveryMethodId;
	}
	public void setDeliveryMethodId(Long deliveryMethodId) {
		this.deliveryMethodId = deliveryMethodId;
	}
	public Long getLogisticsTypeId() {
		return logisticsTypeId;
	}
	public void setLogisticsTypeId(Long logisticsTypeId) {
		this.logisticsTypeId = logisticsTypeId;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	public Long getLmsLocationId() {
		return lmsLocationId;
	}
	public void setLmsLocationId(Long lmsLocationId) {
		this.lmsLocationId = lmsLocationId;
	}
}