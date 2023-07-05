package com.stubhub.domain.account.intf;

import java.math.BigDecimal;

import lombok.Data;




@Data
public class CMAppliedHistory {
	
	private long orderId;
	
	private String currency;
	
	private BigDecimal cmAmount;
	
	private String reasonDescription;
	
	private Long creditMemoPid;
	
	public static class RawSolrCMAppliedHistory {
		
		private long cmTid;
		
		public long getCmTid() {
			return cmTid;
		}

		public void setCmTid(long cmTid) {
			this.cmTid = cmTid;
		}

		public void setCmPid(Long cmPid) {
			this.cmPid = cmPid;
		}

		public long getCmPid() {
			return cmPid;
		}

		public String getReasonDescription() {
			return reasonDescription;
		}

		public void setReasonDescription(String reasonDescription) {
			this.reasonDescription = reasonDescription;
		}

		public BigDecimal getAppliedAmount() {
			return appliedAmount;
		}

		public void setAppliedAmount(BigDecimal appliedAmount) {
			this.appliedAmount = appliedAmount;
		}

		public String getCurrencyCode() {
			return currencyCode;
		}

		public void setCurrencyCode(String currencyCode) {
			this.currencyCode = currencyCode;
		}

		public void mapToResponse(CMAppliedHistory cmAppliedHistory) {
			cmAppliedHistory.setOrderId(cmTid);
			cmAppliedHistory.setCmAmount(appliedAmount);
			cmAppliedHistory.setCreditMemoPid(cmPid);
			cmAppliedHistory.setCurrency(currencyCode);
			cmAppliedHistory.setReasonDescription(reasonDescription);
		}
		
		private Long cmPid;
		
		private String reasonDescription;
		
		private BigDecimal appliedAmount;
		
		private String currencyCode;
		
	}


}
