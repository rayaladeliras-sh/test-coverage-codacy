package com.stubhub.domain.account.datamodel.entity;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;

@Entity
@Table(name = "DELIVERIES")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)	
@NamedNativeQueries({
@NamedNativeQuery(name = "Deliveries.getByTid", query = "" +
		"SELECT " +
		"d.* FROM deliveries d " + 
		"WHERE d.tid =:orderId ", resultClass=Deliveries.class)
		})	
public class Deliveries implements java.io.Serializable {
	@Id
	@Column(name = "ID")
	private Long id;
	@Column(name = "TID")
	private Long tid;
	@Column(name = "EXPECTED_ARRIVAL_DATE")
	private Calendar expectedArrivalDate;
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
	public Calendar getExpectedArrivalDate() {
		return expectedArrivalDate;
	}
	public void setExpectedArrivalDate(Calendar expectedArrivalDate) {
		this.expectedArrivalDate = expectedArrivalDate;
	}	
}
