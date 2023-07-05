package com.stubhub.domain.account.datamodel.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;

@Entity
@NamedNativeQueries(value = {
		@NamedNativeQuery(name = "InventoryData.getSellerInfo", query = ""
				+ "SELECT st.id," + " st.ticket_id,"
				+ " t.external_listing_id," + " t.seller_id," + " t.event_id,"
				+ " u.tt_broker_id," + " u.TT_SH_ORDER_INTG_OPTIN_IND,"
				+ " u.SHIP_ORDER_INTG_OPTIN_IND," 
				+ " t.section," + " t.row_desc," + " t.seats "
			    + " FROM stub_trans st,"
				+ " tickets t," + "  users  u "
				+ "	WHERE st.ticket_id = t.id "
				+ "	AND u.id = t.seller_id AND st.id=:orderId", resultClass = InventoryData.class)
})
public class InventoryData {
	@Id
	@Column(name = "ID")
	private Long orderId;
	@Column(name = "ticket_id")
	private Long listingId;
	@Column(name = "external_listing_id")
	private String ticTecListingId;
	@Column(name = "seller_id")
	private Long sellerId;
	@Column(name = "tt_broker_id")
	private Long brokerId;
	@Column(name = "TT_SH_ORDER_INTG_OPTIN_IND")
	private Boolean optIn;
	@Column(name = "SHIP_ORDER_INTG_OPTIN_IND")
	private Boolean partnerOrderIntegrated;
    @Column(name = "section")
	private String section;
	@Column(name = "row_desc")
	private String row;
	@Column(name = "seats")
	private String seats;
	@Column(name = "event_id")
	private Long eventId;

	


	public void setListingId(Long listingId) {
		this.listingId = listingId;
	}

	public Long getListingId() {
		return listingId;
	}

	public String getTicTecListingId() {
		return ticTecListingId;
	}

	public void setTicTecListingId(String ticTecListingId) {
		this.ticTecListingId = ticTecListingId;
	}

	public Long getSellerId() {
		return sellerId;
	}

	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}

	public Long getBrokerId() {
		return brokerId;
	}

	public void setBrokerId(Long brokerId) {
		this.brokerId = brokerId;
	}
	public Boolean getOptIn() {
		return optIn;
	}

	public void setOptIn(Boolean optIn) {
		this.optIn = optIn;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getSection() {
		return section;
	}

	public void setRow(String row) {
		this.row = row;
	}

	public String getRow() {
		return row;
	}

	public void setSeats(String seats) {
		this.seats = seats;
	}

	public String getSeats() {
		return seats;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public Long getEventId() {
		return eventId;
	}



	public Boolean getPartnerOrderIntegrated() {
		return partnerOrderIntegrated;
	}

	public void setPartnerOrderIntegrated(Boolean partnerOrderIntegrated) {
		this.partnerOrderIntegrated = partnerOrderIntegrated;
	}


}
