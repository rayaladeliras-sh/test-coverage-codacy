package com.stubhub.domain.account.intf;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class EmailLogsResponseTest {
	private EmailLogsResponse emailLogsResponse;
	
	@BeforeTest
	public void setUp() {
		emailLogsResponse = new EmailLogsResponse();
	}
	
	@Test
	public void testEmailLogsResponse(){
		EmailResponse emailResponse = new EmailResponse();
		String addrBcc = "bijain@stubhub.com";
		String addrCc = "bijain1@stubhub.com";
		String addrFrom = "bijain2@stubhub.com";
		String addrTo = "bijain3@stubhub.com";
		String dateSent = "2015-02-02";
		String dateAdded = "2015-02-02";
		Long emailId = Long.valueOf("123456");
		String format = "text";
		String subject = "deliver the tickets";
		String tId = "1234";
		String userId = "12345";
		emailResponse.setAddressBcc(addrBcc);
		emailResponse.setAddressCc(addrCc);
		emailResponse.setAddressFrom(addrFrom);
		emailResponse.setAddressTo(addrTo);
		emailResponse.setDateAdded(dateAdded);
		emailResponse.setDateSent(dateSent);
		emailResponse.setEmailId(emailId);
		emailResponse.setFormat(format);
		emailResponse.setSubject(subject);
		emailResponse.settId(tId);
		emailResponse.setUserId(userId);
		List<EmailResponse> list = new ArrayList<EmailResponse>();
		list.add(emailResponse);
		emailLogsResponse.setEmail(list);		

		Assert.assertEquals(emailResponse.getAddressBcc(),addrBcc);
		Assert.assertEquals(emailResponse.getAddressCc(),addrCc);
		Assert.assertEquals(emailResponse.getAddressFrom(),addrFrom);
		Assert.assertEquals(emailResponse.getAddressTo(),addrTo);
		Assert.assertEquals(emailResponse.getEmailId(),emailId);
		Assert.assertEquals(emailResponse.getDateAdded(),dateAdded);
		Assert.assertEquals(emailResponse.getDateSent(),dateSent);
		Assert.assertEquals(emailResponse.getFormat(),format);
		Assert.assertEquals(emailResponse.getSubject(),subject);
		Assert.assertEquals(emailResponse.gettId(),tId);
		Assert.assertEquals(emailResponse.getUserId(),userId);
		
		Assert.assertEquals(emailLogsResponse.getEmail(), list);
	}

}
