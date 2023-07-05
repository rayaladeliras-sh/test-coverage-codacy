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
	@XmlRootElement(name = "sales")
	@JsonRootName("sales")
	@XmlType(name = "", propOrder = {
			"completedSales", 
			"salesTotalPayment", 
			"unconfirmedSales", 
			"cancelledSales"
			})
public class SalesCountResponse extends Response{
		
		@XmlElement(name = "completedSales", required = true)
		private String completedSales;		
		@XmlElement(name = "salesTotalPayment", required = true)
		private Money salesTotalPayment;		
		@XmlElement(name = "unconfirmedSales", required = true)
		private String unconfirmedSales;		
		@XmlElement(name = "cancelledSales", required = true)
		private String cancelledSales;
		
		public String getCompletedSales() {
			return completedSales;
		}
		public void setCompletedSales(String completedSales) {
			this.completedSales = completedSales;
		}
		public String getUnconfirmedSales() {
			return unconfirmedSales;
		}
		public void setUnconfirmedSales(String unconfirmedSales) {
			this.unconfirmedSales = unconfirmedSales;
		}
		public String getCancelledSales() {
			return cancelledSales;
		}
		public void setCancelledSales(String cancelledSales) {
			this.cancelledSales = cancelledSales;
		}
		public Money getSalesTotalPayment() {
			return salesTotalPayment;
		}
		public void setSalesTotalPayment(Money salesTotalPayment) {
			this.salesTotalPayment = salesTotalPayment;
		}
		
}
