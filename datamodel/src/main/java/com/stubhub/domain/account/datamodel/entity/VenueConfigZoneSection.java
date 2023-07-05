package com.stubhub.domain.account.datamodel.entity;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;

@Entity
@NamedNativeQueries(value = { 
		@NamedNativeQuery(name = "VenueConfigZoneSection.getZoneSectionByVenueConfigId", query = "SELECT VZCS.* ,VCZ.* "+
				" FROM VENUE_CONFIGURATION_ZONES  VCZ, VENUE_CONFIG_ZONE_SECTIONS VZCS " +
				" WHERE VCZ.ID = VZCS.VENUE_CONFIGURATION_ZONE_ID AND VCZ.ACTIVE = 1 AND VZCS.ACTIVE = 1 "+
				" AND VCZ.VENUE_CONFIGURATION_ID = :venue_config_id ", resultClass = VenueConfigZoneSection.class)})
				
@Table(name = "VENUE_CONFIG_ZONE_SECTIONS")
public class VenueConfigZoneSection  implements java.io.Serializable {
	
	private static final long serialVersionUID = -4435057568387387960L;
	
	@Id
	@Column(name="ID")
	private Long id;
	
	@Column(name="VENUE_CONFIGURATION_ZONE_ID")
	private Long zoneId ;	
	
	@Column(name="VENUE_CONFIG_SECTIONS_ID")
	private Long sectionId; 
	
	@Column(name="CREATED_DATE")
	private Calendar createdDate;
	
	@Column(name="LAST_UPDATED_DATE")
	private Calendar updatedDate; 
	
	@Column(name="ACTIVE")
	private Boolean active;
	
	@ManyToOne
    @JoinColumn(name="VENUE_CONFIGURATION_ZONE_ID", insertable=false, updatable=false)
   	private VenueConfigZones venueConfigZones;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}	
	public Long getZoneId() {
		return zoneId;
	}
	public void setZoneId(Long zoneId) {
		this.zoneId = zoneId;
	}
	public Long getSectionId() {
		return sectionId;
	}
	public void setSectionId(Long sectionId) {
		this.sectionId = sectionId;
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
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public VenueConfigZones getVenueConfigZones() {
		return venueConfigZones;
	}
	public void setVenueConfigZones(VenueConfigZones venueConfigZones) {
		this.venueConfigZones = venueConfigZones;
	}
}
