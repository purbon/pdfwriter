package com.purbon.pdfwriter;

import java.io.IOException;
import java.io.OutputStream;

public interface PDFWriter {
		
	public void setPageHeader(PageHeader header);
	public PageHeader getPageHeader();
	
	public void prepareReport(OutputStream oStream) throws IOException;
	
	public void close() throws IOException;
	
	public boolean hasToIncludeHeader();
 
}
