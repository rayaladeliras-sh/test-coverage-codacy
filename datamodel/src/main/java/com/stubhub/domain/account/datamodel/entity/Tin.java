package com.stubhub.domain.account.datamodel.entity;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * @author vpothuru
 * date 10/29/15
 *
 */

@Entity
@Table(name="TIN")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert=true)
@NamedNativeQueries({
	@NamedNativeQuery(
		name="Tin.getTinByGuid", 
		query="SELECT TIN_GUID, TIN, CREATED_BY, LAST_UPDATED_BY FROM TIN WHERE TIN_GUID = :tinGuid ", resultClass=Tin.class),
		
	@NamedNativeQuery(
		name="Tin.updateTin", 
		query = "UPDATE TIN t " +
		"SET t.TIN =:tin, " +
		"t.LAST_UPDATED_BY = :lastUpdatedBy " +
		"WHERE t.TIN_GUID = :tinGuid ", resultClass=Tin.class)
	})

public class Tin implements Serializable {

	private static final long serialVersionUID = -6225268110501687722L;
	
	@Id
	@GeneratedValue(generator="db-guid")
	@GenericGenerator(name="db-guid", strategy = "guid")
	@Column(name="TIN_GUID")
	private String tinGuid;

    @Column(name="TIN")
    private String tin;
    
    @Column(name="CREATED_BY")
    private String createdBy;
    
	@Column(name="LAST_UPDATED_BY")
    private String lastUpdatedBy;

	public String getTinGuid() {
		return tinGuid;
	}

	public void setTinGuid(String tinGuid) {
		this.tinGuid = tinGuid;
	}

	public String getTin() {
		return tin;
	}

	public void setTin(String tin) {
		this.tin = tin;
	}

    public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

}
