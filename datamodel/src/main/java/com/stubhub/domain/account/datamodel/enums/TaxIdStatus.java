package com.stubhub.domain.account.datamodel.enums;

/**
 * @author vpothuru
 * date 10/29/15
 *
 */
public enum TaxIdStatus {
	
	TIN_REQUIRED(1, "TIN REQUIRED"),
	TIN_COLLECTED(2, "TIN COLLECTED"),
	TIN_NOT_NEEDED(3, "TIN NOT NEEDED"),
	TIN_NEEDED(4, "TIN NEEDED"),
	TIN_EXEMPTED(5, "TIN EXEMPTED"),
	TIN_UNBLOCKED(6, "TIN UNBLOCKED"),
	TAX_YEAR_RESET(7, "TAX YEAR RESET"),
	TIN_INVALID(8, "TIN INVALID"),
	TIN_VALID(9, "TIN VALID");
	
    private Integer id;
    private String description;
    
    TaxIdStatus (Integer id, String description) {
    	this.id=id;
    	this.description=description;
    }
    
    public final static TaxIdStatus getTaxIdStatus(Integer id) {
    	
    	if (id == null) return null;
    	
    	TaxIdStatus[] taxIdStatusList = TaxIdStatus.values();
    	for(TaxIdStatus taxIdStatus : taxIdStatusList) {
    		if(taxIdStatus.getId().equals(id)) {
    			return taxIdStatus;
    		}
    	}
    	
    	return null;
    }

    public final static TaxIdStatus getTaxIdStatus(String desc) {
    	
    	if (desc == null) return null;
    	
    	TaxIdStatus[] taxIdStatusList = TaxIdStatus.values();
    	for(TaxIdStatus taxIdStatus : taxIdStatusList) {
    		if(taxIdStatus.description.equalsIgnoreCase(desc)) {
    			return taxIdStatus;
    		}
    	}
    	
    	return null;
    }

	public Integer getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

}
