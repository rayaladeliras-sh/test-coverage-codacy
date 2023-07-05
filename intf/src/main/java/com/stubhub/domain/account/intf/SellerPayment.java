package com.stubhub.domain.account.intf;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import com.stubhub.domain.account.common.Response;
import com.stubhub.newplatform.common.entity.Money;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "payment")
@XmlType(name = "", propOrder = {
		"ID",
		"orderID",
		"paymentAmount",
		"creditMemoAmt",
		"additonalPayAmt",
		"chargeToSellerAmt",
		"sellerPaymentStatus",
		"status",
		"bookOfBusinessID",
		"currencyCode",
		"paymentInitiatedDate",
		"paymentDate",
		"dateLastModified",
		"paymentCondition",
		"paypalEmail",
		"disbursementOptionID",
		"payeeEmailID",
		"payeeName",
		"referenceNumber",
		"eventName",
		"sellerPayoutAmount",
		"paymentTypeId",
		"paymentTermId",
		"cmApplied",
    "paymentMode",
    "bankName",
    "lastFourDigits",
    "chargeSellerDetails",
    "amtPaidAfterCreditMemo",
    "reasonDescription",
    "recordType",
    "cmAppliedHistories",
    "fxDate",
		"fxRate",
		"fxFromCurrency",
		"fxToCurrency",
		"fxPostedAmount"
		})
@JsonIgnoreProperties(ignoreUnknown = true)
public class SellerPayment extends Response {

	@XmlElement(name = "ID", required = true)
	private String ID;
	
	@XmlElement(name = "orderID", required = true)
	private String orderID;
	
	@XmlElement(name = "paymentAmount", required = true)
	private Money paymentAmount;

	@XmlElement(name = "creditMemoAmt", required = false)
	private Money creditMemoAmt;
	@XmlElement(name = "additonalPayAmt", required = false)
	private Money additonalPayAmt;
	@XmlElement(name = "chargeToSellerAmt", required = false)
	private Money chargeToSellerAmt;

	
	@XmlElement(name = "paymentMode", required = true)
	private String paymentMode;
	
	@XmlElement(name = "sellerPaymentStatus", required = true)
	private String sellerPaymentStatus;
	
	@XmlElement(name = "status", required = true)
	private String status;
	
	@XmlElement(name = "bookOfBusinessID", required = true)
	private String bookOfBusinessID;

	@XmlElement(name = "currencyCode", required = false)
	private String currencyCode;

	@XmlElement(name = "paymentDate", required = true)
	private String paymentDate;	

	@XmlElement(name = "paymentInitiatedDate", required = true)
	private String paymentInitiatedDate;

	@XmlElement(name = "dateLastModified", required = false)
	private String dateLastModified;

	@XmlElement(name = "paymentCondition", required = false)
	private String paymentCondition;
	
	@XmlElement(name = "disbursementOptionID", required = true)
	private String disbursementOptionID;
	
	@XmlElement(name = "payeeEmailID", required = true)
	private String payeeEmailID;
	
	@XmlElement(name = "paypalEmail", required = true)
	private String paypalEmail;
	
	@XmlElement(name = "payeeName", required = true)
	private String payeeName;
	
	@XmlElement(name = "referenceNumber", required = true)
	private String referenceNumber;

	@XmlElement(name = "eventName", required = true)
	private String eventName;

	@XmlElement(name = "sellerPayoutAmount", required = false)
	private Money sellerPayoutAmount;
	
	@XmlElement(name = "paymentTypeId", required = false)
	private String paymentTypeId;
	
	@XmlElement(name = "paymentTermId", required = false)
	private String paymentTermId;
	
	@XmlElement(name = "cmApplied", required = false)
	private boolean cmApplied;
	
	@XmlElement(name = "bankName", required = false)
	private String bankName;
	
	@XmlElement(name = "lastFourDigits", required = false)
	private String lastFourDigits;
	
	@XmlElementWrapper(name = "chargeSellerDetails", required = false)
	@XmlElement(name = "chargeSellerDetail", required = false)
	private List<ChargeSellerDetail> chargeSellerDetails;
	
	@XmlElement(name = "amtPaidAfterCreditMemo", required = false)
	private Money amtPaidAfterCreditMemo;
	
	@XmlElement(name = "reasonDescription", required = false)
	private String reasonDescription;
	
	@XmlElement(name = "recordType", required = false)
	private String recordType;
	
	@XmlElement(name = "creditMemosApplied", required = false)
	private List<CMAppliedHistory> creditMemosApplied;
	
	public List<CMAppliedHistory> getCreditMemosApplied() {
		return creditMemosApplied;
	}
	public void setCreditMemosApplied(List<CMAppliedHistory> creditMemosApplied) {
		this.creditMemosApplied = creditMemosApplied;
	}
	public String getRecordType() {
		return recordType;
	}
	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

	@XmlElement(name = "fxDate")
	private String fxDate;

	@XmlElement(name = "fxRate")
	private String fxRate;

	@XmlElement(name = "fxFromCurrency")
	private String fxFromCurrency;

	@XmlElement(name = "fxToCurrency")
	private String fxToCurrency;

	@XmlElement(name = "fxPostedAmount")
	private Money fxPostedAmount;

	@JsonProperty("ID")
	public String getID() {
		return ID;
	}
	public void setID(String ID) {
		this.ID = ID;
	}
	public String getOrderID() {
		return orderID;
	}
	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}
	public Money getPaymentAmount() {
		return paymentAmount;
	}
	public void setPaymentAmount(Money paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public Money getCreditMemoAmt() {
		return creditMemoAmt;
	}

	public void setCreditMemoAmt(Money creditMemoAmt) {
		this.creditMemoAmt = creditMemoAmt;
	}

	public Money getAdditonalPayAmt() {
		return additonalPayAmt;
	}

	public void setAdditonalPayAmt(Money additonalPayAmt) {
		this.additonalPayAmt = additonalPayAmt;
	}

	public Money getChargeToSellerAmt() {
		return chargeToSellerAmt;
	}

	public void setChargeToSellerAmt(Money chargeToSellerAmt) {
		this.chargeToSellerAmt = chargeToSellerAmt;
	}

	public String getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}
	
	public String getSellerPaymentStatus() {
		return sellerPaymentStatus;
	}
	public void setSellerPaymentStatus(String sellerPaymentStatus) {
		this.sellerPaymentStatus = sellerPaymentStatus;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getDisbursementOptionID() {
		return disbursementOptionID;
	}
	public void setDisbursementOptionID(String disbursementOptionID) {
		this.disbursementOptionID = disbursementOptionID;
	}
	
	public String getBookOfBusinessID() {
		return bookOfBusinessID;
	}
	public void setBookOfBusinessID(String bookOfBusinessID) {
		this.bookOfBusinessID = bookOfBusinessID;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}

	public String getDateLastModified() {
		return dateLastModified;
	}

	public void setDateLastModified(String dateLastModified) {
		this.dateLastModified = dateLastModified;
	}

	public String getPaymentCondition() {
		return paymentCondition;
	}

	public void setPaymentCondition(String paymentCondition) {
		this.paymentCondition = paymentCondition;
	}

	public String getPayeeEmailID() {
		return payeeEmailID;
	}
	public void setPayeeEmailID(String payeeEmailID) {
		this.payeeEmailID = payeeEmailID;
	}
	public String getPaypalEmail() {
		return paypalEmail;
	}
	public void setPaypalEmail(String paypalEmail) {
		this.paypalEmail = paypalEmail;
	}
	public String getPayeeName() {
		return payeeName;
	}
	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}
	public String getReferenceNumber() {
		return referenceNumber;
	}
	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public String getPaymentInitiatedDate() {
		return paymentInitiatedDate;
	}
	public void setPaymentInitiatedDate(String paymentInitiatedDate) {
		this.paymentInitiatedDate = paymentInitiatedDate;
	}
	public Money getSellerPayoutAmount() {
		return sellerPayoutAmount;
	}
	public void setSellerPayoutAmount(Money sellerPayoutAmount) {
		this.sellerPayoutAmount = sellerPayoutAmount;
	}
	public String getPaymentTypeId() {
		return paymentTypeId;
	}
	public void setPaymentTypeId(String paymentTypeId) {
		this.paymentTypeId = paymentTypeId;
	}
	public String getPaymentTermId() {
		return paymentTermId;
	}
	public void setPaymentTermId(String paymentTermId) {
		this.paymentTermId = paymentTermId;
	}
	public boolean isCmApplied() {
		return cmApplied;
	}
	public void setCmApplied(boolean cmApplied) {
		this.cmApplied = cmApplied;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getLastFourDigits() {
		return lastFourDigits;
	}
	public void setLastFourDigits(String lastFourDigits) {
		this.lastFourDigits = lastFourDigits;
	}
    public List<ChargeSellerDetail> getChargeSellerDetails() {
        return chargeSellerDetails;
    }
    public void setChargeSellerDetails(List<ChargeSellerDetail> chargeSellerDetails) {
        this.chargeSellerDetails = chargeSellerDetails;
    }
	public Money getAmtPaidAfterCreditMemo() {
		return amtPaidAfterCreditMemo;
	}
	public void setAmtPaidAfterCreditMemo(Money amtPaidAfterCreditMemo) {
		this.amtPaidAfterCreditMemo = amtPaidAfterCreditMemo;
	}
	public String getReasonDescription() {
		return reasonDescription;
	}
	public void setReasonDescription(String reasonDescription) {
		this.reasonDescription = reasonDescription;
	}

	public String getFxDate() {
		return fxDate;
	}

	public void setFxDate(String fxDate) {
		this.fxDate = fxDate;
	}

	public String getFxRate() {
		return fxRate;
	}

	public void setFxRate(String fxRate) {
		this.fxRate = fxRate;
	}

	public String getFxFromCurrency() {
		return fxFromCurrency;
	}

	public void setFxFromCurrency(String fxFromCurrency) {
		this.fxFromCurrency = fxFromCurrency;
	}

	public String getFxToCurrency() {
		return fxToCurrency;
	}

	public void setFxToCurrency(String fxToCurrency) {
		this.fxToCurrency = fxToCurrency;
	}

	public Money getFxPostedAmount() {
		return fxPostedAmount;
	}

	public void setFxPostedAmount(Money fxPostedAmount) {
		this.fxPostedAmount = fxPostedAmount;
	}
}
