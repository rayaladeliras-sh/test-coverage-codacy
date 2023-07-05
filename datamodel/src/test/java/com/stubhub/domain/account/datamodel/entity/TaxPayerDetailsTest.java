package com.stubhub.domain.account.datamodel.entity;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TaxPayerDetailsTest {
	
	private Long taxpayerId = 123L;
	private Long userId = 234L;
	private Integer taxIdStatus = 100;
	private String tinGuid = "tin guid";
	private String tinType = "tin type";
	private String taxPayerNotes  = "taxpayer notes";
	private String nameFirst = "name first";
	private String nameLast = "name last";
	private String company = "company";
	private String addr1 = "addr 1";
	private String addr2 = "addr 2";
	private String addrCity = "addr city";
	private String addrState = "addr state";
	private String addrZip = "addr zip";
	private String phone1 = "phone one";
	private String addrCountry = "addr country";
	private Integer active = 0;
	private String taxCountry = "tax country";
	private String countryCallingCode = "country calling code";
	private String createdBy = "created by";
	private String lastUpdatedBy = "last update by";
	
	TaxPayerDetails tpd = new TaxPayerDetails();
	
	@Test
	public void testGetSet() {
		
		tpd.setTaxpayerId(taxpayerId);
		tpd.setUserId(userId);
		tpd.setTaxIdStatus(taxIdStatus);
		tpd.setTinGuid(tinGuid);
		tpd.setTinType(tinType);
		tpd.setTaxPayerNotes(taxPayerNotes);
		tpd.setNameFirst(nameFirst);
		tpd.setNameLast(nameLast);
		tpd.setCompany(company);
		tpd.setAddr1(addr1);
		tpd.setAddr2(addr2);
		tpd.setAddrCity(addrCity);
		tpd.setAddrState(addrState);
		tpd.setAddrZip(addrZip);
		tpd.setPhone1(phone1);
		tpd.setAddrCountry(addrCountry);
		tpd.setActive(active);
		tpd.setTaxCountry(taxCountry);
		tpd.setCountryCallingCode(countryCallingCode);
		tpd.setCreatedBy(createdBy);
		tpd.setLastUpdatedBy(lastUpdatedBy);
		
		Assert.assertEquals(tpd.getTaxpayerId(),taxpayerId);
		Assert.assertEquals(tpd.getUserId(),userId);
		Assert.assertEquals(tpd.getTaxIdStatus(),taxIdStatus);
		Assert.assertEquals(tpd.getTinGuid(),tinGuid);
		Assert.assertEquals(tpd.getTinType(),tinType);
		Assert.assertEquals(tpd.getTaxPayerNotes(),taxPayerNotes);
		Assert.assertEquals(tpd.getNameFirst(),nameFirst);
		Assert.assertEquals(tpd.getNameLast(),nameLast);
		Assert.assertEquals(tpd.getCompany(),company);
		Assert.assertEquals(tpd.getAddr1(),addr1);
		Assert.assertEquals(tpd.getAddr2(),addr2);
		Assert.assertEquals(tpd.getAddrCity(),addrCity);
		Assert.assertEquals(tpd.getAddrState(),addrState);
		Assert.assertEquals(tpd.getAddrZip(),addrZip);
		Assert.assertEquals(tpd.getPhone1(),phone1);
		Assert.assertEquals(tpd.getAddrCountry(),addrCountry);
		Assert.assertEquals(tpd.getActive(),active);
		Assert.assertEquals(tpd.getTaxCountry(),taxCountry);
		Assert.assertEquals(tpd.getCountryCallingCode(),countryCallingCode);
		Assert.assertEquals(tpd.getCreatedBy(),createdBy);
		Assert.assertEquals(tpd.getLastUpdatedBy(),lastUpdatedBy);
	}
}
