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
	@XmlRootElement(name = "discount")
	@JsonRootName("discount")
	@XmlType(name = "", propOrder = {"id", "type", "description", "usedDiscount"})

public class DiscountResponse extends Response{
		
		@XmlElement(name = "id", required = true)
		private String id;
		
		@XmlElement(name = "type", required = true)
		private String type;
		
		@XmlElement(name = "description", required = true)
		private String description;
		
		@XmlElement(name = "usedDiscount", required = true)
		private Money usedDiscount;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public Money getUsedDiscount() {
			return usedDiscount;
		}

		public void setUsedDiscount(Money usedDiscount) {
			this.usedDiscount = usedDiscount;
		}

}
