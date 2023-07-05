package com.stubhub.domain.account.datamodel.enums;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TaxIdStatusTest {
	
	/*
	 * 	TIN_REQUIRED(1, "TIN REQUIRED"),
	TIN_COLLECTED(2, "TIN COLLECTED"),
	TIN_NOT_NEEDED(3, "TIN NOT NEEDED"),
	TIN_NEEDED(4, "TIN NEEDED"),
	TIN_EXEMPTED(5, "TIN EXEMPTED"),
	TIN_UNBLOCKED(6, "TIN UNBLOCKED"),
	TAX_YEAR_RESET(7, "TAX YEAR RESET"),
	TIN_INVALID(8, "TIN INVALID"),
	TIN_VALID(9, "TIN VALID");
*/
	@Test
	public void testGetSet(){
		Assert.assertTrue(TaxIdStatus.TIN_REQUIRED.getId() == 1 );
		Assert.assertTrue(TaxIdStatus.TIN_COLLECTED.getId() == 2 );
		Assert.assertTrue(TaxIdStatus.TIN_NOT_NEEDED.getId() == 3 );
		Assert.assertTrue(TaxIdStatus.TIN_NEEDED.getId() == 4 );
		Assert.assertTrue(TaxIdStatus.TIN_EXEMPTED.getId() == 5 );
		Assert.assertTrue(TaxIdStatus.TIN_UNBLOCKED.getId() == 6 );
		Assert.assertTrue(TaxIdStatus.TAX_YEAR_RESET.getId() == 7 );
		Assert.assertTrue(TaxIdStatus.TIN_INVALID.getId() == 8 );
		Assert.assertTrue(TaxIdStatus.TIN_VALID.getId() == 9 );

		Assert.assertTrue(TaxIdStatus.TIN_REQUIRED.getDescription().equals("TIN REQUIRED"));
		Assert.assertTrue(TaxIdStatus.TIN_COLLECTED.getDescription().equals("TIN COLLECTED"));
		Assert.assertTrue(TaxIdStatus.TIN_NOT_NEEDED.getDescription().equals("TIN NOT NEEDED"));
		Assert.assertTrue(TaxIdStatus.TIN_NEEDED.getDescription().equals("TIN NEEDED"));
		Assert.assertTrue(TaxIdStatus.TIN_EXEMPTED.getDescription().equals("TIN EXEMPTED"));
		Assert.assertTrue(TaxIdStatus.TIN_UNBLOCKED.getDescription().equals("TIN UNBLOCKED"));
		Assert.assertTrue(TaxIdStatus.TAX_YEAR_RESET.getDescription().equals("TAX YEAR RESET"));
		Assert.assertTrue(TaxIdStatus.TIN_INVALID.getDescription().equals("TIN INVALID"));
		Assert.assertTrue(TaxIdStatus.TIN_VALID.getDescription().equals("TIN VALID"));
		
		TaxIdStatus taxIdStatus = TaxIdStatus.getTaxIdStatus(1);
		Assert.assertTrue(taxIdStatus.getId() == 1);
		
		Integer int01 = null;
		taxIdStatus = TaxIdStatus.getTaxIdStatus(int01);
		Assert.assertNull(taxIdStatus);

		taxIdStatus = TaxIdStatus.getTaxIdStatus("TIN REQUIRED");
		Assert.assertTrue(taxIdStatus.getDescription().equals("TIN REQUIRED"));
		
		String desc01 = null;
		taxIdStatus = TaxIdStatus.getTaxIdStatus(desc01);
		Assert.assertNull(taxIdStatus);

		taxIdStatus = TaxIdStatus.getTaxIdStatus(10);
		Assert.assertNull(taxIdStatus);
		
		taxIdStatus = TaxIdStatus.getTaxIdStatus("invalid");
		Assert.assertNull(taxIdStatus);
	}
}
