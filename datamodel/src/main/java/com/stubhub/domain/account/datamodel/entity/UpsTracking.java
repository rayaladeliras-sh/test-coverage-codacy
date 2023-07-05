package com.stubhub.domain.account.datamodel.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;

@Entity
@NamedNativeQuery(name = "UpsTracking.checkUpsOrder", query = "" +
		"SELECT sfm.tid AS order_id " +
		"FROM stub_trans_fm_dm sfm ,delivery_method dm,fulfillment_method fm " + 
		"WHERE sfm.delivery_method_id=dm.delivery_method_id " + 
		"AND sfm.fulfillment_method_id=fm.fulfillment_method_id " +
		"AND sfm.tid=:orderId " +
		"AND sfm.active=1 " +
		"AND inactive_id=0 " +
		"AND dm.delivery_type_id=5 ", resultClass=UpsTracking.class)

public class UpsTracking implements java.io.Serializable {
	
	@Id
	@Column(name = "order_id")
	private Long orderId;

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	
}




