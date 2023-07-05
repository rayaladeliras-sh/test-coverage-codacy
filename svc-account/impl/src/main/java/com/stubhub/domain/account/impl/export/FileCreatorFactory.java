package com.stubhub.domain.account.impl.export;

import com.stubhub.domain.account.intf.PaymentsService;

public class FileCreatorFactory {

	public static FileCreator getFileCreator(PaymentsService.ExportFileType contentType, ExportFile exportFile) {
		if (PaymentsService.ExportFileType.PDF == contentType) {
		  return new PDFFileCreator(exportFile);
		} else if (PaymentsService.ExportFileType.CSV == contentType) {
		    return  new CSVFileCreator(exportFile);
		} else if (PaymentsService.ExportFileType.TXT == contentType) {
		    return new TextFileCreator(exportFile);
		}
		return null;
	}

}
