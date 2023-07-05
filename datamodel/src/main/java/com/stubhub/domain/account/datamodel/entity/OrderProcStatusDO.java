package com.stubhub.domain.account.datamodel.entity;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "ORDER_PROC_STATUS")	
@org.hibernate.annotations.Entity(dynamicInsert = false, dynamicUpdate = true)
@NamedNativeQuery(name = "OrderProcStatus.getOrderProcStatusByTransId", query = "" +
		"SELECT ops.*, " +
		"opssc.order_proc_status_code AS order_proc_status_code, " + 
		"opsc.order_proc_status_desc AS order_proc_status_desc, " + 
		"opssc.order_proc_sub_status_desc AS order_proc_sub_status_desc " +
		"FROM order_proc_status ops, order_proc_sub_status_code opssc, order_proc_status_code opsc " +
		"WHERE ops.tid=:arg1 " +
		"AND ops.order_proc_sub_status_code=opssc.order_proc_sub_status_code " + 
		"AND opssc.order_proc_status_code = opsc.order_proc_status_code " + 
		"ORDER BY ops.order_proc_status_eff_date DESC", resultSetMapping="OrderProcStatusByTransIdMapping")
		@SqlResultSetMapping(name="OrderProcStatusByTransIdMapping", 
				entities={@EntityResult(entityClass=com.stubhub.domain.account.datamodel.entity.OrderProcStatusDO.class)},
				columns={@ColumnResult(name="order_proc_status_code"),
				@ColumnResult(name="order_proc_status_desc"),
				@ColumnResult(name="order_proc_sub_status_desc")})	
				public class OrderProcStatusDO implements java.io.Serializable {

	@Id
	@SequenceGenerator(name="ORDER_PROC_STATUS_SEQ", sequenceName="ORDER_PROC_STATUS_SEQ", allocationSize=1, initialValue=1)
	@GeneratedValue(generator="ORDER_PROC_STATUS_SEQ", strategy=GenerationType.SEQUENCE)
	@Column(name = "ORDER_PROC_STATUS_ID")
	private Long orderProcStatusId;
	@Column(name = "TID")
	private Long tid;
	@Column(name = "ORDER_PROC_SUB_STATUS_CODE")
	private Long orderProcSubStatusCode;
	@Column(name = "ORDER_PROC_STATUS_EFF_DATE")
	@Type(type="com.stubhub.newplatform.persistence.adapter.UTCCalendarToTimestampAdapter")
	private Calendar orderProcStatusEffDate;
	@Column(name = "ORDER_PROC_STATUS_END_DATE")
	@Type(type="com.stubhub.newplatform.persistence.adapter.UTCCalendarToTimestampAdapter")
	private Calendar orderProcStatusEndDate;
	@Column(name = "CREATED_DATE")
	@Type(type="com.stubhub.newplatform.persistence.adapter.UTCCalendarToTimestampAdapter")
	private Calendar createdDate;
	@Column(name = "LAST_UPDATED_DATE")
	@Type(type="com.stubhub.newplatform.persistence.adapter.UTCCalendarToTimestampAdapter")
	private Calendar lastUpdatedDate;
	@Column(name = "CREATED_BY")
	private String createdBy;
	@Column(name = "LAST_UPDATED_BY")
	private String lastUpdatedBy;

	public Long getOrderProcStatusId() {
		return orderProcStatusId;
	}
	public void setOrderProcStatusId(Long orderProcStatusId) {
		this.orderProcStatusId = orderProcStatusId;
	}
	public Long getOrderProcSubStatusCode() {
		return orderProcSubStatusCode;
	}
	public void setOrderProcSubStatusCode(Long orderProcSubStatusCode) {
		this.orderProcSubStatusCode = orderProcSubStatusCode;
	}
	public Calendar getOrderProcStatusEffDate() {
		return orderProcStatusEffDate;
	}
	public void setOrderProcStatusEffDate(Calendar orderProcStatusEffDate) {
		this.orderProcStatusEffDate = orderProcStatusEffDate;
	}
	public Long getTid() {
		return tid;
	}
	public void setTid(Long tid) {
		this.tid = tid;
	}
	public Calendar getOrderProcStatusEndDate() {
		return orderProcStatusEndDate;
	}
	public void setOrderProcStatusEndDate(Calendar orderProcStatusEndDate) {
		this.orderProcStatusEndDate = orderProcStatusEndDate;
	}
	public Calendar getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Calendar createdDate) {
		this.createdDate = createdDate;
	}
	public Calendar getLastUpdatedDate() {
		return lastUpdatedDate;
	}
	public void setLastUpdatedDate(Calendar lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
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