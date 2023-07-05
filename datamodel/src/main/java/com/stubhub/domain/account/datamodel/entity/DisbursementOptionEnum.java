package com.stubhub.domain.account.datamodel.entity;

public enum DisbursementOptionEnum {
	
	AUTO(1L, "Auto Disburse"),
	MANUAL(2L, "Manual Disburse");

	private Long id;
	private String name;
	private DisbursementOptionEnum(Long id, String name) {
		this.id = id;
		this.name = name;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public static DisbursementOptionEnum getDisbursementOptionById(Long id){
		for(DisbursementOptionEnum e:DisbursementOptionEnum.values()){
			if(e.getId().equals(id)){
				return e;
			}
		}
		return null;
	}
}
