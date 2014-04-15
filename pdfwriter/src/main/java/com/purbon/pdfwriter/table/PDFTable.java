package com.purbon.pdfwriter.table;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import com.purbon.pdfwriter.PDFDocument;
import com.purbon.pdfwriter.PDFPage;
import com.purbon.pdfwriter.chart.PDFChart;

public class PDFTable<V> {

	protected float rowHeight;
	protected PDFDocument document;
	protected PDFPage page;
	private float padding;
	protected String legend;
	
	protected PDFChart chart;
	protected TableCache cache;
	protected boolean hideTable;
	protected String[] headers;
	
	private Filter filter;
	private Filter skip;
	
	public PDFTable(PDFDocument document, PDFPage page, PDFTableCache cache, float startPos) {
		this(document, page, cache, null, startPos, -1, "");
	}
	
	public PDFTable(PDFDocument document, PDFPage page, PDFTableCache cache, PDFChart chart, float startPos) {
		this(document, page, cache, chart, startPos, -1, "");
	}
	
	public PDFTable(PDFDocument document, PDFPage page, PDFTableCache cache, float startPos, float padding, String legend) {
		this(document, page, cache, null, startPos, padding, legend);
	}
	
	public PDFTable(PDFDocument document, PDFPage page, PDFTableCache cache, PDFChart chart, float startPos, float padding, String legend) {
		page.setYCursor(startPos);
		this.document = document;
		this.page = page;
		this.rowHeight = 20;
		this.padding = padding;
		this.legend = legend;
		this.cache  = cache;
		this.chart = chart;
		this.hideTable = false;
		this.filter = new VoidFilter();
		this.skip   = new VoidFilter();
	}
	
	public void addHeaderRow(String[] headers) throws IOException {
		addHeaderRow(headers, 16, (getWidth()/headers.length));
	}
	
	public void addHeaderRow(String[] headers, int fontSize, float cellSize) throws IOException {
		if (headers != null) {  	
  			cache.setHeaders(headers);
  			this.headers = headers;
  		}
		if (hideTable)
			return;
		PDFont font = PDType1Font.HELVETICA_BOLD;
 		flushTableRow(headers, font, fontSize, cellSize);
   		scrolldown();
	}
	
	
	public void addTitleRow(String[] titles) throws IOException {
		if (hideTable)
			return;
		int size = 16;
		float cellSize = (getWidth()/titles.length);
		PDFont font = PDType1Font.HELVETICA_BOLD;
		PDPageContentStream cs = new PDPageContentStream(document, page, true, 	false);
		cs.setFont(font, size);
		float leftX = getLeftX()+30;
	
		for (String title : titles) {
			cs.beginText();
			cs.moveTextPositionByAmount(leftX, page.getYCursor());
			cs.drawString(title);
			cs.endText();
			if (cellSize != -1)
				leftX += cellSize;
			else
				leftX += (font.getStringWidth(title) / 1000 * size)+5;
		}
		cs.drawLine(getLeftX(), page.getYCursor() - 5, getRightX(), page.getYCursor() - 5);
		cs.close();
		
		scrolldown();
	}
	
	public void setFilter(Filter filter) {
		this.filter = filter;
	}
	
	public void setSkip(Filter filter) {
		this.skip = filter;
	}

	public void addRow(String[] elements) throws IOException {
		addRow(elements, 12);
	}
	
	public void addRow(String[] elements, int size) throws IOException {
		if (skip.apply(elements[0]).isEmpty()) {
			return;
		}
  		cache.cacheElements(elements);
		if (hideTable)
			return;
 		float cellSize = (getWidth()/elements.length);
 		elements[0] = filter.apply(elements[0]);
		Filter f = new KeyFilter("(\\d\\d:00)");
		if (filter.getClass().equals(VoidFilter.class) || !f.apply(elements[0]).isEmpty()) {
 			flushTableRow(elements, PDType1Font.HELVETICA, size, cellSize);
   			scrolldown();
 		}
	}
	
	public void flushLegend() throws IOException {

		if (!hideTable && !legend.isEmpty()) {
			PDPageContentStream cs = new PDPageContentStream(document, page, true, false);
			cs.beginText();
			cs.setFont(PDType1Font.HELVETICA_OBLIQUE, 10);
			cs.moveTextPositionByAmount(getRightX()-270, page.getYCursor());
			cs.drawString(legend);
			cs.endText();
			cs.close();
			scrolldown();
		}
		
   		createChartIfDesired();
	}
	
	public void createChartIfDesired() throws IOException {
		if (chart == null)
			return;
		
		chart.setDocument(document);
		chart.setPage(page);
		chart.setPadding(50);
		chart.create(cache);
	}
	
	protected void scrolldown() {
 		page.moveYCursorBy(-rowHeight);
  		if (page.isFull()) {
  			attachNewPage();
  		}
	}	
	
	protected void flushTableRow(String[] elements, PDFont font, int size, float cellSize) throws IOException {
		PDPageContentStream cs = new PDPageContentStream(document, page, true, 	false);
		cs.setFont(font, size);
		float leftX = getLeftX();
		for (String element : elements) {
			cs.beginText();
			cs.moveTextPositionByAmount(leftX, page.getYCursor());
			cs.drawString(element);
			cs.endText();
			if (cellSize != -1)
				leftX += cellSize;
			else
				leftX += (font.getStringWidth(element) / 1000 * size)+5;
		}
		cs.drawLine(getLeftX(), page.getYCursor() - 5, getRightX(), page.getYCursor() - 5);
		cs.close();
	}
	
	protected void attachNewPage() {
		page = document.addPage();
 	}
	
	private float getLeftX() {
		float x = page.getLeftX();
		if (padding > -1) {
			  x += padding;
		}
		return x;
	}
	
	protected float getRightX() {
		float x = page.getRightX();
		if (padding > -1) {
			  x -= padding;
		}
		return x;
	}
	
	private float getWidth() {
		return getRightX()-getLeftX();
	}

	public void hideTable() {
		this.hideTable = true;
	}

}
