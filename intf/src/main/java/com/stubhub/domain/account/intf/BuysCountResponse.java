package com.stubhub.domain.account.intf;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.map.annotate.JsonRootName;

import com.stubhub.domain.account.common.Response;
import com.stubhub.newplatform.common.entity.Money;

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlRootElement(name = "orders")
	@JsonRootName("orders")
	@XmlType(name = "", propOrder = {
			"purchaseCount", 
			"purchaseTotal", 
			"openOrderPurchaseTotal",
			"avgOrderSize",
			"completedOrders", 
			"cancelledOrders",
			"unconfirmedOrders",
			"earliestEventDateUTC", 
			"earliestEventDateLocal"
			})

public class BuysCountResponse extends Response{
		
		@XmlElement(name = "purchaseCount", required = true)
		private String purchaseCount;		
		@XmlElement(name = "purchaseTotal", required = true)
		private Money purchaseTotal;		
		@XmlElement(name = "unconfirmedOrders", required = true)
		private String unconfirmedOrders;
		@XmlElement(name = "openOrderPurchaseTotal", required = true)
		private Money openOrderPurchaseTotal;
		@XmlElement(name = "avgOrderSize", required = true)
		private Money avgOrderSize;
		@XmlElement(name = "completedOrders", required = true)
		private String completedOrders;
		@XmlElement(name = "cancelledOrders", required = true)
		private String cancelledOrders;
		@XmlElement(name = "earliestEventDateUTC", required = true)
		private String earliestEventDateUTC;
		@XmlElement(name = "earliestEventDateLocal", required = true)
		private String earliestEventDateLocal;
		
		public String getPurchaseCount() {
			return purchaseCount;
		}
		public void setPurchaseCount(String purchaseCount) {
			this.purchaseCount = purchaseCount;
		}
		public String getCompletedOrders() {
			return completedOrders;
		}
		public void setCompletedOrders(String completedOrders) {
			this.completedOrders = completedOrders;
		}
		public String getCancelledOrders() {
			return cancelledOrders;
		}
		public void setCancelledOrders(String cancelledOrders) {
			this.cancelledOrders = cancelledOrders;
		}
		public String getEarliestEventDateUTC() {
			return earliestEventDateUTC;
		}
		public void setEarliestEventDateUTC(String earliestEventDateUTC) {
			this.earliestEventDateUTC = earliestEventDateUTC;
		}
		public String getEarliestEventDateLocal() {
			return earliestEventDateLocal;
		}
		public void setEarliestEventDateLocal(String earliestEventDateLocal) {
			this.earliestEventDateLocal = earliestEventDateLocal;
		}
		public Money getPurchaseTotal() {
			return purchaseTotal;
		}
		public void setPurchaseTotal(Money purchaseTotal) {
			this.purchaseTotal = purchaseTotal;
		}
		public Money getOpenOrderPurchaseTotal() {
			return openOrderPurchaseTotal;
		}
		public void setOpenOrderPurchaseTotal(Money openOrderPurchaseTotal) {
			this.openOrderPurchaseTotal = openOrderPurchaseTotal;
		}
		public Money getAvgOrderSize() {
			return avgOrderSize;
		}
		public void setAvgOrderSize(Money avgOrderSize) {
			this.avgOrderSize = avgOrderSize;
		}
		public void setUnconfirmedOrders(String unconfirmedOrders) {
			this.unconfirmedOrders = unconfirmedOrders;
		}
		public String getUnconfirmedOrders() {
			return unconfirmedOrders;
		}		
}
