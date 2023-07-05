package com.stubhub.domain.account.datamodel.entity;

import java.util.Calendar;

import javax.persistence.*;

import org.hibernate.annotations.Type;

@Entity
@SqlResultSetMappings({
    @SqlResultSetMapping(name = "countMapping", columns = {
        @ColumnResult(name = "count")})
})
@NamedNativeQueries(value = {
		@NamedNativeQuery(name = "SellerPayments.getSellerPaymentsDefault", query = "SELECT sp.ID as ID, TID,sp.SELLER_ID as SELLER_ID, sp.SELLER_PAYMENT_STATUS_ID as SELLER_PAYMENT_STATUS_ID, AMOUNT, CURRENCY_CODE," +
				" BOOK_OF_BUSINESS_ID, EVENT_DATE_LOCAL, EVENT_DATE, EVENT_ID, ORDER_STATUS, REASON_DESCRIPTION, REFERENCE_NUMBER, DATE_ADDED, sph.SELLER_PAYMENT_STATUS_EFF_DATE as SELLER_PAYMENT_STATUS_EFF_DATE" +
				" from SELLER_PAYMENTS SP, SELLER_PAYMENT_STATUS_HIST SPH" +
				" WHERE sp.seller_id = :sellerId" +
				" AND record_type = :recordType" +
				" AND sph.pid(+) = sp.id" +
				" AND sph.seller_payment_status_id(+) = 22" +
				" AND sp.date_added > sysdate - :days" +
				" ORDER BY DATE_ADDED DESC", resultClass = SellerPayments.class),
				
		@NamedNativeQuery(name = "SellerPayments.getSellerPaymentsWithFilters", query = "SELECT sp.ID as ID, TID,sp.SELLER_ID as SELLER_ID, sp.SELLER_PAYMENT_STATUS_ID as SELLER_PAYMENT_STATUS_ID, AMOUNT, CURRENCY_CODE," +
				" BOOK_OF_BUSINESS_ID, EVENT_DATE_LOCAL, EVENT_DATE, EVENT_ID, ORDER_STATUS, REASON_DESCRIPTION, REFERENCE_NUMBER, DATE_ADDED, sph.SELLER_PAYMENT_STATUS_EFF_DATE as SELLER_PAYMENT_STATUS_EFF_DATE" +
				" from SELLER_PAYMENTS SP, SELLER_PAYMENT_STATUS_HIST SPH" +
				" WHERE sp.seller_id = :sellerId" +
				" AND record_type = :recordType" +
				" AND sph.pid(+) = sp.id" +
				" AND sph.seller_payment_status_id(+) = 22" +
				" AND sp.date_added between :createdFromDate and :createdToDate" +
				" ORDER BY DATE_ADDED DESC", resultClass = SellerPayments.class),

		@NamedNativeQuery(name = "SellerPayments.getSellerPaymentsWithFiltersCurrency", query = "SELECT sp.ID as ID, TID,sp.SELLER_ID as SELLER_ID, sp.SELLER_PAYMENT_STATUS_ID as SELLER_PAYMENT_STATUS_ID, AMOUNT, CURRENCY_CODE," +
				" BOOK_OF_BUSINESS_ID, EVENT_DATE_LOCAL, EVENT_DATE, EVENT_ID, ORDER_STATUS, REASON_DESCRIPTION, REFERENCE_NUMBER, DATE_ADDED, sph.SELLER_PAYMENT_STATUS_EFF_DATE as SELLER_PAYMENT_STATUS_EFF_DATE" +
				" from SELLER_PAYMENTS SP, SELLER_PAYMENT_STATUS_HIST SPH" +
				" WHERE sp.seller_id = :sellerId" +
				" AND record_type = :recordType" +
				" AND currency_code = :currencyCode" +
				" AND sph.pid(+) = sp.id" +
				" AND sph.seller_payment_status_id(+) = 22" +
				" AND sp.date_added between :createdFromDate and :createdToDate" +
				" ORDER BY DATE_ADDED DESC", resultClass = SellerPayments.class),
				
				@NamedNativeQuery(name = "SellerPayments.getIndyPaymentsWithFilters", query = "SELECT " +
						"  sp.ID                              AS ID, " +
						"  sp.TID, " +
						"  sp.SELLER_ID                       AS SELLER_ID, " +
						"  sp.SELLER_PAYMENT_STATUS_ID        AS SELLER_PAYMENT_STATUS_ID, " +
						"  (CASE nvl(sp.CM_BALANCED_AMT, 0) " +
						"   WHEN 0 " +
						"     THEN sp.AMOUNT " +
						"   ELSE sp.CM_BALANCED_AMT END)      AS AMOUNT, " +
						"  sp.CURRENCY_CODE, " +
						"  sp.BOOK_OF_BUSINESS_ID, " +
						"  sp.EVENT_DATE_LOCAL, " +
						"  sp.EVENT_DATE, " +
						"  sp.EVENT_ID, " +
						"  sp.ORDER_STATUS, " +
						"  ctrc.REASON_CODE ||'-'|| sp.REASON_DESCRIPTION AS REASON_DESCRIPTION, " +
						"  sp.REFERENCE_NUMBER, " +
						"  sp.DATE_ADDED, " +
						"  sph.SELLER_PAYMENT_STATUS_EFF_DATE AS SELLER_PAYMENT_STATUS_EFF_DATE " +
						"FROM SELLER_PAYMENTS SP, SELLER_PAYMENT_STATUS_HIST SPH, USERS u, CC_TRANS_REASON_CODE ctrc " +
						"WHERE record_type = 'Credit Memo' AND sph.pid (+) = sp.id AND " +
						"      (SP.status != 'GP Credit Memo Completed' AND SP.status != 'GP Credit Memo Voided') AND " +
						"      u.ID = sp.SELLER_ID AND u.SELLER_SEGMENT_ID IS NULL AND " +
						"      u.active = 1 " +
						"      AND sp.date_added BETWEEN :createdFromDate AND :createdToDate " +
						"      AND sph.seller_payment_status_id (+) = 22 " +
						"      AND ctrc.REASON_DESCRIPTION = sp.REASON_DESCRIPTION " +
						"ORDER BY DATE_ADDED DESC", resultClass = SellerPayments.class),
            	@NamedNativeQuery(name = "SellerPayments.getIndyPaymentsDefault", query = "SELECT " +
						"  sp.ID                              AS ID, " +
						"  sp.TID, " +
						"  sp.SELLER_ID                       AS SELLER_ID, " +
						"  sp.SELLER_PAYMENT_STATUS_ID        AS SELLER_PAYMENT_STATUS_ID, " +
						"  (CASE nvl(sp.CM_BALANCED_AMT, 0) " +
						"   WHEN 0 " +
						"     THEN sp.AMOUNT " +
						"   ELSE sp.CM_BALANCED_AMT END)      AS AMOUNT, " +
						"  sp.CURRENCY_CODE, " +
						"  sp.BOOK_OF_BUSINESS_ID, " +
						"  sp.EVENT_DATE_LOCAL, " +
						"  sp.EVENT_DATE, " +
						"  sp.EVENT_ID, " +
						"  sp.ORDER_STATUS, " +
						"  ctrc.REASON_CODE ||'-'|| sp.REASON_DESCRIPTION AS REASON_DESCRIPTION, " +
						"  sp.REFERENCE_NUMBER, " +
						"  sp.DATE_ADDED, " +
						"  sph.SELLER_PAYMENT_STATUS_EFF_DATE AS SELLER_PAYMENT_STATUS_EFF_DATE " +
						"FROM SELLER_PAYMENTS SP, SELLER_PAYMENT_STATUS_HIST SPH, USERS u, CC_TRANS_REASON_CODE ctrc " +
						"WHERE record_type = 'Credit Memo' AND sph.pid (+) = sp.id AND " +
						"      (SP.status != 'GP Credit Memo Completed' AND SP.status != 'GP Credit Memo Voided') AND " +
						"      u.ID = sp.SELLER_ID AND u.SELLER_SEGMENT_ID IS NULL AND " +
						"      u.active = 1 " +
						"      AND sp.date_added > sysdate - :days " +
						"      AND sph.seller_payment_status_id (+) = 22  " +
						"      AND ctrc.REASON_DESCRIPTION = sp.REASON_DESCRIPTION " +
						"ORDER BY DATE_ADDED DESC", resultClass = SellerPayments.class),
        @NamedNativeQuery(name = "SellerPayments.countSellerPaymentsDefault", query = "SELECT count(sp.ID) as count"
                + " from SELLER_PAYMENTS SP, SELLER_PAYMENT_STATUS_HIST SPH"
                + " WHERE sp.seller_id = :sellerId"
                + " AND record_type = :recordType"
                + " AND sph.pid(+) = sp.id"
                + " AND sph.seller_payment_status_id(+) = 22"
                + " AND sp.date_added > sysdate - :days", resultSetMapping = "countMapping"),

                
        @NamedNativeQuery(name = "SellerPayments.countSellerPaymentsWithFilters", query = "SELECT count(sp.ID) as count"
                + " from SELLER_PAYMENTS SP, SELLER_PAYMENT_STATUS_HIST SPH"
                + " WHERE sp.seller_id = :sellerId"
                + " AND record_type = :recordType"
                + " AND sph.pid(+) = sp.id"
                + " AND sph.seller_payment_status_id(+) = 22"
                + " AND sp.date_added between :createdFromDate and :createdToDate", resultSetMapping = "countMapping"),

		@NamedNativeQuery(name = "SellerPayments.countSellerPaymentsWithFiltersCurrency", query = "SELECT count(sp.ID) as count"
				+ " from SELLER_PAYMENTS SP, SELLER_PAYMENT_STATUS_HIST SPH"
				+ " WHERE sp.seller_id = :sellerId"
				+ " AND record_type = :recordType"
				+ " AND currency_code = :currencyCode"
				+ " AND sph.pid(+) = sp.id"
				+ " AND sph.seller_payment_status_id(+) = 22"
				+ " AND sp.date_added between :createdFromDate and :createdToDate", resultSetMapping = "countMapping"),

		@NamedNativeQuery(name = "SellerPayments.updatePaymentStatus", query = "update SELLER_PAYMENTS " +
				"set seller_payment_status_id = :statusId, " +
				"STATUS= :status, " +
				"LAST_UPDATED= :lastUpdated, " +
				"LAST_UPDATED_BY= :lastUpdateBy " +
				"where id = :id " +
				"and seller_payment_status_id <> :statusId " +
				"and SELLER_ID = :sellerId", resultClass = SellerPayments.class)
		})
			
public class SellerPayments implements java.io.Serializable{
		
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "SELLER_ID")
	private Long sellerId;
	
	@Column(name = "TID")
	private Long orderId;
	
	@Column(name = "SELLER_PAYMENT_STATUS_ID")
	private Long sellerPaymentStatusId;
	
	@Column(name = "AMOUNT")
	private Double amount;
	
	@Column(name = "CURRENCY_CODE")
	private String currencyCode;
	
	@Column(name = "BOOK_OF_BUSINESS_ID")
	private Long bobId;
	
	@Column(name = "EVENT_DATE_LOCAL")	
	private String eventDateLocal;
	
	@Column(name = "EVENT_DATE")
	@Type(type="com.stubhub.newplatform.persistence.adapter.UTCCalendarToTimestampAdapter")
	private Calendar eventDate;
	
	@Column(name = "EVENT_ID")
	private Long eventId;
	
	@Column(name = "ORDER_STATUS")
	private String orderStatus;
	
	@Column(name = "REASON_DESCRIPTION")
	private String reasonDescription;
	
	@Column(name = "REFERENCE_NUMBER")
	private String referenceNumber;
	
	@Column(name = "DATE_ADDED")
	@Type(type="com.stubhub.newplatform.persistence.adapter.UTCCalendarToTimestampAdapter")
	private Calendar dateAdded;
	
	@Column(name = "SELLER_PAYMENT_STATUS_EFF_DATE")
	@Type(type="com.stubhub.newplatform.persistence.adapter.UTCCalendarToTimestampAdapter")
	private Calendar appliedDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public Calendar getDateAdded() {
		return dateAdded;
	}

	public void setDateAdded(Calendar dateAdded) {
		this.dateAdded = dateAdded;
	}

	public Calendar getEventDate() {
		return eventDate;
	}
	
	public void setEventDate(Calendar eventDate) {
		this.eventDate = eventDate;
	}

	public Long getSellerPaymentStatusId() {
		return sellerPaymentStatusId;
	}

	public void setSellerPaymentStatusId(Long sellerPaymentStatusId) {
		this.sellerPaymentStatusId = sellerPaymentStatusId;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public String getReasonDescription() {
		return reasonDescription;
	}

	public void setReasonDescription(String reasonDescription) {
		this.reasonDescription = reasonDescription;
	}

	public Long getBobId() {
		return bobId;
	}

	public void setBobId(Long bobId) {
		this.bobId = bobId;
	}

	public String getEventDateLocal() {
		return eventDateLocal;
	}

	public void setEventDateLocal(String eventDateLocal) {
		this.eventDateLocal = eventDateLocal;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public Calendar getAppliedDate() {
		return appliedDate;
	}

	public void setAppliedDate(Calendar appliedDate) {
		this.appliedDate = appliedDate;
	}

	public Long getSellerId() {
		return sellerId;
	}

	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}
}
