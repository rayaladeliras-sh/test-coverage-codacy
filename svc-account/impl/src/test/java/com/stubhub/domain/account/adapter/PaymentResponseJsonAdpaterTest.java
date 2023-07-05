package com.stubhub.domain.account.adapter;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.stubhub.domain.account.intf.SellerPayment;
import com.stubhub.domain.account.intf.SellerPayments;

public class PaymentResponseJsonAdpaterTest {
	ObjectMapper om = new ObjectMapper();
	private static final String[] header = new String[] { "Payment Date", "Order Number", "Total Ticket Price",
			"Commission Paid", "Adjustment Amount", "Payout", "Payee", "Payment Type", "Event Name", "Event Date",
			"Venue", "StubHub Listing ID", "External Listing ID", "Quantity", "Section", "Row", "Seat",
			"Payment Status", "Reference Number" };

	@Test
	public void testConvertJsonNodeToSalesResponse() throws Exception {
		SellerPayments sellerPayments = PaymentResponseJsonAdpater.convertJsonNodeToResponse(null, "USD", false, false);
		Assert.assertNotNull(sellerPayments);
		sellerPayments = PaymentResponseJsonAdpater.convertJsonNodeToResponse(om.createArrayNode(), "USD", false, false);
		Assert.assertNotNull(sellerPayments);
		Assert.assertEquals(sellerPayments.getNumFound(), 0L);
		sellerPayments = PaymentResponseJsonAdpater.convertJsonNodeToResponse(om.createObjectNode(), "USD", false, false);
		Assert.assertNotNull(sellerPayments);
		Assert.assertEquals(sellerPayments.getNumFound(), 0L);
		ObjectNode createObjectNode = om.createObjectNode();
		ObjectNode respNode = om.createObjectNode();
		createObjectNode.put("response", respNode);
		sellerPayments = PaymentResponseJsonAdpater.convertJsonNodeToResponse(createObjectNode, "USD", false, false);
		Assert.assertNotNull(sellerPayments);
		Assert.assertEquals(sellerPayments.getNumFound(), 0L);
		respNode.put("docs", om.createArrayNode());
		sellerPayments = PaymentResponseJsonAdpater.convertJsonNodeToResponse(createObjectNode, "USD", false, false);
		Assert.assertNotNull(sellerPayments);
		Assert.assertEquals(sellerPayments.getNumFound(), 0L);
		InputStream in = AccountResponseJsonAdapterTest.class.getClassLoader()
				.getResourceAsStream("paymentResponse.json");
		sellerPayments = PaymentResponseJsonAdpater.convertJsonNodeToResponse(om.readTree(in), "USD", false, false);
		Assert.assertNotNull(sellerPayments);
		Assert.assertEquals(sellerPayments.getNumFound(), 12);
		List<SellerPayment> payments = sellerPayments.getPayments();
		Assert.assertNotNull(payments);
		Assert.assertEquals(payments.size(), 3);
		SellerPayment firstPayment = payments.get(0);
		Assert.assertNotNull(firstPayment);
		Assert.assertEquals(firstPayment.getID(), "131698068");
		Assert.assertNotNull(sellerPayments.getPaymentsSummary());
		in = AccountResponseJsonAdapterTest.class.getClassLoader()
				.getResourceAsStream("paymentResponseWithoutSummary.json");
		sellerPayments = PaymentResponseJsonAdpater.convertJsonNodeToResponse(om.readTree(in), "USD", false, false);
		Assert.assertNull(sellerPayments.getPaymentsSummary());
		in = AccountResponseJsonAdapterTest.class.getClassLoader()
				.getResourceAsStream("paymentResponseWithoutSummary2.json");
		sellerPayments = PaymentResponseJsonAdpater.convertJsonNodeToResponse(om.readTree(in), "USD", false, false);
		Assert.assertNull(sellerPayments.getPaymentsSummary());
		in = AccountResponseJsonAdapterTest.class.getClassLoader()
				.getResourceAsStream("paymentResponseWithoutSummary3.json");
		sellerPayments = PaymentResponseJsonAdpater.convertJsonNodeToResponse(om.readTree(in), "USD", false, false);
		Assert.assertNull(sellerPayments.getPaymentsSummary());

	}

	@Test
	public void testConvertJsonNodeToSellerPaymentList() throws Exception {
		List<String[]> sellerPaymentList = new ArrayList<String[]>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		PaymentResponseJsonAdpater.convertJsonNodeToSellerPaymentList(sellerPaymentList, null, header.length,
				dateFormat);
		Assert.assertTrue(sellerPaymentList.isEmpty());
		sellerPaymentList = new ArrayList<String[]>();
		PaymentResponseJsonAdpater.convertJsonNodeToSellerPaymentList(sellerPaymentList, om.createArrayNode(), header.length,
				dateFormat);
		Assert.assertTrue(sellerPaymentList.isEmpty());
		sellerPaymentList = new ArrayList<String[]>();
		PaymentResponseJsonAdpater.convertJsonNodeToSellerPaymentList(sellerPaymentList, om.createObjectNode(), header.length,
				dateFormat);
		Assert.assertTrue(sellerPaymentList.isEmpty());
		sellerPaymentList = new ArrayList<String[]>();
		ObjectNode createObjectNode = om.createObjectNode();
		ObjectNode respNode = om.createObjectNode();
		createObjectNode.put("response", respNode);
		PaymentResponseJsonAdpater.convertJsonNodeToSellerPaymentList(sellerPaymentList, createObjectNode, header.length,
				dateFormat);
		Assert.assertTrue(sellerPaymentList.isEmpty());
		respNode.put("docs", om.createArrayNode());
		sellerPaymentList = new ArrayList<String[]>();
		PaymentResponseJsonAdpater.convertJsonNodeToSellerPaymentList(sellerPaymentList, createObjectNode, header.length,
				dateFormat);
		Assert.assertTrue(sellerPaymentList.isEmpty());
		sellerPaymentList = new ArrayList<String[]>();
		InputStream in = AccountResponseJsonAdapterTest.class.getClassLoader()
				.getResourceAsStream("paymentResponse2.json");
		PaymentResponseJsonAdpater.convertJsonNodeToSellerPaymentList(sellerPaymentList, om.readTree(in), header.length,
				dateFormat);
		Assert.assertTrue(!sellerPaymentList.isEmpty());

		sellerPaymentList = new ArrayList<String[]>();
		in = AccountResponseJsonAdapterTest.class.getClassLoader()
				.getResourceAsStream("paymentResponseWithoutPriceAndCommision.json");
		PaymentResponseJsonAdpater.convertJsonNodeToSellerPaymentList(sellerPaymentList, om.readTree(in), header.length,
				dateFormat);
		Assert.assertTrue(!sellerPaymentList.isEmpty());
	}

}
