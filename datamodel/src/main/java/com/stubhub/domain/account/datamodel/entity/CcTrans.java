package com.stubhub.domain.account.datamodel.entity;

import java.util.Calendar;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "CC_TRANS")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
@NamedNativeQueries(value ={
		@NamedNativeQuery(name = "ccTrans.updateByTid", query = "" +
				"UPDATE cc_trans " +
				"SET tid =:newTid, last_updated_date =:lastUpdatedDate, last_updated_by =:lastUpdatedBy " +
				"WHERE tid =:oldTid ", resultClass=CcTrans.class)})
public class CcTrans{
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name="CC_TRANS_SEQ", sequenceName="CC_TRANS_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CC_TRANS_SEQ")
	private Long id;
	@Column(name = "TID")
	private Long tid;
	@Type(type="com.stubhub.newplatform.persistence.adapter.UTCCalendarToTimestampAdapter")
	@Column(name = "LAST_UPDATED_DATE")
	private Calendar lastUpdatedDate;
	@Column(name = "LAST_UPDATED_BY")
	private String lastUpdatedBy;
	
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
	public Calendar getLastUpdatedDate() {
		return lastUpdatedDate;
	}
	public void setLastUpdatedDate(Calendar lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}
}

