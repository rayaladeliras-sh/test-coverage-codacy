package com.stubhub.domain.account.intf;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;

import com.stubhub.domain.account.common.Response;

@XmlAccessorType(XmlAccessType.FIELD)
@JsonRootName(value = "summary")
@XmlRootElement(name = "summary")
@XmlType(name = "", propOrder = {
		"userId",
		"isTopBuyer",
		"buyerFlip",
		"orders",
		"sales",
		"listings",
		"drpOrderRate",
		"beyondAudienceYNFlag",
		"beyondAudienceEffDate",
		"beyondAudienceExpDate",
		"semSegmentId",
		"semSegmentName",
		"isLargeSeller"
})
public class TransactionSummaryResponse extends Response {

	@XmlElement(name = "userId", required = false)
	@JsonProperty("userId")
	private String userId;

	@XmlElement(name = "isTopBuyer", required = false)
	@JsonProperty("isTopBuyer")
	private String isTopBuyer;

	@XmlElement(name = "buyerFlip", required = false)
	@JsonProperty("buyerFlip")
	private String buyerFlip;

	@XmlElement(name = "orders", required = false)
	@JsonProperty("orders")
	private BuysCountResponse orders;

	@XmlElement(name = "sales", required = false)
	@JsonProperty("sales")
	private SalesCountResponse sales;

	@XmlElement(name = "listings", required = false)
	@JsonProperty("listings")
	private ListingCountResponse listings;

	@XmlElement(name = "drpOrderRate", required = false)
	@JsonProperty("drpOrderRate")
	private String drpOrderRate;

	@XmlElement(name = "beyondAudienceYNFlag", required = false)
	@JsonProperty("beyondAudienceYNFlag")
	private String beyondAudienceYNFlag;

	@XmlElement(name = "beyondAudienceEffDate", required = false)
	@JsonProperty("beyondAudienceEffDate")
	private String beyondAudienceEffDate;

	@XmlElement(name = "beyondAudienceExpDate", required = false)
	@JsonProperty("beyondAudienceExpDate")
	private String beyondAudienceExpDate;

	@XmlElement(name = "semSegmentId", required = false)
	@JsonProperty("semSegmentId")
	private String semSegmentId;

	@XmlElement(name = "semSegmentName", required = false)
	@JsonProperty("semSegmentName")
	private String semSegmentName;

	@XmlElement(name = "isLargeSeller", required = false)
	@JsonProperty("isLargeSeller")
	private String isLargeSeller;

	public String getBeyondAudienceYNFlag() {
		return beyondAudienceYNFlag;
	}
	public void setBeyondAudienceYNFlag(String beyondAudienceYNFlag) {
		this.beyondAudienceYNFlag = beyondAudienceYNFlag;
	}
	public String getBeyondAudienceEffDate() {
		return beyondAudienceEffDate;
	}
	public void setBeyondAudienceEffDate(String beyondAudienceEffDate) {
		this.beyondAudienceEffDate = beyondAudienceEffDate;
	}
	public String getBeyondAudienceExpDate() {
		return beyondAudienceExpDate;
	}
	public void setBeyondAudienceExpDate(String beyondAudienceExpDate) {
		this.beyondAudienceExpDate = beyondAudienceExpDate;
	}
	public String getSemSegmentId() {
		return semSegmentId;
	}
	public void setSemSegmentId(String semSegmentId) {
		this.semSegmentId = semSegmentId;
	}
	public String getSemSegmentName() {
		return semSegmentName;
	}
	public void setSemSegmentName(String semSegmentName) {
		this.semSegmentName = semSegmentName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public BuysCountResponse getOrders() {
		return orders;
	}
	public void setOrders(BuysCountResponse orders) {
		this.orders = orders;
	}
	public SalesCountResponse getSales() {
		return sales;
	}
	public void setSales(SalesCountResponse sales) {
		this.sales = sales;
	}
	public ListingCountResponse getListings() {
		return listings;
	}
	public void setListings(ListingCountResponse listings) {
		this.listings = listings;
	}
	public String getIsTopBuyer() {
		return isTopBuyer;
	}
	public void setIsTopBuyer(String isTopBuyer) {
		this.isTopBuyer = isTopBuyer;
	}
	public void setBuyerFlip(String buyerFlip) {
		this.buyerFlip = buyerFlip;
	}
	public String getBuyerFlip() {
		return buyerFlip;
	}
	public String getDrpOrderRate() {
		return drpOrderRate;
	}
	public void setDrpOrderRate(String drpOrderRate) {
		this.drpOrderRate = drpOrderRate;
	}

	public String getIsLargeSeller() {
		return isLargeSeller;
	}

	public void setIsLargeSeller(String isLargeSeller) {
		this.isLargeSeller = isLargeSeller;
	}
}
