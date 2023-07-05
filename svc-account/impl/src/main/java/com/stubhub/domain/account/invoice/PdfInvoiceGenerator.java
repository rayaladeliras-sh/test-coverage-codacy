package com.stubhub.domain.account.invoice;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import com.stubhub.domain.account.common.enums.SellerPaymentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.stubhub.domain.account.exception.InvoiceGenerateFailedException;
import com.stubhub.domain.account.intf.AppliedCreditMemo;
import com.stubhub.domain.account.intf.Invoice;
import com.stubhub.domain.account.intf.InvoiceResponse;
import com.stubhub.domain.account.intf.Money;
import com.stubhub.domain.i18n.services.localization.v1.utility.DataSourceMessageSource;

/**
 * Created at 12/25/2014 5:13 PM
 *
 * @author : Caron Zhao
 * @version : 1.0.0
 * @since : PI
 */
@Component("pdfInvoiceGenerator")
public class PdfInvoiceGenerator implements InvoiceGenerator {
	
	@Autowired
	private DataSourceMessageSource messageSource;
    private final static Logger LOGGER = LoggerFactory.getLogger(PdfInvoiceGenerator.class);

    @Override
    public ByteArrayOutputStream generateInvoice(String transactionId, InvoiceResponse invoiceResponse,Locale locale) {
        Document document = new Document();
        ByteArrayOutputStream baosPDF = new ByteArrayOutputStream();
        PdfWriter docWriter = null;
        try {
            docWriter = PdfWriter.getInstance(document, baosPDF);
            document.open();

            // Title
            String paymentReceiptCode = messageSource.getMessage("invoice.paymentReceipt", null, "Payment receipt", locale);
            LOGGER.debug("paymentReceiptCode="+paymentReceiptCode);
            String orderNumCode = messageSource.getMessage("invoice.orderNum", null, "Order #", locale);
            LOGGER.debug("orderNumCode="+orderNumCode);
            String eventCode = messageSource.getMessage("invoice.event", null, "Event", locale);
            LOGGER.debug("eventCode="+eventCode);
            String eventDateCode = messageSource.getMessage("invoice.eventDate", null, "Event date", locale);
            LOGGER.debug("eventDateCode="+eventDateCode);
            String originalOrderPayoutCode = messageSource.getMessage("invoice.originalOrderPayout", null, "Original order payout", locale);
            LOGGER.debug("originalOrderPayoutCode="+originalOrderPayoutCode);
            String creditMemoChargeCode = messageSource.getMessage("invoice.creditMemoCharge", null, "Credit memo charge", locale);
            LOGGER.debug("creditMemoChargeCode="+creditMemoChargeCode);
            String creditMemoTakenFromOrderNumCode = messageSource.getMessage("invoice.creditMemoTakenFromOrderNum", null, "Credit memo taken from order #", locale);
            LOGGER.debug("creditMemoTakenFromOrderNumCode="+creditMemoTakenFromOrderNumCode);
            String chargeAmountCode = messageSource.getMessage("invoice.chargeAmount", null, "Charge amount", locale);
            LOGGER.debug("chargeAmountCode="+chargeAmountCode);
            String totalAmountDeductedCode = messageSource.getMessage("invoice.totalAmountDeducted", null, "Total amount deducted", locale);
            LOGGER.debug("totalAmountDeductedCode="+totalAmountDeductedCode);
            String netPayoutCode = messageSource.getMessage("invoice.netPayout", null, "Net payout", locale);
            LOGGER.debug("netPayoutCode="+netPayoutCode);
            String sentOnCode = "";
            String sentToCode = "";

            Long paymentType =  invoiceResponse.getSellerPaymentTypeId();

            boolean isPaypal = SellerPaymentType.PAYPAL.getId().equals(paymentType);
            if(isPaypal) {
                sentOnCode = messageSource.getMessage("invoice.sentToPaypalOn", null, "Sent to Paypal on", locale);
            } else{
                sentOnCode = messageSource.getMessage("invoice.sentOn", null, "Sent on", locale);
            }

            LOGGER.debug("sentOnCode="+sentOnCode);
            if(isPaypal){
                sentToCode = messageSource.getMessage("invoice.sentToPaypalAccount", null, "Sent to Paypal account", locale);
            }else{
                sentToCode = messageSource.getMessage("invoice.sentTo", null, "Sent to", locale);
            }
            LOGGER.debug("sentToCode="+sentToCode);
            String transactionIdCode = messageSource.getMessage("invoice.transactionId", null, "Transaction ID", locale);
            LOGGER.debug("transactionIdCode="+transactionIdCode);
            
            NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
            
            DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT,locale);
    		  // To show Locale specific short date expression with full year, full day
    		SimpleDateFormat sdf = (SimpleDateFormat) format;
            String pattern = sdf.toPattern().replaceAll("y+","yyyy").replaceAll("M+", "MM").replaceAll("d+", "dd");
            sdf.applyPattern(pattern); 
            
            Chunk titleChunk = new Chunk(paymentReceiptCode, FontFactory.getFont(FontFactory.defaultEncoding, 12f, Font.BOLD));
            Paragraph titleParagraph = new Paragraph();
            titleParagraph.add(titleChunk);
            titleParagraph.setAlignment(Element.ALIGN_CENTER);
            document.add(titleParagraph);
            float[] widths = {0.30f, 0.20f, 0.15f, 0.05f, 0.15f, 0.15f};
            PdfPTable table = new PdfPTable(widths);
            table.setSpacingBefore(15f);
            Font normalFont = FontFactory.getFont(FontFactory.defaultEncoding, 6f, Font.NORMAL);
            Font boldFont = FontFactory.getFont(FontFactory.defaultEncoding, 6f, Font.BOLD);

            // Order Number Row
            Chunk orderNumberTitleChunk = new Chunk(orderNumCode, normalFont);
            table.addCell(createCell(orderNumberTitleChunk, Rectangle.NO_BORDER));
            Chunk orderNumberChunk = new Chunk(invoiceResponse.getOrderNumber().toString(), normalFont);
            table.addCell(createCell(orderNumberChunk, Rectangle.NO_BORDER, 5));

            // Event Row
            Chunk eventTitleChunk = new Chunk(eventCode, normalFont);
            table.addCell(createCell(eventTitleChunk, Rectangle.NO_BORDER));
            Chunk eventChunk = new Chunk(invoiceResponse.getEventDescription(), normalFont);
            table.addCell(createCell(eventChunk, Rectangle.NO_BORDER, 5));

            // Event Date Row
            Chunk eventDateTitleChunk = new Chunk(eventDateCode, normalFont);
            table.addCell(createCell(eventDateTitleChunk, Rectangle.BOTTOM));
            String eventDate = sdf.format(invoiceResponse.getEventDateLocal().getTime());
            Chunk eventDateChunk = new Chunk(eventDate, normalFont);
            table.addCell(createCell(eventDateChunk, Rectangle.BOTTOM, 5));

            // Empty Row
            table.addCell(createEmptyCell(Rectangle.NO_BORDER, 6));

            // Original Order Payout Row
            Chunk originalOrderPayoutTitlechunk = new Chunk(originalOrderPayoutCode, normalFont);
            table.addCell(createCell(originalOrderPayoutTitlechunk, Rectangle.NO_BORDER));
            table.addCell(createEmptyCell(Rectangle.NO_BORDER));
            table.addCell(createEmptyCell(Rectangle.NO_BORDER));
            table.addCell(createEmptyCell(Rectangle.NO_BORDER));
            Money originalOrderPayoutValue = invoiceResponse.getOrderAmount();
            StringBuilder sb = new StringBuilder();
            numberFormat.setCurrency(Currency.getInstance(originalOrderPayoutValue.getCurrency()));
            sb.append(numberFormat.format(originalOrderPayoutValue.getAmount().doubleValue()));
            Chunk originalOrderPayoutChunk = new Chunk(sb.toString(), normalFont);
            PdfPCell originalOrderPayoutCell = createCell(originalOrderPayoutChunk, Rectangle.NO_BORDER, 2);
            originalOrderPayoutCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(originalOrderPayoutCell);

            Invoice invoice = invoiceResponse.getInvoice();
            boolean hasCreditMemoCharge = false;
            if (invoice != null && invoice.getAppliedCreditMemos().size() > 0) {
                hasCreditMemoCharge = true;
            }

            if (hasCreditMemoCharge) {
                // Credit Memo Charge Row
                Chunk creditMemoChargeLabelChunk = new Chunk(creditMemoChargeCode, normalFont);
                table.addCell(createCell(creditMemoChargeLabelChunk, Rectangle.NO_BORDER, 6));
                table.addCell(createEmptyCell(Rectangle.NO_BORDER, 6));

                // Credit Memo Charge Detail Title Row
                Chunk cmcFromOrderLabelChunk = new Chunk(creditMemoTakenFromOrderNumCode, normalFont);
                PdfPCell cmcFromOrderLabelCell = createCell(cmcFromOrderLabelChunk, Rectangle.NO_BORDER);
                cmcFromOrderLabelCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cmcFromOrderLabelCell);

                Chunk cmcEventLabelChunk = new Chunk(eventCode, normalFont);
                PdfPCell cmcEventLabelCell = createCell(cmcEventLabelChunk, Rectangle.NO_BORDER);
                cmcEventLabelCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cmcEventLabelCell);

                Chunk cmcEventDateLabelChunk = new Chunk(eventDateCode, normalFont);
                PdfPCell cmcEventDateLabelCell = createCell(cmcEventDateLabelChunk, Rectangle.NO_BORDER);
                cmcEventDateLabelCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cmcEventDateLabelCell);

                // Credit Memo Balance Title(don't need for now)
                table.addCell(createEmptyCell(Rectangle.NO_BORDER));

                Chunk cmcChargeAmountLabelChunk = new Chunk(chargeAmountCode, normalFont);
                PdfPCell cmcChargeAmountLabelCell = createCell(cmcChargeAmountLabelChunk, Rectangle.NO_BORDER);
                cmcChargeAmountLabelCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cmcChargeAmountLabelCell);

                table.addCell(createEmptyCell(Rectangle.NO_BORDER));
            }

            // Credit Memo Charge Detail Content Rows
            BigDecimal totalAmountDeducted = new BigDecimal(0);
            if (invoice != null) {
                for (AppliedCreditMemo appliedCreditMemo : invoice.getAppliedCreditMemos()) {
                    Chunk cmcFromOrderChunk = new Chunk(appliedCreditMemo.getOrderId().toString(), normalFont);
                    PdfPCell cmcFromOrderCell = createCell(cmcFromOrderChunk, Rectangle.NO_BORDER);
                    cmcFromOrderCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cmcFromOrderCell);

                    Chunk cmcEventChunk = new Chunk(appliedCreditMemo.getEventName(), normalFont);
                    PdfPCell cmcEventCell = createCell(cmcEventChunk, Rectangle.NO_BORDER);
                    cmcEventCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cmcEventCell);

                    String cmcEventDate = sdf.format(appliedCreditMemo.getEvetnDate().getTime());
                    Chunk cmcEventDateChunk = new Chunk(cmcEventDate, normalFont);
                    PdfPCell cmcEventDateCell = createCell(cmcEventDateChunk, Rectangle.NO_BORDER);
                    cmcEventDateCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cmcEventDateCell);

                    // Credit Memo Balance(don't need for now)
                    table.addCell(createEmptyCell(Rectangle.NO_BORDER));

                    StringBuilder cmcChargeAmountDisplay = new StringBuilder();
                    numberFormat.setCurrency(Currency.getInstance(appliedCreditMemo.getCurrency()));
                    cmcChargeAmountDisplay.append(numberFormat.format(appliedCreditMemo.getAmount().doubleValue()));
                    Chunk cmcChargeAmountChunk = new Chunk(cmcChargeAmountDisplay.toString(), normalFont);
                    PdfPCell cmcChargeAmountCell = createCell(cmcChargeAmountChunk, Rectangle.NO_BORDER, 2);
                    cmcChargeAmountCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cmcChargeAmountCell);

                    totalAmountDeducted = totalAmountDeducted.add(BigDecimal.valueOf(appliedCreditMemo.getAmount().doubleValue()));
                }
            }

            if (hasCreditMemoCharge) {
                // Total amount
                Chunk totalAmountTitleChunk = new Chunk(totalAmountDeductedCode, boldFont);
                table.addCell(createCell(totalAmountTitleChunk, Rectangle.TOP));
                table.addCell(createEmptyCell(Rectangle.TOP));
                table.addCell(createEmptyCell(Rectangle.TOP));
                table.addCell(createEmptyCell(Rectangle.TOP));
                StringBuilder totalAmountDeductedDisplay = new StringBuilder();
                numberFormat.setCurrency(Currency.getInstance(originalOrderPayoutValue.getCurrency()));
                totalAmountDeductedDisplay.append(numberFormat.format(totalAmountDeducted.doubleValue()));
                Chunk totalAmountChunk = new Chunk(totalAmountDeductedDisplay.toString(), normalFont);
                PdfPCell totalAmountCell = createCell(totalAmountChunk, Rectangle.TOP, 2);
                totalAmountCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(totalAmountCell);

                // Two Empty Rows
                table.addCell(createEmptyCell(Rectangle.BOTTOM, 6, 10f));
                table.addCell(createEmptyCell(Rectangle.NO_BORDER, 6, 10f));
            }

            // Net Payout Row
            Chunk netPayoutLabelChunk = new Chunk(netPayoutCode, boldFont);
            table.addCell(createCell(netPayoutLabelChunk, Rectangle.NO_BORDER));
            table.addCell(createEmptyCell(Rectangle.NO_BORDER));
            table.addCell(createEmptyCell(Rectangle.NO_BORDER));
            table.addCell(createEmptyCell(Rectangle.NO_BORDER));
            StringBuilder netPayoutValueDisplay = new StringBuilder();
            BigDecimal netPayoutValue = originalOrderPayoutValue.getAmount().subtract(totalAmountDeducted);
            numberFormat.setCurrency(Currency.getInstance(originalOrderPayoutValue.getCurrency()));
            netPayoutValueDisplay.append(numberFormat.format(netPayoutValue.doubleValue()));
            Chunk netPayoutChunk = new Chunk(netPayoutValueDisplay.toString(), normalFont);
            PdfPCell netPayoutCell = createCell(netPayoutChunk, Rectangle.NO_BORDER, 2);
            netPayoutCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(netPayoutCell);

            // Sent to Paypal Account Row
            Chunk sent2LabelChunk = new Chunk(sentToCode, boldFont);
            table.addCell(createCell(sent2LabelChunk, Rectangle.NO_BORDER));
            table.addCell(createEmptyCell(Rectangle.NO_BORDER));
            table.addCell(createEmptyCell(Rectangle.NO_BORDER));
            table.addCell(createEmptyCell(Rectangle.NO_BORDER));
            String sent2Value = "";
            if(SellerPaymentType.PAYPAL.getId().equals(paymentType)){
                sent2Value = invoiceResponse.getPayeeEmailId();
            }else if(SellerPaymentType.ACH.getId().equals(paymentType)){
                sent2Value = (invoiceResponse.getBankName() != null ? invoiceResponse.getBankName() : "")
                        + "****"
                        + (invoiceResponse.getAcctLastFourDigits() != null ? invoiceResponse.getAcctLastFourDigits() : "");
            }
            Chunk sent2Chunk = new Chunk(sent2Value, normalFont);
            PdfPCell sent2PaypalAccountCell = createCell(sent2Chunk, Rectangle.NO_BORDER, 2);
            sent2PaypalAccountCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(sent2PaypalAccountCell);

            // Sent to Paypal On Row
            Chunk sentOnLabelChunk = new Chunk(sentOnCode, boldFont);
            table.addCell(createCell(sentOnLabelChunk, Rectangle.NO_BORDER));
            table.addCell(createEmptyCell(Rectangle.NO_BORDER));
            table.addCell(createEmptyCell(Rectangle.NO_BORDER));
            table.addCell(createEmptyCell(Rectangle.NO_BORDER));

            Calendar paymentSentToGatewayDate = invoiceResponse.getPaymentSentToGatewayDate();
            String paymentSentToGatewayDateValue;
            if (paymentSentToGatewayDate == null) {
                paymentSentToGatewayDateValue = "";
            } else {
            	sdf.setTimeZone(getTimeZone(locale));
                paymentSentToGatewayDateValue = sdf.format(paymentSentToGatewayDate.getTime());
            }
            Chunk sent2paypalOnChunk = new Chunk(paymentSentToGatewayDateValue, normalFont);
            PdfPCell sent2paypalOnCell = createCell(sent2paypalOnChunk, Rectangle.NO_BORDER, 2);
            sent2paypalOnCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(sent2paypalOnCell);

            // Transaction ID Row
            Chunk transactionIdLabelChunk = new Chunk(transactionIdCode, boldFont);
            table.addCell(createCell(transactionIdLabelChunk, Rectangle.NO_BORDER));
            table.addCell(createEmptyCell(Rectangle.NO_BORDER));
            table.addCell(createEmptyCell(Rectangle.NO_BORDER));
            table.addCell(createEmptyCell(Rectangle.NO_BORDER));
            Chunk transcationIdChunk = new Chunk(transactionId, normalFont);
            PdfPCell transcationIdCell = createCell(transcationIdChunk, Rectangle.NO_BORDER, 2);
            transcationIdCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(transcationIdCell);

            document.add(table);
        } catch (DocumentException e) {
            LOGGER.error("message=System error occured while generating invoice:" + e.getMessage());
            throw new InvoiceGenerateFailedException();
        }finally {
            if (document != null) {
                document.close();
            }

            if (docWriter != null) {
                docWriter.close();
            }
        }

        return baosPDF;
    }

    private PdfPCell createCell(Chunk chunk) {
        return new PdfPCell(new Phrase(chunk));
    }

    private PdfPCell createCell(Chunk chunk, int border) {
        PdfPCell cell = createCell(chunk);
        cell.setBorder(border);
        return cell;
    }

    private PdfPCell createCell(Chunk chunk, int border, int colspan) {
        PdfPCell cell = createCell(chunk, border);
        cell.setColspan(colspan);
        return cell;
    }

    private PdfPCell createEmptyCell(int border) {
        PdfPCell cell = new PdfPCell(new Phrase(""));
        cell.setBorder(border);
        return cell;
    }

    private PdfPCell createEmptyCell(int border, int colspan) {
        PdfPCell cell = createEmptyCell(border);
        cell.setColspan(colspan);
        return cell;
    }

    private PdfPCell createEmptyCell(int border, int colspan, float fixedHeight) {
        PdfPCell cell = createEmptyCell(border, colspan);
        cell.setFixedHeight(fixedHeight);
        return cell;
    }
    
    private TimeZone getTimeZone(Locale locale){
    	String[] timeZones  = com.ibm.icu.util.TimeZone.getAvailableIDs(locale.getCountry());
    	if(timeZones != null && timeZones.length > 1){
    		return  TimeZone.getTimeZone(timeZones[0]);
    	}else{
    		return TimeZone.getTimeZone("UTC");
    	}
    	
    }
}