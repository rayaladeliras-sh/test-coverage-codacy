package com.stubhub.domain.account.impl.export;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TextFileCreator extends FileCreator {
    
    static Logger log = LoggerFactory.getLogger(TextFileCreator.class);

    public TextFileCreator(ExportFile exportFile) {
        this.fileContents = exportFile;
    }

    @Override
    public byte[] createExport() {
        if (null != fileContents) {
            StringBuffer lineText = new StringBuffer();
            for (String[] singleLine : fileContents.getData()) {
                StringBuffer record = new StringBuffer();
                for (String field : singleLine) {
                    record.append(field);
                    record.append("\t");
                }
                record.append("\r\n");
                lineText.append(record);
            }

            return lineText.toString().getBytes(utf8);
        }else{
            log.debug("File content is null");
        }
        return null;
    }
}
