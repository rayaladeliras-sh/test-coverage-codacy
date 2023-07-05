package com.stubhub.domain.account.datamodel.entity;

import java.util.Calendar;
import java.util.Currency;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity          
@Table(name = "VENUE_CONFIGURATION_ZONES")
public class VenueConfigZones implements java.io.Serializable {
	private static final long serialVersionUID = -4435057568387387960L;
	@Id
	@Column(name="ID")
	private Long id;
	
	@Column(name="VENUE_CONFIGURATION_ID")
	private Long venueConfigId;
	
	@Column(name="ZONE_DESC")
	private String zoneDesc ;
	
	@Column(name="ACTIVE")
	private Boolean active; 
	
	@Column(name="CREATED_DATE")
	private Calendar createdDate;
	
	@Column(name="LAST_UPDATED_DATE")
	private Calendar updatedDate;
	
	@Column(name="CURRENCY_CODE")
	private Currency currency ; 
	
	@Column(name="ZONE_COLOR")
	private String zoneColor;	

	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getVenueConfigId() {
		return venueConfigId;
	}
	public void setVenueConfigId(Long venueConfigId) {
		this.venueConfigId = venueConfigId;
	}
	public String getZoneDesc() {
		return zoneDesc;
	}
	public void setZoneDesc(String zoneDesc) {
		this.zoneDesc = zoneDesc;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	public Calendar getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Calendar createdDate) {
		this.createdDate = createdDate;
	}
	public Calendar getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Calendar updatedDate) {
		this.updatedDate = updatedDate;
	}
	public Currency getCurrency() {
		return currency;
	}
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}
	public String getZoneColor() {
		return zoneColor;
	}
	public void setZoneColor(String zoneColor) {
		this.zoneColor = zoneColor;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
