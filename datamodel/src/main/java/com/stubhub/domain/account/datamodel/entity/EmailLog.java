package com.stubhub.domain.account.datamodel.entity;

import java.sql.Clob;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;

import org.hibernate.annotations.Type;

@Entity
@NamedNativeQueries(value ={
		@NamedNativeQuery(name = "EmailLogs.getEmailById", query = "" +
				"SELECT " +
				"el.id AS emailId, " +
				"el.user_id AS userId, " +
				"el.tid AS tId, " +
				"NULL AS buyerOrderId, " +
				"el.addr_from AS addressFrom, " +
				"el.addr_to AS addressTo, " +
				"el.addr_cc AS addressCc, " +
				"el.addr_bcc AS addressBcc, " +
				"el.date_added AS dateAdded, " +
				"el.date_sent AS dateSent, " +
				"el.subject AS subject, " +
				"el.format AS format, " +
				"el.body AS body " +
				"FROM email_logs el " + 
				"WHERE el.id =:arg1 " , resultClass=EmailLog.class),
		@NamedNativeQuery(name = "UserMessages.getUserMessagesByUserId", query = ""
				+ "(SELECT emailId, userId, tId, buyerOrderId, addressFrom, addressTo, " 
				+ "addressCc, addressBcc, dateAdded, dateSent, subject, " 
				+ "format, body FROM " 
                + "(SELECT "
                + "ume.user_message_id AS emailId, "
                + "um.user_id AS userId, "
                + "NULL AS tId, "
                + "NULL AS buyerOrderId, " 
                + "ume.addr_from AS addressFrom, "
                + "ume.addr_to AS addressTo, "
                + "ume.addr_cc AS addressCc, "
                + "ume.addr_bcc AS addressBcc, "
                + "ume.created_date AS dateAdded, "
                + "ume.created_date AS dateSent, "
                + "ume.subject AS subject, "
                + "mm.message_mode AS format, "
                + "NULL AS body "
                + "FROM user_message um,user_message_email ume,message_mode mm "
                + "WHERE um.user_id =:usrId AND um.message_mode_id = 2 AND um.user_message_id = ume.user_message_id AND um.message_mode_id = mm.message_mode_id AND ume.created_date BETWEEN :fromDate AND :toDate "
                + "ORDER BY ume.created_date DESC ) " 
                + "WHERE ROWNUM >= :start AND ROWNUM <= :rows) "
                + "UNION "
                + "(SELECT emailId, userId, tId, buyerOrderId, addressFrom, addressTo, " 
				+ "addressCc, addressBcc, dateAdded, dateSent, subject, " 
				+ "format, body FROM "
                + "(SELECT "
                + "ums.user_message_id AS emailId, "
                + "um.user_id AS userId, "
                + "NULL AS tId, "
                + "NULL AS buyerOrderId, " 
                + "ums.sms_stubhub_phone AS addressFrom, "
                + "ums.sms_user_phone AS addressTo, "
                + "NULL AS addressCc, "
                + "NULL AS addressBcc, "
                + "ums.created_date AS dateAdded, "
                + "ums.created_date AS dateSent, "
                + "NULL AS subject, "
                + "mm.message_mode AS format, "
                + "NULL AS body "
                + "FROM user_message um,user_message_sms ums,message_mode mm "
                + "WHERE um.user_id =:usrId AND um.message_mode_id = 1 AND um.user_message_id = ums.user_message_id AND um.message_mode_id = mm.message_mode_id AND ums.created_date BETWEEN :fromDate AND :toDate "
                + "ORDER BY ums.created_date DESC ) "
                + "WHERE ROWNUM >= :start AND ROWNUM <= :rows)", resultClass = EmailLog.class)
})

public class EmailLog implements java.io.Serializable {

	@Id
	@Column(name = "emailId")
	private Long emailId;
	@Column(name = "userId")
	private String userId;
	@Column(name = "tId")
	private String tId;
	@Column(name = "buyerOrderId")
	private String buyerOrderId;
	@Column(name = "addressFrom")
	private String addressFrom;
	@Column(name = "addressTo")
	private String addressTo;
	@Column(name = "addressCc")
	private String addressCc;
	@Column(name = "addressBcc")
	private String addressBcc;
	@Column(name = "dateAdded")
	@Type(type="com.stubhub.newplatform.persistence.adapter.UTCCalendarToTimestampAdapter")
	private Calendar dateAdded;
	@Column(name = "dateSent")
	@Type(type="com.stubhub.newplatform.persistence.adapter.UTCCalendarToTimestampAdapter")
	private Calendar dateSent;
	@Column(name = "subject")
	private String subject;
	@Column(name = "format")
	private String format;
	@Column(name = "body")
	private Clob body;

	public Long getEmailId() {
		return emailId;
	}
	public void setEmailId(Long emailId) {
		this.emailId = emailId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String gettId() {
		return tId;
	}
	public void settId(String tId) {
		this.tId = tId;
	}
	public String getbuyerOrderId() {
		return buyerOrderId;
	}
    public void setbuyerOrderId(String buyerOrderId) {
		this.buyerOrderId = buyerOrderId;
	}
	public String getAddressFrom() {
		return addressFrom;
	}
	public void setAddressFrom(String addressFrom) {
		this.addressFrom = addressFrom;
	}
	public String getAddressTo() {
		return addressTo;
	}
	public void setAddressTo(String addressTo) {
		this.addressTo = addressTo;
	}
	public String getAddressCc() {
		return addressCc;
	}
	public void setAddressCc(String addressCc) {
		this.addressCc = addressCc;
	}
	public String getAddressBcc() {
		return addressBcc;
	}
	public void setAddressBcc(String addressBcc) {
		this.addressBcc = addressBcc;
	}
	public Calendar getDateAdded() {
		return dateAdded;
	}
	public void setDateAdded(Calendar dateAdded) {
		this.dateAdded = dateAdded;
	}
	public Calendar getDateSent() {
		return dateSent;
	}
	public void setDateSent(Calendar dateSent) {
		this.dateSent = dateSent;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public Clob getBody() {
		return body;
	}
	public void setBody(Clob body) {
		this.body = body;
	}

}
