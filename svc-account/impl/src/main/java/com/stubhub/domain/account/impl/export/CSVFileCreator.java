package com.stubhub.domain.account.impl.export;

import java.io.IOException;
import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.bytecode.opencsv.CSVWriter;


public class CSVFileCreator extends FileCreator {

    static Logger log = LoggerFactory.getLogger(CSVFileCreator.class);

    public CSVFileCreator(ExportFile exportFile) {
        this.fileContents = exportFile;
    }

    @Override
    public byte[] createExport() {
        if (null != fileContents) {
            StringWriter sw = new StringWriter();
            CSVWriter writer = null;
            try {
                writer = new CSVWriter(sw);
                for (String[] entry : fileContents.getData()) {
                    writer.writeNext(entry);
                }
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                log.debug("IOException has been occured while creating the CSV file");
            }
            return sw.toString().getBytes(utf8);
        }
        return null;
    }
}
