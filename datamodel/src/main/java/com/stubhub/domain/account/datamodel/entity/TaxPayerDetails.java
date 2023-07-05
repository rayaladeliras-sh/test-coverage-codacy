package com.stubhub.domain.account.datamodel.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;

/**
 * @author vpothuru
 * date 10/29/15
 *
 */

@Entity
@NamedNativeQueries({
	@NamedNativeQuery(
		name="TaxPayer.getTaxPayerByUserId", 
		query="SELECT * FROM TAXPAYER WHERE USER_ID=:userId and ACTIVE = 1 ", resultClass=TaxPayerDetails.class),
		
	@NamedNativeQuery(
		name="TaxPayer.updateTaxPayerWithAddress", 
		query = "update TAXPAYER set "
				+ "TAXPAYER_COLLECT_STATUS_ID = :taxIdStatus, "
				+ "TIN_TYPE = :tinType, "
				+ "TIN_GUID = :tinGuid, "
				+ "NAME_FIRST = :nameFirst, "
				+ "NAME_LAST = :nameLast, "
				+ "COMPANY = :company, "
				+ "ADDR1 = :addr1, "
				+ "ADDR2 = :addr2, "
				+ "ADDR_CITY = :addrCity, "
				+ "ADDR_STATE = :addrState, "
				+ "ADDR_ZIP = :addrZip, "
				+ "PHONE1 = :phone1, "
				+ "ADDR_COUNTRY = :addrCountry, "
				+ "TAX_AUTHORITY_COUNTRY_CODE = :taxCountryCode, "
				+ "COUNTRY_CALLING_CODE = :countryCallingCode, "
				+ "LAST_UPDATED_BY = :lastUpdateBy "
				+ "where USER_ID = :userId and "
				+ "ACTIVE = 1 ", resultClass=TaxPayerDetails.class),
	
	@NamedNativeQuery(
			name="TaxPayer.updateTaxPayerWithOutAddress", 
			query = "update TAXPAYER set "
					+ "TAXPAYER_COLLECT_STATUS_ID = :taxIdStatus, "
					+ "TIN_TYPE = :tinType, "
					+ "TIN_GUID = :tinGuid, "
					+ "LAST_UPDATED_BY = :lastUpdateBy "
					+ "where USER_ID = :userId and "
					+ "ACTIVE = 1 ", resultClass=TaxPayerDetails.class)
	})

@Table(name="TAXPAYER")
public class TaxPayerDetails implements Serializable {

	private static final long serialVersionUID = -7283743318370174718L;
	
	@Id
	@Column(name="TAXPAYER_ID")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TAXPAYER_SEQ")
	@SequenceGenerator(name="TAXPAYER_SEQ", sequenceName="TAXPAYER_SEQ", allocationSize=1)
	private Long taxpayerId;
	
	@Column(name="USER_ID")
	private Long userId;
	
	@Column(name="TAXPAYER_COLLECT_STATUS_ID")
	private Integer taxIdStatus;
	
	@Column(name="TIN_GUID")
	private String tinGuid;
	
	@Column(name="TIN_TYPE")
	private String tinType;
	
	@Column(name="NOTES")
	private String taxPayerNotes;

	@Column(name="NAME_FIRST")
	private String nameFirst;
	
	@Column(name="NAME_LAST")
	private String nameLast;
	
	@Column(name="COMPANY")
	private String company;
	
	@Column(name="ADDR1")
	private String addr1;
	
	@Column(name="ADDR2")
	private String addr2;
	
	@Column(name="ADDR_CITY")
	private String addrCity;
	
	@Column(name="ADDR_STATE")
	private String addrState;
	
	@Column(name="ADDR_ZIP")
	private String addrZip;
	
	@Column(name="PHONE1")
	private String phone1;
	
	@Column(name="ADDR_COUNTRY")
	private String addrCountry;
	
	@Column(name="ACTIVE")
	private Integer active;
	
	@Column(name="TAX_AUTHORITY_COUNTRY_CODE")
	private String taxCountry;
	
	@Column(name="COUNTRY_CALLING_CODE")
	private String countryCallingCode;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Column(name = "LAST_UPDATED_BY")
	private String lastUpdatedBy;

	public Long getTaxpayerId() {
		return taxpayerId;
	}

	public void setTaxpayerId(Long taxpayerId) {
		this.taxpayerId = taxpayerId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getTaxIdStatus() {
		return taxIdStatus;
	}

	public void setTaxIdStatus(Integer taxIdStatus) {
		this.taxIdStatus = taxIdStatus;
	}

	public String getTinGuid() {
		return tinGuid;
	}

	public void setTinGuid(String tinGuid) {
		this.tinGuid = tinGuid;
	}

	public String getTinType() {
		return tinType;
	}

	public void setTinType(String tinType) {
		this.tinType = tinType;
	}

	public String getTaxPayerNotes() {
		return taxPayerNotes;
	}

	public void setTaxPayerNotes(String taxPayerNotes) {
		this.taxPayerNotes = taxPayerNotes;
	}

	public String getNameFirst() {
		return nameFirst;
	}

	public void setNameFirst(String nameFirst) {
		this.nameFirst = nameFirst;
	}

	public String getNameLast() {
		return nameLast;
	}

	public void setNameLast(String nameLast) {
		this.nameLast = nameLast;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getAddr1() {
		return addr1;
	}

	public void setAddr1(String addr1) {
		this.addr1 = addr1;
	}

	public String getAddr2() {
		return addr2;
	}

	public void setAddr2(String addr2) {
		this.addr2 = addr2;
	}

	public String getAddrCity() {
		return addrCity;
	}

	public void setAddrCity(String addrCity) {
		this.addrCity = addrCity;
	}

	public String getAddrState() {
		return addrState;
	}

	public void setAddrState(String addrState) {
		this.addrState = addrState;
	}

	public String getAddrZip() {
		return addrZip;
	}

	public void setAddrZip(String addrZip) {
		this.addrZip = addrZip;
	}

	public String getPhone1() {
		return phone1;
	}

	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}

	public String getAddrCountry() {
		return addrCountry;
	}

	public void setAddrCountry(String addrCountry) {
		this.addrCountry = addrCountry;
	}

	public Integer getActive() {
		return active;
	}

	public void setActive(Integer active) {
		this.active = active;
	}

	public String getTaxCountry() {
		return taxCountry;
	}

	public void setTaxCountry(String taxCountry) {
		this.taxCountry = taxCountry;
	}

	public String getCountryCallingCode() {
		return countryCallingCode;
	}

	public void setCountryCallingCode(String countryCallingCode) {
		this.countryCallingCode = countryCallingCode;
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
