package com.stubhub.domain.account.impl.export;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;


public class PDFFileCreator extends FileCreator {

    static Logger log = LoggerFactory.getLogger(PDFFileCreator.class);    
    
    public PDFFileCreator(ExportFile exportFile) {
        this.fileContents = exportFile;
    }

    @Override
    /**
     * This will Export given data into PDF format
     */
    public byte[] createExport() {
        if (null != fileContents) {
            try {
                Document document = new Document();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                PdfWriter.getInstance(document, baos);              
                document.open();                
                PdfPTable table = new PdfPTable(getHeaderSize( fileContents.getData()));
                table.setWidthPercentage(100);
                table.setHorizontalAlignment(Element.ALIGN_CENTER);
                
                Font rowFont = new Font(Font.HELVETICA, 5.8f, Font.NORMAL);               
            
                for (String[] entry : fileContents.getData()) {
                    for (String hdr : entry) {
                    PdfPCell cell = new PdfPCell(new Paragraph(hdr, rowFont));                       
                    cell.setNoWrap(false);
                    table.addCell(cell);                    
                    }
                }
                document.add(table);
                document.close();
                return baos.toByteArray();

            } catch (DocumentException e) {
                log.error("Error has been occured while creating the PDF File",e.getCause());
            }
        }
        return null;
    }
 
    private int getHeaderSize(List<String[]> header){
    	if(header!=null && header.get(0)!=null){
    		return header.get(0).length;
    	}
    	return 0;
    }
}
