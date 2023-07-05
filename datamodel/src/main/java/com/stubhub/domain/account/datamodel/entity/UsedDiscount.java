package com.stubhub.domain.account.datamodel.entity;

import java.math.BigDecimal;
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

@Entity
@Table(name = "USED_DISCOUNTS")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)	
@NamedNativeQueries({
@NamedNativeQuery(name = "UsedDiscount.getByTid", query = "" +
		"SELECT " +
		"ud.* FROM used_discounts ud " + 
		"WHERE ud.tid =:arg1 ", resultClass=UsedDiscount.class)
		})		
public class UsedDiscount implements java.io.Serializable {
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name="USED_DISCOUNTS_ID_AUTONUM_SEQ", sequenceName="USED_DISCOUNTS_ID_AUTONUM_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="USED_DISCOUNTS_ID_AUTONUM_SEQ")
	private Long id;
	@Column(name = "TID")
	private Long tid;
	@Column(name = "DATE_ADDED")
	private Calendar dateAdded;
	@Column(name = "CREATED_BY")
	private String createdBy;
	@Column(name = "DISCOUNT_ID")
    private Long discountId;
	@Column(name = "AMOUNT_USED")
	private BigDecimal amountUsed;
	@Column(name = "CURRENCY_CODE")
	private String currencyCode;
	@Column(name = "ACTIVE")
	private int active;
	@Column(name = "LAST_UPDATED_BY")
	private String lastUpdateBy;
	@Column(name = "LAST_UPDATED_DATE")
	private Calendar lastUpdatedDate;
	
	public String getLastUpdateBy() {
		return lastUpdateBy;
	}
	public void setLastUpdateBy(String lastUpdateBy) {
		this.lastUpdateBy = lastUpdateBy;
	}
	public Calendar getLastUpdatedDate() {
		return lastUpdatedDate;
	}
	public void setLastUpdatedDate(Calendar lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
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
	public Calendar getDateAdded() {
		return dateAdded;
	}
	public void setDateAdded(Calendar dateAdded) {
		this.dateAdded = dateAdded;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Long getDiscountId() {
		return discountId;
	}
	public void setDiscountId(Long discountId) {
		this.discountId = discountId;
	}
	public BigDecimal getAmountUsed() {
		return amountUsed;
	}
	public void setAmountUsed(BigDecimal amountUsed) {
		this.amountUsed = amountUsed;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public void setActive(int active) {
		this.active = active;
	}
	public int getActive() {
		return active;
	}
	
}




