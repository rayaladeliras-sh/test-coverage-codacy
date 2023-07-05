package com.stubhub.domain.account.intf;

import org.codehaus.jackson.map.annotate.JsonRootName;

import javax.xml.bind.annotation.*;
import java.util.Calendar;
import java.util.Date;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "UpdatePaymentStatusRequest")
@JsonRootName(value = "UpdatePaymentStatusRequest")
@XmlType(name = "", propOrder = { "userId", "action", "latestPaymentDate"})
public class UpdatePaymentStatusRequest {

	@XmlElement(name = "userId")
	private Long userId;

	@XmlElement(name = "action")
	private Action action;

	@XmlElement(name = "latestPaymentDate")
	private Calendar latestPaymentDate;

	public Calendar getLatestPaymentDate() {
		return latestPaymentDate;
	}

	public void setLatestPaymentDate(Calendar latestPaymentDate) {
		this.latestPaymentDate = latestPaymentDate;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public static enum Action {
		RELEASE_PAYMENT_FOR_DUE_DILIGENCE("Release Payment for Due Diligence"),
		HOLD_PAYMENT_FOR_DUE_DILIGENCE("Hold Payment for Due Diligence");

		private String value;

		Action(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}
}