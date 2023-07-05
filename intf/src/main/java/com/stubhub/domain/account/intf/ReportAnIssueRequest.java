package com.stubhub.domain.account.intf;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.map.annotate.JsonRootName;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "reportAnIssue")
@JsonRootName(value = "reportAnIssue")
@XmlType(name = "", propOrder = { "type", "inHandDateChangeData", "replacementData" })
public class ReportAnIssueRequest {
	
	public static enum Type {
		NOTICKET, REPLACEMENT, INHANDDATECHANGE;
	}

	@XmlType(name = "", propOrder = { "newDate", "originalDate" })
	public static class InHandDateChangeData {
		@XmlElement(name = "newDate", required = true)
		private String newDate;
		@XmlElement(name = "originalDate", required = true)
		private String originalDate;

		public String getNewDate() {
			return newDate;
		}
		public void setNewDate(String newDate) {
			this.newDate = newDate;
		}
		public String getOriginalDate() {
			return originalDate;
		}
		public void setOriginalDate(String originalDate) {
			this.originalDate = originalDate;
		}

	}

	@XmlType(name = "", propOrder = { "section", "row", "seats" })
	public static class ReplacementData {
		@XmlElement(name = "section", required = true)
		private String section;
		@XmlElement(name = "row", required = true)
		private String row;
		@XmlElement(name = "seats", required = true)
		private String seats;
		
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
		public String getSeats() {
			return seats;
		}
		public void setSeats(String seats) {
			this.seats = seats;
		}
		
	}
	
	@XmlElement(name = "type", required = true)
	private Type type;
	
	@XmlElement(name = "inHandDateChangeData", required = false)
	private InHandDateChangeData inHandDateChangeData;
	
	@XmlElement(name = "replacementData", required = false)
	private ReplacementData replacementData;
	
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	public InHandDateChangeData getInHandDateChangeData() {
		return inHandDateChangeData;
	}
	public void setInHandDateChangeData(InHandDateChangeData inHandDateChangeData) {
		this.inHandDateChangeData = inHandDateChangeData;
	}
	public ReplacementData getReplacementData() {
		return replacementData;
	}
	public void setReplacementData(ReplacementData replacementData) {
		this.replacementData = replacementData;
	}
	
}
