package com.stubhub.integration.partnerorder.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "id", "section", "row", "seat", "barcode", 
		"barcodeDisplayType", "status", "price"})
@XmlRootElement(name = "Ticket")
public class PartnerOrderResponseTicket {
	
	@XmlElement(name = "Id", required = true)
	private String id;
	@XmlElement(name = "Section", required = true)
	private String section;
	@XmlElement(name = "Row", required = true)
	private String row; 
	@XmlElement(name = "Seat", required = true)
	private String seat;
	@XmlElement(name = "Barcode", required = false)
	private String barcode;
	@XmlElement(name = "BarcodeDisplayType", required = false)
	private String barcodeDisplayType;
	@XmlElement(name = "Status", required = true)
	private String status;
	@XmlElement(name = "Price", required = true)
	private Price price;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}
	public String getRow() {
		return row;
	}
	public void setRow(String row) {
		this.row = row;
	}
	public String getSeat() {
		return seat;
	}
	public void setSeat(String seat) {
		this.seat = seat;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getBarcodeDisplayType() {
		return barcodeDisplayType;
	}
	public void setBarcodeDisplayType(String barcodeDisplayType) {
		this.barcodeDisplayType = barcodeDisplayType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Price getPrice() {
		return price;
	}
	public void setPrice(Price price) {
		this.price = price;
	}
	
	@Override
	public String toString() {
		return "PurchaseInventoryTicket [Id=" + id + ", Section=" + section +  ", Row=" + row +
				", Seat=" + seat + ", Barcode=" + barcode + ", BarcodeDisplayType=" + barcodeDisplayType +
				", Status=" + status +", Price=" + price +"]";
	}
}
