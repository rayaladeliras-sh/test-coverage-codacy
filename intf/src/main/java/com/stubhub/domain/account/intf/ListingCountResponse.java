package com.stubhub.domain.account.intf;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.map.annotate.JsonRootName;

import com.stubhub.domain.account.common.Response;

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlRootElement(name = "listings")
	@JsonRootName("listings")
	@XmlType(name = "", propOrder = {
			"activeListings", 
			"inactiveListings", 
			"incompleteListings", 
			"deletedListings", 
			"pendingLMSActivation", 
			"pendingLock"
			})

public class ListingCountResponse extends Response{
		
		@XmlElement(name = "activeListings", required = true)
		private String activeListings;		
		@XmlElement(name = "inactiveListings", required = true)
		private String inactiveListings;		
		@XmlElement(name = "incompleteListings", required = true)
		private String incompleteListings;
		@XmlElement(name = "deletedListings", required = true)
		private String deletedListings;		
		@XmlElement(name = "pendingLMSActivation", required = true)
		private String pendingLMSActivation;		
		@XmlElement(name = "pendingLock", required = true)
		private String pendingLock;
		public String getActiveListings() {
			return activeListings;
		}
		public void setActiveListings(String activeListings) {
			this.activeListings = activeListings;
		}
		public String getInactiveListings() {
			return inactiveListings;
		}
		public void setInactiveListings(String inactiveListings) {
			this.inactiveListings = inactiveListings;
		}
		public String getIncompleteListings() {
			return incompleteListings;
		}
		public void setIncompleteListings(String incompleteListings) {
			this.incompleteListings = incompleteListings;
		}
		public String getDeletedListings() {
			return deletedListings;
		}
		public void setDeletedListings(String deletedListings) {
			this.deletedListings = deletedListings;
		}
		public String getPendingLMSActivation() {
			return pendingLMSActivation;
		}
		public void setPendingLMSActivation(String pendingLMSActivation) {
			this.pendingLMSActivation = pendingLMSActivation;
		}
		public String getPendingLock() {
			return pendingLock;
		}
		public void setPendingLock(String pendingLock) {
			this.pendingLock = pendingLock;
		}
		
}
