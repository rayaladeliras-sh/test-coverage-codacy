package com.stubhub.domain.account.impl.export;

import java.nio.charset.Charset;


public abstract class FileCreator {
	
	protected static final Charset utf8 = Charset.forName("UTF-8");
  
    protected ExportFile fileContents;
    /**
     * 
     * @return
     * @throws Exception
     */
    public abstract byte[] createExport();

   
}
