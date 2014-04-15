package com.purbon.pdfwriter.example;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.purbon.pdfwriter.DefaultPDFWriter;
import com.purbon.pdfwriter.PageHeader;
import com.purbon.pdfwriter.stream.StreamEvent;

public class PDFRecordWriter  extends DefaultPDFWriter {

	private String filename;
	private PageHeader header; 
	

	public static final String PDF_TITLE = "pdf.title";
	public static final String PDF_TITLE_SUB = "pdf.title.sub";
	public static final String PDF_FILE_NAME = "pdf.file.name";
	
	public static final String PDF_JOB_ID = "pdf.job.id";
	public static final String PDF_JOB_NAME = "pdf.job.name";
	public static final String PDF_TIMESTAMP = "pdf.creation.date";
 	 
	
	public static void main(String[] args) throws Exception {
		Map<String, String> options = new HashMap<String, String>();
		options.put(PDF_TITLE, "PDFWriter Example");
		options.put(PDF_TITLE_SUB, "Example document");
		options.put(PDF_FILE_NAME, "pdfwriter");
		PDFRecordWriter writer = new PDFRecordWriter("example", options);
		String key = "ExampleKey";
		Random rand = new Random(System.currentTimeMillis());
		for(int i=0; i < 5; i++) {
			StringBuilder sb = new StringBuilder();
			for(int j=0; j < 4; j++) {
				if (j > 0)
					sb.append(",");
				sb.append(rand.nextDouble());
			}
			writer.write(key, sb.toString());
		}
		writer.close();
	}
	
	public PDFRecordWriter(String name, Map<String, String> options) {
		super();
  		filename = options.get(PDF_FILE_NAME)+"-"+name+".pdf";
 		
		try {
			prepareReport(new FileOutputStream(filename), false);
			
			String title = options.get(PDF_TITLE);
			String sub   = options.get(PDF_TITLE_SUB);
			options.remove(PDF_TITLE);
			options.remove(PDF_TITLE_SUB);
			options.remove(PDF_FILE_NAME);
			
			addTitlePage(title, sub , options);
 			setPageHeader(new PageHeader("Example table"));
 			addTable(true, true,  getHeadersDefinition());
 		} catch (IOException e) {
 			e.printStackTrace();
		}
	}


	
	public String[] getHeadersDefinition() {
		return new String[]{"MIN", "AVG", "StdDev", "MAX"};
	}
	
	public void write(String key, String value) throws IOException {
 		
		StreamEvent sEvent = new StreamEvent();

		String[] sValues = value.split(",");
  		for(String sValue : sValues) {
  			Double dValue = Double.valueOf(sValue.replaceAll("[\\[|\\]]", ""));
  			       dValue = Math.floor(dValue*1000000)/1000000.0;
  			sEvent.addElement(dValue.toString());
  		}
  		recordSubmited(sEvent);	
	}
	

	public void close() throws IOException {
		flushTableLegend();
 		super.close();
	}

	@Override
	public void setPageHeader(PageHeader header) {
		this.header = header;
	}

	@Override
	public PageHeader getPageHeader() {
 		return header;
	}

	@Override
	public boolean hasToIncludeHeader() {
		return header != null;
	}
}
