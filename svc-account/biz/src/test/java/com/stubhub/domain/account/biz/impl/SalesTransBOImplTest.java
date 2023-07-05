package com.stubhub.domain.account.biz.impl;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.stubhub.common.exception.RecordNotFoundForIdException;
import com.stubhub.domain.account.datamodel.dao.SalesTransDAO;
import com.stubhub.domain.account.datamodel.entity.SalesTrans;





public class SalesTransBOImplTest {
	
	private SalesTransBOImpl salesTransBO;
	private SalesTransDAO salesTransDAO;
	private Integer rowNumber = 5;
	private Integer startRow = 1;
	private Calendar eventStartDate;
	private Calendar eventEndDate;

	
	@BeforeMethod
	public void setUp() throws Exception {
		eventStartDate = Mockito.mock(Calendar.class);
		eventEndDate = Mockito.mock(Calendar.class);
		salesTransBO = new SalesTransBOImpl();
		salesTransDAO = Mockito.mock(SalesTransDAO.class);
		ReflectionTestUtils.setField(salesTransBO, "salesTransDAO", salesTransDAO);
	}
	
	
	
	@Test
	public void testGetSaleTransByBuyerId(){
		String buyerId = "1234";
		Long saleId=11111l;
		List<SalesTrans> list = new ArrayList<SalesTrans>();
		SalesTrans salesTrans = new SalesTrans();
		salesTrans.setBuyerId(buyerId);
        salesTrans.setSaleId(saleId);
        list.add(salesTrans);
        Mockito.when(salesTransDAO.getByBuyerId(Mockito.anyLong(), Mockito.anyInt(),Mockito.anyInt())).thenReturn(list);
        
		List<SalesTrans> result = salesTransBO.getSaleTransByBuyerId(Long.parseLong(buyerId), startRow, rowNumber);
		assertNotNull(result);
		assertEquals(result.get(0).getSaleId(), saleId);
		assertEquals(result.get(0).getBuyerId(), buyerId);
	}
	
	@Test
	public void testGetSaleTransByTID() throws RecordNotFoundForIdException{
		String buyerId = "1234";
		Long saleId=11111l;
		List<SalesTrans> list = new ArrayList<SalesTrans>();
		SalesTrans salesTrans = new SalesTrans();
		salesTrans.setBuyerId(buyerId);
        salesTrans.setSaleId(saleId);
        list.add(salesTrans);
        Mockito.when(salesTransDAO.getByTId(Mockito.anyLong())).thenReturn(salesTrans);
        
		SalesTrans result = salesTransBO.getSaleTransById(saleId);
		assertNotNull(result);
		assertEquals(result.getSaleId(), saleId);
		assertEquals(result.getBuyerId(), buyerId);
	}
	
	
	@Test
	public void testGetSaleTransByEventDate(){
		String buyerId = "1234";
		Long saleId=11111l;
		List<SalesTrans> list = new ArrayList<SalesTrans>();
		SalesTrans salesTrans = new SalesTrans();
		salesTrans.setBuyerId(buyerId);
        salesTrans.setSaleId(saleId);
        list.add(salesTrans);
        Mockito.when(salesTransDAO.getByEventDate(eventStartDate,eventEndDate,startRow,rowNumber)).thenReturn(list);
        
		List<SalesTrans> result = salesTransBO.getSaleTransByEventDate(eventStartDate, eventEndDate, startRow, rowNumber);
		assertNotNull(result);
		assertEquals(result.get(0).getSaleId(), saleId);
		assertEquals(result.get(0).getBuyerId(), buyerId);
	}
	
	
	@Test
	public void testGetSaleTransByBuyerIDAndEventDate(){
		String buyerId = "1234";
		Long saleId=11111l;
		List<SalesTrans> list = new ArrayList<SalesTrans>();
		SalesTrans salesTrans = new SalesTrans();
		salesTrans.setBuyerId(buyerId);
        salesTrans.setSaleId(saleId);
        list.add(salesTrans);
        Mockito.when(salesTransDAO.getByBuyerIDAndEventDate(Long.parseLong(buyerId),eventStartDate,eventEndDate,startRow,rowNumber)).thenReturn(list);
        
		List<SalesTrans> result = salesTransBO.getSaleTransByBuyerIDAndEventDate(Long.parseLong(buyerId), eventStartDate, eventEndDate, startRow, rowNumber);
		assertNotNull(result);
		assertEquals(result.get(0).getSaleId(), saleId);
		assertEquals(result.get(0).getBuyerId(), buyerId);
	}
	
	
	@Test
	public void testSalesTransBean(){
		String buyerId = "1234";
		Long saleId=11111l;
		String listingId = "1111";
		Long quantityPurchased = 1l;
		String section = "1";
		String row = "1";
		Boolean cancelled = false;
		Long buyerContactId = 1l;
		Calendar saleDateUTC = Mockito.mock(Calendar.class);
		Boolean subbedFlag = false;
		String subbedOrderId = "1";
		String eventId = "1";
		Calendar eventDateUTC =Mockito.mock(Calendar.class);
		Long deliveryMethodId = 1l;
		Calendar expectedArrival = Mockito.mock(Calendar.class);
		Calendar inHandDateUTC = Mockito.mock(Calendar.class);
		Calendar shipDateUTC = Mockito.mock(Calendar.class);
		String trackingNumber = "1";
		String saleProcSubStatus = "1";
		String currency = "USD";
		
		BigDecimal amount = new BigDecimal("1");


		SalesTrans salesTrans = new SalesTrans();
		salesTrans.setBuyerId(buyerId);
        salesTrans.setSaleId(saleId);
        salesTrans.setListingId(listingId);
        salesTrans.setQuantityPurchased(quantityPurchased);
        salesTrans.setSection(section);
        salesTrans.setRow(row);
        salesTrans.setCancelled(cancelled);
        salesTrans.setBuyerContactId(buyerContactId);
        salesTrans.setSaleDateUTC(saleDateUTC);
        salesTrans.setSubbedFlag(subbedFlag);
        salesTrans.setSubbedOrderId(subbedOrderId);
        salesTrans.setEventId(eventId);
        salesTrans.setEventDateUTC(eventDateUTC);
        salesTrans.setDeliveryMethodId(deliveryMethodId);
        salesTrans.setExpectedArrivalDateUTC(expectedArrival);
        salesTrans.setInHandDateUTC(inHandDateUTC);
        salesTrans.setShipDateUTC(shipDateUTC);
        salesTrans.setTrackingNumber(trackingNumber);
        salesTrans.setSaleProcSubStatusCode(saleProcSubStatus);
        salesTrans.setCurrency(currency);
        
        com.stubhub.newplatform.common.entity.Money money = new com.stubhub.newplatform.common.entity.Money();
        money.setAmount(amount);
        money.setCurrency(currency);
        
        salesTrans.setTotalCost(money);
        salesTrans.setBuyVAT(money);
        salesTrans.setSellVAT(money);
        salesTrans.setShippingFeeCost(money);
        salesTrans.setDiscountCost(money);
        salesTrans.setTicketCost(money);
        
        assertEquals(salesTrans.getSaleId(), saleId);
		assertEquals(salesTrans.getBuyerId(), buyerId);
		assertEquals(salesTrans.getListingId(), listingId);
		assertEquals(salesTrans.getQuantityPurchased(), quantityPurchased);
		assertEquals(salesTrans.getSection(), section);
		assertEquals(salesTrans.getRow(), row);
		assertEquals(salesTrans.getCancelled(), cancelled);
		assertEquals(salesTrans.getBuyerContactId(), buyerContactId);
		assertNotNull(salesTrans.getSaleDateUTC());
		assertEquals(salesTrans.getSubbedFlag(), subbedFlag);
		assertEquals(salesTrans.getSubbedOrderId(), subbedOrderId);
		assertEquals(salesTrans.getEventId(), eventId);
		assertNotNull(salesTrans.getEventDateUTC());
		assertEquals(salesTrans.getDeliveryMethodId(), deliveryMethodId);
		assertNotNull(salesTrans.getExpectedArrivalDateUTC());
		assertNotNull(salesTrans.getInHandDateUTC());
		assertNotNull(salesTrans.getShipDateUTC());
		assertEquals(salesTrans.getTrackingNumber(), trackingNumber);
		assertEquals(salesTrans.getSaleProcSubStatusCode(), saleProcSubStatus);
		assertEquals(salesTrans.getCurrency(), currency);
		
		assertEquals(salesTrans.getTotalCost().getAmount(), amount);
		assertEquals(salesTrans.getBuyVAT().getAmount(), amount);
		assertEquals(salesTrans.getSellVAT().getAmount(), amount);
		assertEquals(salesTrans.getShippingFeeCost().getAmount(), amount);
		assertEquals(salesTrans.getDiscountCost().getAmount(), amount);
		assertEquals(salesTrans.getTicketCost().getAmount(), amount);
		
		
		
	}

}
