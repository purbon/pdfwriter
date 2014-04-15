package com.purbon.pdfwriter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import com.purbon.pdfwriter.chart.PDFChart;
import com.purbon.pdfwriter.stream.DataStreamListener;
import com.purbon.pdfwriter.stream.StreamEvent;
import com.purbon.pdfwriter.table.PDFTable;
import com.purbon.pdfwriter.table.PDFTableCache;

/**
 * Abstract PDF Writer, whenever you want to create a PDF this class is one of the main points of entrance to that library.
 * 
 * @author purbon
 */
public class DefaultPDFWriter implements PDFWriter, DataStreamListener {

	protected PDFDocument document;
	protected PDFPage page;
	private OutputStream oStream;
	protected PDFTable<Float> table;
	protected int nTables;
	private PageHeader header;
	
	public DefaultPDFWriter() {
		try {
			document = new PDFDocument();
	 		this.nTables = 0;
		} catch (IOException e) {
 			e.printStackTrace();
		}

	}
	
	public void prepareReport(OutputStream oStream) throws IOException  {
		prepareReport(oStream, true);
	}
	
	public void prepareReport(OutputStream oStream, boolean setPage) throws IOException {
		this.oStream = oStream;
		if (setPage) {
			page = document.addPage();
			if (hasToIncludeHeader()) {
				page.setHeader(getPageHeader());
				page.create();
			}
		}
	}
	
	public void addTitlePage(String title, String subtitle, Map<String, String> options) throws IOException {
		page = document.addTitlePage(title, subtitle, options);
		page.create();
		
	}
	
	public void addTable() {
		addTable(false, false, null);
	}
	
	public void addTable(boolean newPage, boolean addHeader, String[] headers) {
		addTable(newPage, addHeader, headers, "");
	}
	
	public void flushTableLegend() {
		try {
			if (table != null)
				table.flushLegend();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addTable(boolean newPage, boolean addHeader, String[] headers, String legend) {
		addTable(newPage, addHeader, headers, legend, null);
	}
	
	public void addTable(boolean newPage, boolean addHeader, String[] headers, String legend, PDFChart chart) {
		float topMargin = page.getTopY();
		if (hasToIncludeHeader()) {
			  topMargin -= getPageHeader().getHeight();
		}
		if (newPage) {
			page = document.addPage();
			
			if (addHeader && hasToIncludeHeader()) {
				page.setHeader(getPageHeader());
				page.setLegend(legend);
				try {
					page.create();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else { 
			topMargin = page.getYCursor()-10;
		}
		nTables++;
		if (chart != null)
			table = new PDFTable<Float>(document, page, new PDFTableCache(3), chart, topMargin, 100, legend);
		else
			table = new PDFTable<Float>(document, page, new PDFTableCache(3), topMargin, 100, legend);

		try {
			table.addHeaderRow(headers);
		} catch (IOException e) {
 			e.printStackTrace();
		}
	}
	
	public void recordSubmited(StreamEvent event) {
		try {
			String [] data = event.getDataArray().toArray(new String[event.getDataArray().size()]);
			table.addRow(data);
		} catch (IOException e) {
 			e.printStackTrace();
		}
	}


	protected void afterCloseDocument(boolean isEmpty) {};

	public void close() throws IOException {
		close(false);
	}
	public void close(boolean isEmpty) throws IOException {
		if (document != null) {
 			document.saveAndClose(oStream);
			afterCloseDocument(isEmpty);		 
		}
	}

	public void setPageHeader(PageHeader header) {
		this.header = header;
	}

	public PageHeader getPageHeader() {
 		return header;
	}

	public boolean hasToIncludeHeader() {
		return header != null;
	}

}
